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

package com.sldeditor.common;

import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The Interface SLDEditorInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface SLDEditorInterface {

    /**
     * Gets the app panel.
     *
     * @return the app panel
     */
    JPanel getAppPanel();

    /**
     * Update window title.
     *
     * @param dataEditedFlag the data edited flag
     */
    void updateWindowTitle(boolean dataEditedFlag);

    /** Choose new sld. */
    void chooseNewSLD();

    /** Exit application. */
    void exitApplication();

    /**
     * Save sld.
     *
     * @param url the url
     */
    void saveFile(URL url);

    /**
     * Save sld data.
     *
     * @param sldData the sld data
     */
    void saveSLDData(SLDDataInterface sldData);

    /**
     * Gets the load sld interface.
     *
     * @return the load sld interface
     */
    LoadSLDInterface getLoadSLDInterface();

    /**
     * Gets the application frame.
     *
     * @return the frame
     */
    JFrame getApplicationFrame();

    /**
     * Open file.
     *
     * @param selectedURL the selected url
     */
    void openFile(URL selectedURL);

    /**
     * Gets the application name.
     *
     * @return the app name
     */
    String getAppName();

    /**
     * Refresh panel.
     *
     * @param parent the parent
     * @param panelClass the panel class
     */
    void refreshPanel(Class<?> parent, Class<?> panelClass);
}
