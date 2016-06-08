package com.nyu.cs9033.eta.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nyu.cs9033.eta.R;
import com.nyu.cs9033.eta.models.Trip;

import java.security.Provider;

/**
 * Created by Pranay on 11/8/2015
 * Activity to view the details of the trip.
 */
public class ViewTripDetails extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_details);

        final String tripID = getIntent().getExtras().getString("Trip ID");
        TripStatusAPIAdapter status = new TripStatusAPIAdapter();
        TextView tripDetails = (TextView) findViewById(R.id.textDetails);
        status.getTripStatus(tripID, tripDetails); //Display the details of the trip in the textview
    }
}
