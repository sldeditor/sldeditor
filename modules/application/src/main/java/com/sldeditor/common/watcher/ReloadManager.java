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
import java.util.Timer;
import java.util.TimerTask;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.SLDEditorDataUpdateInterface;
import com.sldeditor.datasource.SLDEditorFile;

/**
 * The Class ReloadManager, class implemented as a singleton.
 * Receives the currently load SLD file when it updates and compares it to all
 * modified files from the FileWatcher.  If the currently loaded when is modified
 * and the save flag has n't been set then inform the application that the currently
 * loaded file has been modified.
 * Added some protection to prevent multiple file watcher events for the currently loaded
 * file to trigger more than once.
 *
 * @author Robert Ward (SCISYS)
 */
public class ReloadManager implements FileWatcherUpdateInterface, SLDEditorDataUpdateInterface {

    /** The Constant TIMEOUT. */
    private static final int TIMEOUT = 1000;

    /** The singleton instance. */
    private static ReloadManager instance = null;

    /** The current loaded file. */
    private Path currentLoadedFile = null;

    /** The timing out flag. */
    private boolean timingOut = false;

    /** The timer. */
    private Timer timer = new Timer();

    /** The listener to be notified when underlying file has been changed. */
    private LoadSLDInterface listener = null;

    /** The file saved flag. */
    private boolean fileSaved = false;

    /**
     * Instantiates a new reload manager.
     */
    private ReloadManager()
    {
        SLDEditorFile.getInstance().addSLDEditorFileUpdateListener(this);
    }

    /**
     * Gets the single instance of ReloadManager.
     *
     * @return single instance of ReloadManager
     */
    public static ReloadManager getInstance()
    {
        if(instance == null)
        {
            instance = new ReloadManager();
        }

        return instance;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.watcher.FileWatcherUpdateInterface#fileAdded(java.nio.file.Path)
     */
    @Override
    public void fileAdded(Path f) {
        // Ignore
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.watcher.FileWatcherUpdateInterface#fileModified(java.nio.file.Path)
     */
    @Override
    public void fileModified(Path updated) {
        Path current = getCurrentLoadedFile();
        if((updated != null) && (current != null))
        {
            if(current.equals(updated))
            {
                System.out.println("Loaded file modified");
                if(!isFileSaved() && startTimeout())
                {
                    timer.schedule(new TimerTask() {

                        @Override
                        public void run() {
                            timingOutFinished();
                            System.out.println("Finished timeout");

                            if(listener != null)
                            {
                                listener.reloadSLDFile();
                            }
                        }}, TIMEOUT);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.common.watcher.FileWatcherUpdateInterface#fileDeleted(java.nio.file.Path)
     */
    @Override
    public void fileDeleted(Path updated) {
        // Ignore
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.SLDEditorDataUpdateInterface#sldDataUpdated(com.sldeditor.common.SLDDataInterface, boolean)
     */
    @Override
    public void sldDataUpdated(SLDDataInterface sldData, boolean dataEditedFlag) {
        Path path = null;
        if(sldData != null)
        {
            File sldFile = sldData.getSLDFile();
            if(sldFile != null)
            {
                path = sldFile.toPath();
            }
        }
        setCurrentLoadedFile(path);
    }

    /**
     * Gets the current loaded file.
     *
     * @return the currentLoadedFile
     */
    private synchronized Path getCurrentLoadedFile() {
        return currentLoadedFile;
    }

    /**
     * Sets the current loaded file.
     *
     * @param currentLoadedFile the currentLoadedFile to set
     */
    private synchronized void setCurrentLoadedFile(Path currentLoadedFile) {
        this.currentLoadedFile = currentLoadedFile;

        System.out.println("Currently loaded file : " + ((currentLoadedFile != null) ? currentLoadedFile.toString() : "<null>"));
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
        if(this.timingOut == false)
        {
            System.out.println("Starting timeout");
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
     * Checks if is file saved and resets flag.
     *
     * @return the fileSaved
     */
    private synchronized boolean isFileSaved() {
        boolean tmp = fileSaved;
        fileSaved = false;

        return tmp;
    }

    /**
     * Sets the file saved.
     */
    public synchronized void setFileSaved() {
        System.out.println("File saved");
        this.fileSaved = true;
    }

    /**
     * Reset file saved flag.
     */
    public synchronized void reset() {
        System.out.println("Reset");
        this.fileSaved = false;
    }
}
