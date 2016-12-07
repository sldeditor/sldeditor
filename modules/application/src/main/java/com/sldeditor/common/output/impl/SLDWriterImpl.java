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

import java.net.URL;

import javax.xml.transform.TransformerException;

import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDExternalImages;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.generated.Version;

/**
 * Class that converts an SLD stored as a StyledLayerDescriptor to a SLD formatted string.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDWriterImpl implements SLDWriterInterface {

    /** The Constant START_OF_XML_HEADER. */
    private static final String START_OF_XML_HEADER = "<?";

    /** The Constant END_OF_XML_HEADER. */
    private static final String END_OF_XML_HEADER = "?>";

    /** The header. */
    private static final String header = String.format("<!-- Created by %s %s -->",
            Version.getAppName(), Version.getVersionNumber());

    /**
     * Default constructor.
     */
    public SLDWriterImpl() {
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
            DuplicatingStyleVisitor duplicator = new DuplicatingStyleVisitor();
            sld.accept(duplicator);
            StyledLayerDescriptor sldCopy = (StyledLayerDescriptor) duplicator.getCopy();

            if (resourceLocator != null) {
                SLDExternalImages.updateOnlineResources(resourceLocator, sldCopy);
            }

            SLDTransformer transformer = new SLDTransformer();
            transformer.setIndentation(2);
            try {
                xml = transformer.transform(sldCopy);
                if (xml.startsWith(START_OF_XML_HEADER)) {
                    int pos = xml.indexOf(END_OF_XML_HEADER, 0);
                    if (pos > 1) {
                        pos = pos + END_OF_XML_HEADER.length() + 1;
                        String xmlHeader = xml.substring(0, pos);
                        String sldBody = xml.substring(pos);

                        xml = xmlHeader + header + sldBody;
                    }
                }
            } catch (TransformerException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }

        return xml;
    }

}
