/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.util;

import com.iocl.dbconn.Conn;
import com.iocl.rdb_unblocking_ro.CMS_RDB_UNBLOCKING_RO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Aug 19, 2017
 */
public class Util {

    private static String CMS_UNBLOCK_MSG = "";

    public Util() {
        CMS_UNBLOCK_MSG = Conn.getValue("SELECT message_content FROM cmsro.tbl_message WHERE message_id = 4 AND message_type='UNBLOCKING'");
    }

    public static String SendSMSCall(String ROCode, String blockID) throws SQLException {
        String result = "";
        ResultSet rs = null;
        String query = "";
        Statement stm3 = null;
        Connection conRDB = null;
        StringBuilder SMSDetailQueries = new StringBuilder();
        String insertQuery="";
        String insertSMSDetail = "";
        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        try {
            try {
                conRDB = Conn.getConRDB();

                stm3 = conRDB.createStatement();
                query = "select  MOBILE as MOBILE_NO  from retaildb.MST_CUSTOMER_MOBILE where  mobile_ps='P' and custcode = '0000" + ROCode + "' ";
                String mobileNo = null;
                rs = stm3.executeQuery(query);
                String regex = "^[0-9]{10}$";
                Pattern p = Pattern.compile(regex);
                if (rs.next()) {
                    mobileNo = rs.getString("MOBILE_NO");
                    Matcher m = p.matcher(mobileNo);
                    if (m.matches()) {
                        String smsContent = CMS_UNBLOCK_MSG;
                        insertSMSDetail = "INSERT INTO CMSRO.TBL_MESSAGE_LOG (BLOCK_ID,MOBILE_NO,MESSAGE,UPDATE_DATETIME,STATUS,MESSAGE_TYPE,SMS_UNIQUE_ID) values ('" + blockID + "', '" + mobileNo + "', '" + smsContent.trim() + "', SYSDATE, 'I', 'UNBLOCKING','" + ROCode + "'||to_char(sysdate,'ddmmyyyyhh24mmss'))";
                       SMSDetailQueries.append("#").append(insertSMSDetail);            
                         
//                        int insertedRows = obj.updateInsertStatus(conRDB, insertSMSDetail);
                       
                    } else {
                        System.out.println("[WRONG MOBILE FORMAT] RO CODE : " + ROCode + " , Mobile No : " + mobileNo);
                    }
                    
                    
                } else {
                    System.out.println("No mobile numbers updated for SMS sending...");
                }
                 if(!insertSMSDetail.equalsIgnoreCase("")){   
                insertQuery = SMSDetailQueries.substring(1, SMSDetailQueries.length());
                    int insertedRow = obj.updateInsertStatus(conRDB, insertQuery, true);
                    System.out.println("SMS Details Inserted: " + insertedRow);
                 }
            } catch (Exception ee) {
                System.out.println(ee);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (stm3 != null) {
                    stm3.close();
                    stm3 = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (conRDB != null) {
                    conRDB.close();
                    conRDB = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
