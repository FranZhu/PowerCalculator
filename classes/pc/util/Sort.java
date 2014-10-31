package pc.util;

public class Sort
{
  public static void qsort(double[] paramArrayOfDouble)
  {
    qsort(paramArrayOfDouble, 0, paramArrayOfDouble.length - 1);
  }
  
  protected static void qsort(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
  {
    int i = paramInt1;int j = paramInt2;
    double d1 = paramArrayOfDouble[((i + j) / 2)];
    while (i <= j)
    {
      while ((i < paramInt2) && (paramArrayOfDouble[i] < d1)) {
        i++;
      }
      while ((j > paramInt1) && (paramArrayOfDouble[j] > d1)) {
        j--;
      }
      if (i <= j)
      {
        double d2 = paramArrayOfDouble[i];
        paramArrayOfDouble[i] = paramArrayOfDouble[j];
        paramArrayOfDouble[j] = d2;
        i++;
        j--;
      }
    }
    if (paramInt1 < j) {
      qsort(paramArrayOfDouble, paramInt1, j);
    }
    if (i < paramInt2) {
      qsort(paramArrayOfDouble, i, paramInt2);
    }
  }
  
  public static int[] order(double[] paramArrayOfDouble)
  {
    int[] arrayOfInt = new int[paramArrayOfDouble.length];
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      arrayOfInt[i] = i;
    }
    order(arrayOfInt, paramArrayOfDouble, 0, paramArrayOfDouble.length - 1);
    return arrayOfInt;
  }
  
  protected static void order(int[] paramArrayOfInt, double[] paramArrayOfDouble, int paramInt1, int paramInt2)
  {
    int i = paramInt1;int j = paramInt2;
    double d = paramArrayOfDouble[paramArrayOfInt[((i + j) / 2)]];
    while (i <= j)
    {
      while ((i < paramInt2) && (paramArrayOfDouble[paramArrayOfInt[i]] < d)) {
        i++;
      }
      while ((j > paramInt1) && (paramArrayOfDouble[paramArrayOfInt[j]] > d)) {
        j--;
      }
      if (i <= j)
      {
        int k = paramArrayOfInt[i];
        paramArrayOfInt[i] = paramArrayOfInt[j];
        paramArrayOfInt[j] = k;
        i++;
        j--;
      }
    }
    if (paramInt1 < j) {
      order(paramArrayOfInt, paramArrayOfDouble, paramInt1, j);
    }
    if (i < paramInt2) {
      order(paramArrayOfInt, paramArrayOfDouble, i, paramInt2);
    }
  }
  
  public static int[] rank(double[] paramArrayOfDouble)
  {
    int[] arrayOfInt1 = order(paramArrayOfDouble);int i = paramArrayOfDouble.length;
    int[] arrayOfInt2 = new int[i];
    for (int j = 0; j < i; j++) {
      arrayOfInt2[arrayOfInt1[j]] = (j + 1);
    }
    return arrayOfInt2;
  }
  
  public static float[] rankTies(double[] paramArrayOfDouble)
  {
	int j,m;
    int[] arrayOfInt = order(paramArrayOfDouble);
    int i = paramArrayOfDouble.length;
    float[] arrayOfFloat = new float[i];
    for (int k = 0; k < i;)
    {
      double d = paramArrayOfDouble[arrayOfInt[k]];
      for (j = k + 1; (j < i) && (paramArrayOfDouble[arrayOfInt[j]] == d); j++) {}
      for (m = k; m < j; m++) {
        arrayOfFloat[arrayOfInt[m]] = (0.5F * (k + j + 1));
      }
      k = j;
    }
    return arrayOfFloat;
  }
  
  private static double[] toDouble(int[] paramArrayOfInt)
  {
    double[] arrayOfDouble = new double[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      arrayOfDouble[i] = (paramArrayOfInt[i] + 0.0D);
    }
    return arrayOfDouble;
  }
  
  public static int[] order(int[] paramArrayOfInt)
  {
    return order(toDouble(paramArrayOfInt));
  }
}
