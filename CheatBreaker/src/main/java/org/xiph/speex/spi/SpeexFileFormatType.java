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
 * Class: SpeexFileFormatType.java                                            *
 *                                                                            *
 * Author: Marc GIMPEL                                                        *
 *                                                                            *
 * Date: 12th July 2003                                                       *
 *                                                                            *
 ******************************************************************************/

/* $Id: SpeexFileFormatType.java,v 1.2 2004/10/21 16:21:58 mgimpel Exp $ */

package org.xiph.speex.spi;

import javax.sound.sampled.AudioFileFormat;

/**
 * FileFormatTypes used by the Speex audio decoder.
 * 
 * @author Marc Gimpel, Wimba S.A. (mgimpel@horizonwimba.com)
 * @version $Revision: 1.2 $
 */
public class SpeexFileFormatType
  extends AudioFileFormat.Type
{
  /**
   * Specifies an OGG Speex file.
   */
  public static final AudioFileFormat.Type SPEEX =new SpeexFileFormatType("SPEEX", "spx");

  /**
   * Constructs a file type.
   * @param name - the name of the Speex File Format.
   * @param extension - the file extension for this Speex File Format.
   */
  public SpeexFileFormatType(final String name, final String extension)
  {
    super(name, extension);
  }
}
