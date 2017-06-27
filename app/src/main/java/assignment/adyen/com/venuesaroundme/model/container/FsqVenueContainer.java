package assignment.adyen.com.venuesaroundme.model.container;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
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
    private List<FsqExploredVenue> fsqVenueList;
    private static boolean firstDownload = true;

    private FsqVenueContainer() {initRepoList();}

    private void initRepoList(){
        fsqVenueList = new ArrayList<>();
    }

    public static synchronized FsqVenueContainer getInstance() {
        if(fsqVenueContainer == null) {
            fsqVenueContainer = new FsqVenueContainer();
        }

        return fsqVenueContainer;
    }

    public List<FsqExploredVenue> getFsqVenueList() {
        if(firstDownload) {
            FsqVenueRequestController.getInstance().get(true);
            firstDownload = false;
        }

        return fsqVenueList;
    }

    @Override
    public void onFsqVenueListDownloaded(FsqVenueRequestRoot requestResult) {
        addFsqVenueItemToTheList(requestResult);
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
    private void addFsqVenueItemToTheList(FsqVenueRequestRoot requestResult){
        List<FsqExploredVenueItem> fsqExploredResponseGroupItems = new ArrayList<>();
        for (FsqExploredVenueGroup fsqExploredVenueGroup : requestResult.getFsqExploredResponse().getFsqExploredResponseGroups()){
            fsqExploredResponseGroupItems.addAll(fsqExploredVenueGroup.getFsqExploredResponseGroupItems());
        }

        List<FsqExploredVenue> fsqExploredResponseGroupItemVenues = new ArrayList<>();
        for(FsqExploredVenueItem fsqExploredVenueItem : fsqExploredResponseGroupItems){
            fsqExploredResponseGroupItemVenues.add(fsqExploredVenueItem.getFsqExploredResponseGroupItemVenue());
        }

        for(FsqExploredVenue fsqExploredVenue : fsqExploredResponseGroupItemVenues){
            fsqVenueList.add(fsqExploredVenue);
        }
    }

    private void sortVenueListByDistance(){
        Collections.sort(fsqVenueList, new Comparator<FsqExploredVenue>() {
            @Override
            public int compare(FsqExploredVenue fsqExploredVenue, FsqExploredVenue t1) {
                return Integer.valueOf(fsqExploredVenue.getLocation().getDistance()) -
                        Integer.valueOf(t1.getLocation().getDistance());
            }
        });
    }
}
