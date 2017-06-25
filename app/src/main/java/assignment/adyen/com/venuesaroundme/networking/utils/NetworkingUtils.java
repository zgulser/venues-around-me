package assignment.adyen.com.venuesaroundme.networking.utils;

/**
 * Created by Zeki on 25/06/2017.
 */

public class NetworkingUtils {
    public static final String BASE_URL = "https://api.foursquare.com/";
    public static final String VENUE_EXPLORER_REQUEST_PATH = "/v2/venues/explore/";
    public static final String POSITION_QUERY_PARAM_KEY = "ll";
    public static final String CLIENT_ID_QUERY_PARAM_KEY = "client_id";
    public static final String CLIENT_ID_QUERY_PARAM_VALUE="YUDQWPH1UMOZHA5ULATOIOORQGRXIRNXXIOIMB2SYTAAZSTA";
    public static final String CLIENT_SECRET_QUERY_PARAM_KEY = "client_secret";
    public static final String CLIENT_SECRET_QUERY_PARAM_VALUE="1VBJW5L0OWAQ0G134WVQSYDCOZN0OWBOXBPG5TUMWQAFBR44";
    public static final String VERSION_QUERY_PARAM_KEY = "v";
    public static final String VERSION_QUERY_PARAM_VALUE="20170630";
    public static final String REQUEST_FAILED_BROADCAST = "com.zekigu.venues.request.failed_broadcast";
    public static final String REQUEST_SUCCEDED_BROADCAST = "com.zekigu.testapp.request.succeeded_broadcast";
    public static enum RequestType  {
        GET_REQUEST,
        POST_REQUEST
    };
}
