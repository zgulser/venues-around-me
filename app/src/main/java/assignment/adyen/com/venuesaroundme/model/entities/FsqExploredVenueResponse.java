package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueResponse {

    @SerializedName("suggestedRadius")
    int suggestedRadius;

    @SerializedName("groups")
    List<FsqExploredVenueGroup> groups;

    public int getSuggestedRadius() {
        return suggestedRadius;
    }

    public List<FsqExploredVenueGroup> getGroups(){
        return groups;
    }
}
