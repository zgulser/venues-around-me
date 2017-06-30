package assignment.adyen.com.venuesaroundme.ui.proxies;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import assignment.adyen.com.venuesaroundme.R;
import assignment.adyen.com.venuesaroundme.ui.VenuesMapActivity;
import assignment.adyen.com.venuesaroundme.ui.specialobjects.CustomPlaceSelectionListener;
import assignment.adyen.com.venuesaroundme.ui.specialobjects.CustomTextWatcher;

/**
 * Created by Zeki on 28/02/2017.
 */

public class SearchUIProxy implements View.OnClickListener{
    private Activity activity;
    private SearchView searchView;
    private Button customSearchButton;
    private SearchActionsListener searchActionsListener;
    private EditText searchEditText;
    private ImageButton navigationButton;
    private ImageButton clearSearchButton;
    private SearchMode searchMode;

    public enum SearchMode {
        SEARCH_MORE_ON_MAP,
        SEARCH_MORE_ON_LIST
    }

    public interface SearchActionsListener{
        void onNavigateToMapViewIconClicked();
        void onNavigateToListViewIconClicked();
    }

    public SearchUIProxy(VenuesMapActivity venuesMapActivity, int placesFragmentId, SearchView searchView, Button customSearchButton){
        this.activity = venuesMapActivity;
        this.searchView = searchView;
        this.customSearchButton = customSearchButton;
        this.searchActionsListener = venuesMapActivity;
        this.searchMode = SearchMode.SEARCH_MORE_ON_MAP;

        initPlacesSearchFragment(placesFragmentId);
        initListeners();
    }

    private void initPlacesSearchFragment(int placesFragmentId) {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                activity.getFragmentManager().findFragmentById(placesFragmentId);

        initPlaceSelectionListener(autocompleteFragment);
        initVenuesSearchButton(autocompleteFragment);
        initPlacesClearButton(autocompleteFragment);
        initEdittextListener(autocompleteFragment);
    }

    private void initPlaceSelectionListener(PlaceAutocompleteFragment autocompleteFragment){
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder().setTypeFilter(
                AutocompleteFilter.TYPE_FILTER_ADDRESS).build();
        autocompleteFragment.setFilter(typeFilter);
        autocompleteFragment.setOnPlaceSelectedListener(new CustomPlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //TODO: implement this when you add search feature with a custom content provider
            }
        });
    }

    private void initEdittextListener(PlaceAutocompleteFragment autocompleteFragment){
        searchEditText = (EditText) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        searchEditText.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                clearSearchButton.setVisibility(View.VISIBLE);
                if(editable.toString().isEmpty()){
                    clearSearchButton.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     *
     * Modifies 'Search' button on the search bar, mimic maps behaviour
     *
     * @param autocompleteFragment
     */
    private void initVenuesSearchButton(PlaceAutocompleteFragment autocompleteFragment) {
        navigationButton = (ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button);
        navigationButton.setImageResource(R.drawable.ic_menu);
        navigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchMode == SearchMode.SEARCH_MORE_ON_MAP){
                    navigateToListMode();
                } else {
                    navigateToMapMode();
                }
            }
        });
    }

    public void navigateToListMode(){
        searchMode = SearchMode.SEARCH_MORE_ON_LIST;
        navigationButton.setImageResource(R.drawable.ic_arrow_back);
        searchActionsListener.onNavigateToListViewIconClicked();
    }

    public void navigateToMapMode(){
        searchMode = SearchMode.SEARCH_MORE_ON_MAP;
        navigationButton.setImageResource(R.drawable.ic_menu);
        searchActionsListener.onNavigateToMapViewIconClicked();
    }

    private void initPlacesClearButton(PlaceAutocompleteFragment autocompleteFragment) {
        clearSearchButton = (ImageButton) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button);
        clearSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
            }
        });
    }

    private void initListeners() {
        customSearchButton.setOnClickListener(this);
        setOpenSearchIconClickListener();
        setCloseSearchIconClickListener();
    }

    private void setOpenSearchIconClickListener(){
        //TODO: implement this when you add search feature with a custom content provider
    }

    private void setCloseSearchIconClickListener(){
        //TODO: implement this when you add search feature with a custom content provider
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == customSearchButton.getId()){
            //TODO: implement this when you add search feature with a custom content provider
        }
    }
}
