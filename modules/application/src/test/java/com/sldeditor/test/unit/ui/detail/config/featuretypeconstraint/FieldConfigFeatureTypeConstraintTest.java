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

package com.sldeditor.test.unit.ui.detail.config.featuretypeconstraint;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.Test;
import org.opengis.filter.Filter;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigFeatureTypeConstraint.
 * <p>{@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFeatureTypeConstraintTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        // Field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#populateExpression(java.lang.Object)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#setTestValue(com.sldeditor.ui.detail.config.FieldId, java.util.List)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#populateField(java.util.List)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#getFeatureTypeConstraint()}.
     */
    @Test
    public void testGenerateExpression() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        List<FeatureTypeConstraint> testValue = null;
        field.populate(null);
        field.setTestValue(null, testValue);
        field.populateField(testValue);

        field.createUI();
        assertNull(field.getStringValue());

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeConstraint expectedValue1 = styleFactory.createFeatureTypeConstraint("Feature", 
                Filter.INCLUDE,
                new Extent[0]);

        testValue = new ArrayList<FeatureTypeConstraint>();
        testValue.add(expectedValue1);
        field.populateField(testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        field.setTestValue(null, testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        field.populateExpression((String)null);    
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        field.revertToDefaultValue();
        assertTrue(field.getFeatureTypeConstraint().isEmpty());

        field.createUI();
        field.revertToDefaultValue();
        assertTrue(field.getFeatureTypeConstraint().isEmpty());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {
        class TestFieldConfigFeatureTypeConstraint extends FieldConfigFeatureTypeConstraint
        {
            public TestFieldConfigFeatureTypeConstraint(Class<?> panelId, FieldId id, String label) {
                super(panelId, id, label);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigFeatureTypeConstraint field = new TestFieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        FieldConfigFeatureTypeConstraint copy = (FieldConfigFeatureTypeConstraint) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigFeatureTypeConstraint) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeConstraint expectedValue1 = styleFactory.createFeatureTypeConstraint("Feature", 
                Filter.INCLUDE,
                new Extent[0]);

        List<FeatureTypeConstraint> testValue = new ArrayList<FeatureTypeConstraint>();

        testValue.add(expectedValue1);
        field.populateField(testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        FeatureTypeConstraint expectedValue2 = styleFactory.createFeatureTypeConstraint("Feature2", 
                Filter.INCLUDE,
                new Extent[0]);
        List<FeatureTypeConstraint> testValue2 = new ArrayList<FeatureTypeConstraint>();
        testValue2.add(expectedValue1);
        testValue2.add(expectedValue2);
        field.populateField(testValue2);

        UndoManager.getInstance().undo();
        assertEquals(1, field.getFeatureTypeConstraint().size());
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));
        UndoManager.getInstance().redo();
        assertEquals(2, field.getFeatureTypeConstraint().size());
        assertEquals(expectedValue2, field.getFeatureTypeConstraint().get(1));

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#featureTypeConstraintUpdated()}.
     */
    @Test
    public void testFeatureTypeConstraintUpdated() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        field.featureTypeConstraintUpdated();
        // No testing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#extentUpdated()}.
     */
    @Test
    public void testExtentUpdated() {
        FieldConfigFeatureTypeConstraint field = new FieldConfigFeatureTypeConstraint(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        field.extentUpdated();

        field.createUI();
        field.extentUpdated();
        // No testing
    }

}
