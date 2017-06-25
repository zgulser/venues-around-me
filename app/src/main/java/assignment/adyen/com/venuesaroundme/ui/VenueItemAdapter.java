package assignment.adyen.com.venuesaroundme.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import assignment.adyen.com.venuesaroundme.model.entities.FsqExploredVenue;
import plugins.backbase.com.baseapplication.R;
import plugins.backbase.com.baseapplication.core.model.entities.Repo;
import plugins.backbase.com.baseapplication.core.networking.imagerequest.VolleyImageRequestController;

/**
 * Created by Backbase R&D B.V on 15/06/2017.
 */

public class VenueItemAdapter extends RecyclerView.Adapter<VenueItemAdapter.CustomItemHolder>{

    private List<FsqExploredVenue> repoList;
    private IAdapterListener adapterListenerImpl;

    public interface IAdapterListener {}

    VenueItemAdapter(List<FsqExploredVenue> repoList){
        this.repoList = repoList;
    }

    @Override
    public CustomItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_item, parent, false);
        ((RecyclerView.LayoutParams)view.getLayoutParams()).setMargins(20, 20, 20, 20);
        return new CustomItemHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomItemHolder holder, int position) {
        holder.repoName.setText(repoList.get(position).getVenueName());
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    private void setupAdapterListener(Context context) {
        if (context instanceof IAdapterListener){
            adapterListenerImpl = (IAdapterListener) context;
        }
    }

    public static class CustomItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        public TextView repoName;
        public NetworkImageView repoAvatar;

        CustomItemHolder(View itemView){
            super(itemView);
            repoName = (TextView) itemView.findViewById(R.id.repoName);
            repoAvatar = (NetworkImageView) itemView.findViewById(R.id.repoAvatar);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }


}
