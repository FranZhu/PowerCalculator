package pc.util;

public interface Value
{
  public abstract double val();
  
  public abstract boolean isValid(double paramDouble);
  
  public abstract void setValue(double paramDouble);
  
  public abstract String getName();
  
  public abstract String getLabel();
  
  public abstract void setLabel(String paramString);
}
