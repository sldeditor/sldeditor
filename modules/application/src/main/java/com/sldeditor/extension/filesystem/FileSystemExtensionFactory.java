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

import java.util.ArrayList;
import java.util.List;

import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.extension.filesystem.file.FileSystemInput;
import com.sldeditor.extension.filesystem.geoserver.GeoServerInput;

/**
 * A factory for creating FileSystemExtension objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class FileSystemExtensionFactory {

    /** The extension list. */
    private static List<FileSystemInterface> extensionList = new ArrayList<FileSystemInterface>();

    /**
     * Populate default file extensions.
     *
     * @param toolMgr the tool manager
     */
    private static void populateExtensions(ToolSelectionInterface toolMgr) {
        extensionList.add(new GeoServerInput(toolMgr));
        extensionList.add(new FileSystemInput(toolMgr));
    }

    /**
     * Allows list of file extensions to be overridden.
     *
     * @param overrideExtensionList the override extension list
     */
    public static void override(List<FileSystemInterface> overrideExtensionList)
    {
        if(overrideExtensionList != null)
        {
            extensionList = overrideExtensionList;
        }
        else
        {
            extensionList.clear();
        }
    }

    /**
     * Gets the file extension list.
     *
     * @param toolMgr the tool manager
     * @return the file extension list
     */
    public static List<FileSystemInterface> getFileExtensionList(ToolSelectionInterface toolMgr)
    {
        // Populate with defaults if no override has been set
        if(extensionList.isEmpty())
        {
            populateExtensions(toolMgr);
        }

        return extensionList;
    }
}
