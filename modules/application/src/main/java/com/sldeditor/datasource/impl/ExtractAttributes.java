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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FunctionExpression;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.filter.LogicFilterImpl;
import org.geotools.filter.MultiCompareFilterImpl;
import org.geotools.filter.NotImpl;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.BinarySpatialOperator;
import org.opengis.filter.temporal.BinaryTemporalOperator;
import org.opengis.parameter.Parameter;

import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Class that extracts all data source fields from an SLD file.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtractAttributes extends DuplicatingStyleVisitor {

    /** The processed field list. */
    private List<DataSourceAttributeData> processedFieldList = new ArrayList<DataSourceAttributeData>();

    /** The field list. */
    private Map<String, DataSourceAttributeData> fieldList = new HashMap<String, DataSourceAttributeData>();

    /** The geometry field list. */
    private List<String> geometryFieldList = new ArrayList<String>();

    /** The geometry factory. */
    private static GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );

    /** The WKTReader. */
    private static WKTReader reader = new WKTReader( geometryFactory );

    /**
     * Instantiates a new extract expressions.
     */
    public ExtractAttributes()
    {
        super();
    }

    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#copy(org.opengis.filter.expression.Expression)
     */
    protected Expression copy( Expression expression )
    {
        return copy(String.class, expression);
    }

    /**
     * Copy.
     *
     * @param attributeType the attribute type
     * @param expression the expression
     * @return the expression
     */
    private Expression copy(Class<?> attributeType, Expression expression )
    {
        List<String> foundList = new ArrayList<String>();
        extractAttribute(attributeType, expression, foundList);
        return super.copy(expression);
    }

    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.PointSymbolizer)
     */
    public void visit(PointSymbolizer ps) {
        PointSymbolizer copy = sf.getDefaultPointSymbolizer();

        copy.setGeometry(copy(Point.class, ps.getGeometry()));

        copy.setUnitOfMeasure(ps.getUnitOfMeasure());
        copy.setGraphic( copy( ps.getGraphic() ));
        copy.getOptions().putAll(ps.getOptions());

        if( STRICT ){
            if( !copy.equals( ps )){
                throw new IllegalStateException("Was unable to duplicate provided Graphic:"+ps );
            }
        }
        pages.push(copy);
    }

    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.LineSymbolizer)
     */
    public void visit(LineSymbolizer line) {
        LineSymbolizer copy = sf.getDefaultLineSymbolizer();

        copy.setGeometry(copy(LineString.class, line.getGeometry()));

        copy.setUnitOfMeasure(line.getUnitOfMeasure());
        copy.setStroke( copy( line.getStroke()));
        copy.getOptions().putAll(line.getOptions());
        copy.setPerpendicularOffset(line.getPerpendicularOffset());

        if( STRICT && !copy.equals( line )){
            throw new IllegalStateException("Was unable to duplicate provided LineSymbolizer:"+line );
        }
        pages.push(copy);
    }

    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#visit(org.geotools.styling.PolygonSymbolizer)
     */
    public void visit(PolygonSymbolizer poly) {
        PolygonSymbolizer copy = sf.createPolygonSymbolizer();
        copy.setFill( copy( poly.getFill()));

        copy.setGeometry(copy(MultiPolygon.class, poly.getGeometry()));

        copy.setUnitOfMeasure(poly.getUnitOfMeasure());
        copy.setStroke(copy(poly.getStroke()));
        copy.getOptions().putAll(poly.getOptions());

        if( STRICT && !copy.equals( poly )){
            throw new IllegalStateException("Was unable to duplicate provided PolygonSymbolizer:"+poly );
        }
        pages.push(copy);
    }

    /* (non-Javadoc)
     * @see org.geotools.styling.visitor.DuplicatingStyleVisitor#copy(org.opengis.filter.Filter)
     */
    protected Filter copy( Filter filter ){
        if(filter instanceof NotImpl)
        {
            copy(((NotImpl) filter).getFilter());
        }
        else if(filter instanceof LogicFilterImpl)
        {
            LogicFilterImpl logicFilter = (LogicFilterImpl) filter;
            for(Filter childFilter : logicFilter.getChildren())
            {
                copy(childFilter);
            }
        }
        else if(filter instanceof MultiCompareFilterImpl)
        {
            MultiCompareFilterImpl multiCompareFilter = (MultiCompareFilterImpl) filter;
            List<String> foundList1 = new ArrayList<String>();
            Class<?> returnType1 = extractAttribute(String.class, multiCompareFilter.getExpression1(), foundList1);
            List<String> foundList2 = new ArrayList<String>();
            Class<?> returnType2 = extractAttribute(String.class, multiCompareFilter.getExpression2(), foundList2);

            determineTypeFromExpressions(foundList1, returnType1, foundList2, returnType2);
        }
        else if(filter instanceof BinaryTemporalOperator)
        {
            BinaryTemporalOperator binaryTemporalOperator = (BinaryTemporalOperator) filter;
            List<String> foundList1 = new ArrayList<String>();
            Class<?> returnType1 = extractAttribute(String.class, binaryTemporalOperator.getExpression1(), foundList1);
            List<String> foundList2 = new ArrayList<String>();
            Class<?> returnType2 = extractAttribute(String.class, binaryTemporalOperator.getExpression2(), foundList2);

            determineTypeFromExpressions(foundList1, returnType1, foundList2, returnType2);
        }
        else if(filter instanceof BinarySpatialOperator)
        {
            BinarySpatialOperator binarySpatialOperator = (BinarySpatialOperator) filter;
            List<String> foundList1 = new ArrayList<String>();
            Class<?> returnType1 = extractAttribute(String.class, binarySpatialOperator.getExpression1(), foundList1);
            List<String> foundList2 = new ArrayList<String>();
            Class<?> returnType2 = extractAttribute(String.class, binarySpatialOperator.getExpression2(), foundList2);

            determineTypeFromExpressions(foundList1, returnType1, foundList2, returnType2);
        }
        else if(filter instanceof PropertyIsBetween)
        {
            PropertyIsBetween isBetween = (PropertyIsBetween) filter;
            List<String> foundList1 = new ArrayList<String>();
            Class<?> returnType1 = extractAttribute(String.class, isBetween.getLowerBoundary(), foundList1);
            List<String> foundList2 = new ArrayList<String>();
            Class<?> returnType2 = extractAttribute(String.class, isBetween.getExpression(), foundList2);
            List<String> foundList3 = new ArrayList<String>();
            Class<?> returnType3 = extractAttribute(String.class, isBetween.getUpperBoundary(), foundList3);

            determineTypeFromExpressions(foundList1, returnType1, foundList2, returnType2, foundList3, returnType3);
        }
        else if(filter instanceof PropertyIsNull)
        {
            PropertyIsNull isNull = (PropertyIsNull) filter;
            List<String> foundList1 = new ArrayList<String>();
            extractAttribute(String.class, isNull.getExpression(), foundList1);
        }
        else if(filter instanceof PropertyIsLike)
        {
            PropertyIsLike isLike = (PropertyIsLike) filter;
            List<String> foundList1 = new ArrayList<String>();
            extractAttribute(String.class, isLike.getExpression(), foundList1);
        }
        return super.copy(filter);
    }

    /**
     * Determine type from expressions.
     *
     * @param foundList1 the found list 1
     * @param returnType1 the return type 1
     * @param foundList2 the found list 2
     * @param returnType2 the return type 2
     */
    private void determineTypeFromExpressions(List<String> foundList1, Class<?> returnType1,
            List<String> foundList2, Class<?> returnType2) {
        List<List<String>> foundList = new ArrayList<List<String>>();
        foundList.add(foundList1);
        foundList.add(foundList2);

        List< Class<?>> returnTypeList = new ArrayList<Class<?>>();
        returnTypeList.add(returnType1);
        returnTypeList.add(returnType2);

        determineTypeFromExpressions(foundList, returnTypeList);
    }

    /**
     * Determine type from expressions.
     *
     * @param foundList1 the found list 1
     * @param returnType1 the return type 1
     * @param foundList2 the found list 2
     * @param returnType2 the return type 2
     * @param foundList3 the found list 3
     * @param returnType3 the return type 3
     */
    private void determineTypeFromExpressions(List<String> foundList1, Class<?> returnType1,
            List<String> foundList2, Class<?> returnType2,
            List<String> foundList3, Class<?> returnType3) {
        List<List<String>> foundList = new ArrayList<List<String>>();
        foundList.add(foundList1);
        foundList.add(foundList2);
        foundList.add(foundList3);

        List< Class<?>> returnTypeList = new ArrayList<Class<?>>();
        returnTypeList.add(returnType1);
        returnTypeList.add(returnType2);
        returnTypeList.add(returnType3);

        determineTypeFromExpressions(foundList, returnTypeList);
    }

    /**
     * Determine type from expressions.
     *
     * @param foundList the found list
     * @param returnTypeList the return type list
     */
    private void determineTypeFromExpressions(List<List<String>> foundList, List<Class<?>> returnTypeList) {
        int index = 0;
        for(Class<?> returnType : returnTypeList)
        {
            if((returnType != null) && (returnType != String.class))
            {
                List<String> fieldNameList = new ArrayList<String>();
                int childIndex = 0;
                for(List<String> expressionFieldList : foundList)
                {
                    if((index != childIndex) && (expressionFieldList != null))
                    {
                        fieldNameList.addAll(expressionFieldList);
                    }
                    childIndex ++;
                }
                updateFieldType(fieldNameList, returnType);
            }

            index ++;
        }
    }

    /**
     * Update field type.
     *
     * @param foundList the found list
     * @param returnType the return type
     */
    private void updateFieldType(List<String> foundList, Class<?> returnType)
    {
        if(foundList != null)
        {
            for(String fieldName : foundList)
            {
                DataSourceAttributeData data = fieldList.get(fieldName);
                if(data != null)
                {
                    if(!((data.getType() == Double.class) && (returnType == Integer.class)))
                    {
                        data.setType(returnType);
                    }
                }
            }
        }
    }

    /**
     * Extract attribute.
     *
     * @param attributeType the attribute type
     * @param expression the expression
     * @param foundList the found list
     * @return the class
     */
    protected Class<?> extractAttribute(Class<?> attributeType, Expression expression, List<String> foundList) {
        Class<?> returnType = String.class;

        if(expression instanceof AttributeExpressionImpl)
        {
            AttributeExpressionImpl attribute = (AttributeExpressionImpl) expression;
            // Determine if attribute is a geometry
            if((GeometryTypeMapping.getGeometryType(attributeType) != GeometryTypeEnum.UNKNOWN) || (attributeType == Geometry.class))
            {
                if(!geometryFieldList.contains(attribute.getPropertyName()))
                {
                    geometryFieldList.add(attribute.getPropertyName());
                }
            }
            else
            {
                if(!fieldList.containsKey(attribute.getPropertyName()))
                {
                    DataSourceAttributeData field = new DataSourceAttributeData(attribute.getPropertyName(),
                            attributeType, null);
                    processedFieldList.add(field);
                    fieldList.put(attribute.getPropertyName(), field);
                    foundList.add(attribute.getPropertyName());
                }
            }
        }
        else if(expression instanceof FunctionExpression)
        {
            FunctionExpression function = (FunctionExpression) expression;
            FunctionName functionName = function.getFunctionName();
            List<Parameter<?>> argumentList = functionName.getArguments();
            int index = 0;

            for(Expression parameterExpression : function.getParameters())
            {
                Parameter<?> parameter = argumentList.get(index);
                extractAttribute(parameter.getType(), parameterExpression, foundList);
                index ++;
            }

            returnType = functionName.getReturn().getType();
        }
        else if(expression instanceof LiteralExpressionImpl)
        {
            LiteralExpressionImpl literal = (LiteralExpressionImpl) expression;
            try {
                Geometry geometry = reader.read(literal.toString());
                if(geometry != null)
                {
                    returnType = Geometry.class;
                }
            } catch (ParseException e1) {
                // Ignore
            }

            if(returnType == String.class)
            {
                try
                {
                    @SuppressWarnings("unused")
                    int integer = Integer.valueOf(literal.toString());
                    returnType = Integer.class;
                }
                catch(NumberFormatException e)
                {
                    // Ignore
                }
            }

            if(returnType == String.class)
            {
                try
                {
                    @SuppressWarnings("unused")
                    double doubleValue = Double.valueOf(literal.toString());
                    returnType = Double.class;
                }
                catch(NumberFormatException e)
                {
                    // Ignore
                }
            }
        }

        return returnType;
    }

    /**
     * Extract default fields.
     *
     * @param b the b
     * @param sld the sld
     */
    public void extractDefaultFields(SimpleFeatureTypeBuilder b, StyledLayerDescriptor sld)
    {
        visit(sld);

        // Check to see if any geometry fields have been added to processedFieldList
        List<DataSourceAttributeData> fieldsToMoveToGeometryList = new ArrayList<DataSourceAttributeData>();

        for(DataSourceAttributeData dsAttribute : processedFieldList)
        {
            if(dsAttribute.getType() == Geometry.class)
            {
                fieldsToMoveToGeometryList.add(dsAttribute);
            }
        }

        // Move geometry fields to the correct list
        for(DataSourceAttributeData ds : fieldsToMoveToGeometryList)
        {
            geometryFieldList.add(ds.getName());
            processedFieldList.remove(ds);
        }

        // Add non-geometry fields to the feature type builder
        for(DataSourceAttributeData dsAttribute : processedFieldList)
        {
            b.add(dsAttribute.getName(), dsAttribute.getType());
        }
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    public List<DataSourceAttributeData> getFields()
    {
        return processedFieldList;
    }

    /**
     * Gets the geometry fields.
     *
     * @return the geometry fields
     */
    public List<String> getGeometryFields()
    {
        return geometryFieldList;
    }
}
