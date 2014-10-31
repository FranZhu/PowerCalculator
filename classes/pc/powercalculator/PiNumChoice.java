package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Choice;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import pc.awt.RVLayout;

public class PiNumChoice
  extends Panel
  implements DoubleComponent, ItemListener
{
  private String name;
  private String lbl;
  private transient ActionListener actionListener = null;
  private Font font = new Font("Serif", 0, 12);
  private Font bfont = new Font("Serif", 1, 12);
  private Label label;
  private Choice choice = new Choice();
  private double[] values;
  
  PiNumChoice(String paramString1, String paramString2, double[] paramArrayOfDouble, int paramInt)
  {
    setLayout(new RVLayout(2, false, true));
    this.label = new Label(paramString2);
    this.label.setFont(this.bfont);
    this.choice.setFont(this.font);
    add(this.label);
    setName(paramString1, paramString2);
    this.values = new double[paramArrayOfDouble.length];
    for (int i = 0; i < paramArrayOfDouble.length; i++)
    {
      this.choice.add("" + paramArrayOfDouble[i]);
      this.values[i] = paramArrayOfDouble[i];
    }
    this.choice.addItemListener(this);
    this.choice.select(paramInt);
    add(this.choice);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getLabel()
  {
    return this.lbl;
  }
  
  public void setName(String paramString1, String paramString2)
  {
    this.name = paramString1;
    this.lbl = paramString2;
  }
  
  public double getValue()
  {
    return this.values[this.choice.getSelectedIndex()];
  }
  
  public void setValue(double paramDouble)
  {
    if (Math.abs(paramDouble - this.values[this.choice.getSelectedIndex()]) < 1.0E-010D) {
      return;
    }
    int i = 0;
    double d1 = Math.abs(paramDouble - this.values[0]);
    for (int j = 1; j < this.values.length; j++)
    {
      double d2 = Math.abs(paramDouble - this.values[j]);
      if (d2 < d1)
      {
        i = j;
        d1 = d2;
      }
    }
    this.choice.select(i);
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public void itemStateChanged(ItemEvent paramItemEvent)
  {
    ActionEvent localActionEvent = new ActionEvent(this, 1001, "" + getValue());
    

    this.actionListener.actionPerformed(localActionEvent);
  }
}
