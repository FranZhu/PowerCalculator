package pc.awt;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ViewWindow
  extends Frame
  implements ActionListener, AdjustmentListener
{
  public TextArea ta;
  private Scrollbar fontSB;
  private Label fontLab = new Label("12 pt    ");
  private int fontSize = 12;
  private Button clearButton;
  
  public ViewWindow(String paramString, int paramInt1, int paramInt2)
  {
    setTitle(paramString);
    setLayout(new BorderLayout());
    this.ta = new TextArea(paramInt1, paramInt2);
    this.ta.setEditable(false);
    this.ta.setFont(new Font("Courier", 0, this.fontSize));
    
    Panel localPanel = new Panel();
    localPanel.setBackground(Color.lightGray);
    localPanel.setLayout(new FlowLayout());
    this.fontSB = new Scrollbar(0, this.fontSize, 1, 6, 19);
    this.clearButton = new Button("Clear");
    this.clearButton.setVisible(false);
    Button localButton = new Button("Close");
    localPanel.add(new Label("Font size"));
    localPanel.add(this.fontSB);
    localPanel.add(this.fontLab);
    localPanel.add(this.clearButton);
    localPanel.add(localButton);
    localButton.addActionListener(this);
    this.fontSB.addAdjustmentListener(this);
    
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent paramAnonymousWindowEvent)
      {
        ViewWindow.this.dispose();
      }
    });
    add("Center", this.ta);
    add("South", localPanel);
    pack();
    show();
  }
  
  public void actionPerformed(ActionEvent paramActionEvent)
  {
    if (paramActionEvent.getActionCommand().equals("Close")) {
      dispose();
    } else if (paramActionEvent.getActionCommand().equals("Clear")) {
      this.ta.setText("");
    }
  }
  
  public void setClearButton(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.clearButton.addActionListener(this);
      this.clearButton.setVisible(true);
      show();
    }
    else
    {
      this.clearButton.removeActionListener(this);
      this.clearButton.setVisible(false);
      show();
    }
  }
  
  public void adjustmentValueChanged(AdjustmentEvent paramAdjustmentEvent)
  {
    if (paramAdjustmentEvent.getSource().equals(this.fontSB))
    {
      this.fontSize = paramAdjustmentEvent.getValue();
      this.fontLab.setText(this.fontSize + " pt    ");
      this.ta.setFont(new Font("Courier", 0, this.fontSize));
    }
  }
  
  public void append(String paramString)
  {
    this.ta.append(paramString);
  }
  
  public void setText(String paramString)
  {
    this.ta.setText(paramString);
  }
  
  public String getText()
  {
    return this.ta.getText();
  }
  
  public void setTop()
  {
    this.ta.setCaretPosition(0);
  }
}
