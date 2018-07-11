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

package com.sldeditor.test.unit.datasource.extension.filesystem.node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler;
import javax.swing.TransferHandler;
import org.junit.jupiter.api.Test;

/**
 * Unit test for TreeTransferHandler.
 *
 * <p>{@link com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler}
 *
 * @author Robert Ward (SCISYS)
 */
public class TreeTransferHandlerTest {

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler#createTransferable(javax.swing.JComponent)}.
     */
    @Test
    public void testCreateTransferableJComponent() {
        TreeTransferHandler transferHandler = new TreeTransferHandler();

        assertFalse(transferHandler.canImport(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler#exportDone(javax.swing.JComponent,
     * java.awt.datatransfer.Transferable, int)}.
     */
    @Test
    public void testExportDoneJComponentTransferableInt() {
        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler#getSourceActions(javax.swing.JComponent)}.
     */
    @Test
    public void testGetSourceActionsJComponent() {
        assertEquals(TransferHandler.COPY, new TreeTransferHandler().getSourceActions(null));
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler#importData(javax.swing.JComponent,
     * java.awt.datatransfer.Transferable)}.
     */
    @Test
    public void testImportDataJComponentTransferable() {
        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler#isDragging()}.
     */
    @Test
    public void testIsDragging() {
        // fail("Not yet implemented");
    }

    /**
     * Test method for {@link
     * com.sldeditor.datasource.extension.filesystem.node.TreeTransferHandler#canImport(javax.swing.TransferHandler.TransferSupport)}.
     */
    @Test
    public void testCanImportTransferSupport() {
        // fail("Not yet implemented");
    }
}
