/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigSlider;

/**
 * The unit test for FillDetails.
 * <p>{@link com.sldeditor.ui.detail.FillDetails}
 *
 * @author Robert Ward (SCISYS)
 */
public class FillDetailsTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.FillDetails#FillDetails(java.lang.Class, com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testFillDetailsPoint() {
        PointFillDetails panel = new PointFillDetails(null);
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

        PointSymbolizer symbolizer = DefaultSymbols.createDefaultPointSymbolizer();
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

        double expectedAngle = 14.5;
        FieldConfigDouble angleField = (FieldConfigDouble) fieldDataManager.get(FieldIdEnum.ANGLE);
        angleField.populateField(expectedAngle);

        FieldConfigSlider opacityField = (FieldConfigSlider) fieldDataManager.get(FieldIdEnum.OPACITY);
        double opacity = 0.15;
        opacityField.populateField(opacity);

        panel.dataChanged(null);

        double actualValue = angleField.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedAngle) < 0.01);
        assertTrue(panel.isDataPresent());

        actualValue = opacityField.getDoubleValue();
        assertTrue(Math.abs(actualValue - opacity) < 0.01);

        // Reset to default value
        panel.preLoadSymbol();
        actualValue = angleField.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.0) < 0.01);
        actualValue = opacityField.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.5) < 0.01);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.FillDetails#FillDetails(java.lang.Class, com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testFillDetailsPolygon() {
        PolygonFillDetails panel = new PolygonFillDetails(null);
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

        PolygonSymbolizer symbolizer = DefaultSymbols.createDefaultPolygonSymbolizer();
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

        double expectedAngle = 14.5;
        FieldConfigDouble angleField = (FieldConfigDouble) fieldDataManager.get(FieldIdEnum.ANGLE);
        angleField.populateField(expectedAngle);

        FieldConfigSlider opacityField = (FieldConfigSlider) fieldDataManager.get(FieldIdEnum.OPACITY);
        double opacity = 0.15;
        opacityField.populateField(opacity);

        panel.dataChanged(null);

        double actualValue = angleField.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedAngle) < 0.01);
        assertTrue(panel.isDataPresent());

        actualValue = opacityField.getDoubleValue();
        assertTrue(Math.abs(actualValue - opacity) < 0.01);

        // Reset to default value
        panel.preLoadSymbol();
        actualValue = angleField.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.0) < 0.01);
        actualValue = opacityField.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.5) < 0.01);
    }
}
