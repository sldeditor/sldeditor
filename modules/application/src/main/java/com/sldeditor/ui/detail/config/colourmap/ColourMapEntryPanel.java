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

package com.sldeditor.ui.detail.config.colourmap;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigDouble;
import com.sldeditor.ui.detail.config.FieldConfigSlider;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import org.geotools.styling.ColorMapEntry;

/**
 * The Class ColourMapEntryPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ColourMapEntryPanel extends JPanel implements UpdateSymbolInterface {

    /** The Constant NO_OF_PADDING_ROWS. */
    private static final int NO_OF_PADDING_ROWS = 2;

    /** The Constant NO_OF_ROWS. */
    private static final int NO_OF_ROWS = 5 + NO_OF_PADDING_ROWS;

    /** The Constant panelHeight. */
    private static final int panelHeight = NO_OF_ROWS * BasePanel.WIDGET_HEIGHT;

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The label. */
    private FieldConfigString label;

    /** The colour. */
    private FieldConfigColour colour;

    /** The opacity. */
    private FieldConfigSlider opacity;

    /** The quantity. */
    private FieldConfigDouble quantity;

    /** The apply button. */
    private JButton applyButton;

    /** The cancel button. */
    private JButton cancelButton;

    /** The parent obj. */
    private ColourMapEntryUpdateInterface parentObj;

    /** The field list. */
    List<FieldConfigBase> fieldList = new ArrayList<FieldConfigBase>();

    /**
     * Instantiates a new colour map entry panel.
     *
     * @param panelId the panel id
     * @param parent the parent
     */
    public ColourMapEntryPanel(Class<?> panelId, ColourMapEntryUpdateInterface parent) {
        this.parentObj = parent;
        createUI(panelId);
    }

    /**
     * Creates the UI.
     *
     * @param panelId the panel id
     */
    private void createUI(Class<?> panelId) {

        TitledBorder title =
                BorderFactory.createTitledBorder(
                        Localisation.getString(FieldConfigBase.class, "ColourMapEntryPanel.title"));
        setBorder(title);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        label =
                new FieldConfigString(
                        new FieldConfigCommonData(
                                panelId,
                                FieldIdEnum.RASTER_COLOURMAP_ENTRY_LABEL,
                                Localisation.getField(
                                        FieldConfigBase.class, "ColourMapEntryPanel.label"),
                                true,
                                true,
                                true),
                        null);
        label.createUI();
        label.addDataChangedListener(this);
        fieldList.add(label);
        add(label.getPanel());

        colour =
                new FieldConfigColour(
                        new FieldConfigCommonData(
                                panelId,
                                FieldIdEnum.RASTER_COLOURMAP_ENTRY_COLOUR,
                                Localisation.getField(
                                        FieldConfigBase.class, "ColourMapEntryPanel.colour"),
                                false,
                                true,
                                true));
        colour.createUI();
        colour.addDataChangedListener(this);
        fieldList.add(colour);
        add(colour.getPanel());

        opacity =
                new FieldConfigSlider(
                        new FieldConfigCommonData(
                                panelId,
                                FieldIdEnum.RASTER_COLOURMAP_ENTRY_OPACITY,
                                Localisation.getField(
                                        FieldConfigBase.class, "ColourMapEntryPanel.opacity"),
                                false,
                                true,
                                true));
        opacity.createUI();
        opacity.addDataChangedListener(this);
        fieldList.add(opacity);
        add(opacity.getPanel());

        quantity =
                new FieldConfigDouble(
                        new FieldConfigCommonData(
                                panelId,
                                FieldIdEnum.RASTER_COLOURMAP_ENTRY_QUANTITY,
                                Localisation.getField(
                                        FieldConfigBase.class, "ColourMapEntryPanel.quantity"),
                                false,
                                true,
                                true));
        quantity.createUI();
        quantity.addDataChangedListener(this);
        fieldList.add(quantity);
        add(quantity.getPanel());

        JPanel buttonPanel = new JPanel();
        //
        // Apply button
        //
        applyButton = new JButton(Localisation.getString(FieldConfigBase.class, "common.apply"));
        applyButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (parentObj != null) {
                            ColourMapData data = new ColourMapData();
                            data.setLabel(label.getStringValue());
                            data.setColour(colour.getColourExpression());
                            data.setOpacity(opacity.getExpression());
                            data.setQuantity(quantity.getExpression());

                            parentObj.colourMapEntryUpdated(data);
                        }
                    }
                });
        buttonPanel.add(applyButton);

        //
        // Cancel button
        //
        cancelButton = new JButton(Localisation.getString(FieldConfigBase.class, "common.cancel"));
        cancelButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setVisible(false);
                    }
                });
        buttonPanel.add(cancelButton);

        add(buttonPanel);
    }

    /**
     * Gets the panel height.
     *
     * @return the panel height
     */
    public int getPanelHeight() {
        return panelHeight;
    }

    /**
     * Gets the no of rows.
     *
     * @return the noOfRows
     */
    public static int getNoOfRows() {
        return NO_OF_ROWS;
    }

    /**
     * Sets the selected entry.
     *
     * @param entries the new selected entry
     */
    public void setSelectedEntry(List<ColorMapEntry> entries) {
        if (entries == null) {
            setVisible(false);

        } else if (entries.size() == 1) {
            setVisible(true);
            ColorMapEntry entry = entries.get(0);
            label.populateField(entry.getLabel());
            opacity.populate(entry.getOpacity());
            colour.populate(entry.getColor());
            quantity.populate(entry.getQuantity());

            for (FieldConfigBase field : fieldList) {
                field.showOptionField(false);
            }

            applyButton.setEnabled(false);
        } else if (entries.size() > 1) {
            setVisible(true);

            MultipleColourMapEntry multipleValues = new MultipleColourMapEntry();
            multipleValues.parseList(entries);
            ColorMapEntry entry = multipleValues.getColourMapEntry();

            // Label
            String labelValue = "";

            if (entry.getLabel() != null) {
                labelValue = entry.getLabel();
            }
            label.populateField(labelValue);
            label.setOptionFieldValue(entry.getLabel() != null);

            // Opacity
            opacity.populate(entry.getOpacity());
            opacity.setOptionFieldValue(entry.getOpacity() != null);

            // Colour
            colour.populate(entry.getColor());
            colour.setOptionFieldValue(entry.getColor() != null);

            // Quantity
            quantity.populate(entry.getQuantity());
            quantity.setOptionFieldValue(entry.getQuantity() != null);

            for (FieldConfigBase field : fieldList) {
                field.showOptionField(true);
            }
            applyButton.setEnabled(false);
        } else {
            setVisible(false);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        applyButton.setEnabled(true);
    }
}
