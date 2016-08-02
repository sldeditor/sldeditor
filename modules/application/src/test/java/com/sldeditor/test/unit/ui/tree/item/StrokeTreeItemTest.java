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

import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.tree.SLDTreeTools;
import com.sldeditor.ui.tree.item.StrokeTreeItem;

/**
 * The unit test for StrokeTreeItem.
 * <p>{@link com.sldeditor.ui.tree.item.StrokeTreeItem}
 *
 * @author Robert Ward (SCISYS)
 */
public class StrokeTreeItemTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.item.StrokeTreeItem#getTreeString(java.lang.Object)}.
     */
    @Test
    public void testGetTreeString() {
        StrokeTreeItem item = new StrokeTreeItem();
        String actualValue = item.getTreeString(null);
        String expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.stroke");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        Stroke stroke = DefaultSymbols.createDefaultStroke();

        actualValue = item.getTreeString(stroke);
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        actualValue = item.getTreeString(stroke);
        assertTrue(actualValue.compareTo(expectedValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.item.StrokeTreeItem#itemSelected(javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)}.
     */
    @Test
    public void testItemSelected() {
        StrokeTreeItem item = new StrokeTreeItem();
        item.itemSelected(null, null);

        Symbolizer symbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode();
        parentNode.setUserObject(symbolizer);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        parentNode.add(node);

        item.itemSelected(node, symbolizer);

        // Get the code coverage stats up
        item.itemSelected(parentNode, symbolizer);
        parentNode.setUserObject(item);
        item.itemSelected(node, null);
    }

}
