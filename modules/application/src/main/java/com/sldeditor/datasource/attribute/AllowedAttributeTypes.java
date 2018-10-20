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
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * The Class AllowedAttributeTypes determines whether for a data source field can be used for a
 * variable type.
 *
 * <p>For example if a data source field type was an integer then if a sld attribute type is a
 * double then it is a match. The field could be used in the attribute drop down list for an sld
 * attribute.
 *
 * <table border="1">
 * <tr>
 * <td>Data Source Type</td>
 * <td>Valid SLD Attribute Types</td>
 * </tr>
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
    private static Map<Class<?>, List<Class<?>>> allowedClassTypeMap = new HashMap<>();

    /** Private default constructor */
    private AllowedAttributeTypes() {
        // Private default constructor
    }

    /** Initialise. */
    private static void initialise() {
        List<Class<?>> doubleList =
                new ArrayList<>(
                        Arrays.asList(
                                Integer.class,
                                Long.class,
                                Double.class,
                                Float.class,
                                Object.class));
        List<Class<?>> integerList =
                new ArrayList<>(Arrays.asList(Integer.class, Long.class, Object.class));
        List<Class<?>> stringList = new ArrayList<>(Arrays.asList(String.class));
        List<Class<?>> geometryList =
                new ArrayList<>(
                        Arrays.asList(
                                Point.class,
                                LineString.class,
                                Polygon.class,
                                MultiPolygon.class,
                                MultiPoint.class,
                                MultiLineString.class,
                                GridCoverage2D.class,
                                Object.class));
        List<Class<?>> lineStringList =
                new ArrayList<>(
                        Arrays.asList(LineString.class, MultiLineString.class, Object.class));
        List<Class<?>> pointList =
                new ArrayList<>(Arrays.asList(Point.class, MultiPoint.class, Object.class));
        List<Class<?>> linearRingList = new ArrayList<>(Arrays.asList(LinearRing.class));
        List<Class<?>> rangedClassifierList =
                new ArrayList<>(Arrays.asList(RangedClassifier.class, Object.class));
        List<Class<?>> classifierList =
                new ArrayList<>(Arrays.asList(Classifier.class, Object.class));
        List<Class<?>> rasterGeometryList =
                new ArrayList<>(Arrays.asList(GridCoverage2D.class, Object.class));
        List<Class<?>> referencedEnvelopeList =
                new ArrayList<>(Arrays.asList(ReferencedEnvelope.class, Object.class));

        allowedClassTypeMap.put(String.class, stringList);
        allowedClassTypeMap.put(Double.class, doubleList);
        allowedClassTypeMap.put(Float.class, doubleList);
        allowedClassTypeMap.put(Integer.class, integerList);
        allowedClassTypeMap.put(Long.class, integerList);
        allowedClassTypeMap.put(Geometry.class, geometryList);
        allowedClassTypeMap.put(LineString.class, lineStringList);
        allowedClassTypeMap.put(Point.class, pointList);
        allowedClassTypeMap.put(LinearRing.class, linearRingList);
        allowedClassTypeMap.put(RangedClassifier.class, rangedClassifierList);
        allowedClassTypeMap.put(Classifier.class, classifierList);
        allowedClassTypeMap.put(GridCoverage2D.class, rasterGeometryList);
        allowedClassTypeMap.put(ReferencedEnvelope.class, referencedEnvelopeList);

        List<Class<?>> objectList = new ArrayList<>();
        objectList.addAll(doubleList);
        objectList.addAll(integerList);
        objectList.addAll(stringList);
        objectList.addAll(geometryList);
        objectList.addAll(lineStringList);
        objectList.addAll(rasterGeometryList);
        objectList.addAll(referencedEnvelopeList);
        allowedClassTypeMap.put(Object.class, objectList);
    }

    /**
     * Checks if attribute type is allowed.
     *
     * @param typeToCheck the type to check
     * @param attributeType the attribute type
     * @return true, if is allowed
     */
    public static boolean isAllowed(Class<?> typeToCheck, Class<?> attributeType) {
        if (allowedClassTypeMap.isEmpty()) {
            initialise();
        }

        List<Class<?>> list = allowedClassTypeMap.get(attributeType);

        if (list == null) {
            return false;
        }
        return list.contains(typeToCheck);
    }
}
