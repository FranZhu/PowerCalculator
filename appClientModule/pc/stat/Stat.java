package pc.stat;

public class Stat
{
  public static double mean(double[] paramArrayOfDouble)
  {
    int i = paramArrayOfDouble.length;
    double d = paramArrayOfDouble[0];
    for (int j = 1; j < i; j++) {
      d += paramArrayOfDouble[j];
    }
    return d / i;
  }
  
  public static double sd(double[] paramArrayOfDouble, double paramDouble)
  {
    int i = paramArrayOfDouble.length;
    double d1 = 0.0D;
    for (int j = 0; j < i; j++)
    {
      double d2 = paramArrayOfDouble[j] - paramDouble;
      d1 += d2 * d2;
    }
    return Math.sqrt(d1 / (i - 1.0D));
  }
  
  public static double sd(double[] paramArrayOfDouble)
  {
    return meanSD(paramArrayOfDouble)[1];
  }
  
  public static double[] meanSD(double[] paramArrayOfDouble)
  {
    double[] arrayOfDouble = { paramArrayOfDouble[0], 0.0D };
    int i = paramArrayOfDouble.length;
    for (int j = 1; j < i; j++)
    {
      arrayOfDouble[0] += paramArrayOfDouble[j];
      double d2 = paramArrayOfDouble[j] - paramArrayOfDouble[0];
      arrayOfDouble[1] += d2 * d2;
    }
    arrayOfDouble[0] /= i;
    double d1 = paramArrayOfDouble[0] - arrayOfDouble[0];
    arrayOfDouble[1] = Math.sqrt((arrayOfDouble[1] - i * d1 * d1) / (i - 1));
    return arrayOfDouble;
  }
}
