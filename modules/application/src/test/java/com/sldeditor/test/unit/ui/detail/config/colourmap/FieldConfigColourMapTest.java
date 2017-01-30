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
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigPopulate;
import com.sldeditor.ui.detail.config.colourmap.EncodeColourMap;
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
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#internal_setEnabled(boolean)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#isEnabled()}.
     */
    @Test
    public void testSetEnabled() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));

        // Field will not have been created
        boolean expectedValue = true;
        field.internal_setEnabled(expectedValue);

        assertFalse(field.isEnabled());

        // Create text field
        field.createUI();
        assertEquals(expectedValue, field.isEnabled());

        expectedValue = false;
        field.internal_setEnabled(expectedValue);

        assertEquals(expectedValue, field.isEnabled());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#setVisible(boolean)}.
     */
    @Test
    public void testSetVisible() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));

        boolean expectedValue = true;
        field.setVisible(expectedValue);
        field.createUI();
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

        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));
        ColorMap testValue = null;
        field.populate(null);
        field.setTestValue(FieldIdEnum.UNKNOWN, testValue);
        field.populateField(testValue);

        field.createUI();

        ColorMap expectedValue1 = new ColorMapImpl();
        field.populateField(expectedValue1);
        assertEquals(expectedValue1, field.getColourMap());

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColorMap expectedValue2 = new ColorMapImpl();
        ColorMapEntryImpl entry = new ColorMapEntryImpl();
        entry.setColor(ff.literal("#001122"));
        expectedValue2.addColorMapEntry(entry);
        field.setTestValue(FieldIdEnum.UNKNOWN, expectedValue2);
        assertEquals(expectedValue2.getColorMapEntries().length, field.getColourMap().getColorMapEntries().length);

        field.populateExpression((String)null);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));

        field.revertToDefaultValue();
        assertNull(field.getColourMap());

        field.createUI();
        field.revertToDefaultValue();
        assertNotNull(field.getColourMap());
        assertTrue(field.getColourMap().getColorMapEntries().length == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#createCopy(com.sldeditor.ui.detail.config.FieldConfigBase)}.
     */
    @Test
    public void testCreateCopy() {

        class TestFieldConfigColourMap extends FieldConfigColourMap
        {
            public TestFieldConfigColourMap(FieldConfigCommonData commonData) {
                super(commonData);
            }

            public FieldConfigPopulate callCreateCopy(FieldConfigBase fieldConfigBase)
            {
                return createCopy(fieldConfigBase);
            }
        }

        TestFieldConfigColourMap field = new TestFieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));
        FieldConfigColourMap copy = (FieldConfigColourMap) field.callCreateCopy(null);
        assertNull(copy);

        copy = (FieldConfigColourMap) field.callCreateCopy(field);
        assertEquals(field.getFieldId(), copy.getFieldId());
        assertTrue(field.getLabel().compareTo(copy.getLabel()) == 0);
        assertEquals(field.isValueOnly(), copy.isValueOnly());
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#attributeSelection(java.lang.String)}.
     */
    @Test
    public void testAttributeSelection() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));
        field.attributeSelection(null);

        // Does nothing
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testUndoAction() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));
        field.undoAction(null);
        field.redoAction(null);
        field.createUI();

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
        field.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
        field.redoAction(null);
        field.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "", "new"));
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#getStringValue()}.
     */
    @Test
    public void testGetStringValue() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        ColorMap expectedValue = new ColorMapImpl();
        ColorMapEntryImpl entry1 = new ColorMapEntryImpl();
        entry1.setColor(ff.literal("#001122"));
        entry1.setLabel("testlabel");
        entry1.setOpacity(ff.literal(0.42));
        entry1.setQuantity(ff.literal(42));
        expectedValue.addColorMapEntry(entry1);
        ColorMapEntryImpl entry2 = new ColorMapEntryImpl();
        entry2.setColor(ff.literal("#551122"));
        entry2.setLabel("testlabel2");
        entry2.setOpacity(ff.literal(0.22));
        entry2.setQuantity(ff.literal(12));
        expectedValue.addColorMapEntry(entry2);

        field.populateField(expectedValue);

        assertTrue(field.getStringValue().compareTo(EncodeColourMap.encode(expectedValue)) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.colourmap.FieldConfigColourMap#colourMapUpdated()}.
     */
    @Test
    public void testColourMapUpdated() {
        FieldConfigColourMap field = new FieldConfigColourMap(new FieldConfigCommonData(Geometry.class, FieldIdEnum.NAME, "label", true));

        field.createUI();
        field.colourMapUpdated();
    }

}
