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

package com.sldeditor.tool.layerstyle;

import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * Tree that contains the GeoServer styles.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeoServerStyleTree extends JPanel {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant CONNECTED_GEOSERVER. */
    private static final String CONNECTED_GEOSERVER = "GeoServer";

    /** The Constant NOT_CONNECTED. */
    private static final String NOT_CONNECTED =
            Localisation.getString(GeoServerStyleTree.class, "common.notConnected");

    /** The tree model. */
    private DefaultTreeModel treeModel = null;

    /** The root node. */
    private DefaultMutableTreeNode rootNode;

    /** The geoserver style tree. */
    private JTree geoserverStyleTree = null;

    /** The btn apply. */
    protected JButton btnApply;

    /** The btn revert. */
    protected JButton btnRevert;

    /** The style card panel. */
    private JPanel styleCardPanel;

    /** The parent object. */
    private transient SelectedStyleInterface parentObj = null;

    /** The Constant STYLE_PANEL. */
    private static final String STYLE_PANEL = "STYLE";

    /** The Constant EMPTY_PANEL. */
    private static final String EMPTY_PANEL = "EMPTY";

    /** The populating tree flag. */
    private boolean populating = false;

    /** The original style wrapper. */
    private StyleWrapper originalStyleWrapper = null;

    /** Instantiates a new geo server style tree. */
    public GeoServerStyleTree(SelectedStyleInterface parent) {
        parentObj = parent;
        createUI();

        Dimension preferredSize = new Dimension(250, 300);
        setPreferredSize(preferredSize);
    }

    /** Creates the ui. */
    private void createUI() {
        setLayout(new BorderLayout());

        styleCardPanel = new JPanel();
        styleCardPanel.setLayout(new CardLayout(0, 0));

        add(styleCardPanel, BorderLayout.CENTER);

        JPanel styleTreePanel = new JPanel();
        styleTreePanel.setLayout(new BorderLayout());
        styleCardPanel.add(styleTreePanel, STYLE_PANEL);

        geoserverStyleTree = new JTree(treeModel);
        geoserverStyleTree.addTreeSelectionListener(
                new TreeSelectionListener() {
                    @Override
                    public void valueChanged(TreeSelectionEvent e) {
                        if (!populating) {
                            setButtonState(true);
                        }
                    }
                });

        JScrollPane scrollPane = new JScrollPane(geoserverStyleTree);
        styleTreePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel styleButtonsPanel = new JPanel();
        styleTreePanel.add(styleButtonsPanel, BorderLayout.SOUTH);

        btnApply = new JButton(Localisation.getString(GeoServerStyleTree.class, "common.apply"));

        btnApply.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        apply();
                    }
                });
        styleButtonsPanel.add(btnApply);

        btnRevert = new JButton(Localisation.getString(GeoServerStyleTree.class, "common.revert"));
        btnRevert.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        revert();
                    }
                });
        styleButtonsPanel.add(btnRevert);

        //
        // Empty panel
        //
        JPanel emptyPanel = new JPanel();
        emptyPanel.setLayout(new BorderLayout());
        styleCardPanel.add(emptyPanel, EMPTY_PANEL);
    }

    /** Revert button pressed. */
    protected void revert() {
        showSelectedStyle(originalStyleWrapper);
    }

    /**
     * Populate.
     *
     * @param geoserverName the geoserver name
     * @param styleMap the style map
     */
    public void populate(String geoserverName, Map<String, List<StyleWrapper>> styleMap) {
        populating = true;
        CardLayout cardLayout = (CardLayout) styleCardPanel.getLayout();
        cardLayout.show(styleCardPanel, EMPTY_PANEL);

        setButtonState(false);
        rootNode.removeAllChildren(); // this removes all nodes

        if (styleMap == null) {
            rootNode.setUserObject(NOT_CONNECTED);
        } else {
            rootNode.setUserObject((geoserverName != null) ? geoserverName : CONNECTED_GEOSERVER);

            for (String workspaceName : styleMap.keySet()) {
                List<StyleWrapper> styleList = styleMap.get(workspaceName);

                DefaultMutableTreeNode workspaceNode = new DefaultMutableTreeNode(workspaceName);

                // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
                treeModel.insertNodeInto(workspaceNode, rootNode, rootNode.getChildCount());

                for (StyleWrapper styleWrapper : styleList) {
                    LayerStyleNode childNode = new LayerStyleNode(styleWrapper);

                    // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
                    treeModel.insertNodeInto(
                            childNode, workspaceNode, workspaceNode.getChildCount());
                }
            }
        }

        treeModel.reload(); // this notifies the listeners and changes the GUI

        populating = false;
    }

    /**
     * Sets the ApplyRevert button state.
     *
     * @param enabled the new button state
     */
    private void setButtonState(boolean enabled) {
        btnApply.setEnabled(enabled);
        btnRevert.setEnabled(enabled);
    }

    /**
     * Gets the tree.
     *
     * @return the tree
     */
    public Component getTree() {
        return geoserverStyleTree;
    }

    /** Initialise. */
    public void initialise() {
        rootNode = new DefaultMutableTreeNode(NOT_CONNECTED);

        treeModel = new DefaultTreeModel(rootNode);

        geoserverStyleTree.setModel(treeModel);

        CardLayout cardLayout = (CardLayout) styleCardPanel.getLayout();
        cardLayout.show(styleCardPanel, EMPTY_PANEL);
    }

    /** Clear selection. */
    protected void clearSelection() {
        geoserverStyleTree.clearSelection();
    }

    /**
     * Select style.
     *
     * @param styleWrapper the style wrapper
     */
    protected void select(StyleWrapper styleWrapper) {
        originalStyleWrapper = styleWrapper;

        showSelectedStyle(styleWrapper);
    }

    /**
     * Show selected style.
     *
     * @param styleWrapper the style wrapper
     */
    private void showSelectedStyle(StyleWrapper styleWrapper) {
        populating = true;
        CardLayout cardLayout = (CardLayout) styleCardPanel.getLayout();
        cardLayout.show(styleCardPanel, STYLE_PANEL);

        setButtonState(false);

        selectStyleInTree(styleWrapper);

        populating = false;
    }

    /**
     * Select style in tree.
     *
     * @param styleWrapper the style wrapper
     */
    protected void selectStyleInTree(StyleWrapper styleWrapper) {
        DefaultMutableTreeNode node = null;

        if (styleWrapper != null) {
            for (int index = 0; (index < rootNode.getChildCount()) && (node == null); index++) {
                DefaultMutableTreeNode workspaceNode =
                        (DefaultMutableTreeNode) rootNode.getChildAt(index);

                if (workspaceNode.toString().compareTo(styleWrapper.getWorkspace()) == 0) {
                    // Found workspace node, now find style node
                    for (int styleIndex = 0;
                            styleIndex < workspaceNode.getChildCount() && (node == null);
                            styleIndex++) {
                        LayerStyleNode styleNode =
                                (LayerStyleNode) workspaceNode.getChildAt(styleIndex);

                        if (styleNode.getStyleWrapper().compareTo(styleWrapper) == 0) {
                            node = styleNode;
                        }
                    }
                }
            }
        }

        if (node != null) {
            TreeNode[] nodes = treeModel.getPathToRoot(node);
            TreePath path = new TreePath(nodes);
            geoserverStyleTree.setSelectionPath(path);
            geoserverStyleTree.scrollPathToVisible(path);
        }
    }

    /**
     * Gets the selected style.
     *
     * @return the selected style
     */
    public StyleWrapper getSelectedStyle() {
        StyleWrapper selectedStyle = null;
        TreePath selectedStylePath = geoserverStyleTree.getSelectionPath();

        if ((selectedStylePath != null) && (selectedStylePath.getPathCount() == 3)) {
            LayerStyleNode leaf = (LayerStyleNode) selectedStylePath.getLastPathComponent();

            if (leaf.getStyleWrapper() != null) {
                selectedStyle = new StyleWrapper(leaf.getStyleWrapper());
            }
        }

        return selectedStyle;
    }

    /** Apply button pressed. */
    protected void apply() {
        LayerStyleNode node = (LayerStyleNode) geoserverStyleTree.getLastSelectedPathComponent();

        if ((parentObj != null) && (node != null)) {
            parentObj.selectedStyle(node.getStyleWrapper());
        }
    }
}
