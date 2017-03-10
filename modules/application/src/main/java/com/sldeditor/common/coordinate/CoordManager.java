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
package com.sldeditor.common.coordinate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.geotools.factory.Hints;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.opengis.metadata.Identifier;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.vendoroption.VendorOptionManager;
import com.sldeditor.common.vendoroption.VendorOptionVersion;
import com.sldeditor.ui.widgets.ValueComboBoxData;

/**
 * The Class CoordManager.
 *
 * @author Robert Ward (SCISYS)
 */
public class CoordManager {

    private static final String WGS84 = "EPSG:4326";

    /** The singleton instance. */
    private static CoordManager instance = null;

    /** The crs data list. */
    private static List<ValueComboBoxData> crsDataList = new ArrayList<ValueComboBoxData>();

    /** The crs map. */
    private static HashMap<String, ValueComboBoxData> crsMap = new HashMap<String, ValueComboBoxData>();

    /** The default crs. */
    private CoordinateReferenceSystem defaultCRS = null;

    /**
     * Gets the single instance of CoordManager.
     *
     * @return single instance of CoordManager
     */
    public static CoordManager getInstance()
    {
        if(instance == null)
        {
            instance = new CoordManager();
        }

        return instance;
    }

    /**
     * Gets the CRS list.
     *
     * @return the CRS list
     */
    public List<ValueComboBoxData> getCRSList() {
        populateCRSList();

        return crsDataList;
    }

    /**
     * Populate crs list.
     */
    private void populateCRSList() {

        if(crsDataList.isEmpty())
        {
            Hints hints = null;
            for (AuthorityFactory factory : ReferencingFactoryFinder.getCRSAuthorityFactories(hints)) 
            {
                String authorityCode = "";

                Citation citation = factory.getAuthority();
                if(citation != null)
                {
                    @SuppressWarnings("unchecked")
                    Collection<Identifier> identifierList = (Collection<Identifier>) citation.getIdentifiers();
                    authorityCode = identifierList.iterator().next().getCode();
                }
                Set<String> codeList;
                try {
                    codeList = factory.getAuthorityCodes(CoordinateReferenceSystem.class);

                    VendorOptionVersion vendorOptionVersion = VendorOptionManager.getInstance().getDefaultVendorOptionVersion();
                    for(String code : codeList)
                    {
                        String fullCode = String.format("%s:%s", authorityCode, code);
                        String descriptionText = factory.getDescriptionText(code).toString();
                        String text = String.format("%s - %s", fullCode, descriptionText);
                        ValueComboBoxData value = new ValueComboBoxData(fullCode, text, vendorOptionVersion);
                        crsDataList.add(value);
                        crsMap.put(fullCode, value);
                    }
                }
                catch (NoSuchAuthorityCodeException e) {
                    //       ConsoleManager.getInstance().exception(this, e);
                }
                catch (FactoryException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
        }
    }

    /**
     * Gets the CRS code.
     *
     * @param coordinateReferenceSystem the coordinate reference system
     * @return the CRS code
     */
    public String getCRSCode(CoordinateReferenceSystem coordinateReferenceSystem) {
        ReferenceIdentifier identifier = null;
        if(coordinateReferenceSystem != null)
        {
            Set<ReferenceIdentifier> indentifierList = coordinateReferenceSystem.getIdentifiers();

            if(indentifierList != null)
            {
                if(indentifierList.iterator().hasNext())
                {
                    identifier = indentifierList.iterator().next();
                }
            }
        }

        String code = "";

        if(identifier != null)
        {
            ValueComboBoxData data = crsMap.get(identifier.toString());
            if(data != null)
            {
                code = data.getKey();
            }
        }
        return code;
    }

    /**
     * Gets the WGS84 coordinate reference object.
     *
     * @return the WGS84 coordinate reference system
     */
    public CoordinateReferenceSystem getWGS84() {
        if(defaultCRS == null)
        {
            defaultCRS = getCRS(WGS84);
        }
        return defaultCRS;
    }

    /**
     * Gets the coordinate reference object for the supplied CRS code.
     *
     * @param crsCode the crs code
     * @return the coordinate reference system
     */
    public CoordinateReferenceSystem getCRS(String crsCode) {
        CoordinateReferenceSystem crs = null;

        if(crsCode != null)
        {
            try {
                crs = CRS.decode(crsCode);
            }
            catch (NoSuchAuthorityCodeException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
            catch (FactoryException e) {
                ConsoleManager.getInstance().exception(this, e);
            }
        }
        return crs;
    }
}
