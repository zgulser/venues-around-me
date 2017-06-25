package assignment.adyen.com.venuesaroundme.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;

/**
 * Created by Zeki 26/06/2017.
 */

public class VenueRequestListenerProxy {

    private VenuesActivity hostActivity;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionReceived = intent.getAction();
            if(actionReceived.equals(NetworkingUtils.REQUEST_SUCCEDED_BROADCAST)){
                hostActivity.updateList();
            }
        }
    };

    VenueRequestListenerProxy(VenuesActivity hostActivity){
        this.hostActivity = hostActivity;
    }

    void registerBroadcastReceiverProxy() {
        LocalBroadcastManager.getInstance(hostActivity).registerReceiver(
                broadcastReceiver, new IntentFilter(NetworkingUtils.REQUEST_SUCCEDED_BROADCAST));
        LocalBroadcastManager.getInstance(hostActivity).registerReceiver(
                broadcastReceiver, new IntentFilter(NetworkingUtils.REQUEST_FAILED_BROADCAST));
    }

    void unregisterBroadcastReceiverProxy() {
        LocalBroadcastManager.getInstance(hostActivity).unregisterReceiver(broadcastReceiver);
    }
}
