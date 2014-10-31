package pc.util;

import java.lang.reflect.Method;

public class NumAnal
{
  static double aiFac = 10.0D;
  static double AA = 0.0D;
  static double BB = 1.0D;
  static int fCount = 0;
  static int maxFcnCalls = 2000;
  static final Class[] oneDoubleArg = { Double.TYPE };
  static Method fMethod;
  static Method gMethod;
  static Object caller;
  static Object gCaller;
  
  private static double f(double paramDouble)
  {
    if (++fCount > maxFcnCalls) {
      return Utility.NaN("integral(): too many function calls");
    }
    try
    {
      return 
        ((Double)fMethod.invoke(caller, new Double[] { new Double(paramDouble) })).doubleValue();
    }
    catch (Exception localException)
    {
      Utility.warning("Error in f(): " + localException);
      Utility.warning("fMethod = " + fMethod + ", caller = " + caller);
    }
    return (0.0D / 0.0D);
  }
  
  public static double fHalfLine(double paramDouble)
  {
    try
    {
      double d1 = AA + BB * paramDouble / (1.0D - paramDouble);
      double d2 = BB / (1.0D - paramDouble) / (1.0D - paramDouble);
      double d3 = ((Double)gMethod.invoke(gCaller, 
        new Double[] { new Double(d1) })).doubleValue();
      return d3 * d2;
    }
    catch (Exception localException)
    {
      return Utility.NaN("Error in fHalfLine(): " + localException);
    }
  }
  
  public static synchronized double integral(Class paramClass, String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return integral(paramClass, paramString, paramDouble1, paramDouble2, paramDouble3, false, (0.0D / 0.0D), (0.0D / 0.0D));
  }
  
  public static synchronized double integral(Class paramClass, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean)
  {
    return integral(paramClass, paramString, paramDouble1, paramDouble2, paramDouble3, paramBoolean, (0.0D / 0.0D), (0.0D / 0.0D));
  }
  
  public static synchronized double integral(Object paramObject, String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return integral(paramObject, paramString, paramDouble1, paramDouble2, paramDouble3, false, (0.0D / 0.0D), (0.0D / 0.0D));
  }
  
  public static synchronized double integral(Object paramObject, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean)
  {
    return integral(paramObject, paramString, paramDouble1, paramDouble2, paramDouble3, paramBoolean, (0.0D / 0.0D), (0.0D / 0.0D));
  }
  
  public static synchronized double integral(Object paramObject, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean, double paramDouble4, double paramDouble5)
  {
    caller = paramObject;
    return integral(paramObject.getClass(), paramString, paramDouble1, paramDouble2, paramDouble3, paramBoolean, paramDouble4, paramDouble5);
  }
  
  public static synchronized double integral(Class paramClass, String paramString, double paramDouble1, double paramDouble2, double paramDouble3, boolean paramBoolean, double paramDouble4, double paramDouble5)
  {
	double d1, d4;
    boolean bool1 = Double.isInfinite(paramDouble1);boolean bool2 = Double.isInfinite(paramDouble2);
    if ((bool1) || (bool2))
    {
      if ((bool1) && (bool2))
      {
        d1 = integral(paramClass, paramString, paramDouble1, AA, paramDouble3 / 2.0D, true, 0.0D, 0.0D);
        int i = fCount;
        double d3 = integral(paramClass, paramString, AA, paramDouble2, paramDouble3 / 2.0D, true, 0.0D, 0.0D);
        fCount += i;
        return d1 + d3;
      }
      if (bool1)
      {
        AA = paramDouble2;
        BB = -Math.abs(BB);
        gMethod = setupMethod(paramClass, paramString);
        return -integral(NumAnal.class, "fHalfLine", 0.0D, 1.0D, paramDouble3, true, 0.0D, 0.0D);
      }
      AA = paramDouble1;
      BB = Math.abs(BB);
      gMethod = setupMethod(paramClass, paramString);
      return integral(NumAnal.class, "fHalfLine", 0.0D, 1.0D, paramDouble3, true, 0.0D, 0.0D);
    }
    fMethod = setupMethod(paramClass, paramString);
    
    fCount = 0;
    d1 = (paramDouble1 + paramDouble2) / 2.0D;double d2 = f(d1);
    if (Double.isNaN(paramDouble4 + paramDouble5 + d2)) {
      return Utility.NaN("integral: NaN in function evaluation");
    }
    if (paramBoolean)
    {
      double d5 = (paramDouble2 - paramDouble1) / 4.0D;
      double d6 = f(paramDouble1 + d5);
      double d7 = f(paramDouble2 - d5);
      d4 = 4.0D * d5 * (2.0D * (d6 + d7) - d2) / 3.0D;
      return openRefineIntegral(paramDouble1, d1, paramDouble2, d6, d2, d7, d4, paramDouble3);
    }
    if (Double.isNaN(paramDouble4)) {
      paramDouble4 = f(paramDouble1);
    }
    if (Double.isNaN(paramDouble5)) {
      paramDouble5 = f(paramDouble2);
    }
    d4 = (paramDouble4 + 4.0D * d2 + paramDouble5) * (paramDouble2 - paramDouble1) / 6.0D;
    return refineIntegral(paramDouble1, d1, paramDouble2, paramDouble4, d2, paramDouble5, d4, paramDouble3);
  }
  
  private static Method setupMethod(Class paramClass, String paramString)
  {
    try
    {
      Method localMethod = paramClass.getMethod(paramString, oneDoubleArg);
      if (localMethod.getReturnType().equals(Double.TYPE)) {
        return localMethod;
      }
      Utility.warning(localMethod.toString() + ": Wrong return type");
    }
    catch (Exception localException)
    {
      Utility.warning("method \"" + paramString + "\" not found\n" + localException);
    }
    return null;
  }
  
  static double refineIntegral(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
  {
    double d1 = (paramDouble3 - paramDouble1) / 4.0D;
    double d2 = f(paramDouble1 + d1);double d3 = f(paramDouble3 - d1);
    double d4 = (paramDouble4 + 4.0D * d2 + paramDouble5) * d1 / 3.0D;
    double d5 = (paramDouble5 + 4.0D * d3 + paramDouble6) * d1 / 3.0D;
    if (Double.isNaN(d2 + d3)) {
      return Utility.NaN("refineIntegral: NaN in function evaluation");
    }
    if (Math.abs(d4 + d5 - paramDouble7) < aiFac * paramDouble8) {
      return d4 + d5;
    }
    return 
      refineIntegral(paramDouble1, paramDouble1 + d1, paramDouble2, paramDouble4, d2, paramDouble5, d4, paramDouble8 / 2.0D) + refineIntegral(paramDouble2, paramDouble3 - d1, paramDouble3, paramDouble5, d3, paramDouble6, d5, paramDouble8 / 2.0D);
  }
  
  static double openRefineIntegral(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8)
  {
    double d1 = (paramDouble3 - paramDouble1) / 16.0D;double d2 = 4.0D * d1 / 3.0D;
    double d3 = f(paramDouble1 + d1);double d4 = f(paramDouble1 + 2.0D * d1);double d5 = f(paramDouble1 + 3.0D * d1);
    double d6 = f(paramDouble3 - 3.0D * d1);double d7 = f(paramDouble3 - 2.0D * d1);double d8 = f(paramDouble3 - d1);
    double d9 = d2 * (2.0D * (d3 + d5) - d4);
    double d10 = d2 * (paramDouble4 + 4.0D * paramDouble5 + paramDouble6);
    double d11 = d2 * (2.0D * (d6 + d8) - d7);
    double d12 = d9 + d10 + d11;
    if (Double.isNaN(d9 + d11)) {
      return Utility.NaN("openRefineIntegral: NaN in function evaluation");
    }
    if (Math.abs(d12 - paramDouble7) < aiFac * paramDouble8) {
      return d12;
    }
    return 
    
      openRefineIntegral(paramDouble1, paramDouble1 + 2.0D * d1, paramDouble1 + 4.0D * d1, d3, d4, d5, d9, paramDouble8 / 4.0D) + refineIntegral(paramDouble1 + 4.0D * d1, paramDouble2, paramDouble3 - 4.0D * d1, paramDouble4, paramDouble5, paramDouble6, d10, paramDouble8 / 2.0D) + openRefineIntegral(paramDouble3 - 4.0D * d1, paramDouble3 - 2.0D * d1, paramDouble3, d6, d7, d8, d11, paramDouble8 / 4.0D);
  }
  
  public static int getCallCount()
  {
    return fCount;
  }
}
