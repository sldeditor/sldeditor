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
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.console.ConsoleManager;

/**
 * Class that implements a file watcher to inform the file system tree whether files have been added or deleted.
 * 
 * @author Robert Ward (SCISYS)
 */
public class FileSystemWatcher implements Runnable {

    /** The watcher map. */
    private Map<WatchKey, FileWatcherUpdateInterface> watcherMap = new HashMap<WatchKey, FileWatcherUpdateInterface>();

    /** The watch service. */
    private WatchService watchService = null;

    /** The instance. */
    private static FileSystemWatcher instance = null;

    /**
     * Default constructor
     */
    private FileSystemWatcher()
    {
        // First create the watch service instance. This service watches a
        // directory for changes.
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Instantiates a new file system watcher.
     *
     * @param parent the parent
     * @param path the path
     */
    public void addWatch(FileWatcherUpdateInterface parent, Path path) {
        if(path != null)
        {
            // The directory that has to be watched needs to be registered. Any
            // object that implements the Watchable interface can be registered.

            // Register three events. i.e. whenever a file is created, deleted or
            // modified the watcher gets informed
            try {
                WatchKey key = path.register(watchService,
                        StandardWatchEventKinds.ENTRY_CREATE,
                        StandardWatchEventKinds.ENTRY_DELETE,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                watcherMap.put(key, parent);

            } catch (IOException e) {
                // Ignore
            }
        }
    }

    /**
     * Gets the single instance of FileSystemWatcher.
     *
     * @return single instance of FileSystemWatcher
     */
    public static synchronized FileSystemWatcher getInstance()
    {
        if(instance == null)
        {
            instance = new FileSystemWatcher();
            (new Thread(instance)).start();
        }

        return instance;
    }

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        try {
            internal_watchDirectoryPath();
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /**
     * Internal watch directory path method.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void internal_watchDirectoryPath() throws IOException {
        WatchKey key = null;

        boolean stopPolling = false;
        // Poll for events in an infinite loop
        for (;;) {
            try {
                // The take method waits till watch service receives a
                // notification
                key = watchService.take();
            } catch (InterruptedException e) {
                // Ignore
            }

            // once a key is obtained, we poll for events on that key
            List<WatchEvent<?>> keys = key.pollEvents();
            for (WatchEvent<?> watchEvent : keys) {

                Kind<?> watchEventKind = watchEvent.kind();
                // Sometimes events are created faster than they are registered
                // or the implementation may specify a maximum number of events
                // and further events are discarded. In these cases an event of
                // kind overflow is returned. We ignore this case for now
                if (watchEventKind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                Path dir = (Path)key.watchable();
                Path fullPath = dir.resolve((Path) watchEvent.context());

                FileWatcherUpdateInterface parentObj = watcherMap.get(key);
                if (watchEventKind == StandardWatchEventKinds.ENTRY_CREATE) {
                    // A new file has been created
                    if(parentObj != null)
                    {
                        parentObj.fileAdded(fullPath);
                    }
                } else if (watchEventKind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    ReloadManager.getInstance().fileModified(fullPath);
                    // The file has been modified.
                    if(parentObj != null)
                    {
                        parentObj.fileModified(fullPath);
                    }
                } else if (watchEventKind == StandardWatchEventKinds.ENTRY_DELETE) {

                    if(parentObj != null)
                    {
                        parentObj.fileDeleted(fullPath);
                    }
                }
                // Reset the key so the further key events may be polled
                key.reset();
            }

            if (stopPolling) {
                break;
            }
        }

        // Close the watcher service
        watchService.close();
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        // Folder we are going to watch
        File dir = new File(System.getProperty("user.dir"));
        System.out.println(dir.getAbsolutePath());
        FileSystemWatcher.getInstance().addWatch(new FileWatcherUpdateInterface() {

            @Override
            public void fileAdded(Path f) {
                System.out.println("File Created:" + f.toString());
            }

            @Override
            public void fileModified(Path f) {
                System.out.println("File Modified:" + f.toString());
            }

            @Override
            public void fileDeleted(Path f) {
                System.out.println("File Deleted:" + f.toString());
            }}, dir.toPath());
    }
}
