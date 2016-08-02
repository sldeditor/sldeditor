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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.styling.Rule;
import org.geotools.styling.Symbolizer;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.tree.SLDTreeTools;
import com.sldeditor.ui.tree.item.SymbolizerTreeItem;

/**
 * The unit test for SymbolizerTreeItem.
 * <p>{@link com.sldeditor.ui.tree.item.SymbolizerTreeItem}
 *
 * @author Robert Ward (SCISYS)
 */
public class SymbolizerTreeItemTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.item.SymbolizerTreeItem#getTreeString(java.lang.Object)}.
     */
    @Test
    public void testGetTreeString() {
        SymbolizerTreeItem item = new SymbolizerTreeItem();
        String actualValue = item.getTreeString(null);
        assertNull(actualValue);

        Symbolizer pointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        actualValue = item.getTreeString(pointSymbolizer);
        String expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.newMarker");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        pointSymbolizer.setName(null);
        actualValue = item.getTreeString(pointSymbolizer);
        expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.marker");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        Symbolizer lineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        actualValue = item.getTreeString(lineSymbolizer);
        expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.line");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        Symbolizer polygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        actualValue = item.getTreeString(polygonSymbolizer);
        expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.polygon");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        Symbolizer textSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        actualValue = item.getTreeString(textSymbolizer);
        expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.newText");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        textSymbolizer.setName("");
        actualValue = item.getTreeString(textSymbolizer);
        expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.text");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        Symbolizer rasterSymbolizer = DefaultSymbols.createDefaultRasterSymbolizer();
        actualValue = item.getTreeString(rasterSymbolizer);
        expectedValue = Localisation.getString(SLDTreeTools.class, "TreeItem.raster");
        assertTrue(actualValue.compareTo(expectedValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.item.SymbolizerTreeItem#itemSelected(javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)}.
     */
    @Test
    public void testItemSelected() {
        SymbolizerTreeItem item = new SymbolizerTreeItem();
        item.itemSelected(null, null);

        Symbolizer symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        Rule rule = DefaultSymbols.createNewRule();
        DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode();
        parentNode.setUserObject(rule);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode();
        parentNode.add(node);

        item.itemSelected(node, symbolizer);

        // Get the code coverage stats up
        item.itemSelected(parentNode, symbolizer);
        parentNode.setUserObject(item);
        item.itemSelected(node, null);
    }

}
