/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.ui.preferences;

import java.nio.charset.Charset;
import java.util.Set;

import javax.swing.JComboBox;

/**
 * Combobox of file encoding options
 *
 * @author Robert Ward (SCISYS)
 */
public class FileEncodingComboBox extends JComboBox<String> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    public static JComboBox<String> create()
    {
        Set<String> keySet = Charset.availableCharsets().keySet();

        String[] fileEncodingList = keySet.toArray(new String[keySet.size()]);
        
        return new FileEncodingComboBox(fileEncodingList);
    }
    
    private FileEncodingComboBox(String[] items)
    {
        super(items);
    }

}
