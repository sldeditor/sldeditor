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

package com.sldeditor.test.unit.ui.tree.item;

import static org.junit.Assert.assertTrue;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.tree.SLDTreeTools;
import com.sldeditor.ui.tree.item.UserLayerTreeItem;
import org.geotools.styling.UserLayer;
import org.junit.Test;

/**
 * The unit test for UserLayerTreeItem.
 *
 * <p>{@link com.sldeditor.ui.tree.item.UserLayerTreeItem}
 *
 * @author Robert Ward (SCISYS)
 */
public class UserLayerTreeItemTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.item.UserLayerTreeItem#getTreeString(java.lang.Object)}.
     */
    @Test
    public void testGetTreeString() {
        UserLayerTreeItem item = new UserLayerTreeItem();
        String actualValue = item.getTreeString(null, null);
        String expectedValue =
                String.format(
                        "%s : %s",
                        Localisation.getString(SLDTreeTools.class, "TreeItem.userlayer"), "");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        UserLayer userLayer = DefaultSymbols.createNewUserLayer();

        actualValue = item.getTreeString(null, userLayer);
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        String expectedName = "test name";
        userLayer.setName(expectedName);
        actualValue = item.getTreeString(null, userLayer);
        expectedValue =
                String.format(
                        "%s : %s",
                        Localisation.getString(SLDTreeTools.class, "TreeItem.userlayer"),
                        expectedName);
        assertTrue(actualValue.compareTo(expectedValue) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.item.UserLayerTreeItem#itemSelected(javax.swing.tree.DefaultMutableTreeNode,
     * java.lang.Object)}.
     */
    @Test
    public void testItemSelected() {
        UserLayerTreeItem item = new UserLayerTreeItem();
        item.itemSelected(null, null);
    }
}
