package org.soundbytes;

import java.util.Arrays;

public class Chunk
{

  private long position;
  private int[] data;
  private int from;
  private int length;

  public Chunk(int[] data)
  {
    this(0, data);
  }

  public Chunk(long position, int[] data)
  {
    this(0, data, 0, data.length);
  }

  public Chunk(long position, int[] data, int from, int length)
  {
    super();
    this.position = position;
    this.data = data;
    this.from = from;
    this.length = length;
  }

  public int[] getData()
  {
    return data;
  }

  public int getFrom()
  {
    return from;
  }

  public int getLength()
  {
    return length;
  }

  public long getPosition()
  {
    return position;
  }

  public String toString()
  {
    return "[chunk " + position + " " + (data.length < 25 ? Arrays.toString(data) : "#" + data.length) + " " + from
        + " " + length + "]";
  }
}
