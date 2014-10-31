package pc.util;

import java.lang.reflect.Method;

public class FunctionPointer
  extends UniFunction
{
  static final Class[] oneDoubleArg = { Double.TYPE };
  private Object obj;
  private Method meth;
  
  public FunctionPointer(Object paramObject, String paramString)
  {
    this(paramObject.getClass(), paramString);
    this.obj = paramObject;
  }
  
  public FunctionPointer(Class paramClass, String paramString)
  {
    try
    {
      this.meth = paramClass.getMethod(paramString, oneDoubleArg);
      if (!this.meth.getReturnType().equals(Double.TYPE)) {
        Utility.warning(this.meth.toString() + ": Wrong return type");
      }
    }
    catch (Exception localException)
    {
      Utility.warning("method \"" + paramString + "\" not found\n" + localException);
    }
  }
  
  public double of(double paramDouble)
  {
    try
    {
      return 
        ((Double)this.meth.invoke(this.obj, new Double[] { new Double(paramDouble) })).doubleValue();
    }
    catch (Exception localException)
    {
      Utility.warning("Error in FunctionPointer: " + localException);
    }
    return (0.0D / 0.0D);
  }
  
  void setMin(double paramDouble, boolean paramBoolean)
  {
    this.xMin = paramDouble;
    this.closedMin = paramBoolean;
  }
  
  void setMax(double paramDouble, boolean paramBoolean)
  {
    this.xMax = paramDouble;
    this.closedMax = paramBoolean;
  }
  
  void setConv(int paramInt1, int paramInt2, double paramDouble1, double paramDouble2)
  {
    this.maxIter = paramInt1;
    this.maxSearch = paramInt2;
    this.feps = paramDouble1;
    this.xeps = paramDouble2;
  }
  
  public static void main(String[] paramArrayOfString)
  {
    FunctionPointer localFunctionPointer = new FunctionPointer(Math.class, "exp");
    localFunctionPointer.setMin(0.0D, false);
    localFunctionPointer.setMax(2.0D, false);
    localFunctionPointer.verbose = true;
    double d = Solve.search(localFunctionPointer, 2.0D, 1.0D, 0.1D);
  }
}
