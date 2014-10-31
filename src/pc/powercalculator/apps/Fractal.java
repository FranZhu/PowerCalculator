package pc.powercalculator.apps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import pc.awt.IntPlot;
import pc.awt.Plot;
import pc.powercalculator.PiPanel;
import pc.powercalculator.PowerCalculator;

public class Fractal
  extends PowerCalculator
{
  private static String title = "Interactive fractals";
  private int n = 0;
  private double[] x;
  private double[] y;
  private double[] patx;
  private double[] paty;
  private Plot outPlot;
  private IntPlot inPlot;
  public double recur;
  public double npat;
  
  public void gui()
  {
    this.patx = new double[] { 0.0D, 0.25D, 0.5D, 0.75D, 1.0D };
    this.paty = new double[] { 0.0D, 0.0D, 0.25D, 0.0D, 0.0D };
    this.inPlot = new IntPlot(this.patx, this.paty);
    this.inPlot.setLineMode(true);
    this.inPlot.setDotMode(true);
    this.inPlot.setAxisLabels(new String[] { "x" }, new String[] { "y" });
    this.inPlot.setSameScale(true);
    this.outPlot = new Plot(this.patx, this.paty);
    this.outPlot.setLineMode(true);
    this.outPlot.setDotMode(false);
    this.outPlot.setTickMode(true, true);
    this.outPlot.setAxisLabels(new String[] { "x" }, new String[] { "y" });
    this.outPlot.setLineColor(Color.orange.darker());
    this.outPlot.setSameScale(true);
    this.panel.setLayout(new BorderLayout());
    
    beginSubpanel(2);
    component("pattern_changed", this.inPlot);
    beginSubpanel(1, Color.blue);
    choice("recur", "Number of recursions", new double[] { 1.0D, 2.0D, 3.0D, 4.0D, 5.0D, 6.0D }, 2);
    choice("npat", "Number of points in pattern", new double[] { 3.0D, 4.0D, 5.0D, 6.0D, 7.0D, 8.0D, 9.0D, 10.0D }, 2);
    
    endSubpanel();
    this.panel.setStretchable(true);
    PiPanel localPiPanel = this.panel;
    endSubpanel();
    this.panel.add(localPiPanel, "North");
    this.panel.add(this.outPlot, "Center");
  }
  
  public void click()
  {
    doFractal((int)this.recur);
  }
  
  public void pattern_changed()
  {
    double[] arrayOfDouble1 = this.inPlot.getXData()[0];
    double[] arrayOfDouble2 = this.inPlot.getYData()[0];
    int i = this.patx.length - 1;
    if ((arrayOfDouble1[0] != 0.0D) || (arrayOfDouble2[0] != 0.0D) || (arrayOfDouble1[i] != 1.0D) || (arrayOfDouble2[i] != 0.0D))
    {
      double tmp78_77 = (this.paty[i] = 0.0D);this.paty[0] = tmp78_77;this.patx[0] = tmp78_77;
      this.patx[i] = 1.0D;
      this.inPlot.setData(this.patx, this.paty);
      return;
    }
    this.patx = arrayOfDouble1;
    this.paty = arrayOfDouble2;
    doFractal((int)this.recur);
  }
  
  public void npat_changed()
  {
    int i = (int)this.npat;
    int j = this.patx.length;
    double[] arrayOfDouble = this.paty;
    this.patx = new double[i];
    this.paty = new double[i];
    for (int k = 0; k < i; k++)
    {
      this.paty[k] = (k < j ? arrayOfDouble[k] : 0.0D);
      this.patx[k] = (k / (i - 1.0D));
    }
    this.paty[(i - 1)] = 0.0D;
    this.inPlot.setData(this.patx, this.paty);
    doFractal((int)this.recur);
  }
  
  public Fractal()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new Fractal();
  }
  
  void doFractal(int paramInt)
  {
    this.n = ((int)Math.pow(this.patx.length - 1, paramInt) + 1);
    this.x = new double[this.n];
    this.y = new double[this.n]; double 
      tmp51_50 = 0.0D;this.y[0] = tmp51_50;this.x[0] = tmp51_50;
    this.n = 1;
    makePath(new double[] { 0.0D, 0.0D }, new double[] { 1.0D, 0.0D }, paramInt);
    this.outPlot.setData(this.x, this.y);
  }
  
  void makePath(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2, int paramInt)
  {
    double[] arrayOfDouble1 = new double[2];double[] arrayOfDouble2 = new double[2];
    if (paramInt == 0)
    {
      this.x[this.n] = paramArrayOfDouble2[0];
      this.y[(this.n++)] = paramArrayOfDouble2[1];
      if (this.n >= this.x.length) {
        this.n -= 1;
      }
      return;
    }
    double d1 = paramArrayOfDouble2[0] - paramArrayOfDouble1[0];
    double d2 = paramArrayOfDouble2[1] - paramArrayOfDouble1[1];
    arrayOfDouble1[0] = paramArrayOfDouble1[0];
    arrayOfDouble1[1] = paramArrayOfDouble1[1];
    for (int i = 1; i < this.patx.length; i++)
    {
      arrayOfDouble2[0] = (paramArrayOfDouble1[0] + d1 * this.patx[i] - d2 * this.paty[i]);
      arrayOfDouble2[1] = (paramArrayOfDouble1[1] + d2 * this.patx[i] + d1 * this.paty[i]);
      makePath(arrayOfDouble1, arrayOfDouble2, paramInt - 1);
      arrayOfDouble1[0] = arrayOfDouble2[0];
      arrayOfDouble1[1] = arrayOfDouble2[1];
    }
  }
}
