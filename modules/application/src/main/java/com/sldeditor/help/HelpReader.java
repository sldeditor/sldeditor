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
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

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

    /** The Constant RESOURCE_FOLDER. */
    private static final String RESOURCE_FOLDER = "help";

    /** The parser. */
    private Parser parser;

    /** The extension list. */
    private List<Extension> extensionList = null;

    /**
     * Instantiates a new help reader.
     */
    public HelpReader() {
        extensionList = Arrays.asList(TablesExtension.create());

        parser = Parser.builder().extensions(extensionList).build();
    }

    /**
     * Read help data.
     */
    private void readHelpData() {

        List<String> files = null;
        Locale locale = Localisation.getInstance().getCurrentLocale();
        String folder = String.format("%s/%s/", RESOURCE_FOLDER, locale.toString());
        try {
            files = IOUtils.readLines(HelpReader.class.getClassLoader().getResourceAsStream(folder),
                    Charsets.UTF_8);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        if (files != null) {
            for (String key : files) {

                String title = key;
                String file = folder + key;

                HelpData data = new HelpData(title, file);

                helpList.addElement(data);
            }
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
        InputStream inStream = HelpReader.class.getResourceAsStream("/" + helpData.getFile());
        Reader reader = new InputStreamReader(inStream);
        BufferedReader br = new BufferedReader(reader);

        Node document;
        try {
            document = parser.parseReader(br);
            HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensionList).build();
            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"markdown-body\">");
            sb.append(renderer.render(document));
            sb.append("</div>");

            return sb.toString();
          } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }
        return "";
    }

}
