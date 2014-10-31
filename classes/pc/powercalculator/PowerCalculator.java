package pc.powercalculator;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Stack;
import java.util.Vector;

import pc.awt.Dotplot;
import pc.awt.RVLayout;
import pc.awt.Slider;
import pc.awt.ViewWindow;
import pc.powercalculator.apps.RetroPower;
import pc.util.Closeable;
import pc.util.Solve;
import pc.util.Utility;

public abstract class PowerCalculator
  extends Frame
  implements ActionListener, WindowListener, Closeable
{
  public static final String version = "1.76 - 29 June 2011";
  private Stack subpanels = new Stack();
  private Component master = null;
  public double javaVersion = 1.0D;
  protected Vector panels = new Vector();
  protected Vector actors = new Vector();
  protected Vector listeners = new Vector();
  protected PiPanel panel;
  protected MenuBar menuBar = new MenuBar();
  protected Menu optMenu = new Menu("Options");
  protected Menu helpMenu = new Menu("Help");
  protected Font boldFont = new Font("Serif", 1, 12);
  protected Font bigFont = new Font("Serif", 1, 14);
  protected String actionSource = "init";
  protected int sourceIndex = -1;
  
  public PowerCalculator()
  {
    this("PowerCalculator dialog");
  }
  
  public PowerCalculator(String paramString)
  {
    super(paramString);
    build();
    Utility.setGUIWarn(true);
  }
  
  public PowerCalculator(String paramString, boolean paramBoolean)
  {
    super(paramString);
    if (paramBoolean) {
      build();
    }
  }
  
  public static String getVersion()
  {
    return "1.76 - 29 June 2011";
  }
  
  public void build()
  {
    try
    {
      Class.forName("java.awt.event.ActionListener");
      this.javaVersion = 1.1D;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      errmsg("<init>", "JVM version " + this.javaVersion + " is too old." + "  Need at least version 1.1", true);
    }
    MenuItem localMenuItem1 = new MenuItem("Graph...");
    MenuItem localMenuItem2 = new MenuItem("Quit");
    localMenuItem1.addActionListener(this);
    localMenuItem2.addActionListener(this);
    setMenuBar(this.menuBar);
    this.menuBar.add(this.optMenu);
    this.menuBar.setHelpMenu(this.helpMenu);
    setBackground(new Color(220, 220, 255));
    
    newColumn();
    this.optMenu.add(localMenuItem1);
    
    beforeSetup();
    
    gui();
    click();
    updateVars();
    


    PiPanel localPiPanel = new PiPanel(new RVLayout(this.panels.size(), 0, 0, false, true));
    for (int i = 0; i < this.panels.size(); i++) {
      localPiPanel.add((PiPanel)this.panels.elementAt(i));
    }
    this.optMenu.addSeparator();
    menuItem("postHocRant", "Post hoc power...", this.optMenu);
    menuItem("cohenRant", "Cohen's effect sizes...", this.optMenu);
    
    this.optMenu.addSeparator();
    this.optMenu.add(localMenuItem2);
    
    this.helpMenu.addSeparator();
    menuItem("guiHelp", "GUI help", this.helpMenu);
    menuItem("aboutPowerCalculator", "About PowerCalculator", this.helpMenu);
    
    addWindowListener(this);
    

    setLayout(new BorderLayout());
    add(localPiPanel, "Center");
    
    afterSetup();
    
    pack();
    show();
    
    afterShow();
  }
  
  public void aboutPowerCalculator () {
	  
  }
  
  public void errmsg(String paramString1, String paramString2, boolean paramBoolean)
  {
    String str = paramString1 + ": " + paramString2;
    if (paramBoolean) {
      Utility.error(str, this);
    } else {
      Utility.warning(str);
    }
  }
  
  public void errmsg(String paramString1, String paramString2)
  {
    errmsg(paramString1, paramString2, false);
  }
  
  public void errmsg(String paramString)
  {
    errmsg(paramString, (String)null, false);
  }
  
  public void stackTrace(Throwable paramThrowable)
  {
    Utility.error(paramThrowable, this);
  }
  
  public void close()
  {
    Enumeration localEnumeration = ((Vector)this.listeners.clone()).elements();
    while (localEnumeration.hasMoreElements())
    {
      PiListener localPiListener = (PiListener)localEnumeration.nextElement();
      localPiListener.close();
    }
    dispose();
    if (this.master == null) {
      System.exit(0);
    }
  }
  
  public void addComponent(String paramString1, String paramString2, Component paramComponent)
  {
    this.panel.add(paramComponent);
    PiActionAdapter localPiActionAdapter = new PiActionAdapter(paramString1, paramString2, paramComponent);
    localPiActionAdapter.addActionListener(this);
  }
  
  public void addComponent(String paramString, Component paramComponent)
  {
    addComponent(paramString, paramString, paramComponent);
  }
  
  public void addVar(String paramString, DoubleComponent paramDoubleComponent, double paramDouble)
  {
    this.panel.add((Component)paramDoubleComponent);
    paramDoubleComponent.addActionListener(this);
    setVar(paramString, paramDouble);
    this.actors.addElement(paramDoubleComponent);
  }
  
  public void addVar(String paramString, IntComponent paramIntComponent, int paramInt)
  {
    this.panel.add((Component)paramIntComponent);
    paramIntComponent.addActionListener(this);
    setVar(paramString, paramInt);
    this.actors.addElement(paramIntComponent);
  }
  
  public void click()
  {
    errmsg("click()", "Method needed to handle events.", false);
  }
  
  public void gui()
  {
    errmsg("gui()", "Method needed to do anything useful.");
  }
  
  protected void beforeSetup() {}
  
  protected void afterSetup() {}
  
  protected void afterShow() {}
  
  Object[] parseArray(String paramString)
  {
    int i = paramString.indexOf("[");int j = paramString.indexOf("]");
    String str = paramString.substring(0, i);
    Integer localInteger = new Integer(paramString.substring(i + 1, j));
    return new Object[] { str, localInteger };
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
      Field localField = getClass().getField(paramString);
      localField.setDouble(this, paramDouble);
    }
    catch (Exception localException)
    {
      errmsg("setVar(String, double)", "Can't set value of " + paramString);
      
      stackTrace(localException);
    }
  }
  
  protected double getDVar(String paramString)
  {
    if (paramString.endsWith("]")) {
      return getDVar(parseArray(paramString));
    }
    try
    {
      Field localField = getClass().getField(paramString);
      return localField.getDouble(this);
    }
    catch (Exception localException)
    {
      errmsg("getDVar(String)", "Can't get value of " + paramString);
      
      stackTrace(localException);
    }
    return (0.0D / 0.0D);
  }
  
  protected void setVar(Object[] paramArrayOfObject, double paramDouble)
  {
    String str = (String)paramArrayOfObject[0];
    int i = ((Integer)paramArrayOfObject[1]).intValue();
    try
    {
      Object localObject = getClass().getField(str).get(this);
      Array.setDouble(localObject, i, paramDouble);
    }
    catch (Exception localException)
    {
      errmsg("setVar(Object[], double)", "Can't set value of " + str + "[" + i + "]\n");
      

      stackTrace(localException);
    }
  }
  
  protected double getDVar(Object[] paramArrayOfObject)
  {
    String str = (String)paramArrayOfObject[0];
    int i = ((Integer)paramArrayOfObject[1]).intValue();
    try
    {
      Object localObject = getClass().getField(str).get(this);
      return Array.getDouble(localObject, i);
    }
    catch (Exception localException)
    {
      errmsg("getDVar(Object[])", "Can't get value of " + str + "[" + i + "]\n");
      

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
      Field localField = getClass().getField(paramString);
      localField.setInt(this, paramInt);
    }
    catch (Exception localException)
    {
      errmsg("setVar(String, int)", "Can't set value of " + paramString);
      
      stackTrace(localException);
    }
  }
  
  protected int getIVar(String paramString)
  {
    if (paramString.endsWith("]")) {
      return getIVar(parseArray(paramString));
    }
    try
    {
      Field localField = getClass().getField(paramString);
      return localField.getInt(this);
    }
    catch (Exception localException)
    {
      errmsg("getIVar(String)", "Can't get value of " + paramString);
      
      stackTrace(localException);
    }
    return -9999;
  }
  
  protected void setVar(Object[] paramArrayOfObject, int paramInt)
  {
    String str = (String)paramArrayOfObject[0];
    int i = ((Integer)paramArrayOfObject[1]).intValue();
    try
    {
      Object localObject = getClass().getField(str).get(this);
      Array.setInt(localObject, i, paramInt);
    }
    catch (Exception localException)
    {
      errmsg("setVar(Object[], int)", "Can't set value of " + str + "[" + i + "]");
      
      stackTrace(localException);
    }
  }
  
  protected int getIVar(Object[] paramArrayOfObject)
  {
    String str = (String)paramArrayOfObject[0];
    int i = ((Integer)paramArrayOfObject[1]).intValue();
    try
    {
      Object localObject = getClass().getField(str).get(this);
      return Array.getInt(localObject, i);
    }
    catch (Exception localException)
    {
      errmsg("getIVar(Object[])", "Can't get value of " + str + "[" + i + "]");
      
      stackTrace(localException);
    }
    return -9999;
  }
  
  protected void setVar(String paramString, PowerCalculator paramPowerCalculator)
  {
    Object localObject1;
    Object localObject2;
    if (paramString.endsWith("]"))
    {
      localObject1 = parseArray(paramString);
      localObject2 = (String)(parseArray(paramString)[0]);
      try
      {
        Field localField = getClass().getField((String)localObject2);
        Class localClass = localField.getType();
        if (localClass == new double[0].getClass()) {
          setVar((Object[])localObject1, paramPowerCalculator.getDVar(paramString));
        } else if (localClass == new int[0].getClass()) {
          setVar((Object[])localObject1, paramPowerCalculator.getIVar(paramString));
        }
        return;
      }
      catch (Exception localException2)
      {
        return;
      }
    }
    try
    {
      localObject1 = getClass().getField(paramString);
      localObject2 = ((Field)localObject1).getType();
      if (localObject2 == Double.TYPE) {
        setVar(paramString, paramPowerCalculator.getDVar(paramString));
      } else if (localObject2 == Integer.TYPE) {
        setVar(paramString, paramPowerCalculator.getIVar(paramString));
      }
      return;
    }
    catch (Exception localException1) {}
  }
  
  public void callMethod(String paramString)
  {
    try
    {
      Method localMethod = getClass().getMethod(paramString, null);
      localMethod.invoke(this, null);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      errmsg("callMethod(\"" + paramString + "\")", localInvocationTargetException.toString(), false);
      localInvocationTargetException.getTargetException().printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      errmsg("callMethod(\"" + paramString + "\")", localThrowable.toString(), false);
      stackTrace(localThrowable);
    }
  }
  
  public void callMethodFor(String paramString)
  {
    Object localObject;
    if (paramString.endsWith("]"))
    {
      localObject = parseArray(paramString);
      this.actionSource = ((String)parseArray(paramString)[0]);
      this.sourceIndex = ((Integer)parseArray(paramString)[1]).intValue();
    }
    else
    {
      this.actionSource = paramString;
      this.sourceIndex = -1;
    }
    try
    {
      localObject = getClass().getMethod(this.actionSource + "_changed", null);
      ((Method)localObject).invoke(this, null);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      click();
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      errmsg("callMethodFor(\"" + paramString + "\")", localInvocationTargetException.toString(), false);
      localInvocationTargetException.getTargetException().printStackTrace();
    }
    catch (Exception localException)
    {
      errmsg("callMethodFor(\"" + paramString + "\")", localException.toString(), false);
      stackTrace(localException);
    }
  }
  
  public void updateVars()
  {
    Enumeration localEnumeration = this.actors.elements();
    while (localEnumeration.hasMoreElements())
    {
      Object localObject1 = localEnumeration.nextElement();
      Object localObject2;
      if ((localObject1 instanceof DoubleComponent))
      {
        localObject2 = (DoubleComponent)localObject1;
        ((DoubleComponent)localObject2).setValue(getDVar(((PiComponent)localObject2).getName()));
      }
      else if ((localObject1 instanceof IntComponent))
      {
        localObject2 = (IntComponent)localObject1;
        ((IntComponent)localObject2).setValue(getIVar(((PiComponent)localObject2).getName()));
      }
      else
      {
        errmsg("updateVars()", "Unsupported type: " + localObject1.getClass());
      }
    }
  }
  
  public void newColumn()
  {
    this.panel = new PiPanel(new RVLayout(1, false, true));
    this.panels.addElement(this.panel);
  }
  
  public void beginSubpanel(int paramInt)
  {
    this.subpanels.push(this.panel);
    this.panel = new PiPanel(new RVLayout(paramInt, false, true));
  }
  
  public void beginSubpanel(int paramInt, Color paramColor)
  {
    this.subpanels.push(this.panel);
    this.panel = new PiPanel(new RVLayout(paramInt, false, true));
    border(paramColor);
  }
  
  public void beginSubpanel(int paramInt, boolean paramBoolean)
  {
    this.subpanels.push(this.panel);
    this.panel = new PiPanel(new RVLayout(paramInt, false, true));
    set3D(paramBoolean);
  }
  
  public void endSubpanel()
  {
    if (this.subpanels.empty()) {
      errmsg("endSubpanel()", "Subpanel stack is empty", true);
    }
    PiPanel localPiPanel = this.panel;
    this.panel = ((PiPanel)this.subpanels.pop());
    this.panel.add(localPiPanel);
  }
  
  public void border(Color paramColor)
  {
    this.panel.setBorderColor(paramColor);
    this.panel.setBorderType(1);
  }
  
  public void set3D(boolean paramBoolean)
  {
    this.panel.setBorderColor(null);
    this.panel.setBorderType(paramBoolean ? 2 : 3);
  }
  
  public void filler()
  {
    RVLayout localRVLayout = (RVLayout)this.panel.getLayout();
    localRVLayout.vertFill(this.panel);
    this.panel.setStretchable(true);
  }
  
  public void postHocRant()
  {
    RetroPower localRetroPower = new RetroPower();
    localRetroPower.setMaster(this);
  }
  
  public void cohenRant()
  {
    showText(PowerCalculator.class, "Cohen.txt", "Cohen's effect sizes", 25, 60);
  }
  
  public void guiHelp()
  {
    showText(PowerCalculator.class, "PowerCalculatorHelp.txt", "PowerCalculator Help", 25, 50);
  }
  
  
  public ViewWindow showText(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    ViewWindow localViewWindow = new ViewWindow(paramString2, paramInt1, paramInt2);
    localViewWindow.ta.setVisible(false);
    localViewWindow.setText(paramString1);
    localViewWindow.setTop();
    localViewWindow.ta.setVisible(true);
    return localViewWindow;
  }
  
  public ViewWindow showText(Class paramClass, String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    ViewWindow localViewWindow = new ViewWindow(paramString2, paramInt1, paramInt2);
    try
    {
      localViewWindow.ta.setVisible(false);
      InputStream localInputStream = paramClass.getResourceAsStream(paramString1);
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localInputStream));
      String str;
      while ((str = localBufferedReader.readLine()) != null) {
        localViewWindow.append(str + "\n");
      }
      localInputStream.close();
      localViewWindow.setTop();
      localViewWindow.ta.setVisible(true);
    }
    catch (Exception localException)
    {
      errmsg("showText(Class,String,title,int,int)", "Can't display \"" + paramString1 + "\"", false);
    }
    return localViewWindow;
  }
  
  public double solve(String paramString1, String paramString2, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    PowerCalculatorAux localPowerCalculatorAux = new PowerCalculatorAux(paramString1, paramString2, this);
    return solve(localPowerCalculatorAux, paramDouble1, paramDouble2, paramDouble3);
  }
  
  public double solve(PowerCalculatorAux paramPowerCalculatorAux, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double[] arrayOfDouble = saveVars();
    double d = Solve.search(paramPowerCalculatorAux, paramDouble1, paramDouble2, paramDouble3);
    restoreVars(arrayOfDouble);
    return d;
  }
  
  public PiComponent getComponent(String paramString)
  {
    Enumeration localEnumeration = this.actors.elements();
    while (localEnumeration.hasMoreElements())
    {
      PiComponent localPiComponent = (PiComponent)localEnumeration.nextElement();
      if (localPiComponent.getName().equals(paramString)) {
        return localPiComponent;
      }
    }
    return null;
  }
  
  public void setVisible(String paramString, boolean paramBoolean)
  {
    ((Component)getComponent(paramString)).setVisible(paramBoolean);
  }
  
  public void relabel(String paramString1, String paramString2)
  {
    PiComponent localPiComponent = getComponent(paramString1);
    localPiComponent.setName(paramString1, paramString2);
  }
  
  public void label(String paramString, Font paramFont)
  {
    Label localLabel = new Label(paramString);
    localLabel.setFont(paramFont);
    this.panel.add(localLabel);
  }
  
  public void label(String paramString)
  {
    label(paramString, this.bigFont);
  }
  
  public void slider(String paramString1, String paramString2, double paramDouble1, double paramDouble2, double paramDouble3, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
  {
    PiSlider localPiSlider = new PiSlider(paramString1, paramString2, paramDouble1);
    localPiSlider.setMinimum(paramDouble2);
    localPiSlider.setMaximum(paramDouble3);
    localPiSlider.setMutable(!paramBoolean1, !paramBoolean2);
    localPiSlider.setDigits(paramInt);
    localPiSlider.setEditable(paramBoolean3);
    addVar(paramString1, localPiSlider, paramDouble1);
  }
  
  public void slider(String paramString1, String paramString2, double paramDouble, int paramInt)
  {
    PiSlider localPiSlider = new PiSlider(paramString1, paramString2, paramDouble);
    localPiSlider.setDigits(paramInt);
    addVar(paramString1, localPiSlider, paramDouble);
  }
  
  public void slider(String paramString1, String paramString2, double paramDouble)
  {
    slider(paramString1, paramString2, paramDouble, 4);
  }
  
  public void slider(String paramString, double paramDouble, int paramInt)
  {
    slider(paramString, paramString, paramDouble, paramInt);
  }
  
  public void slider(String paramString, double paramDouble)
  {
    slider(paramString, paramString, paramDouble);
  }
  
  public void bar(String paramString1, String paramString2, double paramDouble, int paramInt)
  {
    if (paramDouble > 0.0D) {
      slider(paramString1, paramString2, paramDouble, 0.0D, 1.5D * paramDouble, paramInt, true, false, true);
    } else if (paramDouble < 0.0D) {
      slider(paramString1, paramString2, paramDouble, 1.5D * paramDouble, 0.0D, paramInt, false, true, true);
    } else {
      slider(paramString1, paramString2, paramDouble, 0.0D, 1.0D, paramInt, true, false, true);
    }
  }
  
  public void bar(String paramString1, String paramString2, double paramDouble)
  {
    bar(paramString1, paramString2, paramDouble, 4);
  }
  
  public void bar(String paramString, double paramDouble)
  {
    bar(paramString, paramString, paramDouble);
  }
  
  public void bar(String paramString, double paramDouble, int paramInt)
  {
    bar(paramString, paramString, paramDouble, 4);
  }
  
  public void interval(String paramString1, String paramString2, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    slider(paramString1, paramString2, paramDouble1, paramDouble2, paramDouble3, 4, true, true, true);
  }
  
  public void interval(String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    interval(paramString, paramString, paramDouble1, paramDouble2, paramDouble3);
  }
  
  public void oslider(String paramString1, String paramString2, double paramDouble, int paramInt)
  {
    PiSlider localPiSlider = new PiSlider(paramString1, paramString2, paramDouble);
    localPiSlider.setDigits(paramInt);
    localPiSlider.setEditable(false);
    addVar(paramString1, localPiSlider, paramDouble);
  }
  
  public void oslider(String paramString, double paramDouble, int paramInt)
  {
    oslider(paramString, paramString, paramDouble, paramInt);
  }
  
  public void oslider(String paramString1, String paramString2, double paramDouble)
  {
    oslider(paramString1, paramString2, paramDouble, 4);
  }
  
  public void oslider(String paramString, double paramDouble)
  {
    oslider(paramString, paramString, paramDouble);
  }
  
  public void obar(String paramString1, String paramString2, double paramDouble, int paramInt)
  {
    if (paramDouble > 0.0D) {
      slider(paramString1, paramString2, paramDouble, 0.0D, 1.5D * paramDouble, paramInt, true, false, false);
    } else if (paramDouble < 0.0D) {
      slider(paramString1, paramString2, paramDouble, 1.5D * paramDouble, 0.0D, paramInt, false, true, false);
    } else {
      slider(paramString1, paramString2, paramDouble, 0.0D, 1.0D, paramInt, true, false, false);
    }
  }
  
  public void obar(String paramString1, String paramString2, double paramDouble)
  {
    obar(paramString1, paramString2, paramDouble, 4);
  }
  
  public void obar(String paramString, double paramDouble)
  {
    obar(paramString, paramString, paramDouble);
  }
  
  public void obar(String paramString, double paramDouble, int paramInt)
  {
    obar(paramString, paramString, paramDouble, paramInt);
  }
  
  public void ointerval(String paramString1, String paramString2, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    slider(paramString1, paramString2, paramDouble1, paramDouble2, paramDouble3, 4, true, true, false);
  }
  
  public void ointerval(String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    ointerval(paramString, paramString, paramDouble1, paramDouble2, paramDouble3);
  }
  
  public void field(String paramString1, String paramString2, double paramDouble, int paramInt1, int paramInt2)
  {
    PiDoubleField localPiDoubleField = new PiDoubleField(paramString1, paramString2, paramDouble, paramInt1, paramInt2);
    
    addVar(paramString1, localPiDoubleField, paramDouble);
  }
  
  public void field(String paramString1, String paramString2, double paramDouble)
  {
    PiDoubleField localPiDoubleField = new PiDoubleField(paramString1, paramString2, paramDouble);
    addVar(paramString1, localPiDoubleField, paramDouble);
  }
  
  public void field(String paramString, double paramDouble)
  {
    field(paramString, paramString, paramDouble);
  }
  
  public void ofield(String paramString1, String paramString2, double paramDouble, int paramInt1, int paramInt2)
  {
    PiDoubleField localPiDoubleField = new PiDoubleField(paramString1, paramString2, paramDouble, paramInt1, paramInt2);
    
    localPiDoubleField.setEditable(false);
    addVar(paramString1, localPiDoubleField, paramDouble);
  }
  
  public void ofield(String paramString1, String paramString2, double paramDouble)
  {
    PiDoubleField localPiDoubleField = new PiDoubleField(paramString1, paramString2, paramDouble);
    localPiDoubleField.setEditable(false);
    addVar(paramString1, localPiDoubleField, paramDouble);
  }
  
  public void ofield(String paramString, double paramDouble)
  {
    ofield(paramString, paramString, paramDouble);
  }
  
  public void otext(String paramString1, String paramString2, double paramDouble, int paramInt)
  {
    PiDoubleText localPiDoubleText = new PiDoubleText(paramString1, paramString2, paramDouble, paramInt);
    localPiDoubleText.setFont(this.boldFont);
    addVar(paramString1, localPiDoubleText, paramDouble);
  }
  
  public void otext(String paramString1, String paramString2, double paramDouble)
  {
    otext(paramString1, paramString2, paramDouble, 4);
  }
  
  public PiArrayField arrayField(String paramString1, String paramString2, double[] paramArrayOfDouble, int paramInt)
  {
    PiArrayField localPiArrayField = new PiArrayField(paramString1, paramString2, paramArrayOfDouble, paramInt);
    localPiArrayField.addActionListener(this);
    this.panel.add(localPiArrayField);
    return localPiArrayField;
  }
  
  public PiArrayField arrayField(String paramString1, String paramString2, double[] paramArrayOfDouble)
  {
    return arrayField(paramString1, paramString2, paramArrayOfDouble, 12);
  }
  
  public Dotplot dotplot(double[] paramArrayOfDouble)
  {
    Dotplot localPiDotplot = new Dotplot(paramArrayOfDouble);
    localPiDotplot.addActionListener(this);
    this.panel.add(localPiDotplot);
    return localPiDotplot;
  }
  
  public PiDotplot dotplot(String str1, String str2, double[] paramArrayOfDouble)
  {
    PiDotplot localPiDotplot = new PiDotplot(str1, str2, paramArrayOfDouble);
    localPiDotplot.addActionListener(this);
    this.panel.add(localPiDotplot);
    return localPiDotplot;
  }
  
  public void checkbox(String paramString1, String paramString2, int paramInt)
  {
    paramInt = paramInt == 0 ? 0 : 1;
    PiCheckbox localPiCheckbox = new PiCheckbox(paramString1, paramString2, paramInt);
    addVar(paramString1, localPiCheckbox, paramInt);
  }
  
  public void checkbox(String paramString, int paramInt)
  {
    checkbox(paramString, paramString, paramInt);
  }
  
  public void menuCheckbox(String paramString1, String paramString2, int paramInt, Menu paramMenu)
  {
    paramInt = paramInt == 0 ? 0 : 1;
    PiMenuCheckbox localPiMenuCheckbox = new PiMenuCheckbox(paramString1, paramString2, paramInt);
    paramMenu.add(localPiMenuCheckbox);
    localPiMenuCheckbox.addActionListener(this);
    setVar(paramString1, paramInt);
    this.actors.addElement(localPiMenuCheckbox);
  }
  
  public void menuCheckbox(String paramString1, String paramString2, int paramInt)
  {
    menuCheckbox(paramString1, paramString2, paramInt, this.optMenu);
  }
  
  public void menuCheckbox(String paramString, int paramInt, Menu paramMenu)
  {
    menuCheckbox(paramString, paramString, paramInt, paramMenu);
  }
  
  public void menuCheckbox(String paramString, int paramInt)
  {
    menuCheckbox(paramString, paramString, paramInt, this.optMenu);
  }
  
  public void choice(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    PiChoice localPiChoice = new PiChoice(paramString1, paramString2, paramArrayOfString, paramInt);
    addVar(paramString1, localPiChoice, paramInt);
  }
  
  public void choice(String paramString, String[] paramArrayOfString, int paramInt)
  {
    choice(paramString, paramString, paramArrayOfString, paramInt);
  }
  
  public void choice(String paramString1, String paramString2, double[] paramArrayOfDouble, int paramInt)
  {
    PiNumChoice localPiNumChoice = new PiNumChoice(paramString1, paramString2, paramArrayOfDouble, paramInt);
    addVar(paramString1, localPiNumChoice, paramArrayOfDouble[paramInt]);
  }
  
  public void choice(String paramString, double[] paramArrayOfDouble, int paramInt)
  {
    choice(paramString, paramString, paramArrayOfDouble, paramInt);
  }
  
  public void radio(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    PiRadio localPiRadio = new PiRadio(paramString1, paramString2, paramArrayOfString, paramInt1, paramInt2);
    addVar(paramString1, localPiRadio, paramInt1);
  }
  
  public void hradio(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    radio(paramString1, paramString2, paramArrayOfString, paramInt, 1 + paramArrayOfString.length);
  }
  
  public void hradio(String paramString, String[] paramArrayOfString, int paramInt)
  {
    hradio(paramString, paramString, paramArrayOfString, paramInt);
  }
  
  public void vradio(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    radio(paramString1, paramString2, paramArrayOfString, paramInt, 1);
  }
  
  public void vradio(String paramString, String[] paramArrayOfString, int paramInt)
  {
    vradio(paramString, paramString, paramArrayOfString, paramInt);
  }
  
  public void button(String paramString1, String paramString2)
  {
    PiButton localPiButton = new PiButton(paramString1, paramString2);
    localPiButton.addActionListener(this);
    this.panel.add(localPiButton);
  }
  
  public void menuItem(String paramString1, String paramString2, Menu paramMenu)
  {
    PiMenuItem localPiMenuItem = new PiMenuItem(paramString1, paramString2);
    localPiMenuItem.addActionListener(this);
    paramMenu.add(localPiMenuItem);
  }
  
  public void menuItem(String paramString1, String paramString2)
  {
    menuItem(paramString1, paramString2, this.optMenu);
  }
  
  public void component(String paramString1, String paramString2, Component paramComponent)
  {
    addComponent(paramString1, paramString2, paramComponent);
  }
  
  public void component(String paramString, Component paramComponent)
  {
    addComponent(paramString, paramString, paramComponent);
  }
  
  public void addPiListener(PiListener paramPiListener)
  {
    this.listeners.addElement(paramPiListener);
  }
  
  public void removePiListener(PiListener paramPiListener)
  {
    this.listeners.removeElement(paramPiListener);
  }
  
  public synchronized void notifyListeners(String paramString)
  {
    notifyListeners(paramString, null);
  }
  
  public synchronized void notifyListeners(String paramString, PiListener paramPiListener)
  {
    Enumeration localEnumeration = this.listeners.elements();
    while (localEnumeration.hasMoreElements())
    {
      setCursor(Cursor.getPredefinedCursor(3));
      PiListener localPiListener = (PiListener)localEnumeration.nextElement();
      if (localPiListener != paramPiListener) {
        localPiListener.piAction(paramString);
      }
      setCursor(Cursor.getDefaultCursor());
    }
  }
  
  public synchronized double[] saveVars()
  {
    double[] arrayOfDouble = new double[this.actors.size()];
    for (int i = 0; i < this.actors.size(); i++)
    {
      PiComponent localPiComponent = (PiComponent)this.actors.elementAt(i);
      arrayOfDouble[i] = ((localPiComponent instanceof DoubleComponent) ? getDVar(localPiComponent.getName()) : getIVar(localPiComponent.getName()));
    }
    return arrayOfDouble;
  }
  
  public synchronized void restoreVars(double[] paramArrayOfDouble)
  {
    for (int i = 0; i < this.actors.size(); i++)
    {
      PiComponent localPiComponent = (PiComponent)this.actors.elementAt(i);
      if ((localPiComponent instanceof DoubleComponent))
      {
        setVar(localPiComponent.getName(), paramArrayOfDouble[i]);
        ((DoubleComponent)localPiComponent).setValue(paramArrayOfDouble[i]);
      }
      else
      {
        setVar(localPiComponent.getName(), (int)paramArrayOfDouble[i]);
        ((IntComponent)localPiComponent).setValue((int)paramArrayOfDouble[i]);
      }
    }
  }
  
  protected double eval(String paramString1, String paramString2, double paramDouble)
  {
    setVar(paramString2, paramDouble);
    callMethodFor(paramString2);
    return getDVar(paramString1);
  }
  
  public void setMaster(Component paramComponent)
  {
    this.master = paramComponent;
  }
  
  public Component getMaster()
  {
    return this.master;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject = paramActionEvent.getSource();
    if ((localObject instanceof DoubleComponent))
    {
      String str = ((DoubleComponent)localObject).getName();
      double d = ((DoubleComponent)localObject).getValue();
      if (feq(d, getDVar(str))) {
        return;
      }
      setVar(str, d);
      callMethodFor(str);
      updateVars();
      notifyListeners(str);
      return;
    }
    if ((localObject instanceof IntComponent))
    {
      String str = ((IntComponent)localObject).getName();
      int i = ((IntComponent)localObject).getValue();
      if (i == getIVar(str)) {
        return;
      }
      setVar(str, i);
      callMethodFor(str);
      updateVars();
      notifyListeners(str);
      return;
    }
    if ((localObject instanceof ActionComponent))
    {
      String str = ((ActionComponent)localObject).getName();
      this.actionSource = ((ActionComponent)localObject).getLabel();
      callMethod(str);
      updateVars();
      notifyListeners(str);
      return;
    }
    String str = paramActionEvent.getActionCommand().toString();
    if (str.equals("Graph...")) {
      new PiGraph(this);
    } else if (str.equals("Quit")) {
      close();
    }
  }
  
  public void windowClosing(WindowEvent paramWindowEvent)
  {
    close();
  }
  
  private static final double rlog10 = 1.0D / Math.log(10.0D);
  
  public void windowActivated(WindowEvent paramWindowEvent) {}
  
  public void windowClosed(WindowEvent paramWindowEvent) {}
  
  public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
  public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
  public void windowIconified(WindowEvent paramWindowEvent) {}
  
  public void windowOpened(WindowEvent paramWindowEvent) {}
  
  public static double sin(double paramDouble)
  {
    return Math.sin(paramDouble);
  }
  
  public static double cos(double paramDouble)
  {
    return Math.cos(paramDouble);
  }
  
  public static double tan(double paramDouble)
  {
    return Math.tan(paramDouble);
  }
  
  public static double asin(double paramDouble)
  {
    return Math.asin(paramDouble);
  }
  
  public static double acos(double paramDouble)
  {
    return Math.acos(paramDouble);
  }
  
  public static double atan(double paramDouble)
  {
    return Math.atan(paramDouble);
  }
  
  public static double atan(double paramDouble1, double paramDouble2)
  {
    return Math.atan2(paramDouble1, paramDouble2);
  }
  
  public static double atan2(double paramDouble1, double paramDouble2)
  {
    return Math.atan2(paramDouble1, paramDouble2);
  }
  
  public static double log(double paramDouble)
  {
    return Math.log(paramDouble);
  }
  
  public static double log10(double paramDouble)
  {
    return rlog10 * Math.log(paramDouble);
  }
  
  public static double exp(double paramDouble)
  {
    return Math.exp(paramDouble);
  }
  
  public static double sqrt(double paramDouble)
  {
    return Math.sqrt(paramDouble);
  }
  
  public static long round(double paramDouble)
  {
    return Math.round(paramDouble);
  }
  
  public static double round(double paramDouble, int paramInt)
  {
    double d = Math.pow(10.0D, paramInt);
    return Math.round(paramDouble * d) / d;
  }
  
  public static double pow(double paramDouble1, double paramDouble2)
  {
    return Math.pow(paramDouble1, paramDouble2);
  }
  
  public static int abs(int paramInt)
  {
    return Math.abs(paramInt);
  }
  
  public static double abs(double paramDouble)
  {
    return Math.abs(paramDouble);
  }
  
  public static double floor(double paramDouble)
  {
    return Math.floor(paramDouble);
  }
  
  public static double ceil(double paramDouble)
  {
    return Math.ceil(paramDouble);
  }
  
  public static double max(double paramDouble1, double paramDouble2)
  {
    return Math.max(paramDouble1, paramDouble2);
  }
  
  public static double min(double paramDouble1, double paramDouble2)
  {
    return Math.min(paramDouble1, paramDouble2);
  }
  
  public static int max(int paramInt1, int paramInt2)
  {
    return Math.max(paramInt1, paramInt2);
  }
  
  public static int min(int paramInt1, int paramInt2)
  {
    return Math.min(paramInt1, paramInt2);
  }
  
  public static int sign(double paramDouble)
  {
    return paramDouble < 0.0D ? -1 : paramDouble == 0.0D ? 0 : 1;
  }
  
  public static boolean feq(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    return abs(paramDouble1 - paramDouble2) < paramDouble3;
  }
  
  public static boolean feq(double paramDouble1, double paramDouble2)
  {
    return feq(paramDouble1, paramDouble2, 1.0E-012D);
  }
  
  public static double time()
  {
    return System.currentTimeMillis() / 1000.0D;
  }
  
  private static double theSeed = -1.0D;
  private static double theMult = 63069.0D;
  private static double theTerm = 0.84763521D;
  private static boolean flag = true;
  private static double u1;
  private static double u2;
  private static double r;
  
  public static double random()
  {
    if (theSeed < 0.0D) {
      seed();
    }
    double d = theMult * theSeed + theTerm;
    theSeed = d - Math.floor(d);
    return theSeed;
  }
  
  public static synchronized double nrand()
  {
    flag = !flag;
    if (flag) {
      return r * u2;
    }
    do
    {
      u1 = 2.0D * random() - 1.0D;
      u2 = 2.0D * random() - 1.0D;
      r = u1 * u1 + u2 * u2;
    } while (r >= 1.0D);
    r = Math.sqrt(-2.0D * Math.log(r) / r);
    return r * u1;
  }
  
  public static double seed(double paramDouble)
  {
    theSeed = paramDouble;
    return paramDouble;
  }
  
  public static double seed()
  {
    theSeed = (System.currentTimeMillis() & 0xFFFF) / 65536.0D;
    return theSeed;
  }
}
