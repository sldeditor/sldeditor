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
package com.sldeditor.rendertransformation;

import org.geotools.process.function.ProcessFunction;

import com.sldeditor.common.connection.GeoServerConnectionManager;
import com.sldeditor.ui.detail.config.transform.TransformationExchange;
import com.sldeditor.ui.detail.config.transform.TransformationInterface;

/**
 * Class to allow data to be exchanges between SLDEditor libraries.<p>
 * 
 * Not ideal.
 *
 * @author Robert Ward (SCISYS)
 */
public class RenderTransformManager implements TransformationInterface {

    /**
     * Register the render transformation dialog with backend.
     */
    public void register() {
        TransformationExchange.getInstance().setImpl(this);
    }

    /**
     * Show transformation dialog.
     *
     * @param existingProcessFunction the existing process function
     * @return the new transformation process function
     */
    @Override
    public ProcessFunction showTransformationDialog(ProcessFunction existingProcessFunction) {
        ProcessFunction processFunction = null;
        RenderTransformationDialog dlg = new RenderTransformationDialog(GeoServerConnectionManager.getInstance());

        if(dlg.showDialog(existingProcessFunction))
        {
            processFunction = dlg.getTransformationProcessFunction();
        }

        return processFunction;
    }

}
