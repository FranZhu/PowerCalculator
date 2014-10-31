package pc.powercalculator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import pc.util.Utility;

public class PiDoubleText
  extends Component
  implements DoubleComponent
{
  private String name;
  private String label;
  private double value;
  private int digits;
  
  public PiDoubleText(String paramString1, String paramString2, double paramDouble, int paramInt)
  {
    setFont(new Font("Serif", 1, 12));
    setName(paramString1, paramString2);
    setDigits(paramInt);
  }
  
  public PiDoubleText(String paramString1, String paramString2, double paramDouble)
  {
    this(paramString1, paramString2, paramDouble, 4);
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public String getLabel()
  {
    return this.label;
  }
  
  public double getValue()
  {
    return this.value;
  }
  
  public void setName(String paramString1, String paramString2)
  {
    this.name = paramString1;
    this.label = paramString2;
    if (isValid()) {
      repaint();
    }
  }
  
  public void setValue(double paramDouble)
  {
    this.value = paramDouble;
    repaint();
  }
  
  public void setDigits(int paramInt)
  {
    this.digits = paramInt;
    if (isValid()) {
      repaint();
    }
  }
  
  public void addActionListener(ActionListener paramActionListener) {}
  
  public void paint(Graphics paramGraphics)
  {
    paramGraphics.setColor(getForeground());
    paramGraphics.setFont(getFont());
    FontMetrics localFontMetrics = paramGraphics.getFontMetrics(getFont());
    int i = localFontMetrics.stringWidth("m") / 2;
    int j = 3 * localFontMetrics.getAscent() / 2;
    paramGraphics.drawString(this.label + " = " + Utility.format(this.value, this.digits), i, j);
  }
  
  public Dimension getPreferredSize()
  {
    FontMetrics localFontMetrics = getGraphics().getFontMetrics(getFont());
    int i = localFontMetrics.stringWidth("m");int j = localFontMetrics.getHeight();
    return new Dimension(localFontMetrics.stringWidth(this.label) + this.digits * i, 3 * j / 2);
  }
  
  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }
  
  public Dimension preferredSize()
  {
    return getPreferredSize();
  }
  
  public Dimension minimumSize()
  {
    return getMinimumSize();
  }
}
