package org.soundbytes.wave;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WaveInputStream extends InputStream
{

  public static void main(String[] args)
  {
    try
    {
      // WaveInputStream wis = new WaveInputStream(new FileInputStream("wav/1.wav"));
      // System.out.println(wis);
      WaveInputStream wis = new WaveInputStream(new FileInputStream("wav/1.wav"));
      System.out.println(wis);
      wis = new WaveInputStream(new FileInputStream("wav/ac3_1.wav"));
      System.out.println(wis);
      wis = new WaveInputStream(new FileInputStream("wav/pro_1.wav"));
      System.out.println(wis);
      wis = new WaveInputStream(new FileInputStream("wav/chan-id.wav"));
      System.out.println(wis);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private InputStream in;
  private WaveFormat waveFormat;

  public WaveInputStream(InputStream in)
  {
    super();
    this.in = in;
    waveFormat = WaveFormat.read(in);
  }

  public void close() throws IOException
  {
    in.close();
  }

  public WaveFormat getWaveFormat()
  {
    return waveFormat;
  }

  public int read() throws IOException
  {
    return in.read();
  }

  public String toString()
  {
    return "[wis " + waveFormat.toString() + "]";
  }
}
