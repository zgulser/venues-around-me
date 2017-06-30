package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;
import assignment.adyen.com.venuesaroundme.ui.utils.MapUtils;


/**
 * Created by Zeki on 27/06/2017.
 */

public class VenueMarkerItemProxy {
    private GoogleMap venueMap;
    private ArrayList<Marker> markerList;

    public VenueMarkerItemProxy(GoogleMap map){
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

    public void updateVenueMarkerItemsTitle(){
        for (Marker marker : markerList){
            FsqExploredVenue markerVenue = (FsqExploredVenue) marker.getTag();
            LatLng venueLocation = new LatLng(markerVenue.getLocation().getLatitude(), markerVenue.getLocation().getLongitude());
            LatLng myLocation = LocationProviderProxy.getMyPosition();
            marker.setTitle(markerVenue.getName() + " (" +
                    LocationUtils.getFormattedDistance(MapUtils.getDistanceBetween(myLocation, venueLocation)) + ")");
        }
    }

    public ArrayList<Marker> getMarkerList() {
        return markerList;
    }

    public GoogleMap getVenueMap() {
        return venueMap;
    }

}
