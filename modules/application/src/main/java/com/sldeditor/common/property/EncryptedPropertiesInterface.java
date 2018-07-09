package com.sldeditor.common.property;

public interface EncryptedPropertiesInterface {

    /**
     * Initialise.
     *
     * @param password the password
     */
    void initialise(String password);

    /**
     * Decrypt a string.
     *
     * @param str the str
     * @return the string
     */
    String decrypt(String str);

    /**
     * Encrypt a string.
     *
     * @param str the str
     * @return the string
     */
    String encrypt(String str);
}
