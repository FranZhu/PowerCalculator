package pc.util;

import java.awt.Component;
import java.awt.Window;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.StringTokenizer;
import pc.awt.ViewWindow;

public class Utility
  implements Closeable
{
  public static double strtod(String paramString)
  {
    try
    {
      return Double.valueOf(paramString.trim()).doubleValue();
    }
    catch (NumberFormatException localNumberFormatException) {}
    return (0.0D / 0.0D);
  }
  
  public static float strtof(String paramString)
  {
    try
    {
      return Float.valueOf(paramString.trim()).floatValue();
    }
    catch (NumberFormatException localNumberFormatException) {}
    return (0.0F / 0.0F);
  }
  
  public static int strtoi(String paramString)
  {
    try
    {
      return Integer.valueOf(paramString.trim()).intValue();
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 2147483647;
  }
  
  public static long strtol(String paramString)
  {
    try
    {
      return Long.valueOf(paramString.trim()).longValue();
    }
    catch (NumberFormatException localNumberFormatException) {}
    return 9223372036854775807L;
  }
  
  public static void exit(int paramInt)
  {
    try
    {
      System.exit(paramInt);
    }
    catch (SecurityException localSecurityException)
    {
      warning("You may need to close some windows manually");
    }
  }
  
  public void close()
  {
    exit(1);
  }
  
  public static double NaN(String paramString)
  {
    warning(paramString);
    return (0.0D / 0.0D);
  }
  
  public static void warning(String paramString)
  {
    if (!guiWarn)
    {
      System.err.println(paramString);
      return;
    }
    if (msgWindow == null)
    {
      msgWindow = new ViewWindow("Errors and warnings", 25, 60);
      msgWindow.setClearButton(true);
    }
    msgWindow.append(paramString + "\n");
    if (!msgWindow.isVisible())
    {
      msgWindow.setVisible(true);
      msgWindow.show();
    }
  }
  
  public static void warning(Throwable paramThrowable, boolean paramBoolean)
  {
    if (paramBoolean)
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramThrowable.printStackTrace(new PrintStream(localByteArrayOutputStream));
      warning(localByteArrayOutputStream.toString());
    }
    else
    {
      warning(paramThrowable.toString());
    }
  }
  
  public static void warning(Throwable paramThrowable)
  {
    warning(paramThrowable, false);
  }
  
  public static void error(String paramString, Closeable paramCloseable)
  {
    warning(paramString);
    if (guiWarn)
    {
      new ModelessMsgBox("Fatal error", "See warning/error window", new Utility());
    }
    else
    {
      if (paramCloseable != null) {
        paramCloseable.close();
      }
      exit(1);
    }
  }
  
  public static void error(Throwable paramThrowable, Closeable paramCloseable)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramThrowable.printStackTrace(new PrintStream(localByteArrayOutputStream));
    error(localByteArrayOutputStream.toString(), paramCloseable);
  }
  
  public static void error(String paramString)
  {
    error(paramString, null);
  }
  
  public static void error(Throwable paramThrowable)
  {
    error(paramThrowable, null);
  }
  
  public static void setGUIWarn(boolean paramBoolean)
  {
    guiWarn = paramBoolean;
  }
  
  private static ViewWindow msgWindow = null;
  private static boolean guiWarn = false;
  
  public static double[] parseDoubles(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
    int i = localStringTokenizer.countTokens();
    double[] arrayOfDouble = new double[i];
    for (int j = 0; j < i; j++) {
      arrayOfDouble[j] = strtod(localStringTokenizer.nextToken());
    }
    return arrayOfDouble;
  }
  
  public static double[] nice(double paramDouble1, double paramDouble2, int paramInt, boolean paramBoolean)
  {
    if (paramDouble1 > paramDouble2) {
      return nice(paramDouble2, paramDouble1, paramInt, paramBoolean);
    }
    if (paramDouble2 <= 0.0D)
    {
      double[] arrayOfDouble1 = nice(-paramDouble2, -paramDouble1, paramInt, paramBoolean);
      for (int i = 0; i < arrayOfDouble1.length; i++) {
        arrayOfDouble1[i] *= -1.0D;
      }
      return arrayOfDouble1;
    }
    if (paramDouble1 == paramDouble2)
    {
      if (paramDouble2 == 0.0D) {
        return new double[] { -1.0D, 1.0D };
      }
      paramDouble2 *= 1.1D;
    }
    double d1 = 0.005D * (paramDouble2 - paramDouble1);
    if (!paramBoolean)
    {
      paramDouble1 -= d1;
      paramDouble2 += d1;
    }
    else
    {
      paramDouble1 += d1;
      paramDouble2 -= d1;
      paramInt -= 2;
    }
    d1 *= 2.0D;
    
    double d2 = 0.0D;
    double d3 = Math.floor(Math.log(paramDouble2) / Math.log(10.0D));
    double d4 = Math.pow(10.0D, d3);
    if (paramDouble1 * paramDouble2 > 0.0D) {
      while (d2 + d1 < paramDouble1)
      {
        d2 = d4 * (int)(paramDouble2 / d4);
        d4 /= 10.0D;
      }
    }
    d4 *= 10.0D;
    for (int j = 0; nTicks(paramDouble1, paramDouble2, d2, d4) < paramInt; j = (j + 1) % 3) {
      d4 /= nice_d[j];
    }
    int k = nTicks(paramDouble1, paramDouble2, d2, d4);
    while (d2 - d4 >= paramDouble1) {
      d2 -= d4;
    }
    if (paramBoolean)
    {
      if (Math.abs(paramDouble1 - d2) / d4 > 0.05D)
      {
        d2 -= d4;
        k++;
      }
      if (Math.abs(d2 + (k - 1) * d4 - paramDouble2) / d4 > 0.05D) {
        k++;
      }
    }
    double[] arrayOfDouble2 = new double[k];
    for (int m = 0; m < k; m++) {
      arrayOfDouble2[m] = (d2 + m * d4);
    }
    return arrayOfDouble2;
  }
  
  private static int nTicks(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    int i = 0;
    for (double d1 = paramDouble3; d1 <= paramDouble2; d1 += paramDouble4) {
      i++;
    }
    for (double d2 = paramDouble3 - paramDouble4; d2 >= paramDouble1; d2 -= paramDouble4) {
      i++;
    }
    return i;
  }
  
  private static final double[] nice_d = { 2.0D, 2.5D, 2.0D };
  
  public static String[] fmtNice(double[] paramArrayOfDouble)
  {
    if (paramArrayOfDouble.length < 2) {
      return new String[] { String.valueOf(paramArrayOfDouble) };
    }
    double d = 0.1D * Math.abs(paramArrayOfDouble[1] - paramArrayOfDouble[0]);
    if (paramArrayOfDouble[0] == paramArrayOfDouble[1]) {
      d = Math.max(1.0D, Math.abs(paramArrayOfDouble[0]));
    }
    String[] arrayOfString = new String[paramArrayOfDouble.length];
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      arrayOfString[i] = minFormat(paramArrayOfDouble[i], d);
    }
    return arrayOfString;
  }
  
  public static String minFormat(double paramDouble1, double paramDouble2)
  {
    if (paramDouble1 == 0.0D) {
      return "0";
    }
    for (int i = 1;; i++)
    {
      String str = format(paramDouble1, i);
      if (Math.abs(new Double(str).doubleValue() - paramDouble1) < paramDouble2) {
        return str;
      }
    }
  }
  
  public static String fixedFormat(double paramDouble, int paramInt)
  {
    double d = Math.pow(10.0D, paramInt);
    int i = Math.round((float)(paramDouble * d));
    String str = String.valueOf(i);
    if (paramInt == 0) {
      return str;
    }
    if (paramInt > 0)
    {
      for (int j = str.length(); j <= paramInt; j++) {
        str = "0" + str;
      }
      int k = str.length() - paramInt;
      return str.substring(0, k) + "." + str.substring(k);
    }
    return String.valueOf((int)(i / d + 0.01D));
  }
  
  private static String sciFormat(String paramString, int paramInt)
  {
    if (paramString.length() == 1) {
      return paramString + "e" + paramInt;
    }
    return 
      paramString.substring(0, 1) + "." + paramString.substring(1) + "e" + paramInt;
  }
  
  public static String format(double paramDouble, int paramInt)
  {
    return format(paramDouble, paramInt, true);
  }
  
  public static String format(double paramDouble, int paramInt, boolean paramBoolean)
  {
	StringBuffer localStringBuffer; 
    if ((Double.isInfinite(paramDouble)) || (Double.isNaN(paramDouble))) {
      return String.valueOf(paramDouble);
    }
    if (paramDouble < 0.0D) {
      return "-" + format(-paramDouble, paramInt, paramBoolean);
    }
    if (paramInt <= 0) {
      return fixedFormat(paramDouble, -paramInt);
    }
    if (paramDouble == 0.0D) {
      return "0";
    }
    paramInt = Math.min(paramInt, 15);
    int i = (int)(paramInt - Math.floor(Math.log(paramDouble) / Math.log(10.0D)) - 1.0D);
    long l = (long)Math.floor(paramDouble * Math.pow(10.0D, i) + 0.5D);
    String str = new Long(l).toString();
    int j = str.length();
    int k = j - i;
    if (paramBoolean)
    {
      while (str.charAt(j - 1) == '0')
      {
        j--;
        i--;
      }
      str = str.substring(0, j);
    }
    if (i <= 0)
    {
      if (i < -3) {
        return sciFormat(str, k - 1);
      }
      localStringBuffer = new StringBuffer(str);
      for (int m = 0; m < -i; m++) {
        localStringBuffer.append("0");
      }
      return new String(localStringBuffer);
    }
    if (k >= 0) {
      return str.substring(0, k) + "." + str.substring(k);
    }
    if (k < -3) {
      return sciFormat(str, k - 1);
    }
    localStringBuffer = new StringBuffer(".");
    for (int m = 0; m < -k; m++) {
      localStringBuffer.append("0");
    }
    localStringBuffer.append(str);
    return new String(localStringBuffer);
  }
  
  public static String format(String paramString, int paramInt)
  {
    if (paramString.length() > paramInt) {
      return paramString.substring(0, paramInt - 1);
    }
    StringBuffer localStringBuffer = new StringBuffer(paramInt + 1);
    localStringBuffer.append(paramString);
    while (localStringBuffer.length() < paramInt) {
      localStringBuffer.append(" ");
    }
    return localStringBuffer.toString();
  }
  
  public static void qsort(double[] paramArrayOfDouble)
  {
    qsort(paramArrayOfDouble, 0, paramArrayOfDouble.length - 1);
  }
  
  protected static void qsort(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    double d1 = paramArrayOfDouble[((i + j) / 2)];
    while (i <= j)
    {
      do
      {
        if (i >= paramInt2) {
          break;
        }
      } while (paramArrayOfDouble[i] < d1);
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
      do
      {
        if (i >= paramInt2) {
          break;
        }
      } while (paramArrayOfDouble[paramArrayOfInt[i]] < d);
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
	int j, m;
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
  
  private static void testTicks(double paramDouble1, double paramDouble2, int paramInt, boolean paramBoolean)
  {
    System.out.println("\n a=" + paramDouble1 + ", b=" + paramDouble2 + ", minTicks=" + paramInt + ", enclose=" + paramBoolean);
    double[] arrayOfDouble = nice(paramDouble1, paramDouble2, paramInt, paramBoolean);
    String[] arrayOfString = fmtNice(arrayOfDouble);
    for (int i = 0; i < arrayOfDouble.length; i++) {
      System.out.println(arrayOfString[i]);
    }
  }
  
  public static void main(String[] paramArrayOfString)
  {
    if (paramArrayOfString.length < 4)
    {
      testTicks(-7.99D, 3.99D, 4, true);
      testTicks(-799.0D, 399.0D, 4, true);
      testTicks(-0.007990000000000001D, 0.00399D, 4, true);
      testTicks(-79900000000.0D, 39900000000.0D, 4, true);
      testTicks(-7.99E-010D, 3.99E-010D, 4, true);
    }
    else
    {
      testTicks(strtod(paramArrayOfString[0]), strtod(paramArrayOfString[1]), strtoi(paramArrayOfString[2]), 
        paramArrayOfString[3].equals("true"));
    }
  }
}
