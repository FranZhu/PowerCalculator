package pc.stat.dist;

import pc.util.UniFunction;

class RsqAux
  extends UniFunction
{
  private double N;
  private double rho2;
  private int p;
  
  public RsqAux(double paramDouble1, int paramInt, double paramDouble2)
  {
    this.N = paramDouble1;
    this.p = paramInt;
    this.rho2 = paramDouble2;
    this.xMin = 0.0D;
    this.xMax = 1.0D;
    this.closedMin = true;
    this.closedMax = true;
  }
  
  public double of(double paramDouble)
  {
    return Rsquare.cdf(paramDouble, this.N, this.p, this.rho2);
  }
}
