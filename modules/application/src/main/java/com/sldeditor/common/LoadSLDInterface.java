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

import com.sldeditor.common.filesystem.SelectedFiles;

/**
 * The Interface LoadSLDInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface LoadSLDInterface {

    /** Empty SLD. */
    void emptySLD();

    /** Method called when a new folder/file has been selected but not processed. */
    void preLoad();

    /**
     * Load sld from a string.
     *
     * @param selectedFiles the selected files
     * @return true, if successful
     */
    boolean loadSLDString(SelectedFiles selectedFiles);

    /** Reload SLD file. */
    void reloadSLDFile();
}
