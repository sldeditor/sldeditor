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

package com.sldeditor.test.unit.tool.scale;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sldeditor.common.LoadSLDInterface;
import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.SLDEditorInterface;
import com.sldeditor.common.data.SLDUtils;
import com.sldeditor.common.utils.ScaleUtil;
import com.sldeditor.datasource.SLDEditorFile;
import com.sldeditor.tool.scale.ScaleSLDModel;
import com.sldeditor.tool.scale.ScaleToolPanel;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayerImpl;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayer;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The unit test for ScalePanelUtils.
 *
 * <p>{@link com.sldeditor.tool.scale.ScaleToolPanel}
 *
 * @author Robert Ward (SCISYS)
 */
class ScaleToolPanelTest {

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

    class TestScaleToolPanel extends ScaleToolPanel {
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /** @param application */
        public TestScaleToolPanel(SLDEditorInterface application) {
            super(application);
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.scale.ScaleToolPanel#saveData()
         */
        @Override
        protected void saveData() {
            super.saveData();
        }

        /*
         * (non-Javadoc)
         *
         * @see com.sldeditor.tool.scale.ScaleToolPanel#revertData()
         */
        @Override
        protected void revertData() {
            super.revertData();
        }

        /**
         * Sets the min scale.
         *
         * @param row the row
         * @param scale the scale
         */
        public void setMinScale(int row, double scale) {
            dataModel.setValueAt(String.valueOf(scale), row, ScaleSLDModel.COL_MIN_SCALE);
        }

        /**
         * Sets the max scale.
         *
         * @param row the row
         * @param scale the scale
         */
        public void setMaxScale(int row, double scale) {
            dataModel.setValueAt(String.valueOf(scale), row, ScaleSLDModel.COL_MAX_SCALE);
        }

        /**
         * Gets the min scale.
         *
         * @param row the row
         * @return the min scale
         */
        public Object getMinScale(int row) {
            return dataModel.getValueAt(row, ScaleSLDModel.COL_MIN_SCALE);
        }

        /**
         * Gets the max scale.
         *
         * @param row the row
         * @return the max scale
         */
        public Object getMaxScale(int row) {
            return dataModel.getValueAt(row, ScaleSLDModel.COL_MAX_SCALE);
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.scale.ScaleToolPanel#ScaleToolPanel(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    void testScaleToolPanelNoChange() {
        TestSLDEditorInterface app = new TestSLDEditorInterface();

        TestScaleToolPanel testObj = new TestScaleToolPanel(app);

        DummyScaleSLDFile testData = new DummyScaleSLDFile();

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        sldDataList.add(testData.getSLDData());

        testObj.populate(sldDataList);

        // Make no changes
        testObj.saveData();

        // Should be the same as before
        assertNull(app.savedSldData);
    }

    @BeforeEach
    void before() {
        SLDEditorFile.destroyInstance();
    }

    @AfterEach
    void after() {
        SLDEditorFile.destroyInstance();
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.scale.ScaleToolPanel#ScaleToolPanel(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    void testScaleToolPanel() {
        TestSLDEditorInterface app = new TestSLDEditorInterface();

        TestScaleToolPanel testObj = new TestScaleToolPanel(app);

        DummyScaleSLDFile testData = new DummyScaleSLDFile();

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        SLDEditorFile.getInstance().setSLDData(testData.getSLDData());
        sldDataList.add(testData.getSLDData());

        testObj.populate(sldDataList);

        double expectedMinScale = 100.0;
        testObj.setMinScale(0, expectedMinScale);
        double expectedMaxScale = 999.0;
        testObj.setMaxScale(1, expectedMaxScale);

        // Make no changes
        testObj.saveData();

        // Should be the same as before
        assertNotNull(app.savedSldData);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(app.savedSldData);

        List<Rule> ruleList = null;

        if (sld != null) {
            List<StyledLayer> styledLayerList = sld.layers();

            if (styledLayerList != null) {
                for (StyledLayer styledLayer : styledLayerList) {
                    if (styledLayer instanceof NamedLayerImpl) {
                        NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;

                        for (Style style : namedLayerImpl.styles()) {
                            for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                                ruleList = fts.rules();
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(ruleList);

        assertTrue(Math.abs(ruleList.get(0).getMinScaleDenominator() - expectedMinScale) < 0.0001);
        assertTrue(Math.abs(ruleList.get(1).getMaxScaleDenominator() - expectedMaxScale) < 0.0001);
    }

    /**
     * Test method for {@link
     * com.sldeditor.tool.scale.ScaleToolPanel#ScaleToolPanel(com.sldeditor.common.SLDEditorInterface)}.
     */
    @Test
    void testScaleToolPanelRevert() {
        TestSLDEditorInterface app = new TestSLDEditorInterface();

        TestScaleToolPanel testObj = new TestScaleToolPanel(app);

        DummyScaleSLDFile testData = new DummyScaleSLDFile();

        List<SLDDataInterface> sldDataList = new ArrayList<SLDDataInterface>();

        SLDEditorFile.getInstance().setSLDData(testData.getSLDData());
        sldDataList.add(testData.getSLDData());

        testObj.populate(sldDataList);

        StyledLayerDescriptor sld = SLDUtils.createSLDFromString(testData.getSLDData());

        List<Rule> ruleList = null;

        if (sld != null) {
            List<StyledLayer> styledLayerList = sld.layers();

            if (styledLayerList != null) {
                for (StyledLayer styledLayer : styledLayerList) {
                    if (styledLayer instanceof NamedLayerImpl) {
                        NamedLayerImpl namedLayerImpl = (NamedLayerImpl) styledLayer;

                        for (Style style : namedLayerImpl.styles()) {
                            for (FeatureTypeStyle fts : style.featureTypeStyles()) {
                                ruleList = fts.rules();
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(ruleList);

        double originalMaxScale = ruleList.get(1).getMaxScaleDenominator();

        double expectedMinScale = 100.0;
        testObj.setMinScale(0, expectedMinScale);
        double expectedMaxScale = 999.0;
        testObj.setMaxScale(1, expectedMaxScale);

        // Make no changes
        testObj.revertData();

        assertNull(testObj.getMinScale(0));
        String stringValue = (String) testObj.getMaxScale(1);
        double actualScale = ScaleUtil.extractValue((String) stringValue);

        assertTrue(Math.abs(actualScale - originalMaxScale) < 0.0001);
    }
}
