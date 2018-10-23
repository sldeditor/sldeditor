/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.legend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.ui.legend.LegendManager;
import com.sldeditor.ui.legend.option.LegendOptionData;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.junit.jupiter.api.Test;

/**
 * The unit test for LegendManager.
 *
 * <p>{@link com.sldeditor.ui.legend.LegendManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class LegendManagerTest {

    /** Test create legend styled layer descriptor string string. */
    @Test
    public void testCreateLegendStyledLayerDescriptorStringString() {
        StyledLayerDescriptor sld = testSLD1();
        String heading = "Test Heading";

        LegendManager.getInstance().sldLoaded(null);
        LegendManager.getInstance().updateLegendOptionData(null);
        boolean actualResult = compareLegendImage(sld, heading);
        assertTrue(actualResult);
    }

    /**
     * Create test SLD
     *
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor testSLD1() {
        String sldContents =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" "
                        + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                        + "    xmlns=\"http://www.opengis.net/sld\" "
                        + "    xmlns:sld=\"http://www.opengis.net/sld\" "
                        + "    xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "    xmlns:gml=\"http://www.opengis.net/gml\" "
                        + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "<sld:UserLayer>"
                        + "<sld:Name>Inline</sld:Name>"
                        + "<sld:InlineFeature>"
                        + "  <FeatureCollection>"
                        + "    <featureMember>"
                        + "      <gml:_Feature>"
                        + "        <geometryProperty>"
                        + "          <Polygon>"
                        + "            <outerBoundaryIs>"
                        + "              <LinearRing>"
                        + "              <coordinates>-127,51 -110,51 -110,41 -127,41 -127,51</coordinates>"
                        + "              </LinearRing>"
                        + "            </outerBoundaryIs>"
                        + "          </Polygon>"
                        + "        </geometryProperty>"
                        + "        <title>Pacific NW</title>"
                        + "      </gml:_Feature>"
                        + "    </featureMember>"
                        + "  </FeatureCollection>"
                        + "</sld:InlineFeature>"
                        + "<sld:LayerFeatureConstraints>"
                        + "  <sld:FeatureTypeConstraint/>"
                        + "</sld:LayerFeatureConstraints>"
                        + "<sld:UserStyle>"
                        + "  <sld:Name>Default Styler</sld:Name>"
                        + "  <sld:FeatureTypeStyle>"
                        + "    <sld:Name>name</sld:Name>"
                        + "    <sld:Rule>"
                        + "      <sld:PolygonSymbolizer>"
                        + "        <sld:Stroke>"
                        + "          <sld:CssParameter name=\"stroke\">#FF0000</sld:CssParameter>"
                        + "          <sld:CssParameter name=\"stroke-width\">2</sld:CssParameter>"
                        + "        </sld:Stroke>"
                        + "      </sld:PolygonSymbolizer>"
                        + "      <sld:TextSymbolizer>"
                        + "        <sld:Label>"
                        + "          <ogc:PropertyName>title</ogc:PropertyName>"
                        + "        </sld:Label>"
                        + "        <sld:LabelPlacement>"
                        + "          <sld:PointPlacement>"
                        + "            <sld:AnchorPoint>"
                        + "              <sld:AnchorPointX>0.0</sld:AnchorPointX>"
                        + "              <sld:AnchorPointY>0.5</sld:AnchorPointY>"
                        + "            </sld:AnchorPoint>"
                        + "          </sld:PointPlacement>"
                        + "        </sld:LabelPlacement>"
                        + "        <sld:Fill>"
                        + "          <sld:CssParameter name=\"fill\">#FF0000</sld:CssParameter>"
                        + "        </sld:Fill>"
                        + "      </sld:TextSymbolizer>"
                        + "    </sld:Rule>"
                        + "  </sld:FeatureTypeStyle>"
                        + "</sld:UserStyle>"
                        + "</sld:UserLayer>"
                        + "</StyledLayerDescriptor>";

        SLDData sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);
        return sld;
    }

    /**
     * Create test SLD
     *
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor testSLD2() {
        String sldContents =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" "
                        + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                        + "    xmlns=\"http://www.opengis.net/sld\" "
                        + "    xmlns:sld=\"http://www.opengis.net/sld\" "
                        + "    xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "    xmlns:gml=\"http://www.opengis.net/gml\" "
                        + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "<NamedLayer>"
                        + "    <Name>Attribute-based polygon</Name>"
                        + "    <UserStyle>"
                        + "      <Title>SLD Cook Book: Attribute-based polygon</Title>"
                        + "      <FeatureTypeStyle>"
                        + "        <Rule>"
                        + "          <Name>SmallPop</Name>"
                        + "          <Title>Less Than 200,000</Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:PropertyIsLessThan>"
                        + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "              <ogc:Literal>200000</ogc:Literal>"
                        + "            </ogc:PropertyIsLessThan>"
                        + "          </ogc:Filter>"
                        + "          <PolygonSymbolizer>"
                        + "            <Fill>"
                        + "              <CssParameter name=\"fill\">#66FF66</CssParameter>"
                        + "            </Fill>"
                        + "          </PolygonSymbolizer>"
                        + "        </Rule>"
                        + "        <Rule>"
                        + "          <Name>MediumPop</Name>"
                        + "          <Title>200,000 to 500,000</Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:And>"
                        + "              <ogc:PropertyIsGreaterThanOrEqualTo>"
                        + "                <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "                <ogc:Literal>200000</ogc:Literal>"
                        + "              </ogc:PropertyIsGreaterThanOrEqualTo>"
                        + "              <ogc:PropertyIsLessThan>"
                        + "                <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "                <ogc:Literal>500000</ogc:Literal>"
                        + "              </ogc:PropertyIsLessThan>"
                        + "            </ogc:And>"
                        + "          </ogc:Filter>"
                        + "          <PolygonSymbolizer>"
                        + "            <Fill>"
                        + "              <CssParameter name=\"fill\">#33CC33</CssParameter>"
                        + "            </Fill>"
                        + "          </PolygonSymbolizer>"
                        + "        </Rule>"
                        + "        <Rule>"
                        + "          <Name>LargePop</Name>"
                        + "          <Title>Greater Than 500,000</Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:PropertyIsGreaterThan>"
                        + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "              <ogc:Literal>500000</ogc:Literal>"
                        + "            </ogc:PropertyIsGreaterThan>"
                        + "          </ogc:Filter>"
                        + "          <PolygonSymbolizer>"
                        + "            <Fill>"
                        + "              <CssParameter name=\"fill\">#009900</CssParameter>"
                        + "            </Fill>"
                        + "          </PolygonSymbolizer>"
                        + "        </Rule>"
                        + "      </FeatureTypeStyle>"
                        + "    </UserStyle>"
                        + "  </NamedLayer>"
                        + "</StyledLayerDescriptor>";

        SLDData sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);
        return sld;
    }

    /**
     * Compare generated legend image with expected image.
     *
     * @param sld the sld
     * @param heading the heading
     * @return true, if successful
     */
    private boolean compareLegendImage(StyledLayerDescriptor sld, String heading) {

        if (sld == null) {
            fail("sld == null");
        }

        boolean actualResult = true;

        File outputfile = null;
        try {
            outputfile = File.createTempFile("tmp", ".png");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temporary file");
        }

        BufferedImage actualImage =
                LegendManager.getInstance().createLegend(sld, heading, outputfile.getName());

        LegendManager.getInstance()
                .saveLegendImage(actualImage, LegendManager.getLegendImageFormat(), outputfile);

        outputfile.delete();

        return actualResult;
    }

    @Test
    public void testEmpty() {
        assertNull(LegendManager.getInstance().createLegend(null, null, null));
    }

    @Test
    public void testUpdateLegendOptionData() {
        StyledLayerDescriptor sld = testSLD1();
        String heading = "Test Heading";

        LegendOptionData legendOption = new LegendOptionData();
        legendOption.setBackgroundColour(Color.CYAN);
        legendOption.setBorder(true);
        legendOption.setBandInformation(true);
        legendOption.setBorderColour(Color.GREEN);
        legendOption.setImageHeight(100);
        legendOption.setImageWidth(100);
        legendOption.setImageHeight(100);
        legendOption.setShowLabels(true);
        legendOption.setShowTitle(true);
        int mask = java.awt.Font.BOLD | java.awt.Font.ITALIC;
        Font labelFont = new Font("Serif", mask, 12);
        legendOption.setLabelFont(labelFont);

        LegendManager.getInstance().sldLoaded(legendOption);

        boolean actualResult = compareLegendImage(sld, heading);
        assertTrue(actualResult);
    }

    @Test
    public void testSeparateSymbolizers() {
        StyledLayerDescriptor sld = testSLD1();
        String heading = "Test Heading";

        LegendOptionData legendOption = new LegendOptionData();
        legendOption.setBackgroundColour(Color.CYAN);
        legendOption.setBorder(true);
        legendOption.setBandInformation(true);
        legendOption.setBorderColour(Color.GREEN);
        legendOption.setImageHeight(100);
        legendOption.setImageWidth(100);
        legendOption.setImageHeight(100);
        legendOption.setShowLabels(true);
        legendOption.setShowTitle(true);
        legendOption.setSplitSymbolizers(true);
        int mask = java.awt.Font.PLAIN;
        Font labelFont = new Font("Serif", mask, 12);
        legendOption.setLabelFont(labelFont);

        SelectedSymbol.getInstance().setSld(sld);
        StyledLayer styledlayer = sld.layers().get(0);
        SelectedSymbol.getInstance().setStyledLayer(styledlayer);
        SelectedSymbol.getInstance().setStyle(((UserLayer) styledlayer).userStyles().get(0));

        LegendManager.getInstance().sldLoaded(legendOption);

        boolean actualResult = compareLegendImage(sld, heading);
        assertTrue(actualResult);

        File destinationFolder = new File(System.getProperty("java.io.tmpdir"));
        String filename = "displayed filename";
        List<String> filenameList = new ArrayList<>();
        assertTrue(
                LegendManager.getInstance()
                        .saveLegendImage(
                                sld,
                                destinationFolder,
                                "layer name",
                                heading,
                                filename,
                                filenameList));

        for (String fileCreated : filenameList) {
            File f = new File(fileCreated);
            f.delete();
        }
    }

    @Test
    public void testUpdateLegendOptionData2() {
        StyledLayerDescriptor sld = testSLD2();
        String heading = "Test Heading";

        LegendOptionData legendOption = new LegendOptionData();
        legendOption.setBackgroundColour(Color.CYAN);
        legendOption.setBorder(true);
        legendOption.setBandInformation(true);
        legendOption.setBorderColour(Color.GREEN);
        legendOption.setImageHeight(100);
        legendOption.setImageWidth(100);
        legendOption.setImageHeight(100);
        legendOption.setShowLabels(false);
        legendOption.setShowTitle(false);

        LegendManager.getInstance().sldLoaded(legendOption);

        boolean actualResult = compareLegendImage(sld, heading);
        assertTrue(actualResult);
    }

    @Test
    public void testSaveLegendImageStyledLayerDescriptorFileStringStringStringListOfString() {}

    @Test
    public void testGetLegendImageFormat() {
        assertEquals(LegendManager.getLegendImageFormat(), "png");
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static File stream2file(InputStream in) throws IOException {
        final File tempFile = File.createTempFile("expected_", ".png");
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return tempFile;
    }
}
