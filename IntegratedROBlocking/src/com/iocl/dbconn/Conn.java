package com.iocl.dbconn;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class Conn {

  public Conn() {
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
        } catch (Exception ex) {
            System.out.print("Error in creating RDB database connection ---------------" + ex.getMessage());
        }
        return conRDB;
    }


  public static Connection getCon() {
    Connection con = null;
    String JDBCurlRDB = "";
    String passRDB = "";
    String unameRDB = "";

    try {
       App b = new App();
            String xmlString = b.getDetails("CMSRO");
            String s = null;
            con = DriverManager.getConnection("jdbc:oracle:thin:@" + xmlString.split(",")[0], xmlString.split(",")[1], xmlString.split(",")[2]);
      System.out.println("CMSRO Database Connected");
    } catch (Exception ex) {
      System.out.print("Error in creating RDB database connection ---------------" + ex.getMessage());
    }
    return con;
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
  
    public static String sendMail(String from, String to, String cc, String bcc, String sub, String body) {

    String host = "MKHOEXHT";//or IP address

    Properties properties = System.getProperties();
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.port", "25");

    Session session = Session.getDefaultInstance(properties, null);

    try {
      BodyPart messageBodyPart = new MimeBodyPart();
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(from));
      message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to.replaceAll(";", ","), true));
      message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc.replaceAll(";", ","), true));
      message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc.replaceAll(";", ","), true));
      message.setSubject(sub);
      messageBodyPart.setContent(body, "text/html; charset=utf-8");

      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(messageBodyPart);
      message.setContent(multipart);
      Transport.send(message);
//      System.out.print("Mail sent to :- " + to);
      //Mail Log
   return "Success";
    } catch (Exception mex) {
      System.out.println("ERROR: Sub:- " + sub + "Sent message unsuccessfully. ");
      mex.printStackTrace();
    }
    return "Error";

  }
}
