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

package com.sldeditor.test.unit.filter.v2.function;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.filter.v2.expression.PanelField;
import com.sldeditor.filter.v2.expression.TypeManager;
import com.sldeditor.ui.detail.config.FieldConfigBoolean;
import com.sldeditor.ui.detail.config.FieldConfigBoundingBox;
import com.sldeditor.ui.detail.config.FieldConfigDate;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldConfigMapUnits;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.FieldConfigString;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.measure.Unit;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;

/**
 * Unit test for PanelField class.
 *
 * <p>{@link com.sldeditor.filter.v2.expression.PanelField}
 *
 * @author Robert Ward (SCISYS)
 */
public class PanelFieldTest {

    /**
     * Test method for {@link
     * com.sldeditor.filter.v2.expression.PanelField#getField(java.lang.Class, java.lang.String,
     * java.lang.Class, java.util.List)}.
     */
    @Test
    public void testGetField() {
        Class<?> classType = ExpressionPanelv2.class;
        String valueTextLocalisation = "ExpressionSubPanel.value";

        Map<Class<?>, Class<?>> expectedValueMap = new HashMap<Class<?>, Class<?>>();
        expectedValueMap.put(Float.class, FieldConfigDouble.class);
        expectedValueMap.put(Geometry.class, FieldConfigGeometry.class);
        expectedValueMap.put(Date.class, FieldConfigDate.class);
        expectedValueMap.put(ReferencedEnvelope.class, FieldConfigBoundingBox.class);
        expectedValueMap.put(String.class, FieldConfigString.class);
        expectedValueMap.put(Object.class, FieldConfigString.class);
        expectedValueMap.put(Boolean.class, FieldConfigBoolean.class);
        expectedValueMap.put(Integer.class, FieldConfigInteger.class);
        expectedValueMap.put(Double.class, FieldConfigDouble.class);
        expectedValueMap.put(Unit.class, FieldConfigMapUnits.class);

        for (Class<?> nodeType : expectedValueMap.keySet()) {
            FieldConfigPopulate fieldConfig =
                    PanelField.getField(
                            classType,
                            valueTextLocalisation,
                            nodeType,
                            null,
                            ExpressionNode.UNLIMITED_STRING_SIZE,
                            false,
                            true);

            Class<?> expected = expectedValueMap.get(nodeType);
            Class<?> actual = (fieldConfig == null) ? null : fieldConfig.getClass();
            assertEquals(expected, actual, nodeType.getName());
        }

        // Special case
        // Number.class
        FieldConfigPopulate fieldConfig =
                PanelField.getField(
                        classType,
                        valueTextLocalisation,
                        Number.class,
                        null,
                        ExpressionNode.UNLIMITED_STRING_SIZE,
                        false,
                        true);
        Class<?> expected = FieldConfigInteger.class;
        Class<?> actual = fieldConfig.getClass();
        assertEquals(expected, actual, Number.class.getName());

        TypeManager.getInstance().reset();
        TypeManager.getInstance().setDataType(Float.class);
        fieldConfig =
                PanelField.getField(
                        classType,
                        valueTextLocalisation,
                        Number.class,
                        null,
                        ExpressionNode.UNLIMITED_STRING_SIZE,
                        false,
                        true);
        expected = FieldConfigDouble.class;
        actual = fieldConfig.getClass();
        assertEquals(expected, actual, Number.class.getName() + "/" + Float.class.getName());

        TypeManager.getInstance().reset();
        TypeManager.getInstance().setDataType(Double.class);
        fieldConfig =
                PanelField.getField(
                        classType,
                        valueTextLocalisation,
                        Number.class,
                        null,
                        ExpressionNode.UNLIMITED_STRING_SIZE,
                        false,
                        true);
        expected = FieldConfigDouble.class;
        actual = fieldConfig.getClass();
        assertEquals(expected, actual, Number.class.getName() + "/" + Double.class.getName());

        TypeManager.getInstance().reset();
        TypeManager.getInstance().setDataType(String.class);
        fieldConfig =
                PanelField.getField(
                        classType,
                        valueTextLocalisation,
                        Number.class,
                        null,
                        ExpressionNode.UNLIMITED_STRING_SIZE,
                        false,
                        true);
        expected = FieldConfigInteger.class;
        actual = fieldConfig.getClass();
        assertEquals(expected, actual, Number.class.getName());

        // Try single character string
        TypeManager.getInstance().reset();
        TypeManager.getInstance().setDataType(String.class);
        fieldConfig =
                PanelField.getField(
                        classType, valueTextLocalisation, String.class, null, 1, true, true);
        expected = FieldConfigString.class;
        actual = fieldConfig.getClass();
        assertEquals(expected, actual, String.class.getName());

        TypeManager.getInstance().reset();
    }
}
