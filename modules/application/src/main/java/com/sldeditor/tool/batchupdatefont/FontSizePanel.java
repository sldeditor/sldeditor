/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.geotools.styling.Font;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class FontSizePanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FontSizePanel extends JPanel implements UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The value. */
    private FieldConfigInteger value;

    public FontSizePanel() {
        createUI();
    }

    /**
     * Creates the UI.
     */
    private void createUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        value = new FieldConfigInteger(new FieldConfigCommonData(getClass(), FieldIdEnum.FONT_SIZE,
                Localisation.getField(BatchUpdateFontPanel.class, "BatchUpdateFontPanel.fontSize"),
                true, false, false));
        value.createUI();
        value.addDataChangedListener(this);
        add(value.getPanel());
        populate(null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldIdEnum changedField) {
    }

    /**
     * Populate.
     *
     * @param entries the entries
     */
    public void populate(List<Font> entries) {
        value.populateField(0);
        value.setEnabled((entries != null) && !entries.isEmpty());
    }

    /**
     * Gets the font size.
     *
     * @return the font size
     */
    public int getFontSize() {
        return value.getIntValue();
    }
}
