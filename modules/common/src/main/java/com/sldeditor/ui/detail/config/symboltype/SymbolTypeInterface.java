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
package com.sldeditor.ui.detail.config.symboltype;

import java.util.List;
import java.util.Map;

import org.geotools.styling.Fill;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicFill;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.ui.detail.BasePanel;
import com.sldeditor.ui.detail.FieldEnableState;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigSymbolType;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import com.sldeditor.ui.widgets.ValueComboBoxDataGroup;

/**
 * The Interface SymbolTypeInterface to enforce methods needed for fields that appear
 * in the FieldConfigSymbolType field.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface SymbolTypeInterface {

    /**
     * Gets the field configuration of the symbol type.
     *
     * @return the field configuration
     */
    public abstract FieldConfigBase getConfigField();

    /**
     * Gets the panel id.
     *
     * @return the panel id
     */
    public abstract Class<?> getPanelId();

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    public abstract VendorOptionVersion getVendorOption();

    /**
     * Gets the symbol class.
     *
     * @return the symbol class
     */
    public abstract Class<?> getSymbolClass();

    /**
     * Sets the value.
     *
     * @param fieldConfigManager the field config manager
     * @param multiOptionPanel the multi option panel
     * @param symbol the new value
     */
    public abstract void setValue(GraphicPanelFieldManager fieldConfigManager, FieldConfigSymbolType multiOptionPanel, GraphicalSymbol symbol);

    /**
     * Gets the value.
     *
     * @param fieldConfigManager the field config manager
     * @param symbolType the symbol type
     * @param fillEnabled the fill enabled
     * @param strokeEnabled the stroke enabled
     * @return the value
     */
    public abstract List<GraphicalSymbol> getValue(GraphicPanelFieldManager fieldConfigManager, Expression symbolType, boolean fillEnabled, boolean strokeEnabled);

    /**
     * Populate symbol list.
     *
     * @param symbolizerClass the symbolizer class
     * @param symbolList the symbol list
     */
    public abstract void populateSymbolList(Class<?> symbolizerClass,
            List<ValueComboBoxDataGroup> symbolList);

    /**
     * Gets the fill.
     *
     * @param graphicFill the graphic fill
     * @param fieldConfigManager the field config manager
     * @return the fill
     */
    public abstract Fill getFill(GraphicFill graphicFill, GraphicPanelFieldManager fieldConfigManager);

    /**
     * Gets the base panel interface.
     *
     * @return the base panel interface
     */
    public abstract BasePanel getBasePanel();

    /**
     * Populate field override map.
     *
     * @param symbolizerClass the symbolizer class
     * @param fieldEnableState the field enable state
     */
    public abstract void populateFieldOverrideMap(Class<?> symbolizerClass,
            FieldEnableState fieldEnableState);

    /**
     * Gets the field map.
     *
     * @param fieldConfigManager the field config manager
     * @return the field map
     */
    public abstract Map<FieldId, FieldConfigBase> getFieldList(GraphicPanelFieldManager fieldConfigManager);

    /**
     * Returns true if the class can handle the graphical symbol.
     *
     * @param symbol the symbol
     * @return true, if successful
     */
    public abstract boolean accept(GraphicalSymbol symbol);

    /**
     * Sets the update symbol listener.
     *
     * @param listener the new update symbol listener
     */
    public abstract void setUpdateSymbolListener(UpdateSymbolInterface listener);
}
