package org.rosuda.REngine;

import java.io.PrintStream;

public class REngineStdOutput
  implements REngineCallbacks, REngineOutputInterface
{
  public synchronized void RWriteConsole(REngine paramREngine, String paramString, int paramInt)
  {
    (paramInt == 0 ? System.out : System.err).print(paramString);
  }
  
  public void RShowMessage(REngine paramREngine, String paramString)
  {
    System.err.println("*** " + paramString);
  }
  
  public void RFlushConsole(REngine paramREngine) {}
}
