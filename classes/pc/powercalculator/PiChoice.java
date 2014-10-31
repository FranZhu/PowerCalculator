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

public class PiChoice
  extends Panel
  implements IntComponent, ItemListener
{
  private String name;
  private String lbl;
  private transient ActionListener actionListener = null;
  private Font font = new Font("Serif", 0, 12);
  private Font bfont = new Font("Serif", 1, 12);
  private Label label;
  private Choice choice = new Choice();
  
  PiChoice(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    setLayout(new RVLayout(2, false, true));
    this.label = new Label(paramString2);
    this.label.setFont(this.bfont);
    this.choice.setFont(this.font);
    add(this.label);
    setName(paramString1, paramString2);
    for (int i = 0; i < paramArrayOfString.length; i++) {
      this.choice.add(paramArrayOfString[i]);
    }
    this.choice.addItemListener(this);
    setValue(paramInt);
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
  
  public int getValue()
  {
    return this.choice.getSelectedIndex();
  }
  
  public String getTextValue()
  {
    return this.choice.getSelectedItem();
  }
  
  public void setValue(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = 0;
    } else if (paramInt >= this.choice.getItemCount()) {
      paramInt = this.choice.getItemCount() - 1;
    }
    this.choice.select(paramInt);
  }
  
  public Choice getChoice()
  {
    return this.choice;
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
