package pc.stat.anova;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.io.PrintStream;
import pc.util.Utility;

public class GetFactorsGUI
  extends Frame
{
  Model model;
  List facList;
  boolean firstTime = true;
  boolean random = false;
  Checkbox randomBox;
  Checkbox fixedBox;
  TextField nLev;
  TextField facName;
  
  public GetFactorsGUI(Model paramModel)
  {
    this.model = paramModel;
    setTitle("Enter factors for ANOVA model");
    

    Panel localPanel1 = new Panel();
    localPanel1.setLayout(new GridLayout(5, 1));
    localPanel1.add(new Label("Name of factor"));
    this.facName = new TextField(10);
    this.nLev = new TextField(4);
    localPanel1.add(this.facName);
    
    Panel localPanel2 = new Panel();
    CheckboxGroup localCheckboxGroup = new CheckboxGroup();
    this.fixedBox = new Checkbox("Fixed", localCheckboxGroup, true);
    this.randomBox = new Checkbox("Random", localCheckboxGroup, false);
    localPanel2.add(this.fixedBox);
    localPanel2.add(this.randomBox);
    localPanel1.add(localPanel2);
    localPanel1.add(new Label("# levels"));
    localPanel1.add(this.nLev);
    

    Panel localPanel3 = new Panel();
    localPanel3.setLayout(new BorderLayout());
    localPanel3.add("North", new Label("Nested in..."));
    this.facList = new List(3, true);
    localPanel3.add("Center", this.facList);
    

    Panel localPanel4 = new Panel();
    localPanel4.setLayout(new FlowLayout(0));
    localPanel4.add(new Button("Accept"));
    localPanel4.add(new Button("Finish"));
    localPanel4.add(new Button("Start over"));
    localPanel4.add(new Button("Cancel"));
    

    setLayout(new BorderLayout());
    add("West", localPanel1);
    add("South", localPanel4);
    add("Center", localPanel3);
    
    resize(350, 200);
    show();
  }
  
  public boolean HandleEvent(Event paramEvent)
  {
    if (paramEvent.id == 201)
    {
      System.out.println("Window closed");
      dispose();
      return true;
    }
    return super.handleEvent(paramEvent);
  }
  
  public boolean action(Event paramEvent, Object paramObject)
  {
    if (paramObject.equals("Cancel"))
    {
      dispose();
      System.exit(0);
    }
    else if (paramEvent.target.equals(this.fixedBox))
    {
      this.random = false;
    }
    else if (paramEvent.target.equals(this.randomBox))
    {
      this.random = true;
    }
    else if (paramObject.equals("Accept"))
    {
      String str = this.facName.getText();
      int i = Utility.strtoi(this.nLev.getText());
      int j = 0;
      Object localObject = new Term();
      for (int k = 0; k < this.facList.countItems(); k++) {
        if (this.facList.isSelected(k))
        {
          localObject = j != 0 ? new Term((Term)localObject, this.model.getFac(k)) : this.model.getFac(k);
          

          this.facList.deselect(k);
          j = 1;
        }
      }
      Factor localFactor = j != 0 ? new Factor(str, i, (Term)localObject) : new Factor(str, i, this.random);
      

      this.model.addFactor(localFactor);
      this.facList.addItem(localFactor.getName());
      this.facName.setText("");
      this.nLev.setText("");
      this.random = false;
      this.fixedBox.setState(true);
    }
    else if (paramObject.equals("Finish"))
    {
      this.model.printEMS();
    }
    return true;
  }
}
