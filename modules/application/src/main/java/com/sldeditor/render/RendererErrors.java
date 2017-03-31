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

package com.sldeditor.render;

import org.geotools.renderer.RenderListener;
import org.opengis.feature.simple.SimpleFeature;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class RendererErrors, only output error messages for the first object 
 * allocated top a renderer. The same error will be reported by the symbol
 * renderer and the legend renderer so this class reduces the number of
 * error messages reported.
 *
 * @author Robert Ward (SCISYS)
 */
public class RendererErrors implements RenderListener {

    /** The count. */
    private static int count = 0;

    /** The output messages. */
    private boolean outputMessages = false;

    /**
     * Instantiates a new renderer errors.
     *
     * @param outputMessages the output messages
     */
    public RendererErrors(boolean outputMessages) {
        this.outputMessages = outputMessages;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.RenderListener#featureRenderer(org.opengis.feature.simple.SimpleFeature)
     */
    @Override
    public void featureRenderer(SimpleFeature feature) {
        // Do nothing
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.renderer.RenderListener#errorOccurred(java.lang.Exception)
     */
    @Override
    public void errorOccurred(Exception e) {
        if (outputMessages) {
            if (e != null) {
                ConsoleManager.getInstance().error(this, e.getMessage());
            }
        }
    }

    /**
     * Gets the single instance of RendererErrors.
     *
     * @return single instance of RendererErrors
     */
    public static RenderListener getInstance() {
        boolean shouldOutputMessages = (count == 0);
        count++;
        return new RendererErrors(shouldOutputMessages);
    }

}
