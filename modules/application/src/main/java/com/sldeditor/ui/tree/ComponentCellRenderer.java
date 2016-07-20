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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.Fill;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;

import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.ui.tree.item.SLDTreeItemInterface;

/**
 * A TreeCellRenderer displays each node of a tree. The default renderer
 * displays arbitrary Object nodes by calling their toString() method. The
 * Component.toString() method returns long strings with extraneous
 * information. Therefore, we use this "wrapper" implementation of
 * TreeCellRenderer to convert nodes from Component objects to useful String
 * values before passing those String values on to the default renderer.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ComponentCellRenderer implements TreeCellRenderer {

    /**  The renderer we are a wrapper for. */
    private TreeCellRenderer renderer;

    private JCheckBox leafRenderer = new JCheckBox();

    private JLabel label;
    private JCheckBox checkBox;
    private JPanel panel;

    private Color selectionForeground;
    private Color selectionBackground;
    private Color textForeground;
    private Color textBackground;

    /**
     * Instantiates a new component cell renderer.
     *
     * @param renderer the renderer
     */
    public ComponentCellRenderer(TreeCellRenderer renderer) {
        this.renderer = renderer;

        Boolean booleanValue = (Boolean) UIManager.get("Tree.drawsFocusBorderAroundIcon");
        leafRenderer.setFocusPainted((booleanValue != null) && (booleanValue.booleanValue()));

        selectionForeground = UIManager.getColor("Tree.selectionForeground");
        selectionBackground = UIManager.getColor("Tree.selectionBackground");
        textForeground = UIManager.getColor("Tree.textForeground");
        textBackground = UIManager.getColor("Tree.textBackground");

        checkBox = new JCheckBox();
        checkBox.setBackground(UIManager.getColor("Tree.background"));
        checkBox.setBorder(null);
        label = new JLabel();
        label.setBackground(UIManager.getColor("Tree.background"));
        label.setBorder(null);
        panel = new JPanel();
        panel.setOpaque(false);
        panel.add(checkBox);
        panel.add(label);
    }

    // This is the only TreeCellRenderer method.
    // Compute the string to display, and pass it to the wrapped renderer
    /* (non-Javadoc)
     * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object userObject = node.getUserObject();

        String name = getItemText(userObject);

        boolean showCheckbox = showCheckbox(userObject);
        leaf = isLeaf(userObject);

        if(showCheckbox)
        {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();

            Symbolizer symbolizer = (Symbolizer) parent.getUserObject();

            boolean selectedItem = SLDTreeLeafFactory.getInstance().isItemSelected(userObject, symbolizer);

            if (selected)
            {
                leafRenderer.setForeground(selectionForeground);
                leafRenderer.setBackground(selectionBackground);
            }
            else
            {
                leafRenderer.setForeground(textForeground);
                leafRenderer.setBackground(textBackground);
            }

            label.setText(name);
            checkBox.setSelected(selectedItem);

            return panel;
        }

        return renderer.getTreeCellRendererComponent(tree, name,
                selected, expanded, leaf, row, hasFocus);
    }

    /**
     * Checks if is leaf.
     *
     * @param userObject the user object
     * @return true, if is leaf
     */
    public static boolean isLeaf(Object userObject) {
        boolean leaf = (userObject instanceof TextSymbolizer) || (userObject instanceof RasterSymbolizer) || showCheckbox(userObject);
        return leaf;
    }

    /**
     * Show checkbox.
     *
     * @param userObject the user object
     * @return true, if successful
     */
    public static boolean showCheckbox(Object userObject) {
        boolean showCheckbox = (userObject instanceof Stroke) || (userObject instanceof Fill);
        return showCheckbox;
    }

    /**
     * Gets the item text.
     *
     * @param userObject the user object
     * @return the item text
     */
    public static String getItemText(Object userObject) {
        String name = null;

        if(userObject != null)
        {
            if(userObject instanceof String)
            {
                name = (String) userObject;
            }
            else if(userObject instanceof ExternalGraphicImpl)
            {
                ExternalGraphicImpl externalSymbol = (ExternalGraphicImpl) userObject;
                name = externalSymbol.getFormat();
            }
            else
            {
                SLDTreeItemInterface treeItem = SLDTree.treeItemMap.get(userObject.getClass());

                if(treeItem != null)
                {
                    name = treeItem.getTreeString(userObject);
                }
            }
        }
        return name;
    }

    /**
     * Gets the leaf renderer.
     *
     * @return the leaf renderer
     */
    public JCheckBox getLeafRenderer() {
        return leafRenderer;
    }
}