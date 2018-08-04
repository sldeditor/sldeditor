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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.RuleDetails;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.base.GroupConfig;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.junit.jupiter.api.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for RuleDetails.
 *
 * <p>{@link com.sldeditor.ui.detail.RuleDetails}
 *
 * @author Robert Ward (SCISYS)
 */
public class RuleDetailsTest {

    class TestRuleDetails extends RuleDetails {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.RuleDetails#editFilter(com.sldeditor.common.xml.ui.FieldIdEnum)
         */
        @Override
        protected void editFilter(FieldIdEnum filterFieldId) {
            super.editFilter(filterFieldId);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.RuleDetails#clearFilter(com.sldeditor.common.xml.ui.FieldIdEnum)
         */
        @Override
        protected void clearFilter(FieldIdEnum filterFieldId) {
            super.clearFilter(filterFieldId);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.RuleDetails#RuleDetails(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.RuleDetails#populate(com.sldeditor.common.data.SelectedSymbol)}. Test
     * method for {@link
     * com.sldeditor.ui.detail.RuleDetails#dataChanged(com.sldeditor.ui.detail.config.FieldId)}.
     * Test method for {@link com.sldeditor.ui.detail.RuleDetails#getFieldDataManager()}. Test
     * method for {@link com.sldeditor.ui.detail.RuleDetails#isDataPresent()}. Test method for
     * {@link com.sldeditor.ui.detail.RuleDetails#preLoadSymbol()}.
     */
    @Test
    public void testRuleDetails() {

        RuleDetails panel = new RuleDetails();
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
        String expectedNameValue = "rule test value";
        rule.setName(expectedNameValue);

        Symbolizer symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        rule.symbolizers().add(symbolizer);
        fts.rules().add(rule);
        sld.layers().add(namedLayer);
        SelectedSymbol.getInstance().addNewStyledLayer(namedLayer);
        SelectedSymbol.getInstance().setStyledLayer(namedLayer);
        SelectedSymbol.getInstance().setStyle(style);
        SelectedSymbol.getInstance().setFeatureTypeStyle(fts);
        SelectedSymbol.getInstance().setRule(rule);

        panel.populate(SelectedSymbol.getInstance());

        GraphicPanelFieldManager fieldDataManager = panel.getFieldDataManager();
        assertNotNull(fieldDataManager);

        FieldConfigString filterField =
                (FieldConfigString) fieldDataManager.get(FieldIdEnum.FILTER);
        String filterString = "STATE_ABBR >= 'B' AND STATE_ABBR <= 'O'";
        filterField.populateField(filterString);

        GroupConfig groupConfig =
                (GroupConfig) fieldDataManager.getGroup(RuleDetails.class, GroupIdEnum.SCALE);
        groupConfig.enable(true);
        panel.dataChanged(null);

        FieldConfigString nameField = (FieldConfigString) fieldDataManager.get(FieldIdEnum.NAME);
        String actualValue = nameField.getStringValue();
        assertTrue(expectedNameValue.compareTo(actualValue) == 0);
        assertTrue(panel.isDataPresent());

        // Reset to default value
        panel.preLoadSymbol();
        actualValue = nameField.getStringValue();
        assertTrue("".compareTo(actualValue) == 0);
    }

    @Test
    public void testRuleEditFilter() {
        TestRuleDetails panel = new TestRuleDetails();
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
        String expectedNameValue = "rule test value";
        rule.setName(expectedNameValue);

        Symbolizer symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
        rule.symbolizers().add(symbolizer);
        fts.rules().add(rule);
        sld.layers().add(namedLayer);
        SelectedSymbol.getInstance().setRule(rule);

        panel.populate(SelectedSymbol.getInstance());

        StyledLayerDescriptor sld2 = DefaultSymbols.createNewSLD();
        SelectedSymbol.getInstance().createNewSLD(sld2);
        NamedLayer namedLayer2 = DefaultSymbols.createNewNamedLayer();
        namedLayer2.setName(expectedNameLayerValue);
        Style style2 = DefaultSymbols.createNewStyle();
        style2.setName(expectedNameStyleValue);
        namedLayer2.addStyle(style2);
        FeatureTypeStyle fts2 = DefaultSymbols.createNewFeatureTypeStyle();
        fts2.setName(expectedNameFTSValue);
        style2.featureTypeStyles().add(fts2);
        Rule rule2 = DefaultSymbols.createNewRule();
        rule2.setName(expectedNameValue);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Filter filter = ff.equal(ff.literal(1), ff.literal(2), false);
        rule2.setFilter(filter);

        rule2.symbolizers().add(symbolizer);
        fts2.rules().add(rule2);
        sld2.layers().add(namedLayer2);
        SelectedSymbol.getInstance().setRule(rule2);

        GraphicPanelFieldManager fieldDataManager = panel.getFieldDataManager();
        assertNotNull(fieldDataManager);

        int undoListSize = UndoManager.getInstance().getUndoListSize();
        ExpressionPanelFactory.setTestMode();
        panel.editFilter(FieldIdEnum.FILTER);
        FieldConfigString filterField =
                (FieldConfigString) fieldDataManager.get(FieldIdEnum.FILTER);
        String expectedString1 = "[ 1 = 2 ]";
        assertEquals(expectedString1, filterField.getStringValue());

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());

        panel.clearFilter(FieldIdEnum.FILTER);

        assertEquals(undoListSize + 2, UndoManager.getInstance().getUndoListSize());
        String expectedString2 = "";
        assertEquals(expectedString2, filterField.getStringValue());

        UndoManager.getInstance().undo();
        assertEquals(expectedString1, filterField.getStringValue());

        UndoManager.getInstance().redo();
        assertEquals(expectedString2, filterField.getStringValue());

        ExpressionPanelFactory.destroyInstance();
    }
}
