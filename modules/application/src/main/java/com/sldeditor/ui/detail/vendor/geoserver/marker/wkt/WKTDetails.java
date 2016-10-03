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
package com.sldeditor.ui.detail.vendor.geoserver.marker.wkt;

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
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class WKTDetails panel contains all the fields to configure 
 * an WKT strings.
 * 
 * @author Robert Ward (SCISYS)
 */
public class WKTDetails extends StandardPanel implements PopulateDetailsInterface, 
UpdateSymbolInterface, UndoActionInterface, FieldConfigStringButtonInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private WKTUpdateInterface parentObj = null;

    /** The old value obj. */
    private Object oldValueObj = null;

    /**
     * Instantiates a new feature type style details.
     *
     * @param parentObj the parent obj
     */
    public WKTDetails(WKTUpdateInterface parentObj, FunctionNameInterface functionManager)
    {
        super(WKTDetails.class, functionManager);

        this.parentObj = parentObj;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, this, "symboltype/WKT.xml");

        registerForTextFieldButton(FieldIdEnum.WKT, this);
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /* (non-Javadoc)
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

        if(wellKnownName != null)
        {
            fieldConfigVisitor.populateTextField(FieldIdEnum.WKT, wellKnownName);
        }
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /* (non-Javadoc)
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
        if(!Controller.getInstance().isPopulating())
        {
            if(parentObj != null)
            {
                parentObj.wktValueUpdated();
            }
        }
    }

    /**
     * Gets the field data manager.
     *
     * @return the field data manager
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return fieldConfigManager;
    }

    /**
     * Checks if is data present.
     *
     * @return true, if is data present
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return true;
    }

    /**
     * Gets the expression.
     *
     * @return the expression
     */
    public Expression getExpression() {
        String string = fieldConfigVisitor.getText(FieldIdEnum.WKT);
        
        Expression expression = getFilterFactory().literal(string);
        
        return expression;
    }

    /**
     * Revert to default value.
     */
    public void revertToDefaultValue() {
        List<FieldConfigBase> fieldList = fieldConfigManager.getFields(null);

        for(FieldConfigBase field : fieldList)
        {
            if(field != null)
            {
                field.revertToDefaultValue();
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

        fieldConfigVisitor.populateTextField(FieldIdEnum.WKT, (String) oldValue);
    }

    /**
     * Redo action.
     *
     * @param undoRedoObject the undo redo object
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        String newValue = (String)undoRedoObject.getNewValue();

        fieldConfigVisitor.populateTextField(FieldIdEnum.WKT, (String) newValue);
    }

    /**
     * Button pressed.
     *
     * @param buttonExternal the button external
     */
    @Override
    public void buttonPressed(Component buttonExternal) {
        WKTDialog dlg = new WKTDialog();
        if(dlg.showDialog(fieldConfigVisitor.getText(FieldIdEnum.WKT)));
        {
            String wktString = dlg.getWKTString();
            
            fieldConfigVisitor.populateTextField(FieldIdEnum.WKT, wktString);

            UndoManager.getInstance().addUndoEvent(new UndoEvent(this, FieldIdEnum.WKT, oldValueObj, wktString));

            oldValueObj = wktString;

            EventQueue.invokeLater(new Runnable(){

                public void run(){
                    updateSymbol();
                }
            });
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
