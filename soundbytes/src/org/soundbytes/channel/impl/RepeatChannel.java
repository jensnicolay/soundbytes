package org.soundbytes.channel.impl;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;
import org.soundbytes.mixer.MixerChannel;
import org.soundbytes.mixer.impl.ChannelMixStrategy;
import org.soundbytes.mixer.impl.MixerImpl;
import org.soundbytes.wave.WaveFileChannels;

public class RepeatChannel implements Channel
{

  public static void main(String[] args)
  {
    WaveFileChannels left = new WaveFileChannels("wav/left");
  }

  private MixerChannel mixer;

  public RepeatChannel(Channel source, int times, int repeatInterval)
  {
    super();
    ChannelDuplicator duplicator = new ChannelDuplicator(source, times);
    mixer = new MixerChannel(new MixerImpl<Chunk>(new ChannelMixStrategy(), 65536));
    mixer.addChannel(duplicator.getChannel(0));
    for (int i = 1; i < times; i++)
    {
      mixer.addChannel(new TimeshiftChannel(duplicator.getChannel(i), i * repeatInterval));
    }
  }

  public Chunk nextChunk()
  {
    return mixer.nextChunk();
  }
}
