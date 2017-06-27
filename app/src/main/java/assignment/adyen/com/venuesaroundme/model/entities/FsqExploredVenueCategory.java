package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Backbase R&D B.V on 26/06/2017.
 */

public class FsqExploredVenueCategory {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("pluralName")
    String pluralName;

    @SerializedName("icon")
    FsqExploredVenueCategoryIcon icon;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPluralName() {
        return pluralName;
    }

    public FsqExploredVenueCategoryIcon getIcon() {
        return icon;
    }

}
