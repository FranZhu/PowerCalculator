package pc.stat.dist;

import pc.util.UniFunction;

class Chi2Aux
  extends UniFunction
{
  private double df;
  private double lambda;
  
  public Chi2Aux(double paramDouble1, double paramDouble2)
  {
    this.df = paramDouble1;
    this.lambda = paramDouble2;
    this.xMin = 0.0D;
    this.closedMin = true;
    this.xeps = 1.E-005D;
  }
  
  public double of(double paramDouble)
  {
    return Chi2.cdf(paramDouble, this.df, this.lambda);
  }
}
