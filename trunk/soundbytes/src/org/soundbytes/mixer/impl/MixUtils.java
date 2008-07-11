package org.soundbytes.mixer.impl;

import java.io.IOException;
import java.io.InputStream;

import org.soundbytes.Chunk;
import org.soundbytes.mixer.Mixer;
import org.soundbytes.mixer.MixerChannel;

public class MixUtils
{

  public static MixerChannel getDefaultMixerChannel()
  {
    return new MixerChannel(new MixerImpl<Chunk>(new ChannelMixStrategy(), 65536));
  }

  public static InputStream toInputStream(final Mixer<byte[]> mixer)
  {
    return new InputStream()
    {

      private byte[] buffer = new byte[0];
      private int pos;

      public int read() throws IOException
      {
        if (buffer == null)
        {
          return -1;
        }
        if (pos >= buffer.length)
        {
          buffer = mixer.mix();
          if (buffer == null || buffer.length == 0)
          {
            return -1;
          }
          pos = 0;
        }
        return (buffer[pos++] & 0xff);
      }
    };
  }

  private MixUtils()
  {
    super();
  }
}
