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

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigCommonData;
import com.sldeditor.ui.detail.config.FieldConfigInteger;
import com.sldeditor.ui.iface.UpdateSymbolInterface;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import org.geotools.styling.Font;

/**
 * The Class FontSizePanel.
 *
 * @author Robert Ward (SCISYS)
 */
public class FontSizePanel extends JPanel implements UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The value. */
    private transient FieldConfigInteger value;

    /** Instantiates a new font size panel. */
    public FontSizePanel() {
        createUI();
    }

    /** Creates the UI. */
    private void createUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        value =
                new FieldConfigInteger(
                        new FieldConfigCommonData(
                                getClass(),
                                FieldIdEnum.FONT_SIZE,
                                Localisation.getField(
                                        BatchUpdateFontPanel.class,
                                        "BatchUpdateFontPanel.fontSize"),
                                true,
                                false,
                                false));
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
        // Not used
    }

    /**
     * Populate.
     *
     * @param entries the entries
     */
    public void populate(List<Font> entries) {
        boolean hasData = (entries != null) && !entries.isEmpty();
        Integer fontSize = 0;
        if (hasData) {
            Font f = entries.get(0);
            fontSize = Double.valueOf(f.getSize().toString()).intValue();
        }
        value.populateField(fontSize);
        value.setEnabled(hasData);
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
