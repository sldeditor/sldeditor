/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.tool.scale;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.tool.scale.ScaleSLDData;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * The Class DummyScaleSLDFile.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyScaleSLDFile implements SLDEditorFileInterface {

    private SLDDataInterface sldData = null;

    private StyledLayerDescriptor sld = null;

    private List<ScaleSLDData> expectedScaleList = new ArrayList<ScaleSLDData>();

    /** Instantiates a new dummy internal SLD file 2. */
    public DummyScaleSLDFile() {
        String sldContents =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<sld:StyledLayerDescriptor version=\"1.0.0\" "
                        + "  xmlns=\"http://www.opengis.net/sld\" "
                        + "  xmlns:sld=\"http://www.opengis.net/sld\" "
                        + "  xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "  xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                        + "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "  <sld:NamedLayer>"
                        + "    <sld:Name>Zoom-based polygon</sld:Name>"
                        + "    <sld:UserStyle>"
                        + "      <sld:Title>SLD Cook Book: Zoom-based polygon</sld:Title>"
                        + "      <sld:FeatureTypeStyle>"
                        + "        <sld:Rule>"
                        + "          <sld:Name>Large</sld:Name>"
                        + "          <sld:MaxScaleDenominator>1111</sld:MaxScaleDenominator>"
                        + "          <sld:PolygonSymbolizer>"
                        + "            <sld:Fill>"
                        + "              <sld:CssParameter name=\"fill\">#0000CC</sld:CssParameter>"
                        + "            </sld:Fill>"
                        + "            <sld:Stroke>"
                        + "              <sld:CssParameter name=\"stroke\">#000000</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"stroke-width\">7</sld:CssParameter>"
                        + "            </sld:Stroke>"
                        + "          </sld:PolygonSymbolizer>"
                        + "          <sld:TextSymbolizer>"
                        + "            <sld:Label>"
                        + "              <ogc:PropertyName>name</ogc:PropertyName>"
                        + "            </sld:Label>"
                        + "            <sld:Font>"
                        + "              <sld:CssParameter name=\"font-family\">Arial</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"font-size\">14</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"font-style\">normal</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"font-weight\">bold</sld:CssParameter>"
                        + "            </sld:Font>"
                        + "            <sld:LabelPlacement>"
                        + "              <sld:PointPlacement>"
                        + "                <sld:AnchorPoint>"
                        + "                  <sld:AnchorPointX>0.5</sld:AnchorPointX>"
                        + "                  <sld:AnchorPointY>0.5</sld:AnchorPointY>"
                        + "                </sld:AnchorPoint>"
                        + "              </sld:PointPlacement>"
                        + "            </sld:LabelPlacement>"
                        + "            <sld:Fill>"
                        + "              <sld:CssParameter name=\"fill\">#FFFFFF</sld:CssParameter>"
                        + "            </sld:Fill>"
                        + "          </sld:TextSymbolizer>"
                        + "        </sld:Rule>"
                        + "        <sld:Rule>"
                        + "          <sld:Name>Medium</sld:Name>"
                        + "          <sld:MinScaleDenominator>2222</sld:MinScaleDenominator>"
                        + "          <sld:MaxScaleDenominator>3333</sld:MaxScaleDenominator>"
                        + "          <sld:PolygonSymbolizer>"
                        + "            <sld:Fill>"
                        + "              <sld:CssParameter name=\"fill\">#0000CC</sld:CssParameter>"
                        + "            </sld:Fill>"
                        + "            <sld:Stroke>"
                        + "              <sld:CssParameter name=\"stroke\">#000000</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"stroke-width\">4</sld:CssParameter>"
                        + "            </sld:Stroke>"
                        + "          </sld:PolygonSymbolizer>"
                        + "        </sld:Rule>"
                        + "        <sld:Rule>"
                        + "          <sld:Name>Small</sld:Name>"
                        + "          <sld:MinScaleDenominator>4444</sld:MinScaleDenominator>"
                        + "          <sld:PolygonSymbolizer>"
                        + "            <sld:Fill>"
                        + "              <sld:CssParameter name=\"fill\">#0000CC</sld:CssParameter>"
                        + "            </sld:Fill>"
                        + "            <sld:Stroke>"
                        + "              <sld:CssParameter name=\"stroke\">#000000</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"stroke-width\">1</sld:CssParameter>"
                        + "            </sld:Stroke>"
                        + "          </sld:PolygonSymbolizer>"
                        + "        </sld:Rule>"
                        + "        <sld:Rule>"
                        + "          <sld:Name>Not set</sld:Name>"
                        + "          <sld:PolygonSymbolizer>"
                        + "            <sld:Fill>"
                        + "              <sld:CssParameter name=\"fill\">#0000CC</sld:CssParameter>"
                        + "            </sld:Fill>"
                        + "            <sld:Stroke>"
                        + "              <sld:CssParameter name=\"stroke\">#000000</sld:CssParameter>"
                        + "              <sld:CssParameter name=\"stroke-width\">1</sld:CssParameter>"
                        + "            </sld:Stroke>"
                        + "          </sld:PolygonSymbolizer>"
                        + "        </sld:Rule>"
                        + "      </sld:FeatureTypeStyle>"
                        + "    </sld:UserStyle>"
                        + "  </sld:NamedLayer>"
                        + "</sld:StyledLayerDescriptor>";

        sldData = new SLDData(new StyleWrapper(null, "testscale.sld"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

        File temp = null;
        try {
            temp = File.createTempFile("testscale", ".sld");
            temp.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sldData.setSLDFile(temp);
        sld = SLDUtils.createSLDFromString(sldData);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        ScaleSLDData obj1 = new ScaleSLDData(sld, null);
        obj1.setMaxScale(1111);
        Rule rule1 = styleFactory.createRule();
        rule1.setMaxScaleDenominator(1111);
        rule1.setName("Large");
        obj1.setRule(rule1);
        expectedScaleList.add(obj1);

        ScaleSLDData obj2 = new ScaleSLDData(sld, null);
        obj2.setMinScale(2222);
        obj2.setMaxScale(3333);
        Rule rule2 = styleFactory.createRule();
        rule2.setMinScaleDenominator(2222);
        rule2.setMaxScaleDenominator(3333);
        rule2.setName("Medium");
        obj2.setRule(rule2);
        expectedScaleList.add(obj2);

        ScaleSLDData obj3 = new ScaleSLDData(sld, null);
        obj3.setMinScale(4444);
        Rule rule3 = styleFactory.createRule();
        rule3.setMinScaleDenominator(4444);
        rule3.setName("Small");
        obj3.setRule(rule3);
        expectedScaleList.add(obj3);
    }

    /**
     * Gets the SLD data.
     *
     * @return the SLD data
     */
    @Override
    public SLDDataInterface getSLDData() {
        return sldData;
    }

    /**
     * Gets the data source.
     *
     * @return the data source
     */
    @Override
    public DataSourcePropertiesInterface getDataSource() {
        return sldData.getDataSourceProperties();
    }

    /**
     * Gets the sld.
     *
     * @return the sld
     */
    @Override
    public StyledLayerDescriptor getSLD() {
        return sld;
    }

    public List<ScaleSLDData> getExpectedScaleList() {
        return expectedScaleList;
    }
}
