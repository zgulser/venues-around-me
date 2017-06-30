package assignment.adyen.com.venuesaroundme;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import assignment.adyen.com.venuesaroundme.location.LocationProviderProxy;
import assignment.adyen.com.venuesaroundme.location.TestLocationProvider;
import assignment.adyen.com.venuesaroundme.networking.FsqVenueRequestController;
import assignment.adyen.com.venuesaroundme.networking.IFsqVenueExplorerService;
import assignment.adyen.com.venuesaroundme.networking.VenueTestable;
import assignment.adyen.com.venuesaroundme.networking.utils.NetworkingUtils;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static org.junit.Assert.*;

/**
 * Created by Backbase R&D B.V on 30/06/2017.
 */

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@MediumTest
public class NetworkAPITest {

    private IFsqVenueExplorerService fsqVenueExplorerService;

    @Before
    public void setup(){}

    @Test
    public void test1InitRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetworkingUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        fsqVenueExplorerService = retrofit.create(IFsqVenueExplorerService.class);

        assertTrue(fsqVenueExplorerService != null);
    }

    @Test
    public void test2GetQueryParameters() throws InvocationTargetException, IllegalAccessException {
        FsqVenueRequestController fsqVenueRequestController = FsqVenueRequestController.getInstance();
        Map<String, String> queryParams = null;
        final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(fsqVenueRequestController.getClass().getDeclaredMethods()));
        for(Method method : allMethods){
            if(method.isAnnotationPresent(VenueTestable.class)){
                method.setAccessible(true);
                queryParams = (Map<String, String>) method.invoke(fsqVenueRequestController,
                        TestLocationProvider.testLatitude, TestLocationProvider.testLongitude);
            }
        }

        assertTrue(queryParams.size() == 5);
    }
}

