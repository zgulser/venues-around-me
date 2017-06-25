package assignment.adyen.com.venuesaroundme.location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Zeki on 25/06/2017.
 */

public class LocationProviderProxy implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ILocationProvider{

    private boolean googleApiClientConnected;
    private Handler delayedHandler;
    public GoogleApiClient googleApiClient;
    public LatLng myPosition;

    public final LocationListener myLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            myPosition = new LatLng(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    public LocationProviderProxy(Activity context){
        this.delayedHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                return false;
            }
        });
    }

    public LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(LocationUtils.LOCATION_UPDATE_INTERVAL_IN_MS);
        locationRequest.setFastestInterval(LocationUtils.LOCATION_UPDATE_FASTEST_INTERVAL_IN_MS);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        googleApiClientConnected = true;
        getMyLocationForTheFirstTime();

        delayedHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initRequestLocationUpdateReceiver();
            }
        }, LocationUtils.LOCATION_UPDATE_DELAY_IN_MS);
    }

    private void getMyLocationForTheFirstTime(){
        myPosition = LocationUtils.getMyCurrentLocation(context, googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClientConnected = false;
        unregisterMyLocationUpdateReceiver();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googleApiClientConnected = false;
        unregisterMyLocationUpdateReceiver();
    }

    private void initRequestLocationUpdateReceiver(){
        try {
            if (googleApiClient.isConnected()) {
                //LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, createLocationRequest(), myLocationListener);
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    private void unregisterMyLocationUpdateReceiver() {
        if (googleApiClient.isConnected()) {
            try {
                //LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, myLocationListener);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public double[] getMyLatitudeAndLongitude() {
        double[] positionArray = new double[2];
        positionArray[0] = myPosition.latitude;
        positionArray[1] = myPosition.longitude;
        return positionArray;
    }
}
