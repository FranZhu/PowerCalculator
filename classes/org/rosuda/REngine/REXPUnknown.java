package org.rosuda.REngine;

public class REXPUnknown
  extends REXP
{
  int type;
  
  public REXPUnknown(int paramInt)
  {
    this.type = paramInt;
  }
  
  public REXPUnknown(int paramInt, REXPList paramREXPList)
  {
    super(paramREXPList);this.type = paramInt;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public String toString()
  {
    return super.toString() + "[" + this.type + "]";
  }
}
