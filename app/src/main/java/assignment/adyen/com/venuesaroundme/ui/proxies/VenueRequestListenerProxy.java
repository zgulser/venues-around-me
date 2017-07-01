package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki
 */

public class VenueRequestListenerProxy {

    private VenuesMapActivity venuesMapActivity;
    private BroadcastReceiver venueRequestBroadcastListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionReceived = intent.getAction();
            if(actionReceived.equals(NetworkingUtils.REQUEST_SUCCEDED_BROADCAST)){
                venuesMapActivity.addVenuesToMap();
                venuesMapActivity.refreshVenueAdapter();
            } else if(actionReceived.equals(NetworkingUtils.REQUEST_FILTERED_BROADCAST)){
                venuesMapActivity.clearAndAddVenuesToMap();
            } else if(actionReceived.equals(NetworkingUtils.REQUEST_FAILED_BROADCAST)){
                Snackbar.make(venuesMapActivity.getWindow().getDecorView().getRootView(),
                        venuesMapActivity.getString(R.string.venue_load_error), Snackbar.LENGTH_SHORT).show();
            }
        }
    };

    public VenueRequestListenerProxy(VenuesMapActivity venuesMapActivity){
        this.venuesMapActivity = venuesMapActivity;
    }

    public void registerBroadcastReceiverProxy() {
        LocalBroadcastManager.getInstance(venuesMapActivity).registerReceiver(
                venueRequestBroadcastListener, new IntentFilter(NetworkingUtils.REQUEST_SUCCEDED_BROADCAST));
        LocalBroadcastManager.getInstance(venuesMapActivity).registerReceiver(
                venueRequestBroadcastListener, new IntentFilter(NetworkingUtils.REQUEST_FILTERED_BROADCAST));
        LocalBroadcastManager.getInstance(venuesMapActivity).registerReceiver(
                venueRequestBroadcastListener, new IntentFilter(NetworkingUtils.REQUEST_FAILED_BROADCAST));
    }

    public void unregisterBroadcastReceiverProxy() {
        LocalBroadcastManager.getInstance(venuesMapActivity).unregisterReceiver(venueRequestBroadcastListener);
    }

    public BroadcastReceiver getVenueRequestBroadcastListener() {
        return venueRequestBroadcastListener;
    }

}
