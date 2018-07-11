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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.tool.batchupdatefont.BatchUpdateFontData;
import com.sldeditor.tool.batchupdatefont.BatchUpdateFontUtils;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.Test;

/**
 * The unit test for BatchUpdateFontData.
 *
 * <p>{@link com.sldeditor.tool.batchupdatefont.BatchUpdateFontUtils}
 *
 * @author Robert Ward (SCISYS)
 */
public class BatchUpdateFontUtilsTest {

    /**
     * Test method for {@link
     * com.sldeditor.tool.batchupdatefont.BatchUpdateFontUtils#containsFonts(com.sldeditor.common.SLDDataInterface)}.
     */
    @Test
    public void testContainsFonts() {

        StyledLayerDescriptor sld = DefaultSymbols.createNewSLD();
        SelectedSymbol.getInstance().createNewSLD(sld);
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        String expectedNameLayerValue = "named layer test value";
        namedLayer.setName(expectedNameLayerValue);
        Style style = DefaultSymbols.createNewStyle();
        String expectedNameStyleValue = "style test value";
        style.setName(expectedNameStyleValue);
        namedLayer.addStyle(style);
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        String expectedNameFTSValue = "feature type style test value";
        fts.setName(expectedNameFTSValue);
        style.featureTypeStyles().add(fts);
        Rule rule = DefaultSymbols.createNewRule();
        String expectedRuleValue = "rule test value";
        rule.setName(expectedRuleValue);

        TextSymbolizer symbolizer = DefaultSymbols.createDefaultTextSymbolizer();
        String expectedNameValue = "symbolizer test value";
        symbolizer.setName(expectedNameValue);
        rule.symbolizers().add(DefaultSymbols.createDefaultPointSymbolizer());
        fts.rules().add(rule);
        sld.layers().add(namedLayer);

        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);
        String sldContents = sldWriter.encodeSLD(null, sld);

        SLDData sldData = new SLDData(new StyleWrapper("workspace", "layer.sld"), sldContents);

        // Empty sld
        List<BatchUpdateFontData> actualList = BatchUpdateFontUtils.containsFonts(null);
        assertNull(actualList);

        // No fonts
        actualList = BatchUpdateFontUtils.containsFonts(sldData);
        assertNull(actualList);

        // With textsymbolizer
        rule.symbolizers().add(symbolizer);
        sldContents = sldWriter.encodeSLD(null, sld);
        sldData = new SLDData(new StyleWrapper("workspace", "layer.sld"), sldContents);
        actualList = BatchUpdateFontUtils.containsFonts(sldData);
        assertEquals(1, actualList.size());
        assertEquals(rule.getName(), actualList.get(0).getRuleName());
    }
}
