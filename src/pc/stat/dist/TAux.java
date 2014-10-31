package pc.stat.dist;

import pc.util.UniFunction;

class TAux
  extends UniFunction
{
  private double df;
  private double delta;
  
  public TAux(double paramDouble1, double paramDouble2)
  {
    this.df = paramDouble1;
    this.delta = paramDouble2;
  }
  
  public double of(double paramDouble)
  {
    return T.cdf(paramDouble, this.df, this.delta);
  }
}
