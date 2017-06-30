package assignment.adyen.com.venuesaroundme.ui;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.databinding.VenueItemBinding;
import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import assignment.adyen.com.venuesaroundme.networking.imagerequests.VolleyImageRequestController;

/**
 * Created by Zeki on 27/06/2017.
 */

public class VenueItemAdapter extends RecyclerView.Adapter<VenueItemAdapter.VenueItemHolder>{

    private List<FsqExploredVenue> venueList;
    private IListItemClickListener  listItemClickListenerImpl;

    public interface IListItemClickListener {
        void onListItemClick(FsqExploredVenue venue);
    }

    public VenueItemAdapter(IListItemClickListener listItemClickListener, List<FsqExploredVenue> venueList){
        this.listItemClickListenerImpl = listItemClickListener;
        this.venueList = venueList;
    }

    @Override
    public VenueItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        VenueItemBinding venueItemBinding = VenueItemBinding.inflate(layoutInflater, parent, false);
        ((RecyclerView.LayoutParams)venueItemBinding.getRoot().getLayoutParams()).setMargins(30, 10, 30, 10);
        return new VenueItemHolder(venueItemBinding);
    }

    @Override
    public void onBindViewHolder(VenueItemHolder venueItemHolder, int position) {
        FsqExploredVenue venue = venueList.get(position);
        venueItemHolder.bind(venue);
    }

    @Override
    public int getItemCount() {
        return venueList.size();
    }

    public class VenueItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final VenueItemBinding venueItemBinding;

        public VenueItemHolder(VenueItemBinding venueItemBinding){
            super(venueItemBinding.getRoot());
            venueItemBinding.getRoot().setOnClickListener(this);
            this.venueItemBinding = venueItemBinding;
        }

        public void bind(FsqExploredVenue venue) {
            venueItemBinding.setVenue(venue);
            venueItemBinding.executePendingBindings();

            venueItemBinding.venueRating.setTextColor(Color.parseColor("#" + venue.getRatingColor()));
            venueItemBinding.venueAvatar.setDefaultImageResId(R.drawable.venue_default_icon);
            venueItemBinding.venueAvatar.setErrorImageResId(R.drawable.venue_default_icon);
            venueItemBinding.venueAvatar.setImageUrl(getImageUrl(venue), VolleyImageRequestController.getInstance().getImageLoader());
        }

        private String getImageUrl(FsqExploredVenue venue){
            return venue.getCategories().get(0).getIcon().getUrlPrefix() +
                    venue.getCategories().get(0).getIcon().getUrlSuffix();
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            listItemClickListenerImpl.onListItemClick(venueList.get(position));
        }
    }
}
