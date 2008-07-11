package org.soundbytes.mixer.impl;

import java.util.Arrays;

import junit.framework.TestCase;

import org.soundbytes.Chunk;
import org.soundbytes.channel.impl.ChannelUtils;
import org.soundbytes.channel.impl.ChunkChannel;
import org.soundbytes.mixer.MixStrategy;
import org.soundbytes.mixer.Mixer;
import org.soundbytes.mixer.MixerChannel;

public class MixerTests extends TestCase
{

  private static MixerChannel createMixerChannel(int bufferSize)
  {
    MixStrategy<Chunk> channelMixStrategy = new ChannelMixStrategy();
    Mixer<Chunk> channelMixer = new MixerImpl<Chunk>(channelMixStrategy, bufferSize);
    return new MixerChannel(channelMixer);
  }

  public void testChannelMixer1()
  {
    MixerChannel mixer = createMixerChannel(4);
    Chunk c1 = new Chunk(0, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 0, 10);
    mixer.addChannel(new ChunkChannel(c1));
    int[] result = ChannelUtils.toIntArray(mixer);
    assertTrue(Arrays.equals(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, result));
  }

  public void testChannelMixer2()
  {
    MixerChannel mixer = createMixerChannel(4);
    Chunk c1 = new Chunk(0, new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 0, 10);
    mixer.addChannel(new ChunkChannel(c1));
    Chunk c2 = new Chunk(2, new int[] { 1, 1, 1, 1, 1, 1, 1}, 1, 5);
    mixer.addChannel(new ChunkChannel(c2));
    int[] result = ChannelUtils.toIntArray(mixer);
    assertTrue(Arrays.equals(new int[] { 0, 1, 3, 4, 5, 6, 7, 7, 8, 9}, result));
  }

  public void testChannelMixer3()
  {
    MixerChannel mixer = createMixerChannel(4);
    Chunk c1 = new Chunk(0, new int[] { 1, 1, 1, 1, 1, 1, 1}, 1, 4);
    mixer.addChannel(new ChunkChannel(c1));
    Chunk c2 = new Chunk(1, new int[] { 1, 1, 1, 1, 1, 1, 1}, 0, 4);
    mixer.addChannel(new ChunkChannel(c2));
    Chunk c3 = new Chunk(2, new int[] { 1, 1, 1, 1, 1, 1, 1}, 2, 4);
    mixer.addChannel(new ChunkChannel(c3));
    int[] result = ChannelUtils.toIntArray(mixer);
    assertTrue(Arrays.equals(new int[] { 1, 2, 3, 3, 2, 1}, result));
  }

  public void testChannelMixer4()
  {
    MixerChannel mixer = createMixerChannel(4);
    Chunk c1 = new Chunk(0, new int[] { 1, 1, 1, 1}, 0, 4);
    mixer.addChannel(new ChunkChannel(c1));
    Chunk c2 = new Chunk(4, new int[] { 2, 2, 2, 2}, 0, 4);
    mixer.addChannel(new ChunkChannel(c2));
    Chunk c3 = new Chunk(0, new int[] { 3, 3}, 0, 2);
    mixer.addChannel(new ChunkChannel(c3));
    int[] result = ChannelUtils.toIntArray(mixer);
    assertTrue(Arrays.equals(new int[] { 4, 4, 1, 1, 2, 2, 2, 2}, result));
  }
}
