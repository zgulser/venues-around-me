package assignment.adyen.com.venuesaroundme.location;

/**
 * Created by Backbase R&D B.V on 26/06/2017.
 */

public interface ILocationSettingObserver {
    void onLocationSettingsRequestSuccess();
    void onLocationSettingsRequestFailed(int statusCode);
}
