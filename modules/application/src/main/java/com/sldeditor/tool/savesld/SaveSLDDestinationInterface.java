/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.tool.savesld;

import java.io.File;

/** @author Robert Ward (SCISYS) */
public interface SaveSLDDestinationInterface {

    /**
     * Displays the dialog asking user whether they want to overwrite files
     *
     * @param output the output
     */
    void overwrite(File output);

    /**
     * Checks if is yes to all.
     *
     * @return the yesToAll
     */
    boolean isYesToAll();

    /**
     * Checks if is write output file.
     *
     * @return the writeOutputFile
     */
    boolean isWriteOutputFile();
}
