package info.leonardofontana.modemme.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.content.Intent;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.gms.gcm.GcmListenerService;

import info.leonardofontana.modemme.R;
import info.leonardofontana.modemme.MainActivity;
import info.leonardofontana.modemme.util.SyncUtil;
import info.leonardofontana.modemme.util.VolleyRequestQueue;

/**
 * Created by tetsu on 28/10/2015.
 */
public class GCMListenerService extends GcmListenerService {
    private static final String TAG = "GCMListenerService";
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("message");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);

        if (from.startsWith("/topics/")) {
            Log.d("TAG","Messaggio ricevuto:" + data.toString());
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        SyncUtil.triggerRefresh();
        Context context = getApplicationContext();
        if(
            PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(context.getString(R.string.stato_notifiche),true))
            sendNotification(data);
        // [END_EXCLUDE]
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(final Bundle message) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        final Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String img = message.getString("image");
        final Context context = this;

        if(img != null || img.length() >10)
            VolleyRequestQueue.getInstance(this).addToRequestQueue(
                    new ImageRequest(
                            img,
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap response) {
                                    //Lancia notifica con immage
                                    ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                                            .notify(0,
                                                    new NotificationCompat.Builder(context)
                                                            .setSmallIcon(R.drawable.icona_bianca)
                                                            .setContentTitle(context.getString(R.string.titoloNotifica))
                                                            //.setContentText(message.getString("titolo"))
                                                            .setAutoCancel(true)
                                                            .setSound(defaultSoundUri)
                                                            .setContentIntent(pendingIntent)
                                                            .setStyle(new NotificationCompat.BigPictureStyle()
                                                                    .bigPicture(response)
                                                                    .setSummaryText(message.getString("titolo"))
                                                            )
                                                            .build());

                                }
                            },
                    0,
                    0,
                    null, null,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                                    .notify(0,
                                            new NotificationCompat.Builder(context)
                                                    .setSmallIcon(R.drawable.icona_bianca)
                                                    .setContentTitle(context.getString(R.string.titoloNotifica))
                                                    .setContentText(message.getString("titolo"))
                                                    .setAutoCancel(true)
                                                    .setSound(defaultSoundUri)
                                                    .setContentIntent(pendingIntent)
                                                    .build());
                        }
                    }
                    ));




    }
}
