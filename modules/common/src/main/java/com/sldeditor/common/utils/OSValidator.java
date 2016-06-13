/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
 */

/**
 * Author: Domenico Monaco, Yong Mook Kim
 *  
 * Source: https://gist.github.com/kiuz/816e24aa787c2d102dd0
 *  
 * License: GNU v2 2014
 *
 * Fork / Learned: http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 *
 */

package com.sldeditor.common.utils;

/**
 * The Class OSValidator.
 *
 * @author Robert Ward (SCISYS)
 */
public class OSValidator {

    /** The os. */
    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        System.out.println(OS);

        if (isWindows()) {
            System.out.println("This is Windows");
        } else if (isMac()) {
            System.out.println("This is Mac");
        } else if (isUnix()) {
            System.out.println("This is Unix or Linux");
        } else if (isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not support!!");
        }
    }

    /**
     * Checks if is windows.
     *
     * @return true, if is windows
     */
    public static boolean isWindows() {
        return (OS.indexOf("win") >= 0);
    }

    /**
     * Checks if is mac.
     *
     * @return true, if is mac
     */
    public static boolean isMac() {
        return (OS.indexOf("mac") >= 0);
    }

    /**
     * Checks if is unix.
     *
     * @return true, if is unix
     */
    public static boolean isUnix() {
        return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
    }

    /**
     * Checks if is solaris.
     *
     * @return true, if is solaris
     */
    public static boolean isSolaris() {
        return (OS.indexOf("sunos") >= 0);
    }
    
    /**
     * Gets the os.
     *
     * @return the os
     */
    public static String getOS(){
        if (isWindows()) {
            return "win";
        } else if (isMac()) {
            return "osx";
        } else if (isUnix()) {
            return "uni";
        } else if (isSolaris()) {
            return "sol";
        } else {
            return "err";
        }
    }

}