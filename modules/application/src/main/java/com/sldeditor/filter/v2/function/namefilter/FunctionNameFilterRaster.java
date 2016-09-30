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

package com.sldeditor.filter.v2.function.namefilter;

import java.util.Arrays;
import java.util.List;

import org.opengis.filter.capability.FunctionName;
import org.opengis.parameter.Parameter;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;

/**
 * The Class FunctionNameFilterRaster.  Accept on functions suitable for raster symbols.
 * Ignore functions which return geometry or contain geometry parameters.
 *
 * @author Robert Ward (SCISYS)
 */
public class FunctionNameFilterRaster implements FunctionNameFilterInterface {

    /** The allowed geometry types. */
    private static Class<?>[] allowedGeometryTypes = {Geometry.class, LineString.class, Point.class, MultiPoint.class, LinearRing.class, Object.class};

    /** The allowed geometry type list. */
    private List<Class<?>> allowedGeometryTypeList = Arrays.asList(allowedGeometryTypes);

    /** The disallowed function names. */
    private static String[] disallowedFunctionNames = {"id", "property"};

    /** The disallowed function name list. */
    private List<String> disallowedFunctionNameList = Arrays.asList(disallowedFunctionNames);

    /**
     * Instantiates a new function name filter raster.
     */
    public FunctionNameFilterRaster()
    {
    }

    /* (non-Javadoc)
     * @see com.sldeditor.filter.v2.function.namefilter.FunctionNameFilterInterface#accept(org.opengis.filter.capability.FunctionName)
     */
    @Override
    public boolean accept(FunctionName functionName) {
        if(functionName != null)
        {
            // Check return type
            if(allowedGeometryTypeList.contains(functionName.getReturn().getType()))
            {
                return false;
            }

            // Check all parameters
            for(Parameter<?> parameter : functionName.getArguments())
            {
                if(allowedGeometryTypeList.contains(parameter.getType()))
                {
                    return false;
                }
            }

            // Check to see if function name is on the banned list
            if(disallowedFunctionNameList.contains(functionName.getName()))
            {
                return false;
            }
        }
        return true;
    }
}
