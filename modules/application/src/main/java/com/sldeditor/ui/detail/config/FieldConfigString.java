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
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.widgets.FieldPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.opengis.filter.expression.Expression;

/**
 * The Class FieldConfigString wraps a text field GUI component and an optional
 * value/attribute/expression drop down, Can restrict the maximum number of characters in the
 * string. Able to mark the string as being a regular expression which removes a preceding '.'
 * before display and prepends when field is applied.
 *
 * <p>Supports undo/redo functionality.
 *
 * <p>Instantiated by {@link com.sldeditor.ui.detail.config.panelconfig.ReadPanelConfig}
 *
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigString extends FieldConfigBase implements UndoActionInterface {

    /** The text field. */
    private JTextField textField;

    /** The default value. */
    private String defaultValue = "";

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The button text. */
    private String buttonText = null;

    /** The button pressed listener list. */
    private List<FieldConfigStringButtonInterface> buttonPressedListenerList = null;

    /** The maximum string size. */
    private int maximumStringSize = -1;

    /** The regular expression string flag. */
    private boolean isRegExpString = false;

    /** The Class JTextFieldLimit. */
    public class JTextFieldLimit extends PlainDocument {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The maximum number of characters. */
        private int limit;

        /**
         * Instantiates a new j text field limit.
         *
         * @param limit the limit
         */
        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.swing.text.PlainDocument#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
         */
        @Override
        public void insertString(int offset, String str, AttributeSet attr)
                throws BadLocationException {
            if (str == null) return;

            if ((getLength() + str.length()) <= limit) {
                String oldValue = this.getText(0, getLength());
                super.insertString(offset, str, attr);
                String newValue = this.getText(0, getLength());

                valueStored(oldValue, newValue);
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see javax.swing.text.AbstractDocument#remove(int, int)
         */
        @Override
        public void remove(int offs, int len) throws BadLocationException {
            try {
                String oldValue = this.getText(0, getLength());
                super.remove(offs, len);
                String newValue = this.getText(0, getLength());

                valueStored(oldValue, newValue);
            } catch (BadLocationException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     * @param buttonText the button text
     */
    public FieldConfigString(FieldConfigCommonData commonData, String buttonText) {
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
        if (textField == null) {
            int xPos = getXPos();
            FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

            textField = new TextFieldPropertyChange();
            textField.setBounds(
                    xPos + BasePanel.WIDGET_X_START,
                    0,
                    this.isValueOnly()
                            ? BasePanel.WIDGET_EXTENDED_WIDTH
                            : BasePanel.WIDGET_STANDARD_WIDTH,
                    BasePanel.WIDGET_HEIGHT);
            fieldPanel.add(textField);

            textField.addPropertyChangeListener(
                    TextFieldPropertyChange.TEXT_PROPERTY,
                    new PropertyChangeListener() {

                        /*
                         * (non-Javadoc)
                         *
                         * @see java.beans.PropertyChangeListener#propertyChange(java.beans. PropertyChangeEvent)
                         */
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            String originalValue = (String) evt.getOldValue();
                            String newValueObj = (String) evt.getNewValue();

                            valueStored(originalValue, newValueObj);
                        }
                    });

            if (maximumStringSize > 0) {
                textField.setDocument(new JTextFieldLimit(maximumStringSize));
            }

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
                        xPos + textField.getX() - buttonWidth - padding,
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
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
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
    public void internalSetEnabled(boolean enabled) {
        if (textField != null) {
            textField.setEnabled(enabled);
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

        if (this.textField != null) {
            expression = getFilterFactory().literal(textField.getText());
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
            if (textField != null) {
                return textField.isEnabled();
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
        if ((textField != null) && (getPanel().isValueReadable())) {
            String value = textField.getText();

            // Prepend the regular expression special character
            if (isRegExpString) {
                value = "." + value;
            }
            return value;
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
        if (textField != null) {
            if (undoRedoObject != null) {
                String oldValue = (String) undoRedoObject.getOldValue();

                boolean prevValue = this.isSuppressUndoEvents();
                this.setSuppressUndoEvents(true);
                textField.setText(oldValue);
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
        if (textField != null) {
            if (undoRedoObject != null) {
                String newValue = (String) undoRedoObject.getNewValue();

                boolean prevValue = this.isSuppressUndoEvents();
                this.setSuppressUndoEvents(true);
                textField.setText(newValue);
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
            buttonPressedListenerList = new ArrayList<>();
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
        String tmpValue;

        // If regular expression string remove initial '.'
        if (isRegExpString && value.length() > 1) {
            tmpValue = value.substring(1, 2);
        } else {
            tmpValue = value;
        }

        if (textField != null) {
            textField.setText(tmpValue);
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
        FieldConfigString copy = null;

        if (fieldConfigBase != null) {
            copy = new FieldConfigString(fieldConfigBase.getCommonData(), this.buttonText);
            copy.setMaximumStringSize(this.maximumStringSize);
            copy.setRegExpString(this.isRegExpString);
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
        if (textField != null) {
            textField.setVisible(visible);
        }
    }

    /**
     * Value stored.
     *
     * @param originalValue the original value
     * @param newValueObj the new value obj
     */
    protected void valueStored(String originalValue, String newValueObj) {
        if ((originalValue.compareTo(newValueObj) != 0)) {
            if (!isSuppressUndoEvents()) {
                UndoManager.getInstance()
                        .addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, newValueObj));

                oldValueObj = originalValue;
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

    /**
     * Sets the maximum string size.
     *
     * @param maximumStringSize the new maximum string size
     */
    public void setMaximumStringSize(int maximumStringSize) {
        this.maximumStringSize = maximumStringSize;
    }

    /**
     * Sets the reg exp string.
     *
     * @param isRegExpString the isRegExpString to set
     */
    public void setRegExpString(boolean isRegExpString) {
        this.isRegExpString = isRegExpString;
    }
}
