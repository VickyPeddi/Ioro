package sapConnect;

import com.sap.mw.jco.JCO.Field;
import com.sap.mw.jco.JCO.FieldIterator;
import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.ParameterList;
import com.sap.mw.jco.JCO.Structure;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.Date;









public abstract class sapConnectionAdapter
  implements sapConnectionInterface
{
  public sapConnectionAdapter() {}
  
  public void systemInfo(sapConnect sC)
  {
    try
    {
      JCO.Function function = sC.createFunction("RFC_SYSTEM_INFO");
      sC.execute(function);
      
      JCO.Structure s = function.getExportParameterList().getStructure("RFCSI_EXPORT");
      
      for (JCO.FieldIterator e = s.fields(); e.hasMoreElements();) {
        JCO.Field field = e.nextField();
        System.out.println(field.getName() + ":\t" + field.getString());
      }
      System.out.println("\n\n");
    } catch (Exception ex) {
      System.out.println("Caught an exception: \n" + ex);
    }
  }
  



  protected void start_download(sapConnect sC, String rfcName, String tableName, String[] inputParamName, Object[] inputParamTypes, String[] inputParamValue, String[] outputParamName, String outputFileName)
    throws DownloadException
  {
    try
    {
      FileWriter fhnd = new FileWriter(outputFileName);
      
      JCO.Function function = sC.createFunction(rfcName);
      System.out.println("download details" + function);
      
      if (function != null)
      {
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
  
  public int setInputParams(JCO.Function function, String[] paramName, Object[] paramTypes, String[] paramValue) throws Exception
  {
    int param_incr = 0;
    Object[] typObjArr = null;
    
    JCO.ParameterList input = function.getImportParameterList();
    
    for (param_incr = 0; param_incr < paramName.length; param_incr++) {
      if ((paramName[param_incr] == null) || (paramName[param_incr].equals(""))) {
        break;
      }
      
      if (paramTypes != null) {
        if ((paramTypes[param_incr] == null) || ((paramTypes[param_incr] instanceof String))) {
          input.setValue(paramValue[param_incr].trim(), paramName[param_incr]);
        }
        else if ((paramTypes[param_incr] instanceof Date)) {
          input.setValue(sapUtils.StringToDate(paramValue[param_incr]), paramName[param_incr]);
        }
      } else {
        input.setValue(paramValue[param_incr].trim(), paramName[param_incr]);
      }
    }
    


    System.out.println(input);
    return param_incr;
  }
}
