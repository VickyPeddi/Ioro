/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DbConnection;

import static SMS.SmsApiCall.callSmsApi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbutils.DbUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author t_sunita
 */
public class DbConn {

    public static Connection getCon() {

        Connection con = null;
        try {

            App b = new App();
            String xmlString = b.getDetails("CMSRO");
            String s = null;
            con = DriverManager.getConnection("jdbc:oracle:thin:@" + xmlString.split(",")[0], xmlString.split(",")[1], xmlString.split(",")[2]);

            System.out.println("CMSRO Database Connected");
        } catch (SQLException ex) {
            Logger.getLogger(DbConn.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Error in creating database connection ---------------" + ex.getMessage());
        }
        return con;

    }

    public static Connection getEledgerCon() {

        Connection con = null;
        try {

            App b = new App();
            String xmlString = b.getDetails("ELEDGER");
            String s = null;
            con = DriverManager.getConnection("jdbc:oracle:thin:@" + xmlString.split(",")[0], xmlString.split(",")[1], xmlString.split(",")[2]);

            System.out.println("CMSRO Database Connected");
        } catch (SQLException ex) {
            Logger.getLogger(DbConn.class.getName()).log(Level.SEVERE, null, ex);
            System.out.print("Error in creating database connection ---------------" + ex.getMessage());
        }
        return con;

    }

    public static String getEledgerValue(String query) {
        Connection con = null;
        ResultSet rs = null;
        Statement stmt = null;
        String result = "";
        try {
            con = DbConn.getEledgerCon();
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(query);
            if (rs.next()) {
                result = rs.getString(1);
            } else {
                result = "";
            }

            rs.close();
            stmt.close();
            con.close();

        } catch (Exception ex) {
            //System.out.println("RDB getValue Exception get Value>>" + query + " " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (con != null) {
                    try {
                        con.close();
                        con = null;

                    } catch (SQLException ex) {
                        Logger.getLogger(DbConn.class
                                .getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return result;
    }
    
    public static int insertupdatedatEledger(String query, String[] parameteres) {

        Connection con = null;
        int res = 0;
        PreparedStatement pstm = null;
        try {
            con = DbConn.getEledgerCon();
            pstm = con.prepareStatement(query);
            for (int i = 1; i <= parameteres.length; i++) {
                pstm.setString(i, parameteres[i - 1]);
            }
            res = pstm.executeUpdate();
        } catch (Exception ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException logOrIgnore) {
                    System.out.println("ioconline>>" + logOrIgnore.getMessage());
                }
            }

            System.out.println("ioconline>>" + ex.getMessage());
        } finally {
            DbUtils.closeQuietly(pstm);
            DbUtils.closeQuietly(con);
        }
        return res;
    }

//   private static String outGoingSMS(java.lang.String mobileNo, java.lang.String smsContent, java.lang.String smsType, java.lang.String userID, java.lang.String password) {
//        ioc.SMSGatway service = new ioc.SMSGatway();
//        ioc.SMSGateway port = service.getSMSGatewayPort();
//        return port.outGoingSMS(mobileNo, smsContent, smsType, userID, password);
//    }
//    public static String outGoing_SMS(java.lang.String mobileNo, java.lang.String smsContent, java.lang.String smsType, java.lang.String userID, java.lang.String password) {
//        outGoingSMS(mobileNo,smsContent,smsType,userID,password);
//        return "S";
//    }
    public static String sendMessage(String Mobile, String smsContent, int appUserId) {
        String msg = "";
//        Mobile = "9754026270"; //for testing
        String response = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con = null;
        String APP_SER_STATUS = "";
        int DYNAMIC_TOKEN = 0;
        String payload = "";
        int smsSeqId = 0;
        JSONArray payloadArray = new JSONArray();
        try {
            con = DbConn.getEledgerCon();
            String queryGetSMSApiData = "SELECT DYNAMIC_TOKEN,APP_SER_STATUS,AUTH_TOKEN,API_URL,TEMPLATE_ID,APP_ID,APP_SUB_CAT_ID,GST_FLAG FROM MST_SMS_USER where APP_NAME = ? and APP_USER_ID = ?";
            ps = con.prepareStatement(queryGetSMSApiData);
            ps.setString(1, "IOCONLINE");
            ps.setInt(2, appUserId);
            rs = ps.executeQuery();
            while (rs.next()) {
                APP_SER_STATUS = rs.getString("APP_SER_STATUS");
                DYNAMIC_TOKEN = Integer.parseInt(rs.getString("DYNAMIC_TOKEN"));
                if (APP_SER_STATUS.equalsIgnoreCase("ON")) {
                    smsSeqId = Integer.parseInt(DbConn.getEledgerValue("select SMS_SEQ_ID.nextval from dual"));
                    JSONObject smsJsonObj = getSmsJsonFormat(Mobile, smsContent, rs.getString("TEMPLATE_ID"), smsSeqId, rs.getString("GST_FLAG"));
                    payloadArray.add(smsJsonObj);
                    payload = payloadArray.toString();
                    System.out.println("payload--" + payload);
                    response = callSmsApi(payload, rs.getString("AUTH_TOKEN"), rs.getString("API_URL"), rs.getString("APP_ID"), rs.getString("APP_SUB_CAT_ID"), DYNAMIC_TOKEN);
                    JSONParser parser = new JSONParser();
                    JSONObject jsonResponse = (JSONObject) parser.parse(response);
                    String respCode = (String) jsonResponse.get("responseCode");
                    String responseMessage = (String) jsonResponse.get("responseMessage");
                    //            //insert data in log
                    String queryLog = "insert into TBL_SMS_LOG (APP_USER_ID,SMS_SEQ_ID,MOBILE_NO,SMS_CONTENT,RESPONSE,XID,CREATED_ON) values (?,?,?,?,?,?,systimestamp)";
                    String[] param = {String.valueOf(appUserId), String.valueOf(smsSeqId), Mobile, smsContent, respCode, responseMessage};
                    insertupdatedatEledger(queryLog, param);
                    if (respCode.equalsIgnoreCase("SUCCESS")) {
                        msg = "S";
                    } else {
                        msg = "F";
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(ps);
            DbUtils.closeQuietly(con);
        }
        return msg;
    }

    public static JSONObject getSmsJsonFormat(String mobileNo, String smsContent, String templateId, int smsSeqId, String GST_FLAG) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile_no", mobileNo);
            jsonObject.put("sms_content", smsContent);
            jsonObject.put("gst_flag", GST_FLAG);
            jsonObject.put("template_id", templateId);
            jsonObject.put("ref_in_msg_unique_id", smsSeqId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }
}
