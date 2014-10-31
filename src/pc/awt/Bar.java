package pc.awt;

import java.awt.AWTEventMulticaster;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bar
  extends Canvas
{
  private double value;
  private int intVal;
  private BarGroup group;
  private int hotMaxX;
  private int hotMinY;
  private int hotMaxY;
  private Object barID;
  private boolean intMode = false;
  protected transient ActionListener actionListener = null;
  
  public Bar(Object paramObject, double paramDouble, BarGroup paramBarGroup)
  {
    this.barID = paramObject;
    this.group = paramBarGroup;
    this.value = (Double.isNaN(paramDouble) ? 0.0D : Math.max(paramDouble, 0.0D));
    paramBarGroup.addBar(this, paramDouble);
  }
  
  public Bar(Object paramObject, int paramInt, BarGroup paramBarGroup)
  {
    this.barID = paramObject;
    this.group = paramBarGroup;
    this.intMode = true;
    this.intVal = paramInt;
    this.value = this.intVal;
    paramBarGroup.addBar(this, this.value);
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public double getValue()
  {
    return this.value;
  }
  
  public int getIntVal()
  {
    if (this.intMode) {
      return this.intVal;
    }
    return (int)(this.value + 0.5D);
  }
  
  public void setValue(double paramDouble)
  {
    if (Double.isNaN(paramDouble)) {
      paramDouble = 0.0D;
    }
    this.value = Math.max(paramDouble, 0.0D);
    if (!this.group.rescalable)
    {
      this.value = Math.min(this.value, this.group.limit);
      repaint();
      return;
    }
    if (!this.group.checkValue(this.value)) {
      repaint();
    }
  }
  
  public void setValue(int paramInt)
  {
    this.intVal = paramInt;
    setValue(paramInt);
  }
  
  public Dimension preferredSize()
  {
    return new Dimension(300, 15);
  }
  
  public Dimension minimumSize()
  {
    return preferredSize();
  }
  
  public void paint(Graphics paramGraphics)
  {
    int i = size().width - 10;
    int j = size().height / 2;
    int k = j - 4;int m = j + 4;
    this.hotMaxX = (i + 5);
    this.hotMinY = (j - 2);
    this.hotMaxY = (j + 2);
    double d1 = i / this.group.limit;
    paramGraphics.setColor(this.group.lineColor);
    paramGraphics.drawLine(5, j, this.hotMaxX, j);
    paramGraphics.setColor(this.group.tickColor);
    for (double d2 = 0.0D; d2 < 1.01D * this.group.limit; d2 += this.group.tickInterval)
    {
      int i1 = (int)(5.5D + d2 * d1);
      paramGraphics.drawLine(i1, k, i1, m);
    }
    int n = (int)(0.5D + this.value * d1);
    paramGraphics.setColor(this.group.barColor);
    paramGraphics.fillRect(5, this.hotMinY, n, 5);
  }
  
  private boolean isHot(int paramInt1, int paramInt2)
  {
    return (paramInt2 >= this.hotMinY) && (paramInt2 <= this.hotMaxY) && (paramInt1 >= 5) && (paramInt1 <= this.hotMaxX);
  }
  
  public boolean mouseEnter(Event paramEvent, int paramInt1, int paramInt2)
  {
    if (this.group.readOnly) {
      this.group.parentFrame.setCursor(0);
    } else {
      this.group.parentFrame.setCursor(1);
    }
    return true;
  }
  
  public boolean mouseExit(Event paramEvent, int paramInt1, int paramInt2)
  {
    this.group.parentFrame.setCursor(0);
    return true;
  }
  
  public boolean mouseDown(Event paramEvent, int paramInt1, int paramInt2)
  {
    requestFocus();
    return true;
  }
  
  public boolean mouseUp(Event paramEvent, int paramInt1, int paramInt2)
  {
    if (this.group.readOnly) {
      return true;
    }
    if (!isHot(paramInt1, paramInt2)) {
      return true;
    }
    this.value = (this.group.limit * (paramInt1 - 5) / (size().width - 10));
    if (this.intMode)
    {
      this.intVal = new Double(this.value + 0.5D).intValue();
      this.value = this.intVal;
    }
    repaint();
    if (this.actionListener != null)
    {
      ActionEvent localActionEvent = new ActionEvent(this, 1001, this.barID.toString());
      
      this.actionListener.actionPerformed(localActionEvent);
    }
    else
    {
      deliverEvent(new Event(this, 1001, this.barID));
    }
    this.group.parentFrame.setCursor(1);
    return true;
  }
  
  public boolean mouseDrag(Event paramEvent, int paramInt1, int paramInt2)
  {
    if (this.group.readOnly) {
      return true;
    }
    int i = paramInt1 > this.hotMaxX ? this.hotMaxX : paramInt1 < 5 ? 5 : paramInt1;
    this.value = (this.group.limit * (i - 5) / (size().width - 10));
    if (this.intMode)
    {
      this.intVal = new Double(this.value).intValue();
      this.value = this.intVal;
    }
    repaint();
    deliverEvent(new Event(this, 1001, this.barID));
    return true;
  }
}
