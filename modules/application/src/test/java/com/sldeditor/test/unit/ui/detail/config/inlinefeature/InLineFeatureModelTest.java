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

package com.sldeditor.test.unit.ui.detail.config.inlinefeature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.styling.UserLayer;
import org.junit.Test;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.test.unit.datasource.impl.DummyInlineSLDFile;
import com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel;
import com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUpdateInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The unit test for InLineFeatureModel.
 * <p>{@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel}
 *
 * @author Robert Ward (SCISYS)
 */
public class InLineFeatureModelTest {

    class DummyInlineFeatureUpdated implements InlineFeatureUpdateInterface
    {
        private boolean inlineFeatureUpdatedCalled = false;

        @Override
        public void inlineFeatureUpdated() {
            inlineFeatureUpdatedCalled = true;
        }

        public boolean hasInlineFeatureUpdatedCalled()
        {
            boolean tmp = inlineFeatureUpdatedCalled;
            inlineFeatureUpdatedCalled = false;

            return tmp;
        }
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#isCellEditable(int, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#InLineFeatureModel(com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUpdateInterface)}.
     */
    @Test
    public void testIsCellEditable() {
        InLineFeatureModel model = new InLineFeatureModel(null);
        assertTrue(model.isCellEditable(0, 0));
        assertTrue(model.isCellEditable(0, 1));
        assertTrue(model.isCellEditable(0, -1));
        assertEquals(-1, model.getGeometryFieldIndex());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getColumnCount()}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getColumnName(int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#addNewColumn()}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getGeometryFieldIndex()}.
     */
    @Test
    public void testGetColumnCount() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);

        assertEquals(0, model.getColumnCount());

        // Currently no feature collection
        model.addNewColumn();
        assertEquals(0, model.getColumnCount());

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        assertEquals(2, model.getColumnCount());
        assertFalse(listener.hasInlineFeatureUpdatedCalled());
        model.addNewColumn();
        assertEquals(3, model.getColumnCount());
        assertTrue(listener.hasInlineFeatureUpdatedCalled());

        assertEquals(0, model.getGeometryFieldIndex());

        assertNotNull(model.getColumnName(0));
        assertNull(model.getColumnName(-1));
        assertNull(model.getColumnName(5));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getRowCount()}.
     */
    @Test
    public void testGetRowCount() {
        InLineFeatureModel model = new InLineFeatureModel(null);

        assertEquals(0, model.getRowCount());

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        assertEquals(2, model.getColumnCount());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getValueAt(int, int)}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#populate(org.geotools.styling.UserLayer)}.
     */
    @Test
    public void testGetValueAt() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        assertEquals(1, model.getRowCount());
        assertFalse(listener.hasInlineFeatureUpdatedCalled());

        assertNull(model.getValueAt(-1, 0));
        assertNull(model.getValueAt(5, 0));
        assertNull(model.getValueAt(0, -1));
        assertNull(model.getValueAt(0, 5));
        String actualValue = (String) model.getValueAt(0, 1);
        assertTrue(actualValue.compareTo("Pacific NW") == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#setValueAt(java.lang.Object, int, int)}.
     */
    @Test
    public void testSetValueAtObjectIntInt() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        assertEquals(1, model.getRowCount());
        assertFalse(listener.hasInlineFeatureUpdatedCalled());
        assertFalse(listener.hasInlineFeatureUpdatedCalled());

        model.setValueAt(null, -1, 0);
        model.setValueAt(null, 5, 0);
        model.setValueAt(null, 0, -1);
        model.setValueAt(null, 0, 5);
        assertFalse(listener.hasInlineFeatureUpdatedCalled());

        String expectedValue = "test value";
        model.setValueAt(expectedValue, 0, 1);

        String actualValue = (String) model.getValueAt(0, 1);
        assertTrue(actualValue.compareTo(expectedValue) == 0);
        assertTrue(listener.hasInlineFeatureUpdatedCalled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getFeatureCollection()}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getInlineFeatures()}.
     */
    @Test
    public void testGetFeatureCollection() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);
        assertNull(model.getFeatureCollection());

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);
        assertNotNull(model.getFeatureCollection());
        assertNotNull(model.getInlineFeatures());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#removeColumn(java.lang.String)}.
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#getColumnNames()}.
     */
    @Test
    public void testRemoveColumn() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);
        assertNull(model.getFeatureCollection());

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        // Geometry column not returned, so column name count 1 less than column count
        assertEquals(2, model.getColumnCount());
        assertEquals(1, model.getColumnNames().size());
        model.addNewColumn();
        assertTrue(listener.hasInlineFeatureUpdatedCalled());
        assertEquals(2, model.getColumnNames().size());
        model.removeColumn("unknown column");
        assertFalse(listener.hasInlineFeatureUpdatedCalled());
        assertEquals(2, model.getColumnNames().size());

        // Remove the last column that was just added
        model.removeColumn(model.getColumnNames().get(model.getColumnNames().size() - 1));
        assertTrue(listener.hasInlineFeatureUpdatedCalled());
        assertEquals(1, model.getColumnNames().size());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#updateCRS(com.sldeditor.ui.widgets.ValueComboBoxData)}.
     */
    @Test
    public void testUpdateCRS() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);
        assertNull(model.getFeatureCollection());

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        model.updateCRS(null);

        assertNull(userLayer.getInlineFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());

        ValueComboBoxData expectedCRS = new ValueComboBoxData("EPSG:2000", "Test CRS", VendorOptionManager.getInstance().getDefaultVendorOptionVersion());
        assertFalse(listener.hasInlineFeatureUpdatedCalled());
        model.updateCRS(expectedCRS);
        assertTrue(listener.hasInlineFeatureUpdatedCalled());

        String newCRSCode = userLayer.getInlineFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem().getCoordinateSystem().getName().getCode();

        assertNotNull(newCRSCode);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.inlinefeature.InLineFeatureModel#updateGeometry(int, com.vividsolutions.jts.geom.Geometry)}.
     */
    @Test
    public void testUpdateGeometry() {
        DummyInlineFeatureUpdated listener = new DummyInlineFeatureUpdated();

        InLineFeatureModel model = new InLineFeatureModel(listener);
        assertNull(model.getFeatureCollection());

        DummyInlineSLDFile testData1 = new DummyInlineSLDFile();

        UserLayer userLayer = (UserLayer) testData1.getSLD().layers().get(0);
        model.populate(userLayer);

        assertFalse(listener.hasInlineFeatureUpdatedCalled());
        model.updateGeometry(0, null);
        assertTrue(listener.hasInlineFeatureUpdatedCalled());
    }

}
