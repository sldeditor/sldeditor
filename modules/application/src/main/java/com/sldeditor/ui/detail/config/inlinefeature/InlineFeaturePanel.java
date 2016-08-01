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

package com.sldeditor.ui.detail.config.inlinefeature;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeatureType;

import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTConversion;
import com.sldeditor.ui.detail.vendor.geoserver.marker.wkt.WKTDialog;
import com.sldeditor.ui.menucombobox.ArrowIcon;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import com.vividsolutions.jts.geom.Geometry;

/**
 * The Class InlineFeaturePanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class InlineFeaturePanel extends JPanel {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The crs combo box. */
    private ValueComboBox crsComboBox;

    /** The feature table. */
    private JTable featureTable;

    /** The model. */
    private InLineFeatureModel model;

    /** The column header. */
    private JTableHeader columnHeader;

    /** The rename popup. */
    private JPopupMenu renamePopup;

    /** The column text field. */
    private JTextField columnTextField;

    /** The table column. */
    private TableColumn tableColumn;

    /** The populating flag. */
    private boolean populatingFlag = false;

    /**
     * Instantiates a new inline feature panel.
     *
     * @param parent the parent
     * @param noOfRows the no of rows
     */
    public InlineFeaturePanel(InlineFeatureUpdateInterface parent, int noOfRows)
    {
        model = new InLineFeatureModel(parent);

        createUI(noOfRows);
    }

    /**
     * Creates the UI.
     *
     * @param noOfRows the no of rows
     */
    private void createUI(int noOfRows) {
        setLayout(new BorderLayout());

        int xPos = 0;
        int width = BasePanel.FIELD_PANEL_WIDTH - xPos - 20;
        int height = BasePanel.WIDGET_HEIGHT * (noOfRows - 1);
        this.setBounds(0, 0, width, height);

        // CRS dropdown
        JPanel topPanel = new JPanel();
        topPanel.add(createCRSList("CRS", xPos, topPanel));

        add(topPanel, BorderLayout.NORTH);

        // Feature table panel
        JPanel tablePanel = new JPanel();
        featureTable = new JTable(model);
        featureTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        featureTable.setColumnSelectionAllowed(true);
        featureTable.setAutoscrolls(true);
        featureTable.getTableHeader().setReorderingAllowed(false);
        featureTable.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(noOfRows - 2));
        ListSelectionModel selectionModel = featureTable.getSelectionModel();

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if(!e.getValueIsAdjusting())
                {
                    int row = featureTable.getSelectedRow();
                    if(row >= 0)
                    {
                        int column = featureTable.getSelectedColumn();

                        if(column == model.getGeometryFieldIndex())
                        {
                            WKTDialog wktDialog = new WKTDialog();

                            String geometryString = (String) model.getValueAt(row, column).toString();
                            if(wktDialog.showDialog(geometryString))
                            {
                                String crsCode = null;
                                if(crsComboBox.getSelectedValue() != null)
                                {
                                    crsCode = crsComboBox.getSelectedValue().getKey();
                                }
                                Geometry geometry = WKTConversion.convertToGeometry(wktDialog.getWKTString(), crsCode);

                                model.updateGeometry(row, geometry);
                            }
                        }
                        featureTable.clearSelection();
                    }
                }
            }
        });

        JScrollPane scrollPanel = new JScrollPane(featureTable);
        scrollPanel.setBounds(xPos, 0, BasePanel.FIELD_PANEL_WIDTH, getRowY(noOfRows - 2));

        tablePanel.add(scrollPanel);
        add(tablePanel, BorderLayout.CENTER);

        // Buttons
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        // Feature panel
        JPanel addFeaturePanel = new JPanel();
        addFeaturePanel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(FieldConfigBase.class, "InlineFeature.features")));

        JButton addButton = new JButton(Localisation.getString(FieldConfigBase.class, "InlineFeature.addfeature"));
        addFeaturePanel.add(addButton);

        JButton removeButton = new JButton(Localisation.getString(FieldConfigBase.class, "InlineFeature.removefeature"));
        addFeaturePanel.add(removeButton);

        bottomPanel.add(addFeaturePanel);

        // Attribute panel
        JPanel attributePanel = new JPanel();
        attributePanel.setBorder(BorderFactory.createTitledBorder(Localisation.getString(FieldConfigBase.class, "InlineFeature.attributes")));

        JButton addColumnButton = new JButton(Localisation.getString(FieldConfigBase.class, "InlineFeature.addattribute"));
        addColumnButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.addNewColumn();
            }
        });
        attributePanel.add(addColumnButton);

        JButton removeColumnButton = new JButton(Localisation.getString(FieldConfigBase.class, "InlineFeature.removeattribute"));
        ArrowIcon arrow = new ArrowIcon(SwingConstants.SOUTH, true);
        removeColumnButton.setIcon(arrow);
        removeColumnButton.setHorizontalTextPosition(AbstractButton.LEFT);
        removeColumnButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPopupMenu popupMenu = new JPopupMenu();

                List<String> columnNames = model.getColumnNames();

                for(String columnName : columnNames)
                {
                    JMenuItem menuItem = new JMenuItem(columnName);
                    menuItem.addActionListener(new ActionListener()
                    {
                        public void actionPerformed(ActionEvent event)
                        {
                            model.removeColumn(columnName);
                        }
                    });
                    popupMenu.add(menuItem);
                }

                if(e != null)
                {
                    popupMenu.show(removeColumnButton,
                            removeColumnButton.getX() - removeColumnButton.getWidth(),
                            removeColumnButton.getY());
                }
            }
        });
        attributePanel.add(removeColumnButton);

        bottomPanel.add(attributePanel);
        add(bottomPanel, BorderLayout.SOUTH);

        //
        // Set up the column header editing
        //
        columnHeader = featureTable.getTableHeader();
        columnHeader.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent event)
            {
                if (event.getClickCount() == 2)
                {
                    editColumnAt(event.getPoint());
                }
            }
        });

        columnTextField = new JTextField();
        columnTextField.setBorder(null);
        columnTextField.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                renameColumn();
            }
        });

        renamePopup = new JPopupMenu();
        renamePopup.setBorder(new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY));
        renamePopup.add(columnTextField);
    }

    /**
     * Creates the crs list.
     *
     * @param label the label
     * @param xPos the x pos
     * @param parentPanel the parent panel
     * @return the value combo box
     */
    private ValueComboBox createCRSList(String label, int xPos, JPanel parentPanel) {
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.TRAILING);
        lbl.setBounds(xPos, 0, BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        parentPanel.add(lbl);

        // Populate data list
        List<ValueComboBoxData> crsDataList = CoordManager.getInstance().getCRSList();
        crsComboBox = new ValueComboBox();
        crsComboBox.initialiseSingle(crsDataList);
        crsComboBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_EXTENDED_WIDTH, BasePanel.WIDGET_HEIGHT);
        parentPanel.add(crsComboBox);
        crsComboBox.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!isPopulating())
                {
                    model.updateCRS(crsComboBox.getSelectedValue());
                }
            }});
        return crsComboBox;
    }

    /**
     * Gets the row y.
     *
     * @param row the row
     * @return the row y
     */
    private static int getRowY(int row)
    {
        return BasePanel.WIDGET_HEIGHT * row;
    }

    /**
     * Sets the inline features.
     *
     * @param userLayer the user layer
     */
    public void setInlineFeatures(UserLayer userLayer)
    {
        if(userLayer != null)
        {
            String crsCode = "";
            SimpleFeatureType inlineFeatureType = userLayer.getInlineFeatureType();
            if(inlineFeatureType != null)
            {
                crsCode = CoordManager.getInstance().getCRSCode(inlineFeatureType.getCoordinateReferenceSystem());
            }

            setPopulating(true);
            if(crsCode.isEmpty())
            {
                crsComboBox.setSelectedIndex(-1);
            }
            else
            {
                crsComboBox.setSelectValueKey(crsCode);
            }
            setPopulating(false);
            model.populate(userLayer);
        }
    }

    /**
     * Sets the populating flag.
     *
     * @param populateFlag the new populating
     */
    private void setPopulating(boolean populateFlag) {
        this.populatingFlag = populateFlag;
    }

    /**
     * Checks if is populating.
     *
     * @return true, if is populating
     */
    private boolean isPopulating()
    {
        return this.populatingFlag;
    }

    /**
     * Edits the column at.
     *
     * @param p the point at which the mouse was clicked
     */
    private void editColumnAt(Point p)
    {
        int columnIndex = columnHeader.columnAtPoint(p);

        if((columnIndex != -1) && (columnIndex != model.getGeometryFieldIndex()))
        {
            tableColumn = columnHeader.getColumnModel().getColumn(columnIndex);
            Rectangle columnRectangle = columnHeader.getHeaderRect(columnIndex);

            columnTextField.setText(tableColumn.getHeaderValue().toString());
            renamePopup.setPreferredSize(
                    new Dimension(columnRectangle.width, columnRectangle.height - 1));
            renamePopup.show(columnHeader, columnRectangle.x, 0);

            columnTextField.requestFocusInWindow();
            columnTextField.selectAll();
        }
    }

    /**
     * Rename column.
     */
    private void renameColumn()
    {
        tableColumn.setHeaderValue(columnTextField.getText());
        renamePopup.setVisible(false);
        columnHeader.repaint();
    }

    /**
     * Gets the inline features.
     *
     * @return the inline features
     */
    public String getInlineFeatures() {
        return model.getInlineFeatures();
    }
}
