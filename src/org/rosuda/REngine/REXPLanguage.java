package org.rosuda.REngine;

public class REXPLanguage
  extends REXPList
{
  public REXPLanguage(RList paramRList)
  {
    super(paramRList);
  }
  
  public REXPLanguage(RList paramRList, REXPList paramREXPList)
  {
    super(paramRList, paramREXPList);
  }
  
  public boolean isLanguage()
  {
    return true;
  }
}
