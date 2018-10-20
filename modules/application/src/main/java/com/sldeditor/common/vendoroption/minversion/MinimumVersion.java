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

import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.ui.panels.GetMinimumVersionInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.Symbolizer;

/**
 * The Class MinimumVersion.
 *
 * @author Robert Ward (SCISYS)
 */
public class MinimumVersion {

    /** The ui manager. */
    private GetMinimumVersionInterface uiMgr = null;

    /** The vendor options present list. */
    private List<VendorOptionPresent> vendorOptionsPresentList = new ArrayList<>();

    /** The default vendor option version. */
    private VendorOptionVersion defaultVendorOptionVersion =
            VendorOptionManager.getInstance().getDefaultVendorOptionVersion();

    /**
     * Instantiates a new minimum version.
     *
     * @param uiMgr the ui mgr
     */
    public MinimumVersion(GetMinimumVersionInterface uiMgr) {
        this.uiMgr = uiMgr;
    }

    /**
     * Find minimum version.
     *
     * @param sld the sld
     */
    public void findMinimumVersion(StyledLayerDescriptor sld) {
        vendorOptionsPresentList.clear();
        Object parentObj = null;

        if ((sld != null) && (uiMgr != null)) {
            uiMgr.getMinimumVersion(parentObj, sld, vendorOptionsPresentList);
            List<StyledLayer> styledLayerList = sld.layers();

            parentObj = sld;
            for (StyledLayer styledLayer : styledLayerList) {
                uiMgr.getMinimumVersion(parentObj, styledLayer, vendorOptionsPresentList);
                List<Style> styleList = SLDUtils.getStylesList(styledLayer);

                parentObj = styledLayer;
                for (Style style : styleList) {
                    uiMgr.getMinimumVersion(parentObj, style, vendorOptionsPresentList);
                    parentObj = style;

                    for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                        uiMgr.getMinimumVersion(parentObj, fts, vendorOptionsPresentList);
                        parentObj = fts;

                        for (Rule rule : fts.rules()) {
                            uiMgr.getMinimumVersion(parentObj, rule, vendorOptionsPresentList);
                            parentObj = rule;

                            for (Symbolizer symbolizer : rule.symbolizers()) {
                                uiMgr.getMinimumVersion(
                                        parentObj, symbolizer, vendorOptionsPresentList);
                            }
                        }
                    }
                }
            }
        }

        removeStrictSLD();
    }

    /** Removes the strict SLD. */
    private void removeStrictSLD() {
        List<VendorOptionPresent> newList = new ArrayList<>();

        for (VendorOptionPresent obj : vendorOptionsPresentList) {
            if (obj.getVendorOptionInfo().getVersionData() != defaultVendorOptionVersion) {
                newList.add(obj);
            }
        }

        vendorOptionsPresentList = newList;

        Collections.sort(vendorOptionsPresentList);
    }

    /**
     * Gets the vendor options present list.
     *
     * @return the vendorOptionsPresentList
     */
    public List<VendorOptionPresent> getVendorOptionsPresentList() {
        return vendorOptionsPresentList;
    }

    /**
     * Gets the minimum version.
     *
     * @param userDefaultVendorOption the user default vendor option
     * @return the minimum version
     */
    public List<VersionData> getMinimumVersion(List<VersionData> userDefaultVendorOption) {
        List<VersionData> list = new ArrayList<>();
        if (vendorOptionsPresentList.isEmpty()) {
            list = userDefaultVendorOption;
        } else {
            VendorOptionPresent lastVendorOption =
                    vendorOptionsPresentList.get(vendorOptionsPresentList.size() - 1);
            VendorOptionInfo vendorOptionInfo = lastVendorOption.getVendorOptionInfo();
            VersionData earliestReadFromSymbol = vendorOptionInfo.getVersionData().getEarliest();
            list.add(findLatest(earliestReadFromSymbol, userDefaultVendorOption));
        }

        return list;
    }

    /**
     * Find latest version data between the vendor option version read from the symbol and the user
     * default.
     *
     * @param earliestReadFromSymbol the earliest read from symbol
     * @param userDefaultVendorOptionList the user default vendor option
     * @return the version data
     */
    protected VersionData findLatest(
            VersionData earliestReadFromSymbol, List<VersionData> userDefaultVendorOptionList) {
        // Sort the list, the latest will be last
        Collections.sort(userDefaultVendorOptionList);

        VersionData latestUserDefault =
                userDefaultVendorOptionList.get(userDefaultVendorOptionList.size() - 1);

        return (earliestReadFromSymbol.compareTo(latestUserDefault) < 0)
                ? latestUserDefault
                : earliestReadFromSymbol;
    }
}
