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

package com.sldeditor.extension.filesystem.file.ysld;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.filesystem.FileSystemInterface;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.extension.filesystem.FileSystemUtils;
import com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
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
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;

/**
 * Class that handles reading/writing YSLD files to the file system.
 *
 * @author Robert Ward (SCISYS)
 */
public class YSLDFileHandler implements FileHandlerInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2572421275144887604L;

    /** The Constant YSLD_FILE_EXTENSION. */
    private static final String YSLD_FILE_EXTENSION = "ysld";

    /** The sld writer. */
    private SLDWriterInterface sldWriter = null;

    /** The ysld writer. */
    private SLDWriterInterface ysldWriter = null;

    /** The Constant RESOURCE_ICON. */
    private static final String RESOURCE_ICON = "ui/filesystemicons/ysld.png";

    /** The tree icon SLD. */
    private Icon treeIcon = null;

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.FileHandlerInterface#getFileExtension()
     */
    @Override
    public List<String> getFileExtensionList() {
        return Arrays.asList(YSLD_FILE_EXTENSION);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#populate(com.sldeditor.extension.input.FileSystemInterface,
     * javax.swing.tree.DefaultTreeModel, com.sldeditor.extension.input.file.FileTreeNode)
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
     * @see com.sldeditor.extension.input.FileHandlerInterface#getSLDContents(com.sldeditor.extension.input.NodeInterface)
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
        List<SLDDataInterface> list = null;
        if (f != null) {
            list = new ArrayList<SLDDataInterface>();

            if (f.isDirectory()) {
                File[] listFiles = f.listFiles();
                if (listFiles != null) {
                    for (File subFile : listFiles) {
                        internalOpenFile(subFile, list);
                    }
                }
            } else {
                internalOpenFile(f, list);
            }

            if (list.isEmpty()) {
                list = null;
            }
        }
        return list;
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
                String contents = readFile(f, Charset.defaultCharset());

                StyledLayerDescriptor sld = Ysld.parse(contents);

                // Convert YSLD to SLD string
                if (sldWriter == null) {
                    sldWriter = SLDWriterFactory.createWriter(SLDOutputFormatEnum.SLD);
                }

                String sldContents = sldWriter.encodeSLD(null, sld);

                SLDDataInterface sldData = new SLDData(new StyleWrapper(f.getName()), sldContents);
                sldData.setSLDFile(f);
                sldData.setReadOnly(false);
                sldData.setOriginalFormat(SLDOutputFormatEnum.YSLD);

                list.add(sldData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#save(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public boolean save(SLDDataInterface sldData) {
        if (sldData == null) {
            return false;
        }

        File fileToSave = sldData.getSLDFile();

        if (ysldWriter == null) {
            ysldWriter = SLDWriterFactory.createWriter(SLDOutputFormatEnum.YSLD);
        }

        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(fileToSave));
            String contents =
                    ysldWriter.encodeSLD(
                            sldData.getResourceLocator(), SelectedSymbol.getInstance().getSld());
            out.write(contents);
            out.close();
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        sldData.setSLDFile(fileToSave);

        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.extension.input.file.FileHandlerInterface#getSLDName(com.sldeditor.ui.iface.SLDDataInterface)
     */
    @Override
    public String getSLDName(SLDDataInterface sldData) {
        if (sldData != null) {
            return sldData.getLayerNameWithOutSuffix() + "." + YSLDFileHandler.YSLD_FILE_EXTENSION;
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
     * @see com.sldeditor.datasource.extension.filesystem.node.file.FileHandlerInterface#getIcon(java.lang.String, java.lang.String)
     */
    @Override
    public Icon getIcon(String path, String filename) {
        if (treeIcon == null) {
            URL url = YSLDFileHandler.class.getClassLoader().getResource(RESOURCE_ICON);

            treeIcon = new ImageIcon(url);
        }
        return treeIcon;
    }
}
