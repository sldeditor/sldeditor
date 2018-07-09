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

package com.sldeditor.common.undo;

import com.sldeditor.common.xml.ui.FieldIdEnum;

/**
 * Class that encapsulates an undo/redo event.
 *
 * @author Robert Ward (SCISYS)
 */
public class UndoEvent implements UndoInterface {

    /** The parent object. */
    private UndoActionInterface parentObj = null;

    /** The field id. */
    private FieldIdEnum fieldId = FieldIdEnum.UNKNOWN;

    /** The custom text. */
    private String customText = null;

    /** The old value. */
    private Object oldValue = null;

    /** The new value. */
    private Object newValue = null;

    /** The representation. */
    private String representation;

    /**
     * Instantiates a new undo event.
     *
     * @param parentObj the parent obj
     * @param fieldId the field id
     * @param oldValue the old value
     * @param newValue the new value
     */
    public UndoEvent(
            UndoActionInterface parentObj, FieldIdEnum fieldId, Object oldValue, Object newValue) {
        this.parentObj = parentObj;
        this.fieldId = fieldId;
        this.oldValue = oldValue;
        this.newValue = newValue;

        representation =
                String.format(
                        "Storing value : %s Old : %s New : %s",
                        fieldId.toString(), oldValue, newValue);
    }

    /**
     * Instantiates a new undo event.
     *
     * @param parentObj the parent obj
     * @param text the text
     * @param oldValue the old value
     * @param newValue the new value
     */
    public UndoEvent(UndoActionInterface parentObj, String text, Object oldValue, Object newValue) {
        this.parentObj = parentObj;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.customText = text;

        representation =
                String.format("Storing value : %s Old : %s New : %s", text, oldValue, newValue);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#undo()
     */
    @Override
    public void undo() {}

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#redo()
     */
    @Override
    public void redo() {}

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#getStringRepresentation()
     */
    @Override
    public String getStringRepresentation() {
        return representation;
    }

    /**
     * Gets the field id.
     *
     * @return the field id
     */
    @Override
    public FieldIdEnum getFieldId() {
        return fieldId;
    }

    /**
     * Gets the old value.
     *
     * @return the old value
     */
    @Override
    public Object getOldValue() {
        return oldValue;
    }

    /**
     * Gets the new value.
     *
     * @return the new value
     */
    @Override
    public Object getNewValue() {
        return newValue;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#doUndo()
     */
    @Override
    public void doUndo() {
        if (parentObj != null) {
            parentObj.undoAction(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#doRedo()
     */
    @Override
    public void doRedo() {
        if (parentObj != null) {
            parentObj.redoAction(this);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#getUndoString()
     */
    @Override
    public String getUndoString() {
        String text = (customText != null) ? customText : fieldId.toString();
        String undoString = String.format("Undoing value : %s Value : %s", text, oldValue);
        return undoString;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.undo.UndoInterface#getRedoString()
     */
    @Override
    public String getRedoString() {
        String text = (customText != null) ? customText : fieldId.toString();
        String redoString = String.format("Redoing value : %s Value : %s", text, newValue);
        return redoString;
    }
}
