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
package com.sldeditor.common.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.loadextension.ExtensionTypeEnum;
import com.sldeditor.common.xml.loadextension.LoadExtension;
import com.sldeditor.common.xml.loadextension.XMLExtension;
import com.sldeditor.common.xml.loadextension.XMLExtensionClass;

/**
 * Manages the reading of the ExtensionClass configuration file that configures the application.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExtensionClassLoader {

    /** The Constant SOURCE_DATA_FILE. */
    private static final String SOURCE_DATA_FILE = "ExtensionClasses.xml";

    /** The Constant RESOURCE_FOLDER. */
    private static final String RESOURCE_FOLDER = "/extension/";

    /** The Constant SCHEMA_RESOURCE. */
    private static final String SCHEMA_RESOURCE = "/xsd/loadextension.xsd";

    /** The class map. */
    private static Map<ExtensionTypeEnum, List<String> > classMap = null;

    /**
     * Gets the extension to load.
     *
     * @param key the key
     * @param classList the class list
     */
    public static void getExtensionToLoad(ExtensionTypeEnum key, List<String> classList)
    {
        if(classMap == null)
        {
            populateClassMap();
        }

        List<String> classNameList = classMap.get(key);

        if((classNameList != null) && !classNameList.isEmpty())
        {
            classList.addAll(classNameList);
        }
    }

    /**
     * 
     */
    private static void populateClassMap() {
        classMap = new HashMap<ExtensionTypeEnum, List<String> >();
        LoadExtension localExtension = getExtensionFile("");

        if(localExtension != null)
        {
            for(XMLExtension extension : localExtension.getExtension())
            {
                List<String> classNameList = new ArrayList<String>();

                for(XMLExtensionClass extensionClass : extension.getExtensionClass())
                {
                    classNameList.add(extensionClass.getClassName());
                }
                classMap.put(extension.getType(), classNameList);
            }
        }
    }

    /**
     * Gets the extension file.
     *
     * @param rootFolder the root folder
     * @return the extension file
     */
    public static LoadExtension getExtensionFile(String rootFolder) {
        return getExtensionFile(rootFolder, SOURCE_DATA_FILE);
    }

    /**
     * Gets the extension file.
     *
     * @param rootFolder the root folder
     * @param filename the filename
     * @return the extension file
     */
    public static LoadExtension getExtensionFile(String rootFolder, String filename) {
        LoadExtension localExtension = (LoadExtension) ParseXML.parseFile(rootFolder + RESOURCE_FOLDER, filename, SCHEMA_RESOURCE, LoadExtension.class);
        return localExtension;
    }

    /**
     * Write XML file.
     *
     * @param rootFolder the root folder
     * @param loadExtension the load extension
     */
    public static void write(String rootFolder, LoadExtension loadExtension) {
        ParseXML.writeFile(rootFolder, RESOURCE_FOLDER, SOURCE_DATA_FILE, SCHEMA_RESOURCE, loadExtension);
    }

}
