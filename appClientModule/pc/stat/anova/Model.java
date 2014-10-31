package pc.stat.anova;

import java.io.PrintStream;
import java.util.StringTokenizer;
import java.util.Vector;
import pc.stat.dist.F;
import pc.util.LinAlg;
import pc.util.Utility;

public class Model
{
  Vector fac;
  Vector term;
  Vector fraction;
  int[] coef;
  double[] leadCoef;
  double[] denom;
  double[] dendf;
  double[][] EMSC;
  double[][] LU;
  int[] LUp;
  public boolean recalcLU = true;
  
  public Model()
  {
    this.fac = new Vector(5, 5);
    this.fraction = new Vector(5, 5);
    this.term = new Vector();
  }
  
  public Model(String paramString)
  {
    this.fac = new Vector(5, 5);
    this.fraction = new Vector(5, 5);
    this.term = new Vector();
    
    StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString, "+");
    while (localStringTokenizer1.hasMoreTokens())
    {
      StringTokenizer localStringTokenizer2 = new StringTokenizer(localStringTokenizer1.nextToken(), "|");
      int i = nTerm();
      while (localStringTokenizer2.hasMoreTokens()) {
        addTerm(localStringTokenizer2.nextToken(), i);
      }
    }
  }
  
  public Factor getFac(int paramInt)
  {
    return (Factor)this.fac.elementAt(paramInt);
  }
  
  public Factor getFac(String paramString)
  {
    for (int i = 0; i < nFac(); i++) {
      if (paramString.equalsIgnoreCase(getFac(i).name)) {
        return getFac(i);
      }
    }
    Utility.warning("Warning: Factor named '" + paramString + "' not found");
    return null;
  }
  
  public Term getTerm(int paramInt)
  {
    return (Term)this.term.elementAt(paramInt);
  }
  
  public int nFac()
  {
    return this.fac.size();
  }
  
  public int nTerm()
  {
    return this.term.size();
  }
  
  public void addFactor(Factor paramFactor)
  {
    this.fac.addElement(paramFactor);
    this.term.addElement(paramFactor);
  }
  
  public void addFactor(Factor paramFactor, boolean paramBoolean)
  {
    int i = this.term.size();
    this.fac.addElement(paramFactor);
    this.term.addElement(paramFactor);
    if (!paramBoolean) {
      return;
    }
    for (int j = 0; j < i; j++)
    {
      Term localTerm = getTerm(j);
      if (!localTerm.overlaps(paramFactor)) {
        this.term.addElement(new Term(localTerm, paramFactor));
      }
    }
  }
  
  public void addTerm(Term paramTerm)
  {
    this.term.addElement(paramTerm);
  }
  
  private void addTerm(String paramString, int paramInt)
  {
    String str = "\n\t\r ()*";
    String[] arrayOfString1 = new String[20];
    String[] arrayOfString2 = new String[20];
    char m;
    String localObject;
    
    int i = 0;int j = 0;int k = 0;
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString, str, true);
    while (localStringTokenizer.hasMoreTokens())
    {
      localObject = localStringTokenizer.nextToken();
      m = ((String)localObject).charAt(0);
      if (str.indexOf(m) > -1)
      {
        if (m == 40) {
          k++;
        } else if (m == 41) {
          k--;
        }
      }
      else if (k == 0) {
        arrayOfString1[(i++)] = localObject;
      } else if (k == 1) {
        arrayOfString2[(j++)] = localObject;
      }
    }
    if (i == 0) {
      return;
    }
    if (i == 1)
    {
      Factor localObject1 = new Factor(arrayOfString1[0], 2, false);
      if (j > 0)
      {
        ((Factor)localObject1).nestedIn = new Factor[j];
        for (m = 0; m < j; m++) {
          ((Factor)localObject1).nestedIn[m] = getFac(arrayOfString2[m]);
        }
        ((Factor)localObject1).setName(arrayOfString1[0]);
      }
      int m1 = this.term.size();
      addFactor((Factor)localObject1);
      for (int n = paramInt; n < m1; n++)
      {
        Term localTerm2 = getTerm(n);
        if (!localTerm2.overlaps((Factor)localObject1)) {
          this.term.addElement(new Term(localTerm2, (Factor)localObject1));
        }
      }
      return;
    }
    Factor [] localObject2 = new Factor[i];
    for (int m2 = 0; m2 < i; m2++) {
      localObject2[m2] = getFac(arrayOfString1[m2]);
    }
    Term localTerm1 = new Term((Factor[])localObject2);
    addTerm(localTerm1);
  }
  
  public void removeTerm(int paramInt)
  {
    this.term.removeElementAt(paramInt);
  }
  
  public void setLevels(String paramString)
  {
    StringTokenizer localStringTokenizer1 = new StringTokenizer(paramString);
    while (localStringTokenizer1.hasMoreTokens())
    {
      StringTokenizer localStringTokenizer2 = new StringTokenizer(localStringTokenizer1.nextToken(), "=");
      int i = Integer.parseInt(localStringTokenizer1.nextToken());
      int j = localStringTokenizer2.countTokens();int k = 0;
      FactorSet localFactorSet = new FactorSet(j);
      while (localStringTokenizer2.hasMoreTokens())
      {
        String str = localStringTokenizer2.nextToken();
        Factor localFactor;
        if (str.charAt(0) == '/')
        {
          localFactor = getFac(str.substring(1));
          fractionBy(localFactor);
        }
        else
        {
          localFactor = getFac(str);
        }
        localFactor.levels = i;
        if (j > 1)
        {
          localFactorSet.facset[(k++)] = localFactor;
          localFactor.siblings = localFactorSet;
        }
      }
    }
  }
  
  public void setRandom(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
    while (localStringTokenizer.hasMoreTokens()) {
      getFac(localStringTokenizer.nextToken()).setRandom(true);
    }
    sanityCheck();
  }
  
  public void setFixed(String paramString)
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
    while (localStringTokenizer.hasMoreTokens()) {
      getFac(localStringTokenizer.nextToken()).setRandom(false);
    }
  }
  
  public void sanityCheck()
  {
    for (int i = 0; i < nFac(); i++)
    {
      Factor localFactor = getFac(i);
      localFactor.setRandom(localFactor.isRandom());
    }
  }
  
  private void fractionBy(Factor paramFactor)
  {
    this.fraction.addElement(paramFactor);
  }
  
  public int getNobs()
  {
    int i = 1;
    for (int j = 0; j < this.fac.size(); j++) {
      i *= ((Factor)this.fac.elementAt(j)).getLevels();
    }
    for (int j = 0; j < this.fraction.size(); j++) {
      i /= ((Factor)this.fraction.elementAt(j)).getLevels();
    }
    return i;
  }
  
  private void getCoefs()
  {
    int i = this.term.size();
    int j = getNobs();
    if (this.coef == null)
    {
      this.coef = new int[i];
      this.leadCoef = new double[i];
      this.denom = new double[i];
      this.dendf = new double[i];
    }
    for (int k = 0; k < i; k++)
    {
      Term localTerm = getTerm(k);
      this.coef[k] = ((int)(j / localTerm.span() + 0.1D));
    }
  }
  
  private boolean include(Term paramTerm1, Term paramTerm2)
  {
    if (paramTerm2.isRandom()) {
      return paramTerm2.containsTerm(paramTerm1);
    }
    return false;
  }
  
  public double[][] EMSCoefs()
  {
    int i = this.term.size();
    double[][] arrayOfDouble = new double[i][];
    getCoefs();
    for (int j = 0; j < i; j++)
    {
      Term localTerm = getTerm(j);
      arrayOfDouble[j] = new double[i];
      for (int k = 0; k < i; k++) {
        arrayOfDouble[j][k] = (include(localTerm, getTerm(k)) ? this.coef[k] : 0.0D);
      }
      arrayOfDouble[j][j] = this.coef[j];
    }
    return arrayOfDouble;
  }
  
  public double[][] getErrorTerms(double[][] paramArrayOfDouble)
  {
    int i = this.term.size();int[] arrayOfInt = new int[i];
    double[][] arrayOfDouble = LinAlg.transpose(paramArrayOfDouble);
    double[][] arrayOfDouble1 = new double[i][];
    if (!LinAlg.LUInPlace(arrayOfDouble, arrayOfInt))
    {
      Utility.warning("Variance components are not all estimable");
      return (double[][])null;
    }
    for (int j = 0; j < i; j++)
    {
      arrayOfDouble1[j] = LinAlg.copy(paramArrayOfDouble[j]);
      arrayOfDouble1[j][j] = 0.0D;
      arrayOfDouble1[j] = LinAlg.LUSolveInPlace(arrayOfDouble, arrayOfInt, arrayOfDouble1[j]);
    }
    return arrayOfDouble1;
  }
  
  public double[][] getErrorTerms()
  {
    return getErrorTerms(EMSCoefs());
  }
  
  public double[] power(double[] paramArrayOfDouble, double paramDouble)
  {
    int i = this.term.size();
    double[] arrayOfDouble1 = new double[i];
    

    double[] arrayOfDouble2 = new double[i];
    double[] arrayOfDouble3 = new double[i];
    double[][] arrayOfDouble4 = EMSCoefs();
    double[][] arrayOfDouble5 = getErrorTerms(arrayOfDouble4);
    double [] localObject3;
    
    for (int j = 0; j < i; j++)
    {
      localObject3 = arrayOfDouble4[j];
      arrayOfDouble1[j] = (localObject3[j] * paramArrayOfDouble[j] * paramArrayOfDouble[j]);
      arrayOfDouble2[j] = 0.0D;
      for (int k = 0; k < i; k++) {
        arrayOfDouble2[j] += localObject3[k] * paramArrayOfDouble[k] * paramArrayOfDouble[k];
      }
      this.denom[j] = (arrayOfDouble2[j] - arrayOfDouble1[j]);
      this.leadCoef[j] = localObject3[j];
    }
    for (int j = 0; j < i; j++) {
      try
      {
        Term localObject4 = getTerm(j);
        double d2 = 0.0D;double d3 = 0.0D;
        int m = 1;
        for (int n = 0; n < i; n++)
        {
          double d1;
          if ((d1 = arrayOfDouble5[j][n]) > 1.0E-006D)
          {
            double d4 = d1 * arrayOfDouble2[n];
            d2 += d4;
            d3 += d4 * d4 / getTerm(n).df();
            m = 0;
          }
        }
        if (m != 0)
        {
          arrayOfDouble3[j] = -1.0D;
          this.dendf[j] = (0.0D / 0.0D);
        }
        else
        {
          this.dendf[j] = (d2 * d2 / d3);
          arrayOfDouble3[j] = (this.dendf[j] > 0.1D ? F.power(arrayOfDouble1[j] / this.denom[j], ((Term)localObject4).df(), this.dendf[j], paramDouble, ((Term)localObject4).isRandom()) : -2.0D);
        }
      }
      catch (ArithmeticException localArithmeticException)
      {
        arrayOfDouble3[j] = -3.0D;
      }
    }
    return arrayOfDouble3;
  }
  
  public double[] getPowerInfo(int paramInt)
  {
    return new double[] { this.leadCoef[paramInt], this.denom[paramInt], getTerm(paramInt).df(), this.dendf[paramInt] };
  }
  
  public void printEMS()
  {
    System.out.print(EMSString());
  }
  
  public String EMSString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    int i = this.term.size();
    double[][] arrayOfDouble1 = EMSCoefs();
    double[][] arrayOfDouble2 = getErrorTerms();
    
    localStringBuffer.append("Expected mean squares\n");
    for (int j = 0; j < i; j++)
    {
      Term localTerm = getTerm(j);
      localStringBuffer.append("\n" + localTerm + "\n");
      localStringBuffer.append("  EMS =");
      int k = 1;
      for (int m = 0; m < i; m++) {
        if (arrayOfDouble1[j][m] != 0.0D)
        {
          localStringBuffer.append(k != 0 ? " " : " + ");
          k = 0;
          if (arrayOfDouble1[j][m] != 1.0D) {
            localStringBuffer.append(arrayOfDouble1[j][m] + "*Var{" + getTerm(m).getName() + "}");
          } else {
            localStringBuffer.append("Var{" + getTerm(m).getName() + "}");
          }
        }
      }
      localStringBuffer.append("\n  Denom =");
      k = 1;
      for (int m = 0; m < i; m++) {
        if (arrayOfDouble2[j][m] != 0.0D)
        {
          localStringBuffer.append(k != 0 ? " " : " + ");
          k = 0;
          if (arrayOfDouble2[j][m] != 1.0D) {
            localStringBuffer.append(arrayOfDouble2[j][m] + "*MS{" + getTerm(m).getName() + "}");
          } else {
            localStringBuffer.append("MS{" + getTerm(m).getName() + "}");
          }
        }
      }
      localStringBuffer.append("\n");
    }
    return localStringBuffer.toString();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    for (int i = 0; i < this.term.size(); i++)
    {
      if (i > 0) {
        localStringBuffer.append("\n");
      }
      localStringBuffer.append(getTerm(i).toString());
    }
    return localStringBuffer.toString();
  }
  
  public Vector getCompRestr(Term paramTerm)
  {
    if (paramTerm.isRandom()) {
      return null;
    }
    Vector localVector1 = new Vector();
    localVector1.addElement(null);
    if (paramTerm.order() > 1) {
      for (int i = 0; i < nTerm(); i++)
      {
        Term localTerm = getTerm(i);
        if ((localTerm.isRandom()) && (localTerm.overlaps(paramTerm)) && (!localTerm.containsTerm(paramTerm)))
        {
          Vector localVector2 = new Vector();
          for (int j = 0; j < paramTerm.order(); j++) {
            if (localTerm.containsFactor(paramTerm.factor(j))) {
              localVector2.addElement(paramTerm.factor(j));
            }
          }
          Factor[] arrayOfFactor = new Factor[localVector2.size()];
          for (int k = 0; k < localVector2.size(); k++) {
            arrayOfFactor[k] = ((Factor)localVector2.elementAt(k));
          }
          localVector1.addElement(arrayOfFactor);
        }
      }
    }
    return localVector1;
  }
  
  public Vector getAllCompRestr(Term paramTerm)
  {
    if (paramTerm.isRandom()) {
      return null;
    }
    Vector localVector = new Vector(5);
    localVector.addElement(null);
    if (paramTerm.order() > 1) {
      for (int i = 0; i < nTerm(); i++)
      {
        Term localTerm = getTerm(i);
        if ((localTerm != paramTerm) && (!localTerm.isRandom()) && (paramTerm.containsTerm(localTerm))) {
          if ((localTerm instanceof Factor)) {
            localVector.addElement(new Factor[] { (Factor)localTerm });
          } else {
            localVector.addElement(localTerm.fac);
          }
        }
      }
    }
    return localVector;
  }
  
  public double[] getCompVariance(Term paramTerm, Factor[] paramArrayOfFactor, double[] paramArrayOfDouble)
  {
    double[] arrayOfDouble1 = getCompCoefs(paramTerm, paramArrayOfFactor);
    double[] arrayOfDouble2 = getCompErrorTerms(arrayOfDouble1);
    double[] arrayOfDouble3 = LinAlg.constant(0.0D, nTerm());
    double d1 = 0.0D;double d2 = 0.0D;
    for (int i = 0; i < nTerm(); i++) {
      if (arrayOfDouble1[i] > 0.0D)
      {
        d1 += arrayOfDouble1[i] * paramArrayOfDouble[i] * paramArrayOfDouble[i];
        for (int j = 0; j < nTerm(); j++) {
          if (this.EMSC[i][j] > 0.0D) {
            arrayOfDouble3[i] += this.EMSC[i][j] * paramArrayOfDouble[j] * paramArrayOfDouble[j];
          }
        }
      }
    }
    for (int i = 0; i < nTerm(); i++) {
      if (arrayOfDouble2[i] != 0.0D)
      {
        double d4 = arrayOfDouble2[i] * arrayOfDouble3[i];
        d2 += d4 * d4 / getTerm(i).df();
      }
    }
    double d3 = d1 * d1 / d2;
    return new double[] { d1, d3 };
  }
  
  public double[] getCompCoefs(Term paramTerm, Factor[] paramArrayOfFactor)
  {
    double[] arrayOfDouble = new double[nTerm()];
    Term localTerm1 = paramTerm;
    if (paramArrayOfFactor != null) {
      localTerm1 = paramTerm.minus(paramArrayOfFactor);
    }
    for (int i = 0; i < nTerm(); i++)
    {
      Term localTerm2 = getTerm(i);
      if ((localTerm2.isRandom()) && (localTerm2.overlaps(localTerm1)))
      {
        arrayOfDouble[i] = (1.0D / localTerm2.span());
        for (int j = 0; j < paramTerm.order(); j++) {
          if (localTerm2.containsFactor(paramTerm.factor(j))) {
            arrayOfDouble[i] *= paramTerm.factor(j).span();
          }
        }
      }
      else
      {
        arrayOfDouble[i] = 0.0D;
      }
    }
    return arrayOfDouble;
  }
  
  public double[] getCompErrorTerms(double[] paramArrayOfDouble)
  {
    if (this.recalcLU)
    {
      this.recalcLU = false;
      this.EMSC = EMSCoefs();
      this.LU = LinAlg.transpose(this.EMSC);
      this.LUp = new int[nTerm()];
      if (!LinAlg.LUInPlace(this.LU, this.LUp))
      {
        Utility.warning("Variance components are not all estimable");
        return null;
      }
    }
    double[] arrayOfDouble = LinAlg.copy(paramArrayOfDouble);
    arrayOfDouble = LinAlg.LUSolveInPlace(this.LU, this.LUp, arrayOfDouble);
    return arrayOfDouble;
  }
  
  public String[] getCompVarString(Term paramTerm, Factor[] paramArrayOfFactor)
  {
    double[] arrayOfDouble1 = getCompCoefs(paramTerm, paramArrayOfFactor);
    double[] arrayOfDouble2 = getCompErrorTerms(arrayOfDouble1);
    int i = 1;int j = 1;
    StringBuffer localStringBuffer1 = new StringBuffer("");StringBuffer localStringBuffer2 = new StringBuffer("");
    for (int k = 0; k < nTerm(); k++)
    {
      if (arrayOfDouble1[k] > 0.0D)
      {
        if (i == 0) {
          localStringBuffer1.append(" + ");
        }
        i = 0;
        localStringBuffer1.append("Var{" + getTerm(k).getName() + "}");
        localStringBuffer1.append("/" + Utility.format(1.0D / arrayOfDouble1[k], 3));
      }
      if (arrayOfDouble2[k] > 0.0D)
      {
        if (j == 0) {
          localStringBuffer2.append(" + ");
        }
        j = 0;
        localStringBuffer2.append(Utility.format(arrayOfDouble2[k], 3) + "*");
        localStringBuffer2.append("MS{" + getTerm(k).getName() + "}");
      }
    }
    return new String[] { localStringBuffer1.toString(), localStringBuffer2.toString() };
  }
}
