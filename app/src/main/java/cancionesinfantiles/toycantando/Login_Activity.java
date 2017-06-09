package cancionesinfantiles.toycantando;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.inmobi.sdk.InMobiSdk;
import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Login_Activity extends AppCompatActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private  VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///Asigna el layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide(); //Oculta la barra del titulo en la actividad
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .init();
        InMobiSdk.init(Login_Activity.this, "530da009dec0484bbc6c0c30f525094d");
        ///Oculta los controles laterales de la pantalla
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for lower api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
        videoView = (VideoView) findViewById(R.id.videoView);
        String path = "android.resource://" + getPackageName() + "/" + R.raw.intro;
        videoView.setVideoPath(path);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent activity = new Intent(getBaseContext(),Gallery_ACtivity.class);
                startActivity(activity);
            }
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        addOneSignalTag();
    }

    private void addOneSignalTag(){
        OneSignal.sendTag("app","canciones_infantiles_2");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(videoView != null){
            videoView.start();
        }
    }

    /*Cuando la actividad queda en backgroud y el usuario retorna, ejecuta este codigo*/
    @Override
    public void onResume() {
        super.onResume();
        if(videoView != null){
            videoView.start();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        branch.initSession(new Branch.BranchUniversalReferralInitListener() {
            @Override
            public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    Log.e("BRANCH OK","BRANCH OK");
                    //Branch.getInstance(getApplicationContext()).userCompletedAction("signup");
                } else {
                    Log.e("BRANCH ERROR", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);
    }

    /*Cuando la actividad queda en backgroud e invisible*/
    @Override
    public void onStop() {
        super.onStop();
        if(videoView != null){
            videoView.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(videoView != null){
            videoView.pause();
        }
    }


}
