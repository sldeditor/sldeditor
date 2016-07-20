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

package com.sldeditor.geometryfield.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.FillImpl;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.StyledLayerDescriptorImpl;
import org.geotools.styling.TextSymbolizerImpl;

import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.ui.tree.UpdateTreeStructureInterface;
import com.sldeditor.ui.tree.item.FeatureTypeStyleTreeItem;
import com.sldeditor.ui.tree.item.FillTreeItem;
import com.sldeditor.ui.tree.item.NameLayerTreeItem;
import com.sldeditor.ui.tree.item.RuleTreeItem;
import com.sldeditor.ui.tree.item.SLDTreeItemInterface;
import com.sldeditor.ui.tree.item.StrokeTreeItem;
import com.sldeditor.ui.tree.item.StyleTreeItem;
import com.sldeditor.ui.tree.item.StyledLayerDescriptorTreeItem;
import com.sldeditor.ui.tree.item.SymbolizerTreeItem;

/**
 * The Class GeometryFieldTree.
 *
 * @author Robert Ward (SCISYS)
 */
public class GeometryFieldTree extends JPanel implements TreeSelectionListener, SLDTreeUpdatedInterface, UpdateTreeStructureInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The logger. */
    private static Logger logger = Logger.getLogger(GeometryFieldTree.class);

    /** The sld symbol structure tree. */
    private JTree geometryFieldTree = null;

    /** The root node. */
    protected DefaultMutableTreeNode rootNode = null;

    /** The tree model. */
    private DefaultTreeModel treeModel = null;

    /** The node map. */
    private Map<Object, DefaultMutableTreeNode> nodeMap = new HashMap<Object, DefaultMutableTreeNode>();

    /** The tree item map. */
    protected static Map<Class<?>, SLDTreeItemInterface> treeItemMap = new HashMap<Class<?>, SLDTreeItemInterface>();

    /** The rule tree item. */
    private RuleTreeItem ruleTreeItem = new RuleTreeItem();

    /** The symbolizer tree item. */
    private SymbolizerTreeItem symbolizerTreeItem = new SymbolizerTreeItem();

    /** The object to render the selected symbol. */
    private List<RenderSymbolInterface> renderList = null;

    /**
     * Instantiates a new geometry field tree.
     *
     * @param renderList the render list
     */
    public GeometryFieldTree(List<RenderSymbolInterface> renderList)
    {
        this.renderList = renderList;

        createTreeItemMap();
        createUI();
    }

    /**
     * Creates the tree item map.
     */
    private void createTreeItemMap() {

        if(treeItemMap.isEmpty())
        {
            /** The sld tree item. */
            StyledLayerDescriptorTreeItem sldTreeItem = new StyledLayerDescriptorTreeItem();

            /** The style tree item. */
            StyleTreeItem styleTreeItem = new StyleTreeItem();

            /** The fts tree item. */
            FeatureTypeStyleTreeItem ftsTreeItem = new FeatureTypeStyleTreeItem();

            /** The name layer tree item. */
            NameLayerTreeItem nameLayerTreeItem = new NameLayerTreeItem();

            /** The fill tree item. */
            FillTreeItem fillTreeItem = new FillTreeItem();

            /** The stroke tree item. */
            StrokeTreeItem strokeTreeItem = new StrokeTreeItem();

            treeItemMap.put(StyledLayerDescriptorImpl.class, sldTreeItem);
            treeItemMap.put(StyleImpl.class, styleTreeItem);
            treeItemMap.put(FeatureTypeStyleImpl.class, ftsTreeItem);
            treeItemMap.put(RuleImpl.class, ruleTreeItem);
            treeItemMap.put(PointSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(LineSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(PolygonSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(TextSymbolizerImpl.class, symbolizerTreeItem);
            treeItemMap.put(NamedLayerImpl.class, nameLayerTreeItem);
            treeItemMap.put(StrokeImpl.class, strokeTreeItem);
            treeItemMap.put(FillImpl.class, fillTreeItem);
            treeItemMap.put(RasterSymbolizerImpl.class, symbolizerTreeItem);
        }
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        setBorder(new LineBorder(new Color(0, 0, 0)));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel panelSymbolMarkerTree = new JPanel();
        add(panelSymbolMarkerTree);

        JScrollPane scrollpane = new JScrollPane();

        rootNode = new DefaultMutableTreeNode("SLD");
        treeModel = new DefaultTreeModel(rootNode);
        geometryFieldTree = new JTree(treeModel);
        geometryFieldTree.setEditable(true);
        geometryFieldTree.setBorder(new LineBorder(Color.black));
        ComponentCellRenderer cellRenderer = new ComponentCellRenderer(geometryFieldTree.getCellRenderer());
        geometryFieldTree.setCellRenderer(cellRenderer);
        geometryFieldTree.setCellEditor(new CheckBoxNodeEditor(geometryFieldTree, cellRenderer, this));
        geometryFieldTree.setEditable(true);
        geometryFieldTree.setRowHeight(0);
        geometryFieldTree.setShowsRootHandles(true);
        geometryFieldTree.addTreeWillExpandListener(new TreeWillExpandListener(){

            @Override
            public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {
                throw new ExpandVetoException(e);
            }

            @Override
            public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
            }});
        // Listen for when the selection changes.
        geometryFieldTree.addTreeSelectionListener(this);
        panelSymbolMarkerTree.setLayout(new BorderLayout(0, 0));

        scrollpane.setViewportView(geometryFieldTree);

        panelSymbolMarkerTree.add(scrollpane);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    @Override
    public void valueChanged(TreeSelectionEvent arg0) {
        leafSelected();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.SLDTreeUpdatedInterface#textUpdated()
     */
    @Override
    public void textUpdated() {
        int[] selectedRows = geometryFieldTree.getSelectionRows();
        populateSLD();
        geometryFieldTree.setSelectionRows(selectedRows);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.SLDTreeUpdatedInterface#updateNode(java.lang.Object, java.lang.Object)
     */
    @Override
    public void updateNode(Object objectOld, Object objectNew) {

    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.data.SLDTreeUpdatedInterface#leafSelected()
     */
    @Override
    public void leafSelected() {
    }

    /**
     * Reset the tree structure.
     */
    private void reset() {
        rootNode.removeAllChildren(); // This removes all nodes
        treeModel.reload(); // This notifies the listeners and changes the GUI
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.UpdateTreeStructureInterface#addObject(javax.swing.tree.DefaultMutableTreeNode, java.lang.Object, boolean)
     */
    @Override
    public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
            Object child, 
            boolean shouldBeVisible) {
        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);

        nodeMap.put(child, childNode);

        if (parent == null) {
            parent = rootNode;
        }

        // It is key to invoke this on the TreeModel, and NOT DefaultMutableTreeNode
        treeModel.insertNodeInto(childNode, parent, 
                parent.getChildCount());

        // Make sure the user can see the lovely new node.
        if (shouldBeVisible) {
            geometryFieldTree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.UpdateTreeStructureInterface#populateSLD()
     */
    @Override
    public void populateSLD()
    {
        reset();

        SelectedSymbol selectedSymbol = SelectedSymbol.getInstance();

        if(selectedSymbol != null)
        {
            StyledLayerDescriptor sld = selectedSymbol.getSld();
            rootNode.setUserObject(sld);
            treeModel.nodeChanged(rootNode);

            if(sld != null)
            {
                List<StyledLayer> styledLayerList = sld.layers();

                if(sld != null)
                {
                    for(StyledLayer styledLayer : styledLayerList)
                    {
                        if(styledLayer instanceof NamedLayerImpl)
                        {
                            NamedLayerImpl namedLayerImpl = (NamedLayerImpl)styledLayer;

                            DefaultMutableTreeNode styledLayerTreeNode = addObject(rootNode, namedLayerImpl, true);

                            for(Style style : namedLayerImpl.styles())
                            {
                                DefaultMutableTreeNode styleTreeNode = addObject(styledLayerTreeNode, style, true);

                                for(FeatureTypeStyle fts : style.featureTypeStyles())
                                {
                                    DefaultMutableTreeNode ftsTreeNode = addObject(styleTreeNode, fts, true);

                                    for(Rule rule : fts.rules())
                                    {
                                        DefaultMutableTreeNode ruleTreeNode = addObject(ftsTreeNode, rule, true);

                                        for(org.opengis.style.Symbolizer symbolizer : rule.symbolizers())
                                        {
                                            DefaultMutableTreeNode symbolizerTreeNode = addObject(ruleTreeNode, symbolizer, true);

                                            DefaultMutableTreeNode geometryNode = addObject(symbolizerTreeNode, symbolizer.getGeometryPropertyName(), true);

                                            geometryNode.setAllowsChildren(false);
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

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.UpdateTreeStructureInterface#getUndoObject()
     */
    @Override
    public UndoActionInterface getUndoObject() {
        return null;
    }
}
