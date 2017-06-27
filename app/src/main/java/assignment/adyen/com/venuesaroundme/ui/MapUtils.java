package assignment.adyen.com.venuesaroundme.ui;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;

/**
 * Created by Zeki
 */

public class MapUtils {
    public static final String BOTTOM_SHEET_STATE_CHANGED = "com.zeki.BOTTOM_SHEET_STATE_CHANGED";

    public static enum SearchAction {
        SEARCH_ACTION_OPEN,
        SEARCH_ACTION_CLOSED,
        SEARCH_ACTION_REQUESTED
    }

    public static void runAlphaAnimationOnFAB(final View fab, boolean opaque){
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(fab, View.ALPHA, (opaque == true ? 0 : 1), (opaque == true ? 1 : 0)).setDuration(300);
        alphaAnim.start();
    }

    public static LatLngBounds getBoundsOfMultiplePoints(){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (FsqExploredVenue venue : FsqVenueContainer.getInstance().getFsqVenueList()) {
            builder.include(new LatLng(venue.getLocation().getLatitude(),
                    venue.getLocation().getLongitude()));
        }
        return builder.build();
    }

    public static void putCameraToPosition(boolean zoom, GoogleMap googleMap, LatLng locationPoint, GoogleMap.CancelableCallback callback) {
        if (zoom) {
            //googleMap.moveCamera(CameraUpdateFactory.newLatLng(locationPoint));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationPoint, 12.0F), 5000, callback);
        } else{
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(locationPoint), callback);
        }
    }

    public static boolean isMyLocationVisible(LatLng myPosition, GoogleMap branchesMap){
        LatLngBounds mapBounds = branchesMap.getProjection().getVisibleRegion().latLngBounds;
        return mapBounds.contains(myPosition);
    }

    public static void loadImageToImageView(Activity activity, View fab, int drawableRes){}

    public static void refreshMapWithBranches(Context context){
        getBranchesToMap(context);
    }

    public static void refreshMapWithBranchesByArea(Context context, GoogleMap branchesMap){
        LatLngBounds mapBounds = branchesMap.getProjection().getVisibleRegion().latLngBounds;
        getBranchesToMapByArea(context, mapBounds);
    }

    public static void getBranchesToMap(Context context) {
        //BranchUtils.retrieveBranches(context);
    }

    public static void getBranchesToMapByArea(Context context, LatLngBounds mapBounds){
        //BranchUtils.retrieveBranchesByArea(context, mapBounds);
    }

    public static void clearBranchesOnMap(GoogleMap map) {
        map.clear();
    }
}
