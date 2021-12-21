package com.iocl.blockingROIndentification;

import com.iocl.dbconn.Conn;
import java.sql.CallableStatement;
import java.sql.Connection;

public class BlockingRoIndentification {

  public BlockingRoIndentification() {
  }

  public static void main(String[] args) {
    try {
      BlockingIndentification();
    } catch (Exception e) {
      System.out.println("Exception occurred while BlockingIndentification...");
      e.printStackTrace();
    }
  }

  private static void BlockingIndentification() {
    Connection con = null;
    CallableStatement callableStatement = null;
    String BlockIdentification = "";

    try {
      con = Conn.getCon();
      BlockIdentification = "{call CUSTOMER_IDENTN_FOR_BLOCKING}";


      callableStatement = con.prepareCall(BlockIdentification);
      int isUpdated = callableStatement.executeUpdate();
      System.out.println("isUpdated: " + isUpdated);
      return;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Exception occurred in BlockingIndentification()");
    } finally {
      try {
        if (con != null) {
          con.close();
          con = null;
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
}
