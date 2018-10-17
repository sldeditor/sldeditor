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

package com.sldeditor;

import com.sldeditor.common.localisation.Localisation;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Class SLDEditorDlg.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDEditorDlg implements SLDEditorDlgInterface {

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorDlgInterface#load(javax.swing.JFrame)
     */
    @Override
    public boolean load(JFrame frame) {
        Object[] options = {
            Localisation.getString(SLDEditor.class, "common.discard"),
            Localisation.getString(SLDEditor.class, "common.cancel")
        };

        int result =
                JOptionPane.showOptionDialog(
                        frame,
                        Localisation.getString(SLDEditor.class, "SLDEditor.unsavedChanges"),
                        Localisation.getString(SLDEditor.class, "SLDEditor.unsavedChangesTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);

        // If discard option selected then allow the new symbol to be loaded
        return (result == JOptionPane.OK_OPTION);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.SLDEditorDlgInterface#reload(javax.swing.JFrame)
     */
    @Override
    public boolean reload(JFrame frame) {
        Object[] options = {
            Localisation.getString(SLDEditor.class, "common.yes"),
            Localisation.getString(SLDEditor.class, "common.no")
        };

        int result =
                JOptionPane.showOptionDialog(
                        frame,
                        Localisation.getString(SLDEditor.class, "SLDEditor.reloadFileQuery"),
                        Localisation.getString(SLDEditor.class, "SLDEditor.reloadFileQueryTitle"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[1]);
        return (result == JOptionPane.OK_OPTION);
    }
}
