/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.detail.config.symboltype;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.ui.detail.PointFillDetails;
import com.sldeditor.ui.detail.PolygonFillDetails;
import com.sldeditor.ui.detail.StrokeDetails;
import com.sldeditor.ui.detail.TextSymbolizerDetails;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for GetMinimumVersion.
 *
 * <p>{@link com.sldeditor.ui.detail.config.symboltype.SymbolTypeFactory}
 *
 * @author Robert Ward (SCISYS)
 */
public class GetMinimumVersionTest {

    private StyleFactoryImpl styleFactory =
            (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** Test method for PointFillDetails */
    @Test
    public void testPointGetMinimumVersion() {
        PointFillDetails details = new PointFillDetails();

        details.getMinimumVersion(null, null, null);

        Graphic graphic = styleFactory.createDefaultGraphic();
        graphic.setDisplacement(styleFactory.createDisplacement(ff.literal(6.0), ff.literal(7.0)));
        graphic.setAnchorPoint(styleFactory.anchorPoint(ff.literal(6.0), ff.literal(7.0)));

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        Object parentObj = null;
        details.getMinimumVersion(parentObj, graphic, vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        details.getMinimumVersion(parentObj, graphic, vendorOptionsPresentList);

        assertTrue(vendorOptionsPresentList.size() == 0);
    }

    /** Test method for StrokeDetails */
    @Test
    public void testStrokeGetMinimumVersion() {
        StrokeDetails details = new StrokeDetails();

        details.getMinimumVersion(null, null, null);

        Stroke stroke = styleFactory.createStroke(ff.literal("#654321"), ff.literal(3.2));
        Graphic graphicStroke = styleFactory.createDefaultGraphic();
        stroke.setGraphicStroke(graphicStroke);

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        Object parentObj = null;
        details.getMinimumVersion(parentObj, stroke, vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        details.getMinimumVersion(parentObj, stroke, vendorOptionsPresentList);

        assertTrue(vendorOptionsPresentList.size() == 0);
    }

    /** Test method for PolygonFillDetails */
    @Test
    public void testPolygonFillGetMinimumVersion() {
        PolygonFillDetails details = new PolygonFillDetails();

        details.getMinimumVersion(null, null, null);

        Fill fill = styleFactory.createFill(ff.literal("#654321"), ff.literal(3.2));
        Graphic graphicFill = styleFactory.createDefaultGraphic();
        fill.setGraphicFill(graphicFill);

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        Object parentObj = null;
        details.getMinimumVersion(parentObj, fill, vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        details.getMinimumVersion(parentObj, fill, vendorOptionsPresentList);

        assertTrue(vendorOptionsPresentList.size() == 0);
    }

    /** Test method for TextSymbolizerDetails */
    @Test
    public void testTextSymbolizer2GetMinimumVersion() {
        TextSymbolizerDetails details = new TextSymbolizerDetails();

        details.getMinimumVersion(null, null, null);

        TextSymbolizer text = styleFactory.createTextSymbolizer();
        text.getOptions().put(TextSymbolizer2.MAX_DISPLACEMENT_KEY, "4.2");
        text.getOptions().put(TextSymbolizer2.UNDERLINE_TEXT_KEY, "true");
        text.getOptions().put(TextSymbolizer.WORD_SPACING_KEY, "true");

        List<VendorOptionPresent> vendorOptionsPresentList = null;
        Object parentObj = null;
        details.getMinimumVersion(parentObj, text, vendorOptionsPresentList);

        vendorOptionsPresentList = new ArrayList<VendorOptionPresent>();
        details.getMinimumVersion(parentObj, text, vendorOptionsPresentList);

        assertTrue(vendorOptionsPresentList.size() == 3);
    }
}
