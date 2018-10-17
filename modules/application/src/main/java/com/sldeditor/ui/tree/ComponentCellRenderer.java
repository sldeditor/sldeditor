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

import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.ui.tree.item.SLDTreeItemInterface;
import com.sldeditor.ui.tree.item.TreeItemMap;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

/** The Class ComponentCellRenderer. */
public class ComponentCellRenderer implements TreeCellRenderer {

    /** The renderer we are a wrapper for. */
    private TreeCellRenderer renderer;

    /** The checkbox node panel. */
    private CheckBoxPanel panel = new CheckBoxPanel();

    /**
     * Instantiates a new component cell renderer.
     *
     * @param renderer the renderer
     */
    public ComponentCellRenderer(TreeCellRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * This is the only TreeCellRenderer method. Compute the string to display, and pass it to the
     * wrapped renderer (non-Javadoc)
     *
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree,
     *     java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean selected,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();
        Object parentUserObject = null;

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
        if (parentNode != null) {
            parentUserObject = parentNode.getUserObject();
        }

        String name = getItemText(node, userObject);

        boolean showCheckbox = showCheckbox(parentUserObject, userObject);
        boolean isLeaf = isLeaf(parentUserObject, userObject);

        panel.setCheckboxVisible(showCheckbox);
        if (showCheckbox) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

            Symbolizer symbolizer = (Symbolizer) parent.getUserObject();

            boolean selectedItem =
                    SLDTreeLeafFactory.getInstance().isItemSelected(userObject, symbolizer);

            panel.setSelected(selected, hasFocus);

            panel.setLabelText(name);
            panel.setCheckboxSelected(selectedItem);

            return panel;
        } else {
            if (isLeaf) {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

                if (parent != null) {
                    panel.setLabelText(name);
                    panel.setSelected(selected, hasFocus);

                    return panel;
                }
            }
        }

        return renderer.getTreeCellRendererComponent(
                tree, name, selected, expanded, isLeaf, row, hasFocus);
    }

    /**
     * Checks if tree node is leaf.
     *
     * <p>Is a leaf if the user object is one of the following:
     *
     * <p>
     *
     * <ul>
     *   <li>TextSymbolizer
     *   <li>RasterSymbolizer
     *   <li>Stroke
     *   <li>Fill
     * </ul>
     *
     * @param parentUserObject the parent user object
     * @param userObject the user object
     * @return true, if is leaf
     */
    public static boolean isLeaf(Object parentUserObject, Object userObject) {
        boolean leaf =
                (userObject instanceof TextSymbolizer)
                        || (userObject instanceof RasterSymbolizer)
                        || (userObject instanceof Stroke)
                        || (userObject instanceof Fill);
        return leaf;
    }

    /**
     * Workout whether to show checkbox in the tree. Should only appear for PolygonSymbolizer fill
     * and strokes
     *
     * @param parentUserObject the parent user object
     * @param userObject the user object
     * @return true, if successful
     */
    public static boolean showCheckbox(Object parentUserObject, Object userObject) {
        boolean showCheckbox =
                (parentUserObject instanceof PolygonSymbolizer)
                        && ((userObject instanceof Stroke) || (userObject instanceof Fill));
        return showCheckbox;
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
            } else if (userObject instanceof ExternalGraphicImpl) {
                ExternalGraphicImpl externalSymbol = (ExternalGraphicImpl) userObject;
                name = externalSymbol.getFormat();
            } else {
                SLDTreeItemInterface treeItem =
                        TreeItemMap.getInstance().getValue(userObject.getClass());

                if (treeItem != null) {
                    name = treeItem.getTreeString(node, userObject);
                }
            }
        }
        return name;
    }
}
