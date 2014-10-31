package org.rosuda.JRI;

import java.util.Vector;

public class REXP
{
  public static final int XT_NULL = 0;
  public static final int XT_INT = 1;
  public static final int XT_DOUBLE = 2;
  public static final int XT_STR = 3;
  public static final int XT_LANG = 4;
  public static final int XT_SYM = 5;
  public static final int XT_BOOL = 6;
  public static final int XT_VECTOR = 16;
  public static final int XT_LIST = 17;
  public static final int XT_CLOS = 18;
  public static final int XT_ARRAY_INT = 32;
  public static final int XT_ARRAY_DOUBLE = 33;
  public static final int XT_ARRAY_STR = 34;
  public static final int XT_ARRAY_BOOL_UA = 35;
  public static final int XT_ARRAY_BOOL = 36;
  public static final int XT_ARRAY_BOOL_INT = 37;
  public static final int XT_UNKNOWN = 48;
  public static final int XT_NONE = -1;
  public static final int XT_FACTOR = 127;
  public static final int NILSXP = 0;
  public static final int SYMSXP = 1;
  public static final int LISTSXP = 2;
  public static final int CLOSXP = 3;
  public static final int ENVSXP = 4;
  public static final int PROMSXP = 5;
  public static final int LANGSXP = 6;
  public static final int SPECIALSXP = 7;
  public static final int BUILTINSXP = 8;
  public static final int CHARSXP = 9;
  public static final int LGLSXP = 10;
  public static final int INTSXP = 13;
  public static final int REALSXP = 14;
  public static final int CPLXSXP = 15;
  public static final int STRSXP = 16;
  public static final int DOTSXP = 17;
  public static final int ANYSXP = 18;
  public static final int VECSXP = 19;
  public static final int EXPRSXP = 20;
  public static final int BCODESXP = 21;
  public static final int EXTPTRSXP = 22;
  public static final int WEAKREFSXP = 23;
  public static final int RAWSXP = 24;
  public static final int S4SXP = 25;
  public static final int FUNSXP = 99;
  Rengine eng;
  public long xp;
  public int rtype;
  int Xt;
  REXP attr;
  Object cont;
  
  public REXP(Rengine paramRengine, long paramLong)
  {
    this(paramRengine, paramLong, true);
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      if ((this.Xt == -1) && (this.xp != 0L) && (this.eng != null)) {
        this.eng.rniRelease(this.xp);
      }
    }
    finally
    {
      super.finalize();
    }
  }
  
  public REXP(Rengine paramRengine, long paramLong, boolean paramBoolean)
  {
    this.eng = paramRengine;
    this.xp = paramLong;
    this.rtype = paramRengine.rniExpType(this.xp);
    if (!paramBoolean)
    {
      this.Xt = -1;
      if ((paramRengine != null) && (this.xp != 0L)) {
        paramRengine.rniPreserve(this.xp);
      }
      return;
    }
    if (this.rtype == 16)
    {
      String[] arrayOfString1 = paramRengine.rniGetStringArray(this.xp);
      if ((arrayOfString1 != null) && (arrayOfString1.length == 1))
      {
        this.cont = arrayOfString1[0];
        this.Xt = 3;
      }
      else
      {
        this.cont = arrayOfString1;
        this.Xt = 34;
      }
    }
    else if (this.rtype == 13)
    {
      this.cont = null;
      if (paramRengine.rniInherits(this.xp, "factor"))
      {
        long l1 = paramRengine.rniGetAttr(this.xp, "levels");
        if (l1 != 0L)
        {
          String[] arrayOfString2 = null;
          
          int j = paramRengine.rniExpType(l1);
          if (j == 16)
          {
            arrayOfString2 = paramRengine.rniGetStringArray(l1);
            int[] arrayOfInt = paramRengine.rniGetIntArray(this.xp);
            this.cont = new RFactor(arrayOfInt, arrayOfString2, 1);
            this.Xt = 127;
          }
        }
      }
      if (this.cont == null)
      {
        this.cont = paramRengine.rniGetIntArray(this.xp);
        this.Xt = 32;
      }
    }
    else if (this.rtype == 14)
    {
      this.cont = paramRengine.rniGetDoubleArray(this.xp);
      this.Xt = 33;
    }
    else if (this.rtype == 10)
    {
      this.cont = paramRengine.rniGetBoolArrayI(this.xp);
      this.Xt = 37;
    }
    else
    {
      long l3;
      if (this.rtype == 19)
      {
        long[] arrayOfLong = paramRengine.rniGetVector(this.xp);
        this.cont = new RVector();
        int i = 0;
        
        this.Xt = 16;
        while (i < arrayOfLong.length) {
          ((RVector)this.cont).addElement(new REXP(paramRengine, arrayOfLong[(i++)]));
        }
        l3 = paramRengine.rniGetAttr(this.xp, "names");
        if ((l3 != 0L) && (paramRengine.rniExpType(l3) == 16)) {
          ((RVector)this.cont).setNames(paramRengine.rniGetStringArray(l3));
        }
      }
      else if (this.rtype == 2)
      {
        long l2 = paramRengine.rniCAR(this.xp);
        l3 = paramRengine.rniCDR(this.xp);
        long l4 = paramRengine.rniTAG(this.xp);
        
        REXP localREXP = (l3 == 0L) || (paramRengine.rniExpType(l3) != 2) ? null : new REXP(paramRengine, paramRengine.rniCDR(this.xp));
        this.cont = new RList(new REXP(paramRengine, l2), l4 == 0L ? null : new REXP(paramRengine, l4), localREXP);
        this.Xt = 17;
      }
      else if (this.rtype == 1)
      {
        this.cont = paramRengine.rniGetSymbolName(this.xp);
        this.Xt = 5;
      }
      else
      {
        this.Xt = 0;
      }
    }
  }
  
  long cachedBinaryLength = -1L;
  
  public REXP()
  {
    this.Xt = 0;
    this.attr = null;
    this.cont = null;
  }
  
  public REXP(int paramInt, Object paramObject)
  {
    this.Xt = paramInt;
    this.cont = paramObject;
    this.attr = null;
  }
  
  public REXP(int paramInt, Object paramObject, REXP paramREXP)
  {
    this.Xt = paramInt;
    this.cont = paramObject;
    this.attr = paramREXP;
  }
  
  public REXP(double[] paramArrayOfDouble)
  {
    this(33, paramArrayOfDouble);
  }
  
  public REXP(int[] paramArrayOfInt)
  {
    this(32, paramArrayOfInt);
  }
  
  public REXP(String[] paramArrayOfString)
  {
    this(34, paramArrayOfString);
  }
  
  public REXP(boolean[] paramArrayOfBoolean)
  {
    this.Xt = 37;
    if (paramArrayOfBoolean == null)
    {
      this.cont = new int[0];
    }
    else
    {
      int[] arrayOfInt = new int[paramArrayOfBoolean.length];
      for (int i = 0; i < paramArrayOfBoolean.length; i++) {
        arrayOfInt[i] = (paramArrayOfBoolean[i] != true ? 1 : 0);
      }
      this.cont = arrayOfInt;
    }
    this.attr = null;
  }
  
  public REXP getAttributes()
  {
    return this.attr;
  }
  
  public REXP getAttribute(String paramString)
  {
    long l = this.eng.rniGetAttr(this.xp, paramString);
    if (l == 0L) {
      return null;
    }
    return new REXP(this.eng, l, this.Xt != -1);
  }
  
  public Object getContent()
  {
    return this.cont;
  }
  
  public int getType()
  {
    return this.Xt;
  }
  
  Rengine getEngine()
  {
    return this.eng;
  }
  
  public String asString()
  {
    if (this.cont == null) {
      return null;
    }
    if (this.Xt == 3) {
      return (String)this.cont;
    }
    if (this.Xt == 34)
    {
      String[] arrayOfString = (String[])this.cont;
      return arrayOfString.length > 0 ? arrayOfString[0] : null;
    }
    return null;
  }
  
  public String asSymbolName()
  {
    return this.Xt == 5 ? (String)this.cont : null;
  }
  
  public String[] asStringArray()
  {
    if (this.cont == null) {
      return null;
    }
    if (this.Xt == 3)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = ((String)this.cont);
      return arrayOfString;
    }
    if (this.Xt == 34) {
      return (String[])this.cont;
    }
    return null;
  }
  
  public int asInt()
  {
    if (this.Xt == 32)
    {
      int[] arrayOfInt = (int[])this.cont;
      if ((arrayOfInt != null) && (arrayOfInt.length > 0)) {
        return arrayOfInt[0];
      }
    }
    return this.Xt == 1 ? ((Integer)this.cont).intValue() : 0;
  }
  
  public double asDouble()
  {
    if (this.Xt == 33)
    {
      double[] arrayOfDouble = (double[])this.cont;
      if ((arrayOfDouble != null) && (arrayOfDouble.length > 0)) {
        return arrayOfDouble[0];
      }
    }
    return this.Xt == 2 ? ((Double)this.cont).doubleValue() : 0.0D;
  }
  
  public RVector asVector()
  {
    return this.Xt == 16 ? (RVector)this.cont : null;
  }
  
  public RFactor asFactor()
  {
    return this.Xt == 127 ? (RFactor)this.cont : null;
  }
  
  public RList asList()
  {
    return this.Xt == 16 ? new RList((RVector)this.cont) : this.Xt == 17 ? (RList)this.cont : null;
  }
  
  public RBool asBool()
  {
    if (this.Xt == 37)
    {
      int[] arrayOfInt = (int[])this.cont;
      return (arrayOfInt != null) && (arrayOfInt.length > 0) ? new RBool(arrayOfInt[0]) : null;
    }
    return this.Xt == 6 ? (RBool)this.cont : null;
  }
  
  public double[] asDoubleArray()
  {
    if (this.Xt == 33) {
      return (double[])this.cont;
    }
    double[] localObject;
    if (this.Xt == 2)
    {
      localObject = new double[1];
      localObject[0] = asDouble();
      return localObject;
    }
    if (this.Xt == 1)
    {
      localObject = new double[1];
      localObject[0] = ((Integer)this.cont).doubleValue();
      return localObject;
    }
    
    int[] localObject1;
    if (this.Xt == 32)
    {
      localObject1 = asIntArray();
      if (localObject1 == null) {
        return null;
      }
      double[] arrayOfDouble = new double[localObject1.length];
      int i = 0;
      while (i < localObject1.length)
      {
        arrayOfDouble[i] = localObject1[i];
        i++;
      }
      return arrayOfDouble;
    }
    return null;
  }
  
  public int[] asIntArray()
  {
    if ((this.Xt == 32) || (this.Xt == 37)) {
      return (int[])this.cont;
    }
    if (this.Xt == 1)
    {
      int[] arrayOfInt = new int[1];
      arrayOfInt[0] = asInt();
      return arrayOfInt;
    }
    return null;
  }
  
  public double[][] asDoubleMatrix()
  {
    double[] arrayOfDouble = asDoubleArray();
    if (arrayOfDouble == null) {
      return (double[][])null;
    }
    REXP localREXP = getAttribute("dim");
    if ((localREXP == null) || (localREXP.Xt != 32)) {
      return (double[][])null;
    }
    int[] arrayOfInt = localREXP.asIntArray();
    if ((arrayOfInt == null) || (arrayOfInt.length != 2)) {
      return (double[][])null;
    }
    int i = arrayOfInt[0];int j = arrayOfInt[1];
    double[][] arrayOfDouble1 = new double[i][j];
    if (arrayOfDouble == null) {
      return (double[][])null;
    }
    int k = 0;int m = 0;
    while (k < j)
    {
      int n = 0;
      while (n < i) {
        arrayOfDouble1[(n++)][k] = arrayOfDouble[(m++)];
      }
      k++;
    }
    return arrayOfDouble1;
  }
  
  public double[][] asMatrix()
  {
    return asDoubleMatrix();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("[" + xtName(this.Xt) + " ");
    if (this.attr != null) {
      localStringBuffer.append("\nattr=" + this.attr + "\n ");
    }
    if (this.Xt == 2) {
      localStringBuffer.append((Double)this.cont);
    }
    if (this.Xt == 1) {
      localStringBuffer.append((Integer)this.cont);
    }
    if (this.Xt == 6) {
      localStringBuffer.append((RBool)this.cont);
    }
    if (this.Xt == 127) {
      localStringBuffer.append((RFactor)this.cont);
    }
    
    double [] localObject;
    int i;
    if (this.Xt == 33)
    {
      localObject = (double[])this.cont;
      localStringBuffer.append("(");
      for (i = 0; i < localObject.length; i++)
      {
        localStringBuffer.append(localObject[i]);
        if (i < localObject.length - 1) {
          localStringBuffer.append(", ");
        }
        if (i == 99)
        {
          localStringBuffer.append("... (" + (localObject.length - 100) + " more values follow)");
          
          break;
        }
      }
      localStringBuffer.append(")");
    }
    if (this.Xt == 32)
    {
      int [] localObject1 = (int[])this.cont;
      localStringBuffer.append("(");
      for (i = 0; i < localObject1.length; i++)
      {
        localStringBuffer.append(localObject1[i]);
        if (i < localObject1.length - 1) {
          localStringBuffer.append(", ");
        }
        if (i == 99)
        {
          localStringBuffer.append("... (" + (localObject1.length - 100) + " more values follow)");
          
          break;
        }
      }
      localStringBuffer.append(")");
    }
    if (this.Xt == 36)
    {
      RBool[] localObject2 = (RBool[])this.cont;
      localStringBuffer.append("(");
      for (i = 0; i < localObject2.length; i++)
      {
        localStringBuffer.append(localObject2[i]);
        if (i < localObject2.length - 1) {
          localStringBuffer.append(", ");
        }
      }
      localStringBuffer.append(")");
    }
    if (this.Xt == 34)
    {
    	String[] localObject3 = (String[])this.cont;
      localStringBuffer.append("(");
      for (i = 0; i < localObject3.length; i++)
      {
        localStringBuffer.append("\"" + localObject3[i] + "\"");
        if (i < localObject3.length - 1) {
          localStringBuffer.append(", ");
        }
        if ((i == 10) && (localObject3.length > 14))
        {
          localStringBuffer.append("... (" + (localObject3.length - 10) + " more values follow)");
          break;
        }
      }
      localStringBuffer.append(")");
    }
    if (this.Xt == 16)
    {
      Vector localObject4 = (Vector)this.cont;
      localStringBuffer.append("(");
      for (i = 0; i < ((Vector)localObject4).size(); i++)
      {
        localStringBuffer.append(((REXP)((Vector)localObject4).elementAt(i)).toString());
        if (i < ((Vector)localObject4).size() - 1) {
          localStringBuffer.append(", ");
        }
      }
      localStringBuffer.append(")");
    }
    if (this.Xt == 3) {
      if (this.cont == null)
      {
        localStringBuffer.append("NA");
      }
      else
      {
        localStringBuffer.append("\"");
        localStringBuffer.append((String)this.cont);
        localStringBuffer.append("\"");
      }
    }
    if (this.Xt == 5) {
      localStringBuffer.append((String)this.cont);
    }
    if ((this.Xt == 17) || (this.Xt == 4))
    {
      RList localObject5 = (RList)this.cont;
      localStringBuffer.append(((RList)localObject5).head);
      localStringBuffer.append(":");
      localStringBuffer.append(((RList)localObject5).tag);
      localStringBuffer.append(",(");
      localStringBuffer.append(((RList)localObject5).body);
      localStringBuffer.append(")");
    }
    if (this.Xt == -1) {
      localStringBuffer.append("{" + this.rtype + "}");
    }
    if (this.Xt == 48) {
      localStringBuffer.append((Integer)this.cont);
    }
    localStringBuffer.append("]");
    return localStringBuffer.toString();
  }
  
  public static String quoteString(String paramString)
  {
    if (paramString.indexOf('\\') >= 0) {
      paramString.replaceAll("\\", "\\\\");
    }
    if (paramString.indexOf('"') >= 0) {
      paramString.replaceAll("\"", "\\\"");
    }
    return "\"" + paramString + "\"";
  }
  
  public static String xtName(int paramInt)
  {
    if (paramInt == 0) {
      return "NULL";
    }
    if (paramInt == 1) {
      return "INT";
    }
    if (paramInt == 3) {
      return "STRING";
    }
    if (paramInt == 2) {
      return "REAL";
    }
    if (paramInt == 6) {
      return "BOOL";
    }
    if (paramInt == 32) {
      return "INT*";
    }
    if (paramInt == 34) {
      return "STRING*";
    }
    if (paramInt == 33) {
      return "REAL*";
    }
    if (paramInt == 36) {
      return "BOOL*";
    }
    if (paramInt == 37) {
      return "BOOLi*";
    }
    if (paramInt == 5) {
      return "SYMBOL";
    }
    if (paramInt == 4) {
      return "LANG";
    }
    if (paramInt == 17) {
      return "LIST";
    }
    if (paramInt == 18) {
      return "CLOS";
    }
    if (paramInt == 16) {
      return "VECTOR";
    }
    if (paramInt == 127) {
      return "FACTOR";
    }
    if (paramInt == 48) {
      return "UNKNOWN";
    }
    if (paramInt == -1) {
      return "(SEXP)";
    }
    return "<unknown " + paramInt + ">";
  }
}
