package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Normal;

public class OnePCIGUI
  extends PowerCalculator
{
  private static String title = "CI for a proportion";
  public double conf;
  public double N;
  public double n;
  public double pi;
  public double Sigma = 0.5D;
  public double ME;
  public double zCrit = 1.96D;
  public int isFinite;
  public int worstCase;
  
  public void gui()
  {
    beginSubpanel(2);
    checkbox("isFinite", "Finite population", 1);
    field("N", "N", 1000.0D, 8, 6);
    checkbox("worstCase", "Worst case", 1);
    field("pi", 0.5D);
    endSubpanel();
    choice("conf", "Confidence", new double[] { 0.9D, 0.95D, 0.98D, 0.99D, 0.995D }, 1);
    bar("ME", "Margin of Error", 0.09297D);
    bar("n", 25.0D);
    
    menuItem("localHelp", "This dialog", this.helpMenu);
    
    this.n = 100.0D;
    this.conf = 0.95D;
  }
  
  public void click()
  {
    this.zCrit = Normal.quantile(1.0D - (1.0D - this.conf) / 2.0D);
    this.worstCase = (abs(this.pi - 0.5D) < 1.0E-012D ? 1 : 0);
    this.Sigma = sqrt(this.pi * (1.0D - this.pi));
    this.ME = max(this.ME, 0.001D * this.Sigma);
    for (int i = 0; i < 3; i++)
    {
      this.n = (this.zCrit * this.Sigma / this.ME);
      this.n *= this.n;
      if (this.isFinite == 1) {
        this.n = (1.0D + this.n / (1.0D + this.n / this.N));
      }
      this.n = max(2.0D, this.n);
    }
  }
  
  public void worstCase_changed()
  {
    if (this.worstCase == 1) {
      this.pi = 0.5D;
    }
    click();
  }
  
  public void n_changed()
  {
    this.N = max(2.0D, round(this.N));
    this.n = max(2.0D, round(this.n));
    this.zCrit = Normal.quantile(1.0D - (1.0D - this.conf) / 2.0D);
    this.ME = (this.zCrit * this.Sigma / sqrt(this.n - 1.0D));
    if (this.isFinite == 1) {
      this.ME *= sqrt(1.0D - this.n / this.N);
    }
  }
  
  public void isFinite_changed()
  {
    setVisible("N", this.isFinite == 1);
    click();
  }
  
  public void localHelp()
  {
    showText(OnePCIGUI.class, "OnePCIGUIHelp.txt", "Help: One-sample CI for a mean", 25, 60);
  }
  
  public OnePCIGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new OnePCIGUI();
  }
}
