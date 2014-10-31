package org.rosuda.JRI;

import java.io.IOException;
import java.io.OutputStream;

public class RConsoleOutputStream
  extends OutputStream
{
  Rengine eng;
  int oType;
  boolean isOpen;
  
  public RConsoleOutputStream(Rengine paramRengine, int paramInt)
  {
    this.eng = paramRengine;
    this.oType = paramInt;
    this.isOpen = true;
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (!this.isOpen) {
      throw new IOException("cannot write to a closed stream");
    }
    if (this.eng == null) {
      throw new IOException("missing R engine");
    }
    String str = new String(paramArrayOfByte, paramInt1, paramInt2);
    this.eng.rniPrint(str, this.oType);
  }
  
  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    write(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public void write(int paramInt)
    throws IOException
  {
    write(new byte[] { (byte)(paramInt & 0xFF) });
  }
  
  public void close()
    throws IOException
  {
    this.isOpen = false;this.eng = null;
  }
}
