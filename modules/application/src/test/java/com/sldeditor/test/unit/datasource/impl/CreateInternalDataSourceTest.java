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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayer;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.impl.CreateInternalDataSource;
import com.sldeditor.datasource.impl.DataSourceInfo;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * Unit test for CreateInternalDataSource.
 * 
 * <p>{@link com.sldeditor.datasource.impl.CreateInternalDataSource}
 * 
 * @author Robert Ward (SCISYS)
 */
public class CreateInternalDataSourceTest {

    class TestCreateInternalDataSource extends CreateInternalDataSource {

        /**
         * Call determine geometry type.
         *
         * @param sld the sld
         */
        public GeometryTypeEnum callDetermineGeometryType(StyledLayerDescriptor sld) {
            return super.internal_determineGeometryType(sld);
        }

    }

    /**
     * Test method for
     * {@link com.sldeditor.datasource.impl.CreateInternalDataSource#connect(com.sldeditor.datasource.SLDEditorFileInterface)}.
     */
    @Test
    public void testConnect() {
        CreateInternalDataSource ds = new CreateInternalDataSource();

        List<DataSourceInfo> dataSourceInfoList = ds.connect(null, null, null);

        DataSourceInfo dsInfo = dataSourceInfoList.get(0);

        assertTrue(dsInfo != null);
        assertNull(dsInfo.getDataStore());
        assertNull(dsInfo.getTypeName());

        SLDEditorFileInterface sldEditor = new DummyInternalSLDFile();
        dataSourceInfoList = ds.connect(null, null, sldEditor);
        dsInfo = dataSourceInfoList.get(0);

        assertTrue(dsInfo != null);
        assertTrue(dsInfo.getTypeName() != null);
        assertTrue(dsInfo.getDataStore() != null);
    }

    /**
     * Test method for
     * {@link com.sldeditor.datasource.impl.CreateInternalDataSource#determineGeometryType(StyledLayerDescriptor)}.
     */
    @Test
    public void testDetermineGeometryType() {
        TestCreateInternalDataSource ds = new TestCreateInternalDataSource();

        assertEquals(GeometryTypeEnum.UNKNOWN, ds.callDetermineGeometryType(null));

        // Create StyledLayerDescriptor
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();
        NamedLayer namedLayer = styleFactory.createNamedLayer();
        sld.addStyledLayer(namedLayer);
        UserLayer userLayer = styleFactory.createUserLayer();
        sld.addStyledLayer(userLayer);
        Style style = styleFactory.createStyle();
        namedLayer.addStyle(style);
        List<FeatureTypeStyle> ftsList = style.featureTypeStyles();
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
        ftsList.add(fts);
        Rule rule = styleFactory.createRule();
        fts.rules().add(rule);

        // Raster
        RasterSymbolizer raster = DefaultSymbols.createDefaultRasterSymbolizer();
        rule.symbolizers().add(raster);

        assertEquals(GeometryTypeEnum.RASTER, ds.callDetermineGeometryType(sld));

        // Polygon
        rule.symbolizers().clear();
        PolygonSymbolizer polygon = DefaultSymbols.createDefaultPolygonSymbolizer();
        rule.symbolizers().add(polygon);
        assertEquals(GeometryTypeEnum.POLYGON, ds.callDetermineGeometryType(sld));

        // Line
        rule.symbolizers().clear();
        LineSymbolizer line = DefaultSymbols.createDefaultLineSymbolizer();
        rule.symbolizers().add(line);
        assertEquals(GeometryTypeEnum.LINE, ds.callDetermineGeometryType(sld));

        // Point
        rule.symbolizers().clear();
        PointSymbolizer point = DefaultSymbols.createDefaultPointSymbolizer();
        rule.symbolizers().add(point);
        assertEquals(GeometryTypeEnum.POINT, ds.callDetermineGeometryType(sld));

        // Add line to point
        rule.symbolizers().add(line);
        assertEquals(GeometryTypeEnum.LINE, ds.callDetermineGeometryType(sld));

        // Add point, line
        rule.symbolizers().clear();
        rule.symbolizers().add(line);
        rule.symbolizers().add(point);
        assertEquals(GeometryTypeEnum.LINE, ds.callDetermineGeometryType(sld));

        // Add polygon, line
        rule.symbolizers().clear();
        rule.symbolizers().add(point);
        rule.symbolizers().add(polygon);
        assertEquals(GeometryTypeEnum.POLYGON, ds.callDetermineGeometryType(sld));
    }
}
