package org.rosuda.REngine;

public abstract interface REngineConsoleHistoryInterface
{
  public abstract void RSaveHistory(REngine paramREngine, String paramString);
  
  public abstract void RLoadHistory(REngine paramREngine, String paramString);
}
