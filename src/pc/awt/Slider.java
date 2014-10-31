package pc.awt;

import java.awt.AWTEventMulticaster;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import pc.util.Utility;

public class Slider
  extends Container
  implements ActionListener, ItemListener, KeyListener, FocusListener
{
  private boolean isShifted = false;
  private boolean rescaling = false;
  private boolean configMode = false;
  private boolean showBar = true;
  private boolean minMutable = true;
  private boolean maxMutable = true;
  private boolean editable = true;
  private int mainInc = -1;
  private int digits = 4;
  private int scaleWidth = 0;
  private double roundFactor = 1.0D;
  private int hotMaxY = 0;
  private int hotMinY = 0;
  private transient ActionListener actionListener = null;
  private Cursor rightCursor = new Cursor(11);
  private Cursor arrowCursor = Cursor.getDefaultCursor();
  private Cursor leftCursor = new Cursor(10);
  private String[] choices = { "Value", "Min", "Max", "Min!", "Max!", "Digits" };
  private Choice choice = new Choice();
  private Button setbutton = new Button("OK");
  private String label;
  private double value;
  private double min;
  private double max;
  private boolean adjustMax;
  private int subInc;
  private int em;
  private int mouseStart;
  private Color dotColor;
  private Color scaleColor;
  private Color buttonColor;
  private Font scaleFont;
  private Font labelFont;
  private Font fieldFont;
  private FontMetrics sfm;
  private double[] tick;
  private String[] tickLab;
  private TextField valField;
  
  public Slider(String paramString, double paramDouble)
  {
    this.label = paramString;
    this.value = paramDouble;
    if (paramDouble > 0.0D)
    {
      this.min = 0.0D;
      this.max = (1.25D * paramDouble);
    }
    else if (paramDouble < 0.0D)
    {
      this.min = (1.25D * paramDouble);
      this.max = 0.0D;
    }
    else
    {
      this.min = 0.0D;
      this.max = 1.0D;
    }
    init();
  }
  
  public Slider(String paramString, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    this.label = paramString;
    this.value = paramDouble1;
    this.min = paramDouble2;
    this.max = paramDouble3;
    init();
  }
  
  private void init()
  {
    this.valField = new TextField("0");
    setForeground(Color.black);
    this.dotColor = Color.red;
    this.scaleColor = Color.blue;
    this.buttonColor = Color.lightGray;
    setFont(new Font("Serif", 0, 12));
    this.scaleFont = new Font("SansSerif", 0, 8);
    for (int i = 0; i < this.choices.length; i++) {
      this.choice.add(this.choices[i]);
    }
    setConfig(false);
    setLayout(null);
    this.valField.setFont(this.fieldFont);
    
    this.setbutton.setFont(this.scaleFont);
    add(this.choice);
    add(this.valField);
    add(this.setbutton);
  }
  
  private synchronized void setConfig(boolean paramBoolean)
  {
    this.choice.setVisible(paramBoolean);
    this.valField.setVisible(paramBoolean);
    this.setbutton.setVisible(paramBoolean);
    
    this.configMode = paramBoolean;
    if (paramBoolean)
    {
      this.setbutton.addActionListener(this);
      this.choice.addItemListener(this);
      this.valField.addKeyListener(this);
      this.valField.addFocusListener(this);
      this.valField.setText(Utility.format(this.value, this.digits));
      this.valField.setEditable(this.editable);
      
      disableEvents(32L);
      this.choice.select(0);
    }
    else
    {
      this.setbutton.removeActionListener(this);
      this.choice.removeItemListener(this);
      this.valField.removeKeyListener(this);
      this.valField.removeFocusListener(this);
      enableEvents(16L);
      enableEvents(32L);
      this.scaleWidth = 0;
    }
  }
  
  private synchronized void checkRange()
  {
    if (Double.isNaN(this.value))
    {
      if (Double.isNaN(this.min)) {
        this.min = (Double.isNaN(this.max) ? -1.0D : this.max - 1.0D);
      }
      if (Double.isNaN(this.max)) {
        this.max = (this.min + 2.0D);
      }
    }
    else
    {
      if (this.minMutable) {
        this.min = Math.min(this.min, Math.min(this.value, this.max));
      }
      if (this.maxMutable) {
        this.max = Math.max(this.max, Math.max(this.value, this.min));
      }
      this.value = Math.min(this.max, Math.max(this.value, this.min));
      if (this.min == this.max) {
        this.min = (this.max - Math.max(Math.abs(this.value), 0.1D));
      }
    }
    this.tick = Utility.nice(this.min, this.max, 5, false);
    this.tickLab = Utility.fmtNice(this.tick);
  }
  
  public void setMutable(boolean paramBoolean1, boolean paramBoolean2)
  {
    this.minMutable = paramBoolean1;
    this.maxMutable = paramBoolean2;
  }
  
  public void setValue(double paramDouble)
  {
    this.value = paramDouble;
    checkRange();
    if (this.configMode) {
      this.valField.setText(Utility.format(this.value, this.digits));
    }
    if (isShowing()) {
      repaint();
    }
  }
  
  public void setMinimum(double paramDouble)
  {
    if (!this.minMutable) {
      return;
    }
    this.min = paramDouble;
    checkRange();
    this.scaleWidth = 0;
    if (isShowing()) {
      repaint();
    }
  }
  
  public void setMaximum(double paramDouble)
  {
    if (!this.maxMutable) {
      return;
    }
    this.max = paramDouble;
    checkRange();
    this.scaleWidth = 0;
    if (isShowing()) {
      repaint();
    }
  }
  
  public void setDigits(int paramInt)
  {
    this.digits = paramInt;
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this.editable = paramBoolean;
  }
  
  public boolean isEditable()
  {
    return this.editable;
  }
  
  public void setLabel(String paramString)
  {
    this.label = paramString;
  }
  
  public double getValue()
  {
    return this.value;
  }
  
  public double getMinimum()
  {
    return this.min;
  }
  
  public double getMaximum()
  {
    return this.max;
  }
  
  public void setDotColor(Color paramColor)
  {
    this.dotColor = paramColor;
  }
  
  public void setButtonColor(Color paramColor)
  {
    this.buttonColor = paramColor;
  }
  
  public void setScaleColor(Color paramColor)
  {
    this.scaleColor = paramColor;
  }
  
  private void setIncs()
  {
    FontMetrics localFontMetrics = getFontMetrics(getFont());
    this.mainInc = localFontMetrics.getAscent();
    this.em = localFontMetrics.stringWidth("M");
    this.sfm = getFontMetrics(this.scaleFont);
    this.subInc = this.sfm.getAscent();
    this.labelFont = new Font(getFont().getName(), 1, getFont().getSize());
  }
  
  public Dimension getPreferredSize()
  {
    if (this.mainInc < 0) {
      setIncs();
    }
    return new Dimension(18 * this.em, 5 * (this.mainInc + this.subInc) / 2);
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
  
  private double scaleValue(int paramInt1, int paramInt2, int paramInt3)
  {
    if (paramInt3 != this.scaleWidth)
    {
      this.scaleWidth = paramInt3;
      

      double d1 = 1.5D;
      double d2 = d1 * paramInt3 / (this.max - this.min);
      double d3 = Math.log(d2) / Math.log(10.0D);
      this.roundFactor = Math.pow(10.0D, Math.floor(d3));
      d3 = d2 / this.roundFactor;
      if (d3 >= 5.0D) {
        this.roundFactor *= 5.0D;
      } else if (d3 >= 2.0D) {
        this.roundFactor *= 2.0D;
      }
    }
    double d1 = this.min + (this.max - this.min) * (paramInt1 - paramInt2) / paramInt3;
    return Math.round(this.roundFactor * d1) / this.roundFactor;
  }
  
  private int scalePos(double paramDouble, int paramInt1, int paramInt2)
  {
    if (Double.isNaN(paramDouble)) {
      return -1;
    }
    return (int)(paramInt1 + (paramDouble - this.min) * paramInt2 / (this.max - this.min));
  }
  
  public void repaint()
  {
    Dimension localDimension = getSize();
    if (localDimension.width == 0)
    {
      super.repaint();
      return;
    }
    Image localImage = createImage(localDimension.width, localDimension.height);
    Graphics localGraphics = localImage.getGraphics();
    paint(localGraphics);
    localGraphics.dispose();
    getGraphics().drawImage(localImage, 0, 0, null);
  }
  
  public void paint(Graphics paramGraphics)
  {
    if (!isVisible()) {
      return;
    }
    if (this.tick == null) {
      checkRange();
    }
    if (this.configMode) {
      paintConfig(paramGraphics);
    } else {
      paintSlider(paramGraphics);
    }
  }
  
  private void paintConfig(Graphics paramGraphics)
  {
    int i = getSize().width;
    int j = i / 40;
    int k = 2 * this.mainInc;
    

    paramGraphics.setFont(this.labelFont);
    paramGraphics.setColor(getForeground());
    paramGraphics.drawString(this.label, this.em / 2, 3 * this.mainInc / 2);
    

    paramGraphics.setColor(getBackground().darker());
    paramGraphics.fillRect(i - this.subInc, this.subInc / 2, this.subInc, this.subInc);
    paramGraphics.draw3DRect(i - this.subInc, this.subInc / 2, this.subInc, this.subInc, false);
    int m = this.subInc / 3;
    paramGraphics.draw3DRect(i - this.subInc + m, this.subInc / 2 + m, m, m, false);
    

    int n = this.choice.getPreferredSize().width;
    int i1 = this.choice.getPreferredSize().height;
    this.choice.setBounds(0, k, n, i1);
    this.valField.setBounds(n + j, k, i - n - 8 * j, i1);
    this.setbutton.setBounds(i - 6 * j, k, 6 * j, i1);
    
    String str = "";
    switch (this.choice.getSelectedIndex())
    {
    case 0: 
      str = Utility.format(this.value, this.digits);
      break;
    case 1: 
    case 3: 
      str = Utility.format(this.min, this.digits);
      break;
    case 2: 
    case 4: 
      str = Utility.format(this.max, this.digits);
      break;
    case 5: 
      str = "" + this.digits;
    }
    super.paint(paramGraphics);
  }
  
  private void paintSlider(Graphics paramGraphics)
  {
    int i = 3 * this.mainInc / 2;
    int j = getSize().width;
    int k = j - 2 * this.em;
    if (this.mainInc < 0) {
      setIncs();
    }
    paramGraphics.setFont(this.labelFont);
    paramGraphics.setColor(getForeground());
    paramGraphics.drawString(this.label + " = " + Utility.format(this.value, this.digits), this.em / 2, i);
    

    paramGraphics.setColor(getBackground().darker());
    paramGraphics.fillRect(j - this.subInc, this.subInc / 2, this.subInc, this.subInc);
    paramGraphics.setColor(getBackground());
    paramGraphics.draw3DRect(j - this.subInc, this.subInc / 2, this.subInc, this.subInc, true);
    int m = this.subInc / 3;
    paramGraphics.draw3DRect(j - this.subInc + m, this.subInc / 2 + m, m, m, true);
    if (Double.isNaN(this.value)) {
      return;
    }
    i += (this.mainInc + this.subInc) / 2;
    this.hotMinY = (i - this.subInc / 2);
    this.hotMaxY = (i + this.subInc / 2);
    
    int n = scalePos(this.value, this.em, k);
    if (this.editable)
    {
      paramGraphics.setColor(getBackground().darker());
      paramGraphics.fillRect(n - this.subInc / 2, i - this.subInc, 2 * (this.subInc / 2), 2 * this.subInc);
    }
    paramGraphics.setColor(this.scaleColor);
    paramGraphics.drawLine(this.em, i, j - this.em, i);
    
    int i1 = i - this.subInc / 2;int i2 = i1 + this.subInc;int i3 = i2 + 3 * this.subInc / 2;
    paramGraphics.setFont(this.scaleFont);
    int i4 = 0;
    for (int i5 = 0; i5 < this.tick.length; i5++)
    {
      int i6 = scalePos(this.tick[i5], this.em, k);
      paramGraphics.drawLine(i6, i1, i6, i2);
      int i7 = this.sfm.stringWidth(this.tickLab[i5]);
      i6 = Math.max(this.em / 2, Math.min(i6 - i7 / 2, j - this.em / 2 - i7));
      if (i6 - i4 > this.subInc / 2)
      {
        paramGraphics.drawString(this.tickLab[i5], i6, i3);
        i4 = i6 + i7;
      }
    }
    if ((this.showBar) && (this.min * this.max <= 0.0D))
    {
      int i5 = (int)(this.em - (j - 2 * this.em) * this.min / (this.max - this.min));
      paramGraphics.setColor(this.dotColor);
      paramGraphics.drawLine(i5, i, n, i);
      paramGraphics.drawLine(i5, i - 1, n, i - 1);
      paramGraphics.drawLine(i5, i + 1, n, i + 1);
    }
    paramGraphics.setColor(getForeground());
    paramGraphics.drawLine(n, i - this.subInc, n, i + this.subInc);
    if (this.editable)
    {
      paramGraphics.setColor(getBackground());
      paramGraphics.draw3DRect(n - this.subInc / 2, i - this.subInc, 2 * (this.subInc / 2), 2 * this.subInc, true);
    }
    else
    {
      paramGraphics.setColor(getBackground().darker());
      paramGraphics.drawRoundRect(n - this.subInc / 2, i - this.subInc, 2 * (this.subInc / 2), 2 * this.subInc, 2 * this.subInc / 3, this.subInc);
    }
    super.paint(paramGraphics);
  }
  
  public String toString()
  {
    return "Slider: " + this.label + " = " + Utility.format(this.value, 4) + " on [" + Utility.format(this.min, 4) + "," + Utility.format(this.max, 4) + "] mutable: [" + this.minMutable + "," + this.maxMutable + "]";
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public synchronized void processMouseEvent(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getX() - this.em;
    int j = getSize().width - 2 * this.em;
    int k = paramMouseEvent.getY();
    switch (paramMouseEvent.getID())
    {
    case 500: 
      if ((i > this.em + j - this.subInc) && (k < 3 * this.subInc / 2))
      {
        setConfig(!this.configMode);
        repaint();
        return;
      }
      if ((i < 0) || (i > j) || (this.configMode)) {
        return;
      }
      if ((k >= this.hotMinY) && (k <= this.hotMaxY))
      {
        if ((i > j) || (!this.editable)) {
          return;
        }
        setValue(scaleValue(i, 0, j));
        repaint();
        if (this.actionListener != null) {
          this.actionListener.actionPerformed(new ActionEvent(this, 1001, this.label));
        }
      }
      break;
    case 501: 
      if ((i < 0) || (i > j) || (this.configMode)) {
        return;
      }
      requestFocus();
      if (k >= this.hotMaxY + this.subInc / 2)
      {
        this.rescaling = false;
        if (!this.maxMutable)
        {
          this.adjustMax = false;
          if (this.minMutable) {}
        }
        else
        {
          this.adjustMax = (i > j / 2);
        }
        this.mouseStart = i;
        this.rescaling = true;
        setCursor(this.adjustMax ? this.rightCursor : this.leftCursor);
      }
      break;
    case 502: 
      setCursor(this.arrowCursor);
      if (this.rescaling)
      {
        this.rescaling = false;
        i = Math.max(0, Math.min(i, j));
        if (i == this.mouseStart) {
          return;
        }
        double d = scaleValue(this.mouseStart, 0, j);
        if ((this.maxMutable) && (i > j / 2)) {
          this.max = (this.min + (d - this.min) * j / i);
        } else if (this.minMutable) {
          this.min = (this.max - (this.max - d) * j / (j - i));
        }
        this.scaleWidth = 0;
        checkRange();
        repaint();
      }
      break;
    case 505: 
      this.rescaling = false;
    }
  }
  
  public synchronized void processMouseMotionEvent(MouseEvent paramMouseEvent)
  {
    int i = paramMouseEvent.getX() - this.em;
    int j = getSize().width - 2 * this.em;
    int k = paramMouseEvent.getY();
    if (paramMouseEvent.getID() == 506)
    {
      if (!this.editable) {
        return;
      }
      if ((i < 0) && (this.minMutable))
      {
        this.min -= 0.005D * (this.max - this.min);
        checkRange();
        repaint();
      }
      else if ((i > j) && (this.maxMutable))
      {
        this.max += 0.005D * (this.max - this.min);
        checkRange();
        repaint();
      }
      if ((k >= this.hotMinY) && (k <= this.hotMaxY))
      {
        setValue(scaleValue(i, 0, j));
        repaint();
        if (this.actionListener != null) {
          this.actionListener.actionPerformed(new ActionEvent(this, 1001, this.label));
        }
      }
    }
  }
  
  private void updateChoice(int paramInt, double paramDouble)
  {
    switch (paramInt)
    {
    case 0: 
      if (!this.editable) {
        return;
      }
      this.value = paramDouble;
      if (this.actionListener != null) {
        this.actionListener.actionPerformed(new ActionEvent(this, 1001, this.label));
      }
      return;
    case 1: 
      setMutable(true, this.maxMutable);
      this.min = paramDouble;
      break;
    case 2: 
      setMutable(this.minMutable, true);
      this.max = paramDouble;
      break;
    case 3: 
      setMutable(false, this.maxMutable);
      this.min = paramDouble;
      break;
    case 4: 
      setMutable(this.minMutable, false);
      this.max = paramDouble;
      break;
    case 5: 
      this.digits = Math.max(1, (int)Math.round(paramDouble));
    }
    checkRange();
    
    this.choice.select(0);
    this.valField.setEditable(this.editable);
    this.valField.setText(Utility.format(this.value, this.digits));
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getSource().equals(this.setbutton)) {
      try
      {
        double d = Utility.strtod(this.valField.getText());
        updateChoice(this.choice.getSelectedIndex(), d);
      }
      catch (NumberFormatException localNumberFormatException)
      {
        this.valField.setText(this.valField.getText() + " - INVALID!");
        this.valField.selectAll();
      }
    }
  }
  
  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    if (paramItemEvent.getSource().equals(this.choice))
    {
      String str = "";
      this.valField.setEditable(true);
      switch (this.choice.getSelectedIndex())
      {
      case 0: 
        str = Utility.format(this.value, this.digits);
        this.valField.setEditable(this.editable);
        break;
      case 1: 
      case 3: 
        str = Utility.format(this.min, this.digits);
        break;
      case 2: 
      case 4: 
        str = Utility.format(this.max, this.digits);
        break;
      case 5: 
        str = "" + this.digits;
      }
      this.valField.setText(str);
    }
  }
  
  public void keyPressed(KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent.getKeyCode() == 16) || (paramKeyEvent.getKeyCode() == 18) || (paramKeyEvent.getKeyCode() == 17)) {
      this.isShifted = true;
    }
    if (paramKeyEvent.getKeyCode() == 10) {
      actionPerformed(new ActionEvent(this.setbutton, 1001, "OK"));
    }
    if (paramKeyEvent.getKeyCode() == 27)
    {
      setConfig(false);
      repaint();
    }
  }
  
  public void keyReleased(KeyEvent paramKeyEvent)
  {
    if ((paramKeyEvent.getKeyCode() == 16) || (paramKeyEvent.getKeyCode() == 18) || (paramKeyEvent.getKeyCode() == 17)) {
      this.isShifted = false;
    }
  }
  
  public void keyTyped(KeyEvent paramKeyEvent) {}
  
  public void focusGained(FocusEvent paramFocusEvent)
  {
    if (paramFocusEvent.getSource().equals(this.valField)) {
      this.valField.selectAll();
    }
  }
  
  public void focusLost(FocusEvent paramFocusEvent)
  {
    if (paramFocusEvent.getSource().equals(this.valField))
    {
      this.valField.select(0, 0);
      actionPerformed(new ActionEvent(this.setbutton, 1001, "OK"));
    }
  }
}
