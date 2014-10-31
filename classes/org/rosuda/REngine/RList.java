package org.rosuda.REngine;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class RList
  extends Vector
  implements List
{
  public Vector names;
  
  public RList()
  {
    this.names = null;
  }
  
  public RList(REXP[] paramArrayOfREXP)
  {
    super(paramArrayOfREXP.length);
    int i = 0;
    while (i < paramArrayOfREXP.length) {
      super.add(paramArrayOfREXP[(i++)]);
    }
    this.names = null;
  }
  
  public RList(int paramInt, boolean paramBoolean)
  {
    super(paramInt);
    this.names = null;
    if (paramBoolean) {
      this.names = new Vector(paramInt);
    }
  }
  
  public RList(Collection paramCollection)
  {
    super(paramCollection);
    this.names = null;
  }
  
  public RList(REXP[] paramArrayOfREXP, String[] paramArrayOfString)
  {
    this(paramArrayOfREXP);
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      this.names = new Vector(paramArrayOfString.length);
      int i = 0;
      while (i < paramArrayOfString.length) {
        this.names.add(paramArrayOfString[(i++)]);
      }
      while (this.names.size() < size()) {
        this.names.add(null);
      }
    }
  }
  
  public RList(Collection paramCollection, String[] paramArrayOfString)
  {
    this(paramCollection);
    if ((paramArrayOfString != null) && (paramArrayOfString.length > 0))
    {
      this.names = new Vector(paramArrayOfString.length);
      int i = 0;
      while (i < paramArrayOfString.length) {
        this.names.add(paramArrayOfString[(i++)]);
      }
      while (this.names.size() < size()) {
        this.names.add(null);
      }
    }
  }
  
  public RList(Collection paramCollection1, Collection paramCollection2)
  {
    this(paramCollection1);
    if ((paramCollection2 != null) && (paramCollection2.size() > 0))
    {
      this.names = new Vector(paramCollection2);
      while (this.names.size() < size()) {
        this.names.add(null);
      }
    }
  }
  
  public boolean isNamed()
  {
    return this.names != null;
  }
  
  public REXP at(String paramString)
  {
    if (this.names == null) {
      return null;
    }
    int i = this.names.indexOf(paramString);
    if (i < 0) {
      return null;
    }
    return (REXP)elementAt(i);
  }
  
  public REXP at(int paramInt)
  {
    return (paramInt >= 0) && (paramInt < size()) ? (REXP)elementAt(paramInt) : null;
  }
  
  public String keyAt(int paramInt)
  {
    return (this.names == null) || (paramInt < 0) || (paramInt >= this.names.size()) ? null : (String)this.names.get(paramInt);
  }
  
  public void setKeyAt(int paramInt, String paramString)
  {
    if (paramInt < 0) {
      return;
    }
    if (this.names == null) {
      this.names = new Vector();
    }
    if (this.names.size() < size()) {
      this.names.setSize(size());
    }
    if (paramInt < size()) {
      this.names.set(paramInt, paramString);
    }
  }
  
  public String[] keys()
  {
    if (this.names == null) {
      return null;
    }
    int i = 0;
    String[] arrayOfString = new String[this.names.size()];
    for (; i < arrayOfString.length; i++) {
      arrayOfString[i] = keyAt(i);
    }
    return arrayOfString;
  }
  
  public void add(int paramInt, Object paramObject)
  {
    super.add(paramInt, paramObject);
    if (this.names == null) {
      return;
    }
    this.names.add(paramInt, null);
  }
  
  public boolean add(Object paramObject)
  {
    super.add(paramObject);
    if (this.names != null) {
      this.names.add(null);
    }
    return true;
  }
  
  public boolean addAll(Collection paramCollection)
  {
    boolean bool = super.addAll(paramCollection);
    if (this.names == null) {
      return bool;
    }
    int i = size();
    while (this.names.size() < i) {
      this.names.add(null);
    }
    return bool;
  }
  
  public boolean addAll(int paramInt, Collection paramCollection)
  {
    boolean bool = super.addAll(paramInt, paramCollection);
    if (this.names == null) {
      return bool;
    }
    int i = paramCollection.size();
    while (i-- > 0) {
      this.names.add(paramInt, null);
    }
    return bool;
  }
  
  public void clear()
  {
    super.clear();
    this.names = null;
  }
  
  public Object clone()
  {
    return new RList(this, this.names);
  }
  
  public Object remove(int paramInt)
  {
    Object localObject = super.remove(paramInt);
    if (this.names != null)
    {
      this.names.remove(paramInt);
      if (size() == 0) {
        this.names = null;
      }
    }
    return localObject;
  }
  
  public boolean remove(Object paramObject)
  {
    int i = indexOf(paramObject);
    if (i < 0) {
      return false;
    }
    remove(i);
    if (size() == 0) {
      this.names = null;
    }
    return true;
  }
  
  public boolean removeAll(Collection paramCollection)
  {
    if (this.names == null) {
      return super.removeAll(paramCollection);
    }
    boolean bool = false;
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext()) {
      bool |= remove(localIterator.next());
    }
    return bool;
  }
  
  public boolean retainAll(Collection paramCollection)
  {
    if (this.names == null) {
      return super.retainAll(paramCollection);
    }
    boolean[] arrayOfBoolean = new boolean[size()];
    boolean bool = false;
    int i = 0;
    while (i < arrayOfBoolean.length)
    {
      bool |= (arrayOfBoolean[i] = !paramCollection.contains(get(i)) ? true : false);
      i++;
    }
    while (i > 0)
    {
      i--;
      if (arrayOfBoolean[i] == true) {
        remove(i);
      }
    }
    return bool;
  }
  
  public void removeAllElements()
  {
    clear();
  }
  
  public void insertElementAt(Object paramObject, int paramInt)
  {
    add(paramInt, paramObject);
  }
  
  public void addElement(Object paramObject)
  {
    add(paramObject);
  }
  
  public void removeElementAt(int paramInt)
  {
    remove(paramInt);
  }
  
  public boolean removeElement(Object paramObject)
  {
    return remove(paramObject);
  }
  
  public boolean containsKey(Object paramObject)
  {
    return this.names == null ? false : this.names.contains(paramObject);
  }
  
  public boolean containsValue(Object paramObject)
  {
    return contains(paramObject);
  }
  
  public Set entrySet()
  {
    return null;
  }
  
  public Object get(Object paramObject)
  {
    return at((String)paramObject);
  }
  
  public Set keySet()
  {
    if (this.names == null) {
      return null;
    }
    return new HashSet(this.names);
  }
  
  public Object put(Object paramObject1, Object paramObject2)
  {
    if (paramObject1 == null)
    {
      add(paramObject2);
      return null;
    }
    if (this.names != null)
    {
      int i = this.names.indexOf(paramObject1);
      if (i >= 0) {
        return super.set(i, paramObject2);
      }
    }
    int i = size();
    super.add(paramObject2);
    if (this.names == null) {
      this.names = new Vector(i + 1);
    }
    while (this.names.size() < i) {
      this.names.add(null);
    }
    this.names.add(paramObject1);
    return null;
  }
  
  public void putAll(Map paramMap)
  {
    if (paramMap == null) {
      return;
    }
    Object localObject1;
    if ((paramMap instanceof RList))
    {
      localObject1 = (RList)paramMap;
      if (this.names == null)
      {
        addAll((Collection)localObject1);
        return;
      }
      int i = ((RList)localObject1).size();
      int j = 0;
      while (j < i)
      {
        String str = ((RList)localObject1).keyAt(j);
        if (str == null) {
          add(((RList)localObject1).at(j));
        } else {
          put(str, ((RList)localObject1).at(j));
        }
        j++;
      }
    }
    else
    {
      localObject1 = paramMap.keySet();
      Iterator localIterator = ((Set)localObject1).iterator();
      while (localIterator.hasNext())
      {
        Object localObject2 = localIterator.next();
        put(localObject2, paramMap.get(localObject2));
      }
    }
  }
  
  public void putAll(RList paramRList)
  {
    if (paramRList == null) {
      return;
    }
    RList localRList = paramRList;
    if (this.names == null)
    {
      addAll(localRList);
      return;
    }
    int i = localRList.size();
    int j = 0;
    while (j < i)
    {
      String str = localRList.keyAt(j);
      if (str == null) {
        add(localRList.at(j));
      } else {
        put(str, localRList.at(j));
      }
      j++;
    }
  }
  
  public Object removeByKey(Object paramObject)
  {
    if (this.names == null) {
      return null;
    }
    int i = this.names.indexOf(paramObject);
    if (i < 0) {
      return null;
    }
    Object localObject = elementAt(i);
    removeElementAt(i);
    this.names.removeElementAt(i);
    return localObject;
  }
  
  public Collection values()
  {
    return this;
  }
  
  public String toString()
  {
    return "RList" + super.toString() + "{" + (isNamed() ? "named," : "") + size() + "}";
  }
}
