package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Zeki on 25/06/2017.
 */

public class FsqExploredVenue {

    @SerializedName("id")
    String venueId;

    @SerializedName("name")
    String venueName;

    public String getVenueId() {
        return venueId;
    }

    public String getVenueName() {
        return venueName;
    }
}
