package org.soundbytes.wave;

import org.soundbytes.mixer.MixStrategy;
import org.soundbytes.mixer.MixerChunk;

public class WaveMixStrategy implements MixStrategy<byte[]>
{

  private int channels;
  private int bytesPerSample;

  public WaveMixStrategy(int channels, int bytesPerSample)
  {
    super();
    this.bytesPerSample = bytesPerSample;
    this.channels = channels;
  }

  public byte[] mix(MixerChunk[] chunks, long mixPosition, int mixLength)
  {
    int frameLength = channels * bytesPerSample;
    byte[] mixData = new byte[mixLength * frameLength];
    for (MixerChunk chunk : chunks)
    {
      int[] chunkData = chunk.getData();
      int channel = chunk.getChannel();
      int chunkPosition = (int) (chunk.getPosition() - mixPosition) * frameLength;
      int channelOffset = channel * bytesPerSample;
      int fromChunk = chunk.getFrom();
      int chunkValuePosition = chunkPosition + channelOffset;
      for (int i = 0; i < chunk.getLength(); i++)
      {
        int chunkValue = chunkData[fromChunk + i];
        int b;
        for (b = 0; b < bytesPerSample - 1; b++)
        {
          mixData[chunkValuePosition + b] = (byte) ((chunkValue >> (b << 3)) & 0xff);
        }
        mixData[chunkValuePosition + b] = (byte) (chunkValue >> (b << 3));
        chunkValuePosition += frameLength;
      }
    }
    return mixData;
  }
}
