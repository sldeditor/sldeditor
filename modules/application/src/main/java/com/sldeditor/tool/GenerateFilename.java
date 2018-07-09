/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool;

import com.sldeditor.common.utils.ExternalFilenames;
import java.io.File;

/**
 * The Class GenerateFilename.
 *
 * @author Robert Ward (SCISYS)
 */
public class GenerateFilename {

    /**
     * Find unique name.
     *
     * @param destinationFolder the destination folder
     * @param filename the filename
     * @param fileExtension the file extension
     * @return the file
     */
    public static File findUniqueName(
            String destinationFolder, String filename, String fileExtension) {
        boolean found = false;
        int count = 0;
        String sldFilename;

        String adjustedFileExtension = null;

        if (fileExtension.startsWith(".")) {
            adjustedFileExtension = fileExtension;
        } else {
            adjustedFileExtension = ExternalFilenames.addFileExtensionSeparator(fileExtension);
        }

        while (!found) {
            if (count == 0) {
                sldFilename = String.format("%s%s", filename, adjustedFileExtension);
            } else {
                sldFilename = String.format("%s%d%s", filename, count, adjustedFileExtension);
            }

            File fileToSave = new File(destinationFolder, sldFilename);
            if (!fileToSave.exists()) {
                return fileToSave;
            }
            count++;
        }
        return null;
    }
}
