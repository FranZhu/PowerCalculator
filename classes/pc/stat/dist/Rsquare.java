package pc.stat.dist;

import pc.util.MoreMath;
import pc.util.Solve;
import pc.util.Utility;

public class Rsquare
{
  static double errtol = 1.0E-010D;
  static int maxiter = 1000;
  
  public static double cdf(double paramDouble1, double paramDouble2, int paramInt, double paramDouble3)
  {
    if ((paramInt < 1) || (paramDouble2 < paramInt))
    {
      Utility.warning("Rsquare.cdf: illegal values of N or p");
      return (0.0D / 0.0D);
    }
    if (paramDouble3 < 0.0D)
    {
      Utility.warning("Rsquare.cdf: rho2 must be nonnegative");
      return (0.0D / 0.0D);
    }
    if (paramDouble1 <= 0.0D) {
      return 0.0D;
    }
    if (paramDouble1 >= 1.0D) {
      return 1.0D;
    }
    double d1 = paramDouble2 - 1.0D;
    int i = (int)(d1 * paramDouble3 / (2.0D * (1.0D - paramDouble3)));
    double d2 = (paramInt - 1.0D) / 2.0D + i;
    double d3 = (d1 - paramInt + 1.0D) / 2.0D;
    double d4 = Beta.cdf(paramDouble1, d2, d3);
    if (paramDouble3 < 1.0E-012D) {
      return d4;
    }
    double d5 = d4;
    double d6 = Math.exp((d2 - 1.0D) * Math.log(paramDouble1) + d3 * Math.log(1.0D - paramDouble1) + MoreMath.logGamma(d2 + d3 - 1.0D) - MoreMath.logGamma(d2) - MoreMath.logGamma(d3));
    

    double d7 = d6 * (d2 + d3 - 1.0D) * paramDouble1 / d2;
    double d8 = Math.exp(MoreMath.logGamma(d1 / 2.0D + i) - MoreMath.logGamma(i + 1) - MoreMath.logGamma(d1 / 2.0D) + i * Math.log(paramDouble3) + d1 / 2.0D * Math.log(1.0D - paramDouble3));
    

    double d9 = d8;
    double d10 = 1.0D - d8;
    double d12 = d8 * d4;
    int j = 0;
    for (int k = 1; j == 0; k++)
    {
      d6 *= (d2 + d3 + k - 2.0D) * paramDouble1 / (d2 + k - 1.0D);
      d4 -= d6;
      d8 = d8 * (d1 / 2.0D + i + k - 1.0D) * paramDouble3 / (i + k);
      d12 += d8 * d4;
      double d11 = d10 * d4;
      d10 -= d8;
      if (k > i)
      {
        if ((d11 < errtol) || (k > maxiter)) {
          j = 1;
        }
      }
      else
      {
        d7 *= (d2 - k + 1.0D) / (paramDouble1 * (d2 + d3 - k));
        d5 += d7;
        d9 *= (i - k + 1) / (paramDouble3 * (d1 / 2.0D + i - k));
        d12 += d9 * d5;
        d10 -= d9;
        if ((d10 < errtol) || (k > maxiter)) {
          j = 1;
        }
      }
      if (k > maxiter) {
        Utility.warning("Convergence failure in Rsquare.cdf");
      }
    }
    return d12;
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, int paramInt)
  {
    return cdf(paramDouble1, paramDouble2, paramInt, 0.0D);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, int paramInt, double paramDouble3)
  {
    if ((paramInt < 1) || (paramDouble2 < paramInt))
    {
      Utility.warning("Rsquare.quantile: illegal values of N or p");
      return (0.0D / 0.0D);
    }
    if (paramDouble3 < 0.0D)
    {
      Utility.warning("Rsquare.quantile: rho2 must be nonnegative");
      return (0.0D / 0.0D);
    }
    paramDouble1 = paramDouble1 > 1.0D ? 1.0D : paramDouble1 < 0.0D ? 0.0D : paramDouble1;
    if (paramDouble1 * (1.0D - paramDouble1) == 0.0D) {
      return paramInt;
    }
    double d1 = paramDouble3 / (1.0D - paramDouble3);
    double d2 = Beta.quantile(paramDouble1, paramInt, paramDouble2 - paramInt - 1.0D, d1 * (paramDouble2 - 1.0D));
    RsqAux localRsqAux = new RsqAux(paramDouble2, paramInt, paramDouble3);
    return Solve.search(localRsqAux, paramDouble1, d2, 0.01D);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, int paramInt)
  {
    return quantile(paramDouble1, paramDouble2, paramInt, 0.0D);
  }
}
