package assignment.adyen.com.venuesaroundme.ui.mediator;

import android.support.design.widget.BottomSheetBehavior;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;
import assignment.adyen.com.venuesaroundme.ui.proxies.SearchUIProxy;

/**
 * Created by Zeki
 */

public interface UIItemMediator {
    void onMyLocationClicked();
    void onNavigateToListView(BottomSheetBehavior bottomSheetBehavior);
    void onNavigateToMapView();
    void onItemSelected(FsqExploredVenue venue, BottomSheetBehavior bottomSheetBehavior);
    void onPopulateMapWithVenueMarkers();
    void onClearMap();
    void onInjectMapUIItemProxy(GoogleMap venuesMap);
    void onInjectSearchUIProxy(VenuesMapActivity mapActivity, int fragmentID);
    void onUpdateBottomSheetItem(FsqExploredVenue venue);
    List<Marker> onGetMarkers();
}
