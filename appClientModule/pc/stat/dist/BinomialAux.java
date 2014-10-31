package pc.stat.dist;

import pc.util.UniFunction;

class BinomialAux
  extends UniFunction
{
  private double p0;
  private double n;
  
  public BinomialAux(double paramDouble1, double paramDouble2)
  {
    this.p0 = paramDouble1;
    this.n = paramDouble2;
    this.closedMin = (this.closedMax = true);
    this.xMin = 0.0D;
    this.xMax = 1.0D;
  }
  
  public double of(double paramDouble)
  {
    paramDouble = Math.max(0.0D, Math.min(1.0D, paramDouble));
    double d1 = Math.max(Math.abs(paramDouble - this.p0), 0.01D);
    double d2 = Math.max(Math.sqrt(paramDouble * (1.0D - paramDouble) / this.n), d1 / 1000.0D);
    return (paramDouble - this.p0) / d2;
  }
}
