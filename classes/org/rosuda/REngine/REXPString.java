package org.rosuda.REngine;

public class REXPString
  extends REXPVector
{
  private String[] payload;
  
  public REXPString(String paramString)
  {
    this.payload = new String[] { paramString };
  }
  
  public REXPString(String[] paramArrayOfString)
  {
    this.payload = (paramArrayOfString == null ? new String[0] : paramArrayOfString);
  }
  
  public REXPString(String[] paramArrayOfString, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramArrayOfString == null ? new String[0] : paramArrayOfString);
  }
  
  public int length()
  {
    return this.payload.length;
  }
  
  public boolean isString()
  {
    return true;
  }
  
  public Object asNativeJavaObject()
  {
    return this.payload;
  }
  
  public String[] asStrings()
  {
    return this.payload;
  }
  
  public boolean[] isNA()
  {
    boolean[] arrayOfBoolean = new boolean[this.payload.length];
    for (int i = 0; i < arrayOfBoolean.length; i++) {
      arrayOfBoolean[i] = (this.payload[i] == null ? true : false);
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
      localStringBuffer.append("\"" + this.payload[i] + "\"");
      i++;
    }
    if (i < this.payload.length) {
      localStringBuffer.append(",..");
    }
    return localStringBuffer.toString() + "}";
  }
}
