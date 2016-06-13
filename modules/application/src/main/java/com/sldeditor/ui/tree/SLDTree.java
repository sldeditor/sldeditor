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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

import org.apache.log4j.Logger;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.FillImpl;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.StrokeImpl;
import org.geotools.styling.Style;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.StyledLayerDescriptorImpl;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizerImpl;

import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.Controller;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDTreeUpdatedInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.SelectedTreeItemEnum;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.render.RenderSymbol;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.SymbolSelectedInterface;
import com.sldeditor.ui.iface.SymbolizerSelectedInterface;
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
 * The component that displays the structure of the SLD as a tree.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDTree extends JPanel implements TreeSelectionListener, SLDTreeUpdatedInterface, DataSourceUpdatedInterface, UndoActionInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The logger. */
    private static Logger logger = Logger.getLogger(RenderSymbol.class);

    /** The symbol marker tree. */
    private JTree symbolTree = null;

    /** The root node. */
    private DefaultMutableTreeNode rootNode = null;

    /** The tree model. */
    private DefaultTreeModel treeModel = null;

    /** The tree item map. */
    protected static Map<Class<?>, SLDTreeItemInterface> treeItemMap = new HashMap<Class<?>, SLDTreeItemInterface>();

    /** The display panel. */
    private SymbolizerSelectedInterface displayPanel = null;

    /** The rule tree item. */
    private RuleTreeItem ruleTreeItem = new RuleTreeItem();

    /** The symbolizer tree item. */
    private SymbolizerTreeItem symbolizerTreeItem = new SymbolizerTreeItem();

    /** The node map. */
    private Map<Object, DefaultMutableTreeNode> nodeMap = new HashMap<Object, DefaultMutableTreeNode>();

    /** The object to render the selected symbol. */
    private List<RenderSymbolInterface> renderList = null;

    /** The new text button. */
    private JButton btnNewText;

    /** The new raster button. */
    private JButton btnNewRaster;

    /** The remove marker button. */
    private JButton btnRemoveMarker;

    /** The new polygon button. */
    private JButton btnNewPolygon;

    /** The new line button. */
    private JButton btnNewLine;

    /** The new marker button. */
    private JButton btnNewMarker;

    /** The move up button. */
    private JButton btnMoveUp;

    /** The move down button. */
    private JButton btnMoveDown;

    /** The current geometry type of the loaded data source. */
    private GeometryTypeEnum currentGeometryType = GeometryTypeEnum.UNKNOWN;

    /** The add button. */
    private JButton btnAddButton;

    /** The sld writer. */
    private SLDWriterInterface sldWriter = SLDWriterFactory.createSLDWriter(null);

    /**
     * Instantiates a new SLD tree.
     *
     * @param renderList the render list
     */
    public SLDTree(List<RenderSymbolInterface> renderList)
    {
        this.renderList = renderList;

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.addListener(this);

        for(RenderSymbolInterface render : renderList)
        {
            if(render instanceof DataSourceUpdatedInterface)
            {
                dataSource.addListener(render);
            }
        }

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
        ComponentCellRenderer cellRenderer = new ComponentCellRenderer(symbolTree.getCellRenderer());
        symbolTree.setCellRenderer(cellRenderer);
        symbolTree.setCellEditor(new CheckBoxNodeEditor(symbolTree, cellRenderer, this));
        symbolTree.setEditable(true);
        symbolTree.setRowHeight(0);

        // Listen for when the selection changes.
        symbolTree.addTreeSelectionListener(this);
        panelSymbolMarkerTree.setLayout(new BorderLayout(0, 0));

        scrollpane.setViewportView(symbolTree);

        panelSymbolMarkerTree.add(scrollpane);

        JPanel panelMarkerSymbolItems = new JPanel();
        add(panelMarkerSymbolItems);
        panelMarkerSymbolItems.setLayout(new BoxLayout(panelMarkerSymbolItems, BoxLayout.X_AXIS));

        btnAddButton = new JButton("");
        btnAddButton.setIcon(getResourceIcon("button/add.png"));
        btnAddButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewThing();
            }
        });
        panelMarkerSymbolItems.add(btnAddButton);

        btnNewMarker = new JButton();
        btnNewMarker.setIcon(getResourceIcon("button/point.png"));
        btnNewMarker.setEnabled(false);
        btnNewMarker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewMarker();
            }
        });

        panelMarkerSymbolItems.add(btnNewMarker);

        btnNewLine = new JButton();
        btnNewLine.setIcon(getResourceIcon("button/line.png"));
        btnNewLine.setEnabled(false);
        btnNewLine.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewLine();
            }
        });
        panelMarkerSymbolItems.add(btnNewLine);

        btnNewPolygon = new JButton();
        btnNewPolygon.setIcon(getResourceIcon("button/polygon.png"));
        btnNewPolygon.setEnabled(false);
        btnNewPolygon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewPolygon();
            }
        });
        panelMarkerSymbolItems.add(btnNewPolygon);

        btnNewText = new JButton();
        btnNewText.setIcon(getResourceIcon("button/text.png"));
        btnNewText.setEnabled(false);
        btnNewText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewText();
            }
        });
        panelMarkerSymbolItems.add(btnNewText);

        btnNewRaster = new JButton();
        btnNewRaster.setIcon(getResourceIcon("button/raster.png"));
        btnNewRaster.setEnabled(false);
        btnNewRaster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRaster();
            }
        });
        panelMarkerSymbolItems.add(btnNewRaster);

        btnRemoveMarker = new JButton();
        btnRemoveMarker.setIcon(getResourceIcon("button/delete.png"));
        btnRemoveMarker.setEnabled(false);
        btnRemoveMarker.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeItem();
            }
        });
        panelMarkerSymbolItems.add(btnRemoveMarker);

        btnMoveUp = new JButton();
        btnMoveUp.setIcon(getResourceIcon("button/up.png"));
        btnMoveUp.setEnabled(false);
        btnMoveUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveItemUp();
            }
        });
        panelMarkerSymbolItems.add(btnMoveUp);

        btnMoveDown = new JButton();
        btnMoveDown.setIcon(getResourceIcon("button/down.png"));
        btnMoveDown.setEnabled(false);
        btnMoveDown.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveItemDown();
            }
        });
        panelMarkerSymbolItems.add(btnMoveDown);
    }

    /**
     * Gets the resource icon.
     *
     * @param resourceString the resource string
     * @return the resource icon
     */
    private static ImageIcon getResourceIcon(String resourceString)
    {
        URL url = SLDTree.class.getClassLoader().getResource(resourceString);

        if(url == null)
        {
            ConsoleManager.getInstance().error(SLDTree.class, Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource") + resourceString);
            return null;
        }
        else
        {
            return new ImageIcon(url);
        }
    }

    /**
     * Removes the item.
     */
    private void removeItem() {
        TreePath path = symbolTree.getSelectionPath();

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = lastNode.getUserObject();

        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        if(obj instanceof StyledLayerDescriptor)
        {
            SelectedSymbol.getInstance().removeStyledLayerDescriptor((StyledLayerDescriptor)obj);
            removeTreeNode(lastNode);
        }
        else if(obj instanceof NamedLayer)
        {
            SelectedSymbol.getInstance().removeNamedLayer((NamedLayer)obj);
            removeTreeNode(lastNode);
        }
        else if(obj instanceof Style)
        {
            SelectedSymbol.getInstance().removeStyle((Style)obj);
            removeTreeNode(lastNode);
        }
        else if(obj instanceof FeatureTypeStyle)
        {
            SelectedSymbol.getInstance().removeFeatureTypeStyle((FeatureTypeStyle)obj);
            removeTreeNode(lastNode);
        }
        else if(obj instanceof Rule)
        {
            SelectedSymbol.getInstance().removeRule((Rule)obj);
            removeTreeNode(lastNode);
        }
        else if(obj instanceof Symbolizer)
        {
            SelectedSymbol.getInstance().removeSymbolizer((Symbolizer)obj);
            removeTreeNode(lastNode);
        }

        for(RenderSymbolInterface render : renderList)
        {
            render.renderSymbol();
        }

        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        final UndoActionInterface parentObj = (UndoActionInterface) this;

        UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Removes the tree node.
     *
     * @param nodeToRemove the node to remove
     */
    private void removeTreeNode(DefaultMutableTreeNode nodeToRemove) {

        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodeToRemove.getParent();
        nodeMap.remove(nodeToRemove.getUserObject());
        treeModel.removeNodeFromParent(nodeToRemove);
        treeModel.nodeChanged(parent);

        symbolTree.setSelectionPath(getPath(parent));
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
     * Adds the new thing.
     */
    private void addNewThing() {
        TreePath path = symbolTree.getSelectionPath();

        DefaultMutableTreeNode newNode = null;

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = lastNode.getUserObject();

        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        if(obj instanceof String)
        {
            StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();

            SelectedSymbol.getInstance().createNewSLD(sld);

            NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();

            SelectedSymbol.getInstance().addNewStyledLayer(namedLayer);
            newNode = addObject(lastNode, namedLayer, true);
        }
        else if(obj instanceof StyledLayerDescriptor)
        {
            NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();

            SelectedSymbol.getInstance().addNewStyledLayer(namedLayer);
            newNode = addObject(lastNode, namedLayer, true);
        }
        else if(obj instanceof NamedLayer)
        {
            Style style = DefaultSymbols.createNewStyle();

            SelectedSymbol.getInstance().addNewStyle(style);
            newNode = addObject(lastNode, style, true);
        }
        else if(obj instanceof Style)
        {
            FeatureTypeStyle featureTypeStyle = DefaultSymbols.createNewFeatureTypeStyle();

            SelectedSymbol.getInstance().addNewFeatureTypeStyle(featureTypeStyle);

            newNode = addObject(lastNode, featureTypeStyle, true);
        }
        else if(obj instanceof FeatureTypeStyle)
        {
            Rule rule = DefaultSymbols.createNewRule();

            SelectedSymbol.getInstance().addNewRule(rule);
            newNode = addObject(lastNode, rule, true);
        }

        // Select the item just added
        if(newNode != null)
        {
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);
        }

        // Store current state of the SLD after the add
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Gets the rule tree node.
     *
     * @return the rule tree node
     */
    private DefaultMutableTreeNode getRuleTreeNode() {
        TreePath path = symbolTree.getSelectionPath();

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = lastNode.getUserObject();

        if(obj instanceof Symbolizer)
        {
            return (DefaultMutableTreeNode) lastNode.getParent();
        }
        else if(obj instanceof Rule)
        {
            return (DefaultMutableTreeNode) lastNode;
        }
        return rootNode;
    }

    /**
     * Adds the new marker symbolizer.
     */
    private void addNewMarker() {
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        PointSymbolizer newPointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();

        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newPointSymbolizer);
        DefaultMutableTreeNode newNode = addObject(ruleNode, newPointSymbolizer, true); 

        // Select the item just added
        if(newNode != null)
        {
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);
        }

        // Store current state of the SLD after the add
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Adds the new raster symbolizer.
     */
    private void addRaster() {
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        RasterSymbolizer newRasterSymbolizer = DefaultSymbols.createDefaultRasterSymbolizer();
        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newRasterSymbolizer);
        DefaultMutableTreeNode newNode = addObject(ruleNode, newRasterSymbolizer, true); 

        // Select the item just added
        if(newNode != null)
        {
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);
        }

        // Store current state of the SLD after the add
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Adds the new text symbolizer.
     */
    private void addNewText() {

        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        TextSymbolizer newTextSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newTextSymbolizer);
        DefaultMutableTreeNode newNode = addObject(ruleNode, newTextSymbolizer, true); 

        // Select the item just added
        if(newNode != null)
        {
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);
        }

        // Store current state of the SLD after the add
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Adds the new line symbolizer.
     */
    private void addNewLine() {
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        LineSymbolizer newLineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();

        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newLineSymbolizer);
        DefaultMutableTreeNode newNode = addObject(ruleNode, newLineSymbolizer, true); 

        if(newNode != null)
        {
            addObject(newNode, SLDTreeLeafFactory.getInstance().getStroke(newLineSymbolizer), true);

            // Select the item just added
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);
        }

        // Store current state of the SLD after the add
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Adds the new polygon symbolizer.
     */
    private void addNewPolygon() {
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        PolygonSymbolizer newPolygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newPolygonSymbolizer);
        DefaultMutableTreeNode newNode = addObject(ruleNode, newPolygonSymbolizer, true); 

        if(newNode != null)
        {
            addObject(newNode, SLDTreeLeafFactory.getInstance().getFill(newPolygonSymbolizer), true);
            addObject(newNode, SLDTreeLeafFactory.getInstance().getStroke(newPolygonSymbolizer), true);

            // Select the item just added
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);
        }

        // Store current state of the SLD after the add
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
    }

    /**
     * Move item down.
     */
    private void moveItemDown() {
        moveItem(1);
    }

    /**
     * Move item up.
     */
    private void moveItemUp() {
        moveItem(-1);
    }

    /**
     * Move item within a list.  The direction parameter determines which way the item is moved.
     *
     * @param direction the direction is either up (-1) or down (+1)
     */
    private void moveItem(int direction) {
        TreePath path = symbolTree.getSelectionPath();

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if(lastNode == null)
        {
            return;
        }

        Object obj = lastNode.getUserObject();
        if(obj == null)
        {
            return;
        }

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode)lastNode.getParent();

        if(parentNode == null)
        {
            return;
        }
        Object parentObj = parentNode.getUserObject();

        if(parentObj == null)
        {
            return;
        }

        // Store current state of the SLD before the move
        Object oldValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        if(obj instanceof StyledLayer)
        {
            StyledLayerDescriptor sld = (StyledLayerDescriptor) parentObj;
            int index = sld.layers().indexOf(obj);

            StyledLayer styledLayer = sld.layers().remove(index);
            sld.layers().add(index + direction, styledLayer);

            treeModel.removeNodeFromParent(lastNode);
            treeModel.insertNodeInto(lastNode, parentNode, index + direction);
        }
        else if(obj instanceof Style)
        {
            if(obj instanceof NamedLayerImpl)
            {
                NamedLayerImpl namedLayer = (NamedLayerImpl) parentObj;
                int index = namedLayer.styles().indexOf(obj);

                Style style = namedLayer.styles().remove(index);
                namedLayer.styles().add(index + direction, style);

                treeModel.removeNodeFromParent(lastNode);
                treeModel.insertNodeInto(lastNode, parentNode, index + direction);
            }
        }
        else if(obj instanceof FeatureTypeStyle)
        {
            Style style = (Style) parentObj;
            int index = style.featureTypeStyles().indexOf(obj);

            FeatureTypeStyle fts = style.featureTypeStyles().remove(index);
            style.featureTypeStyles().add(index + direction, fts);

            treeModel.removeNodeFromParent(lastNode);
            treeModel.insertNodeInto(lastNode, parentNode, index + direction);
        }
        else if(obj instanceof Rule)
        {
            FeatureTypeStyle fts = (FeatureTypeStyle) parentObj;
            int index = fts.rules().indexOf(obj);

            Rule rule = fts.rules().remove(index);
            fts.rules().add(index + direction, rule);

            treeModel.removeNodeFromParent(lastNode);
            treeModel.insertNodeInto(lastNode, parentNode, index + direction);
        }
        else if(obj instanceof Symbolizer)
        {
            Rule rule = (Rule) parentObj;
            int index = rule.symbolizers().indexOf(obj);

            Symbolizer symbolizer = rule.symbolizers().remove(index);
            rule.symbolizers().add(index + direction, symbolizer);

            treeModel.removeNodeFromParent(lastNode);
            treeModel.insertNodeInto(lastNode, parentNode, index + direction);
        }

        // Refresh the tree structure. Not very efficient but gets result wanted.
        // The node has been moved in the tree above.  Now going to refresh model.
        treeModel.nodeStructureChanged(lastNode);

        // Get path for item moved
        TreePath newNodePath = getPath(lastNode);
        int[] selectedRows = new int[1];
        selectedRows[0] = symbolTree.getRowForPath(newNodePath);

        // Find the row of item moved

        // Now clear tree structure and re-populate, inefficient but it means
        // that all items are expanded as required.
        populateSLD();

        //  Make item moved selected again
        symbolTree.setSelectionRows(selectedRows);

        for(RenderSymbolInterface render : renderList)
        {
            render.renderSymbol();
        }

        // Store current state of the SLD after the move
        Object newValueObj = sldWriter.encodeSLD(SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getClass().getName(), oldValueObj, newValueObj));
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
     * Adds the overall selected listener.
     *
     * @param toAdd the to add
     */
    public void addOverallSelectedListener(SymbolSelectedInterface toAdd)
    {
        ruleTreeItem.addOverallSelectedListener(toAdd);
        symbolizerTreeItem.addOverallSelectedListener(toAdd);
    }

    /**
     * Reset the tree structure
     */
    private void reset() {
        rootNode.removeAllChildren(); // This removes all nodes
        treeModel.reload(); // This notifies the listeners and changes the GUI
    }

    /**
     *  Add child to the currently selected node.
     *
     * @param child the child
     * @return the default mutable tree node
     */
    @SuppressWarnings("unused")
    private DefaultMutableTreeNode addObject(Object child) {
        DefaultMutableTreeNode parentNode = null;
        TreePath parentPath = symbolTree.getSelectionPath();

        if (parentPath == null) {
            parentNode = rootNode;
        } else {
            parentNode = (DefaultMutableTreeNode)(parentPath.getLastPathComponent());
        }

        return addObject(parentNode, child, true);
    }

    /**
     * Adds the object.
     *
     * @param parent the parent
     * @param child the child
     * @return the default mutable tree node
     */
    @SuppressWarnings("unused")
    private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent, Object child) {
        return addObject(parent, child, false);
    }

    /**
     * Adds the object.
     *
     * @param parent the parent
     * @param child the child
     * @param shouldBeVisible the should be visible
     * @return the default mutable tree node
     */
    private DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,
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
            symbolTree.scrollPathToVisible(new TreePath(childNode.getPath()));
        }
        return childNode;
    }

    /**
     * Populate the tree with the SLD structure
     */
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

                                            if((symbolizer instanceof PointSymbolizer) || (symbolizer instanceof PolygonSymbolizer))
                                            {
                                                addObject(symbolizerTreeNode, SLDTreeLeafFactory.getInstance().getFill(symbolizer), true);
                                            }

                                            if((symbolizer instanceof PolygonSymbolizer) || (symbolizer instanceof LineSymbolizer))
                                            {
                                                addObject(symbolizerTreeNode, SLDTreeLeafFactory.getInstance().getStroke(symbolizer), true);
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

    /* (non-Javadoc)
     * @see javax.swing.event.TreeSelectionListener#valueChanged(javax.swing.event.TreeSelectionEvent)
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        leafSelected();
    }

    /**
     * Sets the buttons state.
     *
     * @param parentNode the parent node
     * @param selectedNode the new button state
     */
    private void setButtonState(DefaultMutableTreeNode parentNode, DefaultMutableTreeNode selectedNode) {
        boolean symbolizerButtonsEnabled = false;
        boolean addButtonEnabled = true;
        boolean hasMoreThan1Item = false;
        boolean isFirstSelected = false;
        boolean isLastSelected = false;

        if(selectedNode != null)
        {
            Object obj = selectedNode.getUserObject();
            Object parentObj = null;

            if(parentNode != null)
            {
                parentObj = parentNode.getUserObject();
            }

            if(obj instanceof StyledLayer)
            {
                if(parentObj != null)
                {
                    StyledLayerDescriptor sld = (StyledLayerDescriptor) parentObj;
                    hasMoreThan1Item = sld.layers().size() > 1;
                    isFirstSelected = (obj == sld.layers().get(0));
                    isLastSelected = (obj == sld.layers().get(sld.layers().size() - 1));
                }
            }
            else if(obj instanceof Style)
            {
                if(obj instanceof NamedLayerImpl)
                {
                    if(parentObj != null)
                    {
                        NamedLayerImpl namedLayer = (NamedLayerImpl) parentObj;
                        hasMoreThan1Item = namedLayer.styles().size() > 1;
                        isFirstSelected = (obj == namedLayer.styles().get(0));
                        isLastSelected = (obj == namedLayer.styles().get(namedLayer.styles().size() - 1));
                    }
                }
            }
            else if(obj instanceof FeatureTypeStyle)
            {
                if(parentObj != null)
                {
                    Style style = (Style) parentObj;
                    hasMoreThan1Item = style.featureTypeStyles().size() > 1;
                    isFirstSelected = (obj == style.featureTypeStyles().get(0));
                    isLastSelected = (obj == style.featureTypeStyles().get(style.featureTypeStyles().size() - 1));
                }
            }
            else if(obj instanceof Rule)
            {
                symbolizerButtonsEnabled = true;
                addButtonEnabled = false;

                if(parentObj != null)
                {
                    FeatureTypeStyle fts = (FeatureTypeStyle) parentObj;
                    hasMoreThan1Item = fts.rules().size() > 1;
                    isFirstSelected = (obj == fts.rules().get(0));
                    isLastSelected = (obj == fts.rules().get(fts.rules().size() - 1));
                }
            }
            else if(obj instanceof Symbolizer)
            {
                symbolizerButtonsEnabled = true;
                addButtonEnabled = false;

                if(parentObj != null)
                {
                    Rule rule = (Rule) parentObj;
                    hasMoreThan1Item = rule.symbolizers().size() > 1;
                    isFirstSelected = (obj == rule.symbolizers().get(0));
                    isLastSelected = (obj == rule.symbolizers().get(rule.symbolizers().size() - 1));
                }
            }
        }

        this.btnAddButton.setEnabled(addButtonEnabled);

        if(symbolizerButtonsEnabled == false)
        {
            btnNewMarker.setEnabled(false);
            btnNewLine.setEnabled(false);
            btnNewPolygon.setEnabled(false);
            btnNewRaster.setEnabled(false);
            btnNewText.setEnabled(false);
        }
        else
        {
            btnNewMarker.setEnabled(this.currentGeometryType == GeometryTypeEnum.POINT);
            btnNewLine.setEnabled(this.currentGeometryType == GeometryTypeEnum.LINE);
            btnNewPolygon.setEnabled(this.currentGeometryType == GeometryTypeEnum.POLYGON);
            btnNewRaster.setEnabled(this.currentGeometryType == GeometryTypeEnum.RASTER);
            btnNewText.setEnabled(true);
        }

        btnRemoveMarker.setEnabled(true);

        // Up / down buttons
        btnMoveUp.setEnabled(hasMoreThan1Item && !isFirstSelected);
        btnMoveDown.setEnabled(hasMoreThan1Item && !isLastSelected);
    }

    /**
     * Select first symbol.
     */
    public void selectFirstSymbol() {
        if(SelectedSymbol.getInstance().getSld() != null)
        {
            symbolTree.setSelectionRow(0);
        }
        else
        {
            // No SLD loaded
            if(displayPanel != null)
            {
                displayPanel.show(null, null);
            }

            for(RenderSymbolInterface render : renderList)
            {
                render.renderSymbol();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeUpdatedInterface#textUpdated()
     */
    @Override
    public void textUpdated() {

        logger.debug("Refreshing tree");

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
        DefaultMutableTreeNode node = null;

        for(DefaultMutableTreeNode n : nodeMap.values())
        {
            Object o = n.getUserObject();

            if(o == objectOld)
            {
                node = n;
                break;
            }
        }

        if(node != null)
        {
            node.setUserObject(objectNew);
            nodeMap.remove(objectOld);
            nodeMap.put(objectNew, node);
            treeModel.nodeChanged(node);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag) {
        currentGeometryType = geometryType;
    }

    /**
     * Select tree item.
     *
     * @param data the data
     * @return true, if successful
     */
    public boolean selectTreeItem(TreeSelectionData data)
    {
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

        if((layerIndex < 0) || (layerIndex >= rootNode.getChildCount()))
        {
            return false;
        }

        layerNode = (DefaultMutableTreeNode)rootNode.getChildAt(layerIndex);

        if(selectedTreeItemEnum == SelectedTreeItemEnum.LAYER)
        {
            path = getPath(layerNode);
        }
        else
        {
            if(layerNode == null)
            {
                return false;
            }
            if((styleIndex < 0) || (styleIndex >= layerNode.getChildCount()))
            {
                return false;
            }

            styleNode = (DefaultMutableTreeNode)layerNode.getChildAt(styleIndex);
            if(selectedTreeItemEnum == SelectedTreeItemEnum.STYLE)
            {
                path = getPath(styleNode);
            }
            else
            {
                if(styleNode == null)
                {
                    return false;  
                }

                if((featureTypeStyleIndex < 0) || (featureTypeStyleIndex >= styleNode.getChildCount()))
                {
                    return false;
                }

                ftsNode = (DefaultMutableTreeNode)styleNode.getChildAt(featureTypeStyleIndex);
                if(selectedTreeItemEnum == SelectedTreeItemEnum.FEATURETYPESTYLE)
                {
                    path = getPath(ftsNode);
                }
                else
                {
                    if(ftsNode == null)
                    {
                        return false;
                    }
                    if((ruleIndex < 0) || (ruleIndex >= ftsNode.getChildCount()))
                    {
                        return false;
                    }

                    ruleNode = (DefaultMutableTreeNode)ftsNode.getChildAt(ruleIndex);
                    if(selectedTreeItemEnum == SelectedTreeItemEnum.RULE)
                    {
                        path = getPath(ruleNode);
                    }
                    else
                    {
                        if(ruleNode == null)
                        {
                            return false;
                        }
                        if((symbolizerIndex < 0) || (symbolizerIndex >= ruleNode.getChildCount()))
                        {
                            return false;
                        }

                        symbolizerNode = (DefaultMutableTreeNode)ruleNode.getChildAt(symbolizerIndex);

                        if((selectedTreeItemEnum == SelectedTreeItemEnum.POINT_SYMBOLIZER) ||
                                (selectedTreeItemEnum == SelectedTreeItemEnum.LINE_SYMBOLIZER) ||
                                (selectedTreeItemEnum == SelectedTreeItemEnum.POLYGON_SYMBOLIZER) ||
                                (selectedTreeItemEnum == SelectedTreeItemEnum.RASTER_SYMBOLIZER) ||
                                (selectedTreeItemEnum == SelectedTreeItemEnum.TEXT_SYMBOLIZER))
                        {
                            path = getPath(symbolizerNode);
                        }
                        else
                        {
                            symbolizerDetailNode = (DefaultMutableTreeNode)symbolizerNode.getChildAt(symbolizerDetailIndex);

                            if(symbolizerDetailNode == null)
                            {
                                return false;
                            }
                            if((symbolizerDetailIndex < 0) || (symbolizerDetailIndex >= symbolizerNode.getChildCount()))
                            {
                                return false;
                            }

                            if((selectedTreeItemEnum == SelectedTreeItemEnum.POINT_FILL) || 
                                    (selectedTreeItemEnum == SelectedTreeItemEnum.POLYGON_FILL) || 
                                    (selectedTreeItemEnum == SelectedTreeItemEnum.STROKE))
                            {
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
    public PopulateDetailsInterface getSelectedSymbolPanel()
    {
        PopulateDetailsInterface panel = null;
        TreePath path = symbolTree.getSelectionPath();

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();

        if(lastNode != null)
        {
            Object nodeInfo = lastNode.getUserObject();
            if(nodeInfo != null)
            {
                Class<?> classSelected = nodeInfo.getClass();
                String key = classSelected.toString();

                Class<?> parentClass = null;
                if(lastNode.getParent() != null)
                {
                    DefaultMutableTreeNode parent = (DefaultMutableTreeNode) lastNode.getParent();

                    parentClass = parent.getUserObject().getClass();
                }

                panel = displayPanel.getPanel(parentClass, key);
            }
        }

        return panel;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.tree.SLDTreeUpdatedInterface#leafSelected()
     */
    @Override
    public void leafSelected() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)symbolTree.getLastSelectedPathComponent();
        DefaultMutableTreeNode parent = null;

        if (node != null)
        {
            Object nodeInfo = node.getUserObject();
            if(nodeInfo != null)
            {
                SLDTreeItemInterface treeItem = treeItemMap.get(nodeInfo.getClass());

                if(treeItem != null)
                {
                    treeItem.itemSelected(node, nodeInfo);
                }

                if(displayPanel != null)
                {
                    Controller.getInstance().setPopulating(true);

                    Class<?> parentClass = null;
                    if(node.getParent() != null)
                    {
                        parent = (DefaultMutableTreeNode) node.getParent();

                        parentClass = parent.getUserObject().getClass();
                    }
                    displayPanel.show(parentClass, nodeInfo.getClass());
                    Controller.getInstance().setPopulating(false);
                }

                for(RenderSymbolInterface render : renderList)
                {
                    render.renderSymbol();
                }
            }
        }
        setButtonState(parent, node);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.undo.UndoActionInterface#undoAction(com.sldeditor.common.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if(undoRedoObject != null)
        {
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

        if(selectedRows != null)
        {
            symbolTree.setSelectionRow(selectedRows[0]);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.undo.UndoActionInterface#redoAction(com.sldeditor.common.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if(undoRedoObject != null)
        {
            repopulateTree((String) undoRedoObject.getNewValue());
        }
    }

}
