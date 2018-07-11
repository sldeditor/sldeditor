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

import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.FeatureTypeStyleDetails;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle.VOGeoServerFTSComposite;
import com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle.VendorOptionFTSFactory;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.jupiter.api.Test;

/**
 * The Class VendorOptionFTSFactoryTest.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionFTSFactoryTest {

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.vendor.geoserver.featuretypestyle.VendorOptionFTSFactory#VendorOptionFTSFactory(java.lang.Class)}.
     */
    @Test
    public void testVendorOptionFTSFactory() {
        VendorOptionFTSFactory obj = new VendorOptionFTSFactory(FeatureTypeStyleDetails.class);

        int expectedNoOfVO = 4;
        assertEquals(expectedNoOfVO, obj.getVendorOptionList().size());

        List<VendorOptionPresent> vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle();
        obj.getMinimumVersion(null, fts, vendorOptionsPresentList);
        assertEquals(0, vendorOptionsPresentList.size());

        // Valid string
        String expectedValue = "exclude,0.12";
        fts.getOptions().put(FeatureTypeStyle.COMPOSITE, expectedValue);
        obj.getMinimumVersion(null, fts, vendorOptionsPresentList);
        assertEquals(1, vendorOptionsPresentList.size());

        assertEquals(expectedNoOfVO, obj.getVendorOptionInfoList().size());
        assertEquals(1, obj.getVendorOptionList(VOGeoServerFTSComposite.class.getName()).size());

        GraphicPanelFieldManager fieldMgr =
                new GraphicPanelFieldManager(FeatureTypeStyleDetails.class);
        obj.getFieldDataManager(fieldMgr);

        obj.populate(fts);
        obj.updateSymbol(fts);
    }
}
