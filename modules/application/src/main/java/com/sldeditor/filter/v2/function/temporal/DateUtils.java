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

package com.sldeditor.filter.v2.function.temporal;

import com.sldeditor.common.console.ConsoleManager;
import java.text.ParseException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.geotools.temporal.object.Utils;

/**
 * The Class DateUtils.
 *
 * @author Robert Ward (SCISYS)
 */
public class DateUtils {

    /** The date/time format. */
    private static DateTimeFormatter dtf = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * Gets the zoned date time.
     *
     * @param dateString the date string
     * @return the zoned date time
     */
    public static ZonedDateTime getZonedDateTime(String dateString) {
        ZonedDateTime zonedDateTime = null;

        if (dateString != null) {
            try {
                String updatedString = dateString.replace("Z", "+00:00");

                Date iso8601 = Utils.getDateFromString(updatedString);
                if (iso8601 != null) {
                    String zoneId = Utils.getTimeZone(updatedString);

                    zonedDateTime = ZonedDateTime.ofInstant(iso8601.toInstant(), ZoneId.of(zoneId));
                }
            } catch (ParseException e) {
                ConsoleManager.getInstance().exception(DateUtils.class, e);
            }
        }
        return zonedDateTime;
    }

    /**
     * Gets the string.
     *
     * @param date the date
     * @return the string
     */
    public static String getString(ZonedDateTime date) {
        if (date != null) {
            return date.format(dtf);
        }
        return "";
    }
}
