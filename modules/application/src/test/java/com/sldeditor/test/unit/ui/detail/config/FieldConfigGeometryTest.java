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

package com.sldeditor.test.unit.ui.detail.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigGeometry;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigGeometry.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigGeometry}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigGeometryTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigGeometry field2 = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI();

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#populateField(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#getStringValue()}.
     */
    @Test
    public void testGenerateExpression() {
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true), "button text");

        field.createUI();
        field.setTestValue(FieldIdEnum.UNKNOWN, (String)null);
        field.populateField((String)null);
        field.populateExpression((String)null);

        String wktPoint = "POINT( 48.44 -123.37)";

        field.populateField(wktPoint);
        String actualValue = field.getStringValue();
        assertTrue(wktPoint.compareTo(actualValue) == 0);

        String wktPolygon = "POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))";

        field.setTestValue(FieldIdEnum.UNKNOWN, wktPolygon);
        actualValue = field.getStringValue();
        assertTrue(wktPolygon.compareTo(actualValue) == 0);

        String wktLine = "LINESTRING(0 2, 2 0, 8 6)";

        field.populateExpression(wktLine);
        actualValue = field.getStringValue();
        assertTrue(wktLine.compareTo(actualValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#setDefaultValue(java.lang.String)}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");

        String expectedDefaultValue = "default value";
        field.revertToDefaultValue();
        assertNull(field.getStringValue());

        field.createUI();
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
        field.setDefaultValue(expectedDefaultValue);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigGeometry extends FieldConfigGeometry
        {
            public TestFieldConfigGeometry(FieldConfigCommonData commonData, String buttonText) {
                super(commonData, buttonText);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigGeometry field = new TestFieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");
        FieldConfigGeometry copy = (TestFieldConfigGeometry) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigGeometry) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", false), "button text");
        field.undoAction(null);
        field.redoAction(null);

        field.createUI();
        field.setTestValue(FieldIdEnum.UNKNOWN, (String)null);
        field.populateField((String)null);
        field.populateExpression((String)null);

        String wktPoint = "POINT( 48.44 -123.37)";

        field.populateField(wktPoint);
        String actualValue = field.getStringValue();
        assertTrue(wktPoint.compareTo(actualValue) == 0);

        String wktPolygon = "POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))";

        field.populateField(wktPolygon);
        actualValue = field.getStringValue();
        assertTrue(wktPolygon.compareTo(actualValue) == 0);

        String wktLine = "LINESTRING(0 2, 2 0, 8 6)";

        field.populateExpression(wktLine);
        actualValue = field.getStringValue();
        assertTrue(wktLine.compareTo(actualValue) == 0);

        UndoManager.getInstance().undo();
        actualValue = field.getStringValue();
        assertTrue(wktPolygon.compareTo(actualValue) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(wktLine.compareTo(actualValue) == 0);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));

    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigGeometry#addButtonPressedListener(com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface)}.
     */
    @Test
    public void testAddButtonPressedListener() {
        boolean valueOnly = true;
        FieldConfigGeometry field = new FieldConfigGeometry(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", valueOnly), "button text");

        field.addButtonPressedListener(null);
    }

}
