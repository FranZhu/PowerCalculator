package org.rosuda.REngine;

public class REXPMismatchException
  extends Exception
{
  REXP sender;
  String access;
  
  public REXPMismatchException(REXP paramREXP, String paramString)
  {
    super("attempt to access " + paramREXP.getClass().getName() + " as " + paramString);
    this.sender = paramREXP;
    this.access = paramString;
  }
  
  public REXP getSender()
  {
    return this.sender;
  }
  
  public String getAccess()
  {
    return this.access;
  }
}
