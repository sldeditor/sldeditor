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

package com.sldeditor.ui.reportissue;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import com.sldeditor.common.console.ConsoleManager;

/**
 * The Class ReportIssue.
 *
 * @author Robert Ward (SCISYS)
 */
public class ReportIssue {

    /** The Constant REPORT_ISSUE_URL. */
    private static final String REPORT_ISSUE_URL = "https://github.com/robward-scisys/sldeditor/wiki/contributing";

    /** The singleton instance. */
    private static ReportIssue instance = null;

    /**
     * Gets the single instance of ReportIssue.
     *
     * @return single instance of ReportIssue
     */
    public static ReportIssue getInstance() {
        if (instance == null) {
            instance = new ReportIssue();
        }

        return instance;
    }

    /**
     * Instantiates a new report issue class.
     */
    private ReportIssue() {
    }

    /**
     * Display report issue section.
     */
    public void display() {
        URL url = null;
        try {
            url = new URL(REPORT_ISSUE_URL);
        } catch (MalformedURLException e1) {
            ConsoleManager.getInstance().exception(this, e1);
        }

        if (url != null) {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(url.toURI());
                } catch (IOException | URISyntaxException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
        }
    }

}
