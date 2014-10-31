package pc.util;

import java.io.PrintStream;

public class LinAlg
{
  public static boolean LUInPlace(double[][] paramArrayOfDouble, int[] paramArrayOfInt)
  {
    int i = paramArrayOfDouble.length, k, i1;
    double[] arrayOfDouble = new double[i];
    for (int j = 0; j < i; j++)
    {
      paramArrayOfInt[j] = j;
      arrayOfDouble[j] = paramArrayOfDouble[j][0];
      for (k = 1; k < i; k++) {
        if (paramArrayOfDouble[j][k] > arrayOfDouble[j]) {
          arrayOfDouble[j] = paramArrayOfDouble[j][k];
        }
      }
    }
    for (k = 0; k < i; k++)
    {
      int m = k;
      double d1 = paramArrayOfDouble[paramArrayOfInt[m]][k] / arrayOfDouble[paramArrayOfInt[m]];
      for (int n = k + 1; n < i; n++) {
        if (paramArrayOfDouble[paramArrayOfInt[n]][k] / arrayOfDouble[paramArrayOfInt[n]] > d1)
        {
          m = n;
          d1 = paramArrayOfDouble[paramArrayOfInt[m]][k] / arrayOfDouble[paramArrayOfInt[m]];
        }
      }
      if (m != k)
      {
        i1 = paramArrayOfInt[k];
        paramArrayOfInt[k] = paramArrayOfInt[m];
        paramArrayOfInt[m] = i1;
      }
      if (isZero(d1)) {
        return false;
      }
      for ( i1 = k + 1; i1 < i; i1++)
      {
        double d2 = paramArrayOfDouble[paramArrayOfInt[i1]][k] / paramArrayOfDouble[paramArrayOfInt[k]][k];
        for (int i2 = k; i2 < i; i2++) {
          paramArrayOfDouble[paramArrayOfInt[i1]][i2] -= d2 * paramArrayOfDouble[paramArrayOfInt[k]][i2];
        }
        paramArrayOfDouble[paramArrayOfInt[i1]][k] = d2;
      }
    }
    return true;
  }
  
  public static double[] LUSolveInPlace(double[][] paramArrayOfDouble, int[] paramArrayOfInt, double[] paramArrayOfDouble1)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++) {
      for (int k = 0; k < j; k++) {
        paramArrayOfDouble1[paramArrayOfInt[j]] -= paramArrayOfDouble[paramArrayOfInt[j]][k] * paramArrayOfDouble1[paramArrayOfInt[k]];
      }
    }
    double[] arrayOfDouble = new double[i];
    for (int m = i - 1; m >= 0; m--)
    {
      arrayOfDouble[m] = paramArrayOfDouble1[paramArrayOfInt[m]];
      for (int n = m + 1; n < i; n++) {
        arrayOfDouble[m] -= paramArrayOfDouble[paramArrayOfInt[m]][n] * arrayOfDouble[n];
      }
      arrayOfDouble[m] /= paramArrayOfDouble[paramArrayOfInt[m]][m];
    }
    return arrayOfDouble;
  }
  
  public static double[][] LU(double[][] paramArrayOfDouble, int[] paramArrayOfInt)
  {
    double[][] arrayOfDouble = new double[paramArrayOfDouble.length][];
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      arrayOfDouble[i] = copy(paramArrayOfDouble[i]);
    }
    if (LUInPlace(arrayOfDouble, paramArrayOfInt)) {
      return arrayOfDouble;
    }
    return null;
  }
  
  public static double[] LUSolve(double[][] paramArrayOfDouble, int[] paramArrayOfInt, double[] paramArrayOfDouble1)
  {
    double[] arrayOfDouble = copy(paramArrayOfDouble1);
    return LUSolveInPlace(paramArrayOfDouble, paramArrayOfInt, arrayOfDouble);
  }
  
  public static double[] copy(double[] paramArrayOfDouble)
  {
    double[] arrayOfDouble = new double[paramArrayOfDouble.length];
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      arrayOfDouble[i] = paramArrayOfDouble[i];
    }
    return arrayOfDouble;
  }
  
  public static double[][] transpose(double[][] paramArrayOfDouble)
  {
    int i = paramArrayOfDouble.length;int j = paramArrayOfDouble[0].length;
    double[][] arrayOfDouble = new double[j][];
    for (int k = 0; k < j; k++)
    {
      arrayOfDouble[k] = new double[i];
      for (int m = 0; m < i; m++) {
        arrayOfDouble[k][m] = paramArrayOfDouble[m][k];
      }
    }
    return arrayOfDouble;
  }
  
  private static boolean isZero(double paramDouble)
  {
    return Math.abs(paramDouble) < 1.0E-008D;
  }
  
  public static double[] constant(double paramDouble, int paramInt)
  {
    double[] arrayOfDouble = new double[paramInt];
    for (int i = 0; i < paramInt; i++) {
      arrayOfDouble[i] = paramDouble;
    }
    return arrayOfDouble;
  }
  
  public static void print(double[] paramArrayOfDouble)
  {
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      System.out.print(" " + paramArrayOfDouble[i]);
    }
  }
  
  public static void println(double[] paramArrayOfDouble)
  {
    print(paramArrayOfDouble);
    System.out.println();
  }
  
  public static void println(double[][] paramArrayOfDouble)
  {
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      println(paramArrayOfDouble[i]);
    }
  }
}
