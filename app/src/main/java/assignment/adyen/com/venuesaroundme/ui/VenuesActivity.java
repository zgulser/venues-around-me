package assignment.adyen.com.venuesaroundme.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import assignment.adyen.com.venuesaroundme.R;

public class VenuesActivity extends AppCompatActivity {

    private RecyclerView venueRecylerView;
    private VenueItemAdapter venueItemAdapter;
    private VenueRequestListenerProxy broadcastReceiverProxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        setupAppBar();
        setupBroadcastReceiver();
        setupRecyclerView();
        setupLoadingView();
    }

    private void setupBroadcastReceiver(){
        broadcastReceiverProxy = new BroadcastReceiverProxy(RepoActivity.this);
    }

    private void setupAppBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setupLoadingView(){
        loadingIndicator = (TextView) findViewById(R.id.loadingView);
        loadingIndicator.setVisibility(View.GONE);
        contentScrim = findViewById(R.id.contentScrim);
        contentScrim.setVisibility(View.GONE);
    }

    private void setupRecyclerView(){
        repoRecylerView = (RecyclerView) findViewById(R.id.testRecylerView);
        repoRecylerView.setHasFixedSize(true);
        repoRecylerView.setLayoutManager(new LinearLayoutManager(RepoActivity.this));
        addAdapter();
    }

    private void addAdapter() {
        repoItemAdapter = new VenueItemAdapter(RepoContainer.getInstance().getRepoList());
        repoRecylerView.setAdapter(repoItemAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        broadcastReceiverProxy.registerBroadcastReceiverProxy();
    }

    @Override
    protected void onStop() {
        super.onStop();

        broadcastReceiverProxy.unregisterBroadcastReceiverProxy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void updateList(){
        repoItemAdapter.notifyDataSetChanged();
    }
}
