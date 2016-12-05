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
package com.sldeditor.ui.preferences;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.ui.layout.UILayoutFactory;

/**
 * Dialog that displays the user preferences to the user.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PrefPanel extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The ok button pressed flag. */
    private boolean okPressed = false;

    /** The use anti alias checkbox. */
    private JCheckBox chckbxUseAntiAlias;

    /** The set save last folder viewed checkbox. */
    private JCheckBox chckbxSetSaveLastFolderViewed;

    /** The chckbx check app version on start up. */
    private JCheckBox chckbxCheckAppVersionOnStartUp;

    /** The ui layout combo box. */
    private JComboBox<String> uiLayoutComboBox;

    /** The ui layout map. */
    private Map<String, String> uiLayoutMap;

    private JLabel uiLayoutPanelLabel;

    /** The populating dialog flag. */
    private boolean populatingDialog = false;

    /**
     * Default constructor.
     */
    public PrefPanel() {
        setTitle(Localisation.getString(PrefPanel.class, "PrefPanel.title"));

        this.setModal(true);

        createUI();

        pack();

        setSize(400, 200);

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Anti-alias
        JPanel chckbxUseAntiAliasPanel = new JPanel();
        chckbxUseAntiAliasPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        chckbxUseAntiAlias = new JCheckBox(
                Localisation.getString(PrefPanel.class, "PrefPanel.useAntiAlias"));
        chckbxUseAntiAliasPanel.add(chckbxUseAntiAlias);
        panel.add(chckbxUseAntiAliasPanel);

        // Save last folder viewed
        JPanel chckbxSetSaveLastFolderViewedPanel = new JPanel();
        chckbxSetSaveLastFolderViewedPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        chckbxSetSaveLastFolderViewed = new JCheckBox(
                Localisation.getString(PrefPanel.class, "PrefPanel.saveLastFolderViewed"));
        chckbxSetSaveLastFolderViewedPanel.add(chckbxSetSaveLastFolderViewed);
        panel.add(chckbxSetSaveLastFolderViewedPanel);

        // Check app version on start up
        JPanel chckbxCheckAppVersionOnStartUpPanel = new JPanel();
        chckbxCheckAppVersionOnStartUpPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        chckbxCheckAppVersionOnStartUp = new JCheckBox(
                Localisation.getString(PrefPanel.class, "PrefPanel.checkAppVersionOnStartUp"));
        chckbxCheckAppVersionOnStartUpPanel.add(chckbxCheckAppVersionOnStartUp);
        panel.add(chckbxCheckAppVersionOnStartUpPanel);

        // Ui layout class
        uiLayoutMap = UILayoutFactory.getAllLayouts();
        String[] uiLayoutNameList = new String[uiLayoutMap.size()];
        int index = 0;
        for (String key : uiLayoutMap.keySet()) {
            uiLayoutNameList[index] = key;
            index++;
        }
        uiLayoutComboBox = new JComboBox<String>(uiLayoutNameList);

        JPanel uiLayoutPanel = new JPanel();
        uiLayoutPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        uiLayoutPanel.add(new JLabel(Localisation.getField(PrefPanel.class, "PrefPanel.uiLayout")));
        uiLayoutPanel.add(uiLayoutComboBox);

        uiLayoutPanelLabel = new JLabel(" ");
        uiLayoutPanel.add(uiLayoutPanelLabel);

        panel.add(uiLayoutPanel);

        uiLayoutComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (!populatingDialog) {
                    uiLayoutPanelLabel.setText(
                            Localisation.getString(PrefPanel.class, "PrefPanel.uiLayoutLabel"));
                }
            }
        });

        // Ok/Cancel buttons
        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setHgap(1);
        flowLayout.setAlignment(FlowLayout.TRAILING);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(PrefPanel.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okPressed = true;
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(PrefPanel.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okPressed = false;
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(btnCancel);
    }

    /**
     * Show dialog.
     *
     * @param prefData the pref data
     * @return true, if successful
     */
    public boolean showDialog(PrefData prefData) {

        populate(prefData);

        setVisible(true);

        return okPressed;
    }

    /**
     * Populate panel.
     *
     * @param prefData the pref data
     */
    protected void populate(PrefData prefData) {
        if (prefData != null) {
            chckbxCheckAppVersionOnStartUp.setSelected(prefData.isCheckAppVersionOnStartUp());
            chckbxUseAntiAlias.setSelected(prefData.isUseAntiAlias());
            chckbxSetSaveLastFolderViewed.setSelected(prefData.isSaveLastFolderView());

            for (String displayName : uiLayoutMap.keySet()) {
                String className = uiLayoutMap.get(displayName);

                if (prefData.getUiLayoutClass() != null) {
                    if (className.compareTo(prefData.getUiLayoutClass()) == 0) {
                        populatingDialog = true;
                        uiLayoutComboBox.setSelectedItem(displayName);
                        populatingDialog = false;
                    }
                }
            }
        }
    }

    /**
     * Get preference data.
     *
     * @return the preference data
     */
    public PrefData getPrefData() {

        PrefData prefData = PrefManager.getInstance().getPrefData();

        prefData.setCheckAppVersionOnStartUp(chckbxCheckAppVersionOnStartUp.isSelected());
        prefData.setUseAntiAlias(chckbxUseAntiAlias.isSelected());
        String uiLayoutClass = uiLayoutMap.get(uiLayoutComboBox.getSelectedItem());
        prefData.setUiLayoutClass(uiLayoutClass);
        prefData.setSaveLastFolderView(chckbxSetSaveLastFolderViewed.isSelected());

        return prefData;
    }
}
