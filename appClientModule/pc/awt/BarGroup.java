package pc.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.util.Vector;
import pc.util.Utility;

public class BarGroup
  extends Canvas
{
  protected double factor = Math.pow(2.0D, 0.25D);
  protected double maxVal = 0.0D;
  protected Color barColor = Color.red;
  protected Color lineColor = Color.white;
  protected Color tickColor = Color.blue;
  protected boolean rescalable = true;
  protected boolean readOnly = false;
  private boolean fontSet = false;
  
  public BarGroup(double paramDouble, Frame paramFrame)
  {
    this.bar = new Vector();
    setLimit(paramDouble, false);
    this.parentFrame = paramFrame;
  }
  
  public void addBar(Bar paramBar, double paramDouble)
  {
    this.bar.addElement(paramBar);
    this.maxVal = Math.max(paramDouble, this.maxVal);
    if (this.maxVal > this.limit) {
      setLimit(this.maxVal, false);
    }
  }
  
  public void setRescalable(boolean paramBoolean)
  {
    this.rescalable = paramBoolean;
  }
  
  public void setReadOnly(boolean paramBoolean)
  {
    this.readOnly = paramBoolean;
  }
  
  Bar getBar(int paramInt)
  {
    return (Bar)this.bar.elementAt(paramInt);
  }
  
  static final double ln10 = Math.log(10.0D);
  protected double limit;
  protected double tickInterval;
  protected Frame parentFrame;
  Vector bar;
  private Font labFont;
  private FontMetrics labMetrics;
  private int labHeight;
  private int xDown;
  
  static double log10(double paramDouble)
  {
    return Math.log(paramDouble) / ln10;
  }
  
  public double getLimit()
  {
    return this.limit;
  }
  
  public void setLimit(double paramDouble, boolean paramBoolean)
  {
    this.maxVal = 0.0D;
    if (this.rescalable)
    {
      for (int i = 0; i < this.bar.size(); i++) {
        this.maxVal = Math.max(this.maxVal, getBar(i).getValue());
      }
      if (paramDouble < this.maxVal) {
        paramDouble = this.maxVal * 1.01D;
      }
      this.limit = paramDouble;
    }
    double d1 = Math.pow(10.0D, Math.floor(log10(this.limit)));
    double d2 = this.limit / d1;
    this.tickInterval = (d2 <= 4.0D ? d1 / 2.0D : d2 <= 1.5D ? d1 / 5.0D : d1);
    if (paramBoolean)
    {
      for (int j = 0; j < this.bar.size(); j++) {
        getBar(j).repaint();
      }
      repaint();
    }
  }
  
  boolean checkValue(double paramDouble)
  {
    if (paramDouble <= this.maxVal) {
      return false;
    }
    setLimit(paramDouble * 1.05D, true);
    return true;
  }
  
  public void grow()
  {
    setLimit(this.factor * this.limit, true);
  }
  
  public void shrink()
  {
    setLimit(this.limit / this.factor, true);
  }
  
  private void setFont(Graphics paramGraphics)
  {
    this.labFont = new Font("Helvetica", 0, 9);
    this.labMetrics = paramGraphics.getFontMetrics(this.labFont);
    this.labHeight = this.labMetrics.getAscent();
    this.fontSet = true;
  }
  
  public void setColor(Color paramColor)
  {
    this.barColor = paramColor;
  }
  
  public void setTickColor(Color paramColor)
  {
    this.tickColor = paramColor;
  }
  
  public void setBackground(Color paramColor)
  {
    this.lineColor = paramColor;
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
    if (!this.fontSet) {
      setFont(paramGraphics);
    }
    int i = size().width - 10;
    int j = (size().height + this.labHeight) / 2;
    double d1 = i / this.limit;
    paramGraphics.setFont(this.labFont);
    paramGraphics.setColor(this.tickColor);
    int k = -100;
    for (double d2 = 0.0D; d2 < 1.01D * this.limit; d2 += this.tickInterval)
    {
      String str = niceLabel(d2);
      int m = this.labMetrics.stringWidth(str);
      int n = (int)(5.5D + d2 * d1 - m / 2);
      if (n + m > 5 + i) {
        n = 5 + i - m;
      }
      if (n > 5 + k)
      {
        paramGraphics.drawString(str, n, j);
        k = n + m;
      }
    }
  }
  
  private String niceLabel(double paramDouble)
  {
    int i = 1;
    String str = Utility.format(paramDouble, i);
    double d = 0.01D * Math.abs(paramDouble);
    while ((i < 10) && (Math.abs(Utility.strtod(str) - paramDouble) > d)) {
      str = Utility.format(paramDouble, ++i);
    }
    return str;
  }
  
  public boolean mouseEnter(Event paramEvent, int paramInt1, int paramInt2)
  {
    if (!this.rescalable) {
      return true;
    }
    this.parentFrame.setCursor(11);
    return true;
  }
  
  public boolean mouseExit(Event paramEvent, int paramInt1, int paramInt2)
  {
    if (!this.rescalable) {
      return true;
    }
    this.parentFrame.setCursor(0);
    return true;
  }
  
  public boolean mouseDown(Event paramEvent, int paramInt1, int paramInt2)
  {
    requestFocus();
    if (!this.rescalable) {
      return true;
    }
    this.xDown = (paramInt1 >= 10 ? paramInt1 : 10);
    return true;
  }
  
  public boolean mouseUp(Event paramEvent, int paramInt1, int paramInt2)
  {
    if (!this.rescalable) {
      return true;
    }
    paramInt1 = paramInt1 >= 10 ? paramInt1 : 10;
    double d = (this.xDown + 0.0D) / paramInt1;
    setLimit(d * this.limit, true);
    return true;
  }
}
