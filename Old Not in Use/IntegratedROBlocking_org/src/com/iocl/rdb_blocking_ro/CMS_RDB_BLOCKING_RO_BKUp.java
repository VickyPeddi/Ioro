///*
// * Copyright @ IndianOil Corporation Ltd.
// * All rights reserved.
// */
//package com.iocl.rdb_blocking_ro;
//
//import com.iocl.dbconn.Conn;
//import static com.iocl.util.Util.SendSMSCall;
//import static com.iocl.util.Util.sendMail;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import sapdownload.YV_IF_CUST_SALE_BLOCK;
//
///**
// *
// * @author Rohit Vyas <rohitvyas0312@gmail.com>
// * Create Date : Aug 19, 2017
// */
//public class CMS_RDB_BLOCKING_RO_BKUp {
//
//    Connection con = null;
//    Connection con1 = null;
//    Connection con2 = null;
//    Connection con3 = null;
//    ResultSet rs = null;
//    Statement stm = null;
//    String query = "";
//    ResultSet rs1 = null;
//    Statement stm1 = null;
//    String query1 = "";
//    ResultSet rs2 = null;
//    Statement stm2 = null;
//    String query2 = "";
//    Statement stm3 = null;
//    String query3 = "";
//    ResultSet rs4 = null;
//    Statement stm4 = null;
//    String query4 = "";
//    String name = "";
//    String desg = "";
//
//    String SAName = "";
//    String SACode = "";
//    String rocode = "";
//    String roname = "";
//    String prod_code = "";
//    String prod_name = "";
//    String eff_date = "";
//    String eff_till = "";
//    String type = "";
//    String rate = "";
//    String docode = "";
//    String salesarea = "";
//    String do_code = "";
////    String body = "";
//    String table = "";
//    String ftable = "";
//    String ftable1 = "";
//    String ftable2 = "";
//    String bcc = "";//"mounisharoy@indianoil.in ,MALTIJOSHI@indianoil.in ,rohitvyas0312@gmail.com";
//    String subject = "";
//    String sender = "retaildashboard@INDIANOIL.IN";//retaildashboard@INDIANOIL.IN
//    String cc = "";
//    String temp = "";
//    String SA = "";
//    String DO = "";
//    int count = 1;
//    int a = 0;
//    int insRow = 0;
//    int insRow1 = 0;
//    Connection conRDB = null;
//
//    public static void main(String[] args) throws SQLException {
//
//        String SAPCon = "SAPQAS";
//
//        try {
//            CallSAPPI(SAPCon, "");
//        } catch (Exception e) {
//            System.out.println("Exception occurred while Syncing...");
//            e.printStackTrace();
//        }
//
//        /*  CMS_RDB_BLOCKING_RO obj = new CMS_RDB_BLOCKING_RO();
//        try {
//            obj.maildealer();
//        } catch (Exception e) {
//            System.out.println("Exception occurred while Mailing to Dealer...");
//            e.printStackTrace();
//        }
//        try {
//            obj.mailFO();
//        } catch (Exception e) {
//            System.out.println("Exception occurred while Mailing to FO...");
//            e.printStackTrace();
//        }*/
//    }
//
//    public static void CallSAPPI(String args, String conn) throws SQLException {
//
//        long startTime = 0L;
//        long endTime = 0L;
//        long timeTaken = 0L;
//        String update7DaysStk = "";
//        ResultSet rs = null;
//        Statement stm = null, stm2 = null;
//        Connection con_RDB = null;
//        CMS_RDB_BLOCKING_RO_BKUp obj = new CMS_RDB_BLOCKING_RO_BKUp();
//        int row_count = Integer.parseInt(getValue("select count(*) from cmsro.SAP_BLOCK_RO_DETAILS where  SAP_BLOCK_STATUS='P' and sap_block_curr is null and sales_org in(1100,1200,1300,1400)"));
//        for (int i = 0; i < row_count; i++) {//Uncomment for loop
//            try {
//                con_RDB = Conn.getConRDB();
//                if (i % 100 == 0) {
//                    update7DaysStk = "update CMSRO.SAP_BLOCK_RO_DETAILS set stk_block_flag='N',sap_block_curr='D' ,SAP_BLOCK_STATUS='D',DATE_UPDATED=sysdate where  sap_block_curr is null and stk_block_flag='Y' and CMS_BLOCK_FLAG='N' and SAP_BLOCK_STATUS='P' and custcode in (\n"
//                            + "  SELECT custcode\n"
//                            + " FROM\n"
//                            + "  (SELECT custcode ,\n"
//                            + "    COUNT(*) AS stock_dates\n"
//                            + "  FROM\n"
//                            + "    (SELECT DISTINCT (stock_date) ,\n"
//                            + "      custcode\n"
//                            + "    FROM tbl_manual_auto_stock\n"
//                            + "    WHERE stock_date BETWEEN TO_CHAR(to_date(sysdate-7,'dd/mm/yy'),'yyyymmdd') AND TO_CHAR(to_date(sysdate-1,'dd/mm/yy'),'yyyymmdd')\n"
//                            + "    )\n"
//                            + "  GROUP BY custcode\n"
//                            + "  )\n"
//                            + " WHERE stock_dates=7\n"
//                            + "  )";
//
//                    int recordsUpdated = obj.updateInsertStatus(con_RDB, update7DaysStk);
//                    System.out.println("Deferred Blocking : " + i + "..." + recordsUpdated + " , Time : " + new Date());
//                }
//
//                String query = "select custcode, sales_org, block_attempt_no, FINAL_BLOCK_STATUS, BLOCK_ID from cmsro.SAP_BLOCK_RO_DETAILS \n"
//                        + "where  SAP_BLOCK_STATUS='P'  and sap_block_curr is null and rownum = 1  and sales_org in(1100,1200,1300,1400)";//and block_attempt_no=3;
//                //  + "and convert(char(8),block_eff_from,112) = convert(char(8),getdate(),112)";
//
//                stm = con_RDB.createStatement();
//                rs = stm.executeQuery(query);
//
//                String RO_Code = "", SO_Code = "", finalBlockStatus = "", blockID = "";
//                int blockAttemptNo = 0;
//
//                String query1 = "";
//                int count = 0;
//                boolean sendMsg = false;
//                while (rs.next()) {
//
//                    RO_Code = rs.getString("custcode");//"284763";//"178895";
//                    SO_Code = rs.getString("sales_org"); //"1400";
//                    blockAttemptNo = rs.getInt("block_attempt_no");
//                    finalBlockStatus = rs.getString("FINAL_BLOCK_STATUS");
//                    blockID = rs.getString("BLOCK_ID");
//
//                    String updateQuery = "UPDATE cmsro.SAP_BLOCK_RO_DETAILS SET SAP_BLOCK_STATUS = 'I', DATE_UPDATED = sysdate WHERE block_id = " + blockID + " and block_attempt_no=" + blockAttemptNo;
//                    int updatedRow = obj.updateInsertStatus(con_RDB, updateQuery);
////                System.out.println("updatedRow: " + updatedRow);
//                    YV_IF_CUST_SALE_BLOCK ss = new YV_IF_CUST_SALE_BLOCK();
//
//                    String sapConf = "./ConnSAP.xml";
//                    if (updatedRow > 0) {
//                        startTime = System.currentTimeMillis();
//
////                        String message = ss.blockCheck(RO_Code, SO_Code, sapConf);
//                        String message = "Order blocked!!Delivery blocked!!Bill blocked!!";//ss.blockCheck(RO_Code, SO_Code, sapConf);
//
//                        endTime = System.currentTimeMillis();
//                        timeTaken = endTime - startTime;
//                        System.out.println("Time taken in Blocking ROCODE " + RO_Code + " is:: " + timeTaken + " ms");
//
//                        try {
//                            if (message.trim().equalsIgnoreCase("Order blocked!!Delivery blocked!!Bill blocked!!") || message.trim().equalsIgnoreCase("Custmer is already blocked in sales area!!")) {
//                                sendMsg = true;
//                                query1 = "update cmsro.SAP_BLOCK_RO_DETAILS set SAP_BLOCK_MSG = '" + message + "' ,  DATE_UPDATED = sysdate,"
//                                        + " SAP_BLOCK_STATUS = 'S', BLOCK_CONF_DATETIME = sysdate, SAP_BLOCK_CURR='B' where block_id = " + blockID + " and SAP_BLOCK_STATUS = 'I' and block_attempt_no=" + blockAttemptNo;
//
//                            } else {
//
//                                query1 = "update cmsro.SAP_BLOCK_RO_DETAILS set SAP_BLOCK_MSG = '" + message + "' , DATE_UPDATED = sysdate,"
//                                        + " SAP_BLOCK_STATUS = 'R' where block_id = " + blockID + " and SAP_BLOCK_STATUS = 'I' and block_attempt_no=" + blockAttemptNo;
//                                String mergeblockingDetails = "";
//                                mergeblockingDetails = "INSERT INTO CMSRO.SAP_BLOCK_RO_DETAILS  \n"
//                                        + " SELECT  CUSTCODE, RO_TYPE, CREATE_DATETIME, CMS_BLOCK_FLAG, STK_BLOCK_FLAG, FINAL_BLOCK_STATUS, 'P', BLOCK_ID, "
//                                        + " BLOCK_ATTEMPT_NO+1 AS BLOCK_ATTEMPT_NO, BLOCK_DATE, SAP_BLOCK_MSG, BLOCK_CONF_DATETIME, DATE_UPDATED, SALES_ORG, SAP_BLOCK_CURR  "
//                                        + " FROM CMSRO.SAP_BLOCK_RO_DETAILS WHERE block_id = " + blockID + "  AND BLOCK_ATTEMPT_NO = " + blockAttemptNo;
//                                try {
//                                    int rowsMerged = obj.updateInsertStatus(con_RDB, mergeblockingDetails);
////                                    System.out.println(rowsMerged + " Rows merged");
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            stm2 = con_RDB.createStatement();
//                            int updatedRows = stm2.executeUpdate(query1);
//                            if (updatedRows > 0) {
//                                if (sendMsg) {
//                                    startTime = System.currentTimeMillis();
//                                    SendSMSCall(RO_Code, finalBlockStatus, blockID);
//                                    endTime = System.currentTimeMillis();
//                                    timeTaken = endTime - startTime;
//                                    System.out.println("Time taken in outgoing SMS ROCODE " + RO_Code + " is:: " + timeTaken + " ms");
//
//                                }
//                                count++;
//                                System.out.println(count + " Rows(s) Updated in SAP (Blocking Process). Block ID: " + blockID + " || Block AttemptNo: " + blockAttemptNo);
//                            }
//
//                        } catch (Exception EE) {
//                            System.out.println(EE);
////            EE.printStackTrace();
//                        } finally {
//                            try {
//                                if (stm2 != null) {
//                                    stm2.close();
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//
//            } catch (Exception e) {
//                System.out.println("Error: " + e.getMessage());
//                e.printStackTrace();
//            } finally {
//                try {
//                    if (rs != null) {
//                        rs.close();
//                    }
//                } catch (Exception e) {
//                }
//                try {
//                    if (stm != null) {
//                        stm.close();
//                    }
//                } catch (Exception e) {
//                }
//                try {
//                    if (con_RDB != null) {
//                        con_RDB.close();
//                    }
//                } catch (Exception e) {
//                    //   e.printStackTrace();
//                }
//
//            }
//            System.out.println("---Process Completed ---");
//        }
//    }
//
//    public void maildealer() throws SQLException {
//        Conn con = new Conn();
//        Connection con_RDB = null;
//        con_RDB = Conn.getConRDB();
//        ResultSet rs = null, rs1 = null;
//        Statement stm = null, stm1 = null, stm2 = null;
//        StringBuffer SA = new StringBuffer();
//        try {
//            String emailID = "";
//            String blockID = "";
//            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//            java.sql.Date myDate = new java.sql.Date(System.currentTimeMillis());
//            System.out.println("Today's Date : " + dateFormat.format(myDate));
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(myDate);
//
//            cal.add(Calendar.DATE, -1);
//            int month = cal.get(Calendar.MONTH);
////            String period = getMonthForInt(month);
//            System.out.println("Yesterday's Date : " + dateFormat.format(cal.getTime()));
//            boolean mailFlag = false;
//            try {
//
//                stm = con_RDB.createStatement();
//
////                query = "SELECT  B.*, A.block_id as block_id FROM \n" + "(select distinct custcode,  block_id  from cmsro.SAP_BLOCK_RO_DETAILS where SAP_BLOCK_STATUS ='S') A\n" + "LEFT OUTER JOIN \n" + "(select custcode , email ,  vender_code  from  IOCL_MD_AOD_SALES) B\n" + "ON  A.CUSTCODE=B.CUSTCODE and  vender_code is not null and rownum=10";
//                query = "SELECT a.custcode as custcode, a.block_id as block_id,\n"
//                        + "  b.email as email\n"
//                        + "FROM\n"
//                        + "  (SELECT *\n"
//                        + "  FROM cmsro.SAP_BLOCK_RO_DETAILS\n"
//                        + "  WHERE sap_block_status='S'\n"
//                        + "  AND sap_block_curr    ='B'\n"
//                        + "  ) a\n"
//                        + "LEFT JOIN IOCL_MD_AOD_SALES b\n"
//                        + "ON a.custcode=b.custcode\n"
//                        + "WHERE email IS NOT NULL ";
//                rs = stm.executeQuery(query);
//                int bccCount = 0;
//                while (rs.next()) {
//                    bccCount++;
//                    String ro_code = rs.getString("custcode");
//                    emailID = rs.getString("email");
//                    blockID = rs.getString("block_id");
////                    String vendor = rs.getString("vender_code");
//
//                    try {
//
//                        query1 = "select CUSTCODE , CUST_NAME  , SALESAREA_NAME  , TELF1 from IOCL_MD_AOD_SALES where CUSTCODE= '" + ro_code + "'\n";
//
//                        stm1 = con_RDB.createStatement();
//                        System.out.println(query1);
//                        rs1 = stm1.executeQuery(query1);
//                        StringBuffer missingDates = null;
//                        while (rs1.next()) {
//
//                            String body = "<html><body style='font-size: 90%;'><br><br> <p class=\"MsoNormal\" style=\"margin-bottom:12.0pt\"><b><span lang=\"EN-US\" style=\"font-size:18.0pt;color:#1f497d\">Dear Channel Partner,\n"
//                                    + "<br>\n"
//                                    + "<br>\n"
//                                    + "Indenting for your RO &nbsp;has been blocked &nbsp;since stock updation is pending &nbsp;as per following details . Please update the stock through e-ledger or xSparsh and then place your indent.<u></u><u></u></span></b></p>"
//                                    + "<table cellspacing='0' style='font-size: 85%;border: 1px solid black'><tr Style='background-color:#9bbb59'>"
//                                    //     + "<td style='border: 1px solid black'>SA.</td>"
//                                    + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>RO CODE</td>"
//                                    + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>RO NAME</td>"
//                                    //    + "<td style='border: 1px solid black'>RO CONTACT NUMBER</td>"
//                                    + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;border: 1px solid black;'>MS STOCK NOT UPDATED FOR</td>"
//                                    + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>HSD STOCK NOT UPDATED FOR</td>"
//                                    + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>XM STOCK NOT UPDATED FOR</td>"
//                                    + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>XP STOCK NOT UPDATED FOR</td>"
//                                    + "</tr>";
//                            String table = "";
//                            missingDates = new StringBuffer();
//                            String custCode = rs1.getString("CUSTCODE");
//                            String custName = rs1.getString("CUST_NAME");
//                            String salesAreaName = rs1.getString("SALESAREA_NAME");
//                            String telf1 = rs1.getString("TELF1");
//
//                            String getMissingDates = "";// "WITH all_dates AS ( SELECT TO_DATE(sysdate+1) - ROWNUM  AS missing_date\n"                                    + "FROM dual\n"                                    + "CONNECT BY ROWNUM <= add_MONTHS(TO_DATE(sysdate),0 ) - TO_DATE(sysdate-8)\n"                                    + ")\n"                                    + "SELECT to_char(all_dates.missing_date,'dd-MON-yyyy') as missing_date,a.prod_code\n"                                    + "FROM all_dates\n"                                    + "LEFT JOIN (select tran_date,prod_code from cms_manual_ro_stk where ro_code='" + custCode + "') a ON ( to_date(a.tran_date,'yyyymmdd') = all_dates.missing_date )\n"                                    + "WHERE a.tran_date IS NULL ORDER BY all_dates.missing_date ";
//                            getMissingDates = "WITH all_dates AS ( SELECT TO_DATE(sysdate) - ROWNUM  AS missing_date\n"
//                                    + "  FROM dual\n"
//                                    + "                CONNECT BY ROWNUM <= add_MONTHS(TO_DATE(sysdate),0 ) - TO_DATE(sysdate-7)\n"
//                                    + "  )\n"
//                                    + "  SELECT to_char(all_dates.missing_date,'dd-MON-yyyy') as missing_date,a.prod_code\n"
//                                    + "  FROM all_dates\n"
//                                    + "  LEFT JOIN (select tran_date,prod_code from retaildb.cms_manual_ro_stk where ro_code='" + custCode + "'\n"
//                                    + "  UNION\n"
//                                    + "        SELECT stk_date, PROD_GROUP as prod_code FROM cmsro.cms_blocking_stk_details WHERE stk_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd') and ro_code='" + custCode + "'\n"
//                                    + "  ) a ON ( to_date(a.tran_date,'yyyymmdd') = all_dates.missing_date )\n"
//                                    + "  WHERE a.tran_date IS NULL ORDER BY all_dates.missing_date ";
//                            try {
//                                stm2 = con_RDB.createStatement();
//                                rs2 = stm2.executeQuery(getMissingDates);
//                                int count = 0;
//                                while (rs2.next()) {
//                                    mailFlag = true;
//                                    if (count != 0) {
//                                        missingDates.append(",");
//                                    }
//                                    missingDates.append("'" + rs2.getString("missing_date").toString() + "'");
//                                    count++;
//                                }
////                                if (!missingDates.toString().equalsIgnoreCase("")) {
////                                    missingDates.append(missingDates.toString().substring(1));
////                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            } finally {
//                                try {
//                                    if (rs2 != null) {
//                                        rs2.close();
//                                    }
//                                } catch (Exception e) {
//                                }
//                                try {
//                                    if (stm2 != null) {
//                                        stm2.close();
//                                    }
//                                } catch (Exception e) {
//                                }
//                            }
//
//                            if (!missingDates.toString().equalsIgnoreCase("")) {
//                                String dates = missingDates.toString().replaceAll("'", "");
//                                table = "<tr>"
//                                        //+ "<td style='border: 1px solid black'>" + rs1.getString("SALESAREA_NAME") + "</td>"
//                                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + rs1.getString("CUSTCODE") + "</td>"
//                                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + rs1.getString("CUST_NAME") + "</td>"
//                                        //  + "<td style='border: 1px solid black'>" + rs1.getString("TELF1") + "</td>"
//                                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                                        + "</tr>";
//                            }
//                            if (mailFlag) {
//                                String footer = "<p class=\"MsoNormal\"><b><span lang=\"EN-US\" style=\"font-size:18.0pt;color:#1f497d\"><u></u>&nbsp;<u></u></span></b></p>"
//                                        + "<p class=\"MsoNormal\"><b><span lang=\"EN-US\" style=\"font-size:18.0pt;color:#1f497d\"><u></u>&nbsp;<u></u></span></b></p>"
//                                        + "<p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Thanking you,<u></u><u></u></span></p>"
//                                        + "<p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Yours faithfully,<u></u><u></u></span></p>"
//                                        + "<p><strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Retail Sales</span></strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\"><u></u><u></u></span></p>"
//                                        + "<p><strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">IndianOil Corporation Limited (Marketing Division)</span></strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\"><u></u><u></u></span></p>"
//                                        + "<p class=\"MsoNormal\" style=\"margin-bottom:12.0pt\"><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Disclaimer :\n"
//                                        + "<i>This is a system generated mail, hence no signature is required. Do not reply to this mail.</i></span><span lang=\"EN-US\"><u></u><u></u></span></p>";
//                                body = body + " " + table + " </table> " + footer + " </body></html> <br><b>";
//                                //+ "You can access the required report  in by clicking on this hyperlink. <a href='https://spandan.indianoil.co.in/RetailNew/' target='_blank'>RetailNew</a><br>System Administrator <br>\n" + "";
//                                System.out.println("body\n" + body);
//                                subject = "Non updation of Retail outlet ";
////                                bcc = "";
////                                if (bccCount < 6) {
////                                    bcc = "mounisharoy@indianoil.in";
////                                }
//                                String response = sendMail(sender, emailID, cc, bcc, subject, body);
//                                if (response.equalsIgnoreCase("Success")) {
//                                    System.out.println("Mail Sent Successfully...To:Dealer: " + custCode);
//                                } else {
//                                    System.out.println("Mail Sending Failed...To:Dealer: " + custCode);
//                                }
//
//                                CMS_RDB_BLOCKING_RO_BKUp obj = new CMS_RDB_BLOCKING_RO_BKUp();
//                                String insertMailDetail = "";
//
//                                insertMailDetail = "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + blockID + "','" + sender + "','BLOCKED RO EMAIL','" + emailID + "','" + response + "',SYSDATE,'DEALER')";
//                                int insertedRows = obj.updateInsertStatus(con_RDB, insertMailDetail);
//                                System.out.println("MAIL Details Inserted: " + insertedRows + "\n" + insertMailDetail);
//                            }
//                            mailFlag = false;
//                        }
//                    } catch (Exception ee) {
//                        ee.printStackTrace();
//                        System.out.println(ee);
//                    } finally {
//                        if (rs1 != null) {
//                            rs1.close();
//                        }
//                        if (stm1 != null) {
//                            stm1.close();
//                        }
//
//                    }
//
//                }
//
//            } catch (Exception e) {
//                System.out.println(e);
//            } finally {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stm != null) {
//                    stm.close();
//                }
//
//            }
//
//        } catch (Exception ee) {
//            System.out.println(ee);
//        } finally {
//            if (con_RDB != null) {
//                con_RDB.close();
//            }
//            if (con1 != null) {
//                con1.close();
//            }
//            if (con3 != null) {
//                con3.close();
//            }
//        }
//    }
//
//    public void mailFO() throws SQLException {
//        Conn con = new Conn();
//        Connection con_RDB = null;
//        con_RDB = Conn.getConRDB();
//        ResultSet rs = null, rs1 = null;
//        Statement stm = null, stm1 = null, stm2 = null;
////        StringBuffer SA = new StringBuffer();
//        String SA = "";
//        try {
//
//            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//            java.sql.Date myDate = new java.sql.Date(System.currentTimeMillis());
//            System.out.println("Today's Date : " + dateFormat.format(myDate));
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(myDate);
//            cal.add(Calendar.DATE, -1);
//            System.out.println("Yesterday's Date : " + dateFormat.format(cal.getTime()));
//
//            try {
//
//                stm = con_RDB.createStatement();
//                String query = "SELECT DISTINCT SALESAREA FROM IOCL_MD_AOD_SALES WHERE CUSTCODE IN (select  custcode  from cmsro.SAP_BLOCK_RO_DETAILS where  SAP_BLOCK_CURR ='B')";
//                //   System.out.println(query2);
//
//                rs = stm.executeQuery(query);
//                while (rs.next()) {
////                SA += ",'" + rs.getString(1) + "'";
////                    SA.append(",'").append(rs.getString(1) + "'");
////                    SA += ",'" + rs.getString("salesarea") + "'";
//                    SA += ",'" + rs.getString("salesarea") + "'";
//                }
//
////                SA = SA.append(SA.substring(1));
//                if (!SA.equalsIgnoreCase("")) {
//                    SA = SA.substring(1);
//                }
//                // SA = "'U20'";
//            } catch (Exception ee) {
//                System.out.println(ee);
//            } finally {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stm != null) {
//                    stm.close();
//                }
//            }
//
//            try {
//
//                stm = con_RDB.createStatement();
//
//                query = " select DISTINCT(SALES_GROUP)SALES_GROUP , SALES_AREA ,EMP_CODE ,EMP_NAME ,EMAIL_ID ,DESIGNATION ,LOC_CODE from MST_EMPLOYEE  where email_id is not NULL  "
//                        + " and EMP_STATUS_CODE= 3  and sales_group is not null and psa_code = 'SL01' "
//                        + " AND SALES_GROUP in (" + SA + ") ";
//                int month = cal.get(Calendar.MONTH);
////                String period = getMonthForInt(month);
//                boolean mailFlag = false;
//                rs = stm.executeQuery(query);
//                int bccCount = 0;
//                while (rs.next()) {//loop for SA Incharge
//                    bccCount++;
//                    String body = "<html><body style='font-size: 90%;'><br><br>Stock details are pending for updation within the last 7 days for the following dealers under your sales area. These dealers will not be able to place their indents, in case such stock details are not entered through either eledger or Xparsh. Please advise the dealers to update their stocks immediately. "
//                            + "<br> <br><br>"
//                            + "<table cellspacing='0' style='font-size: 85%;border: 1px solid black'><tr Style='background-color:silver'>"
//                            + "<td style='border: 1px solid black'>SA.</td>"
//                            + "<td style='border: 1px solid black'>RO CODE</td>"
//                            + "<td style='border: 1px solid black'>RO NAME</td>"
//                            + "<td style='border: 1px solid black'>RO CONTACT NUMBER</td>"
//                            + "<td style='border: 1px solid black'>MS STOCK NOT UPDATED FOR</td>"
//                            + "<td style='border: 1px solid black'>HSD STOCK NOT UPDATED FOR</td>"
//                            + "<td style='border: 1px solid black'>XM STOCK NOT UPDATED FOR</td>"
//                            + "<td style='border: 1px solid black'>XP STOCK NOT UPDATED FOR</td>"
//                            + "</tr>";
//                    String table = "";
//                    SACode = rs.getString("SALES_GROUP");
//                    SAName = rs.getString("SALES_AREA");
//                    name = rs.getString("EMP_NAME");
//                    desg = rs.getString("DESIGNATION");
//                    String emailID = rs.getString("EMAIL_ID");
//                    do_code = rs.getString("LOC_CODE");
//                    /*rdb eracts *//* first query */
//
//
//                    try {
//
//                        query1 = "SELECT   B.CUSTCODE ,  B.CUST_NAME  ,  B.SALESAREA_NAME  ,  B.TELF1  FROM \n"
//                                + "(select distinct custcode  from cmsro.SAP_BLOCK_RO_DETAILS where SAP_BLOCK_STATUS ='S'  AND SAP_BLOCK_CURR ='B')A\n"
//                                + "LEFT OUTER JOIN \n"
//                                + "(select CUSTCODE , CUST_NAME  , SALESAREA_NAME  , TELF1 from IOCL_MD_AOD_SALES where salesarea= '" + SACode + "')B\n"
//                                + "ON  A.CUSTCODE=B.CUSTCODE  where to_char(B.custcode) is not null\n"
//                                + "\n";
//                        stm1 = con_RDB.createStatement();
//                        System.out.println(query1);
//                        rs1 = stm1.executeQuery(query1);
//                        StringBuffer missingDates = null;
//
//                        while (rs1.next()) {//loop for ROCODE
//
//                            missingDates = new StringBuffer();
//                            String custCode = rs1.getString("CUSTCODE");
//
//                            String getMissingDates = " WITH all_dates AS ( SELECT TO_DATE(sysdate) - ROWNUM  AS missing_date\n"
//                                    + " FROM dual\n"
//                                    + "  CONNECT BY ROWNUM <= add_MONTHS(TO_DATE(sysdate),0 ) - TO_DATE(sysdate-7)\n"
//                                    + "    )\n"
//                                    + "   SELECT to_char(all_dates.missing_date,'dd-MON-yyyy') as missing_date,a.prod_code\n"
//                                    + "  FROM all_dates\n"
//                                    + "   LEFT JOIN (select tran_date,prod_code from retaildb.cms_manual_ro_stk where ro_code='" + custCode + "'\n"
//                                    + "   UNION\n"
//                                    + "  SELECT stk_date, PROD_GROUP as prod_code FROM cmsro.cms_blocking_stk_details WHERE stk_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd') and ro_code=" + custCode + "\n"
//                                    + "  ) a ON ( to_date(a.tran_date,'yyyymmdd') = all_dates.missing_date )\n"
//                                    + "  WHERE a.tran_date IS NULL ORDER BY all_dates.missing_date ";
//
//                            try {
//                                stm2 = con_RDB.createStatement();
//                                rs2 = stm2.executeQuery(getMissingDates);
//                                int count = 0;
//                                while (rs2.next()) {
//                                    mailFlag = true;
//                                    if (count != 0) {
//                                        missingDates.append(",");
//                                    }
//                                    missingDates.append("'" + rs2.getString("missing_date").toString() + "'");
//                                    count++;
//                                }
//
////                                if (!missingDates.toString().equalsIgnoreCase("")) {
////                                    missingDates.substring(1);
////                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            } finally {
//                                try {
//                                    if (rs2 != null) {
//                                        rs2.close();
//                                    }
//                                } catch (Exception e) {
//                                }
//                                try {
//                                    if (stm2 != null) {
//                                        stm2.close();
//                                    }
//                                } catch (Exception e) {
//                                }
//                            }
//
////                            table = "";
////                            ftable = "";
//                            if (!missingDates.toString().equalsIgnoreCase("")) {
//                                String mob = rs1.getString("TELF1");
//                                if (mob == null || mob.equalsIgnoreCase("")) {
//                                    mob = "N/A";
//                                }
//                                String dates = missingDates.toString().replaceAll("'", "");
//                                table = table + "<tr>"
//                                        + "<td style='border: 1px solid black'>" + rs1.getString("SALESAREA_NAME") + "</td>"
//                                        + "<td style='border: 1px solid black'>" + rs1.getString("CUSTCODE") + "</td>"
//                                        + "<td style='border: 1px solid black'>" + rs1.getString("CUST_NAME") + "</td>"
//                                        + "<td style='border: 1px solid black'>" + mob + "</td>"
//                                        + "<td style='border: 1px solid black'>" + dates + "</td>"
//                                        + "<td style='border: 1px solid black'>" + dates + "</td>"
//                                        + "<td style='border: 1px solid black'>" + dates + "</td>"
//                                        + "<td style='border: 1px solid black'>" + dates + "</td>"
//                                        + "</tr>";
//                            }
//                        }//End Of -loop for ROCODE
//                    } catch (Exception ee) {
//                        ee.printStackTrace();
//                        System.out.println(ee);
//                    } finally {
//                        if (rs1 != null) {
//                            rs1.close();
//                        }
//                        if (stm1 != null) {
//                            stm1.close();
//                        }
//
//                    }
//                    if (mailFlag) {
//                        body = body + " " + table + " </table></body></html> <br><b>You can access the required report  in by clicking on this hyperlink.  <a href='https://spandan.indianoil.co.in/RetailNew/' target='_blank'>RetailNew</a><br>System Administrator <br>";
//                        System.out.println("body\n" + body);
//                        subject = "Non updation of Retail outlet ";
////                        bcc = "";
////                        if (bccCount < 6) {
////                            bcc = "mounisharoy@indianoil.in";
////                        }
//                        String response = sendMail(sender, emailID, cc, bcc, subject, body);
//                        if (response.equalsIgnoreCase("Success")) {
//                            System.out.println("Mail Sent Successfully...To:SalesArea: " + SACode);
//                        } else {
//                            System.out.println("Mail Sending Failed...To:SalesArea: " + SACode);
//                        }
//
//                        CMS_RDB_BLOCKING_RO_BKUp obj = new CMS_RDB_BLOCKING_RO_BKUp();
//                        String insertMailDetail = "";
//
//                        insertMailDetail = "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + SACode + "'|| to_char(sysdate,'yyyymmddhh24miss'),'" + sender + "','BLOCKED RO EMAIL','" + emailID + "','" + response + "',SYSDATE,'FO')";
//                        int insertedRows = obj.updateInsertStatus(con_RDB, insertMailDetail);
//                        System.out.println("MAIL Details Inserted: " + insertedRows);
//                    }
//                    mailFlag = false;
//                }//End Of -loop for SA Incharge
//
//            } catch (Exception e) {
//                System.out.println(e);
//            } finally {
//                if (rs != null) {
//                    rs.close();
//                }
//                if (stm != null) {
//                    stm.close();
//                }
//
//            }
//
//        } catch (Exception ee) {
//            System.out.println(ee);
//        } finally {
//            if (con_RDB != null) {
//                con_RDB.close();
//            }
//            if (con1 != null) {
//                con1.close();
//            }
//            if (con3 != null) {
//                con3.close();
//            }
//        }
//    }
//
//    public static String getValue(String query) {
//
//        Connection con = null;
//        ResultSet rs = null;
//        String value = "";
//        Statement stmt = null;
//
//        try {
//
//            if (con == null) {
//                con = Conn.getConRDB();
//            }
//
//            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
//
//            rs = stmt.executeQuery(query);
//
//            if (rs.next()) {
//                value = rs.getString(1);
//            } else {
//                value = "";
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            try {
//                if (rs != null) {
//                    rs = null;
//                }
//                if (stmt != null) {
//                    stmt = null;
//                }
//                if (con != null) {
//                    con = null;
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//        return value;
//    }
//
//    public int updateInsertStatus(Connection con_RDB, String updateQuery) {
//
//        Statement stmt = null;
//        int rowsUpdated = 0;
//        try {
//            if (!updateQuery.equalsIgnoreCase("")) {
//                stmt = con_RDB.createStatement();
//                rowsUpdated = stmt.executeUpdate(updateQuery);
////                System.out.println("rowsUpdated are : " + rowsUpdated);
//            } else {
//                System.out.println("No Update Query found..." + updateQuery);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (stmt != null) {
//                    stmt.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return rowsUpdated;
//    }
//}
