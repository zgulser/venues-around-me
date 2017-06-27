package assignment.adyen.com.venuesaroundme.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Marker;

import java.util.Locale;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.application.AppUtils;
import assignment.adyen.com.venuesaroundme.databinding.VenuesActivityBinding;
import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.permission.PermissionUtils;
import assignment.adyen.com.venuesaroundme.ui.mediator.MapsUIMediator;
import assignment.adyen.com.venuesaroundme.ui.mediator.UIItemMediator;
import assignment.adyen.com.venuesaroundme.ui.proxies.LocationChangeListenerProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.LocationSettingCheckerProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.PermissionHandlerProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.SearchUIProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.VenueRecyclerViewProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.VenueRequestListenerProxy;

public class VenuesMapActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnMyLocationButtonClickListener,
        SearchUIProxy.SearchActionsListener, VenueItemAdapter.IListItemClickListener{

    private VenueRecyclerViewProxy venueListProxy;
    private VenueRequestListenerProxy venueRequestListenerProxy;
    private PermissionHandlerProxy permissionHandlerProxy;
    private LocationProviderProxy locationProviderProxy;
    private LocationChangeListenerProxy locationListenerProxy;
    private LocationSettingCheckerProxy locationSettingCheckerProxy;
    private VenuesActivityBinding venuesActivityBinding;
    private UIItemMediator uiItemMediator;
    private Bundle savedActivityInstance;
    private Handler delayedEventHandler;
    private GoogleMap venuesMap;
    private BottomSheetBehavior venueItemBottomSheetBehaviour;

    public void updateViewWhenMyLocationReceivedFirstTime(){
        locationProviderProxy.setFirstLocationRequestReceived(true);
        locationSettingCheckerProxy.hideMyLocationError();

        MapUtils.putCameraToPosition(true, venuesMap, locationProviderProxy.getMyPosition(), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        delayedEventHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                MapUtils.refreshMapWithBranchesByArea(VenuesMapActivity.this.getApplicationContext(), venuesMap);
                            }
                        }, 2000);
                    }
                }).start();
            }
            @Override
            public void onCancel() {}
        });
    }

    public void updateMapMarkersWhenMyLocationUpdated(){
        for (Marker marker : uiItemMediator.onGetMarkers()){
            //TODO: update marker item when my location changes
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        venuesActivityBinding = DataBindingUtil.setContentView(this, R.layout.venues_activity);
        savedActivityInstance = savedInstanceState;

        initMapView();
        initDelayedEventHandler();
        initRestOfTheActivity();
    }

    private void initMapView() {
        venuesActivityBinding.map.onCreate(savedActivityInstance);
        venuesActivityBinding.map.getMapAsync(this);
    }

    private void initDelayedEventHandler(){
        delayedEventHandler = new Handler(Looper.getMainLooper(), null);
    }

    private void initRestOfTheActivity(){
        if(AppUtils.isPlayServicesInstalled(VenuesMapActivity.this)){
            initUIElementsAndProxies();
            permissionHandlerProxy.checkLocationPermissionGranted();
        } else {
            Snackbar.make(venuesActivityBinding.rootLayout, getResources().getString(R.string.play_services_error), Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void initUIElementsAndProxies() {
        initUIElements();
        initProxies();
    }

    private void initUIElements() {
        initVenueItemBottomSheetBehaviour();
        initUIMediator();
        initVenueListRecyclerView();
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

    private void initUIMediator(){
        uiItemMediator = new MapsUIMediator(venuesActivityBinding);
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

    private void initVenueListRecyclerView() {
        venueListProxy = new VenueRecyclerViewProxy(venuesActivityBinding.venuesListContent.venuesRecyclerView, this);
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
        MapsInitializer.initialize(this);
        setMapProperties(venuesMap);
        uiItemMediator.onInjectMapUIItemProxy(venuesMap);
        venueListProxy.setRecyclerViewAdapter();
    }

    private void setMapProperties(GoogleMap googleMap) throws SecurityException{
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (permissionHandlerProxy.isLocationPermissionGranted()) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(this);
            tunePositionOfMyLocationButton();
        }
        googleMap.setMinZoomPreference(6.0f);
        googleMap.setMaxZoomPreference(18.0f);

        googleMap.setInfoWindowAdapter(new MapMarkerInfoAdapter(this));
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                updateViewOnVenueMarkerItemClicked((FsqExploredVenue) marker.getTag());
                return true;
            }
        });
    }

    private void tunePositionOfMyLocationButton(){
        View locationButton = ((View) venuesActivityBinding.map.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
    }

    public void addVenuesToMap(boolean clearMap){
        if(clearMap) {
            uiItemMediator.onClearMap();
        }

        uiItemMediator.onPopulateMapWithVenueMarkers();
    }

    public void refreshVenueAdapter(){
        venueListProxy.refreshVenueAdapter();
    }

    public void updateViewOnVenueMarkerItemClicked(FsqExploredVenue venue){
        uiItemMediator.onUpdateBottomSheetItem(venue);
        uiItemMediator.onItemSelected(venue, venueItemBottomSheetBehaviour);
    }

    public void updateViewOnVenueListItemClick(FsqExploredVenue venue){
        uiItemMediator.onNavigateToMapView();
        uiItemMediator.onItemSelected(venue, venueItemBottomSheetBehaviour);
    }

    public void goToGoogleMapsForDirections(FsqExploredVenue venue){
        uiItemMediator.onItemSelected(venue, venueItemBottomSheetBehaviour);
        sendGoogleMapsIntent(venue);
    }

    private void sendGoogleMapsIntent(FsqExploredVenue fsqExploredVenue){
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
        if(!MapUtils.isMyLocationVisible(locationProviderProxy.getMyPosition(), venuesMap)){
            venuesMap.moveCamera(CameraUpdateFactory.newLatLng(locationProviderProxy.getMyPosition()));
            MapUtils.refreshMapWithBranchesByArea(VenuesMapActivity.this, venuesMap);
        }
        uiItemMediator.onMyLocationClicked();
        return false;
    }

    @Override
    public void onCameraMove() {
        // TODO
    }

    @Override
    public void onListItemClick(FsqExploredVenue venue) {
        updateViewOnVenueListItemClick(venue);
    }

    @Override
    public void onNavigateToMapViewIconClicked() {
        uiItemMediator.onNavigateToMapView();
    }

    @Override
    public void onNavigateToListViewIconClicked() {
        uiItemMediator.onNavigateToListView(venueItemBottomSheetBehaviour);
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
