package assignment.adyen.com.venuesaroundme.location;

/**
 * Created by Zeki on 25/06/2017.
 */

public class LocationUtils {

    public static final String BOTTOM_SHEET_STATE_CHANGED = "adyen.com.venuesaroundme.BOTTOM_SHEET_STATE_CHANGED";
    public static final String MY_LOCATION_RECEIVED = "adyen.com.venuesaroundme.MY_LOCATION_RECEIVED";
    public static final String MY_LOCATION_RECEIVED_NULL = "adyen.com.venuesaroundme.MY_LOCATION_RECEIVED_NULL";
    public static final String MY_LOCATION_RECEIVED_FIRST_TIME = "adyen.com.venuesaroundme.MY_LOCATION_RECEIVED_FIRST_TIME";
    public static final int LOCATION_UPDATE_INTERVAL_IN_MS = 30000;
    public static final int LOCATION_UPDATE_FASTEST_INTERVAL_IN_MS = 20000;
    public static final int LOCATION_UPDATE_DELAY_IN_MS = 10000;

    /**
     *
     * Formatted query param for the API request
     *
     * @param latitute
     * @param longitude
     * @return lat,lon
     */
    public static String getFormattedLatitudeAndLongitude(double latitute, double longitude) {
        String formatted = String.valueOf(latitute) + ',' + String.valueOf(longitude);
        return formatted;
    }


}
