package pc.awt;

import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.util.StringTokenizer;
import pc.util.Utility;

public class DoubleArrayField
  extends Panel
{
  private String label;
  private Font font = new Font("Serif", 1, 12);
  private TextField field;
  
  public DoubleArrayField(String paramString, double[] paramArrayOfDouble)
  {
    this(paramString, paramArrayOfDouble, 12);
  }
  
  public DoubleArrayField(String paramString, double[] paramArrayOfDouble, int paramInt)
  {
    setLayout(new RVLayout(2, false, true));
    Label localLabel = new Label(paramString);
    localLabel.setFont(this.font);
    add(localLabel);
    this.field = new TextField(paramInt);
    setValue(paramArrayOfDouble);
    add(this.field);
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
}
