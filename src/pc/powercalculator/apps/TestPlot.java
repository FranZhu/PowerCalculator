package pc.powercalculator.apps;

import pc.awt.IntPlot;
import pc.awt.Plot;
import pc.powercalculator.PiPanel;
import pc.powercalculator.PowerCalculator;

public class TestPlot
  extends PowerCalculator
{
  private static String title = "Test of Plot";
  private IntPlot plot;
  private String[] factorName;
  public int adjust;
  
  public void gui()
  {
    this.factorName = new String[] { "Variety", "Fertilizer" };
    this.panel.setStretchable(true);
    double[] arrayOfDouble1 = { 1.0D, 2.0D, 3.0D, 4.0D, 5.0D };
    double[] arrayOfDouble2 = { -2.0D, -1.0D, 0, 1.0D, 2.0D };
    double[] arrayOfDouble3 = new double[5];
    double[] arrayOfDouble4 = { 2.0D, 1.0D, 0, -1.0D, -2.0D };
    double[][] arrayOfDouble = { arrayOfDouble2, arrayOfDouble3, arrayOfDouble4 };
    this.plot = new IntPlot(arrayOfDouble1, arrayOfDouble);
    component("plot_changed", this.plot);
    this.plot.setConstraints(true, false);
    this.plot.setLineMode(true);
    this.plot.setDotMode(true);
    this.plot.setTickMode(false, true);
    this.plot.setTitle(">(Drag dots to modify)");
    labelAxes();
    beginSubpanel(2);
    checkbox("adjust", "Remove main effects", 1);
    button("swapFac", "Switch factors");
    endSubpanel();
  }
  
  public void click()
  {
    if (this.adjust == 1) {
      adjustMEs();
    }
  }
  
  public void swapFac()
  {
    String str = this.factorName[0];
    this.factorName[0] = this.factorName[1];
    this.factorName[1] = str;
    labelAxes();
    double[][] arrayOfDouble1 = this.plot.getYData();
    int i = arrayOfDouble1.length;int j = arrayOfDouble1[0].length;
    double[][] arrayOfDouble2 = new double[j][i];double[] arrayOfDouble = new double[i];
    for (int k = 0; k < i; k++)
    {
      arrayOfDouble[k] = k;
      for (int m = 0; m < j; m++) {
        arrayOfDouble2[m][k] = arrayOfDouble1[k][m];
      }
    }
    this.plot.setData(arrayOfDouble, arrayOfDouble2);
  }
  
  private void labelAxes()
  {
    this.plot.setAxisLabels(
      new String[] { "Levels of " + this.factorName[0], 
      ">Profiles: " + this.factorName[1] }, 
      new String[] { "Response" });
  }
  
  public void adjustMEs()
  {
    double[][] arrayOfDouble = this.plot.getYData();
    int i = arrayOfDouble.length;int j = arrayOfDouble[0].length;
    double d = 0.0D;
    double[] arrayOfDouble1 = new double[i];
    double[] arrayOfDouble2 = new double[j];
    for (int k = 0; k < i; k++) {
      arrayOfDouble1[k] = 0.0D;
    }
    for (int m = 0; m < j; m++) {
      arrayOfDouble2[m] = 0.0D;
    }
    for (int n = 0; n < i; n++) {
      for (int i1 = 0; i1 < j; i1++)
      {
        arrayOfDouble1[n] += arrayOfDouble[n][i1] / j;
        arrayOfDouble2[i1] += arrayOfDouble[n][i1] / i;
        d += arrayOfDouble[n][i1] / i / j;
      }
    }
    for (int i1 = 0; i1 < i; i1++) {
      for (int i2 = 0; i2 < j; i2++) {
        arrayOfDouble[i1][i2] -= arrayOfDouble1[i1] + arrayOfDouble2[i2] - d;
      }
    }
    this.plot.setYData(arrayOfDouble);
  }
  
  public void plot_changed()
  {
    if (this.adjust == 1) {
      adjustMEs();
    }
  }
  
  public TestPlot()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TestPlot();
  }
}
