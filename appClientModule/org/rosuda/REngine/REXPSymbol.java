package org.rosuda.REngine;

public class REXPSymbol
  extends REXP
{
  private String name;
  
  public REXPSymbol(String paramString)
  {
    this.name = (paramString == null ? "" : paramString);
  }
  
  public boolean isSymbol()
  {
    return true;
  }
  
  public String asString()
  {
    return this.name;
  }
  
  public String[] asStrings()
  {
    return new String[] { this.name };
  }
  
  public String toString()
  {
    return getClass().getName() + "[" + this.name + "]";
  }
  
  public String toDebugString()
  {
    return super.toDebugString() + "[" + this.name + "]";
  }
  
  public Object asNativeJavaObject()
  {
    return this.name;
  }
}
