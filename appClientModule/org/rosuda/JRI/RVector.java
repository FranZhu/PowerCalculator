package org.rosuda.JRI;

import java.util.Enumeration;
import java.util.Vector;

public class RVector
  extends Vector
{
  Vector names = null;
  
  public void setNames(String[] paramArrayOfString)
  {
    this.names = new Vector(paramArrayOfString.length);
    int i = 0;
    while (i < paramArrayOfString.length) {
      this.names.addElement(paramArrayOfString[(i++)]);
    }
  }
  
  public Vector getNames()
  {
    return this.names;
  }
  
  public REXP at(String paramString)
  {
    if (this.names == null) {
      return null;
    }
    int i = 0;
    for (Enumeration localEnumeration = this.names.elements(); localEnumeration.hasMoreElements();)
    {
      String str = (String)localEnumeration.nextElement();
      if (str.equals(paramString)) {
        return (REXP)elementAt(i);
      }
      i++;
    }
    return null;
  }
  
  public REXP at(int paramInt)
  {
    return (REXP)elementAt(paramInt);
  }
}
