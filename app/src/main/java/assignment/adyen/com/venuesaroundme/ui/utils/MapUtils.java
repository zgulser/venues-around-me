package assignment.adyen.com.venuesaroundme.ui.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.SphericalUtil;

import java.util.Map;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki on 27/06/2017.
 */

public class MapUtils {

    private static Circle radiusCircle;

    public static void putCameraToPosition(boolean zoom, GoogleMap googleMap, LatLng locationPoint, GoogleMap.CancelableCallback callback) {
        if (zoom) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationPoint, 12.0F), 5000, callback);
        } else{
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(locationPoint), callback);
        }
    }

    public static void onMyLocationButtonClicked(LocationProviderProxy locationProviderProxy, GoogleMap venuesMap){
        if(LocationProviderProxy.getMyPosition() != null){
            if(!MapUtils.isMyLocationVisible(venuesMap)) {
                venuesMap.moveCamera(CameraUpdateFactory.newLatLng(LocationProviderProxy.getMyPosition()));
            }
        } else {
            locationProviderProxy.getMyLocationForTheFirstTime();
        }
    }

    private static boolean isMyLocationVisible(GoogleMap branchesMap){
        LatLngBounds mapBounds = branchesMap.getProjection().getVisibleRegion().latLngBounds;
        return mapBounds.contains(LocationProviderProxy.getMyPosition());
    }

    public static void setRadiusCircle(VenuesMapActivity venuesMapActivity, GoogleMap venuesMap) {
        if(radiusCircle == null){
            radiusCircle = venuesMap.addCircle(new CircleOptions()
                    .center(new LatLng(venuesMapActivity.getLocationProviderProxy().getMyPosition().latitude,
                            venuesMapActivity.getLocationProviderProxy().getMyPosition().longitude))
                    .radius(LocationUtils.surroundingRadius)
                    .strokeWidth(3)
                    .strokeColor(venuesMapActivity.getResources().getColor(R.color.colorBorderImageWithAlpha))
                    .fillColor(venuesMapActivity.getResources().getColor(R.color.colorMapBgWithAlpha)));
        }
    }

    public static void updateSurroundingCircleRadius(int surroundingRadius){
        if(radiusCircle != null){
            radiusCircle.setRadius(surroundingRadius);
        }
    }

    public static void updateSurroundingCircleByMyLocation(VenuesMapActivity venuesMapActivity){
        if(radiusCircle != null){
            radiusCircle.setCenter(new LatLng(venuesMapActivity.getLocationProviderProxy().getMyPosition().latitude,
                    venuesMapActivity.getLocationProviderProxy().getMyPosition().longitude));
        }
    }

    public static void clearVenuesOnMap(GoogleMap map) {
        map.clear();
    }

    public static int getDistanceBetween(LatLng start, LatLng dest) {
        float[] results = new float[3];
        Location.distanceBetween(start.latitude, start.longitude, dest.latitude, dest.longitude, results);
        return (int)results[0];
    }
}
