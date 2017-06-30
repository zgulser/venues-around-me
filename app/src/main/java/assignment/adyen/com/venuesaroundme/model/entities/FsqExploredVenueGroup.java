package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqExploredVenueGroup {

    @SerializedName("type")
    String type;

    @SerializedName("name")
    String name;

    @SerializedName("items")
    List<FsqExploredVenueItem> items;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<FsqExploredVenueItem> getItems(){
        return items;
    }
}
