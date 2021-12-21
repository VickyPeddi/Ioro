/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bulkroblockingsms;

import DbConnection.DbConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.commons.dbutils.DbUtils;

/**
 *
 * @author t_sunita
 */
public class ControlThreadCount {

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String query = "";

    public int threadCount() {
        int thread_count = 0;
        int mail_count = 0;
        try {
            con = DbConn.getCon();
            query = "SELECT COUNT(*) FROM SAP_UNBLOCK_CUSTOMERS WHERE SMS_STATUS='P' AND UNBLOCK_STATUS='S' "
                    + "AND TO_CHAR(UNBLOCK_CONF_DATETIME,'YYYYMMDD')>=TO_CHAR(SYSDATE-1,'YYYYMMDD') "
                    + "ORDER BY DATE_UPDATED ";
            pstmt = con.prepareStatement(query);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                mail_count = rs.getInt(1);
            }
            if (mail_count > 0) {
                if(mail_count>500){
                thread_count = mail_count / 500;
                thread_count++;
                }else{
                    thread_count++;
                }
            }
            System.out.print("Thread count :"+thread_count);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbUtils.closeQuietly(con, pstmt, rs);
            return thread_count;
        }
    }
}
