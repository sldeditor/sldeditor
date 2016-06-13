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
package com.sldeditor.ui.layout;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A factory for creating UILayout objects.
 * 
 * @author Robert Ward (SCISYS)
 */
public class UILayoutFactory
{

    /** The ui layout map. */
    private static Map<String, UILayoutInterface> uiLayoutMap = new LinkedHashMap<String, UILayoutInterface>();

    /** The default ui layout. */
    private static String defaultUILayout = null;

    /** The selected layout. */
    private static UILayoutInterface selectedLayout = null;

    /**
     * Gets the UI layout.
     *
     * @param className the class name
     * @return the UI layout
     */
    public static UILayoutInterface getUILayout(String className)
    {
        if(uiLayoutMap.isEmpty())
        {
            populate();
        }

        UILayoutInterface uiLayout = uiLayoutMap.get(className);

        if(uiLayout == null)
        {
            uiLayout = uiLayoutMap.get(defaultUILayout);
        }

        selectedLayout = uiLayout;

        return uiLayout;
    }

    /**
     * Populate.
     */
    private static void populate()
    {
        SLDEditorDefaultLayout defaultUI = new SLDEditorDefaultLayout();

        populateInternal(defaultUI);
        populateInternal(new SLDEditorDockableLayout());

        defaultUILayout = defaultUI.getClass().getName();
    }

    /**
     * Populate internal.
     *
     * @param layout the layout
     */
    private static void populateInternal(UILayoutInterface layout)
    {
        String className = layout.getClass().getName();
        uiLayoutMap.put(className, layout);
    }

    /**
     * Read layout.
     *
     * @param folder the folder
     */
    public static void readLayout(String folder)
    {
        if(selectedLayout != null)
        {
            selectedLayout.readLayout(folder);
        }
    }

    /**
     * Write layout.
     *
     * @param folder the folder
     */
    public static void writeLayout(String folder)
    {
        if(selectedLayout != null)
        {
            selectedLayout.writeLayout(folder);
        }
    }

    /**
     * Gets the all layouts.
     *
     * @return the all layouts
     */
    public static Map<String, String> getAllLayouts()
    {
        Map<String, String> map = new LinkedHashMap<String, String>();

        for(String className : uiLayoutMap.keySet())
        {
            String displayName = uiLayoutMap.get(className).getDisplayName();
            map.put(displayName, className);
        }
        return map;
    }
}
