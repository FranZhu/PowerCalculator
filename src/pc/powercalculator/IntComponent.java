package pc.powercalculator;

public abstract interface IntComponent
  extends PiComponent
{
  public abstract int getValue();
  
  public abstract String getTextValue();
  
  public abstract void setValue(int paramInt);
}
