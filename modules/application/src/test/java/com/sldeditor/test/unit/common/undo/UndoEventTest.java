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
package com.sldeditor.test.unit.common.undo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.xml.ui.FieldIdEnum;

/**
 * Unit test for UndoEvent class.
 * <p>{@link com.sldeditor.common.undo.UndoEvent}
 * 
 * @author Robert Ward (SCISYS)
 */
public class UndoEventTest {

    /**
     * Test method for {@link com.sldeditor.common.undo.UndoEvent#UndoEvent(com.sldeditor.common.undo.UndoActionInterface, com.sldeditor.common.xml.ui.FieldIdEnum, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testUndoEvent1() {
        DummyUndoParent parent = new DummyUndoParent();

        Double oldValue = Double.valueOf(22.0);
        Double newValue = Double.valueOf(42.0);

        FieldIdEnum expectedField = FieldIdEnum.ANGLE;
        UndoEvent undoEvent = new UndoEvent(parent, expectedField, oldValue, newValue);

        assertEquals(expectedField, undoEvent.getFieldId());
        assertEquals(oldValue, undoEvent.getOldValue());
        assertEquals(newValue, undoEvent.getNewValue());

        assertNull(parent.redoAction);
        assertNull(parent.undoAction);

        undoEvent.doUndo();
        assertEquals(oldValue, ((UndoEvent)parent.undoAction).getOldValue());
        assertNull(parent.redoAction);

        undoEvent.doRedo();
        assertEquals(newValue, ((UndoEvent)parent.redoAction).getNewValue());
        assertNull(parent.undoAction);

        undoEvent.doUndo();
        assertEquals(oldValue, ((UndoEvent)parent.undoAction).getOldValue());
        assertNull(parent.redoAction);
    }

    /**
     * Test method for {@link com.sldeditor.common.undo.UndoEvent#UndoEvent(com.sldeditor.common.undo.UndoActionInterface, java.lang.String, java.lang.Object, java.lang.Object)}.
     */
    @Test
    public void testUndoEvent2() {
        DummyUndoParent parent = new DummyUndoParent();

        Double oldValue = Double.valueOf(22.0);
        Double newValue = Double.valueOf(42.0);

        String expectedField = "one more";
        UndoEvent undoEvent = new UndoEvent(parent, expectedField, oldValue, newValue);

        assertEquals(FieldIdEnum.UNKNOWN, undoEvent.getFieldId());
        assertEquals(oldValue, undoEvent.getOldValue());
        assertEquals(newValue, undoEvent.getNewValue());

        assertNull(parent.redoAction);
        assertNull(parent.undoAction);

        undoEvent.doUndo();
        assertEquals(oldValue, ((UndoEvent)parent.undoAction).getOldValue());
        assertNull(parent.redoAction);

        undoEvent.doRedo();
        assertEquals(newValue, ((UndoEvent)parent.redoAction).getNewValue());
        assertNull(parent.undoAction);

        undoEvent.doUndo();
        assertEquals(oldValue, ((UndoEvent)parent.undoAction).getOldValue());
        assertNull(parent.redoAction);
    }
}
