/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.symboltype.FieldConfigMarker;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeFactory;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicStroke;
import org.opengis.style.GraphicalSymbol;

/**
 * The unit test for StrokeDetails.
 *
 * <p>{@link com.sldeditor.ui.detail.StrokeDetails}
 *
 * @author Robert Ward (SCISYS)
 */
public class StrokeDetailsTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.StrokeDetails#StrokeDetails(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testStrokeDetailsLine() {
        StrokeDetails panel = new StrokeDetails();
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

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Stroke stroke = styleFactory.getDefaultStroke();

        SymbolTypeFactory fillFactory =
                new SymbolTypeFactory(
                        StrokeDetails.class,
                        new ColourFieldConfig(
                                GroupIdEnum.FILLCOLOUR,
                                FieldIdEnum.STROKE_FILL_COLOUR,
                                FieldIdEnum.OVERALL_OPACITY,
                                FieldIdEnum.STROKE_FILL_WIDTH),
                        new ColourFieldConfig(
                                GroupIdEnum.STROKECOLOUR,
                                FieldIdEnum.STROKE_FILL_COLOUR,
                                FieldIdEnum.OVERALL_OPACITY,
                                FieldIdEnum.STROKE_FILL_WIDTH),
                        FieldIdEnum.STROKE_STYLE);
        fillFactory.populate(panel, panel.getFieldDataManager());

        Expression symbolType = ff.literal("star");
        List<GraphicalSymbol> symbols =
                fillFactory.getValue(
                        panel.getFieldDataManager(),
                        symbolType,
                        true,
                        true,
                        FieldConfigMarker.class);

        Expression initalGap = ff.literal(0);
        Expression gap = ff.literal(0);

        GraphicStroke graphicStroke =
                styleFactory.graphicStroke(
                        symbols,
                        null,
                        ff.literal(10),
                        ff.literal(0),
                        styleFactory.createAnchorPoint(ff.literal(0.5), ff.literal(0.75)),
                        styleFactory.createDisplacement(ff.literal(0.35), ff.literal(0.12)),
                        initalGap,
                        gap);

        stroke.setDashArray(new float[] {1.0f, 2.0f, 3.0f});
        stroke.setGraphicStroke(graphicStroke);
        LineSymbolizer symbolizer = DefaultSymbols.createDefaultLineSymbolizer();
        symbolizer.setStroke(stroke);
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
        FieldConfigDouble angleField =
                (FieldConfigDouble) fieldDataManager.get(FieldIdEnum.STROKE_SYMBOL_ANGLE);
        angleField.populateField(expectedAngle);

        FieldConfigSlider opacityField =
                (FieldConfigSlider) fieldDataManager.get(FieldIdEnum.OVERALL_OPACITY);
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
        assertTrue(Math.abs(actualValue - 1.0) < 0.01);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.StrokeDetails#StrokeDetails(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testStrokeDetailsCircle() {
        StrokeDetails panel = new StrokeDetails();
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

        FieldConfigSymbolType symbolTypeField =
                (FieldConfigSymbolType) fieldDataManager.get(FieldIdEnum.STROKE_STYLE);
        symbolTypeField.populateField("circle");

        double expectedAngle = 14.5;
        FieldConfigDouble angleField =
                (FieldConfigDouble) fieldDataManager.get(FieldIdEnum.STROKE_SYMBOL_ANGLE);
        angleField.populateField(expectedAngle);

        FieldConfigSlider opacityField =
                (FieldConfigSlider) fieldDataManager.get(FieldIdEnum.OVERALL_OPACITY);
        double opacity = 0.15;
        opacityField.populateField(opacity);

        FieldConfigString dashField =
                (FieldConfigString) fieldDataManager.get(FieldIdEnum.STROKE_DASH_ARRAY);
        String dashArray = "1 2";
        dashField.populateField(dashArray);

        panel.dataChanged(null);

        double actualValue = angleField.getDoubleValue();
        assertTrue(Math.abs(actualValue - expectedAngle) < 0.01);
        assertFalse(panel.isDataPresent());

        actualValue = opacityField.getDoubleValue();
        assertTrue(Math.abs(actualValue - opacity) < 0.01);

        // Reset to default value
        panel.preLoadSymbol();
        actualValue = angleField.getDoubleValue();
        assertTrue(Math.abs(actualValue - 0.0) < 0.01);
        actualValue = opacityField.getDoubleValue();
        assertTrue(Math.abs(actualValue - 1.0) < 0.01);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.StrokeDetails#StrokeDetails(com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testStrokeDetailsPolygon() {
        StrokeDetails panel = new StrokeDetails();
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

        FieldConfigSymbolType symbolTypeField =
                (FieldConfigSymbolType) fieldDataManager.get(FieldIdEnum.STROKE_STYLE);
        symbolTypeField.populateField("circle");

        double expectedAngle = 14.5;
        FieldConfigDouble angleField =
                (FieldConfigDouble) fieldDataManager.get(FieldIdEnum.STROKE_SYMBOL_ANGLE);
        angleField.populateField(expectedAngle);

        FieldConfigSlider opacityField =
                (FieldConfigSlider) fieldDataManager.get(FieldIdEnum.OVERALL_OPACITY);
        double opacity = 0.15;
        opacityField.populateField(opacity);

        FieldConfigString dashField =
                (FieldConfigString) fieldDataManager.get(FieldIdEnum.STROKE_DASH_ARRAY);
        String dashArray = "1 2";
        dashField.populateField(dashArray);

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
        assertTrue(Math.abs(actualValue - 1.0) < 0.01);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.StrokeDetails#parseDashArray(java.lang.String)}.
     */
    @Test
    public void testParseDashArray() {}

    /** Test method for {@link com.sldeditor.ui.detail.StrokeDetails#getStroke()}. */
    @Test
    public void testGetStroke() {}

    /**
     * Test method for {@link com.sldeditor.ui.detail.StrokeDetails#createDashArrayList(float[])}.
     */
    @Test
    public void testCreateDashArrayList() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.StrokeDetails#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    public void testPopulate() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.StrokeDetails#dataChanged(com.sldeditor.ui.detail.config.FieldId)}.
     */
    @Test
    public void testDataChanged() {}

    /** Test method for {@link com.sldeditor.ui.detail.StrokeDetails#getFieldDataManager()}. */
    @Test
    public void testGetFieldDataManager() {}

    /**
     * Test method for {@link com.sldeditor.ui.detail.StrokeDetails#optionSelected(java.lang.Class,
     * java.lang.String)}.
     */
    @Test
    public void testOptionSelected() {}

    /** Test method for {@link com.sldeditor.ui.detail.StrokeDetails#isDataPresent()}. */
    @Test
    public void testIsDataPresent() {}

    /** Test method for {@link com.sldeditor.ui.detail.StrokeDetails#preLoadSymbol()}. */
    @Test
    public void testPreLoadSymbol() {}
}
