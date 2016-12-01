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
     * Gets the vendor option version string, returns the last vendor option in the list
     *
     * @param vendorOptionVersionsList the vendor option versions list
     * @return the version string
     */
    public static String getVersionString(List<VersionData> vendorOptionVersionsList) {
        String versionString = "";

        if(vendorOptionVersionsList != null)
        {
            for (VersionData versionData : vendorOptionVersionsList) {
                versionString = extractVersionData(versionData);
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
    private static String extractVersionData(VersionData versionData) {
        String versionString = "";
        if (versionData != null) {
            try {
                VendorOptionTypeInterface o = (VendorOptionTypeInterface) Class
                        .forName(versionData.getVendorOptionType().getName()).newInstance();
                String vendorOptionName = o.getName();

                if (versionData.getVendorOptionType() == NoVendorOption.class) {
                    versionString = vendorOptionName;
                } else {
                    versionString = String.format("%s %s", vendorOptionName, versionData.getVersionString());
                }
            } catch (InstantiationException e) {
                ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
            } catch (IllegalAccessException e) {
                ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
            } catch (ClassNotFoundException e) {
                ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
            } catch (ClassCastException e) {
                ConsoleManager.getInstance().exception(VendorOptionStatus.class, e);
            }
        }
        return versionString;
    }

}
