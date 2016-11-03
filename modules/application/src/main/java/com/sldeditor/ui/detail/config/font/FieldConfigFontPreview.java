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
package com.sldeditor.ui.detail.config.font;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigFontPreview wraps a text field GUI component showing a font preview.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFontPreview extends FieldConfigBase implements UndoActionInterface {

    /** The text field. */
    private JTextArea textField;

    /** The default value. */
    private String defaultValue = "";

    /** The Constant DEFAULT_FONT_SIZE. */
    private static final double DEFAULT_FONT_SIZE = 12.0;

    /** The Constant sampleText. */
    private static final String sampleText =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ\n" +
                    "abcdefghijklmnopqrstuvwxyz\n" +
                    "0123456789\n" +
                    "The quick brown fox jumped over the lazy dog";

    /** The Constant sampleTextLines. */
    private static final int sampleTextLines = 4;

    /** The Constant styles. */
    private static final String[] styles = {"Normal", "Italic"};

    /** The Constant weights. */
    private static final String[] weights = {"Normal", "Bold"};

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigFontPreview(FieldConfigCommonData commonData) {
        super(commonData);
    }

    /**
     * Creates the ui.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {

        int xPos = getXPos();
        int height = getRowY(sampleTextLines);
        int width = BasePanel.WIDGET_EXTENDED_WIDTH * 2;
        FieldPanel fieldPanel = createFieldPanel(xPos, height, getLabel());

        textField = new JTextArea();
        textField.setBounds(xPos + BasePanel.WIDGET_X_START, 0, width, height);
        textField.setWrapStyleWord(true);
        textField.setLineWrap(false);
        textField.setText(sampleText);
        textField.setRows(sampleTextLines);
        JScrollPane scrollPane = new JScrollPane(textField);
        scrollPane.setBounds(xPos + BasePanel.WIDGET_X_START, 0, width, height);
        fieldPanel.add(scrollPane);
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
     * Attribute selection.
     *
     * @param field the field
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.AttributeButtonSelectionInterface#attributeSelection(java.lang.String)
     */
    @Override
    public void attributeSelection(String field)
    {
        // Not used
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        if(textField != null)
        {
            textField.setEnabled(enabled);
        }
    }

    /**
     * Generate expression.
     *
     * @return the expression
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#generateExpression()
     */
    @Override
    protected Expression generateExpression()
    {
        return null;
    }

    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        return true;
    }

    /**
     * Revert to default value.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#revertToDefaultValue()
     */
    @Override
    public void revertToDefaultValue()
    {
        populateField(defaultValue);
    }

    /**
     * Sets the default value.
     *
     * @param defaultValue the new default value
     */
    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the string value.
     *
     * @return the string value
     */
    @Override
    public String getStringValue()
    {
        if(textField != null)
        {
            return textField.getText();
        }
        return null;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        // Do nothing
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject)
    {
        // Do nothing
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
     * Populate string field, overridden if necessary.
     *
     * @param value the value
     */
    @Override
    public void populateField(String value) {
        StyleBuilder styleBuilder = new StyleBuilder();

        Font font = styleBuilder.createFont(defaultValue, DEFAULT_FONT_SIZE);

        populateField(font);
    }

    /**
     * Populate field.
     *
     * @param font the font
     */
    @Override
    public void populateField(Font font) {
        if((textField != null) && (font != null))
        {
            int styleIndex = 0;
            int weightIndex = 0;
            String familyName = font.getFamily().get(0).toString();
            String styleName = ((Literal) font.getStyle()).getValue().toString();
            for (int index = 0; index < styles.length; index++) {
                if (styles[index].equalsIgnoreCase(styleName)) {
                    styleIndex = index;
                    break;
                }
            }

            String weightName = ((Literal) font.getWeight()).getValue().toString();
            for (int index = 0; index < weights.length; index++) {
                if (weights[index].equalsIgnoreCase(weightName)) {
                    weightIndex = index;
                    break;
                }
            }

            StringBuilder sb = new StringBuilder(familyName);
            if (weightIndex == 0) {
                if (styleIndex == 0) {
                    sb.append("-PLAIN-");
                } else {
                    sb.append("-ITALIC-");
                }
            } else {
                if (styleIndex == 0) {
                    sb.append("-BOLD-");
                } else {
                    sb.append("-BOLDITALIC-");
                }
            }

            // Get font size
            int size = 12;
            Literal sizeExpression = (Literal) font.getSize();
            Object obj = sizeExpression.getValue();
            if(obj instanceof Number)
            {
                Number number = (Number) obj;
                size = number.intValue();
            }
            else if(obj instanceof String)
            {
                size = Double.valueOf((String)obj).intValue();
            }

            sb.append(size);

            java.awt.Font sampleFont = java.awt.Font.decode(sb.toString());

            textField.setFont(sampleFont);
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
        FieldConfigFontPreview copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigFontPreview(fieldConfigBase.getCommonData());
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
        if(textField != null)
        {
            textField.setVisible(visible);
        }
    }

    /**
     * Populate expression.
     *
     * @param objValue the obj value
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#populateExpression(java.lang.Object)
     */
    @Override
    public void populateExpression(Object objValue)
    {
        // Do nothing
    }
}
