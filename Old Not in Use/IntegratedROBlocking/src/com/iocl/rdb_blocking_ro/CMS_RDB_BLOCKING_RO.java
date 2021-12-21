package com.iocl.rdb_blocking_ro;

import com.iocl.dbconn.Conn;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import sapConnect.sapConnect;
import sapdownload.YV_IF_CUST_SALE_BLOCK;




public class CMS_RDB_BLOCKING_RO
{
  public CMS_RDB_BLOCKING_RO() {}
  
  public static void main(String[] args)
    throws SQLException, IOException
  {
    String sapConf = "./ConnSAP.xml";
    String SAPCon = "SAPQAS";
    String username = "";
    String passwd = "";
    sapConnect sC = new sapConnect();
    FileInputStream fIs = null;
    try
    {
      try {
        Properties prop = new Properties();
        fIs = new FileInputStream(sapConf);
        prop.loadFromXML(fIs);
        username = prop.getProperty("SAPUSERID", username);
        passwd = prop.getProperty("SAPPASSWORD", passwd);
        System.out.println("Program starts at : " + new Date());
        

        sC.Connect(username, passwd);
      } catch (Exception e) {
        System.out.println(e);
      }
      try
      {
        CallSAPPI(sC, SAPCon, "");
      } catch (Exception e) {
        System.out.println("Exception occurred while Syncing...");
        e.printStackTrace();
      }
      sC.disconnect();
      try {
        sendBlockingStatusMail();
      } catch (Exception e) {
        System.out.println("Exception occurred while Sending the blocking Status mail...");
        e.printStackTrace();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (fIs != null) {
        fIs.close();
      }
    }
  }
  
  public static void CallSAPPI(sapConnect sC, String args, String conn) throws SQLException
  {
    long startTime = 0L;
    long endTime = 0L;
    long timeTaken = 0L;
    ResultSet res = null;
    Statement stmt = null;
    Connection con_RDB = null;
    CallableStatement callablestmt = null;
    String salesOrg = "";
    String salesOff = "";
    String sales_org_query = "select distinct salesoff, salesorg from retaildb.iocl_md_aod_sales order by salesoff";
    CMS_RDB_BLOCKING_RO obj = new CMS_RDB_BLOCKING_RO();
    try {
      con_RDB = Conn.getCon();
      stmt = con_RDB.createStatement(1004, 1007);
      res = stmt.executeQuery(sales_org_query);
      int a = 0;
      startTime = System.currentTimeMillis();
      while (res.next())
      {
        salesOff = res.getString("salesoff");
        salesOrg = res.getString("salesorg");
        a = blockROSalesOff(sC, salesOrg, salesOff);
        System.out.println("Blocked RO:- " + a);
        

        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("Time taken in the process of  BLOCKING, SALESOFFICE " + salesOff + "  is:: " + timeTaken + " ms  at " + new Date());
        startTime = endTime;
      }
      return;
    }
    catch (Exception e) {
      System.out.println("Exception in Main while loop:" + e);
    } finally {
      try {
        if (res != null) {
          res.close();
        }
      }
      catch (Exception e) {}
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (Exception e) {}
      try
      {
        if (con_RDB != null) {
          con_RDB.close();
        }
      }
      catch (Exception e) {}
    }
  }
  

  public int updateInsertStatus(Connection con_RDB, String updateQuery)
  {
    Statement stmt = null;
    rowsUpdated = 0;
    try {
      if (!updateQuery.equalsIgnoreCase("")) {
        stmt = con_RDB.createStatement();
        rowsUpdated = stmt.executeUpdate(updateQuery);
      }
      else {
        System.out.println("No Update Query found..." + updateQuery);
      }
      










      return rowsUpdated;
    }
    catch (Exception e)
    {
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
  }
  
  public int updateInsertStatus(Connection con_RDB, String query, boolean addToBatch)
  {
    boolean res = false;
    Connection con = null;
    Statement stmt = null;
    counter = 0;
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
      




















      return counter;
    }
    catch (BatchUpdateException ex)
    {
      if (con != null) {
        try {
          con.rollback();
        } catch (SQLException sqlEx) {
          sqlEx.printStackTrace();
        }
      }
      
      System.out.println("CMS_RDB_BLOCKING>>" + ex.getMessage());
    }
    catch (SQLException ex) {}finally {
      try {
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
      }
      catch (Exception ex) {}
    }
  }
  

  public static int blockROSalesOff(sapConnect sC, String salesOrg, String salesOff)
  {
    long startTime = 0L;
    long endTime = 0L;
    long timeTaken = 0L;
    ResultSet rs = null;
    Statement stm = null;Statement stm2 = null;
    Connection con_RDB = null;
    
    String[] message = null;
    String[][] arr = (String[][])null;
    String RO_Code = "";String blockID = "";
    String query = "";
    String query1 = "";
    int rowsMerged = 0;
    int updatedRows = 0;
    String mergeblockingDetails = "";
    String updateQuery = "";
    YV_IF_CUST_SALE_BLOCK ss = new YV_IF_CUST_SALE_BLOCK();
    
    CMS_RDB_BLOCKING_RO obj = new CMS_RDB_BLOCKING_RO();
    
    try
    {
      con_RDB = Conn.getCon();
      
      query = "select CUSTCODE,SALESORG_CODE,BLOCK_ATTEMPT_NO,BLOCK_FLAG,BLOCK_ID from cmsro.SAP_BLOCK_CUSTOMERS     WHERE SAP_BLOCK_STATUS='P' AND BLOCK_FLAG='P' AND SALESOFF_CODE=" + salesOff;
      
      stm = con_RDB.createStatement(1004, 1007);
      rs = stm.executeQuery(query);
      
      int blockAttemptNo = 0;
      
      rs.last();
      int rows = rs.getRow();
      

      if (rows == 0) {
        return 0;
      }
      int index = 0;
      arr = new String[3][rows];
      message = new String[rows];
      rs.beforeFirst();
      while (rs.next()) {
        RO_Code = rs.getString("CUSTCODE");
        blockAttemptNo = rs.getInt("BLOCK_ATTEMPT_NO");
        blockID = rs.getString("BLOCK_ID");
        arr[0][index] = RO_Code;
        arr[1][index] = blockID;
        arr[2][index] = ("" + blockAttemptNo);
        index++;
      }
      
      updateQuery = "UPDATE cmsro.SAP_BLOCK_CUSTOMERS SET SAP_BLOCK_STATUS = 'I', DATE_UPDATED = sysdate WHERE SALESOFF_CODE = " + salesOff + " and SAP_BLOCK_STATUS = 'P' AND BLOCK_FLAG='P'";
      int updatedRow = obj.updateInsertStatus(con_RDB, updateQuery);
      

      startTime = System.currentTimeMillis();
      

      message = ss.blockCheck(sC, arr[0], salesOrg);
      

      endTime = System.currentTimeMillis();
      timeTaken = endTime - startTime;
      System.out.println("Time taken in SAP Blocking ROCODE  salesoff " + salesOff + " is:: " + timeTaken + " ms at" + new Date());
      if (updatedRow == message.length) {
        startTime = System.currentTimeMillis();
        for (int j = 0; j < message.length; j++) {
          try {
            if (message[j] != null) {
              if ((message[j].trim().equalsIgnoreCase("Order blocked!!Delivery blocked!!Bill blocked!!")) || (message[j].trim().equalsIgnoreCase("Custmer is already blocked in sales area!!"))) {
                System.out.println(arr[0][j] + " ,Msg: " + message[j]);
                
                query1 = "update cmsro.SAP_BLOCK_CUSTOMERS set SAP_BLOCK_MSG = '" + message[j] + "' ,  DATE_UPDATED = sysdate," + " SAP_BLOCK_STATUS = 'S', BLOCK_CONF_DATETIME = sysdate, BLOCK_FLAG='B' where block_id = " + arr[1][j] + " and SAP_BLOCK_STATUS = 'I' and block_attempt_no=" + arr[2][j];
              }
              else
              {
                System.out.println(arr[0][j] + " ,Msg: " + message[j]);
                

                query1 = "update cmsro.SAP_BLOCK_CUSTOMERS set SAP_BLOCK_MSG = '" + message[j] + "' , DATE_UPDATED = sysdate," + " SAP_BLOCK_STATUS = 'R' ,SMS_STATUS='R' where block_id = " + arr[1][j] + " and SAP_BLOCK_STATUS = 'I' and block_attempt_no=" + arr[2][j];
              }
            }
            





            stm2 = con_RDB.createStatement();
            updatedRows = stm2.executeUpdate(query1);
            



            try
            {
              if (stm2 != null) {
                stm2.close();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
            
            mergeblockingDetails = null;
          }
          catch (Exception EE)
          {
            System.out.println(EE);
          }
          finally {
            try {
              if (stm2 != null) {
                stm2.close();
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          
          rowsMerged = 0;
          updatedRows = 0;
        }
        endTime = System.currentTimeMillis();
        timeTaken = endTime - startTime;
        System.out.println("Time taken in Database Activity to update records by salesOff " + salesOff + " is:: " + timeTaken + " ms at" + new Date());
      }
      


      try
      {
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e) {}
      try {
        if (stm != null) {
          stm.close();
        }
      }
      catch (Exception e) {}
      try {
        if (con_RDB != null) {
          con_RDB.close();
        }
      }
      catch (Exception e) {}
      

      System.out.println("---Process Completed for SALES_OFFICE : " + salesOff + " ----");
    }
    catch (Exception e)
    {
      System.out.println("Error: " + e.getMessage());
      e.printStackTrace();
    } finally {
      try {
        if (rs != null) {
          rs.close();
        }
      }
      catch (Exception e) {}
      try {
        if (stm != null) {
          stm.close();
        }
      }
      catch (Exception e) {}
      try {
        if (con_RDB != null) {
          con_RDB.close();
        }
      }
      catch (Exception e) {}
    }
    

    arr = (String[][])null;
    message = null;
    ss = null;
    obj = null;
    return 1;
  }
  
  public static void sendBlockingStatusMail()
  {
    try {
      String HtmlBody = Conn.getValue("select listagg (resp, '<br>') WITHIN GROUP (ORDER BY RESP)  from \n table (db_status_msg(to_char(sysdate,'yyyymmdd')))");
      

      String[] TOmail = { "patidars@indianoil.in", "MOUNISHAROY@indianoil.in", "sdebroy@indianoil.in", "rohitkumar@indianoil.in", "maltijoshi@indianoil.in", "srandive@indianoil.in" };
      String FromMail = "retaildashboard@indianoil.in";
      for (int i = 0; i < TOmail.length; i++) {
        Conn.getValue("select RETAILDB.SEND_MAILHTML('" + TOmail[i] + "','" + FromMail + "','RDB Blocking Status','" + HtmlBody + "') from dual");
      }
      System.out.println("Sending Blocking mail Status Completed");
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println("Sending Blocking mail Status failed.");
    }
  }
}
