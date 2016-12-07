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

package com.sldeditor.help;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;

/**
 * The Class HelpPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class HelpPanel extends JDialog {

    /**
     * 
     */
    private static final String STYLESHEET = "/help/markdown.css";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The index list. */
    private JList<HelpData> indexList;

    /** The text area. */
    private JEditorPane textArea;

    /** The help reader. */
    private static HelpReader helpReader = new HelpReader();

    /** The instance. */
    private static HelpPanel instance = null;

    /**
     * Instantiates a new help panel.
     */
    private HelpPanel() {
        setTitle(Localisation.getString(HelpData.class, "Help.dlgTitle"));
        createUI();
        pack();
        setSize(600, 400);
        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        getContentPane().setLayout(new BorderLayout(0, 0));

        indexList = new JList<HelpData>(helpReader.getIndexEntries());
        indexList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                HelpData helpData = indexList.getSelectedValue();

                textArea.setText(helpReader.parse(helpData));
            }
        });
        JScrollPane scrollPaneIndex = new JScrollPane(indexList);
        scrollPaneIndex.setPreferredSize(new Dimension(200, 400));
        getContentPane().add(scrollPaneIndex, BorderLayout.WEST);

        textArea = new JEditorPane();
        HTMLEditorKit kit = new HTMLEditorKit();
        textArea.setEditorKit(kit);
        StyleSheet styleSheet = kit.getStyleSheet();
        
        URL resource=getClass().getResource(STYLESHEET);
        styleSheet.importStyleSheet(resource);
        kit.setStyleSheet(styleSheet);
        Document doc = kit.createDefaultDocument();
        textArea.setDocument(doc);

        JScrollPane scrollPaneHelp = new JScrollPane(textArea);
        getContentPane().add(scrollPaneHelp, BorderLayout.CENTER);
    }

    /**
     * Show dialog.
     */
    public static void showDialog(String key) {
        if (instance == null) {
            instance = new HelpPanel();
        }
        instance.setVisible(true);
    }
}
