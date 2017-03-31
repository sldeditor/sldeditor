/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.tool.batchupdatefont;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Font;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.tool.batchupdatefont.BatchUpdateFontData;
import com.sldeditor.tool.batchupdatefont.BatchUpdateFontModel;

/**
 * The unit test for BatchUpdateFontData.
 * 
 * <p>{@link com.sldeditor.tool.batchupdatefont.BatchUpdateFontModel}
 *
 * @author Robert Ward (SCISYS)
 */
public class BatchUpdateFontModelTest {

    /**
     * Test method for {@link com.sldeditor.tool.batchupdatefont.BatchUpdateFontModel#loadData(java.util.List)}.
     */
    @Test
    public void testLoadData() {

        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        sld.layers().add(namedLayer);
        String expectedNamedLayer = "namedLayer";
        namedLayer.setName(expectedNamedLayer);
        Style style = DefaultSymbols.createNewStyle();
        String expectedStyleLayer = "style";
        style.setName(expectedStyleLayer);
        namedLayer.addStyle(style);
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        String expectedFeatureTypeStyleLayer = "feature type style";
        fts.setName(expectedFeatureTypeStyleLayer);
        style.featureTypeStyles().add(fts);
        Rule rule = DefaultSymbols.createNewRule();
        fts.rules().add(rule);
        String expectedRule = "rule";
        rule.setName(expectedRule);
        String expectedSymbolizer = "text symbolizer";
        TextSymbolizer symbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        symbolizer.setName(expectedSymbolizer);
        rule.symbolizers().add(symbolizer);
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Font font = styleFactory.createFont(ff.literal("abc"), ff.literal("normal"),
                ff.literal("normal"), ff.literal(10));
        symbolizer.setFont(font);
        
        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);
        String sldContents = sldWriter.encodeSLD(null, sld);

        String expectedWorkspace = "workspace";
        String expectedStyle = "layer.sld";
        StyleWrapper styleWrapper = new StyleWrapper(expectedWorkspace, expectedStyle);
        SLDData data = new SLDData(styleWrapper, sldContents);
        File testFile = new File("test.sld");
        data.setSLDFile(testFile);
        SLDEditorFile.getInstance().setSldFile(testFile);
        SLDEditorFile.getInstance().setSLDData(data);

        BatchUpdateFontData testObj = new BatchUpdateFontData(sld, data);

        SelectedSymbol.getInstance().setSld(sld);
        testObj.setNamedLayer(expectedNamedLayer);
        testObj.setStyle(expectedStyleLayer);
        testObj.setFeatureTypeStyle(expectedFeatureTypeStyleLayer);
        testObj.setRule(rule);
        testObj.setSymbolizer(symbolizer);
        testObj.setFont(font);

        List<BatchUpdateFontData> dataList = new ArrayList<BatchUpdateFontData>();
        dataList.add(testObj);
        BatchUpdateFontModel model = new BatchUpdateFontModel();
        model.loadData(dataList);

        for (int col = 0; col < model.getColumnCount(); col++) {
            assertFalse(model.hasValueBeenUpdated(0, col));
        }

        assertFalse(model.anyChanges());
        assertFalse(model.saveData(null));

        List<Font> actualFontlist = model.getFontEntries(new int[] { 0 });
        assertEquals(1, actualFontlist.size());
        assertEquals(1, model.getRowCount());

        // Update font
        String originalFontname = "New Font";
        String originalFontStyle = "italic";
        String originalFontWeight = "bold";
        int originalFontSize = 16;
        font = styleFactory.createFont(ff.literal(originalFontname), ff.literal(originalFontStyle),
                ff.literal(originalFontWeight), ff.literal(originalFontSize));
        model.applyData(new int[] { 0 }, font);
        assertTrue(model.hasValueBeenUpdated(0, 7));
        assertTrue(model.hasValueBeenUpdated(0, 8));
        assertTrue(model.hasValueBeenUpdated(0, 9));
        assertTrue(model.hasValueBeenUpdated(0, 10));
        assertTrue(model.anyChanges());
        assertEquals(originalFontname, model.getValueAt(0, 7));
        assertEquals(originalFontStyle, model.getValueAt(0, 8));
        assertEquals(originalFontWeight, model.getValueAt(0, 9));
        assertEquals(String.valueOf(originalFontSize), model.getValueAt(0, 10));

        model.revertData();
        assertFalse(model.anyChanges());

        int expectedFontSize = 99;
        model.applyData(new int[] { 0 }, expectedFontSize);
        assertTrue(model.hasValueBeenUpdated(0, 10)); // Font size

        assertEquals(expectedWorkspace, model.getValueAt(0, 0));
        assertEquals(expectedStyle, model.getValueAt(0, 1));
        assertEquals(expectedNamedLayer, model.getValueAt(0, 2));
        assertEquals(expectedStyleLayer, model.getValueAt(0, 3));
        assertEquals(expectedFeatureTypeStyleLayer, model.getValueAt(0, 4));
        assertEquals(expectedRule, model.getValueAt(0, 5));
        assertEquals(expectedSymbolizer, model.getValueAt(0, 6));
        assertEquals("abc", model.getValueAt(0, 7));
        assertEquals("normal", model.getValueAt(0, 8));
        assertEquals("normal", model.getValueAt(0, 9));
        assertEquals(String.valueOf(10 + expectedFontSize), model.getValueAt(0, 10));
        assertNull(model.getValueAt(0, 42));
        assertTrue(model.saveData(null));

        SelectedSymbol.destroyInstance();
        SLDEditorFile.destroyInstance();

        model.setColumnRenderer(null);
        JTable table = new JTable();
        table.setModel(model);
        model.setColumnRenderer(table.getColumnModel());

        assertFalse(model.isCellEditable(0, 0));

        assertEquals(Localisation.getString(BatchUpdateFontModel.class,
                "BatchUpdateFontModel.workspace"), model.getColumnName(0));
    }

}
