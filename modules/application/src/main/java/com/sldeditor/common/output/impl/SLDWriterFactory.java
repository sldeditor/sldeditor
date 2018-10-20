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

    /** The map box writer impl. */
    private static MapBoxWriterImpl mapBoxWriterImpl = null;

    /** The default writer. */
    private static SLDOutputFormatEnum defaultWriter = SLDOutputFormatEnum.SLD;

    /** Private default constructor */
    private SLDWriterFactory() {
        // Private default constructor
    }

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

        SLDWriterInterface writer;
        switch (hint) {
            case YSLD:
                writer = createYSLDWriter();
                break;
            case MAPBOX:
                writer = createMapBoxWriter();
                break;
            case SLD:
            default:
                writer = createSLDWriter();
                break;
        }

        return writer;
    }

    /**
     * Creates a new SLD Writer object.
     *
     * @return the SLD writer interface
     */
    private static SLDWriterInterface createSLDWriter() {
        if (sldWriterImpl == null) {
            sldWriterImpl = new SLDWriterImpl();
        }
        return sldWriterImpl;
    }

    /**
     * Creates a new MapBox Writer object.
     *
     * @return the SLD writer interface
     */
    private static SLDWriterInterface createMapBoxWriter() {
        if (mapBoxWriterImpl == null) {
            mapBoxWriterImpl = new MapBoxWriterImpl();
        }
        return mapBoxWriterImpl;
    }

    /**
     * Creates a new YSLD Writer object.
     *
     * @return the SLD writer interface
     */
    private static SLDWriterInterface createYSLDWriter() {
        if (ysldWriterImpl == null) {
            ysldWriterImpl = new YSLDWriterImpl();
        }
        return ysldWriterImpl;
    }
}
