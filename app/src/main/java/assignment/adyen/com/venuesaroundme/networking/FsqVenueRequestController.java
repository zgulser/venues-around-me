package assignment.adyen.com.venuesaroundme.networking;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.model.entities.FsqVenueRequestRoot;
import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;
import assignment.adyen.com.venuesaroundme.testhelpers.VenueTestable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqVenueRequestController implements IVenueRequestContract {

    private static FsqVenueRequestController fsqVenueRequestController;
    private IFsqVenueExplorerService fsqVenueExplorerService;
    private List<IFsqVenueRequestObserver> fsqVenueRequestObservers;

    private FsqVenueRequestController() {
        initObservers();
        initRetrofit();
    }

    public static synchronized FsqVenueRequestController getInstance() {
        if(fsqVenueRequestController == null) {
            fsqVenueRequestController = new FsqVenueRequestController();
        }

        return fsqVenueRequestController;
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkingUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fsqVenueExplorerService = retrofit.create(IFsqVenueExplorerService.class);
    }

    private void initObservers(){
        fsqVenueRequestObservers = new ArrayList<>();
    }

    public void addFsqVenueRequestObserver(IFsqVenueRequestObserver fsqVenueRequestObserver) {
        fsqVenueRequestObservers.add(fsqVenueRequestObserver);
    }

    public void removeFsqVenueRequestObserver(IFsqVenueRequestObserver fsqVenueRequestObserver) {
        fsqVenueRequestObservers.remove(fsqVenueRequestObserver);
    }

    @Override
    public void get(boolean isAsync, double myLat, double myLng) {
        Call call = fsqVenueExplorerService.listFsqVenues(getQueryParameters(myLat, myLng));
        call.enqueue(new Callback<FsqVenueRequestRoot>() { // async retrofit request
            @Override
            public void onResponse(Call<FsqVenueRequestRoot> call, Response<FsqVenueRequestRoot> response) {
                if (response.isSuccessful()) {
                    for (IFsqVenueRequestObserver observer : fsqVenueRequestObservers){
                        observer.onFsqVenueListDownloaded(response.body());
                    }
                } else {
                    LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).
                            sendBroadcast(new Intent(NetworkingUtils.REQUEST_FAILED_BROADCAST));
                }
            }

            @Override
            public void onFailure(Call<FsqVenueRequestRoot> call, Throwable t) {
                LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).
                        sendBroadcast(new Intent(NetworkingUtils.REQUEST_FAILED_BROADCAST));
            }
        });
    }

    @VenueTestable
    private Map<String, String> getQueryParameters(double myLat, double myLng) {
        Map<String, String> queryMap = new TreeMap<>();
        queryMap.put(NetworkingUtils.POSITION_QUERY_PARAM_KEY, LocationUtils.getFormattedLatitudeAndLongitude(myLat, myLng));
        queryMap.put(NetworkingUtils.CLIENT_ID_QUERY_PARAM_KEY, NetworkingUtils.CLIENT_ID_QUERY_PARAM_VALUE);
        queryMap.put(NetworkingUtils.CLIENT_SECRET_QUERY_PARAM_KEY, NetworkingUtils.CLIENT_SECRET_QUERY_PARAM_VALUE);
        queryMap.put(NetworkingUtils.VERSION_QUERY_PARAM_KEY, NetworkingUtils.VERSION_QUERY_PARAM_VALUE);
        queryMap.put(NetworkingUtils.RADIUS_QUERY_PARAM_KEY, String.valueOf(LocationUtils.surroundingRadius));
        return queryMap;
    }

    @Override
    public void post(boolean isAsync) {
        // TODO: Later when post request needed
    }
}
