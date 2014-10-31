package pc.powercalculator.apps;

import java.applet.Applet;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import pc.powercalculator.PowerCalculator;
import pc.util.Utility;

public class PiLaunchButton
  extends Applet
  implements ActionListener
{
  String app;
  Button button;
  
  public void init()
  {
    setLayout(new GridLayout(1, 1));
    this.app = getParameter("app");
    this.button = new Button("Launch");
    this.button.addActionListener(this);
    add(this.button);
  }
  
  public void runApp()
  {
    try
    {
      Object localObject = Class.forName(this.app).newInstance();
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
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource() == this.button)
    {
      System.out.println("Running app: " + this.app);
      runApp();
    }
  }
  
  public String getAppletInfo()
  {
    return "Power Calculator Applet";
  }
}
