/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.rdb_unblocking_ro;

import com.iocl.dbconn.Conn;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sapdownload.YV_IF_CUST_SALE_UNBLOCK;

/**
 *
 * @author t_sunita Create Date : Nov 14, 2018
 */
public class CMS_RDB_UNBLOCKING_RO {

    public static void main(String[] args) throws SQLException, IOException {
        try {
            customerUnblocking();//Uncomment while deploying
        } catch (Exception e) {
            System.out.println("Exception occurred while updating 7 Days Stock...");
            e.printStackTrace();
        }

        
        try {
            CallSAPPI();
        } catch (Exception e) {
            System.out.println("Exception occurred while updatating 7 Days Stock...");
            e.printStackTrace();
        }

    }

    public static void CallSAPPI() throws SQLException {
        CMS_RDB_UNBLOCKING_RO objUnblocker = new CMS_RDB_UNBLOCKING_RO();
        YV_IF_CUST_SALE_UNBLOCK ss = new YV_IF_CUST_SALE_UNBLOCK();


        Connection con_RDB = null;
        String sql = "";
        Statement stmnt = null;
        ResultSet rs = null;
        Statement stmnt1 = null, stmnt2 = null;
        String status = "";
        String rocode = "";
        String socode = "";
        String blockId = "";
        String division = "";
        int unblockAttemptNo = 0;
        StringBuilder updateQueries = new StringBuilder();
        String updateQuery = "";

        int cnt = 0;
        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        try {

            con_RDB = Conn.getCon();
            sql = "select custcode, sales_org, block_id, UNBLOCK_ATTEMPT_NO,DIVISION "
                    + "from SAP_UNBLOCK_CUSTOMERS  "
                    + "where  UNBLOCK_STATUS='P' and rownum < 1001";//and block_id=20238720170819121138

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
                String blockIdArray[] = new String[rows];
                int unblockAttemptNoArray[] = new int[rows];
                String divisionArray[] = new String[rows];
                while (rs.next()) {
                    rocode = rs.getString(1);//"287172";
                    socode = rs.getString(2);
                    blockId = rs.getString("block_id");
                    unblockAttemptNo = rs.getInt("UNBLOCK_ATTEMPT_NO");
                    division = rs.getString("DIVISION");

                    rocodeArray[index] = rocode;
                    socodeArray[index] = socode;
                    blockIdArray[index] = blockId;
                    unblockAttemptNoArray[index] = unblockAttemptNo;
                    divisionArray[index] = division;

                    updateQuery = "UPDATE SAP_UNBLOCK_CUSTOMERS SET UNBLOCK_STATUS = 'I', DATE_UPDATED = sysdate WHERE CUSTCODE = " + rocode + " AND block_id  = " + blockId + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNo;
                    updateQueries.append("#").append(updateQuery);
                    index++;
                }
                updateQuery = updateQueries.substring(1, updateQueries.length());
                int updatedRow = obj.updateInsertStatus(con_RDB, updateQuery, true);
//                    if (updatedRow > 0) {
                String[] message = ss.unblockCheck(rocodeArray, socodeArray,divisionArray);
                System.out.println("message size " + message.length);

                String statusChangeQuery = "";
                String query2 = "";
                System.out.println("RO :: " + message);
//                Util utilObj = new Util();
                if (updatedRow == message.length) {
                    for (int j = 0; j < message.length; j++) {

                        if (message[j] == null) {
                        } else if (message[j].trim().equalsIgnoreCase("Order Unblocked!!Delivery Unblocked!!Billing Unblocked!!") || message[j].trim().equalsIgnoreCase("Custmer is already Unblocked in sales area!!")) {//Already Blocked
                            System.out.println("rocode : " + rocodeArray[j] + "->" + message[j]);
                            status = "S";
                            sendMsg = true;
                            statusChangeQuery = "UPDATE SAP_UNBLOCK_CUSTOMERS set SAP_UNBLOCK_MSG = '" + message[j].trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate, UNBLOCK_CONF_DATETIME = sysdate  where block_id  = " + blockIdArray[j] + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNoArray[j];
                            query2 = "UPDATE SAP_BLOCK_CUSTOMER_DETAILS SET ROW_UNBLOCK_DATETIME=sysdate ,UPDATE_DATETIME = sysdate where BLOCK_ID=" + blockIdArray[j];
                        } else {            //Currently Blocked by user
                            System.out.println("rocode : " + rocodeArray[j] + "->" + message[j]);
                            status = "R";

                            statusChangeQuery = "UPDATE SAP_UNBLOCK_CUSTOMERS set SAP_UNBLOCK_MSG = '" + message[j].trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate, UNBLOCK_CONF_DATETIME = sysdate  where block_id  = " + blockIdArray[j] + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNoArray[j];
                            String mergeUnblockingDetails = "";
                            mergeUnblockingDetails = "INSERT INTO SAP_UNBLOCK_CUSTOMERS \n"
                                    + "SELECT CUSTCODE,  BLOCK_ID,  BLOCK_DATE,  BLOCK_ATTEMPT_NO,  SYSDATE,  'P',  UNBLOCK_ATTEMPT_NO+1,  null,  '',  SYSDATE,  SALES_ORG ,MOBILE_NO,SMS_STATUS,SMS_DATETIME,\n"
                                    + "SMS_TEXT,DIVISION from SAP_UNBLOCK_CUSTOMERS where block_id= " + blockIdArray[j] + " and UNBLOCK_ATTEMPT_NO=" + unblockAttemptNoArray[j];
                            try {
                                int rowsMerged = objUnblocker.updateInsertStatus(con_RDB, mergeUnblockingDetails);
                                System.out.println(rowsMerged + " Rows merged");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        try {
                            if (!statusChangeQuery.equalsIgnoreCase("")) {
                                stmnt1 = con_RDB.createStatement();
                                int updatedRows = stmnt1.executeUpdate(statusChangeQuery);
                                stmnt2 = con_RDB.createStatement();
                                int updatedRows2 = stmnt2.executeUpdate(query2);
                                if (updatedRows > 0) {
                                    if (sendMsg) {

//                                        utilObj.SendSMSCall(rocodeArray[j], blockIdArray[j]);//Uncomment while deploying
                                    }
                                    count++;
                                    System.out.println(count + " Rows(s) Updated");
                                }
                            }
                        } catch (Exception ee) {
                            ee.printStackTrace();
                        } finally {
                            try {
                                if (stmnt1 != null) {
                                    stmnt1.close();
                                }
                                if (stmnt2 != null) {
                                    stmnt2.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
//                    }
//                }
                    }
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

    public static void customerUnblocking() {
//        String query = ""; String insertQuery = ""; String updateQuery = "";//        int rows = 0;//        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        Connection con_RDB = null;
        CallableStatement callableStatement = null;
        String update7DaysStk = "";
        try {
            /*-- last 7 days stock check and updation of STK_BLOCK_FLAG
             */
            con_RDB = Conn.getCon();
            update7DaysStk = "{call CUSTOMER_UNBLOCKING}";//Procedure in RetailDBProd DB

            callableStatement = con_RDB.prepareCall(update7DaysStk);
            int isUpdated = callableStatement.executeUpdate();
            System.out.println("isUpdated: " + isUpdated);

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

            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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

            System.out.println("CMS_RDB_UNBLOCKING>>" + ex.getMessage());
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

}
