///TODO: REFERENCIAS http://pastebin.com/1V63aVSg
///http://pastebin.com/rS4xqMej

package cancionesinfantiles.toycantando;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;

import Entidades.Producto;
import Negocio.Constantes;
import Negocio.Recursos;
import Negocio.Session;

public class VideoActivity extends AppCompatActivity{

    private VideoView videoView;
    private RelativeLayout layout_video;
    private Button btnplay;
    private Button btnadelantar;
    private Button btnatrasar;
    private Button btnSalir;
    private LinearLayout controlVideo;
    private SeekBar mediacontroller_progress;
    private Handler mHandler = new Handler();
    private int ActualVideoPosition=0;
    private Producto producto_reproductor;
    public static  int Position;
    Handler handler;
    private boolean flagControl = false;
    private int DurationVideo = 0;
    public static InterstitialAd interstitial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ///Oculta la barra de notificaciones del telefono
        getSupportActionBar().hide();
        layout_video =(RelativeLayout) findViewById(R.id.layout_video);
        controlVideo= (LinearLayout) findViewById(R.id.controlVideo);
        layout_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlVideo.getVisibility() == View.VISIBLE){
                    controlVideo.setVisibility(View.GONE);
                }
                else if(controlVideo.getVisibility() == View.GONE){
                    controlVideo.setVisibility(View.VISIBLE);
                    if(flagControl) {
                        handler.postDelayed(csRunnable, 5000);
                        flagControl= false;
                    }
                }
            }
        });

        ///Anuncio antes del video
        AdRequest adRequest1 = new AdRequest.Builder().build();
        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(VideoActivity.this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
        interstitial.loadAd(adRequest1);
        // Prepare an Interstitial Ad Listener
        interstitial.setAdListener(new AdListener() {
            public void onAdClosed() {
                Intent goReproductor= new Intent(getBaseContext(),Gallery_ACtivity.class);
                startActivity(goReproductor);
        }});

        btnplay = (Button) findViewById(R.id.btnplay);
        btnadelantar = (Button) findViewById(R.id.btnadelantar);
        btnatrasar = (Button) findViewById(R.id.btnatrasar);

        btnSalir=(Button) findViewById(R.id.btnSalir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Intent goInicio= new Intent(getBaseContext(),Gallery_ACtivity.class);
                //startActivity(goInicio);
            }
        });
        controlVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getVisibility() == View.VISIBLE){
                    v.setVisibility(View.GONE);
                }
                else if(v.getVisibility() == View.GONE){
                    v.setVisibility(View.VISIBLE);
                }
            }
        });
        /*
        * Valida que video va a mostrar
        * */
        Producto prd = null;
        try
        {
            prd = Gallery_ACtivity.SearchProducto(Position);
        }
        catch (Exception ex)
        {
            finish();
        }
        if(prd == null)
        {
            finish();
            return;
        }
        producto_reproductor =prd;
        mediacontroller_progress = (SeekBar) findViewById(R.id.mediacontroller_progress);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediacontroller_progress.setProgressTintList(ColorStateList.valueOf(getResources().getColor(R.color.yellow)));
        }
        //else{
        //    mediacontroller_progress.getProgressDrawable().setColorFilter(
        //            Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
        //}
        mediacontroller_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView == null) {
                    return;
                }
                if (videoView.isPlaying()) {
                    btnplay.setBackgroundResource(R.drawable.play);
                    videoView.pause();
                } else {
                    btnplay.setBackgroundResource(R.drawable.pausa);
                    videoView.start();
                }
            }
        });
        btnadelantar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(ActualVideoPosition + 10000);
            }
        });
        btnatrasar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(ActualVideoPosition - 10000);
            }
        });

        try
        {
            videoView = (VideoView) findViewById(R.id.videoView);
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Gallery_ACtivity.CallFromVideo=true;
                    ///Para el video gratis, muestra publicidad, valida que el usuario no halla apagado quitar la publicidad
                    if(producto_reproductor.isFree() && !Session.ShowAds(getBaseContext()))
                        displayInterstitial(getBaseContext());


                    finish();
                    //Intent activity = new Intent(getBaseContext(),Gallery_ACtivity.class);
                    //startActivity(activity);
                }
            });
            //String Path = "http://www.html5videoplayer.net/videos/toystory.mp4";
            //String Path = "https://s3.amazonaws.com/descargastoycantando/A_MI_BURRO.mp4";
            //Uri url = Uri.parse(this.PathVideo);
            if(producto_reproductor.isFree()) {
                File dir = new File(this.getDir("filesdir", Context.MODE_PRIVATE) + producto_reproductor.getNombrePersonaje()+ ".mp4");
                if(dir.exists())
                {
                    videoView.setVideoPath(dir.getAbsolutePath());
                    handler = new Handler();
                    handler.postDelayed(csRunnable, 5000);
                    playVideo();
                }
                else {
                    Toast.makeText(this,"No hay un video para mostrar",Toast.LENGTH_SHORT).show();
                    this.finish();
                }
            }
            else{
                File dir = Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) +Constantes.local);
                if (dir.exists() == false) {
                    dir.mkdirs();
                }
                dir = Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) +Constantes.local +  producto_reproductor.getNombreProducto() + ".mp4");
                ///Si existe en el dispositivo el video lo reproduce
                if(dir.exists()){
                    producto_reproductor.setVideoLocal(Constantes.PathLocal(getBaseContext()) + Constantes.local + producto_reproductor.getNombreProducto() +".mp4");
                    File uri = Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) +Constantes.local + producto_reproductor.getNombreProducto() + ".mp4");

                    videoView.setVideoPath(uri.getAbsolutePath());
                }
                else{
                    videoView.setVideoPath(producto_reproductor.getVideoServer());
                }
            }

            //videoView.start();
            handler = new Handler();
            handler.postDelayed(csRunnable, 5000);
            /*
            * Captura un error en la reproduccion del contenido
            * */
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    if(producto_reproductor.getPosition() == 0)
                    {
                        File dir = new File(getDir("filesdir", Context.MODE_PRIVATE) + "video"+ ".mp4");
                        if(dir.exists())
                        {
                            dir.delete();
                        }
                    }
                    else {
                        File dir = Environment.getExternalStoragePublicDirectory(Constantes.PathLocal(getBaseContext()) + Constantes.local + producto_reproductor.getNombreProducto() + ".mp4");
                        if (dir.exists()) {
                            dir.delete();
                        }
                        Toast.makeText(getBaseContext(), "Tu video no se descargo correctamente,por favor  vuelve a ir al personaje", Toast.LENGTH_LONG).show();
                    }
                    return false;
                }
            });
        }
        catch (Exception ex){

        }
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
        playVideo();
    }

    /*
     * Muestra el anuncio
     * */
    public void displayInterstitial(Context ctx) {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded() && !Session.ShowAds(this)) {
            interstitial.show();
        }
        else{
            Intent activity = new Intent(ctx,Gallery_ACtivity.class);
            startActivity(activity);
        }
    }


    Runnable csRunnable =new Runnable()
    {
        @Override
        public void run()
        {
            flagControl = true;
            if(controlVideo.getVisibility() == View.VISIBLE){
                controlVideo.setVisibility(View.GONE);
            }
        }
    };

    ///Inicia el video y sincroniza la barra de avance del video
    private void playVideo() {
        videoView.start();
        // Updating progress bar
        updateProgressBar();

    }

    private void updateProgressBar() {
        mHandler.postDelayed(updateTimeTask, 100);
    }

    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            if(videoView != null) {
                mediacontroller_progress.setProgress(videoView.getCurrentPosition());
                ActualVideoPosition = videoView.getCurrentPosition();
                mediacontroller_progress.setMax(videoView.getDuration());
                DurationVideo = videoView.getDuration();
                mHandler.postDelayed(this, 100);
            }
        }
    };


    public void onProgressChanged(SeekBar seekbar, int progress,boolean fromTouch) {

    }
    public void onStartTrackingTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
    }
    public void onStopTrackingTouch(SeekBar seekbar) {
        mHandler.removeCallbacks(updateTimeTask);
        videoView.seekTo(mediacontroller_progress.getProgress());
        updateProgressBar();
    }

    /*Cuando la actividad se destruye*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        //this.stopService(new Intent(this, MusicService.class));
        if (videoView != null) {
            if(videoView != null){
                btnplay.setBackgroundResource(R.drawable.play);
                videoView.pause();
                videoView = null;
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(videoView != null){
            btnplay.setBackgroundResource(R.drawable.pausa);
            videoView.start();
        }
    }

    /*Cuando la actividad queda en backgroud y el usuario retorna, ejecuta este codigo*/
    @Override
    public void onResume() {
        super.onResume();
        if(videoView != null){
            //btnplay.setBackgroundResource(R.drawable.play);
           // videoView.pause();
        }
    }

    /*Cuando la actividad queda en backgroud e invisible*/
    @Override
    public void onStop() {
        super.onStop();
        if(videoView != null){
            btnplay.setBackgroundResource(R.drawable.play);
            videoView.pause();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(videoView != null){
            btnplay.setBackgroundResource(R.drawable.play);
            videoView.pause();
        }
    }
}
