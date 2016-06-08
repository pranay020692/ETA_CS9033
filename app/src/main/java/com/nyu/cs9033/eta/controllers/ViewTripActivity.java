package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyu.cs9033.eta.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ViewTripActivity extends Activity {

    private static final String TAG = "ViewTripActivity";
    CreateTripActivity t = new CreateTripActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        //Read the Database where TripID information is stored
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases/MyDB";
            File f = new File(destPath);
            if (!f.exists()) {
                t.CopyDB(getBaseContext().getAssets().open("mydb"),
                        new FileOutputStream(destPath));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO - fill in here
        viewAllTrips();
    }


    //Reads all the Trips from the database
    public void viewAllTrips() {
        DBAdapter db = new DBAdapter(this);
        db.open();
        Cursor c = db.getAllTrips();
        if (c.moveToFirst()) {
            do {
                DisplayTrip(c);
            } while (c.moveToNext());
        }
        db.close();
    }

    /* Displays the each trip in a seperate textview added in a list view.
        Text view waits for click to redirect to the details which are fetched from the webserver
    */
    public void DisplayTrip(Cursor c) {
        final int tripNo = c.getPosition();
        final String tripID = c.getString(1);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        TextView textView = new TextView(this);
        textView.setText("Trip Number : " + tripNo + " TRIP ID: " + tripID);
        textView.setId(tripNo);
        textView.setOnClickListener(new View.OnClickListener() {

            public void onClick(final View arg) {
                AlertDialog alertDialog = new AlertDialog.Builder(arg.getContext()).create();
                alertDialog.setTitle(tripID);
                alertDialog.setMessage("Select Action:-");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "View trip Info",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String atripID = tripID;
                                Intent detailsIntent = new Intent(arg.getContext(), ViewTripDetails.class);
                                detailsIntent.putExtra("Trip ID", atripID);
                                startActivity(detailsIntent);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Start Trip!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(arg.getContext(), "Trip Started!", Toast.LENGTH_SHORT).show();
                                TripStatusAPIAdapter status = new TripStatusAPIAdapter();
                                TextView tripDetails = (TextView) findViewById(R.id.textViewtrip);
                                status.getTripStatus(tripID, tripDetails);

                                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                // Define the criteria how to select the location provider
                                Criteria criteria = new Criteria();
                                criteria.setAccuracy(Criteria.ACCURACY_COARSE);    //default


                                criteria.setCostAllowed(false);

                                // get the best provider depending on the criteria
                                String provider = locationManager.getBestProvider(criteria, false);

                                // the last known location of this provider
                                Location location = locationManager.getLastKnownLocation(provider);

                                LocationListener mylistener = new MyLocationListener();

                                if (location != null) {
                                    mylistener.onLocationChanged(location);
                                } else {
                                    // leads to the settings because there is no last known location
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    startActivity(intent);
                                }
                                // location updates: at least 5 meter and 5000millsecs change
                                locationManager.requestLocationUpdates(provider, 5000, 5, mylistener);

                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        linearLayout.addView(textView);
    }
}
