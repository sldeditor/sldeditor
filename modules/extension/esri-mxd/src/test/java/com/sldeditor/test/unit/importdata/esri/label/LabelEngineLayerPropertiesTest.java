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
package com.sldeditor.test.unit.importdata.esri.label;

import java.util.List;

import org.geotools.styling.Rule;
import org.junit.Test;

import com.google.gson.JsonElement;
import com.sldeditor.importdata.esri.label.LabelEngineLayerProperties;
import com.sldeditor.test.unit.importdata.esri.ParserUtils;

/**
 * Unit test for LabelEngineLayerProperties class.
 * <p>{@link com.sldeditor.importdata.esri.label.LabelEngineLayerProperties}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class LabelEngineLayerPropertiesTest {

    /**
     * Test method for {@link com.sldeditor.importdata.esri.label.LabelEngineLayerProperties#convert(java.util.List, org.geotools.styling.Rule, com.google.gson.JsonElement, int)}.
     */
    @Test
    public void testConvert() {
        LabelEngineLayerProperties r = new LabelEngineLayerProperties();

        int transparency = 1;

        JsonElement json = ParserUtils.readTestData("/LabelEngineProperties.json");

        List<Rule> labelRuleList = null;
        Rule rule = null;
        r.convert(labelRuleList, rule, json, transparency);
    }

}
