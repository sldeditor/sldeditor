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

import java.util.List;

import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Interface ExpressionFilterInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface ExpressionFilterInterface {

    /**
     * Data applied.
     */
    public void dataApplied();

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
    public List<VersionData> getVendorOptionList();
}
