package sapConnect;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;













public class sapUtils
{
  public sapUtils() {}
  
  public static Date StringToDate(String inputdate)
  {
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
