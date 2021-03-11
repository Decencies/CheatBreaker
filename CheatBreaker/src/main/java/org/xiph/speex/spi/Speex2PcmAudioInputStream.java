/******************************************************************************
 *                                                                            *
 * Copyright (c) 1999-2003 Wimba S.A., All Rights Reserved.                   *
 *                                                                            *
 * COPYRIGHT:                                                                 *
 *      This software is the property of Wimba S.A.                           *
 *      This software is redistributed under the Xiph.org variant of          *
 *      the BSD license.                                                      *
 *      Redistribution and use in source and binary forms, with or without    *
 *      modification, are permitted provided that the following conditions    *
 *      are met:                                                              *
 *      - Redistributions of source code must retain the above copyright      *
 *      notice, this list of conditions and the following disclaimer.         *
 *      - Redistributions in binary form must reproduce the above copyright   *
 *      notice, this list of conditions and the following disclaimer in the   *
 *      documentation and/or other materials provided with the distribution.  *
 *      - Neither the name of Wimba, the Xiph.org Foundation nor the names of *
 *      its contributors may be used to endorse or promote products derived   *
 *      from this software without specific prior written permission.         *
 *                                                                            *
 * WARRANTIES:                                                                *
 *      This software is made available by the authors in the hope            *
 *      that it will be useful, but without any warranty.                     *
 *      Wimba S.A. is not liable for any consequence related to the           *
 *      use of the provided software.                                         *
 *                                                                            *
 * Class: Speex2PcmAudioInputStream.java                                      *
 *                                                                            *
 * Author: Marc GIMPEL                                                        *
 *                                                                            *
 * Date: 12th July 2003                                                       *
 *                                                                            *
 ******************************************************************************/

/* $Id: Speex2PcmAudioInputStream.java,v 1.5 2005/05/27 15:57:55 mgimpel Exp $ */

package org.xiph.speex.spi;

import java.io.InputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;
import javax.sound.sampled.AudioFormat;

import org.xiph.speex.Bits;
import org.xiph.speex.Decoder;
import org.xiph.speex.NbDecoder;
import org.xiph.speex.SbDecoder;

/**
 * Converts an Ogg Speex bitstream into a PCM 16bits/sample audio stream.
 *
 * @author Marc Gimpel, Wimba S.A. (mgimpel@horizonwimba.com)
 * @version $Revision: 1.5 $
 */
public class Speex2PcmAudioInputStream
  extends FilteredAudioInputStream
{
  // InputStream variables
  /** Flag to indicate if this Stream has been initialised. */
  private boolean initialised;

  // audio parameters
  /** The sample rate of the audio, in samples per seconds (Hz). */
  private int     sampleRate;
  /** The number of audio channels (1=mono, 2=stereo). */
  private int     channelCount;

  // Speex variables
  /** Array containing the decoded audio samples. */
  private float[] decodedData;
  /** Array containing the decoded audio samples converted into bytes. */
  private byte[]  outputData;
  /** Speex bit packing and unpacking class. */
  private Bits    bits;
  /** Speex Decoder. */
  private Decoder decoder;
  /** The frame size, in samples. */
  private int     frameSize;
  /** The number of Speex frames that will be put in each Ogg packet. */
  private int     framesPerPacket;

  // Ogg variables
  /** A unique serial number that identifies the Ogg stream. */
  private int     streamSerialNumber;
  /** The number of Ogg packets that are in each Ogg page. */
  private int     packetsPerOggPage;
  /** The number of Ogg packets that have been decoded in the current page. */
  private int     packetCount;
  /** Array containing the sizes of Ogg packets in the current page.*/
  private byte[]  packetSizes;
  
  /**
   * Constructor
   * @param in     the underlying input stream.
   * @param format the target format of this stream's audio data.
   * @param length the length in sample frames of the data in this stream.
   */
  public Speex2PcmAudioInputStream(final InputStream in,
                                   final AudioFormat format,
                                   final long length)
  {
    this(in, format, length, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Constructor
   * @param in     the underlying input stream.
   * @param format the target format of this stream's audio data.
   * @param length the length in sample frames of the data in this stream.
   * @param size   the buffer size.
   * @exception IllegalArgumentException if size <= 0.
   */
  public Speex2PcmAudioInputStream(final InputStream in,
                                   final AudioFormat format,
                                   final long length,
                                   final int size)
  {
    super(in, format, length, size);
    bits = new Bits();
    packetSizes = new byte[256];
    initialised = false;
  }

  /**
   * Initialises the Ogg Speex to PCM InputStream.
   * Read the Ogg Speex header and extract the speex decoder parameters to
   * initialise the decoder. Then read the Comment header.
   * Ogg Header description:
   * <pre>
   *  0 -  3: capture_pattern
   *       4: stream_structure_version
   *       5: header_type_flag (2=bos: beginning of sream)
   *  6 - 13: absolute granule position
   * 14 - 17: stream serial number
   * 18 - 21: page sequence no
   * 22 - 25: page checksum
   *      26: page_segments
   * 27 -...: segment_table
   * </pre>
   * Speex Header description
   * <pre>
   *  0 -  7: speex_string
   *  8 - 27: speex_version
   * 28 - 31: speex_version_id
   * 32 - 35: header_size
   * 36 - 39: rate
   * 40 - 43: mode (0=narrowband, 1=wb, 2=uwb)
   * 44 - 47: mode_bitstream_version
   * 48 - 51: nb_channels
   * 52 - 55: bitrate
   * 56 - 59: frame_size
   * 60 - 63: vbr
   * 64 - 67: frames_per_packet
   * 68 - 71: extra_headers
   * 72 - 75: reserved1
   * 76 - 79: reserved2
   * </pre>
   * @param blocking whether the method should block until initialisation is
   *                 successfully completed or not.
   * @exception IOException
   */
  protected void initialise(final boolean blocking)
    throws IOException
  {
    while (!initialised) {
      int readsize = prebuf.length - precount - 1;
      int avail = in.available();
      if (!blocking && avail <= 0) {
        return;
      }
      readsize = (avail > 0 ? Math.min(avail, readsize) : readsize);
      int n = in.read(prebuf, precount, readsize);
      if (n < 0) {
        throw new StreamCorruptedException("Incomplete Ogg Headers");
      }
      if (n == 0) {
        // This should never happen.
        //assert false : "Read 0 bytes from stream - possible infinate loop";
      }
      precount += n;
      if (decoder==null && precount>=108) { // we can process the speex header
        if (!(new String(prebuf, 0, 4).equals("OggS"))) {
          throw new StreamCorruptedException("The given stream does not appear to be Ogg.");
        }
        streamSerialNumber = readInt(prebuf, 14);
        if (!(new String(prebuf, 28, 8).equals("Speex   "))) {
          throw new StreamCorruptedException("The given stream does not appear to be Ogg Speex.");
        }
        sampleRate      = readInt(prebuf, 28+36);
        channelCount    = readInt(prebuf, 28+48);
        framesPerPacket = readInt(prebuf, 28+64);
        int mode = readInt(prebuf, 28+40);
        switch (mode) {
          case 0:
            decoder = new NbDecoder();
            ((NbDecoder)decoder).nbinit();
            break;
          case 1:
            decoder = new SbDecoder();
            ((SbDecoder)decoder).wbinit();
            break;
          case 2:
            decoder = new SbDecoder();
            ((SbDecoder)decoder).uwbinit();
            break;
          default:
        }  
        /* initialize the speex decoder */
        decoder.setPerceptualEnhancement(true);
        /* set decoder format and properties */
        frameSize      = decoder.getFrameSize();
        decodedData    = new float[frameSize*channelCount];
        outputData     = new byte[2*frameSize*channelCount*framesPerPacket];
        bits.init();
      }
      if (decoder!=null && precount>=108+27) { // we can process the comment (skip them)
        packetsPerOggPage = 0xff & prebuf[108+26];
        if (precount>=108+27+packetsPerOggPage) {
          int size = 0;
          for (int i=0; i<packetsPerOggPage; i++) {
            size += 0xff & prebuf[108+27+i];
          }
          if (precount>=108+27+packetsPerOggPage+size) { // we have read the complete comment page
            prepos = 108+27+packetsPerOggPage+size;
            packetsPerOggPage = 0;
            packetCount = 255;
            initialised = true;
          }
        }
      }
    }
  }
  
  /**
   * Fills the buffer with more data, taking into account shuffling and other
   * tricks for dealing with marks.
   * Assumes that it is being called by a synchronized method.
   * This method also assumes that all data has already been read in, hence
   * pos > count.
   * @exception IOException
   */
  protected void fill()
    throws IOException
  {
    makeSpace();
    while (!initialised) {
      initialise(true);
    }
    while (true) {
      int read = in.read(prebuf, precount, prebuf.length - precount);
      if (read < 0) { // inputstream has ended
        while (prepos < precount) { // still data to decode
          if (packetCount >= packetsPerOggPage) { // read new Ogg Page header
            readOggPageHeader();
          }
          if (packetCount < packetsPerOggPage) { // Ogg Page might be empty (0 packets)
            int n = packetSizes[packetCount++];
            if ((precount-prepos) < n) { // we don't have enough data for a complete speex frame
              throw new StreamCorruptedException("Incompleted last Speex packet");
            }
            // do last stuff here
            decode(prebuf, prepos, n);
            prepos += n;
            while ((buf.length - count) < outputData.length) { // grow buffer
              int nsz = buf.length * 2;
              byte[] nbuf = new byte[nsz];
              System.arraycopy(buf, 0, nbuf, 0, count);
              buf = nbuf;
            }
            System.arraycopy(outputData, 0, buf, count, outputData.length);
            count += outputData.length;
          }
        }
        return;
      }
      // if read=0 but the prebuffer contains data, it is decoded and returned.
      // if read=0 but the prebuffer is almost empty, it loops back to read.
      else if (read >= 0) {
        precount += read;
        // do stuff here
        if (packetCount >= packetsPerOggPage) { // read new Ogg Page header
          readOggPageHeader();
        }
        if (packetCount < packetsPerOggPage) { // read the next packet
          if ((precount-prepos) >= packetSizes[packetCount]) { // we have enough data, lets start decoding
            while (((precount-prepos) >= packetSizes[packetCount]) &&
                   (packetCount < packetsPerOggPage)) { // lets decode all we can
              int n = packetSizes[packetCount++];
              decode(prebuf, prepos, n);
              prepos += n;
              while ((buf.length - count) < outputData.length) { // grow buffer
                int nsz = buf.length * 2;
                byte[] nbuf = new byte[nsz];
                System.arraycopy(buf, 0, nbuf, 0, count);
                buf = nbuf;
              }
              System.arraycopy(outputData, 0, buf, count, outputData.length);
              count += outputData.length;
              if (packetCount >= packetsPerOggPage) { // read new Ogg Page header
                readOggPageHeader();
              }
            }
            System.arraycopy(prebuf, prepos, prebuf, 0, precount-prepos);
            precount -= prepos;
            prepos = 0;
            return; // we have decoded some data (all that we could), so we can leave now, otherwise we return to a potentially blocking read of the underlying inputstream.
          }
        }
      }
    }
  }

  /**
   * This is where the actual decoding takes place.
   * @param data the array of data to decode.
   * @param offset the offset from which to start reading the data.
   * @param len the length of data to read from the array.
   * @throws StreamCorruptedException If the input stream not valid Ogg Speex
   * data.
   */
  protected void decode(final byte[] data,
                        final int offset,
                        final int len)
    throws StreamCorruptedException
  {
    int i;
    short val;
    int outputSize = 0;
    /* read packet bytes into bitstream */
    bits.read_from(data, offset, len);
    for (int frame=0; frame<framesPerPacket; frame++) {
      /* decode the bitstream */
      decoder.decode(bits, decodedData);
      if (channelCount == 2)
        decoder.decodeStereo(decodedData, frameSize);
      /* PCM saturation */
      for (i=0; i<frameSize*channelCount; i++) {
        if (decodedData[i] > 32767.0f)
          decodedData[i] = 32767.0f;
        else if (decodedData[i] < -32768.0f)
          decodedData[i] = -32768.0f;
      }
      /* convert to short and save to buffer */
      for (i=0; i<frameSize*channelCount; i++) {
        val = (decodedData[i]>0) ? (short)(decodedData[i]+.5) :
                                   (short)(decodedData[i]-.5);
        outputData[outputSize++] = (byte) (val & 0xff);
        outputData[outputSize++] = (byte) ((val >> 8) &  0xff );
      }
    }
  }

  /**
   * See the general contract of the <code>skip</code> method of
   * <code>InputStream</code>.
   *
   * @param      n   the number of bytes to be skipped.
   * @return     the actual number of bytes skipped.
   * @exception  IOException  if an I/O error occurs.
   */
  public synchronized long skip(long n)
    throws IOException
  {
    while (!initialised) {
      initialise(true);
    }
    checkIfStillOpen();
    // Sanity check
    if (n <= 0) {
      return 0;
    }
    // Skip buffered data if there is any
    if (pos < count) {
      return super.skip(n);
    }
    // Nothing in the buffers to skip
    else {
      int decodedPacketSize = 2*framesPerPacket*frameSize*channelCount;
      if (markpos < 0 && n >= decodedPacketSize) {
        // We aren't buffering and skipping more than a complete Speex packet:
        // Lets try to skip complete Speex packets without decoding
        if (packetCount >= packetsPerOggPage) { // read new Ogg Page header
          readOggPageHeader();
        }
        if (packetCount < packetsPerOggPage) { // read the next packet
          int skipped = 0;
          if ((precount-prepos) < packetSizes[packetCount]) { // we don't have enough data
            int avail = in.available();
            if (avail > 0) {
              int size = Math.min(prebuf.length - precount, avail);
              int read = in.read(prebuf, precount, size);
              if (read < 0) { // inputstream has ended
                throw new IOException("End of stream but there are still supposed to be packets to decode");
              }
              precount += read;
            }
          }
          while (((precount-prepos) >= packetSizes[packetCount]) &&
                 (packetCount < packetsPerOggPage) &&
                 (n >= decodedPacketSize)) { // lets skip all we can
            prepos += packetSizes[packetCount++];
            skipped += decodedPacketSize;
            n -= decodedPacketSize;
            if (packetCount >= packetsPerOggPage) { // read new Ogg Page header
              readOggPageHeader();
            }
          }
          System.arraycopy(prebuf, prepos, prebuf, 0, precount-prepos);
          precount -= prepos;
          prepos = 0;
          return skipped; // we have skipped some data (all that we could), so we can leave now, otherwise we return to a potentially blocking read of the underlying inputstream.
        }
      }
      // We are buffering, or couldn't skip a complete Speex packet:
      // Read (decode) into buffers and skip (this is potentially blocking)
      return super.skip(n);
    }
  }

  /**
   * Returns the number of bytes that can be read from this inputstream without
   * blocking. 
   * <p>
   * The <code>available</code> method of <code>FilteredAudioInputStream</code>
   * returns the sum of the the number of bytes remaining to be read in the
   * buffer (<code>count - pos</code>).
   * The result of calling the <code>available</code> method of the underlying
   * inputstream is not used, as this data will have to be filtered, and thus
   * may not be the same size after processing (although subclasses that do the
   * filtering should override this method and use the amount of data available
   * in the underlying inputstream).
   *
   * @return the number of bytes that can be read from this inputstream without
   * blocking.
   * @exception IOException if an I/O error occurs.
   * @see #in
   */
  public synchronized int available()
    throws IOException
  {
    if (!initialised) {
      initialise(false);
      if (!initialised) {
        return 0;
      }
    }
    int avail = super.available();
    if (packetCount >= packetsPerOggPage) { // read new Ogg Page header
      readOggPageHeader();
    }
    // See how much we could decode from the underlying stream.
    if (packetCount < packetsPerOggPage) {
      int undecoded = precount - prepos + in.available();
      int size = packetSizes[packetCount];
      int tempCount = 0;
      while (size < undecoded &&
             packetCount + tempCount < packetsPerOggPage) {
        undecoded -= size;
        avail += 2 * frameSize * framesPerPacket;
        tempCount++;
        size = packetSizes[packetCount + tempCount];
      }
    }
    return avail;
  }

  //---------------------------------------------------------------------------
  // Ogg Specific code
  //---------------------------------------------------------------------------
  
  /**
   * Read the Ogg Page header and extract the speex packet sizes.
   * Note: the checksum is ignores.
   * Note: the method should no block on a read because it will not read more
   * then is available
   */
  private void readOggPageHeader()
    throws IOException
  {
    int packets = 0;
    if (precount-prepos<27) {
      int avail = in.available();
      if (avail > 0) {
        int size = Math.min(prebuf.length - precount, avail);
        int read = in.read(prebuf, precount, size);
        if (read < 0) { // inputstream has ended
          throw new IOException("End of stream but available was positive");
        }
        precount += read;
      }
    }
    if (precount-prepos>=27) { // can read beginning of Page header
      if (!(new String(prebuf, prepos, 4).equals("OggS"))) {
        throw new StreamCorruptedException("Lost Ogg Sync");
      }
      if (streamSerialNumber != readInt(prebuf, prepos+14)) {
        throw new StreamCorruptedException("Ogg Stream Serial Number mismatch");
      }
      packets = 0xff & prebuf[prepos+26];
    }
    if (precount-prepos<27+packets) {
      int avail = in.available();
      if (avail > 0) {
        int size = Math.min(prebuf.length - precount, avail);
        int read = in.read(prebuf, precount, size);
        if (read < 0) { // inputstream has ended
          throw new IOException("End of stream but available was positive");
        }
        precount += read;
      }
    }
    if (precount-prepos>=27+packets) { // can read entire Page header
      System.arraycopy(prebuf, prepos+27, packetSizes, 0, packets);
      packetCount = 0;
      prepos += 27+packets;
      packetsPerOggPage = packets;
    }
  }
  
  /**
   * Converts Little Endian (Windows) bytes to an int (Java uses Big Endian).
   * @param data the data to read.
   * @param offset the offset from which to start reading.
   * @return the integer value of the reassembled bytes.
   */
  private static int readInt(final byte[] data, final int offset)
  {
    return (data[offset] & 0xff) |
           ((data[offset+1] & 0xff) <<  8) |
           ((data[offset+2] & 0xff) << 16) |
           (data[offset+3] << 24); // no & 0xff at the end to keep the sign
  }
}
