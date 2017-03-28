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

package com.sldeditor.ui.ttf;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * Character Map #4 - Display Characters and Copy to System Clipboard
 * Written by: Keith Fenske, http://www.psc-consulting.ca/fenske/
 * Monday, 19 May 2008
 * Java class name: CharMap4
 * Copyright (c) 2008 by Keith Fenske.  Released under GNU Public License.
 * 
 * This is a Java 5.0 graphical (GUI) application to display Unicode characters
 * or glyphs in text fonts, and copy those characters to the system clipboard.
 * Its major purpose is as a visual accessory for word processors such as
 * Microsoft Word.  The "character map" utility that comes with Windows suffers
 * from several problems.  This Java application can be resized, for text and
 * the program window, which is important in many languages.  Features are
 * limited to make the application faster and simpler to use.  A single click
 * adds a character to the sample text, and the sample text is automatically
 * copied to the system clipboard on each click.
 *
 * You may choose the font to be displayed and the size of the characters or
 * glyphs.  (Glyphs are bits and pieces that a font combines to produce the
 * characters you see.  In most cases, one character maps to one glyph.)  You
 * may edit the sample text, erase it with the "Clear" button, or copy it to the
 * system clipboard with the "Copy All" button.  Paste the text into your word
 * processor in the normal manner, which is usually a Control-V key combination.
 * Editing the sample text and pressing the Enter key also copies to the
 * clipboard.  Specific characters can be copied from the sample text by
 * selection and with the usual Control-C combination.  More characters are
 * available via the scroll bar on the right.  A description is shown in the
 * "caption" field when characters have a particular name or meaning.  Common
 * readings or sounds are given for Chinese, Japanese, and Korean characters.
 * Cantonese is prefixed with "C", Japanese "Kun" with "J", Korean with "K",
 * Mandarin with "M", and Sino-Japanese "On" with "S".  An English translation
 * of CJK character definitions would have been more amusing but less practical.
 * 
 * Keyboard shortcuts are provided to mimic the scroll bar: the Control-Home key
 * combination goes to the very first character, Control-End goes to the last
 * character, Page Down and Page Up scroll one screen at a time, and the arrow
 * keys scroll one line at a time.  You need to combine the End and Home keys
 * with the Control (Ctrl) key when the sample text has keyboard focus.  The F1
 * key is the only helpful undocumented feature.
 *
 * GNU General Public License (GPL)
 * --------------------------------
 * CharMap4 is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License or (at your option) any later
 * version.  This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY, without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program.  If not, see the http://www.gnu.org/licenses/ web page.
 * 
 * Restrictions and Limitations
 * ----------------------------
 * Which fonts will work with this program depends upon the operating system and
 * version of the Java run-time environment.  Java 5.0 on Windows 2000/XP will
 * show installed TrueType fonts, that is, fonts that have been added with the
 * Control Panel, Fonts icon.  (Temporary fonts are not shown if opened with the
 * Windows Font Viewer by double clicking on a font file name.)  If you think
 * this program is not working correctly on your computer, then "Lucida Console"
 * is a good font for testing the spacing and positioning, because its glyphs
 * are tightly packed.  Version 4 of CharMap supports extended Unicode (up to
 * 1,114,112 characters) and is noticeably slower than version 3, which only
 * supports the standard range of 65,536 characters.  Version 4 also tends to
 * run out of memory for very large fonts; see the -Xmx option on the Java
 * command line.
 * 
 * This program contains character data from the Unicode Consortium; please
 * visit their web site at http://www.unicode.org/ for more information.  Korean
 * character names were converted from Korean standards document KS X 1001:2002
 * with the title "Hangeul Syllables in Unicode 4.0" and dated 25 March 2004.
 * 
 * This class listens to input from the user and passes back event parameters to a static method in the main class.
 */
class CharMap4User extends AbstractAction implements Runnable
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    private CharMap4 charMap4 = null;

    /**
     * Instantiates a new char map4 user.
     * @param charMap4 
     *
     * @param command the command
     */
    public CharMap4User(CharMap4 charMap4, String command)
    {
        super();
        this.putValue(Action.NAME, command); // save action name for later decoding

        this.charMap4 = charMap4;
    }

    /* button listener, dialog boxes, keyboard, etc */

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event)
    {
        String command = (String) this.getValue(Action.NAME); // get saved action
        if (command == null)          // was there a keyboard action name?
        {
            charMap4.userButton(event); // no, process as regular button or dialog
        }
        else                          // yes, there was a saved keyboard action
        {
            charMap4.userKey(command);  // process as a regular keyboard command
        }
    }

    /* separate heavy-duty processing thread */

    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        charMap4.loadConfig();        // load all possible mouse caption strings
    }
}
