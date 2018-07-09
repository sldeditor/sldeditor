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

package com.sldeditor.extension;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.extension.filesystem.FileSystemExtension;
import java.util.ArrayList;
import java.util.List;

/**
 * A factory for creating Extension objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExtensionFactory {

    /** The Constant EXTENSION_PREFIX. */
    public static final String EXTENSION_PREFIX = "-extension";

    /** The extension list. */
    private static List<ExtensionInterface> extensionList = new ArrayList<ExtensionInterface>();

    /**
     * Gets the available extensions.
     *
     * @return the available extensions
     */
    public static List<ExtensionInterface> getAvailableExtensions() {
        if (extensionList.isEmpty()) {
            populate();
        }

        return extensionList;
    }

    /** Populate extension list. */
    private static void populate() {
        String className = FileSystemExtension.class.getName();

        try {
            ExtensionInterface extension =
                    (ExtensionInterface) Class.forName(className).newInstance();
            extensionList.add(extension);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            ConsoleManager.getInstance().exception(ExtensionFactory.class, e);
        }
    }

    /**
     * Gets the arguments.
     *
     * @param extension the extension
     * @param extensionArgList the extension arg list
     * @return the arguments
     */
    public static List<String> getArguments(
            ExtensionInterface extension, List<String> extensionArgList) {
        List<String> specificExtensionArgList = new ArrayList<String>();

        if (extensionArgList != null) {
            for (String extensionArg : extensionArgList) {
                String[] components = extensionArg.split("\\.");

                if (components.length >= 2) {
                    if ((components[0].compareToIgnoreCase(EXTENSION_PREFIX) == 0)
                            && (components[1].compareToIgnoreCase(extension.getExtensionArgPrefix())
                                    == 0)) {
                        StringBuilder sb = new StringBuilder();

                        for (int index = 2; index < components.length; index++) {
                            sb.append(components[index]);

                            if (index + 1 != components.length) {
                                sb.append(".");
                            }
                        }
                        specificExtensionArgList.add(sb.toString());
                    }
                }
            }
        }

        return specificExtensionArgList;
    }

    /**
     * Gets the argument list.
     *
     * @param args the args
     * @return the argument list
     */
    public static List<String> getArgumentList(String[] args) {
        List<String> extensionArgList = new ArrayList<String>();

        for (String extensionArg : args) {
            String[] components = extensionArg.split("\\.");

            if (components.length >= 2) {
                if (components[0].compareToIgnoreCase(EXTENSION_PREFIX) == 0) {
                    extensionArgList.add(extensionArg);
                }
            }
        }
        return extensionArgList;
    }

    /**
     * Update for preferences.
     *
     * @param prefData the pref data
     * @param extensionArgList the extension arg list
     */
    public static void updateForPreferences(PrefData prefData, List<String> extensionArgList) {
        for (ExtensionInterface extension : extensionList) {
            if (extension != null) {
                extension.updateForPreferences(prefData, extensionArgList);
            }
        }
    }
}
