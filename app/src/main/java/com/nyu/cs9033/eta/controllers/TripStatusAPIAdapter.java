package com.nyu.cs9033.eta.controllers;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
import java.util.ArrayList;

public class TripStatusAPIAdapter extends AppCompatActivity {

    URL url;
    HttpURLConnection urlConnection = null;

    public void getTripStatus(String tripID, TextView textview) {

        //String urlstring = "http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx";
        String urlstring = "http://cs9033-homework.appspot.com/";
        //String urlstring = "http://api.bestbuy.com/v1/products(longDescription="+keyword+"*%7Csku=7619002)?show=sku,name,customerReviewAverage,salePrice&pageSize=15&page=5&apiKey=6ru583b35stg5q4mzr23nntx&format=json";
        //http://api.bestbuy.com/v1/products(longDescription=iphone)?show=sku,name&pageSize=15&page=1&apiKey=6ru583b35stg5q4mzr23nntx&format=json
        new CallAPI(tripID, textview).execute(urlstring);

    }
    //"http://api.bestbuy.com/beta/products/mostViewed?apiKey=6ru583b35stg5q4mzr23nntx");

    private void response(String responseData, TextView tripInfoView) {
        ArrayList<String> tripInfoArrayList = new ArrayList();
        try {
            tripInfoArrayList = getDataFromJson(responseData);// trip info Array
            tripInfoView.setText("");
            for (int i = 0; i < tripInfoArrayList.size(); i++) {
                tripInfoView.append(tripInfoArrayList.get(i).toString());
            }
        } catch (JSONException e) {
            tripInfoView.setText(e.getMessage());// set Trip Error
        }

    }

    public static ArrayList getDataFromJson(String jString) throws JSONException {

        JSONObject statusJson = new JSONObject(jString);

        String String_distnace = statusJson.getString("distance_left");
        JSONArray distanceArray = new JSONArray(String_distnace);
        //String a = distanceArray;


        String String_timeLeft = statusJson.getString("time_left");
        JSONArray timeLeftArray = new JSONArray(String_timeLeft);
        //String b = timeLeftArray.toString();

        String String_people = statusJson.getString("people");
        JSONArray PeopleArray = new JSONArray(String_people);
        //String c = PeopleArray.get(1).toString();

        ArrayList<String> tripInfoArray = new ArrayList<>();
        for (int i = 0; i < PeopleArray.length(); i++) {
            if(distanceArray.get(i).toString() != "0")
            tripInfoArray.add("Name: " + PeopleArray.get(i).toString() + "\n" + "ETA: " + timeLeftArray.get(i).toString() + "minutes \n" + "Distance: " + distanceArray.get(i).toString() + "miles \n" + "\n");
            else
            tripInfoArray.add("Name: " + PeopleArray.get(i).toString() + "\nReached Destination");
        }
        return tripInfoArray;
    }

    private class CallAPI extends AsyncTask<String, String, String> {

        private TextView mtextview;
        private String mtripID;

        CallAPI(String tripID, TextView textview) {
            mtextview = textview;
            mtripID = tripID;
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
                    parent.put("command", "TRIP_STATUS");
                    parent.put("trip_id", mtripID);
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
            response(stream_url, mtextview);
        }
    }
}