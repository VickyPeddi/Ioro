/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.dbconn;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Aug 19, 2017
 */
public class Conn {

    public static Connection getConCisdev() {
        Connection con = null;
        FileInputStream fIs = null, fIs1 = null;
        Properties prop = new Properties();
        String DBLogin = "";
        String DBPass = "";
        String JDBCurl = "";
        String Driver = "";

        try {

            //         fIs = new FileInputStream(".\\ConnCMSROASE.xml");
            fIs = new FileInputStream("C:\\snConfig\\ConnCISDEV.xml");
            prop.loadFromXML(fIs);

            DBLogin = prop.getProperty("login", DBLogin);
            DBPass = prop.getProperty("pass", DBPass);
            JDBCurl = prop.getProperty("JDBCurl", JDBCurl);
            Driver = prop.getProperty("Driver", Driver);
            Class.forName(Driver).newInstance();
            con = DriverManager.getConnection(JDBCurl, DBLogin, DBPass);
            System.out.println("CISDEV Database Connected");
        } catch (Exception e) {
            System.out.println("Conn Not Connected :: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    public static Connection getConIQ95() {
        Connection con = null;
        FileInputStream fIs = null, fIs1 = null;
        Properties prop = new Properties();
        String DBLogin = "";
        String DBPass = "";
        String JDBCurl = "";
        String Driver = "";

        try {

//            fIs = new FileInputStream(".\\ConnCMSROIQ95.xml");
            fIs = new FileInputStream("C:\\snConfig\\ConnCMSROIQ95.xml");

            prop.loadFromXML(fIs);

            DBLogin = prop.getProperty("login", DBLogin);
            DBPass = prop.getProperty("pass", DBPass);
            JDBCurl = prop.getProperty("JDBCurl", JDBCurl);
            Driver = prop.getProperty("Driver", Driver);
            Class.forName(Driver).newInstance();
            con = DriverManager.getConnection(JDBCurl, DBLogin, DBPass);
            System.out.println("CMSRO-IQ Database Connected");
        } catch (Exception e) {
            System.out.println("Conn Not Connected :: " + e.getMessage());
            e.printStackTrace();
        }
        return con;
    }

    public static Connection getConRDB() {
        Connection conRDB = null;
        String JDBCurlRDB = "";
        String passRDB = "";
        String unameRDB = "";

        try {
//            Properties p = new Properties();
//            p.load(new FileInputStream("C:\\snConfig\\RetailDashBoard.ini"));
//            unameRDB = p.getProperty("LoginRDB");
//            passRDB = p.getProperty("PassRDB");
//            JDBCurlRDB = p.getProperty("JDBCurlRDB");

            Class.forName("oracle.jdbc.driver.OracleDriver");
            /*live db*/ //
            conRDB = DriverManager.getConnection("jdbc:oracle:thin:@OTHORASCAN.DS.INDIANOIL.IN/MKTORARACKDB", "RETAILDB", "retaildb#321");
            /*testing DB*/
            //  con = DriverManager.getConnection("jdbc:oracle:thin:@10.146.64.194/MKTORADB", "retaildb", "retaildb");

            System.out.println("RDB Database Connected");
        } catch (Exception ex) {
            System.out.print("Error in creating RDB database connection ---------------" + ex.getMessage());
        }
        return conRDB;
    }
}
