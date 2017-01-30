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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Font;
import org.geotools.styling.Rule;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.tool.batchupdatefont.BatchUpdateFontData;

/**
 * The unit test for BatchUpdateFontData.
 * <p>
 * {@link com.sldeditor.tool.batchupdatefont.BatchUpdateFontData}
 *
 * @author Robert Ward (SCISYS)
 */
public class BatchUpdateFontDataTest {

    /**
     * Test method for
     * {@link com.sldeditor.tool.batchupdatefont.BatchUpdateFontData#BatchUpdateFontData(org.geotools.styling.StyledLayerDescriptor, com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testBatchUpdateFontData() {
        BatchUpdateFontData testObj = new BatchUpdateFontData(null, null);

        assertNull(testObj.getName());
        assertNull(testObj.getWorkspace());
        assertNull(testObj.getSldData());

        String expectedWorkspace = "workspace";
        String expectedStyle = "layer.sld";
        StyleWrapper styleWrapper = new StyleWrapper(expectedWorkspace, expectedStyle);
        SLDData data = new SLDData(styleWrapper, null);

        testObj = new BatchUpdateFontData(null, data);
        assertEquals(testObj.getWorkspace(), expectedWorkspace);
        assertEquals(testObj.getName(), expectedStyle);
        assertEquals(testObj.getSldData(), data);

        String expectedNamedLayer = "namedLayer";
        testObj.setNamedLayer(expectedNamedLayer);
        assertEquals(testObj.getNamedLayer(), expectedNamedLayer);

        String expectedStyleLayer = "style";
        testObj.setStyle(expectedStyleLayer);
        assertEquals(testObj.getStyle(), expectedStyleLayer);

        String expectedFeatureTypeStyleLayer = "feature type style";
        testObj.setFeatureTypeStyle(expectedFeatureTypeStyleLayer);
        assertEquals(testObj.getFeatureTypeStyle(), expectedFeatureTypeStyleLayer);

        assertNull(testObj.getRule());
        assertEquals(testObj.getRuleName(), "");
        assertEquals(testObj.getSymbolizer(), "");

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        String expectedRule = "rule";
        Rule rule = styleFactory.createRule();
        rule.setName(expectedRule);
        testObj.setRule(rule);
        assertEquals(testObj.getRule(), rule);
        assertEquals(testObj.getRuleName(), expectedRule);

        String expectedSymbolizer = "text symbolizer";
        TextSymbolizer symbolizer = styleFactory.createTextSymbolizer();
        symbolizer.setName(expectedSymbolizer);
        testObj.setSymbolizer(symbolizer);
        testObj.setFont(symbolizer.getFont());
        assertEquals(testObj.getSymbolizer(), expectedSymbolizer);

        // Test with font == null
        assertFalse(testObj.isFontNameSet());
        assertFalse(testObj.isFontStyleSet());
        assertFalse(testObj.isFontWeightSet());
        assertFalse(testObj.isFontSizeSet());
        assertEquals(testObj.getFontName(), "");
        assertEquals(testObj.getFontStyle(), "");
        assertEquals(testObj.getFontWeight(), "");
        assertEquals(testObj.getFontSize(), "");
        assertFalse(testObj.anyChanges());
        testObj.revertToOriginal();
        testObj.updateFont((Font) null);
        assertFalse(testObj.updateFont((SLDWriterInterface) null));
        testObj.setFont(null);
        testObj.updateFontSize(45);

        // Set font
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        String originalFontname = "Serif";
        String originalFontStyle = "normal";
        String originalFontWeight = "normal";
        int originalFontSize = 24;
        Font font = styleFactory.createFont(ff.literal(originalFontname), ff.literal(originalFontStyle),
                ff.literal(originalFontWeight), ff.literal(originalFontSize));
        testObj.setFont(font);
        assertTrue(testObj.isFontNameSet());
        assertTrue(testObj.isFontStyleSet());
        assertTrue(testObj.isFontWeightSet());
        assertTrue(testObj.isFontSizeSet());
        assertFalse(testObj.isFontNameUpdated());
        assertFalse(testObj.isFontStyleUpdated());
        assertFalse(testObj.isFontWeightUpdated());
        assertFalse(testObj.isFontSizeUpdated());
        assertFalse(testObj.anyChanges());

        // Update with a different copy of the same font - no changes
        Font unchangedFont = styleFactory.createFont(ff.literal(originalFontname), ff.literal(originalFontStyle),
                ff.literal(originalFontWeight), ff.literal(originalFontSize));

        testObj.updateFont(unchangedFont);
        assertFalse(testObj.isFontNameUpdated());
        assertFalse(testObj.isFontStyleUpdated());
        assertFalse(testObj.isFontWeightUpdated());
        assertFalse(testObj.isFontSizeUpdated());

        // Update with a different copy of the same font - changes
        String expectedFontName = "NewFont";
        String expectedFontStyle = "italic";
        String expectedFontWeight = "bold";
        int expectedFontSize = 12;
        Font changedFont = styleFactory.createFont(ff.literal(expectedFontName),
                ff.literal(expectedFontStyle), ff.literal(expectedFontWeight),
                ff.literal(expectedFontSize));

        testObj.updateFont(changedFont);
        assertTrue(testObj.isFontNameUpdated());
        assertTrue(testObj.isFontStyleUpdated());
        assertTrue(testObj.isFontWeightUpdated());
        assertTrue(testObj.isFontSizeUpdated());
        assertTrue(testObj.anyChanges());

        assertEquals(testObj.getFontName(), expectedFontName);
        assertEquals(testObj.getFontStyle(), expectedFontStyle);
        assertEquals(testObj.getFontWeight(), expectedFontWeight);
        assertEquals(testObj.getFontSize(), String.valueOf(expectedFontSize));
        assertEquals(testObj.getFont(), changedFont);

        // Increment font size
        int expectedIncreaseFontSize = 5;
        testObj.updateFontSize(expectedIncreaseFontSize);
        assertEquals(testObj.getFontSize(), String.valueOf(expectedFontSize + expectedIncreaseFontSize));

        // Decrease font size
        expectedIncreaseFontSize *= -1;
        testObj.updateFontSize(expectedIncreaseFontSize);
        assertEquals(testObj.getFontSize(), String.valueOf(expectedFontSize));

        // Try setting the font size less than zero
        expectedIncreaseFontSize = -100;
        testObj.updateFontSize(expectedIncreaseFontSize);
        assertEquals(testObj.getFontSize(), String.valueOf(1));

        // Update font size when font size is a function
        String expectedFieldname = "fieldname";
        changedFont.setSize(ff.property(expectedFieldname));
        testObj.updateFont(changedFont);
        expectedIncreaseFontSize = 5;
        testObj.updateFontSize(expectedIncreaseFontSize);
        String expectedResult = String.format("(%s+%d)", expectedFieldname,
                expectedIncreaseFontSize);
        String actualResult = testObj.getFontSize();
        assertEquals(actualResult, expectedResult);
        
        // Revert to original
        testObj.revertToOriginal();
        assertFalse(testObj.isFontNameUpdated());
        assertFalse(testObj.isFontStyleUpdated());
        assertFalse(testObj.isFontWeightUpdated());
        assertFalse(testObj.isFontSizeUpdated());
        assertFalse(testObj.anyChanges());
        assertEquals(testObj.getFontName(), originalFontname);
        assertEquals(testObj.getFontStyle(), originalFontStyle);
        assertEquals(testObj.getFontWeight(), originalFontWeight);
        assertEquals(testObj.getFontSize(), String.valueOf(originalFontSize));
        
        // Update with the changed font again
        testObj.updateFont(changedFont);

        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);
        symbolizer.fonts().add(changedFont);
        assertFalse(testObj.updateFont(sldWriter));
    }

}
