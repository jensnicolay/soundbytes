package org.soundbytes.wave;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WaveOutputStream extends OutputStream
{

  public static void main(String[] args)
  {
    try
    {
      WaveInputStream wis = new WaveInputStream(new FileInputStream("wav/1.wav"));
      System.out.println(wis.getWaveFormat());
      WaveOutputStream wos = new WaveOutputStream(new FileOutputStream("wav/result.wav"), wis.getWaveFormat());
      byte[] buffer = new byte[65536];
      int read;
      while ((read = wis.read(buffer)) != -1)
      {
        wos.write(buffer, 0, read);
      }
      wis.close();
      wos.close();
      WaveInputStream wis2 = new WaveInputStream(new FileInputStream("wav/result.wav"));
      System.out.println(wis2.getWaveFormat());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private OutputStream out;
  private WaveFormat waveFormat;
  private File temp;
  private FileOutputStream fos;
  private long dataSize;

  public WaveOutputStream(OutputStream out, WaveFormat waveFormat) throws IOException
  {
    super();
    this.out = out;
    this.waveFormat = waveFormat;
    temp = File.createTempFile("wos", "tmp", new File("."));
    temp.deleteOnExit();
    fos = new FileOutputStream(temp);
  }

  public void close()
  {
    try
    {
      fos.close();
      FileInputStream fis = new FileInputStream(temp);
      fos = null;
      waveFormat.write(out, dataSize);
      int read;
      byte[] buffer = new byte[65536];
      while ((read = fis.read(buffer)) != -1)
      {
        out.write(buffer, 0, read);
      }
      fis.close();
      temp.delete();
      temp = null;
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public WaveFormat getWaveFormat()
  {
    return waveFormat;
  }

  public String toString()
  {
    return "[wos " + waveFormat.toString() + "]";
  }

  public void write(int b) throws IOException
  {
    fos.write(b);
    dataSize++;
  }
}
