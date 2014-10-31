package org.rosuda.REngine;

public abstract class REXPVector
  extends REXP
{
  public REXPVector() {}
  
  public REXPVector(REXPList paramREXPList)
  {
    super(paramREXPList);
  }
  
  public abstract int length();
  
  public boolean isVector()
  {
    return true;
  }
  
  public boolean[] isNA()
  {
    boolean[] arrayOfBoolean = new boolean[length()];
    return arrayOfBoolean;
  }
  
  public String toString()
  {
    return super.toString() + "[" + length() + "]";
  }
  
  public String toDebugString()
  {
    return super.toDebugString() + "[" + length() + "]";
  }
}
