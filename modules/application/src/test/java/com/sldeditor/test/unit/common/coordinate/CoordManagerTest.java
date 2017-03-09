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

package com.sldeditor.test.unit.common.coordinate;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.referencing.CRS;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * Unit test for CoordManager class.
 * <p>{@link com.sldeditor.common.coordinate.CoordManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class CoordManagerTest {

    /**
     * Test method for {@link com.sldeditor.common.coordinate.CoordManager#getInstance()}.
     * Test method for {@link com.sldeditor.common.coordinate.CoordManager#getCRSList()}.
     * Test method for {@link com.sldeditor.common.coordinate.CoordManager#getCRSCode(org.opengis.referencing.crs.CoordinateReferenceSystem)}.
     * Test method for {@link com.sldeditor.common.coordinate.CoordManager#getWGS84()}.
     * @throws FactoryException 
     * @throws NoSuchAuthorityCodeException 
     */
    @Test
    public void testGetInstance() throws NoSuchAuthorityCodeException, FactoryException {
        CoordManager.getInstance().populateCRSList();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<ValueComboBoxData> crsList = CoordManager.getInstance().getCRSList();
        assertTrue(crsList.size() > 0);

        CoordinateReferenceSystem crs = CoordManager.getInstance().getCRS(null);
        assertNull(crs);

        crs = CoordManager.getInstance().getWGS84();
        assertTrue(crs != null);

        String code = CoordManager.getInstance().getCRSCode(null);
        assertTrue(code.compareTo("") == 0);

        code = CoordManager.getInstance().getCRSCode(crs);
        assertTrue(code.compareTo("EPSG:4326") == 0);

        String projectedCRSCode = "EPSG:27700";
        CoordinateReferenceSystem projectedCRS = CRS.decode(projectedCRSCode);

        code = CoordManager.getInstance().getCRSCode(projectedCRS);
        assertTrue(code.compareTo(projectedCRSCode) == 0);
    }

}
