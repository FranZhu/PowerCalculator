package pc.stat.anova;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import pc.awt.DoubleField;
import pc.awt.GridLine;
import pc.awt.IntField;
import pc.awt.RVLayout;

public class EffectAdvisor
  extends Frame
{
  private int prec = -2;
  private int wid = 6;
  private double fuzz = 0.0001D;
  private int rows;
  private int cols;
  private double[] rm;
  private double[] cm;
  private double gm;
  private double rc;
  private double[][] cell;
  private double rsd;
  private double csd;
  private double rcsd;
  private double[] re;
  private double[] ce;
  private double[][] celle;
  private double minY;
  private double maxY;
  private double rrng;
  private double crng;
  private double rcrng;
  private DoubleField[] rmFld;
  private DoubleField[] cmFld;
  private DoubleField gmFld;
  private DoubleField[][] cellFld;
  private DoubleField rsdFld;
  private DoubleField csdFld;
  private DoubleField rcsdFld;
  private DoubleField rrngFld;
  private DoubleField crngFld;
  private DoubleField rcrngFld;
  private IntField nRowFld;
  private IntField nColFld;
  private Canvas rowPlot;
  private Canvas colPlot;
  private Panel advisorCard;
  private Panel setupCard;
  private Panel dataPan;
  private CardLayout layout;
  private MenuBar menuBar;
  
  public EffectAdvisor()
  {
    this.nRowFld = new IntField(2, 4);
    this.nColFld = new IntField(2, 4);
    this.gmFld = new DoubleField(0.0D, this.wid, this.prec);
    this.gmFld.setBackground(Color.gray);
    this.rsdFld = new DoubleField(0.0D, 5, -3);
    this.csdFld = new DoubleField(0.0D, 5, -3);
    this.rcsdFld = new DoubleField(0.0D, 5, -3);
    this.rsdFld.setBackground(Color.darkGray);
    this.csdFld.setBackground(Color.darkGray);
    this.rcsdFld.setBackground(Color.darkGray);
    this.rrngFld = new DoubleField(0.0D, 4, -2);
    this.crngFld = new DoubleField(0.0D, 4, -2);
    this.rcrngFld = new DoubleField(0.0D, 4, -2);
    this.rrngFld.setBackground(Color.darkGray);
    this.crngFld.setBackground(Color.darkGray);
    this.rcrngFld.setBackground(Color.darkGray);
    

    Panel localPanel1 = new Panel();
    localPanel1.setLayout(new RVLayout(3));
    localPanel1.setBackground(Color.gray);
    localPanel1.setForeground(Color.white);
    localPanel1.add(new Label("Term", 0));
    localPanel1.add(new Label("Effect SD", 1));
    localPanel1.add(new Label("Range", 1));
    localPanel1.add(new GridLine(0, Color.lightGray));
    localPanel1.add(new GridLine(0, Color.lightGray));
    localPanel1.add(new GridLine(0, Color.lightGray));
    localPanel1.add(new Label("Rows", 0));
    localPanel1.add(this.rsdFld);
    localPanel1.add(this.rrngFld);
    localPanel1.add(new Label("Cols", 0));
    localPanel1.add(this.csdFld);
    localPanel1.add(this.crngFld);
    localPanel1.add(new Label("Rows*Cols", 0));
    localPanel1.add(this.rcsdFld);
    localPanel1.add(this.rcrngFld);
    

    Panel localPanel2 = new Panel();
    localPanel2.setLayout(new GridLayout(1, 2));
    this.rowPlot = new Canvas();
    this.colPlot = new Canvas();
    localPanel2.add(this.rowPlot);
    localPanel2.add(this.colPlot);
    
    Panel localPanel3 = new Panel();
    localPanel3.setLayout(new BorderLayout());
    localPanel3.add("West", localPanel1);
    localPanel3.add("Center", localPanel2);
    localPanel3.add("North", new GridLine(0, Color.black));
    

    this.menuBar = new MenuBar();
    Menu localMenu = new Menu("Options");
    localMenu.add(new MenuItem("New setup"));
    localMenu.add(new MenuItem("Reset values"));
    localMenu.add(new MenuItem("Reset window size"));
    localMenu.addSeparator();
    localMenu.add(new MenuItem("Min SD row effs"));
    localMenu.add(new MenuItem("Linear row effs"));
    localMenu.add(new MenuItem("Max SD row effs"));
    localMenu.addSeparator();
    localMenu.add(new MenuItem("Min SD col effs"));
    localMenu.add(new MenuItem("Linear col effs"));
    localMenu.add(new MenuItem("Max SD col effs"));
    localMenu.addSeparator();
    localMenu.add(new MenuItem("Min SD row*col effs"));
    localMenu.add(new MenuItem("Lin*lin row*col effs"));
    localMenu.add(new MenuItem("Max SD row*col effs"));
    localMenu.addSeparator();
    localMenu.add(new MenuItem("Quit"));
    this.menuBar.add(localMenu);
    

    setTitle("Effect Advisor setup");
    this.layout = new CardLayout();
    setLayout(this.layout);
    
    this.setupCard = new Panel();
    this.setupCard.add(new Label("Rows"));
    this.setupCard.add(this.nRowFld);
    this.setupCard.add(new Label("        Columns"));
    this.setupCard.add(this.nColFld);
    this.setupCard.add(new Label("        "));
    this.setupCard.add(new Button("OK"));
    add("setup", this.setupCard);
    
    this.advisorCard = new Panel();
    this.advisorCard.setLayout(new BorderLayout());
    this.advisorCard.add("South", localPanel3);
    add("advisor", this.advisorCard);
    this.layout.show(this, "setup");
    pack();
    show();
  }
  
  private Panel dataPanel(int paramInt1, int paramInt2)
  {
	int i,j;
    this.rows = paramInt1;
    this.cols = paramInt2;
    this.rc = (paramInt1 * paramInt2);
    this.rm = new double[paramInt1];
    this.re = new double[paramInt1];
    this.rmFld = new DoubleField[paramInt1];
    this.cm = new double[paramInt2];
    this.ce = new double[paramInt2];
    this.cmFld = new DoubleField[paramInt2];
    this.cell = new double[paramInt1][];
    this.celle = new double[paramInt1][];
    this.cellFld = new DoubleField[paramInt1][];
    for ( i = 0; i < paramInt1; i++)
    {
      this.rmFld[i] = new DoubleField(0.0D, this.wid, this.prec);
      this.rmFld[i].setBackground(Color.pink);
      this.cell[i] = new double[paramInt2];
      this.celle[i] = new double[paramInt2];
      this.cellFld[i] = new DoubleField[paramInt2];
      for (j = 0; j < paramInt2; j++)
      {
        this.cellFld[i][j] = new DoubleField(0.0D, this.wid, this.prec);
        this.cellFld[i][j].setBackground(Color.white);
      }
    }
    for (i = 0; i < paramInt2; i++)
    {
      this.cmFld[i] = new DoubleField(0.0D, this.wid, this.prec);
      this.cmFld[i].setBackground(Color.pink);
    }
    Panel localPanel = new Panel();
    localPanel.setLayout(new RVLayout(paramInt2 + 2, 0, 0, false, true));
    for ( j = 0; j < paramInt1; j++)
    {
      for (int k = 0; k < paramInt2; k++) {
        localPanel.add(this.cellFld[j][k]);
      }
      localPanel.add(new GridLine(1, Color.red));
      localPanel.add(this.rmFld[j]);
    }
    for (j = 0; j < paramInt2; j++) {
      localPanel.add(new GridLine(0, Color.red));
    }
    localPanel.add(new GridLine(2, Color.red));
    localPanel.add(new GridLine(0, Color.red));
    for (j = 0; j < paramInt2; j++) {
      localPanel.add(this.cmFld[j]);
    }
    localPanel.add(new GridLine(1, Color.red));
    localPanel.add(this.gmFld);
    
    return localPanel;
  }
  
  private synchronized void updateEffs()
  {
    double d6;
    double d5;
    double d4;
    double d3;
    double d2;
    double d1 = d2 = d3 = d4 = d5 = d6 = 0.0D;
    this.gm = 0.0D;
    for (int i = 0; i < this.cols; this.cm[(i++)] = 0.0D) {}
    this.minY = (this.maxY = this.cell[0][0]);
    int j, i;
    for (i = 0; i < this.rows; i++)
    {
      this.rm[i] = 0.0D;
      for (j = 0; j < this.cols; j++)
      {
        this.rm[i] += this.cell[i][j] / this.cols;
        this.cm[j] += this.cell[i][j] / this.rows;
        this.gm += this.cell[i][j] / this.rc;
        if (this.cell[i][j] < this.minY) {
          this.minY = this.cell[i][j];
        }
        if (this.cell[i][j] > this.maxY) {
          this.maxY = this.cell[i][j];
        }
      }
      this.rmFld[i].setValue(this.rm[i]);
    }
    for (i = 0; i < this.cols; i++) {
      this.cmFld[i].setValue(this.cm[i]);
    }
    this.gmFld.setValue(this.gm);
    this.rsd = (this.csd = this.rcsd = 0.0D);
    for (i = 0; i < this.rows; i++)
    {
      this.re[i] = (this.rm[i] - this.gm);
      if (this.re[i] < d1) {
        d1 = this.re[i];
      }
      if (this.re[i] > d2) {
        d2 = this.re[i];
      }
      this.rsd += this.re[i] * this.re[i];
      for (j = 0; j < this.cols; j++)
      {
        this.celle[i][j] = (this.cell[i][j] - this.re[i] - this.cm[j]);
        this.rcsd += this.celle[i][j] * this.celle[i][j];
        if (this.celle[i][j] < d5) {
          d5 = this.celle[i][j];
        }
        if (this.celle[i][j] > d6) {
          d6 = this.celle[i][j];
        }
      }
    }
    for (i = 0; i < this.cols; i++)
    {
      this.ce[i] = (this.cm[i] - this.gm);
      if (this.ce[i] < d3) {
        d3 = this.ce[i];
      }
      if (this.ce[i] > d4) {
        d4 = this.ce[i];
      }
      this.csd += this.ce[i] * this.ce[i];
    }
    this.rsd = Math.sqrt(this.rsd / (this.rows - 1));
    this.csd = Math.sqrt(this.csd / (this.cols - 1));
    this.rcsd = Math.sqrt(this.rcsd / (this.rows - 1) / (this.cols - 1));
    this.rsdFld.setValue(this.rsd);
    this.csdFld.setValue(this.csd);
    this.rcsdFld.setValue(this.rcsd);
    this.rrng = (d2 - d1);
    this.crng = (d4 - d3);
    this.rcrng = (d6 - d5);
    this.rrngFld.setValue(this.rrng);
    this.crngFld.setValue(this.crng);
    this.rcrngFld.setValue(this.rcrng);
    updatePlots();
  }
  
  private synchronized void updateRow(int paramInt, double paramDouble, boolean paramBoolean)
  {
    double d = paramDouble - this.rm[paramInt];
    for (int i = 0; i < this.cols; i++)
    {
      this.cell[paramInt][i] += d;
      this.cellFld[paramInt][i].setValue(this.cell[paramInt][i]);
    }
    if (paramBoolean) {
      updateEffs();
    }
  }
  
  private synchronized void updateCol(int paramInt, double paramDouble, boolean paramBoolean)
  {
    double d = paramDouble - this.cm[paramInt];
    for (int i = 0; i < this.rows; i++)
    {
      this.cell[i][paramInt] += d;
      this.cellFld[i][paramInt].setValue(this.cell[i][paramInt]);
    }
    if (paramBoolean) {
      updateEffs();
    }
  }
  
  private synchronized void updateGM(double paramDouble)
  {
    for (int i = 0; i < this.rows; i++) {
      updateRow(i, paramDouble + this.re[i], false);
    }
    updateEffs();
  }
  
  private synchronized void rescaleEffs(double paramDouble1, double paramDouble2, double paramDouble3)
  {
    for (int i = 0; i < this.rows; i++) {
      for (int j = 0; j < this.cols; j++)
      {
        this.cell[i][j] = (this.gm + paramDouble1 * this.re[i] + paramDouble2 * this.ce[j] + paramDouble3 * this.celle[i][j]);
        this.cellFld[i][j].setValue(this.cell[i][j]);
      }
    }
    updateEffs();
  }
  
  public boolean handleEvent(Event paramEvent)
  {
    if (paramEvent.id == 201) {
      dispose();
    }
    return super.handleEvent(paramEvent);
  }
  
  public boolean action(Event paramEvent, Object paramObject)
  {
    int j, i;
    if (paramObject.equals("OK"))
    {
      setTitle("Setting up Effect Advisor ...");
      if (this.dataPan != null) {
        this.advisorCard.remove(this.dataPan);
      }
      i = this.nRowFld.getValue();j = this.nColFld.getValue();
      i = Math.max(i, 2);
      j = Math.max(j, 2);
      this.dataPan = dataPanel(i, j);
      updateGM(0.0D);
      rescaleEffs(0.0D, 0.0D, 0.0D);
      this.advisorCard.add("North", this.dataPan);
      setTitle("Effect Advisor");
      setMenuBar(this.menuBar);
      this.layout.show(this, "advisor");
      pack();
      return true;
    }
    if (paramObject.equals("New setup"))
    {
      setTitle("Effect Advisor Setup");
      remove(this.menuBar);
      this.layout.show(this, "setup");
      pack();
      return true;
    }
    if (paramObject.equals("Quit")) {
      dispose();
    }
    if (paramEvent.target.equals(this.gmFld))
    {
      updateGM(this.gmFld.getValue());
      return true;
    }
    if (paramEvent.target.equals(this.rsdFld))
    {
      rescaleEffs(this.rsdFld.getValue() / this.rsd, 1.0D, 1.0D);
      return true;
    }
    if (paramEvent.target.equals(this.csdFld))
    {
      rescaleEffs(1.0D, this.csdFld.getValue() / this.csd, 1.0D);
      return true;
    }
    if (paramEvent.target.equals(this.rcsdFld))
    {
      rescaleEffs(1.0D, 1.0D, this.rcsdFld.getValue() / this.rcsd);
      return true;
    }
    if (paramEvent.target.equals(this.rrngFld))
    {
      rescaleEffs(this.rrngFld.getValue() / this.rrng, 1.0D, 1.0D);
      return true;
    }
    if (paramEvent.target.equals(this.crngFld))
    {
      rescaleEffs(1.0D, this.crngFld.getValue() / this.crng, 1.0D);
      return true;
    }
    if (paramEvent.target.equals(this.rcrngFld))
    {
      rescaleEffs(1.0D, 1.0D, this.rcrngFld.getValue() / this.rcrng);
      return true;
    }
    for (i = 0; i < this.rows; i++) {
      if (paramEvent.target.equals(this.rmFld[i]))
      {
        updateRow(i, this.rmFld[i].getValue(), true);
        return true;
      }
    }
    for (i = 0; i < this.cols; i++) {
      if (paramEvent.target.equals(this.cmFld[i]))
      {
        updateCol(i, this.cmFld[i].getValue(), true);
        return true;
      }
    }
    for (i = 0; i < this.rows; i++) {
      for (j = 0; j < this.cols; j++) {
        if (paramEvent.target.equals(this.cellFld[i][j]))
        {
          this.cell[i][j] = this.cellFld[i][j].getValue();
          updateEffs();
          return true;
        }
      }
    }
    if (paramObject.equals("Reset values"))
    {
      rescaleEffs(0.0D, 0.0D, 0.0D);
      updateGM(0.0D);
      return true;
    }
    if (paramObject.equals("Reset window size"))
    {
      pack();
      return true;
    }
    double d;
    if (paramObject.equals("Min SD row effs"))
    {
      d = this.rrng > this.fuzz ? 0.5D * this.rrng : 1.0D;
      rescaleEffs(0.0D, 1.0D, 1.0D);
      this.re[0] = -1.0D;this.re[(this.rows - 1)] = 1.0D;
      rescaleEffs(d, 1.0D, 1.0D);
      return true;
    }
    int k;
    if (paramObject.equals("Linear row effs"))
    {
      d = this.rrng > this.fuzz ? 0.5D * this.rrng : 1.0D;
      for (k = 0; k < this.rows; k++) {
        this.re[k] = ((2 * k - this.rows + 1.0D) / (this.rows - 1.0D));
      }
      rescaleEffs(d, 1.0D, 1.0D);
      return true;
    }
    if (paramObject.equals("Max SD row effs"))
    {
      d = this.rrng > this.fuzz ? 0.5D * this.rrng : 1.0D;
      this.re[((this.rows - 1) / 2)] = 0.0D;
      for (k = 0; k < this.rows / 2; k++)
      {
        this.re[k] = -1.0D;
        this.re[(this.rows - k - 1)] = 1.0D;
      }
      rescaleEffs(d, 1.0D, 1.0D);
      return true;
    }
    if (paramObject.equals("Min SD col effs"))
    {
      d = this.crng > this.fuzz ? 0.5D * this.crng : 1.0D;
      rescaleEffs(1.0D, 0.0D, 1.0D);
      this.ce[0] = -1.0D;this.ce[(this.cols - 1)] = 1.0D;
      rescaleEffs(1.0D, d, 1.0D);
      return true;
    }
    if (paramObject.equals("Linear col effs"))
    {
      d = this.crng > this.fuzz ? 0.5D * this.crng : 1.0D;
      for (k = 0; k < this.cols; k++) {
        this.ce[k] = ((2 * k - this.cols + 1.0D) / (this.cols - 1.0D));
      }
      rescaleEffs(1.0D, d, 1.0D);
      return true;
    }
    if (paramObject.equals("Max SD col effs"))
    {
      d = this.crng > this.fuzz ? 0.5D * this.crng : 1.0D;
      this.ce[((this.cols - 1) / 2)] = 0.0D;
      for (k = 0; k < this.cols / 2; k++)
      {
        this.ce[k] = -1.0D;
        this.ce[(this.cols - k - 1)] = 1.0D;
      }
      rescaleEffs(1.0D, d, 1.0D);
      return true;
    }
    if (paramObject.equals("Min SD row*col effs"))
    {
      d = this.rcrng > this.fuzz ? 0.5D * this.rcrng : 1.0D;
      rescaleEffs(1.0D, 1.0D, 0.0D); double 
        tmp1218_1217 = 1.0D;this.celle[(this.rows - 1)][(this.cols - 1)] = tmp1218_1217;this.celle[0][0] = tmp1218_1217; double 
        tmp1248_1245 = -1.0D;this.celle[(this.rows - 1)][0] = tmp1248_1245;this.celle[0][(this.cols - 1)] = tmp1248_1245;
      rescaleEffs(1.0D, 1.0D, d);
      return true;
    }
    int m;
    if (paramObject.equals("Lin*lin row*col effs"))
    {
      d = this.rcrng > this.fuzz ? 0.5D * this.rcrng : 1.0D;
      for (k = 0; k < this.rows; k++) {
        for (m = 0; m < this.cols; m++) {
          this.celle[k][m] = ((2 * k - this.rows + 1) * (2 * m - this.cols + 1) / (this.rows - 1.0D) / (this.cols - 1.0D));
        }
      }
      rescaleEffs(1.0D, 1.0D, d);
      return true;
    }
    if (paramObject.equals("Max SD row*col effs"))
    {
      d = this.rcrng > this.fuzz ? 0.5D * this.rcrng : 1.0D;
      rescaleEffs(1.0D, 1.0D, 0.0D);
      for (k = 0; k < this.rows / 2; k++) {
        for (m = 0; m < this.cols / 2; m++)
        {
          double tmp1491_1490 = 1.0D;this.celle[(this.rows - k - 1)][(this.cols - m - 1)] = tmp1491_1490;this.celle[k][m] = tmp1491_1490; double 
            tmp1529_1526 = -1.0D;this.celle[(this.rows - k - 1)][m] = tmp1529_1526;this.celle[k][(this.cols - m - 1)] = tmp1529_1526;
        }
      }
      rescaleEffs(1.0D, 1.0D, d);
      return true;
    }
    return super.action(paramEvent, paramObject);
  }
  
  private void updatePlots()
  {
    Color[] arrayOfColor = { Color.black, Color.blue, Color.red, Color.orange, Color.cyan, Color.magenta };
    
    int i = this.rowPlot.size().width;int j = this.rowPlot.size().height;int k = j / 2;
    double d = 0.0D;
    if (this.minY < this.maxY)
    {
      d = (j - 30) / (this.maxY - this.minY);
      k = 20;
    }
    Graphics localGraphics = drawCanv(this.rowPlot, "Row profiles");
    int m = (i - 20) / (this.cols - 1), n;
    int i1;
    int i2;
    int i3;
    int i4;
    int i5;
    for ( n = this.rows - 1; n >= 0; n--)
    {
      localGraphics.setColor(arrayOfColor[(n % 6)]);
      i1 = 0;i2 = 0;
      for (i3 = 0; i3 < this.cols; i3++)
      {
        i4 = k + (int)(d * (this.maxY - this.cell[n][i3]) + 0.5D);
        i5 = 10 + i3 * m;
        localGraphics.fillOval(i5 - 2, i4 - 2, 5, 5);
        if (i3 > 0) {
          localGraphics.drawLine(i2, i1, i5, i4);
        }
        i1 = i4;
        i2 = i5;
      }
    }
    localGraphics = drawCanv(this.colPlot, "Column profiles");
    m = (i - 20) / (this.rows - 1);
    for (n = this.cols - 1; n >= 0; n--)
    {
      localGraphics.setColor(arrayOfColor[(n % 6)]);
      i1 = 0;i2 = 0;
      for (i3 = 0; i3 < this.rows; i3++)
      {
        i4 = k + (int)(d * (this.maxY - this.cell[i3][n]) + 0.5D);
        i5 = 10 + i3 * m;
        localGraphics.fillOval(i5 - 2, i4 - 2, 5, 5);
        if (i3 > 0) {
          localGraphics.drawLine(i2, i1, i5, i4);
        }
        i1 = i4;
        i2 = i5;
      }
    }
  }
  
  private Graphics drawCanv(Canvas paramCanvas, String paramString)
  {
    Graphics localGraphics = paramCanvas.getGraphics();
    int i = paramCanvas.size().width;int j = paramCanvas.size().height;
    localGraphics.setColor(Color.white);
    localGraphics.fill3DRect(5, 15, i - 10, j - 20, true);
    localGraphics.setColor(Color.black);
    localGraphics.setFont(new Font("Helvetica", 0, 8));
    localGraphics.drawString(paramString, 5, 10);
    return localGraphics;
  }
  
  public void paint(Graphics paramGraphics)
  {
    updatePlots();
  }
  
  public static void main(String[] paramArrayOfString)
  {
    EffectAdvisor localEffectAdvisor = new EffectAdvisor();
  }
}
