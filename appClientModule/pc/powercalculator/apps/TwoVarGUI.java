package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.F;

public class TwoVarGUI
  extends PowerCalculator
{
  private static String title = "Test of equality of two variances";
  public double var1;
  public double var2;
  public double n1;
  public double n2;
  public double Alpha;
  public double Power;
  public int eq;
  public int alt;
  public int cc;
  
  public void gui()
  {
    beginSubpanel(2, true);
    bar("n1", 100.0D);
    bar("var1", "Variance 1", 1.5D);
    bar("n2", 100.0D);
    bar("var2", "Variance 2", 1.0D);
    checkbox("eq", "Equal ns", 1);
    choice("alt", "Alternative", new String[] { "Var1 < Var2", "Var1 != Var2", "Var1 > Var2" }, 1);
    
    endSubpanel();
    beginSubpanel(2, true);
    bar("Alpha", 0.05D);
    ointerval("Power", 0.5D, 0.0D, 1.0D);
    endSubpanel();
    
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  public void click()
  {
    this.n1 = max(2.0D, round(this.n1));
    if (this.eq == 1) {
      this.n2 = this.n1;
    }
    calcPower();
  }
  
  public void n2_changed()
  {
    this.n2 = max(round(this.n2), 2.0D);
    if (this.eq == 1) {
      this.n1 = this.n2;
    }
    calcPower();
  }
  
  private void calcPower()
  {
    double d1;
    double d2;
    double d3;
    if (this.alt >= 1)
    {
      d1 = this.n1 - 1.0D;d2 = this.n2 - 1.0D;d3 = this.var1 / this.var2;
    }
    else
    {
      d1 = this.n2 - 1.0D;d2 = this.n1 - 1.0D;d3 = this.var2 / this.var1;
    }
    double d4 = this.alt == 1 ? this.Alpha / 2.0D : this.Alpha;
    double d5 = F.quantile(1.0D - d4, d1, d2);
    this.Power = (1.0D - F.cdf(d5 / d3, d1, d2));
    if (this.alt == 1)
    {
      d5 = F.quantile(1.0D - d4, d2, d1);
      this.Power += 1.0D - F.cdf(d5 * d3, d2, d1);
    }
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "TwoVarGUIHelp.txt", "Power analysis help: Comparing two variances", 25, 60);
  }
  
  public TwoVarGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    TwoVarGUI localTwoVarGUI = new TwoVarGUI();
  }
}
