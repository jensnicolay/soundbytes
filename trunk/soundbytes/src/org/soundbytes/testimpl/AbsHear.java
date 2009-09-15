package org.soundbytes.testimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.soundbytes.channel.impl.SineWaveChannel;
import org.soundbytes.wave.WaveFilePort;
import org.soundbytes.wave.WaveFormat;
import org.soundbytes.wave.WaveUtils;


public class AbsHear
{

  private static final String[] noteNames = { "C", "C#_Db", "D", "D#_Eb", "E", "F", "F#_Gb", "G", "G#_Ab", "A", "A#_Bb", "B"};
  private static final double[] freqs = new double[144];
  private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static final String[] names = new String[144];
    
  private static void play(double freq)
  {
    SineWaveChannel swc = new SineWaveChannel(freq, (int) (freq / 2.2));
    WaveUtils.playWithAudioSystem(swc);
  }
  
  public static void main(String[] args) throws IOException
  {
    System.out.println("AbsHear v1.0 (J) 2009");
    for (int i = 0; i < freqs.length; i++)
    {
      freqs[i] = 440 * Math.pow(2, (i - 69 + 12) / 12.0);
      names[i] = noteNames[i % 12] + (i / 12);
    }
    System.out.println("(0) hexit");
    System.out.println("(1) shuffle[C4..B4] white");
    System.out.println("(2) shuffle[random(C4..B4)*10] white+black");
    while (true)
    {
      System.out.print("> ");
      String command = reader.readLine(); 
      try
      {
        if ("0".equals(command))
        {
          break;
        }
        else if ("1".equals(command))
        {
          List<String> fs = Arrays.asList("C4", "D4", "E4", "F4", "G4", "A4", "B4");
          Collections.shuffle(fs);
          testNames(fs);
        }
        else if ("2".equals(command))
        {
          int lower = nameToI("C4");
          int upper = nameToI("B4");
          List<Integer> fs = new ArrayList<Integer>();
          for (int i = 0; i < 10; i++)
          {
            fs.add(lower + (int) (Math.random() * (upper - lower)));
          }
          test(fs);
        }
        else
        {
          int i = nameToI(command);
          System.out.println(names[i] + " " + freqs[i] + "Hz");
          play(freqs[i]);
        }
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }

  private static void testNames(List<String> fs) throws IOException
  {
    List<Integer> fsi = new ArrayList<Integer>();
    for (String n : fs)
    {
      fsi.add(nameToI(n));
    }
    test(fsi);
  }
  
  private static void test(List<Integer> fs) throws IOException
  {
    int res[] = new int[fs.size()];
    for (int i = 0; i < fs.size(); i++)
    {
      int note = fs.get(i);
      play(freqs[note]);
      System.out.print("test " + (i + 1) + "/" + fs.size() + "> ");
      String command = reader.readLine();
      int answer = nameToI(command);
      res[i] = answer;
    }
    System.out.println("\nResults");
    for (int i = 0; i < fs.size(); i++)
    {
      System.out.print((i + 1) + "\twas " + names[fs.get(i)]);
      if (fs.get(i) == res[i])
      {
        System.out.println("\tOK");
      }
      else
      {
        System.out.println("\tFAIL (" + names[res[i]] + ")");
      }
    }
    System.out.println();
  }

  private static int nameToI(String name)
  {
    char last = name.charAt(name.length() - 1);
    int octave;
    if (Character.isDigit(last))
    {
      octave = Integer.parseInt(String.valueOf(last));
      name = name.substring(0, name.length() - 1);
    }
    else
    {
      octave = 4;
    }
    int i;
    for (i = 0; i < 12; i++)
    {
      String[] names = noteNames[i].split("_");
      if (names[0].equals(name))
      {
        break;
      }
      else if (names.length > 1 && names[1].equals(name))
      {
        break;
      }
    }
    if (i == 12)
    {
      throw new IllegalArgumentException("unknown note: " + name);
    }
    return octave * 12 + i;
  }
}
