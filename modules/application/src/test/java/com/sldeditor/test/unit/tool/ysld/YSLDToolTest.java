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

package com.sldeditor.test.unit.tool.ysld;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ysld.YSLDTool;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.apache.commons.io.IOUtils;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.ysld.Ysld;
import org.junit.jupiter.api.Test;

/**
 * The unit test for YSLDTool.
 *
 * <p>{@link com.sldeditor.tool.ysld.YSLDTool}
 *
 * @author Robert Ward (SCISYS)
 */
public class YSLDToolTest {
    public static final String PREFIX = "extracted";

    /** Test get layer name. */
    @Test
    public void testPanel() {
        YSLDTool tool = new YSLDTool();

        assertTrue(tool.getPanel() != null);
    }

    /** Test sld file. */
    @Test
    public void testSetSelectedItems() {
        YSLDTool tool = new YSLDTool();

        JPanel panel = tool.getPanel();

        ToolButton toSLD = null;
        ToolButton toYSLD = null;

        for (Component c : panel.getComponents()) {
            if (c instanceof ToolButton) {
                ToolButton button = (ToolButton) c;
                String toolTipText = button.getToolTipText();
                if (toolTipText.compareTo(
                                Localisation.getString(YSLDTool.class, "YSLDTool.exportToSLD"))
                        == 0) {
                    toSLD = button;
                } else if (toolTipText.compareTo(
                                Localisation.getString(YSLDTool.class, "YSLDTool.exportToYSLD"))
                        == 0) {
                    toYSLD = button;
                }
            }
        }

        File testFile1 = null;
        File testFile3 = null;
        try {
            testFile1 = File.createTempFile("invalid", ".tst");
            testFile3 = File.createTempFile("valid", ".ysld");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Should both be disabled
        assertFalse(toSLD.isEnabled());
        assertFalse(toYSLD.isEnabled());

        tool.setSelectedItems(null, null);

        // Invalid file
        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();
        SLDData sldData1 = new SLDData(null, null);
        sldData1.setSLDFile(testFile1);
        sldDataList.add(sldData1);
        tool.setSelectedItems(null, sldDataList);

        // Should both be disabled
        assertFalse(toSLD.isEnabled());
        assertFalse(toYSLD.isEnabled());

        // Try with valid sld file
        sldDataList = new ArrayList<SLDDataInterface>();
        SLDData sldData2 = getSLDDataFile("/point/sld/point_simplepoint.sld");
        sldDataList.add(sldData2);
        tool.setSelectedItems(null, sldDataList);

        // YSLD should be enabled
        assertTrue(toYSLD.isEnabled());
        assertFalse(toSLD.isEnabled());

        toYSLD.doClick();

        // Try with valid ysld file
        sldDataList = new ArrayList<SLDDataInterface>();
        SLDData sldData3 = getSLDDataFile("/point/ysld/point_simplepoint.ysld");
        sldDataList.add(sldData3);
        tool.setSelectedItems(null, sldDataList);

        // SLD should be enabled
        assertTrue(toSLD.isEnabled());
        assertFalse(toYSLD.isEnabled());
        toSLD.doClick();

        // Try with valid sld and ysld files
        sldDataList = new ArrayList<SLDDataInterface>();
        sldDataList.add(sldData2);
        sldDataList.add(sldData3);
        tool.setSelectedItems(null, sldDataList);

        // SLD and YSLD should be enabled
        assertTrue(toSLD.isEnabled());
        assertTrue(toYSLD.isEnabled());

        testFile1.delete();
        testFile3.delete();

        tidyUpTempFiles(sldData2.getSLDFile());
        tidyUpTempFiles(sldData3.getSLDFile());
    }

    private void tidyUpTempFiles(File sldFile) {
        String filename = sldFile.getAbsolutePath();
        int index = filename.lastIndexOf('.');
        filename = filename.substring(0, index);

        File sld = new File(filename + ".sld");
        sld.delete();
        File ysld = new File(filename + ".ysld");
        ysld.delete();
    }

    /** Test get tool name. */
    @Test
    public void testGetToolName() {
        YSLDTool tool = new YSLDTool();

        String toolName = tool.getToolName();
        assertTrue(toolName.compareTo("com.sldeditor.tool.ysld.YSLDTool") == 0);
    }

    /** Test which file types the tool supports. */
    @Test
    public void testSupports() {

        YSLDTool tool = new YSLDTool();

        assertFalse(tool.supports(null, null, null));

        File testFile1 = null;
        File testFile2 = null;
        File testFile3 = null;
        try {
            testFile1 = File.createTempFile("invalid", ".tst");
            testFile2 = File.createTempFile("valid", ".sld");
            testFile3 = File.createTempFile("valid", ".ysld");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Try with invalid file
        try {
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            assertNotNull(testFile1);
            nodeTypeList.add(new FileTreeNode(testFile1.getParentFile(), testFile1.getName()));
            assertFalse(tool.supports(null, nodeTypeList, null));
        } catch (SecurityException | FileNotFoundException e) {
            e.printStackTrace();
        }

        // Try with valid sld file
        try {
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            nodeTypeList.add(new FileTreeNode(testFile2.getParentFile(), testFile2.getName()));
            assertTrue(tool.supports(null, nodeTypeList, null));
        } catch (SecurityException | FileNotFoundException e) {
            e.printStackTrace();
        }

        // Try with valid ysld file
        try {
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            nodeTypeList.add(new FileTreeNode(testFile3.getParentFile(), testFile3.getName()));
            assertTrue(tool.supports(null, nodeTypeList, null));
        } catch (SecurityException | FileNotFoundException e) {
            e.printStackTrace();
        }

        // Try with several files
        try {
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
            nodeTypeList.add(new FileTreeNode(testFile1.getParentFile(), testFile1.getName()));
            nodeTypeList.add(new FileTreeNode(testFile2.getParentFile(), testFile2.getName()));
            nodeTypeList.add(new FileTreeNode(testFile3.getParentFile(), testFile3.getName()));
            assertFalse(tool.supports(null, nodeTypeList, null));
        } catch (SecurityException | FileNotFoundException e) {
            e.printStackTrace();
        }

        testFile1.delete();
        testFile2.delete();
        testFile3.delete();
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @param suffix the suffix
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file(InputStream in, String suffix) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, suffix);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }
        return tempFile;
    }

    /**
     * Gets the SLD/YSLD file.
     *
     * @param testfile the testfile
     * @return the SLD data file
     */
    private static SLDData getSLDDataFile(String testfile) {
        SLDData sldData = null;

        InputStream inputStream = YSLDToolTest.class.getResourceAsStream(testfile);

        if (inputStream == null) {
            assertNotNull(inputStream, "Failed to find test file : " + testfile);
        } else {
            File f = null;
            try {
                String fileExtension = ExternalFilenames.getFileExtension(testfile);
                f =
                        stream2file(
                                inputStream,
                                ExternalFilenames.addFileExtensionSeparator(fileExtension));
                String sldContents = readFile(f.getAbsolutePath());

                if (fileExtension.compareTo("ysld") == 0) {
                    StyledLayerDescriptor sld = Ysld.parse(sldContents);

                    // Convert YSLD to SLD string
                    SLDWriterInterface sldWriter =
                            SLDWriterFactory.createWriter(SLDOutputFormatEnum.SLD);

                    sldContents = sldWriter.encodeSLD(null, sld);
                }

                sldData = new SLDData(new StyleWrapper(f.getName()), sldContents);
                sldData.setSLDFile(f);

                SelectedSymbol.getInstance().setSld(SLDUtils.createSLDFromString(sldData));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sldData;
    }

    /**
     * Read file.
     *
     * @param fileName the file name
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }
}
