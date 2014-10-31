package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Poisson;

public class SimplePoissonGUI
  extends PowerCalculator
{
  private static String title = "Power of a Simple Poisson Test";
  public double lambda0;
  public double lambda;
  public double alpha;
  public double power;
  public double size;
  public double lower;
  public double upper;
  public double n;
  public int alt;
  
  public void gui()
  {
    bar("lambda0", 1.0D);
    choice("alt", "alternative", new String[] { "lambda < lambda0", "lambda != lambda0", "lambda > lambda0" }, 1);
    
    field("alpha", 0.05D);
    label("Boundaries of acceptance region");
    beginSubpanel(2, false);
    otext("lower", "lower", 0.0D);
    otext("upper", "upper", 0.0D);
    endSubpanel();
    otext("size", "size", 0.0D);
    bar("lambda", 1.0D);
    bar("n", 50.0D);
    ointerval("power", 0.0D, 0.0D, 1.0D);
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  public void click()
  {
    double d = this.alt == 1 ? this.alpha / 2.0D : this.alpha;
    this.size = (this.power = 0.0D);
    if (this.alt < 2)
    {
      this.lower = (Poisson.quantile(d, this.n * this.lambda0) + 1);
      this.size += Poisson.cdf((int)this.lower - 1, this.n * this.lambda0);
      this.power += Poisson.cdf((int)this.lower - 1, this.n * this.lambda);
    }
    if (this.alt > 0)
    {
      this.upper = Poisson.quantile(1.0D - d, this.n * this.lambda0);
      this.size += 1.0D - Poisson.cdf((int)this.upper + 1, this.n * this.lambda0);
      this.power += 1.0D - Poisson.cdf((int)this.upper + 1, this.n * this.lambda);
    }
  }
  
  public void alt_changed()
  {
    setVisible("lower", this.alt < 2);
    setVisible("upper", this.alt > 0);
    click();
  }
  
  public void localHelp()
  {
    showText(SimplePoissonGUI.class, "SimplePoissonGUIHelp.txt", "Power analysis help: Simple Poisson test", 25, 60);
  }
  
  public SimplePoissonGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new SimplePoissonGUI();
  }
}
