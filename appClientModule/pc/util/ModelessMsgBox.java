package pc.util;

import java.awt.Window;
import pc.powercalculator.PowerCalculator;

public class ModelessMsgBox
  extends PowerCalculator
{
  private String msg;
  private Closeable listener;
  
  public void gui()
  {
    label(this.msg);
    button("ok", "OK");
  }
  
  public void ok()
  {
    this.listener.close();
    dispose();
  }
  
  public ModelessMsgBox(String paramString1, String paramString2, Closeable paramCloseable)
  {
    super(paramString1, false);
    this.msg = paramString2;
    this.listener = paramCloseable;
    build();
  }
  
  public void click() {}
}
