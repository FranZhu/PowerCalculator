package org.rosuda.REngine;

public class REXPReference
  extends REXP
{
  protected REngine eng;
  protected Object handle;
  protected REXP resolvedValue;
  
  public REXPReference(REngine paramREngine, Object paramObject)
  {
    this.eng = paramREngine;
    this.handle = paramObject;
  }
  
  REXPReference(REngine paramREngine, long paramLong)
  {
    this(paramREngine, new Long(paramLong));
  }
  
  public REXP resolve()
  {
    if (this.resolvedValue != null) {
      return this.resolvedValue;
    }
    try
    {
      this.resolvedValue = this.eng.resolveReference(this);
      return this.resolvedValue;
    }
    catch (REXPMismatchException localREXPMismatchException) {}catch (REngineException localREngineException) {}
    return null;
  }
  
  public void invalidate()
  {
    this.resolvedValue = null;
  }
  
  protected void finalize()
    throws Throwable
  {
    try
    {
      this.eng.finalizeReference(this);
    }
    finally
    {
      super.finalize();
    }
  }
  
  public boolean isString()
  {
    return resolve().isString();
  }
  
  public boolean isNumeric()
  {
    return resolve().isNumeric();
  }
  
  public boolean isInteger()
  {
    return resolve().isInteger();
  }
  
  public boolean isNull()
  {
    return resolve().isNull();
  }
  
  public boolean isFactor()
  {
    return resolve().isFactor();
  }
  
  public boolean isList()
  {
    return resolve().isList();
  }
  
  public boolean isLogical()
  {
    return resolve().isLogical();
  }
  
  public boolean isEnvironment()
  {
    return resolve().isEnvironment();
  }
  
  public boolean isLanguage()
  {
    return resolve().isLanguage();
  }
  
  public boolean isSymbol()
  {
    return resolve().isSymbol();
  }
  
  public boolean isVector()
  {
    return resolve().isVector();
  }
  
  public boolean isRaw()
  {
    return resolve().isRaw();
  }
  
  public boolean isComplex()
  {
    return resolve().isComplex();
  }
  
  public boolean isRecursive()
  {
    return resolve().isRecursive();
  }
  
  public boolean isReference()
  {
    return true;
  }
  
  public String[] asStrings()
    throws REXPMismatchException
  {
    return resolve().asStrings();
  }
  
  public int[] asIntegers()
    throws REXPMismatchException
  {
    return resolve().asIntegers();
  }
  
  public double[] asDoubles()
    throws REXPMismatchException
  {
    return resolve().asDoubles();
  }
  
  public RList asList()
    throws REXPMismatchException
  {
    return resolve().asList();
  }
  
  public RFactor asFactor()
    throws REXPMismatchException
  {
    return resolve().asFactor();
  }
  
  public int length()
    throws REXPMismatchException
  {
    return resolve().length();
  }
  
  public REXPList _attr()
  {
    return resolve()._attr();
  }
  
  public Object getHandle()
  {
    return this.handle;
  }
  
  public REngine getEngine()
  {
    return this.eng;
  }
  
  public String toString()
  {
    return super.toString() + "{eng=" + this.eng + ",h=" + this.handle + "}";
  }
}
