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

import javax.xml.transform.TransformerException;

import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.output.SLDWriterInterface;

/**
 * Class that converts and SLD stored as a StyledLayerDescriptor to a string.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDWriterImpl implements SLDWriterInterface {

    /**
     * Default constructor.
     */
    public SLDWriterImpl()
    {
    }

    /**
     * Encode sld to a string
     *
     * @param sld the sld
     * @return the string
     */
    public String encodeSLD(StyledLayerDescriptor sld)
    {
        String xml = "";

        if(sld != null)
        {
            SLDTransformer transformer = new SLDTransformer();
            transformer.setIndentation(2);
            try {
                xml = transformer.transform(sld);
            } catch (TransformerException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        return xml;
    }
}
