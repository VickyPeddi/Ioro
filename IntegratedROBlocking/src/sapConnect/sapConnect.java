package sapConnect;

import com.iocl.dbconn.Conn;
import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import java.sql.CallableStatement;
import java.sql.Connection;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.dbutils.DbUtils;

public class sapConnect {

    static final String POOL_NAME = "P";

    public sapConnect() {
    }

    JCO.Repository mRepository = null;
    JCO.Client client = null;
    JCO.Function function = null;
    public String succMsg = null;

    public JCO.Client Connect() {
        System.out.println("Connecting to Server using User ID :");
        try {
            String pool_user = POOL_NAME;
            JCO.Pool pool = JCO.getClientPoolManager().getPool(pool_user);
            if (pool == null) {
                System.out.println("inside connect"); // + "," + sap_password);

                Connection con = null;
                CallableStatement call_stmt = null;
                try {
                    con = Conn.getConRDB();
                    String str_procd = "{call GET_DBCON(?,?,?,?,?,?,?,?,?,?)}";
                    call_stmt = con.prepareCall(str_procd);
                    call_stmt.setString(1, "8");
                    call_stmt.setString(2, "BLOCKING");
                    call_stmt.registerOutParameter(3, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(4, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(5, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(6, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(7, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(8, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(9, java.sql.Types.VARCHAR);
                    call_stmt.registerOutParameter(10, java.sql.Types.VARCHAR);
                    call_stmt.executeUpdate();

                    if (call_stmt.getString(8).equalsIgnoreCase("PROD")) {
                        JCO.addClientPool(POOL_NAME, 20, "310", call_stmt.getString(5), decrypt(call_stmt.getString(7)), "EN", call_stmt.getString(3), call_stmt.getString(9), call_stmt.getString(10));
                    } else {
                        JCO.addClientPool(POOL_NAME, 5, "310", call_stmt.getString(5), decrypt(call_stmt.getString(7)), "EN", call_stmt.getString(3), "51");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    DbUtils.closeQuietly(call_stmt);
                    DbUtils.closeQuietly(con);
                }

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

    public static String decrypt(String data) {
        String dPass = new String(Base64.decodeBase64(data));
        return dPass;
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
        } catch (Exception ex) {
            ex.printStackTrace();
            cleanUp();
        }
        return null;
    }

    public void execute(JCO.Function function) {
        client.execute(function);
    }

    public void disconnect() {
        JCO.releaseClient(client);
    }

    public void cleanUp() {
        disconnect();
    }
}
