package org.soundbytes.wave;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WaveFormat
{

  public static enum Format
  {
    PCM(0x1), EXTENSIBLE(0xffee);

    private static Map<Integer, Format> intValues = new HashMap<Integer, Format>();
    static
    {
      for (Format format : Format.values())
      {
        intValues.put(format.getValue(), format);
      }
    }

    public static Format valueOf(int value)
    {
      return intValues.get(value);
    }

    private int value;

    private Format(int value)
    {
      this.value = value;
    }

    public int getValue()
    {
      return value;
    }
  }

  public static enum Speaker
  {
    FRONT_LEFT("FL", 0x1), FRONT_RIGHT("FR", 0x2), FRONT_CENTER("FC", 0x4), LOW_FREQUENCY("LF", 0x8), BACK_LEFT("BL",
        0x10), BACK_RIGHT("BR", 0x20), BACK_CENTER("BC", 0x100);

    // private static Map<Long, Speaker> maskValues = new HashMap<Long, Speaker>();
    // static
    // {
    // for (Speaker speaker : Speaker.values())
    // {
    // maskValues.put(speaker.getMask(), speaker);
    // }
    // }
    public static Set<Speaker> getSpeakers(long value)
    {
      Set<Speaker> result = EnumSet.noneOf(Speaker.class);
      for (Speaker speaker : Speaker.values())
      {
        if ((speaker.getMask() & value) == speaker.getMask())
        {
          result.add(speaker);
        }
      }
      return result;
    }

    private String name;
    private long mask;

    private Speaker(String name, long mask)
    {
      this.name = name;
      this.mask = mask;
    }

    public long getMask()
    {
      return mask;
    }

    public String toString()
    {
      return name;
    }
  }

  public static enum SubFormat
  {
    KSDATAFORMAT_SUBTYPE_PCM(new BigInteger("0000000100000010800000aa00389b71", 16));

    private static Map<BigInteger, SubFormat> intValues = new HashMap<BigInteger, SubFormat>();
    static
    {
      for (SubFormat format : SubFormat.values())
      {
        intValues.put(format.getValue(), format);
      }
    }

    public static SubFormat valueOf(BigInteger value)
    {
      return intValues.get(value);
    }

    private BigInteger value;

    private SubFormat(BigInteger value)
    {
      this.value = value;
    }

    public BigInteger getValue()
    {
      return value;
    }
  }

  private static final Charset ASCII = Charset.forName("ASCII");

  public static void main(String[] args) throws Exception
  {
    System.out.println(WaveFormat.read(new FileInputStream("wav/chan-id.wav")));
  }

  // public SubFormat getSubFormat()
  // {
  // return subFormat;
  // }
  //
  // public void setSubFormat(SubFormat subFormat)
  // {
  // this.subFormat = subFormat;
  // }
  //
  public static WaveFormat read(InputStream in)
  {
    try
    {
      String chunkId = readString(in, 4);
      if (!"RIFF".equals(chunkId))
      {
        throw new IllegalArgumentException("chunkId '" + chunkId + "'");
      }
      long size = readInt4(in);
      String format_ = readString(in, 4);
      if (!"WAVE".equals(format_))
      {
        throw new IllegalArgumentException("format '" + chunkId + "'");
      }
      String subChunkId = readString(in, 4);
      if (!"fmt ".equals(subChunkId))
      {
        throw new IllegalArgumentException("subChunkId '" + chunkId + "'");
      }
      long subChunkSize = readInt4(in);
      // System.out.println("subsize " + subChunkSize);
      // if (subChunkSize == 20)
      // {
      // // PCM
      // }
      // else if (subChunkSize == 40)
      // {
      // // EXTENDED
      // }
      // else
      // {
      // throw new IllegalArgumentException("subChunkSize " + subChunkSize);
      // }
      Format format = WaveFormat.Format.valueOf(readInt2(in));
      int channels = readInt2(in);
      int sampleRate = (int) readInt4(in);
      long byteRate = readInt4(in);
      int blockAlign = readInt2(in);
      int bitsPerSample = readInt2(in);
      // 16 bytes read, skip rest
      in.skip(subChunkSize - 16);
      subChunkId = readString(in, 4);
      subChunkSize = readInt4(in);
      while (!"data".equals(subChunkId))
      {
        long skipped = in.skip(subChunkSize);
        if (skipped != subChunkSize)
        {
          throw new IllegalArgumentException("wrong size for chunk '" + subChunkId);
        }
        subChunkId = readString(in, 4);
        subChunkSize = readInt4(in);
      }
      // waveFormat.dataSize = subChunkSize;
      WaveFormat waveFormat = new WaveFormat(format, channels, bitsPerSample, sampleRate);
      return waveFormat;
    }
    catch (IOException ioe)
    {
      throw new IllegalArgumentException(ioe);
    }
    // 'in' now points to the start of the data chunk, assume it is the last chunk
  }

  private static int readInt2(InputStream in) throws IOException
  {
    byte[] bytes = new byte[2];
    in.read(bytes);
    return (0xff & bytes[0]) + ((0xff & bytes[1]) << 8);
  }

  private static long readInt4(InputStream in) throws IOException
  {
    byte[] bytes = new byte[4];
    in.read(bytes);
    return (0xffL & bytes[0]) + ((0xffL & bytes[1]) << 8) + ((0xffL & bytes[2]) << 16) + ((0xffL & bytes[3]) << 24);
  }

  private static String readString(InputStream in, int length) throws IOException
  {
    byte[] bytes = new byte[length];
    in.read(bytes);
    return new String(bytes, 0, bytes.length, ASCII);
  }

  private Format format;
  private int channels;
  private int sampleRate;
  private int bitsPerSample;

  // waveFormatPCMEx.Format.cbSize = 22;
  // private int validBitsPerSample;
  // private Set<Speaker> speakers;
  // private SubFormat subFormat;
  // private long dataSize;
  // public int getValidBitsPerSample()
  // {
  // return validBitsPerSample;
  // }
  //
  // public void setValidBitsPerSample(int validBitsPerSample)
  // {
  // this.validBitsPerSample = validBitsPerSample;
  // }
  // public Set<Speaker> getSpeakers()
  // {
  // return speakers;
  // }
  //
  // public void setSpeakers(Set<Speaker> speakers)
  // {
  // this.speakers = speakers;
  // }
  public WaveFormat(Format format, int channels, int bitsPerSample, int sampleRate)
  {
    super();
    // dataSize = -1;
    this.format = format;
    this.channels = channels;
    this.bitsPerSample = bitsPerSample;
    this.sampleRate = sampleRate;
  }

  public int getBitsPerSample()
  {
    return bitsPerSample;
  }

  public int getChannels()
  {
    return channels;
  }

  public Format getFormat()
  {
    return format;
  }

  public int getSampleRate()
  {
    return sampleRate;
  }

  public String toString()
  {
    return getFormat() + " " + getChannels() + "ch " + getBitsPerSample() + "bits " + getSampleRate() + "Hz";
    // + (getSpeakers() == null ? "" : " " + getSpeakers());
  }

  public void write(OutputStream out, long dataSize)
  {
    if (dataSize < 0)
    {
      throw new IllegalStateException("dataSize must be set");
    }
    int formatSize = 20;
    long size = 4 + (8 + formatSize) + (8 + dataSize);
    try
    {
      writeString(out, "RIFF");
      writeInt4(out, size);
      writeString(out, "WAVE");
      writeString(out, "fmt ");
      writeInt4(out, formatSize);
      writeInt2(out, getFormat().getValue());
      writeInt2(out, getChannels());
      writeInt4(out, getSampleRate());
      writeInt4(out, getSampleRate() * getChannels() * getBitsPerSample() / 8);
      writeInt2(out, getChannels() * getBitsPerSample() / 8);
      writeInt2(out, getBitsPerSample());
      // 16 written, pad 4 (16 + 4 = formatSize)
      out.write(new byte[] { 0, 0, 0, 0});
      writeString(out, "data");
      writeInt4(out, dataSize);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void writeInt2(OutputStream out, int i) throws IOException
  {
    out.write(i & 0xff);
    out.write(i >> 8);
  }

  private void writeInt4(OutputStream out, long i) throws IOException
  {
    out.write((int) (i & 0xff));
    out.write((int) ((i >> 8) & 0xff));
    out.write((int) ((i >> 16) & 0xff));
    out.write((int) (i >> 24));
  }

  private void writeString(OutputStream out, String s) throws IOException
  {
    byte[] bytes = s.getBytes();
    out.write(bytes);
  }
}
