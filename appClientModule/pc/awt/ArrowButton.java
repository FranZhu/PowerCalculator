package pc.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Polygon;

public class ArrowButton
  extends Canvas
{
  private boolean upward;
  private boolean incFlag = false;
  private Polygon arrow;
  private int wid;
  private int ht;
  
  public ArrowButton(boolean paramBoolean, int paramInt1, int paramInt2)
  {
    this.upward = paramBoolean;
    this.wid = paramInt1;
    this.ht = paramInt2;
    resize(paramInt1, paramInt2);
  }
  
  public Dimension preferredSize()
  {
    return new Dimension(this.wid, this.ht);
  }
  
  public void resize(int paramInt1, int paramInt2)
  {
    super.resize(paramInt1, paramInt2);
    int i = size().width;int j = size().height;int k = i / 2;int m = i / 4;int n = j / 4;
    this.arrow = new Polygon();
    if (this.upward)
    {
      this.arrow.addPoint(m, j - n);
      this.arrow.addPoint(k, n);
      this.arrow.addPoint(i - m, j - n);
    }
    else
    {
      this.arrow.addPoint(m, n);
      this.arrow.addPoint(k, j - n);
      this.arrow.addPoint(i - m, n);
    }
  }
  
  public void draw(Graphics paramGraphics, boolean paramBoolean)
  {
    int i = size().width;int j = size().height;
    paramGraphics.setColor(Color.lightGray);
    paramGraphics.fill3DRect(0, 0, i, j, paramBoolean);
    paramGraphics.setColor(Color.black);
    paramGraphics.fillPolygon(this.arrow);
  }
  
  public void paint(Graphics paramGraphics)
  {
    draw(paramGraphics, true);
  }
  
  public boolean mouseDown(Event paramEvent, int paramInt1, int paramInt2)
  {
    draw(getGraphics(), false);
    this.incFlag = true;
    return true;
  }
  
  public boolean mouseExit(Event paramEvent, int paramInt1, int paramInt2)
  {
    draw(getGraphics(), true);
    this.incFlag = false;
    return true;
  }
  
  public boolean mouseUp(Event paramEvent, int paramInt1, int paramInt2)
  {
    draw(getGraphics(), true);
    if (this.incFlag) {
      deliverEvent(new Event(this, 1001, new String(this.upward ? "up" : "down")));
    }
    this.incFlag = false;
    return true;
  }
}
