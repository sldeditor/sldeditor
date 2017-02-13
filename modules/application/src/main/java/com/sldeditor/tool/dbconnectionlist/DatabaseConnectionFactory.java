/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.tool.dbconnectionlist;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileNameExtensionFilter;

import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.db2.DB2NGDataStoreFactory;
import org.geotools.data.h2.H2DataStoreFactory;
import org.geotools.data.mysql.MySQLDataStoreFactory;
import org.geotools.data.oracle.OracleNGDataStoreFactory;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.spatialite.SpatiaLiteDataStoreFactory;
import org.geotools.data.sqlserver.SQLServerDataStoreFactory;
import org.geotools.data.teradata.TeradataDataStoreFactory;
import org.geotools.geopkg.GeoPkgDataStoreFactory;
import org.geotools.jdbc.JDBCDataStoreFactory;

import com.sldeditor.common.data.DatabaseConnection;
import com.sldeditor.common.data.DatabaseConnectionField;
import com.sldeditor.common.localisation.Localisation;

/**
 * A factory for creating DatabaseConnection objects.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionFactory {

    /** The Constant GEOPACKAGE_FILE_EXTENSION. */
    private static final String GEOPACKAGE_FILE_EXTENSION = "gpkg";

    /** The Constant SPATIALITE_FILE_EXTENSION. */
    private static final String SPATIALITE_FILE_EXTENSION = "db";

    /** The Constant DATABASE_TYPE_KEY. */
    public static final String DATABASE_TYPE_KEY = JDBCDataStoreFactory.DBTYPE.key;

    /** The name map. */
    private static Map<String, String> nameMap = new HashMap<String, String>();

    /**
     * Creates a new DatabaseConnection object for a GeoPackage.
     *
     * @return the database connection
     */
    public static DatabaseConnection createGeoPackage() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(Localisation
                .getString(DatabaseConnector.class, "DatabaseConnectorGeoPkg.fileExtension")
                + " (*." + GEOPACKAGE_FILE_EXTENSION + ")", GEOPACKAGE_FILE_EXTENSION);

        list.add(new DatabaseConnectionField(GeoPkgDataStoreFactory.DATABASE, filter));
        list.add(new DatabaseConnectionField(GeoPkgDataStoreFactory.USER));

        GeoPkgDataStoreFactory factory = new GeoPkgDataStoreFactory();
        DatabaseConnection databaseConnection = new DatabaseConnection(
                GeoPkgDataStoreFactory.DBTYPE, factory.getDisplayName(), false, list,
                new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = "Not set";
                        String databaseName = properties.get(JDBCDataStoreFactory.DATABASE.key);
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
     * Creates a new DatabaseConnection object for a GeoPackage.
     *
     * @return the database connection
     */
    public static DatabaseConnection createSpatiaLite() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(SpatiaLiteDataStoreFactory.DATABASE));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(Localisation
                .getString(DatabaseConnector.class, "DatabaseConnectorGeoPkg.fileExtension")
                + " (*." + SPATIALITE_FILE_EXTENSION + ")", SPATIALITE_FILE_EXTENSION);
        list.add(new DatabaseConnectionField(SpatiaLiteDataStoreFactory.USER, filter));

        SpatiaLiteDataStoreFactory factory = new SpatiaLiteDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(
                SpatiaLiteDataStoreFactory.DBTYPE, factory.getDisplayName(), false, list,
                new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = "Not set";
                        String databaseName = properties.get(JDBCDataStoreFactory.DATABASE.key);
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
     * Creates a new DatabaseConnection object for DB2.
     *
     * @return the database connection
     */
    public static DatabaseConnection createDB2() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(DB2NGDataStoreFactory.HOST));
        list.add(new DatabaseConnectionField(DB2NGDataStoreFactory.PORT));
        Param TABSCHEMA = new Param("tabschema", String.class, "Schema", false);
        list.add(new DatabaseConnectionField(TABSCHEMA));
        list.add(new DatabaseConnectionField(DB2NGDataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(DB2NGDataStoreFactory.USER));
        list.add(new DatabaseConnectionField(DB2NGDataStoreFactory.PASSWD));

        DB2NGDataStoreFactory factory = new DB2NGDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(DB2NGDataStoreFactory.DBTYPE,
                factory.getDisplayName(), true, list, new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s/%s@%s:%s",
                                properties.get("tabschema"),
                                properties.get(JDBCDataStoreFactory.DATABASE.key),
                                properties.get(JDBCDataStoreFactory.HOST.key),
                                properties.get(JDBCDataStoreFactory.PORT.key));

                        for (int i = 0; i < noOfTimesDuplicated; noOfTimesDuplicated++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Creates a new DatabaseConnection object for H2.
     *
     * @return the database connection
     */
    public static DatabaseConnection createH2() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(H2DataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(H2DataStoreFactory.USER));

        H2DataStoreFactory factory = new H2DataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(H2DataStoreFactory.DBTYPE,
                factory.getDisplayName(), false, list, new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = "Not set";
                        String databaseName = properties.get(JDBCDataStoreFactory.DATABASE.key);
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
     * Creates a new DatabaseConnection object for MySQL.
     *
     * @return the database connection
     */
    public static DatabaseConnection createMySQL() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(MySQLDataStoreFactory.HOST));
        list.add(new DatabaseConnectionField(MySQLDataStoreFactory.PORT));
        list.add(new DatabaseConnectionField(MySQLDataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(MySQLDataStoreFactory.STORAGE_ENGINE));
        list.add(new DatabaseConnectionField(MySQLDataStoreFactory.USER));
        list.add(new DatabaseConnectionField(MySQLDataStoreFactory.PASSWD));

        MySQLDataStoreFactory factory = new MySQLDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(MySQLDataStoreFactory.DBTYPE,
                factory.getDisplayName(), true, list, new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s@%s:%s",
                                properties.get(MySQLDataStoreFactory.DATABASE.key),
                                properties.get(MySQLDataStoreFactory.HOST.key),
                                properties.get(MySQLDataStoreFactory.PORT.key));

                        for (int i = 0; i < noOfTimesDuplicated; noOfTimesDuplicated++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Creates a new DatabaseConnection object for Oracle.
     *
     * @return the database connection
     */
    public static DatabaseConnection createOracle() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(OracleNGDataStoreFactory.HOST));
        list.add(new DatabaseConnectionField(OracleNGDataStoreFactory.PORT));
        list.add(new DatabaseConnectionField(OracleNGDataStoreFactory.SCHEMA));
        list.add(new DatabaseConnectionField(OracleNGDataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(OracleNGDataStoreFactory.USER));
        list.add(new DatabaseConnectionField(OracleNGDataStoreFactory.PASSWD));

        OracleNGDataStoreFactory factory = new OracleNGDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(
                OracleNGDataStoreFactory.DBTYPE, factory.getDisplayName(), true, list,
                new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s/%s@%s:%s",
                                properties.get(OracleNGDataStoreFactory.SCHEMA.key),
                                properties.get(OracleNGDataStoreFactory.DATABASE.key),
                                properties.get(OracleNGDataStoreFactory.HOST.key),
                                properties.get(OracleNGDataStoreFactory.PORT.key));

                        for (int i = 0; i < noOfTimesDuplicated; noOfTimesDuplicated++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Creates a new DatabaseConnection object for SQL Server.
     *
     * @return the database connection
     */
    public static DatabaseConnection createSQLServer() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.HOST));
        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.PORT));
        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.INSTANCE));
        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.SCHEMA));
        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.USER));
        list.add(new DatabaseConnectionField(SQLServerDataStoreFactory.PASSWD));

        SQLServerDataStoreFactory factory = new SQLServerDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(
                SQLServerDataStoreFactory.DBTYPE, factory.getDisplayName(), true, list,
                new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s/%s@%s:%s",
                                properties.get(SQLServerDataStoreFactory.INSTANCE.key),
                                properties.get(SQLServerDataStoreFactory.DATABASE.key),
                                properties.get(SQLServerDataStoreFactory.HOST.key),
                                properties.get(SQLServerDataStoreFactory.PORT.key));

                        for (int i = 0; i < noOfTimesDuplicated; noOfTimesDuplicated++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Creates a new DatabaseConnection object for Teradata.
     *
     * @return the database connection
     */
    public static DatabaseConnection createTeradata() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(TeradataDataStoreFactory.HOST));
        list.add(new DatabaseConnectionField(TeradataDataStoreFactory.PORT));
        list.add(new DatabaseConnectionField(TeradataDataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(TeradataDataStoreFactory.USER));
        list.add(new DatabaseConnectionField(TeradataDataStoreFactory.SCHEMA));
        list.add(new DatabaseConnectionField(TeradataDataStoreFactory.PASSWD));

        TeradataDataStoreFactory factory = new TeradataDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(
                TeradataDataStoreFactory.DBTYPE, factory.getDisplayName(), true, list,
                new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s/%s@%s:%s",
                                properties.get(TeradataDataStoreFactory.SCHEMA.key),
                                properties.get(TeradataDataStoreFactory.DATABASE.key),
                                properties.get(TeradataDataStoreFactory.HOST.key),
                                properties.get(TeradataDataStoreFactory.PORT.key));

                        for (int i = 0; i < noOfTimesDuplicated; noOfTimesDuplicated++) {
                            connectionName = duplicatePrefix + connectionName;
                        }
                        return connectionName;
                    }
                });

        return databaseConnection;
    }

    /**
     * Creates a new DatabaseConnection object for Postgres.
     *
     * @return the database connection
     */
    public static DatabaseConnection createPostgres() {
        List<DatabaseConnectionField> list = new ArrayList<DatabaseConnectionField>();

        list.add(new DatabaseConnectionField(PostgisNGDataStoreFactory.HOST));
        list.add(new DatabaseConnectionField(PostgisNGDataStoreFactory.PORT));
        list.add(new DatabaseConnectionField(PostgisNGDataStoreFactory.DATABASE));
        list.add(new DatabaseConnectionField(PostgisNGDataStoreFactory.SCHEMA));
        list.add(new DatabaseConnectionField(PostgisNGDataStoreFactory.USER));
        list.add(new DatabaseConnectionField(PostgisNGDataStoreFactory.PASSWD));

        PostgisNGDataStoreFactory factory = new PostgisNGDataStoreFactory();

        DatabaseConnection databaseConnection = new DatabaseConnection(
                PostgisNGDataStoreFactory.DBTYPE, factory.getDisplayName(), true, list,
                new DatabaseConnectionName() {

                    @Override
                    public String getConnectionName(String duplicatePrefix, int noOfTimesDuplicated,
                            Map<String, String> properties) {
                        String connectionName = String.format("%s/%s@%s:%s",
                                properties.get(PostgisNGDataStoreFactory.SCHEMA.key),
                                properties.get(PostgisNGDataStoreFactory.DATABASE.key),
                                properties.get(PostgisNGDataStoreFactory.HOST.key),
                                properties.get(PostgisNGDataStoreFactory.PORT.key));

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
            if (type.equals(GeoPkgDataStoreFactory.DBTYPE.sample)) {
                return createGeoPackage();
            } else if (type.equals(PostgisNGDataStoreFactory.DBTYPE.sample)) {
                return createPostgres();
            } else if (type.equals(SpatiaLiteDataStoreFactory.DBTYPE.sample)) {
                return createSpatiaLite();
            } else if (type.equals(TeradataDataStoreFactory.DBTYPE.sample)) {
                return createTeradata();
            } else if (type.equals(SQLServerDataStoreFactory.DBTYPE.sample)) {
                return createSQLServer();
            } else if (type.equals(OracleNGDataStoreFactory.DBTYPE.sample)) {
                return createOracle();
            } else if (type.equals(MySQLDataStoreFactory.DBTYPE.sample)) {
                return createMySQL();
            } else if (type.equals(H2DataStoreFactory.DBTYPE.sample)) {
                return createH2();
            } else if (type.equals(DB2NGDataStoreFactory.DBTYPE.sample)) {
                return createDB2();
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
        List<String> list = new ArrayList<String>();

        if (nameMap.isEmpty()) {
            populateNameMap();
        }

        for (String key : nameMap.keySet()) {
            list.add(key);
        }
        return list;
    }

    /**
     * Populate name map.
     */
    private static void populateNameMap() {
        Iterator<DataStoreFactorySpi> datastore = DataStoreFinder.getAvailableDataStores();

        while (datastore.hasNext()) {
            DataStoreFactorySpi dSPI = datastore.next();

            Param dbType = null;
            for (Param param : dSPI.getParametersInfo()) {
                if (param.key.equals(JDBCDataStoreFactory.DBTYPE.key)) {
                    dbType = param;
                    break;
                }
            }
            if (dbType != null) {
                nameMap.put(dSPI.getDisplayName(), (String) dbType.sample);
            }
        }
    }

    /**
     * Gets the new connection.
     *
     * @param selectedDatabaseType the selected database type
     * @return the new connection
     */
    public static DatabaseConnection getNewConnection(String selectedDatabaseType) {
        if (nameMap.isEmpty()) {
            populateNameMap();
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
