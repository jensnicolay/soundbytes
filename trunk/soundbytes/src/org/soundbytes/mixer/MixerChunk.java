package org.soundbytes.mixer;

import org.soundbytes.Chunk;

public class MixerChunk implements Comparable<MixerChunk>
{

  private Chunk chunk;
  private int channel;

  public MixerChunk(Chunk chunk, int channel)
  {
    super();
    this.chunk = chunk;
    this.channel = channel;
  }

  public int compareTo(MixerChunk o)
  {
    return (int) (getPosition() - o.getPosition());
  }

  public int getChannel()
  {
    return channel;
  }

  public int[] getData()
  {
    return chunk.getData();
  }

  public int getFrom()
  {
    return chunk.getFrom();
  }

  public int getLength()
  {
    return chunk.getLength();
  }

  public long getPosition()
  {
    return chunk.getPosition();
  }

  public String toString()
  {
    return "[mixerChunk " + channel + " " + chunk + "]";
  }
}
