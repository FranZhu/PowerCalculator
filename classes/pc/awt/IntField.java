package pc.awt;

import java.awt.Event;
import java.awt.TextField;

public class IntField
  extends TextField
{
  boolean hasFocus = false;
  int currentValue = 0;
  
  public IntField(int paramInt1, int paramInt2)
  {
    super("", paramInt2);
    setValue(paramInt1);
  }
  
  public IntField(int paramInt)
  {
    super("");
    setValue(paramInt);
  }
  
  public void setValue(int paramInt)
  {
    this.currentValue = paramInt;
    setText("" + paramInt);
  }
  
  public int getValue()
  {
    try
    {
      this.currentValue = Double.valueOf(getText().trim()).intValue();
      setText("" + this.currentValue);
      return this.currentValue;
    }
    catch (Exception localException) {}
    return -2147483648;
  }
  
  public boolean keyDown(Event paramEvent, int paramInt)
  {
    if (paramInt == 13)
    {
      deliverEvent(new Event(this, 1001, new Integer(getValue())));
      
      return true;
    }
    return super.keyDown(paramEvent, paramInt);
  }
  
  public boolean gotFocus(Event paramEvent, Object paramObject)
  {
    this.hasFocus = true;
    return true;
  }
  
  public boolean lostFocus(Event paramEvent, Object paramObject)
  {
    if (this.hasFocus)
    {
      this.hasFocus = false;
      int i = this.currentValue;
      if (i != getValue()) {
        deliverEvent(new Event(this, 1001, new Integer(this.currentValue)));
      }
    }
    return true;
  }
}
