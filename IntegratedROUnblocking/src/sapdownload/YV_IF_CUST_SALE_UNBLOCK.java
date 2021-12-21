
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * @author Kuljeet Singh
 */
package sapdownload;

import com.sap.mw.jco.*;

import sapConnect.sapConnect;

public class YV_IF_CUST_SALE_UNBLOCK {

    JCO.Function function = null;
    public String errMsg = null;

    public YV_IF_CUST_SALE_UNBLOCK() {
    }

    public String[] unblockCheck(String roCodeArr[], String soCodeArr[], String divisionArray[]) {

        String ret_msg[] = null;
        sapConnect sC = new sapConnect();
        YV_IF_CUST_SALE_UNBLOCK inv = new YV_IF_CUST_SALE_UNBLOCK();

        try {

            JCO.Client connection = sC.Connect();
            if (connection != null) {
                ret_msg = inv.get_message(sC, roCodeArr, divisionArray, soCodeArr);
                sC.disconnect();
                sC.cleanUp();
            } else {
                System.out.println("Connection to SAP failed...");
            }
            System.out.println("Program ends at : " + new java.util.Date());
        } catch (Exception e) {
            System.out.println(e);
        }
        return ret_msg;
    }

    public String[] get_message(sapConnect sC, String roCodeArr[], String divisionArray[], String soCodeArr[]) {
        String msg[] = null;

        try {

            function = sC.createFunction("YV_IF_CUST_SALE_UNBLOCK_BULK");

            if (function != null) {

                JCO.Table inputValue = function.getTableParameterList().getTable("IT_CUSTOMER");//T_ZVINVACK_DOWNLD

                for (int i = 0; i < roCodeArr.length; i++) {
                    inputValue.appendRow();
                    inputValue.setRow(i);
                    inputValue.setValue("0000" + roCodeArr[i], "KUNNR");
                    inputValue.setValue(soCodeArr[i], "VKORG");
                    inputValue.setValue("RE", "VTWEG");
                    inputValue.setValue(divisionArray[i], "SPART");
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
            } else {
                System.out.println("Function did not enter");
            }

        } catch (Exception ex) {
            System.out.println("Exception Entered");
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
        }
        return msg;
    }
}
