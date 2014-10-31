package org.rosuda.REngine;

import java.util.HashMap;
import java.util.Vector;

public class REXPGenericVector
  extends REXPVector
{
  private RList payload;
  
  public REXPGenericVector(RList paramRList)
  {
    this.payload = (paramRList == null ? new RList() : paramRList);
    if (this.payload.isNamed()) {
      this.attr = new REXPList(new RList(new REXP[] { new REXPString(this.payload.keys()) }, new String[] { "names" }));
    }
  }
  
  public REXPGenericVector(RList paramRList, REXPList paramREXPList)
  {
    super(paramREXPList);
    this.payload = (paramRList == null ? new RList() : paramRList);
  }
  
  public Object asNativeJavaObject()
    throws REXPMismatchException
  {
    int i = this.payload.size();
    if (this.payload.isNamed())
    {
      String [] localObject1 = this.payload.keys();
      if (localObject1.length == i)
      {
        HashMap localHashMap = new HashMap();
        int k = 1;
        for (int m = 0; m < i; m++)
        {
          if (localHashMap.containsKey(localObject1[m]))
          {
            k = 0;
            break;
          }
          Object localObject3 = this.payload.elementAt(m);
          if (localObject3 != null) {
            localObject3 = ((REXP)localObject3).asNativeJavaObject();
          }
          localHashMap.put(localObject3, localObject1[m]);
        }
        if (k != 0) {
          return localHashMap;
        }
      }
    }
    Object localObject1 = new Vector();
    for (int j = 0; j < i; j++)
    {
      Object localObject2 = this.payload.elementAt(j);
      if (localObject2 != null) {
        localObject2 = ((REXP)localObject2).asNativeJavaObject();
      }
      ((Vector)localObject1).addElement(localObject2);
    }
    return localObject1;
  }
  
  public int length()
  {
    return this.payload.size();
  }
  
  public boolean isList()
  {
    return true;
  }
  
  public boolean isRecursive()
  {
    return true;
  }
  
  public RList asList()
  {
    return this.payload;
  }
  
  public String toString()
  {
    return super.toString() + (asList().isNamed() ? "named" : "");
  }
  
  public String toDebugString()
  {
    StringBuffer localStringBuffer = new StringBuffer(super.toDebugString() + "{");
    int i = 0;
    while ((i < this.payload.size()) && (i < maxDebugItems))
    {
      if (i > 0) {
        localStringBuffer.append(",\n");
      }
      localStringBuffer.append(this.payload.at(i).toDebugString());
      i++;
    }
    if (i < this.payload.size()) {
      localStringBuffer.append(",..");
    }
    return localStringBuffer.toString() + "}";
  }
}
