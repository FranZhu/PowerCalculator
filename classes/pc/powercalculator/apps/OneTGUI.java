package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.T;

public class OneTGUI
  extends PowerCalculator
{
  private static String title = "One-sample (or paired) t test";
  public double sigma;
  public double n;
  public double diff;
  public double alpha;
  public double df;
  public double power;
  public double delta;
  public int eqs;
  public int eqn;
  public int tt;
  public int opt;
  
  public void gui()
  {
    bar("sigma", 1.0D);
    bar("diff", "True |mu - mu_0|", 0.5D);
    bar("n", 25.0D);
    beginSubpanel(1, true);
    interval("power", 0.5D, 0.0D, 1.0D);
    choice("opt", "Solve for", new String[] { "n", "Effect size" }, 0);
    
    endSubpanel();
    beginSubpanel(2);
    choice("alpha", new double[] { 0.005D, 0.01D, 0.02D, 0.05D, 0.1D, 0.2D }, 3);
    checkbox("tt", "Two-tailed", 1);
    endSubpanel();
    
    menuItem("localHelp", "t test info", this.helpMenu);
  }
  
  public void click()
  {
    this.n = max(round(this.n), 2.0D);
    this.delta = (sqrt(this.n) * this.diff / this.sigma);
    this.power = T.power(this.delta, this.n - 1.0D, 1 - this.tt, this.alpha);
  }
  
  public void power_changed()
  {
    switch (this.opt)
    {
    case 0: 
      if (abs(this.diff) < 0.001D * this.sigma) {
        return;
      }
      for (int i = 0; i < 3; i++)
      {
        double d2 = this.n;
        this.delta = T.delta(this.power, this.n - 1.0D, 1 - this.tt, this.alpha);
        this.n = pow(this.delta * this.sigma / this.diff, 2.0D);
        if (Double.isNaN(this.n))
        {
          this.n = d2;
          break;
        }
      }
      break;
    case 1: 
      double d1 = this.diff;
      this.delta = T.delta(this.power, this.n - 1.0D, 1 - this.tt, this.alpha);
      this.diff = (this.sigma * this.delta / sqrt(this.n));
      if (Double.isNaN(this.diff)) {
        this.diff = d1;
      }
      break;
    }
    click();
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "OneTGUIHelp.txt", "Power analysis help: One-sample t test", 25, 60);
  }
  
  public OneTGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new OneTGUI();
  }
}
