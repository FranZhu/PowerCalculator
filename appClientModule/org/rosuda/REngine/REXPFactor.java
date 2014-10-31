package org.rosuda.REngine;

public class REXPFactor
  extends REXPInteger
{
  private String[] levels;
  private RFactor factor;
  
  public REXPFactor(int[] paramArrayOfInt, String[] paramArrayOfString)
  {
    super(paramArrayOfInt);
    this.levels = (paramArrayOfString == null ? new String[0] : paramArrayOfString);
    this.factor = new RFactor(this.payload, this.levels, false, 1);
    this.attr = new REXPList(new RList(new REXP[] { new REXPString(this.levels), new REXPString("factor") }, new String[] { "levels", "class" }));
  }
  
  public REXPFactor(int[] paramArrayOfInt, String[] paramArrayOfString, REXPList paramREXPList)
  {
    super(paramArrayOfInt, paramREXPList);
    this.levels = (paramArrayOfString == null ? new String[0] : paramArrayOfString);
    this.factor = new RFactor(this.payload, this.levels, false, 1);
  }
  
  public REXPFactor(RFactor paramRFactor)
  {
    super(paramRFactor.asIntegers(1));
    this.factor = paramRFactor;
    this.levels = paramRFactor.levels();
    this.attr = new REXPList(new RList(new REXP[] { new REXPString(this.levels), new REXPString("factor") }, new String[] { "levels", "class" }));
  }
  
  public REXPFactor(RFactor paramRFactor, REXPList paramREXPList)
  {
    super(paramRFactor.asIntegers(1), paramREXPList);
    this.factor = paramRFactor;
    this.levels = paramRFactor.levels();
  }
  
  public boolean isFactor()
  {
    return true;
  }
  
  public RFactor asFactor()
  {
    return this.factor;
  }
  
  public String[] asStrings()
  {
    return this.factor.asStrings();
  }
  
  public Object asNativeJavaObject()
  {
    return asStrings();
  }
  
  public String toString()
  {
    return super.toString() + "[" + this.levels.length + "]";
  }
}
