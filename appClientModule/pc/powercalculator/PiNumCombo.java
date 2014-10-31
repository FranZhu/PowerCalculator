package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import pc.awt.RVLayout;
import pc.util.Utility;

public class PiNumCombo
  extends Panel
  implements DoubleComponent, ItemListener
{
  private String name;
  private String lbl;
  private transient ActionListener actionListener = null;
  private Font font = new Font("Serif", 0, 12);
  private Font bfont = new Font("Serif", 1, 12);
  private Label label;
  private JComboBox combo = new JComboBox();
  private double[] values;
  
  PiNumCombo(String paramString1, String paramString2, double[] paramArrayOfDouble, int paramInt)
  {
    setLayout(new RVLayout(2, false, true));
    this.label = new Label(paramString2);
    this.label.setFont(this.bfont);
    this.combo.setFont(this.font);
    add(this.label);
    setName(paramString1, paramString2);
    this.values = new double[paramArrayOfDouble.length];
    for (int i = 0; i < paramArrayOfDouble.length; i++)
    {
      this.combo.addItem("" + paramArrayOfDouble[i]);
      this.values[i] = paramArrayOfDouble[i];
    }
    this.combo.setSelectedIndex(paramInt);
    this.combo.setEditable(true);
    this.combo.addItemListener(this);
    add(this.combo);
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
    String str = (String)this.combo.getEditor().getItem();
    return Utility.strtod(str);
  }
  
  public void setValue(double paramDouble)
  {
    if (Math.abs(paramDouble - getValue()) < 1.0E-010D) {
      return;
    }
    this.combo.getEditor().setItem(Utility.format(paramDouble, 5));
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
