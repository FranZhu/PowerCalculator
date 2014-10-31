package pc.util;

public class Param
  implements Value
{
  public String name;
  public String label;
  public double value;
  public double max = (1.0D / 0.0D);
  public double min = (-1.0D / 0.0D);
  public boolean closedMin = false;
  public boolean closedMax = false;
  
  public Param(String paramString, double paramDouble)
  {
    this.name = new String(paramString);
    this.label = this.name;
    this.value = paramDouble;
  }
  
  public Param(String paramString1, double paramDouble, String paramString2)
  {
    this.name = new String(paramString1);
    this.label = this.name;
    this.value = paramDouble;
    this.closedMin = paramString2.startsWith("[");
    this.closedMax = paramString2.endsWith("]");
    int i = paramString2.indexOf(",");
    String str1 = new String(paramString2.substring(1, i).trim());
    String str2 = new String(paramString2.substring(i + 1, paramString2.length() - 1).trim());
    if (str1.length() > 0) {
      this.min = Double.valueOf(str1).doubleValue();
    }
    if (str2.length() > 0) {
      this.max = Double.valueOf(str2).doubleValue();
    }
  }
  
  public boolean isValid(double paramDouble)
  {
    int i = paramDouble <= this.min ? 0 : this.closedMin ? 1 : paramDouble < this.min ? 0 : 1;
    int j = paramDouble >= this.max ? 0 : this.closedMax ? 1 : paramDouble > this.max ? 0 : 1;
    return (i != 0) && (j != 0);
  }
  
  public double val()
  {
    return this.value;
  }
  
  public void setValue(double paramDouble)
  {
    if (isValid(paramDouble)) {
      this.value = paramDouble;
    } else {
      Utility.warning("Illegal value of " + this.label + ": " + paramDouble);
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
