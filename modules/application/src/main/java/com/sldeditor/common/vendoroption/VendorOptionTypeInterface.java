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


package com.sldeditor.common.vendoroption;

import java.util.List;

/**
 * The Interface VendorOptionTypeInterface.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface VendorOptionTypeInterface {

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName();

    /**
     * Adds the version.
     *
     * @param versionData the version data
     */
    public void addVersion(VersionData versionData);

    /**
     * Gets the version.
     *
     * @param versionString the version string
     * @return the version
     */
    public VersionData getVersion(String versionString);

    /**
     * Gets the version string list.
     *
     * @return the version string list
     */
    public List<String> getVersionStringList();

    /**
     * Gets the version data list.
     *
     * @return the version data list
     */
    public List<VersionData> getVersionList();
}
