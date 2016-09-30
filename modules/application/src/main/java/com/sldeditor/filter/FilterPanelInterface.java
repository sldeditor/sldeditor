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
package com.sldeditor.filter;

import org.opengis.filter.Filter;

/**
 * The Interface FilterPanelInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface FilterPanelInterface {

    /**
     * Configure the panel.
     *
     * @param title the title
     * @param fieldType the field type
     * @param isRasterSymbol the is raster symbol flag
     */
    public void configure(String title, Class<?> fieldType, boolean isRasterSymbol);

    /**
     * Show dialog.
     *
     * @return true, if Ok button pressed
     */
    boolean showDialog();

    /**
     * Populate the filter dialog.
     *
     * @param filter the filter to populate
     */
    void populate(Filter filter);

    /**
     * Gets the filter string.
     *
     * @return the filterString
     */
    public String getFilterString();

    /**
     * Gets the filter.
     *
     * @return the filter
     */
    public Filter getFilter();

}
