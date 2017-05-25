/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool;

import java.io.File;

import com.sldeditor.common.utils.ExternalFilenames;

/**
 * The Class GenerateFile.
 *
 * @author Robert Ward (SCISYS)
 */
public class GenerateFile {

    /**
     * Find unique name.
     *
     * @param destinationFolder the destination folder
     * @param filename the filename
     * @param fileExtension the file extension
     * @return the file
     */
    public static File findUniqueName(String destinationFolder, String filename,
            String fileExtension) {
        boolean found = false;
        int count = 0;
        String sldFilename;

        while (!found) {
            if (count == 0) {
                sldFilename = String.format("%s%s", filename,
                        ExternalFilenames.addFileExtensionSeparator(fileExtension));
            } else {
                sldFilename = String.format("%s%d%s", filename, count,
                        ExternalFilenames.addFileExtensionSeparator(fileExtension));
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
