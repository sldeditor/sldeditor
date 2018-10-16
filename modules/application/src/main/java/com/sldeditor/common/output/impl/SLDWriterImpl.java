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
import com.sldeditor.common.data.SLDExternalImages;
import com.sldeditor.common.output.SLDWriterInterface;
import java.net.URL;
import javax.xml.transform.TransformerException;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Class that converts an SLD stored as a StyledLayerDescriptor to a SLD formatted string.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDWriterImpl implements SLDWriterInterface {

    /** Default constructor. */
    public SLDWriterImpl() {
        // Default constructor
    }

    /**
     * Encode sld to a string.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @return the string
     */
    @Override
    public String encodeSLD(URL resourceLocator, StyledLayerDescriptor sld) {
        String xml = "";

        if (sld != null) {
            InlineDatastoreVisitor duplicator = new InlineDatastoreVisitor();
            sld.accept(duplicator);
            StyledLayerDescriptor sldCopy = (StyledLayerDescriptor) duplicator.getCopy();

            if (resourceLocator != null) {
                SLDExternalImages.updateOnlineResources(resourceLocator, sldCopy);
            }

            SLDTransformer transformer = new SLDTransformer();
            transformer.setIndentation(2);
            try {
                xml = transformer.transform(sldCopy);
            } catch (TransformerException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        return xml;
    }
}
