package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.lang.reflect.Method;

public class PiActionAdapter
  implements ActionComponent, ActionListener
{
  private String methodName;
  private String label;
  private transient ActionListener actionListener = null;
  
  public PiActionAdapter(String paramString1, String paramString2, Component paramComponent)
  {
    setName(paramString1, paramString2);
    try
    {
      Method localMethod = paramComponent.getClass().getMethod("addActionListener", new Class[] { ActionListener.class });
      

      localMethod.invoke(paramComponent, new Object[] { this });
    }
    catch (Exception localException)
    {
      System.err.println("Can't register component:\n" + paramComponent + "\n" + localException);
    }
  }
  
  public PiActionAdapter(String paramString, Component paramComponent)
  {
    this(paramString, paramString, paramComponent);
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
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    paramActionEvent = new ActionEvent(this, 1001, this.label);
    this.actionListener.actionPerformed(paramActionEvent);
  }
}
