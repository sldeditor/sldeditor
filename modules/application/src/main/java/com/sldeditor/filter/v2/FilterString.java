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
package com.sldeditor.filter.v2;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * Class that converts a filter from a GeoTools Filter object and converts it to
 * a string with the string literals quoted.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FilterString {

    /** The filter visitor. */
    private static ValidFilterVisitor filterVisitor = null;

    /** The filter factory. */
    private static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    /**
     * Gets the valid filter string.
     *
     * @param filter the filter
     * @return the valid filter string
     */
    public static String getValidFilterString(Filter filter) {

        if(filterVisitor == null)
        {
            filterVisitor = new ValidFilterVisitor(ff);
        }

        Filter validStringFilter = (Filter) filter.accept(filterVisitor, null);

        return validStringFilter.toString();
    }

}
