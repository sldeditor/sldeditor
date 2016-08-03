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

package com.sldeditor.colourramp;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import org.geotools.styling.ColorMap;

import com.sldeditor.colourramp.ramp.ColourRampData;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.colourmap.ColourMapModel;
import com.sldeditor.ui.detail.config.colourmap.ColourMapModelUpdateInterface;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class ColourRampConfigPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourRampConfigPanel extends JPanel implements ColourRampUpdateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The colour ramp map. */
    private Map<ColourRampPanelInterface, List<ColourRamp>> colourRampMap = null;

    /** The type combo box. */
    private ValueComboBox typeComboBox;

    /** The edit panel. */
    private JPanel editPanel;

    /** The colour ramp map cache. */
    private Map<String, ColourRampPanelInterface> colourRampMapCache = new HashMap<String, ColourRampPanelInterface>();

    /** The model. */
    private ColourMapModel colourMapModel = null;

    /** The parent obj. */
    private ColourMapModelUpdateInterface parentObj = null;

    /**
     * Instantiates a new colour ramp dialog.
     *
     * @param parent the parent
     * @param model the model
     */
    public ColourRampConfigPanel(ColourMapModelUpdateInterface parent, ColourMapModel model)
    {
        parentObj = parent;
        colourMapModel = model;

        createUI();
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        setLayout(new BorderLayout());

        colourRampMap = ColourRampFactory.getColourRampMap();

        createTopPanel();
        createEditPanel();
    }

    /**
     * Creates the edit panel.
     */
    private void createEditPanel() {
        editPanel = new JPanel();
        add(editPanel, BorderLayout.CENTER);

        CardLayout cardlayout = new CardLayout();
        editPanel.setLayout(cardlayout);

        if(colourRampMap != null)
        {
            for(ColourRampPanelInterface data : colourRampMap.keySet())
            {
                data.setParent(this);
                String key = data.getTitle();

                editPanel.add(data.getPanel(), key);
            }
        }
    }

    /**
     * Creates the top panel.
     */
    private void createTopPanel() {
        JPanel topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();

        if(colourRampMap != null)
        {
            for(ColourRampPanelInterface data : colourRampMap.keySet())
            {
                Class<?> panel = null;

                if(data.getPanel() != null)
                {
                    panel = data.getPanel().getClass();
                }

                String key = data.getTitle();
                ValueComboBoxData valueData = new ValueComboBoxData(key, data.getTitle(), panel);

                dataList.add(valueData);

                colourRampMapCache.put(key, data);
            }
        }
        typeComboBox = new ValueComboBox();
        typeComboBox.initialiseSingle(dataList);
        typeComboBox.setPreferredSize(new Dimension(BasePanel.WIDGET_EXTENDED_WIDTH, BasePanel.WIDGET_HEIGHT));
        topPanel.add(typeComboBox);
        typeComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                typeChanged(typeComboBox.getSelectedValue());
            }});
    }

    /**
     * Type changed.
     *
     * @param valueComboBoxData the value combo box data
     */
    private void typeChanged(ValueComboBoxData valueComboBoxData) {
        if(valueComboBoxData != null)
        {
            CardLayout cl = (CardLayout)(editPanel.getLayout());
            cl.show(editPanel, valueComboBoxData.getKey());
        }
    }

    /**
     * Populate the configuration fields.
     *
     * @param value the value
     */
    public void populate(ColorMap value) {

        ValueComboBoxData selectedValue = typeComboBox.getSelectedValue();
        if(selectedValue != null)
        {
            ColourRampPanelInterface selectedPanel = this.colourRampMapCache.get(selectedValue.getKey());

            if(selectedPanel != null)
            {
                selectedPanel.populate(value);
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.colourramp.ColourRampUpdateInterface#colourRampUpdate(com.sldeditor.colourramp.ramp.ColourRampData)
     */
    @Override
    public void colourRampUpdate(ColourRampData data) {
        if(colourMapModel != null)
        {
            colourMapModel.updateColourRamp(data);
            
            if(parentObj != null)
            {
                parentObj.colourMapUpdated();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.colourramp.ColourRampUpdateInterface#getColourMapModel()
     */
    @Override
    public ColourMapModel getColourMapModel() {
        return colourMapModel;
    }

}
