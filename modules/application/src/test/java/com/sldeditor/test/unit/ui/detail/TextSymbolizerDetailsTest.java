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

package com.sldeditor.test.unit.ui.detail;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import com.sldeditor.ui.detail.config.FieldConfigString;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.Test;

/**
 * The unit test for TextSymbolizerDetails.
 *
 * <p>{@link com.sldeditor.ui.detail.TextSymbolizerDetails}
 *
 * @author Robert Ward (SCISYS)
 */
public class TextSymbolizerDetailsTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.TextSymbolizerDetails#TextSymbolizerDetails(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.TextSymbolizerDetails#populate(com.sldeditor.common.data.SelectedSymbol)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.TextSymbolizerDetails#dataChanged(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.TextSymbolizerDetails#getFieldDataManager()}.
     * Test method for {@link com.sldeditor.ui.detail.TextSymbolizerDetails#isDataPresent()}. Test
     * method for {@link com.sldeditor.ui.detail.TextSymbolizerDetails#preLoadSymbol()}.
     */
    @Test
    public void testTextSymbolizerDetails() {
        TextSymbolizerDetails panel = new TextSymbolizerDetails();
        panel.populate(null);

        // Set up test data
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
        rule.symbolizers().add(symbolizer);
        fts.rules().add(rule);
        sld.layers().add(namedLayer);
        SelectedSymbol.getInstance().addNewStyledLayer(namedLayer);
        SelectedSymbol.getInstance().setStyledLayer(namedLayer);
        SelectedSymbol.getInstance().setStyle(style);
        SelectedSymbol.getInstance().setFeatureTypeStyle(fts);
        SelectedSymbol.getInstance().setRule(rule);
        SelectedSymbol.getInstance().setSymbolizer(symbolizer);

        panel.populate(SelectedSymbol.getInstance());

        GraphicPanelFieldManager fieldDataManager = panel.getFieldDataManager();
        assertNotNull(fieldDataManager);

        panel.dataChanged(null);

        FieldConfigString nameField = (FieldConfigString) fieldDataManager.get(FieldIdEnum.NAME);
        assertNull(nameField);
        assertTrue(panel.isDataPresent());

        // Reset to default value
        panel.preLoadSymbol();
    }
}
