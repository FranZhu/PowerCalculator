package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pc.awt.Dotplot;
import pc.awt.RVLayout;

public class PiDotplot
  extends Panel
  implements ActionComponent, ActionListener
{
  private String name;
  private String label;
  private String lastAction = "none";
  private Font font = new Font("Serif", 1, 12);
  private Dotplot dotplot;
  private transient ActionListener actionListener = null;
  
  public PiDotplot(String paramString1, String paramString2, double[] paramArrayOfDouble)
  {
    setName(paramString1, paramString2);
    setLayout(new RVLayout(1, false, true));
    Label localLabel = new Label(paramString2);
    localLabel.setFont(this.font);
    if (paramString2.length() > 0) {
      add(localLabel);
    }
    this.dotplot = new Dotplot(paramArrayOfDouble);
    setValue(paramArrayOfDouble);
    add(this.dotplot);
    this.dotplot.addActionListener(this);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public void setName(String paramString1, String paramString2)
  {
    this.name = paramString1;
    this.label = paramString2;
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public double[] getValue()
  {
    return this.dotplot.getValues();
  }
  
  public void setValue(double[] paramArrayOfDouble)
  {
    this.dotplot.setValues(paramArrayOfDouble);
  }
  
  public void setEditable(boolean paramBoolean) {}
  
  public boolean isEditable()
  {
    return true;
  }
  
  public void setBackground(Color paramColor)
  {
    super.setBackground(paramColor);
    this.dotplot.setBackground(paramColor);
  }
  
  public void setForeground(Color paramColor)
  {
    super.setForeground(paramColor);
    this.dotplot.setForeground(paramColor);
  }
  
  public String getActionCommand()
  {
    return this.lastAction;
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    this.lastAction = paramActionEvent.getActionCommand();
    paramActionEvent = new ActionEvent(this, 1001, "PiDotplot");
    this.actionListener.actionPerformed(paramActionEvent);
  }
}
