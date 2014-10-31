package pc.powercalculator.apps;

import java.applet.Applet;
import pc.powercalculator.PowerCalculator;
import pc.util.Utility;

public class PiLaunch
  extends Applet
{
  public void init()
  {
    try
    {
      String str = getParameter("app");
      Object localObject = Class.forName(str).newInstance();
      ((PowerCalculator)localObject).setMaster(this);
    }
    catch (Exception localException)
    {
      Utility.warning(localException, true);
    }
  }
  
  public void quit(String paramString)
  {
    destroy();
  }
  
  public String getAppletInfo()
  {
    return "Power Calculator Applet";
  }
}
