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

import com.sldeditor.common.undo.UndoStateInterface;

/**
 * The Class DummyUndo.
 *
 * @author Robert Ward (SCISYS)
 */
class DummyUndo implements UndoStateInterface {

    /** The undo allowed. */
    public boolean undoAllowed = false;

    /** The redo allowed. */
    public boolean redoAllowed = false;

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.undo.UndoStateInterface#updateUndoRedoState(boolean, boolean)
     */
    @Override
    public void updateUndoRedoState(boolean undoAllowed, boolean redoAllowed) {
        this.undoAllowed = undoAllowed;
        this.redoAllowed = redoAllowed;
    }
}
