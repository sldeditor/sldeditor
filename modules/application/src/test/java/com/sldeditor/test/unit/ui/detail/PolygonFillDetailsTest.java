/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.ui.detail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigSlider;

/**
 * The unit test for PolygonFillDetailsTest.
 * 
 * <p>{@link com.sldeditor.ui.detail.PolygonFillDetailsTest}
 *
 * @author Robert Ward (SCISYS)
 */
public class PolygonFillDetailsTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.PolygonFillDetailsTest#PolygonFillDetailsTest(java.lang.Class, com.sldeditor.filter.v2.function.FunctionNameInterface)}.
     */
    @Test
    public void testFillDetailsPolygon() {
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder
                .getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        
        PolygonFillDetails panel = new PolygonFillDetails();
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

        FieldConfigSlider opacityField = (FieldConfigSlider) fieldDataManager
                .get(FieldIdEnum.OVERALL_OPACITY);
        double opacity = 0.15;
        opacityField.populateField(opacity);

        panel.dataChanged(FieldIdEnum.UNKNOWN);

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
        
        // Increase the coverage, set the fill
        Fill fill = styleFactory.createFill(ff.literal("#123456"));
        Graphic graphic = styleFactory.createDefaultGraphic();
        fill.setGraphicFill(graphic);
        symbolizer.setFill(fill);
        panel.populate(SelectedSymbol.getInstance());

        Graphic graphic2 = styleFactory.createDefaultGraphic();
        graphic2.setDisplacement(styleFactory.createDisplacement(ff.literal(6.0), ff.literal(7.0)));
        graphic2.setAnchorPoint(styleFactory.anchorPoint(ff.literal(6.0), ff.literal(7.0)));
        Fill fill2 = styleFactory.createFill(ff.literal("#123456"));
        fill2.setGraphicFill(graphic2);
        symbolizer.setFill(fill2);
        panel.populate(SelectedSymbol.getInstance());

        symbolizer.setFill(null);
        panel.populate(SelectedSymbol.getInstance());
    }
}
