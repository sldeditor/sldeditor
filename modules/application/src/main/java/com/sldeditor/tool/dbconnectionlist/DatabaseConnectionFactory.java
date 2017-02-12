/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool.dbconnectionlist;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.DatabaseConnectionField;
import com.sldeditor.common.localisation.Localisation;

/**
 * A factory for creating DatabaseConnection objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionFactory {

    private static final String GEOPACKAGE_FILE_EXTENSION = "gpkg";

    private static final String GEO_PACKAGE_NAME = "GeoPackage";

    private static final String POSTGRES_NAME = "Postgres";

    /** The Constant DATABASE_TYPE_KEY. */
    public static final String DATABASE_TYPE_KEY = "dbtype";

    /** The Constant DATABASE_TYPE_GEOPKG. */
    public static final String DATABASE_TYPE_GEOPKG = "geopkg";

    /** The Constant DATABASE_TYPE_POSTGRES. */
    public static final String DATABASE_TYPE_POSTGRES = "postgis";

    /** The name list. */
    private static List<String> nameList = Arrays.asList(POSTGRES_NAME, GEO_PACKAGE_NAME);

    /** The name map. */
    private static Map<String, String> nameMap = new HashMap<String, String>();

    /**
     * Creates a new DatabaseConnection object for a GeoPackage.
     *
     * @return the database connection
     */
    public static DatabaseConnection createGeoPackage() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        DatabaseConnectionField database = new DatabaseConnectionField("database",
                "DatabaseConnectorGeoPkg.database", false, false, false, null);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(Localisation
                .getString(DatabaseConnector.class, "DatabaseConnectorGeoPkg.fileExtension")
                + " (*." + GEOPACKAGE_FILE_EXTENSION + ")", GEOPACKAGE_FILE_EXTENSION);
        database.setFileExtension(filter);
        list.add(database);

        list.add(new DatabaseConnectionField("user", "DatabaseConnectorGeoPkg.username", true, true,
                false, null));

        DatabaseConnection databaseConnection = new DatabaseConnection(DATABASE_TYPE_GEOPKG,
                GEO_PACKAGE_NAME, false, list, new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = "Not set";
                        String databaseName = properties.get("database");
                        if (databaseName != null) {
                            File f = new File(databaseName);
                            if (f.isFile()) {
                                connectionName = f.getName();
                            }
                        }

                        for (int i = 0; i < noOfTimesDuplicated; i++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Creates a new DatabaseConnection object for Postgres
     *
     * @return the database connection
     */
    public static DatabaseConnection createPostgres() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField("host", "DatabaseConnectorPostgres.server", false,
                false, false, null));
        list.add(new DatabaseConnectionField("port", "DatabaseConnectorPostgres.port", false, false,
                false, "5432"));
        list.add(new DatabaseConnectionField("database", "DatabaseConnectorPostgres.database",
                false, false, false, null));
        list.add(new DatabaseConnectionField("user", "DatabaseConnectorPostgres.username", false,
                true, false, null));
        list.add(new DatabaseConnectionField("schema", "DatabaseConnectorPostgres.schema", false,
                false, false, "public"));
        list.add(new DatabaseConnectionField("passwd", "DatabaseConnectorPostgres.password", false,
                false, true, null));

        DatabaseConnection databaseConnection = new DatabaseConnection(DATABASE_TYPE_POSTGRES,
                POSTGRES_NAME, true, list, new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s/%s@%s:%s",
                                properties.get("schema"), properties.get("database"),
                                properties.get("host"), properties.get("port"));

                        for (int i = 0; i < noOfTimesDuplicated; noOfTimesDuplicated++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Decode string.
     *
     * @param localConnectionDataMap the local connection data map
     * @return the database connection
     */
    public static DatabaseConnection decodeString(Map<String, String> localConnectionDataMap) {
        String type = localConnectionDataMap.get(DATABASE_TYPE_KEY);

        return createDefault(type);
    }

    /**
     * Creates a new DatabaseConnection object.
     *
     * @param type the type
     * @return the database connection
     */
    private static DatabaseConnection createDefault(String type) {
        if (type != null) {
            if (type.equals(DATABASE_TYPE_GEOPKG)) {
                return createGeoPackage();
            } else if (type.equals(DATABASE_TYPE_POSTGRES)) {
                return createPostgres();
            }
        }
        return null;
    }

    /**
     * Gets the list of database connection names.
     *
     * @return the names
     */
    public static List<String> getNames() {
        return nameList;
    }

    public static DatabaseConnection getNewConnection(String selectedDatabaseType) {
        if (nameMap.isEmpty()) {
            nameMap.put(GEO_PACKAGE_NAME, DATABASE_TYPE_GEOPKG);
            nameMap.put(POSTGRES_NAME, DATABASE_TYPE_POSTGRES);
        }
        String type = nameMap.get(selectedDatabaseType);
        return createDefault(type);
    }

    /**
     * Gets the new connection.
     *
     * @param databaseConnection the database connection
     * @return the new connection
     */
    public static DatabaseConnection getNewConnection(DatabaseConnection databaseConnection) {
        return createDefault(databaseConnection.getDatabaseType());
    }

}
