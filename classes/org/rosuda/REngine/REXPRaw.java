package org.rosuda.REngine;

public class REXPRaw
  extends REXPVector
{
  private byte[] payload;
  
  public REXPRaw(byte[] paramArrayOfByte)
  {
    this.payload = (paramArrayOfByte == null ? new byte[0] : paramArrayOfByte);
  }
  
  public REXPRaw(byte[] paramArrayOfByte, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramArrayOfByte == null ? new byte[0] : paramArrayOfByte);
  }
  
  public int length()
  {
    return this.payload.length;
  }
  
  public boolean isRaw()
  {
    return true;
  }
  
  public byte[] asBytes()
  {
    return this.payload;
  }
  
  public Object asNativeJavaObject()
  {
    return this.payload;
  }
}
