package pc.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class GridLine
  extends Canvas
{
  int lineType;
  Color foreground = Color.black;
  public static final int HORIZ = 0;
  public static final int VERT = 1;
  public static final int CROSS = 2;
  
  public GridLine(int paramInt)
  {
    this.lineType = paramInt;
  }
  
  public GridLine(int paramInt, Color paramColor)
  {
    this.lineType = paramInt;
    this.foreground = paramColor;
  }
  
  public void paint(Graphics paramGraphics)
  {
    int i = size().width;int j = size().height;
    paramGraphics.setColor(this.foreground);
    if ((this.lineType == 0) || (this.lineType == 2)) {
      paramGraphics.drawLine(0, j / 2, i, j / 2);
    }
    if ((this.lineType == 1) || (this.lineType == 2)) {
      paramGraphics.drawLine(i / 2, 0, i / 2, j);
    }
  }
  
  public Dimension preferredSize()
  {
    return new Dimension(5, 5);
  }
}
