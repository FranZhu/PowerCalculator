package org.rosuda.REngine;

public class REXPList
  extends REXPVector
{
  private RList payload;
  
  public REXPList(RList paramRList)
  {
    this.payload = (paramRList == null ? new RList() : paramRList);
  }
  
  public REXPList(RList paramRList, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramRList == null ? new RList() : paramRList);
  }
  
  public REXPList(REXP paramREXP, String paramString)
  {
    this.payload = new RList(new REXP[] { paramREXP }, new String[] { paramString });
  }
  
  public Object asNativeJavaObject()
    throws REXPMismatchException
  {
    REXPGenericVector localREXPGenericVector = new REXPGenericVector(this.payload);
    return localREXPGenericVector.asNativeJavaObject();
  }
  
  public int length()
  {
    return this.payload.size();
  }
  
  public boolean isList()
  {
    return true;
  }
  
  public boolean isPairList()
  {
    return true;
  }
  
  public boolean isRecursive()
  {
    return true;
  }
  
  public RList asList()
  {
    return this.payload;
  }
  
  public String toString()
  {
    return super.toString() + (asList().isNamed() ? "named" : "");
  }
  
  public String toDebugString()
  {
    StringBuffer localStringBuffer = new StringBuffer(super.toDebugString() + "{");
    int i = 0;
    while ((i < this.payload.size()) && (i < maxDebugItems))
    {
      if (i > 0) {
        localStringBuffer.append(",\n");
      }
      String str = this.payload.keyAt(i);
      if (str != null) {
        localStringBuffer.append(str + "=");
      }
      localStringBuffer.append(this.payload.at(i).toDebugString());
      i++;
    }
    if (i < this.payload.size()) {
      localStringBuffer.append(",..");
    }
    return localStringBuffer.toString() + "}";
  }
}
