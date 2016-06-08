package com.nyu.cs9033.eta.controllers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class UpdateLocationAPIAdapter extends AppCompatActivity {

    URL url;
    HttpURLConnection urlConnection = null;


    public void UpdateLocation(String lat, String lon) {

        String urlstring = "http://cs9033-homework.appspot.com/";
        new CallAPI(lat, lon).execute(urlstring);

    }

    private void response(String responseData) {
        try {
            String responce_code = getDataFromJson(responseData);// trip id
        } catch (JSONException e) {
            e.getMessage();
        }

    }

    public static String getDataFromJson(String jString) throws JSONException {

        JSONObject myjson = new JSONObject(jString);
        String response_code = myjson.get("response_code").toString();
        return response_code;
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        private String mlat;
        private String mlon;

        CallAPI(String latitude, String longitude) {
            this.mlat = latitude;
            this.mlon = longitude;
        }

        @Override
        protected String doInBackground(String... params) {

            InputStream in = null;
            int resCode = -1;
            try {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();

                if (!(urlConn instanceof HttpURLConnection)) {
                    throw new IOException("URL is not an Http URL");
                }
                HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                httpConn.setRequestProperty("Content-Type", "application/json");
                httpConn.setRequestMethod("POST");

                JSONObject parent = new JSONObject();

                try {
                    parent.put("command", "UPDATE_LOCATION");
                    parent.put("latitude", mlat);
                    parent.put("longitude", mlon);
                    parent.put("datetime", "1382591009");
                } catch (JSONException e) {
                    e.getMessage();
                }


                byte[] outputInBytes = parent.toString().getBytes("UTF-8");
                OutputStream os = httpConn.getOutputStream();
                os.write(outputInBytes);
                os.close();


                httpConn.connect();
                resCode = httpConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpConn.getInputStream();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String json = convertStreamToString(in);
            String a = json;
            return json;
        }


        public String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;

            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }

            return sb.toString();
        }

        protected void onPostExecute(String stream_url) {
            super.onPostExecute(stream_url);
            response(stream_url);
        }
    }
}
