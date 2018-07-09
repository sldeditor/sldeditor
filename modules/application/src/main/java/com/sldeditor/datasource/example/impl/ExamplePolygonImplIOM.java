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
 * Example polygon geometry of the Isle Of Man to be displayed when rendering polygon symbols.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExamplePolygonImplIOM implements ExamplePolygonInterface {

    /** The polygon. */
    private Polygon polygon = null;

    /** Instantiates a new example polygon impl iom. */
    public ExamplePolygonImplIOM() {}

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
                        {-4.652710, 54.069059},
                        {-4.634857, 54.075506},
                        {-4.629364, 54.059388},
                        {-4.600525, 54.087590},
                        {-4.574432, 54.102892},
                        {-4.548340, 54.103697},
                        {-4.522247, 54.124626},
                        {-4.476929, 54.143132},
                        {-4.470062, 54.162434},
                        {-4.428864, 54.169670},
                        {-4.383545, 54.194583},
                        {-4.398651, 54.209846},
                        {-4.397278, 54.223496},
                        {-4.373932, 54.229919},
                        {-4.364319, 54.249180},
                        {-4.301147, 54.303704},
                        {-4.372559, 54.315722},
                        {-4.380798, 54.344550},
                        {-4.365692, 54.389354},
                        {-4.364319, 54.420528},
                        {-4.459076, 54.402946},
                        {-4.534607, 54.373359},
                        {-4.578552, 54.322931},
                        {-4.601898, 54.285270},
                        {-4.636230, 54.258807},
                        {-4.671936, 54.237143},
                        {-4.703522, 54.229919},
                        {-4.728241, 54.187352},
                        {-4.743347, 54.173689},
                        {-4.735107, 54.143132},
                        {-4.755707, 54.110138},
                        {-4.783173, 54.101281},
                        {-4.777679, 54.086784},
                        {-4.822998, 54.049714},
                        {-4.737854, 54.066642},
                        {-4.709015, 54.082757},
                        {-4.682922, 54.062612},
                        {-4.652710, 54.069059},
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
