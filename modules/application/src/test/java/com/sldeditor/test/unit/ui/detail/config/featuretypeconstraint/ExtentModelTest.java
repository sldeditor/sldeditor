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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.Test;
import org.opengis.filter.Filter;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel;
import com.sldeditor.ui.detail.config.featuretypeconstraint.FeatureTypeConstraintModelUpdateInterface;

/**
 * The unit test for ExtentModel.
 * <p>{@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel}
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtentModelTest {

    class TestModelUpdate implements FeatureTypeConstraintModelUpdateInterface
    {
        private boolean extentUpdated = false;

        @Override
        public void featureTypeConstraintUpdated() {
        }

        @Override
        public void extentUpdated() {
            extentUpdated = true;
        }

        public boolean hasExtentUpdatedBeenCalled()
        {
            boolean tmp = extentUpdated;
            extentUpdated = false;
            return tmp;
        }
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#isCellEditable(int, int)}.
     */
    @Test
    public void testIsCellEditable() {
        ExtentModel model = new ExtentModel(null);

        assertTrue(model.isCellEditable(0, 0));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#getColumnCount()}.
     */
    @Test
    public void testGetColumnCount() {
        ExtentModel model = new ExtentModel(null);

        assertEquals(5, model.getColumnCount());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#getColumnName(int)}.
     */
    @Test
    public void testGetColumnNameInt() {
        ExtentModel model = new ExtentModel(null);

        assertTrue(model.getColumnName(0).compareTo(Localisation.getString(FieldConfigBase.class, "FeatureTypeConstraintExtentModel.name")) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#getValueAt(int, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#getRowCount()}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#addNewEntry()}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#setValueAt(java.lang.Object, int, int)}.
     */
    @Test
    public void testGetValueAt() {
        TestModelUpdate testUpdate = new TestModelUpdate();
        
        ExtentModel model = new ExtentModel(testUpdate);

        assertEquals(0, model.getRowCount());
        
        assertFalse(testUpdate.hasExtentUpdatedBeenCalled());
        model.addNewEntry();
        assertTrue(testUpdate.hasExtentUpdatedBeenCalled());
        assertEquals(1, model.getRowCount());

        String expectedValue1 = "New Extent";
        String expectedValue2 = "0";

        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(99, 0));
        assertNull(model.getValueAt(0, -1));
        assertNull(model.getValueAt(0, 99));

        String actualValue = (String) model.getValueAt(0, 0);
        assertTrue(expectedValue1.compareTo(actualValue) == 0);
        actualValue = (String) model.getValueAt(0, 1);
        assertTrue(expectedValue2.compareTo(actualValue) == 0);
        actualValue = (String) model.getValueAt(0, 2);
        assertTrue(expectedValue2.compareTo(actualValue) == 0);
        actualValue = (String) model.getValueAt(0, 3);
        assertTrue(expectedValue2.compareTo(actualValue) == 0);
        actualValue = (String) model.getValueAt(0, 4);
        assertTrue(expectedValue2.compareTo(actualValue) == 0);

        // SetValueAt
        model.setValueAt("", -1, 0);
        model.setValueAt("", 99, 0);
        model.setValueAt("", 0, -1);
        model.setValueAt("", 0, 99);

        expectedValue1 = "Updated Extent";
        expectedValue2 = "3.142";
        assertFalse(testUpdate.hasExtentUpdatedBeenCalled());
        model.setValueAt(expectedValue1, 0, 0);
        assertTrue(testUpdate.hasExtentUpdatedBeenCalled());
        model.setValueAt(expectedValue2, 0, 1);
        model.setValueAt(expectedValue2, 0, 2);
        model.setValueAt(expectedValue2, 0, 3);
        model.setValueAt(expectedValue2, 0, 4);
        actualValue = (String) model.getValueAt(0, 0);
        assertTrue(expectedValue1.compareTo(actualValue) == 0);

        actualValue = (String) model.getValueAt(0, 1);
        assertTrue(expectedValue2.compareTo(actualValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#getExtentList()}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#removeEntries(int, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#populate(org.geotools.styling.Extent[])}.
     */
    @Test
    public void testGetExtentList() {
        TestModelUpdate testUpdate = new TestModelUpdate();
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        ExtentModel model = new ExtentModel(testUpdate);

        Extent[] extentArray = null;
        model.populate(extentArray);

        extentArray = new Extent[5];
        extentArray[0] = styleFactory.createExtent("extent 1", "1 1 1 1");
        extentArray[1] = styleFactory.createExtent("extent 2", "2 2 2 2");
        extentArray[2] = styleFactory.createExtent("extent 3", "3 3 3 3");
        extentArray[3] = styleFactory.createExtent("extent 4", "4 4 4 4");
        extentArray[4] = styleFactory.createExtent("extent 5", "5 5 5 5");
        model.populate(extentArray);

        List<Extent> actualList = model.getExtentList();

        assertEquals(5, actualList.size());
        assertTrue(actualList.get(2).getName().compareTo("extent 3") == 0);

        assertFalse(testUpdate.hasExtentUpdatedBeenCalled());
        model.removeEntries(-1, 2);
        assertFalse(testUpdate.hasExtentUpdatedBeenCalled());
        model.removeEntries(2, 22);
        assertFalse(testUpdate.hasExtentUpdatedBeenCalled());
        model.removeEntries(22, 2);
        assertFalse(testUpdate.hasExtentUpdatedBeenCalled());
        model.removeEntries(2, 2);
        assertTrue(testUpdate.hasExtentUpdatedBeenCalled());
        actualList = model.getExtentList();

        assertEquals(4, actualList.size());
        assertTrue(actualList.get(2).getName().compareTo("extent 4") == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.featuretypeconstraint.ExtentModel#updateExtent(org.geotools.styling.FeatureTypeConstraint)}.
     */
    @Test
    public void testUpdateExtent() {
        ExtentModel model = new ExtentModel(null);

        Extent[] extentArray = null;
        model.populate(extentArray);

        extentArray = new Extent[2];
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        extentArray[0] = styleFactory.createExtent("extent 1", "1 1 1 1");
        extentArray[1] = styleFactory.createExtent("extent 2", "2 2 2 2");
        model.populate(extentArray);

        FeatureTypeConstraint ftc = styleFactory.createFeatureTypeConstraint("feature type name", Filter.INCLUDE, null);

        model.updateExtent(null);
        model.updateExtent(ftc);

        assertNotNull(ftc.getExtents());
        assertEquals(2, ftc.getExtents().length);
    }
}
