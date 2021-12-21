package sapConnect;

import java.io.PrintStream;
import java.util.Date;




public class DownloadException
  extends Exception
{
  public DownloadException(String msgStr)
  {
    System.out.println(new Date() + "::>" + msgStr);
  }
}
