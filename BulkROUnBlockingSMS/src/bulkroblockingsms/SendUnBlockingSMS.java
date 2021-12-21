/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bulkroblockingsms;

import DbConnection.DbConn;
import bean.SMSList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author t_sunita
 */
public class SendUnBlockingSMS extends Thread {

    private int thread_no;

    public int getThread_no() {
        return thread_no;
    }

    public void setThread_no(int thread_no) {
        this.thread_no = thread_no;
    }

    public SendUnBlockingSMS(int thread_no) {
        this.thread_no = thread_no;
    }
    Connection con = null;
    PreparedStatement pstmt = null, pstmt1 = null;
    ResultSet rs = null;
    String query = "";
    List<SMSList> smsList = new ArrayList<SMSList>();
    String res = "";
    String mobileNo = "";
    String ROcode = "";
    String updateSMSDetail = "";

    public void run() {
        try {

            con = DbConn.getCon();
//   query = "SELECT * FROM"
//           + "(select  MOBILE_NO, MESSAGE AS SMS_CONTENT, BLOCK_ID, SMS_UNIQUE_ID,ROWNUM r from TBL_MESSAGE_LOG where  STATUS='I' and TO_CHAR(CREATE_DATETIME,'YYYYMMDD')=TO_CHAR(SYSDATE,'YYYYMMDD') order by UPDATE_DATETIME )"
//            + "    where r <=? and r>?";     

            query = "SELECT * FROM"
                    + "(SELECT  MOBILE_NO, SMS_TEXT AS SMS_CONTENT, BLOCK_ID, ROWNUM r FROM SAP_UNBLOCK_CUSTOMERS WHERE SMS_STATUS='P' AND UNBLOCK_STATUS='S'  \n" 
+"AND TO_CHAR(UNBLOCK_CONF_DATETIME,'YYYYMMDD')>=TO_CHAR(SYSDATE-1,'YYYYMMDD') ORDER BY DATE_UPDATED"
                    + " )"
                    + "    where r <=? and r>?";

            pstmt = con.prepareStatement(query);
            System.out.println("1.. " + thread_no * 500 + "..2 " + (thread_no - 1) * 500);

            pstmt.setInt(1, thread_no * 500);
            pstmt.setInt(2, (thread_no - 1) * 500);
            rs = pstmt.executeQuery();


            String regex = "^[0-9]{10}$";
            Pattern p = Pattern.compile(regex);

            int count = 0;

          
                while (rs.next()) {
                    SMSList smsbox = new SMSList();

                    ROcode = rs.getString("BLOCK_ID").substring(0, 6);
//          
                    mobileNo = rs.getString("MOBILE_NO");
                    Matcher m = p.matcher(mobileNo);
                    if (m.matches()) {
                        smsbox.setMobileNo(mobileNo);
                        smsbox.setROcode(ROcode);
                        smsbox.setSmsContent(rs.getString("SMS_CONTENT"));
                        smsbox.setBlockId(rs.getString("BLOCK_ID"));
                       

                        smsList.add(smsbox);
                    } else {
                        System.out.println("[WRONG MOBILE FORMAT] RO CODE : " + ROcode + " , Mobile No : " + mobileNo);
                    }

                }
                
                if(smsList.size()>0){
                }else{
                     System.out.println("No mobile numbers updated for SMS sending...");
                }
             
            System.out.println("Thread No :" + thread_no + " And Mail list :" + smsList.size());

            for (int i = 0; i < smsList.size(); i++) {
                if(!smsList.get(i).getMobileNo().equals("0")){
//                res = DbConn.outGoing_SMS(smsList.get(i).getMobileNo(), smsList.get(i).getSmsContent(), "ENG", "CMSRO", "CMSRO@321");
                res = DbConn.sendMessage(smsList.get(i).getMobileNo(), smsList.get(i).getSmsContent(),15);
//                res="S";
                System.out.println("sms thread" + thread_no + "Status" + res);

                updateSMSDetail = "UPDATE SAP_UNBLOCK_CUSTOMERS set SMS_STATUS=?,SMS_DATETIME= SYSDATE,DATE_UPDATED = SYSDATE WHERE BLOCK_ID =? ";
                pstmt1 = con.prepareStatement(updateSMSDetail);


                pstmt1.setString(1, res.trim());
                pstmt1.setString(2, smsList.get(i).getBlockId());
               
                int b = pstmt1.executeUpdate();


                if (res.equalsIgnoreCase("S")) {
                    System.out.println(smsList.get(i).getROcode() + " SMS sent sucessfully");
                } else {
                    System.out.println(smsList.get(i).getROcode() + " SMS sent failed");
                }
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
           
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(pstmt1);
            DbUtils.closeQuietly(con);
        }
    }
}
