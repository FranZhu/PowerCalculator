package org.rosuda.REngine;

public class REngineException
  extends Exception
{
  protected REngine engine;
  
  public REngineException(REngine paramREngine, String paramString)
  {
    super(paramString);
    this.engine = paramREngine;
  }
  
  public REngine getEngine()
  {
    return this.engine;
  }
}
