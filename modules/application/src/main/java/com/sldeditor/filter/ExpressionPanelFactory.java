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

package com.sldeditor.filter;

import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;
import com.sldeditor.filter.v2.expression.ExpressionNode;
import com.sldeditor.filter.v2.expression.ExpressionPanelv2;
import com.sldeditor.filter.v2.expression.FilterPanelv2;
import java.awt.Color;
import java.util.List;

/**
 * A factory for creating FilterPanel and ExpressionPanel objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class ExpressionPanelFactory implements PrefUpdateInterface, VendorOptionUpdateInterface {

    /** The singleton instance. */
    private static ExpressionPanelFactory instance = null;

    /** The vendor option list. */
    private List<VersionData> vendorOptionVersionsList = null;

    /** The in test mode flag. */
    private static boolean inTestMode = false;

    /**
     * Gets the single instance of ExpressionPanelFactory.
     *
     * @return single instance of ExpressionPanelFactory
     */
    private static ExpressionPanelFactory getInstance() {
        if (instance == null) {
            instance = new ExpressionPanelFactory();
        }

        return instance;
    }

    /** Instantiates a new expression panel factory. */
    private ExpressionPanelFactory() {
        PrefManager.getInstance().addListener(this);
        VendorOptionManager.getInstance().addVendorOptionListener(this);

        EnvironmentManagerInterface envMgr = EnvironmentVariableManager.getInstance();
        ExpressionNode.setEnvMgr(envMgr);
    }

    /**
     * Creates and returns the filter panel.
     *
     * @param hints the hints
     * @return the filter panel
     */
    private FilterPanelInterface internal_getFilterPanel(String hints) {
        return new FilterPanelv2(this.vendorOptionVersionsList, inTestMode);
    }

    /**
     * Creates and returns the expression panel.
     *
     * @param hints the hints
     * @return the expression panel
     */
    private ExpressionPanelInterface internal_getExpressionPanel(String hints) {
        return new ExpressionPanelv2(this.vendorOptionVersionsList, inTestMode);
    }

    /**
     * Creates and returns the expression panel for the RenderTransformation dialog.
     *
     * @param hints the hints
     * @return the expression panel
     */
    private ExpressionPanelInterface internal_getRenderTransformationPanel(String hints) {
        return new RenderTransformationExpressionPanelv2(this.vendorOptionVersionsList, inTestMode);
    }

    /**
     * Creates and returns the filter panel.
     *
     * @param hints the hints
     * @return the filter panel
     */
    public static FilterPanelInterface getFilterPanel(String hints) {
        return getInstance().internal_getFilterPanel(hints);
    }

    /**
     * Creates and returns the expression panel.
     *
     * @param hints the hints
     * @return the expression panel
     */
    public static ExpressionPanelInterface getExpressionPanel(String hints) {
        return getInstance().internal_getExpressionPanel(hints);
    }

    /**
     * Gets the render transformation panel.
     *
     * @param hints the hints
     * @return the render transformation panel
     */
    public static ExpressionPanelInterface getRenderTransformationPanel(String hints) {
        return getInstance().internal_getRenderTransformationPanel(hints);
    }

    /**
     * Use anti alias updated.
     *
     * @param value the value
     */
    @Override
    public void useAntiAliasUpdated(boolean value) {
        // Ignore
    }

    /**
     * Vendor options updated.
     *
     * @param vendorOptionVersionsList the vendor option list
     */
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
    }

    /**
     * Background colour update.
     *
     * @param backgroundColour the background colour
     */
    @Override
    public void backgroundColourUpdate(Color backgroundColour) {
        // Do nothing
    }

    /** Sets the test mode. */
    public static void setTestMode() {
        inTestMode = true;
    }

    /** Destroy instance. */
    public static void destroyInstance() {
        PrefManager.destroyInstance();
        VendorOptionManager.destroyInstance();

        EnvironmentVariableManager.destroyInstance();
        ExpressionNode.setEnvMgr(null);

        instance = null;
    }
}
