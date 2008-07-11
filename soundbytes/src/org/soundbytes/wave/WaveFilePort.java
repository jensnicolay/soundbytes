package org.soundbytes.wave;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.soundbytes.channel.Channel;
import org.soundbytes.channel.impl.RepeatChannel;
import org.soundbytes.mixer.impl.MixerImpl;

public class WaveFilePort
{

  public static void main(String[] args)
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

  // private EnumMap<Speaker, Channel> channels;
  private List<Channel> channels;
  private String waveFile;
  private WaveFormat waveFormat;

  public WaveFilePort(String waveFile, List<Channel> channels, WaveFormat waveFormat)
  {
    super();
    this.channels = channels;
    this.waveFile = waveFile;
    this.waveFormat = waveFormat;
  }

  public void run()
  {
    MixerImpl<byte[]> mixer = new MixerImpl<byte[]>(new WaveMixStrategy(channels.size(),
        waveFormat.getBitsPerSample() / 8), 65536);
    for (Channel channel : channels)
    {
      mixer.addChannel(channel);
    }
    try
    {
      WaveOutputStream wos = new WaveOutputStream(new FileOutputStream(waveFile), waveFormat);
      byte[] data;
      while ((data = mixer.mix()) != null)
      {
        wos.write(data, 0, data.length);
      }
      wos.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
