package assignment.adyen.com.venuesaroundme.networking;

/**
 * Created by Zeki on 25/06/2017.
 */

public interface IWebRequestContract {
    void get(boolean isAsync);
    void post(boolean isAsync);
}
