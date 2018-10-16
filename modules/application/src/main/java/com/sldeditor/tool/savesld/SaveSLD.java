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

package com.sldeditor.tool.savesld;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SLDExternalImages;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.common.output.impl.SLDWriterFactory;
import com.sldeditor.common.utils.ExternalFilenames;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.tool.ToolPanel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.log4j.Logger;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.util.URLs;

/**
 * The Class SaveSLD.
 *
 * @author Robert Ward (SCISYS)
 */
public class SaveSLD implements SaveSLDInterface {

    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 4096;

    /** The logger. */
    private static Logger logger = Logger.getLogger(ToolPanel.class);

    /** The overwrite destination dlg. */
    private SaveSLDDestinationInterface overwriteDestinationDlg = new SaveSLDDestination();

    /** The suffix, separator and file extension */
    private final String suffix =
            ExternalFilenames.addFileExtensionSeparator(SLDEditorFile.getSLDFileExtension());

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.tool.savesld.SaveSLDInterface#saveAllSLDToFolder(java.util.List,
     * java.io.File, boolean)
     */
    @Override
    public void saveAllSLDToFolder(
            List<SLDDataInterface> sldDataList,
            File destinationFolder,
            boolean saveExternalResources) {
        SLDWriterInterface sldWriter = SLDWriterFactory.createWriter(null);

        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        logger.info(Localisation.getString(SaveSLDTool.class, "SaveSLDTool.saveAllSLD"));

        boolean yesToAll = false;

        for (SLDDataInterface sldData : sldDataList) {
            StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);

            if (sld != null) {
                String sldString = sldWriter.encodeSLD(sldData.getResourceLocator(), sld);

                String sldFilename = sldData.getLayerName();

                // Ensure we don't get duplicate sld file extensions
                if (!sldFilename.endsWith(suffix)) {
                    sldFilename = sldFilename + suffix;
                }

                File fileToSave = new File(destinationFolder, sldFilename);

                ConsoleManager.getInstance()
                        .information(
                                this,
                                Localisation.getField(SaveSLDTool.class, "SaveSLDTool.savingSLD")
                                        + " "
                                        + sldData.getLayerName());

                // Write SLD string to file
                try (BufferedWriter out = new BufferedWriter(new FileWriter(fileToSave))) {
                    out.write(sldString);
                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }

                // Save external images if requested
                if (saveExternalResources) {
                    List<String> externalImageList =
                            SLDExternalImages.getExternalImages(sldData.getResourceLocator(), sld);

                    for (String externalImage : externalImageList) {
                        File output = new File(destinationFolder, externalImage);

                        File parentFolder = output.getParentFile();

                        // Check to see if the destination folder exists
                        if (!parentFolder.exists()) {
                            if (output.getParentFile().mkdirs()) {
                                ConsoleManager.getInstance()
                                        .error(
                                                this,
                                                Localisation.getField(
                                                                SaveSLDTool.class,
                                                                "SaveSLDTool.error1")
                                                        + output.getAbsolutePath());
                            }
                        }

                        if (parentFolder.exists()) {
                            boolean writeOutputFileFlag = true;

                            if (output.exists()) {
                                if (!yesToAll) {
                                    overwriteDestinationDlg.overwrite(output);

                                    yesToAll = overwriteDestinationDlg.isYesToAll();
                                    writeOutputFileFlag =
                                            overwriteDestinationDlg.isWriteOutputFile();
                                }
                            }

                            if (writeOutputFileFlag) {
                                writeOutputFile(sldData, externalImage, output);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Write output file.
     *
     * @param sldData the sld data
     * @param externalImage the external image
     * @param output the output
     */
    private void writeOutputFile(SLDDataInterface sldData, String externalImage, File output) {
        URL input;
        InputStream inputStream = null;
        try {
            input = URLs.extendUrl(sldData.getResourceLocator(), externalImage);
            URLConnection connection = input.openConnection();

            inputStream = connection.getInputStream();
        } catch (MalformedURLException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (IOException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        if (inputStream != null) {
            try (FileOutputStream outputStream = new FileOutputStream(output);
                    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream)); ) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int n = -1;

                while ((n = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, n);
                }
                ConsoleManager.getInstance()
                        .information(
                                this,
                                Localisation.getField(
                                                SaveSLDTool.class,
                                                "SaveSLDTool.savingExternalImage")
                                        + " "
                                        + externalImage);
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

    /**
     * Overrides the overwrite destination dialog. Used for testing to prevent a modal dialog from
     * being displayed.
     *
     * @param overwriteDestinationDlg the overwriteDestinationDlg to set
     */
    protected void setOverwriteDestinationDlg(SaveSLDDestinationInterface overwriteDestinationDlg) {
        this.overwriteDestinationDlg = overwriteDestinationDlg;
    }
}
