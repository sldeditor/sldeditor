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

import com.sldeditor.common.console.ConsoleManager;

/**
 * Class that encapsulates a vendor option version.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionVersion {

    /** The Constant DELIMETER. */
    private static final String DELIMETER = ";";

    /** The Constant NULL_STRING. */
    private static final String NULL_STRING = "-";

    /** The class type. */
    private Class<?> classType;

    /** The minimum version. */
    private VersionData minimumVersion = null;

    /** The maximum version. */
    private VersionData maximumVersion = null;

    /**
     * Instantiates a new vendor option version.
     *
     * @param classType the class type
     * @param minimumVersion the minimum version
     * @param maximumVersion the maximum version
     */
    public VendorOptionVersion(Class<?> classType, VersionData minimumVersion,
            VersionData maximumVersion) {
        this.classType = classType;
        this.minimumVersion = minimumVersion;
        this.maximumVersion = maximumVersion;
    }

    /**
     * Instantiates a new vendor option version.
     *
     * @param classType the class type
     * @param version the version
     */
    public VendorOptionVersion(Class<?> classType, VersionData version) {
        this.classType = classType;
        this.minimumVersion = version;
        this.maximumVersion = version;
    }

    /**
     * Checks if vendor option is allowed.
     *
     * @param versionData the version data
     * @return true, if is allowed
     */
    public boolean isAllowed(VersionData versionData) {
        if (versionData == null) {
            return false;
        }

        // Check to see if it is strict SLD, always allowed
        if (classType == NoVendorOption.class) {
            return true;
        }

        if (versionData.getVendorOptionType() != this.classType) {
            return false;
        }

        return versionData.inRange(this.minimumVersion, this.maximumVersion);
    }

    /**
     * From string.
     *
     * @param value the value
     * @return the vendor option version
     */
    public static VendorOptionVersion fromString(String value) {
        String[] components = value.split(DELIMETER);

        if (components.length == 3) {
            Class<?> classType = null;
            VersionData minimumVersion = null;
            VersionData maximumVersion = null;

            try {
                classType = Class.forName(components[0]);
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().error(VendorOptionVersion.class,
                        "Unknown VendorOption class : " + components[0]);
                return null;
            }
            if (components[1].compareTo(NULL_STRING) != 0) {
                minimumVersion = VersionData.getDecodedString(components[1]);
            }

            if (components[2].compareTo(NULL_STRING) != 0) {
                maximumVersion = VersionData.getDecodedString(components[2]);
            }
            VendorOptionVersion vendorOptionVersion = new VendorOptionVersion(classType,
                    minimumVersion, maximumVersion);

            return vendorOptionVersion;
        }

        return null;
    }

    /**
     * Gets the earliest version.
     *
     * @return the earliest version
     */
    public VersionData getEarliest() {
        return this.minimumVersion;
    }

    /**
     * Gets the latest version.
     *
     * @return the latest version
     */
    public VersionData getLatest() {
        return this.maximumVersion;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        String string = String.format("%s%s%s%s%s", classType.getName(), DELIMETER,
                (minimumVersion != null) ? minimumVersion.getEncodedString() : NULL_STRING,
                DELIMETER,
                (maximumVersion != null) ? maximumVersion.getEncodedString() : NULL_STRING);

        return string;
    }

    /**
     * Gets the class type.
     *
     * @return the classType
     */
    public Class<?> getClassType() {
        return classType;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classType == null) ? 0 : classType.hashCode());
        result = prime * result + ((maximumVersion == null) ? 0 : maximumVersion.hashCode());
        result = prime * result + ((minimumVersion == null) ? 0 : minimumVersion.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        VendorOptionVersion other = (VendorOptionVersion) obj;
        if (classType == null) {
            if (other.classType != null) {
                return false;
            }
        } else if (!classType.getName().equals(other.classType.getName())) {
            return false;
        }
        if (maximumVersion == null) {
            if (other.maximumVersion != null) {
                return false;
            }
        } else if (!maximumVersion.equals(other.maximumVersion)) {
            return false;
        }
        if (minimumVersion == null) {
            if (other.minimumVersion != null) {
                return false;
            }
        } else if (!minimumVersion.equals(other.minimumVersion)) {
            return false;
        }
        return true;
    }
}
