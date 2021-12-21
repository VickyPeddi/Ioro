/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.util;

import com.iocl.dbconn.Conn;
import com.iocl.rdb_blocking_ro.CMS_RDB_BLOCKING_RO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Aug 16, 2017
 */
public class Util {

    private static String CMS_STK_BLOCK_MSG = "";
    private static String CMS_BLOCK_MSG = "";
    private static String STK_BLOCK_MSG = "";

    public Util() {

    }

    public static String SendSMSCall(String ROCode, String finalBlockStatus, String blockID) throws SQLException {
        String result = "";
        CMS_STK_BLOCK_MSG = Conn.getValue("SELECT message_content FROM cmsro.tbl_message WHERE message_id=1 AND message_type='BLOCKING'");
        CMS_BLOCK_MSG = Conn.getValue("SELECT message_content FROM cmsro.tbl_message WHERE message_id=2 AND message_type='BLOCKING'");
        STK_BLOCK_MSG = Conn.getValue("SELECT message_content FROM cmsro.tbl_message WHERE message_id=3 AND message_type='BLOCKING'");
        ResultSet rs = null;
        Statement stmt = null;
        String query = "";
        Connection conRDB = null;
        try {
            try {
                conRDB = Conn.getConRDB();

                stmt = conRDB.createStatement();
                query = "select  MOBILE as MOBILE_NO  from retaildb.MST_CUSTOMER_MOBILE where  mobile_ps='P' and custcode = '0000" + ROCode + "' ";
                String mobileNo = null;
                rs = stmt.executeQuery(query);
                String regex = "^[0-9]{10}$";
                Pattern p = Pattern.compile(regex);
                if (rs.next()) {
                    mobileNo = rs.getString("MOBILE_NO");
                    Matcher m = p.matcher(mobileNo);
                    if (m.matches()) {
                        String smsContent = "";
                        if (finalBlockStatus.trim().equalsIgnoreCase("B")) {
                            smsContent = CMS_STK_BLOCK_MSG;
                        } else if (finalBlockStatus.trim().equalsIgnoreCase("C")) {
                            smsContent = CMS_BLOCK_MSG.replaceAll("ROCODE", ROCode);
                        } else if (finalBlockStatus.trim().equalsIgnoreCase("S")) {
                            smsContent = STK_BLOCK_MSG;
                        }

                        CMS_RDB_BLOCKING_RO obj = new CMS_RDB_BLOCKING_RO();
                        String insertSMSDetail = "";

                        insertSMSDetail = "INSERT INTO CMSRO.TBL_MESSAGE_LOG (BLOCK_ID,MOBILE_NO,MESSAGE,UPDATE_DATETIME,STATUS,MESSAGE_TYPE,SMS_UNIQUE_ID) values ('" + blockID + "', '" + mobileNo + "', '" + smsContent.trim() + "', SYSDATE, 'I', 'BLOCKING','" + ROCode + "'||to_char(sysdate,'ddmmyyyyhh24mmss'))";
                        int insertedRows = obj.updateInsertStatus(conRDB, insertSMSDetail);
                        System.out.println("SMS Details Inserted: " + insertedRows);

                    } else {
                        System.out.println("[WRONG MOBILE FORMAT] RO CODE : " + ROCode + " , Mobile No : " + mobileNo);
                    }
                } else {
                    System.out.println("No mobile numbers updated for SMS sending...");
                }

            } catch (Exception ee) {
                System.out.println(ee);
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (conRDB != null) {
                        conRDB.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return result;
    }
}
