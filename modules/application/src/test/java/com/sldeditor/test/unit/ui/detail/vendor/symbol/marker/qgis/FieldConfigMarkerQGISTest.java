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

package com.sldeditor.test.unit.ui.detail.vendor.symbol.marker.qgis;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.vendor.geoserver.marker.qgis.FieldConfigMarkerQGIS;

/**
 * The unit test for FieldConfigMarkerQGIS.
 * <p>{@link com.sldeditor.ui.detail.vendor.geoserver.marker.qgis.FieldConfigMarkerQGIS}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigMarkerQGISTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.vendor.geoserver.marker.qgis.FieldConfigMarkerQGIS#FieldConfigMarkerQGIS(com.sldeditor.ui.detail.config.FieldConfigCommonData, com.sldeditor.ui.detail.ColourFieldConfig, com.sldeditor.ui.detail.ColourFieldConfig, com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testFieldConfigMarkerShape() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigMarkerQGIS field = new FieldConfigMarkerQGIS(new FieldConfigCommonData(String.class, FieldIdEnum.NAME, "test label", valueOnly), null, null, null);

        assertNotNull(field);
    }

}
