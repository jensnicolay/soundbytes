package org.soundbytes.mixer;

public interface MixStrategy<T>
{

  T mix(MixerChunk[] chunks, long mixPosition, int mixLength);
}
