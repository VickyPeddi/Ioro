/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.rdb_blocking_mail;

import com.iocl.dbconn.Conn;
import static com.iocl.util.Util.sendMail;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author 00501310
 * Create Date : Dec 07, 2018
 */
public class ROBlockingDataMail {

    Connection con = null;
    Connection con1 = null;
    Connection con2 = null;
    Connection con3 = null;
    ResultSet rs = null;
    Statement stm = null;
    String query = "";
    ResultSet rs1 = null;
    Statement stm1 = null;
    String query1 = "";
    ResultSet rs2 = null;
    Statement stm2 = null;
    String query2 = "";
    Statement stm3 = null;
    String query3 = "";
    ResultSet rs4 = null;
    Statement stm4 = null;
    String query4 = "";
    String name = "";
    String desg = "";
    String SAName = "";
    String SACode = "";
    String rocode = "";
    String roname = "";
    String prod_code = "";
    String prod_name = "";
    String eff_date = "";
    String eff_till = "";
    String type = "";
    String rate = "";
    String docode = "";
    String salesarea = "";
    String do_code = "";
//    String body = "";
    String table = "";
    String ftable = "";
    String ftable1 = "";
    String ftable2 = "";
    String bcc = "";//"mounisharoy@indianoil.in ,MALTIJOSHI@indianoil.in ,rohitvyas0312@gmail.com";
    String subject = "";
    String sender = "retaildashboard@INDIANOIL.IN";//retaildashboard@INDIANOIL.IN
    String cc = "";
    String temp = "";
    String SA = "";
    String DO = "";
    int count = 1;
    int a = 0;
    int insRow = 0;
    int insRow1 = 0;
    Connection conRDB = null;

    public static void main(String[] args) {
        ROBlockingDataMail obj = new ROBlockingDataMail();
//    try {
//      // obj.maildealer();
//    } catch (Exception e) {
//      System.out.println("Exception occurred while Mailing to Dealer...");
//      e.printStackTrace();
//    }
        try {
            obj.mailFO();
        } catch (Exception e) {
            System.out.println("Exception occurred while Mailing to FO...");
            e.printStackTrace();
        }
        try {
//            obj.mailDRSM();
        } catch (Exception e) {
            System.out.println("Exception occurred while Mailing to FO...");
            e.printStackTrace();
        }
    }

    public void mailFO() throws SQLException {

        Connection con_RDB = null;
        ResultSet rs = null, rs1 = null;
        Statement stm = null, stm1 = null, stm2 = null;
//        StringBuffer SA = new StringBuffer();
        String SA1 = "", SA2 = "";
        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            java.sql.Date myDate = new java.sql.Date(System.currentTimeMillis());
            System.out.println("Today's Date : " + dateFormat.format(myDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            cal.add(Calendar.DATE, -1);
            System.out.println("Yesterday's Date : " + dateFormat.format(cal.getTime()));
            con_RDB = Conn.getConRDB();

            try {
                // Identify the salesarea and mail ID of FO .
                String emailID_fo = "";
                String table_data = "";
                String body = "<html><body style='font-size: 90%;'> <td >Dear Sir/s, </td><br><br>The following RO have been assigned CMS block for reasons as stated under:"
                        + "<br> <br><br>"
                        + "<table cellspacing='0' style='font-size: 85%;border: 1px solid black'><tr Style='background-color:silver'>"
                        + "<td style='border: 1px solid black'>SO</td>"
                        + "<td style='border: 1px solid black'>DO</td>"
                        + "<td style='border: 1px solid black'>SA</td>"
                        + "<td style='border: 1px solid black'>RO Code</td>"
                        + "<td style='border: 1px solid black'>Customer Name</td>"
                        + "<td style='border: 1px solid black'>Phase</td>"
                        + "<td style='border: 1px solid black'>Blocked Due to</td>"
                        + "<td style='border: 1px solid black'>Blocked Date & Time</td>"
                        + "<td style='border: 1px solid black'>Blocking Message</td>"
                        + "</tr>";
                String body_trail = "</table><br><br><p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Regards,<u></u><u></u></span></p>"
                        + "<p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">RDB Admin<u></u><u></u></span></p>"
                        + "<p><strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Retail Sales, HO</span></strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\"><u></u><u></u></span></p>";
                stm = con_RDB.createStatement();
                String query = "SELECT * FROM (SELECT c.SALESORG_NAME ,\n"
                        + "  c.SALESOFF_NAME      ,\n"
                        + "  c.SALESAREA_NAME     ,\n"
                        + "  c.custcode          ,\n"
                        + "  c.CUST_NAME          ,\n"
                        + "  CASE\n"
                        + "    WHEN c.AUTOMATED='A'\n"
                        + "    THEN 'Automated'\n"
                        + "    ELSE 'Non-Automated'\n"
                        + "  END                  Phase,\n"
                        + "   REPLACE(REPLACE( a.sms_text , 'Indenting for RO has been blocked due to ', '')\n"
                        + ",' Please update through e-ledger or xSparsh to place your indent.','')  BLOCKED_DUE_TO,\n"
                        + " TO_CHAR( a.block_conf_datetime,'dd-mm-yy hh:mi am') blocked_date,\n"
                        + "a.sms_text AS MESSAGE, \n"
                        + "  C.SALESAREA,\n"
                        + "  C.FO_EMAIL\n"
                        + "FROM\n"
                        + "  (SELECT custcode,\n"
                        + "    sms_text,\n"
                        + "    block_id,\n"
                        + "    block_attempt_no,\n"
                        + "    sap_block_status,\n"
                        + "    block_flag,\n"
                        + "    block_date,\n"
                        + "    create_datetime,\n"
                        + "    block_conf_datetime,\n"
                        + "    sap_block_msg\n"
                        + "  FROM CMSRO.SAP_BLOCK_CUSTOMERS\n"
                        + "  WHERE  block_flag ='B'\n"
                        + "  ) a\n"
                        + "LEFT JOIN\n"
                        + "  (SELECT CUSTCODE,\n"
                        + "    BLOCK_ID,\n"
                        + "    LISTAGG(CUSTOMER_BLOCK_MSG, ',') WITHIN GROUP (\n"
                        + "  ORDER BY BLOCK_REF_ID) AS CUSTOMER_BLOCK_MSG,\n"
                        + "    LISTAGG(BLOCK_REF_ID, ',') WITHIN GROUP (\n"
                        + "  ORDER BY BLOCK_REF_ID) AS CUSTOMER_BLOCK_REF_ID\n"
                        + "  FROM CMSRO.SAP_BLOCK_CUSTOMER_DETAILS\n"
                        + "  WHERE BLOCK_FLAG='Y'\n"
                        + "  GROUP BY CUSTCODE,\n"
                        + "    BLOCK_ID\n"
                        + "  )b\n"
                        + "ON a.block_id=b.block_id\n"
                        + "LEFT JOIN RETAILDB.VW_GET_RO_FO_DRSM_SRH c\n"
                        + "ON a.custcode=c.custcode\n"
                        + " WHERE b.custcode  IS NOT NULL \n"
                        + " ORDER BY C.SALESAREA,\n"
                        + " phase ) where message is not null and FO_EMAIL <> 'NA'";
                //   System.out.println(query2);

                rs = stm.executeQuery(query);
                subject = "RO Supply Blocking";
                int count = 0;
                int first = 0;
                while (rs.next()) {
                    if (first == 0) {
                        SA2 = rs.getString("salesarea");
                    }
                    first++;
                    SA1 = rs.getString("salesarea");

                    if (!SA1.equals(SA2)) {
//            if (count == 4) {
//              break;
//            } else {
//              count++;
//            }
                        System.out.println("Mail ID:- " + emailID_fo);
                        String response = sendMail(sender, emailID_fo, cc, bcc, subject, body + table_data + body_trail);

                        ROBlockingDataMail obj = new ROBlockingDataMail();
                        int insertedRows = obj.updateInsertStatus(con_RDB, "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + SA1 + "'|| to_char(sysdate,'yyyymmddhh24miss'),'" + sender + "','BLOCKED RO EMAIL','" + emailID_fo + "','" + response + "',SYSDATE,'FO')");
                        System.out.println("MAIL Details Inserted: " + insertedRows);
                        table_data = "";
                        SA2 = SA1;
                        if (response.equalsIgnoreCase("Success")) {
                            System.out.println("Mail Sent Successfully...To:SalesArea: " + SACode);
                        } else {
                            System.out.println("Mail Sending Failed...To:SalesArea: " + SACode);
                        }
                    }
                    emailID_fo = rs.getString("fo_email");

                    table_data += "<tr>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("SALESORG_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("SALESOFF_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("SALESAREA_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("custcode") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("CUST_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("Phase") + "</td>"
                            + "<td style='font-size:10.0pt;color: #FF3342;border: 1px solid black;'>" + rs.getString("Blocked_Due_To") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("blocked_date") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("message") + "</td>"
                            + "</tr>";
                }
                if (!SA1.equals(SA2)) {

                    System.out.println("Mail ID:- " + emailID_fo);
                    String response = sendMail(sender, emailID_fo, cc, bcc, subject, body + table_data + body_trail);

                    ROBlockingDataMail obj = new ROBlockingDataMail();
                    int insertedRows = obj.updateInsertStatus(con_RDB, "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + SA1 + "'|| to_char(sysdate,'yyyymmddhh24miss'),'" + sender + "','BLOCKED RO EMAIL','" + emailID_fo + "','" + response + "',SYSDATE,'FO')");
                    System.out.println("MAIL Details Inserted: " + insertedRows);
                    table_data = "";
                    SA2 = SA1;
                    SACode = SA2;
                    if (response.equalsIgnoreCase("Success")) {
                        System.out.println("Mail Sent Successfully...To:SalesArea: " + SACode);
                    } else {
                        System.out.println("Mail Sending Failed...To:SalesArea: " + SACode);
                    }
                }
            } catch (Exception ee) {
                System.out.println(ee);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            }

        } catch (Exception ee) {
            System.out.println(ee);
        } finally {
            if (con_RDB != null) {
                con_RDB.close();
            }

        }
    }

    public void mailDRSM() throws SQLException {

        Connection con_RDB = null;
        ResultSet rs = null, rs1 = null;
        Statement stm = null, stm1 = null, stm2 = null;
//        StringBuffer SA = new StringBuffer();
        String SalesOff1 = "", SalesOff2 = "";
        try {

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            java.sql.Date myDate = new java.sql.Date(System.currentTimeMillis());
            System.out.println("Today's Date : " + dateFormat.format(myDate));
            Calendar cal = Calendar.getInstance();
            cal.setTime(myDate);
            cal.add(Calendar.DATE, -1);
            System.out.println("Yesterday's Date : " + dateFormat.format(cal.getTime()));
            con_RDB = Conn.getConRDB();

            try {
                // Identify the salesarea and mail ID of FO .
                String emailID_drsm = "";
                String table_data = "";
                String body = "<html><body style='font-size: 90%;'> <td >Dear Sir/s, </td><br><br>The following RO have been assigned CMS block for reasons as stated under:"
                        + "<br> <br><br>"
                        + "<table cellspacing='0' style='font-size: 85%;border: 1px solid black'><tr Style='background-color:silver'>"
                        + "<td style='border: 1px solid black'>SO</td>"
                        + "<td style='border: 1px solid black'>DO</td>"
                        + "<td style='border: 1px solid black'>SA</td>"
                        + "<td style='border: 1px solid black'>RO Code</td>"
                        + "<td style='border: 1px solid black'>Customer Name</td>"
                        + "<td style='border: 1px solid black'>Phase</td>"
                        + "<td style='border: 1px solid black'>Blocked Due to</td>"
                        + "<td style='border: 1px solid black'>Blocked Date & Time</td>"
                        + "<td style='border: 1px solid black'>Last Date when EOD received</td>"
                        + "<td style='border: 1px solid black'>Last Date when Tank- Nozzle Sales Matched</td>"
                        + "<td style='border: 1px solid black'>Last Date when Stock Received</td>"
                        + "<td style='border: 1px solid black'>Last Blocked on</td>"
                        + "<td style='border: 1px solid black'>e-RACTS complaint available for Non-receipt of EOD</td>"
                        + "<td style='border: 1px solid black'>Number of instances on account of CMS this month</td>"
                        + "<td style='border: 1px solid black'>e-RACTS complaint available for Tank Nozzle Sales Mismatch</td>"
                        + "<td style='border: 1px solid black'>RO in Valid Exception List</td>"
                        + "<td style='border: 1px solid black'>No. Of days RO in Exception List</td>"
                        + "<td style='border: 1px solid black'>Blocking Message</td>"
                        + "</tr>";
                String body_trail = "</table><br><br><p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Regards,<u></u><u></u></span></p>"
                        + "<p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">RDB Admin<u></u><u></u></span></p>"
                        + "<p><strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Retail Sales, HO</span></strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\"><u></u><u></u></span></p>";
                stm = con_RDB.createStatement();

                //modify on 10/12/2018
                String query = "SELECT * FROM (SELECT c.SALESORG_NAME ,\n"
                        + "  c.SALESOFF_NAME      ,\n"
                        + "  c.SALESAREA_NAME     ,\n"
                        + "  c.custcode          ,\n"
                        + "  c.CUST_NAME          ,\n"
                        + "  CASE\n"
                        + "    WHEN c.AUTOMATED='A'\n"
                        + "    THEN 'Automated'\n"
                        + "    ELSE 'Non-Automated'\n"
                        + "  END                  Phase,\n"
                        + "   REPLACE(REPLACE( a.sms_text , 'Indenting for RO has been blocked due to ', '')\n"
                        + ",' Please update through e-ledger or xSparsh to place your indent.','')  BLOCKED_DUE_TO,\n"
                        + "  TO_CHAR( a.block_conf_datetime,'dd-mm-yy hh:mi am') blocked_date ,\n"
                        + " NVL(TO_CHAR(f.EOD_RECVD_ON,'DD-MM-YY HH:MI AM '),'NA')       AS MAX_FILERCPT_DATE,\n"
                        + " NVL(TO_CHAR(LAST_TANK_NOZZLE_MATCH,'dd-MM-yy'),'NA')         AS last_tank_match_date,\n"
                        + "  NVL(TO_CHAR(to_date(stock_date,'yyyymmdd'),'dd-MM-yy'),'NA') AS last_stock_date,\n"
                        + "  NVL(TO_CHAR(LAST_CMS_BLOCK_DATE,'dd-MM-yy'),'NA')            AS last_cms_block_date,\n"
                        + " NVL( eod_exempt ,'NA')   AS eod_exempt  ,\n"
                        + "   NVL( tank_exempt ,'NA')   AS tank_exempt ,\n"
                        + "    case when CMS_EXCEPTION_FM_DT is not null then 'Yes' else 'No' end AS CMS_EXCEPTION_FM_DT,\n"
                        + " NVL(TO_CHAR(EOD_EXEMPT_DAYS),'NA')  EOD_EXEMPT_DAYS ,\n"
                        + "a.sms_text AS MESSAGE, \n"
                        + "CASE WHEN INSTR(B.CUSTOMER_BLOCK_REF_ID,'3',1,1)<>0 then '1'\n"
                        + "WHEN INSTR(B.CUSTOMER_BLOCK_REF_ID,'2',1,1)<>0 then '1'\n"
                        + "else 'NA' END cms_blocked_count,\n"
                        + "  C.SALESOFF,\n"
                        + "  C.DRSM_EMAIL\n"
                        + "FROM\n"
                        + "  (SELECT custcode,\n"
                        + "    sms_text,\n"
                        + "    block_id,\n"
                        + "    block_attempt_no,\n"
                        + "    sap_block_status,\n"
                        + "    block_flag,\n"
                        + "    block_date,\n"
                        + "    create_datetime,\n"
                        + "    block_conf_datetime,\n"
                        + "    sap_block_msg\n"
                        + "  FROM CMSRO.SAP_BLOCK_CUSTOMERS\n"
                        + "  WHERE  block_flag ='B'\n"
                        + "  ) a\n"
                        + "LEFT JOIN\n"
                        + "  (SELECT CUSTCODE,\n"
                        + "    BLOCK_ID,\n"
                        + "    LISTAGG(CUSTOMER_BLOCK_MSG, ',') WITHIN GROUP (\n"
                        + "  ORDER BY BLOCK_REF_ID) AS CUSTOMER_BLOCK_MSG,\n"
                        + "    LISTAGG(BLOCK_REF_ID, ',') WITHIN GROUP (\n"
                        + "  ORDER BY BLOCK_REF_ID) AS CUSTOMER_BLOCK_REF_ID\n"
                        + "  FROM CMSRO.SAP_BLOCK_CUSTOMER_DETAILS\n"
                        + "  WHERE BLOCK_FLAG='Y'\n"
                        + "  GROUP BY CUSTCODE,\n"
                        + "    BLOCK_ID\n"
                        + "  )b\n"
                        + "ON a.block_id=b.block_id\n"
                        + "LEFT JOIN RETAILDB.VW_GET_RO_FO_DRSM_SRH c\n"
                        + "ON a.custcode=c.custcode\n"
                        + "LEFT OUTER JOIN\n"
                        + "  (SELECT TO_CHAR(ro_code) AS ro_code,\n"
                        + "    CASE\n"
                        + "      WHEN eod_gap_days     =3\n"
                        + "      AND eracts_eod_exempt ='N'\n"
                        + "      AND FINAL_BLOCK_STATUS='Y'\n"
                        + "      THEN 'EOD Not received for 3 or more days'\n"
                        + "      WHEN tank_nozzle_status ='N'\n"
                        + "      AND eracts_tank_exempt  ='N'\n"
                        + "      AND FINAL_BLOCK_STATUS  ='Y'\n"
                        + "      THEN 'Tank Nozzle Sales Mismatch for 3 days'\n"
                        + "    END               AS block_reason,\n"
                        + "    TO_CHAR(ra_phase) AS ra_phase,\n"
                        + "    MAX_FILERCPT_DATE,\n"
                        + "    CASE\n"
                        + "      WHEN eracts_eod_exempt='Y'\n"
                        + "      THEN 'Yes'\n"
                        + "      ELSE 'No'\n"
                        + "    END AS eod_exempt,\n"
                        + "    CASE\n"
                        + "      WHEN eracts_tank_exempt='Y'\n"
                        + "      THEN 'Yes'\n"
                        + "      ELSE 'No'\n"
                        + "    END AS tank_exempt,\n"
                        + "    eod_gap_days,\n"
                        + "    tank_nozzle_status,\n"
                        + "    CMS_EXCEPTION_FM_DT,\n"
                        + "    LAST_TANK_NOZZLE_MATCH,\n"
                        + "    LAST_CMS_BLOCK_DATE ,\n"
                        + "    (TRUNC(process_date)-TRUNC(CMS_EXCEPTION_FM_DT)) EOD_EXEMPT_DAYS,\n"
                        + "    process_date\n"
                        + "  FROM cmsro.cms_blocking_details \n"
                        + "  ) d\n"
                        + "ON a.custcode                              =d.ro_code\n"
                        + "AND TO_CHAR(a.create_datetime-1,'yyyymmdd')=TO_CHAR(d.process_date,'yyyymmdd')\n"
                        + "LEFT OUTER JOIN\n"
                        + "  (SELECT custcode,\n"
                        + "    MAX(stock_date) AS stock_date\n"
                        + "  FROM TBL_MANUAL_AUTO_STOCK \n"
                        + "  GROUP BY custcode\n"
                        + "  ) e\n"
                        + "ON a.custcode =e.custcode\n"
                        + "LEFT JOIN CMSRO.CMS_EOD_INFO f\n"
                        + "ON a.custcode                              =f.ro_code\n"
                        + "AND TO_CHAR(a.create_datetime-1,'yyyymmdd')=TO_CHAR(f.eod_date,'yyyymmdd')\n"
                        + " WHERE b.custcode                          IS NOT NULL \n"
                        + "                                             ORDER BY C.SALESAREA,\n"
                        + "                                              phase ) where message is not null and DRSM_EMAIL is not null";
                rs = stm.executeQuery(query);
//        if (rs.next()) {
//          SA2 = rs.getString("salesarea");
//        }
//        rs.first();
                subject = "RO Supply Blocking";
                int count = 0;
                int first = 0;
                while (rs.next()) {
                    if (first == 0) {
                        SalesOff2 = rs.getString("SALESOFF");
                    }
                    first++;
                    SalesOff1 = rs.getString("SALESOFF");

                    if (!SalesOff1.equals(SalesOff2)) {
//            if (count == 4) {
//              break;
//            } else {
//              count++;
//            }
                        System.out.println("Mail ID:- " + emailID_drsm);
                        String response = sendMail(sender, emailID_drsm, cc, bcc, subject, body + table_data + body_trail);

                        ROBlockingDataMail obj = new ROBlockingDataMail();
                        int insertedRows = obj.updateInsertStatus(con_RDB, "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + SalesOff1 + "'|| to_char(sysdate,'yyyymmddhh24miss'),'" + sender + "','BLOCKED RO EMAIL','" + emailID_drsm + "','" + response + "',SYSDATE,'DRSM')");
                        System.out.println("MAIL Details Inserted: " + insertedRows);
                        table_data = "";
                        SalesOff2 = SalesOff1;
                        SACode = SalesOff2;
                        if (response.equalsIgnoreCase("Success")) {
                            System.out.println("Mail Sent Successfully...To:SalesArea: " + SACode);
                        } else {
                            System.out.println("Mail Sending Failed...To:SalesArea: " + SACode);
                        }
                    }
                    emailID_drsm = rs.getString("DRSM_EMAIL");

                    table_data += "<tr>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("SALESORG_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("SALESOFF_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("SALESAREA_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("custcode") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("CUST_NAME") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("Phase") + "</td>"
                            + "<td style='font-size:10.0pt;color: #FF3342;border: 1px solid black;'>" + rs.getString("Blocked_Due_To") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("blocked_date") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("MAX_FILERCPT_DATE") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("last_tank_match_date") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("last_stock_date") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("last_cms_block_date") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("eod_exempt") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("cms_blocked_count") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("tank_exempt") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("CMS_EXCEPTION_FM_DT") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("EOD_EXEMPT_DAYS") + "</td>"
                            + "<td style='font-size:10.0pt;color: #1f497d;border: 1px solid black;'>" + rs.getString("message") + "</td>"
                            + "</tr>";
                }
                if (!SalesOff1.equals(SalesOff2)) {
                    System.out.println("Mail ID:- " + emailID_drsm);
                    String response = sendMail(sender, emailID_drsm, cc, bcc, subject, body + table_data + body_trail);

                    ROBlockingDataMail obj = new ROBlockingDataMail();
                    int insertedRows = obj.updateInsertStatus(con_RDB, "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + SalesOff1 + "'|| to_char(sysdate,'yyyymmddhh24miss'),'" + sender + "','BLOCKED RO EMAIL','" + emailID_drsm + "','" + response + "',SYSDATE,'DRSM')");
                    System.out.println("MAIL Details Inserted: " + insertedRows);
                    table_data = "";
                    SalesOff2 = SalesOff1;
                    SACode = SalesOff2;
                    if (response.equalsIgnoreCase("Success")) {
                        System.out.println("Mail Sent Successfully...To:SalesArea: " + SACode);
                    } else {
                        System.out.println("Mail Sending Failed...To:SalesArea: " + SACode);
                    }
                }
            } catch (Exception ee) {
                System.out.println(ee);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stm != null) {
                    stm.close();
                }
            }

        } catch (Exception ee) {
            System.out.println(ee);
        } finally {
            if (con_RDB != null) {
                con_RDB.close();
            }

        }
    }

//  public void maildealer() throws SQLException {
//    Conn con = new Conn();
//    Connection con_RDB = null;
//    con_RDB = Conn.getConRDB();
//    ResultSet rs = null, rs1 = null;
//    Statement stm = null, stm1 = null, stm2 = null;
//    StringBuffer SA = new StringBuffer();
//    try {
//      String emailID = "";
//      String blockID = "";
//      DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//      java.sql.Date myDate = new java.sql.Date(System.currentTimeMillis());
//      System.out.println("Today's Date : " + dateFormat.format(myDate));
//      Calendar cal = Calendar.getInstance();
//      cal.setTime(myDate);
//
//      cal.add(Calendar.DATE, -1);
//      int month = cal.get(Calendar.MONTH);
////            String period = getMonthForInt(month);
//      System.out.println("Yesterday's Date : " + dateFormat.format(cal.getTime()));
//      boolean mailFlag = false;
//      try {
//
//        stm = con_RDB.createStatement();
//
////                query = "SELECT  B.*, A.block_id as block_id FROM \n" + "(select distinct custcode,  block_id  from cmsro.SAP_BLOCK_RO_DETAILS where SAP_BLOCK_STATUS ='S') A\n" + "LEFT OUTER JOIN \n" + "(select custcode , email ,  vender_code  from  IOCL_MD_AOD_SALES) B\n" + "ON  A.CUSTCODE=B.CUSTCODE and  vender_code is not null and rownum=10";
//        query = "SELECT a.custcode as custcode, a.block_id as block_id,\n"
//                + "  b.email as email\n"
//                + "FROM\n"
//                + "  (SELECT *\n"
//                + "  FROM cmsro.SAP_BLOCK_RO_DETAILS\n"
//                + "  WHERE sap_block_status='S'\n"
//                + "  AND sap_block_curr    ='B'\n"
//                + "  ) a\n"
//                + "LEFT JOIN IOCL_MD_AOD_SALES b\n"
//                + "ON a.custcode=b.custcode\n"
//                + "WHERE email IS NOT NULL ";
//        rs = stm.executeQuery(query);
//        int bccCount = 0;
//        while (rs.next()) {
//          bccCount++;
//          String ro_code = rs.getString("custcode");
//          emailID = rs.getString("email");
//          blockID = rs.getString("block_id");
////                    String vendor = rs.getString("vender_code");
//
//          try {
//
//            query1 = "select CUSTCODE , CUST_NAME  , SALESAREA_NAME  , TELF1 from IOCL_MD_AOD_SALES where CUSTCODE= '" + ro_code + "'\n";
//
//            stm1 = con_RDB.createStatement();
//            System.out.println(query1);
//            rs1 = stm1.executeQuery(query1);
//            StringBuffer missingDates = null;
//            while (rs1.next()) {
//
//              String body = "<html><body style='font-size: 90%;'><br><br> <p class=\"MsoNormal\" style=\"margin-bottom:12.0pt\"><b><span lang=\"EN-US\" style=\"font-size:18.0pt;color:#1f497d\">Dear Channel Partner,\n"
//                      + "<br>\n"
//                      + "<br>\n"
//                      + "Indenting for your RO &nbsp;has been blocked &nbsp;since stock updation is pending &nbsp;as per following details . Please update the stock through e-ledger or xSparsh and then place your indent.<u></u><u></u></span></b></p>"
//                      + "<table cellspacing='0' style='font-size: 85%;border: 1px solid black'><tr Style='background-color:#9bbb59'>"
//                      //     + "<td style='border: 1px solid black'>SA.</td>"
//                      + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>RO CODE</td>"
//                      + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>RO NAME</td>"
//                      //    + "<td style='border: 1px solid black'>RO CONTACT NUMBER</td>"
//                      + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;border: 1px solid black;'>MS STOCK NOT UPDATED FOR</td>"
//                      + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>HSD STOCK NOT UPDATED FOR</td>"
//                      + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>XM STOCK NOT UPDATED FOR</td>"
//                      + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>XP STOCK NOT UPDATED FOR</td>"
//                      + "</tr>";
//              String table = "";
//              missingDates = new StringBuffer();
//              String custCode = rs1.getString("CUSTCODE");
//              String custName = rs1.getString("CUST_NAME");
//              String salesAreaName = rs1.getString("SALESAREA_NAME");
//              String telf1 = rs1.getString("TELF1");
//
//              String getMissingDates = "";// "WITH all_dates AS ( SELECT TO_DATE(sysdate+1) - ROWNUM  AS missing_date\n"                                    + "FROM dual\n"                                    + "CONNECT BY ROWNUM <= add_MONTHS(TO_DATE(sysdate),0 ) - TO_DATE(sysdate-8)\n"                                    + ")\n"                                    + "SELECT to_char(all_dates.missing_date,'dd-MON-yyyy') as missing_date,a.prod_code\n"                                    + "FROM all_dates\n"                                    + "LEFT JOIN (select tran_date,prod_code from cms_manual_ro_stk where ro_code='" + custCode + "') a ON ( to_date(a.tran_date,'yyyymmdd') = all_dates.missing_date )\n"                                    + "WHERE a.tran_date IS NULL ORDER BY all_dates.missing_date ";
//              getMissingDates = "WITH all_dates AS ( SELECT TO_DATE(sysdate) - ROWNUM  AS missing_date\n"
//                      + "  FROM dual\n"
//                      + "                CONNECT BY ROWNUM <= add_MONTHS(TO_DATE(sysdate),0 ) - TO_DATE(sysdate-7)\n"
//                      + "  )\n"
//                      + "  SELECT to_char(all_dates.missing_date,'dd-MON-yyyy') as missing_date,a.prod_code\n"
//                      + "  FROM all_dates\n"
//                      + "  LEFT JOIN (select tran_date,prod_code from retaildb.cms_manual_ro_stk where ro_code='" + custCode + "'\n"
//                      + "  UNION\n"
//                      + "        SELECT stk_date, PROD_GROUP as prod_code FROM cmsro.cms_blocking_stk_details WHERE stk_date>TO_CHAR(to_date(sysdate-9,'dd/mm/yy'),'yyyymmdd') and ro_code='" + custCode + "'\n"
//                      + "  ) a ON ( to_date(a.tran_date,'yyyymmdd') = all_dates.missing_date )\n"
//                      + "  WHERE a.tran_date IS NULL ORDER BY all_dates.missing_date ";
//              try {
//                stm2 = con_RDB.createStatement();
//                rs2 = stm2.executeQuery(getMissingDates);
//                int count = 0;
//                while (rs2.next()) {
//                  mailFlag = true;
//                  if (count != 0) {
//                    missingDates.append(",");
//                  }
//                  missingDates.append("'" + rs2.getString("missing_date").toString() + "'");
//                  count++;
//                }
////                                if (!missingDates.toString().equalsIgnoreCase("")) {
////                                    missingDates.append(missingDates.toString().substring(1));
////                                }
//              } catch (Exception e) {
//                e.printStackTrace();
//              } finally {
//                try {
//                  if (rs2 != null) {
//                    rs2.close();
//                  }
//                } catch (Exception e) {
//                }
//                try {
//                  if (stm2 != null) {
//                    stm2.close();
//                  }
//                } catch (Exception e) {
//                }
//              }
//
//              if (!missingDates.toString().equalsIgnoreCase("")) {
//                String dates = missingDates.toString().replaceAll("'", "");
//                table = "<tr>"
//                        //+ "<td style='border: 1px solid black'>" + rs1.getString("SALESAREA_NAME") + "</td>"
//                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + rs1.getString("CUSTCODE") + "</td>"
//                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + rs1.getString("CUST_NAME") + "</td>"
//                        //  + "<td style='border: 1px solid black'>" + rs1.getString("TELF1") + "</td>"
//                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                        + "<td style='font-size:18.0pt;color: #1f497d;border: 1px solid black;'>" + dates + "</td>"
//                        + "</tr>";
//              }
//              if (mailFlag) {
//                String footer = "<p class=\"MsoNormal\"><b><span lang=\"EN-US\" style=\"font-size:18.0pt;color:#1f497d\"><u></u>&nbsp;<u></u></span></b></p>"
//                        + "<p class=\"MsoNormal\"><b><span lang=\"EN-US\" style=\"font-size:18.0pt;color:#1f497d\"><u></u>&nbsp;<u></u></span></b></p>"
//                        + "<p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Thanking you,<u></u><u></u></span></p>"
//                        + "<p><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Yours faithfully,<u></u><u></u></span></p>"
//                        + "<p><strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Retail Sales</span></strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\"><u></u><u></u></span></p>"
//                        + "<p><strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">IndianOil Corporation Limited (Marketing Division)</span></strong><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\"><u></u><u></u></span></p>"
//                        + "<p class=\"MsoNormal\" style=\"margin-bottom:12.0pt\"><span lang=\"EN-US\" style=\"font-size:13.5pt;color:#002060\">Disclaimer :\n"
//                        + "<i>This is a system generated mail, hence no signature is required. Do not reply to this mail.</i></span><span lang=\"EN-US\"><u></u><u></u></span></p>";
//                body = body + " " + table + " </table> " + footer + " </body></html> <br><b>";
//                //+ "You can access the required report  in by clicking on this hyperlink. <a href='https://spandan.indianoil.co.in/RetailNew/' target='_blank'>RetailNew</a><br>System Administrator <br>\n" + "";
//                System.out.println("body\n" + body);
//                subject = "Non updation of Retail outlet ";
////                                bcc = "";
////                                if (bccCount < 6) {
////                                    bcc = "mounisharoy@indianoil.in";
////                                }
//                String response = sendMail(sender, emailID, cc, bcc, subject, body);
//                if (response.equalsIgnoreCase("Success")) {
//                  System.out.println("Mail Sent Successfully...To:Dealer: " + custCode);
//                } else {
//                  System.out.println("Mail Sending Failed...To:Dealer: " + custCode);
//                }
//
//                ROBlockingDataMail_bkup obj = new ROBlockingDataMail_bkup();
//                String insertMailDetail = "";
//
//                insertMailDetail = "INSERT INTO CMSRO.TBL_MAIL_LOG (BLOCK_ID,MAIL_ID,MAIL_REMARKS,MAIL_TO,STATUS,UPDATE_DATETIME,MAIL_TYPE) values ('" + blockID + "','" + sender + "','BLOCKED RO EMAIL','" + emailID + "','" + response + "',SYSDATE,'DEALER')";
//                int insertedRows = obj.updateInsertStatus(con_RDB, insertMailDetail);
//                System.out.println("MAIL Details Inserted: " + insertedRows + "\n" + insertMailDetail);
//              }
//              mailFlag = false;
//            }
//          } catch (Exception ee) {
//            ee.printStackTrace();
//            System.out.println(ee);
//          } finally {
//            if (rs1 != null) {
//              rs1.close();
//            }
//            if (stm1 != null) {
//              stm1.close();
//            }
//
//          }
//
//        }
//
//      } catch (Exception e) {
//        System.out.println(e);
//      } finally {
//        if (rs != null) {
//          rs.close();
//        }
//        if (stm != null) {
//          stm.close();
//        }
//
//      }
//
//    } catch (Exception ee) {
//      System.out.println(ee);
//    } finally {
//      if (con_RDB != null) {
//        con_RDB.close();
//      }
//      if (con1 != null) {
//        con1.close();
//      }
//      if (con3 != null) {
//        con3.close();
//      }
//    }
//  }
    public static String getValue(String query) {

        Connection con = null;
        ResultSet rs = null;
        String value = "";
        Statement stmt = null;

        try {

            if (con == null) {
                con = Conn.getConRDB();
            }

            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery(query);

            if (rs.next()) {
                value = rs.getString(1);
            } else {
                value = "";
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs = null;
                }
                if (stmt != null) {
                    stmt = null;
                }
                if (con != null) {
                    con = null;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return value;
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
}
