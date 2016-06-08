package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nyu.cs9033.eta.R;

public class MainActivity extends Activity {

    public static final String TAG = "MainActivity";
    Button create;
    Button view;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new MyLocationListener();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }*/

        //String tripId = ((CurrentActiveTripID)this.getApplication()).getCurrentActiveTripID();
        //  TextView CurrentTripStatus = (TextView) findViewById(R.id.tripStatus);
        //CurrentTripStatus.setText(tripId);
        //CurrentActiveTripID activeTrip = new CurrentActiveTripID();
        //CurrentTripStatus.setText(activeTrip.getCurrentActiveTripID());
        //if(!tripId.matches("")) {
        //}
        create = (Button) findViewById(R.id.buttonCreate);
        view = (Button) findViewById(R.id.buttonView);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreateTripActivity(view);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startViewTripActivity();
            }
        });
        // TODO - fill in here
    }

    /**
     * This method should start the
     * Activity responsible for creating
     * a Trip.
     */
    public void startCreateTripActivity(View v) {
        Intent createActivityIntent = new Intent(v.getContext(), CreateTripActivity.class);
        startActivity(createActivityIntent);
        // TODO - fill in here
    }

    /**
     * This method should start the
     * Activity responsible for viewing
     * a Trip.
     */
    public void startViewTripActivity() {
        Intent viewActivityIntent = new Intent(view.getContext(), ViewTripActivity.class);
        startActivity(viewActivityIntent);
        // TODO - fill in here
    }

}

