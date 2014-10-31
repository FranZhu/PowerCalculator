package pc.util;

public class SolveObject
  extends UniFunction
{
  public SolveObject(int paramInt, Solvable paramSolvable)
  {
    this.mode = paramInt;
    this.parent = paramSolvable;
  }
  
  public double of(double paramDouble)
  {
    return this.parent.solveHook(this.mode, paramDouble);
  }
  
  public double xMax = 9.0E+300D;
  public double xMin = -9.0E+300D;
  public boolean closedMax = false;
  public boolean closedMin = false;
  public int maxIter = 200;
  public int maxSearch = 50;
  public double xeps = 1.0E-006D;
  public double feps = 1.0E-010D;
  public boolean verbose = false;
  private Solvable parent;
  private int mode;
}
