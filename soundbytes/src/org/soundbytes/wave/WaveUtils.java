package org.soundbytes.wave;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class WaveUtils
{

  public static void playWithAudioSystem(String waveFile)
  {
    try
    {
      AudioInputStream ais = AudioSystem.getAudioInputStream(new File(waveFile));
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, ais.getFormat());
      SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
      sdl.open();
      sdl.start();
      byte[] buffer = new byte[65536];
      int read;
      while ((read = ais.read(buffer)) != -1)
      {
        sdl.write(buffer, 0, read);
      }
      sdl.drain();
      sdl.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private WaveUtils()
  {
    super();
  }
}
