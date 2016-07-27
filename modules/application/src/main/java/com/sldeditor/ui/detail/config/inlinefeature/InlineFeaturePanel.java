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

package com.sldeditor.ui.detail.config.inlinefeature;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.geotools.styling.UserLayer;

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class InlineFeaturePanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineFeaturePanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private InlineFeatureUpdateInterface parentObj = null;

    /** The crs combo box. */
    private ValueComboBox crsComboBox;

    /** The feature table. */
    private JTable featureTable;

    /** The model. */
    private InLineFeatureModel model;

    /**
     * Instantiates a new inline feature panel.
     *
     * @param parent the parent
     * @param noOfRows the no of rows
     */
    public InlineFeaturePanel(InlineFeatureUpdateInterface parent, int noOfRows)
    {
        this.parentObj = parent;

        model = new InLineFeatureModel(parent);

        createUI(noOfRows);
    }

    /**
     * Creates the UI.
     *
     * @param noOfRows the no of rows
     */
    private void createUI(int noOfRows) {
        setLayout(new BorderLayout());

        int xPos = 0;
        int width = BasePanel.FIELD_PANEL_WIDTH - xPos - 20;
        int height = BasePanel.WIDGET_HEIGHT * (noOfRows - 1);
        this.setBounds(0, 0, width, height);

        // CRS dropdown
        JPanel topPanel = new JPanel();
        topPanel.add(createCRSList("CRS", xPos, topPanel));

        add(topPanel, BorderLayout.NORTH);

        // Feature table panel
        JPanel tablePanel = new JPanel();
        featureTable = new JTable(model);
        featureTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        featureTable.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(noOfRows - 2));
        
        JScrollPane scrollPanel = new JScrollPane(featureTable);
        scrollPanel.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(noOfRows - 2));

        tablePanel.add(scrollPanel);
        add(tablePanel, BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel();
        
        JButton addButton = new JButton("Add");
        addButton.setBounds(xPos, 0, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        bottomPanel.add(addButton);

        JButton removeButton = new JButton("Remove");
        removeButton.setBounds(xPos + addButton.getWidth() + 5, 0, BasePanel.WIDGET_BUTTON_WIDTH, BasePanel.WIDGET_HEIGHT);
        bottomPanel.add(removeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates the crs list.
     *
     * @param label the label
     * @param xPos the x pos
     * @param parentPanel the parent panel
     * @return the value combo box
     */
    private ValueComboBox createCRSList(String label, int xPos, JPanel parentPanel) {
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.TRAILING);
        lbl.setBounds(xPos, 0, BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        parentPanel.add(lbl);

        // Populate data list
        List<ValueComboBoxData> crsDataList = CoordManager.getInstance().getCRSList();
        crsComboBox = new ValueComboBox();
        crsComboBox.initialiseSingle(crsDataList);
        crsComboBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_EXTENDED_WIDTH, BasePanel.WIDGET_HEIGHT);
        parentPanel.add(crsComboBox);
        crsComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(parentObj != null)
                {
                    parentObj.inlineFeatureUpdated();
                }
            }});
        return crsComboBox;
    }

    /**
     * Gets the row y.
     *
     * @param row the row
     * @return the row y
     */
    private static int getRowY(int row)
    {
        return BasePanel.WIDGET_HEIGHT * row;
    }

    /**
     * Sets the inline features.
     *
     * @param userLayer the user layer
     */
    public void setInlineFeatures(UserLayer userLayer)
    {
        if(userLayer != null)
        {
            model.populate(userLayer);
        }
    }
}
