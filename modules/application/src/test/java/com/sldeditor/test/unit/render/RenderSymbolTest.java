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

package com.sldeditor.test.unit.render;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.render.RenderSymbol;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

/**
 * Unit test for RenderSymbol class.
 *
 * <p>{@link com.sldeditor.render.RenderSymbol}
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderSymbolTest {

    /** Test method for {@link com.sldeditor.render.RenderSymbol#RenderSymbol()}. */
    @Test
    public void testRenderSymbol() {
        RenderSymbol symbol = new RenderSymbol();

        StyledLayerDescriptor sld = createSLD();

        StyledLayer styledLayer = sld.layers().get(0);

        if (styledLayer instanceof NamedLayerImpl) {
            NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;

            Style expectedStyle = namedLayerImpl.styles().get(0);

            SelectedSymbol.getInstance().setSld(sld);
            Style actualStyle = symbol.getRenderStyle(SelectedSymbol.getInstance());

            assertNull(actualStyle.featureTypeStyles().get(0).rules().get(0).getFilter());
            assertEquals(expectedStyle.getName(), actualStyle.getName());

            Rule expectedRule = expectedStyle.featureTypeStyles().get(0).rules().get(1);
            SelectedSymbol.getInstance()
                    .setFeatureTypeStyle(expectedStyle.featureTypeStyles().get(0));
            SelectedSymbol.getInstance().setRule(expectedRule);
            actualStyle = symbol.getRenderStyle(SelectedSymbol.getInstance());
            assertNull(actualStyle.featureTypeStyles().get(0).rules().get(0).getFilter());
            assertEquals(
                    expectedRule.getName(),
                    actualStyle.featureTypeStyles().get(0).rules().get(0).getName());
        }
    }

    /**
     * Creates the sld.
     *
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor createSLD() {
        String sldContents =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<StyledLayerDescriptor version=\"1.0.0\" "
                        + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                        + "    xmlns=\"http://www.opengis.net/sld\" "
                        + "    xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "  <NamedLayer>"
                        + "    <Name>Attribute-based point</Name>"
                        + "    <UserStyle>"
                        + "      <Title>GeoServer SLD Cook Book: Attribute-based point</Title>"
                        + "      <FeatureTypeStyle>"
                        + "        <Rule>"
                        + "          <Name>SmallPop</Name>"
                        + "          <Title>1 to 50000</Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:PropertyIsLessThan>"
                        + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "              <ogc:Literal>50000</ogc:Literal>"
                        + "            </ogc:PropertyIsLessThan>"
                        + "          </ogc:Filter>"
                        + "          <PointSymbolizer>"
                        + "            <Graphic>"
                        + "              <Mark>"
                        + "                <WellKnownName>star</WellKnownName>"
                        + "                <Fill>"
                        + "                  <CssParameter name=\"fill\">#0033CC</CssParameter>"
                        + "                </Fill>"
                        + "              </Mark>"
                        + "              <Rotation>"
                        + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                        + "              </Rotation>"
                        + "              <Size>"
                        + "                <ogc:PropertyName>size</ogc:PropertyName>"
                        + "              </Size>"
                        + "            </Graphic>"
                        + "          </PointSymbolizer>"
                        + "        </Rule>"
                        + "        <Rule>"
                        + "          <Name>MediumPop</Name>"
                        + "          <Title>50000 to 500000</Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:PropertyIsLessThan>"
                        + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "              <ogc:Literal>500000</ogc:Literal>"
                        + "            </ogc:PropertyIsLessThan>"
                        + "          </ogc:Filter>"
                        + "          <PointSymbolizer>"
                        + "            <Graphic>"
                        + "              <Mark>"
                        + "                <WellKnownName>star</WellKnownName>"
                        + "                <Fill>"
                        + "                  <CssParameter name=\"fill\">#0033CC</CssParameter>"
                        + "                </Fill>"
                        + "              </Mark>"
                        + "              <Rotation>"
                        + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                        + "              </Rotation>"
                        + "              <Size>"
                        + "                <ogc:PropertyName>size</ogc:PropertyName>"
                        + "              </Size>"
                        + "            </Graphic>"
                        + "          </PointSymbolizer>"
                        + "          <PointSymbolizer>"
                        + "            <Graphic>"
                        + "              <Mark>"
                        + "                <WellKnownName>circle</WellKnownName>"
                        + "                <Fill>"
                        + "                  <CssParameter name=\"fill\">#123456</CssParameter>"
                        + "                </Fill>"
                        + "              </Mark>"
                        + "              <Rotation>"
                        + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                        + "              </Rotation>"
                        + "              <Size>"
                        + "                <ogc:PropertyName>size</ogc:PropertyName>"
                        + "              </Size>"
                        + "            </Graphic>"
                        + "          </PointSymbolizer>"
                        + "        </Rule>"
                        + "        <Rule>"
                        + "          <Name>LargePop</Name>"
                        + "          <Title>500000 to 900000</Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:PropertyIsLessThan>"
                        + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "              <ogc:Literal>900000</ogc:Literal>"
                        + "            </ogc:PropertyIsLessThan>"
                        + "          </ogc:Filter>"
                        + "          <PointSymbolizer>"
                        + "            <Graphic>"
                        + "              <Mark>"
                        + "                <WellKnownName>star</WellKnownName>"
                        + "                <Fill>"
                        + "                  <CssParameter name=\"fill\">#0033CC</CssParameter>"
                        + "                </Fill>"
                        + "              </Mark>"
                        + "              <Rotation>"
                        + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                        + "              </Rotation>"
                        + "              <Size>"
                        + "                <ogc:PropertyName>size</ogc:PropertyName>"
                        + "              </Size>"
                        + "            </Graphic>"
                        + "          </PointSymbolizer>"
                        + "        </Rule>"
                        + "      </FeatureTypeStyle>"
                        + "    </UserStyle>"
                        + "  </NamedLayer>"
                        + "</StyledLayerDescriptor>";

        SLDData sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        return sld;
    }
}
