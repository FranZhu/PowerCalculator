package pc.powercalculator.apps;

import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Window;
import java.util.Vector;
import pc.powercalculator.PiArrayField;
import pc.powercalculator.PiChoice;
import pc.powercalculator.PiListener;
import pc.powercalculator.PowerCalculator;
import pc.stat.anova.Factor;
import pc.stat.anova.Model;
import pc.stat.anova.Residual;
import pc.stat.anova.Term;
import pc.stat.anova.WithinCells;
import pc.stat.dist.F;
import pc.stat.dist.T;
import pc.stat.dist.Tukey;
import pc.util.Utility;

public class AnovaCompGUI
  extends PowerCalculator
  implements PiListener
{
  private static String title = "ANOVA Comparisons";
  public double effSize;
  public double[] n;
  public double[] effSD;
  public double power;
  public double alpha;
  public double famSize;
  public double bonfDiv;
  public double[] coef;
  public int comp;
  public int cvType;
  public int autoBonf;
  public int restr;
  public int oneSided;
  public PiArrayField coefFld;
  protected String[] restrStg;
  protected String[] cvStg;
  protected String[] fixedNames;
  protected Model model;
  protected Term[] fixedTerms;
  protected Vector restrVec;
  protected AnovaGUI ag;
  protected boolean ignoreActions = false;
  private int bonfIndex;
  private Component bonfComp;
  private Component famComp;
  private Component restrComp;
  private double[] vdf;
  private double cv;
  
  public void gui()
  {
    label("Levels / Sample size");
    beginSubpanel(1, false);
    Object localObject;
    for (int i = 0; i < this.model.nFac(); i++)
    {
      localObject = this.model.getFac(i);
      String str1 = ((Factor)localObject).isRandom() ? "n[" : "levels[";
      String str2 = str1 + ((Factor)localObject).getName() + "]";
      if (str2.equals("n[Residual]")) {
        str2 = "Replications";
      }
      bar("n[" + i + "]", str2, ((Factor)localObject).getLevels());
    }
    endSubpanel();
    

    newColumn();
    label("Random effects");
    beginSubpanel(1, false);
    int i;
    for (i = 0; i < this.model.nTerm(); i++);
    {
      localObject = this.model.getTerm(i);
      if (((Term)localObject).isRandom()) {
        bar("effSD[" + i + "]", "SD[" + ((Term)localObject).getName() + "]", this.effSD[i]);
      }
    }
    endSubpanel();
    

    newColumn();
    label("Contrasts across fixed levels");
    beginSubpanel(1, false);
    choice("comp", "Contrast levels of", this.fixedNames, 0);
    this.coefFld = arrayField("click", "Contrast coefficients", new double[] { -1.0D, 1.0D });
    
    choice("restr", "Restriction", this.restrStg, 0);
    beginSubpanel(2);
    choice("cvType", "Method", this.cvStg, 2);
    field("famSize", "# means", 1.0D, 2, 3);
    choice("alpha", "Alpha ", new double[] { 0.005D, 0.01D, 0.02D, 0.05D, 0.1D, 0.2D }, 3);
    field("bonfDiv", "# tests", 1.0D, 2, 3);
    checkbox("oneSided", "One-Sided", 0);
    endSubpanel();
    
    beginSubpanel(1, true);
    bar("effSize", "Detectable contrast", 1.0D);
    ointerval("power", "Power", 0.0D, 0.0D, 1.0D);
    endSubpanel();
    endSubpanel();
    

    this.bonfComp = ((Component)getComponent("bonfDiv"));
    this.bonfComp.setVisible(false);
    this.famComp = ((Component)getComponent("famSize"));
    this.restrComp = ((Component)getComponent("restr"));
    this.restrComp.setVisible(false);
    
    menuCheckbox("autoBonf", "Auto Bonferroni", 1);
    menuItem("linkAOV", "ANOVA dialog");
    menuItem("showEMS", "Show EMS");
    menuItem("report", "Report");
    menuItem("help", "ANOVA contrasts help", this.helpMenu);
  }
  
  public void beforeSetup()
  {
    this.n = new double[this.model.nFac()];
    int i;
    if (this.effSD == null)
    {
      this.effSD = new double[this.model.nTerm()];
      for (i = 0; i < this.model.nTerm(); i++) {
        this.effSD[i] = 1.0D;
      }
    }
    i = 0;
    int j = 0, k=0;
    for (k = 0; k < this.model.nTerm(); k++) {
      if (!this.model.getTerm(k).isRandom()) {
        i++;
      }
    }
    if (i == 0)
    {
      Utility.error("There are no fixed factors, so no contrasts can be studied", this);
      return;
    }
    this.fixedNames = new String[i];
    this.fixedTerms = new Term[i];
    for (k = 0; k < this.model.nTerm(); k++) {
      if (!this.model.getTerm(k).isRandom())
      {
        this.fixedTerms[j] = this.model.getTerm(k);
        this.fixedNames[j] = this.fixedTerms[j].getName();
        j++;
      }
    }
    this.restrStg = new String[] { "(no restrictions)" };
    this.restrVec = this.model.getAllCompRestr(this.fixedTerms[0]);
    this.cvStg = new String[] { "t", "Dunnett", "Tukey/HSD", "Bonferroni", "Scheffe" };
    
    this.bonfIndex = 3;
  }
  
  public void afterSetup()
  {
    setFamSize();
    click();
    updateVars();
  }
  
  public void click()
  {
    this.bonfDiv = PowerCalculator.max(1.0D, PowerCalculator.round(this.bonfDiv));
    Factor[] arrayOfFactor = (Factor[])this.restrVec.elementAt(this.restr);
    Term localTerm = this.fixedTerms[this.comp];
    this.vdf = this.model.getCompVariance(localTerm, arrayOfFactor, this.effSD);
    
    double d1 = this.vdf[0];double d2 = this.vdf[1];
    double[] arrayOfDouble = this.coefFld.getValue();double d3 = 0.0D;double d4 = 0.0D;
    for (int i = 0; i < PowerCalculator.min(localTerm.span(), arrayOfDouble.length); i++)
    {
      d3 += arrayOfDouble[i];
      d4 += arrayOfDouble[i] * arrayOfDouble[i];
    }
    if ((PowerCalculator.abs(d3) > 0.001D) || (d4 < 0.001D))
    {
      this.power = (0.0D / 0.0D);
      return;
    }
    double d5 = this.effSize / PowerCalculator.sqrt(d4 * d1);
    double d6 = 2.0D - this.oneSided;
    switch (this.cvType)
    {
    case 0: 
      this.cv = (-T.quantile(this.alpha / d6, d2));
      break;
    case 1: 
      this.cv = (-T.quantile(this.alpha / (d6 * (this.famSize - 1.0D)), d2));
      break;
    case 2: 
      this.cv = (Tukey.quantile(1.0D - 2.0D * this.alpha / d6, this.famSize, d2) / PowerCalculator.sqrt(2.0D));
      break;
    case 3: 
      this.cv = (-T.quantile(this.alpha / (d6 * this.bonfDiv), d2));
      break;
    case 4: 
      this.cv = PowerCalculator.sqrt((this.famSize - 1.0D) * F.quantile(1.0D - 2.0D * this.alpha / d6, this.famSize - 1.0D, d2));
    }
    this.famComp.setVisible(this.cvType > 0);
    
    this.power = (1.0D - T.cdf(this.cv, d2, d5));
    if (this.oneSided == 0) {
      this.power += T.cdf(-this.cv, d2, d5);
    }
  }
  
  public void cvType_changed()
  {
    this.bonfComp.setVisible(this.cvType == this.bonfIndex);
    if (this.cvType == this.bonfIndex)
    {
      show();
      setBonf();
    }
    click();
  }
  
  public void n_changed()
  {
    this.model.recalcLU = true;
    this.n[this.sourceIndex] = PowerCalculator.max(1.0D, PowerCalculator.round(this.n[this.sourceIndex]));
    Factor localFactor = this.model.getFac(this.sourceIndex);
    localFactor.setLevels((int)this.n[this.sourceIndex]);
    for (int i = 0; i < this.model.nFac(); i++) {
      this.n[i] = this.model.getFac(i).getLevels();
    }
    if (!localFactor.isRandom()) {
      restr_changed();
    }
    click();
  }
  
  public void comp_changed()
  {
    this.restrVec = this.model.getAllCompRestr(this.fixedTerms[this.comp]);
    Choice localChoice = ((PiChoice)this.restrComp).getChoice();
    localChoice.removeAll();
    localChoice.add("(no restrictions)");
    this.restr = 0;
    if (this.restrVec.size() < 2)
    {
      this.restrComp.setVisible(false);
    }
    else
    {
      this.restrComp.setVisible(true);
      for (int i = 1; i < this.restrVec.size(); i++)
      {
        Factor[] arrayOfFactor = (Factor[])this.restrVec.elementAt(i);
        String str = "Same ";
        for (int j = 0; j < arrayOfFactor.length; j++)
        {
          if (j > 0) {
            str = str + " and ";
          }
          str = str + arrayOfFactor[j].getShortName();
        }
        localChoice.add(str);
      }
    }
    setFamSize();
    setBonf();
    click();
  }
  
  public void restr_changed()
  {
    setFamSize();
    if (this.autoBonf == 1) {
      setBonf();
    }
    click();
  }
  
  public void autoBonf_changed()
  {
    restr_changed();
  }
  
  public void setBonf()
  {
    if (this.famSize > 1.0D) {
      this.bonfDiv = (this.famSize * (this.famSize - 1.0D) / 2.0D);
    } else {
      this.bonfDiv = 1.0D;
    }
  }
  
  public void setFamSize()
  {
    this.famSize = this.fixedTerms[this.comp].span();
    if (this.restr > 0)
    {
      Factor[] arrayOfFactor = (Factor[])this.restrVec.elementAt(this.restr);
      if (arrayOfFactor != null) {
        for (int i = 0; i < arrayOfFactor.length; i++) {
          this.famSize /= arrayOfFactor[i].getLevels();
        }
      }
    }
  }
  
  public synchronized void restoreVars(double[] paramArrayOfDouble)
  {
    super.restoreVars(paramArrayOfDouble);
    for (int i = 0; i < this.model.nFac(); i++) {
      this.model.getFac(i).setLevels((int)this.n[i]);
    }
  }
  
  public void linkAOV()
  {
    this.ag = new AnovaGUI(getTitle(), this.model, this.effSD);
    addPiListener(this.ag);
    this.ag.addPiListener(this);
    this.ag.acg = this;
  }
  
  public synchronized void piAction(String paramString)
  {
    if ((this.ignoreActions) || (this.ag == null)) {
      return;
    }
    setVar(paramString, this.ag);
    callMethodFor(paramString);
    updateVars();
    this.ag.ignoreActions = true;
    notifyListeners(paramString);
    this.ag.ignoreActions = false;
  }
  
  public void close()
  {
    if (this.ag != null)
    {
      removePiListener(this.ag);
      this.ag.removePiListener(this);
      this.ag.acg = null;
    }
    dispose();
  }
  
  public void showEMS()
  {
    showText(this.model.EMSString(), "Expected mean squares", 25, 50);
  }
  
  public void report()
  {
    Term localTerm1 = this.fixedTerms[this.comp];
    StringBuffer localStringBuffer = new StringBuffer("Power analysis of comparisons/contrasts\n");
    
    localStringBuffer.append("\nModel:\n");
    for (int i = 0; i < this.model.nTerm(); i++)
    {
      Term localTerm2 = this.model.getTerm(i);
      localStringBuffer.append("  " + Utility.format(localTerm2.getName(), 20));
      int j = (localTerm2 instanceof Factor) ? ((Factor)localTerm2).getLevels() : localTerm2.span();
      if (localTerm2.isRandom()) {
        localStringBuffer.append(" \trandom \t " + j + " levels \tSD = " + this.effSD[i] + "\n");
      } else {
        localStringBuffer.append(" \tfixed \t " + j + " levels\n");
      }
    }
    localStringBuffer.append("\nContrast of means at levels of " + this.fixedNames[this.comp] + "\n");
    
    localStringBuffer.append("Contrast coefficients: ");
    double[] arrayOfDouble1 = this.coefFld.getValue();double d1 = 0.0D;
    for (int k = 0; k < arrayOfDouble1.length; k++)
    {
      localStringBuffer.append(Utility.format(arrayOfDouble1[k], 3) + " ");
      d1 += arrayOfDouble1[k] * arrayOfDouble1[k];
    }
    String str = Utility.format(d1, 3);
    localStringBuffer.append("\nEffect size of interest = " + Utility.format(this.effSize, 3) + "\n");
    localStringBuffer.append("Critical value: " + this.cvStg[this.cvType] + "\n");
    localStringBuffer.append("Powers are computed for " + (2 - this.oneSided) + "-sided tests with alpha = " + this.alpha + ".\n");
    
    double[] arrayOfDouble2 = saveVars();
    for (this.restr = 0; this.restr < this.restrVec.size(); this.restr += 1)
    {
      Factor[] arrayOfFactor = (Factor[])this.restrVec.elementAt(this.restr);
      if (this.restr == 0)
      {
        localStringBuffer.append("\nNo restrictions");
      }
      else
      {
        localStringBuffer.append("\nSame ");
        for (int m = 0; m < arrayOfFactor.length; m++)
        {
          if (m > 0) {
            localStringBuffer.append(" and ");
          }
          localStringBuffer.append(arrayOfFactor[m].getShortName());
        }
      }
      restr_changed();
      click();
      
      localStringBuffer.append("  (" + (int)this.famSize + " means");
      if (this.cvType == this.bonfIndex) {
        localStringBuffer.append(", " + (int)this.bonfDiv + " tests");
      }
      localStringBuffer.append(")\n");
      String[] arrayOfString = this.model.getCompVarString(localTerm1, arrayOfFactor);
      double d2 = PowerCalculator.sqrt(d1 * this.vdf[0]);
      localStringBuffer.append("  Variance = " + str + " * [" + arrayOfString[0] + "]\n");
      localStringBuffer.append("  Estimator = " + str + " * [" + arrayOfString[1] + "]\n");
      localStringBuffer.append("  SE = " + Utility.fixedFormat(d2, 4));
      localStringBuffer.append("    LSC = " + Utility.fixedFormat(this.cv * d2, 4));
      localStringBuffer.append("    d.f. = " + Utility.fixedFormat(this.vdf[1], 1));
      localStringBuffer.append("    Power = " + Utility.format(this.power, 4) + "\n");
    }
    localStringBuffer.append("\n(\"LSC\" = \"Least significant contrast\")\n");
    restoreVars(arrayOfDouble2);
    showText(localStringBuffer.toString(), "Power analysis of comparisons/contrasts", 25, 60);
  }
  
  public void help()
  {
    showText(AnovaGUI.class, "AnovaCompHelp.txt", "Help for contrasts power analysis", 25, 60);
  }
  
  public AnovaCompGUI(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4)
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
  
  public AnovaCompGUI(String paramString, Model paramModel, double[] paramArrayOfDouble)
  {
    super(paramString, false);
    this.model = paramModel;
    this.effSD = paramArrayOfDouble;
    build();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new AnovaCompGUI("Nested-factorial model", "grp + Subj(grp) + trt + grp*trt", 2, "grp 4   Subj 5   trt 3", "Subj");
  }
}
