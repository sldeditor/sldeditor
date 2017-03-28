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
 * The Interface UndoInferface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface UndoInterface {
    /**
     * Gets the field id.
     *
     * @return the field id
     */
    public FieldIdEnum getFieldId();

    /**
     * Gets the old value.
     *
     * @return the old value
     */
    public Object getOldValue();

    /**
     * Gets the new value.
     *
     * @return the new value
     */
    public Object getNewValue();

    /**
     * Undo.
     */
    public void undo();

    /**
     * Redo.
     */
    public void redo();

    /**
     * Gets the string representation.
     *
     * @return the string representation
     */
    public String getStringRepresentation();

    /**
     * Do undo.
     */
    public void doUndo();

    /**
     * Do redo.
     */
    public void doRedo();

    /**
     * Gets the undo string.
     *
     * @return the undo string
     */
    public String getUndoString();

    /**
     * Gets the redo string.
     *
     * @return the redo string
     */
    public String getRedoString();
}
