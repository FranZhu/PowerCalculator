package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.T;

public class TEquivGUI
  extends PowerCalculator
{
  private static String title = "Two-sample t test for equivalence";
  public double alpha;
  public double power;
  public double tol;
  public double diff;
  public double sigma;
  public double n;
  
  public void gui()
  {
    bar("tol", "Maximum negligible difference", 0.5D);
    bar("diff", "True difference, |mu1-mu2|", 0.1D);
    bar("sigma", "True SD of each population", 1.0D);
    bar("n", "n for each sample", 25.0D);
    bar("alpha", 0.05D);
    ointerval("power", 0.0D, 0.0D, 1.0D);
  }
  
  public void click()
  {
    this.n = max(2.0D, round(this.n));
    double d = this.sigma * sqrt(2.0D / this.n);
    this.power = T.powerEquiv(this.diff, this.tol, d, 2.0D * (this.n - 1.0D), this.alpha);
  }
  
  public TEquivGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TEquivGUI();
  }
}
