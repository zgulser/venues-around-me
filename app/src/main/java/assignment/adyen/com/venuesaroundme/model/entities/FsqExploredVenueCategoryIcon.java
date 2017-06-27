package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Backbase R&D B.V on 26/06/2017.
 */

public class FsqExploredVenueCategoryIcon {

    @SerializedName("prefix")
    String urlPrefix;

    @SerializedName("suffix")
    String urlSuffix;

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public String getUrlSuffix() {
        return urlSuffix;
    }
}
