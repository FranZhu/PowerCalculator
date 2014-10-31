package pc.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;
import java.util.Vector;

public class RVLayout
  implements LayoutManager, Serializable
{
  private int nhg = 0;
  private int[] vGlue = new int[20];
  private int hGap = 6;
  private int vGap = 6;
  private int nvg = 0;
  private int[] hGlue = new int[20];
  private boolean sizesSet = false;
  private boolean stretchCols = false;
  private boolean stretchRows = false;
  private Vector extras = new Vector();
  private int rows;
  private int cols;
  private int[] rowHgt;
  private int[] colWid;
  
  public RVLayout()
  {
    this(2);
  }
  
  public RVLayout(int paramInt)
  {
    this.cols = paramInt;
  }
  
  public RVLayout(int paramInt1, int paramInt2, int paramInt3)
  {
    this.cols = paramInt1;
    this.hGap = paramInt2;
    this.vGap = paramInt3;
  }
  
  public RVLayout(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.cols = paramInt;
    this.stretchRows = paramBoolean1;
    this.stretchCols = paramBoolean2;
  }
  
  public RVLayout(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.cols = paramInt1;
    this.hGap = paramInt2;
    this.vGap = paramInt3;
    this.stretchRows = paramBoolean1;
    this.stretchCols = paramBoolean2;
  }
  
  public void setHgap(int paramInt)
  {
    this.hGap = paramInt;
  }
  
  public void setVgap(int paramInt)
  {
    this.vGap = paramInt;
  }
  
  public int getHgap()
  {
    return this.hGap;
  }
  
  public int getVgap()
  {
    return this.vGap;
  }
  
  public void setStretchable(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.stretchRows = paramBoolean1;
    this.stretchCols = paramBoolean2;
  }
  
  public boolean[] isStretchable()
  {
    return new boolean[] { this.stretchRows, this.stretchCols };
  }
  
  public void setColWidth(int paramInt1, int paramInt2)
  {
    this.extras.addElement(new Dimension(-paramInt1, paramInt2));
    this.sizesSet = false;
  }
  
  public void setRowHeight(int paramInt1, int paramInt2)
  {
    this.extras.addElement(new Dimension(paramInt1, paramInt2));
    this.sizesSet = false;
  }
  
  public void addLayoutComponent(String paramString, Component paramComponent) {}
  
  public void removeLayoutComponent(Component paramComponent) {}
  
  public Dimension preferredLayoutSize(Container paramContainer)
  {
    Insets localInsets = paramContainer.insets();
    if (!this.sizesSet) {
      setSizes(paramContainer);
    }
    int i = localInsets.left + localInsets.right + this.cols * this.hGap;
    int j = localInsets.top + localInsets.bottom + this.rows * this.vGap;
    for (int k = 0; k < this.rows; j += this.rowHgt[(k++)]) {}
    for (int k = 0; k < this.cols; i += this.colWid[(k++)]) {}
    return new Dimension(i, j);
  }
  
  public Dimension minimumLayoutSize(Container paramContainer)
  {
    return preferredLayoutSize(paramContainer);
  }
  
  public void layoutContainer(Container paramContainer)
  {
    if (!this.sizesSet) {
      setSizes(paramContainer);
    }
    Dimension localDimension1 = preferredLayoutSize(paramContainer);
    
    int[] arrayOfInt1 = new int[this.cols];int[] arrayOfInt2 = new int[this.cols];
    double d = this.stretchCols ? (0.0D + paramContainer.size().width - this.cols * this.hGap) / (localDimension1.width - this.cols * this.hGap) : 1.0D;
    for (int k = 0; k < this.cols; k++)
    {
      arrayOfInt1[k] = ((int)(d * this.colWid[k]));
      arrayOfInt2[k] = 0;
    }
    int[] arrayOfInt3 = new int[this.rows];int[] arrayOfInt4 = new int[this.rows];
    d = this.stretchRows ? (0.0D + paramContainer.size().height - this.rows * this.vGap) / (localDimension1.height - this.rows * this.vGap) : 1.0D;
    for (int m = 0; m < this.rows; m++)
    {
      arrayOfInt3[m] = ((int)(d * this.rowHgt[m]));
      arrayOfInt4[m] = 0;
    }
    Dimension localDimension2 = preferredLayoutSize(paramContainer);
    int n;
    if ((this.nhg > 0) && (!this.stretchCols))
    {
      n = (paramContainer.size().width - localDimension2.width) / this.nhg;
      if (n > 0) {
        for (int i1 = 0; i1 < this.nhg; i1++) {
          if (this.hGlue[i1] < this.cols) {
            arrayOfInt2[this.hGlue[i1]] = n;
          }
        }
      }
    }
    if ((this.nvg > 0) && (!this.stretchRows))
    {
      n = (paramContainer.size().height - localDimension2.height) / this.nvg;
      if (n > 0) {
        for (int i1 = 0; i1 < this.nvg; i1++) {
          if (this.vGlue[i1] < this.rows) {
            arrayOfInt4[this.vGlue[i1]] = n;
          }
        }
      }
    }
    Insets localInsets = paramContainer.insets();
    int j = localInsets.top + this.vGap / 2;
    
    int i1 = paramContainer.countComponents();
    int i2 = 0;
    for (int i3 = 0; i2 < this.rows; i2++)
    {
      j += arrayOfInt4[i2];
      int i = localInsets.left + this.hGap / 2;
      for (int i4 = 0; i4 < this.cols; i4++)
      {
        i += arrayOfInt2[i4];
        Component localComponent = paramContainer.getComponent(i3);
        localComponent.reshape(i, j, arrayOfInt1[i4], arrayOfInt3[i2]);
        i += arrayOfInt1[i4] + this.hGap;
        i3++;
        if (i3 >= i1) {
          return;
        }
      }
      j += arrayOfInt3[i2] + this.vGap;
    }
  }
  
  private void setSizes(Container paramContainer)
  {
	int j;
    int i = paramContainer.countComponents();
    this.rows = ((i + this.cols - 1) / this.cols);
    this.colWid = new int[this.cols];
    this.rowHgt = new int[this.rows];
    for (j = 0; j < this.cols; this.colWid[(j++)] = 0) {}
    for (j = 0; j < this.rows; this.rowHgt[(j++)] = 0) {}
    j = 0;int k = 0;
    for (int m = 0; j < i; j++)
    {
      Dimension localDimension2 = paramContainer.getComponent(j).preferredSize();
      this.colWid[m] = Math.max(this.colWid[m], localDimension2.width);
      this.rowHgt[k] = Math.max(this.rowHgt[k], localDimension2.height);
      m++;
      if (m >= this.cols)
      {
        k++;
        m = 0;
      }
    }
    for (j = 0; j < this.extras.size(); j++)
    {
      Dimension localDimension1 = (Dimension)this.extras.elementAt(j);
      if (localDimension1.width > 0) {
        this.rowHgt[localDimension1.width] = localDimension1.height;
      } else {
        this.colWid[(-localDimension1.width)] = localDimension1.height;
      }
    }
  }
  
  public void horzFill(Container paramContainer)
  {
    this.hGlue[(this.nhg++)] = (paramContainer.countComponents() % this.cols);
  }
  
  public void vertFill(Container paramContainer)
  {
    this.vGlue[(this.nvg++)] = (paramContainer.countComponents() / this.cols);
  }
}
