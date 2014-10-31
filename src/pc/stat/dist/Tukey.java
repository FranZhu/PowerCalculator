package pc.stat.dist;

import java.io.PrintStream;
import pc.util.MoreMath;
import pc.util.Utility;

public class Tukey
{
  static final double M_1_SQRT_2PI = 0.398942280401433D;
  static final double M_LN2 = 0.6931471805599453D;
  
  private static double wprob(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    int i = 12;
    int j = 6;
    double d1 = 1.0D;
    double d2 = -30.0D;
    double d3 = -50.0D;
    double d4 = 60.0D;
    double d5 = 8.0D;
    double d6 = 3.0D;
    double d7 = 2.0D;
    double d8 = 3.0D;
    double[] arrayOfDouble1 = { 0.9815606342467192D, 0.9041172563704749D, 0.7699026741943047D, 0.5873179542866175D, 0.3678314989981802D, 0.1252334085114689D };
    






    double[] arrayOfDouble2 = { 0.04717533638651183D, 0.1069393259953184D, 0.1600783285433462D, 0.2031674267230659D, 0.2334925365383548D, 0.2491470458134028D };
    











    double d23 = paramDouble1 * 0.5D;
    if (d23 >= d5) {
      return 1.0D;
    }
    double d11 = 2.0D * Normal.cdf(d23) - 1.0D;
    if (d11 >= Math.exp(d3 / paramDouble3)) {
      d11 = Math.pow(d11, paramDouble3);
    } else {
      d11 = 0.0D;
    }
    double d26;
    if (paramDouble1 > d6) {
      d26 = d7;
    } else {
      d26 = d8;
    }
    double d14 = d23;
    double d13 = (d5 - d23) / d26;
    double d15 = d14 + d13;
    double d18 = 0.0D;
    


    double d17 = paramDouble3 - 1.0D;
    for (double d25 = 1.0D; d25 <= d26; d25 += 1.0D)
    {
      double d19 = 0.0D;
      double d9 = 0.5D * (d15 + d14);
      


      double d12 = 0.5D * (d15 - d14);
      for (int m = 1; m <= i; m++)
      {
        int k;
        double d27;
        if (j < m)
        {
          k = i - m + 1;
          d27 = arrayOfDouble1[(k - 1)];
        }
        else
        {
          k = m;
          d27 = -arrayOfDouble1[(k - 1)];
        }
        double d16 = d12 * d27;
        double d10 = d9 + d16;
        



        double d22 = d10 * d10;
        if (d22 > d4) {
          break;
        }
        double d21 = 2.0D * Normal.cdf(d10);
        double d20 = 2.0D * Normal.cdf(d10, paramDouble1, 1.0D);
        



        double d24 = d21 * 0.5D - d20 * 0.5D;
        if (d24 >= Math.exp(d2 / d17))
        {
          d24 = arrayOfDouble2[(k - 1)] * Math.exp(-(0.5D * d22)) * Math.pow(d24, d17);
          d19 += d24;
        }
      }
      d19 *= 2.0D * d12 * paramDouble3 * 0.398942280401433D;
      d18 += d19;
      d14 = d15;
      d15 += d13;
    }
    d11 = d18 + d11;
    if (d11 <= Math.exp(d2 / paramDouble2)) {
      return 0.0D;
    }
    d11 = Math.pow(d11, paramDouble2);
    if (d11 >= d1) {
      d11 = 1.0D;
    }
    return d11;
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    int i = 16;
    int j = 8;
    double d1 = -30.0D;
    double d2 = 1.0E-014D;
    double d3 = 100.0D;
    double d4 = 800.0D;
    double d5 = 5000.0D;
    double d6 = 25000.0D;
    double d7 = 1.0D;
    double d8 = 0.5D;
    double d9 = 0.25D;
    double d10 = 0.125D;
    double[] arrayOfDouble1 = { 0.9894009349916499D, 0.944575023073233D, 0.8656312023878318D, 0.755404408355003D, 0.6178762444026438D, 0.4580167776572274D, 0.2816035507792589D, 0.09501250983763744D };
    








    double[] arrayOfDouble2 = { 0.0271524594117541D, 0.06225352393864789D, 0.09515851168249279D, 0.1246289712555339D, 0.1495959888165767D, 0.1691565193950025D, 0.1826034150449236D, 0.189450610455069D };
    if ((Double.isNaN(paramDouble1)) || (Double.isNaN(paramDouble4)) || (Double.isNaN(paramDouble2)) || (Double.isNaN(paramDouble3))) {
      return (0.0D / 0.0D);
    }
    if (paramDouble1 <= 0.0D) {
      return 0.0D;
    }
    if ((paramDouble3 < 2.0D) || (paramDouble4 < 1.0D) || (paramDouble2 < 2.0D)) {
      return (0.0D / 0.0D);
    }
    if (Double.isInfinite(paramDouble1)) {
      return 1.0D;
    }
    if (paramDouble3 > d6) {
      return wprob(paramDouble1, paramDouble4, paramDouble2);
    }
    double d12 = paramDouble3 * 0.5D;
    
    double d14 = d12 * Math.log(paramDouble3) - paramDouble3 * 0.6931471805599453D - MoreMath.logGamma(d12);
    double d13 = d12 - 1.0D;
    




    double d15 = paramDouble3 * 0.25D;
    double d21;
    if (paramDouble3 <= d3) {
      d21 = d7;
    } else if (paramDouble3 <= d4) {
      d21 = d8;
    } else if (paramDouble3 <= d5) {
      d21 = d9;
    } else {
      d21 = d10;
    }
    d14 += Math.log(d21);
    double d16;
    double d11 = d16 = 0.0D;
    for (int k = 1; k <= 50; k++)
    {
      d16 = 0.0D;
      



      double d20 = (2 * k - 1) * d21;
      for (int n = 1; n <= i; n++)
      {
        int m;
        double d19;
        if (j < n)
        {
          m = n - j - 1;
          d19 = d14 + d13 * Math.log(d20 + arrayOfDouble1[m] * d21) - (arrayOfDouble1[m] * d21 + d20) * d15;
        }
        else
        {
          m = n - 1;
          d19 = d14 + d13 * Math.log(d20 - arrayOfDouble1[m] * d21) + (arrayOfDouble1[m] * d21 - d20) * d15;
        }
        if (d19 >= d1)
        {
          double d17;
          if (j < n) {
            d17 = paramDouble1 * Math.sqrt((arrayOfDouble1[m] * d21 + d20) * 0.5D);
          } else {
            d17 = paramDouble1 * Math.sqrt((-(arrayOfDouble1[m] * d21) + d20) * 0.5D);
          }
          double d22 = wprob(d17, paramDouble4, paramDouble2);
          double d18 = d22 * arrayOfDouble2[m] * Math.exp(d19);
          d16 += d18;
        }
      }
      if ((k * d21 >= 1.0D) && (d16 <= d2)) {
        break;
      }
      d11 += d16;
    }
    if (d16 > d2) {
      return Utility.NaN("Tukey.cdf: failed to converge");
    }
    if (d11 > 1.0D) {
      d11 = 1.0D;
    }
    return d11;
  }
  
  public static double cdf(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return cdf(paramDouble1, paramDouble2, paramDouble3, 1.0D);
  }
  
  private static double qinv(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d1 = 0.322232421088D;
    double d2 = 0.099348462606D;
    double d3 = -1.0D;
    double d4 = 0.588581570495D;
    double d5 = -0.342242088547D;
    double d6 = 0.531103462366D;
    double d7 = -0.204231210125D;
    double d8 = 0.10353775285D;
    double d9 = -4.53642210148E-005D;
    double d10 = 0.0038560700634D;
    double d11 = 0.8832D;
    double d12 = 0.2368D;
    double d13 = 1.214D;
    double d14 = 1.208D;
    double d15 = 1.4142D;
    double d16 = 120.0D;
    


    double d17 = 0.5D - 0.5D * paramDouble1;
    double d20 = Math.sqrt(Math.log(1.0D / (d17 * d17)));
    double d19 = d20 + ((((d20 * d9 + d7) * d20 + d5) * d20 + d3) * d20 + d1) / ((((d20 * d10 + d8) * d20 + d6) * d20 + d4) * d20 + d2);
    if (paramDouble3 < d16) {
      d19 += (d19 * d19 * d19 + d19) / paramDouble3 / 4.0D;
    }
    double d18 = d11 - d12 * d19;
    if (paramDouble3 < d16) {
      d18 += -d13 / paramDouble3 + d14 * d19 / paramDouble3;
    }
    return d19 * (d18 * Math.log(paramDouble2 - 1.0D) + d15);
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    double d1 = 0.0001D;
    int i = 50;
    
    double d2 = 0.0D;
    if ((Double.isNaN(paramDouble1)) || (Double.isNaN(paramDouble4)) || (Double.isNaN(paramDouble2)) || (Double.isNaN(paramDouble3))) {
      return (0.0D / 0.0D);
    }
    if ((paramDouble1 < 0.0D) || (paramDouble1 >= 1.0D)) {
      return (0.0D / 0.0D);
    }
    if ((paramDouble3 < 2.0D) || (paramDouble4 < 1.0D) || (paramDouble2 < 2.0D)) {
      return (0.0D / 0.0D);
    }
    if (paramDouble1 == 0.0D) {
      return 0.0D;
    }
    double d5 = qinv(paramDouble1, paramDouble2, paramDouble3);
    


    double d3 = cdf(d5, paramDouble2, paramDouble3, paramDouble4) - paramDouble1;
    double d6;
    if (d3 > 0.0D) {
      d6 = Math.max(0.0D, d5 - 1.0D);
    } else {
      d6 = d5 + 1.0D;
    }
    double d4 = cdf(d6, paramDouble2, paramDouble3, paramDouble4) - paramDouble1;
    for (int j = 1; j < i; j++)
    {
      d2 = d6 - d4 * (d6 - d5) / (d4 - d3);
      d3 = d4;
      


      d5 = d6;
      if (d2 < 0.0D)
      {
        d2 = 0.0D;
        d4 = -paramDouble1;
      }
      d4 = cdf(d2, paramDouble2, paramDouble3, paramDouble4) - paramDouble1;
      d6 = d2;
      



      double d7 = Math.abs(d6 - d5);
      if (d7 < d1) {
        return d2;
      }
    }
    Utility.warning("Tukey.quantile(): failed to converge");
    return d2;
  }
  
  public static double quantile(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return quantile(paramDouble1, paramDouble2, paramDouble3, 1.0D);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    double d = quantile(0.95D, 5.0D, 20.0D);
    System.out.println("q = " + d);
    System.out.println("p = " + cdf(d, 5.0D, 20.0D));
  }
}
