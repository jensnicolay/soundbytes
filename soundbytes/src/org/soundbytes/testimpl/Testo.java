package org.soundbytes.testimpl;

import org.soundbytes.channel.impl.SineWaveChannel;
import org.soundbytes.wave.WaveUtils;


public class Testo
{
  public static void main(String[] args) throws Exception
  {
    double freq = 440;
    SineWaveChannel swc = new SineWaveChannel(freq, (int) (freq / 2.2));
    WaveUtils.playWithAudioSystem(swc);
  }
}
