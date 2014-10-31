package pc.stat.anova;

public class Residual
  extends Term
{
  private Model modl;
  
  public Residual(Model paramModel)
  {
    this.modl = paramModel;
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
  
  public String getName()
  {
    return "RESIDUAL";
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
  
  public int order()
  {
    return 1;
  }
  
  public Factor factor(int paramInt)
  {
    return null;
  }
  
  protected void setNest(Factor[] paramArrayOfFactor) {}
}
