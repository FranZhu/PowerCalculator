package pc.powercalculator;

import java.awt.event.ActionListener;

public abstract interface PiComponent
{
  public abstract String getName();
  
  public abstract String getLabel();
  
  public abstract void setName(String paramString1, String paramString2);
  
  public abstract void addActionListener(ActionListener paramActionListener);
}
