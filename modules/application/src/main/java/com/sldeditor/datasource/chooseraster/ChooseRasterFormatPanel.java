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

package com.sldeditor.datasource.chooseraster;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.gce.image.WorldImageFormat;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;

/**
 * The Class ChooseRasterFormatPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class ChooseRasterFormatPanel extends JDialog implements ChooseRasterFormatInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The format list component. */
    private JList<String> formatListComponent;

    /** The format map. */
    private Map<String, AbstractGridFormat> formatMap = new HashMap<String, AbstractGridFormat>();
    
    /** The description text area. */
    private JTextArea descriptionTextArea;

    /**
     * Instantiates a new choose raster format panel.
     *
     * @param frame the frame
     */
    public ChooseRasterFormatPanel(JFrame frame)
    {
        super(frame, Localisation.getString(ChooseRasterFormatPanel.class, "ChooseRasterFormatPanel.title"), true);

        setLayout(new BorderLayout());

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(1,2));
        formatListComponent = new JList<String>();
        formatListComponent.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        formatListComponent.setPreferredSize(new Dimension(100, 300));
        formatListComponent.addListSelectionListener(new ListSelectionListener(){

            @Override
            public void valueChanged(ListSelectionEvent e) {
                String selectedFormat = formatListComponent.getSelectedValue();

                descriptionTextArea.setText(formatMap.get(selectedFormat).getDescription());
            }});
        JScrollPane listScroller = new JScrollPane(formatListComponent);
        p.add(listScroller);

        descriptionTextArea = new JTextArea();
        descriptionTextArea.setColumns(30);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setEditable(false);
        descriptionTextArea.setPreferredSize(new Dimension(100, 300));

        JScrollPane areaScrollPane = new JScrollPane(descriptionTextArea);
        areaScrollPane.setPreferredSize(new Dimension(100, 300));
        p.add(areaScrollPane);
        add(p, BorderLayout.CENTER);

        //
        // Ok button
        //
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        JButton okButton = new JButton(Localisation.getString(ChooseRasterFormatPanel.class, "common.ok"));
        okButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }});

        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        Controller.getInstance().centreDialog(this);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.impl.chooseraster.ChooseRasterFormatInterface#showPanel(java.util.Set)
     */
    @Override
    public AbstractGridFormat showPanel(Set<AbstractGridFormat> formatList)
    {
        WorldImageFormat wif = new WorldImageFormat();

        DefaultListModel<String> listModel = new DefaultListModel<String>();

        List<AbstractGridFormat> sortedFormatList = new ArrayList<AbstractGridFormat>();
        for(AbstractGridFormat format : formatList)
        {
            if(format.getName().compareTo(wif.getName()) != 0)
            {
                sortedFormatList.add(format);
            }
        }

        // Sort formats alphabetically
        Collections.sort(sortedFormatList, new Comparator<AbstractGridFormat>() {
            @Override
            public int compare(final AbstractGridFormat object1, final AbstractGridFormat object2) {
                return object1.getName().compareTo(object2.getName());
            }
        });

        // Add WorldImageFormat to top of list
        sortedFormatList.add(0, wif);

        // Add format names to list component
        for(AbstractGridFormat format : sortedFormatList)
        {
            formatMap.put(format.getName(), format);
            listModel.addElement(format.getName());
        }

        formatListComponent.setModel(listModel);
        // Select WorldImageFormat
        formatListComponent.setSelectedIndex(0);

        setVisible(true);

        String selectedFormatName = formatListComponent.getSelectedValue();
        AbstractGridFormat selectedFormat = formatMap.get(selectedFormatName);

        return selectedFormat;
    }
}
