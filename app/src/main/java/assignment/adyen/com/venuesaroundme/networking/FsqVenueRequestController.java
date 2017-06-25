package assignment.adyen.com.venuesaroundme.networking;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import assignment.adyen.com.venuesaroundme.application.FsqVenuesApplication;
import assignment.adyen.com.venuesaroundme.location.LocationUtils;
import assignment.adyen.com.venuesaroundme.location.TestLocationProvider;
import assignment.adyen.com.venuesaroundme.model.entities.FsqVenueRequestRoot;
import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Backbase R&D B.V on 25/06/2017.
 */

public class FsqVenueRequestController implements IWebRequestContract{

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

    public IFsqVenueExplorerService getFsqVenueExplorerService() {
        return fsqVenueExplorerService;
    }

    public void addFsqVenueRequestObserver(IFsqVenueRequestObserver fsqVenueRequestObserver) {
        fsqVenueRequestObservers.add(fsqVenueRequestObserver);
    }

    public void removeFsqVenueRequestObserver(IFsqVenueRequestObserver fsqVenueRequestObserver) {
        fsqVenueRequestObservers.remove(fsqVenueRequestObserver);
    }

    @Override
    public void get(boolean isAsync) {
        Call call = fsqVenueExplorerService.listFsqVenues(getQueryParameters());
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
                System.out.println("call url" + call.request().url());
                LocalBroadcastManager.getInstance(FsqVenuesApplication.getAppContext()).
                        sendBroadcast(new Intent(NetworkingUtils.REQUEST_FAILED_BROADCAST));
            }
        });
    }

    private Map<String, String> getQueryParameters() {
        Map<String, String> queryMap = new TreeMap<>();
        queryMap.put(NetworkingUtils.POSITION_QUERY_PARAM_KEY,
                LocationUtils.getFormattedLatitudeAndLongitude(
                      TestLocationProvider.getInstance().testLatitude,
                      TestLocationProvider.getInstance().testLongitude));
        queryMap.put(NetworkingUtils.CLIENT_ID_QUERY_PARAM_KEY, NetworkingUtils.CLIENT_ID_QUERY_PARAM_VALUE);
        queryMap.put(NetworkingUtils.CLIENT_SECRET_QUERY_PARAM_KEY, NetworkingUtils.CLIENT_SECRET_QUERY_PARAM_VALUE);
        queryMap.put(NetworkingUtils.VERSION_QUERY_PARAM_KEY, NetworkingUtils.VERSION_QUERY_PARAM_VALUE);
        return queryMap;
    }

    @Override
    public void post(boolean isAsync) {
        // TODO: Later when post request needed
    }
}
