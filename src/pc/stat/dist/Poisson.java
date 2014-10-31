package pc.stat.dist;

import pc.util.Utility;

public class Poisson
{
  public static double cdf(int paramInt, double paramDouble)
  {
    if (paramInt < 0) {
      return 0.0D;
    }
    if (paramDouble <= 0.0D)
    {
      Utility.warning("Poisson.cdf: lambda must be positive");
      return (0.0D / 0.0D);
    }
    return 1.0D - Chi2.cdf(2.0D * paramDouble, 2.0D * (paramInt + 1.0D));
  }
  
  public static int quantile(double paramDouble1, double paramDouble2)
  {
    if (paramDouble2 <= 0.0D)
    {
      Utility.warning("Poisson.quantile: lambda must be positive");
      return 2147483647;
    }
    if ((paramDouble1 < 0.0D) || (paramDouble1 >= 1.0D))
    {
      Utility.warning("Poisson.quantile: alpha must be in [0,1)");
      return 2147483647;
    }
    if (paramDouble1 == 0.0D) {
      return -1;
    }
    int i = (int)(paramDouble2 + Normal.quantile(paramDouble1) * Math.sqrt(paramDouble2));
    i = Math.max(-1, i);
    double d = cdf(i, paramDouble2);
    if (d < paramDouble1)
    {
      do
      {
        i++;d = cdf(i, paramDouble2);
      } while (d < paramDouble1 - 1.0E-006D);
      i--;
    }
    else if (d > paramDouble1)
    {
      do
      {
        i--;d = cdf(i, paramDouble2);
      } while (d > paramDouble1 + 1.0E-006D);
    }
    return i;
  }
}
