/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sapConnect;

/**
 *
 * @author 00081830
 */
public class DownloadException extends Exception {
  public DownloadException (String msgStr) {
    System.out.println(new java.util.Date() + "::>" + msgStr);
  }
}
