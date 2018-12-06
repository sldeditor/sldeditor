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

package com.sldeditor.test.unit.extension.filesystem.database.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface;
import com.sldeditor.extension.filesystem.database.client.DatabaseClient;
import com.sldeditor.tool.dbconnectionlist.DatabaseConnectionFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

/**
 * Unit test for DatabaseClient
 *
 * @author Robert Ward (SCISYS)
 */
class DatabaseClientTest {

    class TestDatabaseReadProgress implements DatabaseReadProgressInterface {

        /* (non-Javadoc)
         * @see com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface#startPopulating(com.sldeditor.common.data.DatabaseConnection)
         */
        @Override
        public void startPopulating(DatabaseConnection connection) {}

        /* (non-Javadoc)
         * @see com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface#readFeatureClassesComplete(com.sldeditor.common.data.DatabaseConnection, java.util.List)
         */
        @Override
        public void readFeatureClassesComplete(
                DatabaseConnection connection, List<String> featureClassList) {}
    }

    /**
     * Test method for {@link
     * com.sldeditor.extension.filesystem.database.client.DatabaseClient#initialise(com.sldeditor.extension.filesystem.database.DatabaseReadProgressInterface,
     * com.sldeditor.common.data.DatabaseConnection)}.
     */
    @Test
    void testInitialise() {
        TestDatabaseReadProgress progress = new TestDatabaseReadProgress();

        Path tempFolder = null;
        try {
            tempFolder = Files.createTempDirectory("sldeditor_DatabaseClientTest");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to create temp folder in temp folder!");
        }

        try {
            InputStream gpkgInputStream =
                    DatabaseClientTest.class.getResourceAsStream("/test/sld_cookbook_polygon.gpkg");

            File gpkgFile = new File(tempFolder.toFile(), "sld_cookbook_polygon.gpkg");
            try (FileOutputStream out = new FileOutputStream(gpkgFile)) {
                IOUtils.copy(gpkgInputStream, out);
            }

            DatabaseConnection databaseConnection =
                    DatabaseConnectionFactory.getConnection(gpkgFile.getAbsolutePath());

            DatabaseClient client = new DatabaseClient();

            client.initialise(progress, databaseConnection);
            assertFalse(client.isConnected());
            assertTrue(client.getFeatureClassList().isEmpty());

            // Connect to database
            assertTrue(client.connect());
            assertTrue(client.isConnected());

            // Retrieve feature class list from database
            client.retrieveData();
            assertFalse(client.getFeatureClassList().isEmpty());
            assertNotNull(client.getDBConnectionParams());

            // Disconnect from database
            client.disconnect();
            assertFalse(client.isConnected());
            assertTrue(client.getFeatureClassList().isEmpty());

            assertTrue(client.accept(databaseConnection));
            assertFalse(client.accept(null));

            // Delete the files we extracted
            gpkgFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.walk(tempFolder)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Test null parameters */
    @Test
    void testNullParameters() {
        DatabaseClient client = new DatabaseClient();
        assertFalse(client.isConnected());

        assertNull(client.getConnection());
        assertNull(client.getDBConnectionParams());

        // Connect to database
        assertFalse(client.connect());
        assertFalse(client.isConnected());

        // Retrieve feature class list from database
        client.retrieveData();
        assertTrue(client.getFeatureClassList().isEmpty());

        // Disconnect from database
        client.disconnect();
        assertFalse(client.isConnected());
    }
}
