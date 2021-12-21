/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.rdb_blocking_ro;

import com.iocl.dbconn.Conn;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import sapConnect.sapConnect;
import sapdownload.YV_IF_CUST_SALE_BLOCK;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Aug 19, 2017
 */
public class CMS_RDB_BLOCKING_RO {

    public static void main(String[] args) throws SQLException, IOException {
        String sapConf = "./ConnSAP.xml";
        String SAPCon = "SAPQAS";
        String username = "";
        String passwd = "";
        sapConnect sC = new sapConnect();
        FileInputStream fIs = null;
        try {
            try {

                Properties prop = new Properties();
                fIs = new FileInputStream(sapConf);
                prop.loadFromXML(fIs);
                username = prop.getProperty("SAPUSERID", username);
                passwd = prop.getProperty("SAPPASSWORD", passwd);
                System.out.println("Program ends at : " + new java.util.Date());

                //sC.Connect("test05", "iocltest");//UAT
                sC.Connect(username, passwd);//Prod
            } catch (Exception e) {
                System.out.println(e);
            }
            try {
                CallSAPPI(sC, SAPCon, "");
            } catch (Exception e) {
                System.out.println("Exception occurred while Syncing...");
                e.printStackTrace();
            }
            sC.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fIs != null) {
                fIs.close();
            }
        }

    }

    public static void CallSAPPI(sapConnect sC, String args, String conn) throws SQLException {

        ResultSet res = null;
        Statement stmt = null;
        Connection con_RDB = null;
        CallableStatement callablestmt = null;
        String salesOrg = "";
        String salesOff = "";
        String sales_org_query = "select distinct salesoff, salesorg from iocl_md_aod_sales order by salesoff";//and block_attempt_no=3;
        CMS_RDB_BLOCKING_RO obj = new CMS_RDB_BLOCKING_RO();
        try {
            con_RDB = Conn.getConRDB();
            stmt = con_RDB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            res = stmt.executeQuery(sales_org_query);
            int a = 0;
            while (res.next()) {//Uncomment for loop
                salesOff = res.getString("salesoff");
                salesOrg = res.getString("salesorg");
                a = blockROSalesOff(sC, salesOrg, salesOff);
                System.out.println("Blocked RO:- " + a);

                try {
                    if (a != 0) {
                        String updateSmsBlocking = "{call UPDATE_BLOCKING_SMS}";
                        callablestmt = con_RDB.prepareCall(updateSmsBlocking);
                        int recordsUpdated = callablestmt.executeUpdate();
                        System.out.println("recordsUpdated............" + recordsUpdated);
                    }
                } catch (Exception ex) {
                    System.out.println("Exception in updateSmsBlocking:" + ex);
                } finally {
                    if (callablestmt != null) {
                        callablestmt.close();
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Exception in Main while loop:" + e);
        } finally {
            try {
                if (res != null) {
                    res.close();
                }
            } catch (Exception e) {

            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {

            }

            try {
                if (con_RDB != null) {
                    con_RDB.close();
                }
            } catch (Exception e) {
                //   e.printStackTrace();
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
                System.out.println("No Update Query found..." + updateQuery);
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

    public int updateInsertStatus(Connection con_RDB, String query, boolean addToBatch) {
        boolean res = false;
        Connection con = null;
        Statement stmt = null;
        int counter = 0;
        try {
            con = con_RDB;
            if (con == null) {
                return 0;
            }
            con.setAutoCommit(false);
            stmt = con.createStatement();

            if (addToBatch) {
                String[] queries = query.split("#");

                for (int i = 0; i < queries.length; i++) {
                    stmt.addBatch(queries[i]);
                }

                int[] count = stmt.executeBatch();
                if (count.length > 0) {
                    res = true;
                    counter = count.length;
                }
            } else {
                int count = stmt.executeUpdate(query);

                if (count >= 0) {
                    res = true;
                }

            }
            con.commit();
        } catch (BatchUpdateException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
            }

            System.out.println("CMS_RDB_BLOCKING>>" + ex.getMessage());
        } catch (SQLException ex) {

        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }

            } catch (Exception ex) {
            }
        }
        return counter;
    }

    public static int blockROSalesOff(sapConnect sC, String salesOrg, String salesOff) {
        long startTime = 0L;
        long endTime = 0L;
        long timeTaken = 0L;
        ResultSet rs = null;
        Statement stm = null, stm2 = null;
        Connection con_RDB = null;

        String message[] = null;
        String arr[][] = null;
        String RO_Code = "", blockID = "";
        String query = "";
        String query1 = "";
        int rowsMerged = 0;
        int updatedRows = 0;
        String mergeblockingDetails = "";
        String updateQuery = "";
        YV_IF_CUST_SALE_BLOCK ss = new YV_IF_CUST_SALE_BLOCK();

        CMS_RDB_BLOCKING_RO obj = new CMS_RDB_BLOCKING_RO();
        try {

            query = "select custcode, sales_org, block_attempt_no, FINAL_BLOCK_STATUS, BLOCK_ID from cmsro.SAP_BLOCK_RO_DETAILS \n"
                    + "where  SAP_BLOCK_STATUS='P'  and sap_block_curr is null  and SALESOFF=" + salesOff;//and block_attempt_no=3;
            con_RDB = Conn.getConRDB();
            stm = con_RDB.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stm.executeQuery(query);

            int blockAttemptNo = 0;

            rs.last();
            int rows = rs.getRow();

//            int count = 0;
            if (rows == 0) {
                return 0;
            }
            int index = 0;
            arr = new String[3][rows];
            message = new String[rows];
            rs.beforeFirst();
            while (rs.next()) {
                RO_Code = rs.getString("custcode");//"284763";//"178895";
                blockAttemptNo = rs.getInt("block_attempt_no");
                blockID = rs.getString("BLOCK_ID");
                arr[0][index] = RO_Code;
                arr[1][index] = blockID;
                arr[2][index] = "" + blockAttemptNo;
                index++;
            }
            updateQuery = "UPDATE cmsro.SAP_BLOCK_RO_DETAILS SET SAP_BLOCK_STATUS = 'I', DATE_UPDATED = sysdate WHERE salesoff = " + salesOff + " and SAP_BLOCK_STATUS = 'P' AND SAP_BLOCK_CURR IS NULL";
            int updatedRow = obj.updateInsertStatus(con_RDB, updateQuery);

//                if (updatedRow > 0) {
            startTime = System.currentTimeMillis();

//                    message = ss.blockCheck(rocodeArray, socodeArray, sapConf);
            message = ss.blockCheck(sC, arr[0], salesOrg);
//                String[] message = {"Order blocked!!Delivery blocked!!Bill blocked!!", "Order blocked!!Delivery blocked!!Bill blocked!!"};//ss.blockCheck(RO_Code, SO_Code, sapConf);

            endTime = System.currentTimeMillis();
            timeTaken = endTime - startTime;
            System.out.println("Time taken in Blocking ROCODE  is:: " + timeTaken + " ms");
            if (updatedRow == message.length) {
                for (int j = 0; j < message.length; j++) {
                    try {
                        if (message[j] == null) {
                        } else if (message[j].trim().equalsIgnoreCase("Order blocked!!Delivery blocked!!Bill blocked!!") || message[j].trim().equalsIgnoreCase("Custmer is already blocked in sales area!!")) {
//                        } else if (message[j] == 1 || message[j] == 2 || message[j] == 3) {
//                                    sendMsg = true;
                            System.out.println(arr[0][j] + " ,Msg: " + message[j]);
                            query1 = "update cmsro.SAP_BLOCK_RO_DETAILS set SAP_BLOCK_MSG = '" + message[j] + "' ,  DATE_UPDATED = sysdate,"
                                    + " SAP_BLOCK_STATUS = 'S', BLOCK_CONF_DATETIME = sysdate, SAP_BLOCK_CURR='B' where block_id = " + arr[1][j] + " and SAP_BLOCK_STATUS = 'I' and block_attempt_no=" + arr[2][j];

                        } else {
                            System.out.println(arr[0][j] + " ,Msg: " + message[j]);
                            query1 = "update cmsro.SAP_BLOCK_RO_DETAILS set SAP_BLOCK_MSG = '" + message[j] + "' , DATE_UPDATED = sysdate,"
                                    + " SAP_BLOCK_STATUS = 'R' ,SMS_STATUS='R' where block_id = " + arr[1][j] + " and SAP_BLOCK_STATUS = 'I' and block_attempt_no=" + arr[2][j];

                            mergeblockingDetails = "INSERT INTO CMSRO.SAP_BLOCK_RO_DETAILS  \n"
                                    + " SELECT  CUSTCODE, RO_TYPE, CREATE_DATETIME, CMS_BLOCK_FLAG, STK_BLOCK_FLAG, FINAL_BLOCK_STATUS, 'P', BLOCK_ID, "
                                    + " BLOCK_ATTEMPT_NO+1 AS BLOCK_ATTEMPT_NO, BLOCK_DATE, SAP_BLOCK_MSG, BLOCK_CONF_DATETIME, DATE_UPDATED, SALES_ORG, SAP_BLOCK_CURR, MOBILE_NO, SMS_STATUS, SMS_DATETIME, SALESOFF  "
                                    + " FROM CMSRO.SAP_BLOCK_RO_DETAILS WHERE block_id = " + arr[1][j] + "  AND BLOCK_ATTEMPT_NO = " + arr[2][j];
                            try {
                                rowsMerged = obj.updateInsertStatus(con_RDB, mergeblockingDetails);
//                                    System.out.println(rowsMerged + " Rows merged");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        stm2 = con_RDB.createStatement();
                        updatedRows = stm2.executeUpdate(query1);

                    } catch (Exception EE) {
                        System.out.println(EE);
//            EE.printStackTrace();
                    } finally {
                        try {
                            if (stm2 != null) {
                                stm2.close();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mergeblockingDetails = null;
                    rowsMerged = 0;
                    updatedRows = 0;
                }//End of for loop
            }//End of if length chk with message array from SAP
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stm != null) {
                    stm.close();
                }
            } catch (Exception e) {
            }
            try {
                if (con_RDB != null) {
                    con_RDB.close();
                }
            } catch (Exception e) {
                //   e.printStackTrace();
            }
        }
        System.out.println("---Process Completed for SALES_OFFICE : " + salesOff + " ----");
        arr = null;
        message = null;
        ss = null;
        obj = null;
        return 1;
    }
}
