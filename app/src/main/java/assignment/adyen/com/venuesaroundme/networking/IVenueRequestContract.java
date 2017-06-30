package assignment.adyen.com.venuesaroundme.networking;

/**
 * Created by Zeki on 25/06/2017.
 */

public interface IVenueRequestContract {
    void get(boolean isAsync, double myLocationLatitude, double myLocationLongitude);
    void post(boolean isAsync);
}
