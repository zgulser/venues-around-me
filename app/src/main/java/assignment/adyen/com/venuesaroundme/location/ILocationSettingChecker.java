package assignment.adyen.com.venuesaroundme.location;

import assignment.adyen.com.venuesaroundme.location.ILocationSettingObserver;

/**
 * Created by Zeki
 */

public interface ILocationSettingChecker {
    void checkLocationSettingsEnabled();
    void addLocationSettingRequestObserver(ILocationSettingObserver locationSettingObserver);
    void removeLocationSettingRequestObserver(ILocationSettingObserver locationSettingObserver);
    void showLocationSettingError();
    void hideMyLocationError();
}
