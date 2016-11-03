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

import java.io.IOException;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.type.GeometryTypeImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.sldeditor.datasource.example.ExampleLineInterface;
import com.sldeditor.datasource.example.ExamplePointInterface;
import com.sldeditor.datasource.example.ExamplePolygonInterface;

/**
 * The Class CreateSampleData.
 *
 * @author Robert Ward (SCISYS)
 */
public class CreateSampleData {

    /** The memory data store. */
    private MemoryDataStore memory = null;

    /** The geometry type. */
    private GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;

    /**
     * Default constructor
     */
    public CreateSampleData() {
    }

    /**
     * Gets the data store.
     *
     * @return the data store
     */
    public MemoryDataStore getDataStore() {
        return memory;
    }

    /**
     * Gets the geometry type.
     *
     * @return the geometry type
     */
    public GeometryTypeEnum getGeometryType() {
        return geometryType;
    }

    /**
     * Creates the sample data from the supplied schema.
     *
     * @param schema the schema
     * @param fieldList the field list
     */
    public void create(FeatureType schema, List<DataSourceAttributeData> fieldList) {
        if(schema == null)
        {
            return;
        }

        SimpleFeatureType featureType = (SimpleFeatureType) schema;
        memory = new MemoryDataStore();
        try {
            memory.createSchema(featureType);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
            memory = null;
            return;
        }
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(featureType);

        SimpleFeature feature = DataUtilities.template( featureType );

        builder.init((SimpleFeature) feature);
        int index = 0;
        for (AttributeDescriptor descriptor : featureType.getAttributeDescriptors()) {
            AttributeType attributeType = descriptor.getType();
            Object value = null;
            Class<?> fieldType = attributeType.getBinding();
            if(attributeType instanceof GeometryTypeImpl)
            {
                geometryType = GeometryTypeMapping.getGeometryType(fieldType);

                switch(geometryType)
                {
                case POLYGON:
                    ExamplePolygonInterface examplePolygon = DataSourceFactory.createExamplePolygon(null);
                    value = examplePolygon.getPolygon();
                    break;
                case LINE:
                    ExampleLineInterface exampleLine = DataSourceFactory.createExampleLine(null);
                    value = exampleLine.getLine();
                    break;
                case POINT:
                default:
                    ExamplePointInterface examplePoint = DataSourceFactory.createExamplePoint(null);
                    value = examplePoint.getPoint();
                    break;
                }
            }
            else
            {
                if((fieldList != null) && (index < fieldList.size()))
                {
                    value = fieldList.get(index).getValue();
                }

                if(value == null)
                {
                    value = getFieldTypeValue(index, attributeType.getName().getLocalPart(), fieldType);
                }
                else
                {
                    value = fieldList.get(index).getValue();
                }
            }
            builder.add(value);
            index ++;
        }

        SimpleFeature newFeature = builder.buildFeature("1234");
        memory.addFeature(newFeature);
    }

    /**
     * Gets the field type value.
     *
     * @param index the index
     * @param name the name
     * @param fieldType the field type
     * @return the field type value
     */
    public static Object getFieldTypeValue(int index, String name, Class<?> fieldType)
    {
        Object value = null;

        if(fieldType == String.class)
        {
            if(name != null)
            {
                value = name;
            }
        }
        else if(fieldType == Long.class)
        {
            value = Long.valueOf(index);
        }
        else if(fieldType == Integer.class)
        {
            value = Integer.valueOf(index);
        }
        else if(fieldType == Double.class)
        {
            value = Double.valueOf(index);
        }
        else if(fieldType == Float.class)
        {
            value = Float.valueOf(index);
        }
        else if(fieldType == Short.class)
        {
            value = Short.valueOf((short)index);
        }
        return value;
    }
}
