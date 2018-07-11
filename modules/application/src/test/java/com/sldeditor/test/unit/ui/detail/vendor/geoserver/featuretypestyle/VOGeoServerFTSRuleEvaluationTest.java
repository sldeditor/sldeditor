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

package com.sldeditor.test.unit.ui.detail.vendor.geoserver.featuretypestyle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.FeatureTypeStyleDetails;
import com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle.VOGeoServerFTSRuleEvaluation;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.Test;

/**
 * The Class VOGeoServerFTSRuleEvaluationTest.
 *
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerFTSRuleEvaluationTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle.VOGeoServerFTSRuleEvaluationTest#VOGeoServerFTSRuleEvaluationTest(java.lang.Class)}.
     */
    @Test
    public void testVOGeoServerFTSRuleEvaluation() {
        Class<FeatureTypeStyleDetails> panelId = FeatureTypeStyleDetails.class;
        VOGeoServerFTSRuleEvaluation obj = new VOGeoServerFTSRuleEvaluation(panelId);
        assertEquals(panelId, obj.getPanelId());

        assertNotNull(obj.getFieldDataManager());
        assertNotNull(obj.getPanel());
        assertTrue(obj.isDataPresent());

        assertNotNull(obj.getVendorOptionInfo());

        // Check getVendorOptionVersion
        VendorOptionVersion vendorOption = obj.getVendorOption();
        assertEquals(GeoServerVendorOption.class, vendorOption.getClassType());
        String actualVersionString = vendorOption.getEarliest().getVersionString();
        String expectedVersionString = "2.4.0";
        assertEquals(expectedVersionString, actualVersionString);

        obj.setParentPanel(null);
        assertNull(obj.getParentPanel());
        obj.preLoadSymbol();

        List<VendorOptionPresent> vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        String testString = "test";
        obj.getMinimumVersion(null, testString, vendorOptionsPresentList);
        assertTrue(vendorOptionsPresentList.isEmpty());

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
        obj.getMinimumVersion(null, fts, vendorOptionsPresentList);
        assertEquals(0, vendorOptionsPresentList.size());

        // Valid string - first
        String expectedValue = "first";
        fts.getOptions().put(FeatureTypeStyle.KEY_EVALUATION_MODE, expectedValue);
        obj.getMinimumVersion(null, fts, vendorOptionsPresentList);
        assertEquals(1, vendorOptionsPresentList.size());

        obj.populate(fts);
        obj.updateSymbol(fts);
        String actualValue = fts.getOptions().get(FeatureTypeStyle.KEY_EVALUATION_MODE);
        assertEquals(actualValue, expectedValue);

        // Valid string - all
        expectedValue = "all";
        fts.getOptions().put(FeatureTypeStyle.KEY_EVALUATION_MODE, expectedValue);

        obj.populate(fts);
        obj.updateSymbol(fts);
        actualValue = fts.getOptions().get(FeatureTypeStyle.KEY_EVALUATION_MODE);
        assertEquals(actualValue, expectedValue);

        // Invalid string
        expectedValue = "invalid";
        fts.getOptions().put(FeatureTypeStyle.KEY_EVALUATION_MODE, expectedValue);

        obj.populate(fts);
        obj.updateSymbol(fts);
        actualValue = fts.getOptions().get(FeatureTypeStyle.KEY_EVALUATION_MODE);
        assertEquals("all", actualValue);

        // Increase code coverage
        obj.populate((PolygonSymbolizer) null);
        obj.updateSymbol((PolygonSymbolizer) null);
        obj.populate((RasterSymbolizer) null);
        obj.updateSymbol((RasterSymbolizer) null);
        obj.populate((TextSymbolizer) null);
        obj.updateSymbol((TextSymbolizer) null);
        obj.populate((SelectedSymbol) null);
        obj.dataChanged(FieldIdEnum.VO_FTS_RULE_EVALUATION_OPTION);
    }
}
