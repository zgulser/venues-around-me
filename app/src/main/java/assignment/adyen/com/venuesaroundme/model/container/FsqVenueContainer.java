package assignment.adyen.com.venuesaroundme.model.container;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.ArrayMap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenueGroup;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenueItem;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.model.entities.FsqVenueRequestRoot;
import assignment.adyen.com.venuesaroundme.networking.FsqVenueRequestController;
import assignment.adyen.com.venuesaroundme.networking.IFsqVenueRequestObserver;
import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;

/**
 * Created by Zeki on 25/06/2017.
 */

public class FsqVenueContainer implements IFsqVenueRequestObserver {

    private static FsqVenueContainer fsqVenueContainer;
    private ArrayMap<String, FsqExploredVenue> fsqVenueListBuffer;
    private List<FsqExploredVenue> fsqVenueListFilteredByRadius;
    private static boolean newDownload = true;

    private FsqVenueContainer() {initRepoList();}

    private void initRepoList(){
        fsqVenueListBuffer = new ArrayMap<>();
        fsqVenueListFilteredByRadius = new ArrayList<>();
    }

    public static synchronized FsqVenueContainer getInstance() {
        if(fsqVenueContainer == null) {
            fsqVenueContainer = new FsqVenueContainer();
        }

        return fsqVenueContainer;
    }

    public List<FsqExploredVenue> getFsqVenueList(double myLocationLatitude, double myLocationLongitude) {
        if(newDownload) {
            FsqVenueRequestController.getInstance().get(true, myLocationLatitude, myLocationLongitude);
            newDownload = false;
        }

        return fsqVenueListFilteredByRadius;
    }

    @Override
    public void onFsqVenueListDownloaded(FsqVenueRequestRoot response) {
        extractAndAddVenueItemsFromResponse(response);
        filterVenuesByRadius();
        sortVenueListByDistance();
        LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).
                sendBroadcast(new Intent(NetworkingUtils.REQUEST_SUCCEDED_BROADCAST));
    }

    /**
     *
     * Separated for loops instead of making them nested. Index-based approach might be faster.
     *
     * @param requestResult
     */
    private void extractAndAddVenueItemsFromResponse(FsqVenueRequestRoot requestResult){
        List<FsqExploredVenueItem> fsqExploredResponseGroupItems = new ArrayList<>();
        for (FsqExploredVenueGroup fsqExploredVenueGroup : requestResult.getResponse().getGroups()){
            fsqExploredResponseGroupItems.addAll(fsqExploredVenueGroup.getItems());
        }

        List<FsqExploredVenue> fsqExploredResponseGroupItemVenues = new ArrayList<>();
        for(FsqExploredVenueItem fsqExploredVenueItem : fsqExploredResponseGroupItems){
            fsqExploredResponseGroupItemVenues.add(fsqExploredVenueItem.getVenue());
        }

        for(FsqExploredVenue fsqExploredVenue : fsqExploredResponseGroupItemVenues){
            if(!fsqVenueListBuffer.containsKey(fsqExploredVenue.getId())) {
                fsqVenueListBuffer.put(fsqExploredVenue.getId(), fsqExploredVenue);
            }
        }
    }

    private void filterVenuesByRadius(){
        ArrayList<FsqExploredVenue> temporaryBackupList = new ArrayList<>();
        int distBetween = LocationUtils.getBoundingDistance();
        for (FsqExploredVenue venue : fsqVenueListBuffer.values()){
            if(isInsideBounds(venue, distBetween)){
                temporaryBackupList.add(venue);
            }
        }

        fsqVenueListFilteredByRadius.clear();
        fsqVenueListFilteredByRadius.addAll(temporaryBackupList);
    }

    /**
     *
     * sortByDistance parameter might also have been used
     */
    private void sortVenueListByDistance(){
        Collections.sort(fsqVenueListFilteredByRadius, new Comparator<FsqExploredVenue>() {
            @Override
            public int compare(FsqExploredVenue fsqExploredVenue, FsqExploredVenue t1) {
                return Integer.valueOf(fsqExploredVenue.getLocation().getDistance()) -
                        Integer.valueOf(t1.getLocation().getDistance());
            }
        });
    }

    public void refreshVenuesByRadius(int newRadius){
        if(newRadius > LocationUtils.surroundingRadius){
            newDownload = true;
            LocationUtils.surroundingRadius = newRadius;
            getFsqVenueList(LocationProviderProxy.getMyPosition().latitude, LocationProviderProxy.getMyPosition().longitude);
        } else {
            LocationUtils.surroundingRadius = newRadius;
            filterVenuesByRadius();
            LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).sendBroadcast(new Intent(NetworkingUtils.REQUEST_FILTERED_BROADCAST));
        }
    }

    private boolean isInsideBounds(FsqExploredVenue venue, int distBetween){
        return venue.getLocation().getDistance() < distBetween;
    }
}
