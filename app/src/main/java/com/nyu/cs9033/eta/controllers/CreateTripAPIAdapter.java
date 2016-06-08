package com.nyu.cs9033.eta.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
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

public class CreateTripAPIAdapter extends AppCompatActivity {

    URL url;
    HttpURLConnection urlConnection = null;


    public void callCreateTrip(Pair Pair, Context context) {

        String urlstring = "http://cs9033-homework.appspot.com/";
        new CallAPI(Pair, context).execute(urlstring);

    }

    private void response(String responseData, Context context) {
        try {
            String tripid = getDataFromJson(responseData);// trip id

            CreateTripActivity c = new CreateTripActivity();
            DBAdapter db = new DBAdapter(context);
            db.open();
            db.insertTrip(tripid, "kota");
            db.close();
        } catch (JSONException e) {
        }

    }

    public static String getDataFromJson(String jString) throws JSONException {

        JSONObject myjson = new JSONObject(jString);
        String tripid = myjson.get("trip_id").toString();
        String response_code = myjson.get("response_code").toString();
        return tripid;

    }

    private class CallAPI extends AsyncTask<String, String, String> {

        private Pair mtripPair;
        private Context mcontext;


        public CallAPI(Pair tripPair, Context context) {
            mtripPair = tripPair;
            mcontext = context;
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
                JSONObject main = new JSONObject();
                JSONArray locnames = new JSONArray();
                JSONArray peoplenames = new JSONArray();

                locnames.put("location name");
                locnames.put("address");
                locnames.put("latitude");
                locnames.put("longitude");
                peoplenames.put(mtripPair.first.toString());


                try {
                    main.put("command", "CREATE_TRIP");
                    main.put("location", locnames);
                    main.put("datetime", "1382584629");
                    main.put("people", peoplenames);
                } catch (JSONException e) {
                    e.getMessage();
                }

                byte[] outputInBytes = main.toString().getBytes("UTF-8");
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
            response(stream_url, mcontext);
        }
    }
}
