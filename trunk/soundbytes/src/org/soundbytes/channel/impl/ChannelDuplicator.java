package org.soundbytes.channel.impl;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;

public class ChannelDuplicator
{

  private class CopyChannel implements Channel
  {

    private ChunkChannel chunkChannel;

    public CopyChannel()
    {
      super();
      chunkChannel = new ChunkChannel();
    }

    public Chunk nextChunk()
    {
      synchronized (ChannelDuplicator.this)
      {
        Chunk chunk = chunkChannel.nextChunk();
        if (chunk != null)
        {
          return chunk;
        }
        chunk = source.nextChunk();
        if (chunk == null)
        {
          return null;
        }
        for (CopyChannel copyChannel : copies)
        {
          copyChannel.chunkChannel.addChunk(chunk);
        }
        return chunkChannel.nextChunk();
      }
    }
  }

  private Channel source;
  private CopyChannel[] copies;

  public ChannelDuplicator(Channel source, int times)
  {
    super();
    this.source = source;
    copies = new CopyChannel[times];
    for (int i = 0; i < times; i++)
    {
      copies[i] = new CopyChannel();
    }
  }

  public Channel getChannel(int i)
  {
    return copies[i];
  }
}
