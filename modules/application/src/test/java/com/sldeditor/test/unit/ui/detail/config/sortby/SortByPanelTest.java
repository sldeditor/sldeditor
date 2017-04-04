/*
 * SLD Editor - The Open Source Java SLD Editor
 *
 * Copyright (C) 2017, SCISYS UK Limited
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

package com.sldeditor.test.unit.ui.detail.config.sortby;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JDialog;

import org.junit.Test;

import com.sldeditor.ui.detail.config.sortby.SortByPanel;
import com.sldeditor.ui.detail.config.sortby.SortByUpdateInterface;

/**
 * The Class SortByPanelTest.
 *
 * @author Robert Ward (SCISYS)
 */
public class SortByPanelTest {

    class TestSortByUpdate implements SortByUpdateInterface {

        public String text;

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByUpdateInterface#sortByUpdated(java.lang.
         * String)
         */
        @Override
        public void sortByUpdated(String sortByString) {
            text = sortByString;
            System.out.println(sortByString);
        }

    }

    /**
     * Test method for {@link com.sldeditor.ui.detail.config.sortby.SortByPanel#SortByPanel()}.
     */
    @Test
    public void testSortByPanel() {
        TestSortByUpdate output = new TestSortByUpdate();

        SortByPanel panel = new SortByPanel(output, 6);

        JDialog dialog = new JDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setModal(true);
        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocation(200, 200);
        dialog.setTitle("SortBy Test Dialog");

        List<String> fieldList = Arrays.asList("Field1", "Field2", "Field3", "Field4", "Field5");
        String selectedFieldList = "Field2 D, Field4 A";
        panel.populateFieldNames(fieldList);
        panel.setText(selectedFieldList);
        dialog.setVisible(true);
    }

}
