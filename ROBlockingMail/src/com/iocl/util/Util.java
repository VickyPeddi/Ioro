/*
 * Copyright @ IndianOil Corporation Ltd.
 * All rights reserved.
 */
package com.iocl.util;

import com.iocl.rdb_blocking_mail.ROBlockingDataMail;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author 00501310
 * Create Date : Aug 16, 2017
 */
public class Util {

  static String CMS_STK_BLOCK_MSG = "";
  static String CMS_BLOCK_MSG = "";
  static String STK_BLOCK_MSG = "";

  public Util() {
    ROBlockingDataMail blockingObj = new ROBlockingDataMail();
    CMS_STK_BLOCK_MSG = blockingObj.getValue("SELECT message_content FROM tbl_message WHERE message_id=1 AND message_type='BLOCKING'");
    CMS_BLOCK_MSG = blockingObj.getValue("SELECT message_content FROM tbl_message WHERE message_id=2 AND message_type='BLOCKING'");
    STK_BLOCK_MSG = blockingObj.getValue("SELECT message_content FROM tbl_message WHERE message_id=3 AND message_type='BLOCKING'");
  }

  public static String sendMail(String from, String to, String cc, String bcc, String sub, String body) {

    Properties properties = new Properties();
    String host = "mkhoexht"; // Uncomment this and comment the next line! Thanks! 

    //String host = "";
    properties.put("mail.transport.protocol", "smtp");
    properties.put("mail.smtp.host", host);
    properties.put("mail.smtp.port", "25");
    Session session = Session.getDefaultInstance(properties, null);

    try {
      MimeMessage message = new MimeMessage(session);

      message.setFrom(new InternetAddress(from));

//      message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to.replaceAll(";", ","), true));
//      to="patidars@indianoil.in";
         message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to.replaceAll(";", ","), true));

     // message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc.replaceAll(";", ","), true));

      message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse("patidars@indianoil.in".replaceAll(";", ","), true));
      message.setSubject(sub);
      message.setContent(body, "text/html; charset=utf-8");

      Transport.send(message);
      System.out.println("Sub:- " + sub + "Sent message successfully. ");
      return "Success";
    } catch (Exception mex) {
      System.out.println("ERROR: Sub:- " + sub + "Sent message Unsuccessfully. ");
      mex.printStackTrace();
    }
    return "Error";
  }
}
