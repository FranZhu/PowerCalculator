package org.rosuda.REngine;

public class REXPExpressionVector
  extends REXPGenericVector
{
  public REXPExpressionVector(RList paramRList)
  {
    super(paramRList);
  }
  
  public REXPExpressionVector(RList paramRList, REXPList paramREXPList)
  {
    super(paramRList, paramREXPList);
  }
  
  public boolean isExpression()
  {
    return true;
  }
}
