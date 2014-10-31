package pc.awt;

import java.awt.Event;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import pc.util.Utility;

public class DoubleField
  extends TextField
  implements FocusListener
{
  private int digits;
  private double currentValue = (0.0D / 0.0D);
  private String currentText = "NaN";
  private boolean hasFocus = false;
  
  public DoubleField(double paramDouble)
  {
    this(paramDouble, 3, 3);
  }
  
  public DoubleField(double paramDouble, int paramInt)
  {
    this(paramDouble, paramInt, 3);
  }
  
  public DoubleField(double paramDouble, int paramInt1, int paramInt2)
  {
    super("", paramInt1);
    this.digits = paramInt2;
    setValue(paramDouble);
    addFocusListener(this);
  }
  
  public DoubleField(String paramString, int paramInt)
  {
    super(paramInt);
    this.digits = 3;
    setValue((0.0D / 0.0D));
    setText(paramString);
  }
  
  public void setValue(double paramDouble)
  {
    this.currentText = Utility.format(paramDouble, this.digits);
    setText(this.currentText);
    this.currentValue = paramDouble;
  }
  
  public double getValue()
  {
    if (this.currentText == getText().trim()) {
      return this.currentValue;
    }
    try
    {
      this.currentText = getText().trim();
      this.currentValue = Double.valueOf(this.currentText).doubleValue();
      return this.currentValue;
    }
    catch (Exception localException)
    {
      this.currentValue = (0.0D / 0.0D);
    }
    return this.currentValue;
  }
  
  public boolean keyDown(Event paramEvent, int paramInt)
  {
    if (paramInt == 13)
    {
      deliverEvent(new Event(this, 1001, new Double(getValue())));
      
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
      if (this.currentText != getText().trim()) {
        deliverEvent(new Event(this, 1001, new Double(getValue())));
      }
    }
    return true;
  }
  
  public void focusGained(FocusEvent paramFocusEvent)
  {
    if (paramFocusEvent.isTemporary()) {
      return;
    }
    this.hasFocus = true;
  }
  
  public void focusLost(FocusEvent paramFocusEvent)
  {
    if (paramFocusEvent.isTemporary()) {
      return;
    }
    this.hasFocus = false;
    if (this.currentText != getText().trim())
    {
      getValue();
      processEvent(new ActionEvent(this, 1001, this.currentText));
    }
  }
}
