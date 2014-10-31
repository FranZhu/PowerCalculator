package pc.awt;

import java.awt.AWTEventMulticaster;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class IntPlot
  extends Plot
  implements MouseListener, MouseMotionListener
{
  private transient ActionListener actionListener = null;
  private boolean isMoving = false;
  private boolean enableMoves = true;
  private boolean constrainX = false;
  private boolean constrainY = false;
  private int[] index;
  private double[] point;
  private Cursor finger = Cursor.getPredefinedCursor(12);
  private Cursor pointer = Cursor.getDefaultCursor();
  
  public IntPlot(double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2)
  {
    super(paramArrayOfDouble1, paramArrayOfDouble2);
    addMouseListener(this);
    addMouseMotionListener(this);
  }
  
  public IntPlot(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    this(new double[][] { paramArrayOfDouble1 }, new double[][] { paramArrayOfDouble2 });
  }
  
  public IntPlot(double[] paramArrayOfDouble, double[][] paramArrayOfDouble1)
  {
    this(new double[][] { paramArrayOfDouble }, paramArrayOfDouble1);
  }
  
  public void setMoveable(boolean paramBoolean)
  {
    this.enableMoves = paramBoolean;
    this.isMoving = false;
  }
  
  public boolean isMoveable()
  {
    return this.enableMoves;
  }
  
  public void setConstraints(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.constrainX = paramBoolean1;
    this.constrainY = paramBoolean2;
    this.enableMoves = (!(paramBoolean1 & paramBoolean2));
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public void update(Graphics paramGraphics)
  {
    Dimension localDimension = getSize();
    Image localImage = createImage(localDimension.width, localDimension.height);
    Graphics localGraphics = localImage.getGraphics();
    super.paint(localGraphics);
    localGraphics.dispose();
    paramGraphics.drawImage(localImage, 0, 0, this);
  }
  
  protected int[] locate(int paramInt1, int paramInt2, int paramInt3)
  {
    double[][] arrayOfDouble1 = this.swap ? this.y : this.x;
    double[][] arrayOfDouble2 = this.swap ? this.x : this.y;
    double d1 = this.hMin + (paramInt1 - this.h0) / this.mh;
    double d2 = this.vMax - (paramInt2 - this.v0) / this.mv;
    double d3 = paramInt3 / this.mh;
    double d4 = paramInt3 / this.mv;
    int i = arrayOfDouble1.length;int j = arrayOfDouble2.length;int k = Math.max(i, j);
    for (int m = 0; m < k; m++)
    {
      int n = m % i;int i1 = m % j;
      double[] arrayOfDouble3 = arrayOfDouble1[n];double[] arrayOfDouble4 = arrayOfDouble2[i1];
      int i2 = arrayOfDouble3.length;int i3 = arrayOfDouble4.length;int i4 = Math.max(i2, i3);
      for (int i5 = 0; i5 < i4; i5++)
      {
        int i6 = i5 % i2;int i7 = i5 % i3;
        if ((Math.abs(d1 - arrayOfDouble3[i6]) < d3) && (Math.abs(d2 - arrayOfDouble4[i7]) < d4)) {
        	
        	int[] int_array = new int[] { i7, i1, i6, n };
        	if (this.swap) return int_array;
        	else return new int[] { i6, n, i7, i1 };
          //return new int[] { i6, n, i7, this.swap ? new int[] { i7, i1, i6, n } : i1 };
        }
      }
    }
    return null;
  }
  
  private void doAction(String paramString)
  {
    if (this.actionListener != null) {
      this.actionListener.actionPerformed(new ActionEvent(this, 1001, paramString));
    }
  }
  
  public void mouseClicked(MouseEvent paramMouseEvent) {}
  
  public void mouseEntered(MouseEvent paramMouseEvent) {}
  
  public void mouseExited(MouseEvent paramMouseEvent) {}
  
  public void mousePressed(MouseEvent paramMouseEvent)
  {
    if (!this.enableMoves) {
      return;
    }
    this.index = locate(paramMouseEvent.getX(), paramMouseEvent.getY(), 5);
    if (this.index == null) {
      return;
    }
    this.point = new double[] { this.x[this.index[1]][this.index[0]], this.y[this.index[3]][this.index[2]] };
    this.isMoving = true;
    setCursor(this.finger);
  }
  
  public void mouseReleased(MouseEvent paramMouseEvent)
  {
    setCursor(this.pointer);
    this.isMoving = false;
    this.needsRescaling = true;
    doAction("IntPlot");
    repaint();
  }
  
  public void mouseMoved(MouseEvent paramMouseEvent) {}
  
  public void mouseDragged(MouseEvent paramMouseEvent)
  {
    if (!this.isMoving) {
      return;
    }
    double d1 = this.hMin + (paramMouseEvent.getX() - this.h0) / this.mh;
    double d2 = this.vMax - (paramMouseEvent.getY() - this.v0) / this.mv;
    this.needsRescaling = ((d1 < this.hMin) || (d1 > this.hMax) || (d2 < this.vMin) || (d2 > this.vMax));
    if (this.swap)
    {
      if (!this.constrainX) {
        this.x[this.index[1]][this.index[0]] = d2;
      }
      if (!this.constrainY) {
        this.y[this.index[3]][this.index[2]] = d1;
      }
    }
    else
    {
      if (!this.constrainX) {
        this.x[this.index[1]][this.index[0]] = d1;
      }
      if (!this.constrainY) {
        this.y[this.index[3]][this.index[2]] = d2;
      }
    }
    update(getGraphics());
  }
}
