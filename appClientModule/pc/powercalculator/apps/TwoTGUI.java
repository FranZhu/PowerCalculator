package pc.powercalculator.apps;

import java.awt.Color;
import pc.powercalculator.PowerCalculator;
import pc.powercalculator.PowerCalculatorAux;
import pc.stat.dist.T;

public class TwoTGUI
  extends PowerCalculator
{
  private static String title = "Two-sample t test (general case)";
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
  public int rocMeth;
  
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
    
    beginSubpanel(2);
    checkbox("tt", "Two-tailed", 1);
    
    field("alpha", "Alpha", 0.05D);
    checkbox("equiv", "Equivalence", 0);
    field("thresh", "Threshold", 1.0D);
    endSubpanel();
    otext("df", "Degrees of freedom", this.n1 + this.n2 - 2.0D);
    filler();
    bar("diff", "True difference of means", 0.5D);
    beginSubpanel(1, Color.blue.darker());
    interval("power", "Power", 0.5D, 0.0D, 1.0D);
    choice("opt", "Solve for", new String[] { "Sample size", "Diff of means" }, 0);
    
    endSubpanel();
    
    menuCheckbox("rocMeth", "Use integrated power", 0);
    
    this.prevTT = this.tt;
    equiv_changed();
    
    menuItem("localHelp", "t test info", this.helpMenu);
  }
  
  public void click()
  {
    this.n1 = max(2.0D, round(this.n1));
    if (this.equiv == 1) {
      this.tt = 1;
    }
    if (this.eqs == 1) {
      this.sigma2 = this.sigma1;
    }
    if (this.alloc == 1) {
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
    if (this.alloc == 1) {
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
    this.df = (this.eqs == 1 ? this.n1 + this.n2 - 2.0D : (this.v1 + this.v2) * (this.v1 + this.v2) / (this.v1 * this.v1 / (this.n1 - 1.0D) + this.v2 * this.v2 / (this.n2 - 1.0D)));
    if (this.equiv == 0) {
      this.power = (this.rocMeth == 0 ? T.power(this.delta, this.df, 1 - this.tt, this.alpha) : T.rocArea(this.delta, this.df, 1 - this.tt));
    } else {
      this.power = (this.rocMeth == 0 ? T.powerEquiv(this.diff, this.thresh, d, this.df, this.alpha) : T.rocEquiv(this.diff, this.thresh, d, this.df));
    }
  }
  
  public void power_changed()
  {
    if ((this.equiv == 1) || (this.rocMeth == 1))
    {
      power_changed_numerical();
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
  
  public void power_changed_numerical()
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
    showText(AnovaPicker.class, "TwoTGUIHelp.txt", "Power analysis help: Two-sample t test", 25, 60);
  }
  
  public void equiv_changed()
  {
    setVisible("thresh", this.equiv == 1);
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
  
  public void rocMeth_changed()
  {
    if (this.rocMeth == 1) {
      relabel("power", "Integrated power");
    } else {
      relabel("power", "Power");
    }
    setVisible("alpha", this.rocMeth == 0);
    sattPower();
  }
  
  public TwoTGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TwoTGUI();
  }
}
