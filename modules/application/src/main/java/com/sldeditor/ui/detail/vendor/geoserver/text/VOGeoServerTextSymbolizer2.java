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

package com.sldeditor.ui.detail.vendor.geoserver.text;

import java.util.List;
import java.util.Map;

import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.Mark;
import org.geotools.styling.OtherText;
import org.geotools.styling.OtherTextImpl;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.Stroke;
import org.geotools.styling.TextSymbolizer;
import org.geotools.styling.TextSymbolizer2;
import org.opengis.filter.expression.Expression;
import org.opengis.style.GraphicalSymbol;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.ColourFieldConfig;
import com.sldeditor.ui.detail.FieldEnableState;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.base.CurrentFieldState;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.symboltype.SymbolTypeFactory;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.iface.MultiOptionSelectedInterface;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * Class to handle the getting and setting of GeoServer texts symbolizer 2 vendor option data.
 * 
 * @author Robert Ward (SCISYS)
 */
public class VOGeoServerTextSymbolizer2 extends StandardPanel implements VendorOptionInterface,
        PopulateDetailsInterface, UpdateSymbolInterface, MultiOptionSelectedInterface {

    /** The Constant PANEL_CONFIG. */
    private static final String PANEL_CONFIG = "symbol/text/PanelConfig_TextSymbolizer2.xml";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The parent obj. */
    private UpdateSymbolInterface parentObj = null;

    /** The vendor option info. */
    private VendorOptionInfo vendorOptionInfo = null;

    /** The symbol type factory. */
    private SymbolTypeFactory symbolTypeFactory = null;

    /** The field override map, indicates which fields to enable. */
    private FieldEnableState fieldEnableState = null;

    /** The panel id of the selected fill. */
    private Class<?> selectedFillPanelId = null;

    /**
     * Constructor.
     *
     * @param panelId the panel id
     */
    public VOGeoServerTextSymbolizer2(Class<?> panelId) {
        super(panelId);

        setUpdateSymbolListener(this);

        symbolTypeFactory = new SymbolTypeFactory(VOGeoServerTextSymbolizer2.class,
                new ColourFieldConfig(GroupIdEnum.VO_TEXTSYMBOLIZER_2_FILL,
                        FieldIdEnum.VO_TEXTSYMBOLIZER_2_FILL_COLOUR,
                        FieldIdEnum.VO_TEXTSYMBOLIZER_2_FILL_OPACITY,
                        FieldIdEnum.VO_TEXTSYMBOLIZER_2_SIZE),
                new ColourFieldConfig(GroupIdEnum.VO_TEXTSYMBOLIZER_2_STROKE,
                        FieldIdEnum.VO_TEXTSYMBOLIZER_2_STROKE_FILL_COLOUR,
                        FieldIdEnum.VO_TEXTSYMBOLIZER_2_STROKE_OPACITY,
                        FieldIdEnum.VO_TEXTSYMBOLIZER_2_STROKE_FILL_WIDTH),
                FieldIdEnum.VO_TEXTSYMBOLIZER_2_SYMBOL_TYPE);

        fieldEnableState = symbolTypeFactory.getFieldOverrides(VOGeoServerTextSymbolizer2.class);
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {
        readConfigFileNoScrollPane(null, getPanelId(), this, PANEL_CONFIG);
        symbolTypeFactory.populate(this, fieldConfigManager);
    }

    /**
     * Gets the vendor option.
     *
     * @return the vendor option
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOption()
     */
    @Override
    public VendorOptionVersion getVendorOption() {
        return getVendorOptionVersion();
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldId)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        if (parentObj != null) {
            parentObj.dataChanged(changedField);
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
        return this.fieldConfigManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void updateSymbol(RasterSymbolizer rasterSymbolizer) {
        // Do nothing
    }

    /**
     * Update symbol.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void updateSymbol(PolygonSymbolizer polygonSymbolizer) {
        // Do nothing
    }

    /**
     * Update symbol.
     *
     * @param textSymbolizer the text symbolizer
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#updateSymbol(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void updateSymbol(TextSymbolizer textSymbolizer) {
        GroupConfigInterface fillGroup = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_FILL);
        GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_STROKE);

        GroupConfigInterface group = null;

        if (textSymbolizer instanceof TextSymbolizer2) {
            TextSymbolizer2 textSymbol2 = (TextSymbolizer2) textSymbolizer;

            Expression featureDescription = fieldConfigVisitor
                    .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_FEATURE_DESCRIPTION);
            if (!featureDescription.toString().isEmpty()) {
                textSymbol2.setFeatureDescription(featureDescription);
            }

            Expression snippet = fieldConfigVisitor
                    .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_SNIPPET);
            if (!snippet.toString().isEmpty()) {
                textSymbol2.setSnippet(snippet);
            }

            // Extract OtherText
            OtherText otherText = null;
            group = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_OTHERTEXT);
            if (group != null) {
                if (group.isPanelEnabled()) {
                    String target = fieldConfigVisitor
                            .getText(FieldIdEnum.VO_TEXTSYMBOLIZER_2_OTHERTEXT_TARGET);
                    Expression text = fieldConfigVisitor
                            .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_OTHERTEXT_TEXT);

                    if (!target.isEmpty() && !text.toString().isEmpty()) {
                        otherText = new OtherTextImpl();
                        otherText.setTarget(target);
                        otherText.setText(text);
                    }
                }
            }
            textSymbol2.setOtherText(otherText);

            // Graphic
            Graphic graphic = null;
            group = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_GRAPHIC);

            if (group.isPanelEnabled()) {
                Expression symbolType = fieldConfigVisitor
                        .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_SYMBOL_TYPE);

                boolean hasFill = (fillGroup == null) ? false : fillGroup.isPanelEnabled();
                boolean hasStroke = (strokeGroup == null) ? false : strokeGroup.isPanelEnabled();

                Expression size = fieldConfigVisitor
                        .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_SIZE);
                Expression rotation = fieldConfigVisitor
                        .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_ANGLE);

                List<GraphicalSymbol> symbols = symbolTypeFactory.getValue(fieldConfigManager,
                        symbolType, hasFill, hasStroke, selectedFillPanelId);

                AnchorPoint anchor = null;
                Displacement displacement = null;
                graphic = getStyleFactory().graphic(symbols, null, size, rotation, anchor,
                        displacement);

                if (!symbols.isEmpty()) {
                    boolean overallOpacity = (symbols.get(0) instanceof ExternalGraphic);
                    if (overallOpacity) {
                        Expression opacity = fieldConfigVisitor
                                .getExpression(FieldIdEnum.VO_TEXTSYMBOLIZER_2_OVERALL_OPACITY);
                        graphic.setOpacity(opacity);
                    }
                }
            }

            textSymbol2.setGraphic(graphic);
        }
    }

    /**
     * Gets the panel.
     *
     * @return the panel
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getPanel()
     */
    @Override
    public StandardPanel getPanel() {
        return this;
    }

    /**
     * Sets the parent panel.
     *
     * @param parent the new parent panel
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#setParentPanel(com.sldeditor.ui.iface.UpdateSymbolInterface)
     */
    @Override
    public void setParentPanel(UpdateSymbolInterface parent) {
        this.parentObj = parent;
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
     * Populate.
     *
     * @param polygonSymbolizer the polygon symbolizer
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.PolygonSymbolizer)
     */
    @Override
    public void populate(PolygonSymbolizer polygonSymbolizer) {
        // Do nothing
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.RasterSymbolizer)
     */
    @Override
    public void populate(RasterSymbolizer rasterSymbolizer) {
        // Do nothing
    }

    /**
     * Populate.
     *
     * @param textSymbolizer the text symbolizer
     */
    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#populate(org.geotools.styling.TextSymbolizer)
     */
    @Override
    public void populate(TextSymbolizer textSymbolizer) {
        if (textSymbolizer instanceof TextSymbolizer2) {
            TextSymbolizer2 textSymbol2 = (TextSymbolizer2) textSymbolizer;

            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_FEATURE_DESCRIPTION,
                    textSymbol2.getFeatureDescription());
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_SNIPPET,
                    textSymbol2.getSnippet());

            OtherText otherText = textSymbol2.getOtherText();
            String target = null;
            Expression text = null;

            if (otherText != null) {
                target = otherText.getTarget();
                text = otherText.getText();
            }

            GroupConfigInterface group = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_OTHERTEXT);
            if (group != null) {
                group.enable(otherText != null);
            }
            fieldConfigVisitor.populateTextField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_OTHERTEXT_TARGET,
                    target);
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_OTHERTEXT_TEXT, text);

            Graphic graphic = textSymbol2.getGraphic();

            boolean enableFill = false;
            boolean enableStroke = false;

            if (graphic == null) {
                graphic = getStyleFactory().createDefaultGraphic();
                graphic.setSize(getFilterFactory().literal(10.0));
                Mark mark = getStyleFactory().createMark();
                graphic.graphicalSymbols().add(mark);
            }

            Expression fillColour = getFilterFactory().literal(DefaultSymbols.defaultColour());
            Expression fillOpacity = getFilterFactory()
                    .literal(DefaultSymbols.defaultColourOpacity());
            Expression strokeColour = getFilterFactory().literal(DefaultSymbols.defaultColour());
            Expression strokeOpacity = getFilterFactory()
                    .literal(DefaultSymbols.defaultColourOpacity());
            Expression strokeLineWidth = null;

            List<GraphicalSymbol> graphicalSymbolList = graphic.graphicalSymbols();

            if (!graphicalSymbolList.isEmpty()) {
                GraphicalSymbol symbol = graphicalSymbolList.get(0);
                symbolTypeFactory.setValue(TextSymbolizer2.class, this.fieldConfigManager, graphic,
                        symbol);

                if (symbol instanceof Mark) {
                    Mark mark = (Mark) symbol;
                    Fill fill = mark.getFill();
                    if (fill != null) {
                        enableFill = true;
                        fillColour = fill.getColor();
                        fillOpacity = fill.getOpacity();
                    }
                    Stroke stroke = mark.getStroke();
                    if (stroke != null) {
                        enableStroke = true;
                        strokeColour = stroke.getColor();
                        strokeOpacity = stroke.getOpacity();
                        strokeLineWidth = stroke.getWidth();
                    }
                }
            }
            Expression expSize = graphic.getSize();
            Expression expRotation = graphic.getRotation();

            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_SIZE, expSize);
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_ANGLE, expRotation);

            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_FILL_COLOUR,
                    fillColour);
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_FILL_OPACITY,
                    fillOpacity);
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_STROKE_FILL_COLOUR,
                    strokeColour);
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_STROKE_OPACITY,
                    strokeOpacity);
            fieldConfigVisitor.populateField(FieldIdEnum.VO_TEXTSYMBOLIZER_2_STROKE_FILL_WIDTH,
                    strokeLineWidth);

            GroupConfigInterface fillGroup = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_FILL);
            if (fillGroup != null) {
                fillGroup.enable(enableFill);
            }
            GroupConfigInterface strokeGroup = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_STROKE);
            if (strokeGroup != null) {
                group.enable(enableStroke);
            }

            group = getGroup(GroupIdEnum.VO_TEXTSYMBOLIZER_2_GRAPHIC);
            if (group != null) {
                group.enable(textSymbol2.getGraphic() != null);
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
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getParentPanel()
     */
    @Override
    public UpdateSymbolInterface getParentPanel() {
        return parentObj;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface#getVendorOptionInfo()
     */
    @Override
    public VendorOptionInfo getVendorOptionInfo() {
        if (vendorOptionInfo == null) {
            vendorOptionInfo = new VendorOptionInfo(
                    Localisation.getString(VOGeoServerTextSymbolizer2.class,
                            "geoserver.textsymbolizer2.title"),
                    this.getVendorOption(), Localisation.getString(VOGeoServerTextSymbolizer2.class,
                            "geoserver.textsymbolizer2.description"));
        }
        return vendorOptionInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object, java.util.List)
     */
    @Override
    public void getMinimumVersion(Object parentObj, Object sldObj,
            List<VendorOptionPresent> vendorOptionsPresentList) {
        if (sldObj instanceof TextSymbolizer2) {
            TextSymbolizer2 textSymbolizer = (TextSymbolizer2) sldObj;
            if ((textSymbolizer.getFeatureDescription() != null)
                    || (textSymbolizer.getSnippet() != null)
                    || (textSymbolizer.getGraphic() != null)
                    || (textSymbolizer.getOtherText() != null)) {
                VendorOptionPresent voPresent = new VendorOptionPresent(sldObj,
                        getVendorOptionInfo());

                if (!vendorOptionsPresentList.contains(voPresent)) {
                    vendorOptionsPresentList.add(voPresent);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.MultiOptionSelectedInterface#optionSelected(java.lang.Class, java.lang.String)
     */
    @Override
    public void optionSelected(Class<?> fieldPanelId, String selectedItem) {
        setSymbolTypeVisibility(fieldPanelId, selectedItem);

        selectedFillPanelId = fieldPanelId;

        dataHasChanged();
    }

    /**
     * Sets the symbol type visibility.
     *
     * @param panelId the panel id
     * @param selectedItem the selected item
     */
    private void setSymbolTypeVisibility(Class<?> panelId, String selectedItem) {
        Map<GroupIdEnum, Boolean> groupList = fieldEnableState.getGroupIdList(panelId.getName(),
                selectedItem);

        for (GroupIdEnum groupId : groupList.keySet()) {
            boolean groupEnabled = groupList.get(groupId);
            GroupConfigInterface groupConfig = fieldConfigManager.getGroup(getPanelId(), groupId);
            if (groupConfig != null) {
                groupConfig.setGroupStateOverride(groupEnabled);
            } else {
                ConsoleManager.getInstance().error(this,
                        "Failed to find group : " + groupId.toString());
            }
        }

        Map<FieldIdEnum, Boolean> fieldList = fieldEnableState.getFieldIdList(panelId.getName(),
                selectedItem);

        for (FieldIdEnum fieldId : fieldList.keySet()) {
            boolean fieldEnabled = fieldList.get(fieldId);
            FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
            if (fieldConfig != null) {
                CurrentFieldState fieldState = fieldConfig.getFieldState();
                fieldState.setFieldEnabled(fieldEnabled);
                fieldConfig.setFieldState(fieldState);
            } else {
                ConsoleManager.getInstance().error(this,
                        "Failed to find field : " + fieldId.toString());
            }
        }
    }
}
