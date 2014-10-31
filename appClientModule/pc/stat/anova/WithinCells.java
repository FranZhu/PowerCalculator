package pc.stat.anova;

public class WithinCells
  extends Factor
{
  private Model modl;
  
  public WithinCells(Model paramModel, int paramInt)
  {
    this.modl = paramModel;
    if (df() <= 0) {
      setName("Within");
    } else {
      setName("Residual");
    }
    setLevels(paramInt);
    setRandom(true);
  }
  
  public int df()
  {
    int i = span() - 1;
    for (int j = 0; j < this.modl.nTerm(); j++)
    {
      Term localTerm = this.modl.getTerm(j);
      if (!localTerm.equals(this)) {
        i -= localTerm.df();
      }
    }
    return i;
  }
  
  public int span()
  {
    return this.modl.getNobs();
  }
  
  public boolean containsFactor(Factor paramFactor)
  {
    return true;
  }
  
  public boolean containsTerm(Term paramTerm)
  {
    return true;
  }
  
  public boolean overlaps(Factor paramFactor)
  {
    return true;
  }
  
  public boolean isRandom()
  {
    return true;
  }
  
  public void setLevels(int paramInt)
  {
    this.levels = (paramInt < 1 ? 1 : paramInt);
  }
  
  public int order()
  {
    return 1;
  }
  
  public Factor factor(int paramInt)
  {
    return null;
  }
}
