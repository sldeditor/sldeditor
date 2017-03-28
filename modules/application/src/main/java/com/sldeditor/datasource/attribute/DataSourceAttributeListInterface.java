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

import java.util.List;

/**
 * The Interface DataSourceAttributeListInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface DataSourceAttributeListInterface {

    /**
     * Gets the list of attribute data.
     *
     * @return the attribute data data
     */
    public abstract List<DataSourceAttributeData> getData();

    /**
     * Sets the attribute data.
     *
     * @param attributeList the attribute list
     */
    public abstract void setData(List<DataSourceAttributeData> attributeList);
}
