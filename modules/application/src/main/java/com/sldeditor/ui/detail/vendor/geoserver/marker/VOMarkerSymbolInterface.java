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
package com.sldeditor.ui.detail.vendor.geoserver.marker;

import java.util.List;
import java.util.Map;

import com.sldeditor.ui.detail.config.symboltype.SymbolTypeConfig;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Interface VOMarkerSymbolInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface VOMarkerSymbolInterface {

    /**
     * Adds the vendor option.
     *
     * @param symbolizerClass the symbolizer class
     * @param symbolList the symbol list
     * @param fieldEnableMap the field enable map
     * @param panelId the panel id
     */
    public abstract void addVendorOption(Class<?> symbolizerClass,
            List<ValueComboBoxData> symbolList, 
            Map<Class<?>, List<SymbolTypeConfig> > fieldEnableMap,
            Class<?> panelId);

    /**
     * Gets the field map.
     *
     * @return the field map
     */
    public abstract Map<Class<?>, List<SymbolTypeConfig>> getFieldMap();

}