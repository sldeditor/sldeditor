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

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class ArrowDetails panel contains all the fields to configure 
 * an GeoServer vendor option arrow strings.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EmptyDetails extends StandardPanel implements UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new feature type style details.
     *
     * @param resourceFile the resource file
     */
    public EmptyDetails(String resourceFile)
    {
        super(EmptyDetails.class);

        readConfigFileNoScrollPane(null, getClass(), this, resourceFile);
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged()
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        // Do nothing
    }

}
