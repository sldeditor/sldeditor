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

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.output.SLDWriterInterface;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;

/**
 * Class that converts an SLD stored as a StyledLayerDescriptor to a YSLD formatted string.
 *
 * @author Robert Ward (SCISYS)
 */
public class YSLDWriterImpl implements SLDWriterInterface {

    /** Default constructor. */
    public YSLDWriterImpl() {
        // Default constructor
    }

    /**
     * Encode sld to a string.
     *
     * @param sld the sld
     * @return the YSLD string
     */
    @Override
    public String encodeSLD(URL resourceLocator, StyledLayerDescriptor sld) {
        StringWriter out = new StringWriter();
        if (sld != null) {
            try {
                Ysld.encode(sld, out);
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        return out.toString();
    }
}
