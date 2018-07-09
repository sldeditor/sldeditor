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

import com.sldeditor.common.PopulatingInterface;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Class that manages the undo/redo framework.
 *
 * <p>Implemented as a singleton.
 *
 * @author Robert Ward (SCISYS)
 */
public class UndoManager {

    /** The listener list. */
    private List<UndoStateInterface> listenerList = new ArrayList<UndoStateInterface>();

    /** The undo list. */
    private List<UndoInterface> undoList = new ArrayList<UndoInterface>();

    /** The instance. */
    private static UndoManager instance = null;

    /** The current index. */
    private int currentIndex = 0;

    /** The logger. */
    private static Logger logger = Logger.getLogger(UndoManager.class);

    /** The is undo redo action. */
    private boolean isUndoRedoAction = false;

    /** The population check object. */
    private PopulatingInterface populationCheck = null;

    /** Instantiates a new undo manager. */
    private UndoManager() {
        reset();
    }

    /**
     * Adds the listener.
     *
     * @param listener the listener
     */
    public void addListener(UndoStateInterface listener) {
        listenerList.add(listener);
    }

    /** Reset undo list. */
    private void reset() {
        currentIndex = 0;
        undoList.clear();

        updateMenuItems();
    }

    /**
     * Gets the single instance of UndoManager.
     *
     * @return single instance of UndoManager
     */
    public static UndoManager getInstance() {
        if (instance == null) {
            instance = new UndoManager();
        }

        return instance;
    }

    /**
     * Adds the undo event.
     *
     * @param event the event
     */
    public void addUndoEvent(UndoInterface event) {
        if (shouldProcessUndoRedoAction()) {
            boolean atEndOfList = (currentIndex >= undoList.size());

            if (!atEndOfList) {
                while (undoList.size() > currentIndex) {
                    int lastIndex = undoList.size() - 1;
                    undoList.remove(lastIndex);
                }
            }
            undoList.add(event);

            currentIndex = undoList.size();

            updateMenuItems();

            logger.debug(event.getStringRepresentation());
        }
    }

    /** File loaded. */
    public void fileLoaded() {
        reset();
    }

    /** File saved. */
    public void fileSaved() {
        reset();
    }

    /** Undo. */
    public void undo() {
        if (currentIndex > 0) {
            currentIndex--;

            UndoInterface undoObject = undoList.get(currentIndex);

            logger.debug(undoObject.getUndoString());

            setUndoRedoAction(true);
            undoObject.doUndo();
            setUndoRedoAction(false);
        }

        updateMenuItems();
    }

    /** Redo. */
    public void redo() {
        if (currentIndex < undoList.size()) {
            UndoInterface undoObject = undoList.get(currentIndex);
            currentIndex++;

            logger.debug(undoObject.getRedoString());

            setUndoRedoAction(true);
            undoObject.doRedo();
            setUndoRedoAction(false);
        }

        updateMenuItems();
    }

    /** Update menu items. */
    private void updateMenuItems() {
        boolean undoAllowed = (currentIndex > 0) && !undoList.isEmpty();
        boolean redoAllowed = (currentIndex < undoList.size());

        logger.debug(
                String.format(
                        "Current index : %d List : %d Undo %s Redo %s",
                        currentIndex, undoList.size(), undoAllowed, redoAllowed));
        for (UndoStateInterface listener : listenerList) {
            listener.updateUndoRedoState(undoAllowed, redoAllowed);
        }
    }

    /**
     * Sets the undo redo action.
     *
     * @param isUndoRedoAction the new undo redo action
     */
    private void setUndoRedoAction(boolean isUndoRedoAction) {
        this.isUndoRedoAction = isUndoRedoAction;
    }

    /**
     * Should process undo/redo action.
     *
     * @return true, if successful
     */
    private boolean shouldProcessUndoRedoAction() {
        boolean populating = false;

        if (populationCheck != null) {
            populating = populationCheck.isPopulating();
        }

        return (!isUndoRedoAction && !populating);
    }

    /**
     * Sets the population check.
     *
     * @param populationCheck the populationCheck to set
     */
    public void setPopulationCheck(PopulatingInterface populationCheck) {
        this.populationCheck = populationCheck;
    }

    /** Destroy instance. */
    public static void destroyInstance() {
        instance = null;
    }
}
