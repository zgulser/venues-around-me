package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.location.ILocationProvider;
import assignment.adyen.com.venuesaroundme.location.ILocationSettingChecker;
import assignment.adyen.com.venuesaroundme.location.ILocationSettingObserver;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.ui.MapUtils;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki
 */

public class LocationSettingCheckerProxy implements ILocationSettingChecker {

    private VenuesMapActivity venuesMapActivity;
    private Snackbar locationSettingsDisabledSnackBar;
    private List<ILocationSettingObserver> locationSettingObserverList;

    private OnSuccessListener onSuccessListener = new OnSuccessListener() {
        @Override
        public void onSuccess(Object o) {
            for(ILocationSettingObserver observer : locationSettingObserverList){
                observer.onLocationSettingsRequestSuccess();
            }
        }
    };
    private OnFailureListener onFailureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            int statusCode = ((ApiException) e).getStatusCode();
            for(ILocationSettingObserver observer : locationSettingObserverList){
                observer.onLocationSettingsRequestFailed(statusCode);
            }
        }
    };

    public LocationSettingCheckerProxy(VenuesMapActivity venuesMapActivity){
        this.venuesMapActivity = venuesMapActivity;
        this.locationSettingObserverList = new ArrayList<>();
    }

    @Override
    public void checkLocationSettingsEnabled() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(LocationUtils.getLocationRequest());
        SettingsClient client = LocationServices.getSettingsClient(FsqVenuesApplication.getAppContext());

        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(venuesMapActivity, onSuccessListener);
        task.addOnFailureListener(venuesMapActivity, onFailureListener);
    }

    @Override
    public void addLocationSettingRequestObserver(ILocationSettingObserver locationSettingObserver){
        locationSettingObserverList.add(locationSettingObserver);
    }

    @Override
    public void removeLocationSettingRequestObserver(ILocationSettingObserver locationSettingObserver){
        locationSettingObserverList.remove(locationSettingObserver);
    }

    @Override
    public void showLocationSettingError(){
        if(locationSettingsDisabledSnackBar == null){
            locationSettingsDisabledSnackBar = Snackbar.make(
                    venuesMapActivity.findViewById(R.id.parentCoordinatorLayout),
                    venuesMapActivity.getResources().getString(R.string.location_settings_error_msg),
                    Snackbar.LENGTH_INDEFINITE).setAction(venuesMapActivity.getResources().getString(R.string.to_location_settings), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            locationSettingsDisabledSnackBar.dismiss();
                            venuesMapActivity.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), LocationUtils.REQUEST_CHECK_SETTINGS);
                        }
                    });

        }

        if(!locationSettingsDisabledSnackBar.isShown()){
            locationSettingsDisabledSnackBar.show();
        }
    }

    @Override
    public void hideMyLocationError(){
        if(locationSettingsDisabledSnackBar != null){
            locationSettingsDisabledSnackBar.dismiss();
        }
    }
}
