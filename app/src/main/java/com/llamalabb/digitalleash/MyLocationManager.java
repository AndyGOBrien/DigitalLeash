package com.llamalabb.digitalleash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by andy on 10/2/17.
 */

public class MyLocationManager {
    
    private double mLat, mLon;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private static MyLocationManager mInstance;

    public static MyLocationManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new MyLocationManager(context);
        }
        return mInstance;
    }

    private MyLocationManager(Context context) {
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        }

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLat = location.getLatitude();
                mLon = location.getLongitude();
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
        };

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, getLocationListener());

        Location lastKnownLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//
//        mLat = lastKnownLocation.getLatitude();
//        mLon = lastKnownLocation.getLongitude();

        Log.d("MyLocationManager", mLat+ "");

    }

    public LocationListener getLocationListener(){
        return mLocationListener;
    }


    public double getLatitude() {
        return mLat;
    }

    public double getLongitude() {
        return mLon;
    }
}
