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

package com.sldeditor.update;

import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Class UpdateData.
 *
 * @author Robert Ward (SCISYS)
 */
public class UpdateData {

    /** The version. */
    private VersionData version;

    /** The description. */
    private String description;

    /**
     * Instantiates a new update data.
     *
     * @param version the version
     * @param description the description
     */
    public UpdateData(VersionData version, String description) {
        super();
        this.version = version;
        this.description = description;
    }

    /**
     * Gets the version.
     *
     * @return the version
     */
    public VersionData getVersion() {
        return version;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
