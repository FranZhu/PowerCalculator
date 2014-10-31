package pc.powercalculator;

import java.awt.Menu;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;
import org.rosuda.JRI.Rengine;
import org.rosuda.REngine.JRI.JRIEngine;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPInteger;
import org.rosuda.REngine.REXPReference;
import org.rosuda.REngine.REngine;

public class PowerCalculatorRengine
  extends PowerCalculator
{
  private static String title = "";
  protected REngine rengine;
  protected REXP renviron = null;
  public HashMap<String, REXP> actionMap = new HashMap();
  
  public PowerCalculatorRengine(String paramString, REXPReference paramREXPReference)
  {
    super(paramString, false);
    findEngine();
    this.renviron = paramREXPReference.resolve();
    try
    {
      this.rengine.assign(".PowerCalculatorEnv", this.renviron, this.renviron);
    }
    catch (Exception localException)
    {
      errmsg("Failed to initialize environment");
    }
    newColumn();
  }
  
  public PowerCalculatorRengine(String paramString, boolean paramBoolean)
  {
    super(paramString, false);
    findEngine();
    this.renviron = null;
    if ((paramBoolean) && (this.rengine.supportsReferences())) {
      try
      {
        this.renviron = this.rengine.newEnvironment(this.rengine.get(".GlobalEnv", null, true), false);
        this.rengine.assign(".PowerCalculatorEnv", this.renviron, this.renviron);
      }
      catch (Exception localException)
      {
        errmsg("Failed to create a new environment");
      }
    }
    newColumn();
  }
  
  private void findEngine()
  {
    this.rengine = REngine.getLastEngine();
    if (this.rengine == null) {
      try
      {
        this.rengine = new JRIEngine(Rengine.getMainEngine());
      }
      catch (Exception localException)
      {
        System.err.println("Unable to get a running REngine");
        return;
      }
    }
  }
  
  public PowerCalculatorRengine(String paramString)
  {
    this(paramString, true);
  }
  
  public void gui() {}
  
  protected void afterSetup()
  {
    int i = this.optMenu.getItemCount() - 6;
    
    this.optMenu.remove(3 + i);
    this.optMenu.remove(2 + i);
    this.optMenu.remove(1 + i);
  }
  
  public void click() {}
  
  public void close()
  {
    Enumeration localEnumeration = ((Vector)this.listeners.clone()).elements();
    while (localEnumeration.hasMoreElements())
    {
      PiListener localPiListener = (PiListener)localEnumeration.nextElement();
      localPiListener.close();
    }
    dispose();
  }
  
  public void errmsg(String paramString1, String paramString2, boolean paramBoolean)
  {
    String str = paramString1 + ": " + paramString2;
    try
    {
      if (paramBoolean) {
        this.rengine.parseAndEval("stop(\"" + str + "\")", this.renviron, true);
      } else {
        this.rengine.parseAndEval("warning(\"" + str + "\")", this.renviron, true);
      }
    }
    catch (Exception localException) {}
  }
  
  public void stackTrace(Throwable paramThrowable)
  {
    errmsg("Stack trace", paramThrowable.toString(), true);
  }
  
  public void setHandler(String paramString1, String paramString2)
  {
    String str = paramString2 + "(.PowerCalculatorEnv)";
    try
    {
      REXP localREXP = this.rengine.parse(str, false);
      this.actionMap.put(paramString1, localREXP);
    }
    catch (Exception localException)
    {
      errmsg("Error in registering event handler " + str + " for " + paramString1 + "; Syntax error?");
    }
  }
  
  public void callMethodFor(String paramString)
  {
    this.actionSource = paramString;
    this.sourceIndex = -1;
    
    REXP localREXP = (REXP)this.actionMap.get(this.actionSource);
    if (localREXP == null) {
      localREXP = (REXP)this.actionMap.get("default");
    }
    if (localREXP == null) {
      return;
    }
    try
    {
      this.rengine.eval(localREXP, this.renviron, true);
    }
    catch (Exception localException)
    {
      System.err.println("Problem in handling action for " + paramString);
    }
  }
  
  public void callMethod(String paramString)
  {
    if ((paramString.equals("guiHelp")) || (paramString.equals("aboutPowerCalculator"))) {
      super.callMethod(paramString);
    } else {
      callMethodFor(paramString);
    }
  }
  
  protected void setVar(String paramString, double paramDouble)
  {
    if (paramString.endsWith("]"))
    {
      setVar(parseArray(paramString), paramDouble);
      return;
    }
    try
    {
      this.rengine.assign(paramString, new REXPDouble(paramDouble), this.renviron);
    }
    catch (Exception localException)
    {
      errmsg("Cannot set double variable " + paramString);
      stackTrace(localException);
    }
  }
  
  protected double getDVar(String paramString)
  {
    try
    {
      REXP localREXP = this.rengine.get(paramString, this.renviron, true);
      if (localREXP != null) {
        return localREXP.asDouble();
      }
      return (0.0D / 0.0D);
    }
    catch (Exception localException)
    {
      errmsg("Cannot get double variable " + paramString);
      stackTrace(localException);
    }
    return (0.0D / 0.0D);
  }
  
  protected void setVar(Object[] paramArrayOfObject, double paramDouble)
  {
    int i = ((Integer)paramArrayOfObject[1]).intValue() + 1;
    String str = "" + paramArrayOfObject[0] + "[" + i + "] <- " + paramDouble;
    try
    {
      this.rengine.parseAndEval(str, this.renviron, true);
    }
    catch (Exception localException)
    {
      errmsg("Cannot evaluate " + str);
      stackTrace(localException);
    }
  }
  
  protected double getDVar(Object[] paramArrayOfObject)
  {
    int i = ((Integer)paramArrayOfObject[1]).intValue() + 1;
    String str = "" + paramArrayOfObject[0] + "[" + i + "]";
    try
    {
      REXP localREXP = this.rengine.parseAndEval(str, this.renviron, true);
      if (localREXP != null) {
        return localREXP.asDouble();
      }
      return (0.0D / 0.0D);
    }
    catch (Exception localException)
    {
      errmsg("Cannot get double variable " + str);
      stackTrace(localException);
    }
    return (0.0D / 0.0D);
  }
  
  protected void setVar(String paramString, int paramInt)
  {
    if (paramString.endsWith("]"))
    {
      setVar(parseArray(paramString), paramInt);
      return;
    }
    try
    {
      this.rengine.assign(paramString, new REXPInteger(paramInt), this.renviron);
    }
    catch (Exception localException)
    {
      errmsg("Cannot set int variable " + paramString);
      stackTrace(localException);
    }
  }
  
  protected int getIVar(String paramString)
  {
    try
    {
      REXP localREXP = this.rengine.parseAndEval(paramString, this.renviron, true);
      if (localREXP != null) {
        return localREXP.asInteger();
      }
      return -9999;
    }
    catch (Exception localException)
    {
      errmsg("Cannot get int variable " + paramString);
      stackTrace(localException);
    }
    return -9999;
  }
  
  protected void setVar(Object[] paramArrayOfObject, int paramInt)
  {
    int i = ((Integer)paramArrayOfObject[1]).intValue() + 1;
    String str = "" + paramArrayOfObject[0] + "[" + i + "] <- as.integer(" + paramInt + ")";
    try
    {
      this.rengine.parseAndEval(str, this.renviron, true);
    }
    catch (Exception localException)
    {
      errmsg("Cannot evaluate " + str);
      stackTrace(localException);
    }
  }
  
  protected int getIVar(Object[] paramArrayOfObject)
  {
    int i = ((Integer)paramArrayOfObject[1]).intValue() + 1;
    String str = "" + paramArrayOfObject[0] + "[" + i + "]";
    try
    {
      REXP localREXP = this.rengine.parseAndEval(str, this.renviron, true);
      if (localREXP != null) {
        return localREXP.asInteger();
      }
      return -9999;
    }
    catch (Exception localException)
    {
      errmsg("Cannot get int variable " + str);
      stackTrace(localException);
    }
    return -9999;
  }
  
  public REXP getEnviron()
  {
    return this.renviron;
  }
  
  public void saveEnviron(String paramString)
  {
    try
    {
      this.rengine.assign(paramString, this.renviron, null);
    }
    catch (Exception localException)
    {
      errmsg("Unable to save environment in R");
    }
  }
  
  public void refresh()
  {
    callMethodFor("default");
    updateVars();
  }
}
