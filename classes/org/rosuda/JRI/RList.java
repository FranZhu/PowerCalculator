package org.rosuda.JRI;

import java.util.Vector;

public class RList
{
  public REXP head;
  public REXP body;
  public REXP tag;
  String[] keys = null;
  REXP[] values = null;
  boolean dirtyCache = true;
  
  public RList()
  {
    this.head = (this.body = this.tag = null);
  }
  
  public RList(RVector paramRVector)
  {
    Vector localVector = paramRVector.getNames();
    if (localVector != null)
    {
      this.keys = new String[localVector.size()];
      localVector.copyInto(this.keys);
    }
    this.values = new REXP[paramRVector.size()];
    paramRVector.copyInto(this.values);
    this.dirtyCache = false;
  }
  
  public RList(REXP paramREXP1, REXP paramREXP2)
  {
    this.head = paramREXP1;this.body = paramREXP2;this.tag = null;
  }
  
  public RList(REXP paramREXP1, REXP paramREXP2, REXP paramREXP3)
  {
    this.head = paramREXP1;this.body = paramREXP3;this.tag = paramREXP2;
  }
  
  public REXP getHead()
  {
    return this.head;
  }
  
  public REXP getBody()
  {
    return this.body;
  }
  
  public REXP getTag()
  {
    return this.tag;
  }
  
  boolean updateVec()
  {
    if (!this.dirtyCache) {
      return true;
    }
    RList localRList = this;
    int i = 0;
    REXP localREXP1;
    while (localRList != null)
    {
      i++;
      localREXP1 = localRList.getBody();
      localRList = localREXP1 == null ? null : localREXP1.asList();
    }
    this.keys = new String[i];
    this.values = new REXP[i];
    localRList = this;
    i = 0;
    while (localRList != null)
    {
      localREXP1 = localRList.getTag();
      if (localREXP1 != null) {
        this.keys[i] = localREXP1.asSymbolName();
      }
      this.values[i] = localRList.getHead();
      REXP localREXP2 = localRList.getBody();
      localRList = localREXP2 == null ? null : localREXP2.asList();
      i++;
    }
    this.dirtyCache = false;
    return true;
  }
  
  public REXP at(String paramString)
  {
    if ((!updateVec()) || (this.keys == null) || (this.values == null)) {
      return null;
    }
    int i = 0;
    while (i < this.keys.length)
    {
      if (this.keys[i].compareTo(paramString) == 0) {
        return this.values[i];
      }
      i++;
    }
    return null;
  }
  
  public REXP at(int paramInt)
  {
    return (!updateVec()) || (this.values == null) || (paramInt < 0) || (paramInt >= this.values.length) ? null : this.values[paramInt];
  }
  
  public String[] keys()
  {
    return !updateVec() ? null : this.keys;
  }
}
