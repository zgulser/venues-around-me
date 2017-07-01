package assignment.adyen.com.venuesaroundme.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import java.util.Locale;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.application.AppUtils;
import assignment.adyen.com.venuesaroundme.databinding.VenuesActivityBinding;
import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.permission.PermissionUtils;
import assignment.adyen.com.venuesaroundme.ui.mediator.UIMediatorImpl;
import assignment.adyen.com.venuesaroundme.ui.mediator.IUIMediator;
import assignment.adyen.com.venuesaroundme.ui.proxies.LocationChangeListenerProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.LocationSettingCheckerProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.PermissionHandlerProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.SearchAndNavigationUIProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.VenueRecyclerViewProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.VenueRequestListenerProxy;
import assignment.adyen.com.venuesaroundme.ui.specialobjects.CustomGoogleMapCancellableCallback;
import assignment.adyen.com.venuesaroundme.ui.specialobjects.CustomSeekBarChangedListener;
import assignment.adyen.com.venuesaroundme.ui.utils.AnimationUtils;
import assignment.adyen.com.venuesaroundme.ui.utils.MapUtils;

/**
 * Created by Zeki on 27/06/2017.
 */

public class VenuesMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener
        , GoogleMap.OnCameraMoveListener, GoogleMap.OnMyLocationButtonClickListener, VenueItemAdapter.IVenueListItemClickListener{

    private VenueRecyclerViewProxy venueRecyclerViewProxy;
    private VenueRequestListenerProxy venueRequestListenerProxy;
    private PermissionHandlerProxy permissionHandlerProxy;
    private LocationProviderProxy locationProviderProxy;
    private LocationChangeListenerProxy locationListenerProxy;
    private LocationSettingCheckerProxy locationSettingCheckerProxy;
    private VenuesActivityBinding venuesActivityBinding;
    private IUIMediator uiItemMediator;
    private Bundle savedActivityInstance;
    private GoogleMap venuesMap;
    private Handler delayedOperationHandler;
    private BottomSheetBehavior venueItemBottomSheetBehaviour;
    private final int RADIUS_UPDATE_MESSAGE_ID = 1;
    private static final String RADIUS_VALUE_MESSAGE_KEY = "seekbarProgress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        venuesActivityBinding = DataBindingUtil.setContentView(this, R.layout.venues_activity);
        savedActivityInstance = savedInstanceState;

        initMapView();
        initRestOfTheActivity();
        initDelayedOperationHandler();
    }

    private void initMapView() {
        venuesActivityBinding.map.onCreate(savedActivityInstance);
        venuesActivityBinding.map.getMapAsync(this);
    }

    private void initRestOfTheActivity(){
        if(AppUtils.isPlayServicesInstalled(VenuesMapActivity.this)){
            initUIElementsAndProxies();
            permissionHandlerProxy.checkLocationPermissionGranted();
        } else {
            Snackbar.make(venuesActivityBinding.rootLayout, getResources().getString(R.string.play_services_error), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void initDelayedOperationHandler(){
        delayedOperationHandler = new Handler(getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what){
                    case RADIUS_UPDATE_MESSAGE_ID:
                        int newRadius = message.getData().getInt(RADIUS_VALUE_MESSAGE_KEY);
                        FsqVenueContainer.getInstance().refreshVenuesByRadius(newRadius);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void initUIElementsAndProxies() {
        initUIElements();
        initProxies();
    }

    private void initUIElements() {
        initVenueItemBottomSheetBehaviour();
        initSeekBar();
        initUIMediator();
        initVenueRecyclerView();
    }

    private void initProxies(){
        initPermissionHandlerProxy();
        initVenueRequestListenerProxy();
        initLocationProxies();
    }

    private void initVenueItemBottomSheetBehaviour() {
        venueItemBottomSheetBehaviour = BottomSheetBehavior.from(venuesActivityBinding.venueItemBottomSheet);
        venueItemBottomSheetBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
        venueItemBottomSheetBehaviour.setPeekHeight(0);
    }

    private void initSeekBar(){
        venuesActivityBinding.radiusTunerSeekBar.setVisibility(View.GONE);
        venuesActivityBinding.radiusTunerSeekBar.setProgress(LocationUtils.surroundingRadius/1000);
        venuesActivityBinding.radiusTunerSeekBar.setOnSeekBarChangeListener(new CustomSeekBarChangedListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean changedByUser) {
                if(changedByUser) {
                    updateSeekBarRelatedUI(progress);
                    updateRadiusByDelayedHandler(progress);
                }
            }
        });
    }

    private void updateSeekBarRelatedUI(int progress){
        venuesActivityBinding.seekBarValue.setAlpha(1.0f);
        venuesActivityBinding.seekBarValue.setText(String.format(getString(R.string.radius_text), venuesActivityBinding.radiusTunerSeekBar.getProgress()));
        AnimationUtils.runAlphaAnimationOnAView(venuesActivityBinding.seekBarValue, AnimationUtils.DURATION_MEDIUM, false);
        MapUtils.updateSurroundingCircleRadius(progress*1000);

    }

    private void updateRadiusByDelayedHandler(int progress) {
        delayedOperationHandler.removeMessages(RADIUS_UPDATE_MESSAGE_ID);
        Message updateRadiusMessage = delayedOperationHandler.obtainMessage(RADIUS_UPDATE_MESSAGE_ID);
        Bundle data = new Bundle();
        data.putInt(RADIUS_VALUE_MESSAGE_KEY, progress*1000);
        updateRadiusMessage.setData(data);
        delayedOperationHandler.sendMessageDelayed(updateRadiusMessage, 1000);
    }

    private void initUIMediator(){
        uiItemMediator = new UIMediatorImpl(venuesActivityBinding, venueItemBottomSheetBehaviour);
        uiItemMediator.onInjectSearchUIProxy(this, R.id.place_autocomplete_fragment);
    }

    private void initPermissionHandlerProxy() {
        permissionHandlerProxy = new PermissionHandlerProxy(this);
    }

    public void initGoogleLocationServices() {
        locationProviderProxy.setGoogleApiClient();
        locationProviderProxy.connect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LocationUtils.REQUEST_CHECK_SETTINGS){
            locationSettingCheckerProxy.checkLocationSettingsEnabled();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
                    permissionHandlerProxy.setLocationPermissionGranted(true);
                    initGoogleLocationServices();
                    locationProviderProxy.connect();
                    locationListenerProxy.registerLocationBroadcastReceiver();
                    venuesMap.setMyLocationEnabled(true);
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void initLocationProxies() {
        locationListenerProxy = new LocationChangeListenerProxy(VenuesMapActivity.this);
        locationSettingCheckerProxy = new LocationSettingCheckerProxy(this);
        locationProviderProxy = new LocationProviderProxy(locationSettingCheckerProxy);
    }

    private void initVenueRequestListenerProxy() {
        venueRequestListenerProxy = new VenueRequestListenerProxy(VenuesMapActivity.this);
    }

    private void initVenueRecyclerView() {
        venueRecyclerViewProxy = new VenueRecyclerViewProxy(venuesActivityBinding.venuesListContent.venuesRecyclerView, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        venuesActivityBinding.map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        venuesActivityBinding.map.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        venuesActivityBinding.map.onStart();
        locationListenerProxy.registerLocationBroadcastReceiver();
        venueRequestListenerProxy.registerBroadcastReceiverProxy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        venuesActivityBinding.map.onStop();
        locationListenerProxy.unregisterLocationBroadcastReceiver();
        venueRequestListenerProxy.unregisterBroadcastReceiverProxy();
        locationProviderProxy.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        venuesActivityBinding.map.onDestroy();
        locationListenerProxy.unregisterLocationBroadcastReceiver();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        venuesActivityBinding.map.onSaveInstanceState(outState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setImmersiveScreen();
    }

    private void setImmersiveScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        venuesMap = googleMap;
        setMapProperties(venuesMap);
        uiItemMediator.onInjectVenueMarkerItemProxy(venuesMap);
    }

    private void setMapProperties(GoogleMap googleMap) throws SecurityException{
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.setMinZoomPreference(6.0f);
        googleMap.setMaxZoomPreference(18.0f);
        googleMap.setInfoWindowAdapter(new VenueMarkerItemInfoAdapter(this));
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                FsqExploredVenue venue = ((FsqExploredVenue) marker.getTag());
                uiItemMediator.onItemSelected(venue);
                return true;
            }
        });

        if (permissionHandlerProxy.isLocationPermissionGranted()) {
            setMapPropertiesForMyLocation();
        }
    }

    private void setMapPropertiesForMyLocation() throws SecurityException{
        venuesMap.setMyLocationEnabled(true);
        venuesMap.setOnMyLocationButtonClickListener(this);
        tunePositionOfMyLocationButton();
    }

    private void tunePositionOfMyLocationButton(){
        View locationButton = ((View) venuesActivityBinding.map.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
    }

    public void clearAndAddVenuesToMap(){
        uiItemMediator.onClearMap();
        uiItemMediator.onPopulateMapWithVenueMarkers();
    }

    public void addVenuesToMap(){
        uiItemMediator.onPopulateMapWithVenueMarkers();
    }

    public void refreshVenueAdapter(){
        venueRecyclerViewProxy.refreshVenueAdapter();
    }

    public void onMyLocationReceivedFirstTime(){
        locationProviderProxy.setFirstLocationRequestReceived(true);
        locationSettingCheckerProxy.hideLocationSettingError();
        venueRecyclerViewProxy.setRecyclerViewAdapter();

        MapUtils.putCameraToPosition(true, venuesMap, locationProviderProxy.getMyPosition(), new CustomGoogleMapCancellableCallback() {
            @Override
            public void onFinish() {
                uiItemMediator.onMyLocationReceivedForTheFirstTime();
                MapUtils.addRadiusCircle(getResources(), venuesMap);
            }
        });
    }

    public void onMyLocationChanged(){
        MapUtils.updateSurroundingCircleByMyLocation(VenuesMapActivity.this);
        uiItemMediator.updateVenueMarkerItems();
    }

    public void goToGoogleMapsForDirections(FsqExploredVenue venue){
        uiItemMediator.onItemSelected(venue);
        sendGoogleMapsDirectionIntent(venue);
    }

    private void sendGoogleMapsDirectionIntent(FsqExploredVenue fsqExploredVenue){
        Uri gmmIntentUri = Uri.parse(String.format(Locale.ENGLISH, "google.navigation:q=%f,%f",
                fsqExploredVenue.getLocation().getLatitude(), fsqExploredVenue.getLocation().getLongitude()));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        startActivity(mapIntent);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        goToGoogleMapsForDirections((FsqExploredVenue) marker.getTag());
    }

    @Override
    public boolean onMyLocationButtonClick() {
        MapUtils.onMyLocationButtonClicked(locationProviderProxy, venuesMap);
        return false;
    }

    @Override
    public void onCameraMove() {
        //TODO
    }

    @Override
    public void onVenueListItemClick(FsqExploredVenue venue) {
        uiItemMediator.onNavigateToMapView();
        uiItemMediator.onItemSelected(venue);
    }

    public LocationProviderProxy getLocationProviderProxy(){
        return locationProviderProxy;
    }

    public LocationSettingCheckerProxy getLocationSettingCheckerProxy() {
        return locationSettingCheckerProxy;
    }

    public GoogleMap getVenuesMap() {
        return venuesMap;
    }
}
