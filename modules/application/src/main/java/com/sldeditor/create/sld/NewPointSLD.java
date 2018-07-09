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

package com.sldeditor.create.sld;

import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.create.NewSLDBase;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Class to create a new SLD point symbol with default values.
 *
 * @author Robert Ward (SCISYS)
 */
public class NewPointSLD extends NewSLDBase implements NewSLDInterface {

    /** Instantiates a new new point sld. */
    public NewPointSLD() {
        super(Localisation.getString(NewPointSLD.class, "NewPointSLD.title"));
    }

    /**
     * Creates the symbol.
     *
     * @return the styled layer descriptor
     */
    /* (non-Javadoc)
     * @see com.sldeditor.create.NewSLDInterface#create()
     */
    @Override
    public StyledLayerDescriptor create() {
        return DefaultSymbols.createNewPoint();
    }
}
