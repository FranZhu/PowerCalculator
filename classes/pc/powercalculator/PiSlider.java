package pc.powercalculator;

import pc.awt.Slider;

public class PiSlider
  extends Slider
  implements DoubleComponent
{
  private String name;
  private String label;
  
  public PiSlider(String paramString1, String paramString2, double paramDouble)
  {
    super(paramString2, paramDouble);
    setName(paramString1, paramString2);
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
    super.setLabel(paramString2);
  }
}
