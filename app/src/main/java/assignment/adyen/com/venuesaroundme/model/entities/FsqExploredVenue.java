package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zeki on 25/06/2017.
 */

public class FsqExploredVenue {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("contact")
    FsqExploredVenueContact contactInfo;

    @SerializedName("location")
    FsqExploredVenueLocation location;

    @SerializedName("rating")
    String rating;

    @SerializedName("ratingColor")
    String ratingColor;

    @SerializedName("categories")
    List<FsqExploredVenueCategory> categories;

    @SerializedName("photos")
    FsqExploredVenuePhoto photos;

    @SerializedName("hours")
    FsqExploredVenueHours hours;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRating() {
        return rating;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public List<FsqExploredVenueCategory> getCategories() {
        return categories;
    }

    public FsqExploredVenuePhoto getPhotos() {
        return photos;
    }

    public FsqExploredVenueHours getHours() {
        return hours;
    }

    public FsqExploredVenueContact getContactInfo() {
        return contactInfo;
    }

    public FsqExploredVenueLocation getLocation() {
        return location;
    }

}
