package assignment.adyen.com.venuesaroundme.ui.mediator;

import android.os.Build;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.LinearLayoutCompat;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.databinding.VenuesActivityBinding;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenuePhoto;
import assignment.adyen.com.venuesaroundme.networking.imagerequests.VolleyImageRequestController;
import assignment.adyen.com.venuesaroundme.ui.MapUtils;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;
import assignment.adyen.com.venuesaroundme.ui.proxies.MapUIItemProxy;
import assignment.adyen.com.venuesaroundme.ui.proxies.SearchUIProxy;

/**
 * Created by Zeki
 */

public class MapsUIMediator implements UIItemMediator {

    private VenuesActivityBinding venuesActivityBinding;
    private MapUIItemProxy mapUIItemProxy;
    private SearchUIProxy searchUIProxy;

    public MapsUIMediator(VenuesActivityBinding venuesActivityBinding){
        this.venuesActivityBinding = venuesActivityBinding;
    }

    @Override
    public void onMyLocationClicked() {}

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
        for(Marker m : mapUIItemProxy.getMarkerList()){
            m.hideInfoWindow();
        }
    }

    private void findMarkerAndOpenInfoWindow(FsqExploredVenue fsqExploredVenue){
        for(Marker marker : mapUIItemProxy.getMarkerList()){
            if(marker.getTag() == fsqExploredVenue){
                marker.showInfoWindow();
                MapUtils.putCameraToPosition(false, mapUIItemProxy.getVenueMap(), marker.getPosition(), null);
            }
        }
    }

    @Override
    public void onPopulateMapWithVenueMarkers(){
        for(FsqExploredVenue venue : FsqVenueContainer.getInstance().getFsqVenueList()){
            mapUIItemProxy.addMarkerToMap(venue);
        }
    }

    @Override
    public void onClearMap() {
        cleanBranchesOnMap();
        cleanMarkerItems();
    }

    private void cleanBranchesOnMap(){
        if(mapUIItemProxy.getVenueMap() != null){
            MapUtils.clearBranchesOnMap(mapUIItemProxy.getVenueMap());
        }
    }

    private void cleanMarkerItems(){
        mapUIItemProxy.getMarkerList().clear();
    }

    @Override
    public void onInjectMapUIItemProxy(GoogleMap venuesMap) {
        mapUIItemProxy = new MapUIItemProxy(venuesMap);
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
        venuesActivityBinding.bottomSheetContent.venueHoursBottomSheet.setText(venue.getHours().isOpen()
                        ? venuesActivityBinding.getRoot().getResources().getString(R.string.venue_open)
                        : venuesActivityBinding.getRoot().getResources().getString(R.string.venue_close));
        loadVenueImages(venue);
    }

    private void loadVenueImages(FsqExploredVenue venue){
        for (int i=0; i < 20; i++){
            loadVenueImage();
        }
    }

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

    @Override
    public List<Marker> onGetMarkers() {
        return mapUIItemProxy.getMarkerList();
    }

}
