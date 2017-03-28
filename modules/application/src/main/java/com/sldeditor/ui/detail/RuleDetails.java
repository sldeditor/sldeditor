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

package com.sldeditor.ui.detail;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;

import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.styling.Graphic;
import org.opengis.filter.Filter;
import org.opengis.style.GraphicLegend;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.Rule;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.utils.ScaleUtil;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.filter.FilterPanelInterface;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class RuleDetails allows a user to configure rules data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class RuleDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The original filter, stored so it can be used if the new filter is invalid. */
    private Filter originalFilter;

    /**
     * Constructor.
     */
    public RuleDetails() {
        super(RuleDetails.class);
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFile(null, getClass(), this, "Rule.xml");

        createFilter();
    }

    /**
     * Creates the filter.
     *
     * @return the j panel
     */
    private void createFilter() {
        FieldIdEnum filterFieldId = FieldIdEnum.FILTER;
        FieldConfigBase fieldConfig = fieldConfigManager.get(filterFieldId);
        if (fieldConfig != null) {
            FieldPanel fieldPanel = fieldConfig.getPanel();

            JButton btnEditFilter = new JButton(
                    Localisation.getString(RuleDetails.class, "RuleDetails.edit"));
            btnEditFilter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    editFilter(filterFieldId);
                }
            });
            btnEditFilter.setBounds(WIDGET_X_START + WIDGET_EXTENDED_WIDTH, 0, WIDGET_BUTTON_WIDTH,
                    WIDGET_HEIGHT);
            fieldPanel.add(btnEditFilter);

            JButton btnClearFilter = new JButton(
                    Localisation.getString(RuleDetails.class, "RuleDetails.clear"));
            btnClearFilter.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    clearFilter(filterFieldId);
                }
            });

            int x = btnEditFilter.getX() + btnEditFilter.getWidth() + 5;
            btnClearFilter.setBounds(x, 0, WIDGET_BUTTON_WIDTH, WIDGET_HEIGHT);
            fieldPanel.add(btnClearFilter);
        }
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        boolean rangeSet = false;
        String minScaleText = "";
        String maxScaleText = "";

        if (selectedSymbol != null) {
            Rule rule = selectedSymbol.getRule();

            if (rule != null) {
                populateStandardData(rule);

                originalFilter = rule.getFilter();
                String filterString = "";
                if (originalFilter != null) {
                    try {
                        filterString = CQL.toCQL(originalFilter);
                    } catch (Exception e) {
                    }
                }
                fieldConfigVisitor.populateTextField(FieldIdEnum.FILTER, filterString);

                rangeSet = ScaleUtil.isPresent(rule);
                minScaleText = ScaleUtil.getValue(rule.getMinScaleDenominator());
                maxScaleText = ScaleUtil.getValue(rule.getMaxScaleDenominator());

                fieldConfigVisitor.populateBooleanField(FieldIdEnum.ELSE_FILTER,
                        rule.isElseFilter());
            }
        }

        fieldConfigVisitor.populateTextField(FieldIdEnum.MINIMUM_SCALE, minScaleText);
        fieldConfigVisitor.populateTextField(FieldIdEnum.MAXIMUM_SCALE, maxScaleText);
        populateScaleRange(rangeSet);
    }

    /**
     * Populate scale range.
     *
     * @param enabled the enabled
     */
    private void populateScaleRange(boolean enabled) {
        GroupConfigInterface group = getGroup(GroupIdEnum.SCALE);
        group.enable(enabled);
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            StandardData standardData = getStandardData();

            double minScale = getMinimumValue(fieldConfigManager.get(FieldIdEnum.MINIMUM_SCALE));
            double maxScale = getMaximumValue(fieldConfigManager.get(FieldIdEnum.MAXIMUM_SCALE));

            //
            // Read filter string
            //
            String filterText = fieldConfigVisitor.getText(FieldIdEnum.FILTER);
            Filter filter = originalFilter;
            if (originalFilter == null) {
                try {
                    if (!filterText.isEmpty()) {
                        filter = CQL.toFilter(filterText);
                    }
                } catch (CQLException e) {
                    filter = originalFilter;
                    ConsoleManager.getInstance().exception(this, e);
                }
            }

            //
            // Use existing symbolizers
            //
            org.geotools.styling.Rule existingRule = SelectedSymbol.getInstance().getRule();
            if (existingRule != null) {
                List<org.geotools.styling.Symbolizer> symbolizerList = existingRule.symbolizers();

                org.geotools.styling.Symbolizer[] symbolizerArray = new org.geotools.styling.Symbolizer[symbolizerList
                        .size()];
                int index = 0;
                for (org.geotools.styling.Symbolizer symbolizer : symbolizerList) {
                    symbolizerArray[index] = symbolizer;
                    index++;
                }

                //
                // Legend
                //
                GraphicLegend existingLegend = existingRule.getLegend();
                Graphic[] legendGraphics = null;

                if (existingLegend != null) {
                    int legendGraphicCount = existingLegend.graphicalSymbols().size();
                    legendGraphics = new Graphic[legendGraphicCount];

                    index = 0;
                    for (GraphicalSymbol graphicalSymbol : existingLegend.graphicalSymbols()) {
                        legendGraphics[index] = (Graphic) graphicalSymbol;
                        index++;
                    }
                } else {
                    legendGraphics = new Graphic[0];
                }

                //
                // Else filter
                //
                boolean isElseFilter = fieldConfigVisitor.getBoolean(FieldIdEnum.ELSE_FILTER);

                //
                // Create new rule object
                //
                Rule rule = getStyleFactory().createRule(symbolizerArray, standardData.description,
                        legendGraphics, standardData.name, filter, isElseFilter, maxScale,
                        minScale);

                SelectedSymbol.getInstance().replaceRule((org.geotools.styling.Rule) rule);

                this.fireUpdateSymbol();
            }
        }
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return fieldConfigManager;
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent() {
        return true;
    }

    /**
     * Gets the minimum value.
     *
     * @param field the field
     * @return the minimum value
     */
    private static double getMinimumValue(FieldConfigBase field) {
        double value = 0.0;

        if (field.isEnabled()) {
            FieldConfigString textFieldConfig = (FieldConfigString) field;

            value = ScaleUtil.extractValue(textFieldConfig.getStringValue());
        }

        return value;
    }

    /**
     * Gets the maximum value.
     *
     * @param field the field
     * @return the maximum value
     */
    private static double getMaximumValue(FieldConfigBase field) {
        double value = Double.POSITIVE_INFINITY;
        if (field.isEnabled()) {
            FieldConfigString textFieldConfig = (FieldConfigString) field;

            value = ScaleUtil.extractValue(textFieldConfig.getStringValue());
        }

        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        // No vendor options
    }

    /**
     * Edits the filter.
     *
     * @param filterFieldId the filter field id
     */
    protected void editFilter(FieldIdEnum filterFieldId) {
        FilterPanelInterface filterPanel = ExpressionPanelFactory.getFilterPanel(null);

        String panelTitle = Localisation.getString(RuleDetails.class, "RuleDetails.panelTitle");
        filterPanel.configure(panelTitle, Object.class,
                SelectedSymbol.getInstance().isRasterSymbol());

        Rule rule = SelectedSymbol.getInstance().getRule();
        if (rule != null) {
            filterPanel.populate(rule.getFilter());
        }

        if (filterPanel.showDialog()) {
            originalFilter = filterPanel.getFilter();
            fieldConfigVisitor.populateTextField(filterFieldId, filterPanel.getFilterString());

            updateSymbol();
        }
    }

    /**
     * Clear filter.
     */
    protected void clearFilter(FieldIdEnum filterFieldId) {
        originalFilter = null;
        fieldConfigVisitor.populateTextField(filterFieldId, "");

        updateSymbol();
    }
}
