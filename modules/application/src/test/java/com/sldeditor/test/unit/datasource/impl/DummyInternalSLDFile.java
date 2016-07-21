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
 * The Class DummyInternalSLDFile.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyInternalSLDFile implements SLDEditorFileInterface {

    private SLDDataInterface sldData = null;

    private StyledLayerDescriptor sld = null;

    private List<String> expectedFieldList = new ArrayList<String>();

    public DummyInternalSLDFile()
    {
        String sldContents = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
                "<StyledLayerDescriptor version=\"1.0.0\" " +
                "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" " +
                "    xmlns=\"http://www.opengis.net/sld\" " +
                "    xmlns:ogc=\"http://www.opengis.net/ogc\" " +
                "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
                "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "  <NamedLayer>" +
                "    <Name>Attribute-based point</Name>" +
                "    <UserStyle>" +
                "      <Title>GeoServer SLD Cook Book: Attribute-based point</Title>" +
                "      <FeatureTypeStyle>" +
                "        <Rule>" +
                "          <Name>SmallPop</Name>" +
                "          <Title>1 to 50000</Title>" +
                "          <ogc:Filter>" +
                "            <ogc:PropertyIsLessThan>" +
                "              <ogc:PropertyName>pop</ogc:PropertyName>" +
                "              <ogc:Literal>50000</ogc:Literal>" +
                "            </ogc:PropertyIsLessThan>" +
                "          </ogc:Filter>" +
                "          <PointSymbolizer>" +
                "            <Graphic>" +
                "              <Mark>" +
                "                <WellKnownName>star</WellKnownName>" +
                "                <Fill>" +
                "                  <CssParameter name=\"fill\">#0033CC</CssParameter>" +
                "                </Fill>" +
                "              </Mark>" +
                "              <Rotation>" +
                "                <ogc:PropertyName>angle</ogc:PropertyName>" +
                "              </Rotation>" +
                "              <Size>" +
                "                <ogc:PropertyName>size</ogc:PropertyName>" +
                "              </Size>" +
                "            </Graphic>" +
                "          </PointSymbolizer>" +
                "        </Rule>" +
                "      </FeatureTypeStyle>" +
                "    </UserStyle>" +
                "  </NamedLayer>" +
                "</StyledLayerDescriptor>";

        sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

        sld = SLDUtils.createSLDFromString(sldData);

        expectedFieldList.add("pop");
        expectedFieldList.add("angle");
        expectedFieldList.add("size");
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
