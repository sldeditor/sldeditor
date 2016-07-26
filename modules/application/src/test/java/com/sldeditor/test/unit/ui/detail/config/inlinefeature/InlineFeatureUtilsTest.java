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

package com.sldeditor.test.unit.ui.detail.config.inlinefeature;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.junit.Test;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils;

/**
 * The unit test for InlineFeatureUtils.
 * <p>{@link com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineFeatureUtilsTest {

    private static String testInline1a = "<sld:StyledLayerDescriptor xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:sld=\"http://www.opengis.net/sld\" version=\"1.0.0\">"+
            "<sld:UserLayer>" +
            "<sld:Name>Inline</sld:Name>" +
            "<sld:InlineFeature>" +
            "   <FeatureCollection>" +
            "      <gml:featureMember>" +
            "        <feature>" +
            "          <geometryProperty>" +
            "            <gml:Polygon>" +
            "               <gml:outerBoundaryIs>" +
            "                  <gml:LinearRing>" +
            "                     <gml:coordinates>" +
            "     -127.0,51.0 -110.0,51.0 -110.0,41.0 -127.0,41.0 -127.0,51.0" +
            "                     </gml:coordinates>" +
            "                  </gml:LinearRing>" +
            "               </gml:outerBoundaryIs>" +
            "            </gml:Polygon>" +
            "          </geometryProperty>" +
            "          <title>Pacific NW </title>" +
            "        </feature>" +
            "      </gml:featureMember>" +
            "   </FeatureCollection>" +
            "</sld:InlineFeature>" +
            "</sld:UserLayer>" + 
            "</sld:StyledLayerDescriptor>";

    private static String testInline1b = "<sld:StyledLayerDescriptor xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:sld=\"http://www.opengis.net/sld\" version=\"1.0.0\">"+
            "<sld:UserLayer>" +
            "<sld:Name>Inline</sld:Name>" +
            "<sld:InlineFeature>" +
            "   <FeatureCollection>" +
            "      <gml:featureMember>" +
            "         <polygonProperty>" +
            "         <gml:Polygon>" +
            "            <gml:outerBoundaryIs>" +
            "               <gml:LinearRing>" +
            "                  <gml:coordinates>240167.78347885,869905.5610437" +
            "                     249317.75340551,869905.5610437 249317.75340551,879026.25071433" +
            "                     240167.78347885,879026.25071433" +
            "                  240167.78347885,869905.5610437</gml:coordinates>" +
            "               </gml:LinearRing>" +
            "            </gml:outerBoundaryIs>" +
            "         </gml:Polygon>" +
            "         </polygonProperty>" +
            "      </gml:featureMember>" +
            "   </FeatureCollection>" +
            "</sld:InlineFeature>" +
            "</sld:UserLayer>" + 
            "</sld:StyledLayerDescriptor>";

    private static String testInline2 = "   <FeatureCollection>" +
            "      <gml:featureMember>" +
            "        <feature>" +
            "          <geometryProperty>" +
            "            <gml:Polygon>" +
            "               <gml:outerBoundaryIs>" +
            "                  <gml:LinearRing>" +
            "                     <gml:coordinates>" +
            "     -127.0,51.0 -110.0,51.0 -110.0,41.0 -127.0,41.0 -127.0,51.0" +
            "                     </gml:coordinates>" +
            "                  </gml:LinearRing>" +
            "               </gml:outerBoundaryIs>" +
            "            </gml:Polygon>" +
            "          </geometryProperty>" +
            "          <title>Pacific NW </title>" +
            "        </feature>" +
            "      </gml:featureMember>" +
            "   </FeatureCollection>";
;
    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils#getInlineFeaturesText(org.geotools.styling.UserLayer)}.
     */
    @Test
    public void testGetInlineFeaturesText() {
        String actualResult = InlineFeatureUtils.getInlineFeaturesText(null);
        assertTrue(actualResult.compareTo("") == 0);

        // Test 1
        SLDData sldData = new SLDData(null, testInline1a);
        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        UserLayer userLayer1 = (UserLayer) sld.layers().get(0);

        actualResult = InlineFeatureUtils.getInlineFeaturesText(userLayer1);
        assertTrue(actualResult.startsWith("<FeatureCollection>"));
        assertTrue(actualResult.endsWith("</FeatureCollection>"));

        // Test 2
        sldData = new SLDData(null, testInline1b);
        sld = SLDUtils.createSLDFromString(sldData);

        UserLayer userLayer2 = (UserLayer) sld.layers().get(0);

        actualResult = InlineFeatureUtils.getInlineFeaturesText(userLayer2);
        assertTrue(actualResult.startsWith("<FeatureCollection>"));
        assertTrue(actualResult.endsWith("</FeatureCollection>"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUtils#setInlineFeatures(org.geotools.styling.UserLayer, java.lang.String)}.
     */
    @Test
    public void testSetInlineFeatures() {

        UserLayer userLayer = DefaultSymbols.createNewUserLayer();

        InlineFeatureUtils.setInlineFeatures(null, null);
        InlineFeatureUtils.setInlineFeatures(userLayer, null);
        InlineFeatureUtils.setInlineFeatures(null, "");
        
        assertNull(userLayer.getInlineFeatureDatastore());
        InlineFeatureUtils.setInlineFeatures(userLayer, testInline2);
        assertNotNull(userLayer.getInlineFeatureDatastore());
    }

}
