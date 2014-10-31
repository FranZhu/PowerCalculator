package pc.powercalculator;

import java.awt.AWTEventMulticaster;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.StringTokenizer;
import pc.awt.RVLayout;
import pc.util.Utility;

public class PiArrayField
  extends Panel
  implements ActionComponent, KeyListener
{
  private String name;
  private String label;
  private Font font = new Font("Serif", 1, 12);
  private TextField field;
  private transient ActionListener actionListener = null;
  
  public PiArrayField(String paramString1, String paramString2, double[] paramArrayOfDouble)
  {
    this(paramString1, paramString2, paramArrayOfDouble, 12);
  }
  
  public PiArrayField(String paramString1, String paramString2, double[] paramArrayOfDouble, int paramInt)
  {
    setName(paramString1, paramString2);
    setLayout(new RVLayout(2, false, true));
    Label localLabel = new Label(paramString2);
    localLabel.setFont(this.font);
    add(localLabel);
    this.field = new TextField(paramInt);
    setValue(paramArrayOfDouble);
    add(this.field);
    this.field.addKeyListener(this);
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
  }
  
  public void addActionListener(ActionListener paramActionListener)
  {
    this.actionListener = AWTEventMulticaster.add(this.actionListener, paramActionListener);
  }
  
  public double[] getValue()
  {
    StringTokenizer localStringTokenizer = new StringTokenizer(this.field.getText(), " ,\t");
    
    int i = localStringTokenizer.countTokens();
    double[] arrayOfDouble = new double[i];
    for (int j = 0; j < i; j++)
    {
      String str = localStringTokenizer.nextToken();
      arrayOfDouble[j] = Utility.strtod(str);
    }
    return arrayOfDouble;
  }
  
  public void setValue(double[] paramArrayOfDouble)
  {
    String str = "";
    for (int i = 0; i < paramArrayOfDouble.length; i++) {
      str = str + Utility.format(paramArrayOfDouble[i], 3) + " ";
    }
    this.field.setText(str);
  }
  
  public void setEditable(boolean paramBoolean)
  {
    this.field.setEditable(paramBoolean);
  }
  
  public boolean isEditable()
  {
    return this.field.isEditable();
  }
  
  public void setBackground(Color paramColor)
  {
    super.setBackground(paramColor);
    this.field.setBackground(this.field.isEditable() ? Color.white : paramColor);
  }
  
  public void setForeground(Color paramColor)
  {
    super.setForeground(paramColor);
    this.field.setForeground(paramColor);
  }
  
  public void keyPressed(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getKeyCode() != 10) {
      return;
    }
    ActionEvent localActionEvent = new ActionEvent(this, 1001, "" + toString());
    

    this.actionListener.actionPerformed(localActionEvent);
  }
  
  public void keyReleased(KeyEvent paramKeyEvent) {}
  
  public void keyTyped(KeyEvent paramKeyEvent) {}
}
