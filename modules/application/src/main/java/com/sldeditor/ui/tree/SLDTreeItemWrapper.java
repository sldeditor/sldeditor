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

package com.sldeditor.ui.tree;

import org.apache.commons.lang.ObjectUtils;

/**
 * The Class SLDTreeItemWrapper, a wrapper for any class. The return string allows the
 *  differentation of object instances even if the object contents
 * are the same. Need to find the difference between 2 default text symbolizers for example.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeItemWrapper {

    /** The Constant NULL_VALUE. */
    private static final String NULL_VALUE = "<null>";

    /**
     * Generate a unique key.
     *
     * @param sldItem the sld item
     * @return the string
     */
    public static String generateKey(Object sldItem) {
        if (sldItem == null) {
            return NULL_VALUE;
        }
        return ObjectUtils.identityToString(sldItem);
    }

}
