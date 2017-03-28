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

import java.util.HashMap;
import java.util.Map;

import org.geotools.coverage.grid.GridCoverage2D;

import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The Class GeometryTypeMapping.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeometryTypeMapping {

    /** The geometry map. */
    private static Map<Class<?>, GeometryTypeEnum> geometryMap =
            new HashMap<Class<?>, GeometryTypeEnum>();

    /**
     * Populate member data.
     */
    private static void populate() {
        geometryMap.put(Point.class, GeometryTypeEnum.POINT);
        geometryMap.put(MultiPoint.class, GeometryTypeEnum.POINT);
        geometryMap.put(LineString.class, GeometryTypeEnum.LINE);
        geometryMap.put(MultiLineString.class, GeometryTypeEnum.LINE);
        geometryMap.put(Polygon.class, GeometryTypeEnum.POLYGON);
        geometryMap.put(MultiPolygon.class, GeometryTypeEnum.POLYGON);
        geometryMap.put(GridCoverage2D.class, GeometryTypeEnum.RASTER);
    }

    /**
     * Gets the geometry type for the supplied binding type.
     *
     * @param bindingType the binding type
     * @return the geometry type
     */
    public static GeometryTypeEnum getGeometryType(Class<?> bindingType) {

        GeometryTypeEnum geometryType = GeometryTypeEnum.UNKNOWN;

        if (geometryMap.isEmpty()) {
            populate();
        }

        if (geometryMap.containsKey(bindingType)) {
            geometryType = geometryMap.get(bindingType);
        }

        return geometryType;
    }
}
