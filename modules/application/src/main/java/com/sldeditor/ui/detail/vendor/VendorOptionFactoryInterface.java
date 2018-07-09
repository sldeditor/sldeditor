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

package com.sldeditor.ui.detail.vendor;

import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import java.util.List;

/**
 * The Interface VendorOptionFactoryInterface.
 *
 * @author Robert Ward (SCISYS)
 */
public interface VendorOptionFactoryInterface {

    /**
     * Gets the vendor option list.
     *
     * @return the vendor option list
     */
    List<VendorOptionInterface> getVendorOptionList();

    /**
     * Gets the vendor option list.
     *
     * @param className the class name
     * @return the vendor option list
     */
    List<VendorOptionInterface> getVendorOptionList(String className);

    /**
     * Gets the vendor option info list.
     *
     * @return the vendor option info list
     */
    List<VendorOptionInfo> getVendorOptionInfoList();
}
