package pc.stat.dist;

import java.io.PrintStream;
import pc.util.MoreMath;
import pc.util.Solve;
import pc.util.Utility;

public class Beta
{
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d11 = 1.0E-008D;
    
    int j = 500;
    int k = 0;int m = 0;int n = 0;
    if ((paramDouble2 <= 0.0D) || (paramDouble3 <= 0.0D))
    {
      Utility.warning("Beta.cdf: parameters must be positive");
      return (0.0D / 0.0D);
    }
    paramDouble1 = paramDouble1 > 1.0D ? 1.0D : paramDouble1 < 0.0D ? 0.0D : paramDouble1;
    if ((paramDouble1 == 0.0D) || (paramDouble1 == 1.0D)) {
      return paramDouble1;
    }
    double d7 = Math.pow(paramDouble1, paramDouble2) * Math.pow(1.0D - paramDouble1, paramDouble3) * MoreMath.beta(paramDouble2, paramDouble3);
    if (paramDouble2 < 1.5D)
    {
      m = 1;
      d7 *= paramDouble1 * (paramDouble2 + paramDouble3) / paramDouble2;
      paramDouble2 += 1.0D;
    }
    if (paramDouble3 < 1.5D)
    {
      n = 1;
      d7 *= (1.0D - paramDouble1) * (paramDouble2 + paramDouble3) / paramDouble3;
      paramDouble3 += 1.0D;
    }
    double d1;
    if (paramDouble1 >= (paramDouble2 - 1.0D) / (paramDouble2 + paramDouble3 - 2.0D))
    {
      k = 1;
      paramDouble1 = 1.0D - paramDouble1;
      d1 = paramDouble2;paramDouble2 = paramDouble3;paramDouble3 = d1;
    }
    double d2 = 1.0D / (1.0D - paramDouble1 * (paramDouble2 + paramDouble3) / (paramDouble2 + 1.0D));
    double d8 = d2;
    double d10 = d2;
    int i = 1;
    double d3;
    do
    {
      d3 = d2;
      double d6 = paramDouble2 + 2 * i;
      double d4 = i * (paramDouble3 - i) * paramDouble1 / (d6 * (d6 - 1.0D));
      double d5 = -(paramDouble2 + i) * (paramDouble2 + paramDouble3 + i) * paramDouble1 / (d6 * (d6 + 1.0D));
      d8 = d2 + d4 * d8;
      d2 = d8 + d5 * d2;
      d10 = 1.0D + d4 * d10;
      double d9 = d10 + d5;
      d8 /= d9;
      d10 /= d9;
      d2 /= d9;
      i++;
    } while ((Math.abs(d2 - d3) >= d11 * d2) && (i <= j));
    if (i > j) {
      Utility.warning("Convergence failure in beta.cdf - error estimate = " + (d2 - d3) / d2);
    }
    d2 = d7 * d2 / paramDouble2;
    if (k != 0)
    {
      d1 = paramDouble2;paramDouble2 = paramDouble3;paramDouble3 = d1;
      paramDouble1 = 1.0D - paramDouble1;
      d2 = 1.0D - d2;
    }
    if (n != 0)
    {
      paramDouble3 -= 1.0D;
      d7 = paramDouble3 * d7 / ((1.0D - paramDouble1) * (paramDouble2 + paramDouble3));
      d2 -= d7 / paramDouble3;
    }
    if (m != 0)
    {
      paramDouble2 -= 1.0D;
      d2 += d7 / (paramDouble1 * (paramDouble2 + paramDouble3));
    }
    return d2;
  }
  
  public static double oldCdf(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    double d6 = 1.0E-008D;
    
    int j = 500;
    if ((paramDouble2 <= 0.0D) || (paramDouble3 <= 0.0D) || (paramDouble4 < 0.0D))
    {
      Utility.warning("Beta.cdf: parameters must be positive");
      return (0.0D / 0.0D);
    }
    double d1 = cdf(paramDouble1, paramDouble2, paramDouble3);
    double d5;
    if (paramDouble4 == 0.0D)
    {
      d5 = d1;
    }
    else
    {
      double d2 = Math.pow(paramDouble1, paramDouble2) * Math.pow(1.0D - paramDouble1, paramDouble3) * MoreMath.beta(paramDouble2, paramDouble3) / paramDouble2;
      paramDouble4 /= 2.0D;
      double d3 = Math.exp(-paramDouble4);
      double d4 = 1.0D - d3;
      d5 = d3 * d1;
      int i = 0;
      do
      {
        i++;
        d1 -= d2;
        d2 *= paramDouble1 * (paramDouble2 + paramDouble3 + i - 1.0D) / (paramDouble2 + i);
        d3 *= paramDouble4 / i;
        d4 -= d3;
        d5 += d3 * d1;
      } while ((d4 * (d1 - d2) >= d6) && (i <= j));
      if (i > j) {
        Utility.warning("Convergence failure in Beta.cdf - error estimate = " + d4 * (d1 - d2));
      }
    }
    return d5;
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    int j = 0;
    if ((paramDouble2 <= 0.0D) || (paramDouble3 <= 0.0D) || (paramDouble4 < 0.0D))
    {
      Utility.warning("Beta.cdf: parameters must be positive");
      return (0.0D / 0.0D);
    }
    paramDouble4 /= 2.0D;
    if (paramDouble4 < 1.0E-008D) {
      return cdf(paramDouble1, paramDouble2, paramDouble3);
    }
    if (paramDouble4 > 15.0D)
    {
      j = 1 + (int)Chi2.quantile(1.0E-008D, 2.0D * paramDouble4) / 2;
      while ((j > 0) && (Poisson.cdf(j - 1, paramDouble4) > 1.0E-008D)) {
        j--;
      }
    }
    int k = (int)Chi2.quantile(0.99999999D, 2.0D * paramDouble4) / 2;
    while (Poisson.cdf(k, paramDouble4) < 0.99999999D) {
      k++;
    }
    int i = k;
    double d1 = cdf(paramDouble1, paramDouble2 + i, paramDouble3);
    double d2 = MoreMath.logGamma(paramDouble2 + paramDouble3 + i - 1.0D) - MoreMath.logGamma(paramDouble2 + i - 1.0D) - MoreMath.logGamma(paramDouble3) + (paramDouble2 + i - 1.0D) * Math.log(paramDouble1) + paramDouble3 * Math.log(1.0D - paramDouble1);
    
    d2 = Math.exp(d2) / (paramDouble2 + i - 1.0D);
    double d3 = Math.exp(-paramDouble4 + i * Math.log(paramDouble4) - MoreMath.logGamma(i + 1));
    
    double d4 = d3 * d1;
    d3 *= i / paramDouble4;
    for (i = k - 1; i >= j; i--)
    {
      d1 += d2;
      d2 *= (paramDouble2 + i) / paramDouble1 / (paramDouble2 + i + paramDouble3 - 1.0D);
      d4 += d3 * d1;
      d3 *= i / paramDouble4;
    }
    return d4;
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    if (paramDouble1 * (1.0D - paramDouble1) == 0.0D) {
      return paramDouble1;
    }
    double d1 = 4.91D * (Math.pow(paramDouble1, 0.14D) - Math.pow(1.0D - paramDouble1, 0.14D));
    
    double d2 = paramDouble2 + paramDouble4 / 2.0D;
    
    double d3 = d2 / (d2 + paramDouble3) + d1 * Math.sqrt(d2 * paramDouble3 / Math.pow(d2 + paramDouble3, 3.0D));
    d3 = Math.min(0.99D, Math.max(0.01D, d3));
    
    BetaAux localBetaAux = new BetaAux(paramDouble2, paramDouble3, paramDouble4);
    return Solve.search(localBetaAux, paramDouble1, d3, 0.01D);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return quantile(paramDouble1, paramDouble2, paramDouble3, 0.0D);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    double d1 = Utility.strtod(paramArrayOfString[0]);
    double d2 = Utility.strtod(paramArrayOfString[1]);
    double d3 = Utility.strtod(paramArrayOfString[2]);
    double d4 = Utility.strtod(paramArrayOfString[3]);
    
    double d5 = oldCdf(d1, d2, d3, d4);
    double d6 = cdf(d1, d2, d3, d4);
    System.out.println("Old cdf: " + d5);
    System.out.println("New cdf: " + d6);
  }
}
