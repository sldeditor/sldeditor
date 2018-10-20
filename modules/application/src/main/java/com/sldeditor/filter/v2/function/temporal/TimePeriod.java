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

package com.sldeditor.filter.v2.function.temporal;

import java.time.ZoneId;
import org.geotools.temporal.object.DefaultPeriod;
import org.opengis.temporal.Instant;

/**
 * The Class TimePeriod.
 *
 * @author Robert Ward (SCISYS)
 */
public class TimePeriod {

    /** The start of the time period. */
    private Duration start = new Duration();

    /** The end of the time period. */
    private Duration end = new Duration();

    /** The separator. */
    private static String separator = "/";

    /**
     * Sets the start.
     *
     * @param start the start to set
     */
    public void setStart(Duration start) {
        this.start = start;
    }

    /**
     * Sets the end.
     *
     * @param end the end to set
     */
    public void setEnd(Duration end) {
        this.end = end;
    }

    /**
     * Gets the string.
     *
     * @return the string
     */
    public String getString() {
        return String.format("%s %s %s", start.getString(), separator, end.getString());
    }

    /**
     * Gets the start of the duration.
     *
     * @return the start
     */
    public Duration getStart() {
        return start;
    }

    /**
     * Gets the end of the duration.
     *
     * @return the end
     */
    public Duration getEnd() {
        return end;
    }

    /**
     * Decode a time period encoded as a string.
     *
     * @param stringValue the string value
     */
    public void decode(String stringValue) {
        String[] components = stringValue.replace(" ", "").split(separator);
        if (components.length == 2) {
            start = new Duration(components[0]);
            end = new Duration(components[1]);
        }
    }

    /**
     * Decode a DefaultPeriod object.
     *
     * @param objValue the obj value
     */
    public void decode(DefaultPeriod objValue) {
        DefaultPeriod defaultPeriod = objValue;
        Instant beginning = defaultPeriod.getBeginning();
        Duration startDuration = new Duration();
        startDuration.setDate(
                beginning.getPosition().getDate().toInstant().atZone(ZoneId.systemDefault()));
        setStart(startDuration);

        Instant localEnd = defaultPeriod.getEnding();
        Duration endDuration = new Duration();
        endDuration.setDate(
                localEnd.getPosition().getDate().toInstant().atZone(ZoneId.systemDefault()));
        setEnd(endDuration);
    }
}
