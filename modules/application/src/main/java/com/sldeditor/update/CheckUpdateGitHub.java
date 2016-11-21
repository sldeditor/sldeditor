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

package com.sldeditor.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sldeditor.common.console.ConsoleManager;
import com.sldeditor.common.localisation.Localisation;
import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Class CheckUpdateGitHub.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdateGitHub implements CheckUpdateClientInterface {

    /** The Constant TAG_PREFIX. */
    private static final String TAG_PREFIX = "v";

    /** The Constant BODY. */
    private static final String BODY = "body";

    /** The Constant PUBLISHED_AT. */
    private static final String PUBLISHED_AT = "published_at";

    /** The Constant TAG_NAME. */
    private static final String TAG_NAME = "tag_name";

    /** The Constant USER. */
    private static final String USER = "robward-scisys";

    /** The Constant REPO. */
    private static final String REPO = "sldeditor";

    /** The Constant DOWNLOAD_URL. */
    private static final String DOWNLOAD_URL = String.format("https://github.com/%s/%s/releases", USER, REPO);

    /** The Constant URL. */
    private static final String URL = String.format("https://api.github.com/repos/%s/%s/releases", USER, REPO);

    /** The destination reached flag. */
    private boolean destinationReached = false;

    /**
     * Instantiates a new check update github.
     */
    public CheckUpdateGitHub()
    {
    }

    /* (non-Javadoc)
     * @see com.sldeditor.update.CheckUpdateClientInterface#getLatest()
     */
    @Override
    public UpdateData getLatest()
    {
        String json = readData();

        return check(json);
    }

    /**
     * Read data.
     *
     * @return the string
     */
    private String readData()
    {
        return readDataFromURL(URL);
    }

    /**
     * Read data from URL.
     *
     * @param url the url
     * @return the string
     */
    protected String readDataFromURL(String url)
    {
        destinationReached = false;

        StringBuilder sb = new StringBuilder();
        if(url != null)
        {
            try
            {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet getRequest = new HttpGet(url);
                getRequest.addHeader("accept", "application/json");

                HttpResponse response = httpClient.execute(getRequest);

                if (response.getStatusLine().getStatusCode() != 200) {
                    ConsoleManager.getInstance().error(this,
                            String.format("%s %s",
                                    Localisation.getField(CheckUpdatePanel.class, "CheckUpdateGitHub.httpError"),
                                    response.getStatusLine().getStatusCode()));
                }
                else
                {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader((response.getEntity().getContent())));

                    String output;
                    while ((output = br.readLine()) != null) {
                        sb.append(output);
                    }

                    destinationReached = true;
                }

                httpClient.getConnectionManager().shutdown();

            } catch (ClientProtocolException e) {
                ConsoleManager.getInstance().exception(this, e);
            } catch (IOException e) {
                ConsoleManager.getInstance().error(this, 
                        Localisation.getString(CheckUpdatePanel.class, "CheckUpdatePanel.destinationUnreachable"));
            }
        }

        return sb.toString();
    }

    /**
     * Check.
     *
     * @param jsonString the json string
     * @return the update data
     */
    protected UpdateData check(String jsonString) {
        if(jsonString == null)
        {
            return null;
        }

        if(jsonString.isEmpty())
        {
            return null;
        }

        try
        {
            JsonParser parser = new JsonParser();
            JsonArray o = parser.parse(jsonString).getAsJsonArray();

            Map<Calendar, String> map = new HashMap<Calendar, String>();
            Map<Calendar, JsonObject> jsonMap = new HashMap<Calendar, JsonObject>();
            Map<String, String> descriptionMap = new HashMap<String, String>();
            List<Calendar> calList = new ArrayList<Calendar>();

            for(int index = 0; index < o.size(); index ++)
            {
                JsonObject obj = o.get(index).getAsJsonObject();

                String tagName = obj.get(TAG_NAME).getAsString();
                if(tagName.startsWith(TAG_PREFIX))
                {
                    tagName = tagName.substring(1);
                }
                String published = obj.get(PUBLISHED_AT).getAsString();

                Calendar cal = ISO8601toCalendar(published);

                map.put(cal, tagName);
                jsonMap.put(cal, obj);
                calList.add(cal);
                descriptionMap.put(tagName, obj.get(BODY).getAsString());
            }

            Collections.sort(calList, Collections.reverseOrder());

            Calendar latestTime = calList.get(0);
            String latest = map.get(latestTime);
            StringBuilder description = new StringBuilder();

            for(Calendar time : calList)
            {
                String tag = map.get(time);
                formatDescription(description, tag, descriptionMap.get(tag));
            }

            VersionData latestVersion = VersionData.decode(getClass(), latest);

            UpdateData updateData = new UpdateData(latestVersion, description.toString());

            return updateData;
        } catch (ParseException e) {
            ConsoleManager.getInstance().exception(this, e);
        } catch (IllegalStateException e) {
            ConsoleManager.getInstance().exception(this, e);
        }

        return null;
    }

    /**
     * Format description.
     *
     * @param stringBuilder the string builder
     * @param tag the tag
     * @param description the description
     */
    private void formatDescription(StringBuilder stringBuilder, String tag, String description) {
        if(stringBuilder == null)
        {
            return;
        }
        stringBuilder.append(String.format("<h2>Version : %s</h2>",tag));
        stringBuilder.append(description.replace("\r\n","<br>"));
    }

    /**
     *  Transform ISO 8601 string to Calendar.
     *
     * @param iso8601string the iso 8601 string
     * @return the calendar
     * @throws ParseException the parse exception
     */
    private static Calendar ISO8601toCalendar(final String iso8601string)
            throws ParseException {
        Calendar calendar = GregorianCalendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);  // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s);
        calendar.setTime(date);

        return calendar;
    }

    /* (non-Javadoc)
     * @see com.sldeditor.update.CheckUpdateClientInterface#getDownloadURL()
     */
    @Override
    public URL getDownloadURL() {
        URL url = null;
        try {
            url = new URL(DOWNLOAD_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * Checks if is destination reached.
     *
     * @return the destinationReached flag
     */
    @Override
    public boolean isDestinationReached() {
        return destinationReached;
    }

}
