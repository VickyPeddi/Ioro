/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bulkroblockingsms;

/**
 *
 * @author t_sunita
 */
public class BulkROUnBlockingSMS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n=8;
        ControlThreadCount control = new ControlThreadCount();
        n=control.threadCount();
        System.out.println("thread count :"+n);
        for (int i = 1; i <= n; i++) {
            SendUnBlockingSMS sm = new SendUnBlockingSMS(i);
            sm.start();
        }
    }
}
