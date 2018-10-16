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

package com.sldeditor.rendertransformation;

import com.sldeditor.common.Controller;
import com.sldeditor.common.DataTypeEnum;
import com.sldeditor.common.connection.GeoServerConnectionManagerInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.GeoServerConnection;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.extension.filesystem.geoserver.GeoServerReadProgress;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerClientInterface;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClient;
import com.sldeditor.extension.filesystem.geoserver.client.GeoServerWPSClientInterface;
import com.sldeditor.filter.ExpressionPanelFactory;
import com.sldeditor.filter.ExpressionPanelInterface;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import net.opengis.wps10.ProcessBriefType;
import org.geotools.process.function.ProcessFunction;
import org.geotools.process.function.ProcessFunctionFactory;
import org.opengis.filter.capability.FunctionName;
import org.opengis.filter.expression.Expression;

/**
 * Dialog to allow the configuration of render transformation.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformationDialog extends JDialog {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The function parameter table. */
    private JTable functionParameterTable;

    /** The connection combo box. */
    private JComboBox<String> connectionComboBox;

    /** The geo server connection manager. */
    private GeoServerConnectionManagerInterface geoServerConnectionManager = null;

    /** The GeoServer WPS client. */
    private GeoServerWPSClientInterface client = null;

    /** The connection map. */
    private Map<String, GeoServerConnection> connectionMap =
            new HashMap<String, GeoServerConnection>();

    /** The available function list. */
    private List<ProcessBriefType> availableFunctionList = null;

    /** The function list. */
    private JList<String> functionList;

    /** The function list model. */
    private DefaultListModel<String> functionListModel = new DefaultListModel<String>();

    /** The function parameter table model. */
    private FunctionTableModel functionParameterTableModel = new FunctionTableModel();

    /** The Add button. */
    private JButton btnAdd;

    /** The Remove button. */
    private JButton btnRemove;

    /** The expression panel. */
    private static ExpressionPanelInterface expressionPanel = null;

    /** The label showing progress/error messages. */
    private JLabel lblError;

    /** The Connect button. */
    private JButton btnConnect;

    /** The ok button pressed flag. */
    private boolean okButtonPressed = false;

    /** The existing process function. */
    private ProcessFunction existingProcessFunction = null;

    /** The built in process function factory. */
    private ProcessFunctionFactory factory = new ProcessFunctionFactory();

    /**
     * Instantiates a new render transformation dialog.
     *
     * @param geoServerConnectionManager the geo server connection manager
     */
    public RenderTransformationDialog(
            GeoServerConnectionManagerInterface geoServerConnectionManager) {
        super(
                Controller.getInstance().getFrame(),
                Localisation.getString(
                        RenderTransformationDialog.class, "RenderTransformationDialog.title"),
                true);

        this.geoServerConnectionManager = geoServerConnectionManager;

        if (expressionPanel == null) {
            expressionPanel = ExpressionPanelFactory.getRenderTransformationPanel("v2");
        }

        createUI();

        Dimension preferredSize = new Dimension(600, 400);
        setSize(preferredSize);

        Controller.getInstance().centreDialog(this);
    }

    /** Creates the ui. */
    private void createUI() {
        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.WEST);
        panel.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();
        panel.add(scrollPane);

        functionList = new JList<String>();
        functionList.setModel(functionListModel);

        populateBuiltInProcessFunctions();

        scrollPane.setViewportView(functionList);
        functionList.addListSelectionListener(
                new ListSelectionListener() {

                    @Override
                    public void valueChanged(ListSelectionEvent arg0) {
                        if (!arg0.getValueIsAdjusting()) {
                            displayFunction(functionList.getSelectedValue());
                        }
                    }
                });
        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane_1 = new JScrollPane();
        panel_1.add(scrollPane_1);

        functionParameterTable = new JTable();
        functionParameterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        functionParameterTable.setModel(functionParameterTableModel);

        ListSelectionModel selectionModel = functionParameterTable.getSelectionModel();

        selectionModel.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            handleTableSelectionEvent();
                        }
                    }
                });

        TableColumn col =
                functionParameterTable
                        .getColumnModel()
                        .getColumn(FunctionTableModel.getValueColumn());

        col =
                functionParameterTable
                        .getColumnModel()
                        .getColumn(FunctionTableModel.getOptionalColumn());
        CheckBoxRenderer checkBoxRenderer = new CheckBoxRenderer(functionParameterTableModel);
        col.setCellRenderer(checkBoxRenderer);
        col.setCellEditor(new OptionalValueEditor(functionParameterTableModel));

        scrollPane_1.setViewportView(functionParameterTable);

        JPanel panel_3 = new JPanel();
        panel_1.add(panel_3, BorderLayout.SOUTH);

        //
        // Add value button
        //
        btnAdd =
                new JButton(
                        Localisation.getString(
                                RenderTransformationDialog.class,
                                "RenderTransformationDialog.add"));
        btnAdd.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int row = functionParameterTable.getSelectedRow();

                        functionParameterTableModel.addNewValue(row);
                    }
                });
        btnAdd.setEnabled(false);
        panel_3.add(btnAdd);

        //
        // Remove value button
        //
        btnRemove =
                new JButton(
                        Localisation.getString(
                                RenderTransformationDialog.class,
                                "RenderTransformationDialog.remove"));
        btnRemove.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int row = functionParameterTable.getSelectedRow();

                        functionParameterTableModel.removeValue(row);
                    }
                });

        btnRemove.setEnabled(false);
        panel_3.add(btnRemove);

        //
        // Top panel
        //

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout(0, 0));
        getContentPane().add(panel_2, BorderLayout.NORTH);

        //        btnEditConnection =
        //                new JButton(
        //                        Localisation.getString(
        //                                RenderTransformationDialog.class,
        //                                "RenderTransformationDialog.editConnection"));
        //        panel_2.add(btnEditConnection, BorderLayout.EAST);
        //        btnEditConnection.addActionListener(
        //                new ActionListener() {
        //                    public void actionPerformed(ActionEvent e) {}
        //                });

        //
        // Connect button
        //
        btnConnect =
                new JButton(
                        Localisation.getString(
                                RenderTransformationDialog.class,
                                "RenderTransformationDialog.connect"));
        btnConnect.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {

                        updateButtonState(false);

                        populateFunctionList((String) connectionComboBox.getSelectedItem());
                    }
                });
        panel_2.add(btnConnect, BorderLayout.WEST);

        connectionComboBox = new JComboBox<String>();
        populateConnectionComboBox();
        panel_2.add(connectionComboBox, BorderLayout.CENTER);

        //
        // Error label
        //

        lblError = new JLabel(" ");
        panel_2.add(lblError, BorderLayout.SOUTH);

        //
        // Ok button
        //
        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.TRAILING);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        JButton btnOk =
                new JButton(
                        Localisation.getString(
                                RenderTransformationDialog.class, "RenderTransformationDialog.ok"));
        btnOk.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonPressed = true;
                        setVisible(false);
                    }
                });
        buttonPanel.add(btnOk);

        JButton btnCancel =
                new JButton(
                        Localisation.getString(
                                RenderTransformationDialog.class,
                                "RenderTransformationDialog.cancel"));
        btnCancel.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        okButtonPressed = false;
                        setVisible(false);
                    }
                });
        buttonPanel.add(btnCancel);
    }

    /** Populate built in process functions. */
    private void populateBuiltInProcessFunctions() {
        for (FunctionName functionName : factory.getFunctionNames()) {
            functionListModel.addElement(functionName.getName());
        }
    }

    /**
     * Update button state.
     *
     * @param enabled the enabled
     */
    private void updateButtonState(boolean enabled) {
        btnConnect.setEnabled(enabled);
        connectionComboBox.setEnabled(enabled);
    }

    /**
     * Show message, set text colour according to whether message is informational (black) or an
     * error (red).
     *
     * @param message the message
     * @param error the error
     */
    private void showMessage(String message, boolean error) {
        lblError.setForeground(error ? Color.RED : Color.BLACK);
        lblError.setText(message);
    }

    /** Handle table selection event. */
    private void handleTableSelectionEvent() {
        int row = functionParameterTable.getSelectedRow();
        int column = functionParameterTable.getSelectedColumn();

        ProcessFunctionParameterValue value = functionParameterTableModel.getValue(row);

        if (value != null) {
            int count = functionParameterTableModel.getNoOfOccurences(value);
            btnAdd.setEnabled(count < value.getMaxOccurences());
            btnRemove.setEnabled((count > value.getMinOccurences()) && (count > 1));
        } else {
            btnAdd.setEnabled(false);
            btnRemove.setEnabled(false);
        }

        if (column == 3) {
            String title =
                    String.format(
                            "%s - %s : %s",
                            (value != null) ? value.getName() : "unknown",
                            Localisation.getString(
                                    RenderTransformationDialog.class,
                                    "RenderTransformationDialog.type"),
                            (value != null) ? value.getDataType() : "unknown");
            expressionPanel.configure(
                    title, (value != null) ? value.getType() : Object.class, false);
            expressionPanel.populate(
                    (value != null) ? value.getObjectValue().getExpression() : null);
            if (expressionPanel.showDialog()) {
                Expression expression = expressionPanel.getExpression();

                functionParameterTableModel.update(expression, row);
            }
        }
    }

    /**
     * Display function.
     *
     * @param selectedValue the selected value
     */
    protected void displayFunction(String selectedValue) {
        boolean builtInFunctionFound = displayBuiltInProcessFunction(selectedValue);

        if (!builtInFunctionFound) {
            displayCustomProcessFunction(selectedValue);
        }
        functionParameterTableModel.fireTableDataChanged();
    }

    /**
     * Display built in process function.
     *
     * @param selectedValue the selected value
     * @return true, if successful
     */
    private boolean displayBuiltInProcessFunction(String selectedValue) {
        String functionNameString;

        // Strip off the prefix
        functionNameString = SelectedProcessFunction.extractLocalFunctionName(selectedValue);

        for (FunctionName name : factory.getFunctionNames()) {
            if (name.getName().compareToIgnoreCase(functionNameString) == 0) {
                functionList.setSelectedValue(functionNameString, true);
                functionParameterTableModel.populate(name, existingProcessFunction);
                return true;
            }
        }
        return false;
    }

    /**
     * Display custom process function, a process function that was read from GeoServer.
     *
     * @param selectedValue the selected value
     */
    private void displayCustomProcessFunction(String selectedValue) {
        ProcessBriefType selectedFunction = null;
        for (ProcessBriefType function : availableFunctionList) {
            if (function.getIdentifier().getValue().compareTo(selectedValue) == 0) {
                selectedFunction = function;
                break;
            }
        }

        functionParameterTableModel.populate(selectedFunction);
    }

    /**
     * Populate function list.
     *
     * @param selectedItem the selected item
     */
    private void populateFunctionList(String selectedItem) {

        GeoServerConnection connection = connectionMap.get(selectedItem);

        String message =
                String.format(
                        "%s : %s",
                        Localisation.getString(
                                RenderTransformationDialog.class,
                                "RenderTransformationDialog.tryingToConnect"),
                        connection.getUrl().toString());
        showMessage(message, false);

        // Make sure the above messages are displayed by trying to connect to
        // a WPS server in a separate thread.
        SwingUtilities.invokeLater(
                new Runnable() {
                    public void run() {
                        client = new GeoServerWPSClient(connection);
                        if (client.getCapabilities()) {
                            availableFunctionList =
                                    client.getRenderTransformations(DataTypeEnum.E_VECTOR);

                            functionListModel.removeAllElements();

                            populateBuiltInProcessFunctions();
                            for (ProcessBriefType function : availableFunctionList) {
                                functionListModel.addElement(function.getIdentifier().getValue());
                            }

                            // Clear info field
                            showMessage("", false);
                        } else {
                            // Show error message
                            showMessage(
                                    Localisation.getString(
                                            RenderTransformationDialog.class,
                                            "RenderTransformationDialog.errorFailedToConnect"),
                                    true);
                        }

                        // Make ui available again
                        updateButtonState(true);
                    }
                });
    }

    /** Populate connection combo box. */
    private void populateConnectionComboBox() {
        connectionComboBox.removeAllItems();

        if (geoServerConnectionManager != null) {
            List<GeoServerConnection> connectionList =
                    geoServerConnectionManager.getConnectionList();

            for (GeoServerConnection connection : connectionList) {
                connectionComboBox.addItem(connection.getConnectionName());
                connectionMap.put(connection.getConnectionName(), connection);
            }
        }
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {
        GeoServerConnectionManagerInterface dummyInterface =
                new GeoServerConnectionManagerInterface() {

                    @Override
                    public List<GeoServerConnection> getConnectionList() {
                        List<GeoServerConnection> list = new ArrayList<GeoServerConnection>();

                        GeoServerConnection connection = new GeoServerConnection();
                        connection.setConnectionName("Test");
                        try {
                            connection.setUrl(new URL("http://localhost:8080/geoserver"));
                            connection.setUserName("admin");
                            connection.setPassword("geoserver");

                            list.add(connection);
                        } catch (MalformedURLException e) {
                            ConsoleManager.getInstance().exception(this, e);
                        }
                        return list;
                    }

                    @Override
                    public void updateList() {}

                    @Override
                    public GeoServerConnection getConnection(String connectionDataName) {
                        return null;
                    }

                    @Override
                    public void readPropertyFile(GeoServerReadProgress progress) {}

                    @Override
                    public Map<GeoServerConnection, GeoServerClientInterface> getConnectionMap() {
                        return null;
                    }

                    @Override
                    public void removeConnection(GeoServerConnection connection) {}

                    @Override
                    public void addNewConnection(
                            GeoServerReadProgress progress,
                            GeoServerConnection newConnectionDetails) {}
                };

        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        vendorOptionList.add(VersionData.getLatestVersion(GeoServerVendorOption.class));
        VendorOptionManager.getInstance().setSelectedVendorOptions(vendorOptionList);

        RenderTransformationDialog dlg = new RenderTransformationDialog(dummyInterface);

        dlg.showDialog(null);
    }

    /**
     * Show dialog.
     *
     * @param existingProcessFunction the existing process function
     * @return true, if successful
     */
    public boolean showDialog(ProcessFunction existingProcessFunction) {
        internal_showDialog(existingProcessFunction);
        setVisible(true);

        return okButtonPressed;
    }

    /**
     * Interal show dialog.
     *
     * @param existingProcessFunction the existing process function
     */
    protected void internal_showDialog(ProcessFunction existingProcessFunction) {
        this.existingProcessFunction = existingProcessFunction;
        if (existingProcessFunction != null) {
            displayFunction(existingProcessFunction.getName());
        }
    }

    /**
     * Gets the transformation process function.
     *
     * @return the transformation expression
     */
    public ProcessFunction getTransformationProcessFunction() {
        return functionParameterTableModel.getExpression(factory);
    }
}
