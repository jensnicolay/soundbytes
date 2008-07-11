package org.soundbytes.channel.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;

public class ChunkChannel implements Channel
{

  private List<Chunk> chunks;

  public ChunkChannel(Chunk... chunks)
  {
    super();
    this.chunks = new LinkedList<Chunk>(Arrays.asList(chunks));
  }

  public synchronized void addChunk(Chunk chunk)
  {
    chunks.add(chunk);
  }

  public synchronized Chunk nextChunk()
  {
    if (chunks.isEmpty())
    {
      return null;
    }
    else
    {
      return chunks.remove(0);
    }
  }

  public String toString()
  {
    return "[chunkChannel " + (chunks.size() < 4 ? chunks : "#" + chunks.size()) + "]";
  }
}
