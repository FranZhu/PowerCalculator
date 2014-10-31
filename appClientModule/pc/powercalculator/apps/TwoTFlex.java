package pc.powercalculator.apps;

import java.awt.Color;
import java.awt.Component;
import pc.powercalculator.PiComponent;
import pc.powercalculator.PowerCalculator;
import pc.powercalculator.PowerCalculatorAux;
import pc.stat.dist.T;

public class TwoTFlex
  extends PowerCalculator
{
  private static String title = "Two-sample t test (general case)";
  private PiComponent threshField;
  public double sigma1;
  public double sigma2;
  public double n1;
  public double n2;
  public double diff;
  public double thresh;
  public double alpha;
  public double df;
  public double power;
  public double v1;
  public double v2;
  public double delta;
  public double mult;
  public double saveN1;
  public double saveN2;
  public int eqs;
  public int alloc;
  public int tt;
  public int prevTT;
  public int opt;
  public int equiv;
  
  public void gui()
  {
    setBackground(new Color(230, 230, 230));
    beginSubpanel(1, Color.blue.darker());
    bar("sigma1", 1.0D);
    bar("sigma2", 1.0D);
    checkbox("eqs", "Equal sigmas", 1);
    endSubpanel();
    filler();
    beginSubpanel(1, Color.blue.darker());
    bar("n1", 25.0D);
    bar("n2", 25.0D);
    choice("alloc", "Allocation", new String[] { "Independent", "Equal", "Optimal" }, 1);
    
    endSubpanel();
    
    newColumn();
    
    ofield("df", "Degrees of freedom", this.n1 + this.n2 - 2.0D);
    beginSubpanel(2);
    checkbox("tt", "Two-tailed", 1);
    choice("alpha", new double[] { 0.005D, 0.01D, 0.02D, 0.05D, 0.1D, 0.2D }, 3);
    checkbox("equiv", "Equivalence", 0);
    field("thresh", "Threshhold", 1.0D);
    endSubpanel();
    filler();
    bar("diff", "True difference of means", 0.5D);
    beginSubpanel(1, Color.blue.darker());
    interval("power", 0.5D, 0.0D, 1.0D);
    choice("opt", "Solve for", new String[] { "Sample size", "Diff of means" }, 0);
    
    endSubpanel();
    
    this.threshField = getComponent("thresh");
    this.prevTT = this.tt;
    equiv_changed();
    
    menuItem("localHelp", "t test info", this.helpMenu);
  }
  
  public void click()
  {
    this.n1 = max(2.0D, round(this.n1));
    if (this.eqs == 1) {
      this.sigma2 = this.sigma1;
    }
    if ((this.eqs == 1) || (this.alloc == 1)) {
      this.n2 = this.n1;
    } else if (this.alloc == 2) {
      this.n2 = max(2.0D, round(this.n1 * this.sigma2 / this.sigma1));
    }
    sattPower();
  }
  
  public void sigma2_changed()
  {
    if (this.eqs == 1) {
      this.sigma1 = this.sigma2;
    }
    if (this.alloc == 2) {
      this.n2 = max(2.0D, round(this.n1 * this.sigma2 / this.sigma1));
    }
    sattPower();
  }
  
  public void n2_changed()
  {
    this.n2 = max(2.0D, round(this.n2));
    if ((this.eqs == 1) || (this.alloc == 1)) {
      this.n1 = this.n2;
    } else if (this.alloc == 2) {
      this.n1 = max(2.0D, round(this.n2 * this.sigma1 / this.sigma2));
    }
    sattPower();
  }
  
  public void sattPower()
  {
    this.v1 = (this.sigma1 * this.sigma1 / this.n1);
    this.v2 = (this.sigma2 * this.sigma2 / this.n2);
    double d = sqrt(this.v1 + this.v2);
    this.delta = (this.diff / d);
    this.df = ((this.v1 + this.v2) * (this.v1 + this.v2) / (this.v1 * this.v1 / (this.n1 - 1.0D) + this.v2 * this.v2 / (this.n2 - 1.0D)));
    if (this.equiv == 0) {
      this.power = T.power(this.delta, this.df, 1 - this.tt, this.alpha);
    } else {
      this.power = T.powerEquiv(this.diff, this.thresh, d, this.df, this.alpha);
    }
  }
  
  public void power_changed()
  {
    if (this.equiv == 1)
    {
      power_changed_equiv();
      return;
    }
    switch (this.opt)
    {
    case 0: 
      double d = this.power;
      this.diff = max(this.diff, 0.01D * (this.sigma1 + this.sigma2));
      for (int i = 0; i < 3; i++)
      {
        this.delta = T.delta(d, this.df, 1 - this.tt, this.alpha);
        this.mult = ((this.v1 + this.v2) * this.delta * this.delta / this.diff / this.diff);
        this.n1 *= this.mult;
        this.n2 *= this.mult;
        sattPower();
      }
      this.n1 = max(round(this.n1), 2.0D);
      this.n2 = max(round(this.n2), 2.0D);
      sattPower();
      break;
    case 1: 
      this.delta = T.delta(this.power, this.df, 1 - this.tt, this.alpha);
      this.diff = (this.delta * sqrt(this.v1 + this.v2));
      sattPower();
    }
  }
  
  public void power_changed_equiv()
  {
    switch (this.opt)
    {
    case 0: 
      this.saveN1 = this.n1;
      this.saveN2 = this.n2;
      PowerCalculatorAux localPowerCalculatorAux1 = new PowerCalculatorAux("mult", "power", this);
      localPowerCalculatorAux1.xMin = max(2.0D / this.n1, 2.0D / this.n2);
      localPowerCalculatorAux1.closedMin = true;
      localPowerCalculatorAux1.xeps = max(0.5D / this.n1, 0.5D / this.n2);
      this.mult = solve(localPowerCalculatorAux1, this.power, 1.0D, 0.1D);
      this.n1 = max(round(this.n1 * this.mult), 2.0D);
      this.n2 = max(round(this.n2 * this.mult), 2.0D);
      break;
    case 1: 
      PowerCalculatorAux localPowerCalculatorAux2 = new PowerCalculatorAux("diff", "power", this);
      localPowerCalculatorAux2.xeps = (0.005D * (this.sigma1 + this.sigma2));
      this.diff = Math.max(this.diff, 0.1D * (this.sigma1 + this.sigma2));
      this.diff = solve(localPowerCalculatorAux2, this.power, this.diff, 0.1D * this.diff);
    }
    sattPower();
  }
  
  public void mult_changed()
  {
    this.n1 = (this.saveN1 * this.mult);
    this.n2 = (this.saveN2 * this.mult);
    sattPower();
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "TwoTFlexHelp.txt", "Power analysis help: Two-sample t test", 25, 60);
  }
  
  public void equiv_changed()
  {
    ((Component)this.threshField).setVisible(this.equiv == 1);
    if (this.equiv == 1)
    {
      this.prevTT = this.tt;
      this.tt = 1;
    }
    else
    {
      this.tt = this.prevTT;
    }
    sattPower();
  }
  
  public TwoTFlex()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TwoTFlex();
  }
}
