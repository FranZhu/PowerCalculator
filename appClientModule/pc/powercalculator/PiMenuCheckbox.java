package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.CheckboxMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class PiMenuCheckbox
  extends CheckboxMenuItem
  implements IntComponent, ItemListener
{
  private String name;
  private String label;
  private transient ActionListener actionListener = null;
  
  PiMenuCheckbox(String paramString1, String paramString2, int paramInt)
  {
    super(paramString2, paramInt != 0);
    setName(paramString1, paramString2);
    addItemListener(this);
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
  
  public int getValue()
  {
    return getState() ? 1 : 0;
  }
  
  public String getTextValue()
  {
    return getState() ? "true" : "false";
  }
  
  public void setValue(int paramInt)
  {
    setState(paramInt != 0);
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
