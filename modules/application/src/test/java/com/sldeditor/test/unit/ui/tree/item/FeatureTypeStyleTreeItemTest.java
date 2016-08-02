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

import org.geotools.styling.FeatureTypeStyle;
import org.junit.Test;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.tree.SLDTreeTools;
import com.sldeditor.ui.tree.item.FeatureTypeStyleTreeItem;

/**
 * The unit test for FeatureTypeStyleTreeItem.
 * <p>{@link com.sldeditor.ui.tree.item.FeatureTypeStyleTreeItem}
 *
 * @author Robert Ward (SCISYS)
 */
public class FeatureTypeStyleTreeItemTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.item.FeatureTypeStyleTreeItem#getTreeString(java.lang.Object)}.
     */
    @Test
    public void testGetTreeString() {
        FeatureTypeStyleTreeItem item = new FeatureTypeStyleTreeItem();
        String actualValue = item.getTreeString(null);
        String expectedValue = String.format("%s : %s", Localisation.getString(SLDTreeTools.class, "TreeItem.featureTypeStyle"), "");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();

        actualValue = item.getTreeString(fts);
        // Default feature type style sets name = name!
        expectedValue = String.format("%s : %s", Localisation.getString(SLDTreeTools.class, "TreeItem.featureTypeStyle"), "name");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        fts.setName(null);
        actualValue = item.getTreeString(fts);
        // Default feature type style sets name = name!
        expectedValue = String.format("%s : %s", Localisation.getString(SLDTreeTools.class, "TreeItem.featureTypeStyle"), "");
        assertTrue(actualValue.compareTo(expectedValue) == 0);

        String expectedName = "test name";
        fts.setName(expectedName);
        actualValue = item.getTreeString(fts);
        expectedValue = String.format("%s : %s", Localisation.getString(SLDTreeTools.class, "TreeItem.featureTypeStyle"), expectedName);
        assertTrue(actualValue.compareTo(expectedValue) == 0);    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.item.FeatureTypeStyleTreeItem#itemSelected(javax.swing.tree.DefaultMutableTreeNode, java.lang.Object)}.
     */
    @Test
    public void testItemSelected() {
        FeatureTypeStyleTreeItem item = new FeatureTypeStyleTreeItem();
        item.itemSelected(null, null);
    }

}
