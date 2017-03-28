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


package com.sldeditor.ui.widgets;

import javax.swing.ImageIcon;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;

/**
 * Class that encapsulates the data displayed as an item within the ValueComboBox component.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ValueComboBoxData {

    /** The key. */
    private String key;

    /** The text. */
    private String text = null;

    /** The panel id. */
    private Class<?> panelId;

    /** The image icon. */
    private ImageIcon imageIcon = null;

    /** The vendor option. */
    private VendorOptionVersion vendorOptionVersion = VendorOptionManager.getInstance()
            .getDefaultVendorOptionVersion();

    /**
     * Instantiates a new value combo box data.
     *
     * @param key the key
     * @param text the text
     * @param vendorOptionVersion the vendor option
     */
    public ValueComboBoxData(String key, String text, VendorOptionVersion vendorOptionVersion) {
        super();
        this.key = key;
        this.text = text;
        this.vendorOptionVersion = vendorOptionVersion;
    }

    /**
     * Instantiates a new value combo box data.
     *
     * @param key the key
     * @param text the text
     * @param panelId the panel id
     */
    public ValueComboBoxData(String key, String text, Class<?> panelId) {
        super();
        this.key = key;
        this.text = text;
        this.panelId = panelId;
        this.vendorOptionVersion = VendorOptionManager.getInstance()
                .getDefaultVendorOptionVersion();
    }

    /**
     * Instantiates a new value combo box data.
     *
     * @param key the key
     * @param text the text
     * @param vendorOptionVersion the vendor option version
     * @param panelId the panel id
     */
    public ValueComboBoxData(String key, String text, VendorOptionVersion vendorOptionVersion,
            Class<?> panelId) {
        super();
        this.key = key;
        this.text = text;
        this.panelId = panelId;
        this.vendorOptionVersion = vendorOptionVersion;
    }

    /**
     * Instantiates a new value combo box data.
     *
     * @param key the key
     * @param imageIcon the image icon
     * @param panelId the panel id
     */
    public ValueComboBoxData(String key, ImageIcon imageIcon, Class<?> panelId) {
        super();
        this.key = key;
        this.imageIcon = imageIcon;
        this.panelId = panelId;
        this.vendorOptionVersion = VendorOptionManager.getInstance()
                .getDefaultVendorOptionVersion();
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    public VendorOptionVersion getVendorOption() {
        return vendorOptionVersion;
    }

    /**
     * Gets the panel id.
     *
     * @return the panel id
     */
    public Class<?> getPanelId() {
        return panelId;
    }

    /**
     * Sets the panel id.
     *
     * @param panelId the new panel id
     */
    public void setPanelId(Class<?> panelId) {
        this.panelId = panelId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return text;
    }

    /**
     * Gets the image icon.
     *
     * @return the imageIcon
     */
    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
