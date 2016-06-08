package com.nyu.cs9033.eta.controllers;

/**
 * Created by Pranay on 12/3/2015.
 */

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class MyLocationListener implements LocationListener {

    @Override
    public void onLocationChanged(Location location) {

        UpdateLocationAPIAdapter updateloc = new UpdateLocationAPIAdapter();
        updateloc.UpdateLocation(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}

