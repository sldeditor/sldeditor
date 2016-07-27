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

package com.sldeditor.ui.detail.config.inlinefeature;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.styling.UserLayer;
import org.opengis.feature.simple.SimpleFeature;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.ui.detail.config.FieldConfigBase;

/**
 * The Class InLineFeatureModel.
 *
 * @author Robert Ward (SCISYS)
 */
public class InLineFeatureModel extends AbstractTableModel {


    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The column list. */
    private List<String> columnList = new ArrayList<String>();

    /** The parent obj. */
    private InlineFeatureUpdateInterface parentObj = null;

    /** The feature collection. */
    private SimpleFeatureCollection featureCollection = null;

    /** The Constant COL_TYPE. */
    private static final int COL_TYPE = 0;

    /** The Constant COL_CRS. */
    private static final int COL_CRS = 1;

    /** The Constant COL_FEATURE. */
    private static final int COL_FEATURE = 2;

    /**
     * Instantiates a new in line feature model.
     *
     * @param parent the parent
     */
    public InLineFeatureModel(InlineFeatureUpdateInterface parent)
    {
        this.parentObj = parent;

        columnList.add(Localisation.getString(FieldConfigBase.class, "InLineFeatureModel.type"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "InLineFeatureModel.crs"));
        columnList.add(Localisation.getString(FieldConfigBase.class, "InLineFeatureModel.feature"));
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return columnList.size();
    }

    /**
     * Gets the column name.
     *
     * @param column the column
     * @return the column name
     */
    @Override
    public String getColumnName(int column) {
        return columnList.get(column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        if(featureCollection != null)
        {
            return featureCollection.size();
        }
        return 0;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int column) {
        if(featureCollection != null)
        {
            SimpleFeatureIterator iterator = featureCollection.features();

            SimpleFeature feature = iterator.next();
            int index = 0;
            while(iterator.hasNext() && (index < row))
            {
                feature = iterator.next();
                index ++;
            }

            switch(column)
            {
            case COL_TYPE:
                return feature.getFeatureType().getTypeName();
            case COL_CRS:
                break;
            case COL_FEATURE:
                break;
            default:
                break;
            }
        }
        return null;
    }

    /**
     * Populate.
     *
     * @param userLayer the user layer
     */
    public void populate(UserLayer userLayer) {
        featureCollection = null;

        if(userLayer != null)
        {
            String typeName = userLayer.getInlineFeatureType().getTypeName();
            try {
                SimpleFeatureSource featureSource = userLayer.getInlineFeatureDatastore().getFeatureSource(typeName);
                if(featureSource != null)
                {
                    featureCollection = featureSource.getFeatures();
                }
            } catch (IOException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
    }

}
