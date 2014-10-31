package org.rosuda.JRI;

public class RBool
{
  int val;
  
  public RBool(boolean paramBoolean)
  {
    this.val = (paramBoolean ? 1 : 0);
  }
  
  public RBool(RBool paramRBool)
  {
    this.val = paramRBool.val;
  }
  
  public RBool(int paramInt)
  {
    this.val = ((paramInt == 0) || (paramInt == 2) ? paramInt : 1);
  }
  
  public boolean isNA()
  {
    return this.val == 2;
  }
  
  public boolean isTRUE()
  {
    return this.val == 1;
  }
  
  public boolean isFALSE()
  {
    return this.val == 0;
  }
  
  public String toString()
  {
    return this.val == 2 ? "NA" : this.val == 0 ? "FALSE" : "TRUE";
  }
}
