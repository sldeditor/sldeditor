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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;

/**
 * The Class HelpReader.
 *
 * @author Robert Ward (SCISYS)
 */
public class HelpReader {

    /** The help list. */
    private DefaultListModel<HelpData> helpList = new DefaultListModel<HelpData>();

    /** The Constant HTML_RESOURCE_FOLDER. */
    private static final String HTML_RESOURCE_FOLDER = "help/html";

    /** The Constant INDEX_FILE. */
    private static final String INDEX_FILE = "index.txt";

    /**
     * Instantiates a new help reader.
     */
    public HelpReader() {
    }

    /**
     * Read help data.
     */
    private void readHelpData() {
        Locale locale = Localisation.getInstance().getCurrentLocale();
        String indexFile = String.format("/%s/%s/%s", HTML_RESOURCE_FOLDER, locale.toString(),
                INDEX_FILE);

        InputStream inStream = HelpReader.class.getResourceAsStream(indexFile);
        Reader reader = new InputStreamReader(inStream);
        BufferedReader br = new BufferedReader(reader);

        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                String[] components = line.split(",");
                String htmlFile = String.format("/%s/%s/%s", HTML_RESOURCE_FOLDER, locale.toString(), components[1]);

                HelpData helpData = new HelpData(components[0], htmlFile);
                helpList.addElement(helpData);
            }
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
    }

    /**
     * Gets the index entries.
     *
     * @return the index entries
     */
    public ListModel<HelpData> getIndexEntries() {
        if (helpList.isEmpty()) {
            readHelpData();
        }
        return helpList;
    }

    /**
     * Parses the markdown file.
     *
     * @param helpData the help data
     * @return the string
     */
    public String parse(HelpData helpData) {
        InputStream inStream = HelpReader.class.getResourceAsStream(helpData.getFile());
        Reader reader = new InputStreamReader(inStream);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return sb.toString();
    }

}
