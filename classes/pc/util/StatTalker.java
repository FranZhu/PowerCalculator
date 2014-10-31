package pc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class StatTalker
{
  private Socket socket;
  private BufferedReader in;
  private PrintStream out;
  private boolean ok = false;
  
  public StatTalker()
  {
    this(8788);
  }
  
  public StatTalker(int paramInt)
  {
    try
    {
      System.err.println("Opening socket on port " + paramInt);
      this.socket = new Socket("localhost", paramInt);
      System.err.println(this.socket.toString());
      this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
      
      this.out = new PrintStream(this.socket.getOutputStream());
      this.ok = true;
    }
    catch (Exception localException)
    {
      System.err.println(localException);
    }
  }
  
  public boolean isOpen()
  {
    return this.ok;
  }
  
  public String submit(String paramString)
  {
    if (this.out == null) {
      return "<error>";
    }
    if (paramString.trim().equals("")) {
      return "";
    }
    try
    {
      this.out.println(paramString);
      StringBuffer localStringBuffer = new StringBuffer();
      int i = 0;
      while (i == 0)
      {
        String str = this.in.readLine();
        if (str.trim().equals("[end]")) {
          i = 1;
        } else {
          localStringBuffer.append(str + "\n");
        }
      }
      return new String(localStringBuffer);
    }
    catch (IOException localIOException)
    {
      System.err.println(localIOException);
    }
    return "[error]";
  }
  
  public void close()
  {
    this.ok = false;
    try
    {
      this.in.close();
      this.out.close();
      this.socket.close();
    }
    catch (IOException localIOException)
    {
      System.err.println(localIOException);
    }
  }
  
  public static void main(String[] paramArrayOfString)
  {
    int i = 8788;
    if (paramArrayOfString.length >= 1) {
      i = Utility.strtoi(paramArrayOfString[0]);
    }
    StatTalker localStatTalker = new StatTalker(i);
    if (!localStatTalker.isOpen()) {
      System.exit(1);
    }
    int j = 0;
    System.out.println("Type \"quit\" to quit");
    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(System.in));
    try
    {
      while (j == 0)
      {
        System.out.print("\n> ");
        String str = localBufferedReader.readLine();
        if (str.trim().equalsIgnoreCase("quit"))
        {
          j = 1;
          localStatTalker.close();
        }
        double[] arrayOfDouble = Utility.parseDoubles(localStatTalker.submit(str));
        for (int k = 0; k < arrayOfDouble.length; k++) {
          System.out.print(arrayOfDouble[k] + "\t");
        }
      }
    }
    catch (IOException localIOException)
    {
      System.err.println(localIOException);
    }
    System.out.println("We're done");
    System.exit(0);
  }
}
