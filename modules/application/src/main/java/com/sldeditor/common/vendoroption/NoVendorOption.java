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
 * Class that represents no vendor options, i.e. strict SLD only.
 *
 * @author Robert Ward (SCISYS)
 */
public class NoVendorOption implements VendorOptionTypeInterface {

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getName()
     */
    @Override
    public String getName() {
        return "Strict SLD";
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getVersionStringList()
     */
    @Override
    public List<String> getVersionStringList() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#addVersion(com.sldeditor.vendoroption.VersionData)
     */
    @Override
    public void addVersion(VersionData versionData) {
        // Does nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getVersion(java.lang.String)
     */
    @Override
    public VersionData getVersion(String versionString) {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getVersionList()
     */
    @Override
    public List<VersionData> getVersionList() {
        return null;
    }
}
