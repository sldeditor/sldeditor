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
package com.sldeditor.common.preferences;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * Class that encapsulates user preference data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefData implements Cloneable {

    /** The use anti alias flag. */
    private boolean useAntiAlias = false;

    /** The vendor option list. */
    private List<VersionData> vendorOptionList = new ArrayList<VersionData>();

    /** The ui layout class. */
    private String uiLayoutClass;

    /** The background colour. */
    private Color backgroundColour = Color.WHITE;

    /** The save last folder view flag. */
    private boolean saveLastFolderView = false;

    /** The last folder viewed. */
    private String lastFolderViewed = null;
    
    /**
     * Default constructor.
     */
    public PrefData()
    {
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
    }

    /**
     * Clone.
     *
     * @return the pref data
     */
    /* (non-Javadoc)
     * @see java.lang.Object#clone()
     */
    @Override
    public PrefData clone() {
        PrefData copy = new PrefData();
        copy.uiLayoutClass = this.uiLayoutClass;
        copy.useAntiAlias = this.useAntiAlias;
        copy.backgroundColour = new Color(this.backgroundColour.getRGB());
        copy.saveLastFolderView = this.saveLastFolderView;
        copy.lastFolderViewed = this.lastFolderViewed;

        if(this.vendorOptionList != null)
        {
            copy.vendorOptionList = new ArrayList<VersionData>();

            for(VersionData versionData : vendorOptionList)
            {
                copy.vendorOptionList.add(versionData.clone());
            }
        }
        return copy;
    }

    /**
     * Checks if is use anti alias.
     *
     * @return true, if is use anti alias
     */
    public boolean isUseAntiAlias() {
        return useAntiAlias;
    }

    /**
     * Sets the use anti alias.
     *
     * @param useAntiAlias the new use anti alias
     */
    public void setUseAntiAlias(boolean useAntiAlias) {
        this.useAntiAlias = useAntiAlias;
    }

    /**
     * Gets the vendor option version list.
     *
     * @return the vendor option version list
     */
    public List<VersionData> getVendorOptionVersionList() {
        return vendorOptionList;
    }

    /**
     * Sets the vendor option version list.
     *
     * @param vendorOptionList the new vendor option version list
     */
    public void setVendorOptionVersionList(List<VersionData> vendorOptionList) {
        this.vendorOptionList = vendorOptionList;
    }

    /**
     * Gets the ui layout class.
     *
     * @return the ui layout class
     */
    public String getUiLayoutClass()
    {
        return uiLayoutClass;
    }

    /**
     * Sets the ui layout class.
     *
     * @param uiLayoutClass the new ui layout class
     */
    public void setUiLayoutClass(String uiLayoutClass)
    {
        this.uiLayoutClass = uiLayoutClass;
    }

    /**
     * Gets the background colour.
     *
     * @return the background colour
     */
    public Color getBackgroundColour() {
        return backgroundColour;
    }

    /**
     * Sets the background colour.
     *
     * @param backgroundColour the new background colour
     */
    public void setBackgroundColour(Color backgroundColour) {
        this.backgroundColour = backgroundColour;
    }

    /**
     * Gets the last folder viewed.
     *
     * @return the lastFolderViewed
     */
    public String getLastFolderViewed() {
        return lastFolderViewed;
    }

    /**
     * Sets the last folder viewed.
     *
     * @param lastFolderViewed the lastFolderViewed to set
     */
    public void setLastFolderViewed(String lastFolderViewed) {
        this.lastFolderViewed = lastFolderViewed;
    }

    /**
     * Checks if is save last folder view.
     *
     * @return the saveLastFolderView
     */
    public boolean isSaveLastFolderView() {
        return saveLastFolderView;
    }

    /**
     * Sets the save last folder view.
     *
     * @param saveLastFolderView the saveLastFolderView to set
     */
    public void setSaveLastFolderView(boolean saveLastFolderView) {
        this.saveLastFolderView = saveLastFolderView;
    }
}
