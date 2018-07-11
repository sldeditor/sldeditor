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

package com.sldeditor.test.unit.create.sld;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.create.sld.NewPointSLD;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;
import org.junit.jupiter.api.Test;

/**
 * The Class NewPointSLD.
 *
 * @author Robert Ward (SCISYS)
 */
public class NewPointSLDTest {

    /** Test new point sld. */
    @Test
    public void testNewPointSLD() {
        NewPointSLD newData = new NewPointSLD();

        StyledLayerDescriptor sld = newData.create();

        assertTrue(sld.layers().size() == 1);

        StyledLayer styledLayer = sld.layers().get(0);

        NamedLayer namedLayer = (NamedLayer) styledLayer;

        Symbolizer symbolizer =
                namedLayer
                        .styles()
                        .get(0)
                        .featureTypeStyles()
                        .get(0)
                        .rules()
                        .get(0)
                        .symbolizers()
                        .get(0);

        assertTrue(symbolizer instanceof PointSymbolizer);
    }
}
