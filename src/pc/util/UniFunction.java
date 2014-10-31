package pc.util;

public abstract class UniFunction
{
  public double xMax = 9.0E+300D;
  public double xMin = -9.0E+300D;
  public boolean closedMax = false;
  public boolean closedMin = false;
  public int maxIter = 100;
  public int maxSearch = 25;
  public double xeps = 1.0E-006D;
  public double feps = 1.0E-010D;
  public boolean verbose = false;
  
  public abstract double of(double paramDouble);
}
