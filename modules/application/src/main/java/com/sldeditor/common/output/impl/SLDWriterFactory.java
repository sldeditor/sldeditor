/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2016, SCISYS UK Limited
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.sldeditor.common.output.impl;

import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDWriterInterface;

/**
 * A factory for creating SLDWriter objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDWriterFactory {

    /** The sld writer impl. */
    private static SLDWriterImpl sldWriterImpl = null;

    /** The ysld writer impl. */
    private static YSLDWriterImpl ysldWriterImpl = null;

    /** The default writer. */
    private static SLDOutputFormatEnum defaultWriter = SLDOutputFormatEnum.SLD;

    /**
     * Creates a new SLDWriter object.
     *
     * @param hint the hint
     * @return the SLD writer interface
     */
    public static SLDWriterInterface createWriter(SLDOutputFormatEnum hint) {
        if (hint == null) {
            hint = defaultWriter;
        }

        switch (hint) {
        case YSLD: {
            if (ysldWriterImpl == null) {
                ysldWriterImpl = new YSLDWriterImpl();
            }
            return ysldWriterImpl;
        }
        case SLD:
        default: {
            if (sldWriterImpl == null) {
                sldWriterImpl = new SLDWriterImpl();
            }
            return sldWriterImpl;
        }
        }
    }
}
