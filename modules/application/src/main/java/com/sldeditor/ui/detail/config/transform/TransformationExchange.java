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
package com.sldeditor.ui.detail.config.transform;

/**
 * The Class TransformationExchange.
 *
 * @author Robert Ward (SCISYS)
 */
public class TransformationExchange {

    /** The singleton instance. */
    private static TransformationExchange instance = null;

    /** The implementation of the interface. */
    private TransformationInterface impl = null;

    /**
     * Instantiates a new transformation exchange.
     */
    private TransformationExchange()
    {
    }

    /**
     * Gets the singleton instance of TransformationExchange.
     *
     * @return single instance of TransformationExchange
     */
    public static TransformationExchange getInstance()
    {
        if(instance == null)
        {
            instance = new TransformationExchange();
        }

        return instance;
    }

    /**
     * Gets the impl.
     *
     * @return the impl
     */
    public TransformationInterface getImpl() {
        return impl;
    }

    /**
     * Sets the impl.
     *
     * @param impl the impl to set
     */
    public void setImpl(TransformationInterface impl) {
        this.impl = impl;
    }
}
