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
package com.sldeditor.datasource.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.UserLayerImpl;
import org.opengis.feature.simple.SimpleFeatureType;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.SLDEditorFileInterface;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

/**
 * The Class CreateInternalDataSource.
 *
 * @author Robert Ward (SCISYS)
 */
public class CreateInternalDataSource implements CreateDataSourceInterface {

    /** The data source info. */
    private DataSourceInfo dsInfo = new DataSourceInfo();

    /** The Constant INTERNAL_SCHEMA_NAME. */
    private static final String INTERNAL_SCHEMA_NAME = "MEMORY";

    /** The geometry field. */
    private GeometryField geometryField = new GeometryField();

    /** The list of class considered geometry types. */
    private static List<Class<?>> geometryTypeList = Arrays.asList(Geometry.class,
            MultiPolygon.class,
            LineString.class, 
            Point.class);

    /**
     * Creates the.
     *
     * @param editorFile the editor file
     * @return the list of datastores
     */
    @Override
    public List<DataSourceInfo> connect(SLDEditorFileInterface editorFile)
    {
        List<DataSourceInfo> dataSourceInfoList = new ArrayList<DataSourceInfo>();
        dataSourceInfoList.add(dsInfo);

        dsInfo.reset();

        if(editorFile != null)
        {
            StyledLayerDescriptor sld = editorFile.getSLD();
            SLDDataInterface sldData = editorFile.getSLDData();

            determineGeometryType(sld);

            SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();

            //set the name
            String typeName = INTERNAL_SCHEMA_NAME;
            dsInfo.setTypeName(typeName);
            b.setName( typeName );

            String namespace = null;
            b.setNamespaceURI(namespace);

            //add a geometry property
            b.setCRS( DefaultGeographicCRS.WGS84 ); // set crs first

            List<DataSourceAttributeData> fieldList = sldData.getFieldList();

            // Set the geometry field by default
            geometryField.reset();

            if((fieldList == null) || fieldList.isEmpty())
            {
                ExtractAttributes extract = new ExtractAttributes();
                extract.extractDefaultFields(b, sld);
                fieldList = extract.getFields();

                List<String> geometryFields = extract.getGeometryFields();
                for(String geometryFieldName : geometryFields)
                {
                    geometryField.setGeometryFieldName(geometryFieldName);
                }
            }
            else
            {
                addFields(b, fieldList);
            }

            setGeometryField(b, geometryField.getGeometryFieldName());

            // Store the fields
            sldData.setFieldList(fieldList);

            // Build the feature type
            SimpleFeatureType schema = b.buildFeatureType();
            dsInfo.setSchema(schema);

            CreateSampleData sampleData = new CreateSampleData();
            sampleData.create(schema, fieldList);
            MemoryDataStore dataStore = sampleData.getDataStore();

            dsInfo.setDataStore(dataStore);
        }
        return dataSourceInfoList;
    }

    /**
     * Sets the geometry field.
     *
     * @param b the feature type builder
     * @param geometryFieldName the geometry field name
     */
    private void setGeometryField(SimpleFeatureTypeBuilder b, String geometryFieldName)
    {
        switch(dsInfo.getGeometryType())
        {
        case POLYGON:
            b.add(geometryFieldName, MultiPolygon.class);
            break;
        case LINE:
            b.add(geometryFieldName, LineString.class);
            break;
        case POINT:
        default:
            b.add(geometryFieldName, Point.class);
            break;
        }
        b.setDefaultGeometry(geometryFieldName);
    }

    /**
     * Adds the fields.
     *
     * @param b the feature type builder
     * @param fieldList the field list
     */
    private void addFields(SimpleFeatureTypeBuilder b,
            List<DataSourceAttributeData> fieldList) {

        for(DataSourceAttributeData field : fieldList)
        {
            if(isGeometryField(field.getType()))
            {
                geometryField.setGeometryFieldName(field.getName());
            }
            else
            {
                b.add(field.getName(), field.getType());
            }
        }
    }

    /**
     * Checks if field is a geometry field.
     *
     * @param fieldType the field type
     * @return true, if is geometry field
     */
    private boolean isGeometryField(Class<?> fieldType) {
        return geometryTypeList.contains(fieldType);
    }

    /**
     * Determine geometry type.
     *
     * @param sld the sld
     */
    private void determineGeometryType(StyledLayerDescriptor sld)
    {
        GeometryTypeEnum geometryType = internal_determineGeometryType(sld);

        dsInfo.setGeometryType(geometryType);
    }

    protected GeometryTypeEnum internal_determineGeometryType(StyledLayerDescriptor sld)
    {
        GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;

        if(sld != null)
        {
            List<StyledLayer> styledLayerList = sld.layers();
            int pointCount = 0;
            int lineCount = 0;
            int polygonCount = 0;
            int rasterCount = 0;

            for(StyledLayer styledLayer : styledLayerList)
            {
                List<Style> styleList = null;

                if(styledLayer instanceof NamedLayerImpl)
                {
                    NamedLayerImpl namedLayerImpl = (NamedLayerImpl)styledLayer;

                    styleList = namedLayerImpl.styles();
                }
                else if(styledLayer instanceof UserLayerImpl)
                {
                    UserLayerImpl userLayerImpl = (UserLayerImpl)styledLayer;

                    styleList = userLayerImpl.userStyles();
                }

                if(styleList != null)
                {
                    for(Style style : styleList)
                    {
                        for(FeatureTypeStyle fts : style.featureTypeStyles())
                        {
                            for(Rule rule : fts.rules())
                            {
                                for(org.opengis.style.Symbolizer symbolizer : rule.symbolizers())
                                {
                                    if(symbolizer instanceof PointSymbolizer)
                                    {
                                        pointCount ++;
                                    }
                                    else if(symbolizer instanceof LineSymbolizer)
                                    {
                                        lineCount ++;
                                    }
                                    else if(symbolizer instanceof PolygonSymbolizer)
                                    {
                                        polygonCount ++;
                                    }
                                    else if(symbolizer instanceof RasterSymbolizer)
                                    {
                                        rasterCount ++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(polygonCount > 0)
            {
                geometryType = GeometryTypeEnum.POLYGON;
            }
            else if(lineCount > 0)
            {
                geometryType = GeometryTypeEnum.LINE;
            }
            else if(pointCount > 0)
            {
                geometryType = GeometryTypeEnum.POINT;
            }
            else if(rasterCount > 0)
            {
                geometryType = GeometryTypeEnum.RASTER;
            }
        }
        return geometryType;
    }
}
