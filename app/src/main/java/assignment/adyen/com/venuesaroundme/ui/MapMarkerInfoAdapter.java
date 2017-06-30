package assignment.adyen.com.venuesaroundme.ui;

import android.view.View;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Zeki on 29/06/2017.
 */

public class MapMarkerInfoAdapter implements GoogleMap.InfoWindowAdapter {

    VenuesMapActivity venuesMapActivity;

    MapMarkerInfoAdapter(VenuesMapActivity venuesMapActivity){
        this.venuesMapActivity = venuesMapActivity;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
