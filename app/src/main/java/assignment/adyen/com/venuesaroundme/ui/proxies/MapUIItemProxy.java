package assignment.adyen.com.venuesaroundme.ui.proxies;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;


/**
 * Created by Zeki
 */

public class MapUIItemProxy {
    private GoogleMap venueMap;
    private ArrayList<Marker> markerList;

    public MapUIItemProxy(GoogleMap map){
        this.venueMap = map;
        this.markerList = new ArrayList<Marker>();
    }

    public void addMarkerToMap(FsqExploredVenue venue){
        Marker item = venueMap.addMarker(new MarkerOptions()
                .position(new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude()))
                .title(venue.getName() + " (" + LocationUtils.getFormattedDistance(venue.getLocation().getDistance()) + ")")
                .snippet(venue.getLocation().getAddress() +"\n" + venue.getRating())
                .alpha(1.0f)
                .icon(BitmapDescriptorFactory.defaultMarker())
                .zIndex(2.0f)
                .flat(true));
        item.setTag(venue);
        markerList.add(item);
    }

    public ArrayList<Marker> getMarkerList() {
        return markerList;
    }

    public GoogleMap getVenueMap() {
        return venueMap;
    }

}
