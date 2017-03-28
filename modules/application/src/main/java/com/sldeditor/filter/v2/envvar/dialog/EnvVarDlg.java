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

package com.sldeditor.filter.v2.envvar.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.filter.v2.envvar.EnvVar;
import com.sldeditor.filter.v2.envvar.EnvironmentManagerInterface;
import com.sldeditor.filter.v2.envvar.EnvironmentVariableManager;

/**
 * The Class EnvVarDlg.
 *
 * @author Robert Ward (SCISYS)
 */
public class EnvVarDlg extends JDialog {

    private static final String WMS_ENV_PARAMETER = "env";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The table. */
    private JTable table;

    /** The text field. */
    private JTextField textField;

    /** The data model. */
    private EnvVarModel dataModel = null;

    /** The ok button pressed. */
    private boolean okButtonPressed = false;

    private JButton btnRemove;

    /** The environment variable manager. */
    private EnvironmentManagerInterface envVarMgr = null;

    /**
     * Instantiates a new env var dlg.
     */
    public EnvVarDlg(EnvironmentManagerInterface envVarMgr) {
        super(Controller.getInstance().getFrame(),
                Localisation.getString(EnvVarDlg.class, "EnvVarDlg.title"), true);

        this.envVarMgr = envVarMgr;

        createUI();
        setSize(600, 300);
        pack();

        Controller.getInstance().centreDialog(this);
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk = new JButton(Localisation.getString(EnvVarDlg.class, "common.ok"));
        btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = true;

                dataModel.updateEnvVarManager();
                setVisible(false);
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton(Localisation.getString(EnvVarDlg.class, "common.cancel"));
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = false;

                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(new BorderLayout(0, 0));

        table = new JTable();
        dataModel = new EnvVarModel(envVarMgr);
        table.setModel(dataModel);
        TableColumn column = table.getColumnModel().getColumn(1);

        JComboBox<Class<?>> typeComboBox = new JComboBox<Class<?>>();
        typeComboBox.setModel(new EnvVarComboBoxModel(envVarMgr));
        column.setCellEditor(new DefaultCellEditor(typeComboBox));

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                boolean enableRemoveButton = false;

                EnvVar envVar = dataModel.getEnvVar(table.getSelectedRow());
                if (envVar != null) {
                    enableRemoveButton = !envVar.isPredefined();
                }
                btnRemove.setEnabled(enableRemoveButton);
            }
        });
        JPanel panelWMS = new JPanel();
        panel.add(panelWMS, BorderLayout.NORTH);
        panelWMS.setLayout(new BoxLayout(panelWMS, BoxLayout.X_AXIS));

        JButton btnDecode = new JButton(
                Localisation.getString(EnvVarDlg.class, "EnvVarDlg.decode"));
        btnDecode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                URL url;
                try {
                    url = new URL(textField.getText());
                    Map<String, List<String>> parameterMap = splitQuery(url);

                    if (parameterMap.containsKey(WMS_ENV_PARAMETER)) {
                        dataModel.addNew(parameterMap.get(WMS_ENV_PARAMETER));
                    }
                } catch (MalformedURLException e1) {
                    ConsoleManager.getInstance().exception(this, e1);
                } catch (UnsupportedEncodingException e1) {
                    ConsoleManager.getInstance().exception(this, e1);
                }

            }
        });
        panelWMS.add(btnDecode);

        textField = new JTextField();
        panelWMS.add(textField);
        textField.setColumns(40);

        JPanel panel_1 = new JPanel();
        panel.add(panel_1, BorderLayout.SOUTH);

        JButton btnAdd = new JButton(Localisation.getString(EnvVarDlg.class, "EnvVarDlg.add"));
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataModel.addNewVariable();
                btnRemove.setEnabled(false);
            }
        });
        panel_1.add(btnAdd);

        btnRemove = new JButton(Localisation.getString(EnvVarDlg.class, "EnvVarDlg.remove"));
        btnRemove.setEnabled(false);
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataModel.removeEnvVar(table.getSelectedRow());
                btnRemove.setEnabled(false);
            }
        });
        panel_1.add(btnRemove);
    }

    /**
     * Show dialog.
     *
     * @return true, if successful
     */
    public boolean showDialog() {
        dataModel.populate();
        setVisible(true);

        return okButtonPressed;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        EnvVarDlg dlg = new EnvVarDlg(EnvironmentVariableManager.getInstance());

        dlg.showDialog();
    }

    /**
     * Split query.
     *
     * @param url the url
     * @return the map
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    private static Map<String, List<String>> splitQuery(URL url)
            throws UnsupportedEncodingException {
        final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
        final String[] pairs = url.getQuery().split("&");
        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!query_pairs.containsKey(key)) {
                query_pairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1
                    ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            query_pairs.get(key).add(value);
        }
        return query_pairs;
    }
}
