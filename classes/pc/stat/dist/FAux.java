package pc.stat.dist;

import pc.util.UniFunction;

class FAux
  extends UniFunction
{
  private double df1;
  private double df2;
  private double alpha;
  
  public FAux(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    this.alpha = paramDouble3;
    this.df1 = paramDouble1;
    this.df2 = paramDouble2;
    this.xMin = 0.0D;this.closedMin = true;
    this.xeps = 1.E-005D;
  }
  
  public double of(double paramDouble)
  {
    return F.power(paramDouble, this.df1, this.df2, this.alpha);
  }
}
