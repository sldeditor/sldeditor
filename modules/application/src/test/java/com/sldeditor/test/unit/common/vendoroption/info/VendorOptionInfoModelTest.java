/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.common.vendoroption.info;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.info.VendorOptionInfoModel;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

/**
 * Unit test for VendorOptionInfoModel.
 *
 * <p>{@link com.sldeditor.common.vendoroption.info.VendorOptionInfoModel}
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfoModelTest {

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfoModel#isCellEditable(int, int)}.
     */
    @Test
    public void testIsCellEditable() {
        VendorOptionInfoModel model = new VendorOptionInfoModel();

        assertFalse(model.isCellEditable(0, 0));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfoModel#getColumnCount()}.
     */
    @Test
    public void testGetColumnCount() {
        VendorOptionInfoModel model = new VendorOptionInfoModel();

        assertEquals(2, model.getColumnCount());
        assertNotNull(model.getColumnName(0));
        assertNotNull(model.getColumnName(1));
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfoModel#addVendorOptionInfo(java.util.List)}.
     */
    @Test
    public void testAddVendorOptionInfo() {
        VendorOptionInfoModel model = new VendorOptionInfoModel();

        assertEquals(0, model.getRowCount());
        model.addVendorOptionInfo(null);
        List<VendorOptionInfo> vendorOptionInfoList = new ArrayList<VendorOptionInfo>();
        String name = "name";
        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion versionData =
                new VendorOptionVersion(
                        GeoServerVendorOption.class, versionDataMin, versionDataMax);
        String description = "test description";
        VendorOptionInfo info = new VendorOptionInfo(name, versionData, description);

        vendorOptionInfoList.add(info);
        String name2 = "name2";
        String description2 = "test description2";
        VendorOptionVersion versionData2 =
                VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
        vendorOptionInfoList.add(new VendorOptionInfo(name2, versionData2, description2));

        VersionData versionDataMin3 = VersionData.decode(getClass(), "2.8.1");
        VersionData versionDataMax3 = VersionData.decode(getClass(), "Latest");

        VendorOptionVersion versionData3 =
                new VendorOptionVersion(
                        GeoServerVendorOption.class, versionDataMin3, versionDataMax3);
        String name3 = "name3";
        String description3 = "test description3";
        vendorOptionInfoList.add(new VendorOptionInfo(name3, versionData3, description3));

        model.addVendorOptionInfo(vendorOptionInfoList);

        assertEquals(vendorOptionInfoList.size(), model.getRowCount());

        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(-1, -1));
        assertNull(model.getValueAt(4, -1));
        assertNull(model.getValueAt(4, 4));
        assertEquals(name2, model.getValueAt(0, 0));
        assertEquals("Strict SLD", model.getValueAt(0, 1));
        assertEquals("GeoServer 2.4.1-2.8.3", model.getValueAt(1, 1));

        // Test get description
        assertNull(model.getDescription(-1));
        assertNull(model.getDescription(10));

        assertEquals(description2, model.getDescription(0));

        model.setSelectedVersion(VersionData.decode(getClass(), "2.5.1"));

        model.isVendorOptionAvailable(-1);
        model.isVendorOptionAvailable(42);
        model.isVendorOptionAvailable(1);
    }

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfoModel#setSelectedVersion(com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testSetSelectedVersion() {}

    /**
     * Test method for {@link
     * com.sldeditor.common.vendoroption.info.VendorOptionInfoModel#isVendorOptionAvailable(int)}.
     */
    @Test
    public void testIsVendorOptionAvailable() {}
}
