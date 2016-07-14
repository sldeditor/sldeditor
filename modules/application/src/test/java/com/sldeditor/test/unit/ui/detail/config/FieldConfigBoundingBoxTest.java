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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigBoundingBox;
import com.sldeditor.ui.detail.config.FieldId;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigBoundingBox.
 * <p>{@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigBoundingBoxTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#isEnabled()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#createUI(javax.swing.Box)}.
     */
    @Test
    public void testSetEnabled() {
        // Value only, no attribute/expression dropdown
        boolean valueOnly = true;
        FieldConfigBoundingBox field = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        // Text field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI(null);
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());

        // Has attribute/expression dropdown
        valueOnly = false;
        FieldConfigBoundingBox field2 = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        // Text field will not have been created
        expectedValue = true;
        field2.setEnabled(expectedValue);
        assertFalse(field2.isEnabled());

        // Create text field
        field2.createUI(null);

        assertEquals(expectedValue, field2.isEnabled());

        expectedValue = false;
        field2.setEnabled(expectedValue);

        // Actual value is coming from the attribute panel, not the text field
        assertEquals(!expectedValue, field2.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI(null);
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);

    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#populateField(org.geotools.geometry.jts.ReferencedEnvelope)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#setTestValue(com.sldeditor.ui.detail.config.FieldId, boolean)}.
     */
    @Test
    public void testGenerateExpression() {
        boolean valueOnly = true;

        class TestFieldConfigBoundingBox extends FieldConfigBoundingBox
        {
            public TestFieldConfigBoundingBox(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public Expression callGenerateExpression()
            {
                return generateExpression();
            }
        }

        TestFieldConfigBoundingBox field = new TestFieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        Expression actualExpression = field.callGenerateExpression();
        assertNotNull(actualExpression);

        // Try string values - erroneous
        field.createUI(null);
        field.populateExpression("");

        CoordinateReferenceSystem crs = CoordManager.getInstance().getWGS84();
        ReferencedEnvelope envelope1 = new ReferencedEnvelope(0.0, 1.0, 51.0, 51.1, crs);
        field.populateExpression(envelope1);
        actualExpression = field.callGenerateExpression();
        assertTrue(envelope1.toString().compareTo(actualExpression.toString()) == 0);

        ReferencedEnvelope envelope2 = new ReferencedEnvelope(-10.0, -4.0, 31.0, 45.11, crs);
        field.setTestValue(null, envelope2);
        actualExpression = field.callGenerateExpression();
        assertTrue(envelope2.toString().compareTo(actualExpression.toString()) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#revertToDefaultValue()}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#getStringValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        String expectedDefaultValue = "default value";
        field.revertToDefaultValue();
        assertNotNull(field.getStringValue());

        field.createUI(null);
        field.revertToDefaultValue();
        assertTrue(expectedDefaultValue.compareTo(field.getStringValue()) != 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);

        assertEquals(ReferencedEnvelope.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        boolean valueOnly = true;

        class TestFieldConfigBoundingBox extends FieldConfigBoundingBox
        {
            public TestFieldConfigBoundingBox(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
                super(panelId, id, label, valueOnly);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigBoundingBox field = new TestFieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        FieldConfigBoundingBox copy = (FieldConfigBoundingBox) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigBoundingBox) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        boolean valueOnly = true;
        FieldConfigBoundingBox field = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", valueOnly);
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.FieldConfigBoundingBox#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigBoundingBox field = new FieldConfigBoundingBox(Geometry.class, new FieldId(FieldIdEnum.NAME), "label", true);
        field.undoAction(null);
        field.redoAction(null);
        field.createUI(null);

        CoordinateReferenceSystem crs = CoordManager.getInstance().getWGS84();
        ReferencedEnvelope envelope1 = new ReferencedEnvelope(0.0, 1.0, 51.0, 51.1, crs);
        field.populateField(envelope1);
        assertTrue(envelope1.toString().compareTo(field.getStringValue()) == 0);

        ReferencedEnvelope envelope2 = new ReferencedEnvelope(-10.0, -4.0, 31.0, 45.11, crs);
        field.populateField(envelope2);
        assertTrue(envelope2.toString().compareTo(field.getStringValue()) == 0);

        UndoManager.getInstance().undo();
        String actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(envelope1.toString()) == 0);

        UndoManager.getInstance().redo();
        actualValue = field.getStringValue();
        assertTrue(actualValue.compareTo(envelope2.toString()) == 0);

        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
    }
}
