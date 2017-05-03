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

package com.sldeditor.test.unit.common.vendoroption.selection;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.selection.VendorOptionMenuUtils;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * Unit test for VendorOptionMenuUtils.
 * 
 * <p>
 * {@link com.sldeditor.common.vendoroption.selection.VendorOptionMenuUtils}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class VendorOptionMenuUtilsTest {

    /**
     * Test method for
     * {@link com.sldeditor.common.vendoroption.selection.VendorOptionMenuUtils#createMenu(java.util.List)}.
     */
    @Test
    public void testCreateMenu() {
        VendorOptionMenuUtils.createMenu(null);

        List<VersionData> listVersionData = new ArrayList<VersionData>();
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.3.1"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.2.0"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.3.99"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.4.5"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.4.1"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.3.3"));
        listVersionData.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        // Expecting:
        // Not Set
        // 1.2.0
        // 1.3.x - 1.3.1, 1.3.3, 1.3.99
        // 1.4.x - 1.4.1, 1.4.5
        List<ValueComboBoxDataGroup> actual = VendorOptionMenuUtils.createMenu(listVersionData);

        assertEquals(4, actual.size());

        // Repeat, should be cached
        actual = VendorOptionMenuUtils.createMenu(listVersionData);

        assertEquals(4, actual.size());
    }

    /**
     * Test method for
     * {@link com.sldeditor.common.vendoroption.selection.VendorOptionMenuUtils#setSelected(com.sldeditor.ui.menucombobox.MenuComboBox, com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testSetSelected() {
        VendorOptionMenuUtils.setSelected(null, null);

        List<VersionData> listVersionData = new ArrayList<VersionData>();
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.3.1"));
        VersionData expectedVersionData1 = VersionData.decode(GeoServerVendorOption.class, "1.2.0");
        listVersionData.add(expectedVersionData1);
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.3.99"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.4.5"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.4.1"));
        listVersionData.add(VersionData.decode(GeoServerVendorOption.class, "1.3.3"));
        listVersionData.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());

        // Expecting:
        // Not Set
        // 1.2.0
        // 1.3.x - 1.3.1, 1.3.3, 1.3.99
        // 1.4.x - 1.4.1, 1.4.5
        @SuppressWarnings("unused")
        List<ValueComboBoxDataGroup> actual = VendorOptionMenuUtils.createMenu(listVersionData);

        VendorOptionMenuUtils.setSelected(null, expectedVersionData1);
        VendorOptionMenuUtils.setSelected(null,
                VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
    }

}
