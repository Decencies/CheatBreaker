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
 * Class: SpeexFormatConvertionProvider.java                                  *
 *                                                                            *
 * Author: Marc GIMPEL                                                        *
 *                                                                            *
 * Date: 12th July 2003                                                       *
 *                                                                            *
 ******************************************************************************/

/* $Id: SpeexFormatConvertionProvider.java,v 1.2 2004/10/21 16:21:58 mgimpel Exp $ */

package org.xiph.speex.spi;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.spi.FormatConversionProvider;

/**
 * A format conversion provider provides format conversion services from one or
 * more input formats to one or more output formats. Converters include codecs,
 * which encode and/or decode audio data, as well as transcoders, etc.
 * Format converters provide methods for determining what conversions are
 * supported and for obtaining an audio stream from which converted data can be
 * read.
 * 
 * The source format represents the format of the incoming audio data, which
 * will be converted.
 * 
 * The target format represents the format of the processed, converted audio
 * data. This is the format of the data that can be read from the stream
 * returned by one of the getAudioInputStream methods.
 * 
 * @author Marc Gimpel, Wimba S.A. (mgimpel@horizonwimba.com)
 * @version $Revision: 1.2 $
 */
public class SpeexFormatConvertionProvider
  extends FormatConversionProvider
{
  /** */
  public static final AudioFormat.Encoding[] NO_ENCODING = {};
  /** */
  public static final AudioFormat.Encoding[] PCM_ENCODING =
                        {AudioFormat.Encoding.PCM_SIGNED};
  /** */
  public static final AudioFormat.Encoding[] SPEEX_ENCODING =
                        {SpeexEncoding.SPEEX};
  /** */
  public static final AudioFormat.Encoding[] BOTH_ENCODINGS =
                        {SpeexEncoding.SPEEX, AudioFormat.Encoding.PCM_SIGNED};
  /** */
  public static final AudioFormat[] NO_FORMAT = {};

  /**
   * Obtains the set of source format encodings from which format conversion
   * services are provided by this provider.
   * @return array of source format encodings.
   * The array will always have a length of at least 1.
   */
  public AudioFormat.Encoding[] getSourceEncodings()
  {
    AudioFormat.Encoding[] encodings = {SpeexEncoding.SPEEX,
                                        AudioFormat.Encoding.PCM_SIGNED};
    return encodings;
  }
  
  /**
   * Obtains the set of target format encodings to which format conversion
   * services are provided by this provider.
   * @return array of target format encodings.
   * The array will always have a length of at least 1.
   */
  public AudioFormat.Encoding[] getTargetEncodings()
  {
    AudioFormat.Encoding[] encodings = {SpeexEncoding.SPEEX_Q0,
                                        SpeexEncoding.SPEEX_Q1,
                                        SpeexEncoding.SPEEX_Q2,
                                        SpeexEncoding.SPEEX_Q3,
                                        SpeexEncoding.SPEEX_Q4,
                                        SpeexEncoding.SPEEX_Q5,
                                        SpeexEncoding.SPEEX_Q6,
                                        SpeexEncoding.SPEEX_Q7,
                                        SpeexEncoding.SPEEX_Q8,
                                        SpeexEncoding.SPEEX_Q9,
                                        SpeexEncoding.SPEEX_Q10,
                                        SpeexEncoding.SPEEX_VBR0,
                                        SpeexEncoding.SPEEX_VBR1,
                                        SpeexEncoding.SPEEX_VBR2,
                                        SpeexEncoding.SPEEX_VBR3,
                                        SpeexEncoding.SPEEX_VBR4,
                                        SpeexEncoding.SPEEX_VBR5,
                                        SpeexEncoding.SPEEX_VBR6,
                                        SpeexEncoding.SPEEX_VBR7,
                                        SpeexEncoding.SPEEX_VBR8,
                                        SpeexEncoding.SPEEX_VBR9,
                                        SpeexEncoding.SPEEX_VBR10,
                                        AudioFormat.Encoding.PCM_SIGNED};
    return encodings;
  }
  
  /**
   * Obtains the set of target format encodings supported by the format
   * converter given a particular source format. If no target format encodings
   * are supported for this source format, an array of length 0 is returned.
   * @param sourceFormat format of the incoming data.
   * @return array of supported target format encodings.
   */
  public AudioFormat.Encoding[] getTargetEncodings(final AudioFormat sourceFormat)
  {
    if (sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
      AudioFormat.Encoding[] encodings = {SpeexEncoding.SPEEX_Q0,
                                          SpeexEncoding.SPEEX_Q1,
                                          SpeexEncoding.SPEEX_Q2,
                                          SpeexEncoding.SPEEX_Q3,
                                          SpeexEncoding.SPEEX_Q4,
                                          SpeexEncoding.SPEEX_Q5,
                                          SpeexEncoding.SPEEX_Q6,
                                          SpeexEncoding.SPEEX_Q7,
                                          SpeexEncoding.SPEEX_Q8,
                                          SpeexEncoding.SPEEX_Q9,
                                          SpeexEncoding.SPEEX_Q10,
                                          SpeexEncoding.SPEEX_VBR0,
                                          SpeexEncoding.SPEEX_VBR1,
                                          SpeexEncoding.SPEEX_VBR2,
                                          SpeexEncoding.SPEEX_VBR3,
                                          SpeexEncoding.SPEEX_VBR4,
                                          SpeexEncoding.SPEEX_VBR5,
                                          SpeexEncoding.SPEEX_VBR6,
                                          SpeexEncoding.SPEEX_VBR7,
                                          SpeexEncoding.SPEEX_VBR8,
                                          SpeexEncoding.SPEEX_VBR9,
                                          SpeexEncoding.SPEEX_VBR10};
      return encodings;
    }
    else if (sourceFormat.getEncoding() instanceof SpeexEncoding) {
      AudioFormat.Encoding[] encodings = {AudioFormat.Encoding.PCM_SIGNED};
      return encodings;
    }
    else {
      AudioFormat.Encoding[] encodings = {};
      return encodings;
    }
  }

  /**
   * Obtains the set of target formats with the encoding specified supported by
   * the format converter. If no target formats with the specified encoding are
   * supported for this source format, an array of length 0 is returned.
   * @param targetEncoding desired encoding of the outgoing data.
   * @param sourceFormat format of the incoming data.
   * @return array of supported target formats.
   */
  public AudioFormat[] getTargetFormats(final AudioFormat.Encoding targetEncoding,
                                        final AudioFormat sourceFormat)
  {
    if (sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) &&
        targetEncoding instanceof SpeexEncoding) {
      if (sourceFormat.getChannels() > 2 || sourceFormat.getChannels() <= 0 ||
          sourceFormat.isBigEndian()) {
        AudioFormat[] formats = {};
        return formats;
      }
      else {
        AudioFormat[] formats = {new AudioFormat(targetEncoding,
                                                 sourceFormat.getSampleRate(),
                                                 -1, // sample size in bits
                                                 sourceFormat.getChannels(),
                                                 -1, // frame size
                                                 -1, // frame rate
                                                 false)}; // little endian
        return formats;
      }
    }
    else if (sourceFormat.getEncoding() instanceof SpeexEncoding &&
             targetEncoding.equals(AudioFormat.Encoding.PCM_SIGNED)) {
      AudioFormat[] formats = {new AudioFormat(sourceFormat.getSampleRate(),
                                               16, // sample size in bits
                                               sourceFormat.getChannels(),
                                               true, // signed
                                               false)}; // little endian (for PCM wav)
      return formats;
    }
    else {
      AudioFormat[] formats = {};
      return formats;
    }
  }
  
  /**
   * Obtains an audio input stream with the specified encoding from the given
   * audio input stream.
   * @param targetEncoding - desired encoding of the stream after processing.
   * @param sourceStream - stream from which data to be processed should be read.
   * @return stream from which processed data with the specified target
   * encoding may be read.
   * @exception IllegalArgumentException - if the format combination supplied
   * is not supported.
   */
  public AudioInputStream getAudioInputStream(final AudioFormat.Encoding targetEncoding,
                                              final AudioInputStream sourceStream)
  {
    if (isConversionSupported(targetEncoding, sourceStream.getFormat())) {
      AudioFormat[] formats = getTargetFormats(targetEncoding,
                                               sourceStream.getFormat());
      if (formats != null && formats.length > 0) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        AudioFormat targetFormat = formats[0];
        if (sourceFormat.equals(targetFormat)) {
          return sourceStream;
        }
        else if (sourceFormat.getEncoding() instanceof SpeexEncoding &&
                 targetFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
          return new Speex2PcmAudioInputStream(sourceStream, targetFormat, -1);
        }
        else if (sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) &&
                 targetFormat.getEncoding() instanceof SpeexEncoding) {
          return new Pcm2SpeexAudioInputStream(sourceStream, targetFormat, -1);
        }
        else {
          throw new IllegalArgumentException("unable to convert " + sourceFormat.toString() +
                                             " to " + targetFormat.toString());
        }
      }
      else {
        throw new IllegalArgumentException("target format not found");
      }
    }
    else {
      throw new IllegalArgumentException("conversion not supported");
    }
  }
  
  /**
   * Obtains an audio input stream with the specified format from the given
   * audio input stream.
   * @param targetFormat - desired data format of the stream after processing.
   * @param sourceStream - stream from which data to be processed should be read.
   * @return stream from which processed data with the specified format may be
   * read.
   * @exception IllegalArgumentException - if the format combination supplied
   * is not supported.
   */
  public AudioInputStream getAudioInputStream(final AudioFormat targetFormat,
                                              final AudioInputStream sourceStream)
  {
    if (isConversionSupported(targetFormat, sourceStream.getFormat())) {
      AudioFormat[] formats = getTargetFormats(targetFormat.getEncoding(),
                                               sourceStream.getFormat());
      if (formats != null && formats.length > 0) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (sourceFormat.equals(targetFormat)) {
          return sourceStream;
        }
        else if (sourceFormat.getEncoding() instanceof SpeexEncoding &&
                 targetFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
          return new Speex2PcmAudioInputStream(sourceStream, targetFormat, -1);
        }
        else if (sourceFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED) &&
                 targetFormat.getEncoding() instanceof SpeexEncoding) {
          return new Pcm2SpeexAudioInputStream(sourceStream, targetFormat, -1);
        }
        else {
          throw new IllegalArgumentException("unable to convert " + sourceFormat.toString() +
                                             " to " + targetFormat.toString());
        }
      }
      else {
        throw new IllegalArgumentException("target format not found");
      }
    }
    else {
      throw new IllegalArgumentException("conversion not supported");
    }
  }
}
