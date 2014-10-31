package pc.stat.dist;

import pc.util.UniFunction;

class BetaAux
  extends UniFunction
{
  private double a;
  private double b;
  private double lambda;
  
  public BetaAux(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    this.a = paramDouble1;
    this.b = paramDouble2;
    this.lambda = paramDouble3;
    this.xMin = 0.0D;this.closedMin = true;
    this.xMax = 1.0D;this.closedMax = true;
  }
  
  public double of(double paramDouble)
  {
    return Beta.cdf(paramDouble, this.a, this.b, this.lambda);
  }
}
