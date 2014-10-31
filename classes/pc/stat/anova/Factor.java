package pc.stat.anova;

import pc.util.Utility;

public class Factor
  extends Term
{
  protected String name;
  protected int levels;
  protected boolean random;
  protected FactorSet siblings;
  
  public Factor() {}
  
  public Factor(String paramString, int paramInt, boolean paramBoolean)
  {
    setName(paramString);
    setLevels(paramInt);
    setRandom(paramBoolean);
  }
  
  public Factor(String paramString, int paramInt, Term paramTerm)
  {
    setLevels(paramInt);
    this.nestedIn = new Factor[paramTerm.order()];
    for (int i = 0; i < paramTerm.order(); i++) {
      this.nestedIn[i] = paramTerm.factor(i);
    }
    setName(paramString);
    setRandom(true);
  }
  
  public Factor(String paramString, int paramInt1, int paramInt2)
  {
    setLevels(paramInt1);
    this.nestedIn = new Factor[paramInt2];
    this.name = paramString;
    setRandom(true);
  }
  
  public void setName(String paramString)
  {
    paramString = paramString.trim();
    if (paramString.length() > 0)
    {
      this.name = new String(paramString);
      this.fullName = new StringBuffer(paramString);
      if (this.nestedIn != null)
      {
        this.fullName.append("(");
        for (int i = 0; i < this.nestedIn.length;)
        {
          this.fullName.append(this.nestedIn[i].getName());
          i++;
          if (i < this.nestedIn.length) {
            this.fullName.append(" ");
          }
        }
        this.fullName.append(")");
      }
    }
    else
    {
      Utility.error("Factor name cannot be null string");
    }
  }
  
  public String getName()
  {
    return this.fullName.toString();
  }
  
  public String getShortName()
  {
    return this.name;
  }
  
  public void setLevels(int paramInt)
  {
    if (paramInt > 1)
    {
      if (this.siblings == null) {
        this.levels = paramInt;
      } else {
        this.siblings.setLevels(paramInt);
      }
    }
    else {
      Utility.warning("Factor must have at least two levels");
    }
  }
  
  public int getLevels()
  {
    return this.levels;
  }
  
  public int order()
  {
    return 1;
  }
  
  public Factor factor(int paramInt)
  {
    if (paramInt == 0) {
      return this;
    }
    return null;
  }
  
  public void setRandom(boolean paramBoolean)
  {
    this.random = paramBoolean;
    if ((this.nestedIn != null) && (!paramBoolean)) {
      for (int i = 0; i < this.nestedIn.length; i++) {
        if (this.nestedIn[i].isRandom())
        {
          this.random = true;
          Utility.warning("Warning: fixed factor nested in a random factor:");
          Utility.warning("\t" + this.fullName + " was made random.");
        }
      }
    }
  }
  
  public boolean isRandom()
  {
    return this.random;
  }
  
  public boolean contains(Factor paramFactor)
  {
    if (equals(paramFactor)) {
      return true;
    }
    if (this.nestedIn == null) {
      return false;
    }
    for (int i = 0; i < this.nestedIn.length; i++) {
      if (this.nestedIn[i].contains(paramFactor)) {
        return true;
      }
    }
    return false;
  }
}
