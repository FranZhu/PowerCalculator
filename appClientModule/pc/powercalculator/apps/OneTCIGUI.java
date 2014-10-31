package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Normal;
import pc.stat.dist.T;

public class OneTCIGUI
  extends PowerCalculator
{
  private static String title = "CI for a mean";
  public double conf;
  public double N;
  public double n;
  public double Sigma;
  public double ME;
  public double tCrit;
  public int isFinite;
  
  public void gui()
  {
    beginSubpanel(2);
    checkbox("isFinite", "Finite population", 1);
    field("N", "N", 1000.0D, 8, 6);
    endSubpanel();
    choice("conf", "Confidence", new double[] { 0.9D, 0.95D, 0.98D, 0.99D, 0.995D }, 1);
    bar("Sigma", 1.0D);
    bar("ME", "Margin of Error", 0.2D);
    bar("n", 25.0D);
    
    menuItem("localHelp", "This dialog", this.helpMenu);
    
    this.n = 25.0D;
    this.conf = 0.95D;
    n_changed();
  }
  
  public void click()
  {
    this.tCrit = Normal.quantile(1.0D - (1.0D - this.conf) / 2.0D);
    this.ME = max(this.ME, 0.001D * this.Sigma);
    for (int i = 0; i < 3; i++)
    {
      this.n = (this.tCrit * this.Sigma / this.ME);
      this.n *= this.n;
      if (this.isFinite == 1) {
        this.n /= (1.0D + this.n / this.N);
      }
      this.n = max(2.0D, this.n);
      this.tCrit = T.quantile(1.0D - (1.0D - this.conf) / 2.0D, this.n - 1.0D);
    }
  }
  
  public void n_changed()
  {
    this.N = max(2.0D, round(this.N));
    this.n = max(2.0D, round(this.n));
    this.tCrit = T.quantile(1.0D - (1.0D - this.conf) / 2.0D, this.n - 1.0D);
    this.ME = (this.tCrit * this.Sigma / sqrt(this.n));
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
    showText(OneTCIGUI.class, "OneTCIGUIHelp.txt", "Help: One-sample CI for a mean", 25, 60);
  }
  
  public OneTCIGUI()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new OneTCIGUI();
  }
}
