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

import javax.swing.JTabbedPane;

import org.geotools.styling.UserLayer;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.widgets.FieldPanel;

/**
 * The Class FieldConfigInlineFeature wraps a text field GUI
 * component allowing the configuration of inline features
 * <p>
 * Supports undo/redo functionality. 
 * <p>
 * Instantiated by {@link com.sldeditor.ui.detail.config.ReadPanelConfig} 
 * 
 * @author Robert Ward (SCISYS)
 */
public class FieldConfigInlineFeature extends FieldConfigBase implements UndoActionInterface, InlineFeatureUpdateInterface {

    /** The default value. */
    private String defaultValue = "";

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The number of rows the text area will have. */
    private int NO_OF_ROWS = 20;

    /** The inline GML. */
    private InlineGMLPreviewPanel inlineGML = null;

    /** The inline feature. */
    private InlineFeaturePanel inlineFeature = null;

    /** The tabbed pane. */
    private JTabbedPane tabbedPane;

    /**
     * Instantiates a new field config string.
     *
     * @param commonData the common data
     */
    public FieldConfigInlineFeature(FieldConfigCommonData commonData) {
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
        FieldPanel fieldPanel = createFieldPanel(xPos, BasePanel.WIDGET_HEIGHT * NO_OF_ROWS , getLabel());

        inlineGML = new InlineGMLPreviewPanel(this, NO_OF_ROWS);
        inlineFeature = new InlineFeaturePanel(this, NO_OF_ROWS);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.addTab(Localisation.getString(FieldConfigBase.class, "FieldConfigInlineFeature.feature"), null, inlineFeature,
                Localisation.getString(FieldConfigBase.class, "FieldConfigInlineFeature.feature.tooltip"));
        tabbedPane.addTab(Localisation.getString(FieldConfigBase.class, "FieldConfigInlineFeature.gml"), null, inlineGML,
                Localisation.getString(FieldConfigBase.class, "FieldConfigInlineFeature.gml.tooltip"));
        tabbedPane.setBounds(0, 0, inlineGML.getWidth(), inlineGML.getHeight());

        fieldPanel.add(tabbedPane);
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
    public void internal_setEnabled(boolean enabled)
    {
        if(inlineGML != null)
        {
            inlineGML.setEnabled(enabled);
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

        if(inlineGML != null)
        {
            String text = inlineGML.getInlineFeatures();
            if((text != null) && !text.isEmpty())
            {
                expression = getFilterFactory().literal(text);
            }
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
        if(inlineGML != null)
        {
            return inlineGML.isEnabled();
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
            populateField((String)objValue);
        }
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
        if(inlineGML != null)
        {
            return inlineGML.getInlineFeatures();
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
        if(undoRedoObject != null)
        {
            if(undoRedoObject.getOldValue() instanceof String)
            {
                String oldValue = (String)undoRedoObject.getOldValue();

                UserLayer userLayer = DefaultSymbols.createNewUserLayer();

                InlineFeatureUtils.setInlineFeatures(userLayer, oldValue);
                if(inlineGML != null)
                {
                    inlineGML.setInlineFeatures(oldValue);
                }

                if(inlineFeature != null)
                {
                    inlineFeature.setInlineFeatures(userLayer);
                }
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
        if(undoRedoObject != null)
        {
            if(undoRedoObject.getNewValue() instanceof String)
            {
                String newValue = (String)undoRedoObject.getNewValue();

                UserLayer userLayer = DefaultSymbols.createNewUserLayer();

                InlineFeatureUtils.setInlineFeatures(userLayer, newValue);

                if(inlineGML != null)
                {
                    inlineGML.setInlineFeatures(newValue);
                }

                if(inlineFeature != null)
                {
                    inlineFeature.setInlineFeatures(userLayer);
                }
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
    public void setTestValue(FieldIdEnum fieldId, String testValue) {
        UserLayer userLayer = DefaultSymbols.createNewUserLayer();

        InlineFeatureUtils.setInlineFeatures(userLayer, testValue);
        populateField(userLayer);
    }

    /**
     * Populate field.
     *
     * @param value the value
     */
    @Override
    public void populateField(UserLayer value) {
        String inlineFeaturesText = InlineFeatureUtils.getInlineFeaturesText(value);
        if(inlineGML != null)
        {
            inlineGML.setInlineFeatures(inlineFeaturesText);
        }

        if(inlineFeature != null)
        {
            inlineFeature.setInlineFeatures(value);
        }

        UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, new String(inlineFeaturesText)));

        oldValueObj = new String(inlineFeaturesText);

        valueUpdated();
    }

    /**
     * Creates a copy of the field.
     *
     * @param fieldConfigBase the field config base
     * @return the field config base
     */
    @Override
    protected FieldConfigBase createCopy(FieldConfigBase fieldConfigBase) {
        FieldConfigInlineFeature copy = null;
        if(fieldConfigBase != null)
        {
            copy = new FieldConfigInlineFeature(fieldConfigBase.getCommonData());
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
        if(tabbedPane != null)
        {
            tabbedPane.setVisible(visible);
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.config.inlinefeature.InlineFeatureUpdateInterface#inlineFeatureUpdated()
     */
    @Override
    public void inlineFeatureUpdated() {
        if(!Controller.getInstance().isPopulating())
        {
            String value = "";
            if(inlineFeature != null)
            {
                value = inlineFeature.getInlineFeatures();
                UndoManager.getInstance().addUndoEvent(new UndoEvent(this, getFieldId(), oldValueObj, new String(value)));

                oldValueObj = new String(value);

                if(inlineGML != null)
                {
                    inlineGML.setInlineFeatures(value);
                }

                valueUpdated();
            }
        }
    }
}
