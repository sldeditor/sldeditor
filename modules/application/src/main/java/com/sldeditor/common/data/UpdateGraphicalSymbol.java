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

package com.sldeditor.common.data;

import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.ui.detail.config.symboltype.externalgraphic.RelativePath;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.styling.ExternalGraphic;
import org.geotools.styling.ExternalGraphicImpl;
import org.opengis.style.GraphicalSymbol;

/**
 * The Class UpdateGraphicalSymbol.
 *
 * @author Robert Ward (SCISYS)
 */
public class UpdateGraphicalSymbol implements ProcessGraphicSymbolInterface {

    /*
     * (non-Javadoc)
     *
     * @see com.sldeditor.common.data.ProcessGraphicSymbolInterface#processGraphicalSymbol(java.net.URL, java.util.List, java.util.List)
     */
    @Override
    public void processGraphicalSymbol(
            URL resourceLocator,
            List<GraphicalSymbol> graphicalSymbolList,
            List<String> externalImageList) {
        for (GraphicalSymbol symbol : graphicalSymbolList) {
            if (symbol instanceof ExternalGraphic) {
                ExternalGraphicImpl externalGraphic = (ExternalGraphicImpl) symbol;
                OnLineResourceImpl onlineResource =
                        (OnLineResourceImpl) externalGraphic.getOnlineResource();

                String currentValue = null;
                URL currentValueURL = null;
                try {
                    currentValueURL = onlineResource.getLinkage().toURL();
                    currentValue = currentValueURL.toExternalForm();
                } catch (MalformedURLException e) {
                    ConsoleManager.getInstance().exception(SLDExternalImages.class, e);
                }

                if (currentValueURL != null) {
                    if ((resourceLocator == null) || RelativePath.hasHost(currentValueURL)) {
                        // Just report back the external image
                        URI uri = null;
                        try {
                            uri = new URI(currentValue);
                            externalImageList.add(uri.toASCIIString());
                        } catch (URISyntaxException e) {
                            ConsoleManager.getInstance().exception(SLDExternalImages.class, e);
                        }
                    } else {
                        try {
                            File file = new File(currentValueURL.getFile());
                            File folder = new File(resourceLocator.getFile());
                            currentValue = RelativePath.getRelativePath(file, folder);

                            // If the backslashes are not converted to forward slashes
                            // creating the URI does not work
                            currentValue = currentValue.replace('\\', '/');
                            OnLineResourceImpl updatedOnlineResource = new OnLineResourceImpl();
                            URI uri = new URI(currentValue);
                            updatedOnlineResource.setLinkage(uri);
                            externalGraphic.setOnlineResource(updatedOnlineResource);

                            externalGraphic.setURI(uri.toASCIIString());
                            externalImageList.add(uri.toASCIIString());
                        } catch (URISyntaxException e) {
                            ConsoleManager.getInstance().exception(SLDExternalImages.class, e);
                        }
                    }
                }
            }
        }
    }
}
