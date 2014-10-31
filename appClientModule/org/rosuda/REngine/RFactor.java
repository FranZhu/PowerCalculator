package org.rosuda.REngine;

import java.util.Vector;

public class RFactor
{
  int[] ids;
  String[] levels;
  int index_base;
  
  public RFactor()
  {
    this.ids = new int[0];this.levels = new String[0];
  }
  
  public RFactor(int[] paramArrayOfInt, String[] paramArrayOfString, boolean paramBoolean, int paramInt)
  {
    if (paramArrayOfInt == null) {
      paramArrayOfInt = new int[0];
    }
    if (paramArrayOfString == null) {
      paramArrayOfString = new String[0];
    }
    if (paramBoolean)
    {
      this.ids = new int[paramArrayOfInt.length];System.arraycopy(paramArrayOfInt, 0, this.ids, 0, paramArrayOfInt.length);
      this.levels = new String[paramArrayOfString.length];System.arraycopy(paramArrayOfString, 0, this.levels, 0, paramArrayOfString.length);
    }
    else
    {
      this.ids = paramArrayOfInt;this.levels = paramArrayOfString;
    }
    this.index_base = paramInt;
  }
  
  public RFactor(String[] paramArrayOfString, int paramInt)
  {
    this.index_base = paramInt;
    if (paramArrayOfString == null) {
      paramArrayOfString = new String[0];
    }
    Vector localVector = new Vector();
    this.ids = new int[paramArrayOfString.length];
    int i = 0;
    while (i < paramArrayOfString.length)
    {
      int j = paramArrayOfString[i] == null ? -1 : localVector.indexOf(paramArrayOfString[i]);
      if ((j < 0) && (paramArrayOfString[i] != null))
      {
        j = localVector.size();
        localVector.add(paramArrayOfString[i]);
      }
      this.ids[i] = (j < 0 ? -2147483648 : j + paramInt);
      i++;
    }
    this.levels = new String[localVector.size()];
    i = 0;
    while (i < this.levels.length)
    {
      this.levels[i] = ((String)localVector.elementAt(i));
      i++;
    }
  }
  
  public RFactor(String[] paramArrayOfString)
  {
    this(paramArrayOfString, 1);
  }
  
  public RFactor(int[] paramArrayOfInt, String[] paramArrayOfString)
  {
    this(paramArrayOfInt, paramArrayOfString, true, 1);
  }
  
  public String at(int paramInt)
  {
    int i = this.ids[paramInt] - this.index_base;
    return (i < 0) || (i > this.levels.length) ? null : this.levels[i];
  }
  
  public boolean contains(int paramInt)
  {
    int i = 0;
    while (i < this.ids.length)
    {
      if (this.ids[i] == paramInt) {
        return true;
      }
      i++;
    }
    return false;
  }
  
  public boolean contains(String paramString)
  {
    int i = levelIndex(paramString);
    if (i < 0) {
      return false;
    }
    int j = 0;
    while (j < this.ids.length)
    {
      if (this.ids[j] == i) {
        return true;
      }
      j++;
    }
    return false;
  }
  
  public int count(int paramInt)
  {
    int i = 0;
    int j = 0;
    while (i < this.ids.length)
    {
      if (this.ids[i] == paramInt) {
        j++;
      }
      i++;
    }
    return j;
  }
  
  public int count(String paramString)
  {
    return count(levelIndex(paramString));
  }
  
  public int[] counts()
  {
    int[] arrayOfInt = new int[this.levels.length];
    int i = 0;
    while (i < this.ids.length)
    {
      int j = this.ids[i] - this.index_base;
      if ((j >= 0) && (j < this.levels.length)) {
        arrayOfInt[j] += 1;
      }
      i++;
    }
    return arrayOfInt;
  }
  
  public int levelIndex(String paramString)
  {
    if (paramString == null) {
      return -1;
    }
    int i = 0;
    while (i < this.levels.length)
    {
      if ((this.levels[i] != null) && (this.levels[i].equals(paramString))) {
        return i + this.index_base;
      }
      i++;
    }
    return -1;
  }
  
  public String[] levels()
  {
    return this.levels;
  }
  
  public int[] asIntegers()
  {
    return this.ids;
  }
  
  public int[] asIntegers(int paramInt)
  {
    if (paramInt == this.index_base) {
      return this.ids;
    }
    int[] arrayOfInt = new int[this.ids.length];
    for (int i = 0; i < this.ids.length; i++) {
      arrayOfInt[i] = (this.ids[i] - this.index_base + paramInt);
    }
    return arrayOfInt;
  }
  
  public String levelAtIndex(int paramInt)
  {
    paramInt -= this.index_base;
    return (paramInt < 0) || (paramInt > this.levels.length) ? null : this.levels[paramInt];
  }
  
  public int indexAt(int paramInt)
  {
    return this.ids[paramInt];
  }
  
  public String[] asStrings()
  {
    String[] arrayOfString = new String[this.ids.length];
    int i = 0;
    while (i < this.ids.length)
    {
      arrayOfString[i] = at(i);
      i++;
    }
    return arrayOfString;
  }
  
  public int indexBase()
  {
    return this.index_base;
  }
  
  public int size()
  {
    return this.ids.length;
  }
  
  public String toString()
  {
    return super.toString() + "[" + this.ids.length + "," + this.levels.length + ",#" + this.index_base + "]";
  }
}
