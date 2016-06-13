/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.win32.StdCallLibrary;

/**
 * The Interface User32API.
 * 
 * @author Robert Ward (SCISYS)
 */
public interface User32API extends StdCallLibrary
{
    
    /**
     * The Interface WNDENUMPROC.
     */
    public static interface WNDENUMPROC extends com.sun.jna.win32.StdCallLibrary.StdCallCallback 
    {    
        
        /**
         * Callback.
         *
         * @param pointer the pointer
         * @param pointer1 the pointer1
         * @return true, if successful
         */
        public abstract boolean callback(Pointer pointer, Pointer pointer1);
    } 
    
    /** The Constant INSTANCE. */
    public static final User32API INSTANCE = (User32API)Native.loadLibrary("user32", User32API.class);  

    /**
     * Gets the desktop window.
     *
     * @return the pointer
     */
    public abstract Pointer GetDesktopWindow(); 
} 