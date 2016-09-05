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
package com.sldeditor.test.unit.common.data;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.geotools.styling.MarkImpl;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;

/**
 * The unit test for SLDUtils.
 * <p>{@link com.sldeditor.common.data.SLDUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDUtilsTest {

    private String expectedSld = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><StyledLayerDescriptor version=\"1.0.0\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <NamedLayer><Name>Simple Point</Name><UserStyle><Title>SLD Cook Book: Simple Point</Title><FeatureTypeStyle><Rule><PointSymbolizer><Graphic><Mark><WellKnownName>circle</WellKnownName><Fill><CssParameter name=\"fill\">#FF0000</CssParameter></Fill></Mark><Size>6</Size></Graphic></PointSymbolizer></Rule></FeatureTypeStyle></UserStyle></NamedLayer></StyledLayerDescriptor>";

    @Test
    public void testCreateSLDFromString() {

        SLDData sldData = new SLDData(null, expectedSld);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(null);

        assertNull(sld);
        sld = SLDUtils.createSLDFromString(sldData);

        StyledLayer[] styledLayers = sld.getStyledLayers();
        NamedLayer namedLayer = (NamedLayer) styledLayers[0];
        Style[] actualStyles = namedLayer.getStyles();
        PointSymbolizer pointSymbolizer = (PointSymbolizer) actualStyles[0].featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);

        MarkImpl mark = (MarkImpl) pointSymbolizer.getGraphic().graphicalSymbols().get(0);
        assertEquals("circle", mark.getWellKnownName().toString());
    }

    @Test
    public void testReadSLDFile() {
        try {
            File tmpFile = File.createTempFile("test", ".sld");

            FileWriter fileWriter = new FileWriter(tmpFile);
            fileWriter.write(expectedSld);
            fileWriter.flush();
            fileWriter.close();

            StyledLayerDescriptor sld = SLDUtils.readSLDFile(null);

            assertNull(sld);
            sld = SLDUtils.readSLDFile(tmpFile);
            StyledLayer[] styledLayers = sld.getStyledLayers();
            NamedLayer namedLayer = (NamedLayer) styledLayers[0];
            Style[] actualStyles = namedLayer.getStyles();
            PointSymbolizer pointSymbolizer = (PointSymbolizer) actualStyles[0].featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);

            MarkImpl mark = (MarkImpl) pointSymbolizer.getGraphic().graphicalSymbols().get(0);
            assertEquals("circle", mark.getWellKnownName().toString());

            tmpFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create test file");
        }
    }

}
