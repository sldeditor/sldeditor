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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.ExtractGeometryField;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigPopulation;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for ExtractGeometryField.
 * <p>{@link com.sldeditor.ui.detail.ExtractGeometryField}
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtractGeometryFieldTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.ExtractGeometryField#getGeometryField(com.sldeditor.ui.detail.config.FieldConfigPopulation)}.
     */
    @Test
    public void testGetGeometryField() {
        assertNull(ExtractGeometryField.getGeometryField(null));

        FieldIdEnum fieldId = FieldIdEnum.GEOMETRY;

        GraphicPanelFieldManager fieldConfigManager = new GraphicPanelFieldManager(Geometry.class);

        FieldConfigGeometry geometryField = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, fieldId, "label", true), "button");
        geometryField.createUI();
        fieldConfigManager.add(fieldId, geometryField);

        FieldConfigPopulation obj = new FieldConfigPopulation(fieldConfigManager);

        // Try valid geometry field name
        geometryField.populateField("ValidTestField");

        Expression actualExpression = ExtractGeometryField.getGeometryField(obj);

        assertNotNull(actualExpression);

        // Try invalid geometry field name
        geometryField.populateField("");
        actualExpression = ExtractGeometryField.getGeometryField(obj);
        assertNull(actualExpression);
        
        // Try invalid geometry field name
        geometryField.populateField("    ");
        actualExpression = ExtractGeometryField.getGeometryField(obj);
        assertNull(actualExpression);
        
        // Try when there is no geometry field
        obj = new FieldConfigPopulation(fieldConfigManager);
        actualExpression = ExtractGeometryField.getGeometryField(obj);
        assertNull(actualExpression);
    }

}
