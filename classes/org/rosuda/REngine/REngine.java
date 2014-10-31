package org.rosuda.REngine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class REngine
{
  protected static REngine lastEngine = null;
  
  public static REngine engineForClass(String paramString)
    throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    Class localClass = Class.forName(paramString);
    if (localClass == null) {
      throw new ClassNotFoundException("can't find engine class " + paramString);
    }
    Method localMethod = localClass.getMethod("createEngine", (Class[])null);
    Object localObject = localMethod.invoke(null, (Object[])null);
    return REngine.lastEngine = (REngine)localObject;
  }
  
  public static REngine engineForClass(String paramString, String[] paramArrayOfString, REngineCallbacks paramREngineCallbacks, boolean paramBoolean)
    throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
  {
    Class localClass = Class.forName(paramString);
    if (localClass == null) {
      throw new ClassNotFoundException("can't find engine class " + paramString);
    }
    Method localMethod = localClass.getMethod("createEngine", new Class[] { new String[0].getClass(), REngineCallbacks.class, Boolean.TYPE });
    Object localObject = localMethod.invoke(null, new Object[] { paramArrayOfString, paramREngineCallbacks, new Boolean(paramBoolean) });
    return REngine.lastEngine = (REngine)localObject;
  }
  
  public static REngine getLastEngine()
  {
    return lastEngine;
  }
  
  public abstract REXP parse(String paramString, boolean paramBoolean)
    throws REngineException;
  
  public abstract REXP eval(REXP paramREXP1, REXP paramREXP2, boolean paramBoolean)
    throws REngineException, REXPMismatchException;
  
  public abstract void assign(String paramString, REXP paramREXP1, REXP paramREXP2)
    throws REngineException, REXPMismatchException;
  
  public abstract REXP get(String paramString, REXP paramREXP, boolean paramBoolean)
    throws REngineException, REXPMismatchException;
  
  public abstract REXP resolveReference(REXP paramREXP)
    throws REngineException, REXPMismatchException;
  
  public abstract REXP createReference(REXP paramREXP)
    throws REngineException, REXPMismatchException;
  
  public abstract void finalizeReference(REXP paramREXP)
    throws REngineException, REXPMismatchException;
  
  public abstract REXP getParentEnvironment(REXP paramREXP, boolean paramBoolean)
    throws REngineException, REXPMismatchException;
  
  public abstract REXP newEnvironment(REXP paramREXP, boolean paramBoolean)
    throws REngineException, REXPMismatchException;
  
  public REXP parseAndEval(String paramString, REXP paramREXP, boolean paramBoolean)
    throws REngineException, REXPMismatchException
  {
    REXP localREXP = parse(paramString, false);
    return eval(localREXP, paramREXP, paramBoolean);
  }
  
  public REXP parseAndEval(String paramString)
    throws REngineException, REXPMismatchException
  {
    return parseAndEval(paramString, null, true);
  }
  
  public boolean close()
  {
    return false;
  }
  
  public boolean supportsReferences()
  {
    return false;
  }
  
  public boolean supportsEnvironments()
  {
    return false;
  }
  
  public boolean supportsREPL()
  {
    return false;
  }
  
  public boolean supportsLocking()
  {
    return false;
  }
  
  public void assign(String paramString, double[] paramArrayOfDouble)
    throws REngineException
  {
    try
    {
      assign(paramString, new REXPDouble(paramArrayOfDouble), null);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this, "REXPMismatchException in assign(,double[]): " + localREXPMismatchException);
    }
  }
  
  public void assign(String paramString, int[] paramArrayOfInt)
    throws REngineException
  {
    try
    {
      assign(paramString, new REXPInteger(paramArrayOfInt), null);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this, "REXPMismatchException in assign(,int[]): " + localREXPMismatchException);
    }
  }
  
  public void assign(String paramString, String[] paramArrayOfString)
    throws REngineException
  {
    try
    {
      assign(paramString, new REXPString(paramArrayOfString), null);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this, "REXPMismatchException in assign(,String[]): " + localREXPMismatchException);
    }
  }
  
  public void assign(String paramString, byte[] paramArrayOfByte)
    throws REngineException
  {
    try
    {
      assign(paramString, new REXPRaw(paramArrayOfByte), null);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this, "REXPMismatchException in assign(,byte[]): " + localREXPMismatchException);
    }
  }
  
  public void assign(String paramString1, String paramString2)
    throws REngineException
  {
    try
    {
      assign(paramString1, new REXPString(paramString2), null);
    }
    catch (REXPMismatchException localREXPMismatchException)
    {
      throw new REngineException(this, "REXPMismatchException in assign(,String[]): " + localREXPMismatchException);
    }
  }
  
  public void assign(String paramString, REXP paramREXP)
    throws REngineException, REXPMismatchException
  {
    assign(paramString, paramREXP, null);
  }
  
  public synchronized int tryLock()
  {
    return 0;
  }
  
  public synchronized int lock()
  {
    return 0;
  }
  
  public synchronized void unlock(int paramInt) {}
  
  public String toString()
  {
    return super.toString() + (lastEngine == this ? "{last}" : "");
  }
  
  public REXP wrap(Object paramObject)
  {
    return REXPWrapper.wrap(paramObject);
  }
}
