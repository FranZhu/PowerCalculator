package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Normal;

public class TwoPGUI
  extends PowerCalculator
{
  private static String title = "Test of equality of two proportions";
  public double p1;
  public double p2;
  public double n1;
  public double n2;
  public double Alpha;
  public double Power;
  public int eq;
  public int alt;
  public int cc;
  
  public void gui()
  {
    beginSubpanel(1, true);
    interval("p1", 0.5D, 0.0D, 1.0D);
    interval("p2", 0.6D, 0.0D, 1.0D);
    endSubpanel();
    beginSubpanel(1, true);
    bar("n1", 100.0D);
    bar("n2", 100.0D);
    checkbox("eq", "Equal ns", 1);
    endSubpanel();
    beginSubpanel(1, true);
    bar("Alpha", 0.05D);
    ointerval("Power", 0.5D, 0.0D, 1.0D);
    endSubpanel();
    beginSubpanel(2);
    checkbox("cc", "Continuity corr.", 1);
    choice("alt", "Alternative", new String[] { "p1 < p2", "p1 != p2", "p1 > p2" }, 1);
    

    endSubpanel();
    
    menuItem("localHelp", "This dialog", this.helpMenu);
  }
  
  public void click()
  {
    this.n1 = max(2.0D, round(this.n1));
    if (this.eq == 1) {
      this.n2 = this.n1;
    }
    double d1 = 5.0D / this.n1;double d2 = 5.0D / this.n1;
    this.p1 = min(max(this.p1, d1), 1.0D - d1);
    this.p2 = min(max(this.p2, d2), 1.0D - d2);
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
    double d1 = (this.n1 * this.p1 + this.n2 * this.p2) / (this.n1 + this.n2);
    double d2 = sqrt(d1 * (1.0D - d1) * (1.0D / this.n1 + 1.0D / this.n2));
    double d3 = sqrt(this.p1 * (1.0D - this.p1) / this.n1 + this.p2 * (1.0D - this.p2) / this.n2);
    double d4 = this.p1 - this.p2;
    double d5 = min(abs(d4), 0.5D * (1.0D / this.n1 + 1.0D / this.n2));
    this.Alpha = max(1.0E-006D, min(this.Alpha, 0.999999D));
    if (this.cc == 1) {
      d4 = d4 > 0.0D ? d4 - d5 : d4 + d5;
    }
    this.Power = Normal.power(d4 / d2, this.alt - 1, this.Alpha, d3 / d2);
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "TwoPGUIHelp.txt", "Power analysis help: Comparing two proportions", 25, 60);
  }
  
  public TwoPGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TwoPGUI();
  }
}
