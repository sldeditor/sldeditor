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
import com.sldeditor.datasource.attribute.DataSourceAttributeList;
import com.sldeditor.datasource.attribute.DataSourceAttributeListInterface;
import com.sldeditor.datasource.connector.DataSourceConnectorComboBoxModel;
import com.sldeditor.datasource.connector.DataSourceConnectorFactory;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;

/**
 * Panel to be able to edit data source configurations.
 * 
 * @author Robert Ward (SCISYS)
 */
public class DataSourceConfigPanel extends JPanel implements DataSourceUpdatedInterface, UndoActionInterface
{
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The table. */
    private JTable table;

    /** The data model. */
    private DataSourceAttributeModel dataModel = null;

    /** The attribute data. */
    private DataSourceAttributeListInterface attributeData = null;

    /** The data source. */
    private DataSourceInterface dataSource = DataSourceFactory.createDataSource(null);

    /** The btn remove field. */
    private JButton btnRemoveField;

    /** The dsc model. */
    private DataSourceConnectorComboBoxModel dscModel = null;

    /** The data source connector combo box. */
    private JComboBox<String> dataSourceConnectorComboBox;

    /** The data source connector panel. */
    private JPanel dscPanel;

    /** The btn ok. */
    private JButton btnApply;

    /** The btn cancel. */
    private JButton btnCancel;

    /** The table populating. */
    private boolean tablePopulating = false;

    /** The data changed. */
    private boolean dataChanged = false;

    /**
     * Instantiates a new data source config.
     */
    public DataSourceConfigPanel() {
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
    private Component createDataSourceConnectorPanel()
    {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel panel1 = new JPanel();
        JLabel label = new JLabel(Localisation.getField(DataSourceConfigPanel.class, "DataSourceConfigPanel.field"));
        panel1.add(label);

        Map<Class<?>, DataSourceConnectorInterface> dscMap = DataSourceConnectorFactory.getDataSourceConnectorList();

        dscModel = new DataSourceConnectorComboBoxModel(dscMap);
        dataSourceConnectorComboBox = new JComboBox<String>(dscModel);
        dataSourceConnectorComboBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                String selectedItem = (String) dataSourceConnectorComboBox.getSelectedItem();

                CardLayout cl = (CardLayout)(dscPanel.getLayout());
                cl.show(dscPanel, selectedItem);
            }
        });
        panel1.add(dataSourceConnectorComboBox);

        mainPanel.add(panel1, BorderLayout.NORTH);

        //
        // Set up the card layout from the available data source connectors
        //
        dscPanel = new JPanel();
        dscPanel.setLayout(new CardLayout());

        for(Class<?> key : dscMap.keySet())
        {
            DataSourceConnectorInterface dsConnector = dscMap.get(key);
            JPanel panelToAdd = dsConnector.getPanel();
            dscPanel.add(panelToAdd, dsConnector.getDisplayName());
        }
        mainPanel.add(dscPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Creates the table.
     *
     * @return the component
     */
    private Component createTable() {
        final UndoActionInterface parentObj = this;

        dataModel = new DataSourceAttributeModel();
        dataModel.addTableModelListener(new TableModelListener()
        {
            /**
             * Table changed.
             *
             * @param arg0 the arg0
             */
            @Override
            public void tableChanged(TableModelEvent arg0) {
                if(!isPopulatingTable())
                {
                    dataChanged = true;

                    updateButtonState();
                }
            }
        });

        table = new JTable();
        table.setModel(dataModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {

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

        JButton btnAddField = new JButton(Localisation.getString(DataSourceConfigPanel.class, "DataSourceConfigPanel.add"));
        btnAddField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                dataModel.addNewField();

                dataModel.fireTableDataChanged();

                dataChanged = true;

                updateButtonState();
            }
        });
        buttonPanel.add(btnAddField);

        btnRemoveField = new JButton(Localisation.getString(DataSourceConfigPanel.class, "DataSourceConfigPanel.remove"));
        btnRemoveField.setEnabled(false);
        btnRemoveField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                dataModel.removeFields(table.getSelectedRows());

                setPopulatingTable(true);
                dataModel.fireTableDataChanged();
                setPopulatingTable(false);

                dataChanged = true;

                updateButtonState();
            }
        });
        buttonPanel.add(btnRemoveField);

        btnApply = new JButton(Localisation.getString(DataSourceConfigPanel.class, "common.apply"));
        btnApply.setEnabled(false);
        btnApply.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                Object oldValueObj = attributeData;
                Object newValueObj = dataModel.getAttributeData();

                UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, "Preferences", oldValueObj, newValueObj));

                attributeData = (DataSourceAttributeListInterface) newValueObj;

                dataSource.updateFields(attributeData);

                dataChanged = false;

                updateButtonState();
            }
        });
        buttonPanel.add(btnApply);

        btnCancel = new JButton(Localisation.getString(DataSourceConfigPanel.class, "common.cancel"));
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataModel.populate(attributeData.getData());

                setPopulatingTable(true);
                dataModel.fireTableDataChanged();
                setPopulatingTable(false);

                dataChanged = false;

                updateButtonState();
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
        btnRemoveField.setEnabled(table.getSelectedRowCount() > 0);
        btnApply.setEnabled(dataChanged);
        btnCancel.setEnabled(dataChanged);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource.impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag)
    {
        if(attributeData == null)
        {
            attributeData = new DataSourceAttributeList();
        }

        // Populate data source properties panel
        DataSourcePropertiesInterface dataSourceProperties = dataSource.getDataConnectorProperties();
        if(dataSourceProperties != null)
        {
            dataSourceProperties.populate();

            // Set the combo box to display the correct data source connector panel
            DataSourceConnectorInterface dsc = dataSourceProperties.getDataSourceConnector();

            String displayName = dsc.getDisplayName();
            if(dataSourceConnectorComboBox != null)
            {
                dataSourceConnectorComboBox.setSelectedItem(displayName);
            }
        }

        // Populate table
        dataSource.readAttributes(attributeData);

        if(dataModel != null)
        {
            dataModel.setConnectedToDataSource(isConnectedToDataSourceFlag);
            dataModel.populate(attributeData.getData());

            setPopulatingTable(true);
            dataModel.fireTableDataChanged();
            setPopulatingTable(false);
        }
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
    private boolean isPopulatingTable()
    {
        return tablePopulating;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#undoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        attributeData = (DataSourceAttributeListInterface) undoRedoObject.getOldValue();

        dataModel.populate(attributeData.getData());

        setPopulatingTable(true);
        dataModel.fireTableDataChanged();
        setPopulatingTable(false);

        dataSource.updateFields(attributeData);

        dataChanged = false;

        updateButtonState();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.undo.UndoActionInterface#redoAction(com.sldeditor.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        attributeData = (DataSourceAttributeListInterface) undoRedoObject.getNewValue();

        dataModel.populate(attributeData.getData());

        setPopulatingTable(true);
        dataModel.fireTableDataChanged();
        setPopulatingTable(false);

        dataSource.updateFields(attributeData);

        dataChanged = false;

        updateButtonState();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Does nothing
    }

    public void reset() {
        dscModel.reset();
    }
}
