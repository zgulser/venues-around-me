package assignment.adyen.com.venuesaroundme;

/**
 * Created by Backbase R&D B.V on 30/06/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertTrue;

import android.app.Instrumentation;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;

/**
 * Created by Zeki on 29/06/2017.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
public class LocationProviderTest implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private final CountDownLatch signalForTestDisconnectability = new CountDownLatch(1);
    private final CountDownLatch signalForTestLocationUpdates = new CountDownLatch(1);

    private Context appContext;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private boolean isListeningLocationUpdates;
    public final LocationCallback myLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            isListeningLocationUpdates = true;
            signalForTestLocationUpdates.countDown();
        }
    };

    @Before
    public void setup(){
        appContext = InstrumentationRegistry.getTargetContext();
        createGoogleApiClient();
    }

    private void createGoogleApiClient(){
        googleApiClient =  new GoogleApiClient.Builder(appContext)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Test
    public void test1IsGoogleApiConnectable() {
        assertTrue((googleApiClient != null ) && !(googleApiClient.isConnected() | googleApiClient.isConnecting()));
    }

    @Test
    public void test2IsGoogleApiDisconneactable() throws InterruptedException {
        googleApiClient.connect();
        signalForTestDisconnectability.await();
        assertTrue((googleApiClient != null ) && (googleApiClient.isConnected() | googleApiClient.isConnecting()));
    }

    @Test
    public void test3StartLocationUpdates(){
        try {
            if (googleApiClient.isConnected()) {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext);
                fusedLocationProviderClient.requestLocationUpdates(LocationUtils.getLocationRequest(), myLocationCallback, null);
                signalForTestLocationUpdates.await();
                assertTrue(isListeningLocationUpdates);
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4StopLocationUpdates(){
        try {
            Log.i("Test", "Google API connected 2: " + googleApiClient.isConnected());
            if (googleApiClient.isConnected()) {
                fusedLocationProviderClient.removeLocationUpdates(myLocationCallback);
                signalForTestLocationUpdates.await();
                assertTrue(isListeningLocationUpdates);
            }
        } catch (SecurityException se) {
            se.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        signalForTestDisconnectability.countDown();
        Log.i("Test", "Google API connected: " + googleApiClient.isConnected());
    }

    @Override
    public void onConnectionSuspended(int i) {
        signalForTestDisconnectability.countDown();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        signalForTestDisconnectability.countDown();
    }
}

