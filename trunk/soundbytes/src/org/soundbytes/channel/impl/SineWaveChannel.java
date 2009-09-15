package org.soundbytes.channel.impl;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;
import org.soundbytes.wave.WaveFilePort;
import org.soundbytes.wave.WaveFormat;
import org.soundbytes.wave.WaveUtils;


public class SineWaveChannel implements Channel
{
  
  private long position;
  private double freq;
  private int i;
  private int n;
  
  public SineWaveChannel(double freq, int n)
  {
    super();
    this.n = n;
    this.freq = freq;
  }
  
  @Override
  public Chunk nextChunk()
  {
    if (i == n)
    {
      return null;
    }
    int length = (int) (48000 / freq);
    int[] data = new int[length];
    for (int i = 0; i < length; i++)
    {
      data[i] = (int) (Math.sin(Math.PI / length * 2 * i) * 32768);
    }
    Chunk chunk = new Chunk(position, data);
    position += length;
    i++;
    return chunk;
  }
  
  public static void main(String[] args)
  {
    SineWaveChannel swc = new SineWaveChannel(880 / 2, 50);
    WaveFilePort asp = new WaveFilePort("wav/result.wav", new WaveFormat(WaveFormat.Format.PCM, 1, 16, 48000), swc);
    asp.run();
    WaveUtils.playWithAudioSystem("wav/result.wav");
  }
}
