/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

package com.sldeditor.common.data;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Class DatabaseConnectionField.
 *
 * @author Robert Ward (SCISYS)
 */
public class DatabaseConnectionField {

    /** The key. */
    private String key;

    /** The field name. */
    private String fieldName;

    /** The optional. */
    private boolean optional = false;

    /** The username. */
    private boolean username = false;

    /** The password. */
    private boolean password = false;

    /** The default value. */
    private String defaultValue = null;

    /** The is filename. */
    private boolean isFilename = false;

    /** The file extension filter. */
    private FileNameExtensionFilter fileExtensionFilter = null;

    /**
     * Instantiates a new database connection field.
     *
     * @param key the key
     * @param fieldName the field name
     * @param optional the optional
     * @param username the username
     * @param password the password
     * @param defaultValue the default value
     */
    public DatabaseConnectionField(String key, String fieldName, boolean optional, boolean username,
            boolean password, String defaultValue) {
        super();
        this.key = key;
        this.fieldName = fieldName;
        this.optional = optional;
        this.username = username;
        this.password = password;
        this.defaultValue = defaultValue;
    }

    /**
     * Gets the field name.
     *
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Checks if is optional.
     *
     * @return the optional
     */
    public boolean isOptional() {
        return optional;
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
        return key;
    }

    /**
     * Gets the default value.
     *
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
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
     * Sets the file extension map.
     *
     * @param fileExtensionFilter the new file extension
     */
    public void setFileExtension(FileNameExtensionFilter fileExtensionFilter) {
        this.isFilename = true;
        this.fileExtensionFilter = fileExtensionFilter;
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
