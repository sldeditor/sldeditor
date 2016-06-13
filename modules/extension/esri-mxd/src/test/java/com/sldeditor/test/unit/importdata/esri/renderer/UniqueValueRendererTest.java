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
package com.sldeditor.test.unit.importdata.esri.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.NamedLayer;
import org.geotools.styling.Rule;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sldeditor.importdata.esri.renderer.UniqueValueRenderer;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for UniqueValueRenderer class.
 * <p>{@link com.sldeditor.importdata.esri.renderer.UniqueValueRenderer}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class UniqueValueRendererTest {

    /**
     * Test method for {@link com.sldeditor.importdata.esri.renderer.UniqueValueRenderer#convert(com.google.gson.JsonObject, java.lang.String, double, double, int)}.
     */
    @Test
    public void testConvert() {
        UniqueValueRenderer r = new UniqueValueRenderer();

        String layerName = "test layer";
        double minScale = 1234.0;
        double maxScale = 987654.0;
        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/UniqueValueRenderer.json");
        JsonObject jsonObject = json.getAsJsonObject();

        StyledLayerDescriptor sld = r.convert(jsonObject, layerName, minScale, maxScale, transparency);
        
        assertTrue(sld != null);
        
        NamedLayer namedLayer = (NamedLayer) sld.layers().get(0);
        Rule rule = namedLayer.styles().get(0).featureTypeStyles().get(0).rules().get(0);
        assertTrue(Math.abs(minScale - rule.getMinScaleDenominator()) < 0.001);
        assertTrue(Math.abs(maxScale - rule.getMaxScaleDenominator()) < 0.001);
        assertEquals("Something at surface", rule.getName());
    }

}
