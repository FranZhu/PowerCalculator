package org.rosuda.REngine;

public class REngineEvalException
  extends REngineException
{
  public static final int INVALID_INPUT = -1;
  public static final int ERROR = -2;
  protected int type;
  
  public REngineEvalException(REngine paramREngine, String paramString, int paramInt)
  {
    super(paramREngine, paramString);
    this.type = paramInt;
  }
  
  public REngineEvalException(REngine paramREngine, String paramString)
  {
    this(paramREngine, paramString, -2);
  }
  
  public int getType()
  {
    return this.type;
  }
}
