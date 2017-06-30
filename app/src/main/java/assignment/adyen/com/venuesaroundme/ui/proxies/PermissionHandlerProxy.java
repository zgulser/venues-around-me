package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.networking.VenueTestable;
import assignment.adyen.com.venuesaroundme.permission.GetLocationGrant;
import assignment.adyen.com.venuesaroundme.permission.Grant;
import assignment.adyen.com.venuesaroundme.permission.PermissionBroker;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki on 28/06/2017.
 */

public class PermissionHandlerProxy {
    private VenuesMapActivity venuesMapActivity;
    private boolean locationPermissionGranted = false;

    public PermissionHandlerProxy(VenuesMapActivity venuesMapActivity){
        this.venuesMapActivity = venuesMapActivity;
    }

    public void checkLocationPermissionGranted(){
        locationPermissionGranted = (ContextCompat.checkSelfPermission(venuesMapActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!locationPermissionGranted)  {
            handleLocationPermission();
        } else {
            venuesMapActivity.initGoogleLocationServices();
        }
    }

    @VenueTestable
    private void handleLocationPermission() {
        Grant grant = new Grant(android.Manifest.permission.ACCESS_FINE_LOCATION,
                venuesMapActivity.getResources().getString(R.string.permission_exp_location));
        GetLocationGrant locationGrant = new GetLocationGrant(grant, venuesMapActivity);
        PermissionBroker broker = new PermissionBroker();
        broker.takePermission(locationGrant);
        broker.executePermissions();
    }

    public boolean isLocationPermissionGranted() {
        return locationPermissionGranted;
    }

    public void setLocationPermissionGranted(boolean locationPermissionGranted) {
        this.locationPermissionGranted = locationPermissionGranted;
    }
}
