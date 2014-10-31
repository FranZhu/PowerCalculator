package org.rosuda.JRI;

public abstract interface RMainLoopCallbacks
{
  public abstract void rWriteConsole(Rengine paramRengine, String paramString, int paramInt);
  
  public abstract void rBusy(Rengine paramRengine, int paramInt);
  
  public abstract String rReadConsole(Rengine paramRengine, String paramString, int paramInt);
  
  public abstract void rShowMessage(Rengine paramRengine, String paramString);
  
  public abstract String rChooseFile(Rengine paramRengine, int paramInt);
  
  public abstract void rFlushConsole(Rengine paramRengine);
  
  public abstract void rSaveHistory(Rengine paramRengine, String paramString);
  
  public abstract void rLoadHistory(Rengine paramRengine, String paramString);
}
