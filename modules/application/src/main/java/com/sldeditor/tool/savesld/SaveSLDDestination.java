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

package com.sldeditor.tool.savesld;

import com.sldeditor.common.Controller;
import com.sldeditor.common.localisation.Localisation;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * The Class SaveSLDDestination.
 *
 * @author Robert Ward (SCISYS)
 */
public class SaveSLDDestination implements SaveSLDDestinationInterface {

    /** The yes to all. */
    private boolean yesToAll = false;

    /** The write output file. */
    private boolean writeOutputFile = true;

    /* (non-Javadoc)
     * @see com.sldeditor.tool.savesld.SaveSLDDestinationInterface#overwrite(java.io.File)
     */
    @Override
    public void overwrite(File output) {
        yesToAll = false;
        writeOutputFile = true;

        Object[] options = {
            Localisation.getField(SaveSLDTool.class, "SaveSLDTool.yesToAll"),
            Localisation.getField(SaveSLDTool.class, "SaveSLDTool.yes"),
            Localisation.getField(SaveSLDTool.class, "SaveSLDTool.no")
        };

        int n =
                JOptionPane.showOptionDialog(
                        Controller.getInstance().getFrame(),
                        Localisation.getField(SaveSLDTool.class, "SaveSLDTool.overwriteDestFile")
                                + "\n"
                                + output.getAbsolutePath(),
                        Localisation.getField(SaveSLDTool.class, "SaveSLDTool.destFileExists"),
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[2]);

        switch (n) {
            case 0:
                yesToAll = true;
                break;
            case 1:
                break;
            case 2:
            default:
                writeOutputFile = false;
                break;
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.savesld.SaveSLDDestinationInterface#isYesToAll()
     */
    @Override
    public boolean isYesToAll() {
        return yesToAll;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.tool.savesld.SaveSLDDestinationInterface#isWriteOutputFile()
     */
    @Override
    public boolean isWriteOutputFile() {
        return writeOutputFile;
    }
}
