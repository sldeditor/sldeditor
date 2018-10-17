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

package com.sldeditor.test.unit.datasource.extension.filesystem.dataflavour;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.datasource.extension.filesystem.dataflavour.SLDDataFlavour;
import java.awt.datatransfer.DataFlavor;
import org.junit.jupiter.api.Test;

/**
 * Unit test for SLDDataFlavour class.
 *
 * <p>{@link com.sldeditor.datasource.extension.filesystem.dataflavour.SLDDataFlavour}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDDataFlavourTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.dataflavour.SLDDataFlavour#SLDDataFlavour()}.
     */
    @Test
    public void testSLDDataFlavour() {
        SLDDataFlavour actual1 = new SLDDataFlavour();

        String mimeType = "application/json";
        String humanPresentableName = "humanPresentableName";
        ClassLoader classLoader = SLDDataFlavourTest.class.getClassLoader();

        SLDDataFlavour actual2 = null;
        try {
            actual2 = new SLDDataFlavour(mimeType, humanPresentableName, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        SLDDataFlavour actual4 = null;
        SLDDataFlavour actual4a = null;
        try {
            actual4 = new SLDDataFlavour(mimeType);
            actual4a = new SLDDataFlavour(mimeType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        SLDDataFlavour actual5 = new SLDDataFlavour(getClass(), humanPresentableName);
        SLDDataFlavour actual6 = new SLDDataFlavour(getClass(), humanPresentableName);

        DataFlavor tmp = null;

        assertFalse(actual1.equals(tmp));
        assertFalse(actual1.equals(actual2));
        assertTrue(actual5.equals(actual6));
        assertTrue(actual4a.equals(actual4));

        SLDDataFlavour actual3 = new SLDDataFlavour(mimeType, humanPresentableName);
        SLDDataFlavour actual3a = new SLDDataFlavour(mimeType, humanPresentableName);
        SLDDataFlavour actual3b = new SLDDataFlavour(mimeType, "different");

        assertTrue(actual3a.equals(actual3));
        assertFalse(actual3b.equals(actual3));

        Object actual7 = new SLDDataFlavour(mimeType, humanPresentableName);
        assertTrue(actual3.equals(actual7));
    }
}
