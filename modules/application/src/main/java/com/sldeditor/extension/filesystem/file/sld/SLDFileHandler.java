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

package com.sldeditor.extension.filesystem.file.sld;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.datasource.extension.filesystem.FileSystemUtils;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.tool.ToolManager;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.tree.DefaultTreeModel;

/**
 * Class that handles reading/writing SLD files to the file system.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDFileHandler implements FileHandlerInterface {
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3122710411389246976L;

    /** The Constant RESOURCE_ICON. */
    private static final String RESOURCE_ICON = "ui/filesystemicons/sld.png";

    /** The tree icon SLD. */
    private Icon treeIcon = null;

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.FileHandlerInterface#getFileExtension()
     */
    @Override
    public List<String> getFileExtensionList() {
        return Arrays.asList("sld");
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.extension.input.file.FileHandlerInterface#populate(com.sldeditor.extension.
     * input.FileSystemInterface, javax.swing.tree.DefaultTreeModel,
     * com.sldeditor.extension.input.file.FileTreeNode)
     */
    @Override
    public boolean populate(
            FileSystemInterface inputInterface, DefaultTreeModel treeModel, FileTreeNode node) {
        // Do nothing
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.extension.input.FileHandlerInterface#getSLDContents(com.sldeditor.extension.
     * input.NodeInterface)
     */
    @Override
    public List<SLDDataInterface> getSLDContents(NodeInterface node) {
        if (node instanceof FileTreeNode) {
            FileTreeNode fileTreeNode = (FileTreeNode) node;

            if (fileTreeNode.isDir()) {
                // Cater for folders
                return open(fileTreeNode.getFile());
            } else {
                // Cater for single file
                File f = fileTreeNode.getFile();

                String fileExtension = ExternalFilenames.getFileExtension(f.getAbsolutePath());
                if (getFileExtensionList().contains(fileExtension)) {
                    return open(f);
                }
            }
        }
        return null;
    }

    /**
     * Read file.
     *
     * @param file the file
     * @param encoding the encoding
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String readFile(File file, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        return new String(encoded, encoding);
    }

    /**
     * Open.
     *
     * @param f the file
     * @return the list
     */
    @Override
    public List<SLDDataInterface> open(File f) {
        if (f != null) {
            List<SLDDataInterface> list = new ArrayList<SLDDataInterface>();

            if (f.isDirectory()) {
                boolean traverseFolders = ToolManager.getInstance().isRecursiveFlag();

                walk(f, list, traverseFolders);
            } else {
                internalOpenFile(f, list);
            }

            if (list.isEmpty()) {
                return null;
            }

            return list;
        }
        return null;
    }

    /**
     * Walk the file system tree.
     *
     * @param root the root
     * @param list the list
     * @param recurse the recurse
     */
    private void walk(File root, List<SLDDataInterface> list, boolean recurse) {
        File[] fileList = root.listFiles();

        if (fileList == null) {
            return;
        }

        for (File f : fileList) {
            if (f.isDirectory() && recurse) {
                walk(f, list, recurse);
            } else {
                internalOpenFile(f, list);
            }
        }
    }

    /**
     * Internal open file.
     *
     * @param f the file
     * @param list the list
     */
    private void internalOpenFile(File f, List<SLDDataInterface> list) {
        if (f.isFile() && FileSystemUtils.isFileExtensionSupported(f, getFileExtensionList())) {
            try {
                String sldContents = readFile(f, Charset.defaultCharset());

                SLDDataInterface sldData = new SLDData(new StyleWrapper(f.getName()), sldContents);
                sldData.setSLDFile(f);
                sldData.setReadOnly(false);

                list.add(sldData);
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#save(com.sldeditor.ui.iface.
     * SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData) {
        if (sldData == null) {
            return false;
        }

        File fileToSave = sldData.getSLDFile();
        String sldString = sldData.getSld();

        try (BufferedWriter out = new BufferedWriter(new FileWriter(fileToSave))) {
            out.write(sldString);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        sldData.setSLDFile(fileToSave);

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.extension.input.file.FileHandlerInterface#getSLDName(com.sldeditor.ui.iface.
     * SLDDataInterface)
     */
    @Override
    public String getSLDName(SLDDataInterface sldData) {
        if (sldData != null) {
            return sldData.getLayerNameWithOutSuffix()
                    + ExternalFilenames.addFileExtensionSeparator(
                            SLDEditorFile.getSLDFileExtension());
        }

        return "";
    }

    /**
     * Returns if files selected are a data source, e.g. raster or vector.
     *
     * @return true, if is data source
     */
    @Override
    public boolean isDataSource() {
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface#getIcon(java.
     * lang.String, java.lang.String)
     */
    @Override
    public Icon getIcon(String path, String filename) {
        if (treeIcon == null) {
            URL url = SLDFileHandler.class.getClassLoader().getResource(RESOURCE_ICON);

            treeIcon = new ImageIcon(url);
        }
        return treeIcon;
    }
}
