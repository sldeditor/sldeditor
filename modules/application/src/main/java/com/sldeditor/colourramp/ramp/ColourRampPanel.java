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

import com.sldeditor.colourramp.ColourRamp;
import com.sldeditor.colourramp.ColourRampConfigPanel;
import com.sldeditor.colourramp.ColourRampPanelInterface;
import com.sldeditor.colourramp.ColourRampUpdateInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.utils.ColourUtils;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.XMLTwoColourRamp;
import com.sldeditor.common.xml.ui.XMLTwoColourRampList;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.detail.config.colourmap.ColourMapModel;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.opengis.filter.expression.Expression;

/**
 * The Class ColourRampPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampPanel implements ColourRampPanelInterface, UndoActionInterface {

    /** The panel. */
    private JPanel panel = null;

    /** The ramp combo box. */
    private ValueComboBox rampComboBox;

    /** The old colour ramp index. */
    private Integer oldColourRampIndex = 0;

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

    /** The reverse checkbox. */
    private JCheckBox reverseCheckbox;

    /** The is populating flag. */
    private boolean isPopulating = false;

    /**
     * Instantiates a new colour ramp panel.
     *
     * @param xmlTwoColourRampList the ramp data list
     * @param suppressUndoEvents the suppress undo events
     */
    public ColourRampPanel(XMLTwoColourRampList xmlTwoColourRampList, boolean suppressUndoEvents) {
        this.rampDataList = createColourRampList(xmlTwoColourRampList);

        createUI(suppressUndoEvents);
    }

    /** Creates the UI. */
    private void createUI(boolean suppressUndoEvents) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        createTopPanel(suppressUndoEvents);
        createFieldPanel(suppressUndoEvents);
    }

    /**
     * Creates the top panel.
     *
     * @param suppressUndoEvents the suppress undo events
     */
    private void createTopPanel(boolean suppressUndoEvents) {
        final UndoActionInterface undoObj = this;

        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(
                new Dimension(BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT));
        topPanel.setLayout(null);
        panel.add(topPanel, BorderLayout.NORTH);

        List<ValueComboBoxData> dataList = populateColourRamps(false);
        rampComboBox = new ValueComboBox();
        rampComboBox.initialiseSingle(dataList);
        rampComboBox.setBounds(BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_EXTENDED_WIDTH,
                BasePanel.WIDGET_HEIGHT);
        topPanel.add(rampComboBox);
        rampComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isPopulating() && (!suppressUndoEvents)) {
                    Integer newValueObj = rampComboBox.getSelectedIndex();

                    UndoManager.getInstance().addUndoEvent(new UndoEvent(undoObj,
                            FieldIdEnum.COLOUR_RAMP_COLOUR, oldColourRampIndex, newValueObj));

                    oldColourRampIndex = newValueObj;
                }
            }
        });

        reverseCheckbox = new JCheckBox(
                Localisation.getString(ColourRampConfigPanel.class, "ColourRampPanel.reverse"));
        reverseCheckbox.setBounds(rampComboBox.getX() + rampComboBox.getWidth() + 20, 0,
                BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);
        reverseCheckbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isSelected = reverseCheckbox.isSelected();

                if (!suppressUndoEvents) {
                    Boolean oldValueObj = Boolean.valueOf(!isSelected);
                    Boolean newValueObj = Boolean.valueOf(isSelected);

                    UndoManager.getInstance().addUndoEvent(new UndoEvent(undoObj,
                            FieldIdEnum.COLOUR_RAMP_REVERSE, oldValueObj, newValueObj));
                }
                reverseColourRamp(isSelected);
            }
        });
        topPanel.add(reverseCheckbox);
    }

    /**
     * Populate colour ramps.
     *
     * @param reverseColours the reverse colours
     * @return the list
     */
    private List<ValueComboBoxData> populateColourRamps(boolean reverseColours) {
        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();
        colourRampCache.clear();

        if (rampDataList != null) {
            for (ColourRamp data : rampDataList) {
                String key = data.toString();
                ValueComboBoxData valueData = new ValueComboBoxData(key,
                        data.getImageIcon(reverseColours), getClass());

                dataList.add(valueData);
                colourRampCache.put(key, data);
            }
        }

        return dataList;
    }

    /**
     * Creates the field panel.
     *
     * @param suppressUndoEvents the suppress undo events
     */
    private void createFieldPanel(boolean suppressUndoEvents) {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        panel.add(tablePanel, BorderLayout.CENTER);

        JPanel dataPanel = new JPanel();
        tablePanel.add(dataPanel, BorderLayout.NORTH);
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));

        minValueSpinner = new FieldConfigInteger(new FieldConfigCommonData(getClass(),
                FieldIdEnum.UNKNOWN,
                Localisation.getField(ColourRampConfigPanel.class, "ColourRampPanel.minValue"),
                true, suppressUndoEvents));
        minValueSpinner.createUI();
        FieldPanel fieldPanel = minValueSpinner.getPanel();
        dataPanel.add(fieldPanel);

        JButton resetValueButton = new JButton(
                Localisation.getString(ColourRampConfigPanel.class, "ColourRampPanel.reset"));
        minValueSpinner.addUI(resetValueButton, 20, BasePanel.WIDGET_BUTTON_WIDTH,
                BasePanel.WIDGET_HEIGHT);
        minValueSpinner.populateField(0);
        resetValueButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (parentObj != null) {
                    ColourMapModel model = parentObj.getColourMapModel();

                    populate(model.getColourMap());
                }
            }
        });
        maxValueSpinner = new FieldConfigInteger(new FieldConfigCommonData(getClass(),
                FieldIdEnum.UNKNOWN,
                Localisation.getField(ColourRampConfigPanel.class, "ColourRampPanel.maxValue"),
                true, suppressUndoEvents));
        maxValueSpinner.createUI();
        maxValueSpinner.populateField(100);
        dataPanel.add(maxValueSpinner.getPanel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setSize(BasePanel.FIELD_PANEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        JButton applyButton = new JButton(
                Localisation.getString(ColourRampConfigPanel.class, "common.apply"));
        applyButton.setPreferredSize(
                new Dimension(BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT));
        applyButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (parentObj != null) {
                    ColourRampData data = new ColourRampData();
                    data.setMaxValue(maxValueSpinner.getIntValue());
                    data.setMinValue(minValueSpinner.getIntValue());
                    data.setReverseColours(reverseCheckbox.isSelected());
                    ValueComboBoxData selectedItem = (ValueComboBoxData) rampComboBox
                            .getSelectedItem();

                    ColourRamp colourRamp = colourRampCache.get(selectedItem.getKey());
                    data.setColourRamp(colourRamp);

                    parentObj.colourRampUpdate(data);
                }
            }
        });
        buttonPanel.add(applyButton);
        dataPanel.add(buttonPanel);
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    /*
     * (non-Javadoc)
     *
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
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.colourramp.ColourRampPanelInterface#getPanel()
     */
    @Override
    public JPanel getPanel() {
        return panel;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.colourramp.ColourRampPanelInterface#populate(org.geotools.styling.ColorMap)
     */
    @Override
    public void populate(ColorMap value) {
        if (value != null) {
            ColorMapEntry[] entries = value.getColorMapEntries();
            if ((entries != null) && (entries.length > 0)) {
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
        if (spinner == null) {
            return;
        }

        if (colorMapEntry == null) {
            return;
        }

        int quantity = 1;

        Expression quantityExpression = colorMapEntry.getQuantity();

        if (quantityExpression != null) {
            Object quantityValue = ((LiteralExpressionImpl) quantityExpression).getValue();
            if (quantityValue instanceof Integer) {
                quantity = ((Integer) quantityValue).intValue();
            } else if (quantityValue instanceof Double) {
                quantity = ((Double) quantityValue).intValue();
            } else if (quantityValue instanceof String) {
                quantity = Double.valueOf((String) quantityValue).intValue();
            }
        }

        spinner.populateField(quantity);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.colourramp.ColourRampPanelInterface#setParent(com.sldeditor.colourramp.
     * ColourRampUpdateInterface)
     */
    @Override
    public void setParent(ColourRampUpdateInterface parent) {
        this.parentObj = parent;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.undo.UndoActionInterface#undoAction(com.sldeditor.common.undo.
     * UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            switch (undoRedoObject.getFieldId()) {
            case COLOUR_RAMP_COLOUR: {
                int oldValue = ((Integer) undoRedoObject.getOldValue()).intValue();

                rampComboBox.setSelectedIndex(oldValue);
            }
                break;
            case COLOUR_RAMP_REVERSE: {
                Boolean oldValueObj = (Boolean) undoRedoObject.getOldValue();

                reverseCheckbox.setSelected(oldValueObj.booleanValue());
                reverseColourRamp(oldValueObj.booleanValue());
            }
                break;
            default:
                break;
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.undo.UndoActionInterface#redoAction(com.sldeditor.common.undo.
     * UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            switch (undoRedoObject.getFieldId()) {
            case COLOUR_RAMP_COLOUR: {
                int newValue = ((Integer) undoRedoObject.getNewValue()).intValue();

                rampComboBox.setSelectedIndex(newValue);
            }
                break;
            case COLOUR_RAMP_REVERSE: {
                Boolean newValueObj = (Boolean) undoRedoObject.getNewValue();

                reverseCheckbox.setSelected(newValueObj.booleanValue());
                reverseColourRamp(newValueObj.booleanValue());
            }
                break;
            default:
                break;
            }
        }
    }

    /**
     * Checks if the panel is being populated.
     *
     * @return the isPopulating
     */
    private boolean isPopulating() {
        return isPopulating;
    }

    /**
     * Sets the populating flag.
     *
     * @param isPopulating the isPopulating to set
     */
    private void setPopulating(boolean isPopulating) {
        this.isPopulating = isPopulating;
    }

    /**
     * Reverse colour ramp.
     *
     * @param isSelected the is selected
     */
    private void reverseColourRamp(boolean isSelected) {
        int selectedIndex = rampComboBox.getSelectedIndex();

        setPopulating(true);
        List<ValueComboBoxData> dataList = populateColourRamps(isSelected);
        rampComboBox.initialiseSingle(dataList);

        rampComboBox.setSelectedIndex(selectedIndex);
        setPopulating(false);
    }

    /**
     * Gets the colour ramp list.
     *
     * @return the colour ramp list
     */
    @Override
    public List<ColourRamp> getColourRampList() {
        return rampDataList;
    }

    /**
     * Creates the colour ramp list.
     *
     * @param xmlTwoColourRampList the xml two colour ramp list
     * @return the list
     */
    private List<ColourRamp> createColourRampList(XMLTwoColourRampList xmlTwoColourRampList) {
        List<ColourRamp> colourRampList = new ArrayList<ColourRamp>();

        for (XMLTwoColourRamp ramp : xmlTwoColourRampList.getTwoColourRamp()) {
            ColourRamp colourRamp = new ColourRamp();

            Color startColour = ColourUtils.toColour(ramp.getStart());
            Color endColour = ColourUtils.toColour(ramp.getEnd());
            colourRamp.setColourRamp(startColour, endColour);

            colourRampList.add(colourRamp);
        }
        return colourRampList;
    }
}
