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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that represents all the GeoServer vendor options.
 * 
 * @author Robert Ward (SCISYS)
 */
public class GeoServerVendorOption implements VendorOptionTypeInterface {

    /** The version list. */
    private List<VersionData> versionList = new ArrayList<VersionData>();

    /** The list reversed. */
    private boolean listReversed = false;

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getName()
     */
    @Override
    public String getName() {
        return "GeoServer";
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getVersionStringList()
     */
    @Override
    public List<String> getVersionStringList() {
        List<String> versionStringList = new ArrayList<String>();

        for (VersionData versionData : getVersionList()) {
            versionStringList.add(versionData.getVersionString());
        }

        return versionStringList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.sldeditor.vendoroption.VendorOptionTypeInterface#addVersion(com.sldeditor.vendoroption.
     * VersionData)
     */
    @Override
    public void addVersion(VersionData versionData) {
        if (versionData != null) {
            versionList.add(versionData);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getVersion(java.lang.String)
     */
    @Override
    public VersionData getVersion(String versionString) {
        VersionData versionData = VersionData.decode(this.getClass(), versionString);

        return versionData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.vendoroption.VendorOptionTypeInterface#getVersionList()
     */
    @Override
    public List<VersionData> getVersionList() {
        if (!listReversed) {
            Collections.reverse(versionList);
            // Move the Not Set version is first in the list
            VersionData notSet = versionList.remove(versionList.size() - 1);
            versionList.add(0, notSet);
            listReversed = true;
        }

        return versionList;
    }
}
