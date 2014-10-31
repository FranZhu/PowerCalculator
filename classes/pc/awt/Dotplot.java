package pc.awt;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import pc.stat.Stat;
import pc.util.Sort;
import pc.util.Utility;

public class Dotplot
  extends Component
  implements MouseListener, MouseMotionListener
{
  private double[] values;
  private double[] tick;
  private int n;
  private int em;
  private int w;
  private int y;
  private int which = -1;
  private double min;
  private double max;
  private double mean;
  private double binW;
  private double prevVal;
  private String[] tickLab;
  private Cursor pointer = Cursor.getDefaultCursor();
  private Cursor finger = Cursor.getPredefinedCursor(12);
  private Cursor crosshairs = Cursor.getPredefinedCursor(1);
  private transient ActionListener actionListener = null;
  
  public Dotplot(double[] paramArrayOfDouble)
  {
    setValues(paramArrayOfDouble);
    Font localFont = new Font("SansSerif", 0, 9);
    setFont(localFont);
    addMouseListener(this);
    addMouseMotionListener(this);
  }
  
  public void setValues(double[] paramArrayOfDouble)
  {
    if (paramArrayOfDouble == null) {
      paramArrayOfDouble = new double[] { 0.0D };
    }
    this.n = paramArrayOfDouble.length;
    this.values = new double[this.n];
    for (int i = 0; i < this.n; i++) {
      this.values[i] = paramArrayOfDouble[i];
    }
    setRange();
  }
  
  public double[] getValues()
  {
    return this.values;
  }
  
  private void setRange()
  {
    Sort.qsort(this.values);
    this.min = this.values[0];
    this.max = this.values[(this.n - 1)];
    if (this.min == this.max)
    {
      this.min -= 0.5D;
      this.max += 0.5D;
    }
    double d = 0.05D * (this.max - this.min);
    this.min -= d;
    this.max += d;
    this.tick = Utility.nice(this.min, this.max, 5, false);
    this.tickLab = Utility.fmtNice(this.tick);
    this.mean = Stat.mean(this.values);
  }
  
  public Dimension getPreferredSize()
  {
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    int i = localFontMetrics.stringWidth("m");
    int j = localFontMetrics.getAscent();
    return new Dimension(18 * i, 6 * j);
  }
  
  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }
  
  public Dimension preferredSize()
  {
    return getPreferredSize();
  }
  
  public Dimension minimumSize()
  {
    return getMinimumSize();
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public void paint(Graphics paramGraphics)
  {
    if (!isVisible()) {
      return;
    }
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    this.em = localFontMetrics.stringWidth("m");
    this.w = (getSize().width - 2 * this.em);
    int i = localFontMetrics.getAscent();
    int j = getSize().height;
    


    paramGraphics.setFont(getFont());
    this.y = (j - 2 * i);
    paramGraphics.setColor(Color.blue);
    paramGraphics.drawLine(this.em, this.y, this.w + this.em, this.y);
    int i2 = this.y + i / 3;
    int i3 = this.y + 3 * i / 2;
    int k = (int)((this.w + 0.0D) * (this.tick[1] - this.tick[0]) / (this.max - this.min));
    int m;
	for (int i4 = 0; i4 < this.tick.length; i4++)
    {
      m = this.em + (int)((this.w + 0.0D) * (this.tick[i4] - this.min) / (this.max - this.min));
      int i1 = m - localFontMetrics.stringWidth(this.tickLab[i4]) / 2;
      paramGraphics.drawLine(m, this.y, m, i2);
      paramGraphics.drawString(this.tickLab[i4], i1, i3);
    }
    int i4 = k / this.em;
    if (i4 == 0) {
      i4 = 1;
    }
    this.binW = ((this.tick[1] - this.tick[0]) / i4);
    int i1 = 0;
    for (int i5 = 0; i5 < this.n; i5++)
    {
      double d2 = this.binW * Math.round(this.values[i5] / this.binW);
      m = this.em / 2 + 1 + (int)((this.w + 0.0D) * (d2 - this.min) / (this.max - this.min));
      i2 = m == i1 ? i2 - this.em : this.y - this.em;
      i1 = m;
      if (i5 == this.which) {
        paramGraphics.setColor(Color.lightGray);
      } else {
        paramGraphics.setColor(Color.black);
      }
      if (i2 < -this.em / 2)
      {
        i2 += this.em;
        paramGraphics.setColor(Color.red);
      }
      paramGraphics.drawOval(i1, i2, this.em - 2, this.em - 2);
      paramGraphics.fillOval(i1, i2, this.em - 2, this.em - 2);
    }
    double d1 = this.binW * Math.round(this.mean / this.binW);
    m = this.em + (int)((this.w + 0.0D) * (d1 - this.min) / (this.max - this.min));
    Polygon localPolygon = new Polygon(new int[] { m - this.em / 2, m, m + this.em / 2 }, new int[] { this.y + this.em, this.y, this.y + this.em }, 3);
    if (this.which == -99) {
      paramGraphics.setColor(Color.lightGray);
    } else {
      paramGraphics.setColor(Color.red.darker());
    }
    paramGraphics.drawPolygon(localPolygon);
    paramGraphics.fillPolygon(localPolygon);
  }
  
  private void drawMovingDot()
  {
    int i = this.em / 2 + 1 + (int)((this.w + 0.0D) * (this.prevVal - this.min) / (this.max - this.min));
    Graphics localGraphics = getGraphics();
    localGraphics.setColor(Color.red);
    localGraphics.setXORMode(Color.green);
    if (this.which == -99)
    {
      i -= 1 - this.em / 2;
      localGraphics.drawPolygon(new int[] { i - this.em / 2, i, i + this.em / 2 }, new int[] { this.y + this.em, this.y, this.y + this.em }, 3);
    }
    else
    {
      localGraphics.drawOval(i, this.y - this.em, this.em - 2, this.em - 2);
    }
  }
  
  private void tempRescale(double paramDouble1, double paramDouble2)
  {
    while (paramDouble1 >= paramDouble2)
    {
      paramDouble1 -= 0.5D;
      paramDouble2 += 0.5D;
    }
    this.min = paramDouble1;
    this.max = paramDouble2;
    this.tick = Utility.nice(this.min, this.max, 5, false);
    this.tickLab = Utility.fmtNice(this.tick);
    repaint();
  }
  
  public void mousePressed(MouseEvent paramMouseEvent)
  {
    this.which = -1;
    double d = this.min + (this.max - this.min) * (paramMouseEvent.getX() - this.em + 0.0D) / this.w;
    if (paramMouseEvent.getY() < this.y)
    {
      for (int i = 0; (i < this.n) && (this.which < 0); i++) {
        if (2.0D * Math.abs(this.values[i] - d) < this.binW)
        {
          this.which = i;
          repaint();
          this.prevVal = this.values[i];
          drawMovingDot();
        }
      }
    }
    else if (2.0D * Math.abs(this.mean - d) < this.binW)
    {
      this.which = -99;
      repaint();
      this.prevVal = this.mean;
      drawMovingDot();
    }
  }
  
  public void mouseClicked(MouseEvent paramMouseEvent) {}
  
  public void mouseEntered(MouseEvent paramMouseEvent) {}
  
  public void mouseExited(MouseEvent paramMouseEvent) {}
  
  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    if (this.which == -99)
    {
      setCursor(this.pointer);
      this.which = -1;
      double d = this.binW * Math.round(this.prevVal / this.binW) - this.mean;
      for (int i = 0; i < this.values.length; i++) {
        this.values[i] += d;
      }
      setCursor(this.pointer);
      setRange();
      repaint();
      if (this.actionListener != null) {
        this.actionListener.actionPerformed(new ActionEvent(this, 1001, "Dotplot:Shift"));
      }
      return;
    }
    if (this.which < 0) {
      return;
    }
    this.values[this.which] = (this.binW * Math.round(this.prevVal / this.binW));
    this.which = -1;
    setCursor(this.pointer);
    setRange();
    repaint();
    if (this.actionListener != null) {
      this.actionListener.actionPerformed(new ActionEvent(this, 1001, "Dotplot:Point"));
    }
  }
  
  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    if ((this.which < 0) && (this.which != -99)) {
      return;
    }
    double d1 = this.min + (this.max - this.min) * (paramMouseEvent.getX() - this.em + 0.0D) / this.w;
    double d2 = 0.01D * (this.max - this.min);
    drawMovingDot();
    if (d1 < this.min + d2) {
      tempRescale(d1 - d2, this.max);
    }
    if (d1 > this.max - d2) {
      tempRescale(this.min, d1 + d2);
    }
    this.prevVal = d1;
    drawMovingDot();
  }
  
  public void mouseMoved(MouseEvent paramMouseEvent)
  {
    double d = this.min + (this.max - this.min) * (paramMouseEvent.getX() - this.em + 0.0D) / this.w;
    int i = -1;int j = paramMouseEvent.getY();
    Cursor localCursor = getCursor();
    if ((j >= this.y) && (j <= this.y + this.em))
    {
      if (2.0D * Math.abs(d - this.mean) < this.binW)
      {
        if (localCursor != this.crosshairs) {
          setCursor(this.crosshairs);
        }
        i = 99;
      }
    }
    else if ((j < this.y) && (j > this.y - this.em)) {
      for (int k = 0; (k < this.n) && (i < 0); k++) {
        if (2.0D * Math.abs(this.values[k] - d) < this.binW)
        {
          i = k;
          if (localCursor != this.finger) {
            setCursor(this.finger);
          }
        }
      }
    }
    if ((i < 0) && (localCursor != this.pointer)) {
      setCursor(this.pointer);
    }
  }
}
