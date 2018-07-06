/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.ui.detail.vendor.geoserver.raster;

import com.sldeditor.common.xml.ui.FieldIdEnum;

/**
 * Class to handle the getting and setting of red raster channel names using strings
 * 
 * @author Robert Ward (SCISYS)
 */
public class VOChannelNameRedNoExpression extends VOChannelNameNoExpression {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     * @param panelConfig the panel config
     * @param fieldId the field id
     */
    public VOChannelNameRedNoExpression(Class<?> panelId, String panelConfig, FieldIdEnum fieldId) {
        super(panelId, panelConfig, fieldId);
    }
}
