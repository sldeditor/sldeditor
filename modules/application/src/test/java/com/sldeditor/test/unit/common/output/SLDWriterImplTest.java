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
package com.sldeditor.test.unit.common.output;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.output.impl.SLDWriterImpl;

/**
 * The unit test for SLDWriterImpl.
 * <p>{@link com.sldeditor.common.output.impl.SLDWriterImpl}
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDWriterImplTest {

    private String expectedSld = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <NamedLayer><Name>Simple Point</Name><UserStyle><Title>SLD Cook Book: Simple Point</Title><FeatureTypeStyle><Rule><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=\"fill\">#FF0000</CssParameter></Fill></Mark><Size>6</Size></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";

    /**
     * Test method for {@link com.sldeditor.common.output.impl.SLDWriterImpl#encodeSLD(org.geotools.styling.StyledLayerDescriptor)}.
     */
    @Test
    public void testEncodeSLD() {
        SLDWriterImpl writer = new SLDWriterImpl();

        String result = writer.encodeSLD(null);
        assertEquals("", result);
        SLDData sldData = new SLDData(null, expectedSld);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);
        
        result = writer.encodeSLD(sld);
        assertTrue(!result.isEmpty());
    }

}
