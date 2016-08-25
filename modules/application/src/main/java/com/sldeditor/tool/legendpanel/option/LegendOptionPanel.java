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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.BasePanelPadding;
import com.sldeditor.ui.iface.ColourNotifyInterface;
import com.sldeditor.ui.widgets.ColourButton;

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

    /** The Constant FIELD_WIDTH_EXTENDED. */
    private static final int FIELD_WIDTH_EXTENDED = 150;

    /** The Constant PANEL_WIDTH. */
    private static final int PANEL_WIDTH = 250;

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

    /** The checkbox show title. */
    private JCheckBox chckboxShowTitles;

    /** The checkbox separate images. */
    private JCheckBox chckboxSeparateImages;

    /** The checkbox show border. */
    private JCheckBox chckboxShowBorder;

    /** The checkbox band information. */
    private JCheckBox chckboxBandInformation;

    /** The checkbox transparent. */
    private JCheckBox chckboxTransparent;

    /** The image size spinner. */
    private JSpinner imageSizeSpinner;

    /** The background colour button. */
    private ColourButton backgroundColourButton;

    /** The label font colour button. */
    private ColourButton labelFontColourButton;

    /** The border colour button. */
    private ColourButton borderColourButton;

    private JLabel labelFontName;

    private JLabel labelFontSize;

    private JLabel labelFontStyle;

    /**
     * Instantiates a new legend option panel.
     */
    public LegendOptionPanel() {
        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(5, 10, 5, 10) );//adds margin to panel

        Box box = Box.createVerticalBox();

        JPanel widthPanel = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.width"));
        box.add(widthPanel);

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
        box.add(heightPanel);

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
        box.add(panelMaintainAspect);

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
        box.add(panelDPI);

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
        box.add(panelAntiAlias);

        chckboxAntiAlias = new JCheckBox("");
        chckboxAntiAlias.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxAntiAlias.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setFontAntiAliasing(chckboxAntiAlias.isSelected());
                notifyListeners();
            }
        });
        panelAntiAlias.add(chckboxAntiAlias);

        //
        // Show Labels
        //
        JPanel panelShowLabels = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.showLabels"));
        box.add(panelShowLabels);

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
        // Show Titles
        //
        JPanel panelShowTitles = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.showTitles"));
        box.add(panelShowTitles);

        chckboxShowTitles = new JCheckBox("");
        chckboxShowTitles.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxShowTitles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setShowTitle(chckboxShowTitles.isSelected());
                notifyListeners();
            }
        });
        panelShowTitles.add(chckboxShowTitles);

        //
        // Image size
        //
        JPanel panelImageSize = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.imageSize"));
        box.add(panelImageSize);

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
        box.add(panelSeparateImages);

        chckboxSeparateImages = new JCheckBox("");
        chckboxSeparateImages.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxSeparateImages.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setSplitSymbolizers(chckboxSeparateImages.isSelected());
                notifyListeners();
            }
        });
        panelSeparateImages.add(chckboxSeparateImages);

        //
        // Transparent
        //
        JPanel panelTransparent = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.transparent"));
        box.add(panelTransparent);

        chckboxTransparent = new JCheckBox("");
        chckboxTransparent.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxTransparent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setTransparent(chckboxTransparent.isSelected());

                updateFieldStates();

                notifyListeners();
            }
        });
        panelTransparent.add(chckboxTransparent);

        //
        // Background colour
        //
        JPanel panelBackgroundColour = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.backgroundColour"));
        box.add(panelBackgroundColour);

        backgroundColourButton = new ColourButton();
        backgroundColourButton.setBounds(FIELD_X, 0, FIELD_WIDTH, BasePanel.WIDGET_HEIGHT);
        panelBackgroundColour.add(backgroundColourButton);
        backgroundColourButton.registerObserver(new ColourNotifyInterface() {
            @Override
            public void notify(String colourString, double opacity) {

                Color newValueObj = backgroundColourButton.getColour();

                data.setBackgroundColour(newValueObj);
                notifyListeners();
            }
        });

        //
        // Label Font
        //
        JPanel panelLabelFont = new JPanel();
        panelLabelFont.setLayout(null);
        panelLabelFont.setPreferredSize(new Dimension(PANEL_WIDTH, BasePanel.WIDGET_HEIGHT * 3));

        box.add(panelLabelFont);

        JButton labelFont = new JButton(Localisation.getString(LegendOptionPanel.class, "LegendOptionPanel.labelFont"));
        labelFont.setBounds(LABEL_X, 0, FIELD_X - LABEL_X, BasePanel.WIDGET_HEIGHT);
        panelLabelFont.add(labelFont);

        labelFontName = new JLabel();
        labelFontName.setBounds(FIELD_X, 0, FIELD_WIDTH_EXTENDED, BasePanel.WIDGET_HEIGHT);
        panelLabelFont.add(labelFontName);

        labelFontStyle = new JLabel();
        labelFontStyle.setBounds(FIELD_X, BasePanel.WIDGET_HEIGHT, FIELD_WIDTH, BasePanel.WIDGET_HEIGHT);
        panelLabelFont.add(labelFontStyle);

        labelFontSize = new JLabel();
        labelFontSize.setBounds(FIELD_X, BasePanel.WIDGET_HEIGHT + BasePanel.WIDGET_HEIGHT, FIELD_WIDTH, BasePanel.WIDGET_HEIGHT);
        panelLabelFont.add(labelFontSize);

        labelFont.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                StyleBuilder sb = new StyleBuilder();
                Font existingFont = sb.createFont(data.getLabelFont());
                Font font = (Font) JFontChooser.showDialog(Controller.getInstance().getFrame(),
                        Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.labelFontTitle"), existingFont);
                if (font != null) {
                    java.awt.Font newFont = FontUtils.getFont(font);

                    populateLabelFontDetails(newFont);
                    data.setLabelFont(newFont);

                    notifyListeners();
                }
            }});

        //
        // Label font colour
        //
        JPanel panelLabelColour = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.labelFontColour"));
        box.add(panelLabelColour);

        labelFontColourButton = new ColourButton();
        labelFontColourButton.setBounds(FIELD_X, 0, FIELD_WIDTH, BasePanel.WIDGET_HEIGHT);
        panelLabelColour.add(labelFontColourButton);
        labelFontColourButton.registerObserver(new ColourNotifyInterface() {
            @Override
            public void notify(String colourString, double opacity) {

                Color newValueObj = labelFontColourButton.getColour();

                data.setLabelFontColour(newValueObj);
                notifyListeners();
            }
        });

        //
        // Show border
        //
        JPanel panelShowBorder = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.showBorder"));
        box.add(panelShowBorder);

        chckboxShowBorder = new JCheckBox("");
        chckboxShowBorder.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxShowBorder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setBorder(chckboxShowBorder.isSelected());
                updateFieldStates();
                notifyListeners();
            }
        });
        panelShowBorder.add(chckboxShowBorder);

        //
        // Border colour
        //
        JPanel panelBorderColour = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.borderColour"));
        box.add(panelBorderColour);

        borderColourButton = new ColourButton();
        borderColourButton.setBounds(FIELD_X, 0, FIELD_WIDTH, BasePanel.WIDGET_HEIGHT);
        panelBorderColour.add(borderColourButton);
        borderColourButton.registerObserver(new ColourNotifyInterface() {
            @Override
            public void notify(String colourString, double opacity) {

                Color newValueObj = borderColourButton.getColour();

                data.setBorderColour(newValueObj);
                notifyListeners();
            }
        });

        //
        // Show band information
        //
        JPanel panelShowBandInformation = createPanel(Localisation.getField(LegendOptionPanel.class, "LegendOptionPanel.bandInformation"));
        box.add(panelShowBandInformation);

        chckboxBandInformation = new JCheckBox("");
        chckboxBandInformation.setBounds(FIELD_CHECKBOX_X, 0, FIELD_WIDTH, PANEL_HEIGHT);
        chckboxBandInformation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                data.setBandInformation(chckboxBandInformation.isSelected());
                notifyListeners();
            }
        });
        panelShowBandInformation.add(chckboxBandInformation);

        BasePanelPadding padding = new BasePanelPadding(box);
        p.add(box, BorderLayout.CENTER);

        Dimension boxSize = padding.addPadding();

        JScrollPane scrollLegendOptionPanel = new JScrollPane(p);
        scrollLegendOptionPanel.setPreferredSize(boxSize);
        p.setAutoscrolls(true);

        this.add(scrollLegendOptionPanel, BorderLayout.CENTER);

        populate(data);
    }

    /**
     * Update field states.
     */
    private void updateFieldStates() {
        backgroundColourButton.setEnabled(!chckboxTransparent.isSelected());
        borderColourButton.setEnabled(chckboxShowBorder.isSelected());
    }

    /**
     * Populate data into fields.
     *
     * @param optionData the option data
     */
    private void populate(LegendOptionData optionData)
    {
        widthSpinner.setValue(Integer.valueOf(optionData.getImageWidth()));
        heightSpinner.setValue(Integer.valueOf(optionData.getImageHeight()));
        dpiSpinner.setValue(Integer.valueOf(optionData.getDpi()));
        imageSizeSpinner.setValue(Integer.valueOf(optionData.getImageSize()));

        chckboxMaintainAspect.setSelected(optionData.isMaintainAspectRatio());
        chckboxAntiAlias.setSelected(optionData.isFontAntiAliasing());
        chckboxShowLabels.setSelected(optionData.showLabels());
        chckboxShowTitles.setSelected(optionData.isShowTitle());
        chckboxSeparateImages.setSelected(optionData.splitSymbolizers());
        chckboxShowBorder.setSelected(optionData.isBorder());
        chckboxBandInformation.setSelected(optionData.isBandInformation());
        chckboxTransparent.setSelected(optionData.isTransparent());

        backgroundColourButton.populate(optionData.getBackgroundColour());
        labelFontColourButton.populate(optionData.getLabelFontColour());
        borderColourButton.populate(optionData.getBorderColour());

        updateFieldStates();
        populateLabelFontDetails(optionData.getLabelFont());
    }

    /**
     * Populate label font details.
     *
     * @param font the font
     */
    private void populateLabelFontDetails(java.awt.Font font) {
        labelFontName.setText(font.getFontName());
        String style;

        if(font.isBold() && font.isItalic())
        {
            style = "Bold, Italic";
        }
        else if(font.isBold())
        {
            style = "Bold";
        }
        else if(font.isItalic())
        {
            style = "Italic";
        }
        else
        {
            style = "Plain";
        }
        labelFontStyle.setText(style);
        labelFontSize.setText(String.valueOf(font.getSize()));
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
