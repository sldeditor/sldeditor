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

import static org.junit.Assert.assertEquals;

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

    class TestSortByPanel extends SortByPanel {

        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        /**
         * Instantiates a new test sort by panel.
         *
         * @param parentObj the parent obj
         * @param noOfRows the no of rows
         */
        public TestSortByPanel(SortByUpdateInterface parentObj, int noOfRows) {
            super(parentObj, noOfRows);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#moveDestinationDown()
         */
        @Override
        public void moveDestinationDown() {
            super.moveDestinationDown();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#moveDestinationUp()
         */
        @Override
        public void moveDestinationUp() {
            super.moveDestinationUp();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#moveDestinationToSource()
         */
        @Override
        public void moveDestinationToSource() {
            super.moveDestinationToSource();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#moveSrcToDestination()
         */
        @Override
        public void moveSrcToDestination() {
            super.moveSrcToDestination();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#destinationSelected()
         */
        @Override
        public void destinationSelected() {
            super.destinationSelected();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#sourceSelected()
         */
        @Override
        public void sourceSelected() {
            super.sourceSelected();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#selectDestination(int[])
         */
        @Override
        public void selectDestination(int[] selectedIndexes) {
            super.selectDestination(selectedIndexes);
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#selectSource(int[])
         */
        @Override
        public void selectSource(int[] selectedIndexes) {
            super.selectSource(selectedIndexes);
        }

        /* (non-Javadoc)
         * @see com.sldeditor.ui.detail.config.sortby.SortByPanel#setSortOrder(int, boolean)
         */
        @Override
        public void setSortOrder(int index, boolean isAscending) {
            super.setSortOrder(index, isAscending);
        }

    }

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

        TestSortByPanel panel = new TestSortByPanel(output, 6);

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

        assertEquals(selectedFieldList, output.text);
        int[] selectedIndexes = new int[1];
        selectedIndexes[0] = 1;

        panel.selectDestination(selectedIndexes);
        panel.moveDestinationUp();
        assertEquals("Field4 A, Field2 D", output.text);
        panel.moveDestinationUp();
        assertEquals("Field4 A, Field2 D", output.text);

        panel.moveDestinationDown();
        assertEquals("Field2 D, Field4 A", output.text);
        panel.moveDestinationDown();
        assertEquals("Field2 D, Field4 A", output.text);

        // Move source to destination
        int[] selectedSourceIndexes = new int[2];
        selectedSourceIndexes[0] = 1;
        selectedSourceIndexes[1] = 2;
        panel.selectSource(selectedSourceIndexes);

        panel.moveSrcToDestination();
        assertEquals("Field2 D, Field4 A, Field3 A, Field5 A", output.text);
        
        // Move destination to source
        selectedIndexes[0] = 0;

        panel.selectDestination(selectedIndexes);
        panel.moveDestinationToSource();
        assertEquals("Field4 A, Field3 A, Field5 A", output.text);

        panel.setSortOrder(1, false);
        assertEquals("Field4 A, Field3 D, Field5 A", output.text);

        panel.setSortOrder(1, true);
        assertEquals("Field4 A, Field3 A, Field5 A", output.text);
        // dialog.setVisible(true);
    }

}
