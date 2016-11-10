/*
 *    SLDEditor - SLD Editor application
 *
 *    (C) 2016, SCISYS
 *
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
import com.sldeditor.common.vendoroption.VersionData;

/**
 * The Class CheckUpdateGitHub.
 *
 * @author Robert Ward (SCISYS)
 */
public class CheckUpdateGitHub implements CheckUpdateClientInterface {

    private static final String DOWNLOAD_URL = "https://github.com/robward-scisys/sldeditor/releases";
    /** The Constant URL. */
    private static final String URL = "https://api.github.com/repos/robward-scisys/sldeditor/releases";

    /**
     * Instantiates a new check update git hub.
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
        StringBuilder sb = new StringBuilder();
        try
        {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(URL);
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            httpClient.getConnectionManager().shutdown();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
        try
        {
            JsonParser parser = new JsonParser();
            JsonArray o = parser.parse(jsonString).getAsJsonArray();

            Map<Calendar, String> map = new HashMap<Calendar, String>();
            Map<Calendar, JsonObject> jsonMap = new HashMap<Calendar, JsonObject>();
            List<Calendar> calList = new ArrayList<Calendar>();

            for(int index = 0; index < o.size(); index ++)
            {
                JsonObject obj = o.get(index).getAsJsonObject();

                String tagName = obj.get("tag_name").getAsString();
                if(tagName.startsWith("v"))
                {
                    tagName = tagName.substring(1);
                }
                String published = obj.get("published_at").getAsString();

                Calendar cal = ISO8601toCalendar(published);

                map.put(cal, tagName);
                jsonMap.put(cal, obj);
                calList.add(cal);
            }

            Collections.sort(calList);

            Calendar latestTime = calList.get(calList.size() - 1);
            String latest = map.get(latestTime);
            String description = jsonMap.get(latestTime).get("body").getAsString();

            VersionData latestVersion = VersionData.decode(getClass(), latest);

            UpdateData updateData = new UpdateData(latestVersion, description);

            return updateData;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
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


}
