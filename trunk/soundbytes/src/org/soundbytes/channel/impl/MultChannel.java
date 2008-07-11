package org.soundbytes.channel.impl;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;

public class MultChannel implements Channel
{

  private Channel source;
  private float value;

  public MultChannel(Channel source, float value)
  {
    super();
    this.source = source;
    this.value = value;
  }

  public Chunk nextChunk()
  {
    Chunk chunk = source.nextChunk();
    if (chunk == null)
    {
      return null;
    }
    int[] chunkData = chunk.getData();
    int[] resultData = new int[chunk.getLength()];
    int from = chunk.getFrom();
    for (int i = 0; i < resultData.length; i++)
    {
      resultData[i] = Math.round(chunkData[from + i] * value);
    }
    return new Chunk(chunk.getPosition(), resultData);
  }
}
