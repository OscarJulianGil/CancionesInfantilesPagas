package cancionesinfantiles.toycantando;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

/**
 * Created by ogil on 04/04/2017.
 */
class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey", null);
            if (customKey != null)
                Log.i("OneSignalExample", "customkey set with value: " + customKey);
        }
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {

    }
}
