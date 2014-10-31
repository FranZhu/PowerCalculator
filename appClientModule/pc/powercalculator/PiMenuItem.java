package pc.powercalculator;

import java.awt.MenuItem;

public class PiMenuItem
  extends MenuItem
  implements ActionComponent
{
  private String methodName;
  private String label;
  
  PiMenuItem(String paramString1, String paramString2)
  {
    super(paramString2);
    setName(paramString1, paramString2);
  }
  
  public String getName()
  {
    return this.methodName;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public void setName(String paramString1, String paramString2)
  {
    this.methodName = paramString1;
    this.label = paramString2;
  }
}
