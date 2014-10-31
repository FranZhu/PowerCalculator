package pc.powercalculator.apps;

import java.awt.Component;
import pc.powercalculator.PowerCalculator;
import pc.powercalculator.PowerCalculatorAux;
import pc.stat.dist.Beta;
import pc.stat.dist.Binomial;
import pc.stat.dist.Normal;

public class OnePGUI
  extends PowerCalculator
{
  private static String title = "Sample size for one proportion";
  private Component sizeComp;
  public double p0;
  public double p;
  public double n;
  public double Alpha;
  public double Power;
  public double Size;
  public int Method;
  public int alt;
  
  public void gui()
  {
    interval("p0", "Null value (p0)", 0.5D, 0.0D, 1.0D);
    interval("p", "Actual value (p)", 0.6D, 0.0D, 1.0D);
    bar("n", "Sample size", 50.0D);
    beginSubpanel(2, false);
    choice("alt", "Alternative", new String[] { "p < p0", "p != p0", "p > p0" }, 1);
    choice("Alpha", new double[] { 0.005D, 0.01D, 0.02D, 0.05D, 0.1D, 0.2D }, 3);
    choice("Method", new String[] { "Exact", "Normal approx", "Beta approx", "Exact (Wald CV)" }, 1);
    otext("Size", "Size", 0.06D);
    endSubpanel();
    interval("Power", 0.0D, 0.0D, 1.0D);
    
    this.sizeComp = ((Component)getComponent("Size"));
    this.sizeComp.setVisible(false);
    
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  public void click()
  {
    double[] arrayOfDouble = new double[2];
    int i = this.alt - 1;
    this.n = PowerCalculator.max(2.0D, PowerCalculator.round(this.n));
    this.p0 = PowerCalculator.min(PowerCalculator.max(this.p0, 0.01D), 0.99D);
    this.p = PowerCalculator.min(PowerCalculator.max(this.p, 0.01D), 0.99D);
    switch (this.Method)
    {
    case 0: 
      arrayOfDouble = Binomial.power(this.p0, this.p, (int)this.n, i, this.Alpha);
      this.Power = arrayOfDouble[0];
      this.Size = arrayOfDouble[1];
      break;
    case 1: 
      double d1 = PowerCalculator.sqrt(this.p0 * (1.0D - this.p0) / this.n);
      double d2 = PowerCalculator.sqrt(this.p * (1.0D - this.p) / this.n);
      this.Power = nPower(this.p0, d1, this.p, d2, i, this.Alpha);
      this.Size = this.Alpha;
      break;
    case 2: 
      this.Power = bPower((this.n - 1.0D) * this.p0, (this.n - 1.0D) * (1.0D - this.p0), (this.n - 1.0D) * this.p, (this.n - 1.0D) * (1.0D - this.p), i, this.Alpha);
      
      this.Size = this.Alpha;
      break;
    case 3: 
      arrayOfDouble = Binomial.waldPower(this.p0, this.p, (int)this.n, i, this.Alpha);
      this.Power = arrayOfDouble[0];
      this.Size = arrayOfDouble[1];
    }
  }
  
  public void Method_changed()
  {
    this.sizeComp.setVisible((this.Method == 0) || (this.Method == 3));
    click();
  }
  
  double nPower(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt, double paramDouble5)
  {
    double d1;
    if (paramInt > 0)
    {
      d1 = Normal.quantile(1.0D - paramDouble5, paramDouble1, paramDouble2);
      return 1.0D - Normal.cdf(d1, paramDouble3, paramDouble4);
    }
    if (paramInt < 0)
    {
      d1 = Normal.quantile(paramDouble5, paramDouble1, paramDouble2);
      return Normal.cdf(d1, paramDouble3, paramDouble4);
    }
    double d2 = Normal.quantile(paramDouble5 / 2.0D, paramDouble1, paramDouble2);
    double d3 = Normal.quantile(1.0D - paramDouble5 / 2.0D, paramDouble1, paramDouble2);
    return 1.0D + Normal.cdf(d2, paramDouble3, paramDouble4) - Normal.cdf(d3, paramDouble3, paramDouble4);
  }
  
  double bPower(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, int paramInt, double paramDouble5)
  {
    double d1;
    if (paramInt > 0)
    {
      d1 = Beta.quantile(1.0D - paramDouble5, paramDouble1, paramDouble2);
      return 1.0D - Beta.cdf(d1, paramDouble3, paramDouble4);
    }
    if (paramInt < 0)
    {
      d1 = Beta.quantile(paramDouble5, paramDouble1, paramDouble2);
      return Beta.cdf(d1, paramDouble3, paramDouble4);
    }
    double d2 = Beta.quantile(paramDouble5 / 2.0D, paramDouble1, paramDouble2);
    double d3 = Beta.quantile(1.0D - paramDouble5 / 2.0D, paramDouble1, paramDouble2);
    return 1.0D + Beta.cdf(d2, paramDouble3, paramDouble4) - Beta.cdf(d3, paramDouble3, paramDouble4);
  }
  
  public void Power_changed()
  {
    this.Power = PowerCalculator.min(0.99D, PowerCalculator.max(this.Alpha, this.Power));
    PowerCalculatorAux localPowerCalculatorAux = new PowerCalculatorAux("n", "Power", this);
    localPowerCalculatorAux.closedMin = true;
    localPowerCalculatorAux.xMin = 2.0D;
    localPowerCalculatorAux.xeps = 0.5D;
    
    this.n = solve(localPowerCalculatorAux, this.Power, this.n, 20.0D);
    click();
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "OnePGUIHelp.txt", "Power analysis help: Test of one proportion", 25, 60);
  }
  
  public OnePGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    OnePGUI localOnePGUI = new OnePGUI();
  }
}
