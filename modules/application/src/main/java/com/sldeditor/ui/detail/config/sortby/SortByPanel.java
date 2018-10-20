/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.ui.detail.config.sortby;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.datasource.DataSourceInterface;
import com.sldeditor.datasource.DataSourceUpdatedInterface;
import com.sldeditor.datasource.impl.DataSourceFactory;
import com.sldeditor.datasource.impl.GeometryTypeEnum;
import com.sldeditor.ui.detail.BasePanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import org.geotools.data.DataStore;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.FeatureTypeStyle;
import org.opengis.filter.sort.SortBy;

/**
 * The Class SortByPanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class SortByPanel extends JPanel
        implements DataSourceUpdatedInterface, SortOrderUpdateInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The source list model. */
    private DefaultListModel<String> sourceModel = new DefaultListModel<>();

    /** The destination table model. */
    private SortByTableModel destinationModel = null;

    /** The field name list. */
    private transient List<String> fieldNameList = null;

    /** The button move up. */
    private JButton btnMoveUp;

    /** The button move down. */
    private JButton btnMoveDown;

    /** The src to dest button. */
    private JButton btnSrcToDestButton;

    /** The dest to src button. */
    private JButton btnDestToSrcButton;

    /** The source list. */
    private JList<String> sourceList;

    /** The destination list. */
    private JTable destinationTable;

    /** The parent obj. */
    private transient SortByUpdateInterface parentObj = null;

    /**
     * Instantiates a new sort by panel.
     *
     * @param parentObj the parent obj
     * @param noOfRows the no of rows
     */
    public SortByPanel(SortByUpdateInterface parentObj, int noOfRows) {
        this.parentObj = parentObj;

        destinationModel = new SortByTableModel(this);

        setLayout(new BorderLayout(0, 0));

        createUI(noOfRows);

        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        dataSource.addListener(this);
    }

    /** Creates the UI. */
    private void createUI(int noOfRows) {
        int xPos = 0;
        int width = BasePanel.FIELD_PANEL_WIDTH - xPos - 20;
        int height = BasePanel.WIDGET_HEIGHT * (noOfRows - 1);
        this.setBounds(0, 0, width, height);

        JPanel panel = new JPanel();
        add(panel, BorderLayout.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        // Source list
        JScrollPane scrollPaneSource = new JScrollPane();
        scrollPaneSource.setPreferredSize(new Dimension(200, 200));
        panel.add(scrollPaneSource);

        sourceList = new JList<>(sourceModel);
        sourceList.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent e) {
                        if (!e.getValueIsAdjusting()) {
                            sourceSelected();
                        }
                    }
                });
        scrollPaneSource.setViewportView(sourceList);

        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));

        JPanel centrePanel = new JPanel();
        centrePanel.setPreferredSize(new Dimension(50, 50));
        centrePanel.setMaximumSize(new Dimension(50, 50));
        panel.add(centrePanel);
        centrePanel.setLayout(new BoxLayout(centrePanel, BoxLayout.Y_AXIS));

        btnSrcToDestButton = new JButton("->");
        btnSrcToDestButton.setEnabled(false);
        btnSrcToDestButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveSrcToDestination();
                    }
                });
        centrePanel.add(btnSrcToDestButton);

        btnDestToSrcButton = new JButton("<-");
        btnDestToSrcButton.setEnabled(false);
        btnDestToSrcButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveDestinationToSource();
                    }
                });
        centrePanel.add(btnDestToSrcButton);

        // Destination list
        JScrollPane scrollPaneDest = new JScrollPane();
        scrollPaneDest.setPreferredSize(new Dimension(200, 200));
        panel.add(scrollPaneDest);

        destinationTable = new JTable();
        destinationTable.setModel(destinationModel);

        TableColumnModel columnModel = destinationTable.getColumnModel();
        TableColumn col = columnModel.getColumn(SortByTableModel.getSortOrderColumn());
        SortByCheckBoxRenderer checkBoxRenderer = new SortByCheckBoxRenderer();
        col.setCellRenderer(checkBoxRenderer);
        col.setCellEditor(new SortByOptionalValueEditor(destinationModel));

        destinationTable
                .getSelectionModel()
                .addListSelectionListener(
                        new ListSelectionListener() {
                            public void valueChanged(ListSelectionEvent e) {
                                if (!e.getValueIsAdjusting()) {
                                    ListSelectionModel model = destinationTable.getSelectionModel();
                                    if (!model.isSelectionEmpty()) {
                                        destinationSelected();
                                    }
                                }
                            }
                        });

        destinationModel.addTableModelListener(
                new TableModelListener() {

                    @Override
                    public void tableChanged(TableModelEvent e) {
                        if (e.getColumn() == SortByTableModel.getSortOrderColumn()) {
                            ListSelectionModel model = destinationTable.getSelectionModel();
                            model.clearSelection();

                            btnMoveDown.setEnabled(false);
                            btnMoveUp.setEnabled(false);
                            btnSrcToDestButton.setEnabled(false);
                            btnDestToSrcButton.setEnabled(false);
                        }
                    }
                });
        scrollPaneDest.setViewportView(destinationTable);

        JPanel buttonPanel = new JPanel();
        FlowLayout flButtonPanel = (FlowLayout) buttonPanel.getLayout();
        flButtonPanel.setAlignment(FlowLayout.RIGHT);
        add(buttonPanel, BorderLayout.SOUTH);

        btnMoveUp = new JButton(Localisation.getString(SortByPanel.class, "sortby.up"));
        btnMoveUp.setEnabled(false);
        btnMoveUp.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveDestinationUp();
                    }
                });
        buttonPanel.add(btnMoveUp);

        btnMoveDown = new JButton(Localisation.getString(SortByPanel.class, "sortby.down"));
        btnMoveDown.setEnabled(false);
        btnMoveDown.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveDestinationDown();
                    }
                });
        buttonPanel.add(btnMoveDown);
    }

    /** Move destination down. */
    protected void moveDestinationDown() {
        int[] indices = destinationTable.getSelectedRows();
        int[] updatedIndices = new int[indices.length];

        for (int arrayIndex = indices.length - 1; arrayIndex >= 0; arrayIndex--) {
            int index = indices[arrayIndex];
            if (index < destinationModel.getRowCount() - 1) {
                destinationModel.moveRowDown(index);
                updatedIndices[arrayIndex] = index + 1;
            } else {
                // At the bottom of the list so make sure it is still highlighted
                updatedIndices[arrayIndex] = index;
            }
        }

        // Reselect the items after they have moved
        ListSelectionModel model = destinationTable.getSelectionModel();
        model.clearSelection();
        for (int index : updatedIndices) {
            model.addSelectionInterval(index, index);
        }
    }

    /** Move destination up. */
    protected void moveDestinationUp() {
        int[] indices = destinationTable.getSelectedRows();
        int[] updatedIndices = new int[indices.length];
        int arrayIndex = 0;

        for (int index : indices) {
            if (index > 0) {
                destinationModel.moveRowUp(index);
                updatedIndices[arrayIndex] = index - 1;
            } else {
                // At the top of the list so make sure it is still highlighted
                updatedIndices[arrayIndex] = index;
            }
            arrayIndex++;
        }

        // Reselect the items after they have moved
        ListSelectionModel model = destinationTable.getSelectionModel();
        model.clearSelection();
        for (int index : updatedIndices) {
            model.addSelectionInterval(index, index);
        }
    }

    /** Move destination to source. */
    protected void moveDestinationToSource() {
        List<String> selectedItemList = destinationModel.removeSelected(destinationTable);

        for (String item : selectedItemList) {
            sourceModel.addElement(item);
        }

        btnMoveDown.setEnabled(false);
        btnMoveUp.setEnabled(false);
        btnDestToSrcButton.setEnabled(false);
    }

    /** Move source to destination. */
    protected void moveSrcToDestination() {
        List<String> selectedItemList = sourceList.getSelectedValuesList();

        for (String item : selectedItemList) {
            destinationModel.addProperty(item);
            sourceModel.removeElement(item);
        }

        ListSelectionModel model = destinationTable.getSelectionModel();
        model.clearSelection();

        btnSrcToDestButton.setEnabled(false);
    }

    /** Update lists. */
    private void updateLists() {
        sourceModel.clear();

        if (fieldNameList != null) {
            for (String item : fieldNameList) {
                if (!destinationModel.containsProperty(item)) {
                    sourceModel.addElement(item);
                }
            }

            // Check to see if selected fields exist in the data source
            destinationModel.checkForFields(fieldNameList);
        }
    }

    /** Destination selected. */
    protected void destinationSelected() {
        btnMoveDown.setEnabled(true);
        btnMoveUp.setEnabled(true);
        btnDestToSrcButton.setEnabled(true);
    }

    /** Source selected. */
    protected void sourceSelected() {
        btnSrcToDestButton.setEnabled(true);
    }

    /**
     * Gets the text.
     *
     * @return the text
     */
    public String getText() {
        return destinationModel.getEncodedString();
    }

    /**
     * Sets the text.
     *
     * @param value the new text
     */
    public void setText(String value) {
        Map<String, String> options = new HashMap<>();

        ListSelectionModel model = destinationTable.getSelectionModel();
        model.clearSelection();

        SortBy[] sortArray = null;

        if (!value.isEmpty()) {
            options.put(FeatureTypeStyle.SORT_BY, value);

            sortArray = SLDStyleFactory.getSortBy(options);
        }
        destinationModel.populate(sortArray);
        updateLists();

        btnMoveDown.setEnabled(false);
        btnMoveUp.setEnabled(false);
        btnDestToSrcButton.setEnabled(false);
        btnSrcToDestButton.setEnabled(false);
    }

    /**
     * Select destination rows.
     *
     * @param selectedIndexes the selected indexes
     */
    protected void selectDestination(int[] selectedIndexes) {
        ListSelectionModel model = destinationTable.getSelectionModel();
        model.clearSelection();
        for (int index : selectedIndexes) {
            model.addSelectionInterval(index, index);
        }
    }

    /**
     * Select source rows.
     *
     * @param selectedIndexes the selected indexes
     */
    protected void selectSource(int[] selectedIndexes) {
        sourceList.setSelectedIndices(selectedIndexes);
    }

    /**
     * Sets the sort order.
     *
     * @param index the index
     * @param isAscending the is ascending
     */
    protected void setSortOrder(int index, boolean isAscending) {
        destinationModel.setValueAt(
                Boolean.valueOf(isAscending), index, SortByTableModel.getSortOrderColumn());

        destinationModel.fireTableDataChanged();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceLoaded(com.sldeditor.datasource
     * .impl.GeometryTypeEnum, boolean)
     */
    @Override
    public void dataSourceLoaded(
            GeometryTypeEnum geometryType, boolean isConnectedToDataSourceFlag) {
        DataSourceInterface dataSource = DataSourceFactory.getDataSource();
        populateFieldNames(dataSource.getAllAttributes(false));
    }

    /**
     * Populate field names.
     *
     * @param fieldList the field list
     */
    public void populateFieldNames(List<String> fieldList) {
        this.fieldNameList = fieldList;

        updateLists();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.DataSourceUpdatedInterface#dataSourceAboutToUnloaded(org.geotools.
     * data.DataStore)
     */
    @Override
    public void dataSourceAboutToUnloaded(DataStore dataStore) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.sortby.SortOrderUpdateInterface#sortOrderUpdated()
     */
    @Override
    public void sortOrderUpdated() {
        if (parentObj != null) {
            parentObj.sortByUpdated(getText());
        }
    }
}
