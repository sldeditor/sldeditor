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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel;
import com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModelUpdateInterface;

/**
 * The unit test for FeatureTypeConstraintModel.
 * <p>{@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel}
 *
 * @author Robert Ward (SCISYS)
 */
public class FeatureTypeConstraintModelTest {

    class TestModelUpdate implements FeatureTypeConstraintModelUpdateInterface
    {
        private boolean ftcUpdated = false;

        @Override
        public void featureTypeConstraintUpdated() {
            ftcUpdated = true;
        }

        @Override
        public void extentUpdated() {
        }

        public boolean hasFTCUpdatedBeenCalled()
        {
            boolean tmp = ftcUpdated;
            ftcUpdated = false;
            return tmp;
        }
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#isCellEditable(int, int)}.
     */
    @Test
    public void testIsCellEditable() {
        FeatureTypeConstraintModel model = new FeatureTypeConstraintModel(null);

        assertTrue(model.isCellEditable(0, 0));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#getColumnCount()}.
     */
    @Test
    public void testGetColumnCount() {
        FeatureTypeConstraintModel model = new FeatureTypeConstraintModel(null);

        assertEquals(2, model.getColumnCount());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#getColumnName(int)}.
     */
    @Test
    public void testGetColumnNameInt() {
        FeatureTypeConstraintModel model = new FeatureTypeConstraintModel(null);

        assertTrue(model.getColumnName(0).compareTo(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintModel.name")) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#getRowCount()}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#getValueAt(int, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#addNewEntry()}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#setValueAt(java.lang.Object, int, int)}.
     */
    @Test
    public void testGetValueAt() {
        TestModelUpdate testUpdate = new TestModelUpdate();

        FeatureTypeConstraintModel model = new FeatureTypeConstraintModel(testUpdate);

        assertEquals(0, model.getRowCount());

        assertFalse(testUpdate.hasFTCUpdatedBeenCalled());
        model.addNewEntry();
        assertTrue(testUpdate.hasFTCUpdatedBeenCalled());
        assertEquals(1, model.getRowCount());

        String expectedValue1 = "Feature";
        String expectedValue2 = "Filter.INCLUDE";

        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(99, 0));
        assertNull(model.getValueAt(0, -1));
        assertNull(model.getValueAt(0, 99));

        String actualValue = (String) model.getValueAt(0, 0);
        assertTrue(expectedValue1.compareTo(actualValue) == 0);
        String actualValue2 = (String) model.getValueAt(0, 1);
        assertTrue(expectedValue2.compareTo(actualValue2) == 0);

        // SetValueAt
        model.setValueAt("", -1, 0);
        model.setValueAt("", 99, 0);
        model.setValueAt("", 0, -1);
        model.setValueAt("", 0, 99);

        expectedValue1 = "Updated feature";
        expectedValue2 = "Filter.EXCLUDE";
        assertFalse(testUpdate.hasFTCUpdatedBeenCalled());
        model.setValueAt(expectedValue1, 0, 0);
        assertTrue(testUpdate.hasFTCUpdatedBeenCalled());
        model.setValueAt(Filter.EXCLUDE, 0, 1);
        actualValue = (String) model.getValueAt(0, 0);
        assertTrue(expectedValue1.compareTo(actualValue) == 0);

        // Setting filter is done a different way
        actualValue = (String) model.getValueAt(0, 1);
        assertTrue("Filter.INCLUDE".compareTo(actualValue2) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#populate(java.util.List)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#removeEntries(int, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#getFeatureTypeConstraint(int)}.
     */
    @Test
    public void testPopulate() {
        TestModelUpdate testUpdate = new TestModelUpdate();
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        FeatureTypeConstraintModel model = new FeatureTypeConstraintModel(testUpdate);

        List<FeatureTypeConstraint> ftcList = null;
        model.populate(ftcList);

        ftcList = new ArrayList<FeatureTypeConstraint>();
        ftcList.add(styleFactory.createFeatureTypeConstraint("ftc1", Filter.INCLUDE, new Extent[0]));
        ftcList.add(styleFactory.createFeatureTypeConstraint("ftc2", Filter.INCLUDE, new Extent[0]));
        ftcList.add(styleFactory.createFeatureTypeConstraint("ftc3", Filter.INCLUDE, new Extent[0]));
        ftcList.add(styleFactory.createFeatureTypeConstraint("ftc4", Filter.INCLUDE, new Extent[0]));
        ftcList.add(styleFactory.createFeatureTypeConstraint("ftc5", Filter.INCLUDE, new Extent[0]));
        model.populate(ftcList);

        List<FeatureTypeConstraint> actualList = model.getFeatureTypeConstraint();

        assertEquals(5, actualList.size());
        assertTrue(actualList.get(2).getFeatureTypeName().compareTo("ftc3") == 0);

        assertFalse(testUpdate.hasFTCUpdatedBeenCalled());
        model.removeEntries(-1, 2);
        assertFalse(testUpdate.hasFTCUpdatedBeenCalled());
        model.removeEntries(2, 22);
        assertFalse(testUpdate.hasFTCUpdatedBeenCalled());
        model.removeEntries(22, 2);
        assertFalse(testUpdate.hasFTCUpdatedBeenCalled());
        model.removeEntries(2, 2);
        assertTrue(testUpdate.hasFTCUpdatedBeenCalled());
        actualList = model.getFeatureTypeConstraint();

        assertEquals(4, actualList.size());
        assertTrue(actualList.get(2).getFeatureTypeName().compareTo("ftc4") == 0);

        assertNull(model.getFeatureTypeConstraint(-1));
        assertNull(model.getFeatureTypeConstraint(6));
        FeatureTypeConstraint actualFTC = model.getFeatureTypeConstraint(1);
        assertTrue(actualFTC.getFeatureTypeName().compareTo("ftc2") == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModel#isFilterColumn(int[])}.
     */
    @Test
    public void testIsFilterColumn() {
        FeatureTypeConstraintModel model = new FeatureTypeConstraintModel(null);

        assertFalse(model.isFilterColumn(null));

        int[] columns = new int[3];

        columns[0] = 0;
        columns[1] = 10;
        columns[2] = -1;
        assertFalse(model.isFilterColumn(columns));

        columns[2] = 1;
        assertTrue(model.isFilterColumn(columns));
    }

}
