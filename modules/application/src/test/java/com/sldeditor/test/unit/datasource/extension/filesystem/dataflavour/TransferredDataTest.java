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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.swing.tree.TreePath;

import org.junit.Test;

import com.sldeditor.datasource.extension.filesystem.dataflavour.SLDDataFlavour;
import com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData;

/**
 * Unit test for TransferredData class.
 * 
 * <p>{@link com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class TransferredDataTest {

    /**
     * Test method for {@link com.sldeditor.datasource.extension.filesystem.dataflavour.TransferredData#getDataListSize()}.
     */
    @Test
    public void testGetDataListSize() {
        TransferredData data = new TransferredData();

        assertEquals(0, data.getDataListSize());

        Integer userObject1 = Integer.valueOf(42);
        Double userObject2 = Double.valueOf(42.0);
        String userObject3 = "xxxx";
        SLDDataFlavour dataFlavour1 = new SLDDataFlavour(getClass(), "humanPresentableName");
        SLDDataFlavour dataFlavour2 = new SLDDataFlavour(getClass(), "humanPresentableName");
        SLDDataFlavour dataFlavour3 = new SLDDataFlavour(getClass(), "humanPresentableName");

        TreePath treePath1 = new TreePath("test1");
        TreePath treePath2 = new TreePath("test2");
        TreePath treePath3 = new TreePath("test3");
        data.addData(treePath1, userObject1, dataFlavour1);
        data.addData(treePath2, userObject2, dataFlavour2);
        data.addData(treePath3, userObject3, dataFlavour3);
        assertEquals(3, data.getDataListSize());

        assertEquals(dataFlavour2, data.getDataFlavour(1));
        assertNull(data.getDataFlavour(-1));
        assertNull(data.getDataFlavour(11));

        assertEquals(userObject3, data.getUserObject(2));
        assertNull(data.getUserObject(-1));
        assertNull(data.getUserObject(11));

        assertEquals(treePath1, data.getTreePath(0));
        assertNull(data.getTreePath(-1));
        assertNull(data.getTreePath(11));
    }

}
