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
package com.sldeditor.test.unit.datasource.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;

/**
 * The Class DummyInlineSLDFile.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyInlineSLDFile implements SLDEditorFileInterface {

    private SLDDataInterface sldData = null;

    private StyledLayerDescriptor sld = null;

    private List<String> expectedFieldList = new ArrayList<String>();

    public DummyInlineSLDFile()
    {
        String sldContents = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<StyledLayerDescriptor version=\"1.0.0\" " +
                "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" " +
                "    xmlns=\"http://www.opengis.net/sld\" " +
                "    xmlns:sld=\"http://www.opengis.net/sld\" " +
                "    xmlns:ogc=\"http://www.opengis.net/ogc\" " +
                "    xmlns:gml=\"http://www.opengis.net/gml\" " +
                "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<sld:UserLayer>" +
                "<sld:Name>Inline</sld:Name>" +
                "<sld:InlineFeature>" +
                "  <FeatureCollection>" +
                "    <featureMember>" +
                "      <gml:_Feature>" +
                "        <geometryProperty>" +
                "          <Polygon>" +
                "            <outerBoundaryIs>" +
                "              <LinearRing>" +
                "                <coordinates>-127,51 -110,51 -110,41 -127,41 -127,51</coordinates>" +
                "              </LinearRing>" +
                "            </outerBoundaryIs>" +
                "          </Polygon>" +
                "        </geometryProperty>" +
                "        <title>Pacific NW</title>" +
                "      </gml:_Feature>" +
                "    </featureMember>" +
                "  </FeatureCollection>" +
                "</sld:InlineFeature>" +
                "<sld:LayerFeatureConstraints>" +
                "  <sld:FeatureTypeConstraint/>" +
                "</sld:LayerFeatureConstraints>" +
                "<sld:UserStyle>" +
                "  <sld:Name>Default Styler</sld:Name>" +
                "  <sld:FeatureTypeStyle>" +
                "    <sld:Name>name</sld:Name>" +
                "    <sld:Rule>" +
                "      <sld:PolygonSymbolizer>" +
                "        <sld:Stroke>" +
                "          <sld:CssParameter name=\"stroke\">#FF0000</sld:CssParameter>" +
                "          <sld:CssParameter name=\"stroke-width\">2</sld:CssParameter>" +
                "        </sld:Stroke>" +
                "      </sld:PolygonSymbolizer>" +
                "      <sld:TextSymbolizer>" +
                "        <sld:Label>" +
                "          <ogc:PropertyName>title</ogc:PropertyName>" +
                "        </sld:Label>" +
                "        <sld:LabelPlacement>" +
                "          <sld:PointPlacement>" +
                "            <sld:AnchorPoint>" +
                "              <sld:AnchorPointX>0.0</sld:AnchorPointX>" +
                "              <sld:AnchorPointY>0.5</sld:AnchorPointY>" +
                "            </sld:AnchorPoint>" +
                "          </sld:PointPlacement>" +
                "        </sld:LabelPlacement>" +
                "        <sld:Fill>" +
                "          <sld:CssParameter name=\"fill\">#FF0000</sld:CssParameter>" +
                "        </sld:Fill>" +
                "      </sld:TextSymbolizer>" +
                "    </sld:Rule>" +
                "  </sld:FeatureTypeStyle>" +
                "</sld:UserStyle>" +
                "</sld:UserLayer>" +
                "</StyledLayerDescriptor>";

        sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);

        URL url = SLDEditorFile.class.getClassLoader().getResource("polygon/sld/shp/sld_cookbook_polygon.shp");
        String filename = url.toString();

        DataSourcePropertiesInterface dsp = DataSourceConnectorFactory.getDataSourceProperties(filename);
        dsp.setFilename(filename);

        sldData.setDataSourceProperties(dsp);

        sld = SLDUtils.createSLDFromString(sldData);

        expectedFieldList.add("the_geom");
        expectedFieldList.add("title");
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

    public List<String> getExpectedFieldList()
    {
        return expectedFieldList;
    }
}
