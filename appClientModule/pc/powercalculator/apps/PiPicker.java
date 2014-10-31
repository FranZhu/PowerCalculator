package pc.powercalculator.apps;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import pc.powercalculator.PowerCalculator;
import pc.util.Utility;

public class PiPicker
  extends Applet
  implements ActionListener
{
  List list;
  Button runBut;
  Vector classes;
  Properties pl;
  String pkg;
  
  public void init()
  {
    String str1 = getParameter("rcFile");
    if (str1 == null) {
      str1 = "PiPicker.txt";
    }
    setLayout(new BorderLayout());
    this.pl = new Properties();
    this.list = new List();
    this.runBut = new Button("Run selection");
    Panel localPanel = new Panel(new FlowLayout(2));
    localPanel.add(this.runBut);
    this.classes = new Vector();
    try
    {
      InputStream localInputStream = getClass().getResourceAsStream(str1);
      this.pl.load(localInputStream);
      this.pkg = this.pl.getProperty("package", "pc.powercalculator.apps");
      String str2 = this.pl.getProperty("keys");
      StringTokenizer localStringTokenizer = new StringTokenizer(str2, ",");
      while (localStringTokenizer.hasMoreTokens())
      {
        String str3 = localStringTokenizer.nextToken();
        this.classes.addElement(str3);
        this.list.add(this.pl.getProperty(str3, "???"));
      }
      showStatus("");
    }
    catch (IOException localIOException)
    {
      Utility.error(localIOException);
    }
    catch (Exception localException)
    {
      Utility.error(localException);
    }
    add(this.list, "Center");
    add(localPanel, "South");
    this.runBut.addActionListener(this);
    this.list.addActionListener(this);
    
    resize(250, 150);
  }
  
  public void quit(String paramString)
  {
    add(new Label(paramString), "Center");
    resize(400, 300);
    destroy();
  }
  
  public String getAppletInfo()
  {
    return "Power Calculator Applets";
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    Object localObject1 = paramActionEvent.getSource();
    if ((!localObject1.equals(this.runBut)) && (!localObject1.equals(this.list))) {
      return;
    }
    int i = this.list.getSelectedIndex();
    if (i < 0) {
      return;
    }
    String str = (String)this.classes.elementAt(i);
    try
    {
      Object localObject2 = Class.forName(this.pkg + "." + str).newInstance();
      ((PowerCalculator)localObject2).setMaster(this);
    }
    catch (Exception localException)
    {
      Utility.warning(localException, true);
    }
  }
}
