package assignment.adyen.com.venuesaroundme.networking;

import java.util.Map;

import assignment.adyen.com.venuesaroundme.model.entities.FsqVenueRequestRoot;
import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Zeki on 25/06/2017.
 */

public interface IFsqVenueExplorerService {
    @GET(NetworkingUtils.VENUE_EXPLORER_REQUEST_PATH)
    Call<FsqVenueRequestRoot> listFsqVenues(@QueryMap Map<String, String> options);
}
