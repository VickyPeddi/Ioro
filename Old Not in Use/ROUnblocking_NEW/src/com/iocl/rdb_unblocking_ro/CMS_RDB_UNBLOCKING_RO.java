/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.rdb_unblocking_ro;

import com.iocl.dbconn.Conn;
import com.iocl.util.Util;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import sapdownload.YV_IF_CUST_SALE_UNBLOCK;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Oct 12, 2017
 */
public class CMS_RDB_UNBLOCKING_RO {

    public static void main(String[] args) throws SQLException {
        try {
            update7DaysStock();//Uncomment while deploying
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
        StringBuilder updateQueries = new StringBuilder();
        String updateQuery = "";

        int cnt = 0;
        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        try {

            con_RDB = Conn.getConRDB();
            sql = "select custcode, sales_org, block_id, UNBLOCK_ATTEMPT_NO "
                    + "from cmsro.SAP_UNBLOCK_RO_DETAILS  "
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
                while (rs.next()) {
                    rocode = rs.getString(1);//"287172";
                    socode = rs.getString(2);
                    blockId = rs.getString("block_id");
                    unblockAttemptNo = rs.getInt("UNBLOCK_ATTEMPT_NO");

                    rocodeArray[index] = rocode;
                    socodeArray[index] = socode;
                    blockIdArray[index] = blockId;
                    unblockAttemptNoArray[index] = unblockAttemptNo;

                    updateQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS SET UNBLOCK_STATUS = 'I', DATE_UPDATED = sysdate WHERE CUSTCODE = " + rocode + " AND block_id  = " + blockId + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNo;
                    updateQueries.append("#").append(updateQuery);
                    index++;
                }
                updateQuery = updateQueries.substring(1, updateQueries.length());
                int updatedRow = obj.updateInsertStatus(con_RDB, updateQuery, true);
//                    if (updatedRow > 0) {
                String[] message = ss.unblockCheck(rocodeArray, socodeArray, sapConf);
                System.out.println("message size " + message.length);

                String statusChangeQuery = "";
                System.out.println("RO :: " + message);
                Util utilObj = new Util();
                if (updatedRow == message.length) {
                    for (int j = 0; j < message.length; j++) {

                        if (message[j] == null) {
                        } else if (message[j].trim().equalsIgnoreCase("Order Unblocked!!Delivery Unblocked!!Billing Unblocked!!") || message[j].trim().equalsIgnoreCase("Custmer is already Unblocked in sales area!!")) {//Already Blocked
                            System.out.println("rocode : " + rocodeArray[j] + "->" + message[j]);
                            status = "S";
                            sendMsg = true;
                            statusChangeQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS set SAP_UNBLOCK_MSG = '" + message[j].trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate, UNBLOCK_CONF_DATETIME = sysdate  where block_id  = " + blockIdArray[j] + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNoArray[j];

                        } else {            //Currently Blocked by user
                            System.out.println("rocode : " + rocodeArray[j] + "->" + message[j]);
                            status = "R";

                            statusChangeQuery = "UPDATE cmsro.SAP_UNBLOCK_RO_DETAILS set SAP_UNBLOCK_MSG = '" + message[j].trim() + "', UNBLOCK_STATUS  = '" + status + "' ,  DATE_UPDATED = sysdate, UNBLOCK_CONF_DATETIME = sysdate  where block_id  = " + blockIdArray[j] + " AND UNBLOCK_ATTEMPT_NO=" + unblockAttemptNoArray[j];
                            String mergeUnblockingDetails = "";
                            mergeUnblockingDetails = "INSERT INTO CMSRO.SAP_UNBLOCK_RO_DETAILS \n"
                                    + "SELECT CUSTCODE,  RO_TYPE,  BLOCK_ID,  BLOCK_DATE,  BLOCK_ATTEMPT_NO,  SYSDATE,  'P',  UNBLOCK_ATTEMPT_NO+1,  null,  '',  SYSDATE,  SALES_ORG from CMSRO.SAP_UNBLOCK_RO_DETAILS where block_id= " + blockIdArray[j] + " and UNBLOCK_ATTEMPT_NO=" + unblockAttemptNoArray[j];
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
                                if (updatedRows > 0) {
                                    if (sendMsg) {

                                        utilObj.SendSMSCall(rocodeArray[j], blockIdArray[j]);//Uncomment while deploying
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

    public static void update7DaysStock() {
//        String query = ""; String insertQuery = ""; String updateQuery = "";//        int rows = 0;//        CMS_RDB_UNBLOCKING_RO obj = new CMS_RDB_UNBLOCKING_RO();
        Connection con_RDB = null;
        CallableStatement callableStatement = null;
        String update7DaysStk = "";
        try {
            /*-- last 7 days stock check and updation of STK_BLOCK_FLAG
             */
            con_RDB = Conn.getConRDB();
            update7DaysStk = "{call update_7Days_Stock}";//Procedure in RetailDBProd DB
//            try {
//                query = "update CMSRO.SAP_BLOCK_RO_DETAILS set stk_block_flag='N',DATE_UPDATED=sysdate where  sap_block_curr='B' and stk_block_flag='Y' and custcode in ( \n"
//                        + "  SELECT ro_code\n"
//                        + "  FROM\n"
//                        + "    (SELECT ro_code ,\n"
//                        + "      COUNT(*) AS stock_dates\n"
//                        + "    FROM\n"
//                        + "      (SELECT DISTINCT (tran_date) ,\n"
//                        + "        ro_code\n"
//                        + "      FROM\n"
//                        + "        (SELECT ro_code,\n"
//                        + "          tran_date\n"
//                        + "        FROM CMS_MANUAL_RO_STK\n"
//                        + "        WHERE tran_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd')\n"
//                        + "        UNION\n"
//                        + "        SELECT ro_code, stk_date\n"
//                        + "        FROM cmsro.cms_blocking_stk_details\n"
//                        + "        WHERE stk_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd')\n"
//                        + "        )\n"
//                        + "      WHERE tran_date BETWEEN TO_CHAR(to_date(sysdate-7,'dd/mm/yy'),'yyyymmdd') AND TO_CHAR(to_date(sysdate-1,'dd/mm/yy'),'yyyymmdd')\n"
//                        + "      ORDER BY TRAN_DATE\n"
//                        + "      )\n"
//                        + "    GROUP BY ro_code\n"
//                        + "    )\n"
//                        + "  WHERE stock_dates=7 \n"
//                        + "  )";
//                rows = obj.updateInsertStatus(con_RDB, query);
//                System.out.println("----------------------------------------/n Last 7 days stock check and updation of STK_BLOCK_FLAG");
//                System.out.println(rows + " Updated Row(s).");
//                System.out.println("----------------------------------------");
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Exception occurred while executing updateQuery");
//            }
//            /*--insert record to unblock table after checking CMS and RDB block
//             */
//            try {
//                insertQuery = "insert into CMSRO.SAP_UNBLOCK_RO_DETAILS\n"
//                        + "select custcode,ro_type,block_id,block_date,block_attempt_no,sysdate,'P',1,'','',sysdate,SALES_ORG from CMSRO.SAP_BLOCK_RO_DETAILS where sap_block_curr='B' and cms_block_flag='N' and stk_block_flag='N' and sap_block_status='S' and block_id not in ((select block_id from CMSRO.SAP_UNBLOCK_RO_DETAILS))\n"
//                        + " ";
//                rows = obj.updateInsertStatus(con_RDB, insertQuery);
//                System.out.println("----------------------------------------/n insert record to unblock table after checking CMS and RDB block");
//                System.out.println(rows + " Updated Row(s).");
//                System.out.println("----------------------------------------");
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Exception occurred while executing insertQuery");
//            }
//            try {
//                updateQuery = "  update CMSRO.SAP_BLOCK_RO_DETAILS set sap_block_curr='U' where sap_block_curr='B' and block_id in (select block_id from CMSRO.SAP_UNBLOCK_RO_DETAILS)";
//                rows = obj.updateInsertStatus(con_RDB, updateQuery);
//                System.out.println("----------------------------------------/n Updating SAP_BLOCK_RO_DETAILS setting sap_block_curr='U' ");
//                System.out.println(rows + " Updated Row(s).");
//                System.out.println("----------------------------------------");
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println("Exception occurred while executing updateQuery");
//            }

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
