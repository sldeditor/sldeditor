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
package com.sldeditor.test.unit.importdata.esri;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sldeditor.common.xml.ParseXML;

/**
 * @author Robert Ward (SCISYS)
 *
 */
public class ParserUtils {

    /**
     * Read test data.
     *
     * @param testFilename the test filename
     * @return the json object
     */
    public static JsonElement readTestData(String testFilename) {
        InputStream inputStream = ParseXML.class.getResourceAsStream(testFilename); 
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String read;

        try {
            while((read = br.readLine()) != null) {
                sb.append(read);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(sb.toString());

        return jsonElement;
    }
}
