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

package com.sldeditor.test.unit.ui.tree;

import com.sldeditor.ui.tree.SLDTree;
import com.sldeditor.ui.tree.SLDTreeManager;
import org.junit.Test;

/**
 * The unit test for SLDTreeManager.
 *
 * <p>{@link com.sldeditor.ui.tree.SLDTreeManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeManagerTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeManager#createSLDTree(java.util.List,
     * com.sldeditor.ui.tree.SLDTreeTools)}. Test method for {@link
     * com.sldeditor.ui.tree.SLDTreeManager#rebuildTree(com.sldeditor.ui.tree.SLDTree)}.
     */
    @Test
    public void testCreateSLDTree() {
        SLDTreeManager.getInstance().rebuildTree(null);
        SLDTree tree1 = SLDTreeManager.getInstance().createSLDTree(null, null);
        SLDTree tree2 = SLDTreeManager.getInstance().createSLDTree(null, null);

        SLDTreeManager.getInstance().rebuildTree(tree1);
        SLDTreeManager.getInstance().rebuildTree(tree2);
    }
}
