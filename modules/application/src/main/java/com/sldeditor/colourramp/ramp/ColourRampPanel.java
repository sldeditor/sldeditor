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

package com.sldeditor.colourramp.ramp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.opengis.filter.expression.Expression;

import com.sldeditor.colourramp.ColourRamp;
import com.sldeditor.colourramp.ColourRampConfigPanel;
import com.sldeditor.colourramp.ColourRampPanelInterface;
import com.sldeditor.colourramp.ColourRampUpdateInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.detail.config.colourmap.ColourMapModel;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class ColourRampPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampPanel implements ColourRampPanelInterface {

    /** The panel. */
    private JPanel panel = null;

    /** The ramp combo box. */
    private ValueComboBox rampComboBox;

    /** The data list. */
    private List<ColourRamp> rampDataList = null;

    /** The maximum value spinner. */
    private FieldConfigInteger maxValueSpinner;

    /** The minimum value spinner. */
    private FieldConfigInteger minValueSpinner;

    /** The parent obj. */
    private ColourRampUpdateInterface parentObj = null;

    /** The colour ramp cache. */
    private Map<String, ColourRamp> colourRampCache = new HashMap<String, ColourRamp>();

    /**
     * Instantiates a new colour ramp panel.
     *
     * @param rampDataList the ramp data list
     */
    public ColourRampPanel(List<ColourRamp> rampDataList)
    {
        this.rampDataList = rampDataList;

        createUI();
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        createTopPanel();
        createFieldPanel();
    }

    /**
     * Creates the top panel.
     */
    private void createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT));
        topPanel.setLayout(null);
        panel.add(topPanel, BorderLayout.NORTH);

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();

        if(rampDataList != null)
        {
            for(ColourRamp data : rampDataList)
            {
                String key = data.toString();
                ValueComboBoxData valueData = new ValueComboBoxData(key, data.getImageIcon(), getClass());

                dataList.add(valueData);
                colourRampCache.put(key, data);
            }
        }
        rampComboBox = new ValueComboBox();
        rampComboBox.initialiseSingle(dataList);
        rampComboBox.setBounds(BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_EXTENDED_WIDTH, BasePanel.WIDGET_HEIGHT);
        topPanel.add(rampComboBox);
        rampComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            }});
    }

    /**
     * Creates the field panel.
     */
    private void createFieldPanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        panel.add(tablePanel, BorderLayout.CENTER);

        JPanel dataPanel = new JPanel();
        tablePanel.add(dataPanel, BorderLayout.NORTH);
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        minValueSpinner = new FieldConfigInteger(getClass(), new FieldId(FieldIdEnum.UNKNOWN),
                Localisation.getField(ColourRampConfigPanel.class, "ColourRampPanel.minValue"), true);
        minValueSpinner.createUI();
        FieldPanel fieldPanel = minValueSpinner.getPanel();
        dataPanel.add(fieldPanel);

        JButton resetValueButton = new JButton(Localisation.getString(ColourRampConfigPanel.class, "ColourRampPanel.reset"));
        minValueSpinner.addUI(resetValueButton, 20,BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        resetValueButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(parentObj != null)
                {
                    ColourMapModel model = parentObj.getColourMapModel();

                    populate(model.getColourMap());
                }
            }});
        maxValueSpinner = new FieldConfigInteger(getClass(), new FieldId(FieldIdEnum.UNKNOWN),
                Localisation.getField(ColourRampConfigPanel.class, "ColourRampPanel.maxValue"), true);
        maxValueSpinner.createUI();
        dataPanel.add(maxValueSpinner.getPanel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setSize(BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        JButton applyButton = new JButton(Localisation.getString(ColourRampConfigPanel.class, "common.apply"));
        applyButton.setPreferredSize(new Dimension(BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT));
        applyButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                if(parentObj != null)
                {
                    ColourRampData data = new ColourRampData();
                    data.setMaxValue(maxValueSpinner.getIntValue());
                    data.setMinValue(minValueSpinner.getIntValue());
                    ValueComboBoxData selectedItem = (ValueComboBoxData) rampComboBox.getSelectedItem();

                    ColourRamp colourRamp = colourRampCache.get(selectedItem.getKey());
                    data.setColourRamp(colourRamp);

                    parentObj.colourRampUpdate(data);
                }
            }});
        buttonPanel.add(applyButton);
        dataPanel.add(buttonPanel);
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    /* (non-Javadoc)
     * @see com.sldeditor.colourramp.ColourRampPanelInterface#getTitle()
     */
    @Override
    public String getTitle() {
        return Localisation.getString(ColourRampConfigPanel.class, "ColourRampPanel.title");
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    /* (non-Javadoc)
     * @see com.sldeditor.colourramp.ColourRampPanelInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.colourramp.ColourRampPanelInterface#populate(org.geotools.styling.ColorMap)
     */
    @Override
    public void populate(ColorMap value) {
        if(value != null)
        {
            ColorMapEntry[] entries = value.getColorMapEntries();
            if((entries != null) && (entries.length > 0))
            {
                populateValue(minValueSpinner, entries[0]);
                populateValue(maxValueSpinner, entries[entries.length - 1]);
            }
        }

    }

    /**
     * Populate value.
     *
     * @param spinner the spinner
     * @param colorMapEntry the color map entry
     */
    private void populateValue(FieldConfigInteger spinner, ColorMapEntry colorMapEntry) {
        if(spinner == null)
        {
            return;
        }

        if(colorMapEntry == null)
        {
            return;
        }

        int quantity = 1;

        Expression quantityExpression = colorMapEntry.getQuantity();

        if(quantityExpression != null)
        {
            Object quantityValue = ((LiteralExpressionImpl)quantityExpression).getValue();
            if(quantityValue instanceof Integer)
            {
                quantity = ((Integer) quantityValue).intValue();
            }
            else if(quantityValue instanceof Double)
            {
                quantity = ((Double) quantityValue).intValue();
            }
            else if(quantityValue instanceof String)
            {
                quantity = Integer.valueOf((String) quantityValue);
            }
        }

        spinner.populateField(quantity);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.colourramp.ColourRampPanelInterface#setParent(com.sldeditor.colourramp.ColourRampUpdateInterface)
     */
    @Override
    public void setParent(ColourRampUpdateInterface parent) {
        this.parentObj = parent;
    }

}
