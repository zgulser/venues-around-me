package assignment.adyen.com.venuesaroundme.networking;

import assignment.adyen.com.venuesaroundme.model.entities.FsqVenueRequestRoot;

/**
 * Created by Zeki on 25/06/2017.
 */

public interface IFsqVenueRequestObserver {
    void onFsqVenueListDownloaded(FsqVenueRequestRoot fsqRequestResult) ;
}
