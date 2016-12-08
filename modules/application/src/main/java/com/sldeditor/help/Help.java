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

package com.sldeditor.help;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * The Class Help.
 *
 * @author Robert Ward (SCISYS)
 */
public class Help {

    /** The Constant USER_GUIDE_URL. */
    private static final String USER_GUIDE_URL = "https://github.com/robward-scisys/sldeditor/wiki/userguide";

    /** The singleton instance. */
    private static Help instance = null;

    /**
     * Gets the single instance of Help.
     *
     * @return single instance of Help
     */
    public static Help getInstance() {
        if (instance == null) {
            instance = new Help();
        }

        return instance;
    }

    /**
     * Instantiates a new help reader.
     */
    private Help() {
    }

    /**
     * Display user guide section.
     *
     * @param section the section
     * @return the string
     */
    public void display(String section) {
        URL url = null;
        try {
            if(section == null)
            {
                url = new URL(USER_GUIDE_URL);
            }
            else
            {
                url = new URL(USER_GUIDE_URL + "#" + section);
            }
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(url.toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

}
