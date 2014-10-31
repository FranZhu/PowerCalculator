package pc.powercalculator;

import pc.util.UniFunction;

public class PowerCalculatorAux
  extends UniFunction
{
  PowerCalculator powercalculator;
  String yName;
  String xName;
  
  public PowerCalculatorAux(String paramString1, String paramString2, PowerCalculator paramPowerCalculator)
  {
    this.powercalculator = paramPowerCalculator;
    this.xName = paramString1;
    this.yName = paramString2;
  }
  
  public double of(double paramDouble)
  {
    return this.powercalculator.eval(this.yName, this.xName, paramDouble);
  }
}
