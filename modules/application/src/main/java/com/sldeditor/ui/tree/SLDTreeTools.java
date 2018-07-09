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

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.tree.leaf.SLDTreeLeafFactory;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.tree.item.SLDTreeItemInterface;
import java.awt.Dimension;
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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.UserLayer;

/**
 * The component that displays the structure of the SLD as a tree.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeTools {

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 400;

    /** The symbol marker tree. */
    private JTree symbolTree = null;

    /** The root node. */
    private DefaultMutableTreeNode rootNode = null;

    /** The tree model. */
    private DefaultTreeModel treeModel = null;

    /** The tree item map. */
    protected static Map<Class<?>, SLDTreeItemInterface> treeItemMap =
            new HashMap<Class<?>, SLDTreeItemInterface>();

    /** The node map. */
    private Map<Object, DefaultMutableTreeNode> nodeMap =
            new HashMap<Object, DefaultMutableTreeNode>();

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

    /** The new image outline polygon button. */
    private JButton btnNewImageOutlinePolygon;

    /** The new image outline line button. */
    private JButton btnNewImageOutlineLine;

    /** The new marker button. */
    private JButton btnNewMarker;

    /** The move up button. */
    private JButton btnMoveUp;

    /** The move down button. */
    private JButton btnMoveDown;

    /** The add button. */
    private JButton btnAddButton;

    /** The add named layer button. */
    private JButton btnAddNamedLayerButton;

    /** The add user layer button. */
    private JButton btnAddUserLayerButton;

    /** The source arrow button. */
    private JButton btnSourceArrowButton;

    /** The destination arrow button. */
    private JButton btnDestArrowButton;

    /** The sld writer. */
    private SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

    /** The sld tree. */
    private UpdateTreeStructureInterface sldTree = null;

    /** The button panel. */
    private JPanel buttonPanel;

    /** The object to render the selected symbol. */
    private List<RenderSymbolInterface> renderList = null;

    /** The symbolizer button state. */
    private SLDTreeSymbolizerButtonState symbolizerButtonState = new SLDTreeSymbolizerButtonState();

    /** Instantiates a new SLD tree tool class. */
    public SLDTreeTools() {
        createUI();
    }

    /**
     * Configure the necessary field values.
     *
     * @param sldTree the sld tree
     * @param symbolTree the symbol tree
     * @param treeModel the tree model
     * @param renderList the render list
     */
    public void configure(
            UpdateTreeStructureInterface sldTree,
            JTree symbolTree,
            DefaultTreeModel treeModel,
            List<RenderSymbolInterface> renderList) {
        this.sldTree = sldTree;
        this.symbolTree = symbolTree;
        this.treeModel = treeModel;
        this.renderList = renderList;
    }

    /** Creates the ui. */
    private void createUI() {

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setPreferredSize(new Dimension(PANEL_WIDTH, BasePanel.WIDGET_HEIGHT));
        btnAddButton = new JButton("");
        btnAddButton.setIcon(getResourceIcon("button/add.png"));
        btnAddButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewThing(null);
                    }
                });
        buttonPanel.add(btnAddButton);

        btnAddNamedLayerButton =
                new JButton(Localisation.getString(SLDTreeTools.class, "SLDTreeTools.named"));
        btnAddNamedLayerButton.setIcon(getResourceIcon("button/add.png"));
        btnAddNamedLayerButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewThing(NamedLayer.class);
                    }
                });
        buttonPanel.add(btnAddNamedLayerButton);

        btnAddUserLayerButton =
                new JButton(Localisation.getString(SLDTreeTools.class, "SLDTreeTools.user"));
        btnAddUserLayerButton.setIcon(getResourceIcon("button/add.png"));
        btnAddUserLayerButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewThing(UserLayer.class);
                    }
                });
        buttonPanel.add(btnAddUserLayerButton);

        btnNewMarker = new JButton();
        btnNewMarker.setIcon(getResourceIcon("button/point.png"));
        btnNewMarker.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewMarker();
                    }
                });

        buttonPanel.add(btnNewMarker);

        btnNewLine = new JButton();
        btnNewLine.setIcon(getResourceIcon("button/line.png"));
        btnNewLine.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewLine();
                    }
                });
        buttonPanel.add(btnNewLine);

        btnNewImageOutlineLine = new JButton();
        btnNewImageOutlineLine.setIcon(getResourceIcon("button/imageoutline_line.png"));
        btnNewImageOutlineLine.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewImageOutlineLine();
                    }
                });
        buttonPanel.add(btnNewImageOutlineLine);

        btnNewPolygon = new JButton();
        btnNewPolygon.setIcon(getResourceIcon("button/polygon.png"));
        btnNewPolygon.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewPolygon();
                    }
                });
        buttonPanel.add(btnNewPolygon);

        btnNewImageOutlinePolygon = new JButton();
        btnNewImageOutlinePolygon.setIcon(getResourceIcon("button/imageoutline_polygon.png"));
        btnNewImageOutlinePolygon.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewImageOutlinePolygon();
                    }
                });
        buttonPanel.add(btnNewImageOutlinePolygon);

        btnNewText = new JButton();
        btnNewText.setIcon(getResourceIcon("button/text.png"));
        btnNewText.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewText();
                    }
                });
        buttonPanel.add(btnNewText);

        btnNewRaster = new JButton();
        btnNewRaster.setIcon(getResourceIcon("button/raster.png"));
        btnNewRaster.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addNewRaster();
                    }
                });
        buttonPanel.add(btnNewRaster);

        btnRemoveMarker = new JButton();
        btnRemoveMarker.setIcon(getResourceIcon("button/delete.png"));
        btnRemoveMarker.setEnabled(true);
        btnRemoveMarker.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeItem();
                    }
                });
        buttonPanel.add(btnRemoveMarker);

        btnMoveUp = new JButton();
        btnMoveUp.setIcon(getResourceIcon("button/up.png"));
        btnMoveUp.setEnabled(false);
        btnMoveUp.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveItemUp();
                    }
                });
        buttonPanel.add(btnMoveUp);

        btnMoveDown = new JButton();
        btnMoveDown.setIcon(getResourceIcon("button/down.png"));
        btnMoveDown.setEnabled(false);
        btnMoveDown.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveItemDown();
                    }
                });
        buttonPanel.add(btnMoveDown);

        btnSourceArrowButton = new JButton();
        btnSourceArrowButton.setIcon(getResourceIcon("button/srcArrow.png"));
        btnSourceArrowButton.setEnabled(true);
        btnSourceArrowButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addSourceArrow();
                    }
                });
        buttonPanel.add(btnSourceArrowButton);

        btnDestArrowButton = new JButton();
        btnDestArrowButton.setIcon(getResourceIcon("button/destArrow.png"));
        btnDestArrowButton.setEnabled(true);
        btnDestArrowButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        addDestArrow();
                    }
                });
        buttonPanel.add(btnDestArrowButton);
    }

    /**
     * Gets the resource icon.
     *
     * @param resourceString the resource string
     * @return the resource icon
     */
    private static ImageIcon getResourceIcon(String resourceString) {
        URL url = SLDTreeTools.class.getClassLoader().getResource(resourceString);

        if (url == null) {
            ConsoleManager.getInstance()
                    .error(
                            SLDTreeTools.class,
                            Localisation.getField(ParseXML.class, "ParseXML.failedToFindResource")
                                    + resourceString);
            return null;
        } else {
            return new ImageIcon(url);
        }
    }

    /** Removes the item. */
    public void removeItem() {
        if (symbolTree == null) {
            return;
        }

        TreePath path = symbolTree.getSelectionPath();

        if (path == null) {
            return;
        }

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        Object obj = lastNode.getUserObject();

        // CHECKSTYLE:OFF
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());
        // CHECKSTYLE:ON

        if (obj instanceof NamedLayer) {
            SelectedSymbol.getInstance().removeUserNamedLayer((NamedLayer) obj);
            removeTreeNode(lastNode);
        } else if (obj instanceof UserLayer) {
            SelectedSymbol.getInstance().removeUserNamedLayer((UserLayer) obj);
            removeTreeNode(lastNode);
        } else if (obj instanceof Style) {
            SelectedSymbol.getInstance().removeStyle((Style) obj);
            removeTreeNode(lastNode);
        } else if (obj instanceof FeatureTypeStyle) {
            SelectedSymbol.getInstance().removeFeatureTypeStyle((FeatureTypeStyle) obj);
            removeTreeNode(lastNode);
        } else if (obj instanceof Rule) {
            SelectedSymbol.getInstance().removeRule((Rule) obj);
            removeTreeNode(lastNode);
        } else if (obj instanceof Symbolizer) {
            DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) lastNode.getParent();
            if (parentNode != null) {
                if (parentNode.getUserObject() instanceof RasterSymbolizer) {
                    SelectedSymbol.getInstance()
                            .removeRasterImageOutline(
                                    (RasterSymbolizer) parentNode.getUserObject());
                } else {
                    SelectedSymbol.getInstance().removeSymbolizer((Symbolizer) obj);
                }
                removeTreeNode(lastNode);
            }
        } else {
            return;
        }

        SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

        // Re-render the symbol
        if (renderList != null) {
            for (RenderSymbolInterface render : renderList) {
                render.renderSymbol();
            }
        }

        Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance()
                .addUndoEvent(
                        new UndoEvent(
                                sldTree.getUndoObject(),
                                getClass().getName(),
                                oldValueObj,
                                newValueObj));
    }

    /**
     * Removes the tree node.
     *
     * @param nodeToRemove the node to remove
     */
    private void removeTreeNode(DefaultMutableTreeNode nodeToRemove) {

        if (nodeToRemove != null) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) nodeToRemove.getParent();
            nodeMap.remove(nodeToRemove.getUserObject());

            if (treeModel != null) {
                if (parent != null) {
                    treeModel.removeNodeFromParent(nodeToRemove);
                    treeModel.nodeChanged(parent);
                }
            }

            if (symbolTree != null) {
                symbolTree.setSelectionPath(getPath(parent));
            }
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
     * Adds the new thing.
     *
     * @param hint the hint when the are multiple possibilities
     */
    public void addNewThing(Class<?> hint) {
        if (symbolTree == null) {
            return;
        }

        TreePath path = symbolTree.getSelectionPath();

        if (path == null) {
            return;
        }

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();

        DefaultMutableTreeNode newNode = null;

        Object obj = lastNode.getUserObject();

        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        if (obj instanceof String) {
            StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();

            SelectedSymbol.getInstance().createNewSLD(sld);

            if (hint == NamedLayer.class) {
                NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();

                SelectedSymbol.getInstance().addNewStyledLayer(namedLayer);
                newNode = sldTree.addObject(lastNode, namedLayer, true);
            } else if (hint == UserLayer.class) {
                UserLayer userLayer = DefaultSymbols.createNewUserLayer();

                SelectedSymbol.getInstance().addNewStyledLayer(userLayer);
                newNode = sldTree.addObject(lastNode, userLayer, true);
            }
        } else if (obj instanceof StyledLayerDescriptor) {
            if (hint == NamedLayer.class) {
                NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();

                SelectedSymbol.getInstance().addNewStyledLayer(namedLayer);
                newNode = sldTree.addObject(lastNode, namedLayer, true);
            } else if (hint == UserLayer.class) {
                UserLayer userLayer = DefaultSymbols.createNewUserLayer();

                SelectedSymbol.getInstance().addNewStyledLayer(userLayer);
                newNode = sldTree.addObject(lastNode, userLayer, true);
            }
        } else if (obj instanceof NamedLayer) {
            Style style = DefaultSymbols.createNewStyle();

            SelectedSymbol.getInstance().addNewStyle(style);
            newNode = sldTree.addObject(lastNode, style, true);
        } else if (obj instanceof UserLayer) {
            Style style = DefaultSymbols.createNewStyle();

            SelectedSymbol.getInstance().addNewStyle(style);
            newNode = sldTree.addObject(lastNode, style, true);
        } else if (obj instanceof Style) {
            FeatureTypeStyle featureTypeStyle = DefaultSymbols.createNewFeatureTypeStyle();

            SelectedSymbol.getInstance().addNewFeatureTypeStyle(featureTypeStyle);

            newNode = sldTree.addObject(lastNode, featureTypeStyle, true);
        } else if (obj instanceof FeatureTypeStyle) {
            Rule rule = DefaultSymbols.createNewRule();

            SelectedSymbol.getInstance().addNewRule(rule);
            newNode = sldTree.addObject(lastNode, rule, true);
        }

        // Select the item just added
        if (newNode != null) {
            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /**
     * Gets the rule tree node.
     *
     * @return the rule tree node
     */
    private DefaultMutableTreeNode getRuleTreeNode() {
        if (symbolTree != null) {
            TreePath path = symbolTree.getSelectionPath();

            DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object obj = lastNode.getUserObject();

            if (obj instanceof Symbolizer) {
                return (DefaultMutableTreeNode) lastNode.getParent();
            } else if (obj instanceof Rule) {
                return (DefaultMutableTreeNode) lastNode;
            }
        }
        return rootNode;
    }

    /**
     * Gets the raster symbolizer tree node.
     *
     * @return the rule tree node
     */
    private DefaultMutableTreeNode getRasterTreeNode() {
        if (symbolTree != null) {
            TreePath path = symbolTree.getSelectionPath();

            DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
            Object obj = lastNode.getUserObject();

            if (obj instanceof RasterSymbolizer) {
                return (DefaultMutableTreeNode) lastNode;
            }
        }
        return rootNode;
    }

    /** Adds the new marker symbolizer. */
    public void addNewMarker() {
        if (symbolTree == null) {
            return;
        }

        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        PointSymbolizer newPointSymbolizer = DefaultSymbols.createDefaultPointSymbolizer();

        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newPointSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, newPointSymbolizer, true);

        // Select the item just added
        if (newNode != null) {
            sldTree.addObject(
                    newNode, SLDTreeLeafFactory.getInstance().getFill(newPointSymbolizer), true);

            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the new raster symbolizer. */
    public void addNewRaster() {
        if (symbolTree == null) {
            return;
        }
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        RasterSymbolizer newRasterSymbolizer = DefaultSymbols.createDefaultRasterSymbolizer();
        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newRasterSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, newRasterSymbolizer, true);

        // Select the item just added
        if (newNode != null) {
            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the destination arrow. */
    public void addDestArrow() {
        addArrow(false);
    }

    /**
     * Adds the arrow.
     *
     * @param isSourceArrow the is source arrow
     */
    private void addArrow(boolean isSourceArrow) {
        if (symbolTree == null) {
            return;
        }
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        PointSymbolizer pointSymbolizer = DefaultSymbols.createArrow(isSourceArrow);
        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(pointSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, pointSymbolizer, true);

        // Select the item just added
        if (newNode != null) {
            sldTree.addObject(
                    newNode, SLDTreeLeafFactory.getInstance().getFill(pointSymbolizer), true);

            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            // Re-render the symbol
            if (renderList != null) {
                for (RenderSymbolInterface render : renderList) {
                    render.renderSymbol();
                }
            }

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the source arrow. */
    public void addSourceArrow() {
        addArrow(true);
    }

    /** Adds the new text symbolizer. */
    public void addNewText() {
        if (symbolTree == null) {
            return;
        }

        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        TextSymbolizer newTextSymbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newTextSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, newTextSymbolizer, true);

        // Select the item just added
        if (newNode != null) {
            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the new line symbolizer. */
    public void addNewLine() {
        if (symbolTree == null) {
            return;
        }
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        LineSymbolizer newLineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();

        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newLineSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, newLineSymbolizer, true);

        if (newNode != null) {
            sldTree.addObject(
                    newNode, SLDTreeLeafFactory.getInstance().getStroke(newLineSymbolizer), true);

            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            // Select the item just added
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the new image outline line symbolizer. */
    public void addNewImageOutlineLine() {
        if (symbolTree == null) {
            return;
        }
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        LineSymbolizer newLineSymbolizer = DefaultSymbols.createDefaultLineSymbolizer();

        DefaultMutableTreeNode ruleNode = getRasterTreeNode();

        SelectedSymbol.getInstance().addImageOutlineSymbolizerToRaster(newLineSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, newLineSymbolizer, true);

        if (newNode != null) {
            sldTree.addObject(
                    newNode, SLDTreeLeafFactory.getInstance().getStroke(newLineSymbolizer), true);

            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            // Select the item just added
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the new polygon symbolizer. */
    public void addNewPolygon() {
        if (symbolTree == null) {
            return;
        }
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        PolygonSymbolizer newPolygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        DefaultMutableTreeNode ruleNode = getRuleTreeNode();

        SelectedSymbol.getInstance().addSymbolizerToRule(newPolygonSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(ruleNode, newPolygonSymbolizer, true);

        if (newNode != null) {
            sldTree.addObject(
                    newNode, SLDTreeLeafFactory.getInstance().getFill(newPolygonSymbolizer), true);
            sldTree.addObject(
                    newNode,
                    SLDTreeLeafFactory.getInstance().getStroke(newPolygonSymbolizer),
                    true);

            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            // Select the item just added
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Adds the new image outline polygon symbolizer. */
    public void addNewImageOutlinePolygon() {
        if (symbolTree == null) {
            return;
        }
        // Store current state of the SLD before the add
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        PolygonSymbolizer newPolygonSymbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();

        DefaultMutableTreeNode rasterNode = getRasterTreeNode();

        SelectedSymbol.getInstance().addImageOutlineSymbolizerToRaster(newPolygonSymbolizer);
        DefaultMutableTreeNode newNode = sldTree.addObject(rasterNode, newPolygonSymbolizer, true);

        if (newNode != null) {
            sldTree.addObject(
                    newNode, SLDTreeLeafFactory.getInstance().getFill(newPolygonSymbolizer), true);
            sldTree.addObject(
                    newNode,
                    SLDTreeLeafFactory.getInstance().getStroke(newPolygonSymbolizer),
                    true);

            SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

            // Select the item just added
            TreePath newPath = getPath(newNode);

            symbolTree.setSelectionPath(newPath);

            // Store current state of the SLD after the add
            Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

            UndoManager.getInstance()
                    .addUndoEvent(
                            new UndoEvent(
                                    sldTree.getUndoObject(),
                                    getClass().getName(),
                                    oldValueObj,
                                    newValueObj));
        }
    }

    /** Move item down. */
    private void moveItemDown() {
        moveItem(false);
    }

    /** Move item up. */
    private void moveItemUp() {
        moveItem(true);
    }

    /**
     * Move item within a list. The direction parameter determines which way the item is moved.
     *
     * @param moveUp the move up flags (true), or down (false)
     */
    public void moveItem(boolean moveUp) {
        if (symbolTree == null) {
            return;
        }

        if (treeModel == null) {
            return;
        }

        if (sldTree == null) {
            return;
        }

        TreePath path = symbolTree.getSelectionPath();

        if (path == null) {
            return;
        }

        DefaultMutableTreeNode lastNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (lastNode == null) {
            return;
        }

        Object obj = lastNode.getUserObject();
        if (obj == null) {
            return;
        }

        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) lastNode.getParent();

        if (parentNode == null) {
            return;
        }

        Object parentObj = parentNode.getUserObject();

        if (parentObj == null) {
            return;
        }

        // Calculate index offset value based on direction
        int direction = moveUp ? -1 : 1;

        // Store current state of the SLD before the move
        // CHECKSTYLE:OFF
        Object oldValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());
        // CHECKSTYLE:ON

        if (obj instanceof StyledLayer) {
            StyledLayerDescriptor sld = (StyledLayerDescriptor) parentObj;
            // NamedLayerImpl.equals() doesn't work in the way I
            // want it to, so indexOf() does not work
            boolean found = false;
            int index = 0;

            for (StyledLayer styledLayer : sld.layers()) {
                if (styledLayer == obj) {
                    found = true;
                    break;
                } else {
                    index++;
                }
            }

            if (found && ((index + direction) >= 0) && (index + direction) < sld.layers().size()) {
                StyledLayer styledLayer = sld.layers().remove(index);
                sld.layers().add(index + direction, styledLayer);

                treeModel.removeNodeFromParent(lastNode);
                treeModel.insertNodeInto(lastNode, parentNode, index + direction);
            } else {
                return;
            }
        } else if (obj instanceof Style) {
            if (parentObj instanceof NamedLayerImpl) {
                NamedLayerImpl namedLayer = (NamedLayerImpl) parentObj;
                int index = namedLayer.styles().indexOf(obj);

                if (((index + direction) >= 0)
                        && (index + direction) < namedLayer.styles().size()) {
                    Style style = namedLayer.styles().remove(index);
                    namedLayer.styles().add(index + direction, style);

                    treeModel.removeNodeFromParent(lastNode);
                    treeModel.insertNodeInto(lastNode, parentNode, index + direction);
                } else {
                    return;
                }
            }
        } else if (obj instanceof FeatureTypeStyle) {
            Style style = (Style) parentObj;
            int index = style.featureTypeStyles().indexOf(obj);

            if (((index + direction) >= 0)
                    && (index + direction) < style.featureTypeStyles().size()) {
                FeatureTypeStyle fts = style.featureTypeStyles().remove(index);
                style.featureTypeStyles().add(index + direction, fts);

                treeModel.removeNodeFromParent(lastNode);
                treeModel.insertNodeInto(lastNode, parentNode, index + direction);
            } else {
                return;
            }
        } else if (obj instanceof Rule) {
            FeatureTypeStyle fts = (FeatureTypeStyle) parentObj;
            int index = fts.rules().indexOf(obj);

            if (((index + direction) >= 0) && (index + direction) < fts.rules().size()) {
                Rule rule = fts.rules().remove(index);
                fts.rules().add(index + direction, rule);

                treeModel.removeNodeFromParent(lastNode);
                treeModel.insertNodeInto(lastNode, parentNode, index + direction);
            } else {
                return;
            }
        } else if (obj instanceof Symbolizer) {
            Rule rule = (Rule) parentObj;
            int index = rule.symbolizers().indexOf(obj);

            if (((index + direction) >= 0) && (index + direction) < rule.symbolizers().size()) {
                Symbolizer symbolizer = rule.symbolizers().remove(index);
                rule.symbolizers().add(index + direction, symbolizer);

                treeModel.removeNodeFromParent(lastNode);
                treeModel.insertNodeInto(lastNode, parentNode, index + direction);
            } else {
                return;
            }
        }

        // Refresh the tree structure. Not very efficient but gets result wanted.
        // The node has been moved in the tree above. Now going to refresh model.
        treeModel.nodeStructureChanged(lastNode);

        // Get path for item moved
        TreePath newNodePath = getPath(lastNode);
        int[] selectedRows = new int[1];
        selectedRows[0] = symbolTree.getRowForPath(newNodePath);

        // Find the row of item moved

        // Now clear tree structure and re-populate, inefficient but it means
        // that all items are expanded as required.
        SLDTreeManager.getInstance().rebuildTree((SLDTree) sldTree);

        // Make item moved selected again
        symbolTree.setSelectionRows(selectedRows);

        // Re-render the symbol
        if (renderList != null) {
            for (RenderSymbolInterface render : renderList) {
                render.renderSymbol();
            }
        }

        // Store current state of the SLD after the move
        Object newValueObj = sldWriter.encodeSLD(null, SelectedSymbol.getInstance().getSld());

        UndoManager.getInstance()
                .addUndoEvent(
                        new UndoEvent(
                                sldTree.getUndoObject(),
                                getClass().getName(),
                                oldValueObj,
                                newValueObj));
    }

    /**
     * Gets the button panel.
     *
     * @return the buttonPanel
     */
    public JPanel getButtonPanel() {
        return buttonPanel;
    }

    /**
     * Sets the buttons state.
     *
     * @param parentNode the parent node
     * @param selectedNode the new button state
     * @param currentGeometryType the current geometry type
     */
    public void setButtonState(
            DefaultMutableTreeNode parentNode,
            DefaultMutableTreeNode selectedNode,
            GeometryTypeEnum currentGeometryType) {
        boolean addButtonEnabled = true;
        boolean removeButtonEnabled = true;
        boolean addNamedLayerButtonEnabled = false;
        boolean addUserLayerButtonEnabled = false;
        boolean showMoveButtons = true;
        boolean hasMoreThan1Item = false;
        boolean isFirstSelected = false;
        boolean isLastSelected = false;
        boolean showLineArrowButtons = false;

        symbolizerButtonState.setGeometryType(currentGeometryType);

        Object obj = null;
        Object parentObj = null;

        if (selectedNode != null) {
            obj = selectedNode.getUserObject();

            if (parentNode != null) {
                parentObj = parentNode.getUserObject();
            }

            if (obj instanceof StyledLayerDescriptor) {
                addNamedLayerButtonEnabled = true;
                addUserLayerButtonEnabled = true;
                addButtonEnabled = false;
            } else if (obj instanceof StyledLayer) {
                if (parentObj != null) {
                    StyledLayerDescriptor sld = (StyledLayerDescriptor) parentObj;
                    hasMoreThan1Item = sld.layers().size() > 1;
                    isFirstSelected = (obj == sld.layers().get(0));
                    isLastSelected = (obj == sld.layers().get(sld.layers().size() - 1));
                }
            } else if (obj instanceof Style) {
                if (parentObj != null) {
                    if (parentObj instanceof NamedLayerImpl) {
                        NamedLayerImpl namedLayer = (NamedLayerImpl) parentObj;
                        hasMoreThan1Item = namedLayer.styles().size() > 1;
                        isFirstSelected = (obj == namedLayer.styles().get(0));
                        isLastSelected =
                                (obj == namedLayer.styles().get(namedLayer.styles().size() - 1));
                    }
                }
            } else if (obj instanceof FeatureTypeStyle) {
                if (parentObj != null) {
                    Style style = (Style) parentObj;
                    hasMoreThan1Item = style.featureTypeStyles().size() > 1;
                    isFirstSelected = (obj == style.featureTypeStyles().get(0));
                    isLastSelected =
                            (obj
                                    == style.featureTypeStyles()
                                            .get(style.featureTypeStyles().size() - 1));
                }
            } else if (obj instanceof Rule) {
                symbolizerButtonState.showSymbolizerButtons();
                addButtonEnabled = false;

                if (parentObj != null) {
                    FeatureTypeStyle fts = (FeatureTypeStyle) parentObj;
                    hasMoreThan1Item = fts.rules().size() > 1;
                    isFirstSelected = (obj == fts.rules().get(0));
                    isLastSelected = (obj == fts.rules().get(fts.rules().size() - 1));
                }
            } else if (obj instanceof Symbolizer) {
                symbolizerButtonState.showSymbolizerButtons();
                addButtonEnabled = false;

                if (parentObj != null) {
                    if (parentObj instanceof Rule) {
                        Rule rule = (Rule) parentObj;
                        hasMoreThan1Item = rule.symbolizers().size() > 1;
                        isFirstSelected = (obj == rule.symbolizers().get(0));
                        isLastSelected =
                                (obj == rule.symbolizers().get(rule.symbolizers().size() - 1));
                    }
                }

                showLineArrowButtons = (obj instanceof LineSymbolizer);
            } else {
                addButtonEnabled = false;
                removeButtonEnabled = false;
                showMoveButtons = false;
            }
        }

        btnAddNamedLayerButton.setVisible(addNamedLayerButtonEnabled);
        btnAddUserLayerButton.setVisible(addUserLayerButtonEnabled);
        btnAddButton.setVisible(addButtonEnabled);

        // All symbolizer types are allowed be added to an SLD
        boolean allSymbolizer =
                symbolizerButtonState.isMarkerVisible(parentObj, obj)
                        || symbolizerButtonState.isLineVisible(parentObj, obj)
                        || symbolizerButtonState.isPolygonVisible(parentObj, obj)
                        || symbolizerButtonState.isRasterVisible(parentObj, obj);
        btnNewMarker.setVisible(allSymbolizer);
        btnNewLine.setVisible(allSymbolizer);
        btnNewImageOutlineLine.setVisible(
                symbolizerButtonState.isImageOutlineLineVisible(parentObj, obj));
        btnNewPolygon.setVisible(allSymbolizer);
        btnNewImageOutlinePolygon.setVisible(
                symbolizerButtonState.isImageOutlinePolygonVisible(parentObj, obj));
        btnNewRaster.setVisible(allSymbolizer);
        btnNewText.setVisible(symbolizerButtonState.isTextVisible(parentObj, obj));

        btnRemoveMarker.setVisible(removeButtonEnabled);

        // Up / down buttons
        btnMoveUp.setVisible(showMoveButtons);
        btnMoveUp.setEnabled(hasMoreThan1Item && !isFirstSelected);
        btnMoveDown.setVisible(showMoveButtons);
        btnMoveDown.setEnabled(hasMoreThan1Item && !isLastSelected);

        btnSourceArrowButton.setVisible(showLineArrowButtons);
        btnDestArrowButton.setVisible(showLineArrowButtons);
    }

    /**
     * Gets the panel width.
     *
     * @return the panelWidth
     */
    public static int getPanelWidth() {
        return PANEL_WIDTH;
    }
}
