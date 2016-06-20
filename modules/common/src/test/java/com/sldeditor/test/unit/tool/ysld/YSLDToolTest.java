package com.sldeditor.test.unit.tool.ysld;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import org.junit.Assert;
import org.junit.Test;

import com.sldeditor.common.NodeInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.data.StyleWrapper;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.extension.filesystem.node.file.FileTreeNode;
import com.sldeditor.tool.ToolButton;
import com.sldeditor.tool.ysld.YSLDTool;

/**
 * The unit test for YSLDTool.
 * <p>{@link com.sldeditor.tool.ysld.YSLDTool}
 *
 * @author Robert Ward (SCISYS)
 */
public class YSLDToolTest {
    public static final String PREFIX = "extracted";

    /**
     * Test get layer name.
     */
    @Test
    public void testPanel() {
        YSLDTool tool = new YSLDTool();

        assertTrue(tool.getPanel() != null);
    }

    /**
     * Test sld file.
     */
    @Test
    public void testSetSelectedItems() {
        YSLDTool tool = new YSLDTool();

        JPanel panel = tool.getPanel();

        ToolButton toSLD = null;
        ToolButton toYSLD = null;

        for(Component c : panel.getComponents())
        {
            if(c instanceof ToolButton)
            {
                ToolButton button = (ToolButton) c;
                if(button.getToolTipText().compareTo("YSLDTool.exportToSLD") == 0)
                {
                    toSLD = button;
                }
                else if(button.getToolTipText().compareTo("YSLDTool.exportToYSLD") == 0)
                {
                    toYSLD = button;
                }
            }
        }

        File testFile1 = null;
        File testFile3 = null;
        try {
            testFile1 = File.createTempFile("invalid", ".tst");
            testFile1.deleteOnExit();
            testFile3 = File.createTempFile("valid", ".ysld");
            testFile3.deleteOnExit();
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
        SLDData sldData3 = getSLDDataFile("/point/sld/point_simplepoint.ysld");
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
    }

    /**
     * Test get tool name
     */
    @Test
    public void testGetToolName() {
        YSLDTool tool = new YSLDTool();

        String toolName = tool.getToolName();
        assertTrue(toolName.compareTo("com.sldeditor.tool.ysld.YSLDTool") == 0);
    }

    /**
     * Test which file types the tool supports
     */
    @Test
    public void testSupports() {

        YSLDTool tool = new YSLDTool();

        assertFalse(tool.supports(null, null, null));

        File testFile1 = null;
        File testFile2 = null;
        File testFile3 = null;
        try {
            testFile1 = File.createTempFile("invalid", ".tst");
            testFile1.deleteOnExit();
            testFile2 = File.createTempFile("valid", ".sld");
            testFile2.deleteOnExit();
            testFile3 = File.createTempFile("valid", ".ysld");
            testFile3.deleteOnExit();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // Try with invalid file
        try {
            List<NodeInterface> nodeTypeList = new ArrayList<NodeInterface>();
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
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @param suffix the suffix
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file (InputStream in, String suffix) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, suffix);
        tempFile.deleteOnExit();
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
    private static SLDData getSLDDataFile(String testfile)
    {
        SLDData sldData = null;

        InputStream inputStream = YSLDToolTest.class.getResourceAsStream(testfile);

        if(inputStream == null)
        {
            Assert.assertNotNull("Failed to find test file : " + testfile, inputStream);
        }
        else
        {
            File f = null;
            try {
                String fileExtension = ExternalFilenames.getFileExtension(testfile);
                f = stream2file(inputStream, ExternalFilenames.addFileExtensionSeparator(fileExtension));
                String sldContents = readFile(f.getAbsolutePath());

                if(fileExtension.compareTo("ysld") == 0)
                {
                    StyledLayerDescriptor sld = Ysld.parse(sldContents);

                    // Convert YSLD to SLD string
                    SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(SLDOutputFormatEnum.SLD);

                    sldContents = sldWriter.encodeSLD(sld);
                }

                sldData = new SLDData(new StyleWrapper(f.getName()), sldContents);
                sldData.setSLDFile(f);

                SelectedSymbol.getInstance().setSld(SLDUtils.createSLDFromString(sldData));
                f.deleteOnExit();
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
