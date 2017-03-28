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

package com.sldeditor.common.vendoroption.minversion;

import com.sldeditor.common.vendoroption.info.VendorOptionInfo;

/**
 * The Class VendorOptionPresent.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionPresent implements Comparable<VendorOptionPresent> {

    /** The sld obj. */
    private Object sldObj = null;

    /** The vendor option info. */
    private VendorOptionInfo vendorOptionInfo = null;

    /**
     * Instantiates a new vendor option present.
     *
     * @param sldObj the sld obj
     * @param vendorOptionInfo the vendor option info
     */
    public VendorOptionPresent(Object sldObj, VendorOptionInfo vendorOptionInfo) {
        this.sldObj = sldObj;
        this.vendorOptionInfo = vendorOptionInfo;
    }

    /**
     * Gets the sld obj.
     *
     * @return the sldObj
     */
    public Object getSldObj() {
        return sldObj;
    }

    /**
     * Gets the vendor option info.
     *
     * @return the vendorOptionInfo.
     */
    public VendorOptionInfo getVendorOptionInfo() {
        return vendorOptionInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(VendorOptionPresent o) {
        if (o == null) {
            return -1;
        }

        if (this.vendorOptionInfo == null) {
            return (o.vendorOptionInfo == null) ? 0 : 1;
        }

        return this.vendorOptionInfo.compareTo(o.vendorOptionInfo);
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
        result = prime * result + ((sldObj == null) ? 0 : sldObj.hashCode());
        result = prime * result + ((vendorOptionInfo == null) ? 0 : vendorOptionInfo.hashCode());
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
        VendorOptionPresent other = (VendorOptionPresent) obj;
        if (sldObj == null) {
            if (other.sldObj != null) {
                return false;
            }
        } else if (!sldObj.equals(other.sldObj)) {
            return false;
        }
        if (vendorOptionInfo == null) {
            if (other.vendorOptionInfo != null) {
                return false;
            }
        } else if (!vendorOptionInfo.equals(other.vendorOptionInfo)) {
            return false;
        }
        return true;
    }
}
