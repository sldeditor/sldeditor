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

package com.sldeditor.test.unit.common.vendoroption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sldeditor.common.vendoroption.NoVendorOption;

/**
 * Unit test for NoVendorOption.
 * 
 * <p>{@link com.sldeditor.common.vendoroption.NoVendorOption}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class NoVendorOptionTest {

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.NoVendorOption#getName()}.
     */
    @Test
    public void testGetName() {
        NoVendorOption vendorOption = new NoVendorOption();

        assertEquals("Strict SLD", vendorOption.getName());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.NoVendorOption#getVersionStringList()}.
     */
    @Test
    public void testGetVersionStringList() {
        NoVendorOption vendorOption = new NoVendorOption();

        assertNull(vendorOption.getVersionStringList());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.NoVendorOption#addVersion(com.sldeditor.common.vendoroption.VersionData)}.
     */
    @Test
    public void testAddVersion() {
        NoVendorOption vendorOption = new NoVendorOption();

        vendorOption.addVersion(null);
        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.NoVendorOption#getVersion(java.lang.String)}.
     */
    @Test
    public void testGetVersion() {
        NoVendorOption vendorOption = new NoVendorOption();
        
        assertNull(vendorOption.getVersion(null));
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.NoVendorOption#getVersionList()}.
     */
    @Test
    public void testGetVersionList() {
        NoVendorOption vendorOption = new NoVendorOption();
        
        assertNull(vendorOption.getVersionList());
    }

}
