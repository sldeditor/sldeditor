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
 * The Class DummyInternalSLDFile4.
 *
 * @author Robert Ward (SCISYS)
 */
public class DummyInternalSLDFile4 implements SLDEditorFileInterface {

    private SLDDataInterface sldData = null;

    private StyledLayerDescriptor sld = null;

    private List<String> expectedFieldList = new ArrayList<String>();

    private List<String> expectedGeometryFieldList = new ArrayList<String>();

    /** Instantiates a new dummy internal SLD file 2. */
    public DummyInternalSLDFile4() {
        String sldContents =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<sld:StyledLayerDescriptor xmlns=\"http://www.opengis.net/sld\" xmlns:sld=\"http://www.opengis.net/sld\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">"
                        + "<NamedLayer>"
                        + "  <Name>Alpha channel</Name>"
                        + "  <UserStyle>"
                        + "        <Title>SLD Cook Book: Alpha channel</Title>"
                        + "        <FeatureTypeStyle>"
                        + "          <Rule>"
                        + "            <RasterSymbolizer>"
                        + "              <ColorMap>"
                        + "                <ColorMapEntry color=\"#008000\" quantity=\"70\" />"
                        + "                <ColorMapEntry color=\"#008000\" quantity=\"256\" opacity=\"0\"/>"
                        + "              </ColorMap>"
                        + "            </RasterSymbolizer>"
                        + "          </Rule>"
                        + "        </FeatureTypeStyle>"
                        + "     </UserStyle>"
                        + "    </NamedLayer>"
                        + "</sld:StyledLayerDescriptor>";

        sldData = new SLDData(new StyleWrapper(null, "testinternalfile4"), sldContents);
        sldData.setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

        sld = SLDUtils.createSLDFromString(sldData);
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
