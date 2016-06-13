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
package com.sldeditor.ui.tree.item;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * The Interface SLDTreeItemInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface SLDTreeItemInterface {

    /**
     * Gets the tree string.
     *
     * @param nodeObject the node object
     * @return the tree string
     */
    String getTreeString(Object nodeObject);

    /**
     * Item selected.
     *
     * @param node the node
     * @param userObject the user object
     */
    void itemSelected(DefaultMutableTreeNode node, Object userObject);
}
