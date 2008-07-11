package org.soundbytes.channel.impl;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;

public class TimeshiftChannel implements Channel
{

  private Channel source;
  private int timeshift;

  public TimeshiftChannel(Channel source, int timeshift)
  {
    super();
    this.source = source;
    this.timeshift = timeshift;
  }

  public Chunk nextChunk()
  {
    Chunk chunk = source.nextChunk();
    if (chunk == null)
    {
      return null;
    }
    return new Chunk(chunk.getPosition() + timeshift, chunk.getData(), chunk.getFrom(), chunk.getLength());
  }
}
