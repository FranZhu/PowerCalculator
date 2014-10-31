package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.F;
import pc.stat.dist.T;

public class testequiv
  extends PowerCalculator
{
  private static String title = "";
  public double thresh;
  public double n;
  public double alpha;
  public double shuir;
  public double exact;
  public double ratio;
  
  public void gui()
  {
    bar("thresh", 1.0D);
    bar("n", 20.0D);
    bar("alpha", 0.05D);
    obar("shuir", 1.0D);
    obar("exact", 1.0D);
    obar("ratio", 1.0D);
  }
  
  public void click()
  {
    this.n = Math.max(2.0D, this.n);
    double d = Math.sqrt(2.0D / this.n);
    this.shuir = Math.max(0.0D, this.thresh / d - T.quantile(1.0D - this.alpha, 2.0D * (this.n - 1.0D)));
    this.exact = Math.sqrt(F.quantile(this.alpha, 1.0D, 2.0D * (this.n - 1.0D), this.thresh / d * (this.thresh / d)));
    
    this.ratio = (this.shuir / this.exact);
  }
  
  public testequiv()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new testequiv();
  }
}
