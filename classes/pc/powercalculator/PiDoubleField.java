package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pc.awt.DoubleField;
import pc.awt.RVLayout;

public class PiDoubleField
  extends Panel
  implements DoubleComponent, ActionListener
{
  private String name;
  private String label;
  private Font font = new Font("Serif", 1, 12);
  private DoubleField field;
  private Label lbl;
  private transient ActionListener actionListener = null;
  
  public PiDoubleField(String paramString1, String paramString2, double paramDouble)
  {
    this(paramString1, paramString2, paramDouble, 8, 5);
  }
  
  public PiDoubleField(String paramString1, String paramString2, double paramDouble, int paramInt1, int paramInt2)
  {
    setLayout(new RVLayout(2, false, true));
    this.lbl = new Label(paramString2);
    this.lbl.setFont(this.font);
    add(this.lbl);
    this.field = new DoubleField(paramDouble, paramInt1, paramInt2);
    this.field.addActionListener(this);
    add(this.field);
    setName(paramString1, paramString2);
    setVisible(true);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public double getValue()
  {
    return this.field.getValue();
  }
  
  public void setName(String paramString1, String paramString2)
  {
    this.name = paramString1;
    this.label = paramString2;
    this.lbl.setText(paramString2);
  }
  
  public void setValue(double paramDouble)
  {
    this.field.setValue(paramDouble);
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this.field.setEditable(paramBoolean);
  }
  
  public boolean isEditable()
  {
    return this.field.isEditable();
  }
  
  public void setBackground(Color paramColor)
  {
    super.setBackground(paramColor);
    this.field.setBackground(this.field.isEditable() ? Color.white : paramColor);
  }
  
  public void setForeground(Color paramColor)
  {
    super.setForeground(paramColor);
    this.field.setForeground(paramColor);
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public Dimension getPreferredSize()
  {
    Dimension localDimension1 = this.lbl.getPreferredSize();
    Dimension localDimension2 = this.field.getPreferredSize();
    int i = localDimension1.width + localDimension2.width;
    int j = Math.max(localDimension1.height, localDimension2.height);
    return new Dimension(i, j);
  }
  
  public Dimension getMinimumSize()
  {
    Dimension localDimension1 = this.lbl.getMinimumSize();
    Dimension localDimension2 = this.field.getMinimumSize();
    int i = localDimension1.width + localDimension2.width;
    int j = Math.max(localDimension1.height, localDimension2.height);
    return new Dimension(i, j);
  }
  
  public Dimension preferredSize()
  {
    return getPreferredSize();
  }
  
  public Dimension minimumSize()
  {
    return getMinimumSize();
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (!this.field.isEditable()) {
      return;
    }
    ActionEvent localActionEvent = new ActionEvent(this, 1001, "" + getValue());
    

    this.actionListener.actionPerformed(localActionEvent);
  }
}
