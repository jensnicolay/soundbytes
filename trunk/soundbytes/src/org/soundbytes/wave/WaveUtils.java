package org.soundbytes.wave;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

import org.soundbytes.channel.Channel;
import org.soundbytes.wave.WaveFormat.Format;

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

  public static void playWithAudioSystem(Channel... channels)
  {
    try
    {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      CopyOfWaveFilePort wfp = new CopyOfWaveFilePort(baos, new WaveFormat(Format.PCM, 1, 16, 48000), channels);
      wfp.run();
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      //bais.read(new byte[44]); // skip header
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, new AudioFormat(48000, 16, 1, true, false));
      SourceDataLine sdl = (SourceDataLine) AudioSystem.getLine(info);
      sdl.open();
      sdl.start();
      byte[] buffer = new byte[65536];
      int read;
      while ((read = bais.read(buffer)) != -1)
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
