package org.soundbytes.mixer;

import org.soundbytes.channel.Channel;

public interface Mixer<T>
{

  boolean addChannel(Channel channel);

  T mix();

  boolean removeChannel(Channel channel);
}