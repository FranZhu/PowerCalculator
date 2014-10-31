package pc.stat.dist;

import pc.util.UniFunction;

class Chi2Aux2
  extends UniFunction
{
  private double df;
  private double alpha;
  
  public Chi2Aux2(double paramDouble1, double paramDouble2)
  {
    this.df = paramDouble1;
    this.alpha = paramDouble2;
    this.xMin = 0.0D;
    this.closedMin = true;
    this.xeps = 1.E-005D;
  }
  
  public double of(double paramDouble)
  {
    return Chi2.power(paramDouble, this.df, this.alpha);
  }
}
