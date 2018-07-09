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

package com.sldeditor.tool.batchupdatefont;

import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.StandardPanel;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.Font;
import org.opengis.filter.expression.Expression;

/**
 * The Class TextSymbolizerDetails allows a user to configure text data in a panel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FontDetails extends StandardPanel implements UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    public FontDetails() {
        super(FontDetails.class);

        createUI();
    }

    /** Creates the ui. */
    private void createUI() {
        readConfigFileNoScrollPane(null, getClass(), this, "Font.xml");
        populate(null);
    }

    /**
     * Populate.
     *
     * @param fontList the font list
     */
    public void populate(List<Font> fontList) {

        boolean emptyList = (fontList == null) || fontList.isEmpty();

        boolean showMultipleCheckbox = (fontList != null) && (fontList.size() > 1);

        MultipleFont multipleFont = new MultipleFont();
        multipleFont.parseList(fontList);
        Font font = multipleFont.getFont();

        FieldConfigBase fieldConfig = fieldConfigManager.get(FieldIdEnum.FONT_FAMILY);
        if (fieldConfig != null) {
            if (emptyList) {
                fieldConfig.showOptionField(false);
                fieldConfig.setEnabled(false);
            } else {
                fieldConfig.setEnabled(true);
                fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_FAMILY, font);
                fieldConfig.showOptionField(showMultipleCheckbox);
                boolean isSelected = !font.getFamily().isEmpty();
                fieldConfig.setOptionFieldValue(isSelected);
            }
        }
        populateField(emptyList, showMultipleCheckbox, FieldIdEnum.FONT_WEIGHT, font.getWeight());
        populateField(emptyList, showMultipleCheckbox, FieldIdEnum.FONT_STYLE, font.getStyle());
        populateField(emptyList, showMultipleCheckbox, FieldIdEnum.FONT_SIZE, font.getSize());

        fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_PREVIEW, font);
    }

    /**
     * Populate field.
     *
     * @param emptyList the empty list
     * @param showMultipleCheckbox the show multiple checkbox
     * @param fieldId the field id
     * @param value the value
     */
    private void populateField(
            boolean emptyList,
            boolean showMultipleCheckbox,
            FieldIdEnum fieldId,
            Expression value) {
        FieldConfigBase fieldConfig = fieldConfigManager.get(fieldId);
        if (fieldConfig != null) {
            if (emptyList) {
                fieldConfig.showOptionField(false);
                fieldConfig.setEnabled(false);
            } else {
                fieldConfig.setEnabled(true);
                fieldConfigVisitor.populateField(fieldId, value);
                fieldConfig.showOptionField(showMultipleCheckbox);
                boolean isSelected = (value != null);
                fieldConfig.setOptionFieldValue(isSelected);
            }
        }
    }

    /** Update symbol. */
    private void updateSymbol() {
        //
        // Font
        //
        Font font = extractFont();

        // Any changes made to the font details need to be reflected
        // back to the FieldConfigFontPreview field
        fieldConfigVisitor.populateFontField(FieldIdEnum.FONT_PREVIEW, font);
    }

    /**
     * Extract font.
     *
     * @return the font
     */
    private Font extractFont() {
        Expression fontFamily = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_FAMILY);

        List<Expression> fontFamilyList = new ArrayList<Expression>();
        if (fontFamily != null) {
            fontFamilyList.add(fontFamily);
        }

        Font font = getStyleFactory().getDefaultFont();

        font.getFamily().clear();
        font.getFamily().addAll(fontFamilyList);

        Expression fontSize = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_SIZE);
        Expression fontStyle = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_STYLE);
        Expression fontWeight = fieldConfigVisitor.getExpression(FieldIdEnum.FONT_WEIGHT);

        font.setStyle(fontStyle);
        font.setWeight(fontWeight);
        font.setSize(fontSize);

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
     * Gets the font data.
     *
     * @return the font data
     */
    public Font getFontData() {
        Font font = extractFont();

        return font;
    }
}
