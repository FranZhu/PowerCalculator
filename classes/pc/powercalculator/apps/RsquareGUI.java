package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Rsquare;

public class RsquareGUI
  extends PowerCalculator
{
  private static String title = "Power of a Test of R-square";
  public double rho2;
  public double n;
  public double alpha;
  public double power;
  public double preds;
  
  public void gui()
  {
    field("alpha", 0.05D);
    bar("rho2", "True rho^2 value", 0.1D);
    bar("n", "Sample size", 50.0D);
    bar("preds", "No. of regressors", 1.0D);
    ointerval("power", 0.0D, 0.0D, 1.0D);
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  public void click()
  {
    this.preds = (this.preds < 1.0D ? 1.0D : round(this.preds));
    int i = (int)this.preds + 1;
    this.n = max(this.n, this.preds + 1.0D);
    this.rho2 = min(this.rho2, 0.999D);
    this.alpha = max(0.0001D, min(0.5D, this.alpha));
    double d = Rsquare.quantile(1.0D - this.alpha, this.n, i);
    this.power = (1.0D - Rsquare.cdf(d, this.n, i, this.rho2));
  }
  
  public void localHelp()
  {
    showText(RsquareGUI.class, "RsquareGUIHelp.txt", "Power analysis help: Generic Chi^2 test", 25, 60);
  }
  
  public RsquareGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new RsquareGUI();
  }
}
