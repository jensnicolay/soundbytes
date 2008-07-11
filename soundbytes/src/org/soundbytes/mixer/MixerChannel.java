package org.soundbytes.mixer;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;

public class MixerChannel implements Channel
{

  private Mixer<Chunk> mixer;

  public MixerChannel(Mixer<Chunk> mixer)
  {
    super();
    this.mixer = mixer;
  }

  public void addChannel(Channel channel)
  {
    mixer.addChannel(channel);
  }

  public Chunk nextChunk()
  {
    return mixer.mix();
  }

  public void removeChannel(Channel channel)
  {
    mixer.removeChannel(channel);
  }
}
