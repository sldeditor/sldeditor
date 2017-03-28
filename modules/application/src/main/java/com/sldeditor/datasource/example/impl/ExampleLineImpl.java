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

import org.geotools.geometry.jts.JTSFactoryFinder;

import com.sldeditor.datasource.example.ExampleLineInterface;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * Example line geometry to be displayed when rendering line symbols.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExampleLineImpl implements ExampleLineInterface {

    /** The line. */
    private LineString line = null;

    /**
     * Instantiates a new example line impl.
     */
    public ExampleLineImpl() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.impl.ExampleLineInterface#getLine()
     */
    @Override
    public LineString getLine() {
        if (line == null) {
            double[][] rawLocations = new double[][] { { -123.167725, 48.502048 },
                    { -123.464355, 48.297812 }, { -124.738770, 48.603858 },
                    { -125.189209, 48.828566 }, { -125.112305, 48.951366 },
                    { -125.507812, 48.929718 }, { -125.870361, 49.145784 },
                    { -126.035156, 49.167339 }, { -126.112061, 49.253465 },
                    { -126.243896, 49.282140 }, { -126.287842, 49.360912 },
                    { -126.397705, 49.410973 }, { -126.573486, 49.375220 },
                    { -126.584473, 49.560852 }, { -126.815186, 49.610710 },
                    { -127.012939, 49.745781 }, { -126.947021, 49.788357 },
                    { -127.166748, 49.852152 }, { -127.518311, 50.113533 },
                    { -127.814941, 50.078295 }, { -127.957764, 50.120578 },
                    { -127.825928, 50.254230 }, { -128.012695, 50.331436 },
                    { -127.946777, 50.450509 }, { -128.122559, 50.457504 },
                    { -128.364258, 50.652943 }, { -128.342285, 50.792047 },
                    { -128.100586, 50.882243 }, { -127.858887, 50.944584 },
                    { -127.518311, 50.798991 }, { -127.221680, 50.639010 } };
            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

            Coordinate[] coords = new Coordinate[rawLocations.length];
            int index = 0;
            for (double[] point : rawLocations) {
                Coordinate c = new Coordinate(point[0], point[1]);

                coords[index] = c;

                index++;
            }

            line = geometryFactory.createLineString(coords);
        }
        return line;
    }

}
