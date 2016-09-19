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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEParameterSpec;

import com.sldeditor.common.console.ConsoleManager;

/**
 * Class that encrypts/decrypts strings.
 * 
 * @author Robert Ward (SCISYS)
 */
public class EncryptedProperties extends Properties {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The decoder. */
    @SuppressWarnings("restriction")
    private static sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();

    /** The encoder. */
    @SuppressWarnings("restriction")
    private static sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    /** The encrypter. */
    private Cipher encrypter = null;

    /** The decrypter. */
    private Cipher decrypter = null;

    /** The salt. */
    private static byte[] salt = { (byte) 0x03, 0x0F, 0x12, 0x0D, 0x03, 0x0F, 0x12, 0x0D };

    /** The singleton instance. */
    private static EncryptedProperties instance = null;

    /**
     * Gets the single instance of EncryptedProperties.
     *
     * @return single instance of EncryptedProperties
     */
    public static synchronized EncryptedProperties getInstance()
    {
        if(instance == null)
        {
            String password = generatePassword();

            try
            {
                instance = new EncryptedProperties(password);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return instance;
    }

    /**
     * Generate password.
     *
     * @return the string
     */
    private static String generatePassword()
    {
        String password = "sldEditor";

        StringBuilder sb = new StringBuilder();

        sb.append(getUniqueIdentifier());

        sb.append(System.getProperty("user.name"));

        password = sb.toString();

        return password;
    }

    /**
     * Gets a unique identifier using the machine name.
     *
     * @return the unique identifier
     */
    private static String getUniqueIdentifier() {
        String hostname = "Unknown";

        try
        {
            InetAddress addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
        }
        catch (UnknownHostException ex)
        {
            ConsoleManager.getInstance().error(EncryptedProperties.class, "Hostname can not be resolved");
        }

        return hostname;
    }

    /**
     * Instantiates a new encrypted properties.
     *
     * @param password the password
     */
    private EncryptedProperties(String password) {
        PBEParameterSpec ps = new javax.crypto.spec.PBEParameterSpec(salt, 20);
        SecretKeyFactory kf;
        try
        {
            kf = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey k = kf.generateSecret(new javax.crypto.spec.PBEKeySpec(password.toCharArray()));
            encrypter = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            decrypter = Cipher.getInstance("PBEWithMD5AndDES/CBC/PKCS5Padding");
            encrypter.init(Cipher.ENCRYPT_MODE, k, ps);
            decrypter.init(Cipher.DECRYPT_MODE, k, ps);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
        }
        catch (InvalidKeyException e)
        {
            e.printStackTrace();
        }
        catch (InvalidAlgorithmParameterException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Decrypt a string
     *
     * @param str the str
     * @return the string
     */
    @SuppressWarnings("restriction")
    public synchronized String decrypt(String str) {
        byte[] dec;
        try
        {
            dec = decoder.decodeBuffer(str);
            byte[] utf8 = decrypter.doFinal(dec);
            return new String(utf8, "UTF-8");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Encrypt a string
     *
     * @param str the str
     * @return the string
     */
    @SuppressWarnings("restriction")
    public synchronized String encrypt(String str) {
        byte[] utf8;
        try
        {
            utf8 = str.getBytes("UTF-8");
            byte[] enc = encrypter.doFinal(utf8);
            return encoder.encode(enc);
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        }
        catch (BadPaddingException e)
        {
            e.printStackTrace();
        }

        return "";
    }
}
