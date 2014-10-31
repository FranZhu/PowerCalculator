package pc.powercalculator.apps;

import java.awt.Menu;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import pc.powercalculator.PowerCalculator;
import pc.util.Utility;

public class PiPickerSA
  extends PowerCalculator
{
  private static String appList = "PiPicker.txt";
  private static String title = "Power Calculator";
  public int sel;
  public Vector classes;
  public String pkg;
  
  public void gui()
  {
    Properties localProperties = new Properties();
    Vector localVector = new Vector();
    this.classes = new Vector();
    try
    {
      InputStream localInputStream = getClass().getResourceAsStream(appList);
      localProperties.load(localInputStream);
      this.pkg = localProperties.getProperty("package", "pc.powercalculator.apps");
      String localObject = localProperties.getProperty("keys");
      StringTokenizer localStringTokenizer = new StringTokenizer((String)localObject, ",");
      while (localStringTokenizer.hasMoreTokens())
      {
        String str = localStringTokenizer.nextToken();
        this.classes.addElement(str);
        localVector.addElement(localProperties.getProperty(str, "???"));
      }
    }
    catch (IOException localIOException)
    {
      Utility.error(localIOException);
    }
    catch (Exception localException)
    {
      Utility.error(localException);
    }
    int i = localVector.size();
    String [] localObject = new String[i];
    for (int j = 0; j < i; j++) {
      localObject[j] = ((String)localVector.elementAt(j));
    }
    label("Type of analysis");
    choice("sel", "", (String[])localObject, 0);
    beginSubpanel(2);
    label("                           ");
    button("runSelApp", "Run dialog");
    endSubpanel();
  }
  
  protected void afterSetup()
  {
    this.optMenu.remove(4);
    this.optMenu.remove(3);
    this.optMenu.remove(2);
    this.optMenu.remove(1);
    this.optMenu.remove(0);
    this.helpMenu.remove(1);
    this.helpMenu.remove(0);
  }
  
  public void click() {}
  
  public void runSelApp()
  {
    String str = (String)this.classes.elementAt(this.sel);
    try
    {
      Object localObject = Class.forName(this.pkg + "." + str).newInstance();
      ((PowerCalculator)localObject).setMaster(this);
    }
    catch (Exception localException)
    {
      Utility.warning(localException, true);
    }
  }
  
  public PiPickerSA()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length > 0) {
      appList = paramArrayOfString[0];
    }
    new PiPickerSA();
   }
}
