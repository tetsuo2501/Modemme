package info.leonardofontana.modemme.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.leonardofontana.modemme.model.FeedContract;
import info.leonardofontana.modemme.model.FeedParser;
import info.leonardofontana.modemme.util.VolleyRequestQueue;
import info.leonardofontana.modemme.util.VolleyXMLRequest;

/**
 * Created by tetsu on 22/10/2015.
 */
public class SyncAdapter  extends AbstractThreadedSyncAdapter {
    public static final String TAG = "SyncAdapter";
    private static final String FEED_URL = "http://www.modemme.com/feed";

    private Context context;
    private ContentResolver contentResolver;


    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[] {
            FeedContract.Entry._ID,
            FeedContract.Entry.COLUMN_NAME_ENTRY_ID,
            FeedContract.Entry.COLUMN_NAME_TITLE,
            FeedContract.Entry.COLUMN_DESCRIPTION,
            FeedContract.Entry.COLUMN_NAME_LINK,
            FeedContract.Entry.COLUMN_MEDIA_LINK,
            FeedContract.Entry.COLUMN_NUMBERS_COMMENT,
            FeedContract.Entry.COLUMN_COMMENT,
            FeedContract.Entry.COLUMN_CONTENT,
            FeedContract.Entry.COLUMN_NAME_PUBLISHED,
            FeedContract.Entry.COLUMN_AUTHOR
    };
    public final static int COL_ID = 0;
    public final static int COL_ENTRY_ID = 1;
    public final static int COL_TITLE = 2;
    public final static int COL_DESCR = 3;
    public final static int COL_LINK = 4;
    public final static int COL_MEDIA = 5;
    public final static int COL_NUM_COMM = 6;
    public final static int COL_COMM = 7;
    public final static int COL_CONTENT = 8;
    public final static int COL_PUB = 9;
    public final static int COL_AUTH = 10;


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver = context.getContentResolver();
        this.context = context;
    }



    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "Beginning network synchronization");
        VolleyRequestQueue.getInstance(context).addToRequestQueue(
                new VolleyXMLRequest(Request.Method.GET,
                        FEED_URL,
                        new parseResponse(syncResult),
                        new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG,"Errore durante la richiesta http");

                            }
                        }
                )
        );
    }

    class parseResponse implements Response.Listener<Document> {
        SyncResult syncResult;

        public parseResponse(SyncResult syncResult) {
            this.syncResult = syncResult;
        }

        @Override
        public void onResponse(Document response) {
            if(response == null)
                Log.d(TAG,"response null");
            final FeedParser feedParser = new FeedParser();
            ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();
            HashMap<String, FeedParser.Entry> entryMap = new HashMap<String, FeedParser.Entry>();
            final List<FeedParser.Entry> entries = feedParser.parse(response);

            if(entries == null){
                Log.e(TAG,"Il documento è nullo!!!!");
                return;
            }
            for (FeedParser.Entry e : entries) {
                entryMap.put(e.id, e);
            }


            Log.i(TAG, "Fetching local entries for merge");
            Uri uri = FeedContract.Entry.CONTENT_URI; // Get all entries
            Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
            assert c != null;
            Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");


            //todo: Inserire logica aggiornamento database
            while (c.moveToNext()) {
                Log.d(TAG, "sposto il cursore");
                syncResult.stats.numEntries++;
                Log.d(TAG, "++entries");
                int id = c.getInt(COL_ID);
                Log.d(TAG, "Get id" + id);
                String entryId = c.getString(COL_ENTRY_ID);
                String title = c.getString(COL_TITLE);
                String link = c.getString(COL_LINK);
                long published = c.getLong(COL_PUB);
                String author = c.getString(COL_AUTH);
                String content = c.getString(COL_CONTENT);
                String description = c.getString(COL_DESCR);
                int numCommenti = c.getInt(COL_NUM_COMM);
                String media = c.getString(COL_MEDIA);
                String commentsLink = c.getString(COL_COMM);
                FeedParser.Entry match = entryMap.get(entryId);
                Log.d(TAG, "fine ritiro dei valori da db");
                if (match != null) {

                    // Entry exists. Remove from entry map to prevent insert later.
                    entryMap.remove(entryId);
                    // Check to see if the entry needs to be updated
                    Uri existingUri = FeedContract.Entry.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    if ((match.titolo != null && !match.titolo.equals(title)) ||
                            (match.link != null && !match.link.equals(link)) ||
                            (match.numeroCommenti != numCommenti) ||
                            (match.descrizione != null && !match.descrizione.equals(description)) ||
                            (match.mediaLink != null && !match.link.equals(media)) ||
                            (match.data != published)) {
                        // Update existing record
                        Log.i(TAG, "Scheduling update: " + existingUri);
                        batch.add(ContentProviderOperation.newUpdate(existingUri)
                                .withValue(FeedContract.Entry.COLUMN_NAME_TITLE, title)
                                .withValue(FeedContract.Entry.COLUMN_NAME_LINK, link)
                                .withValue(FeedContract.Entry.COLUMN_NAME_PUBLISHED, published)
                                .withValue(FeedContract.Entry.COLUMN_AUTHOR, author)
                                .withValue(FeedContract.Entry.COLUMN_CONTENT, content)
                                .withValue(FeedContract.Entry.COLUMN_DESCRIPTION, description)
                                .withValue(FeedContract.Entry.COLUMN_NUMBERS_COMMENT, numCommenti)
                                .withValue(FeedContract.Entry.COLUMN_MEDIA_LINK, media)
                                .withValue(FeedContract.Entry.COLUMN_COMMENT, commentsLink)
                                .build());
                        syncResult.stats.numUpdates++;
                    } else {
                        Log.i(TAG, "No action: " + existingUri);
                    }
                } else {
                    // Entry doesn't exist. Remove it from the database.
                    Uri deleteUri = FeedContract.Entry.CONTENT_URI.buildUpon()
                            .appendPath(Integer.toString(id)).build();
                    Log.i(TAG, "Scheduling delete: " + deleteUri);
                    batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                    syncResult.stats.numDeletes++;
                }
            }
            c.close();
            Log.d(TAG, "Inserisco nuovi valori");
                // Add new items
                for (FeedParser.Entry e : entryMap.values()) {
                    Log.i(TAG, "Scheduling insert: entry_id=" + e.id);
                    batch.add(ContentProviderOperation.newInsert(FeedContract.Entry.CONTENT_URI)
                            .withValue(FeedContract.Entry.COLUMN_NAME_ENTRY_ID, e.id)
                            .withValue(FeedContract.Entry.COLUMN_NAME_TITLE, e.titolo)
                            .withValue(FeedContract.Entry.COLUMN_NAME_LINK, e.link)
                            .withValue(FeedContract.Entry.COLUMN_NAME_PUBLISHED, e.data)
                            .withValue(FeedContract.Entry.COLUMN_CONTENT, e.contenuto)
                            .withValue(FeedContract.Entry.COLUMN_DESCRIPTION, e.descrizione)
                            .withValue(FeedContract.Entry.COLUMN_NUMBERS_COMMENT, e.numeroCommenti)
                            .withValue(FeedContract.Entry.COLUMN_MEDIA_LINK, e.mediaLink)
                            .withValue(FeedContract.Entry.COLUMN_COMMENT,e.linkCommenti)
                            .build());
                    syncResult.stats.numInserts++;
                    Log.i(TAG, "Merge solution ready. Applying batch update");
                    try {
                        contentResolver.applyBatch(FeedContract.CONTENT_AUTHORITY, batch);
                        Log.d(TAG,"Batch effettuato");
                    }
                    catch (RemoteException e1){
                        Log.e(TAG, "Eccezione di remote exception durante inserimento dati",e1);
                    }
                    catch(OperationApplicationException e1){
                        Log.e(TAG, "Eccezione di operazione",e1);
                    }
                    contentResolver.notifyChange(
                            FeedContract.Entry.CONTENT_URI, // URI where data was modified
                            null,                           // No local observer
                            false);                         // IMPORTANT: Do not sync to network
                    // This sample doesn't support uploads, but if *your* code does, make sure you set
                    // syncToNetwork=false in the line above to prevent duplicate syncs.

                }



        }
    }
}