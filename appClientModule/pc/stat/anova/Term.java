package pc.stat.anova;

import java.util.Vector;

public class Term
{
  protected Factor[] fac;
  protected StringBuffer fullName;
  protected Factor[] nestedIn;
  
  public Term() {}
  
  public Term(Factor[] paramArrayOfFactor)
  {
    this.fac = new Factor[paramArrayOfFactor.length];
    this.fullName = new StringBuffer();
    int i = 0;
    Vector localVector = new Vector(5, 5);
    for (int j = 0; j < paramArrayOfFactor.length; j++)
    {
      this.fac[j] = paramArrayOfFactor[j];
      if (j > 0) {
        this.fullName.append("*");
      }
      this.fullName.append(paramArrayOfFactor[j].name);
      Factor[] arrayOfFactor = paramArrayOfFactor[j].getNest();
      if (arrayOfFactor != null) {
        for (int m = 0; m < arrayOfFactor.length; m++)
        {
          int n = 1;
          Factor localFactor = arrayOfFactor[m];
          for (int i1 = 0; i1 < i; i1++) {
            if (localFactor.equals((Factor)localVector.elementAt(i1))) {
              n = 0;
            }
          }
          if (n != 0)
          {
            localVector.addElement(localFactor);
            i++;
          }
        }
      }
    }
    if (i > 0)
    {
      this.nestedIn = new Factor[i];
      String str = "(";
      for (int k = 0; k < i; k++)
      {
        this.nestedIn[k] = ((Factor)localVector.elementAt(k));
        this.fullName.append(str + this.nestedIn[k].name);
        str = " ";
      }
      this.fullName.append(")");
    }
  }
  
  public Term(Term paramTerm, Factor paramFactor)
  {
    this.fac = new Factor[paramTerm.order() + 1];
    this.fullName = new StringBuffer();
    for (int i = 0; i < paramTerm.order(); i++)
    {
      this.fac[i] = paramTerm.factor(i);
      if (i > 0) {
        this.fullName.append("*");
      }
      this.fullName.append(paramTerm.factor(i).name);
    }
    this.fullName.append("*" + paramFactor.name);
    this.fac[paramTerm.order()] = paramFactor;
    Factor[] arrayOfFactor1 = paramTerm.getNest();Factor[] arrayOfFactor2 = paramFactor.getNest();
    int j;
    if (arrayOfFactor1 != null)
    {
      if (arrayOfFactor2 == null)
      {
        setNest(arrayOfFactor1);
      }
      else
      {
        j = arrayOfFactor1.length;
        for (int k = 0; k < arrayOfFactor2.length; k++) {
          if (!paramTerm.containsFactor(arrayOfFactor2[k])) {
            j++;
          }
        }
        this.nestedIn = new Factor[j];
        j = arrayOfFactor1.length;
        int k;
        for ( k = 0; k < arrayOfFactor1.length; k++) {
          this.nestedIn[k] = arrayOfFactor1[k];
        }
        for (k = 0; k < arrayOfFactor2.length; k++) {
          if (!paramTerm.containsFactor(arrayOfFactor2[k])) {
            this.nestedIn[(j++)] = arrayOfFactor2[k];
          }
        }
      }
    }
    else {
      setNest(arrayOfFactor2);
    }
    if (this.nestedIn != null)
    {
      this.fullName.append("(" + this.nestedIn[0].getName());
      for (j = 1; j < this.nestedIn.length; j++) {
        this.fullName.append(" " + this.nestedIn[j].getName());
      }
      this.fullName.append(")");
    }
  }
  
  protected void setNest(Factor[] paramArrayOfFactor)
  {
    if (paramArrayOfFactor == null) {
      return;
    }
    this.nestedIn = new Factor[paramArrayOfFactor.length];
    for (int i = 0; i < paramArrayOfFactor.length; i++) {
      this.nestedIn[i] = paramArrayOfFactor[i];
    }
  }
  
  public Factor[] getNest()
  {
    return this.nestedIn;
  }
  
  public int order()
  {
    return this.fac.length;
  }
  
  public Factor factor(int paramInt)
  {
    return this.fac[paramInt];
  }
  
  public Factor[] getFactors()
  {
    return this.fac;
  }
  
  public String getName()
  {
    return this.fullName.toString();
  }
  
  public boolean isRandom()
  {
    for (int i = 0; i < this.fac.length; i++) {
      if (this.fac[i].isRandom()) {
        return true;
      }
    }
    return false;
  }
  
  public boolean containsTerm(Term paramTerm)
  {
    for (int i = 0; i < paramTerm.order(); i++) {
      if (!containsFactor(paramTerm.factor(i))) {
        return false;
      }
    }
    return true;
  }
  
  public boolean containsFactor(Factor paramFactor)
  {
    for (int i = 0; i < order(); i++) {
      if (factor(i).contains(paramFactor)) {
        return true;
      }
    }
    return false;
  }
  
  public boolean overlaps(Factor paramFactor)
  {
    Factor[] arrayOfFactor = paramFactor.getNest();
    for (int i = 0; i < order(); i++)
    {
      Factor localFactor = factor(i);
      if (localFactor.equals(paramFactor)) {
        return true;
      }
      if (arrayOfFactor != null) {
        for (int k = 0; k < arrayOfFactor.length; k++) {
          if (arrayOfFactor[k].overlaps(localFactor)) {
            return true;
          }
        }
      }
    }
    if (this.nestedIn != null) {
      int i;
      for (i = 0; i < this.nestedIn.length; i++)
      {
        if (this.nestedIn[i].equals(paramFactor)) {
          return true;
        }
        if (arrayOfFactor != null) {
          for (int j = 0; j < arrayOfFactor.length; j++) {
            if (arrayOfFactor[j].overlaps(this.nestedIn[i])) {
              return true;
            }
          }
        }
      }
    }
    return false;
  }
  
  public boolean overlaps(Term paramTerm)
  {
    if ((paramTerm instanceof Factor)) {
      return containsFactor((Factor)paramTerm);
    }
    Factor[] arrayOfFactor = paramTerm.fac;
    for (int i = 0; i < arrayOfFactor.length; i++) {
      if (overlaps(arrayOfFactor[i])) {
        return true;
      }
    }
    return false;
  }
  
  public Term minus(Factor[] paramArrayOfFactor)
  {
    if (paramArrayOfFactor == null) {
      return this;
    }
    if (paramArrayOfFactor.length == 0) {
      return this;
    }
    Factor[] arrayOfFactor = new Factor[this.fac.length - paramArrayOfFactor.length];
    Term localTerm = new Term(paramArrayOfFactor);
    int j;
    for (int i = j = 0; i < this.fac.length; i++) {
      if (!localTerm.containsFactor(this.fac[i])) {
        arrayOfFactor[(j++)] = this.fac[i];
      }
    }
    return new Term(arrayOfFactor);
  }
  
  public int df()
  {
    int i = 1;
    for (int j = 0; j < order(); j++) {
      i *= (factor(j).getLevels() - 1);
    }
    if (this.nestedIn != null) {
      int j;
      for (j = 0; j < this.nestedIn.length; j++) {
        i *= this.nestedIn[j].span();
      }
    }
    return i;
  }
  
  public int span()
  {
    int i = 1, j;
    for (j = 0; j < order(); j++) {
      i *= factor(j).getLevels();
    }
    if (this.nestedIn != null) {
      for (j = 0; j < this.nestedIn.length; j++) {
        i *= this.nestedIn[j].span();
      }
    }
    return i;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer(getName());
    localStringBuffer.append(" <" + (isRandom() ? "random" : "fixed") + "> ");
    if ((this instanceof Factor)) {
      localStringBuffer.append("" + ((Factor)this).getLevels() + " levels, ");
    }
    localStringBuffer.append("" + df() + " df");
    return localStringBuffer.toString();
  }
}
