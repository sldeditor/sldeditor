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

import com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit test for BuiltInDataFlavour.
 *
 * <p>{@link com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour}
 *
 * @author Robert Ward (SCISYS)
 */
public class BuiltInDataFlavourTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.dataflavour.BuiltInDataFlavour#populate(java.util.List,
     * java.util.List, java.util.List)}.
     */
    @Test
    public void testPopulate() {
        List<DataFlavor> dataFlavourList = new ArrayList<DataFlavor>();
        List<DataFlavor> destinationFolderList = new ArrayList<DataFlavor>();
        List<DataFlavor> destinationGeoServerList = new ArrayList<DataFlavor>();

        BuiltInDataFlavour flavour = new BuiltInDataFlavour();
        flavour.populate(dataFlavourList, destinationFolderList, destinationGeoServerList);

        assertFalse(dataFlavourList.isEmpty());
        assertFalse(destinationFolderList.isEmpty());
        assertFalse(destinationGeoServerList.isEmpty());
    }
}
