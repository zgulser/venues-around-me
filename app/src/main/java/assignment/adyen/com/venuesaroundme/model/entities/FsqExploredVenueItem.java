package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueItem {

    @SerializedName("venue")
    FsqExploredVenue fsqExploredResponseGroupItemVenue;

    @SerializedName("tips")
    List<FsqExploredVenueItemTip> fsqExploredResponseGroupItemTips;

    public FsqExploredVenue getFsqExploredResponseGroupItemVenue(){
        return fsqExploredResponseGroupItemVenue;
    }
}
