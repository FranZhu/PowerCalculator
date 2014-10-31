package pc.stat.dist;

import pc.util.UniFunction;

class NormalAux2
  extends UniFunction
{
  private double alpha;
  
  public NormalAux2(double paramDouble)
  {
    this.alpha = paramDouble;
    this.xeps = 1.E-005D;
  }
  
  public double of(double paramDouble)
  {
    return Normal.power(paramDouble, 0, this.alpha);
  }
}
