package com.iocl.dbconn;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;






public class Conn
{
  public Conn() {}
  
  public static Connection getConCisdev()
  {
    Connection con = null;
    FileInputStream fIs = null;FileInputStream fIs1 = null;
    Properties prop = new Properties();
    String DBLogin = "";
    String DBPass = "";
    String JDBCurl = "";
    String Driver = "";
    

    try
    {
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
    FileInputStream fIs = null;FileInputStream fIs1 = null;
    Properties prop = new Properties();
    String DBLogin = "";
    String DBPass = "";
    String JDBCurl = "";
    String Driver = "";
    

    try
    {
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
  
  public static Connection getCon() {
    Connection con = null;
    String JDBCurlRDB = "";
    String passRDB = "";
    String unameRDB = "";
    





    try
    {
      Class.forName("oracle.jdbc.driver.OracleDriver");
      

      con = DriverManager.getConnection("jdbc:oracle:thin:@OTHORASCAN.DS.INDIANOIL.IN/MKTORARACKDB", "CMSRO", "rocms@323");
      


      System.out.println("RDB Database Connected");
    } catch (Exception ex) {
      System.out.print("Error in creating RDB database connection ---------------" + ex.getMessage());
    }
    return con;
  }
  
  public static String getValue(String query)
  {
    Connection con = null;
    ResultSet rs = null;
    value = "";
    Statement stmt = null;
    
    try
    {
      if (con == null) {
        con = getCon();
      }
      
      stmt = con.createStatement(1005, 1007);
      
      rs = stmt.executeQuery(query);
      
      if (rs.next()) {
        value = rs.getString(1);
      }
      return "";
    }
    catch (Exception ex)
    {
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
  }
}
