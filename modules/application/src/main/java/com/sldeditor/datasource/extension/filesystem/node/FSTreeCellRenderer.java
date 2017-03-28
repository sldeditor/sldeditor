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

package com.sldeditor.datasource.extension.filesystem.node;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.sldeditor.common.NodeInterface;

/**
 * A FSTreeCellRenderer displays each node of a tree. The default renderer displays arbitrary Object nodes by calling their toString() method. The
 * Component.toString() method returns long strings with extraneous information. Therefore, we use this "wrapper" implementation of TreeCellRenderer
 * to convert nodes from Component objects to useful String values before passing those String values on to the default renderer.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FSTreeCellRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new component cell renderer.
     */
    public FSTreeCellRenderer() {
    }

    // This is the only TreeCellRenderer method.
    // Compute the string to display, and pass it to the wrapped renderer
    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int,
     * boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();

        String name = getItemText(node, userObject);

        super.getTreeCellRendererComponent(tree, name, selected, expanded, leaf, row, hasFocus);

        if (node instanceof NodeInterface) {
            Icon icon = ((NodeInterface) node).getIcon();

            if (icon != null) {
                setIcon(icon);
            }
        }
        return this;
    }

    /**
     * Gets the item text.
     *
     * @param node the node
     * @param userObject the user object
     * @return the item text
     */
    public static String getItemText(DefaultMutableTreeNode node, Object userObject) {
        String name = null;

        if (userObject != null) {
            if (userObject instanceof String) {
                name = (String) userObject;
            }
        }
        return name;
    }

}