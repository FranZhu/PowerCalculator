package pc.util;

import java.io.PrintStream;

public class Solve
{
  static void showParams(UniFunction paramUniFunction)
  {
    System.out.println("pc.util.Solve - parameters");
    System.out.println("maxIter = " + paramUniFunction.maxIter + 
      ", maxSearch = " + paramUniFunction.maxSearch + ", feps = " + paramUniFunction.feps + 
      ", xeps = " + paramUniFunction.xeps);
    System.out.println("Function bounds: " + 
      paramUniFunction.xMin + " (closed=" + paramUniFunction.closedMin + "), " + 
      paramUniFunction.xMax + " (closed=" + paramUniFunction.closedMax + ")");
  }
  
  public static double illinois(UniFunction paramUniFunction, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    if (Double.isNaN(paramDouble2 + paramDouble3)) {
      return Utility.NaN("solve: NaNs in starting interval");
    }
    double d1 = paramUniFunction.of(paramDouble2) - paramDouble1;
    double d2 = paramUniFunction.of(paramDouble3) - paramDouble1;
    if (paramUniFunction.verbose)
    {
      System.out.println("solve: x1 = " + paramDouble2);
      System.out.println("solve: x2 = " + paramDouble3);
    }
    return illinois(paramUniFunction, paramDouble1, paramDouble2, paramDouble3, d1, d2);
  }
  
  protected static double illinois(UniFunction paramUniFunction, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5)
  {
    int i = 0;
    if (paramUniFunction.verbose)
    {
      System.out.println("Call to pc.util.Solve.illinois");
      System.out.println("target = " + paramDouble1 + ", interval = [" + 
        paramDouble2 + "," + paramDouble3 + "], fcn values = [" + 
        paramDouble4 + "," + paramDouble5 + "]");
      showParams(paramUniFunction);
    }
    if (Double.isNaN(paramDouble4 + paramDouble5)) {
      return Utility.NaN("solve: function evaluates to NaN");
    }
    if (paramDouble4 * paramDouble5 > 0.0D) {
      return Utility.NaN("solve: bad starting interval");
    }
    while (i++ < paramUniFunction.maxIter)
    {
      if (Math.abs(paramDouble4 - paramDouble5) <= 1.0E-099D) {
        return Utility.NaN("solve: divide by zero");
      }
      double d1 = paramDouble3 - paramDouble5 * (paramDouble3 - paramDouble2) / (paramDouble5 - paramDouble4);
      if (paramUniFunction.verbose) {
        System.out.println("solve: xnew = " + d1);
      }
      double d2 = paramUniFunction.of(d1) - paramDouble1;
      if (Double.isNaN(d2)) {
        return Utility.NaN("solve: function evaluates to NaN");
      }
      if (d2 * paramDouble5 > 0.0D)
      {
        paramDouble4 /= 2.0D;
      }
      else
      {
        paramDouble2 = paramDouble3;
        paramDouble4 = paramDouble5;
      }
      paramDouble3 = d1;
      paramDouble5 = d2;
      if ((Math.abs(paramDouble2 - paramDouble3) <= paramUniFunction.xeps) || (Math.abs(d2) <= paramUniFunction.feps)) {
        return d1;
      }
    }
    return Utility.NaN("solve: too many iterations");
  }
  
  public static double search(UniFunction paramUniFunction, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d1 = paramDouble2 + paramDouble3;
    int i = 0;
    if (paramUniFunction.verbose)
    {
      System.out.println("Call to pc.util.Solve.search");
      System.out.println("target = " + paramDouble1 + ", start = " + 
        paramDouble2 + ", incr = " + paramDouble3);
      showParams(paramUniFunction);
    }
    if (Double.isNaN(paramDouble2 + paramDouble1 + paramDouble3)) {
      return Utility.NaN("solve: NaN encountered in initialization");
    }
    double d2 = paramUniFunction.of(paramDouble2) - paramDouble1;
    double d3 = paramUniFunction.of(d1) - paramDouble1;
    if (paramUniFunction.verbose)
    {
      System.out.println("search: start = " + paramDouble2);
      System.out.println("search: xnew = " + d1);
    }
    if (Double.isNaN(d3)) {
      return Utility.NaN("solve: function evaluates to NaN");
    }
    while (d2 * d3 > 0.0D)
    {
      i++;
      if (i > paramUniFunction.maxSearch) {
        return Utility.NaN("search: can't find enclosing interval after " + 
          paramUniFunction.maxSearch + " tries");
      }
      paramDouble3 = -1.5D * (d1 - paramDouble2) * d3 / (d3 - d2);
      
      paramDouble2 = d1;
      d2 = d3;
      d1 += paramDouble3;
      if (d1 <= paramUniFunction.xMin) {
        d1 = paramUniFunction.closedMin ? paramUniFunction.xMin : paramUniFunction.xMin + 0.1D * (paramDouble2 - paramUniFunction.xMin);
      }
      if (d1 >= paramUniFunction.xMax) {
        d1 = paramUniFunction.closedMax ? paramUniFunction.xMax : paramUniFunction.xMax - 0.1D * (paramUniFunction.xMax - paramDouble2);
      }
      if (paramUniFunction.verbose) {
        System.out.println("search: xnew = " + d1);
      }
      d3 = paramUniFunction.of(d1) - paramDouble1;
      if (Double.isNaN(d3)) {
        return Utility.NaN("solve: function evaluates to NaN");
      }
    }
    return illinois(paramUniFunction, paramDouble1, paramDouble2, d1, d2, d3);
  }
}
