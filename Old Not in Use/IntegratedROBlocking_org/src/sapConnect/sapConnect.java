package sapConnect;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Client;
import com.sap.mw.jco.JCO.Function;
import com.sap.mw.jco.JCO.Pool;
import com.sap.mw.jco.JCO.PoolManager;
import com.sap.mw.jco.JCO.Repository;
import java.io.PrintStream;






public class sapConnect
{
  static final String POOL_NAME = "P";
  
  public sapConnect() {}
  
  JCO.Repository mRepository = null;
  JCO.Client client = null;
  JCO.Function function = null;
  public String succMsg = null;
  
  public JCO.Client Connect(String sap_user_id, String sap_password) {
    System.out.println("Connecting to Server using User ID :" + sap_user_id);
    try {
      String pool_user = "P";
      JCO.Pool pool = JCO.getClientPoolManager().getPool(pool_user);
      if (pool == null) {
        System.out.println("inside connect" + sap_user_id);
        
        JCO.addClientPool(pool_user, 25, "310", sap_user_id, sap_password, "EN", "erpprd.ds.indianoil.in", "PRD", "ERPRFC");
        

        pool = JCO.getClientPoolManager().getPool(pool_user);
      }
      client = JCO.getClient(pool_user);
      if (mRepository == null) {
        mRepository = new JCO.Repository("SAPRepo", pool_user);
      }
      
      System.out.println(client.getAttributes());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return client;
  }
  
  public JCO.Function createFunction(String name) {
    try {
      IFunctionTemplate ft = mRepository.getFunctionTemplate(name.toUpperCase());
      System.out.println("create  function");
      if (ft == null) {
        System.out.println("function null");
        return null;
      }
      System.out.println("function" + ft.getFunction());
      return ft.getFunction();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      cleanUp(); }
    return null;
  }
  
  public void execute(JCO.Function function)
  {
    client.execute(function);
  }
  
  public void disconnect() {
    JCO.releaseClient(client);
  }
  
  public void cleanUp() {
    disconnect();
  }
}
