package org.rosuda.REngine;

public class REXPJavaReference
  extends REXP
{
  Object object;
  
  public REXPJavaReference(Object paramObject)
  {
    this.object = paramObject;
  }
  
  public REXPJavaReference(Object paramObject, REXPList paramREXPList)
  {
    super(paramREXPList);this.object = paramObject;
  }
  
  public Object getObject()
  {
    return this.object;
  }
  
  public Object asNativeJavaObject()
  {
    return this.object;
  }
  
  public String toString()
  {
    return super.toString() + "[" + this.object + "]";
  }
}
