package pc.stat.dist;

import java.io.PrintStream;
import pc.util.MoreMath;
import pc.util.NumAnal;
import pc.util.Solve;
import pc.util.Utility;

public class T
{
  private static double saveDelta;
  private static double saveDf;
  private static double saveMu;
  private static double saveTol;
  private static double saveSE;
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d1 = 0.398942280401433D;
    double d2 = 5.E-009D;
    int i = 400;
    if (Double.isNaN(paramDouble1 + paramDouble2 + paramDouble3)) {
      return (0.0D / 0.0D);
    }
    if (paramDouble2 <= 0.0D) {
      return Utility.NaN("T.cdf: negative d.f.");
    }
    if (paramDouble1 < 0.0D) {
      return 1.0D - cdf(-paramDouble1, paramDouble2, -paramDouble3);
    }
    double d10 = paramDouble3 * paramDouble3;
    double d4 = Math.exp(-d10 / 2.0D) / 2.0D;
    double d5 = d1 * 2.0D * d4 * paramDouble3;
    double d6 = 0.5D - d4;
    double d7 = paramDouble1 * paramDouble1;
    d7 /= (d7 + paramDouble2);
    double d8 = 0.5D;
    double d9 = d8 * paramDouble2;
    double d13 = Beta.cdf(d7, d8, d9);
    double d11 = 2.0D * MoreMath.beta(d8, d9) * Math.pow(d7, d8) * Math.pow(1.0D - d7, d9);
    double d12;
    double d14;
    if (Math.abs(paramDouble3) > 1.0E-008D)
    {
      d12 = Math.pow(1.0D - d7, d9);
      d14 = 1.0D - d12;
      d12 = d9 * d7 * d12;
    }
    else
    {
      d14 = d12 = 0.0D;
    }
    int j = 1;
    double d3 = d4 * d13 + d5 * d14;
    while ((d6 * (d13 - d11) > d2) && (j <= i))
    {
      d8 += 1.0D;
      d13 -= d11;
      d14 -= d12;
      d11 *= d7 * (d8 + d9 - 1.0D) / d8;
      d12 *= d7 * (d8 + d9 - 0.5D) / (d8 + 0.5D);
      d4 *= d10 / (2 * j);
      d5 *= d10 / (2 * j + 1);
      d6 -= d4;
      j++;
      d3 += d4 * d13 + d5 * d14;
    }
    if (j > i) {
      return Utility.NaN("T.cdf: too many iterations");
    }
    return d3 + Normal.cdf(-paramDouble3);
  }
  
  public static double cdf(double paramDouble1, double paramDouble2)
  {
    return cdf(paramDouble1, paramDouble2, 0.0D);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if (Double.isNaN(paramDouble1 + paramDouble2 + paramDouble3)) {
      return (0.0D / 0.0D);
    }
    if ((paramDouble1 <= 0.0D) || (paramDouble1 >= 1.0D)) {
      return Utility.NaN("T.quantile: p not in (0,1)");
    }
    TAux localTAux = new TAux(paramDouble2, paramDouble3);
    double d = paramDouble3 + 5.0D * (Math.pow(paramDouble1, 0.14D) - Math.pow(1.0D - paramDouble1, 0.14D));
    return Solve.search(localTAux, paramDouble1, d - 0.005D, 0.01D);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2)
  {
    return quantile(paramDouble1, paramDouble2, 0.0D);
  }
  
  public static double power(double paramDouble1, double paramDouble2, int paramInt, double paramDouble3)
  {
    if (Double.isNaN(paramDouble1 + paramDouble2 + paramDouble3)) {
      return (0.0D / 0.0D);
    }
    if (paramInt < 0) {
      return power(-paramDouble1, paramDouble2, 1, paramDouble3);
    }
    if (paramInt == 0)
    {
      double d = quantile(paramDouble3 / 2.0D, paramDouble2);
      return cdf(d, paramDouble2, paramDouble1) + 1.0D - cdf(-d, paramDouble2, paramDouble1);
    }
    double d = -quantile(paramDouble3, paramDouble2);
    return 1.0D - cdf(d, paramDouble2, paramDouble1);
  }
  
  public static double delta(double paramDouble1, double paramDouble2, int paramInt, double paramDouble3)
  {
    if (Double.isNaN(paramDouble1 + paramDouble2 + paramDouble3)) {
      return (0.0D / 0.0D);
    }
    if ((paramDouble1 <= 0.0D) || (paramDouble1 >= 1.0D)) {
      return Utility.NaN("T.delta: power not in (0,1)");
    }
    if ((paramDouble3 <= 0.0D) || (paramDouble3 >= 1.0D)) {
      return Utility.NaN("T.delta: alpha not in (0,1)");
    }
    if (paramInt < 0) {
      return -delta(paramDouble1, paramDouble2, 1, paramDouble3);
    }
    TAux2 localTAux2 = new TAux2(paramInt, paramDouble3, paramDouble2);
    double d;
    if (paramInt > 0) {
      d = quantile(1.0D - paramDouble3, paramDouble2) + quantile(1.0D - paramDouble1, paramDouble2);
    } else {
      d = quantile(1.0D - 0.5D * paramDouble3, paramDouble2) + quantile(1.0D - paramDouble1, paramDouble2);
    }
    return Solve.search(localTAux2, paramDouble1, d - 0.05D, 0.1D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, int paramInt, double paramDouble3)
  {
    saveDelta = paramDouble1;
    saveDf = paramDouble2;
    saveTail = paramInt;
    return NumAnal.integral(T.class, "power", 0.0D, 1.0D, paramDouble3, false, 0.0D, 1.0D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, int paramInt)
  {
    return rocArea(paramDouble1, paramDouble2, paramInt, 0.0001D);
  }
  
  public static double power(double paramDouble)
  {
    return power(saveDelta, saveDf, saveTail, paramDouble);
  }
  
  private static int saveTail = 0;
  
  public static double powerEquiv(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5)
  {
    if (paramDouble2 <= 0.0D) {
      return 0.0D;
    }
    double d1 = quantile(1.0D - paramDouble5, paramDouble4);
    double d2 = paramDouble1 / paramDouble3;
    double d3 = paramDouble2 / paramDouble3;
    

    double d6 = 0.5D * paramDouble4;
    double d7 = 0.0D, d4, d8, d9;
    if (Math.abs(d1) < 0.0001D) {
      return Normal.cdf(d3 - d2) - Normal.cdf(-d3 - d2);
    }
    if (d1 > 0.0D) {
      d4 = d3 / d1;
    } else {
      d4 = (1.0D / 0.0D);
    }
    d8 = Math.sqrt(Chi2.quantile(0.9999D, paramDouble4, 0.0D) / paramDouble4);
    d4 = Math.min(d4, d8);
    double d5 = d4 / 100.0D;
    for ( d9 = d5; d9 < d4; d9 += d5)
    {
      double d10 = Normal.cdf(d3 - d2 - d1 * d9) - Normal.cdf(d1 * d9 - d3 - d2);
      
      double d11 = Math.pow(d9, paramDouble4 - 1.0D) * Math.exp(-d6 * d9 * d9);
      d7 += d10 * d11;
    }
    d9 = d6 * Math.log(d6) - MoreMath.logGamma(d6);
    return d7 * 2.0D * d5 * Math.exp(d9);
  }
  
  public static double powerEquiv(double paramDouble)
  {
    return powerEquiv(saveMu, saveTol, saveSE, saveDf, paramDouble);
  }
  
  public static double rocEquiv(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5)
  {
    saveMu = paramDouble1;
    saveTol = paramDouble2;
    saveSE = paramDouble3;
    saveDf = paramDouble4;
    return NumAnal.integral(T.class, "powerEquiv", 0.0D, 1.0D, paramDouble5, false, 0.0D, 1.0D);
  }
  
  public static double rocEquiv(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    return rocEquiv(paramDouble1, paramDouble2, paramDouble3, paramDouble4, 0.0001D);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    double d1 = Utility.strtod(paramArrayOfString[0]);
    double d2 = Utility.strtod(paramArrayOfString[1]);
    double d3 = Utility.strtod(paramArrayOfString[2]);
    double d4 = cdf(d1, d2, d3);
    System.out.println("cdf: " + d4);
  }
}
