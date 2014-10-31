package pc.stat.dist;

import pc.util.UniFunction;

class TAux2
  extends UniFunction
{
  private double df;
  private double critval;
  private int tail;
  
  public TAux2(int paramInt, double paramDouble1, double paramDouble2)
  {
    this.tail = paramInt;
    this.df = paramDouble2;
    if (paramInt > 0) {
      this.critval = T.quantile(1.0D - paramDouble1, paramDouble2);
    } else {
      this.critval = T.quantile(1.0D - 0.5D * paramDouble1, paramDouble2);
    }
  }
  
  public double of(double paramDouble)
  {
    if (this.tail > 0) {
      return 1.0D - T.cdf(this.critval, this.df, paramDouble);
    }
    return T.cdf(-this.critval, this.df, paramDouble) + 1.0D - T.cdf(this.critval, this.df, paramDouble);
  }
}
