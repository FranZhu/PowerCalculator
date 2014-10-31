package pc.powercalculator.apps;

import java.awt.CardLayout;
import java.awt.Choice;
import java.awt.Menu;
import java.awt.event.ActionListener;
import pc.awt.IntPlot;
import pc.awt.RVLayout;
import pc.powercalculator.PiChoice;
import pc.powercalculator.PiDotplot;
import pc.powercalculator.PiListener;
import pc.powercalculator.PiPanel;
import pc.powercalculator.PowerCalculator;
import pc.stat.Stat;
import pc.stat.anova.Factor;
import pc.stat.anova.Model;
import pc.stat.anova.Term;
import pc.stat.dist.Normal;

public class AnovaHelper
  extends PowerCalculator
  implements PiListener, ActionListener
{
  private static String title = "ANOVA Effects Helper";
  public int sel;
  public int distn;
  public int fixedRange;
  public int adjust;
  private AnovaGUI agui;
  private Model model;
  private int termNo;
  private int modFacNo;
  private int[] facNo = new int[2];
  private PiChoice chooser;
  private PiDotplot dotplot;
  private IntPlot plot;
  private CardLayout card;
  private PiPanel cardPanel;
  private String[] facName;
  private boolean MEmode = true;
  
  public void beforeSetup()
  {
    this.optMenu.remove(0);
  }
  
  public void gui()
  {
    choice("sel", "Effect", new String[] { "~" }, 0);
    this.chooser = ((PiChoice)getComponent("sel"));
    

    beginSubpanel(1);
    this.card = new CardLayout();
    this.cardPanel = this.panel;
    this.cardPanel.setLayout(this.card);
    

    this.panel = new PiPanel(new RVLayout(1, true, true));
    this.dotplot = dotplot("dotplot_changed", "Pattern of means", new double[] { -1.0D, 1.0D });
    
    choice("distn", "Distribution", new String[] { "(general)", "min SD", "normal", "uniform", "max SD" }, 1);
    

    choice("fixedRange", "Dist. option", new String[] { "Fixed SD", "Fixed Range" }, 1);
    
    this.cardPanel.add("MEPanel", this.panel);
    

    this.panel = new PiPanel(new RVLayout(1, true, true));
    this.cardPanel.add("2wayPanel", this.panel);
    

    this.panel.setStretchable(true);
    
    this.facName = new String[] { "", "" };
    double[] arrayOfDouble1 = { 1.0D, 2.0D };
    double[] arrayOfDouble2 = { -1.0D, 1.0D };
    double[] arrayOfDouble3 = { 1.0D, -1.0D };
    double[][] arrayOfDouble = { arrayOfDouble2, arrayOfDouble3 };
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
    
    this.panel = this.cardPanel;
    
    endSubpanel();
  }
  
  public void afterSetup()
  {
    setupFixedFactors();
    this.optMenu.remove(0);
  }
  
  public void click() {}
  
  public void showMEs()
  {
    this.card.show(this.cardPanel, "MEPanel");
  }
  
  public void show2way()
  {
    this.card.show(this.cardPanel, "2wayPanel");
  }
  
  private void labelAxes()
  {
    this.plot.setAxisLabels(new String[] { "Levels of " + this.facName[0], ">Profiles: " + this.facName[1] }, new String[] { "Response" });
  }
  
  public void adjustMEs(boolean paramBoolean)
  {
	int k, m, n;
    double[][] arrayOfDouble1 = this.plot.getYData();
    int i = arrayOfDouble1.length;int j = arrayOfDouble1[0].length;
    double d = 0.0D;
    double[] arrayOfDouble2 = new double[i];
    double[] arrayOfDouble3 = new double[j];
    for (k = 0; k < i; k++) {
      arrayOfDouble2[k] = 0.0D;
    }
    for (k = 0; k < j; k++) {
      arrayOfDouble3[k] = 0.0D;
    }
    for (k = 0; k < i; k++) {
      for (m = 0; m < j; m++)
      {
        arrayOfDouble2[k] += arrayOfDouble1[k][m] / j;
        arrayOfDouble3[m] += arrayOfDouble1[k][m] / i;
        d += arrayOfDouble1[k][m] / i / j;
      }
    }
    double[][] arrayOfDouble4 = arrayOfDouble1;
    if (!paramBoolean) {
      arrayOfDouble4 = new double[i][j];
    }
    for (m = 0; m < i; m++) {
      for (n = 0; n < j; n++) {
        arrayOfDouble4[m][n] = (arrayOfDouble1[m][n] - arrayOfDouble2[m] - arrayOfDouble3[n] + d);
      }
    }
    if (paramBoolean) {
      this.plot.setYData(arrayOfDouble4);
    }
    this.agui.effSD[this.termNo] = sd2(arrayOfDouble4);
    String str = "effSD[" + this.termNo + "]";
    this.agui.callMethodFor(str);
    this.agui.updateVars();
    this.agui.notifyListeners(str, this);
  }
  
  public void plot_changed()
  {
    adjustMEs(this.adjust == 1);
  }
  
  public void adjust_changed()
  {
    adjustMEs(this.adjust == 1);
  }
  
  public void swapFac()
  {
    String str = this.facName[0];
    this.facName[0] = this.facName[1];
    this.facName[1] = str;
    int i = this.facNo[0];
    this.facNo[0] = this.facNo[1];
    this.facNo[1] = i;
    labelAxes();
    double[][] arrayOfDouble1 = this.plot.getYData();
    int j = arrayOfDouble1.length;int k = arrayOfDouble1[0].length;
    double[][] arrayOfDouble2 = new double[k][j];double[] arrayOfDouble = new double[j];
    for (int m = 0; m < j; m++)
    {
      arrayOfDouble[m] = m;
      for (int n = 0; n < k; n++) {
        arrayOfDouble2[n][m] = arrayOfDouble1[m][n];
      }
    }
    this.plot.setData(arrayOfDouble, arrayOfDouble2);
  }
  
  public void sel_changed()
  {
	int j;
    this.termNo = -1;
    String str1 = this.chooser.getChoice().getItem(this.sel);
    for (int i = 0; i < this.model.nTerm(); i++) {
      if (this.model.getTerm(i).getName().equals(str1)) {
        this.termNo = i;
      }
    }
    Term localTerm = this.model.getTerm(this.termNo);
    if (localTerm.order() == 2)
    {
      this.MEmode = false;
      this.modFacNo = -1;
      Factor[] arrayOfFactor = localTerm.getFactors();
      this.facName[1] = arrayOfFactor[0].getName();
      this.facName[0] = arrayOfFactor[1].getName();
      for (int k = 0; k < this.model.nFac(); k++)
      {
        String str2 = this.model.getFac(k).getName();
        if (str2.equals(this.facName[0])) {
          this.facNo[0] = k;
        }
        if (str2.equals(this.facName[1])) {
          this.facNo[1] = k;
        }
      }
      show2way();
      setupIntPlot(arrayOfFactor);
    }
    else
    {
      this.MEmode = true;
      showMEs();
      for (j = 0; j < this.model.nFac(); j++) {
        if (this.model.getFac(j).getName().equals(str1)) {
          this.modFacNo = j;
        }
      }
      j = this.fixedRange;
      this.fixedRange = 0;
      reviseDotplot(true);
      this.fixedRange = j;
    }
  }
  
  public double[] linCon(int paramInt)
  {
    double[] arrayOfDouble = new double[paramInt];double d = 0.5D * (paramInt - 1);
    for (int i = 0; i < paramInt; i++) {
      arrayOfDouble[i] = (i - d);
    }
    return arrayOfDouble;
  }
  
  public void distn_changed()
  {
    if (this.distn != 0) {
      revisePatternDotplot((int)(this.agui.n[this.modFacNo] + 0.05D), false);
    }
  }
  
  public void fixedRange_changed()
  {
    Choice localChoice = ((PiChoice)getComponent("distn")).getChoice();
    int i = localChoice.getSelectedIndex();
    localChoice.remove(4);
    localChoice.remove(1);
    if (this.fixedRange == 0)
    {
      localChoice.insert("max range", 1);
      localChoice.insert("min range", 4);
    }
    else
    {
      localChoice.insert("min SD", 1);
      localChoice.insert("max SD", 4);
    }
    localChoice.select(i);
  }
  
  private void setupIntPlot(Factor[] paramArrayOfFactor)
  {
    int i = paramArrayOfFactor[0].getLevels();int j = paramArrayOfFactor[1].getLevels();
    double[] arrayOfDouble1 = new double[j];double[][] arrayOfDouble = new double[i][j];
    double[] arrayOfDouble2 = linCon(i);double[] arrayOfDouble3 = linCon(j);
    for (int k = 0; k < i; k++) {
      for (int m = 0; m < j; m++)
      {
        arrayOfDouble1[m] = m;
        arrayOfDouble[k][m] = (arrayOfDouble2[k] * arrayOfDouble3[m]);
      }
    }
    labelAxes();
    this.plot.setData(arrayOfDouble1, arrayOfDouble, false);
    reviseIntPlotSD(arrayOfDouble);
  }
  
  private void reviseIntPlotSD(double[][] paramArrayOfDouble)
  {
    int i = paramArrayOfDouble.length;int j = paramArrayOfDouble[0].length;
    double d = this.agui.effSD[this.termNo] / sd2(paramArrayOfDouble);
    for (int k = 0; k < i; k++) {
      for (int m = 0; m < j; m++) {
        paramArrayOfDouble[k][m] *= d;
      }
    }
    this.plot.setYData(paramArrayOfDouble);
  }
  
  private void reviseIntPlotSD()
  {
    reviseIntPlotSD(this.plot.getYData());
  }
  
  private void reviseIntPlotN(String paramString)
  {
    double[][] arrayOfDouble;
    int i;
    int j;
    int k;
    int m;
    double [][] localObject;
    int i1;
    if (paramString.equals("n[" + this.facNo[0] + "]"))
    {
      arrayOfDouble = this.plot.getYData();
      i = (int)(this.agui.n[this.facNo[0]] + 0.05D);
      j = arrayOfDouble.length;k = arrayOfDouble[0].length;m = Math.min(k, i);
      localObject = new double[j][i];
      double[] arrayOfDouble1 = new double[i];
      for (i1 = 0; i1 < j; i1++)
      {
        for (int i2 = 0; i2 < m; i2++) {
          localObject[i1][i2] = arrayOfDouble[i1][i2];
        }
        for (int i2 = m; i2 < i; i2++) {
          localObject[i1][i2] = 0.0D;
        }
      }
      for (i1 = 0; i1 < i; i1++) {
        arrayOfDouble1[i1] = i1;
      }
      this.plot.setData(arrayOfDouble1, (double[][])localObject);
      adjustMEs(this.adjust == 1);
    }
    if (paramString.equals("n[" + this.facNo[1] + "]"))
    {
      arrayOfDouble = this.plot.getYData();
      i = (int)(this.agui.n[this.facNo[1]] + 0.05D);
      j = arrayOfDouble.length;k = arrayOfDouble[0].length;m = Math.min(j, i);
      localObject = new double[i][];
      for (int n = 0; n < m; n++) {
        localObject[n] = arrayOfDouble[n];
      }
      for (int n = m; n < i; n++)
      {
        localObject[n] = new double[k];
        for (i1 = 0; i1 < k; i1++) {
          localObject[n][i1] = 0.0D;
        }
      }
      this.plot.setYData((double[][])localObject);
      adjustMEs(this.adjust == 1);
    }
  }
  
  private double sd2(double[][] paramArrayOfDouble)
  {
    double d = 0.0D;
    int i = paramArrayOfDouble.length;int j = paramArrayOfDouble[0].length;
    for (int k = 0; k < i; k++) {
      for (int m = 0; m < j; m++) {
        d += paramArrayOfDouble[k][m] * paramArrayOfDouble[k][m];
      }
    }
    return sqrt(d / (i - 1) / (j - 1));
  }
  
  private void reviseDotplot(boolean paramBoolean)
  {
    int i = (int)(this.agui.n[this.modFacNo] + 0.05D);
    if (this.distn == 0) {
      reviseGeneralDotplot(i, paramBoolean);
    } else {
      revisePatternDotplot(i, paramBoolean);
    }
  }
  
  private void reviseGeneralDotplot(int paramInt, boolean paramBoolean)
  {
    double[] arrayOfDouble1 = this.dotplot.getValue();
    double[] arrayOfDouble2 = new double[paramInt];
    double[] arrayOfDouble3 = Stat.meanSD(arrayOfDouble1);
    if (arrayOfDouble3[1] == 0.0D)
    {
      arrayOfDouble1[0] -= arrayOfDouble3[0];
      arrayOfDouble1[(arrayOfDouble1.length - 1)] += arrayOfDouble3[0];
    }
    for (int i = 1; i < paramInt - 1; i++) {
      arrayOfDouble2[i] = arrayOfDouble3[0];
    }
    for (int i = 0; i < Math.min(paramInt, arrayOfDouble1.length) / 2; i++)
    {
      arrayOfDouble2[i] = arrayOfDouble1[i];
      arrayOfDouble2[(paramInt - i - 1)] = arrayOfDouble1[(arrayOfDouble1.length - i - 1)];
    }
    scaleAndDisplay(arrayOfDouble2, false);
  }
  
  private void revisePatternDotplot(int paramInt, boolean paramBoolean)
  {
    double[] arrayOfDouble = new double[paramInt];
    int i;
    switch (this.distn)
    {
    case 1: 
      arrayOfDouble[0] = -1.0D;arrayOfDouble[(paramInt - 1)] = 1.0D;
      for (i = 1; i < paramInt - 1; i++) {
        arrayOfDouble[i] = 0.0D;
      }
      break;
    case 2: 
      for (i = 0; i < paramInt; i++) {
        arrayOfDouble[i] = Normal.quantile((i + 0.5D) / paramInt);
      }
      break;
    case 3: 
      for (i = 0; i < paramInt; i++) {
        arrayOfDouble[i] = i;
      }
      break;
    case 4: 
      for (i = 0; i < 1 + paramInt / 2; i++)
      {
        arrayOfDouble[i] = -1.0D;
        arrayOfDouble[(paramInt - i - 1)] = 1.0D;
      }
    }
    scaleAndDisplay(arrayOfDouble, (!paramBoolean) && (this.fixedRange == 1));
  }
  
  private void scaleAndDisplay(double[] paramArrayOfDouble, boolean paramBoolean)
  {
    double[] arrayOfDouble1 = this.dotplot.getValue();
    int i = paramArrayOfDouble.length;
    double d1;
    double d2;
    double d3;
    double d4;
    if (paramBoolean)
    {
      d1 = paramArrayOfDouble[0];
      d2 = paramArrayOfDouble[(i - 1)] - paramArrayOfDouble[0];
      d3 = arrayOfDouble1[0];
      d4 = arrayOfDouble1[(arrayOfDouble1.length - 1)] - arrayOfDouble1[0];
    }
    else
    {
      double[] arrayOfDouble2 = Stat.meanSD(paramArrayOfDouble);
      d1 = arrayOfDouble2[0];
      d2 = arrayOfDouble2[1];
      d3 = Stat.mean(arrayOfDouble1);
      d4 = this.agui.effSD[this.termNo];
    }
    double d5 = d4 / d2;
    for (int j = 0; j < i; j++) {
      paramArrayOfDouble[j] = (d3 + d5 * (paramArrayOfDouble[j] - d1));
    }
    this.dotplot.setValue(paramArrayOfDouble);
    this.dotplot.repaint();
    updateSD();
  }
  
  private void updateSD()
  {
    this.agui.effSD[this.termNo] = Stat.sd(this.dotplot.getValue());
    String str = "effSD[" + this.termNo + "]";
    this.agui.callMethodFor(str);
    this.agui.updateVars();
    this.agui.notifyListeners(str, this);
  }
  
  private void setupFixedFactors()
  {
    Choice localChoice = this.chooser.getChoice();
    String str1 = localChoice.getItem(this.sel);
    localChoice.removeAll();
    this.sel = 0;
    for (int i = 0; i < this.model.nTerm(); i++)
    {
      Term localTerm = this.model.getTerm(i);
      if ((localTerm.order() <= 2) && (!localTerm.isRandom()))
      {
        String str2 = localTerm.getName();
        if (str2.equals(str1)) {
          this.sel = localChoice.getItemCount();
        }
        localChoice.add(str2);
      }
    }
    if (localChoice.getItemCount() == 0)
    {
      localChoice.add("(disabled)");
      this.dotplot.setVisible(false);
    }
    else
    {
      this.dotplot.setVisible(true);
      sel_changed();
      localChoice.select(this.sel);
    }
  }
  
  public void piAction(String paramString)
  {
    if ((paramString.equals("effSD[" + this.termNo + "]")) || (paramString.equals("power[" + this.termNo + "]")))
    {
      if (this.MEmode) {
        reviseDotplot(true);
      } else {
        reviseIntPlotSD();
      }
    }
    else if (paramString.startsWith("n["))
    {
      if (!this.MEmode) {
        reviseIntPlotN(paramString);
      } else if (paramString.equals("n[" + this.modFacNo + "]")) {
        reviseDotplot(true);
      }
    }
    else if (paramString.startsWith("random")) {
      setupFixedFactors();
    }
  }
  
  public void dotplot_changed()
  {
    if (this.dotplot.getActionCommand().equals("Dotplot:Shift")) {
      return;
    }
    this.distn = 0;
    updateSD();
  }
  
  public void close()
  {
    this.agui.helper = null;
    this.agui.removePiListener(this);
    dispose();
  }
  
  public AnovaHelper(AnovaGUI paramAnovaGUI)
  {
    super(title, false);
    this.agui = paramAnovaGUI;
    this.model = paramAnovaGUI.getModel();
    paramAnovaGUI.addPiListener(this);
    build();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    AnovaGUI localAnovaGUI = new AnovaGUI("Test of AnovaHelper", "row | col | trt", 10, "row 5 col 3 trt 4", "");
    
    localAnovaGUI.linkHelper();
  }
}
