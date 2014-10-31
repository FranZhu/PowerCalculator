package pc.stat.dist;

import pc.util.MoreMath;
import pc.util.NumAnal;
import pc.util.Solve;
import pc.util.Utility;

public class Chi2
{
  public static double cdf(double paramDouble1, double paramDouble2)
  {
    return cdf(paramDouble1, paramDouble2, 0.0D);
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d12 = 1.0E-008D;
    int j = 500;
    if (paramDouble1 <= 0.0D) {
      return 0.0D;
    }
    if (paramDouble2 < 0.01D)
    {
      Utility.warning("Chi2.cdf: df must be positive");
      return (0.0D / 0.0D);
    }
    if (paramDouble3 < 0.0D)
    {
      Utility.warning("Chi2.cdf: lambda must be nonnegative");
      return (0.0D / 0.0D);
    }
    double d4 = 0.0D;
    double d1 = paramDouble1 / 2.0D;
    double d2 = paramDouble2 / 2.0D;
    double d6 = Math.exp(d2 * Math.log(d1) - d1 - MoreMath.logGamma(d2));
    int i = 1;
    double d7;
    double d10;
    double d3;
    if (paramDouble1 / paramDouble2 > 1.0D)
    {
      d7 = 0.0D;
      double d8 = d6 / d1;
      d10 = 1.0D / d1;
      do
      {
        i++;
        double d9;
        double d11;
        if (2 * (i / 2) < i)
        {
          d9 = (i - 1.0D) / 2.0D;
          d11 = d1;
        }
        else
        {
          d9 = (i - paramDouble2) / 2.0D;
          d11 = 1.0D;
        }
        d10 = 1.0D / (d9 * d10 + d11);
        d3 = d8;
        d8 = d10 * (d9 * d7 + d11 * d8);
        d7 = d10 * d3;
        d3 = d4;
        d4 = 1.0D - d8;
      } while ((Math.abs(d3 - d4) >= d12) && (i <= j));
      if (i > j) {
        Utility.warning("Chi2.cdf: convergence failure");
      }
    }
    else
    {
      d3 = d6 / d2;
      d4 = d3;
      do
      {
        d3 *= paramDouble1 / (paramDouble2 + 2 * i);
        d4 += d3;
        i++;
      } while ((d3 >= d12) && (i <= j));
      if (i > j) {
        Utility.warning("Chi2.cdf: convergence failure");
      }
    }
    if (paramDouble3 > 0.0D)
    {
      double d5 = Math.exp(-paramDouble3 / 2.0D);
      d10 = 1.0D - d5;
      d7 = d4 - d6 / d2;
      d6 = d6 * d1 / (d2 * (d2 + 1.0D));
      d2 += 2.0D;
      d4 *= d5;
      i = 0;
      do
      {
        i++;
        d5 *= paramDouble3 / (2 * i);
        d10 -= d5;
        d4 += d5 * d7;
        d7 -= d6;
        d6 *= d1 / d2;
        d2 += 1.0D;
      } while ((d10 * d7 >= d12) && (i <= j));
      if (i > j) {
        Utility.warning("Chi2.cdf: convergence failure");
      }
    }
    return d4;
  }
  
  public static double quantile(double paramDouble1, double paramDouble2)
  {
    return quantile(paramDouble1, paramDouble2, 0.0D);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if ((paramDouble1 < 0.0D) || (paramDouble1 >= 1.0D))
    {
      Utility.warning("Chi2.quantile: p must be in [0,1)");
      return (0.0D / 0.0D);
    }
    if (paramDouble3 < 0.0D)
    {
      Utility.warning("Chi2.quantile: lambda must be nonnegative");
      return (0.0D / 0.0D);
    }
    if (paramDouble2 < 0.01D)
    {
      Utility.warning("Chi2.quantile: df must be positive");
      return (0.0D / 0.0D);
    }
    double d = paramDouble2 + paramDouble3 + Normal.quantile(paramDouble1) * Math.sqrt(2.0D * paramDouble2 + 4.0D * paramDouble3);
    Chi2Aux localChi2Aux = new Chi2Aux(paramDouble2, paramDouble3);
    return Solve.search(localChi2Aux, paramDouble1, d, 0.1D);
  }
  
  public static double power(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if ((paramDouble3 <= 0.0D) || (paramDouble3 >= 1.0D))
    {
      Utility.warning("Chi2.power: alpha must be in (0,1)");
      return (0.0D / 0.0D);
    }
    if (paramDouble1 < 0.0D)
    {
      Utility.warning("Chi2.power: lambda must be nonnegative");
      return (0.0D / 0.0D);
    }
    if (paramDouble2 < 0.01D)
    {
      Utility.warning("Chi2.power: df must be positive");
      return (0.0D / 0.0D);
    }
    double d = quantile(1.0D - paramDouble3, paramDouble2, 0.0D);
    return 1.0D - cdf(d, paramDouble2, paramDouble1);
  }
  
  public static double lambda(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if ((paramDouble3 <= 0.0D) || (paramDouble3 >= 1.0D))
    {
      Utility.warning("Chi2.lambda: alpha must be in (0,1)");
      return (0.0D / 0.0D);
    }
    if ((paramDouble1 <= 0.0D) || (paramDouble1 >= 1.0D))
    {
      Utility.warning("Chi2.lambda: power must be in (0,1)");
      return (0.0D / 0.0D);
    }
    if (paramDouble2 < 0.01D)
    {
      Utility.warning("Chi2.lambda: df must be positive");
      return (0.0D / 0.0D);
    }
    double d1 = Math.pow(Normal.quantile(1.0D - paramDouble1), 2.0D);
    double d2 = quantile(1.0D - paramDouble3, paramDouble2, 0.0D);
    double d3 = 2.0D * (d2 - paramDouble2) + 4.0D * d1;
    double d4 = Math.pow(d2 - paramDouble2, 2.0D) - 2.0D * paramDouble2 * d1;
    double d5 = Math.pow(d3, 2.0D) - 4.0D * d4;
    double d6 = paramDouble1 > 0.5D ? 1.0D : -1.0D;
    double d7 = d5 < 0.0D ? d2 : (d3 + d6 * Math.sqrt(d5)) / 2.0D;
    d7 = Math.max(0.5D, d7);
    Chi2Aux2 localChi2Aux2 = new Chi2Aux2(paramDouble2, paramDouble3);
    return Solve.search(localChi2Aux2, paramDouble1, d7, 0.5D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    saveLambda = paramDouble1;
    saveDf = paramDouble2;
    return NumAnal.integral(Chi2.class, "power", 0.0D, 1.0D, paramDouble3, false, 0.0D, 1.0D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2)
  {
    return rocArea(paramDouble1, paramDouble2, 0.0001D);
  }
  
  public static double power(double paramDouble)
  {
    return power(saveLambda, saveDf, paramDouble);
  }
  
  private static double saveDf = 1.0D;
  private static double saveLambda = 0.0D;
}
