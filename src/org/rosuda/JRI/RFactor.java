package org.rosuda.JRI;

import java.util.Vector;

public class RFactor
{
  Vector id;
  Vector val;
  
  public RFactor()
  {
    this.id = new Vector();this.val = new Vector();
  }
  
  public RFactor(int[] paramArrayOfInt, String[] paramArrayOfString)
  {
    this(paramArrayOfInt, paramArrayOfString, 0);
  }
  
  RFactor(int[] paramArrayOfInt, String[] paramArrayOfString, int paramInt)
  {
    this.id = new Vector();this.val = new Vector();
    int i;
    if ((paramArrayOfInt != null) && (paramArrayOfInt.length > 0)) {
      for (i = 0; i < paramArrayOfInt.length; i++) {
        this.id.addElement(new Integer(paramArrayOfInt[i] - paramInt));
      }
    }
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0)) {
      for (i = 0; i < paramArrayOfString.length; i++) {
        this.val.addElement(paramArrayOfString[i]);
      }
    }
  }
  
  public void add(String paramString)
  {
    int i = this.val.indexOf(paramString);
    if (i < 0)
    {
      i = this.val.size();
      this.val.addElement(paramString);
    }
    this.id.addElement(new Integer(i));
  }
  
  public String at(int paramInt)
  {
    if ((paramInt < 0) || (paramInt >= this.id.size())) {
      return null;
    }
    int i = ((Integer)this.id.elementAt(paramInt)).intValue();
    
    return (i < 0) || (i > 2147483640) ? null : (String)this.val.elementAt(i);
  }
  
  public int size()
  {
    return this.id.size();
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("{levels=(");
    int i;
    if (this.val == null) {
      localStringBuffer.append("null");
    } else {
      for (i = 0; i < this.val.size(); i++)
      {
        localStringBuffer.append(i > 0 ? ",\"" : "\"");
        localStringBuffer.append((String)this.val.elementAt(i));
        localStringBuffer.append("\"");
      }
    }
    localStringBuffer.append("),ids=(");
    if (this.id == null) {
      localStringBuffer.append("null");
    } else {
      for (i = 0; i < this.id.size(); i++)
      {
        if (i > 0) {
          localStringBuffer.append(",");
        }
        localStringBuffer.append((Integer)this.id.elementAt(i));
      }
    }
    localStringBuffer.append(")}");
    return localStringBuffer.toString();
  }
}
