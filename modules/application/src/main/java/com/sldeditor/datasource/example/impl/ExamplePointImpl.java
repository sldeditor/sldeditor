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

package com.sldeditor.datasource.example.impl;

import com.sldeditor.datasource.example.ExamplePointInterface;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Example point geometry to be displayed when rendering point symbols.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExamplePointImpl implements ExamplePointInterface {

    /** The geometry factory. */
    private GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

    /** The point. */
    private Point point = null;

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.impl.ExamplePointInterface#getPoint()
     */
    @Override
    public Point getPoint() {

        if (point == null) {
            double longitude = -2.11938;
            double latitude = 51.46518;
            point = geometryFactory.createPoint(new Coordinate(longitude, latitude));
        }
        return point;
    }
}
