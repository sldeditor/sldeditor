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
package com.sldeditor.ui.detail;

import java.util.ArrayList;
import java.util.List;

import org.geotools.styling.AnchorPoint;
import org.geotools.styling.AnchorPointImpl;
import org.geotools.styling.Displacement;
import org.geotools.styling.FillImpl;
import org.geotools.styling.Font;
import org.geotools.styling.Halo;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacementImpl;
import org.geotools.styling.PointPlacementImpl;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Fill;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.preferences.iface.PrefUpdateVendorOptionInterface;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.text.VendorOptionTextFactory;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class TextSymbolizerDetails allows a user to configure text data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class TextSymbolizerDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface, PrefUpdateVendorOptionInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option list allowed to be used. */
    private List<VersionData> vendorOptionVersionList = new ArrayList<VersionData>();

    /** The vendor option text factory. */
    private VendorOptionTextFactory vendorOptionTextFactory = null;

    /**
     * Constructor.
     *
     * @param functionManager the function manager
     */
    public TextSymbolizerDetails(FunctionNameInterface functionManager)
    {
        super(TextSymbolizerDetails.class, functionManager);

        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        PrefManager.getInstance().addVendorOptionListener(this);

        createVendorOptionPanel();

        readConfigFile(vendorOptionTextFactory, this, "Text.xml");
    }

    /**
     * Update vendor option panels.
     */
    private void updateVendorOptionPanels()
    {
        if(vendorOptionTextFactory != null)
        {
            List<VendorOptionInterface> veList = vendorOptionTextFactory.getVendorOptionList();
            if(veList != null)
            {
                for(VendorOptionInterface vendorOption : veList)
                {
                    boolean displayVendorOption = VendorOptionManager.getInstance().isAllowed(vendorOptionVersionList, vendorOption.getVendorOption());

                    BasePanel extensionPanel = vendorOption.getPanel();
                    if(extensionPanel != null)
                    {
                        removePanel(vendorOption.getPanel());

                        if(displayVendorOption)
                        {
                            appendPanel(vendorOption.getPanel());
                        }
                    }
                }
            }
        }
    }

    /**
     * Creates the vendor option panel.
     *
     * @return the detail panel
     */
    private void createVendorOptionPanel() {

        vendorOptionTextFactory = new VendorOptionTextFactory(getClass(), getFunctionManager());

        List<VendorOptionInterface> veList = vendorOptionTextFactory.getVendorOptionList();
        if(veList != null)
        {
            for(VendorOptionInterface extension : veList)
            {
                extension.setParentPanel(this);
            }
        }
    }

    /**
     * Populate.
     *
     * @param selectedSymbol the selected symbol
     */
    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if(selectedSymbol != null)
        {
            TextSymbolizer textSymbolizer = (TextSymbolizer) selectedSymbol.getSymbolizer();
            if(textSymbolizer != null)
            {
                populateStandardData(textSymbolizer);

                //
                // Geometry
                //
                fieldConfigVisitor.populateField(FieldIdEnum.GEOMETRY, textSymbolizer.getGeometry());

                //
                // Label
                //
                fieldConfigVisitor.populateField(FieldIdEnum.LABEL, textSymbolizer.getLabel());

                // Font
                Font font = textSymbolizer.getFont();

                GroupConfigInterface group = getGroup(GroupIdEnum.FONT);
                group.enable(font != null);

                if(font != null)
                {
                    fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_FAMILY, font);

                    fieldConfigVisitor.populateField(FieldIdEnum.FONT_WEIGHT, font.getWeight());
                    fieldConfigVisitor.populateField(FieldIdEnum.FONT_STYLE, font.getStyle());
                    fieldConfigVisitor.populateField(FieldIdEnum.FONT_SIZE, font.getSize());
                }
                fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_PREVIEW, font);

                // Fill
                Fill fill = (FillImpl) textSymbolizer.getFill();

                if(fill != null)
                {
                    fieldConfigVisitor.populateField(FieldIdEnum.FILL_COLOUR, fill.getColor());
                }

                // Halo
                Halo halo = textSymbolizer.getHalo();
                group = getGroup(GroupIdEnum.HALO);
                group.enable(halo != null);

                if(halo != null)
                {
                    Fill haloFill = halo.getFill();

                    fieldConfigVisitor.populateField(FieldIdEnum.HALO_COLOUR, haloFill.getColor());
                    fieldConfigVisitor.populateField(FieldIdEnum.HALO_RADIUS, halo.getRadius());
                }
                else
                {
                    fieldConfigVisitor.populateField(FieldIdEnum.HALO_COLOUR, (Expression)null);
                    fieldConfigVisitor.populateField(FieldIdEnum.HALO_RADIUS, (Expression)null);
                }

                group = getGroup(GroupIdEnum.PLACEMENT);
                if(group != null)
                {
                    MultiOptionGroup labelPlacementGroup = (MultiOptionGroup) group;

                    LabelPlacement placement = textSymbolizer.getLabelPlacement();
                    if(placement instanceof PointPlacementImpl)
                    {
                        PointPlacementImpl pointPlacement = (PointPlacementImpl)placement;

                        labelPlacementGroup.setOption(GroupIdEnum.POINTPLACEMENT);
                        AnchorPointImpl anchorPoint = pointPlacement.getAnchorPoint();
                        if(anchorPoint == null)
                        {
                            fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_H, (Expression)null);
                            fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_V, (Expression)null);
                        }
                        else
                        {
                            fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_H, anchorPoint.getAnchorPointX());
                            fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_V, anchorPoint.getAnchorPointY());
                        }

                        Displacement displacement = pointPlacement.getDisplacement();
                        if(displacement == null)
                        {
                            fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_X, (Expression)null);
                            fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_Y, (Expression)null);
                        }
                        else
                        {
                            fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_X, displacement.getDisplacementX());
                            fieldConfigVisitor.populateField(FieldIdEnum.DISPLACEMENT_Y, displacement.getDisplacementY());
                        }

                        fieldConfigVisitor.populateField(FieldIdEnum.ANGLE, pointPlacement.getRotation());
                    }
                    else if(placement instanceof LinePlacementImpl)
                    {
                        LinePlacementImpl linePlacement = (LinePlacementImpl)placement;

                        labelPlacementGroup.setOption(GroupIdEnum.LINEPLACEMENT);
                        fieldConfigVisitor.populateField(FieldIdEnum.GAP, linePlacement.getGap());
                        fieldConfigVisitor.populateField(FieldIdEnum.INITIAL_GAP, linePlacement.getInitialGap());
                        fieldConfigVisitor.populateField(FieldIdEnum.PERPENDICULAR_OFFSET, linePlacement.getPerpendicularOffset());
                        fieldConfigVisitor.populateBooleanField(FieldIdEnum.GENERALISED_LINE, linePlacement.isGeneralizeLine());
                        fieldConfigVisitor.populateBooleanField(FieldIdEnum.ALIGN, linePlacement.isAligned());
                        fieldConfigVisitor.populateBooleanField(FieldIdEnum.REPEATED, linePlacement.isRepeated());
                    }
                }

                if(vendorOptionTextFactory != null)
                {
                    vendorOptionTextFactory.populate(textSymbolizer);
                }
            }
        }
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {

        StandardData standardData = getStandardData();

        Expression label = fieldConfigVisitor.getExpression(FieldIdEnum.LABEL);
        Expression haloRadius = fieldConfigVisitor.getExpression(FieldIdEnum.HALO_RADIUS);

        Expression geometryField = fieldConfigVisitor.getExpression(FieldIdEnum.GEOMETRY);

        String geometryFieldName = null;
        Expression defaultGeometryField = getFilterFactory().property(geometryFieldName);

        // Label placement
        LabelPlacement labelPlacement = null;

        MultiOptionGroup labelPlacementPanel = (MultiOptionGroup) getGroup(GroupIdEnum.PLACEMENT);
        OptionGroup labelPlacementOption = labelPlacementPanel.getSelectedOptionGroup();

        if(labelPlacementOption.getId() == GroupIdEnum.POINTPLACEMENT)
        {
            AnchorPoint anchor = null;
            GroupConfigInterface anchorPointPanel = getGroup(GroupIdEnum.ANCHORPOINT);

            if(anchorPointPanel == null)
            {
                String errorMessage = String.format("%s : %s", Localisation.getString(TextSymbolizerDetails.class, "TextSymbol.error1") , GroupIdEnum.ANCHORPOINT);
                ConsoleManager.getInstance().error(this, errorMessage);
            }
            else if(anchorPointPanel.isPanelEnabled())
            {
                Expression anchorPointH = fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_H);
                Expression anchorPointV = fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_V);
                anchor = (AnchorPoint) getStyleFactory().anchorPoint(anchorPointH,
                        anchorPointV);
            }

            //
            // Displacement
            //
            Displacement displacement = null;
            GroupConfigInterface displacementPanel = getGroup(GroupIdEnum.DISPLACEMENT);

            if(displacementPanel == null)
            {
                ConsoleManager.getInstance().error(this, String.format("%s : %s", Localisation.getString(TextSymbolizerDetails.class, "TextSymbol.error1") , GroupIdEnum.DISPLACEMENT));
            }
            else if(displacementPanel.isPanelEnabled())
            {
                displacement = getStyleFactory().displacement(fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_X),
                        fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_Y));
            }

            //
            // Rotation
            //
            Expression rotation = null;
            GroupConfigInterface rotationPanel = getGroup(GroupIdEnum.ROTATION);

            if(rotationPanel == null)
            {
                ConsoleManager.getInstance().error(this, String.format("%s : %s", Localisation.getString(TextSymbolizerDetails.class, "TextSymbol.error1") , GroupIdEnum.ROTATION));
            }
            else if(rotationPanel.isPanelEnabled())
            {
                rotation = fieldConfigVisitor.getExpression(FieldIdEnum.ANGLE);
            }
            labelPlacement = getStyleFactory().pointPlacement(anchor, displacement, rotation);
        }
        else if(labelPlacementOption.getId() == GroupIdEnum.LINEPLACEMENT)
        {
            Expression offset = fieldConfigVisitor.getExpression(FieldIdEnum.PERPENDICULAR_OFFSET);
            Expression initialGap = fieldConfigVisitor.getExpression(FieldIdEnum.INITIAL_GAP);
            Expression gap = fieldConfigVisitor.getExpression(FieldIdEnum.GAP);

            boolean repeated = fieldConfigVisitor.getBoolean(FieldIdEnum.REPEATED);
            boolean aligned = fieldConfigVisitor.getBoolean(FieldIdEnum.ALIGN);
            boolean generalizedLine = fieldConfigVisitor.getBoolean(FieldIdEnum.GENERALISED_LINE);

            labelPlacement = getStyleFactory().linePlacement(offset, initialGap, gap, repeated, aligned, generalizedLine);
        }

        FieldConfigColour fdmFillColour = (FieldConfigColour)fieldConfigManager.get(FieldIdEnum.FILL_COLOUR);
        Expression fillColour = fdmFillColour.getColourExpression();
        Expression fillColourOpacity = fdmFillColour.getColourOpacityExpression();

        Fill fill = getStyleFactory().createFill(fillColour, fillColourOpacity);

        FieldConfigColour fdmHaloColour = (FieldConfigColour)fieldConfigManager.get(FieldIdEnum.HALO_COLOUR);
        Expression haloFillColour = fdmHaloColour.getColourExpression();
        Expression haloFillColourOpacity = fdmHaloColour.getColourOpacityExpression();
        Fill haloFill = getStyleFactory().fill(null, haloFillColour, haloFillColourOpacity);

        //
        // Halo
        //
        Halo halo = null;
        GroupConfigInterface haloPanel = getGroup(GroupIdEnum.HALO);

        if(haloPanel.isPanelEnabled())
        {
            halo = (Halo) getStyleFactory().halo(haloFill, haloRadius);
        }

        //
        // Font
        //
        Font font = extractFont();

        // Any changes made to the font details need to be reflected back to the FieldConfigFontPreview field
        fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_PREVIEW, font);

        TextSymbolizer textSymbolizer = (TextSymbolizer) getStyleFactory().textSymbolizer(standardData.name,
                defaultGeometryField,
                standardData.description, 
                standardData.unit, 
                label,
                font,
                labelPlacement,
                halo,
                fill);

        if((geometryField != null) && !geometryField.toString().isEmpty())
        {
            textSymbolizer.setGeometry(geometryField);
        }

        if(vendorOptionTextFactory != null)
        {
            vendorOptionTextFactory.updateSymbol(textSymbolizer);
        }

        SelectedSymbol.getInstance().replaceSymbolizer(textSymbolizer);

        this.fireUpdateSymbol();
    }

    /**
     * Extract font.
     *
     * @return the font
     */
    private Font extractFont() {
        Font font = null;
        Expression fontFamily = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_FAMILY);
        Expression fontSize = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_SIZE);
        Expression fontStyle = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_STYLE);
        Expression fontWeight = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_WEIGHT);

        List<Expression> fontFamilyList = new ArrayList<Expression>();
        fontFamilyList.add(fontFamily);

        if((fontFamilyList == null) || (fontStyle == null) || (fontWeight == null) || (fontSize == null))
        {
            font = getStyleFactory().getDefaultFont();
        }
        else
        {
            font = (Font) getStyleFactory().font(fontFamilyList, fontStyle, fontWeight, fontSize);
        }
        return font;
    }

    /**
     * Data changed.
     *
     * @param changedField the changed field
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
        updateSymbol();
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
        if(vendorOptionTextFactory != null)
        {
            vendorOptionTextFactory.getFieldDataManager(fieldConfigManager);
        }

        return fieldConfigManager;
    }

    /**
     * Vendor options updated.
     *
     * @param vendorOptionList the vendor option list
     */
    /* (non-Javadoc)
     * @see com.sldeditor.preferences.iface.PrefUpdateVendorOptionInterface#vendorOptionsUpdated(java.util.List)
     */
    @Override
    public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList)
    {
        this.vendorOptionVersionList = vendorOptionVersionsList;

        updateVendorOptionPanels();
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

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
