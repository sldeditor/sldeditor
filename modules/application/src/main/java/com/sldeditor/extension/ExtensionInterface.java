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

import java.net.URL;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JPanel;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.ToolSelectionInterface;
import com.sldeditor.common.preferences.PrefData;

/**
 * The Interface ExtensionInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface ExtensionInterface {

    /**
     * Initialise.
     *
     * @param parent the parent
     * @param toolMgr the tool mgr
     */
    public void initialise(LoadSLDInterface parent, ToolSelectionInterface toolMgr);

    /**
     * Creates the menus.
     *
     * @param mnTools the mn tools
     */
    public void createMenus(JMenu mnTools);

    /**
     * Gets the tooltip.
     *
     * @return the tooltip
     */
    public String getTooltip();

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName();

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public JPanel getPanel();

    /**
     * Gets the extension arg prefix.
     *
     * @return the extension arg prefix
     */
    public String getExtensionArgPrefix();

    /**
     * Load from url.
     *
     * @param url the url
     * @return the list of SLDDataInterface objects
     */
    public List<SLDDataInterface> open(URL url);

    /**
     * Save url.
     *
     * @param sldData the sld data
     * @return true, if successful
     */
    public boolean save(SLDDataInterface sldData);

    /**
     * Sets the arguments.
     *
     * @param extensionArgList the new arguments
     */
    public void setArguments(List<String> extensionArgList);

    /**
     * Update for preferences.
     *
     * @param prefData the pref data
     * @param extensionArgList the extension arg list
     */
    public void updateForPreferences(PrefData prefData, List<String> extensionArgList);
}
