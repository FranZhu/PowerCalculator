package pc.powercalculator.apps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Menu;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import pc.powercalculator.PiDoubleField;
import pc.powercalculator.PiPanel;
import pc.powercalculator.PowerCalculator;

public class AnovaPicker
  extends PowerCalculator
{
  private static String title = "Select an ANOVA model";
  public int isRep;
  public int preDef;
  public double reps;
  protected TextField ttlFld;
  protected TextField modFld;
  protected TextField levFld;
  protected TextField randFld;
  protected Label noteLbl;
  protected PiDoubleField repFld;
  protected Vector builtIns;
  
  protected String[] setup()
  {
    Properties localProperties = new Properties();
    try
    {
      this.ttlFld = new TextField(50);
      this.modFld = new TextField();
      this.levFld = new TextField();
      this.randFld = new TextField();
      this.repFld = new PiDoubleField("reps", "Observations per factor combination", 1.0D);
      this.noteLbl = new Label("");
      this.noteLbl.setFont(new Font("SanSerif", 0, 10));
      

      this.builtIns = new Vector();
      
      this.builtIns.addElement(new String[] { "User-specified model", "", "", "", "", "(See 'Help/How to use this dialog' for guidelines on specifying models)" });
      localProperties.load(AnovaPicker.class.getResourceAsStream("AnovaPicker.txt"));
      String str = localProperties.getProperty("keys");
      StringTokenizer localStringTokenizer = new StringTokenizer(str, ",");
      while (localStringTokenizer.hasMoreTokens())
      {
        String localObject = localStringTokenizer.nextToken();
        this.builtIns.addElement(new String[] { localProperties.getProperty((String)localObject), localProperties.getProperty((String)localObject + ".model", ""), localProperties.getProperty((String)localObject + ".levels", ""), localProperties.getProperty((String)localObject + ".random", ""), localProperties.getProperty((String)localObject + ".reps", ""), localProperties.getProperty((String)localObject + ".note", "") });
      }
      String [] localObject = new String[this.builtIns.size()];
      localObject[0] = "(Define your own)";
      for (int i = 1; i < this.builtIns.size(); i++) {
        localObject[i] = ((String[])(String[])this.builtIns.elementAt(i))[0];
      }
      return localObject;
    }
    catch (IOException localIOException)
    {
      errmsg("AnovaPicker", "Can't load resource file", true);
    }
    return null;
  }
  
  public void gui()
  {
    setBackground(new Color(215, 235, 235));
    String[] arrayOfString = setup();
    choice("preDef", "Built-in models", arrayOfString, 1);
    beginSubpanel(2);
    this.panel.setLayout(new BorderLayout());
    this.panel.add(new Label("     "), "West");
    this.panel.add(this.noteLbl, "Center");
    endSubpanel();
    beginSubpanel(2, false);
    label("Title");this.panel.add(this.ttlFld);
    label("Model");this.panel.add(this.modFld);
    label("Levels");this.panel.add(this.levFld);
    label("Random factors");this.panel.add(this.randFld);
    checkbox("isRep", "Replicated within cells", 0);this.panel.add(this.repFld);
    endSubpanel();
    beginSubpanel(1);
    this.panel.setLayout(new FlowLayout(2));
    label("Study the power of...");
    button("compDialog", "Differences/Contrasts");
    button("fDialog", "F tests");
    endSubpanel();
    this.repFld.addActionListener(this);
    menuItem("localHelp", "How to use this dialog", this.helpMenu);
    preDef_changed();
  }
  
  protected void afterSetup()
  {
    this.optMenu.remove(0);
    this.optMenu.remove(0);
    this.helpMenu.remove(2);
  }
  
  public void click() {}
  
  public void preDef_changed()
  {
    String[] arrayOfString = (String[])this.builtIns.elementAt(this.preDef);
    this.ttlFld.setText(arrayOfString[0]);
    this.modFld.setText(arrayOfString[1]);
    this.levFld.setText(arrayOfString[2]);
    this.randFld.setText(arrayOfString[3]);
    this.noteLbl.setText(arrayOfString[5]);
    this.isRep = (arrayOfString[4].equals("") ? 0 : 1);
    if (this.isRep != 0) {
      this.repFld.setValue(new Integer(arrayOfString[4]).doubleValue());
    }
    isRep_changed();
  }
  
  public void isRep_changed()
  {
    if (this.isRep == 0) {
      this.repFld.setVisible(false);
    } else {
      this.repFld.setVisible(true);
    }
  }
  
  public void rep_changed() {}
  
  public void fDialog()
  {
    int i = this.isRep == 1 ? (int)this.repFld.getValue() : 0;
    AnovaGUI localAnovaGUI = new AnovaGUI(this.ttlFld.getText(), this.modFld.getText(), i, this.levFld.getText(), this.randFld.getText());
    
    localAnovaGUI.setMaster(this);
  }
  
  public void compDialog()
  {
    int i = this.isRep == 1 ? (int)this.repFld.getValue() : 0;
    AnovaCompGUI localAnovaCompGUI = new AnovaCompGUI(this.ttlFld.getText(), this.modFld.getText(), i, this.levFld.getText(), this.randFld.getText());
    
    localAnovaCompGUI.setMaster(this);
  }
  
  public void localHelp()
  {
    showText(AnovaPicker.class, "AnovaPickerHelp.txt", "Specifying an ANOVA scenario", 25, 60);
  }
  
  public AnovaPicker()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new AnovaPicker();
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    super.actionPerformed(paramActionEvent);
  }
}
