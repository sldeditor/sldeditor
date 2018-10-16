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

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that encapsulates user preference data.
 *
 * @author Robert Ward (SCISYS)
 */
public class PrefData {

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

    /** The last viewed key. */
    private PrefDataLastViewedEnum lastViewedKey = PrefDataLastViewedEnum.FOLDER;

    /** The check app version on start up. */
    private boolean checkAppVersionOnStartUp = true;

    /** Default constructor. */
    public PrefData() {
        vendorOptionList.add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
    }

    /**
     * Copy constructor.
     *
     * @param clone the clone
     */
    public PrefData(PrefData clone) {
        this.uiLayoutClass = clone.uiLayoutClass;
        this.useAntiAlias = clone.useAntiAlias;
        this.backgroundColour = new Color(clone.backgroundColour.getRGB());
        this.saveLastFolderView = clone.saveLastFolderView;
        this.lastFolderViewed = clone.lastFolderViewed;
        this.lastViewedKey = clone.lastViewedKey;
        this.checkAppVersionOnStartUp = clone.checkAppVersionOnStartUp;

        if (clone.vendorOptionList != null) {
            this.vendorOptionList = new ArrayList<>();

            for (VersionData versionData : clone.vendorOptionList) {
                this.vendorOptionList.add(new VersionData(versionData));
            }
        }
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
    public String getUiLayoutClass() {
        return uiLayoutClass;
    }

    /**
     * Sets the ui layout class.
     *
     * @param uiLayoutClass the new ui layout class
     */
    public void setUiLayoutClass(String uiLayoutClass) {
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

    /**
     * Gets the last viewed key.
     *
     * @return the lastViewedKey
     */
    public PrefDataLastViewedEnum getLastViewedKey() {
        return lastViewedKey;
    }

    /**
     * Sets the last viewed key.
     *
     * @param lastViewedKey the lastViewedKey to set
     */
    public void setLastViewedKey(PrefDataLastViewedEnum lastViewedKey) {
        this.lastViewedKey = lastViewedKey;
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
        result = prime * result + ((backgroundColour == null) ? 0 : backgroundColour.hashCode());
        result = prime * result + (checkAppVersionOnStartUp ? 1231 : 1237);
        result = prime * result + ((lastFolderViewed == null) ? 0 : lastFolderViewed.hashCode());
        result = prime * result + ((lastViewedKey == null) ? 0 : lastViewedKey.hashCode());
        result = prime * result + (saveLastFolderView ? 1231 : 1237);
        result = prime * result + ((uiLayoutClass == null) ? 0 : uiLayoutClass.hashCode());
        result = prime * result + (useAntiAlias ? 1231 : 1237);
        result = prime * result + ((vendorOptionList == null) ? 0 : vendorOptionList.hashCode());
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
        PrefData other = (PrefData) obj;
        if (backgroundColour == null) {
            if (other.backgroundColour != null) {
                return false;
            }
        } else if (!backgroundColour.equals(other.backgroundColour)) {
            return false;
        }
        if (checkAppVersionOnStartUp != other.checkAppVersionOnStartUp) {
            return false;
        }
        if (lastFolderViewed == null) {
            if (other.lastFolderViewed != null) {
                return false;
            }
        } else if (!lastFolderViewed.equals(other.lastFolderViewed)) {
            return false;
        }
        if (lastViewedKey != other.lastViewedKey) {
            return false;
        }
        if (saveLastFolderView != other.saveLastFolderView) {
            return false;
        }
        if (uiLayoutClass == null) {
            if (other.uiLayoutClass != null) {
                return false;
            }
        } else if (!uiLayoutClass.equals(other.uiLayoutClass)) {
            return false;
        }
        if (useAntiAlias != other.useAntiAlias) {
            return false;
        }
        if (vendorOptionList == null) {
            if (other.vendorOptionList != null) {
                return false;
            }
        } else if (!vendorOptionList.equals(other.vendorOptionList)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is check app version on start up.
     *
     * @return the checkAppVersionOnStartUp
     */
    public boolean isCheckAppVersionOnStartUp() {
        return checkAppVersionOnStartUp;
    }

    /**
     * Sets the check app version on start up.
     *
     * @param checkAppVersionOnStartUp the checkAppVersionOnStartUp to set
     */
    public void setCheckAppVersionOnStartUp(boolean checkAppVersionOnStartUp) {
        this.checkAppVersionOnStartUp = checkAppVersionOnStartUp;
    }
}
