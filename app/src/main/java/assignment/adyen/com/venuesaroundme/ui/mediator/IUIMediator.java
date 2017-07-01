package assignment.adyen.com.venuesaroundme.ui.mediator;

import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.GoogleMap;

import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki on 28/07/2017
 */

public interface IUIMediator {
    void onMyLocationReceivedForTheFirstTime();
    void onNavigateToListView();
    void onNavigateToMapView();
    void onItemSelected(FsqExploredVenue venue);
    void onPopulateMapWithVenueMarkers();
    void onClearMap();
    void onInjectVenueMarkerItemProxy(GoogleMap venuesMap);
    void onInjectSearchUIProxy(VenuesMapActivity mapActivity, int fragmentID);
    void onUpdateBottomSheetItem(FsqExploredVenue venue);
    void updateVenueMarkerItems();
}
