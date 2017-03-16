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
package com.sldeditor.datasource.config;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.geotools.data.DataStore;

import com.sldeditor.common.DataSourceConnectorInterface;
import com.sldeditor.common.DataSourcePropertiesInterface;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * Panel to be able to edit data source configurations.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConfigPanel extends JPanel
        implements DataSourceUpdatedInterface, UndoActionInterface {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The table. */
    private JTable table;

    /** The data model. */
    private DataSourceAttributeModel dataModel = null;

    /** The attribute data. */
    private DataSourceAttributeListInterface attributeData = null;

    /** The data source. */
    private DataSourceInterface dataSource = null;

    /** The data source connector panel. */
    private JPanel dscPanel;

    /** The btn disconnect. */
    private JButton btnDisconnect;

    /** The btn add field. */
    private JButton btnAddField;

    /** The btn remove field. */
    private JButton btnRemoveField;

    /** The btn ok. */
    private JButton btnApply;

    /** The btn cancel. */
    private JButton btnCancel;

    /** The table populating. */
    private boolean tablePopulating = false;

    /** The data changed. */
    private boolean dataChanged = false;

    /** The is connected to data source flag. */
    private boolean isConnectedToDataSourceFlag = false;

    /**
     * Instantiates a new data source config.
     */
    public DataSourceConfigPanel() {
        dataSource = DataSourceFactory.createDataSource(null);

        dataSource.addListener(this);
        setLayout(new BorderLayout());

        add(createDataSourceConnectorPanel(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
    }

    /**
     * Creates the data source connector panel.
     *
     * @return the component
     */
    private Component createDataSourceConnectorPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        Map<Class<?>, DataSourceConnectorInterface> dscMap = DataSourceConnectorFactory
                .getDataSourceConnectorList();

        //
        // Set up the card layout from the available data source connectors
        //
        dscPanel = new JPanel();
        dscPanel.setLayout(new CardLayout());

        for (Class<?> key : dscMap.keySet()) {
            DataSourceConnectorInterface dsConnector = dscMap.get(key);
            JPanel panelToAdd = dsConnector.getPanel();
            dscPanel.add(panelToAdd, dsConnector.getDisplayName());
        }
        mainPanel.add(dscPanel, BorderLayout.CENTER);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEADING));
        JLabel label = new JLabel(Localisation.getString(DataSourceConfigPanel.class,
                "DataSourceConfigPanel.dataSource"));

        panel1.add(label);

        mainPanel.add(panel1, BorderLayout.NORTH);

        return mainPanel;
    }

    /**
     * Show panel.
     *
     * @param selectedItem the selected item
     */
    protected void showPanel(String selectedItem) {
        if (dscPanel != null) {
            CardLayout cl = (CardLayout) (dscPanel.getLayout());
            cl.show(dscPanel, selectedItem);
        }
    }

    /**
     * Creates the table.
     *
     * @return the component
     */
    private Component createTable() {
        final UndoActionInterface parentObj = this;

        dataModel = new DataSourceAttributeModel();
        dataModel.addTableModelListener(new TableModelListener() {
            /**
             * Table changed.
             *
             * @param arg0 the arg0
             */
            @Override
            public void tableChanged(TableModelEvent arg0) {
                if (!isPopulatingTable()) {
                    dataChanged = true;

                    updateButtonState();
                }
            }
        });

        table = new JTable();
        table.setModel(dataModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent arg0) {
                updateButtonState();
            }

        });
        TableColumn tmpColum = table.getColumnModel().getColumn(1);

        JComboBox<String> comboBox = new JComboBox<String>(dataModel.getTypeData());

        DefaultCellEditor defaultCellEditor = new DefaultCellEditor(comboBox);
        tmpColum.setCellEditor(defaultCellEditor);
        tmpColum.setCellRenderer(new ComboBoxCellRenderer(comboBox));

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setHgap(1);
        flowLayout.setAlignment(FlowLayout.TRAILING);
        add(buttonPanel, BorderLayout.SOUTH);

        btnDisconnect = new JButton(Localisation.getString(DataSourceConfigPanel.class,
                "DataSourceConfigPanel.disconnect"));
        btnDisconnect.setEnabled(false);
        btnDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SLDEditorFile.getInstance().getSLDData()
                        .setDataSourceProperties(DataSourceConnectorFactory.getNoDataSource());

                applyData(parentObj);
            }
        });
        buttonPanel.add(btnDisconnect);

        btnAddField = new JButton(
                Localisation.getString(DataSourceConfigPanel.class, "DataSourceConfigPanel.add"));
        btnAddField.setEnabled(false);
        btnAddField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addNewField();
            }
        });
        buttonPanel.add(btnAddField);

        btnRemoveField = new JButton(Localisation.getString(DataSourceConfigPanel.class,
                "DataSourceConfigPanel.remove"));
        btnRemoveField.setEnabled(false);
        btnRemoveField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                removeField();
            }
        });
        buttonPanel.add(btnRemoveField);

        btnApply = new JButton(Localisation.getString(DataSourceConfigPanel.class, "common.apply"));
        btnApply.setEnabled(false);
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                applyData(parentObj);
            }
        });
        buttonPanel.add(btnApply);

        btnCancel = new JButton(
                Localisation.getString(DataSourceConfigPanel.class, "common.cancel"));
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelData();
            }
        });
        buttonPanel.add(btnCancel);

        JScrollPane scrollPane = new JScrollPane(table);

        return scrollPane;
    }

    /**
     * Update button state.
     */
    private void updateButtonState() {
        if (btnDisconnect != null) {
            btnDisconnect.setEnabled(isConnectedToDataSourceFlag);
            btnAddField.setEnabled(!isConnectedToDataSourceFlag);
            btnRemoveField
                    .setEnabled(!isConnectedToDataSourceFlag && (table.getSelectedRowCount() > 0));
            btnApply.setEnabled(dataChanged);
            btnCancel.setEnabled(dataChanged);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType,
            boolean isConnectedToDataSourceFlag) {
        this.isConnectedToDataSourceFlag = isConnectedToDataSourceFlag;
        if (attributeData == null) {
            attributeData = new DataSourceAttributeList();
        }

        // Populate data source properties panel
        DataSourcePropertiesInterface dataSourceProperties = dataSource
                .getDataConnectorProperties();
        if (dataSourceProperties != null) {
            dataSourceProperties.populate();

            // Display the correct data source connector panel
            DataSourceConnectorInterface dsc = dataSourceProperties.getDataSourceConnector();

            showPanel(dsc.getDisplayName());
        }

        // Populate table
        dataSource.readAttributes(attributeData);

        if (dataModel != null) {
            dataModel.setConnectedToDataSource(isConnectedToDataSourceFlag);
            dataModel.populate(attributeData.getData());

            setPopulatingTable(true);
            dataModel.fireTableDataChanged();
            setPopulatingTable(false);
        }

        updateButtonState();
    }

    /**
     * Sets the populating table.
     *
     * @param b the new populating table
     */
    private void setPopulatingTable(boolean b) {
        tablePopulating = b;
    }

    /**
     * Checks if is populating table.
     *
     * @return true, if is populating table
     */
    private boolean isPopulatingTable() {
        return tablePopulating;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            attributeData = (DataSourceAttributeListInterface) undoRedoObject.getOldValue();

            dataModel.populate(attributeData.getData());

            setPopulatingTable(true);
            dataModel.fireTableDataChanged();
            setPopulatingTable(false);

            dataSource.updateFields(attributeData);

            dataChanged = false;

            updateButtonState();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            attributeData = (DataSourceAttributeListInterface) undoRedoObject.getNewValue();

            dataModel.populate(attributeData.getData());

            setPopulatingTable(true);
            dataModel.fireTableDataChanged();
            setPopulatingTable(false);

            dataSource.updateFields(attributeData);

            dataChanged = false;

            updateButtonState();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }

    /**
     * Reset.
     */
    public void reset() {
    }

    /**
     * Adds the new field.
     */
    protected void addNewField() {
        dataModel.addNewField();

        dataModel.fireTableDataChanged();

        dataChanged = true;

        updateButtonState();
    }

    /**
     * Removes the field.
     */
    protected void removeField() {
        dataModel.removeFields(table.getSelectedRows());

        setPopulatingTable(true);
        dataModel.fireTableDataChanged();
        setPopulatingTable(false);

        dataChanged = true;

        updateButtonState();
    }

    /**
     * Apply data.
     *
     * @param parentObj the parent obj
     */
    protected void applyData(final UndoActionInterface parentObj) {
        Object oldValueObj = attributeData;
        Object newValueObj = dataModel.getAttributeData();

        UndoManager.getInstance()
                .addUndoEvent(new UndoEvent(parentObj, "Preferences", oldValueObj, newValueObj));

        attributeData = (DataSourceAttributeListInterface) newValueObj;

        dataSource.updateFields(attributeData);

        dataChanged = false;

        updateButtonState();
    }

    /**
     * Cancel data.
     */
    protected void cancelData() {
        dataModel.populate(attributeData.getData());

        setPopulatingTable(true);
        dataModel.fireTableDataChanged();
        setPopulatingTable(false);

        dataChanged = false;

        updateButtonState();
    }
}
