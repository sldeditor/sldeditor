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

import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * The Class DummyInternalSLDFile3.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyInternalSLDFile3 implements SLDEditorFileInterface {

    private SLDDataInterface sldData = null;

    private StyledLayerDescriptor sld = null;

    private List<String> expectedFieldList = new ArrayList<String>();

    private List<String> expectedGeometryFieldList = new ArrayList<String>();

    /** Instantiates a new dummy internal SLD file 3. */
    public DummyInternalSLDFile3() {
        String sldContents =
                "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
                        + "<abc:StyledLayerDescriptor version=\"1.0.0\" "
                        + "    xsi:schemaLocation=\"http://www.opengis.net/sld StyledLayerDescriptor.xsd\" "
                        + "    xmlns:abc=\"http://www.opengis.net/sld\" "
                        + "    xmlns:ogc=\"http://www.opengis.net/ogc\" "
                        + "    xmlns:xlink=\"http://www.w3.org/1999/xlink\" "
                        + "    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                        + "  <abc:NamedLayer>"
                        + "    <abc:Name>Attribute-based point</abc:Name>"
                        + "    <abc:UserStyle>"
                        + "      <abc:Title>GeoServer SLD Cook Book: Attribute-based point</abc:Title>"
                        + "      <abc:FeatureTypeStyle>"
                        + "        <abc:Rule>"
                        + "          <abc:Name>SmallPop</abc:Name>"
                        + "          <abc:Title>1 to 50000</abc:Title>"
                        + "          <ogc:Filter>"
                        + "            <ogc:PropertyIsLessThan>"
                        + "              <ogc:PropertyName>pop</ogc:PropertyName>"
                        + "              <ogc:Literal>50000</ogc:Literal>"
                        + "            </ogc:PropertyIsLessThan>"
                        + "          </ogc:Filter>"
                        + "          <abc:PointSymbolizer>"
                        + "            <abc:Geometry>"
                        + "              <ogc:PropertyName>qwerty</ogc:PropertyName>"
                        + "            </abc:Geometry>"
                        + "            <abc:Graphic>"
                        + "              <abc:Mark>"
                        + "                <abc:WellKnownName>star</abc:WellKnownName>"
                        + "                <abc:Fill>"
                        + "                  <abc:CssParameter name=\"fill\">#0033CC</abc:CssParameter>"
                        + "                </abc:Fill>"
                        + "              </abc:Mark>"
                        + "              <abc:Rotation>"
                        + "                <ogc:PropertyName>angle</ogc:PropertyName>"
                        + "              </abc:Rotation>"
                        + "              <abc:Size>"
                        + "                <ogc:PropertyName>size</ogc:PropertyName>"
                        + "              </abc:Size>"
                        + "            </abc:Graphic>"
                        + "          </abc:PointSymbolizer>"
                        + "          <abc:TextSymbolizer>"
                        + "            <abc:Geometry>"
                        + "              <ogc:Function name=\"centroid\">"
                        + "                <ogc:PropertyName>qwerty</ogc:PropertyName>"
                        + "              </ogc:Function>"
                        + "            </abc:Geometry>"
                        + "            <abc:Label>"
                        + "              <ogc:Function name=\"strToLowerCase\">"
                        + "                <ogc:PropertyName>popstring</ogc:PropertyName>"
                        + "              </ogc:Function>"
                        + "            </abc:Label>"
                        + "           </abc:TextSymbolizer>"
                        + "        </abc:Rule>"
                        + "      </abc:FeatureTypeStyle>"
                        + "    </abc:UserStyle>"
                        + "  </abc:NamedLayer>"
                        + "</abc:StyledLayerDescriptor>";

        sldData = new SLDData(new StyleWrapper(null, "test.sld"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

        sld = SLDUtils.createSLDFromString(sldData);

        expectedFieldList.add("pop");
        expectedFieldList.add("angle");
        expectedFieldList.add("size");
        expectedFieldList.add("popstring");

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

    public List<String> getExpectedFieldList() {
        return expectedFieldList;
    }

    public List<String> getExpectedGeometryFieldList() {
        return expectedGeometryFieldList;
    }
}
