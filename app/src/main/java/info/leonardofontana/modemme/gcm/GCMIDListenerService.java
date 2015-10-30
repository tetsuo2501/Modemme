package info.leonardofontana.modemme.gcm;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

import info.leonardofontana.modemme.RegistrationIntentService;

/**
 * Created by tetsu on 28/10/2015.
 */
public class GCMIDListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}
