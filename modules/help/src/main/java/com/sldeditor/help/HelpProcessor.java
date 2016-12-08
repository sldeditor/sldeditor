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

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

/**
 * The Class HelpReader.
 *
 * @author Robert Ward (SCISYS)
 */
public class HelpProcessor {

    /** The Constant INDEX_FILE. */
    private static final String INDEX_FILE = "index.txt";

    /** The Constant SRC_RESOURCE_FOLDER. */
    private static final String SRC_RESOURCE_FOLDER = "help";
    
    /** The Constant DEST_RESOURCE_FOLDER. */
    private static final String DEST_RESOURCE_FOLDER = "help/html";

    /** The parser. */
    private Parser parser;

    /** The extension list. */
    private List<Extension> extensionList = null;

    /** The source folder. */
    private String sourceFolder = null;

    /** The destination folder. */
    private String destFolder = null;

    /** The console. */
    private ConsoleManager console = new ConsoleManager();

    /** The index list. */
    private List<String> indexList = new ArrayList<String>();

    /** The extensions. */
    private String[] extensions = new String[] { "md" };

    /**
     * Instantiates a new help reader.
     *
     * @param sourceFolder the source folder
     * @param destFolder the dest folder
     */
    public HelpProcessor(String sourceFolder, String destFolder) {
        this.sourceFolder = sourceFolder;
        this.destFolder = destFolder;

        extensionList = Arrays.asList(TablesExtension.create());

        parser = Parser.builder().extensions(extensionList).build();

        // Clean out destination folder
        File folder = new File(destFolder, DEST_RESOURCE_FOLDER);
        
        try {
            FileUtils.cleanDirectory(folder);
        } catch (IOException e) {
            console.exception(this, e);
        }
    }

    /**
     * Read help data.
     */
    public void process() {

        File f = new File(sourceFolder, SRC_RESOURCE_FOLDER);
        File[] subdirs = f.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
        if (subdirs != null) {
            for (File dir : subdirs) {
                processLocale(dir.getName());
            }
        }
    }

    /**
     * Process locale.
     *
     * @param locale the locale
     */
    private void processLocale(String locale) {
        System.out.println("Processing locale : " + locale);

        Collection<File> files = null;
        File folder = new File(getInputFolder(sourceFolder, locale));
        files = FileUtils.listFiles(folder, extensions, false);

        if (files != null) {
            for (File inputFile : files) {

                String filename = String.format("%s/%s/%s/%s.html", destFolder, DEST_RESOURCE_FOLDER,
                        locale.toString(), FilenameUtils.getBaseName(inputFile.getAbsolutePath()));
                File outputFile = new File(filename);

                String html = parse(inputFile);

                try {
                    console.information(this, "Parsing " + inputFile.getName());
                    FileUtils.writeByteArrayToFile(outputFile, html.getBytes());
                } catch (IOException e) {
                    console.exception(this, e);
                }
            }
            try {
                // Index file
                String indexFilename = String.format("%s/%s/%s/%s", destFolder, DEST_RESOURCE_FOLDER,
                        locale.toString(), INDEX_FILE);
                File indexFile = new File(indexFilename);

                console.information(this, "Writing index");
                StringBuilder indexSb = new StringBuilder();
                for (String index : indexList) {
                    indexSb.append(index);
                    indexSb.append("\n");
                }
                FileUtils.writeByteArrayToFile(indexFile, indexSb.toString().getBytes());
            } catch (IOException e) {
                console.exception(this, e);
            }
        }
    }

    /**
     * Gets the input folder.
     *
     * @param srcFolder the src folder
     * @param localeString the locale string
     * @return the input folder
     */
    private static String getInputFolder(String srcFolder, String localeString) {
        return String.format("%s/%s/%s/", srcFolder, SRC_RESOURCE_FOLDER, localeString);
    }

    /**
     * Parses the markdown file.
     *
     * @param file the file
     * @return the string
     */
    private String parse(File file) {
        try {
            int count = 0;
            StringBuilder sb = new StringBuilder();
            LineIterator it = FileUtils.lineIterator(file, "UTF-8");
            try {
                while (it.hasNext()) {
                    String line = it.nextLine();
                    if (count == 0) {
                        addIndex(line, FilenameUtils.getBaseName(file.getAbsolutePath()) + ".html");
                    }
                    sb.append(line);
                    sb.append("\n");
                    count++;
                }
            } finally {
                it.close();
            }

            Node document;
            document = parser.parse(sb.toString());
            HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensionList).build();
            StringBuilder outputSb = new StringBuilder();
            outputSb.append("<div class=\"markdown-body\">");
            outputSb.append(renderer.render(document));
            outputSb.append("</div>");

            return outputSb.toString();
        } catch (FileNotFoundException e1) {
            console.exception(this, e1);
        } catch (IOException e1) {
            console.exception(this, e1);
        }
        return "";
    }

    /**
     * Adds the index.
     *
     * @param line the line
     * @param file the file
     */
    private void addIndex(String line, String file) {
        String s = String.format("%s,%s", line.replace("#", "").trim(), file);
        indexList.add(s);
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("HelpProcess <source folder> <destination folder>");
        } else {
            String sourceFolder = args[0];
            String destFolder = args[1];

            File f = new File(sourceFolder, SRC_RESOURCE_FOLDER);
            if (!f.exists()) {
                System.err.println("Source folder does not exist : " + f.getAbsolutePath());
            } else {
                System.err.println("Source folder : " + f.getAbsolutePath());

                f = new File(destFolder, SRC_RESOURCE_FOLDER);
                if (!f.exists()) {
                    System.err.println("Destination folder does not exist : " + f.getAbsolutePath());
                } else {
                    System.err.println("Destination folder : " + f.getAbsolutePath());
                    HelpProcessor help = new HelpProcessor(sourceFolder, destFolder);

                    help.process();
                }
            }
        }
    }
}
