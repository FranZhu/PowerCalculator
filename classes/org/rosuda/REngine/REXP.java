package org.rosuda.REngine;

public class REXP
{
  protected REXPList attr;
  
  public REXP() {}
  
  public REXP(REXPList paramREXPList)
  {
    this.attr = paramREXPList;
  }
  
  public boolean isString()
  {
    return false;
  }
  
  public boolean isNumeric()
  {
    return false;
  }
  
  public boolean isInteger()
  {
    return false;
  }
  
  public boolean isNull()
  {
    return false;
  }
  
  public boolean isFactor()
  {
    return false;
  }
  
  public boolean isList()
  {
    return false;
  }
  
  public boolean isPairList()
  {
    return false;
  }
  
  public boolean isLogical()
  {
    return false;
  }
  
  public boolean isEnvironment()
  {
    return false;
  }
  
  public boolean isLanguage()
  {
    return false;
  }
  
  public boolean isExpression()
  {
    return false;
  }
  
  public boolean isSymbol()
  {
    return false;
  }
  
  public boolean isVector()
  {
    return false;
  }
  
  public boolean isRaw()
  {
    return false;
  }
  
  public boolean isComplex()
  {
    return false;
  }
  
  public boolean isRecursive()
  {
    return false;
  }
  
  public boolean isReference()
  {
    return false;
  }
  
  public String[] asStrings()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "String");
  }
  
  public int[] asIntegers()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "int");
  }
  
  public double[] asDoubles()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "double");
  }
  
  public byte[] asBytes()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "byte");
  }
  
  public RList asList()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "list");
  }
  
  public RFactor asFactor()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "factor");
  }
  
  public Object asNativeJavaObject()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "native Java Object");
  }
  
  public int length()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "vector");
  }
  
  public boolean[] isNA()
    throws REXPMismatchException
  {
    throw new REXPMismatchException(this, "vector");
  }
  
  public int asInteger()
    throws REXPMismatchException
  {
    int[] arrayOfInt = asIntegers();return arrayOfInt[0];
  }
  
  public double asDouble()
    throws REXPMismatchException
  {
    double[] arrayOfDouble = asDoubles();return arrayOfDouble[0];
  }
  
  public String asString()
    throws REXPMismatchException
  {
    String[] arrayOfString = asStrings();return arrayOfString[0];
  }
  
  public REXP getAttribute(String paramString)
  {
    REXPList localREXPList = _attr();
    if ((localREXPList == null) || (!localREXPList.isList())) {
      return null;
    }
    return localREXPList.asList().at(paramString);
  }
  
  public boolean hasAttribute(String paramString)
  {
    REXPList localREXPList = _attr();
    return (localREXPList != null) && (localREXPList.isList()) && (localREXPList.asList().at(paramString) != null);
  }
  
  public int[] dim()
  {
    try
    {
      return hasAttribute("dim") ? _attr().asList().at("dim").asIntegers() : null;
    }
    catch (REXPMismatchException localREXPMismatchException) {}
    return null;
  }
  
  public boolean inherits(String paramString)
  {
    if (!hasAttribute("class")) {
      return false;
    }
    try
    {
      String[] arrayOfString = getAttribute("class").asStrings();
      if (arrayOfString != null)
      {
        int i = 0;
        while (i < arrayOfString.length)
        {
          if ((arrayOfString[i] != null) && (arrayOfString[i].equals(paramString))) {
            return true;
          }
          i++;
        }
      }
    }
    catch (REXPMismatchException localREXPMismatchException) {}
    return false;
  }
  
  public REXPList _attr()
  {
    return this.attr;
  }
  
  public String toString()
  {
    return super.toString() + (this.attr != null ? "+" : "");
  }
  
  public String toDebugString()
  {
    return this.attr != null ? "<" + this.attr.toDebugString() + ">" + super.toString() : super.toString();
  }
  
  public double[][] asDoubleMatrix()
    throws REXPMismatchException
  {
    double[] arrayOfDouble = asDoubles();
    REXP localREXP = getAttribute("dim");
    if (localREXP == null) {
      throw new REXPMismatchException(this, "matrix (dim attribute missing)");
    }
    int[] arrayOfInt = localREXP.asIntegers();
    if (arrayOfInt.length != 2) {
      throw new REXPMismatchException(this, "matrix (wrong dimensionality)");
    }
    int i = arrayOfInt[0];int j = arrayOfInt[1];
    
    double[][] arrayOfDouble1 = new double[i][j];
    

    int k = 0;
    for (int m = 0; m < j; m++) {
      for (int n = 0; n < i; n++) {
        arrayOfDouble1[n][m] = arrayOfDouble[(k++)];
      }
    }
    return arrayOfDouble1;
  }
  
  public static REXP createDoubleMatrix(double[][] paramArrayOfDouble)
  {
    int i = 0;int j = 0;
    double[] arrayOfDouble;
    if ((paramArrayOfDouble != null) && (paramArrayOfDouble.length != 0) && (paramArrayOfDouble[0].length != 0))
    {
      i = paramArrayOfDouble.length;
      j = paramArrayOfDouble[0].length;
      arrayOfDouble = new double[i * j];
      int k = 0;
      for (int m = 0; m < j; m++) {
        for (int n = 0; n < i; n++) {
          arrayOfDouble[(k++)] = paramArrayOfDouble[n][m];
        }
      }
    }
    else
    {
      arrayOfDouble = new double[0];
    }
    return new REXPDouble(arrayOfDouble, new REXPList(new RList(new REXP[] { new REXPInteger(new int[] { i, j }) }, new String[] { "dim" })));
  }
  
  public static REXP createDataFrame(RList paramRList)
    throws REXPMismatchException
  {
    if ((paramRList == null) || (paramRList.size() < 1)) {
      throw new REXPMismatchException(new REXPList(paramRList), "data frame (must have dim>0)");
    }
    if (!(paramRList.at(0) instanceof REXPVector)) {
      throw new REXPMismatchException(new REXPList(paramRList), "data frame (contents must be vectors)");
    }
    REXPVector localREXPVector = (REXPVector)paramRList.at(0);
    return new REXPGenericVector(paramRList, new REXPList(new RList(new REXP[] { new REXPString("data.frame"), new REXPString(paramRList.keys()), new REXPInteger(new int[] { -2147483648, -localREXPVector.length() }) }, new String[] { "class", "names", "row.names" })));
  }
  
  public static int maxDebugItems = 32;
}
