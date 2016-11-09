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

package com.sldeditor.test.unit.common.preferences;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.VendorOptionTableModel;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * Unit test for VendorOptionTableModel.
 * <p>{@link com.sldeditor.common.preferences.VendorOptionTableModel}
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionTableModelTest {

    /**
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getColumnCount()}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getColumnName(int)}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getColumnClass(int)}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#isCellEditable(int, int)}.
     */
    @Test
    public void testGetColumnCount() {
        VendorOptionTableModel model = new VendorOptionTableModel(null);

        assertEquals(2, model.getColumnCount());
        assertEquals(Localisation.getString(VendorOptionTableModel.class, "VendorOptionTableModel.vendor"), model.getColumnName(0));
        assertEquals(Localisation.getString(VendorOptionTableModel.class, "VendorOptionTableModel.selected"), model.getColumnName(1));

        assertEquals(String.class, model.getColumnClass(1));
        assertEquals(Object.class, model.getColumnClass(0));

        assertEquals(0, model.getRowCount());

        assertFalse(model.isCellEditable(0, 0));
        assertTrue(model.isCellEditable(0, 1));
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getValueAt(int, int)}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getRowCount()}.
     */
    @Test
    public void testGetValueAt() {
        Map<VendorOptionTypeInterface, String> options = new LinkedHashMap<VendorOptionTypeInterface, String>();
        VendorOptionTypeInterface vendorOption = VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class);

        options.put(vendorOption, vendorOption.getName());

        VendorOptionTableModel model = new VendorOptionTableModel(options);

        assertEquals(1, model.getRowCount());
        String actualValue1 = (String) model.getValueAt(0, 0);
        assertEquals(vendorOption.getName(), actualValue1);

        VersionData actualValue2 = (VersionData) model.getValueAt(0, 1);
        VersionData latest = VersionData.getLatestVersion(GeoServerVendorOption.class);
        assertEquals(latest.getVersionString(), actualValue2.getVersionString());

        assertNull(model.getValueAt(0, -1));
        assertNull(model.getValueAt(0, 5));
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#setValueAt(java.lang.Object, int, int)}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#setSelectedVersion(com.sldeditor.common.vendoroption.VersionData, int)}.
     */
    @Test
    public void testSetValueAtObjectIntInt() {
        Map<VendorOptionTypeInterface, String> options = new LinkedHashMap<VendorOptionTypeInterface, String>();
        VendorOptionTypeInterface vendorOption = VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class);

        options.put(vendorOption, vendorOption.getName());

        VendorOptionTableModel model = new VendorOptionTableModel(options);

        assertEquals(1, model.getRowCount());

        VersionData latest = VersionData.getLatestVersion(GeoServerVendorOption.class);
        VersionData earliest = VersionData.getEarliestVersion(GeoServerVendorOption.class);
        // Invalid column
        model.setValueAt(earliest, 0, 0);

        VersionData actual = (VersionData) model.getValueAt(0, 1);
        assertEquals(latest.getVersionString(), actual.getVersionString());

        // Invalid row
        model.setValueAt(earliest, -1, 1);
        actual = (VersionData) model.getValueAt(0, 1);
        assertEquals(latest.getVersionString(), actual.getVersionString());

        model.setValueAt(earliest, 1, 1);
        actual = (VersionData) model.getValueAt(0, 1);
        assertEquals(latest.getVersionString(), actual.getVersionString());

        // Valid row
        model.setValueAt(earliest, 0, 1);
        actual = (VersionData) model.getValueAt(0, 1);
        assertEquals(earliest.getVersionString(), actual.getVersionString());

        // Try setSelectedVersion
        model.setSelectedVersion(latest, 0);
        actual = (VersionData) model.getValueAt(0, 1);
        assertEquals(latest.getVersionString(), actual.getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getVendorOption(int)}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#setSelectedVendorOptionVersions(java.util.List)}.
     * Test method for {@link com.sldeditor.common.preferences.VendorOptionTableModel#getVendorOptionVersionList()}.
     */
    @Test
    public void testGetVendorOption() {
        Map<VendorOptionTypeInterface, String> options = new LinkedHashMap<VendorOptionTypeInterface, String>();
        VendorOptionTypeInterface vendorOption = VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class);

        options.put(vendorOption, vendorOption.getName());

        VendorOptionTableModel model = new VendorOptionTableModel(options);

        assertEquals(1, model.getRowCount());

        // Try invalid calls to getVendorOption
        assertNull(model.getVendorOption(-1));
        assertNull(model.getVendorOption(1));

        // Valid call to getVendorOption
        List<VersionData> actualList = model.getVendorOption(0);
        assertEquals(vendorOption.getVersionList().size(), actualList.size());
        assertEquals("Not Set", actualList.get(0).getVersionString());

        model.setSelectedVendorOptionVersions(null);

        List<VersionData> selectedVersionList = new ArrayList<VersionData>();
        VersionData expectedVersionData = vendorOption.getVersionList().get(1);
        selectedVersionList.add(expectedVersionData);
        model.setSelectedVendorOptionVersions(selectedVersionList);

        actualList = model.getVendorOptionVersionList();
        assertEquals(2, actualList.size());
        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData(), actualList.get(0));
        assertEquals(expectedVersionData.getVersionString(), actualList.get(1).getVersionString());

        // Try with unknown
        selectedVersionList = new ArrayList<VersionData>();
        expectedVersionData = VersionData.getEarliestVersion(Double.class);
        selectedVersionList.add(expectedVersionData);
        model.setSelectedVendorOptionVersions(selectedVersionList);

        actualList = model.getVendorOptionVersionList();
        assertEquals(1, actualList.size());
        assertEquals(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData(), actualList.get(0));
    }

}
