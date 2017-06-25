package assignment.adyen.com.venuesaroundme.application;

import android.app.Application;
import android.content.Context;

import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.networking.FsqVenueRequestController;

/**
 * Created by Zeki on 25/06/2017.
 */

public class FsqVenuesApplication extends Application{

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();

        initApp();
    }

    private void initApp(){
        setAppContext();
        addObservers();
    }

    private void setAppContext(){
        appContext = getApplicationContext();
    }

    private void addObservers(){
        FsqVenueRequestController.getInstance().addFsqVenueRequestObserver(FsqVenueContainer.getInstance());
    }

    public static Context getAppContext() {
        return appContext;
    }
}
