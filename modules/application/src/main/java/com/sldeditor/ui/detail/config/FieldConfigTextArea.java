/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigTextArea wraps a text area GUI component
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigTextArea extends FieldConfigBase implements UndoActionInterface {

    /** The text area. */
    private JTextArea textArea;

    /** The default value. */
    private String defaultValue = "";

    /** The old value obj. */
    private String oldValueObj = "";

    /** The button text. */
    private String buttonText = null;

    /** The button pressed listener list. */
    private List<FieldConfigStringButtonInterface> buttonPressedListenerList = null;

    /**
     * Instantiates a new field config text area.
     *
     * @param commonData the common data
     * @param buttonText the button text
     */
    public FieldConfigTextArea(FieldConfigCommonData commonData, String buttonText) {
        super(commonData);

        this.buttonText = buttonText;
    }

    /** Creates the ui. */
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        if (textArea == null) {
            int xPos = getXPos();
            FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

            int rows = 10;
            textArea = new JTextArea(30, rows);
            textArea.setBounds(xPos + BasePanel.WIDGET_X_START, 0,
                    this.isValueOnly() ? BasePanel.WIDGET_EXTENDED_WIDTH
                            : BasePanel.WIDGET_STANDARD_WIDTH,
                    rows * BasePanel.WIDGET_HEIGHT);
            
            textArea.setEditable(true);

            fieldPanel.add(textArea);

            textArea.getDocument().addDocumentListener(new DocumentListener() {

                @Override
                public void changedUpdate(DocumentEvent arg0) {
                    // Won't get called when using a PlainDocument
                }

                @Override
                public void insertUpdate(DocumentEvent arg0) {
                    try {
                        valueStored(arg0.getDocument().getText(0, arg0.getLength()));
                    } catch (BadLocationException e) {
                        ConsoleManager.getInstance().exception(FieldConfigTextArea.class, e);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent arg0) {
                    try {
                        valueStored(arg0.getDocument().getText(0, arg0.getLength()));
                    } catch (BadLocationException e) {
                        ConsoleManager.getInstance().exception(FieldConfigTextArea.class, e);
                    }
                }});

            if (buttonText != null) {
                final JButton buttonExternal = new JButton(buttonText);
                buttonExternal.addActionListener(
                        new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                externalButtonPressed(buttonExternal);
                            }
                        });

                int buttonWidth = 26;
                int padding = 3;
                buttonExternal.setBounds(
                        xPos + textArea.getX() - buttonWidth - padding,
                        0,
                        buttonWidth,
                        BasePanel.WIDGET_HEIGHT);
                fieldPanel.add(buttonExternal);
            }

            if (!isValueOnly()) {
                setAttributeSelectionPanel(
                        fieldPanel.internalCreateAttrButton(String.class, this, isRasterSymbol()));
            }
        }
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
        // Not used
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
        if (textArea != null) {
            textArea.setEnabled(enabled);
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
        Expression expression = null;

        if (this.textArea != null) {
            expression = getFilterFactory().literal(textArea.getText());
        }
        return expression;
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
            if (textArea != null) {
                return textArea.isEnabled();
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
        populateField(defaultValue);
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
        if (objValue instanceof String) {
            String sValue = (String) objValue;

            populateField(sValue);
        }
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue() {
        if (textArea != null) {
            if (getPanel().isValueReadable()) {
                String value = textArea.getText();

                return value;
            }
        }
        return null;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (textArea != null) {
            if (undoRedoObject != null) {
                String oldValue = (String) undoRedoObject.getOldValue();

                boolean prevValue = this.isSuppressUndoEvents();
                this.setSuppressUndoEvents(true);
                textArea.setText(oldValue);
                this.setSuppressUndoEvents(prevValue);
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
        if (textArea != null) {
            if (undoRedoObject != null) {
                String newValue = (String) undoRedoObject.getNewValue();

                boolean prevValue = this.isSuppressUndoEvents();
                this.setSuppressUndoEvents(true);
                textArea.setText(newValue);
                this.setSuppressUndoEvents(prevValue);
            }
        }
    }

    /**
     * Adds the button pressed listener.
     *
     * @param listener the listener
     */
    public void addButtonPressedListener(FieldConfigStringButtonInterface listener) {

        if (buttonPressedListenerList == null) {
            buttonPressedListenerList = new ArrayList<FieldConfigStringButtonInterface>();
        }

        if (listener != null) {
            buttonPressedListenerList.add(listener);
        }
    }

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        populateField(testValue);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {

        if (textArea != null) {
            textArea.setText(value);
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
        FieldConfigTextArea copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigTextArea(fieldConfigBase.getCommonData(), this.buttonText);
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
        if (textArea != null) {
            textArea.setVisible(visible);
        }
    }

    /**
     * Value stored.
     *
     * @param originalValue the original value
     * @param newValueObj the new value obj
     */
    protected void valueStored(String newValueObj) {
        if ((oldValueObj.compareTo(newValueObj) != 0)) {
            if (!isSuppressUndoEvents()) {
                UndoManager.getInstance()
                        .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, newValueObj));

                oldValueObj = newValueObj;
            }

            valueUpdated();
        }
    }

    /**
     * External button pressed.
     *
     * @param buttonExternal the button external
     */
    protected void externalButtonPressed(final JButton buttonExternal) {
        if (buttonPressedListenerList != null) {
            // CHECKSTYLE:OFF
            for (FieldConfigStringButtonInterface listener : buttonPressedListenerList) {
                listener.buttonPressed(buttonExternal);
            }
            // CHECKSTYLE:ON
        }
    }
}
