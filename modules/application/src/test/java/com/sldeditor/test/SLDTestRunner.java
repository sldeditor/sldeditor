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

package com.sldeditor.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.sldeditor.SLDEditor;
import com.sldeditor.TreeSelectionData;
import com.sldeditor.common.utils.OSValidator;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.common.xml.ParseXML;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.common.xml.ui.GroupIdEnum;
import com.sldeditor.common.xml.ui.SldEditorTest;
import com.sldeditor.common.xml.ui.XMLColourMapEntries;
import com.sldeditor.common.xml.ui.XMLFieldAttribute;
import com.sldeditor.common.xml.ui.XMLFieldBase;
import com.sldeditor.common.xml.ui.XMLFieldDisabled;
import com.sldeditor.common.xml.ui.XMLFieldExpression;
import com.sldeditor.common.xml.ui.XMLFieldLiteralBase;
import com.sldeditor.common.xml.ui.XMLFieldLiteralBoolean;
import com.sldeditor.common.xml.ui.XMLFieldLiteralDouble;
import com.sldeditor.common.xml.ui.XMLFieldLiteralInt;
import com.sldeditor.common.xml.ui.XMLFieldLiteralString;
import com.sldeditor.common.xml.ui.XMLFieldTest;
import com.sldeditor.common.xml.ui.XMLIndex;
import com.sldeditor.common.xml.ui.XMLPanelTest;
import com.sldeditor.common.xml.ui.XMLSetFieldAttribute;
import com.sldeditor.common.xml.ui.XMLSetFieldLiteralBase;
import com.sldeditor.common.xml.ui.XMLSetFieldLiteralInterface;
import com.sldeditor.common.xml.ui.XMLSetGroup;
import com.sldeditor.common.xml.ui.XMLSetMultiOptionGroup;
import com.sldeditor.common.xml.ui.XMLSetup;
import com.sldeditor.common.xml.ui.XMLVendorOption;
import com.sldeditor.ui.detail.GraphicPanelFieldManager;
import com.sldeditor.ui.detail.config.FieldConfigBase;
import com.sldeditor.ui.detail.config.FieldConfigColour;
import com.sldeditor.ui.detail.config.ReadPanelConfig;
import com.sldeditor.ui.detail.config.base.GroupConfigInterface;
import com.sldeditor.ui.detail.config.base.MultiOptionGroup;
import com.sldeditor.ui.detail.config.base.OptionGroup;
import com.sldeditor.ui.detail.config.colourmap.EncodeColourMap;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.widgets.ValueComboBoxData;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.apache.commons.io.IOUtils;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.LiteralExpressionImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;

/**
 * The Class SLDTestRunner.
 *
 * @author Robert Ward (SCISYS)
 */
public class SLDTestRunner {

    /** The Constant TTF_PREFIX. */
    private static final String TTF_PREFIX = "ttf://";

    /** The Constant DEFAULT_FONT. */
    private static final String DEFAULT_FONT = "Arial";

    /** The Constant DEFAULT_UNIX_FONT. */
    private static final String DEFAULT_UNIX_FONT = "Century Schoolbook L";

    /** The sld editor. */
    private SLDEditor sldEditor;

    /** The Constant SCHEMA_RESOURCE. */
    private static final String SCHEMA_RESOURCE = "/xsd/testvalue.xsd";

    /** The epsilon. */
    private double epsilon = 0.0001;

    /** The colour fields list. */
    private List<FieldIdEnum> colourFieldsList = new ArrayList<FieldIdEnum>();

    /** The filename list. */
    private List<FieldIdEnum> filenameList = new ArrayList<FieldIdEnum>();

    private SLDOutputTest testOutput = new SLDOutputTest();

    public static final String PREFIX = "extracted";

    public static final String SUFFIX = ".sld";

    /** The filter factory. */
    private static FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    /** Instantiates a new SLD test runner. */
    public SLDTestRunner() {
        //      BaseTestRunner.setPreference("maxmessage", "-1");

        System.out.println("Operating system is : " + OSValidator.getOS());
        // Populate the list of fields that are colours
        colourFieldsList.add(FieldIdEnum.FILL_COLOUR);
        colourFieldsList.add(FieldIdEnum.STROKE_FILL_COLOUR);
        colourFieldsList.add(FieldIdEnum.STROKE_STROKE_COLOUR);
        colourFieldsList.add(FieldIdEnum.HALO_COLOUR);

        filenameList.add(FieldIdEnum.SYMBOL_TYPE);
        filenameList.add(FieldIdEnum.EXTERNAL_GRAPHIC);
        filenameList.add(FieldIdEnum.TTF_SYMBOL);

        try {
            sldEditor = SLDEditor.createAndShowGUI(null, null, true, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an InputStream to a temporary file.
     *
     * @param in the in
     * @return the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static File stream2file(InputStream in) throws IOException {
        final File tempFile = File.createTempFile(PREFIX, SUFFIX);
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        // Update the font for the operating system
        String newFont = getFontForOS();
        if (newFont.compareToIgnoreCase(DEFAULT_FONT) != 0) {
            BufferedReader br = new BufferedReader(new FileReader(tempFile));
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line.replace(DEFAULT_FONT, newFont));
                    sb.append("\n");
                    line = br.readLine();
                }
                try {
                    FileWriter fileWriter = new FileWriter(tempFile);
                    fileWriter.write(sb.toString());
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } finally {
                br.close();
            }
        }
        return tempFile;
    }

    /**
     * Gets the font for the operating system.
     *
     * @return the new font
     */
    private static String getFontForOS() {
        if (OSValidator.isUnix()) {
            return DEFAULT_UNIX_FONT;
        }
        return DEFAULT_FONT;
    }

    /**
     * Run the test.
     *
     * @param folder the folder
     * @param testConfig the test config
     */
    public void runTest(String folder, String testConfig) {
        // read JSON file data as String
        String fullPath = "/" + folder + "/test/" + testConfig;

        SldEditorTest testSuite =
                (SldEditorTest)
                        ParseXML.parseFile("", fullPath, SCHEMA_RESOURCE, SldEditorTest.class);

        assertNotNull(testSuite, "Failed to read test config file : " + fullPath);

        String testsldfile = testSuite.getTestsldfile();
        if (!testsldfile.startsWith("/")) {
            testsldfile = "/" + testsldfile;
        }

        System.out.println("Opening : " + testsldfile);

        List<XMLVendorOption> xmlVendorOptionList = testSuite.getVendorOption();
        List<VersionData> versionDataList = new ArrayList<VersionData>();
        if ((xmlVendorOptionList != null) && !xmlVendorOptionList.isEmpty()) {

            for (XMLVendorOption vo : xmlVendorOptionList) {
                VersionData versionData = ReadPanelConfig.decodeVersionData(vo);
                versionDataList.add(versionData);
            }
        }

        // If in doubt revert to strict SLD
        if (versionDataList.isEmpty()) {
            versionDataList.add(
                    VendorOptionManager.getInstance().getDefaultVendorOptionVersionData());
        }
        sldEditor.setVendorOptions(versionDataList);

        InputStream inputStream = SLDTestRunner.class.getResourceAsStream(testsldfile);

        if (inputStream == null) {
            assertNotNull(inputStream, "Failed to find sld test file : " + testsldfile);
        } else {
            File f = null;
            try {
                f = stream2file(inputStream);

                int noOfRetries = 3;
                int attempt = 0;

                while (attempt < noOfRetries) {
                    try {
                        sldEditor.openFile(f.toURI().toURL());
                        break;
                    } catch (NullPointerException nullException) {
                        nullException.printStackTrace();
                        StackTraceElement[] stackTraceElements = nullException.getStackTrace();

                        System.out.println(stackTraceElements[0].getMethodName());

                        System.out.println("Attempt : " + attempt + 1);
                        attempt++;
                    }
                }

                f.delete();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            GraphicPanelFieldManager mgr = sldEditor.getFieldDataManager();

            for (XMLPanelTest test : testSuite.getPanelTests()) {
                XMLSetup selectedItem = test.getSetup();

                TreeSelectionData selectionData = new TreeSelectionData();
                selectionData.setLayerIndex(getXMLValue(selectedItem.getLayer()));
                selectionData.setStyleIndex(getXMLValue(selectedItem.getStyle()));
                selectionData.setFeatureTypeStyleIndex(
                        getXMLValue(selectedItem.getFeatureTypeStyle()));
                selectionData.setRuleIndex(getXMLValue(selectedItem.getRule()));
                selectionData.setSymbolizerIndex(getXMLValue(selectedItem.getSymbolizer()));
                selectionData.setSymbolizerDetailIndex(
                        getXMLValue(selectedItem.getSymbolizerDetail()));
                try {
                    selectionData.setSelectedPanel(Class.forName(selectedItem.getExpectedPanel()));
                } catch (ClassNotFoundException e1) {
                    fail("Unknown class : " + selectedItem.getExpectedPanel());
                }

                boolean result = sldEditor.selectTreeItem(selectionData);

                assertTrue(result, "Failed to select tree item");
                PopulateDetailsInterface panel = sldEditor.getSymbolPanel();

                String panelClassName = panel.getClass().getName();
                assertEquals(selectedItem.getExpectedPanel(), panelClassName);

                assertEquals(
                        panel.isDataPresent(),
                        selectedItem.getEnabled(),
                        "Check panel data present");

                Class<?> panelId = null;
                try {
                    panelId = Class.forName(selectedItem.getExpectedPanel());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (test.getFieldTests() != null) {
                    for (XMLFieldTest testItem : test.getFieldTests()) {
                        if (testItem != null) {
                            if (testItem.getDisabledOrLiteralStringOrLiteralInt() != null) {
                                for (Object xmlTestValueObj :
                                        testItem.getDisabledOrLiteralStringOrLiteralInt()) {
                                    if (xmlTestValueObj instanceof XMLSetMultiOptionGroup) {
                                        XMLSetMultiOptionGroup testValue =
                                                (XMLSetMultiOptionGroup) xmlTestValueObj;
                                        GroupIdEnum groupId = testValue.getMultiOptionGroupId();
                                        String outputText =
                                                "Checking multioption group : " + groupId;

                                        System.out.println(outputText);
                                        assertNotNull(groupId, outputText);

                                        MultiOptionGroup multiOptionGroup =
                                                mgr.getMultiOptionGroup(panelId, groupId);

                                        assertNotNull(
                                                multiOptionGroup,
                                                panelId.getName()
                                                        + "/"
                                                        + groupId
                                                        + " multi option group should exist");

                                        multiOptionGroup.setOption(testValue.getOption());

                                        OptionGroup optionGroupSelected =
                                                multiOptionGroup.getSelectedOptionGroup();

                                        assertTrue(
                                                optionGroupSelected.getId()
                                                        == testValue.getOption(),
                                                groupId + " should be set");
                                    } else if (xmlTestValueObj instanceof XMLSetGroup) {
                                        XMLSetGroup testValue = (XMLSetGroup) xmlTestValueObj;
                                        GroupIdEnum groupId = testValue.getGroupId();
                                        String outputText = "Checking group : " + groupId;

                                        System.out.println(outputText);
                                        assertNotNull(groupId, outputText);

                                        GroupConfigInterface groupConfig =
                                                mgr.getGroup(panelId, groupId);

                                        assertNotNull(
                                                groupConfig,
                                                panelId.getName()
                                                        + "/"
                                                        + groupId
                                                        + " group should exist");

                                        groupConfig.enable(testValue.getEnable());

                                        assertTrue(
                                                groupConfig.isPanelEnabled()
                                                        == testValue.getEnable(),
                                                groupId + " should be set");
                                    } else {
                                        XMLFieldBase testValue = (XMLFieldBase) xmlTestValueObj;
                                        FieldIdEnum fieldId = testValue.getField();
                                        String outputText = "Checking : " + fieldId;
                                        System.out.println(outputText);
                                        assertNotNull(fieldId, outputText);

                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        FieldConfigBase fieldConfig = mgr.getData(panelId, fieldId);

                                        assertNotNull(
                                                fieldConfig,
                                                String.format(
                                                        "Failed to field panel %s field %s",
                                                        selectedItem.getExpectedPanel(), fieldId));

                                        if (testValue instanceof XMLSetFieldLiteralBase) {
                                            XMLSetFieldLiteralInterface testInterface =
                                                    (XMLSetFieldLiteralInterface) testValue;
                                            testInterface.accept(fieldConfig, fieldId);

                                            if (!((XMLSetFieldLiteralBase) testValue)
                                                    .getIgnoreCheck()) {
                                                String sldContentString = sldEditor.getSLDString();

                                                boolean actualResult =
                                                        testOutput.testValue(
                                                                sldContentString,
                                                                selectionData,
                                                                testValue.getField(),
                                                                testValue);

                                                assertTrue(
                                                        actualResult, fieldId + " should be set");
                                            }
                                        } else if (testValue instanceof XMLSetFieldAttribute) {
                                            XMLSetFieldLiteralInterface testInterface =
                                                    (XMLSetFieldLiteralInterface) testValue;
                                            testInterface.accept(fieldConfig, fieldId);

                                            String sldContentString = sldEditor.getSLDString();

                                            boolean actualResult =
                                                    testOutput.testAttribute(
                                                            sldContentString,
                                                            selectionData,
                                                            testValue.getField(),
                                                            (XMLSetFieldAttribute) testValue);

                                            assertTrue(actualResult, fieldId + " should be set");
                                        } else if (testValue instanceof XMLFieldDisabled) {
                                            assertFalse(
                                                    fieldConfig.isEnabled(),
                                                    fieldId + " should be disabled");
                                        } else {
                                            assertTrue(
                                                    fieldConfig.isEnabled(),
                                                    fieldId + " should be enabled");
                                            Expression expression = null;

                                            if (fieldConfig.isValueOnly()) {
                                                String expectedValue = "";

                                                if (testValue instanceof XMLFieldLiteralBase) {
                                                    Object literalValue =
                                                            getLiteralValue(
                                                                    (XMLFieldLiteralBase)
                                                                            testValue);
                                                    expectedValue = String.valueOf(literalValue);

                                                    if (fieldId == FieldIdEnum.TTF_SYMBOL) {
                                                        expectedValue =
                                                                processTTFField(expectedValue)
                                                                        .toString();
                                                    }

                                                } else if (testValue instanceof XMLFieldAttribute) {
                                                    expectedValue =
                                                            ((XMLFieldAttribute) testValue)
                                                                    .getAttribute();
                                                    // CHECKSTYLE:OFF
                                                } else if (testValue
                                                        instanceof XMLFieldExpression) {
                                                    expectedValue =
                                                            ((XMLFieldExpression) testValue)
                                                                    .getExpression();
                                                } else if (testValue
                                                        instanceof XMLColourMapEntries) {
                                                    expectedValue =
                                                            EncodeColourMap.encode(
                                                                    ((XMLColourMapEntries)
                                                                                    testValue)
                                                                            .getEntry());
                                                    // CHECKSTYLE:ON
                                                } else {
                                                    fail(
                                                            fieldId
                                                                    + " has unsupported type "
                                                                    + testValue
                                                                            .getClass()
                                                                            .getName());
                                                }

                                                String actualValue = fieldConfig.getStringValue();

                                                String msg =
                                                        String.format(
                                                                "%s Expected : '%s' Actual : '%s'",
                                                                outputText,
                                                                expectedValue,
                                                                actualValue);

                                                boolean condition;
                                                if (comparingFilename(fieldId)) {
                                                    File actualFile = new File(actualValue);
                                                    File expectedFile = new File(expectedValue);

                                                    String actualFileString =
                                                            actualFile.getAbsolutePath();
                                                    String expectedFileString =
                                                            expectedFile.getAbsolutePath();
                                                    expectedFileString =
                                                            expectedFileString.substring(
                                                                    expectedFileString.length()
                                                                            - expectedValue
                                                                                    .length());
                                                    condition =
                                                            actualFileString.endsWith(
                                                                    expectedFileString);
                                                } else {
                                                    condition =
                                                            (expectedValue.compareTo(actualValue)
                                                                    == 0);
                                                }
                                                assertTrue(condition, msg);
                                            } else {
                                                if (colourFieldsList.contains(fieldId)) {
                                                    FieldConfigColour fieldColour =
                                                            (FieldConfigColour) fieldConfig;

                                                    expression = fieldColour.getColourExpression();
                                                } else {
                                                    expression = fieldConfig.getExpression();

                                                    if (fieldId == FieldIdEnum.SYMBOL_TYPE) {
                                                        String string = expression.toString();

                                                        expression =
                                                                ff.literal(
                                                                        string.replace(
                                                                                File.separatorChar,
                                                                                '/'));
                                                    } else if (fieldId == FieldIdEnum.FONT_FAMILY) {
                                                        // Handle the case where a font is not
                                                        // available on all operating systems
                                                        String string = expression.toString();

                                                        if (string.compareToIgnoreCase(DEFAULT_FONT)
                                                                != 0) {
                                                            expression = ff.literal(getFontForOS());
                                                            // CHECKSTYLE:OFF
                                                            System.out.println(
                                                                    "Updated font family to test for : "
                                                                            + expression
                                                                                    .toString());
                                                            // CHECKSTYLE:ON
                                                        }
                                                    } else if (fieldId == FieldIdEnum.TTF_SYMBOL) {
                                                        expression =
                                                                processTTFField(
                                                                        expression.toString());
                                                    }
                                                }
                                                if (expression != null) {
                                                    if (testValue instanceof XMLFieldLiteralBase) {
                                                        Object literalValue =
                                                                getLiteralValue(
                                                                        (XMLFieldLiteralBase)
                                                                                testValue);

                                                        if (literalValue.getClass()
                                                                == Double.class) {
                                                            checkLiteralValue(
                                                                    outputText,
                                                                    expression,
                                                                    (Double) literalValue);
                                                        } else if (literalValue.getClass()
                                                                == Integer.class) {
                                                            checkLiteralValue(
                                                                    outputText,
                                                                    expression,
                                                                    (Integer) literalValue);
                                                        } else if (literalValue.getClass()
                                                                == String.class) {
                                                            // CHECKSTYLE:OFF
                                                            if (fieldId
                                                                    == FieldIdEnum.FONT_FAMILY) {
                                                                // Handle the case where a font is
                                                                // not
                                                                // available on all operating
                                                                // systems
                                                                // CHECKSTYLE:ON
                                                                checkLiteralValue(
                                                                        outputText,
                                                                        expression,
                                                                        getFontForOS());
                                                            } else {
                                                                checkLiteralValue(
                                                                        outputText,
                                                                        expression,
                                                                        (String) literalValue);
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    String actualValue;
                                                    String expectedValue =
                                                            fieldConfig.getStringValue();

                                                    Object literalValue =
                                                            getLiteralValue(
                                                                    (XMLFieldLiteralBase)
                                                                            testValue);

                                                    if (literalValue.getClass() == Double.class) {
                                                        actualValue =
                                                                String.valueOf(
                                                                        (Double) literalValue);
                                                    } else if (literalValue.getClass()
                                                            == Integer.class) {
                                                        actualValue =
                                                                String.valueOf(
                                                                        (Integer) literalValue);
                                                    } else if (literalValue.getClass()
                                                            == String.class) {
                                                        actualValue = (String) literalValue;
                                                    } else {
                                                        actualValue = "";
                                                    }

                                                    String msg =
                                                            String.format(
                                                                    "%s Expected : '%s' Actual : '%s'",
                                                                    outputText,
                                                                    expectedValue,
                                                                    actualValue);
                                                    boolean condition =
                                                            (expectedValue.compareTo(actualValue)
                                                                    == 0);
                                                    assertTrue(condition, msg);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        JFrame frame = sldEditor.getApplicationFrame();
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Process TTF field.
     *
     * @param expectedValue the expected value
     * @return the expression
     */
    protected Expression processTTFField(String expectedValue) {
        Expression expression = null;
        // Handle the case where a font is not available on all operating systems
        String string = expectedValue.toLowerCase();

        if (string.startsWith(TTF_PREFIX + DEFAULT_FONT.toLowerCase())) {
            String charCode = expectedValue.substring(TTF_PREFIX.length() + DEFAULT_FONT.length());
            expression = ff.literal(TTF_PREFIX + getFontForOS() + charCode);
            System.out.println("Updated font family to test for : " + expression.toString());
        } else {
            expression = ff.literal(expectedValue);
        }

        return expression;
    }

    /**
     * Gets the literal value as an Object.
     *
     * @param testValue the test value
     * @return the literal value
     */
    private Object getLiteralValue(XMLFieldLiteralBase testValue) {
        if (testValue instanceof XMLFieldLiteralString) {
            return ((XMLFieldLiteralString) testValue).getValue();
        } else if (testValue instanceof XMLFieldLiteralInt) {
            return ((XMLFieldLiteralInt) testValue).getValue();
        } else if (testValue instanceof XMLFieldLiteralDouble) {
            return ((XMLFieldLiteralDouble) testValue).getValue();
        } else if (testValue instanceof XMLFieldLiteralBoolean) {
            return ((XMLFieldLiteralBoolean) testValue).getValue();
        }

        return null;
    }

    /**
     * Gets the XML value.
     *
     * @param xmlIndex the xml index
     * @return the XML value
     */
    private int getXMLValue(XMLIndex xmlIndex) {
        if (xmlIndex == null) {
            return -1;
        }
        return xmlIndex.getIndex();
    }

    /**
     * Comparing filename.
     *
     * @param field the field
     * @return true, if successful
     */
    private boolean comparingFilename(FieldIdEnum field) {
        return filenameList.contains(field);
    }

    /**
     * Check literal value.
     *
     * @param message the message
     * @param expression the expression
     * @param expectedValue the expected value
     */
    private void checkLiteralValue(String message, Expression expression, double expectedValue) {
        assertEquals(expression.getClass(), LiteralExpressionImpl.class);
        LiteralExpressionImpl literalExpression = (LiteralExpressionImpl) expression;
        Object value = literalExpression.getValue();
        assertEquals(value.getClass(), Double.class, message);
        Double actualValue = (Double) value;
        String additional = String.format(" Expected '%f' Actual '%f'", expectedValue, actualValue);
        assertTrue(Math.abs(expectedValue - actualValue) < epsilon, message + additional);
    }

    /**
     * Check literal value.
     *
     * @param message the message
     * @param expression the expression
     * @param expectedValue the expected value
     */
    private void checkLiteralValue(String message, Expression expression, int expectedValue) {
        assertEquals(expression.getClass(), LiteralExpressionImpl.class);
        LiteralExpressionImpl literalExpression = (LiteralExpressionImpl) expression;
        Object value = literalExpression.getValue();
        assertEquals(value.getClass(), Integer.class, message);
        Integer actualValue = (Integer) value;
        String additional = String.format(" Expected '%d' Actual '%d'", expectedValue, actualValue);
        assertTrue((expectedValue == actualValue), message + additional);
    }

    /**
     * Check literal value.
     *
     * @param message the message
     * @param expression the expression
     * @param expectedValue the expected value
     */
    private void checkLiteralValue(String message, Expression expression, String expectedValue) {
        assertEquals(expression.getClass(), LiteralExpressionImpl.class);
        LiteralExpressionImpl literalExpression = (LiteralExpressionImpl) expression;
        Object value = literalExpression.getValue();
        String actualValue = null;
        if (value.getClass() == ValueComboBoxData.class) {
            actualValue = ((ValueComboBoxData) value).getKey();
        } else {
            assertEquals(value.getClass(), String.class, message);
            actualValue = (String) value;
        }
        String additional = String.format(" Expected '%s' Actual '%s'", expectedValue, actualValue);
        assertTrue(expectedValue.equals(actualValue), message + additional);
    }
}
