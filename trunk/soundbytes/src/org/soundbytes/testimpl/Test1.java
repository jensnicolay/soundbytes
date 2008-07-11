package org.soundbytes.testimpl;

import java.util.ArrayList;
import java.util.List;

import org.soundbytes.channel.Channel;
import org.soundbytes.channel.impl.ChannelDuplicator;
import org.soundbytes.channel.impl.RepeatChannel;
import org.soundbytes.mixer.MixerChannel;
import org.soundbytes.mixer.impl.MixUtils;
import org.soundbytes.wave.WaveFileChannels;
import org.soundbytes.wave.WaveFilePort;
import org.soundbytes.wave.WaveFormat;

public class Test1
{

  public static void main(String[] args)
  {
  }

  public static void test1()
  {
    System.out.println("start");
    WaveFileChannels wfcLeft = new WaveFileChannels("wav/left48.wav");
    WaveFileChannels wfcRight = new WaveFileChannels("wav/right48.wav");
    WaveFileChannels wfcCenter = new WaveFileChannels("wav/center48.wav");
    List<Channel> channels = new ArrayList<Channel>();
    channels.add(new RepeatChannel(wfcLeft.getChannel(0), 9, 100000));
    channels.add(new RepeatChannel(wfcRight.getChannel(0), 9, 95000));
    channels.add(new RepeatChannel(wfcCenter.getChannel(0), 9, 90000));
    WaveFilePort asp = new WaveFilePort("wav/result.wav", channels, new WaveFormat(WaveFormat.Format.PCM, channels
        .size(), 16, 48000));
    asp.run();
    System.out.println("done");
  }

  public static void test2()
  {
    WaveFileChannels wfc = new WaveFileChannels("wav/1.wav");
    MixerChannel monoMixer = MixUtils.getDefaultMixerChannel();
    monoMixer.addChannel(wfc.getChannel(0));
    monoMixer.addChannel(wfc.getChannel(1));
    ChannelDuplicator duper = new ChannelDuplicator(monoMixer, 2);
    // WaveFilePort wfp = new WaveFilePort();
    // wfp.
  }
}
