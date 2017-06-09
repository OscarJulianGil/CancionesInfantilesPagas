package cancionesinfantiles.toycantando;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * Created by ogil on 12/04/2017.
 */

public final class CustomApplicationClass extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Branch object
        Branch.getAutoInstance(this);
    }
}