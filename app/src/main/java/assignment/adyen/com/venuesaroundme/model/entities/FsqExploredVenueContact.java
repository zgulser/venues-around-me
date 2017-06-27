package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueContact {

    @SerializedName("twitter")
    String twitterID;

    @SerializedName("instagram")
    String instagramID;

    public String getTwitterID() {
        return twitterID;
    }

    public String getInstagramID() {
        return instagramID;
    }

}
