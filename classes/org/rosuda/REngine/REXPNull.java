package org.rosuda.REngine;

public class REXPNull
  extends REXP
{
  public REXPNull() {}
  
  public REXPNull(REXPList paramREXPList)
  {
    super(paramREXPList);
  }
  
  public boolean isNull()
  {
    return true;
  }
  
  public boolean isList()
  {
    return true;
  }
  
  public RList asList()
  {
    return new RList();
  }
  
  public Object asNativeJavaObject()
  {
    return null;
  }
}
