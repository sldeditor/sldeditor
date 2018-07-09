/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.common.data;

import javax.swing.filechooser.FileNameExtensionFilter;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.jdbc.JDBCDataStoreFactory;

/**
 * The Class DatabaseConnectionField.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionField {

    private Param param = null;

    /** The username. */
    private boolean username = false;

    /** The password. */
    private boolean password = false;

    /** The is filename. */
    private boolean isFilename = false;

    /** The file extension filter. */
    private FileNameExtensionFilter fileExtensionFilter = null;

    /**
     * Instantiates a new database connection field.
     *
     * @param param the param
     */
    public DatabaseConnectionField(Param param) {
        super();
        this.param = param;
        if (param.key.equals(JDBCDataStoreFactory.USER.key)) {
            username = true;
        }

        password = param.isPassword();
    }

    /**
     * Instantiates a new database connection field.
     *
     * @param param the param
     * @param fileExtensionFilter the file extension filter
     */
    public DatabaseConnectionField(Param param, FileNameExtensionFilter fileExtensionFilter) {
        super();
        this.param = param;
        this.isFilename = true;
        this.fileExtensionFilter = fileExtensionFilter;
    }

    /**
     * Gets the field name.
     *
     * @return the fieldName
     */
    public String getFieldName() {
        return param.getDescription().toString();
    }

    /**
     * Checks if is optional.
     *
     * @return the optional
     */
    public boolean isOptional() {
        return !param.required;
    }

    /**
     * Checks if is username.
     *
     * @return the username
     */
    public boolean isUsername() {
        return username;
    }

    /**
     * Checks if is password.
     *
     * @return the password
     */
    public boolean isPassword() {
        return password;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return param.key;
    }

    public Class<?> getType() {
        return param.getType();
    }

    /**
     * Gets the default value.
     *
     * @return the defaultValue
     */
    public Object getDefaultValue() {
        return param.getDefaultValue();
    }

    /**
     * Checks if is filename.
     *
     * @return the isFilename
     */
    public boolean isFilename() {
        return isFilename;
    }

    /**
     * Gets the file extension filter.
     *
     * @return the fileExtensionFilter
     */
    public FileNameExtensionFilter getFileExtensionFilter() {
        return fileExtensionFilter;
    }
}
