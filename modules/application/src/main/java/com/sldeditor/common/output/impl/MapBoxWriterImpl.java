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

package com.sldeditor.common.output.impl;

import com.sldeditor.common.SLDDataInterface;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.output.SLDOutputFormatEnum;
import com.sldeditor.common.output.SLDWriterInterface;
import com.sldeditor.datasource.SLDEditorFile;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Stream;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Class that converts an SLD stored as a StyledLayerDescriptor to a MapBox formatted string.
 *
 * @author Robert Ward (SCISYS)
 */
public class MapBoxWriterImpl implements SLDWriterInterface {

    /** The cached string. */
    private String cachedString = null;

    /** The cached file. */
    private File cachedFile = null;

    /** Default constructor. */
    public MapBoxWriterImpl() {}

    /**
     * Encode sld to a string.
     *
     * @param resourceLocator the resource locator
     * @param sld the sld
     * @return the MppBox string
     */
    @Override
    public String encodeSLD(URL resourceLocator, StyledLayerDescriptor sld) {

        SLDDataInterface sldData = SLDEditorFile.getInstance().getSLDData();
        if (sldData.getOriginalFormat() == SLDOutputFormatEnum.MAPBOX) {
            File f = sldData.getSLDFile();
            if ((cachedFile == null) || !cachedFile.equals(f)) {
                cachedFile = f;

                try {
                    StringBuilder data = new StringBuilder();
                    Stream<String> lines = Files.lines(f.toPath());
                    lines.forEach(line -> data.append(line).append("\n"));
                    lines.close();

                    cachedString = data.toString();
                } catch (IOException e) {
                    ConsoleManager.getInstance().exception(this, e);
                }
            }
            return cachedString;
        }

        return Localisation.getString(MapBoxWriterImpl.class, "MapBoxWriterImpl.notSupported");
    }
}
