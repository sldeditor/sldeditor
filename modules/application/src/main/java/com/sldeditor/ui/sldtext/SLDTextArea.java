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

package com.sldeditor.ui.sldtext;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.datasource.SLDEditorDataUpdateInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.render.RenderPanelFactory;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Text area component that displays the SLD as xml.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTextArea implements SLDOutputInterface, SLDEditorDataUpdateInterface {
    /** The sld source text area. */
    private JTextArea sldSourceTextArea;

    /** The instance. */
    private static SLDTextArea instance = null;

    /** The combo box. */
    private ValueComboBox comboBox;

    /** The output format. */
    private SLDOutputFormatEnum outputFormat = SLDOutputFormatEnum.SLD;

    /** The displayed sld. */
    private StyledLayerDescriptor displayedSld = null;

    /** The resource locator. */
    private URL resourceLocator = null;

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public static JPanel getPanel() {
        if (instance == null) {
            instance = new SLDTextArea();
        }

        return instance.makeTextAreaPanel();
    }

    /** Instantiates a new SLD text area. */
    private SLDTextArea() {
        // Listen for changes in the SLD
        RenderPanelFactory.addSLDOutputListener(this);

        SLDEditorFile.getInstance().addSLDEditorFileUpdateListener(this);
    }

    /**
     * Make text area panel.
     *
     * @return the j component
     */
    private JPanel makeTextAreaPanel() {

        JPanel sldSourcePanel = new JPanel(false);
        sldSourcePanel.setLayout(new BorderLayout());

        List<ValueComboBoxData> dataList = new ArrayList<ValueComboBoxData>();

        dataList.add(
                new ValueComboBoxData(
                        SLDOutputFormatEnum.SLD.name(),
                        "SLD",
                        VendorOptionManager.getInstance().getDefaultVendorOptionVersion()));
        dataList.add(
                new ValueComboBoxData(
                        SLDOutputFormatEnum.YSLD.name(),
                        "YSLD",
                        VendorOptionManager.getInstance().getDefaultVendorOptionVersion()));

        /*
         * Code to be uncommented to used when gt-mbstyles becomes a supported module.
         *
         * dataList.add(new ValueComboBoxData(SLDOutputFormatEnum.MAPBOX.name(), "MapBox",
         * VendorOptionManager.getInstance().getDefaultVendorOptionVersion()));
         */
        // Options panel
        JPanel optionsPanel = new JPanel();

        JLabel label = new JLabel(Localisation.getField(SLDTextArea.class, "SLDTextArea.format"));
        optionsPanel.add(label);

        comboBox = new ValueComboBox();
        comboBox.setPreferredSize(
                new Dimension(BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT));
        comboBox.initialiseSingle(dataList);
        comboBox.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ValueComboBox comboBox = (ValueComboBox) e.getSource();
                        if (comboBox.getSelectedItem() != null) {

                            String name = comboBox.getSelectedValue().getKey();
                            outputFormat =
                                    SLDOutputFormatEnum.valueOf(SLDOutputFormatEnum.class, name);
                            outputText();
                        }
                    }
                });
        optionsPanel.add(comboBox);
        sldSourcePanel.add(optionsPanel, BorderLayout.NORTH);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane();
        sldSourcePanel.add(scrollPane);
        sldSourceTextArea = new JTextArea();
        scrollPane.setViewportView(sldSourceTextArea);
        sldSourceTextArea.setEditable(false);
        sldSourceTextArea.setWrapStyleWord(true);
        sldSourceTextArea.setLineWrap(true);

        sldSourcePanel.add(scrollPane, BorderLayout.CENTER);
        return sldSourcePanel;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.output.SLDOutputInterface#updatedSLD(com.sldeditor.common.
     * SLDDataInterface, org.geotools.styling.StyledLayerDescriptor)
     */
    @Override
    public void updatedSLD(SLDDataInterface sldData, StyledLayerDescriptor sld) {
        this.displayedSld = sld;
        this.resourceLocator = sldData.getResourceLocator();

        outputText();
    }

    /** Output text. */
    private void outputText() {
        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(outputFormat);

        String encodedSLD = sldWriter.encodeSLD(resourceLocator, displayedSld);
        sldSourceTextArea.setText(encodedSLD);
    }

    /**
     * Set the output format when a new file is loaded.
     *
     * @param sldData the sld data
     * @param dataEditedFlag the data edited flag
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.SLDEditorDataUpdateInterface#sldDataUpdated(com.sldeditor.common.
     * SLDDataInterface, boolean)
     */
    @Override
    public void sldDataUpdated(SLDDataInterface sldData, boolean dataEditedFlag) {
        if (sldData != null) {
            outputFormat = sldData.getOriginalFormat();

            comboBox.setSelectValueKey(outputFormat.name());
        }
    }
}
