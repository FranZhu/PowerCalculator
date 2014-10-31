package pc.powercalculator.apps;

import java.awt.Menu;
import pc.powercalculator.PowerCalculator;

public class RetroPower
  extends PowerCalculator
{
  private static String title = "Retrospective Power";
  public double Power;
  public int didRej;
  
  public void gui()
  {
    beginSubpanel(1, false);
    label("Was the test \"significant\"?");
    hradio("didRej", "", new String[] { "No", "Yes" }, 0);
    endSubpanel();
    ointerval("Power", "Retrospective power", 0.0D, 0.0D, 1.0D);
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  protected void afterSetup()
  {
    this.optMenu.remove(4);
    this.optMenu.remove(3);
    this.optMenu.remove(2);
    this.optMenu.remove(1);
    this.optMenu.remove(0);
    this.helpMenu.remove(2);
  }
  
  public void click()
  {
    this.Power = this.didRej;
  }
  
  public void localHelp()
  {
    showText(RetroPower.class, "RetroPowerHelp.txt", "Why retrospective power is silly", 25, 60);
  }
  
  public RetroPower()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new RetroPower();
  }
}
