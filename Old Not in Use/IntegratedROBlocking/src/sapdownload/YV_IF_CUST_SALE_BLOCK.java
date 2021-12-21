package sapdownload;

import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.ParameterList;
import com.sap.mw.jco.JCO.Table;
import java.io.PrintStream;
import java.util.Date;
import sapConnect.sapConnect;


















public class YV_IF_CUST_SALE_BLOCK
{
  JCO.Function function = null;
  public String errMsg = null;
  

  public YV_IF_CUST_SALE_BLOCK() {}
  

  public String[] blockCheck(sapConnect sC, String[] roCodeArr, String soCode)
  {
    String[] ret_msg = null;
    YV_IF_CUST_SALE_BLOCK inv = new YV_IF_CUST_SALE_BLOCK();
    try {
      ret_msg = inv.get_message(sC, roCodeArr, soCode);
      System.out.println("Program ends at : " + new Date());
    } catch (Exception e) {
      System.out.println(e);
    }
    return ret_msg;
  }
  
  public String[] get_message(sapConnect sC, String[] roCodeArr, String soCode) {
    String[] msg = null;
    
    try
    {
      function = sC.createFunction("YV_IF_CUST_SALE_BLOCK_BULK");
      
      if (function != null) {
        JCO.Table inputValue = function.getTableParameterList().getTable("IT_CUSTOMER");
        
        for (int i = 0; i < roCodeArr.length; i++) {
          inputValue.appendRow();
          inputValue.setRow(i);
          inputValue.setValue("0000" + roCodeArr[i], "KUNNR");
          inputValue.setValue(soCode, "VKORG");
          inputValue.setValue("RE", "VTWEG");
          inputValue.setValue("MH", "SPART");
          inputValue.setValue("11", "P_ORD_BLK");
          inputValue.setValue("10", "P_DEL_BLK");
          inputValue.setValue("10", "P_BILL_BLK");
        }
        
        JCO.Table table_Detail = function.getTableParameterList().getTable("IT_RET_MSG");
        System.out.println("table_Detail ::::: \n" + table_Detail);
        sC.execute(function);
        
        int noOfRows = 0;
        int noOfCols = 0;
        
        if (table_Detail != null) {
          noOfRows = table_Detail.getNumRows();
          noOfCols = table_Detail.getNumColumns();
        }
        
        System.out.println(noOfRows + " :: Rows and " + noOfCols + "::columns");
        msg = new String[noOfRows];
        if (noOfRows > 0) {
          for (int i = 0; i < noOfRows; i++) {
            System.out.println("Row no.--" + (i + 1));
            table_Detail.setRow(i);
            System.out.println(table_Detail.getString("KUNNR"));
            System.out.println(table_Detail.getString("VKORG"));
            System.out.println(table_Detail.getString("VTWEG"));
            System.out.println(table_Detail.getString("SPART"));
            System.out.println(table_Detail.getString("RET_MSG"));
            






            msg[i] = table_Detail.getString("RET_MSG");
          }
        }
        table_Detail.clear();
        inputValue.clear();
      } else {
        System.out.println("Function did not enter");
      }
    } catch (Exception ex) {
      ex = 
      



        ex;System.out.println("Exception Entered");ex.printStackTrace();System.out.println(ex.getMessage()); } finally {}
    return msg;
  }
}
