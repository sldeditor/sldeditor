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

package com.sldeditor.test.unit.ui.detail.config.symboltype.externalgraphic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sldeditor.common.undo.UndoManager;
import com.sldeditor.common.xml.ui.FieldIdEnum;
import com.sldeditor.ui.detail.config.FieldConfigString;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicUpdateInterface;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.RelativePath;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.ExternalGraphicImpl;
import org.geotools.styling.StyleFactoryImpl;
import org.junit.Test;

/**
 * The unit test for ExternalGraphicDetails.
 *
 * <p>{@link com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails}
 *
 * @author Robert Ward (SCISYS)
 */
public class ExternalGraphicDetailsTest {

    /** The Class TestExternalGraphicDetails. */
    class TestExternalGraphicDetails extends ExternalGraphicDetails {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test external graphic details.
         *
         * @param parentObj the parent obj
         */
        public TestExternalGraphicDetails(ExternalGraphicUpdateInterface parentObj) {
            super(parentObj);
        }

        /**
         * Test user selected file URL.
         *
         * @param url the url
         */
        public void testUserSelectedFileURL(URL url) {
            this.userSelectedFileURL(url);
        }
    }

    /** The Class DummyExternalGraphicUpdate. */
    class DummyExternalGraphicUpdate implements ExternalGraphicUpdateInterface {

        /** The data changed called. */
        private boolean dataChangedCalled = false;

        /**
         * Checks if is called.
         *
         * @return true, if is called
         */
        public boolean isCalled() {
            boolean tmp = dataChangedCalled;
            dataChangedCalled = false;
            return tmp;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicUpdateInterface#
         * externalGraphicValueUpdated()
         */
        @Override
        public void externalGraphicValueUpdated() {
            dataChangedCalled = true;
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#setEnabled(boolean)}.
     */
    @Test
    public void testSetEnabled() {
        ExternalGraphicDetails panel = new ExternalGraphicDetails(null);

        panel.setEnabled(true);

        assertTrue(panel.isEnabled());
        panel.setEnabled(false);

        assertFalse(panel.isEnabled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#populate(com.sldeditor.common.data.SelectedSymbol)}.
     */
    @Test
    public void testPopulate() {
        ExternalGraphicDetails panel = new ExternalGraphicDetails(null);

        panel.populate(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#populateExpression(java.lang.String)}.
     */
    @Test
    public void testPopulateExpression() {
        DummyExternalGraphicUpdate callback = new DummyExternalGraphicUpdate();

        ExternalGraphicDetails panel = new ExternalGraphicDetails(callback);

        panel.populateExpression(null);

        String expectedString = "test.png";

        assertFalse(callback.isCalled());
        panel.populateExpression(expectedString);

        assertEquals(expectedString, panel.getExpression().toString());
        assertTrue(callback.isCalled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#dataChanged(com.sldeditor.common.xml.ui.FieldIdEnum)}.
     */
    @Test
    public void testDataChanged() {}

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#getFieldDataManager()}.
     */
    @Test
    public void testGetFieldDataManager() {
        ExternalGraphicDetails panel = new ExternalGraphicDetails(null);

        assertNotNull(panel.getFieldDataManager());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#isDataPresent()}.
     */
    @Test
    public void testIsDataPresent() {
        ExternalGraphicDetails panel = new ExternalGraphicDetails(null);
        assertTrue(panel.isDataPresent());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#revertToDefaultValue()}.
     */
    @Test
    public void testRevertToDefaultValue() {
        ExternalGraphicDetails panel = new ExternalGraphicDetails(null);
        panel.revertToDefaultValue();
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#setValue(org.geotools.styling.ExternalGraphicImpl)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#getSymbol()}.
     */
    @Test
    public void testSetValueExternalGraphicImpl() {
        StyleFactoryImpl styleFactory = (StyleFactoryImpl) CommonFactoryFinder.getStyleFactory();

        ExternalGraphicImpl externalGraphic = null;

        DummyExternalGraphicUpdate callback = new DummyExternalGraphicUpdate();
        ExternalGraphicDetails panel = new ExternalGraphicDetails(callback);
        assertNull(panel.getSymbol());
        panel.setValue(externalGraphic);

        String expectedString = "a/b/c/test.png";
        URL expectedURL = null;
        try {
            expectedURL = new File(expectedString).toURI().toURL();
            externalGraphic =
                    (ExternalGraphicImpl)
                            styleFactory.createExternalGraphic(expectedURL, "image/png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
        assertFalse(callback.isCalled());
        panel.setValue(externalGraphic);
        ExternalGraphicImpl actual = (ExternalGraphicImpl) panel.getSymbol();
        try {
            assertEquals(expectedURL.toExternalForm(), actual.getLocation().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
        assertTrue(callback.isCalled());

        expectedString = "http://example.com/test.png";
        externalGraphic =
                (ExternalGraphicImpl)
                        styleFactory.createExternalGraphic(expectedString, "image/png");
        assertFalse(callback.isCalled());
        panel.setValue(externalGraphic);
        actual = (ExternalGraphicImpl) panel.getSymbol();
        assertTrue(callback.isCalled());
        try {
            assertEquals(
                    externalGraphic.getLocation().toExternalForm(),
                    actual.getLocation().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#setValue(java.lang.String)}.
     */
    @Test
    public void testSetValueString() {
        DummyExternalGraphicUpdate callback = new DummyExternalGraphicUpdate();
        ExternalGraphicDetails panel = new ExternalGraphicDetails(callback);

        String expectedString = null;
        panel.setValue(expectedString);

        expectedString = "a/b/c/test.png";
        assertFalse(callback.isCalled());
        panel.setValue(expectedString);
        assertTrue(callback.isCalled());

        expectedString = "http://example.com/test.png";
        assertFalse(callback.isCalled());
        panel.setValue(expectedString);
        assertTrue(callback.isCalled());
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#buttonPressed(java.awt.Component)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#undoAction(com.sldeditor.common.undo.UndoInterface)}.
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#redoAction(com.sldeditor.common.undo.UndoInterface)}.
     */
    @Test
    public void testButtonPressed() {
        DummyExternalGraphicUpdate callback = new DummyExternalGraphicUpdate();
        TestExternalGraphicDetails panel = new TestExternalGraphicDetails(callback);

        panel.testUserSelectedFileURL(null);
        String expectedString = "a/b/c/test.png";

        URL expectedURL1 = null;
        try {
            expectedURL1 = new File(expectedString).toURI().toURL();
            assertFalse(callback.isCalled());
            panel.testUserSelectedFileURL(expectedURL1);
            assertTrue(callback.isCalled());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(RelativePath.convert(expectedURL1, true), panel.getExpression().toString());
        panel.testUserSelectedFileURL(expectedURL1);
        assertTrue(callback.isCalled());
        assertEquals(RelativePath.convert(expectedURL1, true), panel.getExpression().toString());

        expectedString = "http://example.com/test.png";
        URL expectedURL2 = null;
        try {
            expectedURL2 = new URL(expectedString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }
        panel.testUserSelectedFileURL(expectedURL2);
        assertTrue(callback.isCalled());
        assertEquals(RelativePath.convert(expectedURL2, true), panel.getExpression().toString());

        // As if user has entered data directly into field
        FieldConfigString field =
                (FieldConfigString) panel.getFieldDataManager().get(FieldIdEnum.EXTERNAL_GRAPHIC);
        expectedString = "a/test2.png";
        field.populateField(expectedString);
        assertTrue(callback.isCalled());
        assertEquals(expectedString, panel.getExpression().toString());

        // Undo
        UndoManager.getInstance().undo();
        assertEquals(RelativePath.convert(expectedURL2, true), panel.getExpression().toString());
        UndoManager.getInstance().undo();
        assertEquals(RelativePath.convert(expectedURL1, true), panel.getExpression().toString());
        UndoManager.getInstance().redo();
        assertEquals(RelativePath.convert(expectedURL2, true), panel.getExpression().toString());
        UndoManager.getInstance().redo();
        // Make sure Windows and unix strings are the same
        String actual = panel.getExpression().toString().replace("\\", "/");
        assertEquals(expectedString, actual);

        // Increase code coverage
        panel.undoAction(null);
        panel.redoAction(null);
    }

    /**
     * Test method for {@link
     * com.sldeditor.ui.detail.config.symboltype.externalgraphic.ExternalGraphicDetails#preLoadSymbol()}.
     */
    @Test
    public void testPreLoadSymbol() {
        ExternalGraphicDetails panel = new ExternalGraphicDetails(null);
        panel.preLoadSymbol();
    }
}
