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

package com.sldeditor.extension.filesystem;

import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.extension.filesystem.database.DatabaseInput;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.extension.filesystem.geoserver.GeoServerInput;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory for creating FileSystemExtension objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemExtensionFactory {

    /** The extension list. */
    private static List<FileSystemInterface> extensionList = new ArrayList<>();

    /** The geo server input. */
    private static GeoServerInput geoServerInput;

    /** The database input. */
    private static DatabaseInput databaseInput;

    /** The file system input. */
    private static FileSystemInput fileSystemInput;

    /** Private default constructor */
    private FileSystemExtensionFactory() {
        // Private default constructor
    }

    /**
     * Populate default file extensions.
     *
     * @param toolMgr the tool manager
     */
    private static void populateExtensions(ToolSelectionInterface toolMgr) {
        geoServerInput = new GeoServerInput(toolMgr);
        geoServerInput.readPropertyFile();
        extensionList.add(geoServerInput);
        databaseInput = new DatabaseInput(toolMgr);
        databaseInput.readPropertyFile();
        extensionList.add(databaseInput);
        fileSystemInput = new FileSystemInput(toolMgr);
        extensionList.add(fileSystemInput);
    }

    /**
     * Allows list of file extensions to be overridden.
     *
     * @param overrideExtensionList the override extension list
     */
    public static void override(List<FileSystemInterface> overrideExtensionList) {
        if (overrideExtensionList != null) {
            extensionList = overrideExtensionList;
        } else {
            extensionList.clear();
        }
    }

    /**
     * Gets the file extension list.
     *
     * @param toolMgr the tool manager
     * @return the file extension list
     */
    public static List<FileSystemInterface> getFileExtensionList(ToolSelectionInterface toolMgr) {
        // Populate with defaults if no override has been set
        if (extensionList.isEmpty()) {
            populateExtensions(toolMgr);
        }

        return extensionList;
    }

    /**
     * Gets the geo server input.
     *
     * @return the geoServerInput
     */
    public static GeoServerInput getGeoServerInput() {
        return geoServerInput;
    }

    /**
     * Gets the file system input.
     *
     * @return the fileSystemInput
     */
    public static FileSystemInput getFileSystemInput() {
        return fileSystemInput;
    }
}
