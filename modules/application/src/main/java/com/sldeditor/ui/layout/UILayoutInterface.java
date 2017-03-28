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

import java.util.List;

import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.extension.ExtensionInterface;
import com.sldeditor.ui.panels.SLDEditorUIPanels;

/**
 * The Interface UILayoutInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface UILayoutInterface {

    /**
     * Creates the ui.
     *
     * @param application the application
     * @param uiMgr the ui mgr
     * @param extensionList the extension list
     */
    void createUI(SLDEditorInterface application, SLDEditorUIPanels uiMgr,
            List<ExtensionInterface> extensionList);

    /**
     * Read layout.
     *
     * @param folder the folder
     */
    public void readLayout(String folder);

    /**
     * Write layout.
     *
     * @param folder the folder
     */
    void writeLayout(String folder);

    /**
     * Gets the display name.
     *
     * @return the display name
     */
    String getDisplayName();
}
