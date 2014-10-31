package pc.util;

public class Category
  implements Value
{
  public String name;
  public String label;
  public String[] catName;
  public double value;
  
  public Category(String paramString, String[] paramArrayOfString, int paramInt)
  {
    this.name = new String(paramString);
    this.label = this.name;
    this.value = (0.0D + paramInt);
    this.catName = new String[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++) {
      this.catName[i] = new String(paramArrayOfString[i]);
    }
  }
  
  public boolean isValid(double paramDouble)
  {
    return (paramDouble > -0.5D) && (paramDouble < this.catName.length - 0.5D);
  }
  
  public double val()
  {
    return Math.round(this.value);
  }
  
  public void setValue(double paramDouble)
  {
    if (isValid(paramDouble)) {
      this.value = paramDouble;
    }
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public void setLabel(String paramString)
  {
    this.label = new String(paramString);
  }
}
