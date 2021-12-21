/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.dbconn;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 *
 * @author Rohit Vyas <rohitvyas0312@gmail.com>
 * Create Date : Aug 19, 2017
 */
public class Conn {

    public static Connection getCon() {
        Connection conRDB = null;
        try {
           App b = new App();
            String xmlString = b.getDetails("CMSRO");
            String s = null;
            conRDB = DriverManager.getConnection("jdbc:oracle:thin:@" + xmlString.split(",")[0], xmlString.split(",")[1], xmlString.split(",")[2]);
           
            System.out.println("CMSRO Database Connected");
        } catch (Exception ex) {
            System.out.print("Error in creating CMSRO database connection ---------------" + ex.getMessage());
        }
        return conRDB;
    }

    public static Connection getConRDB() {
        Connection conRDB = null;
        String JDBCurlRDB = "";
        String passRDB = "";
        String unameRDB = "";

        try {
            App b = new App();
            String xmlString = b.getDetails("RETAILDB");
            String s = null;
            conRDB = DriverManager.getConnection("jdbc:oracle:thin:@" + xmlString.split(",")[0], xmlString.split(",")[1], xmlString.split(",")[2]);
            System.out.println("CMSRO Database Connected");
        } catch (Exception ex) {
            System.out.print("Error in creating RDB database connection ---------------" + ex.getMessage());
        }
        return conRDB;
    }

    public static String getValue(String query) {

        Connection con = null;
        ResultSet rs = null;
        String value = "";
        Statement stmt = null;

        try {

            if (con == null) {
                con = Conn.getCon();
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
}
