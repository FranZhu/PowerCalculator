package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import pc.awt.RVLayout;

public class PiRadio
  extends Panel
  implements IntComponent, ItemListener
{
  private String name;
  private String lbl;
  private transient ActionListener actionListener = null;
  private Font font = new Font("Serif", 0, 12);
  private Font bfont = new Font("Serif", 1, 12);
  private Label label;
  private CheckboxGroup group = new CheckboxGroup();
  private Checkbox[] buttons;
  private String[] itemLabels;
  private int nButtons = 0;
  
  PiRadio(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt)
  {
    this(paramString1, paramString2, paramArrayOfString, paramInt, paramArrayOfString.length + 1);
  }
  
  PiRadio(String paramString1, String paramString2, String[] paramArrayOfString, int paramInt1, int paramInt2)
  {
    setLayout(new RVLayout(paramInt2, false, true));
    this.label = new Label(paramString2);
    this.label.setFont(this.bfont);
    add(this.label);
    this.buttons = new Checkbox[paramArrayOfString.length];
    this.itemLabels = new String[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      if (paramArrayOfString[i].trim().length() == 0)
      {
        add(new Label(""));
      }
      else
      {
        this.buttons[this.nButtons] = new Checkbox(paramArrayOfString[i]);
        this.buttons[this.nButtons].setCheckboxGroup(this.group);
        this.buttons[this.nButtons].setFont(this.font);
        this.buttons[this.nButtons].addItemListener(this);
        this.itemLabels[this.nButtons] = paramArrayOfString[i];
        add(this.buttons[this.nButtons]);
        this.nButtons += 1;
      }
    }
    setName(paramString1, paramString2);
    setValue(paramInt1);
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
    int i = 0;
    while (!this.buttons[i].getState()) {
      i++;
    }
    return i;
  }
  
  public String getTextValue()
  {
    return this.itemLabels[getValue()];
  }
  
  public void setValue(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = 0;
    } else if (paramInt >= this.nButtons) {
      paramInt = this.nButtons - 1;
    }
    this.group.setSelectedCheckbox(this.buttons[paramInt]);
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
