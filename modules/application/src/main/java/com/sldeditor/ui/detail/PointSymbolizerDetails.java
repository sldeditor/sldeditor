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
package com.sldeditor.ui.detail;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.Symbolizer;
import org.opengis.filter.expression.Expression;

import com.sldeditor.common.Controller;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.data.SelectedSymbol;
import com.sldeditor.datasource.RenderSymbolInterface;
import com.sldeditor.filter.v2.function.FunctionManager;
import com.sldeditor.filter.v2.function.FunctionNameInterface;
import com.sldeditor.ui.detail.config.FieldId;
import com.sldeditor.ui.iface.PopulateDetailsInterface;
import com.sldeditor.ui.iface.UpdateSymbolInterface;

/**
 * The Class PointSymbolizerDetails allows a user to configure point symbolizer data in a panel.
 * 
 * @author Robert Ward (SCISYS)
 */
public class PointSymbolizerDetails extends StandardPanel implements PopulateDetailsInterface, UpdateSymbolInterface {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public PointSymbolizerDetails(FunctionNameInterface functionManager)
    {
        super(PointSymbolizerDetails.class, functionManager);
        createUI();
    }

    /**
     * Creates the ui.
     */
    private void createUI() {

        readConfigFile(this, "Point.xml");
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#populate(com.sldeditor.ui.detail.selectedsymbol.SelectedSymbol)
     */
    @Override
    public void populate(SelectedSymbol selectedSymbol) {

        if(selectedSymbol != null)
        {
            Symbolizer symbolizer = selectedSymbol.getSymbolizer();
            populateStandardData(symbolizer);
        }

        updateSymbol();
    }

    /**
     * Update symbol.
     */
    private void updateSymbol() {
        if(!Controller.getInstance().isPopulating())
        {
            StandardData standardData = getStandardData();

            String geometryFieldName = null;
            Expression geometryField = getFilterFactory().property(geometryFieldName);

            PointSymbolizer pointSymbolizer = (PointSymbolizer)SelectedSymbol.getInstance().getSymbolizer();

            if(pointSymbolizer != null)
            {
                pointSymbolizer.setName(standardData.name);
                pointSymbolizer.setDescription(standardData.description);
                pointSymbolizer.setUnitOfMeasure(standardData.unit);

                if((geometryField != null) && !geometryField.toString().isEmpty())
                {
                    pointSymbolizer.setGeometry(geometryField);
                }

                this.fireUpdateSymbol();
            }
        }
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.UpdateSymbolInterface#dataChanged(com.sldeditor.ui.detail.config.xml.FieldIdEnum)
     */
    @Override
    public void dataChanged(FieldId changedField) {
        updateSymbol();
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.detail.BasePanel#addRenderer(com.sldeditor.render.iface.RenderSymbolInterface)
     */
    @Override
    public void addRenderer(RenderSymbolInterface renderer) {
        super.addRenderer(renderer);
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#getFieldDataManager()
     */
    @Override
    public GraphicPanelFieldManager getFieldDataManager()
    {
        return this.fieldConfigManager;
    }

    /**
     * The main method.
     *
     * @param args the arguments
     */
    public static void main(String[] args) {

        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } 
                catch (UnsupportedLookAndFeelException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
                catch (ClassNotFoundException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
                catch (InstantiationException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
                catch (IllegalAccessException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
                JFrame frame = new JFrame("Test");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                // Add contents to the window.
                JPanel panel = new JPanel();
                panel.setPreferredSize(new Dimension(600, 464));
                panel.setLayout(new BorderLayout());

                PointSymbolizerDetails pointDetails = new PointSymbolizerDetails(FunctionManager.getInstance());

                panel.add(pointDetails, BorderLayout.CENTER);
                frame.getContentPane().add(panel);

                //Display the window.
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#isDataPresent()
     */
    @Override
    public boolean isDataPresent()
    {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.ui.iface.PopulateDetailsInterface#initialseFields()
     */
    @Override
    public void preLoadSymbol() {
        setAllDefaultValues();
    }
}
