package org.soundbytes.mixer.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;
import org.soundbytes.mixer.MixStrategy;
import org.soundbytes.mixer.Mixer;
import org.soundbytes.mixer.MixerChunk;

public class MixerImpl<T> implements Mixer<T>
{

  // private static final int DEFAULT_BUFFER_SIZE = 65536;
  private int bufferSize;
  private List<Channel> channels;
  private PriorityQueue<MixerChunk> chunks;
  private long position;
  private MixStrategy<T> mixStrategy;

  public MixerImpl(MixStrategy<T> mixStrategy, int bufferSize)
  {
    super();
    this.mixStrategy = mixStrategy;
    this.bufferSize = bufferSize;
    channels = new LinkedList<Channel>();
    chunks = new PriorityQueue<MixerChunk>();
  }

  public synchronized boolean addChannel(Channel channel)
  {
    return channels.add(channel);
  }

  public synchronized T mix()
  {
    for (int i = 0; i < channels.size(); i++)
    {
      Chunk nextChunk = channels.get(i).nextChunk();
      if (nextChunk != null)
      {
        if (nextChunk.getPosition() < position)
        {
          System.out.println("dropped " + nextChunk + " from " + channels.get(i) + ", position " + position);
        }
        else
        {
          MixerChunk mixerChunk = new MixerChunk(nextChunk, i);
          chunks.add(mixerChunk);
        }
      }
    }
    if (chunks.isEmpty())
    {
      return null;
    }
    List<MixerChunk> poppedChunks = new ArrayList<MixerChunk>();
    long consolidatedStart = position + bufferSize;
    long consolidatedEnd = consolidatedStart;
    while (!chunks.isEmpty() && chunks.peek().getPosition() < consolidatedEnd)
    {
      MixerChunk poppedChunk = chunks.remove();
      poppedChunks.add(poppedChunk);
      consolidatedStart = Math.min(consolidatedStart, poppedChunk.getPosition());
      consolidatedEnd = Math.min(consolidatedEnd, poppedChunk.getPosition() + poppedChunk.getLength());
    }
    MixerChunk[] mixerChunks = new MixerChunk[poppedChunks.size()];
    for (int i = 0; i < poppedChunks.size(); i++)
    {
      MixerChunk poppedChunk = poppedChunks.get(i);
      int[] chunkData = poppedChunk.getData();
      int cutoffLength = (int) Math.max(0, poppedChunk.getPosition() + poppedChunk.getLength() - consolidatedEnd);
      if (cutoffLength > 0)
      {
        MixerChunk cutoffChunk = new MixerChunk(new Chunk(consolidatedEnd, chunkData, poppedChunk.getFrom()
            + poppedChunk.getLength() - cutoffLength, cutoffLength), poppedChunk.getChannel());
        chunks.add(cutoffChunk);
      }
      mixerChunks[i] = new MixerChunk(new Chunk(poppedChunk.getPosition(), poppedChunk.getData(),
          poppedChunk.getFrom(), poppedChunk.getLength() - cutoffLength), poppedChunk.getChannel());
    }
    int length = (int) (consolidatedEnd - consolidatedStart);
    T result = mixStrategy.mix(mixerChunks, consolidatedStart, length);
    position = consolidatedEnd;
    return result;
  }

  public synchronized boolean removeChannel(Channel channel)
  {
    return channels.remove(channel);
  }
}
