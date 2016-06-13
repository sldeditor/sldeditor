/*
 *    ConvertMXD - Part of SLD Editor application
 *    Exports an Esri MXD file to an intermediate json structure file
 *    for use in SLDEditor.
 *
 *    (C) 2016, SCISYS
 *
 */
package com.sldeditor.importdata.esri;

import com.sun.jna.Pointer;

/**
 * The Class SystemWin, java interface to Windows User32 dll
 * 
 * @author Robert Ward (SCISYS)
 */
public class SystemWin
{   
    /** The Constant api. */
    private static final User32API api;  

    /**
     * Instantiates a new system win.
     */
    public SystemWin()   
    {  
    }

    /**
     * Gets the desktop window.
     *
     * @return the desktop window
     */
    public Pointer getDesktopWindow()
    {
        return api.GetDesktopWindow();
    }

    static   
    {      
        api = User32API.INSTANCE;   
    }  
} 
