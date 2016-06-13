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
package com.sldeditor.test.unit.helper;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.datasource.SLDEditorDataUpdateInterface;

/**
 * The Class SLDEditorDataUpdateInterfaceTestHelper.
 */
public class SLDEditorDataUpdateInterfaceTestHelper implements SLDEditorDataUpdateInterface {

    /** The method reached. */
    public boolean sldDataUpdated_methodReached = false;
    
    /** The sld data. */
    public SLDDataInterface sldDataUpdated_sldData = null;
    
    /** The data edited flag. */
    public boolean sldDataUpdated_dataEditedFlag = false;

    public void reset()
    {
        sldDataUpdated_methodReached = false;
        sldDataUpdated_sldData = null;
        sldDataUpdated_dataEditedFlag = false;
    }
    
    /**
     * Sld data updated.
     *
     * @param sldData the sld data
     * @param dataEditedFlag the data edited flag
     */
    @Override
    public void sldDataUpdated(SLDDataInterface sldData, boolean dataEditedFlag) {
        sldDataUpdated_methodReached = true;
        sldDataUpdated_sldData = sldData;
        sldDataUpdated_dataEditedFlag = dataEditedFlag;
    }

}
