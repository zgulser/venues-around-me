package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Backbase R&D B.V on 26/06/2017.
 */

public class FsqExploredVenueHours {

    @SerializedName("isOpen")
    boolean isOpen;

    public boolean isOpen() {
        return isOpen;
    }

}
