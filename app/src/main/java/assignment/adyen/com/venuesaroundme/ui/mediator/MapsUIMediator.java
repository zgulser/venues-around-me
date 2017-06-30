package assignment.adyen.com.venuesaroundme.ui.mediator;

import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.databinding.VenuesActivityBinding;
import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.networking.imagerequests.VolleyImageRequestController;
import assignment.adyen.com.venuesaroundme.ui.utils.MapUtils;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;
import assignment.adyen.com.venuesaroundme.ui.proxies.VenueMarkerItemProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.SearchUIProxy;

/**
 * Created by Zeki 28/07/2016
 */

public class MapsUIMediator implements UIItemMediator {

    private VenuesActivityBinding venuesActivityBinding;
    private VenueMarkerItemProxy venueMarkerItemProxy;
    private SearchUIProxy searchUIProxy;

    public MapsUIMediator(VenuesActivityBinding venuesActivityBinding){
        this.venuesActivityBinding = venuesActivityBinding;
    }

    @Override
    public void onMyLocationReceivedForTheFirstTime() {
        venuesActivityBinding.radiusTunerSeekBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNavigateToListView(BottomSheetBehavior bottomSheetBehavior) {
        if (Build.VERSION.SDK_INT >= 19) {
            TransitionManager.beginDelayedTransition(venuesActivityBinding.rootLayout);
        }

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        venuesActivityBinding.venueMapViewParent.setVisibility(View.GONE);
        venuesActivityBinding.venueListViewParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNavigateToMapView(){
        if (Build.VERSION.SDK_INT >= 19) {
            TransitionManager.beginDelayedTransition(venuesActivityBinding.rootLayout);
        }

        venuesActivityBinding.venueMapViewParent.setVisibility(View.VISIBLE);
        venuesActivityBinding.venueListViewParent.setVisibility(View.GONE);
    }

    @Override
    public void onItemSelected(FsqExploredVenue venue, BottomSheetBehavior bottomSheetBehavior) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        closeInfoViews();
        findMarkerAndOpenInfoWindow(venue);
    }

    private void closeInfoViews(){
        for(Marker m : venueMarkerItemProxy.getMarkerList()){
            m.hideInfoWindow();
        }
    }

    private void findMarkerAndOpenInfoWindow(FsqExploredVenue fsqExploredVenue){
        for(Marker marker : venueMarkerItemProxy.getMarkerList()){
            if(marker.getTag() == fsqExploredVenue){
                marker.showInfoWindow();
                MapUtils.putCameraToPosition(false, venueMarkerItemProxy.getVenueMap(), marker.getPosition(), null);
            }
        }
    }

    @Override
    public void onPopulateMapWithVenueMarkers(){
        double myLat = LocationProviderProxy.getMyPosition().latitude;
        double myLng = LocationProviderProxy.getMyPosition().longitude;
        for(FsqExploredVenue venue : FsqVenueContainer.getInstance().getFsqVenueList(myLat, myLng)){
            venueMarkerItemProxy.addMarkerToMap(venue);
        }
    }

    @Override
    public void onClearMap() {
        cleanVenuesOnMap();
        cleanMarkerItems();
    }

    private void cleanVenuesOnMap(){
        if(venueMarkerItemProxy.getVenueMap() != null){
            MapUtils.clearVenuesOnMap(venueMarkerItemProxy.getVenueMap());
        }
    }

    private void cleanMarkerItems(){
        venueMarkerItemProxy.getMarkerList().clear();
    }

    @Override
    public void onInjectVenueMarkerItemProxy(GoogleMap venuesMap) {
        venueMarkerItemProxy = new VenueMarkerItemProxy(venuesMap);
    }

    @Override
    public void onInjectSearchUIProxy(VenuesMapActivity venuesMapActivity, int fragmentID) {
        searchUIProxy = new SearchUIProxy(venuesMapActivity,
                fragmentID,
                venuesActivityBinding.searchView,
                venuesActivityBinding.buttonSearch);
    }

    @Override
    public void onUpdateBottomSheetItem(FsqExploredVenue venue) {
        venuesActivityBinding.bottomSheetContent.venueNameBottomSheet.setText(venue.getName() + ' '
                + '(' + LocationUtils.getFormattedDistance(venue.getLocation().getDistance()) + ")");
        venuesActivityBinding.bottomSheetContent.venueRatingBar.setRating(Float.parseFloat(venue.getRating())/2.0f);
        venuesActivityBinding.bottomSheetContent.venueRatingBottomSheet.setText(venue.getRating());
        venuesActivityBinding.bottomSheetContent.venueAddressBottomSheet.setText(venue.getLocation().getAddress());
        setVenueHoursValue(venue);
        loadVenueImages(venue);
    }

    private void setVenueHoursValue(FsqExploredVenue venue){
        if (venue.getHours() != null) {
            venuesActivityBinding.bottomSheetContent.venueHoursBottomSheet.setText(venue.getHours().isOpen()
                    ? venuesActivityBinding.getRoot().getResources().getString(R.string.venue_open) :
                    venuesActivityBinding.getRoot().getResources().getString(R.string.venue_close));
        }
    }

    @Override
    public void updateVenueMarkerItems(){
        venueMarkerItemProxy.updateVenueMarkerItemsTitle();
    }

    /**
     *
     * Desc: Just a mock method to populate scroll view with images
     *
     * @param venue
     */
    private void loadVenueImages(FsqExploredVenue venue){
        for (int i=0; i < 20; i++){
            loadVenueImage();
        }
    }

    /**
     *
     * Desc: Part of the mock method above
     *
     */
    private void loadVenueImage(){
        NetworkImageView imageView = new NetworkImageView(venuesActivityBinding.getRoot().getContext());
        imageView.setDefaultImageResId(R.drawable.london);
        imageView.setErrorImageResId(R.drawable.london);
        imageView.setImageUrl("", VolleyImageRequestController.getInstance().getImageLoader());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
        params.setMargins(5, 5, 5, 5);
        imageView.setLayoutParams(params);
        venuesActivityBinding.bottomSheetContent.horizontalScrollLinear.addView(imageView);
    }
}
