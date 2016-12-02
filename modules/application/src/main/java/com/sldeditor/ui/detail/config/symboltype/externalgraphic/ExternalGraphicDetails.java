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
package com.sldeditor.ui.detail.config.symboltype.externalgraphic;

import java.awt.Component;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.swing.JFileChooser;

import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ExternalGraphicImpl;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.undo.UndoActionInterface;
import com.sldeditor.common.undo.UndoEvent;
import com.sldeditor.common.undo.UndoInterface;
import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigStringButtonInterface;
import com.sldeditor.ui.detail.config.base.CurrentFieldState;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ExternalGraphicFilter;

/**
 * The Class ExternalGraphicDetails panel contains all the fields to configure an external graphic.
 * 
 * @author Robert Ward (SCISYS)
 */
public class ExternalGraphicDetails extends StandardPanel implements PopulateDetailsInterface,
        UpdateSymbolInterface, UndoActionInterface, FieldConfigStringButtonInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/marker/external/PanelConfig_ExternalGraphicSymbol.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private ExternalGraphicUpdateInterface parentObj = null;

    /** The external file url. */
    private URL externalFileURL;

    /** The old value obj. */
    private Object oldValueObj = null;

    /** The display relative paths flag. */
    private boolean useRelativePaths = true;
    
    /**
     * Instantiates a new feature type style details.
     *
     * @param parentObj the parent obj
     * @param functionManager the function manager
     */
    public ExternalGraphicDetails(ExternalGraphicUpdateInterface parentObj,
            FunctionNameInterface functionManager) {
        super(ExternalGraphicDetails.class, functionManager);

        this.parentObj = parentObj;
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, this, PANEL_CONFIG);

        registerForTextFieldButton(FieldIdEnum.EXTERNAL_GRAPHIC, this);

        for (FieldConfigBase fieldConfig : this.getFieldConfigList()) {
            CurrentFieldState fieldState = fieldConfig.getFieldState();
            fieldState.setFieldEnabled(true);
            fieldConfig.setFieldState(fieldState);
        }
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
            fieldConfigVisitor.populateTextField(FieldIdEnum.EXTERNAL_GRAPHIC, wellKnownName);
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
            String path = RelativePath.convert(externalFileURL, useRelativePaths);

            fieldConfigVisitor.populateTextField(FieldIdEnum.EXTERNAL_GRAPHIC, path);

            if (parentObj != null) {
                parentObj.externalGraphicValueUpdated();
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
        String string = fieldConfigVisitor.getText(FieldIdEnum.EXTERNAL_GRAPHIC);

        Expression expression = getFilterFactory().literal(string);

        return expression;
    }

    /**
     * Revert to default value.
     */
    public void revertToDefaultValue() {
        List<FieldConfigBase> fieldList = fieldConfigManager.getFields(null);

        for (FieldConfigBase field : fieldList) {
            if (field != null) {
                field.revertToDefaultValue();
            }
        }
    }

    /**
     * Sets the value.
     *
     * @param externalGraphic the new value
     */
    public void setValue(ExternalGraphicImpl externalGraphic) {
        try {
            if (externalGraphic != null) {
                externalFileURL = externalGraphic.getLocation();
            }
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        UndoManager.getInstance().addUndoEvent(
                new UndoEvent(this, FieldIdEnum.EXTERNAL_GRAPHIC, oldValueObj, externalFileURL));
        try {
            oldValueObj = new URL(externalFileURL.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String path = ExternalFilenames.getText(SLDEditorFile.getInstance().getSLDData(),
                externalFileURL);
        populateExpression(path);
    }

    /**
     * Sets the value.
     *
     * @param filename the new value
     */
    public void setValue(String filename) {
        try {
            externalFileURL = new URL(filename);
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        UndoManager.getInstance().addUndoEvent(
                new UndoEvent(this, FieldIdEnum.EXTERNAL_GRAPHIC, oldValueObj, externalFileURL));
        try {
            oldValueObj = new URL(externalFileURL.toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String path = ExternalFilenames.getText(SLDEditorFile.getInstance().getSLDData(),
                externalFileURL);
        populateExpression(path);
    }

    /**
     * Gets the symbol.
     *
     * @return the symbol
     */
    public ExternalGraphic getSymbol() {
        ExternalGraphic extGraphic = null;

        if (externalFileURL != null) {
            String fileExtension = ExternalFilenames.getFileExtension(externalFileURL.toString());
            String imageFormat = ExternalFilenames.getImageFormat(fileExtension);
            String uri = "";
            try {
                uri = externalFileURL.toURI().toString();
            } catch (URISyntaxException e) {
                ConsoleManager.getInstance().exception(this, e);
            }

            extGraphic = getStyleFactory().createExternalGraphic(uri, imageFormat);
        }

        return extGraphic;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.undo.UndoActionInterface#undoAction(com.sldeditor.common.undo.UndoInterface)
     */
    @Override
    public void undoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            if (undoRedoObject.getOldValue() instanceof URL) {
                URL oldValue = (URL) undoRedoObject.getOldValue();

                populateExpression(ExternalFilenames
                        .getText(SLDEditorFile.getInstance().getSLDData(), oldValue));
                externalFileURL = oldValue;

                if (parentObj != null) {
                    parentObj.externalGraphicValueUpdated();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.common.undo.UndoActionInterface#redoAction(com.sldeditor.common.undo.UndoInterface)
     */
    @Override
    public void redoAction(UndoInterface undoRedoObject) {
        if (undoRedoObject != null) {
            if (undoRedoObject.getNewValue() instanceof URL) {
                URL newValue = (URL) undoRedoObject.getNewValue();

                populateExpression(ExternalFilenames
                        .getText(SLDEditorFile.getInstance().getSLDData(), newValue));
                externalFileURL = newValue;

                if (parentObj != null) {
                    parentObj.externalGraphicValueUpdated();
                }
            }
        }
    }

    /**
     * Button pressed.
     *
     * @param buttonExternal the button external
     */
    @Override
    public void buttonPressed(Component buttonExternal) {
        JFileChooser fc = new JFileChooser();

        if (externalFileURL != null) {
            String filename = externalFileURL.toExternalForm();
            File currentFile = ExternalFilenames.getFile(SLDEditorFile.getInstance().getSLDData(),
                    filename);

            if (currentFile.exists()) {
                fc.setCurrentDirectory(currentFile.getParentFile());
            }
        }

        fc.addChoosableFileFilter(new ExternalGraphicFilter());

        int returnVal = fc.showOpenDialog(buttonExternal);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                externalFileURL = fc.getSelectedFile().toURI().toURL();

                UndoManager.getInstance().addUndoEvent(new UndoEvent(this,
                        FieldIdEnum.EXTERNAL_GRAPHIC, oldValueObj, externalFileURL));
                oldValueObj = externalFileURL;

                updateSymbol();
            } catch (MalformedURLException e1) {
                ConsoleManager.getInstance().exception(this, e1);
            }
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

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.JComponent#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        for (FieldConfigBase fieldConfig : getFieldConfigList()) {
            fieldConfig.setEnabled(enabled);
        }
        super.setEnabled(enabled);
    }
}
