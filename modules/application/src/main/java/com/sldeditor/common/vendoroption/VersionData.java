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
import com.sldeditor.common.localisation.Localisation;

/**
 * Class that represents the vendor option versions supported.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VersionData implements Comparable<VersionData>, Cloneable {
    private static final String SNAPSHOT_SUFFIX = "-SNAPSHOT";

    /** The Constant EARLIEST. */
    private static final String EARLIEST = "First";

    /** The Constant LATEST. */
    private static final String LATEST = "Latest";

    /** The Constant ALL_VERSIONS. */
    private static final int ALL_VERSIONS = Integer.MAX_VALUE;

    /** The Constant DELIMETER. */
    private static final String DELIMETER = "@";

    /** The version string. */
    private String versionString = Localisation.getString(VersionData.class, "common.notSet");

    /** The is not set. */
    private boolean isNotSet = true;

    /** The major number. */
    private int majorNumber = ALL_VERSIONS;

    /** The minor number. */
    private int minorNumber = ALL_VERSIONS;

    /** The point number. */
    private int pointNumber = ALL_VERSIONS;

    /** The sub point number. */
    private int subPointNumber = ALL_VERSIONS;

    /** The vendor option. Set when read from json file using reflection */
    private String vendorOption = null;

    /** The vendor option type. */
    private Class<?> vendorOptionType = NoVendorOption.class;

    /** The snapshot flag. */
    private boolean snapshot = false;

    /** The is earliest flag. */
    private boolean isEarliest = false;

    /** The is latest flag. */
    private boolean isLatest = false;

    /**
     * Default constructor.
     */
    public VersionData() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    public VersionData clone() {
        VersionData versionData = new VersionData();
        versionData.vendorOptionType = vendorOptionType;
        versionData.isNotSet = isNotSet;
        versionData.majorNumber = majorNumber;
        versionData.minorNumber = minorNumber;
        versionData.pointNumber = pointNumber;
        versionData.subPointNumber = subPointNumber;
        versionData.vendorOption = vendorOption;
        versionData.versionString = versionString;
        versionData.isEarliest = isEarliest;
        versionData.isLatest = isLatest;

        return versionData;
    }

    /**
     * Decode.
     *
     * @param vendorOptionType the vendor option type
     * @param versionString the version string
     * @return the version data
     */
    public static VersionData decode(Class<?> vendorOptionType, String versionString) {
        if ((versionString == null) || versionString.isEmpty()) {
            return null;
        }

        VersionData versionData = new VersionData();

        versionData.setVendorOptionType(vendorOptionType);
        if (!versionData.setVersionString(versionString)) {
            return null;
        }

        return versionData;
    }

    /**
     * Decode number.
     *
     * @param string the string
     * @return the int
     */
    private static int decodeNumber(String string) {
        if (string.compareToIgnoreCase("x") == 0) {
            return ALL_VERSIONS;
        }

        return Integer.valueOf(string);
    }

    /**
     * Gets the version string.
     *
     * @return the version string
     */
    public String getVersionString() {
        return versionString;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(VersionData o) {
        if (vendorOptionType != o.vendorOptionType) {
            return (vendorOptionType == NoVendorOption.class) ? -1 : 1;
        }

        if (majorNumber == o.majorNumber) {
            if (minorNumber == o.minorNumber) {
                if (pointNumber == o.pointNumber) {
                    if (subPointNumber == o.subPointNumber) {
                        if (snapshot == o.snapshot) {
                            return 0;
                        }
                        return (snapshot) ? -1 : 1;
                    } else {
                        return (subPointNumber < o.subPointNumber) ? -1 : 1;
                    }
                } else {
                    return (pointNumber < o.pointNumber) ? -1 : 1;
                }
            } else {
                return (minorNumber < o.minorNumber) ? -1 : 1;
            }
        } else {
            return (majorNumber < o.majorNumber) ? -1 : 1;
        }
    }

    /**
     * Instantiates a new version data.
     *
     * @param vendorOptionType the vendor option type
     * @param name the name
     * @param version the version
     */
    private VersionData(Class<?> vendorOptionType, String name, int version) {
        this.isNotSet = false;
        this.vendorOptionType = vendorOptionType;
        this.versionString = name;
        this.majorNumber = version;
        this.minorNumber = version;
        this.pointNumber = version;
        this.subPointNumber = version;

        if (versionString.compareToIgnoreCase(EARLIEST) == 0) {
            this.isEarliest = true;
        } else if (versionString.compareToIgnoreCase(LATEST) == 0) {
            this.isLatest = true;
        }
    }

    /**
     * Gets the earliest version.
     *
     * @param vendorOptionType the vendor option type
     * @return the earliest version
     */
    public static VersionData getEarliestVersion(Class<?> vendorOptionType) {
        return new VersionData(vendorOptionType, EARLIEST, Integer.MIN_VALUE);
    }

    /**
     * Gets the latest version.
     *
     * @param vendorOptionType the vendor option type
     * @return the latest version
     */
    public static VersionData getLatestVersion(Class<?> vendorOptionType) {
        return new VersionData(vendorOptionType, LATEST, Integer.MAX_VALUE);
    }

    /**
     * Gets the not set version.
     *
     * @param vendorOptionType the vendor option type
     * @return the not set version
     */
    public static VersionData getNotSetVersion(Class<?> vendorOptionType) {
        VersionData versionData = new VersionData();
        versionData.vendorOptionType = vendorOptionType;

        return versionData;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.versionString;
    }

    /**
     * Gets the vendor option type.
     *
     * @return the vendor option type
     */
    public Class<?> getVendorOptionType() {
        return vendorOptionType;
    }

    /**
     * Checks to see if this object is in the version range.
     * Returns false if either supplied version is null.
     *
     * @param minimumVersion the minimum version
     * @param maximumVersion the maximum version
     * @return true, if successful
     */
    public boolean inRange(VersionData minimumVersion, VersionData maximumVersion) {
        if ((minimumVersion == null) || (maximumVersion == null)) {
            return false;
        }

        boolean inRange = minimumVersion.greaterThan(this) && this.lessThan(maximumVersion);

        return inRange;
    }

    /**
     * Less than.
     *
     * @param maximumVersion the maximum version
     * @return true, if successful
     */
    private boolean lessThan(VersionData maximumVersion) {
        if (this.majorNumber < maximumVersion.majorNumber) {
            return true;
        } else if (this.majorNumber == maximumVersion.majorNumber) {
            if (this.minorNumber < maximumVersion.minorNumber) {
                return true;
            } else if (this.minorNumber == maximumVersion.minorNumber) {
                if (this.pointNumber < maximumVersion.pointNumber) {
                    return true;
                } else if (this.pointNumber == maximumVersion.pointNumber) {
                    return (this.subPointNumber <= maximumVersion.subPointNumber);
                }
            }
        }
        return false;
    }

    /**
     * Greater than.
     *
     * @param versionData the version data
     * @return true, if successful
     */
    private boolean greaterThan(VersionData versionData) {
        if (versionData.isNotSet != this.isNotSet) {
            return this.isNotSet;
        } else if (versionData.majorNumber > this.majorNumber) {
            return true;
        } else if (versionData.majorNumber == this.majorNumber) {
            if (versionData.minorNumber > this.minorNumber) {
                return true;
            } else if (versionData.minorNumber == this.minorNumber) {
                if (versionData.pointNumber > this.pointNumber) {
                    return true;
                } else if (versionData.pointNumber == this.pointNumber) {
                    return (versionData.subPointNumber >= this.subPointNumber);
                }
            }
        }
        return false;
    }

    /**
     * Gets the encoded string.
     *
     * @return the encoded string
     */
    public String getEncodedString() {
        StringBuilder sb = new StringBuilder();
        sb.append(vendorOptionType.getName());
        sb.append(DELIMETER);
        sb.append(versionString);

        return sb.toString();
    }

    /**
     * Gets the decoded string.
     *
     * @param string the string
     * @return the decoded string
     */
    public static VersionData getDecodedString(String string) {
        VersionData versionData = null;

        if (string != null) {
            String[] components = string.split(DELIMETER);

            if (components.length == 2) {
                try {
                    Class<?> classType = Class.forName(components[0]);
                    versionData = VersionData.decode(classType, components[1]);
                } catch (ClassNotFoundException e) {
                    ConsoleManager.getInstance().error(VersionData.class,
                            "Unknown vendor option class : " + components[0]);
                }
            }
        }
        return versionData;
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
        result = prime * (result + majorNumber);
        result = prime * (result + minorNumber);
        result = prime * (result + pointNumber);
        result = prime * (result + subPointNumber);
        result = prime * (result + ((vendorOptionType == null) ? 0 : vendorOptionType.hashCode()));
        result = prime * (result + ((versionString == null) ? 0 : versionString.hashCode()));
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

        VersionData other = (VersionData) obj;

        if (majorNumber != other.majorNumber) {
            return false;
        }
        if (minorNumber != other.minorNumber) {
            return false;
        }
        if (pointNumber != other.pointNumber) {
            return false;
        }
        if (subPointNumber != other.subPointNumber) {
            return false;
        }
        if (vendorOptionType == null) {
            if (other.vendorOptionType != null) {
                return false;
            }
        } else if (!vendorOptionType.equals(other.vendorOptionType)) {
            return false;
        }

        if (versionString == null) {
            if (other.versionString != null) {
                return false;
            }
        } else if (!versionString.equals(other.versionString)) {
            return false;
        }

        return true;
    }

    /**
     * Sets the version string.
     *
     * @param versionString the new version string
     * @return true, if successful
     */
    private boolean setVersionString(String versionString) {
        isNotSet = false;
        this.versionString = versionString;

        if (versionString.compareToIgnoreCase(EARLIEST) == 0) {
            this.majorNumber = Integer.MIN_VALUE;
            this.minorNumber = Integer.MIN_VALUE;
            this.pointNumber = Integer.MIN_VALUE;
            this.subPointNumber = Integer.MIN_VALUE;
            this.isEarliest = true;
        } else if (versionString.compareToIgnoreCase(LATEST) == 0) {
            this.majorNumber = Integer.MAX_VALUE;
            this.minorNumber = Integer.MAX_VALUE;
            this.pointNumber = Integer.MAX_VALUE;
            this.subPointNumber = Integer.MAX_VALUE;
            this.isLatest = true;
        } else {
            snapshot = versionString.endsWith(SNAPSHOT_SUFFIX);
            if (snapshot) {
                versionString = versionString.substring(0,
                        versionString.length() - SNAPSHOT_SUFFIX.length());
            }

            String[] versionComponents = versionString.split("\\.");

            try {
                if (versionComponents.length > 0) {
                    this.majorNumber = decodeNumber(versionComponents[0]);
                }

                if (versionComponents.length > 1) {
                    this.minorNumber = decodeNumber(versionComponents[1]);
                }

                if (versionComponents.length > 2) {
                    this.pointNumber = decodeNumber(versionComponents[2]);
                }

                if (versionComponents.length > 3) {
                    this.subPointNumber = decodeNumber(versionComponents[2]);
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the vendor option type.
     *
     * @param vendorOptionType the new vendor option type
     */
    private void setVendorOptionType(Class<?> vendorOptionType) {
        isNotSet = false;
        this.vendorOptionType = vendorOptionType;
    }

    /**
     * Returns the not set flag.
     *
     * @return true, if data is not set
     */
    public boolean isNotSet() {
        return isNotSet;
    }

    /**
     * Returns true if earliest.
     *
     * @return true, if is earliest
     */
    public boolean isEarliest() {
        return isEarliest;
    }

    /**
     * Returns true if latest.
     *
     * @return true, if is latest
     */
    public boolean isLatest() {
        return isLatest;
    }
}
