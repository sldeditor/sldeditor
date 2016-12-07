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
package com.sldeditor.ui.detail.config.symboltype.ttf;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.List;

import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.ttf.CharMap4;

/**
 * The Class TTFDetails panel contains all the fields to configure an TrueType fonts.
 * 
 * @author Robert Ward (SCISYS)
 */
public class TTFDetails extends StandardPanel implements PopulateDetailsInterface,
        UpdateSymbolInterface, UndoActionInterface, FieldConfigStringButtonInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/marker/ttf/PanelConfig_TTFSymbol.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private TTFUpdateInterface parentObj = null;

    /** The old value obj. */
    private Object oldValueObj = null;

    /**
     * Instantiates a new feature type style details.
     *
     * @param parentObj the parent obj
     * @param functionManager the function manager
     */
    public TTFDetails(TTFUpdateInterface parentObj, FunctionNameInterface functionManager) {
        super(TTFDetails.class, functionManager);

        this.parentObj = parentObj;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, this, PANEL_CONFIG);

        registerForTextFieldButton(FieldIdEnum.TTF_SYMBOL, this);
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
        // Do nothing
    }

    /**
     * Populate expression.
     *
     * @param wellKnownName the well known name
     */
    public void populateExpression(String wellKnownName) {

        if (wellKnownName != null) {
            fieldConfigVisitor.populateTextField(FieldIdEnum.TTF_SYMBOL, wellKnownName);
        }
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged()
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {
            if (parentObj != null) {
                parentObj.ttfValueUpdated();
            }
        }
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        return fieldConfigManager;
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent() {
        return true;
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        String string = fieldConfigVisitor.getText(FieldIdEnum.TTF_SYMBOL);

        Expression expression = getFilterFactory().literal(string);

        return expression;
    }

    /**
     * Revert to default value.
     */
    public void revertToDefaultValue() {
        List<FieldConfigBase> fieldList = fieldConfigManager.getFields(null);

        if (fieldList != null) {
            for (FieldConfigBase field : fieldList) {
                if (field != null) {
                    field.revertToDefaultValue();
                }
            }
        }
    }

    /**
     * Undo action.
     *
     * @param undoRedoObject the undo redo object
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        Object oldValue = undoRedoObject.getOldValue();

        fieldConfigVisitor.populateTextField(FieldIdEnum.TTF_SYMBOL, (String) oldValue);
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        String newValue = (String) undoRedoObject.getNewValue();

        fieldConfigVisitor.populateTextField(FieldIdEnum.TTF_SYMBOL, (String) newValue);
    }

    /**
     * Button pressed.
     *
     * @param buttonExternal the button external
     */
    @Override
    public void buttonPressed(Component buttonExternal) {
        CharMap4 charMap4 = new CharMap4();

        charMap4.loadConfig();

        charMap4.setTTFString(fieldConfigVisitor.getText(FieldIdEnum.TTF_SYMBOL));

        String selectedChar = charMap4.showDialog();

        if (selectedChar != null) {
            fieldConfigVisitor.populateTextField(FieldIdEnum.TTF_SYMBOL, selectedChar);

            UndoManager.getInstance().addUndoEvent(
                    new UndoEvent(this, FieldIdEnum.TTF_SYMBOL, oldValueObj, selectedChar));

            oldValueObj = selectedChar;

            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    updateSymbol();
                }
            });
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        // No vendor options
    }
}
