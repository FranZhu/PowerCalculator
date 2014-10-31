package org.rosuda.REngine;

public abstract interface REngineUIInterface
{
  public abstract void RBusyState(REngine paramREngine, int paramInt);
  
  public abstract String RChooseFile(REngine paramREngine, boolean paramBoolean);
}
