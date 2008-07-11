package org.soundbytes.channel.impl;

import java.util.Arrays;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;

public class ChannelUtils
{

  public static int[] toIntArray(Channel channel)
  {
    int[] result = new int[0];
    Chunk chunk;
    while ((chunk = channel.nextChunk()) != null)
    {
      int writePos = result.length;
      int length = chunk.getLength();
      result = Arrays.copyOf(result, writePos + length);
      System.arraycopy(chunk.getData(), chunk.getFrom(), result, writePos, length);
    }
    return result;
  }

  private ChannelUtils()
  {
    super();
  }
}
