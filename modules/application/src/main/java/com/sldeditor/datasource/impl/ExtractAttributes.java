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
import org.geotools.filter.MultiCompareFilterImpl;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;
import org.opengis.parameter.Parameter;

import com.sldeditor.datasource.attribute.DataSourceAttributeData;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;

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

    private Expression copy(Class<?> attributeType, Expression expression )
    {
        List<String> foundList = new ArrayList<String>();
        extractAttribute(attributeType, expression, foundList);
        return super.copy(expression);
    }

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

    protected Filter copy( Filter filter ){
        if(filter instanceof MultiCompareFilterImpl)
        {
            MultiCompareFilterImpl multiCompareFilter = (MultiCompareFilterImpl) filter;
            List<String> foundList1 = new ArrayList<String>();
            Class<?> returnType1 = extractAttribute(String.class, multiCompareFilter.getExpression1(), foundList1);
            List<String> foundList2 = new ArrayList<String>();
            Class<?> returnType2 = extractAttribute(String.class, multiCompareFilter.getExpression2(), foundList2);

            if(!foundList1.isEmpty() && returnType2 != String.class)
            {
                for(String fieldName : foundList1)
                {
                    DataSourceAttributeData data = fieldList.get(fieldName);
                    if(data != null)
                    {
                        data.setType(returnType2);
                    }
                }
            }
            System.out.println();
        }
        return super.copy(filter);
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
