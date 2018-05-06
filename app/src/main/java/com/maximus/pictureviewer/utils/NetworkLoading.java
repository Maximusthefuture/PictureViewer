package com.maximus.pictureviewer.utils;


import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maximus.pictureviewer.model.PictureItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkLoading {

    private static final String TAG = NetworkLoading.class.getSimpleName();
    private static final String API_KEY = "433bad267bfdf673b8d331d91715c245";


    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            InputStream inputStream = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage()
                 + ": " + urlSpec);
            }

            int byteRead = 0;
            byte[] buffer = new byte[1024];

            while ((byteRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, byteRead);
            }
            outputStream.close();
            return outputStream.toByteArray();
        } finally {
            connection.disconnect();
        }
    }


    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<PictureItem> fetchItems() {
        List<PictureItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseWithGson(items, jsonBody);
            Log.i(TAG, "JSON body : " + jsonString);
        } catch (IOException ex) {
            Log.e(TAG, "Failed", ex);
        } catch (JSONException ex) {
            Log.e(TAG, "Error with parsing JSON: ", ex);

        }

        return items;
    }

    private void parseWithGson(List<PictureItem> items, JSONObject jsonBody)
                                         throws JSONException, IOException {
       JSONObject photosJSonObject = jsonBody.getJSONObject("photos");
       JSONArray photoJsonArray = photosJSonObject.getJSONArray("photo");

       String jsonArray = photoJsonArray.toString();
        //GSON need this
       Type galleryType = new TypeToken<ArrayList<PictureItem>>() {
       }.getType();

       List<PictureItem> pictureItems = new Gson().fromJson(jsonArray, galleryType);

       items.addAll(pictureItems);

    }
}
