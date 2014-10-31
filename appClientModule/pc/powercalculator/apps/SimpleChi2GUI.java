package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Chi2;

public class SimpleChi2GUI
  extends PowerCalculator
{
  public double proChi2;
  public double proN;
  public double n;
  public double df;
  public double Alpha;
  public double Power;
  
  public void gui()
  {
    beginSubpanel(1, false);
    label("Prototype data");
    beginSubpanel(2);
    field("proChi2", "Chi2*", 10.0D);
    field("proN", "n*", 100.0D);
    endSubpanel();
    endSubpanel();
    label("Study parameters");
    beginSubpanel(2);
    field("df", 3.0D);
    field("Alpha", 0.05D);
    endSubpanel();
    bar("n", 50.0D);
    interval("Power", 0.0D, 0.0D, 1.0D);
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  public void click()
  {
    this.n = max(round(this.n), 2.0D);
    this.df = max(round(this.df), 1.0D);
    this.Power = Chi2.power(this.n * this.proChi2 / this.proN, this.df, this.Alpha);
  }
  
  public void Power_changed()
  {
    this.n = (Chi2.lambda(this.Power, this.df, this.Alpha) * this.proN / this.proChi2);
    click();
  }
  
  public SimpleChi2GUI()
  {
    super("Chi-Square Power");
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "SimpleChi2GUIHelp.txt", "Power analysis help: Generic Chi^2 test", 25, 60);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new SimpleChi2GUI();
  }
}
