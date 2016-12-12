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

package com.sldeditor.common.console;

/**
 * The Class ConsoleData.
 *
 * @author Robert Ward (SCISYS)
 */
public class ConsoleData {

    /** The message. */
    private String message;

    /** The type. */
    private ConsoleDataEnum type = ConsoleDataEnum.INFORMATION;

    /**
     * Instantiates a new console data.
     *
     * @param message the message
     * @param type the type
     */
    public ConsoleData(String message, ConsoleDataEnum type) {
        super();
        this.message = message;
        this.type = type;
    }

    /**
     * Gets the message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public ConsoleDataEnum getType() {
        return type;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return message;
    }

}
