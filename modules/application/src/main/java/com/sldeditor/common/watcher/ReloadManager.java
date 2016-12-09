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

package com.sldeditor.common.watcher;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.SLDEditorDataUpdateInterface;
import com.sldeditor.datasource.SLDEditorFile;

/**
 * The Class ReloadManager, class implemented as a singleton. Receives the currently load SLD file when it updates and compares it to all modified
 * files from the FileWatcher. If the currently loaded when is modified and the save flag has n't been set then inform the application that the
 * currently loaded file has been modified. Added some protection to prevent multiple file watcher events for the currently loaded file to trigger
 * more than once.
 *
 * @author Robert Ward (SCISYS)
 */
public class ReloadManager implements FileWatcherUpdateInterface, SLDEditorDataUpdateInterface {

    /** The Constant TIMEOUT. */
    private static final int TIMEOUT = 1000;

    /** The singleton instance. */
    private static ReloadManager instance = null;

    /** The under test flag. */
    private static boolean underTest = false;;

    /** The current loaded file list. */
    private Map<Path, Boolean> currentLoadedFileList = new HashMap<Path, Boolean>();

    /** The timing out flag. */
    private boolean timingOut = false;

    /** The timer. */
    private Timer timer = new Timer();

    /** The listener to be notified when underlying file has been changed. */
    private LoadSLDInterface listener = null;

    /** The file saved flag. */
    private boolean fileSaved = false;

    /** The Constant RELOAD_ENABLED. */
    private static final boolean RELOAD_ENABLED = false;

    /**
     * Instantiates a new reload manager.
     */
    private ReloadManager() {
        SLDEditorFile.getInstance().addSLDEditorFileUpdateListener(this);
    }

    /**
     * Gets the single instance of ReloadManager.
     *
     * @return single instance of ReloadManager
     */
    public static ReloadManager getInstance() {
        if (instance == null) {
            instance = new ReloadManager();
        }

        return instance;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.watcher.FileWatcherUpdateInterface#fileAdded(java.nio.file.Path)
     */
    @Override
    public void fileAdded(Path f) {
        fileModified(f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.watcher.FileWatcherUpdateInterface#fileModified(java.nio.file.Path)
     */
    @Override
    public void fileModified(Path updated) {
        if (!underTest) {
            if ((updated != null) && proceed(updated)) {
                if (startTimeout()) {
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            timingOutFinished();

                            if (listener != null) {
                                listener.reloadSLDFile();
                            }
                        }
                    }, TIMEOUT);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.watcher.FileWatcherUpdateInterface#fileDeleted(java.nio.file.Path)
     */
    @Override
    public void fileDeleted(Path updated) {
        // Ignore
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.SLDEditorDataUpdateInterface#sldDataUpdated(com.sldeditor.common.SLDDataInterface, boolean)
     */
    @Override
    public void sldDataUpdated(SLDDataInterface sldData, boolean dataEditedFlag) {
        Map<Path, Boolean> pathList = new HashMap<Path, Boolean>();

        if (sldData != null) {
            File sldFile = sldData.getSLDFile();
            if (sldFile != null) {
                pathList.put(sldFile.toPath(), fileSaved);
            }

            File sldEditorFile = sldData.getSldEditorFile();
            if (sldEditorFile != null) {
                pathList.put(sldEditorFile.toPath(), fileSaved);
            }
        }
        setCurrentLoadedFileList(pathList);
    }

    /**
     * Gets the current loaded file list.
     *
     * @param updated the updated
     * @return the current loaded file list
     */
    private synchronized boolean proceed(Path updated) {
        if (RELOAD_ENABLED) {
            if (this.currentLoadedFileList.keySet().contains(updated)) {
                this.currentLoadedFileList.put(updated, false);
                System.out.println("Proceed : " + this.currentLoadedFileList);

                if (fileSaved) {
                    fileSaved = !this.currentLoadedFileList.values().contains(true);
                    System.out.println("Proceed file saved : " + fileSaved);
                    return fileSaved;
                } else {
                    System.out.println("Proceed");
                    return !this.currentLoadedFileList.isEmpty();
                }
            }
        }
        return false;
    }

    /**
     * Sets the current loaded file list.
     *
     * @param currentLoadedFileList the new current loaded file list
     */
    private synchronized void setCurrentLoadedFileList(Map<Path, Boolean> currentLoadedFileList) {
        this.currentLoadedFileList = currentLoadedFileList;
    }

    /**
     * Mark timeout as finished.
     */
    private synchronized void timingOutFinished() {
        this.timingOut = false;
    }

    /**
     * Check to see if timeout is running, and set flag if it is not.
     *
     * @return true, if timeout should be started, false it is already running
     */
    private synchronized boolean startTimeout() {
        if (this.timingOut == false) {
            this.timingOut = true;
            return true;
        }
        return false;
    }

    /**
     * Adds the listener.
     *
     * @param listener the listener
     */
    public void addListener(LoadSLDInterface listener) {
        this.listener = listener;
    }

    /**
     * Sets the file saved.
     */
    public synchronized void setFileSaved() {
        fileSaved = true;

        for (Path key : this.currentLoadedFileList.keySet()) {
            this.currentLoadedFileList.put(key, true);
        }
        System.out.println("FILE SAVED : " + this.currentLoadedFileList);
    }

    /**
     * Reset file saved flag.
     */
    public synchronized void reset() {
        for (Path key : this.currentLoadedFileList.keySet()) {
            this.currentLoadedFileList.put(key, false);
        }
        fileSaved = false;
        System.out.println("RESET : " + this.currentLoadedFileList);
    }

    /**
     * Sets the under test flag.
     *
     * @param underTest the new under test
     */
    public static void setUnderTest(boolean underTest) {
        ReloadManager.underTest = underTest;
    }
}
