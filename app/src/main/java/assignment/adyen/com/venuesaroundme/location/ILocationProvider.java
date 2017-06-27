package assignment.adyen.com.venuesaroundme.location;

import com.google.android.gms.location.LocationRequest;

/**
 * Created by Zeki
 */

public interface ILocationProvider extends ILocationSettingObserver{
    double[] getMyLatitudeAndLongitude();
}
