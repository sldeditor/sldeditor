/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.datasource.checks;

import java.util.ArrayList;
import java.util.List;

/**
 * A factory for creating objects to check the attributes in a data source.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckAttributeFactory {
    
    /** The check list. */
    private static List<CheckAttributeInterface> checkList = new ArrayList<CheckAttributeInterface>();

    /**
     * Gets the check list.
     *
     * @return the checkList
     */
    public static List<CheckAttributeInterface> getCheckList() {
        if(checkList.isEmpty())
        {
            checkList.add(new MissingSLDAttributes());
        }
        return checkList;
    }

    /**
     * Sets the overide check list.
     *
     * @param checkList the checkList to set
     */
    public static void setOverideCheckList(List<CheckAttributeInterface> checkList) {
        CheckAttributeFactory.checkList = checkList;
    }
}
