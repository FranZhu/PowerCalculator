package pc.stat.anova;

public class FactorSet
{
  protected Factor[] facset;
  
  public FactorSet(int paramInt)
  {
    this.facset = new Factor[paramInt];
  }
  
  public void setLevels(int paramInt)
  {
    for (int i = 0; i < this.facset.length; i++) {
      this.facset[i].levels = paramInt;
    }
  }
  
  public int getLevels()
  {
    return this.facset[0].getLevels();
  }
}
