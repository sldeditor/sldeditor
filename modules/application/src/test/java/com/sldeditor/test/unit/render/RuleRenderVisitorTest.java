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

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.render.RuleRenderVisitor;
import com.sldeditor.ui.render.RuleRenderOptions;

/**
 * Unit test for RuleRenderVisitor class.
 * 
 * <p>{@link com.sldeditor.render.RuleRenderVisitor}
 * 
 * @author Robert Ward (SCISYS)
 */
public class RuleRenderVisitorTest {

    /**
     * Test rule render visitor.
     */
    @Test
    public void testRuleRenderVisitor() {

        StyledLayerDescriptor sld = createSLD();

        StyledLayer styledLayer = sld.layers().get(0);

        if (styledLayer instanceof NamedLayerImpl) {
            NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;

            Style style = namedLayerImpl.styles().get(0);

            FeatureTypeStyle ftsToRender = style.featureTypeStyles().get(0);

            // Just find matching feature type style
            int symbolIndex = 0;

            RuleRenderVisitor visitor = new RuleRenderVisitor(ftsToRender, null, symbolIndex,
                    new RuleRenderOptions());
            style.accept(visitor);
            Style copy = (Style) visitor.getCopy();

            Rule ruleToRender = ftsToRender.rules().get(0);
            Rule actualRule = copy.featureTypeStyles().get(0).rules().get(0);

            // Filters should be null
            assertNull(actualRule.getFilter());
            assertEquals(ruleToRender.symbolizers(), actualRule.symbolizers());

            // Now try and find 2nd rule, 2nd symbolizer
            ruleToRender = ftsToRender.rules().get(1);
            symbolIndex = 1;
            visitor = new RuleRenderVisitor(ftsToRender, ruleToRender, symbolIndex,
                    new RuleRenderOptions());
            style.accept(visitor);
            copy = (Style) visitor.getCopy();

            assertEquals(1, copy.featureTypeStyles().get(0).rules().size());
            actualRule = copy.featureTypeStyles().get(0).rules().get(0);
            assertNull(actualRule.getFilter());
            assertEquals(1, actualRule.symbolizers().size());
            assertEquals(ruleToRender.symbolizers().get(1), actualRule.symbolizers().get(0));
        }
    }

    /**
     * Creates the sld.
     *
     * @return the styled layer descriptor
     */
    private StyledLayerDescriptor createSLD() {
        String sldContents = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                + "<StyledLayerDescriptor version=\"1.0.0\" "
                + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                + "    xmlns=\"http://www.opengis.net/sld\" "
                + "    xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" + "  <NamedLayer>"
                + "    <Name>Attribute-based point</Name>" + "    <UserStyle>"
                + "      <Title>GeoServer SLD Cook Book: Attribute-based point</Title>"
                + "      <FeatureTypeStyle>" + "        <Rule>" + "          <Name>SmallPop</Name>"
                + "          <Title>1 to 50000</Title>" + "          <ogc:Filter>"
                + "            <ogc:PropertyIsLessThan>"
                + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                + "              <ogc:Literal>50000</ogc:Literal>"
                + "            </ogc:PropertyIsLessThan>" + "          </ogc:Filter>"
                + "          <PointSymbolizer>" + "            <Graphic>" + "              <Mark>"
                + "                <WellKnownName>star</WellKnownName>" + "                <Fill>"
                + "                  <CssParameter name=\"fill\">#0033CC</CssParameter>"
                + "                </Fill>" + "              </Mark>" + "              <Rotation>"
                + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                + "              </Rotation>" + "              <Size>"
                + "                <ogc:PropertyName>size</ogc:PropertyName>"
                + "              </Size>" + "            </Graphic>"
                + "          </PointSymbolizer>" + "        </Rule>" + "        <Rule>"
                + "          <Name>MediumPop</Name>" + "          <Title>50000 to 500000</Title>"
                + "          <ogc:Filter>" + "            <ogc:PropertyIsLessThan>"
                + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                + "              <ogc:Literal>500000</ogc:Literal>"
                + "            </ogc:PropertyIsLessThan>" + "          </ogc:Filter>"
                + "          <PointSymbolizer>" + "            <Graphic>" + "              <Mark>"
                + "                <WellKnownName>star</WellKnownName>" + "                <Fill>"
                + "                  <CssParameter name=\"fill\">#0033CC</CssParameter>"
                + "                </Fill>" + "              </Mark>" + "              <Rotation>"
                + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                + "              </Rotation>" + "              <Size>"
                + "                <ogc:PropertyName>size</ogc:PropertyName>"
                + "              </Size>" + "            </Graphic>"
                + "          </PointSymbolizer>" + "          <PointSymbolizer>"
                + "            <Graphic>" + "              <Mark>"
                + "                <WellKnownName>circle</WellKnownName>" + "                <Fill>"
                + "                  <CssParameter name=\"fill\">#123456</CssParameter>"
                + "                </Fill>" + "              </Mark>" + "              <Rotation>"
                + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                + "              </Rotation>" + "              <Size>"
                + "                <ogc:PropertyName>size</ogc:PropertyName>"
                + "              </Size>" + "            </Graphic>"
                + "          </PointSymbolizer>" + "        </Rule>" + "        <Rule>"
                + "          <Name>LargePop</Name>" + "          <Title>500000 to 900000</Title>"
                + "          <ogc:Filter>" + "            <ogc:PropertyIsLessThan>"
                + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                + "              <ogc:Literal>900000</ogc:Literal>"
                + "            </ogc:PropertyIsLessThan>" + "          </ogc:Filter>"
                + "          <PointSymbolizer>" + "            <Graphic>" + "              <Mark>"
                + "                <WellKnownName>star</WellKnownName>" + "                <Fill>"
                + "                  <CssParameter name=\"fill\">#0033CC</CssParameter>"
                + "                </Fill>" + "              </Mark>" + "              <Rotation>"
                + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                + "              </Rotation>" + "              <Size>"
                + "                <ogc:PropertyName>size</ogc:PropertyName>"
                + "              </Size>" + "            </Graphic>"
                + "          </PointSymbolizer>" + "        </Rule>" + "      </FeatureTypeStyle>"
                + "    </UserStyle>" + "  </NamedLayer>" + "</StyledLayerDescriptor>";

        SLDData sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        return sld;
    }
}
