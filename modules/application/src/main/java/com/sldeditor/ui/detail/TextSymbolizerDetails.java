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

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.vendor.geoserver.VendorOptionInterface;
import com.sldeditor.ui.detail.vendor.geoserver.text.VendorOptionTextFactory;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.AnchorPoint;
import org.geotools.styling.Displacement;
import org.geotools.styling.DisplacementImpl;
import org.geotools.styling.Font;
import org.geotools.styling.Halo;
import org.geotools.styling.LabelPlacement;
import org.geotools.styling.LinePlacementImpl;
import org.geotools.styling.PointPlacement;
import org.geotools.styling.PointPlacementImpl;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.TextSymbolizer;
import org.opengis.filter.expression.Expression;
import org.opengis.style.Fill;

/**
 * The Class TextSymbolizerDetails allows a user to configure text data in a panel.
 *
 * @author Robert Ward (SCISYS)
 */
public class TextSymbolizerDetails extends StandardPanel
        implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant ERROR_MESSAGE_FORMAT. */
    private static final String ERROR_MESSAGE_FORMAT = "%s : %s";

    /** The Constant TEXT_SYMBOL_ERROR. */
    private static final String TEXT_SYMBOL_ERROR = "TextSymbol.error1";

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The vendor option text factory. */
    private transient VendorOptionTextFactory vendorOptionTextFactory = null;

    /** The default point placement anchor point X. */
    private transient Expression defaultPointPlacementAnchorPointX = null;

    /** The default point placement anchor point Y. */
    private transient Expression defaultPointPlacementAnchorPointY = null;

    /** Constructor. */
    public TextSymbolizerDetails() {
        super(TextSymbolizerDetails.class);

        // Cache the default point placement anchor point values
        StyleBuilder styleBuilder = new StyleBuilder();
        PointPlacement defaultPointPlacement = styleBuilder.createPointPlacement();
        defaultPointPlacementAnchorPointX =
                defaultPointPlacement.getAnchorPoint().getAnchorPointX();
        defaultPointPlacementAnchorPointY =
                defaultPointPlacement.getAnchorPoint().getAnchorPointY();

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {

        createVendorOptionPanel();

        readConfigFile(vendorOptionTextFactory, getClass(), this, "Text.xml");
    }

    /**
     * Creates the vendor option panel.
     *
     * @return the detail panel
     */
    private void createVendorOptionPanel() {

        vendorOptionTextFactory = new VendorOptionTextFactory(getClass());

        List<VendorOptionInterface> veList = vendorOptionTextFactory.getVendorOptionList();
        if (veList != null) {
            for (VendorOptionInterface extension : veList) {
                extension.setParentPanel(this);
            }
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
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.
     * selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if (selectedSymbol != null) {
            TextSymbolizer textSymbolizer = (TextSymbolizer) selectedSymbol.getSymbolizer();
            if (textSymbolizer != null) {
                populateStandardData(textSymbolizer);

                //
                // Geometry
                //
                fieldConfigVisitor.populateField(
                        FieldIdEnum.GEOMETRY, textSymbolizer.getGeometry());

                //
                // Label
                //
                fieldConfigVisitor.populateField(FieldIdEnum.LABEL, textSymbolizer.getLabel());

                // Font
                GroupConfigInterface group;
                populateFont(textSymbolizer);

                // Fill
                populateFill(textSymbolizer);

                // Halo
                populateHalo(textSymbolizer);

                group = getGroup(GroupIdEnum.PLACEMENT);
                if (group != null) {
                    MultiOptionGroup labelPlacementGroup = (MultiOptionGroup) group;

                    LabelPlacement placement = textSymbolizer.getLabelPlacement();
                    if (placement instanceof PointPlacementImpl) {
                        populatePointPlacement(labelPlacementGroup, placement);
                    } else if (placement instanceof LinePlacementImpl) {
                        populateLinePlacement(labelPlacementGroup, placement);
                    }
                }

                if (vendorOptionTextFactory != null) {
                    vendorOptionTextFactory.populate(textSymbolizer);
                }
            }
        }
    }

    /**
     * Populate point placement.
     *
     * @param labelPlacementGroup the label placement group
     * @param placement the placement
     */
    private void populatePointPlacement(
            MultiOptionGroup labelPlacementGroup, LabelPlacement placement) {
        PointPlacementImpl pointPlacement = (PointPlacementImpl) placement;

        labelPlacementGroup.setOption(GroupIdEnum.POINTPLACEMENT);
        Expression anchorPointX = null;
        Expression anchorPointY = null;
        AnchorPoint anchorPoint = pointPlacement.getAnchorPoint();
        if (anchorPoint != null) {
            anchorPointX = anchorPoint.getAnchorPointX();
            anchorPointY = anchorPoint.getAnchorPointY();
        } else {
            // Use the defaults as non specified
            anchorPointX = defaultPointPlacementAnchorPointX;
            anchorPointY = defaultPointPlacementAnchorPointY;
        }
        fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_H, anchorPointX);
        fieldConfigVisitor.populateField(FieldIdEnum.ANCHOR_POINT_V, anchorPointY);

        Displacement displacement = pointPlacement.getDisplacement();
        if (displacement == null) {
            displacement = DisplacementImpl.DEFAULT;
        }
        fieldConfigVisitor.populateField(
                FieldIdEnum.DISPLACEMENT_X, displacement.getDisplacementX());
        fieldConfigVisitor.populateField(
                FieldIdEnum.DISPLACEMENT_Y, displacement.getDisplacementY());

        fieldConfigVisitor.populateField(FieldIdEnum.ANGLE, pointPlacement.getRotation());
    }

    /**
     * Populate line placement.
     *
     * @param labelPlacementGroup the label placement group
     * @param placement the placement
     */
    private void populateLinePlacement(
            MultiOptionGroup labelPlacementGroup, LabelPlacement placement) {
        LinePlacementImpl linePlacement = (LinePlacementImpl) placement;

        labelPlacementGroup.setOption(GroupIdEnum.LINEPLACEMENT);
        fieldConfigVisitor.populateField(FieldIdEnum.GAP, linePlacement.getGap());
        fieldConfigVisitor.populateField(FieldIdEnum.INITIAL_GAP, linePlacement.getInitialGap());
        fieldConfigVisitor.populateField(
                FieldIdEnum.PERPENDICULAR_OFFSET, linePlacement.getPerpendicularOffset());
        fieldConfigVisitor.populateBooleanField(
                FieldIdEnum.GENERALISED_LINE, linePlacement.isGeneralizeLine());
        fieldConfigVisitor.populateBooleanField(FieldIdEnum.ALIGN, linePlacement.isAligned());
        fieldConfigVisitor.populateBooleanField(FieldIdEnum.REPEATED, linePlacement.isRepeated());
    }

    /** @param textSymbolizer */
    private void populateHalo(TextSymbolizer textSymbolizer) {
        GroupConfigInterface group;
        Halo halo = textSymbolizer.getHalo();
        group = getGroup(GroupIdEnum.HALO);
        group.enable(halo != null);

        if (halo != null) {
            Fill haloFill = halo.getFill();

            fieldConfigVisitor.populateField(FieldIdEnum.HALO_COLOUR, haloFill.getColor());
            fieldConfigVisitor.populateField(FieldIdEnum.HALO_RADIUS, halo.getRadius());
        } else {
            fieldConfigVisitor.populateField(FieldIdEnum.HALO_COLOUR, (Expression) null);
            fieldConfigVisitor.populateField(FieldIdEnum.HALO_RADIUS, (Expression) null);
        }
    }

    /** @param textSymbolizer */
    private void populateFill(TextSymbolizer textSymbolizer) {
        Fill fill = textSymbolizer.getFill();

        if (fill != null) {
            fieldConfigVisitor.populateField(FieldIdEnum.FILL_COLOUR, fill.getColor());
            fieldConfigVisitor.populateField(FieldIdEnum.TEXT_OPACITY, fill.getOpacity());
        }
    }

    /** @param textSymbolizer */
    private void populateFont(TextSymbolizer textSymbolizer) {
        Font font = textSymbolizer.getFont();

        GroupConfigInterface group = getGroup(GroupIdEnum.FONT);
        group.enable(font != null);

        if (font != null) {
            fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_FAMILY, font);

            fieldConfigVisitor.populateField(FieldIdEnum.FONT_WEIGHT, font.getWeight());
            fieldConfigVisitor.populateField(FieldIdEnum.FONT_STYLE, font.getStyle());
            fieldConfigVisitor.populateField(FieldIdEnum.FONT_SIZE, font.getSize());
        }
        fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_PREVIEW, font);
    }

    /** Update symbol. */
    private void updateSymbol() {
        if (!Controller.getInstance().isPopulating()) {

            Expression haloRadius = fieldConfigVisitor.getExpression(FieldIdEnum.HALO_RADIUS);

            // Label placement
            LabelPlacement labelPlacement = null;

            MultiOptionGroup labelPlacementPanel =
                    (MultiOptionGroup) getGroup(GroupIdEnum.PLACEMENT);
            OptionGroup labelPlacementOption = labelPlacementPanel.getSelectedOptionGroup();

            if (labelPlacementOption.getId() == GroupIdEnum.POINTPLACEMENT) {
                labelPlacement = updatePointPlacement();
            } else if (labelPlacementOption.getId() == GroupIdEnum.LINEPLACEMENT) {
                labelPlacement = updateLinePlacement();
            }

            FieldConfigColour fdmFillColour =
                    (FieldConfigColour) fieldConfigManager.get(FieldIdEnum.FILL_COLOUR);
            Expression fillColour = fdmFillColour.getColourExpression();
            Expression fillColourOpacity =
                    fieldConfigVisitor.getExpression(FieldIdEnum.TEXT_OPACITY);

            Fill fill = getStyleFactory().createFill(fillColour, fillColourOpacity);

            FieldConfigColour fdmHaloColour =
                    (FieldConfigColour) fieldConfigManager.get(FieldIdEnum.HALO_COLOUR);
            Expression haloFillColour = fdmHaloColour.getColourExpression();
            Expression haloFillColourOpacity = fdmHaloColour.getColourOpacityExpression();
            Fill haloFill = getStyleFactory().fill(null, haloFillColour, haloFillColourOpacity);

            //
            // Halo
            //
            Halo halo = updateHalo(haloRadius, haloFill);

            //
            // Font
            //
            Font font = extractFont();

            // Any changes made to the font details need to be reflected
            // back to the FieldConfigFontPreview field
            fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_PREVIEW, font);

            StandardData standardData = getStandardData();
            Expression label = fieldConfigVisitor.getExpression(FieldIdEnum.LABEL);
            Expression geometryField = fieldConfigVisitor.getExpression(FieldIdEnum.GEOMETRY);

            String geometryFieldName = null;
            Expression defaultGeometryField = getFilterFactory().property(geometryFieldName);

            TextSymbolizer textSymbolizer =
                    getStyleFactory()
                            .textSymbolizer(
                                    standardData.getName(),
                                    defaultGeometryField,
                                    standardData.getDescription(),
                                    (standardData.getUnit() != null)
                                            ? standardData.getUnit().getUnit()
                                            : null,
                                    label,
                                    font,
                                    labelPlacement,
                                    halo,
                                    fill);

            if ((geometryField != null) && !geometryField.toString().isEmpty()) {
                textSymbolizer.setGeometry(geometryField);
            }

            if (vendorOptionTextFactory != null) {
                vendorOptionTextFactory.updateSymbol(textSymbolizer);
            }

            SelectedSymbol.getInstance().replaceSymbolizer(textSymbolizer);

            this.fireUpdateSymbol();
        }
    }

    /**
     * Update halo.
     *
     * @param haloRadius the halo radius
     * @param haloFill the halo fill
     * @return the halo
     */
    private Halo updateHalo(Expression haloRadius, Fill haloFill) {
        Halo halo = null;
        GroupConfigInterface haloPanel = getGroup(GroupIdEnum.HALO);

        if (haloPanel.isPanelEnabled()) {
            halo = getStyleFactory().halo(haloFill, haloRadius);
        }
        return halo;
    }

    /**
     * Update point placement.
     *
     * @return the label placement
     */
    private LabelPlacement updatePointPlacement() {
        LabelPlacement labelPlacement;
        AnchorPoint anchor = null;
        GroupConfigInterface anchorPointPanel = getGroup(GroupIdEnum.ANCHORPOINT);

        if (anchorPointPanel == null) {
            String errorMessage =
                    String.format(
                            ERROR_MESSAGE_FORMAT,
                            Localisation.getString(TextSymbolizerDetails.class, TEXT_SYMBOL_ERROR),
                            GroupIdEnum.ANCHORPOINT);
            ConsoleManager.getInstance().error(this, errorMessage);
        } else if (anchorPointPanel.isPanelEnabled()) {
            Expression anchorPointH = fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_H);
            Expression anchorPointV = fieldConfigVisitor.getExpression(FieldIdEnum.ANCHOR_POINT_V);
            anchor = getStyleFactory().anchorPoint(anchorPointH, anchorPointV);
        }

        //
        // Displacement
        //
        Displacement displacement = null;
        GroupConfigInterface displacementPanel = getGroup(GroupIdEnum.DISPLACEMENT);

        if (displacementPanel == null) {
            ConsoleManager.getInstance()
                    .error(
                            this,
                            String.format(
                                    ERROR_MESSAGE_FORMAT,
                                    Localisation.getString(
                                            TextSymbolizerDetails.class, TEXT_SYMBOL_ERROR),
                                    GroupIdEnum.DISPLACEMENT));
        } else if (displacementPanel.isPanelEnabled()) {
            displacement =
                    getStyleFactory()
                            .displacement(
                                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_X),
                                    fieldConfigVisitor.getExpression(FieldIdEnum.DISPLACEMENT_Y));
        }

        //
        // Rotation
        //
        Expression rotation = null;
        GroupConfigInterface rotationPanel = getGroup(GroupIdEnum.ROTATION);

        if (rotationPanel == null) {
            ConsoleManager.getInstance()
                    .error(
                            this,
                            String.format(
                                    ERROR_MESSAGE_FORMAT,
                                    Localisation.getString(
                                            TextSymbolizerDetails.class, TEXT_SYMBOL_ERROR),
                                    GroupIdEnum.ROTATION));
        } else if (rotationPanel.isPanelEnabled()) {
            rotation = fieldConfigVisitor.getExpression(FieldIdEnum.ANGLE);
        }
        labelPlacement = getStyleFactory().pointPlacement(anchor, displacement, rotation);
        return labelPlacement;
    }

    /**
     * Update line placement.
     *
     * @return the label placement
     */
    private LabelPlacement updateLinePlacement() {
        LabelPlacement labelPlacement;
        Expression offset = fieldConfigVisitor.getExpression(FieldIdEnum.PERPENDICULAR_OFFSET);
        Expression initialGap = fieldConfigVisitor.getExpression(FieldIdEnum.INITIAL_GAP);
        Expression gap = fieldConfigVisitor.getExpression(FieldIdEnum.GAP);

        boolean repeated = fieldConfigVisitor.getBoolean(FieldIdEnum.REPEATED);
        boolean aligned = fieldConfigVisitor.getBoolean(FieldIdEnum.ALIGN);
        boolean generalizedLine = fieldConfigVisitor.getBoolean(FieldIdEnum.GENERALISED_LINE);

        labelPlacement =
                getStyleFactory()
                        .linePlacement(offset, initialGap, gap, repeated, aligned, generalizedLine);
        return labelPlacement;
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

        List<Expression> fontFamilyList = new ArrayList<>();
        fontFamilyList.add(fontFamily);

        if ((fontStyle == null) || (fontWeight == null) || (fontSize == null)) {
            font = getStyleFactory().getDefaultFont();
        } else {
            font = getStyleFactory().font(fontFamilyList, fontStyle, fontWeight, fontSize);
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
    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager() {
        if (vendorOptionTextFactory != null) {
            vendorOptionTextFactory.getFieldDataManager(fieldConfigManager);
        }

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
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getMinimumVersion(java.lang.Object,
     * java.util.List)
     */
    @Override
    public void getMinimumVersion(
            Object parentObj, Object sldObj, List<VendorOptionPresent> vendorOptionsPresentList) {

        vendorOptionTextFactory.getMinimumVersion(parentObj, sldObj, vendorOptionsPresentList);
    }
}
