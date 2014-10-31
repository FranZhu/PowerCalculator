package pc.powercalculator.apps;

import java.io.PrintStream;
import pc.awt.Dotplot;
import pc.powercalculator.PowerCalculator;
import pc.util.Utility;

public class TestDotplot
  extends PowerCalculator
{
  private static String title = "Test of Dotplot";
  private Dotplot pd;
  
  public void gui()
  {
    this.pd = dotplot(new double[] { 5.05D, 6.0D, 5.0D, 3.0D, 1.0D, 2.0D, 4.9D });
  }
  
  public void click()
  {
    double[] arrayOfDouble = this.pd.getValues();
    System.out.print("Values:");
    for (int i = 0; i < arrayOfDouble.length; i++) {
      System.out.print("\t" + Utility.format(arrayOfDouble[i], 3));
    }
    System.out.print("\n");
  }
  
  public TestDotplot()
  {
    super(title);
  }
  
  public static void main(String[] paramArrayOfString)
  {
    new TestDotplot();
  }
}
