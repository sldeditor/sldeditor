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

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.ui.tree.item.StyledLayerDescriptorTreeItem;
import org.junit.jupiter.api.Test;

/**
 * The unit test for StyledLayerDescriptorTreeItem.
 *
 * <p>{@link com.sldeditor.ui.tree.item.StyledLayerDescriptorTreeItem}
 *
 * @author Robert Ward (SCISYS)
 */
public class StyledLayerDescriptorTreeItemTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.item.StyledLayerDescriptorTreeItem#getTreeString(java.lang.Object)}.
     */
    @Test
    public void testGetTreeString() {
        StyledLayerDescriptorTreeItem item = new StyledLayerDescriptorTreeItem();
        String actualValue = item.getTreeString(null, null);
        String expectedValue = "SLD";
        assertTrue(actualValue.compareTo(expectedValue) == 0);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.item.StyledLayerDescriptorTreeItem#itemSelected(javax.swing.tree.DefaultMutableTreeNode,
     * java.lang.Object)}.
     */
    @Test
    public void testItemSelected() {
        StyledLayerDescriptorTreeItem item = new StyledLayerDescriptorTreeItem();
        item.itemSelected(null, null);
    }
}
