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

package com.sldeditor.ui.iface;

import java.util.Set;

/**
 * The Interface SymbolizerSelectedInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface SymbolizerSelectedInterface {

    /**
     * Gets the panel for the given panel key.
     *
     * @param parentClass the parent class
     * @param key the key
     * @return the panel
     */
    public PopulateDetailsInterface getPanel(Class<?> parentClass, String key);

    /**
     * Gets the panel ids.
     *
     * @return the panel ids
     */
    public Set<String> getPanelIds();

    /**
     * Show panel for selected tree item.
     *
     * @param parentClass the parent class
     * @param classSelected the class selected
     */
    public void show(Class<?> parentClass, Class<?> classSelected);

    /**
     * Refresh contents of panel if displayed.
     *
     * @param parentClass the parent class
     * @param classSelected the class selected
     */
    public void refresh(Class<?> parentClass, Class<?> classSelected);
}
