package pc.powercalculator.apps;

import java.awt.Color;
import pc.powercalculator.PiListener;
import pc.powercalculator.PiPanel;
import pc.powercalculator.PowerCalculator;
import pc.stat.anova.Factor;
import pc.stat.anova.Model;
import pc.stat.anova.Residual;
import pc.stat.anova.Term;
import pc.stat.anova.WithinCells;
import pc.stat.dist.F;
import pc.util.Utility;

public class AnovaGUI
  extends PowerCalculator
  implements PiListener
{
  public double[] n;
  public double[] effSD;
  public double[] power;
  public double alpha;
  public int[] random;
  private Model model;
  protected AnovaCompGUI acg;
  protected boolean ignoreActions = false;
  protected AnovaHelper helper = null;
  
  public void gui()
  {
    this.n = new double[this.model.nFac()];
    if (this.effSD == null)
    {
      this.effSD = new double[this.model.nTerm()];
      for (int i = 0; i < this.model.nTerm(); i++) {
        this.effSD[i] = 1.0D;
      }
    }
    this.power = new double[this.model.nTerm()];
    this.random = new int[this.model.nFac()];
    
    String[] arrayOfString = { "Fixed", "Random" };
    
    beginSubpanel(1, false);
    for (int j = 0; j < this.model.nFac(); j++)
    {
      beginSubpanel(1, true);
      Factor localFactor = this.model.getFac(j);
      String str = "levels[" + localFactor.getName() + "]";
      if (str.equals("n[Residual]")) {
        str = "Replications";
      }
      hradio("random[" + j + "]", localFactor.getName(), arrayOfString, localFactor.isRandom() ? 1 : 0);
      bar("n[" + j + "]", str, localFactor.getLevels());
      endSubpanel();
    }
    endSubpanel();
    
    newColumn();
    int j = this.model.nTerm() > 10 ? 1 : 0;
    int k = this.model.nTerm() / 2;
    

    beginSubpanel(1, false);
    this.panel.setBackground(new Color(255, 255, 200));
    beginSubpanel(2, true);
    for (int m = 0; m < this.model.nTerm(); m++)
    {
      if ((j != 0) && (m == k))
      {
        endSubpanel();
        endSubpanel();
        newColumn();
        beginSubpanel(1, false);
        this.panel.setBackground(new Color(255, 255, 200));
        beginSubpanel(2, true);
      }
      Term localTerm = this.model.getTerm(m);
      bar("effSD[" + m + "]", "SD[" + localTerm.getName() + "]", this.effSD[m]);
      if (m < this.model.nTerm() - 1) {
        interval("power[" + m + "]", "Power[" + localTerm.getName() + "]", 0.5D, 0.0D, 1.0D);
      } else {
        choice("alpha", "Significance level", new double[] { 0.001D, 0.005D, 0.01D, 0.02D, 0.05D, 0.1D, 0.2D }, 4);
      }
    }
    endSubpanel();
    endSubpanel();
    
    menuItem("linkHelper", "Effect SD helper");
    menuItem("linkComps", "Contrasts/Comparisons");
    menuItem("showEMS", "Show EMS");
    menuItem("report", "Report");
    menuItem("help", "ANOVA help", this.helpMenu);
  }
  
  public void click()
  {
    this.power = this.model.power(this.effSD, this.alpha);
    for (int i = 0; i < this.model.nTerm(); i++) {
      if (this.power[i] < 0.0D) {
        this.power[i] = (0.0D / 0.0D);
      }
    }
  }
  
  public void random_changed()
  {
    if (this.acg != null)
    {
      errmsg("WARNING: Changing between fixed and random may invalidate");
      errmsg("results in the associated comparisons/contrasts dialog!");
    }
    Factor localFactor = this.model.getFac(this.sourceIndex);
    localFactor.setRandom(this.random[this.sourceIndex] == 1);
    this.random[this.sourceIndex] = (localFactor.isRandom() ? 1 : 0);
    click();
  }
  
  public void n_changed()
  {
    this.n[this.sourceIndex] = max(1.0D, round(this.n[this.sourceIndex]));
    this.model.getFac(this.sourceIndex).setLevels((int)this.n[this.sourceIndex]);
    for (int i = 0; i < this.model.nFac(); i++) {
      this.n[i] = this.model.getFac(i).getLevels();
    }
    click();
  }
  
  public synchronized void restoreVars(double[] paramArrayOfDouble)
  {
    super.restoreVars(paramArrayOfDouble);
    for (int i = 0; i < this.model.nFac(); i++) {
      this.model.getFac(i).setLevels((int)this.n[i]);
    }
  }
  
  public void power_changed()
  {
    double[] arrayOfDouble = this.model.getPowerInfo(this.sourceIndex);
    Term localTerm = this.model.getTerm(this.sourceIndex);
    double d1;
    if (!localTerm.isRandom())
    {
      double d2 = F.lambda(this.power[this.sourceIndex], arrayOfDouble[2], arrayOfDouble[3], this.alpha);
      
      d1 = arrayOfDouble[1] * d2 / arrayOfDouble[2] / arrayOfDouble[0];
    }
    else
    {
      d1 = arrayOfDouble[1] / arrayOfDouble[0] * (F.quantile(1.0D - this.alpha, arrayOfDouble[2], arrayOfDouble[3]) / F.quantile(1.0D - this.power[this.sourceIndex], arrayOfDouble[2], arrayOfDouble[3]) - 1.0D);
    }
    if (d1 > 0.0D) {
      this.effSD[this.sourceIndex] = sqrt(d1);
    }
    click();
  }
  
  public void linkHelper()
  {
    if (this.helper == null)
    {
      this.helper = new AnovaHelper(this);
      this.helper.setMaster(this);
    }
    else
    {
      this.helper.show();
    }
  }
  
  public void linkComps()
  {
    this.acg = new AnovaCompGUI(getTitle(), this.model, this.effSD);
    addPiListener(this.acg);
    this.acg.addPiListener(this);
    this.acg.ag = this;
  }
  
  public void piAction(String paramString)
  {
    if ((this.ignoreActions) || (this.acg == null)) {
      return;
    }
    setVar(paramString, this.acg);
    callMethodFor(paramString);
    updateVars();
    this.acg.ignoreActions = true;
    notifyListeners(paramString);
    this.acg.ignoreActions = false;
  }
  
  public void close()
  {
    if (this.helper != null) {
      this.helper.close();
    }
    if (this.acg != null)
    {
      removePiListener(this.acg);
      this.acg.removePiListener(this);
      this.acg.ag = null;
    }
    dispose();
  }
  
  public void showEMS()
  {
    showText(this.model.EMSString(), "Expected mean squares", 25, 50);
  }
  
  public void report()
  {
    showText(reportString(), "Power-analysis report", 25, 60);
  }
  
  public void help()
  {
    showText(AnovaGUI.class, "AnovaGUIHelp.txt", "Help for ANOVA power analysis", 25, 60);
  }
  
  public String reportString()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("Below is a summary of the factors and terms for the\n");
    localStringBuffer.append("balanced ANOVA model under study.  Further explanation is\n");
    localStringBuffer.append("given after the results.\n\n");
    localStringBuffer.append(getTitle() + "\n\n");
    localStringBuffer.append(Utility.format("Factor", 14) + "\tlevels\n");
    Object localObject;
    for (int i = 0; i < this.model.nFac(); i++)
    {
      localObject = this.model.getFac(i);
      localStringBuffer.append(Utility.format(((Factor)localObject).getName(), 14) + "\t  " + ((Factor)localObject).getLevels());
      if (((Factor)localObject).isRandom()) {
        localStringBuffer.append("\trandom\n");
      } else {
        localStringBuffer.append("\tfixed\n");
      }
    }
    localStringBuffer.append(Utility.format("\nTerm", 22) + "\tdf\tStdDev\tPower\n");
    int i;
    
    for (i = 0; i < this.model.nTerm(); i++)
    {
      localObject = this.model.getTerm(i);
      localStringBuffer.append(Utility.format(((Term)localObject).getName(), 22) + "\t" + ((Term)localObject).df() + "\t");
      localStringBuffer.append(Utility.format(this.effSD[i], 4));
      if (this.power[i] >= 0.0D) {
        localStringBuffer.append("\t" + Utility.format(this.power[i], 4) + "\n");
      } else {
        localStringBuffer.append("\n");
      }
    }
    localStringBuffer.append("\nAlpha = " + this.alpha + "\n");
    localStringBuffer.append("\n\nNOTES:\n\n");
    localStringBuffer.append("Effect sizes for each term are expressed as a standard\n");
    localStringBuffer.append("deviation.  In the case of a random effect, the standard\n");
    localStringBuffer.append("deviation is the square root of its variance component.\n");
    localStringBuffer.append("For a fixed effect, the standard deviation is the square\n");
    localStringBuffer.append("root of the sum of squares of the model effects (in the\n");
    localStringBuffer.append("constrained model), divided by the degrees of freedom for\n");
    localStringBuffer.append("the term in question.\n\n");
    
    localStringBuffer.append("In both cases, the expected mean square for a term is\n\n");
    
    localStringBuffer.append("\tK*(StdDev)^2 + EMS(ET)\n\n");
    
    localStringBuffer.append("where K is the number of observations at each distinct\n");
    localStringBuffer.append("level of the term, and EMS(ET) is the expected mean square\n");
    localStringBuffer.append("for the error term for testing the significance of the\n");
    localStringBuffer.append("term.  Please note that the error term ET is based on the\n");
    localStringBuffer.append("random (and mixed) effects in the model, and that the\n");
    localStringBuffer.append("computed power depends on both the effect size shown for\n");
    localStringBuffer.append("the term under test, but also the standard deviations shown\n");
    localStringBuffer.append("for all random terms involved in the error term.\n\n");
    
    localStringBuffer.append("This power analysis is based on the \"unrestricted\"\n");
    localStringBuffer.append("parameterization of the balanced mixed ANOVA model.  Where\n");
    localStringBuffer.append("necessary, error terms are constructed using linear\n");
    localStringBuffer.append("combinations of mean squares, and the degrees of freedom\n");
    localStringBuffer.append("for the denominator of the approximate F test are computed\n");
    localStringBuffer.append("using the Satterthwaite method.\n");
    return localStringBuffer.toString();
  }
  
  public Model getModel()
  {
    return this.model;
  }
  
  public AnovaGUI(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4)
  {
    super(paramString1, false);
    this.model = new Model(paramString2);
    if (paramInt <= 1) {
      this.model.addTerm(new Residual(this.model));
    } else {
      this.model.addFactor(new WithinCells(this.model, paramInt));
    }
    if (paramString3 != null) {
      this.model.setLevels(paramString3);
    }
    if (paramString4 != null) {
      this.model.setRandom(paramString4);
    }
    build();
  }
  
  public AnovaGUI(String paramString, Model paramModel, double[] paramArrayOfDouble)
  {
    super(paramString, false);
    this.model = paramModel;
    this.effSD = paramArrayOfDouble;
    build();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new AnovaGUI("Nested-factorial model", "grp + Subj(grp) + trt + grp*trt", 2, "grp 4   Subj 5   trt 3", "Subj");
  }
}
