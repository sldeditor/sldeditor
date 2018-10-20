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

package com.sldeditor.ui.detail.config;

import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import java.util.List;
import javax.swing.Box;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigVendorOption.
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigVendorOption extends FieldConfigBase
        implements VendorOptionUpdateInterface {

    /** The vendor option versions list. */
    private List<VersionData> vendorOptionVersionsList = null;

    /** The option box. */
    private Box optionBox = null;

    /** The has the object been initialised flag. */
    private boolean initialised = false;

    /** The vendor option list. */
    private List<VendorOptionInterface> veList = null;

    /**
     * Instantiates a new field config vendor option.
     *
     * @param commonData the common data
     * @param veList the ve list
     */
    public FieldConfigVendorOption(
            FieldConfigCommonData commonData, List<VendorOptionInterface> veList) {
        super(commonData);

        this.veList = veList;

        VendorOptionManager.getInstance().addVendorOptionListener(this);
    }

    /** Creates the ui. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        if (!initialised) {
            createFieldPanel();
            initialised = true;
        }
    }

    /**
     * Adds vendor option to the option box.
     *
     * @param optionBox the option box
     */
    public void addToOptionBox(Box optionBox) {
        this.optionBox = optionBox;
        updateVendorOptionPanels(vendorOptionVersionsList);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(
     * java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {

        this.vendorOptionVersionsList = vendorOptionVersionsList;

        if (initialised) {
            updateVendorOptionPanels(vendorOptionVersionsList);
        }
    }

    /**
     * Update vendor option panels.
     *
     * @param vendorOptionVersionsList the vendor option versions list
     */
    private void updateVendorOptionPanels(List<VersionData> vendorOptionVersionsList) {
        if (veList != null) {
            for (VendorOptionInterface vendorOption : veList) {
                boolean displayVendorOption =
                        VendorOptionManager.getInstance()
                                .isAllowed(
                                        vendorOptionVersionsList, vendorOption.getVendorOption());

                BasePanel extensionPanel = vendorOption.getPanel();
                if (extensionPanel != null) {
                    BasePanel parentPanel = (BasePanel) vendorOption.getParentPanel();
                    if (parentPanel != null) {
                        if (this.optionBox != null) {
                            // Vendor option is part of an option box
                            this.optionBox.remove(extensionPanel);
                        } else {
                            parentPanel.removePanel(extensionPanel);
                        }

                        if (displayVendorOption) {
                            parentPanel.insertPanel(this, extensionPanel, this.optionBox);
                        }
                    }
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigValuePopulateInterface#getStringValue()
     */
    @Override
    public String getStringValue() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void internalSetEnabled(boolean enabled) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression() {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue() {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.detail.config.FieldConfigBase#createCopy(com.sldeditor.ui.detail.config.
     * FieldConfigBase)
     */
    @Override
    public FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigVendorOption copy = null;

        if (fieldConfigBase instanceof FieldConfigVendorOption) {
            FieldConfigVendorOption existing = (FieldConfigVendorOption) fieldConfigBase;
            copy = new FieldConfigVendorOption(existing.getCommonData(), existing.veList);
        }
        return copy;
    }
}
