package pc.stat.dist;

import pc.util.NumAnal;
import pc.util.Solve;
import pc.util.Utility;

public class F
{
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if ((paramDouble2 <= 0.0D) || (paramDouble3 <= 0.0D))
    {
      Utility.warning("F.cdf: must use positive df");
      return (0.0D / 0.0D);
    }
    return cdf(paramDouble1, paramDouble2, paramDouble3, 0.0D);
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if ((paramDouble2 <= 0.0D) || (paramDouble3 <= 0.0D) || (paramDouble4 < 0.0D))
    {
      Utility.warning("F.cdf: must use positive df and lambda");
      return (0.0D / 0.0D);
    }
    paramDouble1 = Math.max(paramDouble1, 0.0D);
    return Beta.cdf(1.0D - paramDouble3 / (paramDouble2 * paramDouble1 + paramDouble3), paramDouble2 / 2.0D, paramDouble3 / 2.0D, paramDouble4);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if ((paramDouble2 <= 0.0D) || (paramDouble3 <= 0.0D) || (paramDouble4 < 0.0D))
    {
      Utility.warning("F.quantile: must use positive df and lambda");
      return (0.0D / 0.0D);
    }
    if ((paramDouble1 < 0.0D) || (paramDouble1 >= 1.0D))
    {
      Utility.warning("F.quantile: p is out of bounds");
      return (0.0D / 0.0D);
    }
    double d = Beta.quantile(paramDouble1, paramDouble2 / 2.0D, paramDouble3 / 2.0D, paramDouble4);
    return paramDouble3 / paramDouble2 * d / (1.0D - d);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return quantile(paramDouble1, paramDouble2, paramDouble3, 0.0D);
  }
  
  public static double power(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if ((paramDouble1 < 0.0D) || (paramDouble2 < 0.0D) || (paramDouble3 < 0.0D) || (paramDouble4 < 0.0D) || (paramDouble4 > 1.0D))
    {
      Utility.warning("F.power: illegal argument");
      return (0.0D / 0.0D);
    }
    double d = quantile(1.0D - paramDouble4, paramDouble2, paramDouble3);
    return 1.0D - cdf(d, paramDouble2, paramDouble3, paramDouble1);
  }
  
  public static double power(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, boolean paramBoolean)
  {
    double d = quantile(1.0D - paramDouble4, paramDouble2, paramDouble3);
    if (paramBoolean) {
      return 1.0D - cdf(d / (1.0D + paramDouble1), paramDouble2, paramDouble3, 0.0D);
    }
    return 1.0D - cdf(d, paramDouble2, paramDouble3, paramDouble2 * paramDouble1);
  }
  
  public static double lambda(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if ((paramDouble1 < paramDouble4) || (paramDouble1 > 1.0D) || (paramDouble2 < 0.0D) || (paramDouble3 < 0.0D) || (paramDouble4 < 0.0D) || (paramDouble4 > 1.0D))
    {
      Utility.warning("F.lambda: illegal argument");
      return (0.0D / 0.0D);
    }
    FAux localFAux = new FAux(paramDouble2, paramDouble3, paramDouble4);
    double d = quantile(1.0D - paramDouble4, paramDouble2, paramDouble3) + quantile(paramDouble1, paramDouble2, paramDouble3);
    return Solve.search(localFAux, paramDouble1, d, 0.1D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    saveLambda = paramDouble1;
    saveDf1 = paramDouble2;
    saveDf2 = paramDouble3;
    return NumAnal.integral(F.class, "power", 0.0D, 1.0D, paramDouble4, false, 0.0D, 1.0D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return rocArea(paramDouble1, paramDouble2, paramDouble3, 0.0001D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean, double paramDouble4)
  {
    saveEffSize = paramDouble1;
    saveDf1 = paramDouble2;
    saveDf2 = paramDouble3;
    saveRandom = paramBoolean;
    return NumAnal.integral(F.class, "powerr", 0.0D, 1.0D, paramDouble4, false, 0.0D, 1.0D);
  }
  
  public static double rocArea(double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean)
  {
    return rocArea(paramDouble1, paramDouble2, paramDouble3, paramBoolean, 0.0001D);
  }
  
  public static double power(double paramDouble)
  {
    return power(saveLambda, saveDf1, saveDf2, paramDouble);
  }
  
  public static double powerr(double paramDouble)
  {
    return power(saveEffSize, saveDf1, saveDf2, paramDouble, saveRandom);
  }
  
  private static double saveEffSize = 1.0D;
  private static double saveLambda = 0.0D;
  private static double saveDf2 = 1.0D;
  private static double saveDf1 = 1.0D;
  private static boolean saveRandom = false;
}
