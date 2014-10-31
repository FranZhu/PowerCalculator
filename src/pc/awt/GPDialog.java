package pc.awt;

import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Point;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GPDialog
  extends Dialog
  implements ActionListener
{
  protected int cols = 30;
  protected int rows = 4;
  protected boolean ok = false;
  protected Component[] component;
  Object localObject = new Button("OK");
  
  public GPDialog(Frame paramFrame, String paramString, String[][] paramArrayOfString)
  {
	super(paramFrame, paramString, true);
    setLayout(new RVLayout(1));
    
    //Object localObject = new Button("OK");
    
    this.component = new Component[paramArrayOfString.length];
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      add(new Label(paramArrayOfString[i][0]));
      if (paramArrayOfString[i][1].equals("TextField"))
      {
        localObject = new TextField(this.cols);
        add((Component)localObject);
        this.component[i] = (Component)localObject;
      }
      else
      {
        localObject = new TextArea(this.rows, this.cols);
        add((Component)localObject);
        this.component[i] = (Component)localObject;
      }
    }
    Panel localPanel = new Panel();
    localPanel.setLayout(new FlowLayout(2));
    localObject = new Button("OK");
    Button localButton = new Button("Cancel");
    localPanel.add((Component)localObject);
    localPanel.add(localButton);
    ((Button)localObject).addActionListener(this);
    localButton.addActionListener(this);
    add(localPanel);
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        GPDialog.this.ok = false;
        GPDialog.this.dispose();
      }
    });
    Point localPoint = paramFrame.getLocation();
    localPoint.x += 50;localPoint.y += 50;
    setLocation(localPoint);
    
    pack();
  }
  
  public GPDialog(Frame paramFrame, String paramString, String[] paramArrayOfString, Component[] paramArrayOfComponent, boolean paramBoolean)
  {
    super(paramFrame, paramString, true);
    setLayout(new RVLayout(1));
    
    this.component = paramArrayOfComponent;
    for (int i = 0; i < paramArrayOfString.length; i++)
    {
      add(new Label(paramArrayOfString[i]));
      add(paramArrayOfComponent[i]);
    }
    Panel localPanel = new Panel();
    localPanel.setLayout(new FlowLayout(2));
    Button localButton = new Button("OK");
    localPanel.add(localButton);
    localButton.addActionListener(this);
    if (paramBoolean)
    {
      localObject = new Button("Cancel");
      ((Button)localObject).addActionListener(this);
      localPanel.add((Component)localObject);
    }
    add(localPanel);
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        GPDialog.this.ok = false;
        GPDialog.this.dispose();
      }
    });
    Point localObject1 = paramFrame.getLocation();
    localObject1.x += 50;localObject1.y += 50;
    setLocation((Point)localObject);
    
    pack();
  }
  
  public GPDialog(Frame paramFrame, String paramString, String[] paramArrayOfString, Component[] paramArrayOfComponent)
  {
    this(paramFrame, paramString, paramArrayOfString, paramArrayOfComponent, true);
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    String str = paramActionEvent.getActionCommand();
    if (str.equals("OK")) {
      this.ok = true;
    } else if (str.equals("Cancel")) {
      this.ok = false;
    }
    dispose();
  }
  
  public static String[] dialog(Frame paramFrame, String paramString, String[][] paramArrayOfString)
  {
    if (paramFrame == null) {
      paramFrame = new Frame();
    }
    GPDialog localGPDialog = new GPDialog(paramFrame, paramString, paramArrayOfString);
    localGPDialog.show();
    if (localGPDialog.ok)
    {
      String[] arrayOfString = new String[paramArrayOfString.length];
      for (int i = 0; i < paramArrayOfString.length; i++)
      {
        Component localComponent = localGPDialog.component[i];
        if ((localComponent instanceof TextField)) {
          arrayOfString[i] = ((TextField)localComponent).getText();
        } else if ((localComponent instanceof TextArea)) {
          arrayOfString[i] = ((TextArea)localComponent).getText();
        }
      }
      return arrayOfString;
    }
    return null;
  }
  
  public static String[] dialog(Frame paramFrame, String paramString, String[] paramArrayOfString, Component[] paramArrayOfComponent)
  {
    if (paramFrame == null) {
      paramFrame = new Frame();
    }
    GPDialog localGPDialog = new GPDialog(paramFrame, paramString, paramArrayOfString, paramArrayOfComponent);
    localGPDialog.show();
    if (paramArrayOfComponent.length > 0) {
      paramArrayOfComponent[0].requestFocus();
    }
    if (localGPDialog.ok)
    {
      String[] arrayOfString = new String[paramArrayOfString.length];
      for (int i = 0; i < paramArrayOfString.length; i++)
      {
        Component localComponent = localGPDialog.component[i];
        if ((localComponent instanceof TextComponent)) {
          arrayOfString[i] = ((TextComponent)localComponent).getText();
        } else {
          arrayOfString[i] = ((String)null);
        }
      }
      return arrayOfString;
    }
    return null;
  }
  
  public static void msgBox(Frame paramFrame, String paramString1, String paramString2)
  {
    if (paramFrame == null) {
      paramFrame = new Frame();
    }
    GPDialog localGPDialog = new GPDialog(paramFrame, paramString1, new String[] { paramString2 }, new Component[] { new GridLine(0) }, false);
    
    localGPDialog.show();
  }
  
  public static boolean okCancelDialog(Frame paramFrame, String paramString1, String paramString2)
  {
    if (paramFrame == null) {
      paramFrame = new Frame();
    }
    GPDialog localGPDialog = new GPDialog(paramFrame, paramString1, new String[] { paramString2 }, new Component[] { new Label("") });
    
    localGPDialog.show();
    return localGPDialog.ok;
  }
  
  public static String stringDialog(Frame paramFrame, String paramString1, String paramString2, String paramString3, int paramInt)
  {
    if (paramFrame == null) {
      paramFrame = new Frame();
    }
    TextField localTextField = new TextField(paramString3, paramInt);
    GPDialog localGPDialog = new GPDialog(paramFrame, paramString1, new String[] { paramString2 }, new Component[] { localTextField });
    
    localTextField.requestFocus();
    localGPDialog.show();
    if (localGPDialog.ok) {
      return localTextField.getText();
    }
    return (String)null;
  }
  
  public static String stringDialog(Frame paramFrame, String paramString1, String paramString2)
  {
    return stringDialog(paramFrame, paramString1, paramString2, "", 30);
  }
  
  public static String stringDialog(Frame paramFrame, String paramString1, String paramString2, String paramString3)
  {
    return stringDialog(paramFrame, paramString1, paramString2, paramString3, 30);
  }
  
  public static int intDialog(Frame paramFrame, String paramString1, String paramString2, String paramString3)
  {
    String str = stringDialog(paramFrame, paramString1, paramString2, paramString3, 5);
    if (str == null) {
      return -2147483648;
    }
    try
    {
      return new Integer(str).intValue();
    }
    catch (NumberFormatException localNumberFormatException)
    {
      msgBox(paramFrame, paramString1, "Invalid integer: " + str + " - try again");
    }
    return intDialog(paramFrame, paramString1, paramString2, paramString3);
  }
  
  public static int intDialog(Frame paramFrame, String paramString1, String paramString2, int paramInt)
  {
    return intDialog(paramFrame, paramString1, paramString2, "" + paramInt);
  }
  
  public static int intDialog(Frame paramFrame, String paramString1, String paramString2)
  {
    return intDialog(paramFrame, paramString1, paramString2, "");
  }
  
  public static double doubleDialog(Frame paramFrame, String paramString1, String paramString2, String paramString3)
  {
    String str = stringDialog(paramFrame, paramString1, paramString2, paramString3, 15);
    if (str == null) {
      return (0.0D / 0.0D);
    }
    try
    {
      return new Double(str).doubleValue();
    }
    catch (NumberFormatException localNumberFormatException)
    {
      msgBox(paramFrame, paramString1, "Invalid number: " + str + " - try again");
    }
    return doubleDialog(paramFrame, paramString1, paramString2, paramString3);
  }
  
  public static double doubleDialog(Frame paramFrame, String paramString1, String paramString2, double paramDouble)
  {
    return doubleDialog(paramFrame, paramString1, paramString2, "" + paramDouble);
  }
  
  public static double doubleDialog(Frame paramFrame, String paramString1, String paramString2)
  {
    return doubleDialog(paramFrame, paramString1, paramString2, "");
  }
}
