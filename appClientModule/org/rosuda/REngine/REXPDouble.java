package org.rosuda.REngine;

public class REXPDouble
  extends REXPVector
{
  private double[] payload;
  public static final double NA = Double.longBitsToDouble(9218868437227407266L);
  static final long NA_bits = Double.doubleToRawLongBits(Double.longBitsToDouble(9218868437227407266L));
  
  public static boolean isNA(double paramDouble)
  {
    return (Double.doubleToRawLongBits(paramDouble) & 0xFFFFFFFF) == (NA_bits & 0xFFFFFFFF);
  }
  
  public REXPDouble(double paramDouble)
  {
    this.payload = new double[] { paramDouble };
  }
  
  public REXPDouble(double[] paramArrayOfDouble)
  {
    this.payload = (paramArrayOfDouble == null ? new double[0] : paramArrayOfDouble);
  }
  
  public REXPDouble(double[] paramArrayOfDouble, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramArrayOfDouble == null ? new double[0] : paramArrayOfDouble);
  }
  
  public int length()
  {
    return this.payload.length;
  }
  
  public Object asNativeJavaObject()
  {
    return this.payload;
  }
  
  public boolean isNumeric()
  {
    return true;
  }
  
  public double[] asDoubles()
  {
    return this.payload;
  }
  
  public int[] asIntegers()
  {
    int[] arrayOfInt = new int[this.payload.length];
    for (int i = 0; i < this.payload.length; i++) {
      arrayOfInt[i] = ((int)this.payload[i]);
    }
    return arrayOfInt;
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
      arrayOfBoolean[i] = isNA(this.payload[i]);
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
