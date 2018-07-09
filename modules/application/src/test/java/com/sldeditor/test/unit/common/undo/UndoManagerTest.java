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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import org.junit.Test;

/**
 * Unit test for UndoManager class.
 *
 * <p>{@link com.sldeditor.common.undo.UndoManager}
 *
 * @author Robert Ward (SCISYS)
 */
public class UndoManagerTest {

    /** Test method for adding undo events and calling undo and redo. */
    @Test
    public void testUndoMechanism() {
        UndoManager.destroyInstance();
        DummyUndo listener = new DummyUndo();
        DummyUndoParent parentListener = new DummyUndoParent();

        UndoManager instance = UndoManager.getInstance();
        instance.addListener(listener);

        // CHECKSTYLE:OFF
        UndoEvent event1 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event2 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event3 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.DISPLACEMENT_Y,
                        Integer.valueOf(20),
                        Integer.valueOf(900));
        UndoEvent event4 =
                new UndoEvent(
                        parentListener, FieldIdEnum.DEFAULT_STYLE, Boolean.TRUE, Boolean.FALSE);
        UndoEvent event5 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.HALO_RADIUS,
                        Double.valueOf(44.3),
                        Double.valueOf(67.5));
        // CHECKSTYLE:ON

        instance.addUndoEvent(event1);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
        instance.addUndoEvent(event2);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
        instance.addUndoEvent(event3);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
        instance.addUndoEvent(event4);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
        instance.addUndoEvent(event5);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);

        // Test going beyond end of last item
        assertNull(parentListener.redoAction);
        assertNull(parentListener.undoAction);
        instance.redo();
        assertNull(parentListener.redoAction);
        assertNull(parentListener.undoAction);

        instance.undo();
        assertNull(parentListener.redoAction);
        assertEquals(event5, parentListener.undoAction);
        assertTrue(listener.undoAllowed);
        assertTrue(listener.redoAllowed);

        instance.redo();
        assertNull(parentListener.undoAction);
        assertEquals(event5, parentListener.redoAction);

        instance.undo();
        instance.undo();
        instance.undo();
        instance.undo();
        assertNull(parentListener.redoAction);
        assertEquals(event2, parentListener.undoAction);

        instance.redo();
        assertNull(parentListener.undoAction);
        assertEquals(event2, parentListener.redoAction);

        // Test going beyond start of list
        instance.undo();
        assertEquals(true, listener.redoAllowed);
        assertEquals(true, listener.undoAllowed);

        instance.undo();
        assertEquals(true, listener.redoAllowed);
        assertEquals(false, listener.undoAllowed);

        instance.undo();

        assertEquals(true, listener.redoAllowed);
        assertEquals(false, listener.undoAllowed);
        assertEquals(event1, parentListener.undoAction);
        assertNull(parentListener.redoAction);

        instance.redo();
        assertNull(parentListener.undoAction);
        assertEquals(event1, parentListener.redoAction);

        // Add new event when the undo pointer is not at the end
        UndoEvent event10 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ALIGN,
                        Integer.valueOf(19),
                        Integer.valueOf(20));

        // Expecting the undo events from the pointer onwards to be deleted and replaced with new
        // event
        parentListener.undoAction = null;
        parentListener.redoAction = null;
        instance.addUndoEvent(event10);

        instance.redo();

        // Should be at the end
        assertNull(parentListener.undoAction);
        assertNull(parentListener.redoAction);

        instance.undo();
        assertNull(parentListener.redoAction);
        assertEquals(event10, parentListener.undoAction);
    }

    @Test
    public void testFileSaved() {
        UndoManager.destroyInstance();
        DummyUndo listener = new DummyUndo();
        DummyUndoParent parentListener = new DummyUndoParent();

        UndoManager.getInstance().addListener(listener);

        UndoEvent event1 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event2 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event3 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.DISPLACEMENT_Y,
                        Integer.valueOf(20),
                        Integer.valueOf(900));
        UndoEvent event4 =
                new UndoEvent(
                        parentListener, FieldIdEnum.DEFAULT_STYLE, Boolean.TRUE, Boolean.FALSE);
        UndoEvent event5 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.HALO_RADIUS,
                        Double.valueOf(44.3),
                        Double.valueOf(67.5));

        UndoManager.getInstance().addUndoEvent(event1);
        UndoManager.getInstance().addUndoEvent(event2);
        UndoManager.getInstance().addUndoEvent(event3);
        UndoManager.getInstance().addUndoEvent(event4);
        UndoManager.getInstance().addUndoEvent(event5);

        // This clears out all the undo events
        UndoManager.getInstance().fileSaved();
        assertFalse(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
    }

    @Test
    public void testFileLoaded() {
        UndoManager.destroyInstance();
        DummyUndo listener = new DummyUndo();
        DummyUndoParent parentListener = new DummyUndoParent();

        UndoManager.getInstance().addListener(listener);

        // CHECKSTYLE:OFF
        UndoEvent event1 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event2 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event3 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.DISPLACEMENT_Y,
                        Integer.valueOf(20),
                        Integer.valueOf(900));
        UndoEvent event4 =
                new UndoEvent(
                        parentListener, FieldIdEnum.DEFAULT_STYLE, Boolean.TRUE, Boolean.FALSE);
        UndoEvent event5 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.HALO_RADIUS,
                        Double.valueOf(44.3),
                        Double.valueOf(67.5));
        // CHECKSTYLE:ON

        UndoManager.getInstance().addUndoEvent(event1);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
        UndoManager.getInstance().addUndoEvent(event2);
        UndoManager.getInstance().addUndoEvent(event3);
        UndoManager.getInstance().addUndoEvent(event4);
        UndoManager.getInstance().addUndoEvent(event5);
        UndoManager.getInstance().undo();
        UndoManager.getInstance().undo();

        // This clears out all the undo events
        UndoManager.getInstance().fileLoaded();
        assertFalse(listener.undoAllowed);
        assertFalse(listener.redoAllowed);
    }

    /**
     * Test method for {@link com.sldeditor.common.undo.UndoManager#shouldProcessUndoRedoAction()}.
     */
    @Test
    public void testCheckPopulationFlag() {
        UndoManager.destroyInstance();
        DummyUndo listener = new DummyUndo();
        DummyUndoParent parentListener = new DummyUndoParent();

        UndoManager.getInstance().addListener(listener);

        DummyPopulating populationCheck = new DummyPopulating();
        UndoManager.getInstance().setPopulationCheck(populationCheck);

        // CHECKSTYLE:OFF
        UndoEvent event1 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event2 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.ANCHOR_POINT_H,
                        Integer.valueOf(2),
                        Integer.valueOf(3));
        UndoEvent event3 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.DISPLACEMENT_Y,
                        Integer.valueOf(20),
                        Integer.valueOf(900));
        UndoEvent event4 =
                new UndoEvent(
                        parentListener, FieldIdEnum.DEFAULT_STYLE, Boolean.TRUE, Boolean.FALSE);
        UndoEvent event5 =
                new UndoEvent(
                        parentListener,
                        FieldIdEnum.HALO_RADIUS,
                        Double.valueOf(44.3),
                        Double.valueOf(67.5));
        // CHECKSTYLE:ON

        UndoManager.getInstance().addUndoEvent(event1);
        assertTrue(listener.undoAllowed);
        assertFalse(listener.redoAllowed);

        populationCheck.setPopulating(true);

        // These should be added
        UndoManager.getInstance().addUndoEvent(event2);
        UndoManager.getInstance().addUndoEvent(event3);

        UndoManager.getInstance().undo();
        assertEquals(event1, parentListener.undoAction);
        assertNull(parentListener.redoAction);

        UndoManager.getInstance().addUndoEvent(event4);
        populationCheck.setPopulating(false);

        UndoManager.getInstance().addUndoEvent(event5);
        UndoManager.getInstance().undo();
        assertEquals(event5, parentListener.undoAction);
        assertNull(parentListener.redoAction);
        UndoManager.getInstance().setPopulationCheck(null);
    }
}
