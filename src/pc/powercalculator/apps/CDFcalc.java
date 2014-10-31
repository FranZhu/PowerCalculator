package pc.powercalculator.apps;

import pc.powercalculator.PowerCalculator;
import pc.stat.dist.Beta;
import pc.stat.dist.Binomial;
import pc.stat.dist.Chi2;
import pc.stat.dist.F;
import pc.stat.dist.Normal;
import pc.stat.dist.Poisson;
import pc.stat.dist.T;
import pc.stat.dist.Tukey;

public class CDFcalc
  extends PowerCalculator
{
  private static String title = "Online tables";
  public double x;
  public double Fx;
  public double df1;
  public double df2;
  public double ncp;
  public int dist;
  public int RT;
  private static final int NORMAL = 0;
  private static final int TDIST = 1;
  private static final int FDIST = 2;
  private static final int CHISQ = 3;
  private static final int BETA = 4;
  private static final int TUKEY = 5;
  private static final int BINOMIAL = 6;
  private static final int POISSON = 7;
  private static final double[][] dflts = { { 0.0D, 0.0D, 1.0D }, { 0.0D, 10.0D, 0.008999999999999999D }, { 1.0D, 2.0D, 10.0D }, { 5.0D, 10.0D, 0.008999999999999999D }, { 0.5D, 1.0D, 1.0D }, { 1.0D, 30.0D, 3.0D }, { 5.0D, 10.0D, 0.5D }, { 5.0D, 10.0D, 0.008999999999999999D } };
  
  public void gui()
  {
    choice("dist", "Distribution", new String[] { "Normal", "t", "F", "Chi-square", "Beta", "Studentized range", "Binomial", "Poisson" }, 1);
    


    field("x", "x   ", 0.0D);
    field("Fx", "F(x)", 0.5D);
    checkbox("RT", "Right tail", 0);
    
    newColumn();
    
    beginSubpanel(1, false);
    label("Parameter values...");
    field("df1", "df", 10.0D);
    field("df2", 10.0D);
    field("ncp", 0.0D);
    endSubpanel();
    
    menuItem("localHelp", "Using this calculator", this.helpMenu);
  }
  
  public void click()
  {
    double d = 0.0D;
    switch (this.dist)
    {
    case 0: 
      this.Fx = Normal.cdf(this.x, this.df1, this.df2); break;
    case 1: 
      this.Fx = T.cdf(this.x, this.df1, this.ncp); break;
    case 2: 
      this.Fx = F.cdf(this.x, this.df1, this.df2, this.ncp); break;
    case 3: 
      this.Fx = Chi2.cdf(this.x, this.df1, this.ncp); break;
    case 4: 
      this.Fx = Beta.cdf(this.x, this.df1, this.df2, this.ncp); break;
    case 5: 
      this.Fx = Tukey.cdf(this.x, this.df2, this.df1); break;
    case 6: 
      this.Fx = Binomial.cdf((int)this.x, (int)this.df1, this.df2); break;
    case 7: 
      this.Fx = Poisson.cdf((int)this.x, this.df1);
    }
    if (this.RT > 0) {
      this.Fx = (1.0D - this.Fx);
    }
  }
  
  public void Fx_changed()
  {
    double d = this.RT > 0 ? 1.0D - this.Fx : this.Fx;
    switch (this.dist)
    {
    case 0: 
      this.x = Normal.quantile(d, this.df1, this.df2); break;
    case 1: 
      this.x = T.quantile(d, this.df1, this.ncp); break;
    case 2: 
      this.x = F.quantile(d, this.df1, this.df2, this.ncp); break;
    case 3: 
      this.x = Chi2.quantile(d, this.df1, this.ncp); break;
    case 4: 
      this.x = Beta.quantile(d, this.df1, this.df2, this.ncp); break;
    case 5: 
      this.x = Tukey.quantile(d, this.df2, this.df1); break;
    case 6: 
      this.x = Binomial.quantile(d, (int)this.df1, this.df2);
      this.Fx = (this.Fx = Binomial.cdf((int)this.x, (int)this.df1, this.df2));
      if (this.RT > 0) {
        this.Fx = (1.0D - this.Fx);
      }
      break;
    case 7: 
      this.x = Poisson.quantile(d, this.df1);
      this.Fx = Poisson.cdf((int)this.x, this.df1);
      if (this.RT > 0) {
        this.Fx = (1.0D - this.Fx);
      }
      break;
    }
  }
  
  public void dist_changed()
  {
    setVisible("df2", (this.dist != 1) && (this.dist != 3) && (this.dist != 7));
    setVisible("ncp", (this.dist == 1) || (this.dist == 2) || (this.dist == 3) || (this.dist == 4));
    relabel("df1", "df");relabel("df2", "df2");
    switch (this.dist)
    {
    case 0: 
      relabel("df1", "mu");relabel("df2", "sigma"); break;
    case 4: 
      relabel("df1", "alpha");relabel("df2", "beta"); break;
    case 5: 
      relabel("df1", "df");relabel("df2", "k"); break;
    case 6: 
      relabel("df1", "n");relabel("df2", "p"); break;
    case 7: 
      relabel("df1", "lambda"); break;
    case 2: 
      relabel("df1", "df1");
    }
    this.x = dflts[this.dist][0];
    this.df1 = dflts[this.dist][1];
    this.df2 = dflts[this.dist][2];
    this.ncp = 0.0D;
    click();
  }
  
  public void RT_changed()
  {
    if (this.RT == 0) {
      relabel("Fx", "F(x)");
    } else {
      relabel("Fx", "1 - F(x)");
    }
    this.Fx = (1.0D - this.Fx);
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "CDFcalcHelp.txt", "How to use online tables", 25, 60);
  }
  
  public CDFcalc()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    CDFcalc localCDFcalc = new CDFcalc();
  }
}
