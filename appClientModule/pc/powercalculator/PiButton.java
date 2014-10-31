package pc.powercalculator;

import java.awt.Button;

public class PiButton
  extends Button
  implements ActionComponent
{
  private String methodName;
  private String label;
  
  PiButton(String paramString1, String paramString2)
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
