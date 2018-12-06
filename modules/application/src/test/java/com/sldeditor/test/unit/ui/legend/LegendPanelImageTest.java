/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2018, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.legend;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.test.unit.datasource.impl.DummyExternalSLDFile;
import com.sldeditor.ui.legend.LegendPanelImage;
import com.sldeditor.ui.legend.filechooser.ImageFileExtensionUtils;
import com.sldeditor.ui.legend.filechooser.ImageFilter;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileFilter;
import org.geotools.styling.StyledLayer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The unit test for LegendPanelImage.
 *
 * <p>{@link com.sldeditor.ui.legend.LegendPanelImage}
 *
 * @author Robert Ward (SCISYS)
 */
class LegendPanelImageTest {

    class TestLegendPanelImage extends LegendPanelImage {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.legend.LegendPanelImage#writeLegendImage(javax.swing.filechooser.
         * FileFilter, java.io.File, java.awt.Image)
         */
        @Override
        protected File writeLegendImage(FileFilter fileFilter, File selectedFile, Image image)
                throws IOException {
            return super.writeLegendImage(fileFilter, selectedFile, image);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.legend.LegendPanelImage#copyToClipboard()
         */
        @Override
        protected void copyToClipboard() {
            super.copyToClipboard();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.legend.LegendPanelImage#showFilenamePressed()
         */
        @Override
        protected void showFilenamePressed() {
            super.showFilenamePressed();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.legend.LegendPanelImage#showStyleNamePressed()
         */
        @Override
        protected void showStyleNamePressed() {
            super.showStyleNamePressed();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.legend.LegendPanelImage#setStyleNameDisplayed(boolean)
         */
        @Override
        protected void setStyleNameDisplayed(boolean selected) {
            super.setStyleNameDisplayed(selected);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.ui.legend.LegendPanelImage#setFilenameDisplayed(boolean)
         */
        @Override
        protected void setFilenameDisplayed(boolean selected) {
            super.setFilenameDisplayed(selected);
        }

        /**
         * Gets the image icon.
         *
         * @return the image icon
         */
        public ImageIcon getImageIcon() {
            return super.getImageIcon();
        }
    }

    @AfterEach
    void clearUp() {
        SelectedSymbol.destroyInstance();
    }

    /** Test method for {@link com.sldeditor.ui.legend.LegendPanelImage#renderSymbol()}. */
    @Test
    void testRenderSymbol() {
        TestLegendPanelImage testObj = new TestLegendPanelImage();

        DummyExternalSLDFile external = new DummyExternalSLDFile();

        SelectedSymbol.getInstance().setSld(external.getSLD());

        testObj.renderSymbol();

        testObj.setFilenameDisplayed(true);
        testObj.showFilenamePressed();
        testObj.setFilenameDisplayed(false);
        testObj.showFilenamePressed();

        testObj.setStyleNameDisplayed(true);
        testObj.showStyleNamePressed();
        testObj.setStyleNameDisplayed(false);
        testObj.showStyleNamePressed();

        // Empty style layer name
        StyledLayer styledLayer = external.getSLD().layers().get(0);
        styledLayer.setName(null);
        testObj.setStyleNameDisplayed(true);
        testObj.showStyleNamePressed();

        File selectedFile = null;
        try {
            selectedFile = File.createTempFile("test", ".sld");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        // Write legend image with no file filter
        Image image = testObj.getImageIcon().getImage();
        File f = null;
        try {
            f = testObj.writeLegendImage(null, selectedFile, image);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertTrue(f.exists());
        f.delete();
        selectedFile.delete();

        // Write legend image with file filter and no file suffix
        try {
            selectedFile = File.createTempFile("test", "test");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        image = testObj.getImageIcon().getImage();
        try {
            f =
                    testObj.writeLegendImage(
                            new ImageFilter(ImageFileExtensionUtils.PNG), selectedFile, image);
        } catch (IOException e) {
            fail(e.getMessage());
        }

        assertTrue(f.exists());
        f.delete();

        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection("cleared clipboard");
        c.setContents(stringSelection, null);

        testObj.copyToClipboard();

        c = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertTrue(c.isDataFlavorAvailable(DataFlavor.imageFlavor));

        selectedFile.delete();
    }

    /** Test method for {@link com.sldeditor.ui.legend.LegendPanelImage#renderSymbol()}. */
    @Test
    void testRenderSymbolEmpty() {
        TestLegendPanelImage testObj = new TestLegendPanelImage();

        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection("empty clipboard");
        c.setContents(stringSelection, null);

        testObj.copyToClipboard();

        c = Toolkit.getDefaultToolkit().getSystemClipboard();
        assertFalse(c.isDataFlavorAvailable(DataFlavor.imageFlavor));
    }
}
