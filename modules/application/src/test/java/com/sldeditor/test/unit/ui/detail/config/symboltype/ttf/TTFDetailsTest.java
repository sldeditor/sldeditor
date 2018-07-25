/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.detail.config.symboltype.ttf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.Controller;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails;
import com.sldeditor.ui.detail.config.symboltype.ttf.TTFUpdateInterface;
import com.sldeditor.ui.ttf.CharMap4;
import org.junit.jupiter.api.Test;

/**
 * The unit test for class TTFDetail.
 *
 * @author Robert Ward (SCISYS)
 */
class TTFDetailsTest {

    class TestTTFUpdateInterface implements TTFUpdateInterface {
        public boolean valueUpdated = false;

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.symboltype.ttf.TTFUpdateInterface#ttfValueUpdated()
         */
        @Override
        public void ttfValueUpdated() {
            valueUpdated = true;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    void testPopulate() {
        TTFDetails testObj = new TTFDetails(null, false);
        testObj.populate(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#populateExpression(java.lang.String)}.
     */
    @Test
    void testPopulateExpression() {
        TTFDetails testObj = new TTFDetails(null, false);
        testObj.populateExpression(null);
        assertEquals("", testObj.getExpression().toString());

        String expected = "star";
        testObj.populateExpression(expected);
        assertEquals(expected, testObj.getExpression().toString());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    void testDataChanged() {
        TTFDetails testObj = new TTFDetails(null, false);
        testObj.dataChanged(FieldIdEnum.TTF_SYMBOL);

        TestTTFUpdateInterface receiver = new TestTTFUpdateInterface();
        assertFalse(receiver.valueUpdated);
        testObj = new TTFDetails(receiver, false);
        testObj.dataChanged(FieldIdEnum.TTF_SYMBOL);
        assertTrue(receiver.valueUpdated);

        receiver.valueUpdated = false;

        Controller.getInstance().setPopulating(true);
        testObj.dataChanged(FieldIdEnum.TTF_SYMBOL);
        assertFalse(receiver.valueUpdated);
        Controller.getInstance().setPopulating(false);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#getFieldDataManager()}.
     */
    @Test
    void testGetFieldDataManager() {
        TTFDetails testObj = new TTFDetails(null, false);
        assertNotNull(testObj.getFieldDataManager());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#isDataPresent()}.
     */
    @Test
    void testIsDataPresent() {
        TTFDetails testObj = new TTFDetails(null, false);
        assertTrue(testObj.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#revertToDefaultValue()}.
     */
    @Test
    void testRevertToDefaultValue() {
        TTFDetails testObj = new TTFDetails(null, false);
        testObj.preLoadSymbol();
        testObj.revertToDefaultValue();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    void testUndoRedoAction() {
        TTFDetails testObj = new TTFDetails(null, false);

        testObj.undoAction(null);
        testObj.undoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(1.0), Double.valueOf(2.0)));
        testObj.undoAction(new UndoEvent(null, FieldIdEnum.NAME, "previous", "next"));
        testObj.redoAction(null);
        testObj.redoAction(
                new UndoEvent(null, FieldIdEnum.NAME, Double.valueOf(1.0), Double.valueOf(2.0)));
        testObj.redoAction(new UndoEvent(null, FieldIdEnum.NAME, "previous", "next"));
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#buttonPressed(java.awt.Component)}.
     */
    @Test
    void testButtonPressed() {
        TestTTFUpdateInterface receiver = new TestTTFUpdateInterface();
        CharMap4.setInTestMode(true);
        TTFDetails testObj = new TTFDetails(receiver, false);

        int undoListSize = UndoManager.getInstance().getUndoListSize();
        testObj.buttonPressed(null);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(receiver.valueUpdated);

        assertEquals(undoListSize + 1, UndoManager.getInstance().getUndoListSize());

        receiver.valueUpdated = false;

        // Suppress undo events
        testObj = new TTFDetails(receiver, true);

        undoListSize = UndoManager.getInstance().getUndoListSize();
        testObj.buttonPressed(null);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(receiver.valueUpdated);

        assertEquals(undoListSize, UndoManager.getInstance().getUndoListSize());

        CharMap4.setInTestMode(false);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.ttf.TTFDetails#getMinimumVersion(java.lang.Object,
     * java.lang.Object, java.util.List)}.
     */
    @Test
    void testGetMinimumVersion() {
        TTFDetails testObj = new TTFDetails(null, false);
        testObj.getMinimumVersion(null, null, null);
    }
}
