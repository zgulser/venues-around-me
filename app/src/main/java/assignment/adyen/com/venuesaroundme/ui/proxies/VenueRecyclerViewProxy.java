package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import assignment.adyen.com.venuesaroundme.model.container.FsqVenueContainer;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.ui.VenueItemAdapter;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;

/**
 * Created by Zeki on 27/06/2017.
 */

public class VenueRecyclerViewProxy {

    private VenueItemAdapter venuesItemAdapter;
    private VenuesMapActivity venuesMapActivity;
    private RecyclerView venueListRecyclerView;

    public VenueRecyclerViewProxy(RecyclerView venueListRecyclerView, VenuesMapActivity mapsActivity){
        this.venueListRecyclerView = venueListRecyclerView;
        this.venuesMapActivity = mapsActivity;

        initRecyclerView();
    }

    private void initRecyclerView() {
        venueListRecyclerView.setHasFixedSize(true);
        venueListRecyclerView.setLayoutManager(new LinearLayoutManager(venuesMapActivity));
    }

    public void setRecyclerViewAdapter(){
        double myLat = venuesMapActivity.getLocationProviderProxy().getMyPosition().latitude;
        double myLng = venuesMapActivity.getLocationProviderProxy().getMyPosition().longitude;
        venuesItemAdapter = new VenueItemAdapter(venuesMapActivity, FsqVenueContainer.getInstance().getFsqVenueList(myLat, myLng));
        venueListRecyclerView.setAdapter(venuesItemAdapter);
    }

    public void refreshVenueAdapter(){
        venuesItemAdapter.notifyDataSetChanged();
    }
}
