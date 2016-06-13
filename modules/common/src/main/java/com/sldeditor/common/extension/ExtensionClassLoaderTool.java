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

import com.sldeditor.common.xml.loadextension.ExtensionTypeEnum;
import com.sldeditor.common.xml.loadextension.LoadExtension;
import com.sldeditor.common.xml.loadextension.XMLExtension;

/**
 * Manages the creation and merging of the ExtensionClass configuration file that configures the application.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExtensionClassLoaderTool {

    public static void main(String[] args) {
        String rootFolder = null;
        String destinationFolder = null;

        System.out.println(args.length);
        for(String arg : args)
        {
            System.out.println(arg);
        }

        if((args.length != 2) && (args.length != 4))
        {
            System.out.println("ExtensionClassLoaderTool <root folder> [create | file to merge]");
            System.exit(0);
        }
        rootFolder = args[0];
        LoadExtension loadExtension = null;
        if(args[1].compareTo("create") == 0)
        {
            destinationFolder = args[0];
            loadExtension = new LoadExtension();

            for(ExtensionTypeEnum extensionType : ExtensionTypeEnum.values())
            {
                XMLExtension xmlExtension = new XMLExtension();
                xmlExtension.setType(extensionType);
                loadExtension.getExtension().add(xmlExtension);
            }
        }
        else
        {
            destinationFolder = args[3];

            loadExtension = ExtensionClassLoader.getExtensionFile(rootFolder);
            if(loadExtension == null)
            {
                System.err.println("Failed to load source : " + rootFolder);
            }
            LoadExtension loadExtensionToMerge = ExtensionClassLoader.getExtensionFile(args[1], args[2]);

            for(ExtensionTypeEnum extensionType : ExtensionTypeEnum.values())
            {
                for(XMLExtension xmlExtension : loadExtension.getExtension())
                {
                    if(extensionType == xmlExtension.getType())
                    {
                        for(XMLExtension xmlExtensionToMerge : loadExtensionToMerge.getExtension())
                        {
                            if(extensionType == xmlExtensionToMerge.getType())
                            {
                                xmlExtension.getExtensionClass().addAll(xmlExtensionToMerge.getExtensionClass());
                                break;
                            }
                        }
                        break;
                    }
                }
            }
        }

        ExtensionClassLoader.write(destinationFolder, loadExtension);
    }

}
