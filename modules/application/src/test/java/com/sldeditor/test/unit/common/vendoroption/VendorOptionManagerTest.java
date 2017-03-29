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

package com.sldeditor.test.unit.common.vendoroption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Style;
import org.geotools.styling.StyledLayerDescriptor;
import org.junit.Test;

import com.sldeditor.common.data.SLDData;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.common.defaultsymbol.DefaultSymbols;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.preferences.PrefData;
import com.sldeditor.common.preferences.PrefManager;
import com.sldeditor.common.vendoroption.GeoServerVendorOption;
import com.sldeditor.common.vendoroption.NoVendorOption;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionTypeInterface;
import com.sldeditor.common.vendoroption.VendorOptionUpdateInterface;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.vendoroption.info.VendorOptionInfo;
import com.sldeditor.common.vendoroption.minversion.VendorOptionPresent;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.ui.panels.GetMinimumVersionInterface;

/**
 * Unit test for VendorOptionManager.
 * 
 * <p>{@link com.sldeditor.common.vendoroption.VendorOptionManager}
 * 
 * @author Robert Ward (SCISYS)
 *
 */
public class VendorOptionManagerTest {

    /**
     * The Class DummyVendorOptionUpdate.
     */
    class DummyVendorOptionUpdate implements VendorOptionUpdateInterface {

        /** The vendor option versions list. */
        public List<VersionData> vendorOptionVersionsList = new ArrayList<VersionData>();

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.common.vendoroption.VendorOptionUpdateInterface#vendorOptionsUpdated(java.util.List)
         */
        @Override
        public void vendorOptionsUpdated(List<VersionData> vendorOptionVersionsList) {
            this.vendorOptionVersionsList = vendorOptionVersionsList;
        }

    }

    /** The obj. */
    private DummyVendorOptionUpdate obj = new DummyVendorOptionUpdate();

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getClass(java.lang.Class)}.
     */
    @Test
    public void testGetClassClassOfQ() {
        assertNull(VendorOptionManager.getInstance().getClass(String.class));
        assertTrue(VendorOptionManager.getInstance().getClass(NoVendorOption.class) != null);
        assertTrue(VendorOptionManager.getInstance().getClass(GeoServerVendorOption.class) != null);
    }

    /**
     * Test method for
     * {@link com.sldeditor.common.vendoroption.VendorOptionManager#getVendorOptionVersion(java.lang.Class, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetVendorOptionVersionClassOfQStringString() {
        assertNull(VendorOptionManager.getInstance().getVendorOptionVersion(null, null, null));

        String exepectedEndVersion = "2.8.1";
        VendorOptionVersion voGS = VendorOptionManager.getInstance()
                .getVendorOptionVersion(GeoServerVendorOption.class, "2.4.1", exepectedEndVersion);
        assertEquals(exepectedEndVersion, voGS.getLatest().getVersionString());

        VendorOptionVersion voGS1 = VendorOptionManager.getInstance()
                .getVendorOptionVersion(GeoServerVendorOption.class, null, exepectedEndVersion);
        assertEquals(exepectedEndVersion, voGS1.getLatest().getVersionString());

        exepectedEndVersion = "2.8.a";
        VendorOptionVersion voGS2 = VendorOptionManager.getInstance()
                .getVendorOptionVersion(GeoServerVendorOption.class, "2.4.1", exepectedEndVersion);
        assertEquals("Latest", voGS2.getLatest().getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getDefaultVendorOption()}.
     */
    @Test
    public void testGetDefaultVendorOption() {
        VendorOptionTypeInterface vo = VendorOptionManager.getInstance().getDefaultVendorOption();

        assertEquals(NoVendorOption.class, vo.getClass());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getDefaultVendorOptionVersion()}.
     */
    @Test
    public void testGetDefaultVendorOptionVersion() {
        VendorOptionVersion vo = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();

        assertEquals("Latest", vo.getLatest().getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getVendorOptionVersion(java.lang.Class)}.
     */
    @Test
    public void testGetVendorOptionVersionClassOfQ() {
        assertNull(VendorOptionManager.getInstance().getVendorOptionVersion(null));

        VendorOptionVersion vo = VendorOptionManager.getInstance()
                .getVendorOptionVersion(NoVendorOption.class);
        assertEquals("Latest", vo.getLatest().getVersionString());

        VendorOptionVersion voGS = VendorOptionManager.getInstance()
                .getVendorOptionVersion(GeoServerVendorOption.class);
        assertEquals("Latest", voGS.getLatest().getVersionString());
    }

    /**
     * Test method for
     * {@link com.sldeditor.common.vendoroption.VendorOptionManager#isAllowed(java.util.List, com.sldeditor.common.vendoroption.VendorOptionVersion)}.
     */
    @Test
    public void testIsAllowed() {
        assertFalse(VendorOptionManager.getInstance().isAllowed(null, null));

        VersionData versionDataMin = VersionData.decode(getClass(), "2.4.1");
        VersionData versionDataMax = VersionData.decode(getClass(), "2.8.3");

        VendorOptionVersion vo = new VendorOptionVersion(GeoServerVendorOption.class,
                versionDataMin, versionDataMax);

        List<VersionData> versionList = new ArrayList<VersionData>();
        versionList.add(VersionData.decode(GeoServerVendorOption.class, "1.8.3"));

        assertFalse(VendorOptionManager.getInstance().isAllowed(versionList, vo));

        versionList.add(VersionData.decode(GeoServerVendorOption.class, "2.7.x"));
        assertTrue(VendorOptionManager.getInstance().isAllowed(versionList, vo));
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getDefaultVendorOptionVersionData()}.
     */
    @Test
    public void testGetDefaultVendorOptionVersionData() {
        VersionData versionData = VendorOptionManager.getInstance()
                .getDefaultVendorOptionVersionData();
        assertEquals("Latest", versionData.getVersionString());
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getTitle()}.
     */
    @Test
    public void testGetTitle() {
        StringBuilder expectedValue = new StringBuilder();
        expectedValue.append("- ");
        expectedValue.append(Localisation.getString(ParseXML.class, "ParseXML.vendorOption"));
        expectedValue.append(" ");

        String actualValue = VendorOptionManager.getInstance().getTitle(null);
        assertTrue(expectedValue.toString().compareTo(actualValue) == 0);

        // Now try with some real values
        VersionData noVO = VersionData
                .getDecodedString("com.sldeditor.common.vendoroption.NoVendorOption@Latest");

        VendorOptionVersion noVOV = new VendorOptionVersion(NoVendorOption.class, noVO);
        actualValue = VendorOptionManager.getInstance().getTitle(noVOV);

        StringBuilder expectedValue1 = new StringBuilder();
        expectedValue1.append(expectedValue.toString());
        expectedValue1.append("(Strict SLD Latest)");

        assertTrue(expectedValue1.toString().compareTo(actualValue) == 0);

        VersionData geoServerVOMin = VersionData
                .getDecodedString("com.sldeditor.common.vendoroption.GeoServerVendorOption@2.4.2");
        VersionData geoServerVOMax = VersionData
                .getDecodedString("com.sldeditor.common.vendoroption.GeoServerVendorOption@2.9.0");
        VendorOptionVersion geoServerVOV = new VendorOptionVersion(GeoServerVendorOption.class,
                geoServerVOMin, geoServerVOMax);
        actualValue = VendorOptionManager.getInstance().getTitle(geoServerVOV);
        StringBuilder expectedValue2 = new StringBuilder();
        expectedValue2.append(expectedValue.toString());
        expectedValue2.append("(GeoServer 2.4.2-2.9.0)");

        assertTrue(expectedValue2.toString().compareTo(actualValue) == 0);
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getVendorOptionVersion()}.
     */
    @Test
    public void testLoadSLDFile() {

        VendorOptionManager.destroyInstance();
        PrefData prefData = PrefManager.getInstance().getPrefData();
        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        VersionData actualPrefVO = VersionData.decode(GeoServerVendorOption.class, "2.4.2");
        vendorOptionList.add(actualPrefVO);
        prefData.setVendorOptionVersionList(vendorOptionList);
        PrefManager.getInstance().setPrefData(prefData);

        SLDData sldData = new SLDData(null, null);
        StyledLayerDescriptor sld = null;

        VendorOptionManager.getInstance().addVendorOptionListener(obj);
        obj.vendorOptionVersionsList.clear();

        // No SLD Editor file, SLD is empty so it should you PrefManager default set above
        VendorOptionManager.getInstance().loadSLDFile(null, sld, sldData);

        assertEquals(1, obj.vendorOptionVersionsList.size());
        assertEquals(actualPrefVO, obj.vendorOptionVersionsList.get(0));

        // No SLD Editor file, SLD contains PointSymboliser with vendor
        // option so it should use the SLD vendor option
        obj.vendorOptionVersionsList.clear();
        sld = DefaultSymbols.createNewSLD();
        SelectedSymbol.getInstance().createNewSLD(sld);
        NamedLayer namedLayer = DefaultSymbols.createNewNamedLayer();
        String expectedNameLayerValue = "named layer test value";
        namedLayer.setName(expectedNameLayerValue);
        Style style = DefaultSymbols.createNewStyle();
        String expectedNameStyleValue = "style test value";
        style.setName(expectedNameStyleValue);
        namedLayer.addStyle(style);
        FeatureTypeStyle fts = DefaultSymbols.createNewFeatureTypeStyle();
        String expectedNameFTSValue = "feature type style test value";
        fts.setName(expectedNameFTSValue);
        style.featureTypeStyles().add(fts);
        Rule rule = DefaultSymbols.createNewRule();
        String expectedNameValue = "rule test value";
        rule.setName(expectedNameValue);

        PointSymbolizer symbolizer = DefaultSymbols.createDefaultPointSymbolizer();
        rule.symbolizers().add(symbolizer);
        fts.rules().add(rule);
        sld.layers().add(namedLayer);
        VersionData testVersionData = VersionData.decode(GeoServerVendorOption.class, "2.8.0");

        VendorOptionManager.getInstance().loadSLDFile(new GetMinimumVersionInterface() {

            @Override
            public void getMinimumVersion(Object parentObj, Object sldObj,
                    List<VendorOptionPresent> vendorOptionsPresentList) {
                VendorOptionInfo vendorOptionInfo = new VendorOptionInfo("qgis://",
                        new VendorOptionVersion(testVersionData.getVendorOptionType(),
                                testVersionData),
                        "");
                VendorOptionPresent voPresent = new VendorOptionPresent(sldObj, vendorOptionInfo);
                vendorOptionsPresentList.add(voPresent);
            }
        }, sld, sldData);
        assertEquals(1, obj.vendorOptionVersionsList.size());
        assertEquals(testVersionData, obj.vendorOptionVersionsList.get(0));

        // Now set the SLD Editor file vendor option
        obj.vendorOptionVersionsList.clear();
        List<VersionData> sldEditorVendorOptionList = new ArrayList<VersionData>();
        VersionData testVersionData2 = VersionData.decode(GeoServerVendorOption.class, "2.10.0");
        sldEditorVendorOptionList.add(testVersionData2);
        sldEditorVendorOptionList
                .add(VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
        sldData.setVendorOptionList(sldEditorVendorOptionList);
        sldData.setSldEditorFile(new File("."));
        VendorOptionManager.getInstance().loadSLDFile(new GetMinimumVersionInterface() {

            @Override
            public void getMinimumVersion(Object parentObj, Object sldObj,
                    List<VendorOptionPresent> vendorOptionsPresentList) {
                VendorOptionInfo vendorOptionInfo = new VendorOptionInfo("qgis://",
                        new VendorOptionVersion(testVersionData.getVendorOptionType(),
                                testVersionData),
                        "");
                VendorOptionPresent voPresent = new VendorOptionPresent(sldObj, vendorOptionInfo);
                vendorOptionsPresentList.add(voPresent);
            }
        }, sld, sldData);
        Collections.sort(obj.vendorOptionVersionsList);
        assertEquals(2, obj.vendorOptionVersionsList.size());
        assertEquals(testVersionData2,
                obj.vendorOptionVersionsList.get(obj.vendorOptionVersionsList.size() - 1));

        // Increase code coverage
        VendorOptionManager.getInstance().loadSLDFile(null, null, null);
    }

    /**
     * Test method for
     * {@link com.sldeditor.common.vendoroption.VendorOptionManager#overrideSelectedVendorOptions(java.util.List, com.sldeditor.common.vendoroption.VendorOptionVersion)}.
     */
    @Test
    public void testOverrideSelectedVendorOptions() {
        VendorOptionManager.destroyInstance();
        VendorOptionManager.getInstance().addVendorOptionListener(obj);
        obj.vendorOptionVersionsList.clear();

        List<VersionData> vendorOptionList = new ArrayList<VersionData>();
        VersionData actualPrefVO = VersionData.decode(GeoServerVendorOption.class, "2.4.2");
        vendorOptionList.add(actualPrefVO);
        VendorOptionManager.getInstance().setSelectedVendorOptions(vendorOptionList);
        assertEquals(1, obj.vendorOptionVersionsList.size());

        obj.vendorOptionVersionsList.clear();
        List<VersionData> vendorOptionList2 = new ArrayList<VersionData>();
        VersionData actualPrefVO2 = VersionData.decode(GeoServerVendorOption.class, "2.3.2");
        vendorOptionList2.add(actualPrefVO2);
        VendorOptionManager.getInstance().overrideSelectedVendorOptions(vendorOptionList2);
        assertEquals(1, obj.vendorOptionVersionsList.size());

        // Should now be unaffected by changes in vendor option
        obj.vendorOptionVersionsList.clear();
        VendorOptionManager.getInstance().setSelectedVendorOptions(vendorOptionList2);
        assertEquals(0, obj.vendorOptionVersionsList.size());

        VendorOptionManager.destroyInstance();
    }

    /**
     * Test method for {@link com.sldeditor.common.vendoroption.VendorOptionManager#getLatest()}.
     */
    @Test
    public void testGetLatest() {
        List<VersionData> actualData = VendorOptionManager.getInstance().getLatest();
        assertEquals(1, actualData.size());
    }
}
