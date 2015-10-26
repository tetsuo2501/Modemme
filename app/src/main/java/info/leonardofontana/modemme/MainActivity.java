package info.leonardofontana.modemme;

import android.accounts.Account;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import info.leonardofontana.modemme.accounts.GenericAccountService;
import info.leonardofontana.modemme.model.CardListAdapter;
import info.leonardofontana.modemme.model.FeedContract;
import info.leonardofontana.modemme.model.ProjectionContract;
import info.leonardofontana.modemme.util.SyncUtil;
import info.leonardofontana.modemme.util.VolleyRequestQueue;

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor>{
    private RecyclerView recycler;
    private SwipeRefreshLayout swipeRefresh;
    private CardListAdapter cardListAdapter;
    private Object mSyncObserverHandle;
    private static final String TAG = "Fragment";
    /**
     * Create a new anonymous SyncStatusObserver. It's attached to the app's ContentResolver in
     * onResume(), and removed in onPause(). If status changes, it sets the state of the Refresh
     * button. If a sync is active or pending, the Refresh button is replaced by an indeterminate
     * ProgressBar; otherwise, the button itself is displayed.
     */
    private SyncStatusObserver mSyncStatusObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //////Inizializzo la coda dei download con cache
        VolleyRequestQueue.getInstance(getApplicationContext());

        recycler = (RecyclerView) findViewById(R.id.my_recycler_view);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSyncStatusObserver = new MySyncStatusObserver(swipeRefresh);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        cardListAdapter = new CardListAdapter(this,null);
        recycler.setAdapter(cardListAdapter);

        SyncUtil.createSyncAccount(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "avvio refresh contenuti");
                SyncUtil.triggerRefresh();
            }
        });
        getLoaderManager().initLoader(0, null, this);
    }

    public void refreshFromMenu(MenuItem i){
        Log.d(TAG, "avvio refresh contenuti");
        SyncUtil.triggerRefresh();
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
        Log.d(TAG,"Verifico azione toolbar");
        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            Log.d(TAG,"Pulsante premuto");
            return true;
        }
    */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,  // Context
                FeedContract.Entry.CONTENT_URI, // URI
                ProjectionContract.PROJECTION,  // Projection
                null,                           // Selection
                null,                           // Selection args
                FeedContract.Entry.COLUMN_NAME_PUBLISHED + " desc"); // Sort
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i(TAG,"Fine aggiornamento espongo risultati");
        cardListAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cardListAdapter.changeCursor(null);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSyncStatusObserver.onStatusChanged(0);

        // Watch for sync state changes
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING |
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }


    class MySyncStatusObserver implements SyncStatusObserver {
        /** Callback invoked with the sync adapter status changes. */
        SwipeRefreshLayout swipe;

        public MySyncStatusObserver(SwipeRefreshLayout swipe) {
            this.swipe = swipe;
        }

        @Override
        public void onStatusChanged(int which) {
            runOnUiThread(new Runnable() {
                /**
                 * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
                 * runs on the UI thread.
                 */
                @Override
                public void run() {
                    // Create a handle to the account that was created by
                    // SyncService.CreateSyncAccount(). This will be used to query the system to
                    // see how the sync status has changed.
                    Account account = GenericAccountService.GetAccount();
                    if (account == null) {
                        // GetAccount() returned an invalid value. This shouldn't happen, but
                        // we'll set the status to "not refreshing".
                        swipe.setRefreshing(false);

                        return;
                    }

                    // Test the ContentResolver to see if the sync adapter is active or pending.
                    // Set the state of the refresh button accordingly.
                    boolean syncActive = ContentResolver.isSyncActive(
                            account, FeedContract.CONTENT_AUTHORITY);
                    boolean syncPending = ContentResolver.isSyncPending(
                            account, FeedContract.CONTENT_AUTHORITY);
                    swipe.setRefreshing(syncActive || syncPending);
                }
            });
        }
    }
}
