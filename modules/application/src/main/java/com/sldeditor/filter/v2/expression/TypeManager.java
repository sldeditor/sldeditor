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

package com.sldeditor.filter.v2.expression;

/**
 * The Class TypeManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class TypeManager {

    /** The singleton instance. */
    private static TypeManager instance = null;

    /** The first literal type. */
    private Class<?> firstLiteralType = null;

    /**
     * Gets the single instance of TypeManager.
     *
     * @return single instance of TypeManager
     */
    public static TypeManager getInstance() {
        if (instance == null) {
            instance = new TypeManager();
        }

        return instance;
    }

    /**
     * Reset the literal data type.
     */
    public void reset() {
        firstLiteralType = null;
    }

    /**
     * Sets the data type.
     *
     * @param dataType the new literal type
     */
    public void setDataType(Class<?> dataType) {
        if (firstLiteralType == null) {
            firstLiteralType = dataType;
        }
    }

    /**
     * Gets the data type.
     *
     * @return the data type
     */
    public Class<?> getDataType() {
        return firstLiteralType;
    }
}
