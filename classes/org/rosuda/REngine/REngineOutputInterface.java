package org.rosuda.REngine;

public abstract interface REngineOutputInterface
{
  public abstract void RWriteConsole(REngine paramREngine, String paramString, int paramInt);
  
  public abstract void RShowMessage(REngine paramREngine, String paramString);
  
  public abstract void RFlushConsole(REngine paramREngine);
}
