package pc.powercalculator;

public abstract interface DoubleComponent
  extends PiComponent
{
  public abstract double getValue();
  
  public abstract void setValue(double paramDouble);
}
