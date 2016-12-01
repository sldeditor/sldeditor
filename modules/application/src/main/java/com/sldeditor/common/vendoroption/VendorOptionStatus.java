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

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class VendorOptionStatus.
 *
 * @author Robert Ward (SCISYS)
 */
public class VendorOptionStatus {

    /**
     * Gets the vendor option version string, returns the last vendor option in the list.
     *
     * @param vendorOptionVersionsList the vendor option versions list
     * @return the version string
     */
    public static String getVersionString(List<VersionData> vendorOptionVersionsList) {
        String versionString = "";

        if (vendorOptionVersionsList != null) {
            for (VersionData versionData : vendorOptionVersionsList) {
                versionString = internal_getVersionString(versionData);
            }
        }
        return versionString;
    }

    /**
     * Extract information from VersionData.
     *
     * @param versionData the version data
     * @return the version string
     */
    private static String internal_getVersionString(VersionData versionData) {
        String versionString = "";
        if (versionData != null) {
            String vendorOptionName = getVendorOptionName(versionData.getVendorOptionType());

            if(vendorOptionName != null)
            {
                if (versionData.getVendorOptionType() == NoVendorOption.class) {
                    versionString = vendorOptionName;
                } else {
                    versionString = String.format("%s %s", vendorOptionName,
                            versionData.getVersionString());
                }
            }
        }
        return versionString;
    }

    /**
     * Gets the vendor option name.
     *
     * @param vendorOptionType the vendor option type
     * @return the vendor option name
     */
    private static String getVendorOptionName(Class<?> vendorOptionType) {
        try {
            VendorOptionTypeInterface o = (VendorOptionTypeInterface) Class
                    .forName(vendorOptionType.getName()).newInstance();
            return o.getName();
        } catch (InstantiationException e) {
            ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
        } catch (IllegalAccessException e) {
            ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
        } catch (ClassNotFoundException e) {
            ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
        } catch (ClassCastException e) {
            ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
        }
        return null;
    }

    /**
     * Gets the version string for a vendor option.
     *
     * @param versionData the version data
     * @return the version string
     */
    public static String getVendorOptionVersionString(VendorOptionVersion versionData) {
        boolean hasEarliest = !versionData.getEarliest().isEarliest();
        boolean hasLatest = !versionData.getLatest().isLatest();

        String vendorOptionName = getVendorOptionName(versionData.getClassType());

        if (versionData.getClassType() != NoVendorOption.class) {
            if (hasEarliest && !hasLatest) {
                return String.format("%s %s-", vendorOptionName,
                        versionData.getEarliest().getVersionString());
            } else if (!hasEarliest && hasLatest) {
                return String.format("%s -%s", vendorOptionName,
                        versionData.getLatest().getVersionString());
            } else if (hasEarliest && hasLatest) {
                return String.format("%s %s-%s", vendorOptionName,
                        versionData.getEarliest().getVersionString(),
                        versionData.getLatest().getVersionString());
            }
        }

        return vendorOptionName;
    }

}
