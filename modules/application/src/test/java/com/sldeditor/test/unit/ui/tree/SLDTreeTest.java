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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.EmptyPanel;
import com.sldeditor.ui.detail.FeatureTypeStyleDetails;
import com.sldeditor.ui.detail.LineSymbolizerDetails;
import com.sldeditor.ui.detail.NamedLayerDetails;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PointSymbolizerDetails;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.PolygonSymbolizerDetails;
import com.sldeditor.ui.detail.RasterSymbolizerDetails;
import com.sldeditor.ui.detail.RuleDetails;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.StyleDetails;
import com.sldeditor.ui.detail.SymbolizerDetailsPanel;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.tree.SLDTree;
import com.sldeditor.ui.tree.SLDTreeTools;
import com.sldeditor.ui.tree.UpdateTreeStructureInterface;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.jupiter.api.Test;

/**
 * The unit test for SLDTree.
 *
 * <p>{@link com.sldeditor.ui.tree.SLDTree}
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTreeTest {

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTree#SLDTree(java.util.List)} using null
     * values.
     */
    @Test
    public void testSLDTreeNull() {
        List<RenderSymbolInterface> renderList = null;
        SLDTree tree1 = new SLDTree(renderList, null);

        // Select top level node
        tree1.selectFirstSymbol();

        URL url = SLDTreeTest.class.getResource("/polygon/sld/polygon_attributebasedpolygon.sld");

        String sldContents = readFile(new File(url.getFile()).getAbsolutePath());

        SLDData sldData = new SLDData(null, sldContents);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        SelectedSymbol.getInstance().setSld(sld);

        tree1.populateSLD();

        // Nothing selected at this stage
        tree1.leafSelected();
        PopulateDetailsInterface panel = tree1.getSelectedSymbolPanel();
        assertNull(panel);

        // Select top level node
        tree1.selectFirstSymbol();

        tree1.leafSelected();

        panel = tree1.getSelectedSymbolPanel();
        assertNull(panel);
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTree#SLDTree(java.util.List)} using values.
     * Test method for {@link
     * com.sldeditor.ui.tree.SLDTree#addSymbolSelectedListener(com.sldeditor.ui.iface.SymbolizerSelectedInterface)}.
     * Test method for {@link com.sldeditor.ui.tree.SLDTree#selectFirstSymbol()}. Test method for
     * {@link com.sldeditor.ui.tree.SLDTree#selectTreeItem(com.sldeditor.TreeSelectionData)}. Test
     * method for {@link com.sldeditor.ui.tree.SLDTree#getSelectedSymbolPanel()}. Test method for
     * {@link com.sldeditor.ui.tree.SLDTree#leafSelected()}.
     */
    @Test
    public void testSLDTree() {
        List<RenderSymbolInterface> renderList = null;
        SLDTreeTools treeTools = new SLDTreeTools();
        SLDTree tree1 = new SLDTree(renderList, treeTools);

        URL url = SLDTreeTest.class.getResource("/test/polygon_line_point.sld");

        String sldContents = readFile(new File(url.getFile()).getAbsolutePath());

        SLDData sldData = new SLDData(null, sldContents);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

        SelectedSymbol.getInstance().setSld(sld);

        try {
            SymbolizerDetailsPanel symbolizerSelectedPanel = new SymbolizerDetailsPanel(null, null);
            tree1.addSymbolSelectedListener(symbolizerSelectedPanel);
            tree1.populateSLD();

            // Nothing selected at this stage
            tree1.leafSelected();
            PopulateDetailsInterface panel = tree1.getSelectedSymbolPanel();
            assertNull(panel);

            // Select top level node
            tree1.selectFirstSymbol();

            tree1.leafSelected();

            panel = tree1.getSelectedSymbolPanel();
            assertEquals(EmptyPanel.class, panel.getClass());

            // Select layer
            TreeSelectionData selectedTreeData = new TreeSelectionData();
            selectedTreeData.setLayerIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(NamedLayerDetails.class, panel.getClass());

            // Select style
            selectedTreeData.setStyleIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(StyleDetails.class, panel.getClass());

            // Select feature type style
            selectedTreeData.setFeatureTypeStyleIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(FeatureTypeStyleDetails.class, panel.getClass());

            // Select rule
            selectedTreeData.setRuleIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(RuleDetails.class, panel.getClass());

            // Select polygon symbolizer
            selectedTreeData.setSelectedPanel(PolygonSymbolizerDetails.class);
            selectedTreeData.setSymbolizerIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(PolygonSymbolizerDetails.class, panel.getClass());

            // Select line symbolizer
            selectedTreeData.setSelectedPanel(LineSymbolizerDetails.class);
            selectedTreeData.setRuleIndex(1);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(LineSymbolizerDetails.class, panel.getClass());

            // Select point symbolizer
            selectedTreeData.setSelectedPanel(PointSymbolizerDetails.class);
            selectedTreeData.setRuleIndex(2);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(PointSymbolizerDetails.class, panel.getClass());

            // Select text symbolizer
            selectedTreeData.setSelectedPanel(TextSymbolizerDetails.class);
            selectedTreeData.setSymbolizerIndex(1);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(TextSymbolizerDetails.class, panel.getClass());

            // Select raster symbolizer
            selectedTreeData.setSelectedPanel(RasterSymbolizerDetails.class);
            selectedTreeData.setSymbolizerIndex(2);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(RasterSymbolizerDetails.class, panel.getClass());

            // Select polygon symbolizer / fill
            selectedTreeData.setSelectedPanel(PolygonFillDetails.class);
            selectedTreeData.setSymbolizerIndex(0);
            selectedTreeData.setSymbolizerDetailIndex(0);
            selectedTreeData.setRuleIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(PolygonFillDetails.class, panel.getClass());

            // Select polygon symbolizer / stroke
            selectedTreeData.setSelectedPanel(StrokeDetails.class);
            selectedTreeData.setSymbolizerDetailIndex(1);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(StrokeDetails.class, panel.getClass());

            // Select point symbolizer / fill
            selectedTreeData.setSelectedPanel(PointFillDetails.class);
            selectedTreeData.setRuleIndex(2);
            selectedTreeData.setSymbolizerIndex(0);
            selectedTreeData.setSymbolizerDetailIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(PointFillDetails.class, panel.getClass());

            // Select line symbolizer / stroke
            selectedTreeData.setSelectedPanel(StrokeDetails.class);
            selectedTreeData.setRuleIndex(1);
            selectedTreeData.setSymbolizerIndex(0);
            selectedTreeData.setSymbolizerDetailIndex(0);
            assertTrue(tree1.selectTreeItem(selectedTreeData));
            panel = tree1.getSelectedSymbolPanel();
            assertEquals(StrokeDetails.class, panel.getClass());

            // Error cases
            selectedTreeData.setSymbolizerDetailIndex(-2);
            assertFalse(tree1.selectTreeItem(selectedTreeData));
            selectedTreeData.setSymbolizerDetailIndex(1);
            assertFalse(tree1.selectTreeItem(selectedTreeData));
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
     * Read file.
     *
     * @param fileName the file name
     * @return the string
     */
    private static String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        assertNotNull(br, "Failed to read test file : " + fileName);

        try {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /** Test method for {@link com.sldeditor.ui.tree.SLDTree#populateSLD()}. */
    @Test
    public void testPopulateSLD() {
        List<RenderSymbolInterface> renderList = null;
        UpdateTreeStructureInterface tree1 = new SLDTree(renderList, null);

        tree1.populateSLD();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.SLDTree#valueChanged(javax.swing.event.TreeSelectionEvent)}.
     */
    @Test
    public void testValueChanged() {
        // Do nothing, calls leafSelected()
        SLDTree tree1 = new SLDTree(null, null);
        tree1.valueChanged(null);
    }

    /** Test method for {@link com.sldeditor.ui.tree.SLDTree#textUpdated()}. */
    @Test
    public void testTextUpdated() {
        SLDTree tree1 = new SLDTree(null, null);
        tree1.textUpdated();
    }

    /**
     * Test method for {@link com.sldeditor.ui.tree.SLDTree#updateNode(java.lang.Object,
     * java.lang.Object)}.
     */
    @Test
    public void testUpdateNode() {
        SLDTree tree1 = new SLDTree(null, null);
        tree1.updateNode(null, null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.SLDTree#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum,
     * boolean)}.
     */
    @Test
    public void testDataSourceLoaded() {
        SLDTree tree1 = new SLDTree(null, null);
        tree1.dataSourceLoaded(GeometryTypeEnum.POLYGON, false);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.SLDTree#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        SLDTree tree1 = new SLDTree(null, null);

        assertEquals(tree1, tree1.getUndoObject());
        tree1.undoAction(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.tree.SLDTree#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testRedoAction() {
        SLDTree tree1 = new SLDTree(null, null);

        tree1.redoAction(null);
    }
}
