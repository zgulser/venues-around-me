package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki
 */

public class LocationChangeListenerProxy {
    private VenuesMapActivity venuesMapActivity;
    private BroadcastReceiver locationUpdateBroadcastListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(LocationUtils.MY_LOCATION_RECEIVED)) {
                venuesMapActivity.updateMapMarkersWhenMyLocationUpdated();
            } else if (action.equals(LocationUtils.MY_LOCATION_RECEIVED_NULL)) {
                if(!venuesMapActivity.getLocationProviderProxy().isFirstLocationRequestReceived()){
                    venuesMapActivity.getLocationSettingCheckerProxy().showLocationSettingError();
                    venuesMapActivity.getLocationProviderProxy().getGoogleApiClient().connect();
                }
            } else if (action.equals(LocationUtils.MY_LOCATION_RECEIVED_FIRST_TIME)) {
                venuesMapActivity.updateViewWhenMyLocationReceivedFirstTime(); // center my pos
            }
        }
    };

    public LocationChangeListenerProxy(VenuesMapActivity venuesMapActivity){
        this.venuesMapActivity = venuesMapActivity;
    }

    public void registerLocationBroadcastReceiver() {
        LocalBroadcastManager.getInstance(venuesMapActivity).registerReceiver(locationUpdateBroadcastListener,
                new IntentFilter(LocationUtils.MY_LOCATION_RECEIVED));
        LocalBroadcastManager.getInstance(venuesMapActivity).registerReceiver(locationUpdateBroadcastListener,
                new IntentFilter(LocationUtils.MY_LOCATION_RECEIVED_NULL));
        LocalBroadcastManager.getInstance(venuesMapActivity).registerReceiver(locationUpdateBroadcastListener,
                new IntentFilter(LocationUtils.MY_LOCATION_RECEIVED_FIRST_TIME));
    }

    public void unregisterLocationBroadcastReceiver() {
        try{
            LocalBroadcastManager.getInstance(venuesMapActivity).unregisterReceiver(locationUpdateBroadcastListener);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public BroadcastReceiver getLocationUpdateBroadcastListener() {
        return locationUpdateBroadcastListener;
    }

}
