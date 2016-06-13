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

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.geotools.styling.StyledLayerDescriptor;

import com.sldeditor.common.output.SLDOutputInterface;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.render.RenderPanelFactory;

/**
 * Text area component that displays the SLD as xml.
 * 
 * @author Robert Ward (SCISYS)
 */
public class SLDTextArea implements SLDOutputInterface
{
    /** The sld source text area. */
    private JTextArea sldSourceTextArea;

    /** The instance. */
    private static SLDTextArea instance = null;

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    public static JPanel getPanel()
    {
        if(instance == null)
        {
            instance = new SLDTextArea();
        }

        return instance.makeTextAreaPanel();
    }

    /**
     * Instantiates a new SLD text area.
     */
    private SLDTextArea()
    {
        sldWriter = SLDWriterFactory.createSLDWriter(null);

        // Listen for changes in the SLD
        RenderPanelFactory.addSLDOutputListener(this);
    }

    /**
     * Make text area panel.
     *
     * @return the j component
     */
    private JPanel makeTextAreaPanel() {

        JPanel sldSourcePanel = new JPanel(false);
        sldSourcePanel.setLayout(new GridLayout(1, 1));

        JScrollPane scrollPane = new JScrollPane();
        sldSourcePanel.add(scrollPane);
        sldSourceTextArea = new JTextArea();
        scrollPane.setViewportView(sldSourceTextArea);
        sldSourceTextArea.setEditable(false);
        sldSourceTextArea.setWrapStyleWord(true);
        sldSourceTextArea.setLineWrap(true);
        return sldSourcePanel;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.output.SLDOutputInterface#updatedSLD(org.geotools.styling.StyledLayerDescriptor)
     */
    @Override
    public void updatedSLD(StyledLayerDescriptor sld)
    {
        String encodedSLD = sldWriter.encodeSLD(sld);
        sldSourceTextArea.setText(encodedSLD);
    }

}
