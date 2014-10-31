package pc.stat.dist;

import pc.util.NumAnal;
import pc.util.Solve;
import pc.util.Utility;

public class Normal
{
  private static double saveMu1 = 0.0D;
  private static int saveTail = 0;
  
  public static double cdf(double paramDouble)
  {
    double d1 = 0.2316419D;double d2 = 0.31938153D;double d3 = -0.356563782D;
    double d4 = 1.781477937D;double d5 = -1.821255978D;double d6 = 1.330274429D;
    if (paramDouble > 0.0D) {
      return 1.0D - cdf(-paramDouble);
    }
    double d7;
    if (paramDouble > -5.0D)
    {
      d7 = 1.0D / (1.0D - d1 * paramDouble);
      d7 = pdf(paramDouble) * d7 * (d2 + d7 * (d3 + d7 * (d4 + d7 * (d5 + d7 * d6))));
    }
    else
    {
      d7 = paramDouble * paramDouble;
      d7 = pdf(paramDouble) * (1.0D - (1.0D - 3.0D * (1.0D - 5.0D * (1.0D - 7.0D / d7) / d7) / d7) / d7) / -paramDouble;
    }
    return d7;
  }
  
  public static double pdf(double paramDouble)
  {
    return 0.39894228D * Math.exp(-0.5D * paramDouble * paramDouble);
  }
  
  public static double quantile(double paramDouble)
  {
    if ((paramDouble <= 0.0D) || (paramDouble >= 1.0D)) {
      return Utility.NaN("Normal.quantile: p must be in (0,1)");
    }
    NormalAux localNormalAux = new NormalAux();
    double d = 4.91D * (Math.pow(paramDouble, 0.14D) - Math.pow(1.0D - paramDouble, 0.14D));
    return Solve.search(localNormalAux, paramDouble, d - 0.0025D, 0.005D);
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return cdf((paramDouble1 - paramDouble2) / paramDouble3);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return paramDouble2 + paramDouble3 * quantile(paramDouble1);
  }
  
  public static double power(double paramDouble1, int paramInt, double paramDouble2, double paramDouble3)
  {
    if (paramDouble3 <= 0.0D) {
      return Utility.NaN("Normal.power: sigma1 <= 0");
    }
    if ((paramDouble2 <= 0.0D) || (paramDouble2 >= 1.0D)) {
      return Utility.NaN("Normal.power: alpha not in (0,1)");
    }
    if (paramInt < 0) {
      return power(-paramDouble1, 1, paramDouble2, paramDouble3);
    }
    if (paramInt == 0)
    {
      double d = quantile(paramDouble2 / 2.0D);
      return cdf(d, paramDouble1, paramDouble3) + 1.0D - cdf(-d, paramDouble1, paramDouble3);
    }
    double d = -quantile(paramDouble2);
    return 1.0D - cdf(d, paramDouble1, paramDouble3);
  }
  
  public static double power(double paramDouble1, int paramInt, double paramDouble2)
  {
    return power(paramDouble1, paramInt, paramDouble2, 1.0D);
  }
  
  public static double effectSize(double paramDouble1, int paramInt, double paramDouble2)
  {
    if ((paramDouble1 <= 0.0D) || (paramDouble1 >= 1.0D)) {
      return Utility.NaN("Normal.effectSize: goal not in (0,1)");
    }
    if ((paramDouble2 <= 0.0D) || (paramDouble2 >= 1.0D)) {
      return Utility.NaN("Normal.effectSize: alpha not in (0,1)");
    }
    if (paramInt > 0) {
      return quantile(paramDouble1) - quantile(paramDouble2);
    }
    if (paramInt < 0) {
      return -effectSize(paramDouble1, 1, paramDouble2);
    }
    double d = effectSize(paramDouble1, 1, paramDouble2 / 2.0D);
    NormalAux2 localNormalAux2 = new NormalAux2(paramDouble2);
    return Solve.search(localNormalAux2, paramDouble1, d, -0.05D);
  }
  
  public static double rocArea(double paramDouble1, int paramInt, double paramDouble2)
  {
    saveMu1 = paramDouble1;
    saveTail = paramInt;
    return NumAnal.integral(Normal.class, "power", 0.0D, 1.0D, paramDouble2, false, 0.0D, 1.0D);
  }
  
  public static double rocArea(double paramDouble, int paramInt)
  {
    return rocArea(paramDouble, paramInt, 0.0001D);
  }
  
  public static double power(double paramDouble)
  {
    return power(saveMu1, saveTail, paramDouble);
  }
}
