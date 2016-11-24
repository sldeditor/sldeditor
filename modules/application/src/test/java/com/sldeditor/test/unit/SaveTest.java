/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.test.unit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.sldeditor.SLDEditor;

/**
 * Test the save/save as reload functionality.
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class SaveTest {

    @Test
    public void test() {
        String testsldfile = "/line/sld/line_dashdot.sld";
        SLDEditor sldEditor = null;

        InputStream inputStream = SaveTest.class.getResourceAsStream(testsldfile);

        try {
            File file = stream2file(inputStream);

            sldEditor = SLDEditor.createAndShowGUI(null, null, false);

            sldEditor.openFile(file.toURI().toURL());

            sldEditor.saveFile(file.toURI().toURL());

            File saveAsFile = new File(file.getParentFile(), "SaveAs" + file.getName());
            sldEditor.saveFile(saveAsFile.toURI().toURL());
            sldEditor.saveFile(saveAsFile.toURI().toURL());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static File stream2file (InputStream in) throws IOException {
        final File tempFile = File.createTempFile(SaveTest.class.getName(), ".sld");
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        return tempFile;
    }
}
