package pc.stat.dist;

import pc.util.UniFunction;

class NormalAux
  extends UniFunction
{
  public double of(double paramDouble)
  {
    return Normal.cdf(paramDouble);
  }
}
