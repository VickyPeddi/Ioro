/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.rdb_unblocking_ro;

import com.iocl.dbconn.Conn;
import ioc.SMSGateway;
import ioc.SMSGatway;
//import static com.iocl.util.Util.SendSMSCall;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import sapdownload.YV_IF_CUST_SALE_UNBLOCK;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Aug 19, 2017
 */
public class CMS_RDB_UNBLOCKING_RO {

    public static void main(String[] args) throws SQLException {
        try {
//            update7DaysStock();//Uncomment while deploying
        } catch (Exception e) {
            System.out.println("Exception occurred while updatating 7 Days Stock...");
            e.printStackTrace();
        }

        String SAPCon = "SAPQAS";
        try {
            CallSAPPI(SAPCon, "");
        } catch (Exception e) {
            System.out.println("Exception occurred while updatating 7 Days Stock...");
            e.printStackTrace();
        }

    }

    public static void CallSAPPI(String args, String conn) throws SQLException {
        CMS_RDB_UNBLOCKING_RO objUnblocker = new CMS_RDB_UNBLOCKING_RO();
        YV_IF_CUST_SALE_UNBLOCK ss = new YV_IF_CUST_SALE_UNBLOCK();

        String sapConf = "./ConnSAP.xml";

//        java.sql.Connection con = null;//        java.sql.Connection con2 = null;
        Connection con_RDB = null;
        String OS = System.getProperty("os.name").toLowerCase();
        String sql = "";
        Statement stmnt = null;
        ResultSet rs = null;

        String sql1 = "";
        Statement stmnt1 = null;
        ResultSet rs1 = null;

        int i = 0;
        String status = "";
        String rocode = "";
        String socode = "";
        String blockId = "";
        int unblockAttemptNo = 0;
//        try {//            String DBLogin = "";//            String DBPass = "";//            String JDBCurl = "";//            String Driver = "";//            String conn_RDB = "";////            Connection con_RDB = null;//            con_RDB = Conn.getConRDB();//////            FileInputStream fIs = null, fIs1 = null;////            Properties prop = new Properties();////            // fIs = new FileInputStream(connASE);////            prop.loadFromXML(fIs);////            DBLogin = prop.getProperty("login", DBLogin);////            DBPass = prop.getProperty("pass", DBPass);////            JDBCurl = prop.getProperty("JDBCurl", JDBCurl);////            Driver = prop.getProperty("Driver", Driver);////            Class.forName(Driver).newInstance();////            con = DriverManager.getConnection(JDBCurl, DBLogin, DBPass);////            System.out.println("Connected to RDB");//        } catch (Exception e) {//            e.printStackTrace();//        } finally {//        }
        int cnt = 0;
        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        try {

            con_RDB = Conn.getConRDB();
            sql = "select custcode, sales_org, block_id, UNBLOCK_ATTEMPT_NO "
                    + "from cmsro.SAP_UNBLOCK_RO_DETAILS  "
                    + "where  UNBLOCK_STATUS!='P' and sales_org in(1100,1200,1300,1400)  and rownum<4";//and block_id=20238720170819121138
//            sql = "select custcode,salesorg   from STK_BLOCK_RO_DETAIL where  block_flag='A' and sales_org in(3200,2300,7500,1300)";

            stmnt = con_RDB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmnt.executeQuery(sql);
            rs.last();
            int rows = rs.getRow();
            rs.beforeFirst();
            if (rs.next()) {

                rs.beforeFirst();
                int count = 0;
                int index = 0;
                boolean sendMsg = false;
                String rocodeArray[] = new String[rows];
                String socodeArray[] = new String[rows];
                while (rs.next()) {
                    rocode = rs.getString(1);//"287172";
                    socode = rs.getString(2);
                    blockId = rs.getString("block_id");
                    unblockAttemptNo = rs.getInt("UNBLOCK_ATTEMPT_NO");

                    rocodeArray[index] = rocode;
//                    String updateQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS SET UNBLOCK_STATUS = 'I', DATE_UPDATED = sysdate WHERE CUSTCODE = " + rocode + " AND block_id  = " + blockId + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNo;
//                    int updatedRow = obj.updateInsertStatus(con_RDB, updateQuery);
//                    System.out.println("rowsUpdated are : " + updatedRow);
//                    if (updatedRow > 0) {
                    String message = ss.unblockCheck(rocodeArray, socodeArray, sapConf);
//                        String message = "Order Unblocked!!Delivery Unblocked!!Billing Unblockeds!!";
//                        String statusChangeQuery = "";
//                        System.out.println("RO :: " + message);
//                        if (message == null) {
//                        } else if (message.trim().equalsIgnoreCase("Order Unblocked!!Delivery Unblocked!!Billing Unblocked!!") || message.trim().equalsIgnoreCase("Custmer is already Unblocked in sales area!!")) {//Already Blocked
//                            System.out.println("rocode : " + rocode + "->" + message);
//                            status = "S";
//                            sendMsg = true;
//                            statusChangeQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS set SAP_UNBLOCK_MSG = '" + message.trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate, UNBLOCK_CONF_DATETIME = sysdate  where block_id  = " + blockId + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNo;
//
//                        } else {//else if (message.trim().contains("is currently blocked by user")) {//Currently Blocked by user
//                            System.out.println("rocode : " + rocode + "->" + message);
//                            status = "R";
//
//                            statusChangeQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS set SAP_UNBLOCK_MSG = '" + message.trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate, UNBLOCK_CONF_DATETIME = sysdate  where block_id  = " + blockId + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNo;
//                            String mergeUnblockingDetails = "";
////                            mergeUnblockingDetails = "INSERT INTO CMSRO.SAP_UNBLOCK_RO_DETAILS   \n"                                    + "SELECT CUSTCODE,  RO_TYPE,  BLOCK_ID,  BLOCK_DATE,  BLOCK_ATTEMPT_NO,  SYSDATE,  'P',  UNBLOCK_ATTEMPT_NO,  SAP_UNBLOCK_MSG,  '',  SYSDATE,  SALES_ORG from (\n"                                    + "select row_number() over (PARTITION BY block_id ORDER BY UNBLOCK_ATTEMPT_NO DESC ) AS row_id,CUSTCODE,  RO_TYPE,  BLOCK_ID,  BLOCK_DATE,  BLOCK_ATTEMPT_NO,  SYSDATE,  'P',  UNBLOCK_ATTEMPT_NO+1 as UNBLOCK_ATTEMPT_NO,  SAP_UNBLOCK_MSG,  '',  SYSDATE,  SALES_ORG from CMSRO.SAP_UNBLOCK_RO_DETAILS where BLOCK_ID= " + blockId + " ) where row_id=1";
//                            mergeUnblockingDetails = "INSERT INTO CMSRO.SAP_UNBLOCK_RO_DETAILS \n"
//                                    + "SELECT CUSTCODE,  RO_TYPE,  BLOCK_ID,  BLOCK_DATE,  BLOCK_ATTEMPT_NO,  SYSDATE,  'P',  UNBLOCK_ATTEMPT_NO+1,  null,  '',  SYSDATE,  SALES_ORG from CMSRO.SAP_UNBLOCK_RO_DETAILS where block_id= " + blockId + " and UNBLOCK_ATTEMPT_NO=" + unblockAttemptNo;
//                            try {
//                                int rowsMerged = objUnblocker.updateInsertStatus(con_RDB, mergeUnblockingDetails);
//                                System.out.println(rowsMerged + " Rows merged");
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        else {
//                            status = "E";
//                            System.out.println("rocode : " + rocode + "->" + message);
//
//                            statusChangeQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS set SAP_UNBLOCK_MSG = '" + message.trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate  where CUSTCODE  = '" + rocode + "' and BLOCK_STATUS='I'";
//
//                        }

//                        try {
//                            if (!statusChangeQuery.equalsIgnoreCase("")) {
//                                stmnt1 = con_RDB.createStatement();
//                                int updatedRows = stmnt1.executeUpdate(statusChangeQuery);
//                                if (updatedRows > 0) {
//                                    if (sendMsg) {
//                                        SendSMSCall(rocode, blockId);//Uncomment while deploying
//                                    }
//                                    count++;
//                                    System.out.println(count + " Rows(s) Updated");
//                                }
//                            }
//                        } catch (Exception ee) {
//                            ee.printStackTrace();
//                        } finally {
//                            try {
//                                if (stmnt1 != null) {
//                                    stmnt1.close();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//                    }
                }
            } else {
            }

        } catch (Exception ee) {
            ee.printStackTrace();
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
                if (stmnt != null) {
                    stmnt.close();
                    stmnt = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (con_RDB != null) {
                    con_RDB.close();
                    con_RDB = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public int updateInsertStatus(Connection con_RDB, String updateQuery) {

        Statement stmt = null;
        int rowsUpdated = 0;
        try {
            if (!updateQuery.equalsIgnoreCase("")) {
                stmt = con_RDB.createStatement();
                rowsUpdated = stmt.executeUpdate(updateQuery);
//                System.out.println("rowsUpdated are : " + rowsUpdated);
            } else {
                System.out.println("No Update Query found...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rowsUpdated;
    }

    public static void update7DaysStock() {
        String query = "";
        String insertQuery = "";
        String updateQuery = "";
        Connection con_RDB = null;
        int rows = 0;
        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        try {
            /*-- last 7 days stock check and updation of STK_BLOCK_FLAG
             */
            con_RDB = Conn.getConRDB();
            try {
                query = "update CMSRO.SAP_BLOCK_RO_DETAILS set stk_block_flag='N',DATE_UPDATED=sysdate where  sap_block_curr='B' and stk_block_flag='Y' and custcode in ( \n"
                        + "  SELECT ro_code\n"
                        + "  FROM\n"
                        + "    (SELECT ro_code ,\n"
                        + "      COUNT(*) AS stock_dates\n"
                        + "    FROM\n"
                        + "      (SELECT DISTINCT (tran_date) ,\n"
                        + "        ro_code\n"
                        + "      FROM\n"
                        + "        (SELECT ro_code,\n"
                        + "          tran_date\n"
                        + "        FROM CMS_MANUAL_RO_STK\n"
                        + "        WHERE tran_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd')\n"
                        + "        UNION\n"
                        + "        SELECT ro_code, stk_date\n"
                        + "        FROM cmsro.cms_blocking_stk_details\n"
                        + "        WHERE stk_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd')\n"
                        + "        )\n"
                        + "      WHERE tran_date BETWEEN TO_CHAR(to_date(sysdate-7,'dd/mm/yy'),'yyyymmdd') AND TO_CHAR(to_date(sysdate-1,'dd/mm/yy'),'yyyymmdd')\n"
                        + "      ORDER BY TRAN_DATE\n"
                        + "      )\n"
                        + "    GROUP BY ro_code\n"
                        + "    )\n"
                        + "  WHERE stock_dates=7 \n"
                        + "  )";
                rows = obj.updateInsertStatus(con_RDB, query);
                System.out.println("----------------------------------------/n Last 7 days stock check and updation of STK_BLOCK_FLAG");
                System.out.println(rows + " Updated Row(s).");
                System.out.println("----------------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception occurred while executing updateQuery");
            }
            /*--insert record to unblock table after checking CMS and RDB block
             */
            try {
                insertQuery = "insert into CMSRO.SAP_UNBLOCK_RO_DETAILS\n"
                        + "select custcode,ro_type,block_id,block_date,block_attempt_no,sysdate,'P',1,'','',sysdate,SALES_ORG from CMSRO.SAP_BLOCK_RO_DETAILS where sap_block_curr='B' and cms_block_flag='N' and stk_block_flag='N' and sap_block_status='S' and block_id not in ((select block_id from CMSRO.SAP_UNBLOCK_RO_DETAILS))\n"
                        + " ";
                rows = obj.updateInsertStatus(con_RDB, insertQuery);
                System.out.println("----------------------------------------/n insert record to unblock table after checking CMS and RDB block");
                System.out.println(rows + " Updated Row(s).");
                System.out.println("----------------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception occurred while executing insertQuery");
            }
            try {
                updateQuery = "  update CMSRO.SAP_BLOCK_RO_DETAILS set sap_block_curr='U' where sap_block_curr='B' and block_id in (select block_id from CMSRO.SAP_UNBLOCK_RO_DETAILS)";
                rows = obj.updateInsertStatus(con_RDB, updateQuery);
                System.out.println("----------------------------------------/n Updating SAP_BLOCK_RO_DETAILS setting sap_block_curr='U' ");
                System.out.println(rows + " Updated Row(s).");
                System.out.println("----------------------------------------");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Exception occurred while executing updateQuery");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception occurred in update7DaysStock()");
        } finally {

            try {
                if (con_RDB != null) {
                    con_RDB.close();
                    con_RDB = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String SendSMSCall(String ROCode, String blockID) throws SQLException {
        String result = "";

        ResultSet rs = null;
        String query = "";
        Statement stm3 = null;
        Connection conRDB = null;
        try {

            SMSGatway service = new SMSGatway();
            SMSGateway port = service.getSMSGatewayPort();

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
//                    mobileNo = "7045682188";
                    Matcher m = p.matcher(mobileNo);
                    if (m.matches()) {
//                        String smsContent = "Your pending Stock Data have been updated successfully. You may now place your indents. Please note you need to place fresh indent through SMS, e-ledger or xSparsh.";
                        String smsContent = "Stock Data have been updated successfully and Indenting for your RO has been unblocked. You can now place indent through SMS, e-ledger or xSparsh";
                        String userID = "CMSRO";
                        String password = "CMSRO@321";
                        String smsType = "ENG";
                        result = port.outGoingSMS(mobileNo, smsContent, smsType, userID, password);
                        System.out.print("Result = " + result);
                        System.out.print(", Mobile = " + mobileNo);
                        System.out.println(", SMS CONTENT = " + smsContent);
//                        result = "S";
                        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
                        String insertSMSDetail = "";

                        insertSMSDetail = "INSERT INTO CMSRO.TBL_MESSAGE_LOG (BLOCK_ID,MOBILE_NO,MESSAGE,UPDATE_DATETIME,STATUS,MESSAGE_TYPE) values ('" + blockID + "', '" + mobileNo + "', '" + smsContent.trim() + "', SYSDATE, '" + result.trim() + "', 'UNBLOCKING')";
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
