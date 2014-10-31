package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Chi2;

public class Pilot
  extends PowerCalculator
{
  private static String title = "Pilot study";
  public double risk;
  public double pctUnder;
  public double df;
  
  public void gui()
  {
    bar("pctUnder", "Percent by which N is under-estimated", 20.0D);
    bar("risk", "Risk of exceeding this percentage", 0.1D);
    bar("df", "d.f. for error in pilot study", 80.0D);
    menuItem("localHelp", "Pilot study help", this.helpMenu);
  }
  
  protected void afterShow() {}
  
  public void click()
  {
    this.df = round(this.df);
    this.risk = Chi2.cdf((1.0D - 0.01D * this.pctUnder) * this.df, this.df);
  }
  
  public void risk_changed()
  {
    this.df = solve("df", "risk", this.risk, this.df, 0.1D * max(10.0D, this.df));
    click();
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "PilotHelp.txt", "Help: Pilot study", 25, 60);
  }
  
  public Pilot()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new Pilot();
  }
}
