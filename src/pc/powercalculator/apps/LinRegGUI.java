package pc.powercalculator.apps;

import java.awt.Color;
import pc.powercalculator.PowerCalculator;
import pc.stat.dist.T;

public class LinRegGUI
  extends PowerCalculator
{
  private static String title = "Linear Regression";
  public double k;
  public double n;
  public double sdx;
  public double vif;
  public double beta;
  public double sderr;
  public double alpha;
  public double power;
  public int tt;
  public int opt;
  
  public void gui()
  {
    setBackground(new Color(230, 230, 230));
    beginSubpanel(1, Color.blue.darker());
    
    slider("k", "No. of predictors", 1.0D, 1.0D, 8.0D, 1, true, false, true);
    bar("sdx", "SD of x[j]", 1.0D);
    slider("vif", "VIF[j]", 1.0D, 1.0D, 10.0D, 4, true, false, true);
    endSubpanel();
    bar("alpha", "Alpha", 0.05D);
    checkbox("tt", "Two-tailed", 1);
    
    newColumn();
    
    bar("sderr", "Error SD", 1.0D);
    bar("beta", "Detectable beta[j]", 1.0D);
    bar("n", "Sample size", 10.0D);
    beginSubpanel(1, Color.blue.darker());
    interval("power", "Power", 0.5D, 0.0D, 1.0D);
    choice("opt", "Solve for", new String[] { "Sample size", "Detectable beta[j]" }, 0);
    
    endSubpanel();
    
    menuItem("localHelp", "Regression dialog help", this.helpMenu);
  }
  
  public void click()
  {
    this.k = round(max(1.0D, this.k));
    this.n = round(max(this.k + 2.0D, this.n));
    this.alpha = min(0.999D, max(0.001D, this.alpha));
    if (this.k > 1.5D)
    {
      this.vif = max(1.0D, this.vif);
      setVisible("vif", true);
    }
    else
    {
      this.vif = 1.0D;
      setVisible("vif", false);
    }
    calcPower();
  }
  
  public void power_changed()
  {
    this.power = min(0.999D, max(0.001D, this.power));
    double d1;
    switch (this.opt)
    {
    case 0: 
      this.beta = max(0.001D * this.sderr / this.sdx, this.beta);
      double d2 = this.vif * pow(this.sderr / (this.beta * this.sdx), 2.0D);
      for (int i = 0; i < 3; i++)
      {
        d1 = T.delta(this.power, this.n - this.k - 1.0D, 1 - this.tt, this.alpha);
        this.n = max(this.k + 1.0D, d2 * d1 * d1);
      }
      this.n = round(this.n);
      break;
    case 1: 
      d1 = T.delta(this.power, this.n - this.k - 1.0D, 1 - this.tt, this.alpha);
      this.beta = (d1 * sqrt(this.vif / this.n) * this.sderr / this.sdx);
    }
    calcPower();
  }
  
  private void calcPower()
  {
    double d1 = sqrt(this.vif / this.n) * this.sderr / this.sdx;
    double d2 = this.beta / d1;
    double d3 = this.n - this.k - 1.0D;
    this.power = T.power(d2, d3, 1 - this.tt, this.alpha);
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "LinRegGUIHelp.txt", "Power analysis help: Linear regression", 25, 60);
  }
  
  public LinRegGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new LinRegGUI();
  }
}
