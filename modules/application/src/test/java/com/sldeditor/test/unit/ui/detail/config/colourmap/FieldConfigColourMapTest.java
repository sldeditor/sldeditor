/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit.ui.detail.config.colourmap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntryImpl;
import org.geotools.styling.ColorMapImpl;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The unit test for FieldConfigColourMap.
 * <p>{@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigColourMapTest {

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        // Field will not have been created
        boolean expectedValue = true;
        field.setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI(null);
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI(null);
        field.setVisible(expectedValue);

        expectedValue = false;
        field.setVisible(expectedValue);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#generateExpression()}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#populateExpression(java.lang.Object, org.opengis.filter.expression.Expression)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#populateField(org.geotools.styling.ColorMap)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#setTestValue(com.sldeditor.ui.detail.config.FieldId, org.geotools.styling.ColorMap)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#getColourMap()}.
     */
    @Test
    public void testGenerateExpression() {

        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        ColorMap testValue = null;
        field.populate(null);
        field.setTestValue(null, testValue);
        field.populateField(testValue);

        field.createUI(null);

        ColorMap expectedValue1 = new ColorMapImpl();
        field.populateField(expectedValue1);
        assertEquals(expectedValue1, field.getColourMap());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColorMap expectedValue2 = new ColorMapImpl();
        ColorMapEntryImpl entry = new ColorMapEntryImpl();
        entry.setColor(ff.literal("#001122"));
        expectedValue2.addColorMapEntry(entry);
        field.setTestValue(null, expectedValue2);
        assertEquals(expectedValue2.getColorMapEntries().length, field.getColourMap().getColorMapEntries().length);

        field.populateExpression((String)null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        field.revertToDefaultValue();
        assertNull(field.getColourMap());

        field.createUI(null);
        field.revertToDefaultValue();
        assertNotNull(field.getColourMap());
        assertTrue(field.getColourMap().getColorMapEntries().length == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#getClassType()}.
     */
    @Test
    public void testGetClassType() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        assertEquals(ColorMap.class, field.getClassType());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {

        class TestFieldConfigColourMap extends FieldConfigColourMap
        {
            public TestFieldConfigColourMap(Class<?> panelId, FieldId id, String label) {
                super(panelId, id, label);
            }

            public FieldConfigBase callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigColourMap field = new TestFieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        FieldConfigColourMap copy = (FieldConfigColourMap) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigColourMap) field.callCreateCopy(field);
        assertEquals(field.getFieldId().getFieldId(), copy.getFieldId().getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");
        field.undoAction(null);
        field.redoAction(null);
        field.createUI(null);

        ColorMap expectedValue1 = new ColorMapImpl();
        field.populateField(expectedValue1);
        assertEquals(expectedValue1, field.getColourMap());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColorMap expectedValue2 = new ColorMapImpl();
        ColorMapEntryImpl entry = new ColorMapEntryImpl();
        entry.setColor(ff.literal("#001122"));
        expectedValue2.addColorMapEntry(entry);
        field.populateField(expectedValue2);

        UndoManager.getInstance().undo();
        assertEquals(expectedValue1.getColorMapEntries().length, field.getColourMap().getColorMapEntries().length);
        UndoManager.getInstance().redo();
        assertEquals(expectedValue2.getColorMapEntries().length, field.getColourMap().getColorMapEntries().length);

        // Increase the code coverage
        field.undoAction(null);
        field.undoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, new FieldId(FieldIdEnum.NAME), "", "new"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#getStringValue()}.
     */
    @Test
    public void testGetStringValue() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        assertNull(field.getStringValue());
    }

    /**
     */
    @Test
    public void testColourMapUpdated() {
        FieldConfigColourMap field = new FieldConfigColourMap(Geometry.class, new FieldId(FieldIdEnum.NAME), "label");

        field.createUI(null);
        field.colourMapUpdated();
    }

}
