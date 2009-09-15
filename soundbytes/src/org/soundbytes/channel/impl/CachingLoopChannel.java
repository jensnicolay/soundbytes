package org.soundbytes.channel.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;


public class CachingLoopChannel implements Channel
{
  
  private int n;
  private int i;
  private long length;
  private List<Chunk> chunks;
  private Channel out;
  
  public CachingLoopChannel(Channel source, int n)
  {
    super();
    this.n = n;
    chunks = new ArrayList<Chunk>();
    Chunk chunk;
    while ((chunk = source.nextChunk()) != null)
    {
      chunks.add(chunk);
    }
    length = chunks.get(chunks.size() - 1).getPosition() + chunks.get(chunks.size() - 1).getLength() - chunks.get(0).getPosition();
  }
  
  @Override
  public Chunk nextChunk()
  {
    if (i == n)
    {
      return null;
    }
    Chunk chunk;
    if (out == null)
    {
      out = new TimeshiftChannel(new ChunkChannel(chunks.toArray(new Chunk[chunks.size()])), i * length);        
    }
   chunk = out.nextChunk();
    if (chunk == null)
    {
      i++;
      out = null;
      chunk = nextChunk();
    }
    return chunk;
  }
  
  public static void main(String[] args)
  {
    Channel c = new ChunkChannel(new Chunk(new int[] {1, 2, 3}));
    CachingLoopChannel lc = new CachingLoopChannel(c, 3);
    int[] result = ChannelUtils.toIntArray(lc);
    System.out.println(Arrays.toString(result));
  }
}
