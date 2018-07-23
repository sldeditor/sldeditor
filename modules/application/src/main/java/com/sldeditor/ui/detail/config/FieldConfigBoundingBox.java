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

package com.sldeditor.ui.detail.config;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.coordinate.CoordManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.filter.expression.Expression;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The Class FieldConfigBoundingBox wraps bounding box fields.
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigBoundingBox extends FieldConfigBase implements UndoActionInterface {

    /** The x min text field. */
    private JTextField xMinTextField;

    /** The x max text field. */
    private JTextField xMaxTextField;

    /** The y min text field. */
    private JTextField yMinTextField;

    /** The y max text field. */
    private JTextField yMaxTextField;

    /** The crs combo box. */
    private ValueComboBox crsComboBox;

    /** The old value obj. */
    private Object oldValueObj;

    /**
     * Instantiates a new field config boolean.
     *
     * @param commonData the common data
     */
    public FieldConfigBoundingBox(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Gets the row y.
     *
     * @param row the row
     * @return the row y
     */
    private int getRowY(int row) {
        return (row * BasePanel.WIDGET_HEIGHT);
    }

    /** Creates the ui. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        if (xMinTextField == null) {
            int xPos = getXPos();
            FieldPanel fieldPanel = createFieldPanel(xPos, "");

            int row = 0;
            xMinTextField =
                    createRow(
                            Localisation.getField(
                                    FieldConfigBase.class, "FieldConfigBoundingBox.minx"),
                            xPos,
                            fieldPanel,
                            row);
            xMaxTextField =
                    createRow(
                            Localisation.getField(
                                    FieldConfigBase.class, "FieldConfigBoundingBox.maxx"),
                            xPos,
                            fieldPanel,
                            ++row);
            yMinTextField =
                    createRow(
                            Localisation.getField(
                                    FieldConfigBase.class, "FieldConfigBoundingBox.miny"),
                            xPos,
                            fieldPanel,
                            ++row);
            yMaxTextField =
                    createRow(
                            Localisation.getField(
                                    FieldConfigBase.class, "FieldConfigBoundingBox.maxy"),
                            xPos,
                            fieldPanel,
                            ++row);
            crsComboBox =
                    createCRSList(
                            Localisation.getField(
                                    FieldConfigBase.class, "FieldConfigBoundingBox.crs"),
                            xPos,
                            fieldPanel,
                            ++row);

            Dimension preferredSize =
                    new Dimension(
                            (int) fieldPanel.getPreferredSize().getWidth(),
                            crsComboBox.getY() + crsComboBox.getHeight());

            fieldPanel.setPreferredSize(preferredSize);

            if (!isValueOnly()) {
                setAttributeSelectionPanel(
                        fieldPanel.internalCreateAttrButton(Boolean.class, this, isRasterSymbol()));
            }
        }
    }

    /**
     * Creates the crs list.
     *
     * @param label the label
     * @param xPos the x pos
     * @param fieldPanel the field panel
     * @param row the row
     * @return the value combo box
     */
    private ValueComboBox createCRSList(String label, int xPos, FieldPanel fieldPanel, int row) {
        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.TRAILING);
        lbl.setBounds(xPos, getRowY(row), BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(lbl);

        // Populate data list
        List<ValueComboBoxData> crsDataList = CoordManager.getInstance().getCRSList();
        crsComboBox = new ValueComboBox();
        crsComboBox.initialiseSingle(crsDataList);
        crsComboBox.setBounds(
                xPos + BasePanel.WIDGET_X_START,
                getRowY(row),
                this.isValueOnly()
                        ? BasePanel.WIDGET_EXTENDED_WIDTH
                        : BasePanel.WIDGET_STANDARD_WIDTH,
                BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(crsComboBox);
        crsComboBox.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        valueUpdated();
                    }
                });
        return crsComboBox;
    }

    /**
     * Creates the row.
     *
     * @param label the label
     * @param xPos the x pos
     * @param fieldPanel the field panel
     * @param row the row
     * @return the j text field
     */
    private JTextField createRow(String label, int xPos, FieldPanel fieldPanel, int row) {
        final UndoActionInterface parentObj = this;

        JLabel lbl = new JLabel(label);
        lbl.setHorizontalAlignment(SwingConstants.TRAILING);
        lbl.setBounds(xPos, getRowY(row), BasePanel.LABEL_WIDTH, BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(lbl);

        JTextField textField = new JTextField();
        textField.setBounds(
                xPos + BasePanel.WIDGET_X_START,
                getRowY(row),
                this.isValueOnly()
                        ? BasePanel.WIDGET_EXTENDED_WIDTH
                        : BasePanel.WIDGET_STANDARD_WIDTH,
                BasePanel.WIDGET_HEIGHT);
        fieldPanel.add(textField);

        textField.addFocusListener(
                new FocusListener() {
                    private String originalValue = "";

                    @Override
                    public void focusGained(FocusEvent e) {
                        originalValue = textField.getText();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        String newValueObj = textField.getText();

                        if (originalValue.compareTo(newValueObj) != 0) {
                            if (!FieldConfigBoundingBox.this.isSuppressUndoEvents()) {
                                UndoManager.getInstance()
                                        .addUndoEvent(
                                                new UndoEvent(
                                                        parentObj,
                                                        getFieldId(),
                                                        oldValueObj,
                                                        newValueObj));

                                oldValueObj = originalValue;
                            }
                            valueUpdated();
                        }
                    }
                });

        return textField;
    }

    /**
     * Attribute selection.
     *
     * @param field the field
     */
    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field) {
        // Does nothing
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void internal_setEnabled(boolean enabled) {
        if (this.crsComboBox != null) {
            crsComboBox.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression() {
        ReferencedEnvelope envelope = getBBox();

        return getFilterFactory().literal(envelope);
    }

    /**
     * Generates the bounding box from the ui.
     *
     * @return the b box
     */
    private ReferencedEnvelope getBBox() {
        if (xMinTextField == null) {
            return null;
        }

        double minX =
                xMinTextField.getText().isEmpty() ? 0.0 : Double.valueOf(xMinTextField.getText());
        double maxX =
                xMaxTextField.getText().isEmpty() ? 0.0 : Double.valueOf(xMaxTextField.getText());
        double minY =
                yMinTextField.getText().isEmpty() ? 0.0 : Double.valueOf(yMinTextField.getText());
        double maxY =
                yMaxTextField.getText().isEmpty() ? 0.0 : Double.valueOf(yMaxTextField.getText());

        ValueComboBoxData crsDataValue = crsComboBox.getSelectedValue();

        CoordinateReferenceSystem crs = null;
        try {
            if (crsDataValue != null) {
                crs = CRS.decode(crsDataValue.getKey());
            }
        } catch (NoSuchAuthorityCodeException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (FactoryException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        ReferencedEnvelope envelope = new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
        return envelope;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        if ((attributeSelectionPanel != null) && !isValueOnly()) {
            return attributeSelectionPanel.isEnabled();
        } else {
            if (this.crsComboBox != null) {
                return crsComboBox.isEnabled();
            }
        }
        return false;
    }

    /** Revert to default value. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue() {
        if (this.crsComboBox != null) {
            crsComboBox.setSelectedIndex(-1);
        }
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue) {
        if (objValue instanceof ReferencedEnvelope) {
            populateField((ReferencedEnvelope) objValue);

            valueUpdated();
        }
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        ReferencedEnvelope envelope = getBBox();
        if (envelope == null) {
            return "";
        }
        return envelope.toString();
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            if (undoRedoObject.getOldValue() instanceof ReferencedEnvelope) {
                ReferencedEnvelope oldValue = (ReferencedEnvelope) undoRedoObject.getOldValue();

                populateField(oldValue);
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            if (undoRedoObject.getNewValue() instanceof ReferencedEnvelope) {
                ReferencedEnvelope oldValue = (ReferencedEnvelope) undoRedoObject.getNewValue();

                populateField(oldValue);
            }
        }
    }

    /**
     * Sets the test value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, ReferencedEnvelope testValue) {
        populateField(testValue);

        valueUpdated();
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(ReferencedEnvelope value) {
        xMinTextField.setText(Double.toString(value.getMinX()));
        xMaxTextField.setText(Double.toString(value.getMaxX()));
        yMinTextField.setText(Double.toString(value.getMinY()));
        yMaxTextField.setText(Double.toString(value.getMaxY()));

        String key = CoordManager.getInstance().getCRSCode(value.getCoordinateReferenceSystem());

        crsComboBox.setSelectValueKey(key);

        if (!FieldConfigBoundingBox.this.isSuppressUndoEvents()) {
            UndoManager.getInstance()
                    .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, value));
            oldValueObj = value;
        }
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigBoundingBox copy = null;
        if (fieldConfigBase != null) {
            copy = new FieldConfigBoundingBox(fieldConfigBase.getCommonData());
        }
        return copy;
    }

    /**
     * Sets the field visible.
     *
     * @param visible the new visible state
     */
    @Override
    public void setVisible(boolean visible) {
        if (crsComboBox != null) {
            crsComboBox.setVisible(visible);
        }
    }
}
