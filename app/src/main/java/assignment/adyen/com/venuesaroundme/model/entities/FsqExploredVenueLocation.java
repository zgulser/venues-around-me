package assignment.adyen.com.venuesaroundme.model.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Zeki
 */

public class FsqExploredVenueLocation {

    @SerializedName("address")
    String address;

    @SerializedName("crossStreet")
    String crossStreet;

    @SerializedName("lat")
    double latitude;

    @SerializedName("lng")
    double longitude;

    @SerializedName("distance")
    int distance;

    @SerializedName("postalCode")
    String postalCode;

    @SerializedName("cc")
    String countryCode;

    @SerializedName("city")
    String city;

    @SerializedName("country")
    String country;

    @SerializedName("formattedAddress")
    List<String> fullAddressList;

    public String getAddress() {
        return address;
    }

    public String getCrossStreet() {
        return crossStreet;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getDistance() {
        return distance;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public List<String> getFullAddressList() {
        return fullAddressList;
    }
}
