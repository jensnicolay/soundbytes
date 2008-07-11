package org.soundbytes.wave;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.soundbytes.channel.Channel;

public class WaveFileChannels
{

  private ByteStreamChannels bsc;
  private WaveFormat waveFormat;

  public WaveFileChannels(String waveFile)
  {
    super();
    WaveInputStream wis = null;
    try
    {
      wis = new WaveInputStream(new FileInputStream(waveFile));
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    waveFormat = wis.getWaveFormat();
    int numChannels = waveFormat.getChannels();
    int bytesPerSample = (waveFormat.getBitsPerSample() >>> 3);
    bsc = new ByteStreamChannels(wis, numChannels, bytesPerSample);
  }

  public Channel getChannel(int channel)
  {
    return bsc.getChannel(channel);
  }

  public WaveFormat getWaveFormat()
  {
    return waveFormat;
  }
}
