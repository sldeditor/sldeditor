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

package com.sldeditor.test.unit.update;

import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.update.CheckUpdate;
import com.sldeditor.update.CheckUpdateClientInterface;
import com.sldeditor.update.CheckUpdatePanel;
import com.sldeditor.update.UpdateData;

/**
 * The unit test for CheckUpdatePanel.
 * <p>{@link com.sldeditor.update.CheckUpdatePanel}
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdatePanelTest {

    /**
     * The Class TestCheckUpdatePanel.
     */
    class TestCheckUpdatePanel extends CheckUpdatePanel
    {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The Constant LATEST_VERSION. */
        private static final String LATEST_VERSION = "0.4.0";

        /** The Constant DESCRIPTION. */
        private static final String DESCRIPTION = "<html>\n  <head>\n    \n  </head>\n  <body>\n    Description\n  </body>\n</html>\n";

        /** The Constant EMPTY_STRING. */
        private static final String EMPTY_STRING = "<html>\r\n  <head>\r\n\r\n  </head>\r\n  <body>\r\n    <p style=\"margin-top: 0\">\r\n      \r\n    </p>\r\n  </body>\r\n</html>\r\n";;

        /**
         * Test check for latest version.
         *
         * @param currentVersion the current version
         * @param client the client
         */
        public void testCheckForLatestVersion(String currentVersion,
                CheckUpdateClientInterface client)
        {
            checkForLatestVersion(currentVersion, client, false);
        }

        /**
         * Test in unreachable state.
         *
         * @return true, if successful
         */
        public boolean testUnreachable() {
            String expectedStatus = Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.destinationUnreachable");
            boolean status = expectedStatus.equals(lblStatus.getText());
            boolean latest = "".equals(lblLatestVersion.getText());
            boolean getButton = (btnGet.isVisible() == false);

            return status && getButton && latest;
        }

        /**
         * Test no update.
         *
         * @return true, if successful
         */
        public boolean testNoUpdate() {
            String latestVersionString = String.format("%s %s",
                    Localisation.getField(CheckUpdatePanel.class, "CheckUpdatePanel.latestVersion"), 
                    TestCheckUpdatePanel.LATEST_VERSION);

            String expectedStatus = Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.runningLatest");
            boolean status = expectedStatus.equals(lblStatus.getText());
            boolean latest = latestVersionString.equals(lblLatestVersion.getText());
            boolean getButton = (btnGet.isVisible() == false);
            String text = textArea.getText();
            boolean description = compareHtml(EMPTY_STRING, text);

            return status && getButton && latest && description;
        }

        /**
         * Test later version.
         *
         * @return true, if successful
         */
        public boolean testLaterVersion() {
            String latestVersionString = String.format("%s %s",
                    Localisation.getField(CheckUpdatePanel.class, "CheckUpdatePanel.latestVersion"), 
                    TestCheckUpdatePanel.LATEST_VERSION);

            String expectedStatus = Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.newVersionAvailable");
            boolean status = expectedStatus.equals(lblStatus.getText());
            boolean latest = latestVersionString.equals(lblLatestVersion.getText());
            boolean getButton = (btnGet.isVisible() == true);
            String text = textArea.getText();
            boolean description = compareHtml(DESCRIPTION, text);

            return status && getButton && latest && description;
        }
    }

    /**
     * The Class TestCheckUpdateClient.
     */
    class TestCheckUpdateClient implements CheckUpdateClientInterface
    {

        /** The destination reached. */
        public boolean destinationReached = false;

        /* (non-Javadoc)
         * @see com.sldeditor.update.CheckUpdateClientInterface#getLatest()
         */
        @Override
        public UpdateData getLatest() {
            UpdateData updateData = new UpdateData(VersionData.decode(CheckUpdate.class, TestCheckUpdatePanel.LATEST_VERSION), TestCheckUpdatePanel.DESCRIPTION);
            return updateData;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.update.CheckUpdateClientInterface#getDownloadURL()
         */
        @Override
        public URL getDownloadURL() {
            return null;
        }

        /* (non-Javadoc)
         * @see com.sldeditor.update.CheckUpdateClientInterface#isDestinationReached()
         */
        @Override
        public boolean isDestinationReached() {
            return destinationReached;
        }

    }
    /**
     * Test method for {@link com.sldeditor.update.CheckUpdatePanel#checkForLatestVersion(java.lang.String, com.sldeditor.update.CheckUpdateClientInterface)}.
     */
    @Test
    public void testCheckForLatestVersion() {
        TestCheckUpdatePanel panel = new TestCheckUpdatePanel();

        panel.testCheckForLatestVersion("0.3.0", null);

        TestCheckUpdateClient client = new TestCheckUpdateClient();
        panel.testCheckForLatestVersion("0.3.0", client);
        assertTrue(panel.testUnreachable());

        client.destinationReached = true;
        panel.testCheckForLatestVersion("0.6.0", client);
        assertTrue(panel.testNoUpdate());

        panel.testCheckForLatestVersion("0.1.0", client);
        assertTrue(panel.testLaterVersion());
    }

    /**
     * Compare html strings by stripping out newlines, carriage returns
     * so unit tests work on different operating systems.
     *
     * @param string the string
     * @param text the text
     * @return true, if successful
     */
    private static boolean compareHtml(String string, String text) {
        return removeNewLine(string).equals(removeNewLine(text));
    }

    /**
     * Removes the new line, carriage returns.
     *
     * @param string the string
     * @return the string
     */
    private static String removeNewLine(String string) {
        return string.replace("\n", "").replace("\r", "");
    }

}
