/*
 * sapConnectionAdapter.java
 *
 * Created on 27 May, 2011, 10:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package sapConnect;

//import java.io.*;
import com.sap.mw.jco.*;
import java.io.*;

import sapConnect.DownloadException;

/**
 *
 * @author 00023569
 */
public abstract class sapConnectionAdapter implements sapConnectionInterface {

    /** Creates a new instance of sapConnectionAdapter */
    public sapConnectionAdapter() {
    }

    public void systemInfo(sapConnect sC) {
        try {
            JCO.Function function = sC.createFunction("RFC_SYSTEM_INFO");
            sC.execute(function);
            // The export parameter 'RFCSI_EXPORT' contains a structure of type 'RFCSI'
            JCO.Structure s = function.getExportParameterList().getStructure("RFCSI_EXPORT");
            // Use enumeration to loop over all fields of the structure
            for (JCO.FieldIterator e = s.fields(); e.hasMoreElements();) {
                JCO.Field field = e.nextField();
                System.out.println(field.getName() + ":\t" + field.getString());
            }
            System.out.println("\n\n");
        } catch (Exception ex) {
            System.out.println("Caught an exception: \n" + ex);
            //errMsg = ex.getMessage();
        }
    }

     protected void start_download(sapConnect sC, String rfcName, String tableName,
            String inputParamName[], Object[] inputParamTypes, String inputParamValue[],
            String outputParamName[], String outputFileName) throws DownloadException {
        try {
            //String[][] table_array = {};
            //   StockPosition sp = new StockPosition();
            String y;
            FileWriter fhnd = new FileWriter(outputFileName);
            //System.out.println("sales INVOICE details");
            JCO.Function function = sC.createFunction(rfcName);
            System.out.println("download details" + function);

                  if (function != null) {

        int nParams = setInputParams(function, inputParamName, inputParamTypes, inputParamValue);

        JCO.ParameterList output = function.getExportParameterList();

        sC.execute(function);
        output.writeXML(fhnd);

        fhnd.close();
      } else {
                System.out.println("Never inside function");
            }
        } catch (Exception ex) {
            System.out.println("exception entered");
            ex.printStackTrace();
            System.out.println(ex.getMessage());
            throw new DownloadException(" Error in Download...");
        } finally {
            System.out.println("Execution Completed...");
        }
    }

    public int setInputParams(JCO.Function function, String[] paramName,
            Object[] paramTypes, String[] paramValue) throws Exception {
        int param_incr = 0;
        Object typObjArr[] = null;
        // JCO.ParameterList output = function.getExportParameterList();
        JCO.ParameterList input = function.getImportParameterList();
        //System.out.println("Download details parameterlist");
        for (param_incr = 0; param_incr < paramName.length; param_incr++) {
            if (paramName[param_incr] == null || paramName[param_incr].equals("")) {
                break;
            }
            //  -----  This is specific to this Class ---------- { -------
            if (paramTypes != null) {
                if (paramTypes[param_incr] == null || paramTypes[param_incr] instanceof String) {
                    input.setValue(paramValue[param_incr].trim(), paramName[param_incr]);
                    //pSIns.setString(nfld++, paramArr[param_incr]);s
                } else if (paramTypes[param_incr] instanceof java.util.Date) {
                    input.setValue(sapUtils.StringToDate(paramValue[param_incr]), paramName[param_incr]);
                }
            } else {
                input.setValue(paramValue[param_incr].trim(), paramName[param_incr]);
                //param_incr++;
            }
        }


        System.out.println(input);
        return (param_incr);
    }
}
