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

package com.sldeditor.create;

import com.sldeditor.create.sld.NewLineSLD;
import com.sldeditor.create.sld.NewPointSLD;
import com.sldeditor.create.sld.NewPolygonSLD;
import com.sldeditor.create.sld.NewRasterSLD;
import com.sldeditor.create.sld.NewSLDInterface;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A factory for creating NewSLD objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class NewSLDFactory {

    /** The available map. */
    private static Map<String, NewSLDInterface> availableMap =
            new LinkedHashMap<String, NewSLDInterface>();

    /** Instantiates a new NewSLDFactory. */
    public NewSLDFactory() {}

    /**
     * Gets the available New SLD options.
     *
     * @return the available
     */
    public static Map<String, NewSLDInterface> getAvailable() {
        if (availableMap.isEmpty()) {
            List<NewSLDInterface> list = populateAvailableList();

            for (NewSLDInterface obj : list) {
                availableMap.put(obj.getName(), obj);
            }
        }

        return availableMap;
    }

    /**
     * Populate available list of SLDs.
     *
     * @return the list
     */
    private static List<NewSLDInterface> populateAvailableList() {
        List<NewSLDInterface> list = new ArrayList<NewSLDInterface>();

        list.add(new NewPointSLD());
        list.add(new NewLineSLD());
        list.add(new NewPolygonSLD());
        list.add(new NewRasterSLD());

        return list;
    }
}
