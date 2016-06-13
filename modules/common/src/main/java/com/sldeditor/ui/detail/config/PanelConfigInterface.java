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
package com.sldeditor.ui.detail.config;

import java.util.List;
import java.util.Map;

import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;

/**
 * The Interface PanelConfigInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface PanelConfigInterface {

    /**
     * Gets the panel title.
     *
     * @return the panel title
     */
    public String getPanelTitle();
    
    /**
     * Gets the vendor option version read from the configuration.
     *
     * @return the vendor option version
     */
    VendorOptionVersion getVendorOptionVersion();

    /**
     * Gets the group list read from the configuration.
     *
     * @return the group list
     */
    List<GroupConfigInterface> getGroupList();

    /**
     * Gets the default field map.
     *
     * @return the defaultFieldMap
     */
    Map<FieldId, Object> getDefaultFieldMap();
}
