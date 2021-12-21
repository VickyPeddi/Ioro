/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.util;

import com.iocl.dbconn.Conn;
import com.iocl.rdb_unblocking_ro.CMS_RDB_UNBLOCKING_RO;
import ioc.SMSGateway;
import ioc.SMSGatway;
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

//    public static String SendSMSCall(String ROCode, String blockID) throws SQLException {
//        String result = "";
//
//        ResultSet rs = null;
//        String query = "";
//        Statement stm3 = null;
//        Connection conRDB = null;
//        try {
//
//            SMSGatway service = new SMSGatway();
//            SMSGateway port = service.getSMSGatewayPort();
//
//            try {
//                conRDB = Conn.getConRDB();
//
//                stm3 = conRDB.createStatement();
//                query = "select  MOBILE as MOBILE_NO  from retaildb.MST_CUSTOMER_MOBILE where  mobile_ps='P' and custcode = '0000" + ROCode + "' ";
//                String mobileNo = null;
//                rs = stm3.executeQuery(query);
//                String regex = "^[0-9]{10}$";
//                Pattern p = Pattern.compile(regex);
//                if (rs.next()) {
//                    mobileNo = rs.getString("MOBILE_NO");
////                    mobileNo = "7045682188";
//                    Matcher m = p.matcher(mobileNo);
//                    if (m.matches()) {
////                        String smsContent = "Your pending Stock Data have been updated successfully. You may now place your indents. Please note you need to place fresh indent through SMS, e-ledger or xSparsh.";
//                        String smsContent = "Stock Data have been updated successfully and Indenting for your RO has been unblocked. You can now place indent through SMS, e-ledger or xSparsh";
//                        String userID = "CMSRO";
//                        String password = "CMSRO@321";
//                        String smsType = "ENG";
//                        result = port.outGoingSMS(mobileNo, smsContent, smsType, userID, password);
//                        System.out.print("Result = " + result);
//                        System.out.print(", Mobile = " + mobileNo);
//                        System.out.println(", SMS CONTENT = " + smsContent);
////                        result = "S";
//                        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
//                        String insertSMSDetail = "";
//
//                        insertSMSDetail = "INSERT INTO CMSRO.TBL_MESSAGE_LOG (BLOCK_ID,MOBILE_NO,MESSAGE,UPDATE_DATETIME,STATUS,MESSAGE_TYPE) values ('" + blockID + "', '" + mobileNo + "', '" + smsContent.trim() + "', SYSDATE, '" + result.trim() + "', 'UNBLOCKING')";
//                        int insertedRows = obj.updateInsertStatus(conRDB, insertSMSDetail);
//                        System.out.println("SMS Details Inserted: " + insertedRows);
//                    } else {
//                        System.out.println("[WRONG MOBILE FORMAT] RO CODE : " + ROCode + " , Mobile No : " + mobileNo);
//                    }
//                } else {
//                    System.out.println("No mobile numbers updated for SMS sending...");
//                }
//
//            } catch (Exception ee) {
//                System.out.println(ee);
//            }
//
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) {
//                    rs.close();
//                    rs = null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                if (stm3 != null) {
//                    stm3.close();
//                    stm3 = null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                if (conRDB != null) {
//                    conRDB.close();
//                    conRDB = null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }

}
