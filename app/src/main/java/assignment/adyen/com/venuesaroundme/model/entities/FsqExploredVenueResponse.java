package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueResponse {

    @SerializedName("groups")
    List<FsqExploredVenueGroup> fsqExploredResponseGroups;

    public List<FsqExploredVenueGroup> getFsqExploredResponseGroups(){
        return fsqExploredResponseGroups;
    }
}
