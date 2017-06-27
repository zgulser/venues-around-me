package assignment.adyen.com.venuesaroundme.location;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Zeki on 25/06/2017.
 */

public class LocationUtils {

    public static final String BOTTOM_SHEET_STATE_CHANGED = "adyen.com.venuesaroundme.BOTTOM_SHEET_STATE_CHANGED";
    public static final String MY_LOCATION_RECEIVED = "adyen.com.venuesaroundme.MY_LOCATION_RECEIVED";
    public static final String MY_LOCATION_RECEIVED_NULL = "adyen.com.venuesaroundme.MY_LOCATION_RECEIVED_NULL";
    public static final String MY_LOCATION_RECEIVED_FIRST_TIME = "adyen.com.venuesaroundme.MY_LOCATION_RECEIVED_FIRST_TIME";
    public static final String LOCATION_SETTINGS_ALREADY_DISABLED = "adyen.com.venuesaroundme.LOCATION_SETTINGS_ALREADY_DISABLED";
    public static final String LOCATION_SETTINGS_NEEDS_TO_BE_CHECKED = "adyen.com.venuesaroundme.LOCATION_SETTINGS_NEEDS_TO_BE_CHECKED";
    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 30000;
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL_IN_MS = 20000;
    public static final int LOCATION_UPDATE_DELAY_IN_MS = 10000;
    public static int REQUEST_CHECK_SETTINGS = 1;
    public static LocationRequest locationRequest;
    public static int radius = -1;

    /**
     *
     * Formatted query param for the API request
     *
     * @param latitute
     * @param longitude
     * @return lat,lon
     */
    public static String getFormattedLatitudeAndLongitude(double latitute, double longitude) {
        String formatted = String.valueOf(latitute) + ',' + String.valueOf(longitude);
        return formatted;
    }

    public static LocationRequest getLocationRequest() {
        if(locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(LocationUtils.LOCATION_UPDATE_INTERVAL_IN_MS);
            locationRequest.setFastestInterval(LocationUtils.LOCATION_UPDATE_FASTEST_INTERVAL_IN_MS);
            locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
        return locationRequest;
    }

    public static LatLng getMyCurrentLocation(Context context, GoogleApiClient googleApiClient) {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if(location != null){
            return new LatLng(location.getLatitude(), location.getLongitude());
        }
        return null;
    }

    public static String getFormattedDistance(int distance) {
        if (distance > 1000){
            int km = distance / 1000;
            int meters = distance % 1000;
            return String.valueOf(km) + '.' + String.valueOf(meters).charAt(0) + "km";
        } else {
            return String.valueOf(distance) + 'm';
        }
    }
}
