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

import com.sldeditor.common.filesystem.FileSystemInterface;
import java.awt.datatransfer.DataFlavor;
import javax.swing.Icon;

/**
 * The Interface NodeInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface NodeInterface {

    /**
     * Gets the handler.
     *
     * @return the handler
     */
    FileSystemInterface getHandler();

    /**
     * Gets the data flavour.
     *
     * @return the data flavour
     */
    DataFlavor getDataFlavour();

    /**
     * Gets the destination text.
     *
     * @return the destination text
     */
    String getDestinationText();

    /**
     * Gets the icon.
     *
     * @return the icon
     */
    Icon getIcon();
}
