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
 * Class: Pcm2SpeexAudioInputStream.java                                      *
 *                                                                            *
 * Author: Marc GIMPEL                                                        *
 *                                                                            *
 * Date: 12th July 2003                                                       *
 *                                                                            *
 ******************************************************************************/

/* $Id: Pcm2SpeexAudioInputStream.java,v 1.2 2004/10/21 16:21:58 mgimpel Exp $ */

package org.xiph.speex.spi;

import java.util.Random;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import javax.sound.sampled.AudioFormat;

import org.xiph.speex.AudioFileWriter;
import org.xiph.speex.OggCrc;
import org.xiph.speex.Encoder;
import org.xiph.speex.SpeexEncoder;

/**
 * Converts a PCM 16bits/sample mono audio stream to Ogg Speex
 *
 * @author Marc Gimpel, Wimba S.A. (mgimpel@horizonwimba.com)
 * @version $Revision: 1.2 $
 */
public class Pcm2SpeexAudioInputStream
  extends FilteredAudioInputStream
{
  /** The default size of the buffer (UWB stereo requires at least 2560b). */
  public static final int DEFAULT_BUFFER_SIZE = 2560;

//  public static final boolean DEFAULT_VBR              = true;
  /** The default sample rate if none is given in the constructor. */
  public static final int DEFAULT_SAMPLERATE           = 8000;
  /** The default number of channels if none is given in the constructor. */
  public static final int DEFAULT_CHANNELS             = 1;
  /** The default quality setting for the Speex encoder. */
  public static final int DEFAULT_QUALITY              = 3;
  /** The default number of Speex frames that will be put in each Ogg packet. */
  public static final int DEFAULT_FRAMES_PER_PACKET    = 1;
  /** The default number of Ogg packets that will be put in each Ogg page. */
  public static final int DEFAULT_PACKETS_PER_OGG_PAGE = 20; // .4s of audio
  /** Indicates the value is unknown or undetermined. */
  public static final int UNKNOWN = -1;

  // Speex variables
  /** The Speex Encoder class. */
  private SpeexEncoder encoder;
  /** The encoder mode (0=NB, 1=WB, 2=UWB). */
  private int     mode;
  /** The size in bytes of PCM data that will be encoded into 1 Speex frame. */
  private int     frameSize;
  /** The number of Speex frames that will be put in each Ogg packet. */
  private int     framesPerPacket;
  /** The number of audio channels (1=mono, 2=stereo). */
  private int     channels;

  // Ogg variables
  /** The comment String that will appear in the Ogg comment packet. */
  private String  comment = null;
  /** A counter for the number of PCM samples that have been encoded. */
  private int     granulepos;
  /** A unique serial number that identifies the Ogg stream. */
  private int     streamSerialNumber;
  /** The number of Ogg packets that will be put in each Ogg page. */
  private int     packetsPerOggPage;
  /** The number of Ogg packets that have been encoded in the current page. */
  private int     packetCount;
  /** The number of Ogg pages that have been written to the stream. */
  private int     pageCount;
  /** Pointer in the buffer to the point where Ogg data is added. */
  private int     oggCount;
  /** Flag to indicate if this is the first time a encode method is called. */
  private boolean first;
  
  /**
   * Constructor
   * @param in      the underlying input stream.
   * @param format  the target format of this stream's audio data.
   * @param length  the length in sample frames of the data in this stream.
   */
  public Pcm2SpeexAudioInputStream(final InputStream in,
                                   final AudioFormat format,
                                   final long length)
  {
    this(UNKNOWN, UNKNOWN, in, format, length, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Constructor
   * @param mode    the mode of the encoder (0=NB, 1=WB, 2=UWB).
   * @param quality the quality setting of the encoder (between 0 and 10).
   * @param in      the underlying input stream.
   * @param format  the target format of this stream's audio data.
   * @param length  the length in sample frames of the data in this stream.
   */
  public Pcm2SpeexAudioInputStream(final int mode,
                                   final int quality,
                                   final InputStream in,
                                   final AudioFormat format,
                                   final long length)
  {
    this(mode, quality, in, format, length, DEFAULT_BUFFER_SIZE);
  }

  /**
   * Constructor
   * @param in      the underlying input stream.
   * @param format  the target format of this stream's audio data.
   * @param length  the length in sample frames of the data in this stream.
   * @param size    the buffer size.
   * @exception IllegalArgumentException if size <= 0.
   */
  public Pcm2SpeexAudioInputStream(final InputStream in,
                                   final AudioFormat format,
                                   final long length,
                                   final int size)
  {
    this(UNKNOWN, UNKNOWN, in, format, length, size);
  }
  
  /**
   * Constructor
   * @param mode    the mode of the encoder (0=NB, 1=WB, 2=UWB).
   * @param quality the quality setting of the encoder (between 0 and 10).
   * @param in      the underlying input stream.
   * @param format  the target format of this stream's audio data.
   * @param length  the length in sample frames of the data in this stream.
   * @param size    the buffer size.
   * @exception IllegalArgumentException if size <= 0.
   */
  public Pcm2SpeexAudioInputStream(int mode,
                                   int quality,
                                   final InputStream in,
                                   final AudioFormat format,
                                   final long length,
                                   final int size)
  {
    super(in, format, length, size);
    // Ogg initialisation
    granulepos = 0;
    if (streamSerialNumber == 0)
      streamSerialNumber = new Random().nextInt();
    packetsPerOggPage = DEFAULT_PACKETS_PER_OGG_PAGE;
    packetCount = 0;
    pageCount = 0;
    // Speex initialisation
    framesPerPacket = DEFAULT_FRAMES_PER_PACKET;
    int samplerate = (int) format.getSampleRate();
    if (samplerate < 0)
      samplerate = DEFAULT_SAMPLERATE;
    channels = format.getChannels();
    if (channels < 0)
      channels = DEFAULT_CHANNELS;
    if (mode < 0)
      mode = (samplerate < 12000) ? 0 : ((samplerate < 24000) ? 1 : 2);
    this.mode = mode;
    AudioFormat.Encoding encoding = format.getEncoding();
    if (quality < 0) {
      if (encoding instanceof SpeexEncoding) {
        quality = ((SpeexEncoding) encoding).getQuality();
      }
      else {
        quality = DEFAULT_QUALITY;
      }
    }
    encoder = new SpeexEncoder();
    encoder.init(mode, quality, samplerate, channels);
    if (encoding instanceof SpeexEncoding &&
        ((SpeexEncoding) encoding).isVBR()) {
      setVbr(true);
    }
    else {
      setVbr(false);
    }
    frameSize = 2 * channels * encoder.getFrameSize();
    // Misc initialsation
    comment = "Encoded with " + SpeexEncoder.VERSION;
    first = true;
  }

  /**
   * Sets the Stream Serial Number.
   * Must not be changed mid stream.
   * @param serialNumber
   */
  public void setSerialNumber(final int serialNumber)
  {
    if (first) {
      this.streamSerialNumber = serialNumber;
    }
  }

  /**
   * Sets the number of Audio Frames that are to be put in every Speex Packet.
   * An Audio Frame contains 160 samples for narrowband, 320 samples for
   * wideband and 640 samples for ultra-wideband.
   * @param framesPerPacket
   * @see #DEFAULT_FRAMES_PER_PACKET
   */
  public void setFramesPerPacket(int framesPerPacket)
  {
    if (framesPerPacket <= 0) {
      framesPerPacket = DEFAULT_FRAMES_PER_PACKET;
    }
    this.framesPerPacket = framesPerPacket;
  }

  /**
   * Sets the number of Speex Packets that are to be put in every Ogg Page.
   * This value must be less than 256 as the value is encoded in 1 byte in the
   * Ogg Header (just before the array of packet sizes)
   * @param packetsPerOggPage
   * @see #DEFAULT_PACKETS_PER_OGG_PAGE
   */
  public void setPacketsPerOggPage(int packetsPerOggPage)
  {
    if (packetsPerOggPage <= 0) {
      packetsPerOggPage = DEFAULT_PACKETS_PER_OGG_PAGE;
    }
    if (packetsPerOggPage > 255) {
      packetsPerOggPage = 255;
    }
    this.packetsPerOggPage = packetsPerOggPage;
  }
  
  /**
   * Sets the comment for the Ogg Comment Header.
   * @param comment
   * @param appendVersion
   */
  public void setComment(final String comment,
                         final boolean appendVersion)
  {
    this.comment = comment;
    if (appendVersion) {
      this.comment += SpeexEncoder.VERSION;
    }
  }

  /**
   * Sets the Speex encoder Quality.
   * @param quality
   */
  public void setQuality(final int quality)
  {
    encoder.getEncoder().setQuality(quality);
    if (encoder.getEncoder().getVbr()) {
      encoder.getEncoder().setVbrQuality((float)quality);
    }
  }
  
  /**
   * Sets whether of not the encoder is to use VBR.
   * @param vbr
   */
  public void setVbr(final boolean vbr)
  {
    encoder.getEncoder().setVbr(vbr);
  }

  /**
   * Returns the Encoder.
   * @return the Encoder.
   */
  public Encoder getEncoder()
  {
    return encoder.getEncoder();
  }

  /**
   * Fills the buffer with more data, taking into account
   * shuffling and other tricks for dealing with marks.
   * Assumes that it is being called by a synchronized method.
   * This method also assumes that all data has already been read in,
   * hence pos > count.
   * @exception IOException
   */
  protected void fill()
    throws IOException
  {
    makeSpace();
    if (first) {
      writeHeaderFrames();
      first = false;
    }
    while (true) {
      if ((prebuf.length - prepos) < framesPerPacket*frameSize*packetsPerOggPage) { // grow prebuf
        int nsz = prepos + framesPerPacket*frameSize*packetsPerOggPage;
        byte[] nbuf = new byte[nsz];
        System.arraycopy(prebuf, 0, nbuf, 0, precount);
        prebuf = nbuf;
      }
      int read = in.read(prebuf, precount, prebuf.length - precount);
      if (read < 0) { // inputstream has ended
        if ((precount-prepos) % 2 != 0) { // we don't have a complete last PCM sample
          throw new StreamCorruptedException("Incompleted last PCM sample when stream ended");
        }
        while (prepos < precount) { // still data to encode
          if ((precount - prepos) < framesPerPacket*frameSize) {
            // fill end of frame with zeros
            for (;precount < (prepos+framesPerPacket*frameSize); precount++) {
              prebuf[precount] = 0;
            }
          }
          if (packetCount == 0) {
            writeOggPageHeader(packetsPerOggPage, 0);
          }
          for (int i=0; i<framesPerPacket; i++) {
            encoder.processData(prebuf, prepos, frameSize);
            prepos += frameSize;
          }
          int size = encoder.getProcessedDataByteSize();
          while ((buf.length - oggCount) < size) { // grow buffer
            int nsz = buf.length * 2;
            byte[] nbuf = new byte[nsz];
            System.arraycopy(buf, 0, nbuf, 0, oggCount);
            buf = nbuf;
          }
          buf[count + 27 + packetCount] = (byte)(0xff & size);
          encoder.getProcessedData(buf, oggCount);
          oggCount += size;
          packetCount++;
          if (packetCount >= packetsPerOggPage) {
            writeOggPageChecksum();
            return;
          }
        }
        if (packetCount > 0) {
          // we have less than the normal number of packets in this page.
          buf[count+5] = (byte)(0xff & 4); // set page header type to end of stream
          buf[count+26] = (byte)(0xff & packetCount);
          System.arraycopy(buf, count+27+packetsPerOggPage,
                           buf, count+27+packetCount,
                           oggCount-(count+27+packetsPerOggPage));
          oggCount -= packetsPerOggPage-packetCount;
          writeOggPageChecksum();
        }
        return;
      }
      else if (read > 0) {
        precount += read;
        if ((precount - prepos) >= framesPerPacket*frameSize*packetsPerOggPage) { // enough data to encode frame
          while ((precount - prepos) >= framesPerPacket*frameSize*packetsPerOggPage) { // lets encode all we can
            if (packetCount == 0) {
              writeOggPageHeader(packetsPerOggPage, 0);
            }
            while (packetCount < packetsPerOggPage) {
              for (int i=0; i<framesPerPacket; i++) {
                encoder.processData(prebuf, prepos, frameSize);
                prepos += frameSize;
              }
              int size = encoder.getProcessedDataByteSize();
              while ((buf.length - oggCount) < size) { // grow buffer
                int nsz = buf.length * 2;
                byte[] nbuf = new byte[nsz];
                System.arraycopy(buf, 0, nbuf, 0, oggCount);
                buf = nbuf;
              }
              buf[count + 27 + packetCount] = (byte)(0xff & size);
              encoder.getProcessedData(buf, oggCount);
              oggCount += size;
              packetCount++;
            }
            if (packetCount >= packetsPerOggPage) {
              writeOggPageChecksum();
            }
          }
          System.arraycopy(prebuf, prepos, prebuf, 0, precount-prepos);
          precount -= prepos;
          prepos = 0;
          // we have encoded some data (all that we could),
          // so we can leave now, otherwise we return to a potentially
          // blocking read of the underlying inputstream.
          return;
        }
      }
      else { // read == 0
        // read 0 bytes from underlying stream yet it is not finished.
        if (precount >= prebuf.length) {
          // no more room in buffer
          if (prepos > 0) {
            // free some space
            System.arraycopy(prebuf, prepos, prebuf, 0, precount-prepos);
            precount -= prepos;
            prepos = 0;
          }
          else {
            // we could grow the pre-buffer but that risks in turn growing the
            // buffer which could lead sooner or later to an
            // OutOfMemoryException. 
            return;
          }
        }
        else {
          return;
        }
      }
    }
  }

  /**
   * Returns the number of bytes that can be read from this inputstream without
   * blocking. 
   * <p>
   * The <code>available</code> method of <code>FilteredAudioInputStream</code>
   * returns the sum of the the number of bytes remaining to be read in the
   * buffer (<code>count - pos</code>) and the result of calling the
   * <code>available</code> method of the underlying inputstream. 
   *
   * @return the number of bytes that can be read from this inputstream without
   * blocking.
   * @exception IOException if an I/O error occurs.
   * @see #in
   */
  public synchronized int available()
    throws IOException
  {
    int avail = super.available();
    int unencoded = precount - prepos + in.available();
    if (encoder.getEncoder().getVbr()) {
      switch (mode) {
        case 0: // Narrowband
          // ogg header size = 27 + packetsPerOggPage
          // count 1 byte (min 5 bits) for each block available
          return avail + (27 + 2 * packetsPerOggPage) *
                         (unencoded / (packetsPerOggPage*framesPerPacket*320));
        case 1: // Wideband
          // ogg header size = 27 + packetsPerOggPage
          // count 2 byte (min 9 bits) for each block available
          return avail + (27 + 2 * packetsPerOggPage) *
                         (unencoded / (packetsPerOggPage*framesPerPacket*640));
        case 2: // Ultra wideband
          // ogg header size = 27 + packetsPerOggPage
          // count 2 byte (min 13 bits) for each block available
          return avail + (27 + 3 * packetsPerOggPage) *
                         (unencoded / (packetsPerOggPage*framesPerPacket*1280));
        default:
          return avail;
      }
    }
    else {
      // Calculate size of a packet of Speex data.
      int spxpacketsize = encoder.getEncoder().getEncodedFrameSize();
      if (channels > 1) {
        spxpacketsize += 17; // 1+4(14=inband)+4(9=stereo)+8(stereo data)
      }
      spxpacketsize *= framesPerPacket;
      spxpacketsize = (spxpacketsize + 7) >> 3; // convert bits to bytes
      // Calculate size of an Ogg packet containing X Speex packets.
      // Ogg Packet = Ogg header + size of each packet + Ogg packets 
      int oggpacketsize = 27 + packetsPerOggPage * (spxpacketsize + 1);
      int pcmframesize; // size of PCM data necessary to encode 1 Speex packet.
      switch (mode) {
        case 0: // Narrowband
          // 1 frame = 20ms = 160ech * channels = 320bytes * channels
          pcmframesize = framesPerPacket * 320 * encoder.getChannels();
          avail += oggpacketsize *
                   (unencoded / (packetsPerOggPage * pcmframesize));
          return avail;
        case 1: // Wideband
          // 1 frame = 20ms = 320ech * channels = 640bytes * channels
          pcmframesize = framesPerPacket * 640 * encoder.getChannels();
          avail += oggpacketsize *
                   (unencoded / (packetsPerOggPage * pcmframesize));
          return avail;
        case 2: // Ultra wideband
          // 1 frame = 20ms = 640ech * channels = 1280bytes * channels
          pcmframesize = framesPerPacket * 1280 * encoder.getChannels();
          avail += oggpacketsize *
                   (unencoded / (packetsPerOggPage * pcmframesize));
          return avail;
        default:
          return avail;
      }
    }
  }
  
  //---------------------------------------------------------------------------
  // Ogg Specific Code
  //---------------------------------------------------------------------------
  
  /**
   * Write an OGG page header.
   * @param packets - the number of packets in the Ogg Page (must be between 1 and 255)
   * @param headertype - 2=bos: beginning of sream, 4=eos: end of sream
   */
  private void writeOggPageHeader(final int packets,
                                  final int headertype)
  {
    while ((buf.length - count) < (27 + packets)) { // grow buffer
      int nsz = buf.length * 2;
      byte[] nbuf = new byte[nsz];
      System.arraycopy(buf, 0, nbuf, 0, count);
      buf = nbuf;
    }
    AudioFileWriter.writeOggPageHeader(buf, count, headertype, granulepos,
                                       streamSerialNumber, pageCount++,
                                       packets, new byte[packets]);
    oggCount = count + 27 + packets;
  }

  /**
   * Calculate and write the OGG page checksum. This now closes the Ogg page.
   */
  private void writeOggPageChecksum()
  {
    // write the granulpos
    granulepos += framesPerPacket * frameSize * packetCount / 2;
    AudioFileWriter.writeLong(buf, count+6, granulepos);
    // write the checksum
    int chksum = OggCrc.checksum(0, buf, count, oggCount-count);
    AudioFileWriter.writeInt(buf, count+22, chksum);
    // reset variables for a new page.
    count = oggCount;
    packetCount = 0;
  }
  
  /**
   * Write the OGG Speex header then the comment header.
   */
  private void writeHeaderFrames()
  {
    int length = comment.length();
    if (length > 247) {
      comment = comment.substring(0, 247);
      length = 247;
    }
    while ((buf.length - count) < length + 144) {
      // grow buffer (108 = 28 + 80 = size of Ogg Header Frame)
      //             (28 + length + 8 = size of Comment Frame) 
      int nsz = buf.length * 2;
      byte[] nbuf = new byte[nsz];
      System.arraycopy(buf, 0, nbuf, 0, count);
      buf = nbuf;
    }
    // writes the OGG header page
    AudioFileWriter.writeOggPageHeader(buf, count, 2, granulepos,
                                       streamSerialNumber, pageCount++,
                                       1, new byte[] {80});
    oggCount = count + 28;
    /* writes the Speex header */
    AudioFileWriter.writeSpeexHeader(buf, oggCount, encoder.getSampleRate(),
                                     mode, encoder.getChannels(),
                                     encoder.getEncoder().getVbr(),
                                     framesPerPacket);
    oggCount += 80;
    /* Calculate Checksum */
    int chksum = OggCrc.checksum(0, buf, count, oggCount-count);
    AudioFileWriter.writeInt(buf, count+22, chksum);
    count = oggCount;
    // writes the OGG header page
    AudioFileWriter.writeOggPageHeader(buf, count, 0, granulepos,
                                       streamSerialNumber, pageCount++,
                                       1, new byte[] {(byte)(length+8)});
    oggCount = count + 28;
    /* writes the OGG comment page */
    AudioFileWriter.writeSpeexComment(buf, oggCount, comment);
    oggCount += length+8;
    /* Calculate Checksum */
    chksum = OggCrc.checksum(0, buf, count, oggCount-count);
    AudioFileWriter.writeInt(buf, count+22, chksum);
    count = oggCount;
    // reset variables for a new page.
    packetCount = 0;
  }
}
