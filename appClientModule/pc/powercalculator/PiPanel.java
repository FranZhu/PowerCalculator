package pc.powercalculator;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Panel;
import pc.awt.RVLayout;

public class PiPanel
  extends Panel
{
  public static final int NO_BORDER = 0;
  public static final int PLAIN_BORDER = 1;
  public static final int RAISED = 2;
  public static final int LOWERED = 3;
  private int borderType = 0;
  private Color borderColor = null;
  protected boolean isStretchable = false;
  
  public PiPanel() {}
  
  public PiPanel(LayoutManager paramLayoutManager)
  {
    super(paramLayoutManager);
  }
  
  public void setBorderType(int paramInt)
  {
    this.borderType = paramInt;
  }
  
  public void setBorderColor(Color paramColor)
  {
    this.borderColor = paramColor;
  }
  
  public void setStretchable(boolean paramBoolean)
  {
    this.isStretchable = paramBoolean;
    LayoutManager localLayoutManager = getLayout();
    if ((localLayoutManager instanceof RVLayout)) {
      ((RVLayout)localLayoutManager).setStretchable(paramBoolean, true);
    }
    Container localContainer = getParent();
    if ((localContainer != null) && 
      ((localContainer instanceof PiPanel))) {
      ((PiPanel)localContainer).setStretchable(paramBoolean);
    }
  }
  
  public Component add(Component paramComponent)
  {
    if (((paramComponent instanceof PiPanel)) && 
      (((PiPanel)paramComponent).isStretchable)) {
      setStretchable(true);
    }
    return super.add(paramComponent);
  }
  
  public void paint(Graphics paramGraphics)
  {
    int i = getSize().width - 1;int j = getSize().height - 1;
    switch (this.borderType)
    {
    case 0: 
      break;
    case 1: 
      if (this.borderColor == null) {
        setBorderColor(getForeground());
      }
      paramGraphics.setColor(this.borderColor);
      paramGraphics.drawRect(0, 0, i, j);
      break;
    case 2: 
      if (this.borderColor == null) {
        setBorderColor(getBackground());
      }
      paramGraphics.setColor(this.borderColor);
      paramGraphics.draw3DRect(0, 0, i, j, true);
      paramGraphics.setColor(this.borderColor.brighter());
      paramGraphics.draw3DRect(1, 1, i - 2, j - 2, true);
      break;
    case 3: 
      if (this.borderColor == null) {
        setBorderColor(getBackground());
      }
      paramGraphics.setColor(this.borderColor);
      paramGraphics.draw3DRect(0, 0, i, j, false);
      paramGraphics.setColor(this.borderColor.darker());
      paramGraphics.draw3DRect(1, 1, i - 2, j - 2, false);
    }
    super.paint(paramGraphics);
  }
}
