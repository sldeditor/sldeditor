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
package com.sldeditor.tool.legendpanel.option;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sldeditor.common.localisation.Localisation;

/**
 * Panel that allows the user to configure legend option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class LegendOptionPanel extends JPanel
{
    /** The Constant LABEL_X. */
    private static final int LABEL_X = 2;
    
    /** The Constant FIELD_X. */
    private static final int FIELD_X = 100;

    /** The Constant FIELD_X2. */
    private static final int FIELD_CHECKBOX_X = 100;

    /** The Constant LABEL_WIDTH. */
    private static final int LABEL_WIDTH = 95;
    
    /** The Constant FIELD_WIDTH. */
    private static final int FIELD_WIDTH = 50;

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 150;
    
    /** The Constant PANEL_HEIGHT. */
    private static final int PANEL_HEIGHT = 20;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The listener list. */
    private List<LegendOptionDataUpdateInterface> listenerList = new ArrayList<LegendOptionDataUpdateInterface>();

    /** The data. */
    private LegendOptionData data = new LegendOptionData();

    /** The spinner dpi. */
    private JSpinner dpiSpinner;

    /** The checkbox maintain aspect. */
    private JCheckBox chckboxMaintainAspect;

    /** The height spinner. */
    private JSpinner heightSpinner;

    /** The width spinner. */
    private JSpinner widthSpinner;
    
    /** The checkbox anti alias. */
    private JCheckBox chckboxAntiAlias;

    /** The checkbox show labels. */
    private JCheckBox chckboxShowLabels;
    
    /** The chckbox separate images. */
    private JCheckBox chckboxSeparateImages;

    /** The image size spinner. */
    private JSpinner imageSizeSpinner;
    
    /**
     * Instantiates a new legend option panel.
     */
    public LegendOptionPanel() {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        JPanel widthPanel = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.width"));
        add(widthPanel);

        heightSpinner = new JSpinner();
        heightSpinner.setBounds(FIELD_X, 0, FIELD_WIDTH, PANEL_HEIGHT);

        //
        // Width field
        //
        
        widthSpinner = new JSpinner();
        widthSpinner.setBounds(FIELD_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        widthSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        widthPanel.add(widthSpinner);
        widthSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Integer imageWidth = (Integer)widthSpinner.getValue();

                data.setImageWidth(imageWidth);
                if(data.isMaintainAspectRatio())
                {
                    heightSpinner.setValue(imageWidth);
                }
                notifyListeners();
            }
        });

        //
        // Height field
        //
        JPanel heightPanel = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.height"));
        add(heightPanel);

        heightSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        heightPanel.add(heightSpinner);
        heightSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Integer imageHeight = (Integer)heightSpinner.getValue();

                data.setImageHeight(imageHeight);

                if(data.isMaintainAspectRatio())
                {
                    widthSpinner.setValue(imageHeight);
                }
                notifyListeners();
            }
        });

        JPanel panelMaintainAspect = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.linked"));
        add(panelMaintainAspect);

        chckboxMaintainAspect = new JCheckBox("");
        chckboxMaintainAspect.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxMaintainAspect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setMaintainAspectRatio(chckboxMaintainAspect.isSelected());
            }
        });
        panelMaintainAspect.add(chckboxMaintainAspect);

        //
        // DPI field
        //
        JPanel panelDPI = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.dpi"));
        add(panelDPI);

        dpiSpinner = new JSpinner();
        dpiSpinner.setBounds(FIELD_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        dpiSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
        panelDPI.add(dpiSpinner);
        dpiSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Integer dpi = (Integer)dpiSpinner.getValue();

                data.setDpi(dpi);

                notifyListeners();
            }
        });
        
        //
        // Anti alias
        //
        JPanel panelAntiAlias = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.antiAlias"));
        add(panelAntiAlias);

        chckboxAntiAlias = new JCheckBox("");
        chckboxAntiAlias.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxAntiAlias.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setAntiAlias(chckboxAntiAlias.isSelected());
                notifyListeners();
            }
        });
        panelAntiAlias.add(chckboxAntiAlias);

        //
        // Show Labels
        //
        JPanel panelShowLabels = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.showLabels"));
        add(panelShowLabels);

        chckboxShowLabels = new JCheckBox("");
        chckboxShowLabels.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxShowLabels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setShowLabels(chckboxShowLabels.isSelected());
                notifyListeners();
            }
        });
        panelShowLabels.add(chckboxShowLabels);
        
        //
        // Image size
        //
        JPanel panelImageSize = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.imageSize"));
        add(panelImageSize);

        imageSizeSpinner = new JSpinner();
        imageSizeSpinner.setBounds(FIELD_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        
        panelImageSize.add(imageSizeSpinner);
        imageSizeSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                Integer imageSize = (Integer)imageSizeSpinner.getValue();

                data.setImageSize(imageSize);

                notifyListeners();
            }
        });

        //
        // Separate images
        //
        JPanel panelSeparateImages = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.separate"));
        add(panelSeparateImages);

        chckboxSeparateImages = new JCheckBox("");
        chckboxSeparateImages.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxSeparateImages.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setSplitSymbolizers(chckboxSeparateImages.isSelected());
                notifyListeners();
            }
        });
        panelSeparateImages.add(chckboxSeparateImages);
        
        populate(data);
    }

    /**
     * Populate data into fields.
     *
     * @param optionData the option data
     */
    private void populate(LegendOptionData optionData)
    {
        chckboxMaintainAspect.setSelected(optionData.isMaintainAspectRatio());
        
        widthSpinner.setValue(Integer.valueOf(optionData.getImageWidth()));
        heightSpinner.setValue(Integer.valueOf(optionData.getImageHeight()));
        dpiSpinner.setValue(Integer.valueOf(optionData.getDpi()));
        
        chckboxAntiAlias.setSelected(optionData.isAntiAlias());
        chckboxShowLabels.setSelected(optionData.showLabels());
        chckboxSeparateImages.setSelected(optionData.splitSymbolizers());
        
        imageSizeSpinner.setValue(Integer.valueOf(optionData.getImageSize()));
    }

    /**
     * Creates the panel.
     *
     * @param label the label
     * @return the j panel
     */
    private JPanel createPanel(String label)
    {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        if(label != null)
        {
            JLabel lbl = new JLabel(label);
            lbl.setBounds(LABEL_X, 0, LABEL_WIDTH, PANEL_HEIGHT);
            panel.add(lbl);
        }

        return panel;
    }

    /**
     * Notify listeners.
     */
    private void notifyListeners()
    {
        for(LegendOptionDataUpdateInterface listener : listenerList)
        {
            listener.updateLegendOptionData(data);
        }
    }

    /**
     * Adds the listener.
     *
     * @param listener the listener
     */
    public void addListener(LegendOptionDataUpdateInterface listener)
    {
        if(listener != null)
        {
            listenerList.add(listener);
            listener.registerPanel(this);
        }
    }
    
    /**
     * Gets the legend option data.
     *
     * @return the data
     */
    public LegendOptionData getData()
    {
        return data;        
    }

    /**
     * Update data.
     *
     * @param newOptionData the new option data
     */
    public void updateData(LegendOptionData newOptionData)
    {
        data = newOptionData;
        
        populate(newOptionData);
    }
}
