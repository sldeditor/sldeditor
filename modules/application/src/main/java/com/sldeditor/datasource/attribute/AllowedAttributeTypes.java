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
package com.sldeditor.datasource.attribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The Class AllowedAttributeTypes determines whether for a data source field
 * can be used for a variable type.
 * <p>For example if a data source field type was an integer then if a sld attribute type is
 * a double then it is a match.  The field could be used in the attribute drop down list for an
 * sld attribute.
 * <table border="1">
 * <tr><td>Data Source Type</td><td>Valid SLD Attribute Types</td></tr>
 * <tr>
 * <td>Float, Double</td>
 * <td>Integer, Long, Double, Float</td>
 * </tr>
 * <tr>
 * <td>Integer, Long</td>
 * <td>Integer, Long</td>
 * </tr>
 * <tr>
 * <td>String</td>
 * <td>String</td>
 * </tr>
 * </table>
 * 
 * @author Robert Ward (SCISYS)
 */
public class AllowedAttributeTypes {

    /** The allowed class type map. */
    private static Map<Class<?>, List<Class<?> > > allowedClassTypeMap = new HashMap<Class<?>, List<Class<?> > >();

    /**
     * Initialise.
     */
    private static void initialise()
    {
        List<Class<?> > doubleList = new ArrayList<Class<?> >(Arrays.asList(Integer.class, Long.class, Double.class, Float.class));
        List<Class<?> > integerList = new ArrayList<Class<?> >(Arrays.asList(Integer.class, Long.class));
        List<Class<?> > stringList = new ArrayList<Class<?> >(Arrays.asList(String.class));
        List<Class<?> > geometryList = new ArrayList<Class<?> >(Arrays.asList(Point.class, LineString.class, Polygon.class, MultiPolygon.class, MultiPoint.class, MultiLineString.class));

        allowedClassTypeMap.put(String.class, stringList);
        allowedClassTypeMap.put(Double.class, doubleList);
        allowedClassTypeMap.put(Float.class, doubleList);
        allowedClassTypeMap.put(Integer.class, integerList);
        allowedClassTypeMap.put(Long.class, integerList);
        allowedClassTypeMap.put(Geometry.class, geometryList);

        List<Class<?> > objectList = new ArrayList<Class<?>>();
        objectList.addAll(doubleList);
        objectList.addAll(integerList);
        objectList.addAll(stringList);
        objectList.addAll(geometryList);
        allowedClassTypeMap.put(Object.class, objectList);
    }

    /**
     * Checks if attribute type is allowed.
     *
     * @param typeToCheck the type to check
     * @param attributeType the attribute type
     * @return true, if is allowed
     */
    public static boolean isAllowed(Class<?> typeToCheck, Class<?> attributeType)
    {
        if(allowedClassTypeMap.isEmpty())
        {
            initialise();
        }

        List<Class<?> > list = allowedClassTypeMap.get(attributeType);

        if(list == null)
        {
            return false;
        }
        return list.contains(typeToCheck);
    }
}
