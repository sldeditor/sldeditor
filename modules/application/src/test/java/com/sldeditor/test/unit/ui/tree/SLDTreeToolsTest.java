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

package com.sldeditor.test.unit.ui.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import org.geotools.data.DataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyleImpl;
import org.geotools.styling.LineSymbolizerImpl;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.PointSymbolizerImpl;
import org.geotools.styling.PolygonSymbolizerImpl;
import org.geotools.styling.RasterSymbolizerImpl;
import org.geotools.styling.RuleImpl;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizerImpl;
import org.geotools.styling.UserLayer;
import org.geotools.styling.UserLayerImpl;
import org.junit.Test;

import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import com.sldeditor.ui.render.RuleRenderOptions;
import com.sldeditor.ui.tree.SLDTree;
import com.sldeditor.ui.tree.SLDTreeTools;

/**
 * The unit test for SLDTreeTools.
 * 
 * <p>{@link com.sldeditor.ui.tree.SLDTreeTools}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeToolsTest {

    /**
     * The Class TestRenderSymbolInterface.
     */
    class TestRenderSymbolInterface implements RenderSymbolInterface {
        private boolean renderSymbolCalled = false;

        @Override
        public void dataSourceLoaded(GeometryTypeEnum geometryType,
                boolean isConnectedToDataSourceFlag) {
        }

        @Override
        public void addSLDOutputListener(SLDOutputInterface sldOutput) {
        }

        @Override
        public void renderSymbol() {
            renderSymbolCalled = true;
        }

        @Override
        public RuleRenderOptions getRuleRenderOptions() {
            return null;
        }

        /**
         * Checks if is render symbol been called.
         *
         * @return true, if is render symbol been called
         */
        public boolean hasRenderSymbolBeenCalled() {
            boolean tmp = renderSymbolCalled;

            renderSymbolCalled = false;

            return tmp;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.
         * geotools.data.DataStore)
         */
        @Override
        public void dataSourceAboutToUnloaded(DataStore dataStore) {
            // Does nothing
        }
    }

    /**
     * The Class TestSLDTree, inherits SLDTree to expose the root tree node for testing purposes.
     */
    class TestSLDTree extends SLDTree {
        public TestSLDTree(List<RenderSymbolInterface> renderList, SLDTreeTools treeTools) {
            super(renderList, treeTools);
        }

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Gets the root node.
         *
         * @return the root node
         */
        public DefaultMutableTreeNode getRootNode() {
            return rootNode;
        }
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.tree.SLDTreeTools#SLDTreeTools(com.sldeditor.ui.tree.UpdateTreeStructureInterface, javax.swing.JTree, java.util.List)}.
     */
    @Test
    public void testSLDTreeTools() {
        SLDTreeTools treeTools = new SLDTreeTools();

        // Try calling public methods when constructor parameters are null
        treeTools.addNewLine();
        treeTools.addNewMarker();
        treeTools.addNewPolygon();
        treeTools.addNewText();
        treeTools.addNewThing(null);
        treeTools.addNewThing(UserLayer.class);
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewRaster();
        treeTools.addDestArrow();
        treeTools.addSourceArrow();
        treeTools.moveItem(true);
        treeTools.removeItem();
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewThing()}.
     */
    @Test
    public void testAddNewThingNamedLayer() {

        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        treeTools.addNewThing(null);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Check it has no structure below just the root node
        assertEquals(0, rootNode.getChildCount());

        // Add a thing - a quick way of creating sld structure

        // This should add a named layer and select it
        treeTools.addNewThing(NamedLayer.class);
        assertEquals(1, rootNode.getChildCount());
        DefaultMutableTreeNode namedLayer = (DefaultMutableTreeNode) rootNode.getChildAt(0);

        assertEquals(NamedLayerImpl.class, namedLayer.getUserObject().getClass());

        // This should add a style and select it
        assertEquals(0, namedLayer.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(1, namedLayer.getChildCount());

        DefaultMutableTreeNode styleLayer = (DefaultMutableTreeNode) namedLayer.getChildAt(0);

        assertEquals(StyleImpl.class, styleLayer.getUserObject().getClass());

        // This should add a feature type style and select it
        assertEquals(0, styleLayer.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(1, styleLayer.getChildCount());

        DefaultMutableTreeNode featureTypeStyle = (DefaultMutableTreeNode) styleLayer.getChildAt(0);

        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyle.getUserObject().getClass());

        // This should add a rule and select it
        assertEquals(0, featureTypeStyle.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(1, featureTypeStyle.getChildCount());

        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) featureTypeStyle.getChildAt(0);

        assertEquals(RuleImpl.class, rule.getUserObject().getClass());

        // This should do nothing
        assertEquals(0, rule.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(0, rule.getChildCount());

        // Undo last add of rule to feature type style
        UndoManager.getInstance().undo();
        DefaultMutableTreeNode featureTypeStyleNode = (DefaultMutableTreeNode) sldTree.getRootNode()
                .getChildAt(0).getChildAt(0).getChildAt(0);
        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyleNode.getUserObject().getClass());
        assertEquals(0, featureTypeStyleNode.getChildCount());

        // Undo last add of rule to feature type style
        UndoManager.getInstance().redo();
        featureTypeStyleNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyleNode.getUserObject().getClass());
        assertEquals(1, featureTypeStyleNode.getChildCount());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewThing()}.
     */
    @Test
    public void testAddNewThingUserLayer() {

        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        treeTools.addNewThing(null);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Check it has no structure below just the root node
        assertEquals(0, rootNode.getChildCount());

        // Add a thing - a quick way of creating sld structure

        // This should add a user layer and select it
        treeTools.addNewThing(UserLayer.class);
        assertEquals(1, rootNode.getChildCount());
        DefaultMutableTreeNode userLayer = (DefaultMutableTreeNode) rootNode.getChildAt(0);

        assertEquals(UserLayerImpl.class, userLayer.getUserObject().getClass());

        // This should add a style and select it
        assertEquals(0, userLayer.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(1, userLayer.getChildCount());

        DefaultMutableTreeNode styleLayer = (DefaultMutableTreeNode) userLayer.getChildAt(0);

        assertEquals(StyleImpl.class, styleLayer.getUserObject().getClass());

        // This should add a feature type style and select it
        assertEquals(0, styleLayer.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(1, styleLayer.getChildCount());

        DefaultMutableTreeNode featureTypeStyle = (DefaultMutableTreeNode) styleLayer.getChildAt(0);

        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyle.getUserObject().getClass());

        // This should add a rule and select it
        assertEquals(0, featureTypeStyle.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(1, featureTypeStyle.getChildCount());

        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) featureTypeStyle.getChildAt(0);

        assertEquals(RuleImpl.class, rule.getUserObject().getClass());

        // This should do nothing
        assertEquals(0, rule.getChildCount());
        treeTools.addNewThing(null);
        assertEquals(0, rule.getChildCount());

        // Undo last add of rule to feature type style
        UndoManager.getInstance().undo();
        DefaultMutableTreeNode featureTypeStyleNode = (DefaultMutableTreeNode) sldTree.getRootNode()
                .getChildAt(0).getChildAt(0).getChildAt(0);
        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyleNode.getUserObject().getClass());
        assertEquals(0, featureTypeStyleNode.getChildCount());

        // Undo last add of rule to feature type style
        UndoManager.getInstance().redo();
        featureTypeStyleNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyleNode.getUserObject().getClass());
        assertEquals(1, featureTypeStyleNode.getChildCount());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewMarker()}.
     */
    @Test
    public void testAddNewMarker() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewMarker();

        DefaultMutableTreeNode marker = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PointSymbolizerImpl.class, marker.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addRaster()}.
     */
    @Test
    public void testAddRaster() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewRaster();

        DefaultMutableTreeNode rasterNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(RasterSymbolizerImpl.class, rasterNode.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewText()}.
     */
    @Test
    public void testAddNewText() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewText();

        DefaultMutableTreeNode textNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(TextSymbolizerImpl.class, textNode.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewLine()}.
     */
    @Test
    public void testAddNewLine() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewLine();

        DefaultMutableTreeNode lineNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(LineSymbolizerImpl.class, lineNode.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewPolygon()}.
     */
    @Test
    public void testAddNewPolygon() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewPolygon();

        DefaultMutableTreeNode polygonNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, polygonNode.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewImageOutlinePolygon()}.
     */
    @Test
    public void testAddNewImageOutlinePolygon() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewRaster();

        DefaultMutableTreeNode rasterNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(RasterSymbolizerImpl.class, rasterNode.getUserObject().getClass());

        treeTools.addNewImageOutlinePolygon();
        DefaultMutableTreeNode imageOutlineNode = (DefaultMutableTreeNode) rasterNode.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, imageOutlineNode.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addNewImageOutlineLine()}.
     */
    @Test
    public void testAddNewImageOutlineLine() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewRaster();

        DefaultMutableTreeNode rasterNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(RasterSymbolizerImpl.class, rasterNode.getUserObject().getClass());

        treeTools.addNewImageOutlineLine();
        DefaultMutableTreeNode imageOutlineNode = (DefaultMutableTreeNode) rasterNode.getChildAt(0);
        assertEquals(LineSymbolizerImpl.class, imageOutlineNode.getUserObject().getClass());
    }

    /**
     * Check NamedLayers Test method for
     * {@link com.sldeditor.ui.tree.SLDTreeTools#moveItem(boolean)}.
     */
    @Test
    public void testMoveItemNamedLayer() {
        TestRenderSymbolInterface renderSymbol = new TestRenderSymbolInterface();
        List<RenderSymbolInterface> renderList = new ArrayList<RenderSymbolInterface>();
        renderList.add(renderSymbol);

        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(renderList, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();
        DefaultMutableTreeNode namedLayer1Node = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        NamedLayerImpl namedLayer1 = (NamedLayerImpl) namedLayer1Node.getUserObject();
        String expectedNamedLayer1 = "named layer 1";
        namedLayer1.setName(expectedNamedLayer1);
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);

        DefaultMutableTreeNode namedLayer2Node = (DefaultMutableTreeNode) rootNode.getChildAt(1);
        NamedLayerImpl namedLayer2 = (NamedLayerImpl) namedLayer2Node.getUserObject();
        String expectedNamedLayer2 = "named layer 2";
        namedLayer2.setName(expectedNamedLayer2);

        // Have 2 named layers
        TreeSelectionData data = new TreeSelectionData();
        data.setLayerIndex(1);
        sldTree.selectTreeItem(data);

        // Move named layer 2 so that they are reversed
        treeTools.moveItem(true);
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());

        DefaultMutableTreeNode testNode = (DefaultMutableTreeNode) sldTree.getRootNode()
                .getChildAt(0);
        NamedLayerImpl testNamedLayer = (NamedLayerImpl) testNode.getUserObject();
        assertTrue(testNamedLayer.getName().compareTo(expectedNamedLayer2) == 0);

        // Try and move 'named layer 2' up to index -1 which is invalid, result should be same as
        // last time
        data.setLayerIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(true);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0);
        testNamedLayer = (NamedLayerImpl) testNode.getUserObject();
        assertTrue(testNamedLayer.getName().compareTo(expectedNamedLayer2) == 0);

        // Move it back
        data.setLayerIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(1);
        testNamedLayer = (NamedLayerImpl) testNode.getUserObject();
        assertTrue(testNamedLayer.getName().compareTo(expectedNamedLayer2) == 0);

        // Move it beyond the end of the list, should be the same answer as before
        data.setLayerIndex(1);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(1);
        testNamedLayer = (NamedLayerImpl) testNode.getUserObject();
        assertTrue(testNamedLayer.getName().compareTo(expectedNamedLayer2) == 0);

        // Undo the last valid move
        UndoManager.getInstance().undo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0);
        testNamedLayer = (NamedLayerImpl) testNode.getUserObject();
        assertTrue(testNamedLayer.getName().compareTo(expectedNamedLayer2) == 0);

        // Redo the last valid move
        UndoManager.getInstance().redo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(1);
        testNamedLayer = (NamedLayerImpl) testNode.getUserObject();
        assertTrue(testNamedLayer.getName().compareTo(expectedNamedLayer2) == 0);
    }

    /**
     * Check Styles Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#moveItem(boolean)}.
     */
    @Test
    public void testMoveItemStyle() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();
        DefaultMutableTreeNode style1Node = (DefaultMutableTreeNode) rootNode.getChildAt(0)
                .getChildAt(0);
        StyleImpl style1 = (StyleImpl) style1Node.getUserObject();
        String expectedStyle1 = "style 1";
        style1.setName(expectedStyle1);

        TreeSelectionData data = new TreeSelectionData();
        data.setLayerIndex(0);
        sldTree.selectTreeItem(data);

        treeTools.addNewThing(null);

        DefaultMutableTreeNode style2Node = (DefaultMutableTreeNode) rootNode.getChildAt(0)
                .getChildAt(1);
        StyleImpl style2 = (StyleImpl) style2Node.getUserObject();
        String expectedStyle2 = "style 2";
        style2.setName(expectedStyle2);

        // Have 2 styles
        data = new TreeSelectionData();
        data.setLayerIndex(0);
        data.setStyleIndex(1);
        sldTree.selectTreeItem(data);

        // Move style 2 so that they are reversed
        treeTools.moveItem(true);

        DefaultMutableTreeNode testNode = (DefaultMutableTreeNode) sldTree.getRootNode()
                .getChildAt(0).getChildAt(0);
        StyleImpl testStyle = (StyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedStyle2) == 0);

        // Try and move 'style 2' up to index -1 which is invalid, result should be same as last
        // time
        data.setStyleIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(true);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0);
        testStyle = (StyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedStyle2) == 0);

        // Move it back
        data.setStyleIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(1);
        testStyle = (StyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedStyle2) == 0);

        // Move it beyond the end of the list, should be the same answer as before
        data.setStyleIndex(1);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(1);
        testStyle = (StyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedStyle2) == 0);

        // Undo the last valid move
        UndoManager.getInstance().undo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0);
        testStyle = (StyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedStyle2) == 0);

        // Redo the last valid move
        UndoManager.getInstance().redo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(1);
        testStyle = (StyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedStyle2) == 0);
    }

    /**
     * Check Feature Type Styles Test method for
     * {@link com.sldeditor.ui.tree.SLDTreeTools#moveItem(boolean)}.
     */
    @Test
    public void testMoveItemFeatureTypeStyles() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();
        DefaultMutableTreeNode featureTypeStyle1Node = (DefaultMutableTreeNode) rootNode
                .getChildAt(0).getChildAt(0).getChildAt(0);
        FeatureTypeStyleImpl featureTypeStyle1 = (FeatureTypeStyleImpl) featureTypeStyle1Node
                .getUserObject();
        String expectedFeatureTypeStyle1 = "feature type style 1";
        featureTypeStyle1.setName(expectedFeatureTypeStyle1);

        TreeSelectionData data = new TreeSelectionData();
        data.setLayerIndex(0);
        data.setStyleIndex(0);
        sldTree.selectTreeItem(data);

        treeTools.addNewThing(null);

        DefaultMutableTreeNode featureTypeStyle2Node = (DefaultMutableTreeNode) rootNode
                .getChildAt(0).getChildAt(0).getChildAt(1);
        FeatureTypeStyleImpl featureTypeStyle2 = (FeatureTypeStyleImpl) featureTypeStyle2Node
                .getUserObject();
        String expectedFeatureTypeStyle2 = "feature type style 2";
        featureTypeStyle2.setName(expectedFeatureTypeStyle2);

        // Have 2 styles
        data = new TreeSelectionData();
        data.setLayerIndex(0);
        data.setStyleIndex(0);
        data.setFeatureTypeStyleIndex(1);
        sldTree.selectTreeItem(data);

        // Move style 2 so that they are reversed
        treeTools.moveItem(true);

        DefaultMutableTreeNode testNode = (DefaultMutableTreeNode) sldTree.getRootNode()
                .getChildAt(0).getChildAt(0).getChildAt(0);
        FeatureTypeStyleImpl testStyle = (FeatureTypeStyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedFeatureTypeStyle2) == 0);

        // Try and move 'style 2' up to index -1 which is invalid, result should be same as last
        // time
        data.setFeatureTypeStyleIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(true);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0);
        testStyle = (FeatureTypeStyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedFeatureTypeStyle2) == 0);

        // Move it back
        data.setFeatureTypeStyleIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(1);
        testStyle = (FeatureTypeStyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedFeatureTypeStyle2) == 0);

        // Move it beyond the end of the list, should be the same answer as before
        data.setFeatureTypeStyleIndex(1);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(1);
        testStyle = (FeatureTypeStyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedFeatureTypeStyle2) == 0);

        // Undo the last valid move
        UndoManager.getInstance().undo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0);
        testStyle = (FeatureTypeStyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedFeatureTypeStyle2) == 0);

        // Redo the last valid move
        UndoManager.getInstance().redo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(1);
        testStyle = (FeatureTypeStyleImpl) testNode.getUserObject();
        assertTrue(testStyle.getName().compareTo(expectedFeatureTypeStyle2) == 0);
    }

    /**
     * Check Rules Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#moveItem(boolean)}.
     */
    @Test
    public void testMoveItemRules() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();
        DefaultMutableTreeNode rule1Node = (DefaultMutableTreeNode) rootNode.getChildAt(0)
                .getChildAt(0).getChildAt(0).getChildAt(0);
        RuleImpl rule1 = (RuleImpl) rule1Node.getUserObject();
        String expectedRule1 = "rule 1";
        rule1.setName(expectedRule1);

        TreeSelectionData data = new TreeSelectionData();
        data.setLayerIndex(0);
        data.setStyleIndex(0);
        data.setFeatureTypeStyleIndex(0);
        sldTree.selectTreeItem(data);

        treeTools.addNewThing(null);

        DefaultMutableTreeNode rule2Node = (DefaultMutableTreeNode) rootNode.getChildAt(0)
                .getChildAt(0).getChildAt(0).getChildAt(1);
        RuleImpl rule2 = (RuleImpl) rule2Node.getUserObject();
        String expectedRule2 = "rule 2";
        rule2.setName(expectedRule2);

        // Have 2 styles
        data = new TreeSelectionData();
        data.setLayerIndex(0);
        data.setStyleIndex(0);
        data.setFeatureTypeStyleIndex(0);
        data.setRuleIndex(1);
        sldTree.selectTreeItem(data);

        // Move style 2 so that they are reversed
        treeTools.moveItem(true);

        DefaultMutableTreeNode testNode = (DefaultMutableTreeNode) sldTree.getRootNode()
                .getChildAt(0).getChildAt(0).getChildAt(0).getChildAt(0);
        RuleImpl testRule = (RuleImpl) testNode.getUserObject();
        assertTrue(testRule.getName().compareTo(expectedRule2) == 0);

        // Try and move 'style 2' up to index -1 which is invalid, result should be same as last
        // time
        data.setRuleIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(true);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        testRule = (RuleImpl) testNode.getUserObject();
        assertTrue(testRule.getName().compareTo(expectedRule2) == 0);

        // Move it back
        data.setRuleIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(1);
        testRule = (RuleImpl) testNode.getUserObject();
        assertTrue(testRule.getName().compareTo(expectedRule2) == 0);

        // Move it beyond the end of the list, should be the same answer as before
        data.setRuleIndex(1);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(1);
        testRule = (RuleImpl) testNode.getUserObject();
        assertTrue(testRule.getName().compareTo(expectedRule2) == 0);

        // Undo the last valid move
        UndoManager.getInstance().undo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        testRule = (RuleImpl) testNode.getUserObject();
        assertTrue(testRule.getName().compareTo(expectedRule2) == 0);

        // Redo the last valid move
        UndoManager.getInstance().redo();
        testNode = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(1);
        testRule = (RuleImpl) testNode.getUserObject();
        assertTrue(testRule.getName().compareTo(expectedRule2) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#moveItem(boolean)}.
     */
    @Test
    public void testMoveItemSymbolizer() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewPolygon();
        treeTools.addNewMarker();
        treeTools.addNewLine();
        treeTools.addNewText();

        assertEquals(4, rule.getChildCount());

        // Now have 4 symbolizers for the rule
        DefaultMutableTreeNode symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        // The last text symbolizer is selected as it was the last added
        treeTools.moveItem(true);
        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);

        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        // The last text symbolizer is selected as it was the last added
        TreeSelectionData data = new TreeSelectionData();
        data.setLayerIndex(0);
        data.setStyleIndex(0);
        data.setFeatureTypeStyleIndex(0);
        data.setRuleIndex(0);
        data.setSymbolizerIndex(2);
        data.setSelectedPanel(TextSymbolizerDetails.class);
        sldTree.selectTreeItem(data);

        treeTools.moveItem(true);

        data.setSymbolizerIndex(1);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(true);

        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        // Now try and move to index -1, everything should still be the same
        data.setSymbolizerIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(true);

        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        // Now try and move to index -1, everything should still be the same
        data.setSymbolizerIndex(0);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);
        data.setSymbolizerIndex(1);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);
        data.setSymbolizerIndex(2);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        data.setSymbolizerIndex(3);
        sldTree.selectTreeItem(data);
        treeTools.moveItem(false);

        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        // Undo the last valid move
        UndoManager.getInstance().undo();

        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());

        // Redo the last valid move
        UndoManager.getInstance().redo();
        // Tree has been repopulated so the root node has changed so get hold of rule node again
        rule = (DefaultMutableTreeNode) sldTree.getRootNode().getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(PolygonSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(LineSymbolizerImpl.class, symbolizer.getUserObject().getClass());
        symbolizer = (DefaultMutableTreeNode) rule.getChildAt(3);
        assertEquals(TextSymbolizerImpl.class, symbolizer.getUserObject().getClass());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#getButtonPanel()}.
     */
    @Test
    public void testGetButtonPanel() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        assertNotNull(sldTree);
        assertNotNull(treeTools.getButtonPanel());
    }

    /**
     * Test method for
     * {@link com.sldeditor.ui.tree.SLDTreeTools#setButtonState(javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.DefaultMutableTreeNode, com.sldeditor.datasource.impl.GeometryTypeEnum)}.
     */
    @Test
    public void testSetButtonState() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);
        assertNotNull(sldTree);
        treeTools.setButtonState(null, null, null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#removeItem()}.
     */
    @Test
    public void testRemoveItem() {
        SLDTreeTools treeTools = new SLDTreeTools();
        TestRenderSymbolInterface renderSymbol = new TestRenderSymbolInterface();
        List<RenderSymbolInterface> renderList = new ArrayList<RenderSymbolInterface>();
        renderList.add(renderSymbol);

        TestSLDTree sldTree = new TestSLDTree(renderList, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class); // Named layer
        treeTools.addNewThing(null); // Style
        treeTools.addNewThing(null); // Feature type style
        treeTools.addNewThing(null); // Rule

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewText();

        rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0).getChildAt(0)
                .getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(1, rule.getChildCount());

        // The selected item (text symbolizer) is removed
        treeTools.removeItem();
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());
        rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0).getChildAt(0)
                .getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        // The selected item (rule) is removed
        treeTools.removeItem();
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());
        DefaultMutableTreeNode featureTypeStyle = (DefaultMutableTreeNode) rootNode.getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(FeatureTypeStyleImpl.class, featureTypeStyle.getUserObject().getClass());
        assertEquals(0, featureTypeStyle.getChildCount());

        // The selected item (style) is removed
        treeTools.removeItem();
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());
        DefaultMutableTreeNode style = (DefaultMutableTreeNode) rootNode.getChildAt(0)
                .getChildAt(0);
        assertEquals(StyleImpl.class, style.getUserObject().getClass());
        assertEquals(0, style.getChildCount());

        // The selected item (named layer) is removed
        treeTools.removeItem();
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());
        DefaultMutableTreeNode namedLayer = (DefaultMutableTreeNode) rootNode.getChildAt(0);
        assertEquals(NamedLayerImpl.class, namedLayer.getUserObject().getClass());
        assertEquals(0, namedLayer.getChildCount());

        // The selected item (styled layer descriptor) is removed
        treeTools.removeItem();
        assertTrue(renderSymbol.hasRenderSymbolBeenCalled());
        assertEquals(0, rootNode.getChildCount());

        treeTools.removeItem();
        assertEquals(0, rootNode.getChildCount());
        assertFalse(renderSymbol.hasRenderSymbolBeenCalled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTreeTools#addSourceArrow()}. Test method for
     * {@link com.sldeditor.ui.tree.SLDTreeTools#addDestArrow()}.
     */
    @Test
    public void testAddArrows() {
        SLDTreeTools treeTools = new SLDTreeTools();

        TestSLDTree sldTree = new TestSLDTree(null, treeTools);

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        // Start off with just a top level SLD and no structure below it
        StyledLayerDescriptor sld = styleFactory.createStyledLayerDescriptor();

        SelectedSymbol.getInstance().setSld(sld);

        sldTree.populateSLD();
        sldTree.selectFirstSymbol();
        treeTools.addNewThing(NamedLayer.class);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);
        treeTools.addNewThing(null);

        DefaultMutableTreeNode rootNode = sldTree.getRootNode();

        // Make sure we have a rule selected
        DefaultMutableTreeNode rule = (DefaultMutableTreeNode) rootNode.getChildAt(0).getChildAt(0)
                .getChildAt(0).getChildAt(0);
        assertEquals(RuleImpl.class, rule.getUserObject().getClass());
        assertEquals(0, rule.getChildCount());

        treeTools.addNewLine();
        treeTools.addSourceArrow();
        treeTools.addDestArrow();

        DefaultMutableTreeNode lineNode = (DefaultMutableTreeNode) rule.getChildAt(0);
        assertEquals(LineSymbolizerImpl.class, lineNode.getUserObject().getClass());

        DefaultMutableTreeNode srcArrowNode = (DefaultMutableTreeNode) rule.getChildAt(1);
        assertEquals(PointSymbolizerImpl.class, srcArrowNode.getUserObject().getClass());

        DefaultMutableTreeNode destArrowNode = (DefaultMutableTreeNode) rule.getChildAt(2);
        assertEquals(PointSymbolizerImpl.class, destArrowNode.getUserObject().getClass());
    }
}
