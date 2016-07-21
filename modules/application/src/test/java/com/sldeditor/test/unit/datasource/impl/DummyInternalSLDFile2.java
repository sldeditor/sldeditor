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

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;

/**
 * The Class DummyInternalSLDFile2.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyInternalSLDFile2 implements SLDEditorFileInterface {

    private SLDDataInterface sldData = null;
    
    private StyledLayerDescriptor sld = null;
    
    private List<String> expectedFieldList = new ArrayList<String>();
    
    private List<String> expectedGeometryFieldList = new ArrayList<String>();

    public DummyInternalSLDFile2()
    {
        String sldContents = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">" +
        "  <sld:NamedLayer>" +
        "    <sld:Name>Simple polygon</sld:Name>" +
        "    <sld:UserStyle>" +
        "      <sld:Name>Default Styler</sld:Name>" +
        "      <sld:Title>SLD Cook Book: Simple polygon</sld:Title>" +
        "      <sld:FeatureTypeStyle>" +
        "        <sld:Name>name</sld:Name>" +
        "        <sld:Rule>" +
        "          <sld:PolygonSymbolizer>" +
        "            <sld:Geometry>" +
        "              <ogc:PropertyName>qwerty</ogc:PropertyName>" +
        "            </sld:Geometry>" +
        "            <sld:Fill>" +
        "              <sld:CssParameter name=\"fill\">#000080</sld:CssParameter>" +
        "            </sld:Fill>" +
        "          </sld:PolygonSymbolizer>" +
        "        </sld:Rule>" +
        "      </sld:FeatureTypeStyle>" +
        "    </sld:UserStyle>" +
        "  </sld:NamedLayer>" +
        "</sld:StyledLayerDescriptor>";

        sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());
        
        sld = SLDUtils.createSLDFromString(sldData);
        
        expectedGeometryFieldList.add("qwerty");
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

    public List<String> getExpectedGeometryFieldList() {
        return expectedGeometryFieldList;
    }
}
