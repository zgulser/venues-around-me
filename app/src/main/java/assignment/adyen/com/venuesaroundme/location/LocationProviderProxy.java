package assignment.adyen.com.venuesaroundme.location;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;

/**
 * Created by Zeki
 */

public class LocationProviderProxy implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ILocationProvider, ILocationSettingObserver{

    private Handler delayedEventHandler;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleApiClient googleApiClient;
    private ILocationSettingChecker locationSettingChecker;
    private static LatLng myPosition;
    private boolean isFirstLocationRequestReceived;

    public final LocationCallback myLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            for(Location location : locationResult.getLocations()){
                myPosition = new LatLng(location.getLatitude(), location.getLongitude());
                if(myPosition != null) {
                    LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).sendBroadcast(new Intent(LocationUtils.MY_LOCATION_RECEIVED));
                } else {
                    LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).sendBroadcast(new Intent(LocationUtils.MY_LOCATION_RECEIVED_NULL));
                }
            }
        }
    };

    public LocationProviderProxy(ILocationSettingChecker locationSettingChecker){
        this.locationSettingChecker = locationSettingChecker;
        this.locationSettingChecker.addLocationSettingRequestObserver(this);
        this.delayedEventHandler = new Handler(Looper.getMainLooper(), null);
    }

    public void connect(){
        if(isGoogleApiConneactable()) {
            googleApiClient.connect();
        }
    }

    private boolean isGoogleApiConneactable() {
        return (googleApiClient != null ) && !(googleApiClient.isConnected() | googleApiClient.isConnecting());
    }

    public void disconnect(){
        if(isGoogleApiDisconneactable()) {
            googleApiClient.disconnect();
        }
    }

    private boolean isGoogleApiDisconneactable() {
        return (googleApiClient != null ) && (googleApiClient.isConnected() | googleApiClient.isConnecting());
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationSettingChecker.checkLocationSettingsEnabled();
    }

    private void initFusedLocationProviderClient(){
        if(fusedLocationProviderClient == null) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(FsqVenuesApplication.getAppContext());
        }
    }

    private void getMyLocationForTheFirstTime(){
        myPosition = LocationUtils.getMyCurrentLocation(FsqVenuesApplication.getAppContext(), googleApiClient);
        if(myPosition != null) {
            LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).sendBroadcast(
                    new Intent(LocationUtils.MY_LOCATION_RECEIVED_FIRST_TIME));
        } else {
            LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).sendBroadcast(
                    new Intent(LocationUtils.MY_LOCATION_RECEIVED_NULL));
        }
    }

    private void sendEventToStartLocationUpdates(){
        delayedEventHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startLocationUpdates();
            }
        }, LocationUtils.LOCATION_UPDATE_DELAY_IN_MS);
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        stopLocationUpdates();
    }

    private void startLocationUpdates(){
        try {
            if (googleApiClient.isConnected()) {
                fusedLocationProviderClient.requestLocationUpdates(LocationUtils.getLocationRequest(), myLocationCallback, null);
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        }
    }

    private void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            try {
                fusedLocationProviderClient.removeLocationUpdates(myLocationCallback);
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

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public void setGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(FsqVenuesApplication.getAppContext())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onLocationSettingsRequestSuccess() {
        if (!isFirstLocationRequestReceived){
            initFusedLocationProviderClient();
            getMyLocationForTheFirstTime();
            sendEventToStartLocationUpdates();
        }
    }

    @Override
    public void onLocationSettingsRequestFailed(int statusCode) {
        switch (statusCode) {
            case CommonStatusCodes.RESOLUTION_REQUIRED:
                locationSettingChecker.showLocationSettingError();
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // TODO: Show something more grave
                break;
        }
    }

    public static LatLng getMyPosition() {
        return myPosition;
    }

    public boolean isFirstLocationRequestReceived() {
        return isFirstLocationRequestReceived;
    }

    public void setFirstLocationRequestReceived(boolean firstLocationRequestReceived) {
        isFirstLocationRequestReceived = firstLocationRequestReceived;
    }

}
