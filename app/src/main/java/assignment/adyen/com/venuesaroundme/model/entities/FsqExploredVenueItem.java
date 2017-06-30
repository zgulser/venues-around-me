package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueItem {

    @SerializedName("venue")
    FsqExploredVenue venue;

    @SerializedName("tips")
    List<FsqExploredVenueItemTip> tips;

    public FsqExploredVenue getVenue(){
        return venue;
    }

    public List<FsqExploredVenueItemTip> getTips() {
        return tips;
    }
}
