package pc.powercalculator;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Vector;
import pc.awt.DoubleField;
import pc.awt.RVLayout;
import pc.awt.ViewWindow;
import pc.util.Utility;

public class PiGraph
  extends Frame
  implements PiListener, ActionListener, WindowListener
{
  Choice yChoice = new Choice();
  Choice xChoice = new Choice();
  DoubleField fromFld = new DoubleField("", 5);
  DoubleField toFld = new DoubleField("", 5);
  DoubleField byFld = new DoubleField("", 5);
  Checkbox accumChk = new Checkbox("Persistent");
  Button quitButton = new Button("Close");
  Button dataButton = new Button("Show Data");
  Button drawButton = new Button("Redraw");
  MenuBar menuBar = new MenuBar();
  Menu helpMenu = new Menu("Help");
  int cpHeight = 0;
  boolean init = true;
  Vector data = new Vector();
  Vector intComps = new Vector();
  Vector vars = new Vector();
  Vector labs = new Vector();
  Font scaleFont = new Font("SansSerif", 0, 10);
  PowerCalculator powercalculator;
  int xVar;
  int yVar;
  double xMin;
  double xMax;
  double yMin;
  double yMax;
  String xName;
  String yName;
  Object localObject = new MenuItem("Help");
  
  public PiGraph(PowerCalculator paramPowerCalculator)
  {
    this.powercalculator = paramPowerCalculator;
    
    Panel localPanel1 = new Panel(new RVLayout(1, true, true));
    Panel localPanel2 = new Panel(new RVLayout(2, false, true));
    Panel localPanel3 = new Panel(new RVLayout(6, false, true));
    Panel localPanel4 = new Panel(new RVLayout(4, false, true));
    
    localPanel2.add(new Label("Vertical (y) axis"));
    localPanel2.add(this.yChoice);
    localPanel2.add(new Label("Horizontal (x) axis"));
    localPanel2.add(this.xChoice);
    
    localPanel3.add(new Label("from", 2));
    localPanel3.add(this.fromFld);
    localPanel3.add(new Label("to", 2));
    localPanel3.add(this.toFld);
    localPanel3.add(new Label("by", 2));
    localPanel3.add(this.byFld);
    
    localPanel4.add(this.accumChk);
    localPanel4.add(this.dataButton);
    localPanel4.add(this.drawButton);
    localPanel4.add(this.quitButton);
    
    localPanel1.add(localPanel2);
    localPanel1.add(localPanel3);
    localPanel1.add(localPanel4);
    
    Enumeration localEnumeration = this.powercalculator.actors.elements();
    int i = 0;
    while (localEnumeration.hasMoreElements())
    {
      PiComponent localObject1 = (PiComponent)localEnumeration.nextElement();
      if ((localObject1 instanceof DoubleComponent))
      {
        this.vars.addElement(((PiComponent)localObject1).getName());
        this.labs.addElement(((PiComponent)localObject1).getLabel());
        this.xChoice.addItem(((PiComponent)localObject1).getLabel());
        this.yChoice.addItem(((PiComponent)localObject1).getLabel());
        i++;
      }
      else if ((localObject1 instanceof IntComponent))
      {
        this.intComps.addElement(localObject1);
      }
    }
    if (i == 0)
    {
      this.yChoice.addItem("Sorry,");
      this.xChoice.addItem("No variables are available!");
    }
    else
    {
      this.xChoice.select(Math.max(0, i - 2));
      this.yChoice.select(i - 1);
    }
    setLayout(new BorderLayout());
    add(localPanel1, "South");
    
    setTitle("PowerCalculator Graph");
    
    setBackground(paramPowerCalculator.getBackground());
    this.powercalculator.addPiListener(this);
    this.quitButton.addActionListener(this);
    this.drawButton.addActionListener(this);
    this.dataButton.addActionListener(this);
    this.dataButton.setVisible(false);
    addWindowListener(this);
    
    //Object localObject = new MenuItem("Help");
    MenuItem localMenuItem = new MenuItem("About PowerCalculator");
    ((MenuItem)localObject).addActionListener(this);
    localMenuItem.addActionListener(this);
    setMenuBar(this.menuBar);
    this.menuBar.setHelpMenu(this.helpMenu);
    this.helpMenu.add((MenuItem)localObject);
    this.helpMenu.add(localMenuItem);
    

    pack();
    this.drawButton.setLabel("Draw");
    
    this.cpHeight = localPanel1.getSize().height;
    Point localPoint = this.powercalculator.getLocation();
    localPoint.x += this.powercalculator.getSize().width;
    setLocation(localPoint);
    show();
  }
  
  public void close()
  {
    this.powercalculator.removePiListener(this);
    dispose();
  }
  
  private synchronized void computePlotData()
  {
    double d1 = this.fromFld.getValue();
    double d2 = this.byFld.getValue();
    double d3 = this.toFld.getValue();
    if ((Double.isNaN(d1 + d3 + d2)) || (d1 == d3) || (d2 == 0.0D)) {
      return;
    }
    int i = (int)Math.abs((d3 - d1) / d2 + 1.0D);
    if (i < 2) {
      return;
    }
    if ((!this.accumChk.getState()) || (this.xVar != this.xChoice.getSelectedIndex()) || (this.yVar != this.yChoice.getSelectedIndex())) {
      this.data.removeAllElements();
    }
    this.xVar = this.xChoice.getSelectedIndex();
    this.yVar = this.yChoice.getSelectedIndex();
    String str1 = (String)this.vars.elementAt(this.xVar);
    String str2 = (String)this.vars.elementAt(this.yVar);
    

    StringBuffer localStringBuffer = new StringBuffer("# " + this.yChoice.getSelectedItem() + " vs. " + this.xChoice.getSelectedItem() + "\n");
    for (int j = 0; j < this.intComps.size(); j++)
    {
      localObject = (IntComponent)this.intComps.elementAt(j);
      localStringBuffer.append("#   " + ((IntComponent)localObject).getLabel() + ": " + ((IntComponent)localObject).getTextValue() + "\n");
    }
    for (int j = 0; j < this.vars.size(); j++)
    {
      localObject = (String)this.vars.elementAt(j);
      if ((!((String)localObject).equals(str1)) && (!((String)localObject).equals(str2))) {
        localStringBuffer.append("#   " + this.labs.elementAt(j) + " = " + Utility.format(this.powercalculator.getDVar((String)localObject), 5) + "\n");
      }
    }
    localStringBuffer.append(str1 + "\t" + str2 + "\n");
    this.data.addElement(new String(localStringBuffer));
    
    double[] arrayOfDouble1 = new double[i];
    //Object localObject = new double[i];
    double[]  localObject = new double[i];
    double[] arrayOfDouble2 = this.powercalculator.saveVars();
    for (int k = 0; k < i; k++)
    {
      localObject[k] = this.powercalculator.eval(str2, str1, d1 + k * d2);
      arrayOfDouble1[k] = this.powercalculator.getDVar(str1);
    }
    this.powercalculator.restoreVars(arrayOfDouble2);
    this.data.addElement(arrayOfDouble1);
    this.data.addElement(localObject);
  }
  
  void computeRanges()
  {
    int i = 0;
    this.xMin = (this.xMax = this.yMin = this.yMax = 0.0D);
    if (this.data.size() == 0) {
      return;
    }
    Enumeration localEnumeration = this.data.elements();
    while (localEnumeration.hasMoreElements())
    {
      localEnumeration.nextElement();
      double[] arrayOfDouble1 = (double[])localEnumeration.nextElement();
      double[] arrayOfDouble2 = (double[])localEnumeration.nextElement();
      int j = arrayOfDouble1.length;
      for (int k = 0; k < j; k++) {
        if (!Double.isNaN(arrayOfDouble1[k] + arrayOfDouble2[k]))
        {
          if (i == 0)
          {
            this.xMin = (this.xMax = arrayOfDouble1[k]);
            this.yMin = (this.yMax = arrayOfDouble2[k]);
            i = 1;
          }
          if (arrayOfDouble1[k] < this.xMin) {
            this.xMin = arrayOfDouble1[k];
          }
          if (arrayOfDouble1[k] > this.xMax) {
            this.xMax = arrayOfDouble1[k];
          }
          if (arrayOfDouble2[k] < this.yMin) {
            this.yMin = arrayOfDouble2[k];
          }
          if (arrayOfDouble2[k] > this.yMax) {
            this.yMax = arrayOfDouble2[k];
          }
        }
      }
    }
  }
  
  void showData()
  {
    int i = 0;
    StringBuffer localStringBuffer = new StringBuffer();
    Enumeration localEnumeration = this.data.elements();
    double [] localObject;
    while (localEnumeration.hasMoreElements())
    {
      localStringBuffer.append("# Curve number " + ++i + "\n");
      localStringBuffer.append((String)localEnumeration.nextElement());
      localObject = (double[])localEnumeration.nextElement();
      double[] arrayOfDouble = (double[])localEnumeration.nextElement();
      for (int j = 0; j < localObject.length; j++) {
        localStringBuffer.append(Utility.format(localObject[j], 5) + "\t" + Utility.format(arrayOfDouble[j], 5) + "\n");
      }
    }
    localStringBuffer.append("\n");
    Object localObject1 = new ViewWindow("Plot data", 25, 40);
    ((ViewWindow)localObject1).setText(new String(localStringBuffer));
  }
  
  public void paint(Graphics paramGraphics)
  {
    if (this.init) {
      return;
    }
    setTitle(this.yChoice.getSelectedItem() + " vs. " + this.xChoice.getSelectedItem());
    
    Insets localInsets = getInsets();
    Dimension localDimension = getSize();
    
    int j = localDimension.width - 10 - localInsets.left - localInsets.right;
    int k = localDimension.height - 10 - localInsets.top - localInsets.bottom - this.cpHeight;
    int m = 5 + localInsets.left;
    int n = 5 + localInsets.top;
    if ((j <= 0) || (k <= 0)) {
      return;
    }
    paramGraphics.setColor(Color.white);
    paramGraphics.fillRect(m, n, j, k);
    paramGraphics.setColor(Color.lightGray);
    paramGraphics.draw3DRect(m, n, j, k, false);
    paramGraphics.setColor(Color.lightGray.darker());
    paramGraphics.draw3DRect(m + 1, n + 1, j - 2, k - 2, false);
    if ((this.xVar != this.xChoice.getSelectedIndex()) || (this.yVar != this.yChoice.getSelectedIndex())) {
      computePlotData();
    }
    if (this.data.size() == 0) {
      return;
    }
    computeRanges();
    if ((j < 0) || (k < 0)) {
      return;
    }
    double d1 = 1.1D * (this.xMax - this.xMin);double d2 = 1.1D * (this.yMax - this.yMin);
    double d3 = this.xMin - 0.05D * (this.xMax - this.xMin);double d4 = this.yMax + 0.05D * (this.yMax - this.yMin);
    if (d1 == 0.0D) {
      return;
    }
    if (d2 == 0.0D)
    {
      d2 = 1.0D;
      d4 += d2 / 2.0D;
    }
    double[] arrayOfDouble1 = Utility.nice(d4 - d2, d4, 5, false);
    String[] arrayOfString = Utility.fmtNice(arrayOfDouble1);
    FontMetrics localFontMetrics = getFontMetrics(this.scaleFont);
    int i1 = localFontMetrics.getAscent();
    int i2 = 2 * i1;
    int i3 = 0;
    for (int i = 0; i < arrayOfDouble1.length; i++)
    {
      int i6 = localFontMetrics.stringWidth(arrayOfString[i]);
      if (i6 > i3) {
        i3 = i6;
      }
    }
    i3 += i1;
    m += i3;
    n += 5;
    j -= i3 + 5;
    k -= i2 + 5;
    
    int i;
    paramGraphics.setColor(Color.blue.darker());
    paramGraphics.drawRect(m, n, j, k);
    paramGraphics.setFont(this.scaleFont);
    int i5 = m - i1 / 3;
    int i7;
    for (i = 0; i < arrayOfDouble1.length; i++)
    {
      int i6 = (int)(n + k * (d4 - arrayOfDouble1[i]) / d2);
      paramGraphics.drawLine(m, i6, i5, i6);
      i7 = localFontMetrics.stringWidth(arrayOfString[i]);
      paramGraphics.drawString(arrayOfString[i], m - i7 - i1 / 2, i6 + i1 / 3);
    }
    arrayOfDouble1 = Utility.nice(d3, d3 + d1, 5, false);
    arrayOfString = Utility.fmtNice(arrayOfDouble1);
    int i4 = n + k;
    i5 = i4 + i1 / 3;
    int i6 = i5 + i1;
    int i8;
    for (i = 0; i < arrayOfDouble1.length; i++)
    {
      i7 = (int)(m + j * (arrayOfDouble1[i] - d3) / d1);
      paramGraphics.drawLine(i7, i4, i7, i5);
      i8 = localFontMetrics.stringWidth(arrayOfString[i]);
      paramGraphics.drawString(arrayOfString[i], i7 - i8 / 2, i6);
    }
    Color[] arrayOfColor = { Color.black, Color.blue, Color.red, Color.orange, Color.green.darker(), Color.magenta };
    

    i8 = 0;
    Enumeration localEnumeration = this.data.elements();
    while (localEnumeration.hasMoreElements())
    {
      localEnumeration.nextElement();
      paramGraphics.setColor(arrayOfColor[(i8++ % arrayOfColor.length)]);
      double[] arrayOfDouble2 = (double[])localEnumeration.nextElement();
      double[] arrayOfDouble3 = (double[])localEnumeration.nextElement();
      int i9 = 0;
      int i10 = 0;int i11 = 0;
      for (i = 0; i < arrayOfDouble2.length; i++) {
        if (Double.isNaN(arrayOfDouble2[i] + arrayOfDouble3[i]))
        {
          i9 = 0;
        }
        else
        {
          int i12 = (int)(m + j * (arrayOfDouble2[i] - d3) / d1);
          int i13 = (int)(n + k * (d4 - arrayOfDouble3[i]) / d2);
          if (i9 == 0)
          {
            i10 = i12;
            i11 = i13;
            i9 = 1;
          }
          paramGraphics.drawLine(i10, i11, i12, i13);
          i10 = i12;
          i11 = i13;
        }
      }
    }
  }
  
  public void piAction(String paramString)
  {
    computePlotData();
    repaint();
  }
  
  public static ViewWindow showText(Class paramClass, String paramString1, String paramString2, int paramInt1, int paramInt2)
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
    catch (Exception localException) {}
    return localViewWindow;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand().toString();
    if (str.equals("Close"))
    {
      close();
    }
    else if (str.equals("Help"))
    {
      showText(PiGraph.class, "PiGraphHelp.txt", "Graphics help", 25, 50);
    }
    else if (str.equals("About PowerCalculator"))
    {
    }
    else if (str.equals("Show Data"))
    {
      showData();
    }
    else if (str.equals("Draw"))
    {
      setVisible(false);
      this.init = false;
      Dimension localDimension = getSize();
      int i = 3 * (localDimension.width - 10) / 4 + localDimension.height + 10;
      setSize(localDimension.width, i);
      this.dataButton.setVisible(true);
      this.drawButton.setLabel("Redraw");
      setVisible(true);
      this.xVar = this.xChoice.getSelectedIndex();
      this.yVar = this.yChoice.getSelectedIndex();
      
      computePlotData();
      repaint();
    }
    else if (str.equals("Redraw"))
    {
      computePlotData();
      repaint();
    }
  }
  
  public void windowClosing(WindowEvent paramWindowEvent)
  {
    close();
  }
  
  public void windowActivated(WindowEvent paramWindowEvent) {}
  
  public void windowClosed(WindowEvent paramWindowEvent) {}
  
  public void windowDeactivated(WindowEvent paramWindowEvent) {}
  
  public void windowDeiconified(WindowEvent paramWindowEvent) {}
  
  public void windowIconified(WindowEvent paramWindowEvent) {}
  
  public void windowOpened(WindowEvent paramWindowEvent) {}
}
