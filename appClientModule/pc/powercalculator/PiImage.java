package pc.powercalculator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

public class PiImage
  extends Component
{
  private Image image = null;
  private int w;
  private int h;
  
  public PiImage(String paramString, Class paramClass)
  {
    this(paramString, paramClass, 0, 0);
  }
  
  public PiImage(String paramString, Class paramClass, int paramInt1, int paramInt2)
  {
    try
    {
      InputStream localInputStream = paramClass.getResourceAsStream(paramString);
      int i = localInputStream.available();int j = 0;
      byte[] arrayOfByte = new byte[i];
      while (j < i) {
        j += localInputStream.read(arrayOfByte, j, i - j);
      }
      localInputStream.close();
      this.image = Toolkit.getDefaultToolkit().createImage(arrayOfByte, 0, i);
      if (paramInt1 * paramInt2 > 0)
      {
        this.w = paramInt1;
        this.h = paramInt2;
      }
      else
      {
        MediaTracker localMediaTracker = new MediaTracker(this);
        localMediaTracker.addImage(this.image, 0);
        localMediaTracker.waitForID(0);
        this.w = this.image.getWidth(this);
        this.h = this.image.getHeight(this);
      }
    }
    catch (IOException localIOException)
    {
      System.err.println("PiImage(\"" + paramString + "\",Class)\n" + localIOException.toString());
    }
    catch (InterruptedException localInterruptedException)
    {
      System.err.println("PiImage(\"" + paramString + "\",Class)\n" + localInterruptedException.toString());
    }
  }
  
  public void paint(Graphics paramGraphics)
  {
    if (this.image != null)
    {
      Dimension localDimension1 = getPreferredSize();
      Dimension localDimension2 = getSize();
      int i = (localDimension2.width - localDimension1.width) / 2;
      int j = (localDimension2.height - localDimension1.height) / 2;
      paramGraphics.drawImage(this.image, i, j, this);
    }
  }
  
  public Dimension getPreferredSize()
  {
    if (this.image == null) {
      return new Dimension(0, 0);
    }
    return new Dimension(this.w, this.h);
  }
  
  public Dimension preferredSize()
  {
    return getPreferredSize();
  }
}
