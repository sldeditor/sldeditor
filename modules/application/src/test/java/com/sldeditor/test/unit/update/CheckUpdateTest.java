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

package com.sldeditor.test.unit.update;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.junit.Test;

import com.sldeditor.common.vendoroption.VersionData;
import com.sldeditor.update.CheckUpdate;
import com.sldeditor.update.CheckUpdateClientInterface;
import com.sldeditor.update.UpdateData;

/**
 * The unit test for CheckUpdate.
 * 
 * <p>{@link com.sldeditor.update.CheckUpdate}
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdateTest {

    /**
     * The Class TestCheckUpdateClient.
     */
    class TestCheckUpdateClient implements CheckUpdateClientInterface {
        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.update.CheckUpdateClientInterface#getLatest()
         */
        @Override
        public UpdateData getLatest() {
            UpdateData data = new UpdateData(VersionData.decode(CheckUpdate.class, "0.5.0"),
                    "Some description");
            return data;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.update.CheckUpdateClientInterface#getDownloadURL()
         */
        @Override
        public URL getDownloadURL() {
            return null;
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.update.CheckUpdateClientInterface#isDestinationReached()
         */
        @Override
        public boolean isDestinationReached() {
            return true;
        }
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdate#getLatestData()}. Test method for
     * {@link com.sldeditor.update.CheckUpdate#shouldUpdate(java.lang.String)}.
     */
    @Test
    public void testGetLatestData() {
        CheckUpdate obj = new CheckUpdate(null);
        assertFalse(obj.shouldUpdate("0.1.0"));
        assertNull(obj.getLatestData());

        obj = new CheckUpdate(new TestCheckUpdateClient());
        assertNull(obj.getLatestData());
        assertTrue(obj.shouldUpdate("0.1.0"));
        assertTrue(obj.shouldUpdate("0.5.0-SNAPSHOT"));
        assertFalse(obj.shouldUpdate("0.5.0"));
        assertFalse(obj.shouldUpdate("0.5.1"));
        assertFalse(obj.shouldUpdate("1.3.1"));
        assertFalse(obj.shouldUpdate("0.6.0"));
        assertNotNull(obj.getLatestData());
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdate#showDownloadPage()}.
     */
    @Test
    public void testShowDownloadPage() {
        CheckUpdate obj = new CheckUpdate(null);
        obj.showDownloadPage();
    }

    /**
     * Test method for {@link com.sldeditor.update.CheckUpdate#isDestinationReached()}.
     */
    @Test
    public void testIsDestinationReached() {
        CheckUpdate obj = new CheckUpdate(null);
        assertFalse(obj.isDestinationReached());

        obj = new CheckUpdate(new TestCheckUpdateClient());
        assertTrue(obj.isDestinationReached());
    }
}
