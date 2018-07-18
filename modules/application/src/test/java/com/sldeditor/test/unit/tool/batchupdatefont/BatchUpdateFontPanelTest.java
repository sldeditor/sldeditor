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

package com.sldeditor.test.unit.tool.batchupdatefont;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.test.unit.datasource.impl.DummyInternalSLDFile3;
import com.sldeditor.tool.batchupdatefont.BatchUpdateFontPanel;
import java.awt.GraphicsEnvironment;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.Font;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.TextSymbolizer;
import org.junit.jupiter.api.Test;
import org.opengis.filter.FilterFactory;

/**
 * The unit test for BatchUpdateFontData.
 *
 * <p>{@link com.sldeditor.tool.batchupdatefont.BatchUpdateFontPanel}
 *
 * @author Robert Ward (SCISYS)
 */
class BatchUpdateFontPanelTest {

    class TestBatchUpdateFontPanel extends BatchUpdateFontPanel {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test batch update font panel.
         *
         * @param application the application
         */
        public TestBatchUpdateFontPanel(SLDEditorInterface application) {
            super(application);
        }

        public void testApplyData() {
            applyData();
        }

        /** Save data. */
        public void testSaveData() {
            saveData();
        }

        /**
         * Sets the selected option to update font, used for testing.
         *
         * @param font the new update font
         */
        public void setUpdateFont(Font font) {
            super.setUpdateFont(font);
        }

        /**
         * Sets the selected option to update font size, used for testing.
         *
         * @param font the new update font size
         */
        public void setUpdateFontSize(Font font) {
            super.setUpdateFontSize(font);
        }
    }

    class TestSLDEditorInterface implements SLDEditorInterface {

        public SLDDataInterface savedSldData = null;

        public Class<?> savedParent = null;

        public Class<?> savedPanelClass = null;

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#getAppPanel()
         */
        @Override
        public JPanel getAppPanel() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#updateWindowTitle(boolean)
         */
        @Override
        public void updateWindowTitle(boolean dataEditedFlag) {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#chooseNewSLD()
         */
        @Override
        public void chooseNewSLD() {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#exitApplication()
         */
        @Override
        public void exitApplication() {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#saveFile(java.net.URL)
         */
        @Override
        public void saveFile(URL url) {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#saveSLDData(com.sldeditor.common.
         * SLDDataInterface)
         */
        @Override
        public void saveSLDData(SLDDataInterface sldData) {
            savedSldData = sldData;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#getLoadSLDInterface()
         */
        @Override
        public LoadSLDInterface getLoadSLDInterface() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#getApplicationFrame()
         */
        @Override
        public JFrame getApplicationFrame() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#openFile(java.net.URL)
         */
        @Override
        public void openFile(URL selectedURL) {}

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#getAppName()
         */
        @Override
        public String getAppName() {
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.common.SLDEditorInterface#refreshPanel(java.lang.Class,
         * java.lang.Class)
         */
        @Override
        public void refreshPanel(Class<?> parent, Class<?> panelClass) {
            savedParent = parent;
            savedPanelClass = panelClass;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.batchupdatefont.BatchUpdateFontPanel#populate(java.util.List)}.
     */
    @Test
    void testPopulateUpdateFontSize() {
        TestSLDEditorInterface app = new TestSLDEditorInterface();

        TestBatchUpdateFontPanel testObj = new TestBatchUpdateFontPanel(app);

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyInternalSLDFile3 dummy = new DummyInternalSLDFile3();
        sldDataList.add(dummy.getSLDData());

        Double originalFontSize = getFontSize(dummy.getSLDData());

        testObj.populate(sldDataList);

        testObj.testSaveData();
        assertNull(app.savedParent);

        Double expectedFontSizeIncrease = 24.0;

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        String originalFontname = "Serif";
        String originalFontStyle = "normal";
        String originalFontWeight = "normal";
        Font expectedFont =
                styleFactory.createFont(
                        ff.literal(originalFontname),
                        ff.literal(originalFontStyle),
                        ff.literal(originalFontWeight),
                        ff.literal(expectedFontSizeIncrease));

        testObj.setUpdateFontSize(expectedFont);

        testObj.testApplyData();
        testObj.testSaveData();

        Double actualFontSize = getFontSize(app.savedSldData);

        Double expectedFontSize = originalFontSize + expectedFontSizeIncrease;
        assertEquals(actualFontSize.doubleValue(), expectedFontSize.doubleValue());
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.batchupdatefont.BatchUpdateFontPanel#populate(java.util.List)}.
     */
    @Test
    void testPopulateUpdateFontData() {
        TestSLDEditorInterface app = new TestSLDEditorInterface();

        TestBatchUpdateFontPanel testObj = new TestBatchUpdateFontPanel(app);

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        DummyInternalSLDFile3 dummy = new DummyInternalSLDFile3();
        sldDataList.add(dummy.getSLDData());

        testObj.populate(sldDataList);

        testObj.testSaveData();
        assertNull(app.savedParent);

        Double expectedFontSizeIncrease = 24.0;

        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        String[] families =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        String originalFontname = families[0];
        String originalFontStyle = "normal";
        String originalFontWeight = "normal";
        Font expectedFont =
                styleFactory.createFont(
                        ff.literal(originalFontname),
                        ff.literal(originalFontStyle),
                        ff.literal(originalFontWeight),
                        ff.literal(expectedFontSizeIncrease));

        testObj.setUpdateFont(expectedFont);

        testObj.testApplyData();
        testObj.testSaveData();

        assertEquals(originalFontname, getFontName(app.savedSldData));
    }

    private Double getFontSize(SLDDataInterface sldData) {
        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);
        NamedLayer namedLayer = (NamedLayer) sld.layers().get(0);
        TextSymbolizer text =
                (TextSymbolizer)
                        namedLayer
                                .styles()
                                .get(0)
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(2);
        return Double.valueOf(text.getFont().getSize().toString());
    }

    private String getFontName(SLDDataInterface sldData) {
        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(sldData);
        NamedLayer namedLayer = (NamedLayer) sld.layers().get(0);
        TextSymbolizer text =
                (TextSymbolizer)
                        namedLayer
                                .styles()
                                .get(0)
                                .featureTypeStyles()
                                .get(0)
                                .rules()
                                .get(0)
                                .symbolizers()
                                .get(2);
        return text.getFont().getFamily().get(0).toString();
    }
}
