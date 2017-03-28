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

package com.sldeditor.ui.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import com.sldeditor.common.undo.UndoActionInterface;

/**
 * Interface that allows the tree structure to be updated.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface UpdateTreeStructureInterface {

    /**
     * Adds the object.
     *
     * @param parent the parent
     * @param child the child
     * @param shouldBeVisible the should be visible
     * @return the default mutable tree node
     */
    DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child,
            boolean shouldBeVisible);

    /**
     * Populate the tree with the SLD structure.
     */
    void populateSLD();

    /**
     * Gets the undo object.
     *
     * @return the undo object
     */
    UndoActionInterface getUndoObject();
}