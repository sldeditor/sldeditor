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

import com.sldeditor.datasource.example.ExamplePolygonInterface;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;

/**
 * Example polygon geometry to be displayed when rendering polygon symbols.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExamplePolygonImpl implements ExamplePolygonInterface {

    /** The polygon. */
    private Polygon polygon = null;

    /** Default constructor. */
    public ExamplePolygonImpl() {
        // Default constructor
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.datasource.impl.ExamplePolygonInterface#getPolygon()
     */
    @Override
    public Polygon getPolygon() {
        if (polygon == null) {
            // CHECKSTYLE:OFF
            double[][] rawLocations =
                    new double[][] {
                        {-4.49210295036, 54.4153472858},
                        {-4.4634856663, 54.4269825687},
                        {-4.43426958965, 54.4117153967},
                        {-4.40869623532, 54.4326291409},
                        {-4.32782927985, 54.4641980089},
                        {-4.3659606463, 54.4197683392},
                        {-4.33467823679, 54.4265693547},
                        {-4.32454274819, 54.4024986924},
                        {-4.34126081686, 54.3660155026},
                        {-4.3424304253, 54.3042112639},
                        {-4.37506398925, 54.3014094498},
                        {-4.41392105869, 54.2658635384},
                        {-4.44375514123, 54.2532227674},
                        {-4.44763651915, 54.196776024},
                        {-4.48315404347, 54.1850220956},
                        {-4.52311962815, 54.1455956993},
                        {-4.58362722513, 54.1091637546},
                        {-4.62431015799, 54.0527236394},
                        {-4.71452726534, 54.0188283696},
                        {-4.71863162723, 54.0497614848},
                        {-4.75157122164, 54.0647816773},
                        {-4.79755603397, 54.0685543663},
                        {-4.79717105693, 54.122792557},
                        {-4.74451711581, 54.1875314993},
                        {-4.73842361793, 54.2081776896},
                        {-4.71656215204, 54.2185876346},
                        {-4.71759940991, 54.2322672444},
                        {-4.73514361565, 54.2446507516},
                        {-4.69488449392, 54.2771110727},
                        {-4.65558887927, 54.2914459801},
                        {-4.65220617099, 54.3116519242},
                        {-4.63949760848, 54.3400051903},
                        {-4.58879948143, 54.3629767901},
                        {-4.57315512904, 54.3829958979},
                        {-4.54023908795, 54.387968746},
                        {-4.51678123729, 54.4207829193},
                        {-4.50855200379, 54.405875113},
                        {-4.49210295036, 54.4153472858},
                    };
            // CHECKSTYLE:ON

            GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

            Coordinate[] coords = new Coordinate[rawLocations.length];
            int index = 0;
            for (double[] point : rawLocations) {
                Coordinate c = new Coordinate(point[0], point[1]);

                coords[index] = c;

                index++;
            }

            LinearRing ring = geometryFactory.createLinearRing(coords);
            LinearRing holes[] = null; // use LinearRing[] to represent holes
            polygon = geometryFactory.createPolygon(ring, holes);
        }
        return polygon;
    }
}
