package org.soundbytes.wave;

import java.util.Arrays;

import junit.framework.TestCase;

import org.soundbytes.Chunk;
import org.soundbytes.channel.impl.ChannelUtils;
import org.soundbytes.channel.impl.ChunkChannel;
import org.soundbytes.mixer.Mixer;
import org.soundbytes.mixer.impl.MixUtils;
import org.soundbytes.mixer.impl.MixerImpl;

public class WaveTests extends TestCase
{

  public void testWave1()
  {
    int[] in1 = new int[] { 0, 1, 2, 3, 16, 17, 18, 101, 201, 301, 800, 900, 1000, 30121, -1, -2, -3, -16, -17, -18,
        -101, -201, -301, -800, -900, -1000, -30121};
    int channels = 1;
    int bytesPerSample = 2;
    for (int bufferSize : new int[] { 1, 2, 3, 5, 7, 8, 11, 117, 128})
    {
      Mixer<byte[]> mixer = new MixerImpl<byte[]>(new WaveMixStrategy(channels, bytesPerSample), bufferSize);
      mixer.addChannel(new ChunkChannel(new Chunk(in1)));
      ByteStreamChannels bsc = new ByteStreamChannels(MixUtils.toInputStream(mixer), channels, bytesPerSample);
      int[] out1 = ChannelUtils.toIntArray(bsc.getChannel(0));
      assertTrue(Arrays.equals(in1, out1));
    }
  }

  public void testWave2()
  {
    int[] in1 = new int[] { 0, -11, -22, -33, -164, 165, 36, 27, 188, 9, 1710, 3111, -64123, -3113, -15914};
    int[] in2 = new int[] { 16, 30, 21, 12, 3, 174, 31500, -176, -317, -1798, 1799, 0, 64000, -212, -314};
    int channels = 2;
    int bytesPerSample = 3;
    for (int bufferSize : new int[] { 1, 2, 3, 5, 7, 8, 11, 117, 128})
    {
      Mixer<byte[]> mixer = new MixerImpl<byte[]>(new WaveMixStrategy(channels, bytesPerSample), bufferSize);
      mixer.addChannel(new ChunkChannel(new Chunk(in1)));
      mixer.addChannel(new ChunkChannel(new Chunk(in2)));
      ByteStreamChannels bsc = new ByteStreamChannels(MixUtils.toInputStream(mixer), channels, bytesPerSample);
      int[] out1 = ChannelUtils.toIntArray(bsc.getChannel(0));
      int[] out2 = ChannelUtils.toIntArray(bsc.getChannel(1));
      assertTrue(Arrays.equals(in1, out1));
      assertTrue(Arrays.equals(in2, out2));
    }
  }
}
