package assignment.adyen.com.venuesaroundme.application;

import android.content.Context;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Backbase R&D B.V on 28/02/2017.
 */

public class AppUtils {

    public static boolean isPlayServicesInstalled(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        return (googleAPI.isGooglePlayServicesAvailable(context) == ConnectionResult.SUCCESS);
    }
}
