package pc.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import pc.util.Utility;

public class Plot
  extends Component
{
  protected int defaultDotSize = 5;
  protected int[][] dotSize = { { this.defaultDotSize } };
  protected Color[] defaultColor = { Color.black, Color.blue, Color.red, Color.green.darker(), Color.orange, Color.darkGray, Color.red.darker(), Color.blue.darker() };
  protected Color scaleColor = Color.blue.darker();
  protected String[] yAxisLab = null;
  protected String[] xAxisLab = null;
  protected String[] title = null;
  protected boolean yCustomLab = false;
  protected boolean xCustomLab = false;
  protected boolean sameScale = false;
  protected boolean needsRescaling = true;
  protected boolean swap = false;
  protected double[][] x;
  protected double[][] y;
  protected Color[][] dotColor;
  protected Color[][] lineColor;
  protected double[] xTick;
  protected double[] yTick;
  protected String[] xTickLab;
  protected String[] yTickLab;
  protected double xMin;
  protected double xMax;
  protected double yMin;
  protected double yMax;
  protected double h0;
  protected double v0;
  protected double h1;
  protected double v1;
  protected double mh;
  protected double mv;
  protected double hMin;
  protected double hMax;
  protected double vMin;
  protected double vMax;
  
  public Plot(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    this(new double[][] { paramArrayOfDouble1 }, new double[][] { paramArrayOfDouble2 });
  }
  
  public Plot(double[] paramArrayOfDouble, double[][] paramArrayOfDouble1)
  {
    this(new double[][] { paramArrayOfDouble }, paramArrayOfDouble1);
  }
  
  public Plot(double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2)
  {
    setData(paramArrayOfDouble1, paramArrayOfDouble2);
    setDotMode(true);
    setLineMode(false);
    setBackground(Color.white);
    setFont(new Font("SansSerif", 0, 9));
  }
  
  public void setData(double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2, boolean paramBoolean)
  {
    this.x = paramArrayOfDouble1;
    this.y = paramArrayOfDouble2;
    this.needsRescaling = paramBoolean;
    repaint();
  }
  
  public void setData(double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2)
  {
    setData(paramArrayOfDouble1, paramArrayOfDouble2, true);
  }
  
  public void setData(double[] paramArrayOfDouble, double[][] paramArrayOfDouble1, boolean paramBoolean)
  {
    setData(new double[][] { paramArrayOfDouble }, paramArrayOfDouble1, paramBoolean);
  }
  
  public void setData(double[] paramArrayOfDouble, double[][] paramArrayOfDouble1)
  {
    setData(new double[][] { paramArrayOfDouble }, paramArrayOfDouble1, true);
  }
  
  public void setData(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, boolean paramBoolean)
  {
    setData(new double[][] { paramArrayOfDouble1 }, new double[][] { paramArrayOfDouble2 }, paramBoolean);
  }
  
  public void setData(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    setData(new double[][] { paramArrayOfDouble1 }, new double[][] { paramArrayOfDouble2 }, true);
  }
  
  public double[][] getXData()
  {
    return this.x;
  }
  
  public void setXData(double[][] paramArrayOfDouble, boolean paramBoolean)
  {
    this.x = paramArrayOfDouble;
    this.needsRescaling = paramBoolean;
    repaint();
  }
  
  public void setXData(double[][] paramArrayOfDouble)
  {
    setXData(paramArrayOfDouble, true);
  }
  
  public double[][] getYData()
  {
    return this.y;
  }
  
  public void setYData(double[][] paramArrayOfDouble, boolean paramBoolean)
  {
    this.y = paramArrayOfDouble;
    this.needsRescaling = paramBoolean;
    repaint();
  }
  
  public void setYData(double[][] paramArrayOfDouble)
  {
    setYData(paramArrayOfDouble, true);
  }
  
  public void setDotMode(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      setDotSize(new int[] { this.defaultDotSize });
      setDotColor(this.defaultColor);
    }
    else
    {
      setDotSize((int[][])null);
    }
  }
  
  public void setLineMode(boolean paramBoolean)
  {
    if (paramBoolean) {
      setLineColor(this.defaultColor);
    } else {
      setLineColor((Color[][])null);
    }
  }
  
  public void setDotColor(Color paramColor)
  {
    this.dotColor = new Color[][] { { paramColor } };
  }
  
  public void setDotColor(Color[] paramArrayOfColor)
  {
    this.dotColor = new Color[paramArrayOfColor.length][1];
    for (int i = 0; i < paramArrayOfColor.length; i++) {
      this.dotColor[i][0] = paramArrayOfColor[i];
    }
  }
  
  public void setDotColor(Color[][] paramArrayOfColor)
  {
    this.dotColor = paramArrayOfColor;
  }
  
  public void setDotSize(int paramInt)
  {
    this.dotSize = new int[][] { { paramInt } };
  }
  
  public void setDotSize(int[] paramArrayOfInt)
  {
    this.dotSize = new int[paramArrayOfInt.length][1];
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      this.dotSize[i][0] = paramArrayOfInt[i];
    }
  }
  
  public void setDotSize(int[][] paramArrayOfInt)
  {
    this.dotSize = paramArrayOfInt;
  }
  
  public void setLineColor(Color paramColor)
  {
    this.lineColor = new Color[][] { { paramColor } };
  }
  
  public void setLineColor(Color[] paramArrayOfColor)
  {
    this.lineColor = new Color[paramArrayOfColor.length][1];
    for (int i = 0; i < paramArrayOfColor.length; i++) {
      this.lineColor[i][0] = paramArrayOfColor[i];
    }
  }
  
  public void setLineColor(Color[][] paramArrayOfColor)
  {
    this.lineColor = paramArrayOfColor;
  }
  
  public void setDefaultColor(Color[] paramArrayOfColor)
  {
    this.defaultColor = paramArrayOfColor;
  }
  
  public void setDefaultDotSize(int paramInt)
  {
    this.defaultDotSize = paramInt;
  }
  
  public void setScaleColor(Color paramColor)
  {
    this.scaleColor = paramColor;
  }
  
  public void setTranspose(boolean paramBoolean)
  {
    this.swap = paramBoolean;
    this.needsRescaling = true;
  }
  
  public void setSameScale(boolean paramBoolean)
  {
    this.sameScale = paramBoolean;
  }
  
  public void setAxisLabels(String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    this.xAxisLab = paramArrayOfString1;
    this.yAxisLab = paramArrayOfString2;
  }
  
  public void setAxisLabels(String paramString1, String paramString2)
  {
    setAxisLabels(new String[] { paramString1 }, new String[] { paramString2 });
  }
  
  public void setTitle(String[] paramArrayOfString)
  {
    this.title = paramArrayOfString;
  }
  
  public void setTitle(String paramString)
  {
    setTitle(new String[] { paramString });
  }
  
  public void setTickMode(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.xCustomLab = (!paramBoolean1);
    this.yCustomLab = (!paramBoolean2);
    if (this.xCustomLab)
    {
      this.xTick = null;
      this.xTickLab = null;
    }
    if (this.yCustomLab)
    {
      this.yTick = null;
      this.yTickLab = null;
    }
  }
  
  public void setXTicks(double[] paramArrayOfDouble, String[] paramArrayOfString)
  {
    this.xTick = paramArrayOfDouble;
    this.xTickLab = paramArrayOfString;
    this.xCustomLab = true;
  }
  
  public void setYTicks(double[] paramArrayOfDouble, String[] paramArrayOfString)
  {
    this.yTick = paramArrayOfDouble;
    this.yTickLab = paramArrayOfString;
    this.yCustomLab = true;
  }
  
  public void paint(Graphics paramGraphics)
  {
    paramGraphics.setColor(getBackground());
    paramGraphics.fillRect(0, 0, getSize().width, getSize().height);
    if (this.needsRescaling) {
      rescale();
    }
    setScales();
    drawBox(paramGraphics);
    if (this.swap)
    {
      drawVAxis(paramGraphics, this.xTick, this.xTickLab, this.xAxisLab);
      drawHAxis(paramGraphics, this.yTick, this.yTickLab, this.yAxisLab);
      drawLines(paramGraphics, this.y, this.x);
      drawDots(paramGraphics, this.y, this.x);
    }
    else
    {
      drawVAxis(paramGraphics, this.yTick, this.yTickLab, this.yAxisLab);
      drawHAxis(paramGraphics, this.xTick, this.xTickLab, this.xAxisLab);
      drawLines(paramGraphics, this.x, this.y);
      drawDots(paramGraphics, this.x, this.y);
    }
  }
  
  protected boolean isBad(double paramDouble)
  {
    return (Double.isNaN(paramDouble)) || (Double.isInfinite(paramDouble));
  }
  
  protected void rescale()
  {
    this.xMin = (this.yMin = (1.0D / 0.0D));
    this.xMax = (this.yMax = (-1.0D / 0.0D));
    for (int i = 0; i < this.x.length; i++) {
      for (int j = 0; j < this.x[i].length; j++)
      {
        double d2 = this.x[i][j];
        if (!isBad(d2))
        {
          this.xMax = Math.max(this.xMax, d2);
          this.xMin = Math.min(this.xMin, d2);
        }
      }
    }
    if ((Double.isInfinite(this.xMin)) || (Double.isInfinite(this.xMax))) {
      this.xMin = (this.xMax = 0.0D);
    }
    if (this.xMin >= this.xMax)
    {
      this.xMin -= 0.5D;
      this.xMax += 0.5D;
    }
    double d1 = 0.05D * (this.xMax - this.xMin);
    this.xMax += d1;
    this.xMin -= d1;
    for (int k = 0; k < this.y.length; k++) {
      for (int m = 0; m < this.y[k].length; m++)
      {
        double d3 = this.y[k][m];
        if (!isBad(d3))
        {
          this.yMax = Math.max(this.yMax, d3);
          this.yMin = Math.min(this.yMin, d3);
        }
      }
    }
    if ((Double.isInfinite(this.yMin)) || (Double.isInfinite(this.yMax))) {
      this.yMin = (this.yMax = 0.0D);
    }
    if (this.yMin >= this.yMax)
    {
      this.yMin -= 0.5D;
      this.yMax += 0.5D;
    }
    d1 = 0.05D * (this.yMax - this.yMin);
    this.yMax += d1;
    this.yMin -= d1;
    if (!this.xCustomLab)
    {
      this.xTick = Utility.nice(this.xMin, this.xMax, 5, false);
      this.xTickLab = Utility.fmtNice(this.xTick);
    }
    if (!this.yCustomLab)
    {
      this.yTick = Utility.nice(this.yMin, this.yMax, 5, false);
      this.yTickLab = Utility.fmtNice(this.yTick);
    }
    this.needsRescaling = false;
  }
  
  private void setScales()
  {
    Dimension localDimension = getSize();
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    int i = localFontMetrics.getHeight();
    if (this.sameScale)
    {
      double d1 = this.swap ? this.yMax - this.yMin : this.xMax - this.xMin;
      double d2 = this.swap ? this.xMax - this.xMin : this.yMax - this.yMin;
      double d3 = localDimension.width;double d4 = localDimension.height;
      double d5;
      double d6;
      boolean bool;
      if (d2 / d4 < d1 / d3)
      {
        d5 = d4 * d1 / d3;
        d6 = 0.5D * (d5 - d2);
        bool = this.swap;
      }
      else
      {
        d5 = d3 * d2 / d4;
        d6 = 0.5D * (d5 - d1);
        bool = !this.swap;
      }
      if (bool)
      {
        this.xMin -= d6;
        this.xMax += d6;
        if (!this.xCustomLab)
        {
          this.xTick = Utility.nice(this.xMin, this.xMax, 5, false);
          this.xTickLab = Utility.fmtNice(this.xTick);
        }
      }
      else
      {
        this.yMin -= d6;
        this.yMax += d6;
        if (!this.yCustomLab)
        {
          this.yTick = Utility.nice(this.yMin, this.yMax, 5, false);
          this.yTickLab = Utility.fmtNice(this.yTick);
        }
      }
    }
    int k;
    int j;
    if (this.swap)
    {
      this.hMin = this.yMin;
      this.hMax = this.yMax;
      this.vMin = this.xMin;
      this.vMax = this.xMax;
      k = this.yTickLab == null ? 0 : i;
      if (this.yAxisLab != null) {
        k += i * this.yAxisLab.length;
      }
      j = this.xTickLab == null ? 0 : i;
      if (this.xAxisLab != null) {
        j += i * this.xAxisLab.length;
      }
    }
    else
    {
      this.hMin = this.xMin;
      this.hMax = this.xMax;
      this.vMin = this.yMin;
      this.vMax = this.yMax;
      k = this.xTickLab == null ? 0 : i;
      if (this.xAxisLab != null) {
        k += i * this.xAxisLab.length;
      }
      j = this.yTickLab == null ? 0 : i;
      if (this.yAxisLab != null) {
        j += i * this.yAxisLab.length;
      }
    }
    int m = this.title == null ? -i / 2 : i * this.title.length;
    this.v0 = (i + m);
    this.v1 = (localDimension.height - i - k);
    this.h0 = (i + j);
    this.h1 = (localDimension.width - i / 2);
    
    this.mh = ((this.h1 - this.h0) / (this.hMax - this.hMin));
    this.mv = ((this.v1 - this.v0) / (this.vMax - this.vMin));
  }
  
  private void drawBox(Graphics paramGraphics)
  {
    paramGraphics.setColor(this.scaleColor);
    paramGraphics.drawRect((int)this.h0, (int)this.v0, (int)(this.h1 - this.h0), (int)(this.v1 - this.v0));
  }
  
  private void drawHAxis(Graphics paramGraphics, double[] paramArrayOfDouble, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if ((paramArrayOfDouble == null) && (paramArrayOfString2 == null)) {
      return;
    }
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    int j = localFontMetrics.getHeight();
    int k = (int)this.v1;int m = k + j / 3;
    int n = (int)(this.v1 + 1.25D * j);
    paramGraphics.setColor(this.scaleColor);
    paramGraphics.setFont(getFont());
    int i, i1;
    if (paramArrayOfDouble != null) {
      for (i1 = 0; i1 < paramArrayOfDouble.length; i1++)
      {
        i = (int)(this.h0 + this.mh * (paramArrayOfDouble[i1] - this.hMin));
        paramGraphics.drawLine(i, k, i, m);
        if (paramArrayOfString1 != null)
        {
          i -= localFontMetrics.stringWidth(paramArrayOfString1[i1]) / 2;
          paramGraphics.drawString(paramArrayOfString1[i1], i, n);
        }
      }
    } else {
      n -= j;
    }
    if (paramArrayOfString2 != null)
    {
      n += j / 2 - localFontMetrics.getDescent();
      for (i1 = 0; i1 < paramArrayOfString2.length; i1++)
      {
        n += j;
        String str1 = paramArrayOfString2[i1];
        int i3 = str1.toCharArray()[0];
        switch (i3)
        {
        case 62: 
          str1 = str1.substring(1);
          i = (int)(this.h1 - localFontMetrics.stringWidth(str1));
          break;
        case 60: 
          str1 = str1.substring(1);
          i = (int)this.h0;
          break;
        default: 
          i = (int)(0.5D * (this.h0 + this.h1 - localFontMetrics.stringWidth(str1)));
        }
        paramGraphics.drawString(str1, i, n);
      }
    }
    if (this.title == null) {
      return;
    }
    n = j;
    i1 = getSize().width;
    for (int i2 = 0; i2 < this.title.length; i2++)
    {
      String str2 = this.title[i2];
      int i4 = str2.toCharArray()[0];
      switch (i4)
      {
      case 62: 
        str2 = str2.substring(1);
        i = i1 - localFontMetrics.stringWidth(str2) - j / 2;
        break;
      case 60: 
        str2 = str2.substring(1);
        i = j / 2;
        break;
      default: 
        i = (i1 - localFontMetrics.stringWidth(str2)) / 2;
      }
      paramGraphics.drawString(str2, i, n);
      n += j;
    }
  }
  
  private void drawVAxis(Graphics paramGraphics, double[] paramArrayOfDouble, String[] paramArrayOfString1, String[] paramArrayOfString2)
  {
    if ((paramArrayOfDouble == null) && (paramArrayOfString2 == null)) {
      return;
    }
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    int j = localFontMetrics.getHeight();
    int k = (int)this.h0;int m = k - j / 3;
    int n = (int)(this.h0 - 0.5D * j);
    int i1 = (int)this.h0;int i2 = getSize().height;
    double d1 = i2 - this.v1;double d2 = i2 - this.v0;
    
    Image localImage = createImage(i2, i1);
    Graphics localGraphics = localImage.getGraphics();
    localGraphics.setColor(getBackground());
    localGraphics.fillRect(0, 0, i2, i1);
    localGraphics.setColor(this.scaleColor);
    localGraphics.setFont(getFont());
    int i, i3;
    if (paramArrayOfDouble != null) {
      for (i3 = 0; i3 < paramArrayOfDouble.length; i3++)
      {
        i = (int)(d1 + this.mv * (paramArrayOfDouble[i3] - this.vMin));
        localGraphics.drawLine(i, k, i, m);
        if (paramArrayOfString1 != null)
        {
          i -= localFontMetrics.stringWidth(paramArrayOfString1[i3]) / 2;
          localGraphics.drawString(paramArrayOfString1[i3], i, n);
        }
      }
    } else {
      n += j;
    }
    if (paramArrayOfString2 == null) {
      return;
    }
    n -= j;
    String localObject;
    for (i3 = 0; i3 < paramArrayOfString2.length; i3++)
    {
      localObject = paramArrayOfString2[i3];
      int i4 = localObject.toCharArray()[0];
      switch (i4)
      {
      case 62: 
        localObject = ((String)localObject).substring(1);
        i = (int)(d2 - localFontMetrics.stringWidth((String)localObject));
        break;
      case 60: 
        localObject = ((String)localObject).substring(1);
        i = (int)d1;
        break;
      default: 
        i = (int)(0.5D * (d1 + d2 - localFontMetrics.stringWidth((String)localObject)));
      }
      localGraphics.drawString((String)localObject, i, n);
      if (i3 == 0) {
        n = j;
      } else {
        n += j;
      }
    }
    localGraphics.dispose();
    PixelGrabber localPixelGrabber = new PixelGrabber(localImage, 0, 0, -1, -1, true);
    try
    { 
      if (localPixelGrabber.grabPixels())
      { int[] localObject1;
        localObject1 = (int[])localPixelGrabber.getPixels();
        int[] arrayOfInt = new int[localObject1.length];
        int i5 = i2 - 1;int i6 = i1 - 1;
        for (int i7 = 0; i7 < i2; i7++) {
          for (int i8 = 0; i8 < i1; i8++) {
            arrayOfInt[(i8 + i1 * (i5 - i7))] = localObject1[(i7 + i2 * i8)];
          }
        }
        localImage = createImage(new MemoryImageSource(i1, i2, arrayOfInt, 0, i1));
        paramGraphics.drawImage(localImage, 0, 0, null);
      }
    }
    catch (InterruptedException localInterruptedException) {}
  }
  
  private void drawLines(Graphics paramGraphics, double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2)
  {
    if (this.lineColor == null) {
      return;
    }
    int i = paramArrayOfDouble1.length;int j = paramArrayOfDouble2.length;
    int k = this.lineColor.length;int m = Math.max(i, j);
    for (int n = 0; n < m; n++) {
      drawLines(paramGraphics, paramArrayOfDouble1[(n % i)], paramArrayOfDouble2[(n % j)], this.lineColor[(n % k)]);
    }
  }
  
  private void drawLines(Graphics paramGraphics, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, Color[] paramArrayOfColor)
  {
    if (paramArrayOfColor == null) {
      return;
    }
    int i = paramArrayOfDouble1.length;int j = paramArrayOfDouble2.length;int k = paramArrayOfColor.length;int m = Math.max(i, j);
    int n = 0;int i1 = 0;
    int i4 = 0;
    for (int i5 = 0; i5 < m; i5++)
    {
      double d1 = paramArrayOfDouble1[(i5 % i)];double d2 = paramArrayOfDouble2[(i5 % j)];
      if ((isBad(d1)) || (isBad(d2)))
      {
        i4 = 0;
      }
      else
      {
        int i2 = (int)(this.h0 + this.mh * (d1 - this.hMin));
        int i3 = (int)(this.v0 + this.mv * (this.vMax - d2));
        if (i4 != 0) {
          paramGraphics.drawLine(n, i1, i2, i3);
        }
        n = i2;
        i1 = i3;
        Color localColor = paramArrayOfColor[(i5 % k)];
        if (localColor == null)
        {
          i4 = 0;
        }
        else
        {
          paramGraphics.setColor(localColor);
          i4 = 1;
        }
      }
    }
  }
  
  private void drawDots(Graphics paramGraphics, double[][] paramArrayOfDouble1, double[][] paramArrayOfDouble2)
  {
    if ((this.dotColor == null) || (this.dotSize == null)) {
      return;
    }
    int i = paramArrayOfDouble1.length;int j = paramArrayOfDouble2.length;int k = this.dotSize.length;
    int m = this.dotColor.length;int n = Math.max(i, j);
    for (int i1 = 0; i1 < n; i1++) {
      drawDots(paramGraphics, paramArrayOfDouble1[(i1 % i)], paramArrayOfDouble2[(i1 % j)], this.dotColor[(i1 % m)], this.dotSize[(i1 % k)]);
    }
  }
  
  private void drawDots(Graphics paramGraphics, double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, Color[] paramArrayOfColor, int[] paramArrayOfInt)
  {
    if ((paramArrayOfColor == null) || (paramArrayOfInt == null)) {
      return;
    }
    int i = paramArrayOfDouble1.length;int j = paramArrayOfDouble2.length;int k = paramArrayOfColor.length;int m = paramArrayOfInt.length;
    int n = Math.max(i, j);
    for (int i5 = 0; i5 < n; i5++)
    {
      int i3 = paramArrayOfInt[(i5 % m)];
      Color localColor = paramArrayOfColor[(i5 % k)];
      if ((i3 > 0) && (localColor != null))
      {
        double d1 = paramArrayOfDouble1[(i5 % i)];double d2 = paramArrayOfDouble2[(i5 % j)];
        if ((!isBad(d1)) && (!isBad(d2)))
        {
          int i4 = i3 / 2;
          int i1 = (int)(this.h0 - i4 + this.mh * (d1 - this.hMin));
          int i2 = (int)(this.v0 - i4 + this.mv * (this.vMax - d2));
          paramGraphics.setColor(localColor);
          paramGraphics.drawOval(i1, i2, i3, i3);
          paramGraphics.fillOval(i1, i2, i3, i3);
        }
      }
    }
  }
  
  public Dimension getPreferredSize()
  {
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    int i = localFontMetrics.stringWidth("m");
    int j = localFontMetrics.getAscent();
    return new Dimension(24 * i, 16 * j);
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
}
