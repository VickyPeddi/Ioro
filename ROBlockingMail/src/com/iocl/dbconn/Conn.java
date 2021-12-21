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

  
 

    public static Connection getConRDB() {
        Connection conRDB = null;
        String JDBCurlRDB = "";
        String passRDB = "";
        String unameRDB = "";

        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
            /*live db*/ //
            conRDB = DriverManager.getConnection("jdbc:oracle:thin:@OTHORASCAN.DS.INDIANOIL.IN:1521/MKTORARACKDB", "RETAILDB", "retaildb#321");
            /*testing DB*/
            //  con = DriverManager.getConnection("jdbc:oracle:thin:@10.146.64.194/MKTORADB", "retaildb", "retaildb");

            System.out.println("RDB Database Connected");
        } catch (Exception ex) {
            System.out.print("Error in creating RDB database connection ---------------" + ex.getMessage());
        }
        return conRDB;
    }
}
