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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import org.geotools.data.DataStore;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.FillImpl;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.UserLayerImpl;

import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.xml.ui.SelectedTreeItemEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.SymbolizerSelectedInterface;
import com.sldeditor.ui.tree.item.SLDTreeItemInterface;
import com.sldeditor.ui.tree.item.TreeItemMap;

/**
 * The component that displays the structure of the SLD as a tree.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDTree extends JPanel implements TreeSelectionListener, SLDTreeUpdatedInterface,
        DataSourceUpdatedInterface, UndoActionInterface, UpdateTreeStructureInterface {

    private static final int PANEL_HEIGHT = 350;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The sld symbol structure tree. */
    private JTree symbolTree = null;

    /** The root node. */
    protected DefaultMutableTreeNode rootNode = null;

    /** The tree model. */
    private DefaultTreeModel treeModel = null;

    /** The display panel. */
    private SymbolizerSelectedInterface displayPanel = null;

    /** The node map. */
    private Map<String, DefaultMutableTreeNode> nodeMap = new HashMap<String, DefaultMutableTreeNode>();

    /** The object to render the selected symbol. */
    private List<RenderSymbolInterface> renderList = null;

    /** The tree tools. */
    private SLDTreeTools treeTools = null;

    /** The current geometry type of the loaded data source. */
    private GeometryTypeEnum currentGeometryType = GeometryTypeEnum.UNKNOWN;

    /**
     * Instantiates a new SLD tree.
     *
     * @param renderList the render list
     * @param treeTools the tree tools
     */
    public SLDTree(List<RenderSymbolInterface> renderList, SLDTreeTools treeTools) {
        this.renderList = renderList;
        this.treeTools = treeTools;

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        if (dataSource != null) {
            dataSource.addListener(this);
        }

        if (renderList != null) {
            for (RenderSymbolInterface render : renderList) {
                if (render instanceof DataSourceUpdatedInterface) {
                    if (dataSource != null) {
                        dataSource.addListener(render);
                    }
                }
            }
        }

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        setBorder(new LineBorder(new Color(0, 0, 0)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panelSymbolMarkerTree = new JPanel();
        add(panelSymbolMarkerTree);

        JScrollPane scrollpane = new JScrollPane();

        rootNode = new DefaultMutableTreeNode("SLD");
        treeModel = new DefaultTreeModel(rootNode);
        symbolTree = new JTree(treeModel);
        symbolTree.setEditable(true);
        symbolTree.setBorder(new LineBorder(Color.black));
        ComponentCellRenderer cellRenderer = new ComponentCellRenderer(
                symbolTree.getCellRenderer());
        symbolTree.setCellRenderer(cellRenderer);
        symbolTree.setCellEditor(new CheckBoxNodeEditor(symbolTree, cellRenderer, this));
        symbolTree.setEditable(true);
        symbolTree.setRowHeight(0);

        // Listen for when the selection changes.
        symbolTree.addTreeSelectionListener(this);
        panelSymbolMarkerTree.setLayout(new BorderLayout(0, 0));

        scrollpane.setViewportView(symbolTree);

        panelSymbolMarkerTree.add(scrollpane);

        // Add the tree tools if they were supplied
        if (treeTools != null) {
            treeTools.configure(this, symbolTree, treeModel, renderList);
            add(treeTools.getButtonPanel());
        } else {
            setPreferredSize(new Dimension(SLDTreeTools.getPanelWidth(), PANEL_HEIGHT));
        }
    }

    /**
     * Gets the path.
     *
     * @param treeNode the tree node
     * @return the path
     */
    private static TreePath getPath(TreeNode treeNode) {
        List<Object> nodes = new ArrayList<Object>();
        if (treeNode != null) {
            nodes.add(treeNode);
            treeNode = treeNode.getParent();
            while (treeNode != null) {
                nodes.add(0, treeNode);
                treeNode = treeNode.getParent();
            }
        }

        return nodes.isEmpty() ? null : new TreePath(nodes.toArray());
    }

    /**
     * Adds the symbol selected listener.
     *
     * @param symbolizerSelectedPanel the symbolizer selected panel
     */
    public void addSymbolSelectedListener(SymbolizerSelectedInterface symbolizerSelectedPanel) {
        displayPanel = symbolizerSelectedPanel;
    }

    /**
     * Reset the tree structure
     */
    private void reset() {
        rootNode.removeAllChildren(); // This removes all nodes
        treeModel.reload(); // This notifies the listeners and changes the GUI
        nodeMap.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.tree.UpdateTreeStructureInterface#addObject(javax.swing.tree.DefaultMutableTreeNode, java.lang.Object, boolean)
     */
    @Override
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child,
            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        nodeMap.put(SLDTreeItemWrapper.generateKey(child), childNode);

        if (parent == null) {
            parent = rootNode;
        }

        // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            symbolTree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.tree.UpdateTreeStructureInterface#populateSLD()
     */
    @Override
    public void populateSLD() {
        reset();

        SelectedSymbol selectedSymbol = SelectedSymbol.getInstance();

        if (selectedSymbol != null) {
            StyledLayerDescriptor sld = selectedSymbol.getSld();
            rootNode.setUserObject(sld);
            treeModel.nodeChanged(rootNode);

            if (sld != null) {
                List<StyledLayer> styledLayerList = sld.layers();

                if (styledLayerList != null) {
                    for (StyledLayer styledLayer : styledLayerList) {
                        DefaultMutableTreeNode styledLayerTreeNode = null;
                        List<Style> styleList = null;

                        if (styledLayer instanceof NamedLayerImpl) {
                            NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;
                            styleList = namedLayerImpl.styles();

                            styledLayerTreeNode = addObject(rootNode, namedLayerImpl, true);
                        } else if (styledLayer instanceof UserLayerImpl) {
                            UserLayerImpl userLayerImpl = (UserLayerImpl) styledLayer;
                            styleList = userLayerImpl.userStyles();

                            styledLayerTreeNode = addObject(rootNode, userLayerImpl, true);
                        }

                        if (styleList != null) {
                            for (Style style : styleList) {
                                DefaultMutableTreeNode styleTreeNode = addObject(
                                        styledLayerTreeNode, style, true);

                                for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                                    DefaultMutableTreeNode ftsTreeNode = addObject(styleTreeNode,
                                            fts, true);

                                    for (Rule rule : fts.rules()) {
                                        DefaultMutableTreeNode ruleTreeNode = addObject(ftsTreeNode,
                                                rule, true);

                                        for (Symbolizer symbolizer : rule.symbolizers()) {
                                            DefaultMutableTreeNode symbolizerTreeNode = addObject(
                                                    ruleTreeNode, symbolizer, true);

                                            if ((symbolizer instanceof PointSymbolizer)
                                                    || (symbolizer instanceof PolygonSymbolizer)) {
                                                addObject(
                                                        symbolizerTreeNode, SLDTreeLeafFactory
                                                                .getInstance().getFill(symbolizer),
                                                        true);
                                            }

                                            if ((symbolizer instanceof PolygonSymbolizer)
                                                    || (symbolizer instanceof LineSymbolizer)) {
                                                addObject(symbolizerTreeNode, SLDTreeLeafFactory
                                                        .getInstance().getStroke(symbolizer), true);
                                            }

                                            if (symbolizer instanceof RasterSymbolizer) {
                                                // Handle the image outline symbolizer for raster symbols
                                                Symbolizer outlineSymbolizer = ((RasterSymbolizer) symbolizer)
                                                        .getImageOutline();

                                                if (outlineSymbolizer instanceof LineSymbolizer) {
                                                    LineSymbolizer outlineLineSymbolizer = (LineSymbolizer) outlineSymbolizer;
                                                    if (outlineLineSymbolizer != null) {
                                                        DefaultMutableTreeNode symbolizerImageOutlineLineNode = addObject(
                                                                symbolizerTreeNode,
                                                                outlineLineSymbolizer, true);

                                                        addObject(symbolizerImageOutlineLineNode,
                                                                SLDTreeLeafFactory.getInstance()
                                                                        .getStroke(
                                                                                outlineLineSymbolizer),
                                                                true);
                                                    }
                                                } else if (outlineSymbolizer instanceof PolygonSymbolizer) {
                                                    PolygonSymbolizer outlinePolygonSymbolizer = (PolygonSymbolizer) outlineSymbolizer;

                                                    if (outlinePolygonSymbolizer != null) {
                                                        DefaultMutableTreeNode symbolizerImageOutlinePolygonNode = addObject(
                                                                symbolizerTreeNode,
                                                                outlinePolygonSymbolizer, true);

                                                        addObject(symbolizerImageOutlinePolygonNode,
                                                                SLDTreeLeafFactory.getInstance()
                                                                        .getFill(
                                                                                outlinePolygonSymbolizer),
                                                                true);
                                                        addObject(symbolizerImageOutlinePolygonNode,
                                                                SLDTreeLeafFactory.getInstance()
                                                                        .getStroke(
                                                                                outlinePolygonSymbolizer),
                                                                true);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        leafSelected();
    }

    /**
     * Select first symbol.
     */
    public void selectFirstSymbol() {
        if (SelectedSymbol.getInstance().getSld() != null) {
            symbolTree.setSelectionRow(0);
        } else {
            // No SLD loaded
            if (displayPanel != null) {
                displayPanel.show(null, null);
            }

            if (renderList != null) {
                for (RenderSymbolInterface render : renderList) {
                    render.renderSymbol();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.tree.SLDTreeUpdatedInterface#textUpdated()
     */
    @Override
    public void textUpdated() {

        int[] selectedRows = symbolTree.getSelectionRows();
        populateSLD();
        symbolTree.setSelectionRows(selectedRows);
    }

    /**
     * Update node.
     *
     * @param objectOld the object old
     * @param objectNew the object new
     */
    @Override
    public void updateNode(Object objectOld, Object objectNew) {

        String key = SLDTreeItemWrapper.generateKey(objectOld);
        DefaultMutableTreeNode node = nodeMap.get(key);

        if (node != null) {
            node.setUserObject(objectNew);
            nodeMap.remove(key.toString());
            String newKey = SLDTreeItemWrapper.generateKey(objectNew);
            nodeMap.put(newKey, node);
            treeModel.nodeChanged(node);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType,
            boolean isConnectedToDataSourceFlag) {
        currentGeometryType = geometryType;
    }

    /**
     * Select tree item.
     *
     * @param data the data
     * @return true, if successful
     */
    public boolean selectTreeItem(TreeSelectionData data) {
        symbolTree.clearSelection();

        SelectedTreeItemEnum selectedTreeItemEnum = data.getSelection();
        int layerIndex = data.getLayerIndex();
        int styleIndex = data.getStyleIndex();
        int featureTypeStyleIndex = data.getFeatureTypeStyleIndex();
        int ruleIndex = data.getRuleIndex();
        int symbolizerIndex = data.getSymbolizerIndex();
        int symbolizerDetailIndex = data.getSymbolizerDetailIndex();

        DefaultMutableTreeNode layerNode = null;
        DefaultMutableTreeNode styleNode = null;
        DefaultMutableTreeNode ftsNode = null;
        DefaultMutableTreeNode ruleNode = null;
        DefaultMutableTreeNode symbolizerNode = null;
        DefaultMutableTreeNode symbolizerDetailNode = null;

        TreePath path = null;

        if ((layerIndex < 0) || (layerIndex >= rootNode.getChildCount())) {
            return false;
        }

        layerNode = (DefaultMutableTreeNode) rootNode.getChildAt(layerIndex);

        if (selectedTreeItemEnum == SelectedTreeItemEnum.LAYER) {
            path = getPath(layerNode);
        } else {
            if (layerNode == null) {
                return false;
            }
            if ((styleIndex < 0) || (styleIndex >= layerNode.getChildCount())) {
                return false;
            }

            styleNode = (DefaultMutableTreeNode) layerNode.getChildAt(styleIndex);
            if (selectedTreeItemEnum == SelectedTreeItemEnum.STYLE) {
                path = getPath(styleNode);
            } else {
                if (styleNode == null) {
                    return false;
                }

                if ((featureTypeStyleIndex < 0)
                        || (featureTypeStyleIndex >= styleNode.getChildCount())) {
                    return false;
                }

                ftsNode = (DefaultMutableTreeNode) styleNode.getChildAt(featureTypeStyleIndex);
                if (selectedTreeItemEnum == SelectedTreeItemEnum.FEATURETYPESTYLE) {
                    path = getPath(ftsNode);
                } else {
                    if (ftsNode == null) {
                        return false;
                    }
                    if ((ruleIndex < 0) || (ruleIndex >= ftsNode.getChildCount())) {
                        return false;
                    }

                    ruleNode = (DefaultMutableTreeNode) ftsNode.getChildAt(ruleIndex);
                    if (selectedTreeItemEnum == SelectedTreeItemEnum.RULE) {
                        path = getPath(ruleNode);
                    } else {
                        if (ruleNode == null) {
                            return false;
                        }
                        if ((symbolizerIndex < 0)
                                || (symbolizerIndex >= ruleNode.getChildCount())) {
                            return false;
                        }

                        symbolizerNode = (DefaultMutableTreeNode) ruleNode
                                .getChildAt(symbolizerIndex);

                        if ((selectedTreeItemEnum == SelectedTreeItemEnum.POINT_SYMBOLIZER)
                                || (selectedTreeItemEnum == SelectedTreeItemEnum.LINE_SYMBOLIZER)
                                || (selectedTreeItemEnum == SelectedTreeItemEnum.POLYGON_SYMBOLIZER)
                                || (selectedTreeItemEnum == SelectedTreeItemEnum.RASTER_SYMBOLIZER)
                                || (selectedTreeItemEnum == SelectedTreeItemEnum.TEXT_SYMBOLIZER)) {
                            path = getPath(symbolizerNode);
                        } else {
                            if ((symbolizerDetailIndex < 0)
                                    || (symbolizerDetailIndex >= symbolizerNode.getChildCount())) {
                                return false;
                            }

                            symbolizerDetailNode = (DefaultMutableTreeNode) symbolizerNode
                                    .getChildAt(symbolizerDetailIndex);

                            if (symbolizerDetailNode == null) {
                                return false;
                            }

                            if ((selectedTreeItemEnum == SelectedTreeItemEnum.POINT_FILL)
                                    || (selectedTreeItemEnum == SelectedTreeItemEnum.POLYGON_FILL)
                                    || (selectedTreeItemEnum == SelectedTreeItemEnum.STROKE)) {
                                path = getPath(symbolizerDetailNode);
                            }
                        }
                    }
                }
            }
        }

        symbolTree.setSelectionPath(path);

        return true;
    }

    /**
     * Gets the panel.
     *
     * @return the selected symbol panel
     */
    public PopulateDetailsInterface getSelectedSymbolPanel() {
        PopulateDetailsInterface panel = null;
        TreePath path = symbolTree.getSelectionPath();

        if (path != null) {
            DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();

            if (lastNode != null) {
                Object nodeInfo = lastNode.getUserObject();
                if (nodeInfo != null) {
                    Class<?> classSelected = nodeInfo.getClass();
                    String key = classSelected.toString();

                    Class<?> parentClass = null;
                    if (lastNode.getParent() != null) {
                        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) lastNode
                                .getParent();

                        parentClass = parent.getUserObject().getClass();
                    }

                    if (displayPanel != null) {
                        panel = displayPanel.getPanel(parentClass, key);
                    }
                }
            }
        }

        return panel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.tree.SLDTreeUpdatedInterface#leafSelected()
     */
    @Override
    public void leafSelected() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) symbolTree
                .getLastSelectedPathComponent();
        DefaultMutableTreeNode parent = null;

        if (node != null) {
            Object nodeInfo = node.getUserObject();
            if (nodeInfo != null) {
                Class<?> classSelected = nodeInfo.getClass();
                SLDTreeItemInterface treeItem = TreeItemMap.getInstance().getValue(classSelected);

                if (treeItem != null) {
                    treeItem.itemSelected(node, nodeInfo);
                }

                if (displayPanel != null) {
                    Controller.getInstance().setPopulating(true);

                    Class<?> parentClass = null;
                    if (node.getParent() != null) {
                        parent = (DefaultMutableTreeNode) node.getParent();
                        parentClass = parent.getUserObject().getClass();

                        // Check to see if node represents a fill
                        if (classSelected == FillImpl.class) {
                            // Check to see if fill has been selected
                            Symbolizer symbolizer = (Symbolizer) parent.getUserObject();
                            if (!SLDTreeLeafFactory.getInstance().hasFill(symbolizer)) {
                                parentClass = null;
                                classSelected = null;
                            }
                        } else if (classSelected == StrokeImpl.class) {
                            // Check to see if stroke has been selected
                            Symbolizer symbolizer = (Symbolizer) parent.getUserObject();

                            if (!SLDTreeLeafFactory.getInstance().hasStroke(symbolizer)) {
                                parentClass = null;
                                classSelected = null;
                            }
                        }
                    }
                    displayPanel.show(parentClass, classSelected);
                    Controller.getInstance().setPopulating(false);
                }

                if (renderList != null) {
                    for (RenderSymbolInterface render : renderList) {
                        render.renderSymbol();
                    }
                }
            }
        }

        if (treeTools != null) {
            treeTools.setButtonState(parent, node, this.currentGeometryType);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.undo.UndoActionInterface#undoAction(com.sldeditor.common.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            repopulateTree((String) undoRedoObject.getOldValue());
        }
    }

    /**
     * Repopulate tree for a undo/redo operation.
     *
     * @param sldContents the sld contents
     */
    private void repopulateTree(String sldContents) {
        int[] selectedRows = symbolTree.getSelectionRows();

        SLDDataInterface sldData = new SLDData(null, sldContents);
        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        SelectedSymbol.getInstance().setSld(sld);

        populateSLD();

        if ((selectedRows != null) && (selectedRows.length > 0)) {
            symbolTree.setSelectionRow(selectedRows[0]);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.undo.UndoActionInterface#redoAction(com.sldeditor.common.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            repopulateTree((String) undoRedoObject.getNewValue());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.tree.UpdateTreeStructureInterface#getUndoObject()
     */
    @Override
    public UndoActionInterface getUndoObject() {
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }
}
