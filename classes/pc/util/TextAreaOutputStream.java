package pc.util;

import java.awt.TextArea;
import java.io.ByteArrayOutputStream;

public class TextAreaOutputStream
  extends ByteArrayOutputStream
{
  private TextArea textArea;
  
  public TextAreaOutputStream(TextArea paramTextArea)
  {
    this.textArea = paramTextArea;
  }
  
  public void flush()
  {
    this.textArea.append(toString());
    reset();
  }
}
