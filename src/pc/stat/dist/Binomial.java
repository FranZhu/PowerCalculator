package pc.stat.dist;

import pc.util.Solve;
import pc.util.Utility;

public class Binomial
{
  public static double cdf(int paramInt1, int paramInt2, double paramDouble)
  {
    if ((paramDouble < 0.0D) || (paramDouble > 1.0D))
    {
      Utility.warning("Binomial.cdf: p must be in [0,1]");
      return 2147483647.0D;
    }
    if (paramInt1 < 0) {
      return 0.0D;
    }
    if (paramInt1 >= paramInt2) {
      return 1.0D;
    }
    return Beta.cdf(1.0D - paramDouble, 0.0D + paramInt2 - paramInt1, paramInt1 + 1.0D);
  }
  
  public static int quantile(double paramDouble1, int paramInt, double paramDouble2)
  {
    if ((paramDouble1 < 0.0D) || (paramDouble1 > 1.0D))
    {
      Utility.warning("Binomial.quantile: alpha must be in [0,1]");
      return 2147483647;
    }
    if ((paramDouble2 < 0.0D) || (paramDouble2 > 1.0D))
    {
      Utility.warning("Binomial.quantile: p must be in [0,1]");
      return 2147483647;
    }
    int i = (int)(paramInt * paramDouble2 + Normal.quantile(paramDouble1) * Math.sqrt(paramInt * paramDouble2 * (1.0D - paramDouble2)));
    i = Math.max(-1, Math.min(paramInt, i));
    double d = cdf(i, paramInt, paramDouble2);
    if (d < paramDouble1)
    {
      do
      {
        i++;d = cdf(i, paramInt, paramDouble2);
      } while (d < paramDouble1 - 1.0E-006D);
      i--;
    }
    else if (d > paramDouble1)
    {
      do
      {
        i--;d = cdf(i, paramInt, paramDouble2);
      } while (d > paramDouble1 + 1.0E-006D);
    }
    return i;
  }
  
  public static double[] power(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, double paramDouble3)
  {
    int i;
    double d1;
    double d2;
    if (paramInt2 < 0)
    {
      i = quantile(paramDouble3, paramInt1, paramDouble1);
      d1 = cdf(i, paramInt1, paramDouble1);
      d2 = cdf(i, paramInt1, paramDouble2);
    }
    else if (paramInt2 == 0)
    {
      i = quantile(paramDouble3 / 2.0D, paramInt1, paramDouble1);
      d1 = cdf(i, paramInt1, paramDouble1);
      d2 = cdf(i, paramInt1, paramDouble2);
      i = 1 + quantile(1.0D - paramDouble3 / 2.0D, paramInt1, paramDouble1);
      d1 += 1.0D - cdf(i, paramInt1, paramDouble1);
      d2 += 1.0D - cdf(i, paramInt1, paramDouble2);
    }
    else
    {
      i = 1 + quantile(1.0D - paramDouble3, paramInt1, paramDouble1);
      d1 = 1.0D - cdf(i, paramInt1, paramDouble1);
      d2 = 1.0D - cdf(i, paramInt1, paramDouble2);
    }
    return new double[] { d2, d1 };
  }
  
  public static double[] waldPower(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2, double paramDouble3)
  {
    double d1 = 0.0D;double d2 = 0.0D;
    double d6 = paramInt2 == 0 ? paramDouble3 / 2.0D : paramDouble3;
    

    double d7 = Normal.quantile(1.0D - d6);
    BinomialAux localBinomialAux;
    double d4;
    double d5;
    double d3;
    int i;
    if (paramInt2 <= 0)
    {
      localBinomialAux = new BinomialAux(paramDouble1, 4.0D + paramInt1);
      d4 = Math.max(paramDouble1 - d7 * Math.sqrt(paramDouble1 * (1.0D - paramDouble1) / paramInt1), 0.0D);
      d5 = Math.min(0.01D, 1.0D - d4);
      d3 = Solve.search(localBinomialAux, -d7, d4, d5);
      i = (int)Math.floor((paramInt1 + 4) * d3 - 2.0D);
      d2 = cdf(i, paramInt1, paramDouble2);
      d1 = cdf(i, paramInt1, paramDouble1);
    }
    if (paramInt2 >= 0)
    {
      localBinomialAux = new BinomialAux(1.0D - paramDouble1, 4.0D + paramInt1);
      d4 = Math.max(1.0D - paramDouble1 - d7 * Math.sqrt(paramDouble1 * (1.0D - paramDouble1) / paramInt1), 0.0D);
      d5 = Math.min(0.01D, 1.0D - d4);
      d3 = Solve.search(localBinomialAux, -d7, d4, d5);
      i = (int)Math.floor((paramInt1 + 4) * d3 - 2.0D);
      d2 += cdf(i, paramInt1, 1.0D - paramDouble2);
      d1 += cdf(i, paramInt1, 1.0D - paramDouble1);
    }
    return new double[] { d2, d1 };
  }
}
