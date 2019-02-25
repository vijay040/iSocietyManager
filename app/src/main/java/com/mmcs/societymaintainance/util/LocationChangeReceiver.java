package com.mmcs.societymaintainance.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Created by VIJAY on 2/11/2017.
 */

public class LocationChangeReceiver extends BroadcastReceiver {

    public static String currentLocation;

    @Override
    public void onReceive(final Context context, Intent intent) {
        try {

            LocationManager locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // gps enabled

                Log.e("Loaction********", "Location enabled");

                MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
                    @Override
                    public void gotLocation(Location location) {
                        //Got the location!
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            currentLocation = GetAddress(latitude, longitude, context);
                            // text_location.setText(location);
                            // Intent i=new Intent(context,LandingActivity.class);
                            // i.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            // context.startActivity(i);
                            Log.e("currentLocation", ""+currentLocation);
                        } else
                            Log.e("Loaction****null****", "Location is null");

                    }
                };

                MyLocation myLocation = new MyLocation();
                myLocation.getLocation(context, locationResult);

            } else {
                // gps disabled
                Log.e("Loaction********", "Location desabled");
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }


    public String GetAddress(double latitude, double longitude, Context con) {

        Geocoder geocoder = new Geocoder(con, Locale.getDefault());
        String city = "", state = "", address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("Addrss", addresses + "");
            // latlong = new LatLng(latitude, longitude);
            address = addresses.get(0).getAddressLine(0) + " " + addresses.get(0).getAddressLine(1) + " " + addresses.get(0).getAddressLine(2);
            city = addresses.get(0).getAddressLine(1);
            state = addresses.get(0).getAdminArea();
            String zip = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();
        } catch (Exception e) {

        }
        return address;
    }
}