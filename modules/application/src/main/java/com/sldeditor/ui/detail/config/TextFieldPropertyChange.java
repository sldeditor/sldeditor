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

package com.sldeditor.ui.detail.config;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * The Class TextFieldPropertyChange, informs parent panel via propertyChange mechanism as soon as
 * text changes.
 *
 * @author Robert Ward (SCISYS)
 */
public class TextFieldPropertyChange extends JTextField {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The Constant TEXT_PROPERTY, property changed. */
    public static final String TEXT_PROPERTY = "text";

    /**
     * Instantiates a new text field.
     */
    public TextFieldPropertyChange() {
        this(0);
    }

    /**
     * Instantiates a new text field.
     *
     * @param nbColumns the nb columns
     */
    public TextFieldPropertyChange(int nbColumns) {
        super("", nbColumns);
        this.setDocument(new UpdatedDocument());
    }

    /**
     * The Class UpdatedDocument.
     */
    private class UpdatedDocument extends PlainDocument {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** The ignore events flag. */
        private boolean ignoreEvents = false;

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.text.AbstractDocument#replace(int, int, java.lang.String,
         * javax.swing.text.AttributeSet)
         */
        @Override
        public void replace(int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            String oldValue = TextFieldPropertyChange.this.getText();
            this.ignoreEvents = true;
            super.replace(offset, length, text, attrs);
            this.ignoreEvents = false;
            String newValue = TextFieldPropertyChange.this.getText();
            if (!oldValue.equals(newValue))
                TextFieldPropertyChange.this.firePropertyChange(TEXT_PROPERTY, oldValue, newValue);
        }

        /*
         * (non-Javadoc)
         * 
         * @see javax.swing.text.AbstractDocument#remove(int, int)
         */
        @Override
        public void remove(int offs, int len) throws BadLocationException {
            String oldValue = TextFieldPropertyChange.this.getText();
            super.remove(offs, len);
            String newValue = TextFieldPropertyChange.this.getText();
            if (!ignoreEvents && !oldValue.equals(newValue))
                TextFieldPropertyChange.this.firePropertyChange(TEXT_PROPERTY, oldValue, newValue);
        }
    }
}
