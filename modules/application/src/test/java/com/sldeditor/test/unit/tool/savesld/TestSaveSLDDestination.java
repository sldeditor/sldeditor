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

package com.sldeditor.test.unit.tool.savesld;

import com.sldeditor.tool.savesld.SaveSLDDestinationInterface;
import java.io.File;

/**
 * Test class to exercise all save SLD destination dialog possibilities.
 *
 * @author Robert Ward (SCISYS)
 */
public class TestSaveSLDDestination implements SaveSLDDestinationInterface {

    /** The yes to all. */
    private boolean yesToAll = false;

    /** The write output file. */
    private boolean writeOutputFile = true;

    /* (non-Javadoc)
     * @see com.sldeditor.tool.savesld.SaveSLDDestinationInterface#overwrite(java.io.File)
     */
    @Override
    public void overwrite(File output) {
        // Do nothing
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

    /**
     * Sets the yes to all.
     *
     * @param yesToAll the yesToAll to set
     */
    protected void setYesToAll(boolean yesToAll) {
        this.yesToAll = yesToAll;
    }

    /**
     * Sets the write output file.
     *
     * @param writeOutputFile the writeOutputFile to set
     */
    protected void setWriteOutputFile(boolean writeOutputFile) {
        this.writeOutputFile = writeOutputFile;
    }
}
