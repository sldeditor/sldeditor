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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelListener;
import java.text.NumberFormat;
import java.util.TreeMap;

import javax.swing.ActionMap;
import javax.swing.Box;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeListener;

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
 */
public class CharMap4 extends JDialog
{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant UNICODE_PREFIX. */
    private static final String UNICODE_PREFIX = "U+";

    /** The Constant TTF_PREFIX. */
    private static final String TTF_PREFIX = "ttf://";

    /** The Constant ACTION_GOTO_END. */
    private static final String ACTION_GOTO_END = "GotoEnd";

    /** The Constant ACTION_GOTO_HOME. */
    private static final String ACTION_GOTO_HOME = "GotoHome";

    /** The Constant ACTION_LINE_DOWN. */
    private static final String ACTION_LINE_DOWN = "LineDown";

    /** The Constant ACTION_LINE_UP. */
    private static final String ACTION_LINE_UP = "LineUp";

    /** The Constant ACTION_PAGE_DOWN. */
    private static final String ACTION_PAGE_DOWN = "PageDown";

    /** The Constant ACTION_PAGE_UP. */
    private static final String ACTION_PAGE_UP = "PageUp";

    /** The Constant ACTION_REPORT_SHOW. */
    private static final String ACTION_REPORT_SHOW = "ReportShow";

    /** Default font name. */
    private static final String DEFAULT_FONT = "Verdana";

    /**  Default window height in pixels. */
    private static final int DEFAULT_HEIGHT = 500;

    /**  Default window left position ("x"). */
    private static final int DEFAULT_LEFT = 50; 

    /**  Default point size for display text. */
    private static final int DEFAULT_SIZE = 30;

    /**  Default window top position ("y"). */
    private static final int DEFAULT_TOP = 50; 

    /**  Default window width in pixels. */
    private static final int DEFAULT_WIDTH = 700;

    /**  Message when no status to display. */
    private static final String EMPTY_STATUS = " ";

    /**  Standard point sizes for display text. */
    private static final String[] FONT_SIZES = {"18", "24", "30", "36", "48", "60", "72",
    "96"};

    /**  Maximum point size for display text. */
    private static final int MAX_SIZE = 999;

    /**  Minimum point size for display text. */
    private static final int MIN_SIZE = 10;

    /** The Constant PROGRAM_TITLE. */
    private static final String PROGRAM_TITLE = "TTF Character Selector";

    /**  Unicode replacement character. */
    private static final char REPLACE_CHAR = '\uFFFD'; 

    /**  This font is always available. */
    private static final String SYSTEM_FONT = "Dialog"; 

    /** The ok button pressed. */
    private boolean okButtonPressed = false;

    // The following constants are our definitions for the supported Unicode
    // range.  We use names separate from the Java standard, so that we can more
    // easily compile 16-bit Unicode on Java 1.4 and extended Unicode on Java 5.0
    // or later.  Only a few lines in this entire program depend upon the Unicode
    // range, and all have "Java 1.4" or "Java 5.0" comment flags.  If you want a
    // character map to start from something other than binary zero (such as from
    // 0x20 or U+0020 for a space), then change MIN_UNICODE.  Both the minimum and
    // the maximum are assumed to be non-negative integers, with the maximum larger
    // than the minimum.

    /** The Constant MAX_UNICODE. */
    private static final int MAX_UNICODE = Character.MAX_CODE_POINT; // Java 5.0 0x10FFFF

    /** The Constant MIN_UNICODE. */
    private static final int MIN_UNICODE = Character.MIN_CODE_POINT; // Java 5.0 0x000000

    /**  Font for buttons, labels, status, etc. */
    private Font buttonFont = null;

    /** Mapping of char values to mouse captions. */
    private TreeMap<Integer, String> captionMap = null;

    /**  common font object for display text. */
    private static Font displayFont = null;

    /** The font name. */
    private String fontName = DEFAULT_FONT;

    /** The font size. */
    private int fontSize = DEFAULT_SIZE;

    /**  formats with commas (digit grouping). */
    private static NumberFormat formatComma;

    /**  true if showing glyphs, false for chars. */
    private static boolean glyphFlag = false;

    /**  displays a grid of characters for a font. */
    private CharMap4Grid gridPanel = null;

    /**  vertical scroll bar beside */
    private JScrollBar gridScroll;

    /** true if running on Microsoft Windows. */
    private static boolean mswinFlag = System.getProperty("os.name").startsWith("Windows");

    /** graphical option for fontName. */
    private JComboBox<String> nameDialog;

    /** Selected character */
    private JTextField selectedCharField;

    /** graphical option for fontSize. */
    private JComboBox<String> sizeDialog;

    /** for mouse captions to identify characters. */
    private static JLabel statusDialog;

    /** shared action listener. */
    private static ActionListener userActions;

    /** The window height. */
    private int windowHeight = DEFAULT_HEIGHT;

    /** The window left. */
    private int windowLeft = DEFAULT_LEFT;

    /** The window top. */
    private int windowTop = DEFAULT_TOP;

    /** The window width. */
    private int windowWidth = DEFAULT_WIDTH;

    /**
     * CharMap4 default constructor.
     */
    public CharMap4()
    {
        setTitle(PROGRAM_TITLE);
        setModal(true);

        // Initialise number formatting styles.
        formatComma = NumberFormat.getInstance(); // current locale
        formatComma.setGroupingUsed(true); // use commas or digit groups

        // Initialise shared graphical objects.

        setFontName(fontName);        // checks preferred name, sets <displayFont>
        userActions = new CharMap4User(this, null); // create our shared action listener

        // Create the graphical interface as a series of little panels inside
        // bigger panels.  The intermediate panel names are

        // and hence are only numbered (panel1, panel2, etc). */

        // Create a top row for the menu button, font name and size, and caption
        // text.  We put this panel inside a BorderLayout so that we can control the margins.

        JPanel panel1 = new JPanel(new BorderLayout(10, 0)); // contains top row

        JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        String [] availableFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        nameDialog = new JComboBox<String>(availableFonts);
        nameDialog.setEditable(true); // allow user to edit this dialog field
        if (buttonFont != null)
        {
            nameDialog.setFont(buttonFont);
        }
        nameDialog.setSelectedItem(fontName); // select default font name
        nameDialog.setToolTipText("Font name for display text.");
        nameDialog.addActionListener(userActions); // do last so don't fire early
        panel2.add(nameDialog);

        panel2.add(Box.createHorizontalStrut(10));

        sizeDialog = new JComboBox<String>(FONT_SIZES); // create list from standard sizes
        sizeDialog.setEditable(false); // temporarily disable editing during layout
        if (buttonFont != null) sizeDialog.setFont(buttonFont);
        sizeDialog.setPrototypeDisplayValue("9999"); // allow for up to four digits
        sizeDialog.setToolTipText("Point size for display text.");
        panel2.add(sizeDialog);

        panel1.add(panel2, BorderLayout.WEST); // put menu, font on left side

        statusDialog = new JLabel(getEmptyStatus(), JLabel.CENTER);
        if (buttonFont != null) statusDialog.setFont(buttonFont);
        panel1.add(statusDialog, BorderLayout.CENTER); // put status in center

        JPanel panel3 = new JPanel(new BorderLayout(0, 0));
        panel3.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
        panel3.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        panel3.add(panel1, BorderLayout.CENTER);
        panel3.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        panel3.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);

        // Create a panel to display the grid of characters.  To the right of that
        // is a vertical scroll bar that we control.

        gridPanel = new CharMap4Grid(this); // create display as special JPanel
        gridPanel.setFocusable(true); // allow keyboard focus for character grid

        gridScroll = new JScrollBar(JScrollBar.VERTICAL, 0, 1, 0, 1);
        getGridScroll().addMouseWheelListener((MouseWheelListener) gridPanel);
        getGridScroll().setEnabled(true);  // scroll bar always present, always enabled
        getGridScroll().setFocusable(true); // allow keyboard focus for scroll bar
        getGridScroll().getModel().addChangeListener((ChangeListener) gridPanel);

        // Create bottom row for clear button, sample text, and copy button.

        JPanel panel4 = new JPanel(new BorderLayout(10, 0));

        selectedCharField = new JTextField();
        selectedCharField.addActionListener(userActions);
        selectedCharField.setEditable(false);
        selectedCharField.setFont(new Font(SYSTEM_FONT, Font.PLAIN, 14)); // apply new font to sample text
        selectedCharField.setMargin(new Insets(1, 5, 2, 5)); // top, left, bottom, right
        panel4.add(selectedCharField, BorderLayout.CENTER); // put sample in center

        JPanel buttonPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) buttonPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.RIGHT);

        JButton btnOk = new JButton("Ok");
        btnOk.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okButtonPressed  = true;
                setVisible(false);
            }
        });
        buttonPanel.add(btnOk);

        JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okButtonPressed = false;
                setVisible(false);
            }
        });
        buttonPanel.add(btnCancel);

        JPanel panel5 = new JPanel(new BorderLayout(0, 0));
        panel5.add(Box.createVerticalStrut(5), BorderLayout.NORTH);
        panel5.add(Box.createHorizontalStrut(10), BorderLayout.WEST);
        panel5.add(panel4, BorderLayout.CENTER);
        panel5.add(Box.createHorizontalStrut(10), BorderLayout.EAST);
        panel5.add(buttonPanel, BorderLayout.SOUTH);

        // Create the main window frame for this application.

        JPanel panel6 = (JPanel) getContentPane(); // content meets frame
        panel6.setLayout(new BorderLayout(0, 0));
        panel6.add(panel3, BorderLayout.NORTH); // menu, font, size, caption
        panel6.add(gridPanel, BorderLayout.CENTER); // character or glyph cells
        panel6.add(getGridScroll(), BorderLayout.EAST); // scroll bar for grid display
        panel6.add(panel5, BorderLayout.SOUTH); // clear, sample, copy button

        //       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //       if (maximizeFlag) setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocation(windowLeft, windowTop); // initial top-left corner
        setSize(windowWidth, windowHeight); // initial window size
        validate();         // do the application window layout

        // The default width for editable combo boxes is much too wide for the font
        // point size.  A better width is obtained by making the dialog non-editable
        // and then fixing the dialog at that size, before turning editing back on. */

        sizeDialog.setMaximumSize(sizeDialog.getPreferredSize());
        sizeDialog.setEditable(true); // now allow user to edit this dialog field
        sizeDialog.setSelectedItem(String.valueOf(fontSize));
        // selected item is our default size
        sizeDialog.addActionListener(userActions); // do last so don't fire early

        // Hook into the keyboard to mimic the scroll bar.  It's better to do this
        // here, with mappings, than an old-style keyboard listener in CharMap4Grid,
        // because then we don't have problems about which component has the focus. */

        InputMap inmap = panel6.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_MASK), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0), ACTION_GOTO_END);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_END, InputEvent.CTRL_MASK), ACTION_GOTO_END);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), ACTION_REPORT_SHOW);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.ALT_MASK), ACTION_REPORT_SHOW);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), ACTION_GOTO_HOME);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, InputEvent.CTRL_MASK), ACTION_GOTO_HOME);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, 0), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_DOWN, InputEvent.CTRL_MASK), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, 0), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_LEFT, InputEvent.CTRL_MASK), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, 0), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_RIGHT, InputEvent.CTRL_MASK), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, 0), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_KP_UP, InputEvent.CTRL_MASK), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent.CTRL_MASK), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0), ACTION_PAGE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, InputEvent.CTRL_MASK), ACTION_PAGE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0), ACTION_PAGE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, InputEvent.CTRL_MASK), ACTION_PAGE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.ALT_MASK), ACTION_REPORT_SHOW);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent.CTRL_MASK), ACTION_LINE_DOWN);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), ACTION_LINE_UP);
        inmap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_MASK), ACTION_LINE_UP);

        ActionMap acmap = panel6.getActionMap();
        acmap.put(ACTION_GOTO_END, new CharMap4User(this, ACTION_GOTO_END));
        acmap.put(ACTION_GOTO_HOME, new CharMap4User(this, ACTION_GOTO_HOME));
        acmap.put(ACTION_LINE_DOWN, new CharMap4User(this, ACTION_LINE_DOWN));
        acmap.put(ACTION_LINE_UP, new CharMap4User(this, ACTION_LINE_UP));
        acmap.put(ACTION_PAGE_DOWN, new CharMap4User(this, ACTION_PAGE_DOWN));
        acmap.put(ACTION_PAGE_UP, new CharMap4User(this, ACTION_PAGE_UP));
        acmap.put(ACTION_REPORT_SHOW, new CharMap4User(this, ACTION_REPORT_SHOW));
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args)
    {
        CharMap4 charMap4 = new CharMap4();

        charMap4.loadConfig();
        //        Thread loader;                // low-priority thread for loading captions
        //
        //        // It can take several seconds to load the caption strings, because there
        //        // are so many Unicode character numbers.  Rather than delaying the user when
        //        // this application first starts, or when the mouse first moves over a cell in
        //        // the display grid, load the captions as a low-priority background task.
        //
        //        loader = new Thread((Runnable) userActions, "loadConfig");
        //        // re-use the same action listener as above
        //        loader.setPriority(Thread.MIN_PRIORITY); // low priority has less impact
        //        loader.start();               // run separate thread to load captions

        charMap4.setTTFString("ttf://Symbol#221");

        charMap4.showDialog();
    }

    /**
     * Copy text.
     * 
     * Copy all sample text to the system clipboard.  Remember the current caret
     * position (selection) and restore that afterwards.
     */
    public void copyText()
    {
        int end, start;               // text positions for caret and/or selection

        end = selectedCharField.getSelectionEnd(); // remember current position in text
        start = selectedCharField.getSelectionStart();
        selectedCharField.selectAll();     // select all text in the dialog box
        selectedCharField.copy();          // place that text onto the clipboard
        selectedCharField.select(start, end); // restore previous caret position
    }

    /**
     * Caption get.
     *
     * @param value the value
     * @return the string
     */
    public String captionGet(int value)
    {
        StringBuffer buffer;          // faster than String for multiple appends
        String caption;               // defined caption string or <null>

        // Convert the character number to Unicode "U+nnnn" notation.

        buffer = new StringBuffer();  // allocate empty string buffer for result
        buffer.append(unicodeNotation(value));
        buffer.append(" = ");

        // Append the defined caption string, or create a generic caption.

        if (captionMap == null)       // have the caption strings been loaded?
            caption = null;             // no, follow through with generic caption
        else                          // yes, attempt to fetch defined caption
            caption = (String) captionMap.get(new Integer(value)); // may be <null>

        if (caption == null)          // was there a defined caption string?
        {
            /* No caption string has been defined for this character.  Use the name
             * of the Unicode "block" or range instead, as obtained from the file:
             * 
             * http://www.unicode.org/Public/UNIDATA/Blocks.txt
             *
             * Block names differ slightly from the "First" and "Last" names found in
             * the UNIDATA/UnicodeData.txt file.  Whatever you may think of the block
             * names, they are the official punctuation and spelling.
             * 
             * The following piece of code (except for the first line) is mechanically
             * generated from UNIDATA/Blocks.txt by the unpublished CharMapParse1 Java
             * application.  Do not edit this code manually.
             * 
             * Last updated from the 2011-06-15 UNIDATA/Blocks.txt file (6.1.0).
             */

            if ((value >= 0x0000) && (value <= 0x007F)) // known Unicode range?
                caption = "Basic Latin";
            else if ((value >= 0x0080) && (value <= 0x00FF))
                caption = "Latin-1 Supplement";
            else if ((value >= 0x0100) && (value <= 0x017F))
                caption = "Latin Extended-A";
            else if ((value >= 0x0180) && (value <= 0x024F))
                caption = "Latin Extended-B";
            else if ((value >= 0x0250) && (value <= 0x02AF))
                caption = "IPA Extensions";
            else if ((value >= 0x02B0) && (value <= 0x02FF))
                caption = "Spacing Modifier Letters";
            else if ((value >= 0x0300) && (value <= 0x036F))
                caption = "Combining Diacritical Marks";
            else if ((value >= 0x0370) && (value <= 0x03FF))
                caption = "Greek and Coptic";
            else if ((value >= 0x0400) && (value <= 0x04FF))
                caption = "Cyrillic";
            else if ((value >= 0x0500) && (value <= 0x052F))
                caption = "Cyrillic Supplement";
            else if ((value >= 0x0530) && (value <= 0x058F))
                caption = "Armenian";
            else if ((value >= 0x0590) && (value <= 0x05FF))
                caption = "Hebrew";
            else if ((value >= 0x0600) && (value <= 0x06FF))
                caption = "Arabic";
            else if ((value >= 0x0700) && (value <= 0x074F))
                caption = "Syriac";
            else if ((value >= 0x0750) && (value <= 0x077F))
                caption = "Arabic Supplement";
            else if ((value >= 0x0780) && (value <= 0x07BF))
                caption = "Thaana";
            else if ((value >= 0x07C0) && (value <= 0x07FF))
                caption = "NKo";
            else if ((value >= 0x0800) && (value <= 0x083F))
                caption = "Samaritan";
            else if ((value >= 0x0840) && (value <= 0x085F))
                caption = "Mandaic";
            else if ((value >= 0x08A0) && (value <= 0x08FF))
                caption = "Arabic Extended-A";
            else if ((value >= 0x0900) && (value <= 0x097F))
                caption = "Devanagari";
            else if ((value >= 0x0980) && (value <= 0x09FF))
                caption = "Bengali";
            else if ((value >= 0x0A00) && (value <= 0x0A7F))
                caption = "Gurmukhi";
            else if ((value >= 0x0A80) && (value <= 0x0AFF))
                caption = "Gujarati";
            else if ((value >= 0x0B00) && (value <= 0x0B7F))
                caption = "Oriya";
            else if ((value >= 0x0B80) && (value <= 0x0BFF))
                caption = "Tamil";
            else if ((value >= 0x0C00) && (value <= 0x0C7F))
                caption = "Telugu";
            else if ((value >= 0x0C80) && (value <= 0x0CFF))
                caption = "Kannada";
            else if ((value >= 0x0D00) && (value <= 0x0D7F))
                caption = "Malayalam";
            else if ((value >= 0x0D80) && (value <= 0x0DFF))
                caption = "Sinhala";
            else if ((value >= 0x0E00) && (value <= 0x0E7F))
                caption = "Thai";
            else if ((value >= 0x0E80) && (value <= 0x0EFF))
                caption = "Lao";
            else if ((value >= 0x0F00) && (value <= 0x0FFF))
                caption = "Tibetan";
            else if ((value >= 0x1000) && (value <= 0x109F))
                caption = "Myanmar";
            else if ((value >= 0x10A0) && (value <= 0x10FF))
                caption = "Georgian";
            else if ((value >= 0x1100) && (value <= 0x11FF))
                caption = "Hangul Jamo";
            else if ((value >= 0x1200) && (value <= 0x137F))
                caption = "Ethiopic";
            else if ((value >= 0x1380) && (value <= 0x139F))
                caption = "Ethiopic Supplement";
            else if ((value >= 0x13A0) && (value <= 0x13FF))
                caption = "Cherokee";
            else if ((value >= 0x1400) && (value <= 0x167F))
                caption = "Unified Canadian Aboriginal Syllabics";
            else if ((value >= 0x1680) && (value <= 0x169F))
                caption = "Ogham";
            else if ((value >= 0x16A0) && (value <= 0x16FF))
                caption = "Runic";
            else if ((value >= 0x1700) && (value <= 0x171F))
                caption = "Tagalog";
            else if ((value >= 0x1720) && (value <= 0x173F))
                caption = "Hanunoo";
            else if ((value >= 0x1740) && (value <= 0x175F))
                caption = "Buhid";
            else if ((value >= 0x1760) && (value <= 0x177F))
                caption = "Tagbanwa";
            else if ((value >= 0x1780) && (value <= 0x17FF))
                caption = "Khmer";
            else if ((value >= 0x1800) && (value <= 0x18AF))
                caption = "Mongolian";
            else if ((value >= 0x18B0) && (value <= 0x18FF))
                caption = "Unified Canadian Aboriginal Syllabics Extended";
            else if ((value >= 0x1900) && (value <= 0x194F))
                caption = "Limbu";
            else if ((value >= 0x1950) && (value <= 0x197F))
                caption = "Tai Le";
            else if ((value >= 0x1980) && (value <= 0x19DF))
                caption = "New Tai Lue";
            else if ((value >= 0x19E0) && (value <= 0x19FF))
                caption = "Khmer Symbols";
            else if ((value >= 0x1A00) && (value <= 0x1A1F))
                caption = "Buginese";
            else if ((value >= 0x1A20) && (value <= 0x1AAF))
                caption = "Tai Tham";
            else if ((value >= 0x1B00) && (value <= 0x1B7F))
                caption = "Balinese";
            else if ((value >= 0x1B80) && (value <= 0x1BBF))
                caption = "Sundanese";
            else if ((value >= 0x1BC0) && (value <= 0x1BFF))
                caption = "Batak";
            else if ((value >= 0x1C00) && (value <= 0x1C4F))
                caption = "Lepcha";
            else if ((value >= 0x1C50) && (value <= 0x1C7F))
                caption = "Ol Chiki";
            else if ((value >= 0x1CC0) && (value <= 0x1CCF))
                caption = "Sundanese Supplement";
            else if ((value >= 0x1CD0) && (value <= 0x1CFF))
                caption = "Vedic Extensions";
            else if ((value >= 0x1D00) && (value <= 0x1D7F))
                caption = "Phonetic Extensions";
            else if ((value >= 0x1D80) && (value <= 0x1DBF))
                caption = "Phonetic Extensions Supplement";
            else if ((value >= 0x1DC0) && (value <= 0x1DFF))
                caption = "Combining Diacritical Marks Supplement";
            else if ((value >= 0x1E00) && (value <= 0x1EFF))
                caption = "Latin Extended Additional";
            else if ((value >= 0x1F00) && (value <= 0x1FFF))
                caption = "Greek Extended";
            else if ((value >= 0x2000) && (value <= 0x206F))
                caption = "General Punctuation";
            else if ((value >= 0x2070) && (value <= 0x209F))
                caption = "Superscripts and Subscripts";
            else if ((value >= 0x20A0) && (value <= 0x20CF))
                caption = "Currency Symbols";
            else if ((value >= 0x20D0) && (value <= 0x20FF))
                caption = "Combining Diacritical Marks for Symbols";
            else if ((value >= 0x2100) && (value <= 0x214F))
                caption = "Letterlike Symbols";
            else if ((value >= 0x2150) && (value <= 0x218F))
                caption = "Number Forms";
            else if ((value >= 0x2190) && (value <= 0x21FF))
                caption = "Arrows";
            else if ((value >= 0x2200) && (value <= 0x22FF))
                caption = "Mathematical Operators";
            else if ((value >= 0x2300) && (value <= 0x23FF))
                caption = "Miscellaneous Technical";
            else if ((value >= 0x2400) && (value <= 0x243F))
                caption = "Control Pictures";
            else if ((value >= 0x2440) && (value <= 0x245F))
                caption = "Optical Character Recognition";
            else if ((value >= 0x2460) && (value <= 0x24FF))
                caption = "Enclosed Alphanumerics";
            else if ((value >= 0x2500) && (value <= 0x257F))
                caption = "Box Drawing";
            else if ((value >= 0x2580) && (value <= 0x259F))
                caption = "Block Elements";
            else if ((value >= 0x25A0) && (value <= 0x25FF))
                caption = "Geometric Shapes";
            else if ((value >= 0x2600) && (value <= 0x26FF))
                caption = "Miscellaneous Symbols";
            else if ((value >= 0x2700) && (value <= 0x27BF))
                caption = "Dingbats";
            else if ((value >= 0x27C0) && (value <= 0x27EF))
                caption = "Miscellaneous Mathematical Symbols-A";
            else if ((value >= 0x27F0) && (value <= 0x27FF))
                caption = "Supplemental Arrows-A";
            else if ((value >= 0x2800) && (value <= 0x28FF))
                caption = "Braille Patterns";
            else if ((value >= 0x2900) && (value <= 0x297F))
                caption = "Supplemental Arrows-B";
            else if ((value >= 0x2980) && (value <= 0x29FF))
                caption = "Miscellaneous Mathematical Symbols-B";
            else if ((value >= 0x2A00) && (value <= 0x2AFF))
                caption = "Supplemental Mathematical Operators";
            else if ((value >= 0x2B00) && (value <= 0x2BFF))
                caption = "Miscellaneous Symbols and Arrows";
            else if ((value >= 0x2C00) && (value <= 0x2C5F))
                caption = "Glagolitic";
            else if ((value >= 0x2C60) && (value <= 0x2C7F))
                caption = "Latin Extended-C";
            else if ((value >= 0x2C80) && (value <= 0x2CFF))
                caption = "Coptic";
            else if ((value >= 0x2D00) && (value <= 0x2D2F))
                caption = "Georgian Supplement";
            else if ((value >= 0x2D30) && (value <= 0x2D7F))
                caption = "Tifinagh";
            else if ((value >= 0x2D80) && (value <= 0x2DDF))
                caption = "Ethiopic Extended";
            else if ((value >= 0x2DE0) && (value <= 0x2DFF))
                caption = "Cyrillic Extended-A";
            else if ((value >= 0x2E00) && (value <= 0x2E7F))
                caption = "Supplemental Punctuation";
            else if ((value >= 0x2E80) && (value <= 0x2EFF))
                caption = "CJK Radicals Supplement";
            else if ((value >= 0x2F00) && (value <= 0x2FDF))
                caption = "Kangxi Radicals";
            else if ((value >= 0x2FF0) && (value <= 0x2FFF))
                caption = "Ideographic Description Characters";
            else if ((value >= 0x3000) && (value <= 0x303F))
                caption = "CJK Symbols and Punctuation";
            else if ((value >= 0x3040) && (value <= 0x309F))
                caption = "Hiragana";
            else if ((value >= 0x30A0) && (value <= 0x30FF))
                caption = "Katakana";
            else if ((value >= 0x3100) && (value <= 0x312F))
                caption = "Bopomofo";
            else if ((value >= 0x3130) && (value <= 0x318F))
                caption = "Hangul Compatibility Jamo";
            else if ((value >= 0x3190) && (value <= 0x319F))
                caption = "Kanbun";
            else if ((value >= 0x31A0) && (value <= 0x31BF))
                caption = "Bopomofo Extended";
            else if ((value >= 0x31C0) && (value <= 0x31EF))
                caption = "CJK Strokes";
            else if ((value >= 0x31F0) && (value <= 0x31FF))
                caption = "Katakana Phonetic Extensions";
            else if ((value >= 0x3200) && (value <= 0x32FF))
                caption = "Enclosed CJK Letters and Months";
            else if ((value >= 0x3300) && (value <= 0x33FF))
                caption = "CJK Compatibility";
            else if ((value >= 0x3400) && (value <= 0x4DBF))
                caption = "CJK Unified Ideographs Extension A";
            else if ((value >= 0x4DC0) && (value <= 0x4DFF))
                caption = "Yijing Hexagram Symbols";
            else if ((value >= 0x4E00) && (value <= 0x9FFF))
                caption = "CJK Unified Ideographs";
            else if ((value >= 0xA000) && (value <= 0xA48F))
                caption = "Yi Syllables";
            else if ((value >= 0xA490) && (value <= 0xA4CF))
                caption = "Yi Radicals";
            else if ((value >= 0xA4D0) && (value <= 0xA4FF))
                caption = "Lisu";
            else if ((value >= 0xA500) && (value <= 0xA63F))
                caption = "Vai";
            else if ((value >= 0xA640) && (value <= 0xA69F))
                caption = "Cyrillic Extended-B";
            else if ((value >= 0xA6A0) && (value <= 0xA6FF))
                caption = "Bamum";
            else if ((value >= 0xA700) && (value <= 0xA71F))
                caption = "Modifier Tone Letters";
            else if ((value >= 0xA720) && (value <= 0xA7FF))
                caption = "Latin Extended-D";
            else if ((value >= 0xA800) && (value <= 0xA82F))
                caption = "Syloti Nagri";
            else if ((value >= 0xA830) && (value <= 0xA83F))
                caption = "Common Indic Number Forms";
            else if ((value >= 0xA840) && (value <= 0xA87F))
                caption = "Phags-pa";
            else if ((value >= 0xA880) && (value <= 0xA8DF))
                caption = "Saurashtra";
            else if ((value >= 0xA8E0) && (value <= 0xA8FF))
                caption = "Devanagari Extended";
            else if ((value >= 0xA900) && (value <= 0xA92F))
                caption = "Kayah Li";
            else if ((value >= 0xA930) && (value <= 0xA95F))
                caption = "Rejang";
            else if ((value >= 0xA960) && (value <= 0xA97F))
                caption = "Hangul Jamo Extended-A";
            else if ((value >= 0xA980) && (value <= 0xA9DF))
                caption = "Javanese";
            else if ((value >= 0xAA00) && (value <= 0xAA5F))
                caption = "Cham";
            else if ((value >= 0xAA60) && (value <= 0xAA7F))
                caption = "Myanmar Extended-A";
            else if ((value >= 0xAA80) && (value <= 0xAADF))
                caption = "Tai Viet";
            else if ((value >= 0xAAE0) && (value <= 0xAAFF))
                caption = "Meetei Mayek Extensions";
            else if ((value >= 0xAB00) && (value <= 0xAB2F))
                caption = "Ethiopic Extended-A";
            else if ((value >= 0xABC0) && (value <= 0xABFF))
                caption = "Meetei Mayek";
            else if ((value >= 0xAC00) && (value <= 0xD7AF))
                caption = "Hangul Syllables";
            else if ((value >= 0xD7B0) && (value <= 0xD7FF))
                caption = "Hangul Jamo Extended-B";
            else if ((value >= 0xD800) && (value <= 0xDB7F))
                caption = "High Surrogates";
            else if ((value >= 0xDB80) && (value <= 0xDBFF))
                caption = "High Private Use Surrogates";
            else if ((value >= 0xDC00) && (value <= 0xDFFF))
                caption = "Low Surrogates";
            else if ((value >= 0xE000) && (value <= 0xF8FF))
                caption = "Private Use Area";
            else if ((value >= 0xF900) && (value <= 0xFAFF))
                caption = "CJK Compatibility Ideographs";
            else if ((value >= 0xFB00) && (value <= 0xFB4F))
                caption = "Alphabetic Presentation Forms";
            else if ((value >= 0xFB50) && (value <= 0xFDFF))
                caption = "Arabic Presentation Forms-A";
            else if ((value >= 0xFE00) && (value <= 0xFE0F))
                caption = "Variation Selectors";
            else if ((value >= 0xFE10) && (value <= 0xFE1F))
                caption = "Vertical Forms";
            else if ((value >= 0xFE20) && (value <= 0xFE2F))
                caption = "Combining Half Marks";
            else if ((value >= 0xFE30) && (value <= 0xFE4F))
                caption = "CJK Compatibility Forms";
            else if ((value >= 0xFE50) && (value <= 0xFE6F))
                caption = "Small Form Variants";
            else if ((value >= 0xFE70) && (value <= 0xFEFF))
                caption = "Arabic Presentation Forms-B";
            else if ((value >= 0xFF00) && (value <= 0xFFEF))
                caption = "Halfwidth and Fullwidth Forms";
            else if ((value >= 0xFFF0) && (value <= 0xFFFF))
                caption = "Specials";
            else if ((value >= 0x10000) && (value <= 0x1007F))
                caption = "Linear B Syllabary";
            else if ((value >= 0x10080) && (value <= 0x100FF))
                caption = "Linear B Ideograms";
            else if ((value >= 0x10100) && (value <= 0x1013F))
                caption = "Aegean Numbers";
            else if ((value >= 0x10140) && (value <= 0x1018F))
                caption = "Ancient Greek Numbers";
            else if ((value >= 0x10190) && (value <= 0x101CF))
                caption = "Ancient Symbols";
            else if ((value >= 0x101D0) && (value <= 0x101FF))
                caption = "Phaistos Disc";
            else if ((value >= 0x10280) && (value <= 0x1029F))
                caption = "Lycian";
            else if ((value >= 0x102A0) && (value <= 0x102DF))
                caption = "Carian";
            else if ((value >= 0x10300) && (value <= 0x1032F))
                caption = "Old Italic";
            else if ((value >= 0x10330) && (value <= 0x1034F))
                caption = "Gothic";
            else if ((value >= 0x10380) && (value <= 0x1039F))
                caption = "Ugaritic";
            else if ((value >= 0x103A0) && (value <= 0x103DF))
                caption = "Old Persian";
            else if ((value >= 0x10400) && (value <= 0x1044F))
                caption = "Deseret";
            else if ((value >= 0x10450) && (value <= 0x1047F))
                caption = "Shavian";
            else if ((value >= 0x10480) && (value <= 0x104AF))
                caption = "Osmanya";
            else if ((value >= 0x10800) && (value <= 0x1083F))
                caption = "Cypriot Syllabary";
            else if ((value >= 0x10840) && (value <= 0x1085F))
                caption = "Imperial Aramaic";
            else if ((value >= 0x10900) && (value <= 0x1091F))
                caption = "Phoenician";
            else if ((value >= 0x10920) && (value <= 0x1093F))
                caption = "Lydian";
            else if ((value >= 0x10980) && (value <= 0x1099F))
                caption = "Meroitic Hieroglyphs";
            else if ((value >= 0x109A0) && (value <= 0x109FF))
                caption = "Meroitic Cursive";
            else if ((value >= 0x10A00) && (value <= 0x10A5F))
                caption = "Kharoshthi";
            else if ((value >= 0x10A60) && (value <= 0x10A7F))
                caption = "Old South Arabian";
            else if ((value >= 0x10B00) && (value <= 0x10B3F))
                caption = "Avestan";
            else if ((value >= 0x10B40) && (value <= 0x10B5F))
                caption = "Inscriptional Parthian";
            else if ((value >= 0x10B60) && (value <= 0x10B7F))
                caption = "Inscriptional Pahlavi";
            else if ((value >= 0x10C00) && (value <= 0x10C4F))
                caption = "Old Turkic";
            else if ((value >= 0x10E60) && (value <= 0x10E7F))
                caption = "Rumi Numeral Symbols";
            else if ((value >= 0x11000) && (value <= 0x1107F))
                caption = "Brahmi";
            else if ((value >= 0x11080) && (value <= 0x110CF))
                caption = "Kaithi";
            else if ((value >= 0x110D0) && (value <= 0x110FF))
                caption = "Sora Sompeng";
            else if ((value >= 0x11100) && (value <= 0x1114F))
                caption = "Chakma";
            else if ((value >= 0x11180) && (value <= 0x111DF))
                caption = "Sharada";
            else if ((value >= 0x11680) && (value <= 0x116CF))
                caption = "Takri";
            else if ((value >= 0x12000) && (value <= 0x123FF))
                caption = "Cuneiform";
            else if ((value >= 0x12400) && (value <= 0x1247F))
                caption = "Cuneiform Numbers and Punctuation";
            else if ((value >= 0x13000) && (value <= 0x1342F))
                caption = "Egyptian Hieroglyphs";
            else if ((value >= 0x16800) && (value <= 0x16A3F))
                caption = "Bamum Supplement";
            else if ((value >= 0x16F00) && (value <= 0x16F9F))
                caption = "Miao";
            else if ((value >= 0x1B000) && (value <= 0x1B0FF))
                caption = "Kana Supplement";
            else if ((value >= 0x1D000) && (value <= 0x1D0FF))
                caption = "Byzantine Musical Symbols";
            else if ((value >= 0x1D100) && (value <= 0x1D1FF))
                caption = "Musical Symbols";
            else if ((value >= 0x1D200) && (value <= 0x1D24F))
                caption = "Ancient Greek Musical Notation";
            else if ((value >= 0x1D300) && (value <= 0x1D35F))
                caption = "Tai Xuan Jing Symbols";
            else if ((value >= 0x1D360) && (value <= 0x1D37F))
                caption = "Counting Rod Numerals";
            else if ((value >= 0x1D400) && (value <= 0x1D7FF))
                caption = "Mathematical Alphanumeric Symbols";
            else if ((value >= 0x1EE00) && (value <= 0x1EEFF))
                caption = "Arabic Mathematical Alphabetic Symbols";
            else if ((value >= 0x1F000) && (value <= 0x1F02F))
                caption = "Mahjong Tiles";
            else if ((value >= 0x1F030) && (value <= 0x1F09F))
                caption = "Domino Tiles";
            else if ((value >= 0x1F0A0) && (value <= 0x1F0FF))
                caption = "Playing Cards";
            else if ((value >= 0x1F100) && (value <= 0x1F1FF))
                caption = "Enclosed Alphanumeric Supplement";
            else if ((value >= 0x1F200) && (value <= 0x1F2FF))
                caption = "Enclosed Ideographic Supplement";
            else if ((value >= 0x1F300) && (value <= 0x1F5FF))
                caption = "Miscellaneous Symbols And Pictographs";
            else if ((value >= 0x1F600) && (value <= 0x1F64F))
                caption = "Emoticons";
            else if ((value >= 0x1F680) && (value <= 0x1F6FF))
                caption = "Transport And Map Symbols";
            else if ((value >= 0x1F700) && (value <= 0x1F77F))
                caption = "Alchemical Symbols";
            else if ((value >= 0x20000) && (value <= 0x2A6DF))
                caption = "CJK Unified Ideographs Extension B";
            else if ((value >= 0x2A700) && (value <= 0x2B73F))
                caption = "CJK Unified Ideographs Extension C";
            else if ((value >= 0x2B740) && (value <= 0x2B81F))
                caption = "CJK Unified Ideographs Extension D";
            else if ((value >= 0x2F800) && (value <= 0x2FA1F))
                caption = "CJK Compatibility Ideographs Supplement";
            else if ((value >= 0xE0000) && (value <= 0xE007F))
                caption = "Tags";
            else if ((value >= 0xE0100) && (value <= 0xE01EF))
                caption = "Variation Selectors Supplement";
            else if ((value >= 0xF0000) && (value <= 0xFFFFF))
                caption = "Supplementary Private Use Area-A";
            else if ((value >= 0x100000) && (value <= 0x10FFFF))
                caption = "Supplementary Private Use Area-B";

            // Korean Hangul syllables are so regular that they can be re-created
            // from Unicode character numbers ... if so desired.  Break into initial,
            // medial (middle), and final phonetic pieces.  For the code below to be
            // effective, the U+AC00 to U+D7AF range above must be commented out. */

            //    else if ((value >= 0xAC00) && (value <= 0xD7A3)) // Korean Hangul range?
            //    {
            //      int first = value - 0xAC00; // set zero point for following calculation
            //      int third = first % 28;   // index of "final" phonetic piece
            //      first = first / 28;       // remove value of final piece
            //      int second = first % 21;  // index of "medial" phonetic piece
            //      first = first / 21;       // index of "initial" phonetic piece
            //
            //      caption = "Hangul Syllable " + HANGUL_NAME_INITIAL[first] + " "
            //        + HANGUL_NAME_MEDIAL[second] + " " + HANGUL_NAME_FINAL[third];
            //      caption = caption.trim(); // remove any unused third piece
            //      String sound = HANGUL_SOUND_INITIAL[first]
            //        + HANGUL_SOUND_MEDIAL[second] + HANGUL_SOUND_FINAL[third];
            //      caption += " (" + sound.charAt(0) + sound.substring(1).toLowerCase()
            //        + ")";                  // first "letter" may be from second piece
            //    }

            // Default to a numeric caption in decimal if nothing else found.

            else                        // no defined caption, unknown character
                caption = "decimal " + getFormatComma().format(value);
        }
        buffer.append(caption);       // append selected caption to result
        return(buffer.toString());    // give caller our converted string

    }


    /**
     * Caption put.
     * 
     * Save a mouse caption (string) corresponding to a character value.  Do not
     * include a character number in the caption; that is added by captionGet().
     *
     * @param value the value
     * @param text the text
     */
    private void captionPut(int value, String text)
    {
        captionMap.put(new Integer(value), text);
    }

    /**
     * Char to string.
     *
     * Convert an integer character number to a standard Java string (that is,
     * encode the character as UTF-16 text).  This isolates one of the code
     * differences between Java 1.4 and Java 5.0 inside a single common method.
     * 
     * @param value the value
     * @return the string
     */
    public static String charToString(int value)
    {
        return(String.valueOf(Character.toChars(value))); // Java 5.0
    }

    /**
     * loadConfig() method
     *
     * Load configuration data from a text file in the current working directory,
     * which is usually the same folder as the program's *.class files.  Should we
     * encounter an error, then print a message, but continue normal execution.
     * None of the file data is critical to the operation of this program.
     * 
     * Please see the following web sources for the most recent Unicode mapping
     * tables for regular characters:
     *     http://www.unicode.org/Public/UNIDATA/UCD.html
     *     http://www.unicode.org/Public/UNIDATA/UnicodeData.txt
     *     
     * The best source for information about Chinese-Japanese-Korean ideographs is:
     * 
     *     http://www.unicode.org/Public/UNIDATA/Unihan.html
     *     http://www.unicode.org/Public/UNIDATA/Unihan.txt
     *     
     * Names for the Korean Hangul syllables can be found in:
     * 
     *     http://www.unicode.org/Public/UNIDATA/HangulSyllableType.txt
     *     http://www.unicode.org/Public/UNIDATA/Jamo.txt
     *     http://www.iana.org/assignments/idn/kr-korean.html (best raw data file)
     *     http://www.unicode.org/Public/MAPPINGS/VENDORS/MICSFT/WINDOWS/CP949.TXT
     */
    public void loadConfig()
    {
        byte[] array;                 // an array for exactly one byte
        String caption;               // defined caption string or <null>
        char ch;                      // one character from input line
        int i;                        // index variable
        String text;                  // one input line from file, or otherwise

        captionMap = new TreeMap<Integer, String>();   // start without any mouse captions

        // Supply default names for the plain text ASCII characters only.

        captionPut(0x0020, "Space");
        captionPut(0x0021, "Exclamation Mark");
        captionPut(0x0022, "Quotation Mark");
        captionPut(0x0023, "Number Sign");
        captionPut(0x0024, "Dollar Sign");
        captionPut(0x0025, "Percent Sign");
        captionPut(0x0026, "Ampersand");
        captionPut(0x0027, "Apostrophe");
        captionPut(0x0028, "Left Parenthesis");
        captionPut(0x0029, "Right Parenthesis");
        captionPut(0x002A, "Asterisk");
        captionPut(0x002B, "Plus Sign");
        captionPut(0x002C, "Comma");
        captionPut(0x002D, "Hyphen-Minus");
        captionPut(0x002E, "Full Stop");
        captionPut(0x002F, "Solidus");
        captionPut(0x0030, "Digit Zero");
        captionPut(0x0031, "Digit One");
        captionPut(0x0032, "Digit Two");
        captionPut(0x0033, "Digit Three");
        captionPut(0x0034, "Digit Four");
        captionPut(0x0035, "Digit Five");
        captionPut(0x0036, "Digit Six");
        captionPut(0x0037, "Digit Seven");
        captionPut(0x0038, "Digit Eight");
        captionPut(0x0039, "Digit Nine");
        captionPut(0x003A, "Colon");
        captionPut(0x003B, "Semicolon");
        captionPut(0x003C, "Less-Than Sign");
        captionPut(0x003D, "Equals Sign");
        captionPut(0x003E, "Greater-Than Sign");
        captionPut(0x003F, "Question Mark");
        captionPut(0x0040, "Commercial At");
        captionPut(0x0041, "Latin Capital Letter A");
        captionPut(0x0042, "Latin Capital Letter B");
        captionPut(0x0043, "Latin Capital Letter C");
        captionPut(0x0044, "Latin Capital Letter D");
        captionPut(0x0045, "Latin Capital Letter E");
        captionPut(0x0046, "Latin Capital Letter F");
        captionPut(0x0047, "Latin Capital Letter G");
        captionPut(0x0048, "Latin Capital Letter H");
        captionPut(0x0049, "Latin Capital Letter I");
        captionPut(0x004A, "Latin Capital Letter J");
        captionPut(0x004B, "Latin Capital Letter K");
        captionPut(0x004C, "Latin Capital Letter L");
        captionPut(0x004D, "Latin Capital Letter M");
        captionPut(0x004E, "Latin Capital Letter N");
        captionPut(0x004F, "Latin Capital Letter O");
        captionPut(0x0050, "Latin Capital Letter P");
        captionPut(0x0051, "Latin Capital Letter Q");
        captionPut(0x0052, "Latin Capital Letter R");
        captionPut(0x0053, "Latin Capital Letter S");
        captionPut(0x0054, "Latin Capital Letter T");
        captionPut(0x0055, "Latin Capital Letter U");
        captionPut(0x0056, "Latin Capital Letter V");
        captionPut(0x0057, "Latin Capital Letter W");
        captionPut(0x0058, "Latin Capital Letter X");
        captionPut(0x0059, "Latin Capital Letter Y");
        captionPut(0x005A, "Latin Capital Letter Z");
        captionPut(0x005B, "Left Square Bracket");
        captionPut(0x005C, "Reverse Solidus");
        captionPut(0x005D, "Right Square Bracket");
        captionPut(0x005E, "Circumflex Accent");
        captionPut(0x005F, "Low Line");
        captionPut(0x0060, "Grave Accent");
        captionPut(0x0061, "Latin Small Letter A");
        captionPut(0x0062, "Latin Small Letter B");
        captionPut(0x0063, "Latin Small Letter C");
        captionPut(0x0064, "Latin Small Letter D");
        captionPut(0x0065, "Latin Small Letter E");
        captionPut(0x0066, "Latin Small Letter F");
        captionPut(0x0067, "Latin Small Letter G");
        captionPut(0x0068, "Latin Small Letter H");
        captionPut(0x0069, "Latin Small Letter I");
        captionPut(0x006A, "Latin Small Letter J");
        captionPut(0x006B, "Latin Small Letter K");
        captionPut(0x006C, "Latin Small Letter L");
        captionPut(0x006D, "Latin Small Letter M");
        captionPut(0x006E, "Latin Small Letter N");
        captionPut(0x006F, "Latin Small Letter O");
        captionPut(0x0070, "Latin Small Letter P");
        captionPut(0x0071, "Latin Small Letter Q");
        captionPut(0x0072, "Latin Small Letter R");
        captionPut(0x0073, "Latin Small Letter S");
        captionPut(0x0074, "Latin Small Letter T");
        captionPut(0x0075, "Latin Small Letter U");
        captionPut(0x0076, "Latin Small Letter V");
        captionPut(0x0077, "Latin Small Letter W");
        captionPut(0x0078, "Latin Small Letter X");
        captionPut(0x0079, "Latin Small Letter Y");
        captionPut(0x007A, "Latin Small Letter Z");
        captionPut(0x007B, "Left Curly Bracket");
        captionPut(0x007C, "Vertical Line");
        captionPut(0x007D, "Right Curly Bracket");
        captionPut(0x007E, "Tilde");

        /* Windows has a pre-defined way of entering non-keyboard characters up to
         * 0xFF or decimal 255: hold down the Alt key and press exactly four digits
         * from 0000 to 0255 on the numeric keypad with NumLock on.  This will use the
         * system's default character set encoding.  (Three digits are interpreted
         * with an older MS-DOS character set.)  The Alt+nnnn numbers are helpful, but
         * since they vary from locale to locale, they must be re-generated here, and
         * can not be fixed as part of the regular caption strings.  Dingbat fonts
         * that use the C1 control region (0x80 to 0x9F) may not receive correct Alt+
         * numbers for that region.  This is unavoidable because the C1 control region
         * has shift codes for many double-byte character sets. */

        if (isMswinFlag())                // only if running on Microsoft Windows
        {
            array = new byte[1];        // test one encoded byte at a time
            for (i = 0x20; i <= 0xFF; i ++) // do all non-control 8-bit bytes
            {
                array[0] = (byte) i;      // construct byte array for decoding
                text = new String(array); // convert byte to Unicode, or replace char
                if ((text.length() == 1) && ((ch = text.charAt(0)) != REPLACE_CHAR))
                {
                    caption = (String) captionMap.get(new Integer((int) ch));
                    // fetch actual value, not via <captionGet>
                    if (caption == null)    // but have we already created a caption?
                        caption = "";         // no, use empty string, nothing to append to
                    else                    // yes, there is a caption and we are adding
                        caption += " = ";     // insert delimiter between caption and Alt+
                    caption += winaltNotation(i); // append Windows Alt+nnnn key code
                    captionPut((int) ch, caption); // save new caption string
                }
            } // end of <for> loop
        }
    }

    /**
     * Put error.
     * Show an error message to the user, either printed or as a pop-up dialog.
     *
     * @param text the text
     */
    private void putError(String text)
    {
        if(isVisible())
        {
            JOptionPane.showMessageDialog(this, text); // pop-up dialog for GUI
        }
    }

    /**
     * Sets the display font.
     * 
     * This method is called after either the font name or the point size changes
     * for display text.
     */
    private void setDisplayFont()
    {
        displayFont = new Font(fontName, Font.PLAIN, fontSize);

        if(gridPanel != null)
        {
            validate();         // redo the application window layout
            gridPanel.clear();  // display characters from the beginning
        }
    }

    /**
     * Sets the font name.
     * 
     * The caller gives us a preferred font name for display text.  We use that font
     * if it's available.  Otherwise, we default to the local system font.
     *
     * @param text the new font name
     */
    private void setFontName(String text)
    {
        fontName = text;              // assume name is valid, remember this name
        if (fontName.equals((new Font(fontName, Font.PLAIN, fontSize)).getFamily()))
        {
            /* This is a valid font name.  No changes are required. */
        }
        else                          // can't find requested font
        {
            putError("Font name <" + fontName + "> not found; using <" + SYSTEM_FONT
                    + "> instead.");
            fontName = SYSTEM_FONT;     // replace with standard system font
            if(nameDialog != null)
            {
                nameDialog.setSelectedItem(fontName); // reset dialog
            }
        }
        setDisplayFont();             // redo the layout with the new font or size
    }

    /**
     * Sets the point size.
     * 
     * The caller gives us a preferred point size for display text, as a string.  We
     * use that size if it's available.  Otherwise, we default to our initial size.
     *
     * @param text the new point size
     */
    private void setPointSize(String text)
    {
        try                           // try to parse parameter as an integer
        {
            fontSize = Integer.parseInt(text); // return signed integer, or exception
        }
        catch (NumberFormatException nfe) // if not a number or bad syntax
        {
            fontSize = -1;              // mark result as invalid
        }
        if ((fontSize >= MIN_SIZE) && (fontSize <= MAX_SIZE))
        {
            /* This is a valid point size.  No changes are required. */
        }
        else                          // given point size was out of range
        {
            putError("Point size <" + text + "> must be from " + MIN_SIZE + " to "
                    + MAX_SIZE + "; using " + DEFAULT_SIZE + " instead.");
            fontSize = DEFAULT_SIZE;    // default point size for display text
            sizeDialog.setSelectedItem(String.valueOf(fontSize)); // reset dialog
        }
        setDisplayFont();             // redo the layout with the new font or size
    }

    /**
     * Unicode notation.
     * 
     * Given an integer, return the Unicode "U+nnnn" notation for that character number.
     *
     * @param value the value
     * @return the string
     */
    public static String unicodeNotation(int value)
    {
        String result;                // our converted result

        result = Integer.toHexString(value).toUpperCase(); // convert binary to hex
        if (result.length() < 4)      // must have at least four digits
        {
            result = "0000".substring(result.length()) + result;
        }
        result = UNICODE_PREFIX + result;       // insert the "U+" prefix

        return(result);               // give caller our converted string
    }

    /**
     * Decode unicode notation.
     *
     * @param unicodeString the unicode string
     * @return the int
     */
    public static int decodeUnicodeNotation(String unicodeString)
    {
        String string = unicodeString.substring(UNICODE_PREFIX.length());

        int result = (int) Long.parseLong(string, 16);

        return(result);
    }

    /**
     * Checks if is unicode.
     *
     * @param unicodeString the unicode string
     * @return true, if is unicode
     */
    public static boolean isUnicode(String unicodeString)
    {
        return unicodeString.startsWith(UNICODE_PREFIX);
    }

    /**
     * User button.
     * 
     * This method is called by our action listener actionPerformed() to process
     * buttons, in the context of the main CharMap4 class.
     *
     * @param event the event
     */
    public void userButton(ActionEvent event)
    {
        Object source = event.getSource(); // where the event came from
        if (source == nameDialog) // font name for display text
        {
            setFontName((String) nameDialog.getSelectedItem());
        }
        else if (source == sizeDialog) // point size for display text
        {
            setPointSize((String) sizeDialog.getSelectedItem());
        }

        /* Programming error: unknown GUI element invoked this action listener. */

        else                          // fault in program logic, not by user
        {
            System.err.println("Error in userButton(): unknown ActionEvent: "
                    + event);                 // should never happen, so write on console
        }
    }

    /**
     * User key.
     * 
     *  The caller gives us a command string for a keyboard action.  The only actions
     *  currently defined are to mimic the scroll bar or menu items.
     *
     * @param command the command
     */
    public void userKey(String command)
    {
        if (command.equals(ACTION_GOTO_END))
        {
            getGridScroll().setValue(getGridScroll().getMaximum());
        }
        else if (command.equals(ACTION_GOTO_HOME))
        {
            getGridScroll().setValue(getGridScroll().getMinimum());
        }
        else if (command.equals(ACTION_LINE_DOWN))
        {
            getGridScroll().setValue(getGridScroll().getValue() + 1);
        }
        else if (command.equals(ACTION_LINE_UP))
        {
            getGridScroll().setValue(getGridScroll().getValue() - 1);
        }
        else if (command.equals(ACTION_PAGE_DOWN))
        {
            getGridScroll().setValue(getGridScroll().getValue()
                    + getGridScroll().getBlockIncrement());
        }
        else if (command.equals(ACTION_PAGE_UP))
        {
            getGridScroll().setValue(getGridScroll().getValue()
                    - getGridScroll().getBlockIncrement());
        }
        else                          // fault in program logic, not by user
        {
            System.err.println("Error in userKey(): unknown command: "
                    + command);       // should never happen, so write on console
        }
    }

    /**
     * Winalt notation.
     * 
     *   Given an integer, return the Windows "Alt+nnnn" notation for that character
     *   number.  Valid range is only from 0032 to 0255 decimal.
     *
     * @param value the value
     * @return the string
     */
    static String winaltNotation(int value)
    {
        String result;                // our converted result

        result = Integer.toString(value); // convert binary to decimal
        if (result.length() < 4)      // must have at least four digits
        {
            result = "0000".substring(result.length()) + result;
        }
        result = "Alt+" + result;     // insert the "Alt+" prefix

        return(result);               // give caller our converted string
    }

    /**
     * Gets the empty status.
     *
     * @return the emptyStatus
     */
    public static String getEmptyStatus() {
        return EMPTY_STATUS;
    }

    /**
     * Gets the max unicode.
     *
     * @return the maxUnicode
     */
    public static int getMaxUnicode() {
        return MAX_UNICODE;
    }

    /**
     * Gets the min unicode.
     *
     * @return the minUnicode
     */
    public static int getMinUnicode() {
        return MIN_UNICODE;
    }

    /**
     * Gets the display font.
     *
     * @return the displayFont
     */
    public Font getDisplayFont() {
        return displayFont;
    }

    /**
     * Gets the format comma.
     *
     * @return the formatComma
     */
    public static NumberFormat getFormatComma() {
        return formatComma;
    }

    /**
     * Checks if is glyph flag.
     *
     * @return the glyphFlag
     */
    public static boolean isGlyphFlag() {
        return glyphFlag;
    }

    /**
     * Checks if is mswin flag.
     *
     * @return the mswinFlag
     */
    public static boolean isMswinFlag() {
        return mswinFlag;
    }

    /**
     * Gets the grid scroll.
     *
     * @return the gridScroll
     */
    public JScrollBar getGridScroll() {
        return gridScroll;
    }

    /**
     * Sets the selected character.
     *
     * @param displayText the new selected character
     */
    public void setSelectedCharacter(String displayText)
    {
        selectedCharField.setText(displayText);
    }

    /**
     * Sets the status text.
     *
     * @param text the new status text
     */
    public void setStatusText(String text)
    {
        // Don't waste time setting the dialog box if nothing has changed.

        if ((text != null)            // if we constructed a new caption string
                && (statusDialog.getText().equals(text) == false))
        {
            statusDialog.setText(text); // copy new string to mouse caption
        }
    }


    /**
     * Sets the selected character.
     *
     * @param ttfFontString the new TTF string
     */
    public void setTTFString(String ttfFontString)
    {
        String string = ttfFontString;
        if(ttfFontString.startsWith(TTF_PREFIX))
        {
            string = ttfFontString.substring(TTF_PREFIX.length());

            String[] components = string.split("#");

            if(components.length == 2)
            {
                nameDialog.setSelectedItem(components[0]);

                gridPanel.setSelected(components[0], components[1]);
            }
        }
        selectedCharField.setText(ttfFontString);
    }

    /**
     * Show dialog.
     *
     * @return the string
     */
    public String showDialog()
    {
        setVisible(true);

        if(okButtonPressed)
        {
            return selectedCharField.getText();
        }
        return null;
    }
}
