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
package com.sldeditor.ui.detail.config.base;

import javax.swing.Box;

import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Interface GroupConfigInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface GroupConfigInterface {

    /**
     * Gets the id.
     *
     * @return the id
     */
    GroupIdEnum getId();

    /**
     * Gets the label.
     *
     * @return the label
     */
    String getLabel();

    /**
     * Checks if is show label.
     *
     * @return true, if is show label
     */
    boolean isShowLabel();

    /**
     * Create title if necessary.
     *
     * @param textPanel the text panel
     * @param parent the parent
     */
    void createTitle(Box textPanel, UpdateSymbolInterface parent);

    /**
     * Enable group.
     *
     * @param enable the enable
     */
    void enable(boolean enable);

    /**
     * Checks if is panel enabled.
     *
     * @return true, if is panel enabled
     */
    boolean isPanelEnabled();
    
    /**
     * Removes components from the from ui.
     */
    void removeFromUI();
}
