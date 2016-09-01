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

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import org.geotools.filter.LiteralExpressionImpl;
import org.geotools.styling.Font;
import org.geotools.styling.StyleBuilder;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.widgets.FieldPanel;
import com.sldeditor.ui.widgets.ValueComboBox;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class FieldConfigFont wraps a button and text field that allows the selection of a font.
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigFont extends FieldConfigBase implements UndoActionInterface {

    private static final double DEFAULT_FONT_SIZE = 12.0;

    /** The default font value. */
    private String defaultValue = "Arial";

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The current font. */
    private Font currentFont = null;

    /** The font family list. */
    private static List<ValueComboBoxData> fontFamilyList = null;

    /** The combo box. */
    private ValueComboBox comboBox = null;

    /**
     * Instantiates a new field config string.
     *
     * @param panelId the panel id
     * @param id the id
     * @param label the label
     * @param valueOnly the value only
     */
    public FieldConfigFont(Class<?> panelId, FieldId id, String label, boolean valueOnly) {
        super(panelId, id, label, valueOnly);
    }

    /**
     * Creates the ui.
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.FieldConfigBase#createUI()
     */
    @Override
    public void createUI() {
        final UndoActionInterface parentObj = this;

        int xPos = getXPos();
        FieldPanel fieldPanel = createFieldPanel(xPos, getLabel());

        populateFontFamilyList();

        if(!fontFamilyList.isEmpty())
        {
            defaultValue = fontFamilyList.get(0).getKey();
        }

        comboBox = new ValueComboBox();
        comboBox.initialiseSingle(fontFamilyList);
        comboBox.setBounds(xPos + BasePanel.WIDGET_X_START, 0, BasePanel.WIDGET_STANDARD_WIDTH, BasePanel.WIDGET_HEIGHT);

        fieldPanel.add(comboBox);
        comboBox.setSelectValueKey(defaultValue);

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ValueComboBox comboBox = (ValueComboBox) e.getSource();
                if (comboBox.getSelectedItem() != null) {

                    Object newValueObj = comboBox.getSelectedValue().getKey();

                    if((oldValueObj == null) && comboBox.getItemCount() > 0)
                    {
                        oldValueObj = comboBox.getFirstItem().getKey();
                    }

                    UndoManager.getInstance().addUndoEvent(new UndoEvent(parentObj, getFieldId(), oldValueObj, newValueObj));

                    oldValueObj = newValueObj;

                    valueUpdated();
                }
            }
        });

        if(!isValueOnly())
        {
            setAttributeSelectionPanel(fieldPanel.internalCreateAttrButton(Font.class, this));
        }
    }

    /**
     * Populate font family list.
     */
    private synchronized void populateFontFamilyList() {
        if(fontFamilyList == null)
        {
            fontFamilyList = new ArrayList<ValueComboBoxData>();

            String[] families = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            for(String fontFamily : families)
            {
                fontFamilyList.add(new ValueComboBoxData(fontFamily, fontFamily, getPanelId()));
            }
        }
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
        if(comboBox != null)
        {
            comboBox.setEnabled(enabled);
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
        Expression expression = null;

        if(this.comboBox != null)
        {
            expression = getFilterFactory().literal(comboBox.getSelectedValue().getKey());
        }

        return expression;
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
        if((attributeSelectionPanel != null) && !isValueOnly())
        {
            return attributeSelectionPanel.isEnabled();
        }
        else
        {
            if(comboBox != null)
            {
                return comboBox.isEnabled();
            }
        }

        return false;
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
        if(objValue instanceof String)
        {
            String sValue = (String) objValue;

            populateField(sValue);
        }
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
        Font font = getFont();

        if(font != null)
        {
            if(font.getFamily().size() > 0)
            {
                return font.getFamily().get(0).toString();
            }
        }
        return null;
    }

    /**
     * Gets the font value.
     *
     * @return the font value
     */
    @Override
    public Font getFont()
    {
        return currentFont;
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject)
    {
        if((comboBox != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getOldValue() instanceof String)
            {
                String oldValue = (String)undoRedoObject.getOldValue();

                comboBox.setSelectValueKey(oldValue);
            }
        }
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo/redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject)
    {
        if((comboBox != null) && (undoRedoObject != null))
        {
            if(undoRedoObject.getNewValue() instanceof String)
            {
                String newValue = (String)undoRedoObject.getNewValue();

                comboBox.setSelectValueKey(newValue);
            }
        }
    }

    /**
     * Sets the test string value.
     *
     * @param fieldId the field id
     * @param testValue the test value
     */
    @Override
    public void setTestValue(FieldId fieldId, String testValue) {
        if(comboBox != null)
        {
            comboBox.setSelectValueKey(testValue);
        }
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(Font value) {

        if(setFont(value))
        {
            valueUpdated();
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
        FieldConfigFont copy = null;

        if(fieldConfigBase != null)
        {
            copy = new FieldConfigFont(fieldConfigBase.getPanelId(),
                    fieldConfigBase.getFieldId(),
                    fieldConfigBase.getLabel(),
                    fieldConfigBase.isValueOnly());
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
        if(comboBox != null)
        {
            comboBox.setVisible(visible);
        }
    }

    /**
     * Sets the font.
     *
     * @param font the new font
     * @return true, if successful
     */
    private boolean setFont(Font font)
    {
        String fontName = "";
        boolean differentFamilyName = false;

        if(font != null)
        {
            fontName = getFontName(font);

            String oldFontName = getFontName(currentFont);
            differentFamilyName = (fontName.compareTo(oldFontName) != 0);
        }
        else
        {
            differentFamilyName = (currentFont != null);
        }

        if(comboBox == null)
        {
            return false;
        }
        comboBox.setSelectValueKey(fontName);
        currentFont = font;

        if(differentFamilyName)
        {
            UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, fontName));

            oldValueObj = fontName;
        }

        return differentFamilyName;
    }

    /**
     * Gets the font name.
     *
     * @param font the font
     * @return the font name
     */
    private String getFontName(Font font) {
        String fontName = "";
        if(font != null)
        {
            List<Expression> expressionList = font.getFamily();

            if((expressionList != null) && !expressionList.isEmpty())
            {
                LiteralExpressionImpl expression = (LiteralExpressionImpl) expressionList.get(0);

                fontName = expression.toString();
            }
        }
        return fontName;
    }
}
