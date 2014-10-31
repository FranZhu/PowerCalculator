package pc.util;

public class MoreMath
{
  public static double logGamma(double paramDouble)
  {
    if (paramDouble <= 0.0D)
    {
      Utility.warning("logGamma: argument must be > 0");
      return (0.0D / 0.0D);
    }
    if (paramDouble < 15.0D) {
      return logGamma(paramDouble + 1.0D) - Math.log(paramDouble);
    }
    double d = 1.0D / (paramDouble * paramDouble);
    return (paramDouble - 0.5D) * Math.log(paramDouble) - paramDouble + 0.9189385332046727D + (1.0D - d * (1.0D - d * (1.0D - 0.75D * d) / 3.5D) / 30.0D) / 12.0D / paramDouble;
  }
  
  public static double gamma(double paramDouble)
  {
    return Math.exp(logGamma(paramDouble));
  }
  
  public static double beta(double paramDouble1, double paramDouble2)
  {
    return Math.exp(logGamma(paramDouble1 + paramDouble2) - logGamma(paramDouble1) - logGamma(paramDouble2));
  }
}
