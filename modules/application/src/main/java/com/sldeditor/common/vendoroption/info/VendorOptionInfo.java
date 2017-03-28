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

package com.sldeditor.common.vendoroption.info;

import com.sldeditor.common.vendoroption.VendorOptionStatus;
import com.sldeditor.common.vendoroption.VendorOptionVersion;

/**
 * The Class VendorOptionInfo.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionInfo implements Comparable<VendorOptionInfo> {

    /** The version data. */
    private VendorOptionVersion versionData;

    /** The name. */
    private String name;

    /** The description. */
    private String description;

    /**
     * Instantiates a new vendor option info.
     *
     * @param name the name
     * @param versionData the version data
     * @param description the description
     */
    public VendorOptionInfo(String name, VendorOptionVersion versionData, String description) {
        super();
        this.name = name;
        this.versionData = versionData;
        this.description = description;
    }

    /**
     * Gets the version data.
     *
     * @return the versionData
     */
    public VendorOptionVersion getVersionData() {
        return versionData;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the version string.
     *
     * @return the version string
     */
    public String getVersionString() {
        if (versionData != null) {
            return VendorOptionStatus.getVendorOptionVersionString(versionData);
        }
        return "";
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(VendorOptionInfo o) {
        // Sort on the version data field
        if ((versionData == null) && (o.versionData == null)) {
            return 0;
        }

        if ((versionData != null) && (o.versionData != null)) {
            return versionData.getEarliest().compareTo(o.versionData.getEarliest());
        }

        return (versionData != null) ? -1 : 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((versionData == null) ? 0 : versionData.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        VendorOptionInfo other = (VendorOptionInfo) obj;
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (versionData == null) {
            if (other.versionData != null) {
                return false;
            }
        } else if (!versionData.equals(other.versionData)) {
            return false;
        }
        return true;
    }
}
