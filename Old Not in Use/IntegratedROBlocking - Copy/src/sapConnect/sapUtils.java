/*
 * sapUtils.java
 *
 * Created on 27 May, 2011, 10:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sapConnect;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
/**
 *
 * @author 00023569
 */
public class sapUtils {
  
  /** Creates a new instance of sapUtils */
  public sapUtils() {
  }
  
  public static Date StringToDate(String inputdate) {
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    Date today = null;
    try {
      today = df.parse(inputdate);
      System.out.println("Today = " + df.format(today));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return today;
  }
  
  public static String DateToString(Date inputdate) {
    DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
    String StringDate = df.format(inputdate);
    return StringDate;
  }
  
  public static String TimeToString(Date inputdate) {
    DateFormat df = new SimpleDateFormat("hh:mm:ss");
    String StringDate = df.format(inputdate);
    return StringDate;
  }
  
}
