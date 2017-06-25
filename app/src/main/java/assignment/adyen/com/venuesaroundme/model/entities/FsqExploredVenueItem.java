package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueItem {

    @SerializedName("venue")
    FsqExploredVenue fsqExploredResponseGroupItemVenue;

    public FsqExploredVenue getFsqExploredResponseGroupItemVenue(){
        return fsqExploredResponseGroupItemVenue;
    }
}
