package assignment.adyen.com.venuesaroundme.model.container;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
    private List<FsqExploredVenue> fsqVenueListBuffer;
    private List<FsqExploredVenue> fsqVenueListFilteredByRadius;
    private static boolean firstDownload = true;

    private FsqVenueContainer() {initRepoList();}

    private void initRepoList(){
        fsqVenueListBuffer = new ArrayList<>();
        fsqVenueListFilteredByRadius = new ArrayList<>();
    }

    public static synchronized FsqVenueContainer getInstance() {
        if(fsqVenueContainer == null) {
            fsqVenueContainer = new FsqVenueContainer();
        }

        return fsqVenueContainer;
    }

    public List<FsqExploredVenue> getFsqVenueList(double myLocationLatitude, double myLocationLongitude) {
        if(firstDownload) {
            fsqVenueListBuffer.clear();
            FsqVenueRequestController.getInstance().get(true, myLocationLatitude, myLocationLongitude);
            firstDownload = false;
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
            fsqVenueListBuffer.add(fsqExploredVenue);
        }
    }

    private void filterVenuesByRadius(){
        ArrayList<FsqExploredVenue> temporaryBackupList = new ArrayList<>();
        LatLngBounds circularBound = LocationUtils.getBoundsOfCurrentRadius(LocationUtils.surroundingRadius);
        for (FsqExploredVenue venue : fsqVenueListBuffer){
            if(isInside(venue, circularBound)){
                temporaryBackupList.add(venue);
            }
        }

        fsqVenueListFilteredByRadius.addAll(temporaryBackupList);
        temporaryBackupList = null;
    }

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
            firstDownload = true;
            getFsqVenueList(LocationProviderProxy.getMyPosition().latitude, LocationProviderProxy.getMyPosition().longitude);
            LocationUtils.surroundingRadius = newRadius;
        } else {
            LocationUtils.surroundingRadius = newRadius;
            filterVenuesByRadius();
        }
    }

    private boolean isInside(FsqExploredVenue venue, LatLngBounds circularBound){
        return circularBound.contains(new LatLng(venue.getLocation().getLatitude(), venue.getLocation().getLongitude()));
    }

    public List<FsqExploredVenue> getFsqVenueListFilteredByRadius() {
        return fsqVenueListFilteredByRadius;
    }
}
