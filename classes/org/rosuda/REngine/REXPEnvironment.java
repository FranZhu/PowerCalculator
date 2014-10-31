package org.rosuda.REngine;

public class REXPEnvironment
  extends REXP
{
  REngine eng;
  Object handle;
  
  public REXPEnvironment(REngine paramREngine, Object paramObject)
  {
    this.eng = paramREngine;
    this.handle = paramObject;
  }
  
  public boolean isEnvironment()
  {
    return true;
  }
  
  public Object getHandle()
  {
    return this.handle;
  }
  
  public REXP get(String paramString, boolean paramBoolean)
    throws REngineException
  {
    try
    {
      return this.eng.get(paramString, this, paramBoolean);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this.eng, "REXPMismatchException:" + localREXPMismatchException + " in get()");
    }
  }
  
  public REXP get(String paramString)
    throws REngineException
  {
    return get(paramString, true);
  }
  
  public void assign(String paramString, REXP paramREXP)
    throws REngineException, REXPMismatchException
  {
    this.eng.assign(paramString, paramREXP, this);
  }
  
  public REXP parent(boolean paramBoolean)
    throws REngineException
  {
    try
    {
      return this.eng.getParentEnvironment(this, paramBoolean);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this.eng, "REXPMismatchException:" + localREXPMismatchException + " in parent()");
    }
  }
  
  public REXPEnvironment parent()
    throws REngineException
  {
    return (REXPEnvironment)parent(true);
  }
}
