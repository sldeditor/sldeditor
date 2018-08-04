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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint;
import java.util.ArrayList;
import java.util.List;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * The unit test for FieldConfigFeatureTypeConstraint.
 *
 * <p>{@link com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFeatureTypeConstraintTest {

    class TestFieldConfigFeatureTypeConstraint extends FieldConfigFeatureTypeConstraint {

        /**
         * Instantiates a new test field config feature type constraint.
         *
         * @param commonData the common data
         */
        public TestFieldConfigFeatureTypeConstraint(FieldConfigCommonData commonData) {
            super(commonData);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#addEntry()
         */
        @Override
        protected void addEntry() {
            super.addEntry();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#removeEntry()
         */
        @Override
        protected void removeEntry() {
            super.removeEntry();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#addExtentEntry()
         */
        @Override
        protected void addExtentEntry() {
            super.addExtentEntry();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#removeExtentEntry()
         */
        @Override
        protected void removeExtentEntry() {
            super.removeExtentEntry();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#generateExpression()
         */
        @Override
        protected Expression generateExpression() {
            return super.generateExpression();
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)
         */
        @Override
        protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
            return super.createCopy(fieldConfigBase);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#filterTableItemSelected()
         */
        @Override
        protected void filterTableItemSelected() {
            super.filterTableItemSelected();
        }

        /** The filter table. */
        public void selectFilterTableRow(int fromRow, int toRow) {
            filterTable.setColumnSelectionInterval(1, 1);
            filterTable.setRowSelectionInterval(fromRow, toRow);
        }

        public void selectExtentTableRow(int fromRow, int toRow) {
            extentTable.setRowSelectionInterval(fromRow, toRow);
        }

        public boolean isAddFTCButtonEnabled() {
            return addFTCButton.isEnabled();
        }

        public boolean isRemoveFTCButtonEnabled() {
            return removeFTCButton.isEnabled();
        }

        public boolean isAddExtentButtonEnabled() {
            return addExtentButton.isEnabled();
        }

        public boolean isRemoveExtentButtonEnabled() {
            return removeExtentButton.isEnabled();
        }

        public int getFilterRowCount() {
            return filterModel.getRowCount();
        }

        public int getExtentRowCount() {
            return extentModel.getRowCount();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#internal_setEnabled(boolean)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));

        // Field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#generateExpression()}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#populateExpression(java.lang.Object)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#setTestValue(com.sldeditor.ui.detail.config.FieldId,
     * java.util.List)}. Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#populateField(java.util.List)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#getFeatureTypeConstraint()}.
     */
    @Test
    public void testGenerateExpression() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));
        List<FeatureTypeConstraint> testValue = null;
        field.populate(null);
        field.setTestValue(FieldIdEnum.UNKNOWN, testValue);
        field.populateField(testValue);

        field.createUI();
        assertNull(field.getStringValue());

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeConstraint expectedValue1 =
                styleFactory.createFeatureTypeConstraint("Feature", Filter.INCLUDE, new Extent[0]);

        testValue = new ArrayList<FeatureTypeConstraint>();
        testValue.add(expectedValue1);
        field.populateField(testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        field.setTestValue(FieldIdEnum.UNKNOWN, testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        field.populateExpression((String) null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));

        field.revertToDefaultValue();
        assertTrue(field.getFeatureTypeConstraint().isEmpty());

        field.createUI();
        field.revertToDefaultValue();
        assertTrue(field.getFeatureTypeConstraint().isEmpty());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {

        TestFieldConfigFeatureTypeConstraint field =
                new TestFieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", false, false));
        FieldConfigFeatureTypeConstraint copy =
                (FieldConfigFeatureTypeConstraint) field.createCopy(null);
        assertNull(copy);

        copy = (FieldConfigFeatureTypeConstraint) field.createCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeConstraint expectedValue1 =
                styleFactory.createFeatureTypeConstraint("Feature", Filter.INCLUDE, new Extent[0]);

        List<FeatureTypeConstraint> testValue = new ArrayList<FeatureTypeConstraint>();

        testValue.add(expectedValue1);
        field.populateField(testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        FeatureTypeConstraint expectedValue2 =
                styleFactory.createFeatureTypeConstraint("Feature2", Filter.INCLUDE, new Extent[0]);
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
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

    @Test
    public void testUndoActionSuppress() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, true));
        field.createUI();

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FeatureTypeConstraint expectedValue1 =
                styleFactory.createFeatureTypeConstraint("Feature", Filter.INCLUDE, new Extent[0]);

        List<FeatureTypeConstraint> testValue = new ArrayList<FeatureTypeConstraint>();

        testValue.add(expectedValue1);
        int undoListSize = UndoManager.getInstance().getUndoListSize();
        field.populateField(testValue);
        assertEquals(expectedValue1, field.getFeatureTypeConstraint().get(0));

        FeatureTypeConstraint expectedValue2 =
                styleFactory.createFeatureTypeConstraint("Feature2", Filter.INCLUDE, new Extent[0]);
        List<FeatureTypeConstraint> testValue2 = new ArrayList<FeatureTypeConstraint>();
        testValue2.add(expectedValue1);
        testValue2.add(expectedValue2);
        field.populateField(testValue2);
        field.featureTypeConstraintUpdated();
        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());
    }

    @Test
    public void testAddRemoveEntry() {
        ExpressionPanelFactory.setTestMode();

        TestFieldConfigFeatureTypeConstraint field =
                new TestFieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));
        field.createUI();

        assertTrue(field.isAddFTCButtonEnabled());
        assertFalse(field.isRemoveFTCButtonEnabled());
        assertFalse(field.isAddExtentButtonEnabled());
        assertFalse(field.isRemoveExtentButtonEnabled());

        // Add an entry
        field.addEntry();

        assertEquals(1, field.getFilterRowCount());
        assertEquals(0, field.getExtentRowCount());

        assertFalse(field.isRemoveFTCButtonEnabled());
        assertFalse(field.isAddExtentButtonEnabled());
        assertFalse(field.isRemoveExtentButtonEnabled());

        // Select filter row
        field.selectFilterTableRow(0, 0);
        assertTrue(field.isRemoveFTCButtonEnabled());
        assertTrue(field.isAddExtentButtonEnabled());

        // Add 2 extent entries
        field.addExtentEntry();
        field.addExtentEntry();
        assertFalse(field.isRemoveExtentButtonEnabled());
        assertEquals(2, field.getExtentRowCount());

        // Remove 1 extent entry
        field.selectExtentTableRow(1, 1);
        assertTrue(field.isRemoveExtentButtonEnabled());

        field.removeExtentEntry();
        assertEquals(1, field.getExtentRowCount());
        assertFalse(field.isRemoveExtentButtonEnabled());

        // Remove filter row
        field.removeEntry();
        assertEquals(1, field.getFilterRowCount());
        assertEquals(0, field.getExtentRowCount());

        assertTrue(field.isAddFTCButtonEnabled());
        assertFalse(field.isRemoveFTCButtonEnabled());
        assertFalse(field.isAddExtentButtonEnabled());
        assertFalse(field.isRemoveExtentButtonEnabled());

        ExpressionPanelFactory.destroyInstance();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#featureTypeConstraintUpdated()}.
     */
    @Test
    public void testFeatureTypeConstraintUpdated() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));

        field.featureTypeConstraintUpdated();
        // No testing
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.featuretypeconstraint.FieldConfigFeatureTypeConstraint#extentUpdated()}.
     */
    @Test
    public void testExtentUpdated() {
        FieldConfigFeatureTypeConstraint field =
                new FieldConfigFeatureTypeConstraint(
                        new FieldConfigCommonData(
                                Geometry.class, FieldIdEnum.NAME, "label", true, false));

        field.extentUpdated();

        field.createUI();
        field.extentUpdated();
        // No testing
    }
}
