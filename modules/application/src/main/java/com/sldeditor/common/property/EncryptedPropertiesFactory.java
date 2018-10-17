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

package com.sldeditor.common.property;

import com.sldeditor.common.console.ConsoleManager;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Class that encrypts/decrypts strings.
 *
 * @author Robert Ward (SCISYS)
 */
public class EncryptedPropertiesFactory {

    /** The singleton instance. */
    private static EncryptedPropertiesInterface instance = null;

    /** The encrypted properties class name. */
    @SuppressWarnings("unused")
    private static String encryptedPropertiesClassName = null;

    /**
     * Override class name.
     *
     * @param className the class name
     */
    public static void overrideClassName(String className) {
        encryptedPropertiesClassName = className;
    }

    /** Reset singleton. */
    public static void reset() {
        instance = null;
    }

    /**
     * Gets the singleton instance of EncryptedPropertiesFactory.
     *
     * @return singleton instance of EncryptedPropertiesFactory
     */
    public static synchronized EncryptedPropertiesInterface getInstance() {
        if (instance == null) {
            String password = generatePassword();

            try {
                instance = new EncryptedPropertiesApache();

                instance.initialise(password);
            } catch (Exception e) {
                ConsoleManager.getInstance().exception(EncryptedPropertiesFactory.class, e);
            }
        }

        return instance;
    }

    /**
     * Generate password.
     *
     * @return the string
     */
    private static String generatePassword() {
        StringBuilder sb = new StringBuilder();

        sb.append(getUniqueIdentifier());

        sb.append(System.getProperty("user.name"));

        String password = sb.toString();

        return password;
    }

    /**
     * Gets a unique identifier using the machine name.
     *
     * @return the unique identifier
     */
    private static String getUniqueIdentifier() {
        String hostname = "Unknown";

        try {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        } catch (UnknownHostException ex) {
            ConsoleManager.getInstance()
                    .error(EncryptedPropertiesFactory.class, "Hostname can not be resolved");
        }

        return hostname;
    }

    /**
     * Decrypt a string.
     *
     * @param str the str
     * @return the string
     */
    public synchronized String decrypt(String str) {
        return instance.decrypt(str);
    }

    /**
     * Encrypt a string.
     *
     * @param str the str
     * @return the string
     */
    public synchronized String encrypt(String str) {
        return instance.encrypt(str);
    }
}
