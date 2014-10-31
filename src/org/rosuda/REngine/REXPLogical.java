package org.rosuda.REngine;

public class REXPLogical
  extends REXPVector
{
  protected byte[] payload;
  static final int NA_internal = -2147483648;
  public static final byte NA = -128;
  public static final byte TRUE = 1;
  public static final byte FALSE = 0;
  
  public static boolean isNA(byte paramByte)
  {
    return paramByte == -128;
  }
  
  public REXPLogical(boolean paramBoolean)
  {
    this.payload = new byte[] { (byte) (paramBoolean ? 1 : 0) };
  }
  
  public REXPLogical(byte paramByte)
  {
    this.payload = new byte[] { paramByte };
  }
  
  public REXPLogical(byte[] paramArrayOfByte)
  {
    this.payload = (paramArrayOfByte == null ? new byte[0] : paramArrayOfByte);
  }
  
  public REXPLogical(boolean[] paramArrayOfBoolean)
  {
    this.payload = new byte[paramArrayOfBoolean == null ? 0 : paramArrayOfBoolean.length];
    if (paramArrayOfBoolean != null) {
      for (int i = 0; i < paramArrayOfBoolean.length; i++) {
        this.payload[i] = (byte) (paramArrayOfBoolean[i] != true ? 1 : 0);
      }
    }
  }
  
  public REXPLogical(byte[] paramArrayOfByte, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramArrayOfByte == null ? new byte[0] : paramArrayOfByte);
  }
  
  public REXPLogical(boolean[] paramArrayOfBoolean, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = new byte[paramArrayOfBoolean == null ? 0 : paramArrayOfBoolean.length];
    if (paramArrayOfBoolean != null) {
      for (int i = 0; i < paramArrayOfBoolean.length; i++) {
        this.payload[i] = (byte) (paramArrayOfBoolean[i] != false ? 1 : 0);
      }
    }
  }
  
  public int length()
  {
    return this.payload.length;
  }
  
  public boolean isLogical()
  {
    return true;
  }
  
  public Object asNativeJavaObject()
  {
    return this.payload;
  }
  
  public int[] asIntegers()
  {
    int[] arrayOfInt = new int[this.payload.length];
    for (int i = 0; i < this.payload.length; i++) {
      arrayOfInt[i] = (this.payload[i] == 0 ? 0 : this.payload[i] == -128 ? -2147483648 : 1);
    }
    return arrayOfInt;
  }
  
  public byte[] asBytes()
  {
    return this.payload;
  }
  
  public double[] asDoubles()
  {
    double[] arrayOfDouble = new double[this.payload.length];
    for (int i = 0; i < this.payload.length; i++) {
      arrayOfDouble[i] = (this.payload[i] == 0 ? 0.0D : this.payload[i] == -128 ? REXPDouble.NA : 1.0D);
    }
    return arrayOfDouble;
  }
  
  public String[] asStrings()
  {
    String[] arrayOfString = new String[this.payload.length];
    for (int i = 0; i < this.payload.length; i++) {
      arrayOfString[i] = (this.payload[i] == 0 ? "FALSE" : this.payload[i] == -128 ? "NA" : "TRUE");
    }
    return arrayOfString;
  }
  
  public boolean[] isNA()
  {
    boolean[] arrayOfBoolean = new boolean[this.payload.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = (this.payload[i] == -128 ? true : false);
    }
    return arrayOfBoolean;
  }
  
  public boolean[] isTRUE()
  {
    boolean[] arrayOfBoolean = new boolean[this.payload.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = ((this.payload[i] != -128) && (this.payload[i] != 0) ? true : false);
    }
    return arrayOfBoolean;
  }
  
  public boolean[] isFALSE()
  {
    boolean[] arrayOfBoolean = new boolean[this.payload.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = (this.payload[i] == 0 ? true : false);
    }
    return arrayOfBoolean;
  }
  
  /**
   * @deprecated
   */
  public boolean[] isTrue()
  {
    return isTRUE();
  }
  
  /**
   * @deprecated
   */
  public boolean[] isFalse()
  {
    return isFALSE();
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
      localStringBuffer.append(this.payload[i] == 0 ? "FALSE" : this.payload[i] == -128 ? "NA" : "TRUE");
      i++;
    }
    if (i < this.payload.length) {
      localStringBuffer.append(",..");
    }
    return localStringBuffer.toString() + "}";
  }
}
