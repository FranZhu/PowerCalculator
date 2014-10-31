package org.rosuda.REngine;

public class REXPInteger
  extends REXPVector
{
  protected int[] payload;
  public static final int NA = -2147483648;
  
  public static boolean isNA(int paramInt)
  {
    return paramInt == -2147483648;
  }
  
  public REXPInteger(int paramInt)
  {
    this.payload = new int[] { paramInt };
  }
  
  public REXPInteger(int[] paramArrayOfInt)
  {
    this.payload = (paramArrayOfInt == null ? new int[0] : paramArrayOfInt);
  }
  
  public REXPInteger(int[] paramArrayOfInt, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramArrayOfInt == null ? new int[0] : paramArrayOfInt);
  }
  
  public Object asNativeJavaObject()
  {
    return this.payload;
  }
  
  public int length()
  {
    return this.payload.length;
  }
  
  public boolean isInteger()
  {
    return true;
  }
  
  public boolean isNumeric()
  {
    return true;
  }
  
  public int[] asIntegers()
  {
    return this.payload;
  }
  
  public double[] asDoubles()
  {
    double[] arrayOfDouble = new double[this.payload.length];
    for (int i = 0; i < this.payload.length; i++) {
      arrayOfDouble[i] = this.payload[i];
    }
    return arrayOfDouble;
  }
  
  public String[] asStrings()
  {
    String[] arrayOfString = new String[this.payload.length];
    for (int i = 0; i < this.payload.length; i++) {
      arrayOfString[i] = ("" + this.payload[i]);
    }
    return arrayOfString;
  }
  
  public boolean[] isNA()
  {
    boolean[] arrayOfBoolean = new boolean[this.payload.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = (this.payload[i] == -2147483648 ? true : false);
    }
    return arrayOfBoolean;
  }
  
  public String toDebugString()
  {
    StringBuffer localStringBuffer = new StringBuffer(super.toDebugString() + "{");
    int i = 0;
    while ((i < this.payload.length) && (i < maxDebugItems))
    {
      if (i > 0) {
        localStringBuffer.append(",");
      }
      localStringBuffer.append(this.payload[i]);
      i++;
    }
    if (i < this.payload.length) {
      localStringBuffer.append(",..");
    }
    return localStringBuffer.toString() + "}";
  }
}
