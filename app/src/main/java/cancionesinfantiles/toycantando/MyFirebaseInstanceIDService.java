package cancionesinfantiles.toycantando;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import Negocio.Constantes;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    public MyFirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("TOKEN", "Refreshed token: " + refreshedToken);

        //Save Token in User Preferences
        SharedPreferences settings;
        settings = getSharedPreferences(Constantes.Preferences, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("FCM", refreshedToken);
        editor.commit();
    }
}
