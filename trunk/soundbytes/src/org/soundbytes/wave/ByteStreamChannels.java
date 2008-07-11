package org.soundbytes.wave;

import java.io.IOException;
import java.io.InputStream;

import org.soundbytes.Chunk;
import org.soundbytes.channel.Channel;
import org.soundbytes.channel.impl.ChunkChannel;

public class ByteStreamChannels
{

  private class InternalChunkChannel implements Channel
  {

    private int channel;
    private ChunkChannel chunkChannel;

    public InternalChunkChannel(int channel)
    {
      super();
      this.channel = channel;
      chunkChannel = new ChunkChannel();
    }

    public Chunk nextChunk()
    {
      synchronized (ByteStreamChannels.this)
      {
        Chunk channelChunk = chunkChannel.nextChunk();
        if (channelChunk != null)
        {
          return channelChunk;
        }
        if (is == null)
        {
          return null;
        }
        int chunkBufferSize = 65536;
        int aisBufferSize = bytesPerSample * numChannels * chunkBufferSize;
        byte[] aisBuffer = new byte[aisBufferSize];
        int read;
        try
        {
          read = is.read(aisBuffer);
          if (read == -1)
          {
            is.close();
            is = null;
            return null;
          }
        }
        catch (IOException e)
        {
          e.printStackTrace();
          is = null;
          return null;
        }
        int framePos = 0;
        int channelPos = 0;
        int[][] chunkBuffers = new int[numChannels][chunkBufferSize];
        while (framePos < read)
        {
          for (int c = 0; c < numChannels; c++)
          {
            int channelResult = 0;
            int b;
            for (b = 0; b < bytesPerSample - 1; b++)
            {
              channelResult |= ((aisBuffer[framePos + b] & 0xff) << (b << 3));
            }
            channelResult |= (aisBuffer[framePos + b] << (b << 3));
            framePos += bytesPerSample;
            chunkBuffers[c][channelPos] = channelResult;
          }
          channelPos++;
        }
        for (int c = 0; c < numChannels; c++)
        {
          Chunk chunk = new Chunk(position, chunkBuffers[c], 0, channelPos);
          channels[c].chunkChannel.addChunk(chunk);
        }
        position += channelPos;
        channelChunk = chunkChannel.nextChunk();
        return channelChunk;
      }
    }

    public String toString()
    {
      return "[byteStreamChannel " + channel + " " + chunkChannel + "]";
    }
  }

  private int numChannels;
  private int bytesPerSample;
  private InputStream is;
  private long position;
  private InternalChunkChannel[] channels;

  public ByteStreamChannels(InputStream is, int numChannels, int bytesPerSample)
  {
    super();
    this.is = is;
    this.numChannels = numChannels;
    this.bytesPerSample = bytesPerSample;
    channels = new InternalChunkChannel[numChannels];
    for (int c = 0; c < numChannels; c++)
    {
      channels[c] = new InternalChunkChannel(c);
    }
  }

  public Channel getChannel(int channel)
  {
    return channels[channel];
  }
}
