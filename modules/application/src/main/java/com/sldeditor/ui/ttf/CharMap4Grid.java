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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Character Map #4 - Display Characters and Copy to System Clipboard Written by: Keith Fenske,
 * http://www.psc-consulting.ca/fenske/ Monday, 19 May 2008 Java class name: CharMap4 Copyright (c)
 * 2008 by Keith Fenske. Released under GNU Public License.
 *
 * <p>This is a Java 5.0 graphical (GUI) application to display Unicode characters or glyphs in text
 * fonts, and copy those characters to the system clipboard. Its major purpose is as a visual
 * accessory for word processors such as Microsoft Word. The "character map" utility that comes with
 * Windows suffers from several problems. This Java application can be resized, for text and the
 * program window, which is important in many languages. Features are limited to make the
 * application faster and simpler to use. A single click adds a character to the sample text, and
 * the sample text is automatically copied to the system clipboard on each click.
 *
 * <p>You may choose the font to be displayed and the size of the characters or glyphs. (Glyphs are
 * bits and pieces that a font combines to produce the characters you see. In most cases, one
 * character maps to one glyph.) You may edit the sample text, erase it with the "Clear" button, or
 * copy it to the system clipboard with the "Copy All" button. Paste the text into your word
 * processor in the normal manner, which is usually a Control-V key combination. Editing the sample
 * text and pressing the Enter key also copies to the clipboard. Specific characters can be copied
 * from the sample text by selection and with the usual Control-C combination. More characters are
 * available via the scroll bar on the right. A description is shown in the "caption" field when
 * characters have a particular name or meaning. Common readings or sounds are given for Chinese,
 * Japanese, and Korean characters. Cantonese is prefixed with "C", Japanese "Kun" with "J", Korean
 * with "K", Mandarin with "M", and Sino-Japanese "On" with "S". An English translation of CJK
 * character definitions would have been more amusing but less practical.
 *
 * <p>Keyboard shortcuts are provided to mimic the scroll bar: the Control-Home key combination goes
 * to the very first character, Control-End goes to the last character, Page Down and Page Up scroll
 * one screen at a time, and the arrow keys scroll one line at a time. You need to combine the End
 * and Home keys with the Control (Ctrl) key when the sample text has keyboard focus. The F1 key is
 * the only helpful undocumented feature.
 *
 * <p>GNU General Public License (GPL) -------------------------------- CharMap4 is free software:
 * you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License or (at your option)
 * any later version. This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY, without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * <p>You should have received a copy of the GNU General Public License along with this program. If
 * not, see the http://www.gnu.org/licenses/ web page.
 *
 * <p>Restrictions and Limitations ---------------------------- Which fonts will work with this
 * program depends upon the operating system and version of the Java run-time environment. Java 5.0
 * on Windows 2000/XP will show installed TrueType fonts, that is, fonts that have been added with
 * the Control Panel, Fonts icon. (Temporary fonts are not shown if opened with the Windows Font
 * Viewer by double clicking on a font file name.) If you think this program is not working
 * correctly on your computer, then "Lucida Console" is a good font for testing the spacing and
 * positioning, because its glyphs are tightly packed. Version 4 of CharMap supports extended
 * Unicode (up to 1,114,112 characters) and is noticeably slower than version 3, which only supports
 * the standard range of 65,536 characters. Version 4 also tends to run out of memory for very large
 * fonts; see the -Xmx option on the Java command line.
 *
 * <p>This program contains character data from the Unicode Consortium; please visit their web site
 * at http://www.unicode.org/ for more information. Korean character names were converted from
 * Korean standards document KS X 1001:2002 with the title "Hangeul Syllables in Unicode 4.0" and
 * dated 25 March 2004.
 */

/**
 * CharMap4Grid class
 *
 * <p>This class draws the character grid and listens for mouse and scroll events. Keyboard events
 * are handled by the main class and mimic the scroll bar.
 */
class CharMap4Grid extends JPanel
        implements ChangeListener, MouseListener, MouseMotionListener, MouseWheelListener {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant GRID_COLOUR. */
    private static final Color GRID_COLOUR = Color.LIGHT_GRAY; // colour of boxes, lines

    private static final Color SELECTED_COLOUR = Color.RED;

    /** The Constant GRID_WIDTH. */
    private static final int GRID_WIDTH = 2; // width of grid lines in pixels

    /** The Constant MOUSE_DRIFT. */
    private static final int MOUSE_DRIFT = 10; // pixel movement allowed on mouse click

    /** The Constant NO_MOUSE. */
    private static final int NO_MOUSE = -1; // index when mouse not on defined character

    /** The Constant PANEL_COLOUR. */
    private static final Color PANEL_COLOUR = Color.WHITE; // normal background colour

    /** The Constant PANEL_MARGIN. */
    private static final int PANEL_MARGIN = 5; // outside margin of panel in pixels

    /** The Constant TEXT_COLOUR. */
    private static final Color TEXT_COLOUR = Color.BLACK; // colour of all display text

    /** The Constant TEXT_MARGIN. */
    private static final int TEXT_MARGIN = 4; // margin inside cell for each character

    /* instance variables */

    /** The click index. */
    private int clickIndex; // cell index of clicked character

    /** The click start y. */
    private int clickStartX;

    private int clickStartY; // starting pixel coordinates of mouse click

    /** The corner index. */
    private int cornerIndex; // cell index of top-left corner

    /** The font data. */
    private FontMetrics fontData; // information about current display font

    /** The horiz step. */
    private int horizStep; // horizontal offset from one cell to next

    /** The hover index. */
    private int hoverIndex; // cell index of mouse over character

    /** The line ascent. */
    private int lineAscent; // number of pixels above baseline

    /** The line height. */
    private int lineHeight; // height of each display line in pixels

    /** The max width. */
    private int maxWidth; // maximum pixel width of all characters

    /** The panel columns. */
    private int panelColumns; // number of complete text columns displayed

    /** The panel count. */
    private int panelCount; // saved value of <cellCount> used previously

    /** The panel font. */
    private Font panelFont; // saved font for drawing text on this panel

    /** The panel width. */
    private int panelHeight;

    private int panelWidth; // saved panel height and width in pixels

    /** The panel rows. */
    private int panelRows; // number of complete lines (rows) displayed

    /** The verti step. */
    private int vertiStep; // vertical offset from one cell to next

    // To switch between displaying characters and displaying raw glyphs, we
    // create identical information structures for both, and switch only object
    // references. A "cell" is our generic name for either one or the other. */

    /** The cell char. */
    private int[] cellChar; // unique character number, or -1

    /** The cell count. */
    private int cellCount; // number of displayed characters or glyphs

    /** The cell glyph. */
    private int[] cellGlyph; // unique glyph number, or -1

    /** The char char. */
    private int[] charChar; // as above, but for Unicode characters

    /** The char count. */
    private int charCount;

    /** The char glyph. */
    private int[] charGlyph;

    /** The glyph char. */
    private int[] glyphChar; // as above, but for raw glyph numbers

    /** The glyph count. */
    private int glyphCount;

    /** The glyph glyph. */
    private int[] glyphGlyph;

    private CharMap4 charMap4 = null;

    private String selectedFont = null;

    private int selectedChar = -1;

    private boolean pendingUpdates = false;

    private int pendingCharacter = -1;

    /**
     * Instantiates a new char map4 grid.
     *
     * @param charMap4
     */
    public CharMap4Grid(CharMap4 charMap4) {
        super(); // initialize our superclass first (JPanel)

        this.charMap4 = charMap4;
        // Set class instance variables to undefined values that we will recognise
        // if we are called before the layout and first "paint" is complete. */

        cellCount = charCount = glyphCount = 0; // no chars or glyphs to display
        clickIndex = NO_MOUSE; // cell index of clicked character
        clickStartX = clickStartY = NO_MOUSE; // no starting coordinates for click
        cornerIndex = 0; // cell index of top-left corner
        fontData = null; // information about current display font
        horizStep = 100; // horizontal offset from one cell to next
        hoverIndex = NO_MOUSE; // cell index of mouse over character
        lineAscent = 100; // number of pixels above baseline
        lineHeight = 100; // height of each display line in pixels
        maxWidth = 100; // maximum pixel width of all characters
        panelColumns = 10; // number of complete text columns displayed
        panelCount = -1; // saved value of <cellCount> used previously
        panelFont = null; // saved font for drawing text on this panel
        panelHeight = panelWidth = -1; // saved panel height and width in pixels
        panelRows = 10; // number of complete lines (rows) displayed
        vertiStep = 100; // vertical offset from one cell to next

        /* Install our mouse and scroll listeners. */

        this.addMouseListener((MouseListener) this);
        this.addMouseMotionListener((MouseMotionListener) this);
        this.addMouseWheelListener((MouseWheelListener) this);
        // this.setFocusable(false); // we don't handle keyboard input, owner does

    } // end of CharMap4Grid() constructor

    /**
     * Clear. The caller wants us to initialize the display, from the beginning. Flag our class
     * variables so that this will happen when the panel is next redrawn.
     */
    void clear() {
        panelFont = null; // saved font for drawing text on this panel
        this.repaint(); // mark ourselves as needing to be repainted
    }

    /**
     * Convert mouse.
     *
     * <p>Convert mouse coordinates to a cell index. Return <NO_MOUSE> if the mouse is not
     * well-centered on a defined character.
     *
     * @param event the event
     * @return the int
     */
    int convertMouse(MouseEvent event) {
        int result; // converted cell index or <NO_MOUSE>

        if (panelFont == null) // can't do conversion if we haven't painted
        {
            return (NO_MOUSE); // tell caller to come back some other time
        }

        int colOff = event.getX() - GRID_WIDTH - PANEL_MARGIN; // known margins
        int colNum = colOff / horizStep; // convert pixels to column number
        int colRem = colOff % horizStep; // how far inside cell is the mouse?
        if ((colNum >= panelColumns)
                || (colRem < TEXT_MARGIN)
                || (colRem > (horizStep - GRID_WIDTH - TEXT_MARGIN))) {
            return (NO_MOUSE); // horizontal coordinate is out of range
        }

        int rowOff = event.getY() - GRID_WIDTH - PANEL_MARGIN; // known margins
        int rowNum = rowOff / vertiStep; // convert pixels to row number
        int rowRem = rowOff % vertiStep; // how far inside cell is the mouse?
        if ((rowRem < TEXT_MARGIN) || (rowRem > (vertiStep - GRID_WIDTH - TEXT_MARGIN))) {
            return (NO_MOUSE); // vertical coordinate is out of range
        }

        result = cornerIndex + (rowNum * panelColumns) + colNum;
        if (result >= cellCount) // is the mouse beyond the last character?
        {
            return (NO_MOUSE); // character index is out of range
        }

        return (result); // give caller a valid character index
    } // end of convertMouse() method

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    /*
     * mouseClicked(), mouseDragged(), ..., mouseReleased() methods
     *
     * These are the mouse click and movement listeners.
     */
    public void mouseClicked(MouseEvent event) {
        /* See mouseReleased(). */ }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent event) {
        mouseMoved(event); // treat click-and-drag as simple movement
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent event) {
        /* not used */ }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent event) {
        /* not used */
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent event) {
        /*
         * Called when the mouse changes position, often pixel by pixel. We only use this for a mouse caption, which doesn't actually follow the
         * mouse, as that would be annoying. The text appears in a fixed location at the top of the screen. Mouse movement can change a "click" into a
         * "drag". We don't automatically cancel our click highlighting upon movement, because some tolerance is more comfortable for users.
         */

        StringBuilder buffer; // faster than String for multiple appends
        int ch; // one character from string as an integer
        int index; // cell index for character or glyph
        boolean repaint; // true if we should repaint our display
        String text; // mouse caption for this cell, if any

        // Fetch the correct caption for the cell pointed to by the mouse.

        index = convertMouse(event); // convert pixel coordinates to index value
        repaint = false; // assume that we won't need to repaint
        text = null; // assume no changes to caption string
        if (index < 0) // is mouse is over defined character?
        {
            repaint |= (hoverIndex >= 0); // no, repaint if position has changed
            hoverIndex = NO_MOUSE; // this character is no longer highlighted
            text = CharMap4.getEmptyStatus(); // remove the caption string, if any
        } else if (hoverIndex != index) // has there been a change in position?
        {
            buffer = new StringBuilder(); // allocate empty string buffer for result
            ch = cellChar[index]; // character number or -1 if unmapped glyph
            hoverIndex = index; // turn on highlighting for this character
            repaint = true; // mark ourselves as needing to be repainted

            // When displaying glyphs, always show the glyph number. Then try to add
            // information for a corresponding character number.

            if (CharMap4.isGlyphFlag()) // are we displaying raw glyphs?
            {
                buffer.append("Glyph ");
                buffer.append(CharMap4.getFormatComma().format(cellGlyph[index]));
                buffer.append(" = ");
                if (ch < 0) {
                    buffer.append("No Unicode character mapping");
                }
            }

            // Java maps old 8-bit non-Unicode dingbat (symbol) fonts to the range
            // between 0xF020 to 0xF0FF. Since this is in the "private use" area of
            // Unicode, we don't have a meaningful caption anyway unless we assume a
            // remapping.

            if ((ch >= 0xF020) && (ch <= 0xF0FF) && (charCount <= 256)) {
                buffer.append(CharMap4.unicodeNotation(ch));
                buffer.append(" =? ");
                if (ch < 0xF07F) // remapping to standard keyboard?
                {
                    ch -= 0xF000; // continue by assuming this character
                } else if (CharMap4.isMswinFlag()) // are we running on Microsoft Windows?
                {
                    buffer.append(CharMap4.unicodeNotation(ch - 0xF000));
                    buffer.append(" = ");
                    buffer.append(CharMap4.winaltNotation(ch - 0xF000));
                    ch = -1; // that's the end of the caption
                } else // not keyboard map, not running Windows
                {
                    buffer.append(CharMap4.unicodeNotation(ch - 0xF000));
                    buffer.append(" = ");
                    buffer.append("decimal ");
                    buffer.append(CharMap4.getFormatComma().format((long) (ch - 0xF000)));
                    ch = -1; // that's the end of the caption
                }
            }

            // Get a standard caption string if we still have a character number.
            if (ch >= 0) // do we have a character number?
            {
                buffer.append(charMap4.captionGet(ch)); // get the standard caption
            }
            text = buffer.toString(); // convert string buffer to regular string
        }

        charMap4.setStatusText(text);

        // Avoid redrawing the screen unless the mouse has changed cells.
        if ((clickIndex >= 0) && (clickIndex != index)) // away from old click?
        {
            clickIndex = NO_MOUSE; // cancel forgotten highlight character
            repaint = true; // mark ourselves as needing to be repainted
        }

        if (repaint) this.repaint(); // repaint our display if something changed
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent event) {
        // Called when a mouse button is first pressed, and before it is released.
        // We highlight the cell that the mouse is pointing at.

        int index; // cell index for character or glyph
        boolean repaint; // true if we should repaint our display

        clickStartX = clickStartY = NO_MOUSE; // no starting coordinates for click
        index = convertMouse(event); // convert pixel coordinates to index value
        repaint = false; // assume that we won't need to repaint
        if (index >= 0) // only if mouse is over defined character
        {
            clickIndex = hoverIndex = index; // turn on highlighting this character
            clickStartX = event.getX(); // get starting X coordinate of mouse click
            clickStartY = event.getY(); // get starting Y coordinate of mouse click
            repaint = true; // mark ourselves as needing to be repainted
        } else if ((clickIndex >= 0) || (hoverIndex >= 0)) // previous highlights?
        {
            clickIndex = hoverIndex = NO_MOUSE; // cancel forgotten highlight char
            repaint = true; // mark ourselves as needing to be repainted
        }

        if (repaint) this.repaint(); // repaint our display if something changed
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent event) {
        // Called after a mouse button is released, and before mouseClicked(). If
        // the mouse moves too much, then Java doesn't call mouseClicked(), so we use
        // this method to implement our own rules for how much a mouse can move while
        // being clicked.

        int index; // cell index for character or glyph
        boolean repaint; // true if we should repaint our display

        index = convertMouse(event); // convert pixel coordinates to index value
        repaint = false; // assume that we won't need to repaint
        if ((index >= 0) // only if mouse is over defined character
                && (clickIndex == index) // and it's the same as when mouse pressed
                && (Math.abs(clickStartX - event.getX()) <= MOUSE_DRIFT)
                && (Math.abs(clickStartY - event.getY()) <= MOUSE_DRIFT)) {
            /* Mouse is over a defined character or glyph. */

            if ((event.getButton() != MouseEvent.BUTTON1) // right means not primary
                    || event.isAltDown()
                    || event.isControlDown()
                    || event.isShiftDown()) {
                // TODO
                // CharMap4.rightSaveCaption = CharMap4.statusDialog.getText(); // caption
                // CharMap4.rightSaveChar = cellChar[index]; // save character number
                // CharMap4.rightSaveGlyph = cellGlyph[index]; // save glyph number
                //
                // CharMap4.rightCopyCaption.setEnabled((CharMap4.rightSaveCaption != null)
                // && (CharMap4.rightSaveCaption.length() > 0));
                // CharMap4.rightCopyGlyph.setEnabled(CharMap4.rightSaveGlyph >= 0);
                // CharMap4.rightCopyNotation.setEnabled(CharMap4.rightSaveChar >= 0);
                // CharMap4.rightCopyNumber.setEnabled(CharMap4.rightSaveChar >= 0);
                // CharMap4.rightCopyText.setEnabled(CharMap4.rightSaveChar >= 0);
            } else {
                // A left click or primary button click copies the character as text,
                // if there is a unique character number.

                if (cellChar[index] >= 0) {
                    mouseReplaceText(CharMap4.unicodeNotation(cellChar[index]), index);
                }
            }
        }

        // Avoid redrawing the screen unless the mouse has changed cells.

        if (clickIndex >= 0) // mouse release always ends click highlight
        {
            clickIndex = NO_MOUSE; // this character is no longer highlighted
            repaint = true; // mark ourselves as needing to be repainted
        }
        clickStartX = clickStartY = NO_MOUSE; // no starting coordinates for click

        if (hoverIndex != index) // has there been a change in position?
        {
            hoverIndex = index; // turn on highlighting for this character
            repaint = true; // mark ourselves as needing to be repainted
        }

        if (repaint) this.repaint(); // repaint our display if something changed
    }

    /**
     * Mouse replace text.
     *
     * <p>After a mouse click, or the pop-up menu simulating a mouse click, call this method with a
     * string to be added to the sample text.
     *
     * @param text the text
     */
    private void mouseReplaceText(String text, int index) {
        Font font = charMap4.getDisplayFont();

        if (font != null) {
            selectedFont = font.getFontName();
            selectedChar = index;
            String displayText = String.format("ttf://%s#%s", font.getFontName(), text);

            charMap4.setSelectedCharacter(displayText);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
     */
    /**
     * mouseWheelMoved
     *
     * <p>This is the mouse wheel listener, for the scroll wheel on some mice. The "unit" scroll
     * uses the local system preferences for how many lines/rows per click of the mouse. The "unit"
     * scroll may be too big if there are only one or two lines in the display.
     *
     * <p>The mouse wheel listener has no interaction with the other mouse listeners above.
     */
    public void mouseWheelMoved(MouseWheelEvent event) {
        switch (event.getScrollType()) // different mice scroll differently
        {
            case (MouseWheelEvent.WHEEL_BLOCK_SCROLL):
                {
                    charMap4.getGridScroll()
                            .setValue(
                                    charMap4.getGridScroll().getValue()
                                            + (event.getWheelRotation()
                                                    * charMap4.getGridScroll()
                                                            .getBlockIncrement()));
                }
                break;

            case (MouseWheelEvent.WHEEL_UNIT_SCROLL):
                {
                    int i = charMap4.getGridScroll().getBlockIncrement(); // maximum scroll rows
                    i = Math.max((-i), Math.min(i, event.getUnitsToScroll())); // limits
                    charMap4.getGridScroll().setValue(charMap4.getGridScroll().getValue() + i);
                    // scroll using limited local preferences
                }
                break;

            default: // ignore anything that we don't recognize
                break;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    /**
     * paintComponent() method
     *
     * <p>This is the "paint" method for a Java Swing component. We have to worry about the window
     * size changing, new options chosen by the user, etc. There are many temporary variables in
     * this method, because some calculations are difficult and declaring all variables at the
     * beginning would be worse than declaring them when they are first used.
     */
    @Override
    protected void paintComponent(Graphics context) {
        Graphics2D gr2d; // special subclass of graphics context
        int i;
        int k; // index variables
        FontRenderContext render; // needed for displaying low-level glyphs

        // Most of this code would work with the standard Graphics object, but some
        // of the glyph routines need the newer Graphics2D subclass.

        gr2d = (Graphics2D) context; // another name for the same graphics context
        render = gr2d.getFontRenderContext(); // for displaying low-level glyphs

        /* Erase the entire panel using our choice of background colors. */

        gr2d.setColor(PANEL_COLOUR); // flood fill with background color
        gr2d.fillRect(0, 0, this.getWidth(), this.getHeight());

        // If the font has changed, then we need to redo both the height and the
        // width, and we must collect new information about the font.

        boolean redoHeight = false; // assume that panel height doesn't change
        boolean redoWidth = false; // assume that panel width doesn't change

        if (charMap4.getDisplayFont() == null) // is there a font to display characters?
        {
            return; // no, then can't do anything more
        } else if (charMap4.getDisplayFont().equals(panelFont) == false) // a new font?
        {
            clickIndex = hoverIndex = NO_MOUSE; // cancel forgotten highlight char
            cornerIndex = 0; // force top-left corner to first character
            panelFont = charMap4.getDisplayFont(); // save current character display font
            redoHeight = redoWidth = true; // force both directions to be redone

            // Get the font metrics. We want the "official" maximum height and
            // width. Note that even though a font provides this information, there may
            // still be characters that draw outside the declared bounds. For critical
            // applications, you would have to call FontMetrics.getMaxCharBounds(), and
            // that may be slow if the font is very large (not tested).

            fontData = gr2d.getFontMetrics(panelFont); // save a copy of metrics
            lineAscent = fontData.getAscent(); // number of pixels above baseline
            lineHeight = fontData.getHeight(); // height of each line in pixels
            maxWidth = Math.max(10, fontData.getMaxAdvance()); // maximum all chars
            horizStep = maxWidth + (2 * TEXT_MARGIN) + GRID_WIDTH; // between cells
            vertiStep = lineHeight + (2 * TEXT_MARGIN) + GRID_WIDTH; // between cells

            // The <charTemp> array is indexed by Unicode character number and has a
            // non-negative glyph number for each character, or -1 for no mapping. We
            // only need the first non-spacing glyph even if a character maps to more
            // than one glyph.

            charCount = 0; // start with no characters in the list
            int[] charTemp = new int[CharMap4.getMaxUnicode() + 1]; // use maximum size
            for (i = 0; i < charTemp.length; i++) {
                charTemp[i] = -1; // default to no mapping for all characters
            }

            // The <glyphChar> array is indexed by internal glyph number and has a
            // non-negative character number for each glyph, or -1 for no mapping. We
            // save only the first character even if a glyph is used by more than one
            // character. It would be nice to have a complete list of characters that
            // map to each glyph, but with upwards of 50,000 glyphs in some fonts, this
            // would take too many resources (too much memory for Vector objects and too
            // much processing time).

            glyphCount = Math.max(0, panelFont.getNumGlyphs()); // don't trust source
            int glymissing = panelFont.getMissingGlyphCode(); // undefined characters
            glyphChar = new int[glyphCount]; // we always know final size for these
            glyphGlyph = new int[glyphCount];
            for (i = 0; i < glyphCount; i++) {
                glyphChar[i] = -1; // default to no mapping for all glyphs
                glyphGlyph[i] = i; // assume that all glyphs map to themselves!
            }

            // Enumerate all possible Unicode characters.

            for (i = CharMap4.getMinUnicode(); i <= CharMap4.getMaxUnicode(); i++) {
                /* Ignore characters that Java knows it can't display. */

                // if (panelFont.canDisplay((char) i) == false) // Java 1.4
                if (panelFont.canDisplay(i) == false) // Java 5.0
                {
                    continue; // jump to next interaction of <for> loop
                }
                // Update mapping information between characters and glyphs. Early
                // Java 5.0 on the Apple Macintosh has a bug where canDisplay() returns
                // true for every possible Unicode character number. Ignore characters
                // that map to the "missing" glyph (usually number 0) or to a "spacing"
                // glyph (often the out-of-range glyph number of 65,535).

                String charToString = CharMap4.charToString(i);
                GlyphVector glyvector =
                        panelFont.createGlyphVector(
                                render, charToString); // get glyph list for this character
                int glycount = glyvector.getNumGlyphs(); // supposed number of glyphs
                for (k = 0; k < glycount; k++) // for each glyph in the glyph vector
                {
                    int glyph = glyvector.getGlyphCode(k); // get one glyph number
                    if ((glyph >= 0)
                            && (glyph < glyphCount)
                            && (glyph != glymissing)) { // ignore missing and spacing glyphs
                        if (charTemp[i] < 0) // does this character already have a glyph?
                        {
                            charTemp[i] = glyph; // no, save the first good glyph we find
                        }

                        if (glyphChar[glyph] < 0) // does this glyph already have a char?
                        {
                            glyphChar[glyph] = i; // no, save the first character we find
                        }
                    }
                }

                if (charTemp[i] >= 0) // count each character as displayable ...
                {
                    charCount++; // ... only if a non-spacing glyph found
                }
            }

            // Use <charTemp> to create compressed (smaller) arrays for only those
            // characters that can be displayed.

            charChar = new int[charCount]; // list of Unicode character numbers
            charGlyph = new int[charCount]; // list of internal glyph numbers
            k = 0; // place displayable entries starting here
            for (i = CharMap4.getMinUnicode(); i <= CharMap4.getMaxUnicode(); i++) {
                int glyph = charTemp[i]; // get glyph number, if any
                if (glyph >= 0) // if character mapped to at least one glyph
                {
                    charChar[k] = i; // save Unicode character number
                    charGlyph[k] = glyph; // save glyph number, if any
                    k++; // finish one more displayable character
                }
            }
            charTemp = null; // release memory used by this larger array

            // Protect ourselves from fonts that have no displayable characters.

            charMap4.setStatusText(
                    CharMap4.getFormatComma().format(charCount)
                            + " characters with "
                            + CharMap4.getFormatComma().format(glyphCount)
                            + " glyphs"); // subvert "mouse caption" for extra trivia

            if ((charCount <= 0) || (glyphCount <= 0)) // need at least one defined
            {
                panelFont = null; // crude, but prevents font from being used
                return; // give up, again and again, on each call
            }
        }

        // Set up our display cells using either character or glyph data.

        if (CharMap4.isGlyphFlag()) // are we displaying raw glyphs?
        {
            cellChar = glyphChar;
            cellCount = glyphCount;
            cellGlyph = glyphGlyph;
        } else // no, doing Unicode characters
        {
            cellChar = charChar;
            cellCount = charCount;
            cellGlyph = charGlyph;
        }

        // If the panel width has changed, then we need to recalculate how many
        // complete columns of text can be displayed inside this panel with the
        // specified margins. We don't want partial columns, because there is no
        // horizontal scroll bar, only a vertical scroll bar.

        if (redoWidth || (this.getWidth() != panelWidth)) {
            panelWidth = this.getWidth(); // save current panel width in pixels
            redoWidth = true; // remember that the width has changed

            panelColumns =
                    Math.max(1, ((panelWidth - (2 * PANEL_MARGIN) - GRID_WIDTH) / horizStep));
        }

        // If the panel height has changed, then we need to recalculate how many
        // complete lines (rows) can be displayed. The scroll bar handles any partial
        // later lines that aren't included in our row count.

        if (redoHeight || (this.getHeight() != panelHeight)) {
            panelHeight = this.getHeight(); // save current panel height in pixels
            redoHeight = true; // remember that the height has changed

            panelRows = Math.max(1, ((panelHeight - (2 * PANEL_MARGIN) - GRID_WIDTH) / vertiStep));
        }

        // When the window size changes, we need to recalculate several settings
        // for the vertical scroll bar. These are otherwise static except the current
        // position. As a programming note, please call setValues() when setting more
        // than one of the parameters, otherwise the change listener may fire between
        // calls to the individual methods for setting parameters. The maximum value
        // is one more than what you might expect, but that's what Java wants. */

        if ((cellCount != panelCount) || redoHeight || redoWidth) // changed size?
        {
            panelCount = cellCount; // save current number of displayed cells
            int row = cornerIndex / panelColumns;
            // convert character index to row number
            row =
                    Math.max(
                            0,
                            Math.min(
                                    row,
                                    ((cellCount / panelColumns)
                                            - panelRows
                                            + 1))); // if possible, don't leave blank rows at end
            charMap4.getGridScroll()
                    .setValues(
                            row, // scroll value
                            panelRows, // extent (visible amount)
                            0, // minimum: always zero
                            ((cellCount + panelColumns - 1) / panelColumns));
            // maximum: allow partial last row
            cornerIndex = charMap4.getGridScroll().getValue() * panelColumns;
            // convert scroll row back to character index

            charMap4.getGridScroll().setBlockIncrement(Math.max(1, (panelRows - 1)));
            // lines/rows per "scroll one page"
            charMap4.getGridScroll().setUnitIncrement(1); // rows per "scroll one line"
        }

        if (pendingUpdates) {
            pendingUpdates = false;

            for (int index = 0; index < cellChar.length; index++) {
                if (cellChar[index] == pendingCharacter) {
                    selectedChar = index;
                    break;
                }
            }
        }

        // Draw each character that is wholely or partially visible in the current
        // grid. The code below is very sloppy: it calls a subroutine for all index
        // values that *might* be defined, including a partial next line. */

        int end = cornerIndex + (panelColumns * (panelRows + 1)) - 1; // partials
        for (i = cornerIndex; i <= end; i++) // display all possible characters
        {
            paintGridCell(gr2d, render, i); // paint each and every possible cell
        }
    }

    /**
     * Paint grid cell.
     *
     * <p>The caller wants us to paint one cell in the character grid. The cell may or may not be
     * defined. The cell may be highlighted during mouse clicks.
     *
     * <p>Please note that many fonts draw outside of their declared bounding boxes! View a font
     * like "Lucida Console" before making any judgements about the accuracy of this method.
     *
     * @param gr2d the gr2d
     * @param render the render
     * @param index the index
     */
    void paintGridCell(Graphics2D gr2d, FontRenderContext render, int index) {
        if (index >= cellCount) // is there a defined character?
        {
            return; // no, do nothing and return to caller
        }

        /* Calculate top-left drawing corner of the border for this cell. */

        int x = (((index - cornerIndex) % panelColumns) * horizStep) + PANEL_MARGIN;
        int y = (((index - cornerIndex) / panelColumns) * vertiStep) + PANEL_MARGIN;

        // Despite appearances, we don't actually draw lines for the grid! We
        // erase a rectangle with the gridline colour, then later a smaller interior
        // rectangle with the background colour

        gr2d.setColor(GRID_COLOUR); // flood fill with gridline colour
        gr2d.fillRect(x, y, (horizStep + GRID_WIDTH), (vertiStep + GRID_WIDTH));

        // If we're not highlighting, then clear the interior of the cell to the
        // background colour.

        if (index == clickIndex) // is the mouse clicking on this character?
        {
            gr2d.setColor(TEXT_COLOUR);
            /*
             * (non-Javadoc)
             *
             * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
             */
            /*
             * (non-Javadoc)
             *
             * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
             */
            /*
             * (non-Javadoc)
             *
             * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
             */
        }
        // click reverses background-foreground
        else if (index == hoverIndex) // is the mouse over this character?
        {
            /* do nothing: keep gridline color */ } else {
            gr2d.setColor(PANEL_COLOUR); // mouse is elsewhere, normal background
        }

        // Highlight the selected character
        if ((index == selectedChar)
                && (selectedFont.compareTo(charMap4.getDisplayFont().getFontName()) == 0)) {
            gr2d.setColor(SELECTED_COLOUR);
        }

        gr2d.fillRect(
                (x + GRID_WIDTH),
                (y + GRID_WIDTH),
                (horizStep - GRID_WIDTH),
                (vertiStep - GRID_WIDTH));

        // Draw the defined character or raw glyph.

        gr2d.setColor((index == clickIndex) ? PANEL_COLOUR : TEXT_COLOUR);
        if (CharMap4.isGlyphFlag()) // are we displaying raw glyphs?
        {
            int[] list = new int[1]; // need a list for createGlyphVector()
            list[0] = cellGlyph[index]; // only value in list is the glyph index
            GlyphVector glyvector = panelFont.createGlyphVector(render, list);
            gr2d.drawGlyphVector(
                    glyvector,
                    (x
                            + GRID_WIDTH
                            + TEXT_MARGIN
                            + ((maxWidth - ((int) glyvector.getGlyphMetrics(0).getAdvanceX()))
                                    / 2)),
                    (y + GRID_WIDTH + TEXT_MARGIN + lineAscent));
        } else // no, displaying standard characters
        {
            int ch = cellChar[index]; // get the character we want to display
            gr2d.setFont(panelFont); // set the correct font
            gr2d.drawString(
                    CharMap4.charToString(ch),
                    (x + GRID_WIDTH + TEXT_MARGIN + ((maxWidth - fontData.charWidth(ch)) / 2)),
                    (y + GRID_WIDTH + TEXT_MARGIN + lineAscent));
        }
    } // end of paintGridCell() method

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.event.ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    /**
     * stateChanged()
     *
     * <p>Currently only used for the vertical scroll bar. This method gets called often, perhaps
     * too often. Try to invoke other methods only if something important has changed.
     */
    public void stateChanged(ChangeEvent event) {
        if (panelFont != null) // are we ready to handle this yet?
        {
            int scroll = charMap4.getGridScroll().getValue(); // scroll bar row position
            int newCorner = scroll * panelColumns; // convert rows to characters
            if (newCorner != cornerIndex) // has drawing position truly changed?
            {
                cornerIndex = newCorner; // yes, remember new starting position
                this.repaint(); // mark ourselves as needing to be repainted
            }
        }
    }

    /**
     * Sets the selected.
     *
     * @param fontName the font name
     * @param fontCharacter the font character
     */
    public void setSelected(String fontName, String fontCharacter) {
        selectedFont = fontName;

        pendingCharacter = -1;
        if (CharMap4.isUnicode(fontCharacter)) {
            pendingCharacter = CharMap4.decodeUnicodeNotation(fontCharacter);
        } else {
            pendingCharacter = Integer.valueOf(fontCharacter);
        }

        pendingCharacter += 0xF000;

        pendingUpdates = true;
        repaint();
    }
}
