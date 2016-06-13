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
package com.sldeditor.create;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleFactoryImpl;

/**
 * Base class used to create a new SLD symbols with default values.
 * 
 * @author Robert Ward (SCISYS)
 */
public class NewSLDBase {

    /** The name. */
    private String name;

    /** The style factory. */
    private static StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

    /**
     * Instantiates a new new sld base.
     *
     * @param name the name
     */
    public NewSLDBase(String name)
    {
        this.name = name;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the style factory.
     *
     * @return the style factory
     */
    protected static StyleFactoryImpl getStyleFactory()
    {
        return styleFactory;
    }
}
