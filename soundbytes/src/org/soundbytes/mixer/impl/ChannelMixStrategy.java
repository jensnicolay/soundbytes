package org.soundbytes.mixer.impl;

import org.soundbytes.Chunk;
import org.soundbytes.mixer.MixStrategy;
import org.soundbytes.mixer.MixerChunk;

public class ChannelMixStrategy implements MixStrategy<Chunk>
{

  public Chunk mix(MixerChunk[] chunks, long mixPosition, int mixLength)
  {
    int[] mixData = new int[mixLength];
    for (MixerChunk chunk : chunks)
    {
      int chunkPosition = (int) (chunk.getPosition() - mixPosition);
      int[] chunkData = chunk.getData();
      int fromChunk = chunk.getFrom();
      for (int i = 0; i < chunk.getLength(); i++)
      {
        mixData[chunkPosition + i] += chunkData[fromChunk + i];
      }
    }
    return new Chunk(mixPosition, mixData, 0, mixLength);
  }
}
